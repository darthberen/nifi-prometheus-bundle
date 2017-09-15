package org.apache.nifi.reporting.prometheus.metrics;

import com.yammer.metrics.core.VirtualMachineMetrics;
import io.prometheus.client.*;
import org.apache.nifi.controller.status.ConnectionStatus;
import org.apache.nifi.controller.status.PortStatus;
import org.apache.nifi.controller.status.ProcessGroupStatus;
import org.apache.nifi.controller.status.ProcessorStatus;
import org.apache.nifi.controller.status.RunStatus;

import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;

import static org.apache.nifi.reporting.prometheus.metrics.MetricNames.*;

/**
 * A service used to produce key/value metrics based on a given input.
 */
public class MetricsService {
    //private Logger logger = LoggerFactory.getLogger(getClass().getName());

    private Gauge procFlowFilesReceived, procFlowFilesRemoved, procFlowFilesSent, procActiveThreads, procBytesRead, procBytesWritten, procProcessingTime, procInvocations;
    private Gauge procInputBytes, procOutputBytes, procInputCount, procOutputCount;
    private Gauge portActiveThreads, portInputCount, portInputBytes, portOutputCount, portOutputBytes;
    private Gauge connBackPressureBytes, connBackPressureObj, connInputCount, connInputBytes, connOutputCount, connOutputBytes, connQueuedCount, connQueuedBytes;
    private Gauge procGroupActiveThreads, procGroupBytesRead, procGroupBytesReceived, procGroupBytesSent, procGroupBytesTransferred, procGroupBytesWritten, procGroupFlowFilesReceived, procGroupFlowFilesSent, procGroupStatusCount;
    private Gauge procGroupFlowFilesTransferred, procGroupInputContentSize, procGroupInputCount, procGroupOutputContentSize, procGroupOutputCount, procGroupQueuedContentSize, procGroupQueuedCount, procGroupProcessingTime;
    private Gauge jvmUptime, jvmDaemonThreadCount, jvmFileDescUsage, jvmHeapUsage, jvmHeapUsed, jvmNonHeapUsage, jvmThreadCount;
    private Gauge jvmHeapCommitted, jvmHeapInit, jvmHeapMax, jvmTotalMemCommitted, jvmTotalMemInit, jvmTotalMemMax, jvmTotalMemUsed;
    private Gauge jvmBlockedThreads, jvmTerminatedThreads, jvmRunnableThreads, jvmTimedWaitingThreads;
    private Gauge jvmGCTotalRuns, jvmGCTotalTime, jvmGCRuns, jvmGCTime;

    public MetricsService(CollectorRegistry registry){
        this.procFlowFilesReceived = Gauge.build().name(PROC_FLOW_FILES_RECEIVED.getName()).help(PROC_FLOW_FILES_RECEIVED.getHelp()).labelNames(PROCESSOR_LABELS).register(registry);
        this.procFlowFilesRemoved = Gauge.build().name(PROC_FLOW_FILES_REMOVED.getName()).help(PROC_FLOW_FILES_REMOVED.getHelp()).labelNames(PROCESSOR_LABELS).register(registry);
        this.procFlowFilesSent = Gauge.build().name(PROC_FLOW_FILES_SENT.getName()).help(PROC_FLOW_FILES_SENT.getHelp()).labelNames(PROCESSOR_LABELS).register(registry);
        this.procActiveThreads = Gauge.build().name(PROC_ACTIVE_THREADS.getName()).help(PROC_ACTIVE_THREADS.getHelp()).labelNames(PROCESSOR_LABELS).register(registry);
        this.procBytesRead = Gauge.build().name(PROC_BYTES_READ.getName()).help(PROC_BYTES_READ.getHelp()).labelNames(PROCESSOR_LABELS).register(registry);
        this.procBytesWritten = Gauge.build().name(PROC_BYTES_WRITTEN.getName()).help(PROC_BYTES_WRITTEN.getHelp()).labelNames(PROCESSOR_LABELS).register(registry);
        this.procProcessingTime = Gauge.build().name(PROC_PROCESSING_NS.getName()).help(PROC_PROCESSING_NS.getHelp()).labelNames(PROCESSOR_LABELS).register(registry);
        this.procInvocations = Gauge.build().name(PROC_INVOCATIONS.getName()).help(PROC_INVOCATIONS.getHelp()).labelNames(PROCESSOR_LABELS).register(registry);
        this.procInputBytes = Gauge.build().name(PROC_INPUT_BYTES.getName()).help(PROC_INPUT_BYTES.getHelp()).labelNames(PROCESSOR_LABELS).register(registry);
        this.procOutputBytes = Gauge.build().name(PROC_OUTPUT_BYTES.getName()).help(PROC_OUTPUT_BYTES.getHelp()).labelNames(PROCESSOR_LABELS).register(registry);
        this.procInputCount = Gauge.build().name(PROC_INPUT_COUNT.getName()).help(PROC_INPUT_COUNT.getHelp()).labelNames(PROCESSOR_LABELS).register(registry);
        this.procOutputCount = Gauge.build().name(PROC_OUTPUT_COUNT.getName()).help(PROC_OUTPUT_COUNT.getHelp()).labelNames(PROCESSOR_LABELS).register(registry);

        this.portActiveThreads = Gauge.build().name(PORT_ACTIVE_THREADS.getName()).help(PORT_ACTIVE_THREADS.getHelp()).labelNames(PORT_LABELS).register(registry);
        this.portInputCount = Gauge.build().name(PORT_INPUT_COUNT.getName()).help(PORT_INPUT_COUNT.getHelp()).labelNames(PORT_LABELS).register(registry);
        this.portInputBytes = Gauge.build().name(PORT_INPUT_BYTES.getName()).help(PORT_INPUT_BYTES.getHelp()).labelNames(PORT_LABELS).register(registry);
        this.portOutputCount = Gauge.build().name(PORT_OUTPUT_COUNT.getName()).help(PORT_OUTPUT_COUNT.getHelp()).labelNames(PORT_LABELS).register(registry);
        this.portOutputBytes = Gauge.build().name(PORT_OUTPUT_BYTES.getName()).help(PORT_OUTPUT_BYTES.getHelp()).labelNames(PORT_LABELS).register(registry);

        this.connBackPressureBytes = Gauge.build().name(CONN_BACK_PRESSURE_BYTES_THRESHOLD.getName()).help(CONN_BACK_PRESSURE_BYTES_THRESHOLD.getHelp()).labelNames(CONNECTION_LABELS).register(registry);
        this.connBackPressureObj = Gauge.build().name(CONN_BACK_PRESSURE_OBJ_THRESHOLD.getName()).help(CONN_BACK_PRESSURE_OBJ_THRESHOLD.getHelp()).labelNames(CONNECTION_LABELS).register(registry);
        this.connInputCount = Gauge.build().name(CONN_INPUT_COUNT.getName()).help(CONN_INPUT_COUNT.getHelp()).labelNames(CONNECTION_LABELS).register(registry);
        this.connInputBytes = Gauge.build().name(CONN_INPUT_BYTES.getName()).help(CONN_INPUT_BYTES.getHelp()).labelNames(CONNECTION_LABELS).register(registry);
        this.connOutputCount = Gauge.build().name(CONN_OUTPUT_COUNT.getName()).help(CONN_OUTPUT_COUNT.getHelp()).labelNames(CONNECTION_LABELS).register(registry);
        this.connOutputBytes = Gauge.build().name(CONN_OUTPUT_BYTES.getName()).help(CONN_OUTPUT_BYTES.getHelp()).labelNames(CONNECTION_LABELS).register(registry);
        this.connQueuedCount = Gauge.build().name(CONN_QUEUED_COUNT.getName()).help(CONN_QUEUED_COUNT.getHelp()).labelNames(CONNECTION_LABELS).register(registry);
        this.connQueuedBytes = Gauge.build().name(CONN_QUEUED_BYTES.getName()).help(CONN_QUEUED_BYTES.getHelp()).labelNames(CONNECTION_LABELS).register(registry);
        
        this.procGroupActiveThreads = Gauge.build().name(PROC_GROUP_ACTIVE_THREADS.getName()).help(PROC_GROUP_ACTIVE_THREADS.getHelp()).labelNames(PROC_GROUP_LABELS).register(registry);
        this.procGroupBytesRead = Gauge.build().name(PROC_GROUP_BYTES_READ.getName()).help(PROC_GROUP_BYTES_READ.getHelp()).labelNames(PROC_GROUP_LABELS).register(registry);
        this.procGroupBytesReceived = Gauge.build().name(PROC_GROUP_BYTES_RECEIVED.getName()).help(PROC_GROUP_BYTES_RECEIVED.getHelp()).labelNames(PROC_GROUP_LABELS).register(registry);
        this.procGroupBytesSent = Gauge.build().name(PROC_GROUP_BYTES_SENT.getName()).help(PROC_GROUP_BYTES_SENT.getHelp()).labelNames(PROC_GROUP_LABELS).register(registry);
        this.procGroupBytesTransferred = Gauge.build().name(PROC_GROUP_BYTES_TRANSFERRED.getName()).help(PROC_GROUP_BYTES_TRANSFERRED.getHelp()).labelNames(PROC_GROUP_LABELS).register(registry);
        this.procGroupBytesWritten = Gauge.build().name(PROC_GROUP_BYTES_WRITTEN.getName()).help(PROC_GROUP_BYTES_WRITTEN.getHelp()).labelNames(PROC_GROUP_LABELS).register(registry);
        this.procGroupFlowFilesReceived = Gauge.build().name(PROC_GROUP_FLOW_FILES_RECEIVED.getName()).help(PROC_GROUP_FLOW_FILES_RECEIVED.getHelp()).labelNames(PROC_GROUP_LABELS).register(registry);
        this.procGroupFlowFilesSent = Gauge.build().name(PROC_GROUP_FLOW_FILES_SENT.getName()).help(PROC_GROUP_FLOW_FILES_SENT.getHelp()).labelNames(PROC_GROUP_LABELS).register(registry);
        this.procGroupFlowFilesTransferred = Gauge.build().name(PROC_GROUP_FLOW_FILES_TRANSFERRED.getName()).help(PROC_GROUP_FLOW_FILES_TRANSFERRED.getHelp()).labelNames(PROC_GROUP_LABELS).register(registry);
        this.procGroupInputContentSize = Gauge.build().name(PROC_GROUP_INPUT_CONTENT_SIZE.getName()).help(PROC_GROUP_INPUT_CONTENT_SIZE.getHelp()).labelNames(PROC_GROUP_LABELS).register(registry);
        this.procGroupInputCount = Gauge.build().name(PROC_GROUP_INPUT_COUNT.getName()).help(PROC_GROUP_INPUT_COUNT.getHelp()).labelNames(PROC_GROUP_LABELS).register(registry);
        this.procGroupOutputContentSize = Gauge.build().name(PROC_GROUP_OUTPUT_CONTENT_SIZE.getName()).help(PROC_GROUP_OUTPUT_CONTENT_SIZE.getHelp()).labelNames(PROC_GROUP_LABELS).register(registry);
        this.procGroupOutputCount = Gauge.build().name(PROC_GROUP_OUTPUT_COUNT.getName()).help(PROC_GROUP_OUTPUT_COUNT.getHelp()).labelNames(PROC_GROUP_LABELS).register(registry);
        this.procGroupQueuedContentSize = Gauge.build().name(PROC_GROUP_QUEUED_CONTENT_SIZE.getName()).help(PROC_GROUP_QUEUED_CONTENT_SIZE.getHelp()).labelNames(PROC_GROUP_LABELS).register(registry);
        this.procGroupQueuedCount = Gauge.build().name(PROC_GROUP_QUEUED_COUNT.getName()).help(PROC_GROUP_QUEUED_COUNT.getHelp()).labelNames(PROC_GROUP_LABELS).register(registry);
        this.procGroupProcessingTime = Gauge.build().name(PROC_GROUP_PROCESSING_NS.getName()).help(PROC_GROUP_PROCESSING_NS.getHelp()).labelNames(PROC_GROUP_LABELS).register(registry);

        this.procGroupStatusCount = Gauge.build().name(PROC_GROUP_STATUS_COUNT.getName()).help(PROC_GROUP_STATUS_COUNT.getHelp()).labelNames(PROC_GROUP_STATUS_LABELS).register(registry);
        
        this.jvmUptime = Gauge.build().name(JVM_UPTIME.getName()).help(JVM_UPTIME.getHelp()).labelNames(JVM_LABELS).register(registry);
        this.jvmDaemonThreadCount = Gauge.build().name(JVM_DAEMON_THREAD_COUNT.getName()).help(JVM_DAEMON_THREAD_COUNT.getHelp()).labelNames(JVM_LABELS).register(registry);
        this.jvmFileDescUsage = Gauge.build().name(JVM_FILE_DESC_USAGE.getName()).help(JVM_FILE_DESC_USAGE.getHelp()).labelNames(JVM_LABELS).register(registry);
        this.jvmHeapUsage = Gauge.build().name(JVM_HEAP_USAGE.getName()).help(JVM_HEAP_USAGE.getHelp()).labelNames(JVM_LABELS).register(registry);
        this.jvmHeapUsed = Gauge.build().name(JVM_HEAP_USED.getName()).help(JVM_HEAP_USED.getHelp()).labelNames(JVM_LABELS).register(registry);
        this.jvmNonHeapUsage = Gauge.build().name(JVM_NON_HEAP_USAGE.getName()).help(JVM_NON_HEAP_USAGE.getHelp()).labelNames(JVM_LABELS).register(registry);
        this.jvmThreadCount = Gauge.build().name(JVM_THREAD_COUNT.getName()).help(JVM_THREAD_COUNT.getHelp()).labelNames(JVM_LABELS).register(registry);
        this.jvmHeapCommitted = Gauge.build().name(JVM_HEAP_COMMITTED.getName()).help(JVM_HEAP_COMMITTED.getHelp()).labelNames(JVM_LABELS).register(registry);
        this.jvmHeapInit = Gauge.build().name(JVM_HEAP_INIT.getName()).help(JVM_HEAP_INIT.getHelp()).labelNames(JVM_LABELS).register(registry);
        this.jvmHeapMax = Gauge.build().name(JVM_HEAP_MAX.getName()).help(JVM_HEAP_MAX.getHelp()).labelNames(JVM_LABELS).register(registry);
        this.jvmTotalMemCommitted = Gauge.build().name(JVM_TOTAL_MEM_COMMITTED.getName()).help(JVM_TOTAL_MEM_COMMITTED.getHelp()).labelNames(JVM_LABELS).register(registry);
        this.jvmTotalMemInit = Gauge.build().name(JVM_TOTAL_MEM_INIT.getName()).help(JVM_TOTAL_MEM_INIT.getHelp()).labelNames(JVM_LABELS).register(registry);
        this.jvmTotalMemMax = Gauge.build().name(JVM_TOTAL_MEM_MAX.getName()).help(JVM_TOTAL_MEM_MAX.getHelp()).labelNames(JVM_LABELS).register(registry);
        this.jvmTotalMemUsed = Gauge.build().name(JVM_TOTAL_MEM_USED.getName()).help(JVM_TOTAL_MEM_USED.getHelp()).labelNames(JVM_LABELS).register(registry);
        this.jvmBlockedThreads = Gauge.build().name(JVM_BLOCKED_THREADS.getName()).help(JVM_BLOCKED_THREADS.getHelp()).labelNames(JVM_LABELS).register(registry);
        this.jvmTerminatedThreads = Gauge.build().name(JVM_TERMINATED_THREADS.getName()).help(JVM_TERMINATED_THREADS.getHelp()).labelNames(JVM_LABELS).register(registry);
        this.jvmRunnableThreads = Gauge.build().name(JVM_RUNNABLE_THREADS.getName()).help(JVM_RUNNABLE_THREADS.getHelp()).labelNames(JVM_LABELS).register(registry);
        this.jvmTimedWaitingThreads = Gauge.build().name(JVM_TIMED_WAITING_THREADS.getName()).help(JVM_TIMED_WAITING_THREADS.getHelp()).labelNames(JVM_LABELS).register(registry);
        this.jvmGCTotalRuns = Gauge.build().name(JVM_GC_TOTAL_RUNS.getName()).help(JVM_GC_TOTAL_RUNS.getHelp()).labelNames(JVM_LABELS).register(registry);
        this.jvmGCTotalTime = Gauge.build().name(JVM_GC_TOTAL_TIME.getName()).help(JVM_GC_TOTAL_TIME.getHelp()).labelNames(JVM_LABELS).register(registry);
    
        this.jvmGCRuns = Gauge.build().name(JVM_GC_RUNS.getName()).help(JVM_GC_RUNS.getHelp()).labelNames(JVM_GC_LABELS).register(registry);
        this.jvmGCTime = Gauge.build().name(JVM_GC_TIME.getName()).help(JVM_GC_TIME.getHelp()).labelNames(JVM_GC_LABELS).register(registry);
    }

    /**
     * Given the JVM metrics this records those metrics 
     * 
     * @param vmMetrics is the JVM metrics to record
     */
    public void updateJVMMetrics(VirtualMachineMetrics vmMetrics) {
        String[] labels = {
            vmMetrics.name(),
            vmMetrics.version()
        };

        jvmUptime.labels(labels).set(new Double(vmMetrics.uptime()));
        jvmDaemonThreadCount.labels(labels).set(new Double(vmMetrics.daemonThreadCount()));
        jvmFileDescUsage.labels(labels).set(vmMetrics.fileDescriptorUsage());
        jvmHeapUsage.labels(labels).set(vmMetrics.heapUsage());
        jvmHeapUsed.labels(labels).set(vmMetrics.heapUsed());
        jvmNonHeapUsage.labels(labels).set(vmMetrics.nonHeapUsage());
        jvmThreadCount.labels(labels).set(new Double(vmMetrics.threadCount()));
        jvmHeapCommitted.labels(labels).set(vmMetrics.heapCommitted());
        jvmHeapInit.labels(labels).set(vmMetrics.heapInit());
        jvmHeapMax.labels(labels).set(vmMetrics.heapMax());
        jvmTotalMemCommitted.labels(labels).set(vmMetrics.totalCommitted());
        jvmTotalMemInit.labels(labels).set(vmMetrics.totalInit());
        jvmTotalMemMax.labels(labels).set(vmMetrics.totalMax());
        jvmTotalMemUsed.labels(labels).set(vmMetrics.totalUsed());
        
        double blockedThreads = 0d;
        double runnableThreads = 0d;
        double terminatedThreads = 0d;
        double waitingThreads = 0d;
        for (Map.Entry<Thread.State, Double> entry : vmMetrics.threadStatePercentages().entrySet()) {
            switch (entry.getKey()) {
            case BLOCKED:
                blockedThreads++;
                break;
            case RUNNABLE:
                runnableThreads++;
                break;
            case TERMINATED:
                terminatedThreads++;
                break;
            case TIMED_WAITING:
                waitingThreads++;
                break;
            default:
                break;
            }
        }
        jvmBlockedThreads.labels(labels).set(blockedThreads);
        jvmTerminatedThreads.labels(labels).set(terminatedThreads);
        jvmRunnableThreads.labels(labels).set(runnableThreads);
        jvmTimedWaitingThreads.labels(labels).set(waitingThreads);
        
        double totalGCRuns = 0;
        double totalGCTime = 0;
        for (Map.Entry<String, VirtualMachineMetrics.GarbageCollectorStats> entry : vmMetrics.garbageCollectors().entrySet()) {
            String[] gcLabels = {
                vmMetrics.name(),
                vmMetrics.version(),
                entry.getKey()
            };
            final double runs = new Double(entry.getValue().getRuns());
            final double timeMS = new Double(entry.getValue().getTime(TimeUnit.MILLISECONDS));
            jvmGCRuns.labels(gcLabels).set(runs);
            jvmGCTime.labels(gcLabels).set(timeMS);
            totalGCRuns += runs;
            totalGCTime += timeMS;
        }
        jvmGCTotalRuns.labels(labels).set(totalGCRuns);
        jvmGCTotalTime.labels(labels).set(totalGCTime);
    }

    /**
     * Given the status of an connection this records those metrics 
     * 
     * @param status is the status of a connection you wish to record metrics for
     */
    public void updateConnectionStatusMetrics(ConnectionStatus status) {
        String[] labels = {
            status.getId(),
            status.getName(),
            status.getGroupId(),
            status.getSourceId(),
            status.getSourceName(),
            status.getDestinationId(),
            status.getDestinationName()
        };

        connBackPressureBytes.labels(labels).set(new Double(status.getBackPressureBytesThreshold()));
        connBackPressureObj.labels(labels).set(new Double(status.getBackPressureObjectThreshold()));
        connInputCount.labels(labels).set(new Double(status.getInputCount()));
        connInputBytes.labels(labels).set(new Double(status.getInputBytes()));
        connOutputCount.labels(labels).set(new Double(status.getOutputCount()));
        connOutputBytes.labels(labels).set(new Double(status.getOutputBytes()));
        connQueuedCount.labels(labels).set(new Double(status.getQueuedCount()));
        connQueuedBytes.labels(labels).set(new Double(status.getQueuedBytes()));
    }

    /**
     * Given the status of an process group this records those metrics 
     * 
     * @param status is the status of a process group you wish to record metrics for
     */
    public void updateProcessGroupMetrics(ProcessGroupStatus status) {
        String[] labels = {
            status.getId(),
            status.getName()
        };

        procGroupActiveThreads.labels(labels).set(new Double(status.getActiveThreadCount()));
        procGroupBytesRead.labels(labels).set(new Double(status.getBytesRead()));
        procGroupBytesReceived.labels(labels).set(new Double(status.getBytesReceived()));
        procGroupBytesSent.labels(labels).set(new Double(status.getBytesSent()));
        procGroupBytesTransferred.labels(labels).set(new Double(status.getBytesTransferred()));
        procGroupBytesWritten.labels(labels).set(new Double(status.getBytesWritten()));
        procGroupFlowFilesReceived.labels(labels).set(new Double(status.getFlowFilesReceived()));
        procGroupFlowFilesSent.labels(labels).set(new Double(status.getFlowFilesSent()));
        procGroupFlowFilesTransferred.labels(labels).set(new Double(status.getFlowFilesTransferred()));
        procGroupInputContentSize.labels(labels).set(new Double(status.getInputContentSize()));
        procGroupInputCount.labels(labels).set(new Double(status.getInputCount()));
        procGroupOutputContentSize.labels(labels).set(new Double(status.getOutputContentSize()));
        procGroupOutputCount.labels(labels).set(new Double(status.getOutputCount()));
        procGroupQueuedContentSize.labels(labels).set(new Double(status.getQueuedContentSize()));
        procGroupQueuedCount.labels(labels).set(new Double(status.getQueuedCount()));
        procGroupProcessingTime.labels(labels).set(new Double(calculateProcessingNanos(status)));

        Map<RunStatus,Double> statuses = calculateProcessorStatus(status);
        for (Map.Entry<RunStatus,Double> entry : statuses.entrySet()) {
            String[] statusLabels = {
                status.getId(),
                status.getName(),
                entry.getKey().toString()
            };
            procGroupStatusCount.labels(statusLabels).set(entry.getValue());
        }
    }

    /**
     * Given the status of an input/output port this records those metrics 
     * 
     * @param status is the status of a port you wish to record metrics for
     */
    public void updatePortStatusMetrics(PortStatus status){
        String[] labels = {
            status.getId(),
            status.getName(),
            status.getGroupId()
        };

        portActiveThreads.labels(labels).set(new Double(status.getActiveThreadCount()));
        portInputCount.labels(labels).set(new Double(status.getInputCount()));
        portInputBytes.labels(labels).set(new Double(status.getInputBytes()));
        portOutputCount.labels(labels).set(new Double(status.getOutputCount()));
        portOutputBytes.labels(labels).set(new Double(status.getOutputBytes()));
    }

    /**
     * Given the status of a processor this records those metrics 
     * 
     * @param status is the status of a processor you wish to record metrics for
     */
    public void updateProcessorMetrics(ProcessorStatus status) {
        String[] labels = {
            status.getId(),
            status.getName(),
            status.getGroupId(),
            status.getType()
        };

        procFlowFilesReceived.labels(labels).set(new Double(status.getFlowFilesReceived()));
        procFlowFilesRemoved.labels(labels).set(new Double(status.getFlowFilesRemoved()));
        procFlowFilesSent.labels(labels).set(new Double(status.getFlowFilesSent()));
        procActiveThreads.labels(labels).set(new Double(status.getActiveThreadCount()));
        procInputCount.labels(labels).set(new Double(status.getInputCount()));
        procOutputCount.labels(labels).set(new Double(status.getOutputCount()));
        procBytesRead.labels(labels).set(new Double(status.getBytesRead()));
        procBytesWritten.labels(labels).set(new Double(status.getBytesWritten()));
        procInputBytes.labels(labels).set(new Double(status.getInputBytes()));
        procOutputBytes.labels(labels).set(new Double(status.getOutputBytes()));
        procProcessingTime.labels(labels).set(new Double(status.getProcessingNanos()));
        procInvocations.labels(labels).set(new Double(status.getInvocations()));
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
    protected Map<RunStatus,Double> calculateProcessorStatus(final ProcessGroupStatus status) {
        Map<RunStatus,Double> statuses = new HashMap<RunStatus,Double>();
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
            Map<RunStatus,Double> tmp = calculateProcessorStatus(childGroupStatus);
            for (Map.Entry<RunStatus,Double> entry : tmp.entrySet()) {
                statuses.put(entry.getKey(), statuses.get(entry.getKey()) + tmp.get(entry.getKey()));
            }
        }

        return statuses;
    }

}
