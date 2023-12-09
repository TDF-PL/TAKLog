package pl.tdf.atak.TAKLog;

import android.util.Log;

import com.atakmap.android.maps.MapItem;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.math.MathUtils;
import com.atakmap.coremap.maps.time.CoordinatedTime;

public class MapItemDataRetriever {

    private static final String TAG = "TakLogDataReceiver";

    public static String getReadableTime(MapItem mapItem) {
        long now = new CoordinatedTime().getMilliseconds();
        long lastUpdateTime = getTime(mapItem);
        return MathUtils.GetTimeRemainingOrDateString(now, now - lastUpdateTime, true);
    }

    public static long getTime(MapItem mapItem) {
        return mapItem.getMetaLong("lastUpdateTime", 0);
    }

    public static String getAuthorName(MapItem mapItem, MapView mapView) {
        final String parentUid = mapItem.getMetaString("parent_uid", null);
        if (parentUid == null) {
            return null;
        }

        final MapItem author_item = mapView.getMapItem(parentUid);
        if (author_item == null) {
            return null;
        }

        String authorName = author_item.getMetaString("callsign", null);
        if (authorName != null) {
            return authorName;
        }

        String parentCallsign = author_item.getMetaString("parent_callsign", null);
        if (parentCallsign != null) {
            return parentCallsign;
        }

        Log.d(TAG, "No author name for object" + mapItem.getUID());
        return null;
    }
}
