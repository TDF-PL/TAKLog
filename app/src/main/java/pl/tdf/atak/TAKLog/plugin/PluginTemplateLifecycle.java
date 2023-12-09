package pl.tdf.atak.TAKLog.plugin;


import com.atak.plugins.impl.AbstractPlugin;
import gov.tak.api.plugin.IServiceController;
import com.atak.plugins.impl.PluginContextProvider;
import pl.tdf.atak.TAKLog.TakLogMapComponent;


public class PluginTemplateLifecycle extends AbstractPlugin {

   public PluginTemplateLifecycle(IServiceController serviceController) {
        super(serviceController, new TakLogTool(serviceController.getService(PluginContextProvider.class).getPluginContext()), new TakLogMapComponent());
        PluginNativeLoader.init(serviceController.getService(PluginContextProvider.class).getPluginContext());
    }
}

