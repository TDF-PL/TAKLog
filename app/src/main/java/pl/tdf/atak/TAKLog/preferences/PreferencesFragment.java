package pl.tdf.atak.TAKLog.preferences;

import static pl.tdf.atak.TAKLog.preferences.PreferencesKeys.PREFERENCES_EVENTS_LIMIT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import pl.tdf.atak.TAKLog.plugin.R;

import com.atakmap.android.gui.PanEditTextPreference;
import com.atakmap.android.preference.PluginPreferenceFragment;

public class PreferencesFragment extends PluginPreferenceFragment {

    private static Context staticPluginContext;
    public static final String TAG = "TakLogPreferenceFr";

    /**
     * Only will be called after this has been instantiated with the 1-arg constructor.
     * Fragments must has a zero arg constructor.
     */
    public PreferencesFragment() {
        super(staticPluginContext, R.xml.preferences);
    }

    @SuppressLint("ValidFragment")
    public PreferencesFragment(final Context pluginContext) {
        super(pluginContext, R.xml.preferences);
        staticPluginContext = pluginContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PanEditTextPreference limitPreference = (PanEditTextPreference) findPreference(PREFERENCES_EVENTS_LIMIT);
        limitPreference.setValidIntegerRange(1, 1000);
    }
}