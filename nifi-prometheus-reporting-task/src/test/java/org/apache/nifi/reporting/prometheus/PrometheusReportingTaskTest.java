package org.apache.nifi.reporting.prometheus;

import com.kstruct.gethostname4j.Hostname;
import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.PushGateway;
import org.apache.nifi.controller.ConfigurationContext;
import org.apache.nifi.controller.status.ConnectionStatus;
import org.apache.nifi.controller.status.PortStatus;
import org.apache.nifi.controller.status.ProcessGroupStatus;
import org.apache.nifi.controller.status.ProcessorStatus;
import org.apache.nifi.reporting.EventAccess;
import org.apache.nifi.reporting.ReportingContext;
import org.apache.nifi.util.MockPropertyValue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


public class PrometheusReportingTaskTest {

    private Map<String, String> sharedLabels;
    private ConfigurationContext configurationContext;
    private ReportingContext context;
    private String gateway = "localhost:9091";
    private String jobName = "sample-job";
    private ProcessGroupStatus groupStatus;

    @Before
    public void setup() throws Exception {
        initProcessGroupStatuses();
        initContexts();
    }

    //init all contexts
    private void initContexts() throws Exception {
        configurationContext = Mockito.mock(ConfigurationContext.class);
        when(configurationContext.getProperty(PrometheusReportingTask.PROMETHEUS_GATEWAY))
                .thenReturn(new MockPropertyValue(gateway, null));
        when(configurationContext.getProperty(PrometheusReportingTask.PROMETHEUS_JOB_NAME))
                .thenReturn(new MockPropertyValue(jobName, null));

        sharedLabels = new HashMap<>();
        sharedLabels.put("instance", Hostname.getHostname());

        context = Mockito.mock(ReportingContext.class);
        EventAccess eventAccess = Mockito.mock(EventAccess.class);
        when(eventAccess.getControllerStatus()).thenReturn(groupStatus);
        when(context.getEventAccess()).thenReturn(eventAccess);
    }

    //prep all the data required
    private void initProcessGroupStatuses() {
        groupStatus = TestFactory.createProcessGroupStatus();

        Collection<ProcessorStatus> processorStatuses = new ArrayList<>();
        processorStatuses.add(TestFactory.createProcessorStatus());
        groupStatus.setProcessorStatus(processorStatuses);

        Collection<PortStatus> portStatuses = new ArrayList<>();
        portStatuses.add(TestFactory.createPortStatus());
        groupStatus.setInputPortStatus(portStatuses);

        Collection<ConnectionStatus> connectionStatuses = new ArrayList<>();
        connectionStatuses.add(TestFactory.createConnectionStatus());
        groupStatus.setConnectionStatus(connectionStatuses);

        Collection<ProcessGroupStatus> groupStatuses = new ArrayList<>();
        groupStatuses.add(TestFactory.createProcessGroupStatus());
        groupStatus.setProcessGroupStatus(groupStatuses);
    }

    //test onTrigger method
    @Test
    public void testOnTrigger() throws IOException {
        PrometheusReportingTask testClass = Mockito.spy(new PrometheusReportingTask());

        //mock pushgateway
        PushGateway mockGateway = Mockito.mock(PushGateway.class);
        doNothing().when(mockGateway).push(isA(CollectorRegistry.class),isA(String.class),isA(Map.class));
        when(testClass.getPushGateway()).thenReturn(mockGateway);

        //trigger and verify data
        testClass.setup(configurationContext);
        testClass.onTrigger(context);
        assertHelper(testClass.getCollectorRegistry());
    }

    private void assertHelper(CollectorRegistry registry) {
        List<Collector.MetricFamilySamples> mfs = Collections.list(registry.metricFamilySamples());

        //total count of metrics captured
        assertEquals(52, mfs.size());

        for (Collector.MetricFamilySamples mf : mfs) {
            //System.out.println(mf.samples);
            assertNotEquals(0, mf.samples.size());
        }
    }
}