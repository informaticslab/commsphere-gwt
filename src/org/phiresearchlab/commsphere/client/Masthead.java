package org.phiresearchlab.commsphere.client;

import org.phiresearchlab.commsphere.shared.PersonDTO;
import org.phiresearchlab.commsphere.shared.PersonService;
import org.phiresearchlab.commsphere.shared.PersonServiceAsync;
import org.phiresearchlab.commsphere.shared.SessionID;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Oct 26, 2011
 *
 */
public class Masthead extends AbsolutePanel
{
    private static SessionID sessionID;
    private static PersonDTO currentUser;

    private PersonServiceAsync personService = GWT.create(PersonService.class);

    private WorkArea workArea;
    private HorizontalPanel greenBar;
    private HorizontalPanel masthead;
    private Label greetingsLabel;
    
    public Masthead(WorkArea workArea) {
        this.workArea = workArea;
        
        Label title = new Label("EOC Communication Surveilance");
        title.setStyleName("headerTitle");
        
        greetingsLabel = new Label("Hello, ");
        greetingsLabel.setStyleName("greetingsLabel");
        Image divider = new Image("images/header/divider.png");
        Label logOutLabel = createLogOutButton();
        ImageButton settingsButton = createSettingsButton();
        ImageButton helpButton = createHelpButton();
        
        HorizontalPanel controlPanel = new HorizontalPanel();
        controlPanel.setSpacing(6);
        controlPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        controlPanel.add(greetingsLabel);
        controlPanel.add(divider);
        controlPanel.add(logOutLabel);
        controlPanel.add(settingsButton);
        controlPanel.add(helpButton);
        
        greenBar = new HorizontalPanel();
        greenBar.setStyleName("greenBar");
        greenBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        greenBar.add(title);
        greenBar.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_LEFT);
        greenBar.add(controlPanel);
        greenBar.setCellHorizontalAlignment(controlPanel, HasHorizontalAlignment.ALIGN_RIGHT);
        
        Image rightEdge = new Image("images/header/right_edge.png");
        
        masthead = new HorizontalPanel();
        masthead.setStyleName("masthead");
        masthead.add(greenBar);
        masthead.add(rightEdge);
        
        Image logo = new Image("images/header/logo.png");
        logo.setStyleName("logo");
        
        this.add(masthead);
        this.add(logo);
    }
    
    PersonDTO getCurrentUser() {
        return currentUser;
    }
    
    SessionID getSessionID() {
        return sessionID;
    }
    
    void launchHelp() {
        Window.alert("The Help screen is not available yet.");
    }
    
    void logout() {
        sessionID = null;
        currentUser = null;
        greetingsLabel.setText("");
        workArea.clear();
    }
    
    void setContext(SessionID sid, PersonDTO user) {
        this.sessionID = sid;
        setCurrentUser(user);
    }
    
    void setSessionId(SessionID sid) {
        sessionID = sid;
        
        personService.getCurrentUser(sid, new AsyncCallback<PersonDTO>() {
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to establish session with server: " + caught.getMessage());
                logout();
            }

             public void onSuccess(PersonDTO person)
             {
                 setCurrentUser(person);
             }
         });
   
    }
    
    private ImageButton createHelpButton() {
        ImageButton help = new ImageButton("images/header/help_icon.png", 
                                           "images/header/help_icon_down.png");
        help.setHoverImage("images/header/help_icon_down.png");
        help.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                launchHelp();
            } 
        });
        
        return help;
    }
    
    private Label createLogOutButton() {
        Label label = new Label("Log out");
        label.setStyleName("logoutButton");
        label.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                logout();
            } 
        });
        
        return label;
    }
    
    private ImageButton createSettingsButton() {
        ImageButton settings = new ImageButton("images/header/settings_icon.png", 
                                               "images/header/settings_icon_down.png");
        settings.setHoverImage("images/header/settings_icon_down.png");
        settings.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                workArea.launchSettings();
            } 
        });
        
        return settings;
    }
    
    private void setCurrentUser(PersonDTO user) {
        currentUser = user;
        greetingsLabel.setText("Hello, " + currentUser.getGivenName() + 
                " " + currentUser.getFamilyName()); 
    }
    
}
