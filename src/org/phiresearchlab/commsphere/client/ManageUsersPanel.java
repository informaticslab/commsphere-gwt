package org.phiresearchlab.commsphere.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import org.phiresearchlab.commsphere.shared.PersonDTO;
import org.phiresearchlab.commsphere.shared.PersonService;
import org.phiresearchlab.commsphere.shared.PersonServiceAsync;
import org.phiresearchlab.commsphere.shared.Role;
import org.phiresearchlab.commsphere.shared.SessionID;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Nov 23, 2011
 *
 */
public class ManageUsersPanel extends VerticalPanel
{
    
    private static String[] groups = { "All", "Administrator", "Analyst", "Coordinator" };
    
    private PersonServiceAsync personService = GWT.create(PersonService.class);
    
    private SessionID sessionId;
    private SortableFlexTable userListTable;
    private AddNewUserPopup newUserPopup;

    public ManageUsersPanel(SessionID sid) {
        this.sessionId = sid;
        this.newUserPopup  = new AddNewUserPopup(this);
        
        Image header = new Image("images/settings/manage_users_header.png");   
        header.setStyleName("manageUsersHeader");
        
        userListTable = createUserListTable();
        userListTable.addStyleName("userListTable");

        add(header);
        add(createManageUsersSubheader());
        add(userListTable);
        Widget deleteButton = createDeleteButton();
        add(deleteButton);
        setCellHorizontalAlignment(deleteButton, HasHorizontalAlignment.ALIGN_LEFT);
        add(createSpacer());
        
        setWidth("700px");
        setHeight("400px");
    }
    
    public void reload() {
        filterUsers("All");
    }
    
    public void show(SessionID sid) {
        this.sessionId = sid;
        filterUsers("All");
    }
    
    private Widget createDeleteButton() {
        ImageButton deleteButton = new ImageButton("images/settings/settings_delete.png",
                                                   "images/settings/settings_delete_down.png",
                                                   "images/settings/settings_delete_down.png");
        
        deleteButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                deleteSelected();
            }            
        });
        
        return deleteButton;
    }

    private Panel createManageUsersSubheader() {
        Panel viewGroup = createViewGroupPanel();
        
        ImageButton addButton = new ImageButton("images/settings/add_new_user_button.png",
                                                "images/settings/add_new_user_button_down.png",
                                                "images/settings/add_new_user_button_down.png");
        
        addButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                newUserPopup.center();
                newUserPopup.show(sessionId);
            }            
        });
        
        HorizontalPanel panel = new HorizontalPanel();
        panel.setWidth("100%");
        panel.add(viewGroup);
        panel.setCellHorizontalAlignment(viewGroup, HasHorizontalAlignment.ALIGN_LEFT);
        panel.add(addButton);
        panel.setCellHorizontalAlignment(addButton, HasHorizontalAlignment.ALIGN_RIGHT);
        
        return panel;
    }
    
    private Widget createSpacer() {
        HorizontalPanel panel = new HorizontalPanel();
        panel.setHeight("10px");
        return panel;
    }
    
    private SortableFlexTable createUserListTable() {
        SortableFlexTable table = new SortableFlexTable();
        table.makeSelectable(true);
        table.addColumn("Name", 20);
        table.addColumn("User Name", 15);
        table.addColumn("E-mail", 15);
        table.addColumn("Group(s)", 20);
        table.addColumn("Last Visit", 30);
        
        return table;
    }
    
    private Panel createViewGroupPanel() {
        Label label = new Label("View group:");
        
        ListBox filter = new ListBox();
        
        for (String group: groups)
            filter.addItem(group);
        
        filter.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event)
            {
                ListBox lb = (ListBox) event.getSource();
                String group = lb.getItemText(lb.getSelectedIndex());
                filterUsers(group);
            }            
        });
        
        HorizontalPanel panel = new HorizontalPanel();
        panel.setSpacing(6);
        panel.add(label);
        panel.add(filter);
        
        return panel;
    }
    
    private void deleteSelected() {
        Stack<Integer> deleteStack = new Stack<Integer>();
        
        for (int i = 0; i < userListTable.getRowCount(); i++) 
            if (userListTable.isSelected(i))
                deleteStack.push(i);
        
        while (!deleteStack.empty()) {
            Integer row = deleteStack.pop();
            userListTable.deleteRow(row);
        }
            
        // TODO: actually delete users.
    }
    
    private DateTimeFormat formatter = DateTimeFormat.getFormat("MM/dd/yyyy h:mm a");
    
    private void filterUsers(String roleName) {
        personService.getUsersByRole(sessionId, roleName, new AsyncCallback<List<PersonDTO>>() {
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to get list of users from server: " + caught.getMessage());
            }

            public void onSuccess(List<PersonDTO> users)
            {
                userListTable.clear();
                
                Date now = new Date();
                for (PersonDTO dto: users) {
                    List<Object> row = new ArrayList<Object>(6);
                    row.add(dto.getGivenName() + " " + dto.getFamilyName());
                    row.add(dto.getUsername());
                    row.add(dto.getEmailAddress());
                    StringBuffer buffer = new StringBuffer();
                    boolean first = true;
                    for (Role role: dto.getRoles()) {
                        if (!first)
                            buffer.append(", ");
                        buffer.append(role.name());
                        first = false;
                    }
                    row.add(buffer.toString());
                    row.add(formatter.format(now));
                    userListTable.addRow(row);
                }
                
                userListTable.render();
            }            
        });
    }

}
