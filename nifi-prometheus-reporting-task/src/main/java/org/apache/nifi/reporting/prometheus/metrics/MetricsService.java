package org.apache.nifi.reporting.prometheus.metrics;

import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;

import com.yammer.metrics.core.VirtualMachineMetrics;

import io.prometheus.client.*;

import org.apache.nifi.controller.status.ConnectionStatus;
import org.apache.nifi.controller.status.PortStatus;
import org.apache.nifi.controller.status.ProcessGroupStatus;
import org.apache.nifi.controller.status.ProcessorStatus;
import org.apache.nifi.controller.status.RunStatus;

import static org.apache.nifi.reporting.prometheus.metrics.MetricDefs.*;

/**
 * Associates the actual metrics from NiFi/JVM to the appropriate Prometheus mapping.
 */
public class MetricsService {
    private Map<String, Gauge> gaugeTracker;

    public MetricsService(CollectorRegistry registry) {
        gaugeTracker = new HashMap<String, Gauge>();

        for (final MetricInfo info : PROCESSOR_METRICS) {
            gaugeTracker.put(info.getName(), Gauge.build().name(info.getName()).help(info.getHelp())
                    .labelNames(info.getLabels()).register(registry));
        }
        for (final MetricInfo info : PORT_METRICS) {
            gaugeTracker.put(info.getName(), Gauge.build().name(info.getName()).help(info.getHelp())
                    .labelNames(info.getLabels()).register(registry));
        }
        for (final MetricInfo info : CONNECTION_METRICS) {
            gaugeTracker.put(info.getName(), Gauge.build().name(info.getName()).help(info.getHelp())
                    .labelNames(info.getLabels()).register(registry));
        }
        for (final MetricInfo info : PROCESS_GROUP_METRICS) {
            gaugeTracker.put(info.getName(), Gauge.build().name(info.getName()).help(info.getHelp())
                    .labelNames(info.getLabels()).register(registry));
        }
        for (final MetricInfo info : JVM_METRICS) {
            gaugeTracker.put(info.getName(), Gauge.build().name(info.getName()).help(info.getHelp())
                    .labelNames(info.getLabels()).register(registry));
        }
    }

    /**
     * Given the JVM metrics this records those metrics 
     * 
     * @param vmMetrics is the JVM metrics to record
     */
    public void updateJVMMetrics(VirtualMachineMetrics vmMetrics) {
        String[] labels = { vmMetrics.name(), vmMetrics.version() };

        gaugeTracker.get(JVM_UPTIME.getName()).labels(labels).set(new Double(vmMetrics.uptime()));
        gaugeTracker.get(JVM_DAEMON_THREAD_COUNT.getName()).labels(labels)
                .set(new Double(vmMetrics.daemonThreadCount()));
        gaugeTracker.get(JVM_FILE_DESC_USAGE.getName()).labels(labels).set(vmMetrics.fileDescriptorUsage());
        gaugeTracker.get(JVM_HEAP_USAGE.getName()).labels(labels).set(vmMetrics.heapUsage());
        gaugeTracker.get(JVM_HEAP_USED.getName()).labels(labels).set(vmMetrics.heapUsed());
        gaugeTracker.get(JVM_THREAD_COUNT.getName()).labels(labels).set(vmMetrics.threadCount());
        gaugeTracker.get(JVM_HEAP_COMMITTED.getName()).labels(labels).set(vmMetrics.heapCommitted());
        gaugeTracker.get(JVM_HEAP_INIT.getName()).labels(labels).set(vmMetrics.heapInit());
        gaugeTracker.get(JVM_TOTAL_MEM_COMMITTED.getName()).labels(labels).set(vmMetrics.totalCommitted());
        gaugeTracker.get(JVM_TOTAL_MEM_INIT.getName()).labels(labels).set(vmMetrics.totalInit());
        gaugeTracker.get(JVM_TOTAL_MEM_USED.getName()).labels(labels).set(vmMetrics.totalUsed());

        for (Map.Entry<Thread.State, Double> entry : vmMetrics.threadStatePercentages().entrySet()) {
            String[] threadLabels = { vmMetrics.name(), vmMetrics.version(), entry.getKey().toString() };
            gaugeTracker.get(JVM_THREAD_STATE.getName()).labels(threadLabels).set(entry.getValue());
        }

        double totalGCRuns = 0;
        double totalGCTime = 0;
        for (Map.Entry<String, VirtualMachineMetrics.GarbageCollectorStats> entry : vmMetrics.garbageCollectors()
                .entrySet()) {
            String[] gcLabels = { vmMetrics.name(), vmMetrics.version(), entry.getKey() };
            final double runs = new Double(entry.getValue().getRuns());
            final double timeMS = new Double(entry.getValue().getTime(TimeUnit.MILLISECONDS));
            gaugeTracker.get(JVM_GC_RUNS.getName()).labels(gcLabels).set(runs);
            gaugeTracker.get(JVM_GC_TIME.getName()).labels(gcLabels).set(timeMS);
            totalGCRuns += runs;
            totalGCTime += timeMS;
        }
        gaugeTracker.get(JVM_GC_TOTAL_RUNS.getName()).labels(labels).set(totalGCRuns);
        gaugeTracker.get(JVM_GC_TOTAL_TIME.getName()).labels(labels).set(totalGCTime);
    }

    /**
     * Given the status of an connection this records those metrics 
     * 
     * @param status is the status of a connection you wish to record metrics for
     */
    public void updateConnectionStatusMetrics(ConnectionStatus status) {
        String[] labels = { status.getId(), status.getName(), status.getGroupId(), status.getSourceId(),
                status.getSourceName(), status.getDestinationId(), status.getDestinationName() };

        gaugeTracker.get(CONN_BACK_PRESSURE_BYTES_THRESHOLD.getName()).labels(labels)
                .set(new Double(status.getBackPressureBytesThreshold()));
        gaugeTracker.get(CONN_BACK_PRESSURE_OBJ_THRESHOLD.getName()).labels(labels)
                .set(new Double(status.getBackPressureObjectThreshold()));
        gaugeTracker.get(CONN_INPUT_COUNT.getName()).labels(labels).set(new Double(status.getInputCount()));
        gaugeTracker.get(CONN_INPUT_BYTES.getName()).labels(labels).set(new Double(status.getInputBytes()));
        gaugeTracker.get(CONN_OUTPUT_COUNT.getName()).labels(labels).set(new Double(status.getOutputCount()));
        gaugeTracker.get(CONN_OUTPUT_BYTES.getName()).labels(labels).set(new Double(status.getOutputBytes()));
        gaugeTracker.get(CONN_QUEUED_COUNT.getName()).labels(labels).set(new Double(status.getQueuedCount()));
        gaugeTracker.get(CONN_QUEUED_BYTES.getName()).labels(labels).set(new Double(status.getQueuedBytes()));
    }

    /**
     * Given the status of an process group this records those metrics 
     * 
     * @param status is the status of a process group you wish to record metrics for
     */
    public void updateProcessGroupMetrics(ProcessGroupStatus status) {
        String[] labels = { status.getId(), status.getName() };
        gaugeTracker.get(PROC_GROUP_ACTIVE_THREADS.getName()).labels(labels)
                .set(new Double(status.getActiveThreadCount()));
        gaugeTracker.get(PROC_GROUP_BYTES_READ.getName()).labels(labels).set(new Double(status.getBytesRead()));
        gaugeTracker.get(PROC_GROUP_BYTES_TRANSFERRED.getName()).labels(labels)
                .set(new Double(status.getBytesTransferred()));
        gaugeTracker.get(PROC_GROUP_BYTES_WRITTEN.getName()).labels(labels).set(new Double(status.getBytesWritten()));
        gaugeTracker.get(PROC_GROUP_FLOW_FILES_TRANSFERRED.getName()).labels(labels)
                .set(new Double(status.getFlowFilesTransferred()));
        gaugeTracker.get(PROC_GROUP_INPUT_CONTENT_SIZE.getName()).labels(labels)
                .set(new Double(status.getInputContentSize()));
        gaugeTracker.get(PROC_GROUP_INPUT_COUNT.getName()).labels(labels).set(new Double(status.getInputCount()));
        gaugeTracker.get(PROC_GROUP_OUTPUT_CONTENT_SIZE.getName()).labels(labels)
                .set(new Double(status.getOutputContentSize()));
        gaugeTracker.get(PROC_GROUP_OUTPUT_COUNT.getName()).labels(labels).set(new Double(status.getOutputCount()));
        gaugeTracker.get(PROC_GROUP_QUEUED_CONTENT_SIZE.getName()).labels(labels)
                .set(new Double(status.getQueuedContentSize()));
        gaugeTracker.get(PROC_GROUP_QUEUED_COUNT.getName()).labels(labels).set(new Double(status.getQueuedCount()));
        gaugeTracker.get(PROC_GROUP_PROCESSING_NS.getName()).labels(labels)
                .set(new Double(calculateProcessingNanos(status)));

        Map<RunStatus, Double> statuses = calculateProcessorStatus(status);
        for (Map.Entry<RunStatus, Double> entry : statuses.entrySet()) {
            String[] statusLabels = { status.getId(), status.getName(), entry.getKey().toString() };
            gaugeTracker.get(PROC_GROUP_STATUS_COUNT.getName()).labels(statusLabels).set(entry.getValue());
        }
    }

    /**
     * Given the status of an input/output port this records those metrics 
     * 
     * @param status is the status of a port you wish to record metrics for
     */
    public void updatePortStatusMetrics(PortStatus status) {
        String[] labels = { status.getId(), status.getName(), status.getGroupId() };

        gaugeTracker.get(PORT_ACTIVE_THREADS.getName()).labels(labels).set(new Double(status.getActiveThreadCount()));
        gaugeTracker.get(PORT_INPUT_COUNT.getName()).labels(labels).set(new Double(status.getInputCount()));
        gaugeTracker.get(PORT_INPUT_BYTES.getName()).labels(labels).set(new Double(status.getInputBytes()));
        gaugeTracker.get(PORT_OUTPUT_COUNT.getName()).labels(labels).set(new Double(status.getOutputCount()));
        gaugeTracker.get(PORT_OUTPUT_BYTES.getName()).labels(labels).set(new Double(status.getOutputBytes()));
    }

    /**
     * Given the status of a processor this records those metrics 
     * 
     * @param status is the status of a processor you wish to record metrics for
     */
    public void updateProcessorMetrics(ProcessorStatus status) {
        String[] labels = { status.getId(), status.getName(), status.getGroupId(), status.getType() };

        gaugeTracker.get(PROC_FLOW_FILES_REMOVED.getName()).labels(labels)
                .set(new Double(status.getFlowFilesRemoved()));
        gaugeTracker.get(PROC_ACTIVE_THREADS.getName()).labels(labels).set(new Double(status.getActiveThreadCount()));
        gaugeTracker.get(PROC_BYTES_READ.getName()).labels(labels).set(new Double(status.getBytesRead()));
        gaugeTracker.get(PROC_BYTES_WRITTEN.getName()).labels(labels).set(new Double(status.getBytesWritten()));
        gaugeTracker.get(PROC_PROCESSING_NS.getName()).labels(labels).set(new Double(status.getProcessingNanos()));
        gaugeTracker.get(PROC_INVOCATIONS.getName()).labels(labels).set(new Double(status.getInvocations()));
        gaugeTracker.get(PROC_INPUT_BYTES.getName()).labels(labels).set(new Double(status.getInputBytes()));
        gaugeTracker.get(PROC_OUTPUT_BYTES.getName()).labels(labels).set(new Double(status.getOutputBytes()));
        gaugeTracker.get(PROC_INPUT_COUNT.getName()).labels(labels).set(new Double(status.getInputCount()));
        gaugeTracker.get(PROC_OUTPUT_COUNT.getName()).labels(labels).set(new Double(status.getOutputCount()));
    }

    /**
     * Calculates the total processing time of all processors in a process group and child process groups
     * in nanoseconds.  Taken from 
     * https://github.com/apache/nifi/blob/897b8ab601c2c5912163a8fc3c492cfd41d8e129/nifi-nar-bundles/nifi-datadog-bundle/nifi-datadog-reporting-task/src/main/java/org/apache/nifi/reporting/datadog/metrics/MetricsService.java#L171
     * 
     * @param status the status of the process group you wish to record the processing time of
     * @return       the total processing time for the given process group
     */
    protected long calculateProcessingNanos(final ProcessGroupStatus status) {
        long nanos = 0L;

        for (final ProcessorStatus procStats : status.getProcessorStatus()) {
            nanos += procStats.getProcessingNanos();
        }

        for (final ProcessGroupStatus childGroupStatus : status.getProcessGroupStatus()) {
            nanos += calculateProcessingNanos(childGroupStatus);
        }

        return nanos;
    }

    /**
     * Calculates the count of all processors and/or ports in a process group and child process groups.
     * 
     * @param status the status of the process group you wish to record the status counts of
     * @return       a hashmap where the status is the key and the count is the value
     */
    protected Map<RunStatus, Double> calculateProcessorStatus(final ProcessGroupStatus status) {
        Map<RunStatus, Double> statuses = new HashMap<RunStatus, Double>();
        for (RunStatus runStatus : RunStatus.values()) {
            statuses.put(runStatus, 0d);
        }

        for (final ProcessorStatus procStats : status.getProcessorStatus()) {
            RunStatus runStatus = procStats.getRunStatus();
            statuses.put(runStatus, statuses.get(runStatus) + 1d);
        }

        for (final PortStatus procStats : status.getInputPortStatus()) {
            RunStatus runStatus = procStats.getRunStatus();
            statuses.put(runStatus, statuses.get(runStatus) + 1d);
        }

        for (final PortStatus procStats : status.getOutputPortStatus()) {
            RunStatus runStatus = procStats.getRunStatus();
            statuses.put(runStatus, statuses.get(runStatus) + 1d);
        }

        for (final ProcessGroupStatus childGroupStatus : status.getProcessGroupStatus()) {
            Map<RunStatus, Double> tmp = calculateProcessorStatus(childGroupStatus);
            for (Map.Entry<RunStatus, Double> entry : tmp.entrySet()) {
                statuses.put(entry.getKey(), statuses.get(entry.getKey()) + tmp.get(entry.getKey()));
            }
        }

        return statuses;
    }

}
