package org.phiresearchlab.commsphere.client;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Nov 29, 2011
 *
 */
public class DisplayOptionsPanel extends VerticalPanel
{
    private String[] timePeriods = { "Today", "Last Week", "Last 30 Days" };
    private String[] sortColumns = { "Event", "Report ID", "Event Type", "Day & Time", "Status" };
    
    public DisplayOptionsPanel() {
       Image header = new Image("images/settings/display_options_header.png");
       
       add(header);
       add(createCoordinatorTasksPanel());
       add(createAnalystTasksPanel());
       
       setWidth("700px");
       setHeight("400px");
    }
    
    private Panel createAnalystTasksPanel() {
        Label header = new Label("Analyst Tasks");
        header.setStyleName("displayOptionsLabel");
        
        VerticalPanel panel = new VerticalPanel();
        panel.setStyleName("analystTasksPanel");
        panel.setSpacing(12);
        panel.add(header);
        panel.add(createTimePeriodPanel());
        panel.add(createSortColumnPanel());
        
        return panel;
    }
    
    private Panel createCoordinatorTasksPanel() {
        Label header = new Label("Coordinator Tasks");
        header.setStyleName("displayOptionsLabel");
        
        VerticalPanel panel = new VerticalPanel();
        panel.setStyleName("coordinatorTasksPanel");
        panel.setSpacing(12);
        panel.add(header);
        panel.add(createTimePeriodPanel());
        panel.add(createSortColumnPanel());
        
        return panel;
    }
    
    private Panel createSortColumnPanel() {
        Label label = new Label("Default sort:");
        
        ListBox sortColumn = new ListBox();
        for (String column: sortColumns) 
            sortColumn.addItem(column);
        sortColumn.setVisibleItemCount(1);
        
        HorizontalPanel panel = new HorizontalPanel();
        panel.setSpacing(6);
        panel.add(label);
        panel.setCellHorizontalAlignment(label, ALIGN_RIGHT);
        panel.add(sortColumn);
        panel.setCellHorizontalAlignment(sortColumn, ALIGN_LEFT);
        
        return panel;
    }
    
    private Panel createTimePeriodPanel() {
        Label label = new Label("Default time period:");
        
        ListBox timePeriod = new ListBox();
        for (String period: timePeriods) 
            timePeriod.addItem(period);
        timePeriod.setVisibleItemCount(1);
                
        HorizontalPanel panel = new HorizontalPanel();
        panel.setSpacing(6);
        panel.add(label);
        panel.setCellHorizontalAlignment(label, ALIGN_RIGHT);
        panel.add(timePeriod);
        panel.setCellHorizontalAlignment(timePeriod, ALIGN_LEFT);
        
        return panel;
    }
    
}
