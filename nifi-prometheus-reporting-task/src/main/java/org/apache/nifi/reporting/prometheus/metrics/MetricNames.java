package org.apache.nifi.reporting.prometheus.metrics;

/**
 * The Metric names to send to Prometheus.
 */
public final class MetricNames {
    /***** Processor Metrics *****/
    // Note: bytes read/written/received/sent and flowfiles sent/received are not used by Nifi for processors
    protected static final String[] PROCESSOR_LABELS = {"id", "name", "group_id", "type"};
    
    protected static final MetricInfo PROC_FLOW_FILES_RECEIVED = new MetricInfo(
        "processor_flowfiles_received",
        "The number of flowfiles received by a processor over the past 5 minutes"
    );
    protected static final MetricInfo PROC_FLOW_FILES_REMOVED = new MetricInfo(
        "processor_flowfiles_removed",
        "The number of flowfiles removed by a processor over the past 5 minutes"
    );
    protected static final MetricInfo PROC_FLOW_FILES_SENT = new MetricInfo(
        "processor_flowfiles_sent",
        "The number of active threads for a processor"
    );
    protected static final MetricInfo PROC_ACTIVE_THREADS = new MetricInfo(
        "processor_active_threads",
        "The number of active threads for a processor"
    );
    protected static final MetricInfo PROC_BYTES_READ = new MetricInfo( // DONE
        "processor_bytes_read",
        "The number of bytes read from the Content Repository by this Processor in the past 5 minutes"
    );
    protected static final MetricInfo PROC_BYTES_WRITTEN = new MetricInfo( // DONE
        "processor_bytes_written",
        "The number of bytes written to the Content Repository by this Processor in the past 5 minutes"
    );
    protected static final MetricInfo PROC_PROCESSING_NS = new MetricInfo(
        "processor_processing_time_ns",
        "The processing time a processor has used over the past 5 minutes in wall clock time (nanaseconds)"
    );
    protected static final MetricInfo PROC_INVOCATIONS = new MetricInfo(
        "processor_invocations",
        "The number of times that a processor has been invoked over the past 5 minutes"
    );
    protected static final MetricInfo PROC_INPUT_BYTES = new MetricInfo( // DONE
        "processor_input_bytes",
        "The cumulative size in bytes of all FlowFiles that this Processor has pulled from its queues in the past 5 minutes"
    );
    protected static final MetricInfo PROC_OUTPUT_BYTES = new MetricInfo( // DONE
        "processor_output_bytes",
        "The cumulative size in bytes of all FlowFiles that this Processor has transferred to downstream queues in the past 5 minutes"
    );
    protected static final MetricInfo PROC_INPUT_COUNT = new MetricInfo( // DONE
        "processor_input_count",
        "The number of FlowFiles that this Processor has pulled from its queues in the past 5 minutes"
    );
    protected static final MetricInfo PROC_OUTPUT_COUNT = new MetricInfo( // DONE
        "processor_output_cout",
        "The number of FlowFiles that this Processor has transferred to downstream queues in the past 5 minutes"
    );

    /***** Port Metrics *****/
    // Note: flow files received/sent and bytes received/sent are not used by Nifi for ports
    protected static final String[] PORT_LABELS = {"id", "name", "group_id"};

    protected static final MetricInfo PORT_ACTIVE_THREADS = new MetricInfo(
        "port_active_threads",
        "The number of active threads for a port"
    );
    protected static final MetricInfo PORT_INPUT_COUNT = new MetricInfo(
        "port_input_count",
        "The number of flowfiles received by a port over the past 5 minutes"
    );
    protected static final MetricInfo PORT_INPUT_BYTES = new MetricInfo(
        "port_input_bytes",
        "The number of received bytes into a port over the past 5 minutes"
    );
    protected static final MetricInfo PORT_OUTPUT_COUNT = new MetricInfo(
        "port_output_count",
        "The number of flowfiles sent by a port over the past 5 minutes"
    );
    protected static final MetricInfo PORT_OUTPUT_BYTES = new MetricInfo(
        "port_output_bytes",
        "The number of bytes sent out from a port over the past 5 minutes"
    );

    /***** Connection Metrics *****/
    // Note: max queued count/bytes are not used by Nifi for connections
    protected static final String[] CONNECTION_LABELS = {"id", "name", "group_id", "source_id", "source_name", "destination_id", "destination_name"};
    
    protected static final MetricInfo CONN_BACK_PRESSURE_BYTES_THRESHOLD = new MetricInfo(
        "conn_back_pressure_bytes_threshold",
        "The back pressure bytes threshold for a connection"
    );
    protected static final MetricInfo CONN_BACK_PRESSURE_OBJ_THRESHOLD = new MetricInfo(
        "conn_back_pressure_obj_threshold",
        "The back pressure flowfile threshold for a connection"
    );
    protected static final MetricInfo CONN_INPUT_COUNT = new MetricInfo(
        "conn_input_count",
        "The number of flowfiles received by a connection over the past 5 minutes"
    );
    protected static final MetricInfo CONN_INPUT_BYTES = new MetricInfo(
        "conn_input_bytes",
        "The number of bytes into a connection over the past 5 minutes"
    );
    protected static final MetricInfo CONN_OUTPUT_COUNT = new MetricInfo(
        "conn_output_count",
        "The number of flowfiles sent by a connection over the past 5 minutes"
    );
    protected static final MetricInfo CONN_OUTPUT_BYTES = new MetricInfo(
        "conn_output_bytes",
        "The number of bytes out from a connection over the past 5 minutes"
    );
    protected static final MetricInfo CONN_QUEUED_COUNT = new MetricInfo(
        "conn_queued_count",
        "The number of flowfiles queued by a connection over the past 5 minutes"
    );
    protected static final MetricInfo CONN_QUEUED_BYTES = new MetricInfo(
        "conn_queued_bytes",
        "The number of bytes queued by a connection over the past 5 minutes"
    );

    /***** Process Group Metrics *****/
    // Note: XXX are not used by Nifi for process groups
    protected static final String[] PROC_GROUP_LABELS = {"id", "name"};
    protected static final String[] PROC_GROUP_STATUS_LABELS = {"id", "name", "status"};
    
    protected static final MetricInfo PROC_GROUP_ACTIVE_THREADS = new MetricInfo(
        "proc_group_active_threads",
        "The number of active threads in a process group"
    );
    protected static final MetricInfo PROC_GROUP_BYTES_READ = new MetricInfo( // DONE
        "proc_group_bytes_read",
        "The number of bytes read from the Content Repository by Processors in this Process Group in the past 5 minutes"
    );
    protected static final MetricInfo PROC_GROUP_BYTES_RECEIVED = new MetricInfo( // No metrics?
        "proc_group_bytes_received",
        "The number of bytes received by a process group"
    );
    protected static final MetricInfo PROC_GROUP_BYTES_SENT = new MetricInfo( // No metrics?
        "proc_group_bytes_sent",
        "The number of bytes sent by a process group"
    );
    protected static final MetricInfo PROC_GROUP_BYTES_TRANSFERRED = new MetricInfo( // DONE
        "proc_group_bytes_transferred",
        "The number of bytes read from or written to the Content Repository by Processors in this Process Group in the past 5 minutes"
    );
    protected static final MetricInfo PROC_GROUP_BYTES_WRITTEN = new MetricInfo( // DONE
        "proc_group_bytes_written",
        "The number of bytes written to the Content Repository by Processors in this Process Group in the past 5 minutes"
    );
    protected static final MetricInfo PROC_GROUP_FLOW_FILES_RECEIVED = new MetricInfo( // No metrics?
        "proc_group_flow_files_received",
        "The number of bytes sent by a process group"
    );
    protected static final MetricInfo PROC_GROUP_FLOW_FILES_SENT = new MetricInfo( // No metrics?
        "proc_group_flow_files_sent",
        "The number of bytes sent by a process group"
    );
    protected static final MetricInfo PROC_GROUP_FLOW_FILES_TRANSFERRED = new MetricInfo(
        "proc_group_flow_files_transferred",
        "XXX"
    );
    protected static final MetricInfo PROC_GROUP_INPUT_CONTENT_SIZE = new MetricInfo( // DONE
        "proc_group_input_content_size",
        "The cumulative size in bytes of FlowFiles that have entered this Process Group via its Input Ports in the past 5 minutes"
    );
    protected static final MetricInfo PROC_GROUP_INPUT_COUNT = new MetricInfo(  // DONE
        "proc_group_input_count",
        "The number of FlowFiles that have entered this Process Group via its Input Ports in the past 5 minutes"
    );
    protected static final MetricInfo PROC_GROUP_OUTPUT_CONTENT_SIZE = new MetricInfo( // DONE
        "proc_group_output_content_size",
        "The cumulative size in bytes of FlowFiles that have exited this Process Group via its Output Ports in the past 5 minutes"
    );
    protected static final MetricInfo PROC_GROUP_OUTPUT_COUNT = new MetricInfo( // DONE
        "proc_group_output_count",
        "The number of FlowFiles that have exited this Process Group via its Output Ports in the past 5 minutes"
    );
    protected static final MetricInfo PROC_GROUP_QUEUED_CONTENT_SIZE = new MetricInfo( // DONE
        "proc_group_queued_content_size",
        "The cumulative size in bytes of all FlowFiles queued in all Connections of this Process Group"
    );
    protected static final MetricInfo PROC_GROUP_QUEUED_COUNT = new MetricInfo( // DONE
        "proc_group_queued_count",
        "The number of FlowFiles queued in all Connections of this Process Group"
    );
    protected static final MetricInfo PROC_GROUP_PROCESSING_NS = new MetricInfo( // DONE
        "proc_group_processing_time_ns",
        "The total number of thread-nanoseconds that the Processors within this Process Group have used to complete their tasks in the past 5 minutes"
    );
    protected static final MetricInfo PROC_GROUP_STATUS_COUNT = new MetricInfo( // DONE
        "proc_group_status_count",
        "The number of Processors and/or Ports for this Process Group with the given status"
    );

    /***** JVM Metrics *****/
    protected static final String[] JVM_LABELS = {"jvm", "version"};
    protected static final String[] JVM_GC_LABELS = {"jvm", "version", "gc"};
    
    protected static final MetricInfo JVM_UPTIME = new MetricInfo(
        "jvm_uptime",
        "The uptime in seconds for the JVM process"
    );
    protected static final MetricInfo JVM_DAEMON_THREAD_COUNT = new MetricInfo(
        "jvm_daemon_thread_count",
        "The number of live daemon threads"
    );
    protected static final MetricInfo JVM_FILE_DESC_USAGE = new MetricInfo(
        "jvm_file_descriptor_usage",
        "The percentage of available file descriptors which are in use"
    );
    protected static final MetricInfo JVM_HEAP_USAGE = new MetricInfo(
        "jvm_heap_usage",
        "The percentage of the JVM's heap that is in use"
    );
    protected static final MetricInfo JVM_HEAP_USED = new MetricInfo(
        "jvm_heap_used",
        "The amount of heap memory in bytes that is in current use"
    );
    protected static final MetricInfo JVM_NON_HEAP_USAGE = new MetricInfo( // this is negative???
        "jvm_non_heap_usage",
        "The percentage of the JVM's non-heap memory that is in current use"
    );
    protected static final MetricInfo JVM_THREAD_COUNT = new MetricInfo(
        "jvm_thread_count",
        "Returns the total number of live threads (includes daemon threads)"
    );
    protected static final MetricInfo JVM_HEAP_COMMITTED = new MetricInfo(
        "jvm_heap_committed",
        "The amount of heap memory in bytes committed to the JVM"
    );
    protected static final MetricInfo JVM_HEAP_INIT = new MetricInfo(
        "jvm_heap_initial",
        "The initial heap memory in bytes of the JVM"
    );
    protected static final MetricInfo JVM_HEAP_MAX = new MetricInfo(
        "jvm_heap_max",
        "The amount of heap memory currently used by the JVM" // TODO
    );
    protected static final MetricInfo JVM_TOTAL_MEM_COMMITTED = new MetricInfo(
        "jvm_total_memory_committed",
        "The total about of memory in bytes that has been committed to the JVM"
    );
    protected static final MetricInfo JVM_TOTAL_MEM_INIT = new MetricInfo(
        "jvm_total_memory_initial",
        "The total initial memory of the JVM"
    );
    protected static final MetricInfo JVM_TOTAL_MEM_MAX = new MetricInfo(
        "jvm_total_memory_max",
        "The total memory used by the JVM" // TODO
    );
    protected static final MetricInfo JVM_TOTAL_MEM_USED = new MetricInfo(
        "jvm_total_memory_used",
        "The total memory currently used by the JVM"
    );
    protected static final MetricInfo JVM_BLOCKED_THREADS = new MetricInfo(
        "jvm_blocked_threads",
        "The total number of threads in the blocked state"
    );
    protected static final MetricInfo JVM_TERMINATED_THREADS = new MetricInfo(
        "jvm_terminated_threads",
        "The total number of threads in the terminated state"
    );
    protected static final MetricInfo JVM_RUNNABLE_THREADS = new MetricInfo(
        "jvm_runnable_threads",
        "The total number of threads in the runnable state"
    );
    protected static final MetricInfo JVM_TIMED_WAITING_THREADS = new MetricInfo(
        "jvm_timed_waiting_threads",
        "The total number of threads in the timed waiting state"
    );
    protected static final MetricInfo JVM_GC_TOTAL_RUNS = new MetricInfo(
        "jvm_gc_total_runs",
        "The total number of garbage collector executions for the JVM across all garbage collectors"
    );
    protected static final MetricInfo JVM_GC_TOTAL_TIME = new MetricInfo(
        "jvm_gc_total_time",
        "The total run time in milliseconds of all garbage collectors for the JVM"
    );
    protected static final MetricInfo JVM_GC_RUNS = new MetricInfo(
        "jvm_gc_runs",
        "The number of garbage collector executions for the JVM for a specific garbage collector"
    );
    protected static final MetricInfo JVM_GC_TIME = new MetricInfo(
        "jvm_gc_time",
        "The run time in milliseconds of all garbage collectors for the JVM"
    );
}
