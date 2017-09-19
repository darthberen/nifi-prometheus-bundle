package org.apache.nifi.reporting.prometheus.metrics;


/**
 * A Prometheus metric definition.  Maps a metric name to its help and labels.
 */
public class MetricInfo {
    private final String name;
    private final String help;
    private final String[] labels;

    /**
     * Initializes MetricInfo with the given parameters.
     * 
     * @param name   the metric name
     * @param help   the help string for this metric
     * @param labels the labels for this metric
     */
    MetricInfo(String name, String help, String[] labels) {
        this.name = name;
        this.help = help;
        this.labels = labels;
    }

    public String getName() {
        return this.name;
    }

    public String getHelp() {
        return this.help;
    }

    public String[] getLabels() {
        return this.labels;
    }
}