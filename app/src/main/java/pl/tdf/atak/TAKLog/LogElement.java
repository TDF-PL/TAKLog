package pl.tdf.atak.TAKLog;

import com.atakmap.android.maps.MapItem;
import com.atakmap.android.maps.MapGroup;

import java.util.Objects;

public class LogElement {
    private final MapItem mapItem;
    private final MapGroup mapGroup;

    public LogElement(MapItem mapItem, MapGroup mapGroup) {
        this.mapItem = mapItem;
        this.mapGroup = mapGroup;
    }

    public MapItem getMapItem() {
        return mapItem;
    }

    public MapGroup getMapGroup() {
        return mapGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogElement that = (LogElement) o;

        if (!Objects.equals(mapItem, that.mapItem)) return false;
        return Objects.equals(mapGroup, that.mapGroup);
    }

    @Override
    public int hashCode() {
        int result = mapItem != null ? mapItem.hashCode() : 0;
        result = 31 * result + (mapGroup != null ? mapGroup.hashCode() : 0);
        return result;
    }
}
