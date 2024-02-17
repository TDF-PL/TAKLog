package pl.tdf.atak.TAKLog.sorting;

import static pl.tdf.atak.TAKLog.MapItemDataRetriever.getTime;

import java.util.Comparator;

import pl.tdf.atak.TAKLog.LogElement;

public class TimeComparator implements Comparator<LogElement> {
    @Override
    public int compare(LogElement o1, LogElement o2) {
        return -Long.compare(getTime(o1.getMapItem()), getTime(o2.getMapItem()));
    }
}
