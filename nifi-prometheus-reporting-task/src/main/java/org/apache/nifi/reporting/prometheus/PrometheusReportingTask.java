package org.apache.nifi.reporting.prometheus;

import com.kstruct.gethostname4j.Hostname;
import com.yammer.metrics.core.VirtualMachineMetrics;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.PushGateway;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.controller.ConfigurationContext;
import org.apache.nifi.controller.status.ConnectionStatus;
import org.apache.nifi.controller.status.PortStatus;
import org.apache.nifi.controller.status.ProcessGroupStatus;
import org.apache.nifi.controller.status.ProcessorStatus;
import org.apache.nifi.processor.util.StandardValidators;
import org.apache.nifi.reporting.AbstractReportingTask;
import org.apache.nifi.reporting.ReportingContext;
import org.apache.nifi.reporting.prometheus.metrics.MetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Prometheus NiFi reporting task.  It will push NiFi and JVM metrics to a Prometheus Push Gateway.
 */
@Tags({ "reporting", "prometheus", "metrics" })
@CapabilityDescription("Publishes metrics from NiFi to a Prometheus Push Gateway.  For accurate and informative reporting, components should have unique names.")
public class PrometheusReportingTask extends AbstractReportingTask {

    private static final Logger logger = LoggerFactory.getLogger(PrometheusReportingTask.class);
    private MetricsService metricsService;
    private CollectorRegistry collectorRegistry;
    private volatile VirtualMachineMetrics virtualMachineMetrics;
    private String gateway, jobName;
    private Map<String, String> sharedLabels;
    private PushGateway pushGateway;

    /***** Define the exported reporting task properties *****/
    static final PropertyDescriptor PROMETHEUS_GATEWAY = new PropertyDescriptor.Builder()
            .name("Prometheus Gateway")
            .description("Prometheus gateway endpoint.  Expected format is HOST[:PORT]")
            .required(true)
            .expressionLanguageSupported(false)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    static final PropertyDescriptor PROMETHEUS_JOB_NAME = new PropertyDescriptor.Builder()
            .name("Prometheus Job Name")
            .description("The desired name for this Prometheus job")
            .required(true)
            .expressionLanguageSupported(false)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    /**
     * Sets up the properties with NiFi that the reporting task requires.
     * 
     * @return list of NiFi properties that should be exposed to the end user
     */
    @Override
    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        final List<PropertyDescriptor> properties = new ArrayList<>();
        properties.add(PROMETHEUS_GATEWAY);
        properties.add(PROMETHEUS_JOB_NAME);
        return properties;
    }

    /**
     * Run a single time when the reporting task is initially started.
     * 
     * @param context the NiFi configuration context for this reporting task
     */
    @OnScheduled
    public void setup(final ConfigurationContext context) {
        logger.debug("setting up the collector registry and registering all processor metrics");
        gateway = context.getProperty(PROMETHEUS_GATEWAY).getValue();
        jobName = context.getProperty(PROMETHEUS_JOB_NAME).getValue();

        collectorRegistry = new CollectorRegistry();
        pushGateway = new PushGateway(gateway);
        metricsService = new MetricsService(getCollectorRegistry());
        virtualMachineMetrics = VirtualMachineMetrics.getInstance();

        sharedLabels = new HashMap<>();
        sharedLabels.put("instance", Hostname.getHostname());
    }

    /**
     * The main entry point for the reporting task and is run each time the reporting task is executed by NiFi.
     * 
     * @param context the NiFi reporting context for this reporting task
     */
    @Override
    public void onTrigger(ReportingContext context) {
        logger.debug("triggering prometheus reporting task");
        final ProcessGroupStatus status = context.getEventAccess().getControllerStatus();

        try {
            populateMetrics(status);
            getPushGateway().push(getCollectorRegistry(), jobName, sharedLabels);
        } catch (IOException e) {
            logger.warn("exception while pushing metrics to prometheus pushgateway: " + e.toString());
        }
    }

    private void populateMetrics(ProcessGroupStatus processGroupStatus) {
        final List<ProcessorStatus> processorStatuses = new ArrayList<>();
        populateProcessorStatuses(processGroupStatus, processorStatuses);
        for (final ProcessorStatus processorStatus : processorStatuses) {
            metricsService.updateProcessorMetrics(processorStatus);
        }

        final List<ConnectionStatus> connectionStatuses = new ArrayList<>();
        populateConnectionStatuses(processGroupStatus, connectionStatuses);
        for (ConnectionStatus connectionStatus : connectionStatuses) {
            metricsService.updateConnectionStatusMetrics(connectionStatus);
        }

        final List<PortStatus> inputPortStatuses = new ArrayList<>();
        populateInputPortStatuses(processGroupStatus, inputPortStatuses);
        for (PortStatus portStatus : inputPortStatuses) {
            metricsService.updatePortStatusMetrics(portStatus);
        }

        final List<PortStatus> outputPortStatuses = new ArrayList<>();
        populateOutputPortStatuses(processGroupStatus, outputPortStatuses);
        for (PortStatus portStatus : outputPortStatuses) {
            metricsService.updatePortStatusMetrics(portStatus);
        }

        final List<ProcessGroupStatus> processGroupStatuses = new ArrayList<>();
        processGroupStatuses.add(processGroupStatus);
        populateProcessGroupStatuses(processGroupStatus, processGroupStatuses);
        for (ProcessGroupStatus stat : processGroupStatuses) {
            metricsService.updateProcessGroupMetrics(stat);
        }

        metricsService.updateJVMMetrics(virtualMachineMetrics);
    }

    private void populateProcessGroupStatuses(final ProcessGroupStatus root, final List<ProcessGroupStatus> statuses) {
        statuses.addAll(root.getProcessGroupStatus());
        for (final ProcessGroupStatus childGroupStatus : root.getProcessGroupStatus()) {
            populateProcessGroupStatuses(childGroupStatus, statuses);
        }
    }

    // Taken from https://github.com/apache/nifi/blob/66479464be67917c308ccc344cfff58da325748c/nifi-nar-bundles/nifi-datadog-bundle/nifi-datadog-reporting-task/src/main/java/org/apache/nifi/reporting/datadog/DataDogReportingTask.java#L236-L241
    private void populateProcessorStatuses(final ProcessGroupStatus groupStatus, final List<ProcessorStatus> statuses) {
        statuses.addAll(groupStatus.getProcessorStatus());
        for (final ProcessGroupStatus childGroupStatus : groupStatus.getProcessGroupStatus()) {
            populateProcessorStatuses(childGroupStatus, statuses);
        }
    }

    // Taken from https://github.com/apache/nifi/blob/66479464be67917c308ccc344cfff58da325748c/nifi-nar-bundles/nifi-datadog-bundle/nifi-datadog-reporting-task/src/main/java/org/apache/nifi/reporting/datadog/DataDogReportingTask.java#L243-L248
    private void populateConnectionStatuses(final ProcessGroupStatus groupStatus, final List<ConnectionStatus> statuses) {
        statuses.addAll(groupStatus.getConnectionStatus());
        for (final ProcessGroupStatus childGroupStatus : groupStatus.getProcessGroupStatus()) {
            populateConnectionStatuses(childGroupStatus, statuses);
        }
    }

    // Taken from https://github.com/apache/nifi/blob/66479464be67917c308ccc344cfff58da325748c/nifi-nar-bundles/nifi-datadog-bundle/nifi-datadog-reporting-task/src/main/java/org/apache/nifi/reporting/datadog/DataDogReportingTask.java#L250-L255
    private void populateInputPortStatuses(final ProcessGroupStatus groupStatus, final List<PortStatus> statuses) {
        statuses.addAll(groupStatus.getInputPortStatus());
        for (final ProcessGroupStatus childGroupStatus : groupStatus.getProcessGroupStatus()) {
            populateInputPortStatuses(childGroupStatus, statuses);
        }
    }

    // Taken from https://github.com/apache/nifi/blob/66479464be67917c308ccc344cfff58da325748c/nifi-nar-bundles/nifi-datadog-bundle/nifi-datadog-reporting-task/src/main/java/org/apache/nifi/reporting/datadog/DataDogReportingTask.java#L257-L262
    private void populateOutputPortStatuses(final ProcessGroupStatus groupStatus, final List<PortStatus> statuses) {
        statuses.addAll(groupStatus.getOutputPortStatus());
        for (final ProcessGroupStatus childGroupStatus : groupStatus.getProcessGroupStatus()) {
            populateOutputPortStatuses(childGroupStatus, statuses);
        }
    }

    public CollectorRegistry getCollectorRegistry() {
        return collectorRegistry;
    }

    public PushGateway getPushGateway() {
        return pushGateway;
    }
}