package pl.tdf.atak.TAKLog.sorting;

import java.util.Comparator;

import pl.tdf.atak.TAKLog.LogElement;

public class DistanceComparator implements Comparator<LogElement> {
    @Override
    public int compare(LogElement o1, LogElement o2) {
        return Double.compare(o1.getDistance(), o2.getDistance());
    }
}
