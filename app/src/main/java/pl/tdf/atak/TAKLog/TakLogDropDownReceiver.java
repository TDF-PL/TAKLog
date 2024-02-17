package pl.tdf.atak.TAKLog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

import pl.tdf.atak.TAKLog.preferences.PreferencesResolver;
import pl.tdf.atak.TAKLog.preferences.SharedPreferenceChangeListener;
import pl.tdf.atak.TAKLog.recyclerview.RecyclerView;
import pl.tdf.atak.TAKLog.recyclerview.RecyclerViewAdapter;
import pl.tdf.atak.TAKLog.plugin.R;
import pl.tdf.atak.TAKLog.sorting.DistanceComparator;
import pl.tdf.atak.TAKLog.sorting.SortMode;
import pl.tdf.atak.TAKLog.sorting.TimeComparator;

public class TakLogDropDownReceiver extends DropDownReceiver implements DropDown.OnStateListener, TimeListener {

    public static final String SHOW_DROP_DOWN = "pl.tdf.atak.TAKLog.SHOW_DROP_DOWN";
    public static final String TAG = "TakLogDropDownReceiver";

    private final View logView;
    private final RecyclerViewAdapter recyclerViewAdapter;
    private final TimeViewUpdater timeUpdater;
    private final RecyclerView recyclerView;
    private final SharedPreferenceChangeListener preferenceChangeListener;
    private final SharedPreferences sharedPrefs;
    private final Context context;

    private SortMode sortMode = SortMode.TIME;

    protected TakLogDropDownReceiver(MapView mapView, final Context context) {
        super(mapView);
        this.context = context;
        logView = PluginLayoutInflater.inflate(context, R.layout.log_layout, null);
        recyclerViewAdapter = new RecyclerViewAdapter(mapView, context);

        recyclerView = logView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false)
        );

        Button deleteBtn = logView.findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(view -> recyclerViewAdapter.clear());

        Button sortButton = logView.findViewById(R.id.sort);
        sortButton.setText(context.getString(R.string.sort_by, SortMode.DISTANCE.getLabel()));
        sortButton.setOnClickListener(view -> {
            toggleSort((Button) view);
            recyclerView.scrollToPosition(0);
        });

        timeUpdater = new TimeViewUpdater(mapView, 1000);
        timeUpdater.register(this);

        updateSubtitle();

        preferenceChangeListener = new SharedPreferenceChangeListener(() -> {
            updateSubtitle();
            recyclerViewAdapter.revalidateListSize();
        });
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mapView.getContext());
        sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    private void toggleSort(Button button) {
        if (sortMode == SortMode.DISTANCE) {
            sortMode = SortMode.TIME;
            recyclerViewAdapter.sort(new TimeComparator());
            button.setText(context.getString(R.string.sort_by, SortMode.DISTANCE.getLabel()));
        } else if (sortMode == SortMode.TIME) {
            sortMode = SortMode.DISTANCE;
            recyclerViewAdapter.recalculateDistance();
            recyclerViewAdapter.sort(new DistanceComparator());
            button.setText(context.getString(R.string.sort_by, SortMode.TIME.getLabel()));
        }
        updateSubtitle();
    }

    public void addItem(MapItem item) {
        recyclerViewAdapter.addItem(item);
        recyclerView.scrollToPosition(0);
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

    private void updateSubtitle() {
        TextView subtitle = logView.findViewById(R.id.subtitle);
        subtitle.setText(context.getString(R.string.subtitle, PreferencesResolver.getLogLimit(), sortMode.getLabel()));
    }

    @Override
    protected void disposeImpl() {
        timeUpdater.unregister(this);
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
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
