package pl.tdf.atak.TAKLog;

import static pl.tdf.atak.TAKLog.TakLogCotDetailHandler.*;
import static pl.tdf.atak.TAKLog.TakLogDropDownReceiver.SHOW_DROP_DOWN;

import android.content.Context;
import android.content.Intent;

import com.atakmap.android.cot.detail.CotDetailHandler;
import com.atakmap.android.cot.detail.CotDetailManager;
import com.atakmap.android.dropdown.DropDownMapComponent;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;

import pl.tdf.atak.TAKLog.plugin.R;


public class TakLogMapComponent extends DropDownMapComponent {

    private static final String TAG = "TakLogMapComponent";
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
    }

    @Override
    protected void onDestroyImpl(Context context, MapView view) {
        takLogDropDownReceiver.closeDropDown();
        CotDetailManager.getInstance().unregisterHandler(handler);
    }
}