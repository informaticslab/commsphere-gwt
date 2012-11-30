package org.phiresearchlab.commsphere.client;

import org.phiresearchlab.commsphere.shared.PersonDTO;
import org.phiresearchlab.commsphere.shared.SessionID;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Nov 23, 2011
 *
 */
public class SettingsPopup extends PopupPanel implements WorkArea
{
    private CommSphere parent;
    private Masthead masthead;
    private ManageUsersPanel manageUsersPanel;
    private DisplayOptionsPanel displayOptionsPanel;
    private HorizontalPanel bodyPanel;
    
    public SettingsPopup(CommSphere parent) {
        super(false);
        setGlassEnabled(true);
        
        this.parent = parent;
        masthead = new Masthead(this);
        manageUsersPanel = new ManageUsersPanel(masthead.getSessionID());
        manageUsersPanel.addStyleName("manageUsersPanel-body");
        displayOptionsPanel = new DisplayOptionsPanel();
        displayOptionsPanel.addStyleName("displayOptionsPanel-body");
        bodyPanel = createBodyPanel();
        
        Image header = new Image("images/settings/settings_header.png");
        header.setStyleName("settingsHeader");
        
        Widget closeButton = createCloseButton();
        
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setStyleName("mainPanel");
        mainPanel.setWidth("1000px");
        mainPanel.add(masthead);
        mainPanel.add(header);
        mainPanel.add(bodyPanel);
        mainPanel.add(closeButton);
        mainPanel.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
        
        addStyleName("settingsPopup");
        getElement().getStyle().setBorderWidth(0, Unit.PX);
        setWidget(mainPanel);
    }
    
    public void show(SessionID sid, PersonDTO user) {
        super.show();
        masthead.setContext(sid, user);
        manageUsersPanel.show(sid);
    }

    public void clear() {    
     
    }
    
    public void launchSettings() {
        // Do nothing since this is the Settings popup.
    }
    
    private HorizontalPanel createBodyPanel() {
        Panel menuPanel = createMenuPanel();
        menuPanel.setStyleName("settingMenuPanel");
        
        HorizontalPanel panel = new HorizontalPanel();
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        panel.add(menuPanel);
        panel.setCellVerticalAlignment(menuPanel, HasVerticalAlignment.ALIGN_TOP);
        panel.add(manageUsersPanel);
        
        
        return panel;
    }
    
    private Widget createCloseButton() {
        ImageButton closeButton = new ImageButton("images/settings/settings_ok_button.png",
                                                  "images/settings/settings_ok_button_down.png",
                                                  "images/settings/settings_ok_button_down.png");
        closeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                hide();
            }            
        });
        
        return closeButton;
    }
    
    private Panel createMenuPanel() {
        final ImageButton manageUsersButton= new ImageButton("images/settings/manage_users_no_select.png",
                                                       "images/settings/manage_users_select.png",
                                                       "images/settings/manage_users_hover.png");
        final ImageButton displayOptionsButton = new ImageButton("images/settings/display_options_no_select.png",
                                                           "images/settings/display_options_select.png",
                                                           "images/settings/display_options_hover.png");

        manageUsersButton.makeSelectable();
        manageUsersButton.select();
        manageUsersButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                displayOptionsButton.deselect();
                bodyPanel.remove(1);
                bodyPanel.add(manageUsersPanel);
                manageUsersPanel.show(masthead.getSessionID());
            }            
        });
        
        displayOptionsButton.makeSelectable();
        displayOptionsButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                manageUsersButton.deselect();
                bodyPanel.remove(1);
                bodyPanel.add(displayOptionsPanel);
            }            
        });

        VerticalPanel panel = new VerticalPanel();
        panel.add(manageUsersButton);
        panel.add(displayOptionsButton);
        
        return panel;
    }
    
}
