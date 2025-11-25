package com.myorg.gitinsights;

/**
 * Time series statistics (commits per time period).
 */
public class TimeSeriesStats implements Comparable<TimeSeriesStats> {
    private final String period;
    private final int count;

    public TimeSeriesStats(String period, int count) {
        this.period = period;
        this.count = count;
    }

    public String getPeriod() {
        return period;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(TimeSeriesStats other) {
        // Sort by period (chronological)
        return this.period.compareTo(other.period);
    }

    @Override
    public String toString() {
        return String.format("%-15s %5d", period, count);
    }
}
