package pl.tdf.atak.TAKLog;

import static pl.tdf.atak.TAKLog.TakLogConstants.LIST_LIMIT;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.dropdown.DropDown;
import com.atakmap.android.dropdown.DropDownReceiver;
import com.atakmap.android.maps.MapItem;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.util.time.TimeListener;
import com.atakmap.android.util.time.TimeViewUpdater;
import com.atakmap.coremap.maps.time.CoordinatedTime;

import pl.tdf.atak.TAKLog.recyclerview.RecyclerView;
import pl.tdf.atak.TAKLog.recyclerview.RecyclerViewAdapter;
import pl.tdf.atak.TAKLog.plugin.R;

public class TakLogDropDownReceiver extends DropDownReceiver implements DropDown.OnStateListener, TimeListener {

    public static String SHOW_DROP_DOWN = "pl.tdf.atak.TAKLog.SHOW_DROP_DOWN";
    public static String TAG = "TakLogDropDownReceiver";

    private final View logView;
    private final RecyclerViewAdapter recyclerViewAdapter;
    private final TimeViewUpdater timeUpdater;

    protected TakLogDropDownReceiver(MapView mapView, final Context context) {
        super(mapView);
        logView = PluginLayoutInflater.inflate(context, R.layout.log_layout, null);
        recyclerViewAdapter = new RecyclerViewAdapter(mapView, context);
        TextView subtitle = logView.findViewById(R.id.subtitle);
        subtitle.setText("Showing last " + LIST_LIMIT + " events");
        RecyclerView recyclerView = logView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false)
        );

        Button deleteBtn = logView.findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(view -> recyclerViewAdapter.clear());

        timeUpdater = new TimeViewUpdater(mapView, 1000);
        timeUpdater.register(this);
    }

    public void addItem(MapItem item) {
        recyclerViewAdapter.addItem(item);
    }

    @Override
    public void onTimeChanged(CoordinatedTime ot, CoordinatedTime nt) {
        if (isVisible())
            recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action == null)
            return;

        if (action.equals(SHOW_DROP_DOWN)) {
            if (!isClosed()) {
                unhideDropDown();
                return;
            }
            showDropDown(logView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH, HALF_HEIGHT, false, this);
        }
    }

    @Override
    protected void disposeImpl() {
        timeUpdater.unregister(this);
    }

    @Override
    public void onDropDownSelectionRemoved() {
        hideDropDown();
    }

    @Override
    public void onDropDownClose() {
        hideDropDown();
    }

    @Override
    public void onDropDownSizeChanged(double v, double v1) {

    }

    @Override
    public void onDropDownVisible(boolean b) {

    }
}
