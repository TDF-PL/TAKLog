package pl.tdf.atak.TAKLog.recyclerview;


import static com.atakmap.android.maps.MapItem.computeDistance;
import static com.atakmap.android.maps.MapView.getMapView;
import static pl.tdf.atak.TAKLog.MapItemDataRetriever.getAuthorName;
import static pl.tdf.atak.TAKLog.MapItemDataRetriever.getReadableTime;
import static pl.tdf.atak.TAKLog.TakLogConstants.LIST_LIMIT;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapGroup;
import com.atakmap.android.maps.MapItem;
import com.atakmap.android.maps.MapTouchController;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.util.ATAKUtilities;
import com.atakmap.coremap.maps.coords.GeoPoint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import pl.tdf.atak.TAKLog.LogElement;
import pl.tdf.atak.TAKLog.plugin.R;
import pl.tdf.atak.TAKLog.sorting.TimeComparator;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private static final String TAG = "TakLogAdapter";
    private final MapView mapView;
    private final LayoutInflater inflater;
    private final List<LogElement> items = new ArrayList<>();
    private Comparator<LogElement> comparator = new TimeComparator();

    public RecyclerViewAdapter(MapView mapView, Context plugin) {
        this.mapView = mapView;
        this.inflater = LayoutInflater.from(plugin);
    }

    public void addItem(MapItem item) {
        GeoPoint selfPoint = getMapView().getSelfMarker().getPoint();
        double distance = computeDistance(item, selfPoint);
        LogElement newElement = new LogElement(item, item.getGroup(), distance);

        if (items.contains(newElement)) {
            return;
        }

        if (items.size() == LIST_LIMIT) {
            items.remove(items.size() - 1);
        }

        items.add(newElement);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            items.sort(comparator);
        }
    }

    public void recalculateDistance() {
        GeoPoint selfPoint = getMapView().getSelfMarker().getPoint();
        for (LogElement logElement : items) {
            double distance = computeDistance(logElement.getMapItem(), selfPoint);
            logElement.setDistance(distance);
        }
    }

    public void clear() {
        items.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.marker_callsign_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rh, int pos) {
        if (!(rh instanceof ViewHolder)) return;

        LogElement logElement = items.get(pos);
        MapItem mapItem = (MapItem) logElement.getMapItem();
        ViewHolder view = (ViewHolder) rh;

        String readableTime = getReadableTime(mapItem);
        String author_name = getAuthorName(mapItem, mapView);
        String callsign = mapItem.getTitle();
        String distance = String.format(Locale.US, "%.1f", logElement.getDistance()) + "m";
        String subtitle = author_name == null ?
                readableTime + ", " + distance :
                readableTime + " by " + author_name + ", " + distance;

        ATAKUtilities.SetIcon(mapView.getContext(), view.icon, mapItem);
        view.subtitle.setText(subtitle);
        view.callsign.setText(callsign);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void sort(Comparator<LogElement> comparator) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            items.sort(comparator);
            this.comparator = comparator;
        } else {
            Log.e(TAG, "You need plugin version ?=" + Build.VERSION_CODES.N + " to run this feature");
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView icon;
        final TextView callsign;
        final TextView subtitle;

        public ViewHolder(View v) {
            super(v);
            icon = v.findViewById(R.id.icon);
            callsign = v.findViewById(R.id.callsign);
            subtitle = v.findViewById(R.id.subtitle);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (pos < 0 || pos >= getItemCount()) return;

            LogElement logElement = items.get(pos);
            MapItem item = logElement.getMapItem();
            boolean existsOnMap = mapView.getMapItem(item.getUID()) != null;

            if (existsOnMap) {
                MapTouchController.goTo(item, true);
            } else {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(mapView.getContext());
                dialog.setTitle("Not existing item");
                dialog.setMessage("The item does not exist. Would you like to add it to the map?");
                dialog.setPositiveButton("Yes, add", (d, which) -> {
                    restoreMarker(logElement);
                    d.dismiss();
                });
                dialog.setNegativeButton("Cancel", (d, which) -> d.dismiss());
                dialog.show();
            }
        }

        private void restoreMarker(LogElement logElement) {

            MapGroup fallbackMapGroup = getMapView().getRootGroup().findMapGroup("Cursor on Target").findMapGroup(getGroupName(logElement.getMapItem().getType()));

            MapItem item = logElement.getMapItem();

            MapGroup mapGroup = logElement.getMapGroup() == null ? fallbackMapGroup : logElement.getMapGroup();
            mapGroup.addItem(logElement.getMapItem());

            logElement.getMapItem().persist(mapView.getMapEventDispatcher(), null, this.getClass());

            Intent newCotIntent = new Intent();
            newCotIntent.setAction("com.atakmap.android.maps.COT_PLACED");
            newCotIntent.putExtra("uid", item.getUID());
            AtakBroadcast.getInstance().sendBroadcast(newCotIntent);
        }

        @NonNull
        private String getGroupName(String type) {
            switch (type) {
                case "a-h-G":
                    return "Hostile";
                case "a-u-G":
                    return "Unknown";
                case "a-f-G":
                    return "Friendly";
                case "a-n-G":
                    return "Neutral";
                default:
                    return "Other";
            }
        }
    }
}
