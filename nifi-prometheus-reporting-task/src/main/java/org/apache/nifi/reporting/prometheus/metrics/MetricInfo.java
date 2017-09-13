package org.apache.nifi.reporting.prometheus.metrics;


public class MetricInfo {
    private final String name;
    private final String help;

    MetricInfo(String name, String help) {
        this.name = name;
        this.help = help;
    }

    public String getName() {
        return this.name;
    }

    public String getHelp() {
        return this.help;
    }
}