package org.apache.nifi.reporting.prometheus;

import io.prometheus.client.*;
import io.prometheus.client.exporter.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class configures MetricRegistry (passed outside or created from scratch) with Prometheus support
 */
public class PrometheusMetricRegistryBuilder {
    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    private CollectorRegistry collectorRegistry = null;
    private String gateway = "";
    private PushGateway exporter;

    public PrometheusMetricRegistryBuilder (CollectorRegistry collectorRegistry) {
        this.collectorRegistry = collectorRegistry;
    }

    public PushGateway getPrometheusExporter(String gateway) {
        if (exporter == null){
            logger.warn("creating an PushGateway instance for gateway: " + gateway);
            exporter = createExporter(gateway);
        }
        return exporter;
    }

    private PushGateway createExporter(String gateway) {
        if (this.gateway.equals(gateway)) {
            logger.warn("using the existing pubshgateway: " + this.exporter);
            return this.exporter;
        } else {
            this.gateway = gateway;
            exporter = new PushGateway(gateway);
            logger.warn("creating a total new pubshgateway: " + exporter);
            return exporter;
        }
    }
}