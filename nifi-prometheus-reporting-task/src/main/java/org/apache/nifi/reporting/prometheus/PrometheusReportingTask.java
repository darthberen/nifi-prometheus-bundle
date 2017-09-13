package org.apache.nifi.reporting.prometheus;

import io.prometheus.client.*;
//import com.google.common.collect.Lists;
import com.yammer.metrics.core.VirtualMachineMetrics;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
//import org.apache.nifi.components.AllowableValue;
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

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kstruct.gethostname4j.Hostname;


@Tags({"reporting", "prometheus", "metrics"})
@CapabilityDescription("Publishes metrics from NiFi to prometheus. For accurate and informative reporting, components should have unique names.")
public class PrometheusReportingTask extends AbstractReportingTask {
    private Logger logger = LoggerFactory.getLogger(getClass().getName());
    private MetricsService metricsService;
    private PrometheusMetricRegistryBuilder prometheusMetricRegistryBuilder;
    private CollectorRegistry collectorRegistry;
    private String statusId;
    private String jobName;
    private volatile VirtualMachineMetrics virtualMachineMetrics;

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

    @Override
    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        final List<PropertyDescriptor> properties = new ArrayList<>();
        properties.add(PROMETHEUS_GATEWAY);
        properties.add(PROMETHEUS_JOB_NAME);
        return properties;
    }

    @OnScheduled
    public void setup(final ConfigurationContext context) {
        logger.warn("Setting up the collector registry and register all processor metrics");
        collectorRegistry = getCollectorRegistry();
        prometheusMetricRegistryBuilder = getMetricRegistryBuilder(collectorRegistry);
        metricsService = getMetricsService(collectorRegistry);
        virtualMachineMetrics = VirtualMachineMetrics.getInstance(); 
    }

    @Override
    public void onTrigger(ReportingContext context){
        final ProcessGroupStatus status = context.getEventAccess().getControllerStatus();
        statusId = status.getId();
        String gateway = context.getProperty(PROMETHEUS_GATEWAY).getValue();
        jobName = context.getProperty(PROMETHEUS_JOB_NAME).getValue();

        String hostname = Hostname.getHostname();
        Map<String,String> labels = new HashMap<String,String>();
        labels.put("instance", hostname);

        try {
            logger.warn("Process Group: " + statusId + "; Prometheus gateway: " + gateway);
            updateAllMetricGroups(status);
            logger.warn("pushing to registry: " + collectorRegistry.toString());
            prometheusMetricRegistryBuilder.getPrometheusExporter(gateway).push(collectorRegistry, jobName, labels);
        } catch (IOException e) {
            logger.warn("Erroring while pushing metrics to pushgateway: " + e.toString());
        }
    }

    private void updateAllMetricGroups(ProcessGroupStatus processGroupStatus) {
        final List<ProcessorStatus> processorStatuses = new ArrayList<>();
        populateProcessorStatuses(processGroupStatus, processorStatuses);
        for (final ProcessorStatus processorStatus : processorStatuses) {
            //logger.warn("Updating Processor Metrics on Processor: " + processorStatus.getId());
            metricsService.updateProcessorMetrics(processorStatus);
        }

        final List<ConnectionStatus> connectionStatuses = new ArrayList<>();
        populateConnectionStatuses(processGroupStatus, connectionStatuses);
        for (ConnectionStatus connectionStatus: connectionStatuses) {
            //logger.warn("Updating Processor Metrics on Processor: " + connectionStatus.getId());
            metricsService.updateConnectionStatusMetrics(connectionStatus);
        }

        final List<PortStatus> inputPortStatuses = new ArrayList<>();
        populateInputPortStatuses(processGroupStatus, inputPortStatuses);
        for (PortStatus portStatus: inputPortStatuses) {
            //logger.warn("Updating Processor Metrics on Processor: " + portStatus.getId());
            metricsService.updatePortStatusMetrics(portStatus);
        }

        final List<PortStatus> outputPortStatuses = new ArrayList<>();
        populateOutputPortStatuses(processGroupStatus, outputPortStatuses);
        for (PortStatus portStatus: outputPortStatuses) {
            //logger.warn("Updating Processor Metrics on Processor: " + portStatus.getId());
            metricsService.updatePortStatusMetrics(portStatus);
        }

        
        final List<ProcessGroupStatus> processGroupStatuses = new ArrayList<>();
        processGroupStatuses.add(processGroupStatus);
        populateProcessGroupStatuses(processGroupStatus, processGroupStatuses);
        for (ProcessGroupStatus stat: processGroupStatuses) {
            logger.warn("Process group " + stat.getName() + " (" + stat.getId() + ") getting status");
            //logger.warn("Updating Processor Metrics on Processor: " + processGroupStatus.getId());
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

    private void populateProcessorStatuses(final ProcessGroupStatus groupStatus, final List<ProcessorStatus> statuses) {
        statuses.addAll(groupStatus.getProcessorStatus());
        for (final ProcessGroupStatus childGroupStatus : groupStatus.getProcessGroupStatus()) {
            populateProcessorStatuses(childGroupStatus, statuses);
        }
    }

    private void populateConnectionStatuses(final ProcessGroupStatus groupStatus, final List<ConnectionStatus> statuses) {
        statuses.addAll(groupStatus.getConnectionStatus());
        for (final ProcessGroupStatus childGroupStatus : groupStatus.getProcessGroupStatus()) {
            populateConnectionStatuses(childGroupStatus, statuses);
        }
    }

    private void populateInputPortStatuses(final ProcessGroupStatus groupStatus, final List<PortStatus> statuses) {
        statuses.addAll(groupStatus.getInputPortStatus());
        for (final ProcessGroupStatus childGroupStatus : groupStatus.getProcessGroupStatus()) {
            populateInputPortStatuses(childGroupStatus, statuses);
        }
    }

    private void populateOutputPortStatuses(final ProcessGroupStatus groupStatus, final List<PortStatus> statuses) {
        statuses.addAll(groupStatus.getOutputPortStatus());
        for (final ProcessGroupStatus childGroupStatus : groupStatus.getProcessGroupStatus()) {
            populateOutputPortStatuses(childGroupStatus, statuses);
        }
    }

    protected MetricsService getMetricsService(CollectorRegistry registry) {
        return new MetricsService(registry);
    }

    protected PrometheusMetricRegistryBuilder getMetricRegistryBuilder(CollectorRegistry registry) {
        return new PrometheusMetricRegistryBuilder(registry);
    }

    protected CollectorRegistry getCollectorRegistry() {
        return new CollectorRegistry();
    }

}