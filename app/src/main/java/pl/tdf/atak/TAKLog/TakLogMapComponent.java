package pl.tdf.atak.TAKLog;

import static pl.tdf.atak.TAKLog.TakLogCotDetailHandler.*;
import static pl.tdf.atak.TAKLog.TakLogDropDownReceiver.SHOW_DROP_DOWN;
import static pl.tdf.atak.TAKLog.preferences.PreferencesKeys.PREFERENCES_KEY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.atakmap.android.cot.detail.CotDetailHandler;
import com.atakmap.android.cot.detail.CotDetailManager;
import com.atakmap.android.dropdown.DropDownMapComponent;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;
import com.atakmap.app.preferences.ToolsPreferenceFragment;

import pl.tdf.atak.TAKLog.plugin.R;
import pl.tdf.atak.TAKLog.preferences.PreferencesFragment;


public class TakLogMapComponent extends DropDownMapComponent {

    private TakLogDropDownReceiver takLogDropDownReceiver;
    private CotDetailHandler handler;


    public void onCreate(final Context context, Intent intent, final MapView view) {
        context.setTheme(R.style.ATAKPluginTheme);

        AtakBroadcast.DocumentedIntentFilter ddFilter = new AtakBroadcast.DocumentedIntentFilter();
        ddFilter.addAction(SHOW_DROP_DOWN, "Show the logger drop-down");
        takLogDropDownReceiver = new TakLogDropDownReceiver(view, context);
        this.registerDropDownReceiver(takLogDropDownReceiver, ddFilter);

        handler = new TakLogCotDetailHandler(TAK_LOG_COT_HANDLER_NAME, takLogDropDownReceiver);
        CotDetailManager.getInstance().registerHandler(handler);
        registerPreferences(context);
    }

    private void registerPreferences(final Context context) {
        ToolsPreferenceFragment.register(new ToolsPreferenceFragment.ToolPreference("TakLog Preferences", "TakLog Preferences", PREFERENCES_KEY, context.getResources().getDrawable(R.drawable.taklog, null), new PreferencesFragment(context)));
    }

    @Override
    protected void onDestroyImpl(Context context, MapView view) {
        takLogDropDownReceiver.closeDropDown();
        CotDetailManager.getInstance().unregisterHandler(handler);
        ToolsPreferenceFragment.unregister(PREFERENCES_KEY);
    }
}