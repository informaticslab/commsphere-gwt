package org.phiresearchlab.commsphere.client;

import java.util.List;

import org.phiresearchlab.commsphere.shared.MediaRealmDTO;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;

public class MediaSelectPopup extends PopupPanel
{

    public MediaSelectPopup() {
        
    }
    
    public void show(List<MediaRealmDTO> realms) {
        
    }
    
    private Panel createHeaderPanel() {
        Label title = new Label("Media Selection");
        HorizontalPanel panel = new HorizontalPanel();
        panel.setWidth("100%");
        panel.setHeight("37px");
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        
        return panel;
    }
}
