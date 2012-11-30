package org.phiresearchlab.commsphere.client;

import java.util.ArrayList;
import java.util.List;

import org.phiresearchlab.commsphere.shared.PersonDTO;
import org.phiresearchlab.commsphere.shared.PersonService;
import org.phiresearchlab.commsphere.shared.PersonServiceAsync;
import org.phiresearchlab.commsphere.shared.Role;
import org.phiresearchlab.commsphere.shared.SessionID;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Nov 28, 2011
 *
 */
public class AddNewUserPopup extends PopupPanel
{
    private PersonServiceAsync personService = GWT.create(PersonService.class);
        
    ManageUsersPanel parent;
    private SessionID sessionId;
    private Grid entryTable;
    private Grid groupTable;
    ImageButton addButton;

    public AddNewUserPopup(ManageUsersPanel parent) {
        super(false);
        setGlassEnabled(true);
        
        this.parent = parent;
        
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setWidth("600px");
        mainPanel.add(createHeaderPanel());
        mainPanel.add(createBodyPanel());
        mainPanel.add(createButtonPanel());
        
        addStyleName("addNewUserPopup");
        getElement().getStyle().setBorderWidth(2, Unit.PX);
        getElement().getStyle().setBorderColor("#8DB335");
        setWidget(mainPanel);
    }
    
    public void show(SessionID sid) {
        this.sessionId = sid;
        show();
    }
    
    private void addEntryItem(Grid table, int row, String text) {
        table.setText(row, 0, text);
        TextBox entry = new TextBox();
        entry.setStyleName("newUserEntry");
        entry.setVisibleLength(32);
        entry.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event)
            {
                addButton.setEnabled(isSavable());   
            }            
        });
        
        table.setWidget(row, 1, entry);
    }
    
    private void addUser() {
        String username = getEntryText(2);
        
        personService.checkUsername(sessionId, username, new AsyncCallback<Boolean>() {
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to communicate with the server: " + caught.getMessage());
            }

            public void onSuccess(Boolean available)
            {
                if (!available) {
                    Window.alert("The username you have chosen is already in use.");
                    return;
                }
                
                PersonDTO user = new PersonDTO();
                user.setGivenName(getEntryText(0));
                user.setFamilyName(getEntryText(1));
                user.setUsername(getEntryText(2));
                user.setPassword(getEntryText(3));
                user.setEmailAddress(getEntryText(5));
                
                if (getGroupSelected(0))
                    user.addRole(Role.Analyst);
                
                if (getGroupSelected(1))
                    user.addRole(Role.Coordinator);
                
                if (getGroupSelected(2))
                    user.addRole(Role.Administrator);
             
                personService.createUser(sessionId, user, new AsyncCallback<PersonDTO>() {
                    public void onFailure(Throwable caught)
                    {
                        Window.alert("Failed to create new user: " + caught.getMessage());
                    }

                    public void onSuccess(PersonDTO result)
                    {
                        hide();
                        parent.reload();
                    }
                    
                });
            }            
        });
        
    }
    
    private Panel createBodyPanel() {
        
        HorizontalPanel panel = new HorizontalPanel();
        panel.setSpacing(20);
        panel.add(createEntryPanel());
        panel.add(createGroupPanel());
        
        return panel;
    }
    
    private Panel createButtonPanel() {
        addButton = new ImageButton("images/settings/add_button.png",
                                                "images/settings/add_button_down.png",
                                                "images/settings/add_button_down.png");  
        addButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                addUser();
            }            
        });
        addButton.setDisabledImage("images/settings/add_button_disabled.png");
        addButton.setEnabled(false);

        ImageButton cancelButton = new ImageButton("images/global/popup_cancel_button.png",
                                                   "images/global/popup_cancel_button_down.png",
                                                   "images/global/popup_cancel_button_down.png");  
        cancelButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                hide();
            }            
        });

        HorizontalPanel panel = new HorizontalPanel();
        panel.setStyleName("newUserButtonPanel");
        panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        panel.setSpacing(10);
        panel.add(addButton);
        panel.setCellHorizontalAlignment(addButton, HasHorizontalAlignment.ALIGN_RIGHT);
        panel.add(cancelButton);
        panel.setCellHorizontalAlignment(cancelButton, HasHorizontalAlignment.ALIGN_LEFT);
        
        return panel;
    }
    
    private Widget createEntryPanel() {
        entryTable = new Grid(6, 2);
        entryTable.setStyleName("newUserEntryPanel");
        addEntryItem(entryTable, 0, "First Name:");
        addEntryItem(entryTable, 1, "Last Name:");
        addEntryItem(entryTable, 2, "Username:");
        addEntryItem(entryTable, 3, "Password:");
        entryTable.setText(4, 1, "(case sensitive)");
        addEntryItem(entryTable, 5, "E-mail:");
        
        return entryTable;
    }
        
    private Panel createHeaderPanel() {
        Image header = new Image("images/settings/add_new_user_popup_header.png");
        
        ImageButton closeButton = new ImageButton("images/global/popup_close_button.png",
                                                  "images/global/popup_close_button_down.png",
                                                  "images/global/popup_close_button_down.png");  
        closeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                hide();
            }            
        });
        
        HorizontalPanel panel = new HorizontalPanel();
        panel.setStyleName("newUserHeader");
        panel.add(header);
        panel.setCellHorizontalAlignment(header, HasHorizontalAlignment.ALIGN_LEFT);
        panel.add(closeButton);
        panel.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
        panel.setCellVerticalAlignment(closeButton, HasVerticalAlignment.ALIGN_TOP);
        
        return panel;
    }
    
    private CheckBox createGroupCheckBox() {
        CheckBox cb = new CheckBox();
        
        cb.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                addButton.setEnabled(isSavable());
            }            
        });
        
        return cb;
    }
    
    private Panel createGroupPanel() {
        Label label = new Label("Groups:");
        
        groupTable = new Grid(3, 2);
        groupTable.setWidget(0, 0, createGroupCheckBox());
        groupTable.setText(0, 1, "Analyst");
        groupTable.setWidget(1, 0, createGroupCheckBox());
        groupTable.setText(1, 1, "Coordinator");
        groupTable.setWidget(2, 0, createGroupCheckBox());
        groupTable.setText(2, 1, "Administrator");
        
        VerticalPanel panel = new VerticalPanel();
        panel.setStyleName("newUserGroupPanel");
        panel.add(label);
        panel.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_LEFT);
        panel.add(groupTable);
        panel.setCellHorizontalAlignment(groupTable, HasHorizontalAlignment.ALIGN_CENTER);
        
        return panel;
    }
    
    private String getEntryText(int row) {
        TextBox textBox = (TextBox) entryTable.getWidget(row, 1);
        String text = textBox.getText();
        return null == text ? null : text.trim();
    }
    
    boolean getGroupSelected(int row) {
        CheckBox checkBox = (CheckBox) groupTable.getWidget(row, 0);
        return checkBox.getValue();
    }
    
    private boolean isSavable() {
        List<String> fields = new ArrayList<String>(6);
        fields.add(getEntryText(0));
        fields.add(getEntryText(1));
        fields.add(getEntryText(2));
        fields.add(getEntryText(3));
        fields.add(getEntryText(5));
        
        for (String field: fields)
            if (null == field || field.length() == 0)
                return false;
        
        for (int i = 0; i < 3; i++)
            if (getGroupSelected(i))
                return true;
            
        return false;
    }
}
