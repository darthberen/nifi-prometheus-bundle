package org.apache.nifi.reporting.prometheus;

import org.apache.nifi.controller.status.*;

public class TestFactory {

    public static ProcessGroupStatus createProcessGroupStatus() {
        ProcessGroupStatus status = new ProcessGroupStatus();
        status.setId("5");
        status.setName("sample");
        status.setActiveThreadCount(5);
        status.setBytesRead(5l);
        status.setBytesTransferred(5);
        status.setBytesWritten(5l);
        status.setFlowFilesTransferred(5);
        status.setInputContentSize(5l);
        status.setInputCount(5);
        status.setOutputContentSize(5l);
        status.setOutputCount(5);
        status.setQueuedContentSize(5l);
        status.setQueuedCount(5);
        return status;
    }

    public static PortStatus createPortStatus() {
        PortStatus portStatus = new PortStatus();
        portStatus.setActiveThreadCount(5);
        portStatus.setInputCount(5);
        portStatus.setInputBytes(5);
        portStatus.setOutputCount(5);
        portStatus.setOutputBytes(5);
        portStatus.setId("5");
        portStatus.setName("sample");
        portStatus.setGroupId("5");
        portStatus.setRunStatus(RunStatus.Running);
        return portStatus;
    }

    public static ConnectionStatus createConnectionStatus() {
        ConnectionStatus connStatus = new ConnectionStatus();
        connStatus.setId("5");
        connStatus.setName("sample");
        connStatus.setGroupId("5");
        connStatus.setSourceId("5");
        connStatus.setSourceName("sample-source");
        connStatus.setDestinationId("6");
        connStatus.setDestinationName("sample-dest");
        connStatus.setBackPressureBytesThreshold(4);
        connStatus.setBackPressureObjectThreshold(4);
        connStatus.setInputCount(5);
        connStatus.setInputBytes(5);
        connStatus.setOutputCount(5);
        connStatus.setOutputBytes(5);
        connStatus.setQueuedCount(5);
        connStatus.setQueuedBytes(5);
        return connStatus;
    }

    public static ProcessorStatus createProcessorStatus() {
        ProcessorStatus procStatus = new ProcessorStatus();
        procStatus.setFlowFilesRemoved(5);
        procStatus.setActiveThreadCount(5);
        procStatus.setBytesRead(5);
        procStatus.setBytesWritten(5);
        procStatus.setProcessingNanos(123456789);
        procStatus.setInvocations(5);
        procStatus.setInputBytes(5);
        procStatus.setOutputBytes(5);
        procStatus.setInputCount(5);
        procStatus.setOutputCount(5);
        procStatus.setId("5");
        procStatus.setName("sample");
        procStatus.setGroupId("5");
        procStatus.setType("sample");
        procStatus.setRunStatus(RunStatus.Disabled);
        return procStatus;
    }
}
