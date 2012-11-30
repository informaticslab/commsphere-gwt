package org.phiresearchlab.commsphere.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.phiresearchlab.commsphere.shared.PersonDTO;
import org.phiresearchlab.commsphere.shared.PersonService;
import org.phiresearchlab.commsphere.shared.PersonServiceAsync;
import org.phiresearchlab.commsphere.shared.Role;
import org.phiresearchlab.commsphere.shared.SessionID;
import org.phiresearchlab.commsphere.shared.TaskDTO;
import org.phiresearchlab.commsphere.shared.TaskService;
import org.phiresearchlab.commsphere.shared.TaskServiceAsync;
import org.phiresearchlab.commsphere.shared.TaskStatus;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CommSphere implements EntryPoint, WorkArea {
    
    private PersonServiceAsync personService = GWT.create(PersonService.class);
    private TaskServiceAsync taskService = GWT.create(TaskService.class);
    
	private VerticalPanel mainPanel;
    private Masthead masthead;   
    private LoginPanel loginPanel;
    private HorizontalPanel newTaskPanel;
    private ImageButton newTaskButton;
    private NewTaskPopup newTaskPopup;
    private DataCollectionPopup dataCollectionPopup;
    private ListBox eventTypeDropDown;
    private ListBox timePeriodDropDown;
    private ListBox statusDropDown;
    private SortableFlexTable tasksTable;
    private Label pageCountLabel;
    private ImageButton leftPageArrow;
    private ImageButton rightPageArrow;
    private InfoPopup infoPopup = new InfoPopup();
    private ProgressPopup progressPopup = new ProgressPopup();
    private SettingsPopup settingsPopup;
    
    private List<String> eventTypesList;
    
	public void onModuleLoad() {
	    loginPanel = new LoginPanel(this);
	    masthead = new Masthead(this);
	    newTaskPopup = new NewTaskPopup(this);
	    dataCollectionPopup = new DataCollectionPopup(this);
	    settingsPopup = new SettingsPopup(this);
	    
        mainPanel = new VerticalPanel();
        mainPanel.setStyleName("mainPanel");
        mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        mainPanel.add(masthead);
        mainPanel.setCellHeight(masthead, "70px");
        mainPanel.add(createTasksPanel());   
        mainPanel.add(createPageControls());
        
        loginPanel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                int left = offsetWidth < 100 ? 250 : (Window.getClientWidth() - offsetWidth) / 2;
                int top = offsetHeight < 100 ? 125 : (Window.getClientHeight() - offsetHeight) / 4;
                loginPanel.setPopupPosition(left, top);
            }
        });
	}
	
	public void clear() {
        clearTaskList();
        RootLayoutPanel.get().remove(mainPanel);

        loginPanel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                int left = (Window.getClientWidth() - offsetWidth) / 2;
                int top = (Window.getClientHeight() - offsetHeight) / 4;
                loginPanel.setPopupPosition(left, top);
            }
        });
	}
	
	void launchCreateReport(final TaskDTO task) {
	    Window.alert("The Create Report screen is not implemented yet");
	}
	
	void launchDataCollectionPanel(final TaskDTO task) {
        dataCollectionPopup.center();
        dataCollectionPopup.show(masthead.getSessionID(), masthead.getCurrentUser(), task);
	}
	
	void launchNewTaskPanel(final TaskDTO task) {
	    personService.getAllAnalysts(masthead.getSessionID(), new AsyncCallback<List<PersonDTO>>() {
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to get list of analysts from server: " + caught.getMessage());
            }

            public void onSuccess(List<PersonDTO> result)
            {
                newTaskPopup.setLists(eventTypesList, result);
                newTaskPopup.center();
                newTaskPopup.show(masthead.getSessionID(), masthead.getCurrentUser(), task);
            }	        
	    });
	}
	
	void launchProgressPanel(TaskDTO task) {
	    progressPopup.center();
	    progressPopup.show(task);
	}
	
    public void launchSettings() {
        settingsPopup.center();
        settingsPopup.show(masthead.getSessionID(), masthead.getCurrentUser());
    }
    
	void setSessionId(final SessionID sid) {
        personService.getCurrentUser(sid, new AsyncCallback<PersonDTO>() {
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to establish session with server: " + caught.getMessage());
            }

             public void onSuccess(PersonDTO person)
             {
                 masthead.setContext(sid, person);
                 
                 if (person.hasRole(Role.Analyst)) {
                     newTaskPanel.remove(1);
                 } else if (newTaskPanel.getWidgetCount() < 2) {
                     newTaskPanel.add(newTaskButton);
                 }

                 RootLayoutPanel.get().add(mainPanel);
                 initLists();
                 showDefaultTaskList();
             }
         });
	}
	
    void update() {
        if (null== masthead.getSessionID()) 
            return;
        
        String eventType = getDropDownValue(eventTypeDropDown);
        String timePeriod = getDropDownValue(timePeriodDropDown);
        String status = getDropDownValue(statusDropDown);
        searchForTasks(eventType, timePeriod, status);
        getEventTypes();
    }
    
	private void clearTaskList() {
	    // TODO: implement
	}
	
	private ListBox createDropDownList() {
        ListBox listBox = new ListBox();
        listBox.setStyleName("dropDownFilter");
        listBox.setVisibleItemCount(1);
        return listBox;
	}
	
	private Image createInfoIcon() {
        Image infoImage = new Image("images/tasks/info_icon.png");
        infoImage.addMouseOverHandler(new MouseOverHandler() {
            public void onMouseOver(MouseOverEvent event)
            {
                final int left = event.getClientX() - 150;
                final int top = event.getClientY() + 25;
                infoPopup.setPopupPositionAndShow(new PositionCallback() {
                    public void setPosition(int offsetWidth, int offsetHeight) {
                        infoPopup.setPopupPosition(left, top);
                    }
                });
            }            
        });
        infoImage.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event)
            {
                infoPopup.hide(); 
            }            
        });

        return infoImage;
	}
	
	private Panel createPageControls() {
        pageCountLabel = new Label("0-0 of 0");
        pageCountLabel.setStyleName("pageCountLabel");
        
        leftPageArrow = new ImageButton("images/tasks/table_left_arrow.png",
                                        "images/tasks/table_left_arrow_down.png",
                                        "images/tasks/table_left_arrow_down.png");
        leftPageArrow.setDisabledImage("images/tasks/table_left_arrow_disabled.png");
        leftPageArrow.setEnabled(false);
        rightPageArrow = new ImageButton("images/tasks/table_right_arrow.png",
                                         "images/tasks/table_right_arrow_down.png",
                                         "images/tasks/table_right_arrow_down.png");
        rightPageArrow.setDisabledImage("images/tasks/table_right_arrow_disabled.png");
        rightPageArrow.setEnabled(false);
        
        HorizontalPanel controls = new HorizontalPanel();
        controls.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        controls.setSpacing(8);
        controls.add(pageCountLabel);
        controls.add(leftPageArrow);
        controls.add(rightPageArrow);

        HorizontalPanel panel = new HorizontalPanel();
	    panel.setWidth("100%");
	    panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	    panel.add(controls);
	    
	    return panel;
	}
	
    private Label createReportLabel(String text) {
        Label label = new Label(text);
        label.setStyleName("tasksTableLink");
        label.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                Window.alert("Not implemented yet");   
            }           
        });
        return label;
    }
    
	private Label createReportIdLabel(final TaskDTO task) {
	    Label label = new Label(task.getReportId());
	    label.setStyleName("tasksTableLink");
	    label.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                if (masthead.getCurrentUser().hasRole(Role.Coordinator)) {
                    launchNewTaskPanel(task);
                    return;
                }
                
                if (masthead.getCurrentUser().hasRole(Role.Analyst))
                    launchDataCollectionPanel(task);
            }	        
	    });
	    return label;
	}
	
    private Label createStatusLabel(final TaskDTO task) {
        Label label = new Label(task.getStatus().toString());
        label.setStyleName("tasksTableLink");
        label.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                if (masthead.getCurrentUser().hasRole(Role.Coordinator))
                    if (task.getStatus() == TaskStatus.InDevelopment)
                        launchNewTaskPanel(task);   
                    else
                        launchProgressPanel(task);
                
                if (masthead.getCurrentUser().hasRole(Role.Analyst))
                    launchDataCollectionPanel(task);
            }           
        });
        return label;
    }
    
    private Panel createTasksDropDownPanel(String name, ListBox listBox) {
        Label label = new Label(name);
        label.setStyleName("dropDownFilterLabel");
        HorizontalPanel panel = new HorizontalPanel();
        panel.setStyleName("dropDownPanel");
        panel.setSpacing(4);
        panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        panel.add(label);
        panel.add(listBox);
        return panel;
    }
    
    private Panel createTasksFilterPanel() {
        ChangeHandler changeHandler = new ChangeHandler() {
            public void onChange(ChangeEvent event)
            {
                String eventType = getDropDownValue(eventTypeDropDown);
                String timePeriod = getDropDownValue(timePeriodDropDown);
                String status = getDropDownValue(statusDropDown);
                searchForTasks(eventType, timePeriod, status);
            }      
        };
        
        eventTypeDropDown = createDropDownList();
        eventTypeDropDown.addChangeHandler(changeHandler);
        
        timePeriodDropDown = createDropDownList();
        timePeriodDropDown.addChangeHandler(changeHandler);
        
        statusDropDown = createDropDownList();
        statusDropDown.addChangeHandler(changeHandler);
        
        Image searchImage = new Image("images/filter/search_icon.png");
        
        TextBox searchBox = new TextBox();
        searchBox.setStyleName("searchBoxEmpty");
        searchBox.setText("Search tasks");
        
        HorizontalPanel searchPanel = new HorizontalPanel();
        searchPanel.setStyleName("searchPanel");
        searchPanel.setSpacing(6);
        searchPanel.add(searchImage);
        searchPanel.add(searchBox);
        searchPanel.setCellVerticalAlignment(searchBox, HasVerticalAlignment.ALIGN_MIDDLE);
        
        Image leftEdge = new Image("images/filter/filter_bkgd_left_edge.png");
        leftEdge.setStyleName("filterPanelLeftEdge");
        Image rightEdge = new Image("images/filter/filter_bkgd_right_edge.png");
        rightEdge.setStyleName("filterPanelRightEdge");
        
        HorizontalPanel filterBar = new HorizontalPanel();
        filterBar.setStyleName("filterBar");
        filterBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        filterBar.add(createTasksDropDownPanel("Event type:", eventTypeDropDown));
        filterBar.add(new Image("images/tasks/filter_divider.png"));
        filterBar.add(createTasksDropDownPanel("Time period:", timePeriodDropDown));
        filterBar.add(new Image("images/filter/filter_divider.png"));
        filterBar.add(createTasksDropDownPanel("Status:", statusDropDown));
        
        HorizontalPanel panel = new HorizontalPanel();
        panel.setStyleName("tasksFilterPanel");
        panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        panel.add(leftEdge);
        panel.add(filterBar);
        
        AbsolutePanel container = new AbsolutePanel();
        container.add(panel);
        container.add(rightEdge);
        container.add(searchPanel);
        
        return container;
    }
    
    private Panel createTasksPanel() {
        VerticalPanel panel = new VerticalPanel();
        panel.setStyleName("tasksPanel");
        panel.add(createTasksPanelHeader());
        panel.add(createTasksFilterPanel());
        panel.add(createTasksTable());
        
        return panel;
    }
    
    private Panel createTasksPanelHeader() {
        Image tasksLabel = new Image("images/tasks/tasks_page_header.png");
        newTaskButton = new ImageButton("images/tasks/create_new_task_button.png",
                                        "images/tasks/create_new_task_button_down.png",
                                        "images/tasks/create_new_task_button_down.png");
        newTaskButton.addStyleName("newTaskButton");
        newTaskButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                launchNewTaskPanel(null);
            } 
        });
        
        newTaskPanel = new HorizontalPanel();
        newTaskPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        newTaskPanel.add(tasksLabel);
        newTaskPanel.setCellHorizontalAlignment(tasksLabel, HasHorizontalAlignment.ALIGN_LEFT);
        newTaskPanel.add(newTaskButton);
        
        return newTaskPanel;
    }
    
    private Panel createTasksTable() {        
        Comparator<Object> columnComparator = new ColumnComparator();
        tasksTable = new SortableFlexTable();
        tasksTable.addStyleName("tasksTable");
        tasksTable.addColumn("Event", 25, columnComparator);
        tasksTable.addColumn("Report ID", 10, columnComparator);
        tasksTable.addColumn("Event Type", 20, columnComparator);
        tasksTable.addColumn("Day & Time", createInfoIcon(), 18, columnComparator);
        tasksTable.addColumn("Status", createInfoIcon(), 15, columnComparator);
        tasksTable.addColumn("Report", 12, columnComparator);
        
        return tasksTable;
    }
    
    private String getDropDownValue(ListBox dropDown) {
        return dropDown.getValue(dropDown.getSelectedIndex());
    }
    
    private void getEventTypes() {
        taskService.getEventTypes(masthead.getSessionID(), new AsyncCallback<List<String>>() {
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to get list of event types from server: " + caught.getMessage());   
            }

            public void onSuccess(List<String> result)
            {
                eventTypesList = result;
                eventTypeDropDown.clear();
                eventTypeDropDown.addItem("All", "All");
                for (String eventType: result)
                    eventTypeDropDown.addItem(eventType, eventType);
                
                eventTypeDropDown.addItem("Add new...");
            }            
        });
    }
    
    private void initLists() {
        getEventTypes();
        
        timePeriodDropDown.clear();
        timePeriodDropDown.addItem("Today", "Today");
        timePeriodDropDown.addItem("Last Week", "Last Week");
        timePeriodDropDown.addItem("Last 30 Days", "Last 30 Days");
        
        statusDropDown.clear();
        statusDropDown.addItem("All", "All");
        statusDropDown.addItem("Not Started", "NotStarted");
        statusDropDown.addItem("Complete", "Complete");
        statusDropDown.addItem("In Development", "InDevelopment");
        statusDropDown.addItem("In Progress", "InProgress");
    }
    
    private DateTimeFormat formatter = DateTimeFormat.getFormat("MM/dd/yyyy h:mm a");

    private void searchForTasks(String eventType, String timePeriod, String status) {
        Date since = timePeriodToDate(timePeriod);
        
        taskService.findTasksByType(masthead.getSessionID(), eventType, since, status, new AsyncCallback<List<TaskDTO>>() {
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to get list of tasks: " + caught.getMessage());
            }

            public void onSuccess(List<TaskDTO> taskList)
            {
                tasksTable.clear();
                
                
                for (TaskDTO task: taskList) {
                    List<Object> row = new ArrayList<Object>(7);
                    row.add(task.getEventName());
                    row.add(createReportIdLabel(task));
                    row.add(task.getEventType());
                    row.add(formatter.format(task.getLastActivity()));
                    row.add(createStatusLabel(task));
                    if (task.getStatus() == TaskStatus.Complete)
                        if (task.hasReport())
                            row.add(createReportLabel("Create Report"));
                        else
                            row.add(createReportLabel("View Report"));
                    else
                        row.add("N/A");
                    tasksTable.addRow(row);
                }
                
                tasksTable.render();
                int first = taskList.size() == 0 ? 0 : 1;
                pageCountLabel.setText(first + "-" + taskList.size() + " of " + taskList.size());
            }            
        });   
    }
    
    private void setPageControls() {
        int total = tasksTable.getRowCount();
        int page = tasksTable.getCurrentPage();
        int pageSize = tasksTable.getPageSize();
        int totalPages = total / pageSize;
        int start = pageSize * (page - 1) + 1;
        int end = total < pageSize ? total : start + pageSize - 1;
        
        pageCountLabel.setText(start + "-" + end + " of " + total);
        leftPageArrow.setEnabled(page > 1);
        rightPageArrow.setEnabled(page <= totalPages);
    }
    
	private void showDefaultTaskList() {
	    searchForTasks("All", "Today", "All");
	}
	
	@SuppressWarnings("deprecation")
    private Date timePeriodToDate(String timePeriod) {
        Date now = new Date();
        int dayOfMonth = now.getDate();
        int month = now.getMonth();
        int year = now.getYear();        
        
        if ("Last 30 Days".equals(timePeriod)) {
            if (0 == month) {
                month = 11;
                year--;
            } else {
                month--;
            }            
        }
        
        if ("Last Week".equals(timePeriod)) {
            if (dayOfMonth > 7) {
                dayOfMonth -= 7;
            } else {
                if (0 == month) {
                    month = 11;
                    year--;
                } else {
                    month--;
                }
            }
        }
        
        return new Date(year, month, dayOfMonth);
	}
	
	private class ColumnComparator implements Comparator<Object> {

        @Override
        public int compare(Object first, Object second)
        {
            if (first instanceof String) {
                String one = (String) first;
                String two = (String) second;
                if (one.contains("AM") || one.contains("PM")) {
                    Date firstDate = formatter.parse(one);
                    Date secondDate = formatter.parse(two);
                    return firstDate.compareTo(secondDate);
                }
                return one.compareTo(two);
            }
            
            Label one = (Label) first;
            Label two = (Label) second;
            return one.getText().compareTo(two.getText());
        }
	};
	
}
