package org.phiresearchlab.commsphere.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Nov 14, 2011
 *
 */
public class InfoPopup extends PopupPanel
{
    
    public InfoPopup() {
        this.addStyleName("infoPopup");
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setStyleName("infoPopupMainPanel");
        mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        mainPanel.add(createHeaderPanel());
        mainPanel.add(createTable());
        mainPanel.setWidth("380px");
        
        this.getElement().getStyle().setBorderWidth(2, Unit.PX);
        this.getElement().getStyle().setBorderColor("#8DB335");
        this.setWidget(mainPanel);
    }
    
    private Panel createHeaderPanel() {
        Image icon = new Image("images/tasks/large_info_icon.png");
        icon.setStyleName("infoPopupIcon");
        
        Label text = new Label("The day & time pertain to status.");
        
        HorizontalPanel panel = new HorizontalPanel();
        panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        panel.setStyleName("infoPopupHeader");
        panel.add(icon);
        panel.add(text);
        
        return panel;
    }
    
    private Grid createTable() {
        Grid table = new Grid(5, 2);
        table.setStyleName("infoTable");
        table.setText(0, 0, "Status");
        table.getCellFormatter().setStyleName(0, 0, "infoTableHeader");
        table.setText(0, 1, "Day & time when...");
        table.getCellFormatter().setStyleName(0, 1, "infoTableHeader");

        table.setText(1, 0, "In Development:");
        table.getCellFormatter().setStyleName(1, 0, "infoTableLeftCell");
        table.setText(1, 1, "the coordinator saved the task the he/she is creating.");
        table.getCellFormatter().setStyleName(1, 1, "infoTableRightCell");
        
        table.setText(2, 0, "Not Started:");
        table.getCellFormatter().setStyleName(2, 0, "infoTableLeftCell");
        table.setText(2, 1, "the coordinator published the task.");
        table.getCellFormatter().setStyleName(2, 1, "infoTableRightCell");
        
        table.setText(3, 0, "In Progress:");
        table.getCellFormatter().setStyleName(3, 0, "infoTableLeftCell");
        table.setText(3, 1, "the analyst who most recently worked on the task saved or finished.");
        table.getCellFormatter().setStyleName(3, 1, "infoTableRightCell");
       
        table.setText(4, 0, "Completed:");
        table.getCellFormatter().setStyleName(4, 0, "infoTableLeftCell");
        table.setText(4, 1, "the last analyst to complete the task was finished.");
        table.getCellFormatter().setStyleName(4, 1, "infoTableRightCell");
        
        return table;
    }
    
}
