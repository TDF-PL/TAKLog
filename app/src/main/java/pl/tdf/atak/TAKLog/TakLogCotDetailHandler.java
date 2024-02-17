package pl.tdf.atak.TAKLog;

import com.atakmap.android.cot.detail.CotDetailHandler;
import com.atakmap.android.maps.MapItem;
import com.atakmap.comms.CommsMapComponent;
import com.atakmap.coremap.cot.event.CotDetail;
import com.atakmap.coremap.cot.event.CotEvent;

public class TakLogCotDetailHandler extends CotDetailHandler {
    public static final String TAK_LOG_COT_HANDLER_NAME = "pl.tdf.atak.TAKLog.TakLogCotDetailHandler";
    private final TakLogDropDownReceiver takLogDropDownReceiver;

    protected TakLogCotDetailHandler(String detailName, TakLogDropDownReceiver takLogDropDownReceiver) {
        super(detailName);
        this.takLogDropDownReceiver = takLogDropDownReceiver;
    }

    @Override
    public CommsMapComponent.ImportResult toItemMetadata(MapItem mapItem, CotEvent cotEvent, CotDetail cotDetail) {
        return CommsMapComponent.ImportResult.SUCCESS;
    }

    @Override
    public boolean toCotDetail(MapItem mapItem, CotEvent cotEvent, CotDetail cotDetail) {
        if (mapItem != null && mapItem.getTitle() != null) {
            takLogDropDownReceiver.addItem(mapItem);
        }
        return true;
    }

}
