package pl.tdf.atak.TAKLog.preferences;

import static pl.tdf.atak.TAKLog.preferences.PreferencesKeys.PREFERENCES_EVENTS_LIMIT;

import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {
    public interface Callback {
        void execute();
    }

    private static final String TAK = "TakLogPrefsChangedLis";
    private final Callback callback;

    public SharedPreferenceChangeListener(Callback callback) {
        this.callback = callback;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PREFERENCES_EVENTS_LIMIT)) {
            Log.d(TAK, PREFERENCES_EVENTS_LIMIT + " changed");
            callback.execute();
        }
    }
}
