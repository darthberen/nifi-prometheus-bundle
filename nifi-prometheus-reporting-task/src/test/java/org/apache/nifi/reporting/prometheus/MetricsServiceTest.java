package org.apache.nifi.reporting.prometheus;

import com.yammer.metrics.core.VirtualMachineMetrics;
import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import org.apache.nifi.reporting.prometheus.metrics.MetricsService;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MetricsServiceTest {

    //test updating JMV metrics
    @Test
    public void testUpdateJVMMetrics() {
        CollectorRegistry registry = new CollectorRegistry();
        MetricsService ms = new MetricsService(registry);
        ms.updateJVMMetrics(VirtualMachineMetrics.getInstance());
        assertHelper(registry, "jvm");
    }

    //test updating ProcessorStatus metrics
    @Test
    public void testUpdateProcessorMetrics() {
        CollectorRegistry registry = new CollectorRegistry();
        MetricsService ms = new MetricsService(registry);
        ms.updateProcessorMetrics(TestFactory.createProcessorStatus());
        assertHelper(registry, "processor");
    }

    //test updating PortStatus metrics
    @Test
    public void testUpdatePortStatusMetrics() {
        CollectorRegistry registry = new CollectorRegistry();
        MetricsService ms = new MetricsService(registry);
        ms.updatePortStatusMetrics(TestFactory.createPortStatus());
        assertHelper(registry, "port");
    }

    //test updating ConnectionStatus metrics
    @Test
    public void testUpdateConnectionStatusMetrics() {
        CollectorRegistry registry = new CollectorRegistry();
        MetricsService ms = new MetricsService(registry);
        ms.updateConnectionStatusMetrics(TestFactory.createConnectionStatus());
        assertHelper(registry, "conn");
    }

    //test updating ProcessGroupStatus metrics
    @Test
    public void testUpdateProcessGroupMetrics() {
        CollectorRegistry registry = new CollectorRegistry();
        MetricsService ms = new MetricsService(registry);
        ms.updateProcessGroupMetrics(TestFactory.createProcessGroupStatus());
        assertHelper(registry, "proc_");
    }

    private void assertHelper(CollectorRegistry registry, String processor) {
        List<Collector.MetricFamilySamples> mfs = Collections.list(registry.metricFamilySamples());
        for (Collector.MetricFamilySamples mf : mfs) {
            if (mf.name.startsWith(processor))
                assertNotEquals(0, mf.samples.size());
            else
                assertEquals(0, mf.samples.size());
        }
    }
}