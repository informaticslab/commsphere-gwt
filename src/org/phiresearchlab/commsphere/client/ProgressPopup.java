package org.phiresearchlab.commsphere.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.phiresearchlab.commsphere.shared.MediaRealmDTO;
import org.phiresearchlab.commsphere.shared.TaskDTO;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Nov 15, 2011
 *
 */
public class ProgressPopup extends PopupPanel
{
    private Label titleLabel;
    private Label publishedLabel;
    private SortableFlexTable table;

    public ProgressPopup() {
        
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setWidth("800px");
        mainPanel.add(createHeaderPanel());
        mainPanel.add(createTable());
        mainPanel.add(createFooterPanel());
        
        this.getElement().getStyle().setBorderWidth(2, Unit.PX);
        this.getElement().getStyle().setBorderColor("#8DB335");
        this.setWidget(mainPanel);
    }
    
    private DateTimeFormat formatter = DateTimeFormat.getFormat("MM/dd/yyyy HH:mm a");
    
    public void show(TaskDTO task) {
        titleLabel.setText("Details for Report " + task.getReportId());
        publishedLabel.setText("Published: " + formatter.format(task.getCreated()));
        
        table.clear();
        
        for (MediaRealmDTO realm: task.getMediaRealms()) {
            List<Object> row = new ArrayList<Object>(7);
            row.add(realm.getAnalyst().getGivenName() + " " + realm.getAnalyst().getFamilyName());
            row.add(realm.getTitle());
            Date started = realm.getStarted();
            Date completed = realm.getCompleted();
            CheckBox checkBox = new CheckBox();
            checkBox.setValue(false);
            checkBox.setEnabled(false);
            
            if (null == started) {
                row.add("Not Started");
                row.add("N/A");
                row.add("N/A");
           } else if (null == completed) {
                row.add("In Progress");
                row.add(formatter.format(started));
                row.add("N/A");
            } else {
                row.add("Completed");
                row.add(formatter.format(started));
                row.add(formatter.format(completed));
                checkBox.setEnabled(true);
                checkBox.setValue(true);
            }
            
            row.add(checkBox);
            table.addRow(row);
        }
        
        table.render();
        show();
    }
    
    private Panel createHeaderPanel() {
        titleLabel = new Label("Details for Report ");
        titleLabel.setStyleName("progressTitle");
        publishedLabel = new Label("Published: ");
        publishedLabel.setStyleName("progressPopupPublished");
        
        HorizontalPanel leftPanel = new HorizontalPanel();
        leftPanel.setSpacing(10);
        leftPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        leftPanel.add(new Image("images/tasks/details_statusPopup_icon.png"));
        leftPanel.add(titleLabel);
        leftPanel.add(new Image("images/tasks/divider_details_statusPopup.png"));
        leftPanel.add(publishedLabel);
       
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
        panel.setWidth("100%");
        panel.add(leftPanel);
        panel.setCellHorizontalAlignment(leftPanel, HasHorizontalAlignment.ALIGN_LEFT);
        panel.add(closeButton);
        panel.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
        panel.setCellVerticalAlignment(closeButton, HasVerticalAlignment.ALIGN_TOP);
        
        return panel;
    }
    
    private Panel createFooterPanel() {
        ImageButton okButton = new ImageButton("images/global/popup_OK_button.png",
                                               "images/global/popup_OK_button_down.png",
                                                "images/global/popup_OK_button_down.png");
        okButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                hide();
            }
        });
        
        HorizontalPanel panel = new HorizontalPanel();
        panel.setStyleName("progressFooter");
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        panel.add(okButton);
        
        return panel;
    }
    
    private Widget createTable() {
        table = new SortableFlexTable();
        table.addStyleName("progressTable");
        table.addColumn("User", 15);
        table.addColumn("Media", 15);
        table.addColumn("Status", 15);
        table.addColumn("Started", 20);
        table.addColumn("Completed", 20);
        table.addColumn("Locked", 15);
        
        AbsolutePanel wrapper = new AbsolutePanel();
        wrapper.setStyleName("progressTableWrapper");
        wrapper.add(table);
        
        return wrapper;
    }
}
