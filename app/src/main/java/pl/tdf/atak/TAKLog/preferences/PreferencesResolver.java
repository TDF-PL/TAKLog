package pl.tdf.atak.TAKLog.preferences;

import static com.atakmap.android.maps.MapView.getMapView;

import static pl.tdf.atak.TAKLog.preferences.PreferencesKeys.PREFERENCES_EVENTS_LIMIT;

import android.preference.PreferenceManager;

public class PreferencesResolver {

    private static final int logLimitFallback = 100;

    public static int getLogLimit() {
        String limit = PreferenceManager
                .getDefaultSharedPreferences(getMapView().getContext())
                .getString(PREFERENCES_EVENTS_LIMIT, logLimitFallback + "");

        try {
            return Integer.parseInt(limit);
        } catch (Exception e) {
            return logLimitFallback;
        }
    }
}
