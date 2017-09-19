package org.apache.nifi.reporting.prometheus.metrics;

/**
 * The metric definitions that will be sent to Prometheus.
 */
public final class MetricDefs {
    /***** Processor Metrics *****/
    // Note: flowfiles sent/received are not used
    protected static final String[] PROCESSOR_LABELS = {"id", "name", "group_id", "type"};

    protected static final MetricInfo PROC_FLOW_FILES_REMOVED = new MetricInfo(
        "processor_flowfiles_removed",
        "The total number of FlowFiles removed by this Processor over the past 5 minutes",
        PROCESSOR_LABELS
    );
    protected static final MetricInfo PROC_ACTIVE_THREADS = new MetricInfo(
        "processor_active_threads",
        "The number of active threads for this Processor",
        PROCESSOR_LABELS
    );
    protected static final MetricInfo PROC_BYTES_READ = new MetricInfo(
        "processor_bytes_read",
        "The number of bytes read from the Content Repository by this Processor in the past 5 minutes",
        PROCESSOR_LABELS
    );
    protected static final MetricInfo PROC_BYTES_WRITTEN = new MetricInfo(
        "processor_bytes_written",
        "The number of bytes written to the Content Repository by this Processor in the past 5 minutes",
        PROCESSOR_LABELS
    );
    protected static final MetricInfo PROC_PROCESSING_NS = new MetricInfo(
        "processor_processing_time_ns",
        "The total number of thread-nanoseconds that the Processor has used to complete its tasks in the past 5 minutes",
        PROCESSOR_LABELS
    );
    protected static final MetricInfo PROC_INVOCATIONS = new MetricInfo(
        "processor_invocations",
        "The number of times that this Processor has been invoked (completed tasks) over the past 5 minutes",
        PROCESSOR_LABELS
    );
    protected static final MetricInfo PROC_INPUT_BYTES = new MetricInfo(
        "processor_input_bytes",
        "The cumulative size in bytes of all FlowFiles that this Processor has pulled from its queues in the past 5 minutes",
        PROCESSOR_LABELS
    );
    protected static final MetricInfo PROC_OUTPUT_BYTES = new MetricInfo(
        "processor_output_bytes",
        "The cumulative size in bytes of all FlowFiles that this Processor has transferred to downstream queues in the past 5 minutes",
        PROCESSOR_LABELS
    );
    protected static final MetricInfo PROC_INPUT_COUNT = new MetricInfo(
        "processor_input_count",
        "The number of FlowFiles that this Processor has pulled from its queues in the past 5 minutes",
        PROCESSOR_LABELS
    );
    protected static final MetricInfo PROC_OUTPUT_COUNT = new MetricInfo(
        "processor_output_count",
        "The number of FlowFiles that this Processor has transferred to downstream queues in the past 5 minutes",
        PROCESSOR_LABELS
    );

    protected static final MetricInfo[] PROCESSOR_METRICS = {
        PROC_FLOW_FILES_REMOVED,
        PROC_ACTIVE_THREADS,
        PROC_BYTES_READ,
        PROC_BYTES_WRITTEN,
        PROC_PROCESSING_NS,
        PROC_INVOCATIONS,
        PROC_INPUT_BYTES,
        PROC_OUTPUT_BYTES,
        PROC_INPUT_COUNT,
        PROC_OUTPUT_COUNT
    };

    /***** Port Metrics *****/
    // Note: flow files received/sent and bytes received/sent are not used
    protected static final String[] PORT_LABELS = {"id", "name", "group_id"};

    protected static final MetricInfo PORT_ACTIVE_THREADS = new MetricInfo(
        "port_active_threads",
        "The number of active threads for this Port",
        PORT_LABELS
    );
    protected static final MetricInfo PORT_INPUT_COUNT = new MetricInfo(
        "port_input_count",
        "The number of FlowFiles that have entered this Port in the past 5 minutes",
        PORT_LABELS
    );
    protected static final MetricInfo PORT_INPUT_BYTES = new MetricInfo(
        "port_input_bytes",
        "The cumulative size in bytes of all FlowFiles that have entered this Port in the past 5 minutes",
        PORT_LABELS
    );
    protected static final MetricInfo PORT_OUTPUT_COUNT = new MetricInfo(
        "port_output_count",
        "The number of FlowFiles that have exited this Port in the past 5 minutes",
        PORT_LABELS
    );
    protected static final MetricInfo PORT_OUTPUT_BYTES = new MetricInfo(
        "port_output_bytes",
        "The cumulative size in bytes of all FlowFiles that have exited this Port in the past 5 minutes",
        PORT_LABELS
    );

    protected static final MetricInfo[] PORT_METRICS = {
        PORT_ACTIVE_THREADS,
        PORT_INPUT_COUNT,
        PORT_INPUT_BYTES,
        PORT_OUTPUT_COUNT,
        PORT_OUTPUT_BYTES
    };

    /***** Connection Metrics *****/
    // Note: max queued count/bytes are not used
    protected static final String[] CONNECTION_LABELS = {"id", "name", "group_id", "source_id", "source_name", "destination_id", "destination_name"};

    protected static final MetricInfo CONN_BACK_PRESSURE_BYTES_THRESHOLD = new MetricInfo(
        "conn_back_pressure_bytes_threshold",
        "The back pressure bytes threshold for this Connection",
        CONNECTION_LABELS
    );
    protected static final MetricInfo CONN_BACK_PRESSURE_OBJ_THRESHOLD = new MetricInfo(
        "conn_back_pressure_obj_threshold",
        "The back pressure FlowFile threshold for this Connection",
        CONNECTION_LABELS
    );
    protected static final MetricInfo CONN_INPUT_COUNT = new MetricInfo(
        "conn_input_count",
        "The total number of FlowFiles that were transferred to this Connection in the past 5 minutes",
        CONNECTION_LABELS
    );
    protected static final MetricInfo CONN_INPUT_BYTES = new MetricInfo(
        "conn_input_bytes",
        "The cumulative size in bytes of all FlowFiles that were transferred to this Connection in the past 5 minutes",
        CONNECTION_LABELS
    );
    protected static final MetricInfo CONN_OUTPUT_COUNT = new MetricInfo(
        "conn_output_count",
        "The total number of FlowFiles that were pulled from this Connection in the past 5 minutes",
        CONNECTION_LABELS
    );
    protected static final MetricInfo CONN_OUTPUT_BYTES = new MetricInfo(
        "conn_output_bytes",
        "The cumulative size in bytes of all FlowFiles that were pulled from this Connection in the past 5 minutes",
        CONNECTION_LABELS
    );
    protected static final MetricInfo CONN_QUEUED_COUNT = new MetricInfo(
        "conn_queued_count",
        "The number of FlowFiles queued by this Connection in the past 5 minutes",
        CONNECTION_LABELS
    );
    protected static final MetricInfo CONN_QUEUED_BYTES = new MetricInfo(
        "conn_queued_bytes",
        "The number of bytes queued by this Connection in the past 5 minutes",
        CONNECTION_LABELS
    );

    protected static final MetricInfo[] CONNECTION_METRICS = {
        CONN_BACK_PRESSURE_BYTES_THRESHOLD,
        CONN_BACK_PRESSURE_OBJ_THRESHOLD,
        CONN_INPUT_COUNT,
        CONN_INPUT_BYTES,
        CONN_OUTPUT_COUNT,
        CONN_OUTPUT_BYTES,
        CONN_QUEUED_COUNT,
        CONN_QUEUED_BYTES
    };

    /***** Process Group Metrics *****/
    // Note: flowfiles sent/received and bytes sent/received are not used
    protected static final String[] PROC_GROUP_LABELS = {"id", "name"};
    protected static final String[] PROC_GROUP_STATUS_LABELS = {"id", "name", "status"};

    protected static final MetricInfo PROC_GROUP_ACTIVE_THREADS = new MetricInfo(
        "proc_group_active_threads",
        "The number of active threads in this Process Group",
        PROC_GROUP_LABELS
    );
    protected static final MetricInfo PROC_GROUP_BYTES_READ = new MetricInfo(
        "proc_group_bytes_read",
        "The number of bytes read from the Content Repository by Processors in this Process Group in the past 5 minutes",
        PROC_GROUP_LABELS
    );
    protected static final MetricInfo PROC_GROUP_BYTES_TRANSFERRED = new MetricInfo(
        "proc_group_bytes_transferred",
        "The number of bytes read from or written to the Content Repository by Processors in this Process Group in the past 5 minutes",
        PROC_GROUP_LABELS
    );
    protected static final MetricInfo PROC_GROUP_BYTES_WRITTEN = new MetricInfo(
        "proc_group_bytes_written",
        "The number of bytes written to the Content Repository by Processors in this Process Group in the past 5 minutes",
        PROC_GROUP_LABELS
    );
    protected static final MetricInfo PROC_GROUP_FLOW_FILES_TRANSFERRED = new MetricInfo(
        "proc_group_flow_files_transferred",
        "The number of FlowFiles read from or written to the Content Repository by Processors in this Process Group in the past 5 minutes",
        PROC_GROUP_LABELS
    );
    protected static final MetricInfo PROC_GROUP_INPUT_CONTENT_SIZE = new MetricInfo(
        "proc_group_input_content_size",
        "The cumulative size in bytes of FlowFiles that have entered this Process Group via its Input Ports in the past 5 minutes",
        PROC_GROUP_LABELS
    );
    protected static final MetricInfo PROC_GROUP_INPUT_COUNT = new MetricInfo(
        "proc_group_input_count",
        "The number of FlowFiles that have entered this Process Group via its Input Ports in the past 5 minutes",
        PROC_GROUP_LABELS
    );
    protected static final MetricInfo PROC_GROUP_OUTPUT_CONTENT_SIZE = new MetricInfo(
        "proc_group_output_content_size",
        "The cumulative size in bytes of FlowFiles that have exited this Process Group via its Output Ports in the past 5 minutes",
        PROC_GROUP_LABELS
    );
    protected static final MetricInfo PROC_GROUP_OUTPUT_COUNT = new MetricInfo(
        "proc_group_output_count",
        "The number of FlowFiles that have exited this Process Group via its Output Ports in the past 5 minutes",
        PROC_GROUP_LABELS
    );
    protected static final MetricInfo PROC_GROUP_QUEUED_CONTENT_SIZE = new MetricInfo(
        "proc_group_queued_content_size",
        "The cumulative size in bytes of all FlowFiles queued in all Connections of this Process Group",
        PROC_GROUP_LABELS
    );
    protected static final MetricInfo PROC_GROUP_QUEUED_COUNT = new MetricInfo(
        "proc_group_queued_count",
        "The number of FlowFiles queued in all Connections of this Process Group",
        PROC_GROUP_LABELS
    );
    protected static final MetricInfo PROC_GROUP_PROCESSING_NS = new MetricInfo(
        "proc_group_processing_time_ns",
        "The total number of thread-nanoseconds that the Processors within this Process Group have used to complete their tasks in the past 5 minutes",
        PROC_GROUP_LABELS
    );
    protected static final MetricInfo PROC_GROUP_STATUS_COUNT = new MetricInfo(
        "proc_group_status_count",
        "The number of Processors and/or Ports for this Process Group with the given status",
        PROC_GROUP_STATUS_LABELS
    );

    protected static final MetricInfo[] PROCESS_GROUP_METRICS = {
        PROC_GROUP_ACTIVE_THREADS,
        PROC_GROUP_BYTES_READ,
        PROC_GROUP_BYTES_TRANSFERRED,
        PROC_GROUP_BYTES_WRITTEN,
        PROC_GROUP_FLOW_FILES_TRANSFERRED,
        PROC_GROUP_INPUT_CONTENT_SIZE,
        PROC_GROUP_INPUT_COUNT,
        PROC_GROUP_OUTPUT_CONTENT_SIZE,
        PROC_GROUP_OUTPUT_COUNT,
        PROC_GROUP_QUEUED_CONTENT_SIZE,
        PROC_GROUP_QUEUED_COUNT,
        PROC_GROUP_PROCESSING_NS,
        PROC_GROUP_STATUS_COUNT
    };

    /***** JVM Metrics *****/
    protected static final String[] JVM_LABELS = {"jvm", "version"};
    protected static final String[] JVM_THREAD_LABELS = {"jvm", "version", "status"};
    protected static final String[] JVM_GC_LABELS = {"jvm", "version", "gc"};

    protected static final MetricInfo JVM_UPTIME = new MetricInfo(
        "jvm_uptime",
        "The uptime in seconds for the JVM process",
        JVM_LABELS
    );
    protected static final MetricInfo JVM_DAEMON_THREAD_COUNT = new MetricInfo(
        "jvm_daemon_thread_count",
        "The number of live daemon threads",
        JVM_LABELS
    );
    protected static final MetricInfo JVM_FILE_DESC_USAGE = new MetricInfo(
        "jvm_file_descriptor_usage",
        "The percentage of available file descriptors which are in use",
        JVM_LABELS
    );
    protected static final MetricInfo JVM_HEAP_USAGE = new MetricInfo(
        "jvm_heap_usage",
        "The percentage of the JVM's heap that is in use",
        JVM_LABELS
    );
    protected static final MetricInfo JVM_HEAP_USED = new MetricInfo(
        "jvm_heap_used",
        "The amount of heap memory in bytes that is in current use",
        JVM_LABELS
    );
    protected static final MetricInfo JVM_THREAD_COUNT = new MetricInfo(
        "jvm_thread_count",
        "Returns the total number of live threads (includes daemon threads)",
        JVM_LABELS
    );
    protected static final MetricInfo JVM_HEAP_COMMITTED = new MetricInfo(
        "jvm_heap_committed",
        "The amount of heap memory in bytes committed to the JVM",
        JVM_LABELS
    );
    protected static final MetricInfo JVM_HEAP_INIT = new MetricInfo(
        "jvm_heap_initial",
        "The initial heap memory in bytes of the JVM",
        JVM_LABELS
    );
    protected static final MetricInfo JVM_TOTAL_MEM_COMMITTED = new MetricInfo(
        "jvm_total_memory_committed",
        "The total about of memory in bytes that has been committed to the JVM",
        JVM_LABELS
    );
    protected static final MetricInfo JVM_TOTAL_MEM_INIT = new MetricInfo(
        "jvm_total_memory_initial",
        "The total initial memory of the JVM",
        JVM_LABELS
    );
    protected static final MetricInfo JVM_TOTAL_MEM_USED = new MetricInfo(
        "jvm_total_memory_used",
        "The total memory currently used by the JVM",
        JVM_LABELS
    );
    protected static final MetricInfo JVM_THREAD_STATE = new MetricInfo(
        "jvm_thread_state",
        "The total number of threads in the given state",
        JVM_THREAD_LABELS
    );
    protected static final MetricInfo JVM_GC_TOTAL_RUNS = new MetricInfo(
        "jvm_gc_total_runs",
        "The total number of garbage collector executions for the JVM across all garbage collectors",
        JVM_LABELS
    );
    protected static final MetricInfo JVM_GC_TOTAL_TIME = new MetricInfo(
        "jvm_gc_total_time",
        "The total run time in milliseconds of all garbage collectors for the JVM",
        JVM_LABELS
    );
    protected static final MetricInfo JVM_GC_RUNS = new MetricInfo(
        "jvm_gc_runs",
        "The number of garbage collector executions for the JVM for a specific garbage collector",
        JVM_GC_LABELS
    );
    protected static final MetricInfo JVM_GC_TIME = new MetricInfo(
        "jvm_gc_time",
        "The run time in milliseconds of a specific garbage collector for the JVM",
        JVM_GC_LABELS
    );

    protected static final MetricInfo[] JVM_METRICS = {
        JVM_UPTIME,
        JVM_DAEMON_THREAD_COUNT,
        JVM_FILE_DESC_USAGE,
        JVM_HEAP_USAGE,
        JVM_HEAP_USED,
        JVM_THREAD_COUNT,
        JVM_HEAP_COMMITTED,
        JVM_HEAP_INIT,
        JVM_TOTAL_MEM_COMMITTED,
        JVM_TOTAL_MEM_INIT,
        JVM_TOTAL_MEM_USED,
        JVM_THREAD_STATE,
        JVM_GC_TOTAL_RUNS,
        JVM_GC_TOTAL_TIME,
        JVM_GC_RUNS,
        JVM_GC_TIME
    };
}
