package pl.tdf.atak.TAKLog.plugin;

import static pl.tdf.atak.TAKLog.TakLogDropDownReceiver.SHOW_DROP_DOWN;

import android.content.Context;

import com.atak.plugins.impl.AbstractPluginTool;


public class TakLogTool extends AbstractPluginTool {

    public TakLogTool(final Context context) {
        super(context, context.getString(R.string.app_name), context.getString(R.string.app_name), context.getResources().getDrawable(R.drawable.taklog), SHOW_DROP_DOWN);
    }
}
