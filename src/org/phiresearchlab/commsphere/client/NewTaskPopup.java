package org.phiresearchlab.commsphere.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.phiresearchlab.commsphere.shared.CategoryDTO;
import org.phiresearchlab.commsphere.shared.DataItemDTO;
import org.phiresearchlab.commsphere.shared.MediaRealmDTO;
import org.phiresearchlab.commsphere.shared.PersonDTO;
import org.phiresearchlab.commsphere.shared.SessionID;
import org.phiresearchlab.commsphere.shared.SubcategoryDTO;
import org.phiresearchlab.commsphere.shared.TaskDTO;
import org.phiresearchlab.commsphere.shared.TaskService;
import org.phiresearchlab.commsphere.shared.TaskServiceAsync;
import org.phiresearchlab.commsphere.shared.TaskStatus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Nov 1, 2011
 *
 */
public class NewTaskPopup extends PopupPanel implements WorkArea
{
    private static final String SELECT_EVENT_TYPE = "Select an event type";
    private static final String SELECT_AN_ANALYST ="Select an analyst";
    private static final String ADD_NEW = "Add new...";

    private TaskServiceAsync taskService = GWT.create(TaskService.class);

    private CommSphere parent;
    private Masthead masthead;   
    private ListBox eventTypeList;
    private TextBox eventNameInput;
    private Label reportIdLabel;
    private List<ListBox> analystDropDowns = new ArrayList<ListBox>();
    private List<FlexTable> categoryTables = new ArrayList<FlexTable>();
    private FlexTable dataItemsTable;
    private List<ImageButton> saveButtons = new ArrayList<ImageButton>();
    private List<ImageButton> closeButtons = new ArrayList<ImageButton>();
    private List<ImageButton> publishButtons = new ArrayList<ImageButton>();
    
    private List<MediaTab> mediaTabs = new ArrayList<MediaTab>(4);
    private TaskDTO currentTask;
    
    private List<PersonDTO> analystList;
    private boolean dirty = false;
    
    public NewTaskPopup(CommSphere parent) {
        super(false);
        setGlassEnabled(true);
        
        this.parent = parent;
        this.masthead = new Masthead(this);
        
        Image header = new Image("images/tasks/new/header.png");
        
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setStyleName("mainPanel");
        mainPanel.setWidth("1000px");
        mainPanel.add(masthead);
        mainPanel.add(createButtonPanel());
        mainPanel.add(header);
        mainPanel.setCellHorizontalAlignment(header, HasHorizontalAlignment.ALIGN_LEFT);
        mainPanel.add(createTypeNamePanel());
        mainPanel.add(createMediaTabPanel());
        mainPanel.add(createButtonPanel());
        
        this.addStyleName("newTaskPopup");
        this.getElement().getStyle().setBorderWidth(0, Unit.PX);
        this.setWidget(mainPanel);
    }
    
    public void clear() {
        if (close())
            parent.clear();
    }
    
    public void launchSettings() {
        // Nothing for now.
    }
    
    void setLists(List<String >eventTypeList, List<PersonDTO> analystList) {
        setEventTypeList(eventTypeList);
        setAnalystLists(analystList);
    }
    
    void show(SessionID sid, PersonDTO user, TaskDTO task) {
        currentTask = task;
        masthead.setContext(sid, user);
        eventNameInput.setText("");
        for (MediaTab tab: mediaTabs) 
            tab.clear();
        populate(task);
        show();
    }
    
    private void addDataItem(FlexTable dataItems, String title, String details) {
        ChangeHandler handler = new ChangeHandler() {
            public void onChange(ChangeEvent event)
            {
                markAsDirty(true);
            }            
        };
        
        TextBox item = new TextBox();
        item.setVisibleLength(40);
        item.setMaxLength(40);
        item.setText(title);
        item.addChangeHandler(handler);
        
        TextBox detail = new TextBox();
        detail.setVisibleLength(40);
        detail.setMaxLength(40);
        detail.setText(details);
        detail.addChangeHandler(handler);
        
        TextBox number = new TextBox();
        number.setVisibleLength(10);
        number.setMaxLength(10);
        number.setEnabled(false);
        
        TextBox change = new TextBox();
        change.setVisibleLength(10);
        change.setMaxLength(10);
        change.setEnabled(false);
        
        int row = dataItems.getRowCount() - 1;                
        ImageButton deleteButton = createDeleteButton(dataItems, row);
       
        dataItems.insertRow(row);
        dataItems.setWidget(row, 1, item);
        dataItems.setWidget(row, 2, detail);
        dataItems.setWidget(row, 3, number);
        dataItems.setWidget(row, 4, change);
        dataItems.setWidget(row, 5, deleteButton);
        
        if (1 < row) {
            ImageButton upArrow = createUpArrow(dataItems, row, 1);
            dataItems.setWidget(row, 0, upArrow);
        }
    }
    
    private void addSubcategory(FlexTable subcategories, String value) {
        ChangeHandler handler = new ChangeHandler() {
            public void onChange(ChangeEvent event)
            {
                markAsDirty(true);
            }            
        };
        
        TextBox subcategory = new TextBox();
        subcategory.setStyleName("subcategory");
        subcategory.setVisibleLength(64);
        subcategory.setMaxLength(64);
        subcategory.setText(value);
        subcategory.addChangeHandler(handler);

        int row = subcategories.getRowCount() - 1;
        ImageButton deleteButton = createDeleteButton(subcategories, row);
       
        subcategories.insertRow(row);
        subcategories.setWidget(row, 1, subcategory);
        subcategories.setWidget(row, 2, deleteButton);
        
        if (0 < row) {
            ImageButton upArrow = createUpArrow(subcategories, row, 0);
            subcategories.setWidget(row, 0, upArrow);
        }
    }
    
    private void addTableHeader(FlexTable table, int col, String header) {
        table.setText(0, col, header);
        table.getCellFormatter().setStyleName(0, col, "dataTableHeader");
    }
    
    private boolean close() {
        if (!dirty) {
            hide();
            parent.update();
            return true;
        }
            
        if (Window.confirm("There are changes to the task. " +
                           "Are you sure you want to close this window?")) {
                hide();
                parent.update();
                return true;
        }
        
        return false;
    }
    
    private TaskDTO collectTaskData(TaskDTO task) {
        String eventType = eventTypeList.getItemText(eventTypeList.getSelectedIndex());
        task.setEventName(eventNameInput.getValue());
        task.setEventType(eventType);

        task.getMediaRealms().clear();
        
        for (MediaTab tab: mediaTabs) {
            if (null == tab.getAnalyst())
                continue;
            
            String username = tab.getAnalyst();
            PersonDTO person = null;
            for (PersonDTO dto: analystList)
                if (dto.getUsername().equals(username))
                    person = dto;
            
            MediaRealmDTO realm = new MediaRealmDTO(tab.getName(), person);
            
            Set<String> categoryNames = tab.getCategories();
            for (String categoryName: categoryNames) {
                CategoryDTO category = new CategoryDTO(categoryName);
                List<String> subcategories = tab.getSubcategories(categoryName);
                for (String subName: subcategories) {
                    SubcategoryDTO sub = new SubcategoryDTO(subName);
                    category.addSubcategory(sub);
                }
                realm.addCategory(category);
            }
            
            FlexTable dataItems = tab.getDataTable();
            for (int row = 1; row < (dataItems.getRowCount() - 1); row++) {
                TextBox textBox = (TextBox) dataItems.getWidget(row, 1);
                String title = textBox.getText();
                textBox = (TextBox) dataItems.getWidget(row, 2);
                String details = textBox.getText();
                realm.addDataItem(new DataItemDTO(title, details));
            }
            
            task.addMediaRealm(realm);
        }
        
        return task;
    }
    
    private Panel createButtonPanel() {
        HorizontalPanel inner = new HorizontalPanel();
        inner.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        Panel saveClosePanel = createSaveClosePanel();
        saveClosePanel.getElement().getStyle().setPaddingRight(14, Unit.PX);
        inner.add(saveClosePanel);
        inner.add(createPublishButton());

        HorizontalPanel panel = new HorizontalPanel();
        panel.setWidth("100%");
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        panel.add(inner);
        
        return panel;
    }
    
    private Panel createCategoryPanel(String category, MediaTab mediaTab, Image header) {
        header.setStyleName("categoryHeader");

        Panel subcategories = createSubcategoryPanel(category, mediaTab);
        
        AbsolutePanel panel = new AbsolutePanel();
        panel.setStyleName("categoryPanel");
        panel.add(header);
        panel.add(subcategories);
        
        return panel;
    }
    
    private Panel createDataItems(String name, MediaTab mediaTab) {
        HorizontalPanel addPanel = new HorizontalPanel();
        addPanel.setSpacing(4);

        final FlexTable dataItems = new FlexTable();
        dataItems.setStyleName("subcategoriesPanel");
        dataItems.setWidget(0, 0, new Image("images/tasks/new/move_up_arrow_gray.png"));
        dataItems.getCellFormatter().setStyleName(0, 0, "arrowColumn");
        addTableHeader(dataItems, 1, "Item");
        addTableHeader(dataItems, 2, "Detail");
        addTableHeader(dataItems, 3, "Number");
        addTableHeader(dataItems, 4, "% Change");
        dataItems.setWidget(1, 1, addPanel);
        mediaTab.setDataTable(dataItems);
        
        ClickHandler addClickHandler = new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                addDataItem(dataItems, "", "");
            } 
        };
        
        Image plus = new Image("images/global/add.png");
        plus.setStyleName("addImage");
        plus.addClickHandler(addClickHandler);

        Label addLabel = new Label("Add row");
        addLabel.setStyleName("addLabel");
        addLabel.addClickHandler(addClickHandler);
        
        addPanel.add(plus);
        addPanel.add(addLabel);

        dataItemsTable = dataItems;
        return dataItems;
    }
    
    private Panel createDataPanel(String name, MediaTab mediaTab, Image header) {
        header.setStyleName("categoryHeader");

        Panel dataItems = createDataItems(name, mediaTab);
        
        AbsolutePanel panel = new AbsolutePanel();
        panel.setStyleName("categoryPanel");
        panel.add(header);
        panel.add(dataItems);
        
        return panel;
    }
    
    private ImageButton createDeleteButton(final FlexTable table, final int row) {
        final ImageButton deleteButton = new ImageButton("images/global/delete_row_button.png",
                                                         "images/global/delete_row_button_down.png",
                                                         "images/global/delete_row_button_down.png");
        deleteButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                table.removeRow(row);
                markAsDirty(true);
            }
        });

        return deleteButton;
    }
    
    private Panel createLabelledPanel(String label, Widget widget) {
        HorizontalPanel panel = new HorizontalPanel();
        panel.setSpacing(4);
        panel.add(new Label(label));
        panel.add(widget);
        return panel;
    }
    
    private Panel createMediaTab(String name, Image icon) {
        MediaTab mediaTab = new MediaTab(name);
        mediaTabs.add(mediaTab);
        
        ListBox dropDown = new ListBox();
        dropDown.setStyleName("dropDownFilter");
        dropDown.setVisibleItemCount(1);
        mediaTab.setAnalystList(dropDown);
        analystDropDowns.add(dropDown);

        HorizontalPanel top = new HorizontalPanel();
        top.setSpacing(6);
        top.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        top.add(icon);
        top.add(createLabelledPanel("Assign to:", dropDown));
        
        VerticalPanel categories = new VerticalPanel();
        categories.setWidth("100%");
        categories.setStyleName("categoriesPanel");
        categories.add(createCategoryPanel("Information Gaps", 
                                           mediaTab, 
                                           new Image("images/tasks/new/informationGapsHeader.png")));
        categories.add(createCategoryPanel("Possible Points of Confusion, Miscommunication, or Rumors", 
                                           mediaTab, 
                                           new Image("images/tasks/new/PPOC_Header.png")));
        categories.add(createCategoryPanel("Emerging Themes",
                                           mediaTab, 
                                           new Image("images/tasks/new/emergingThemesHeader.png")));
        categories.add(createCategoryPanel("Major Themes from Traditional & Social Media",
                                           mediaTab, 
                                           new Image("images/tasks/new/majorThemesHeader.png")));
        categories.add(createCategoryPanel("USG/CDC Mentions", 
                                           mediaTab, 
                                           new Image("images/tasks/new/USG_CDCmentionsHeader.png")));
        categories.add(createDataPanel("Data", mediaTab, new Image("images/tasks/new/dataHeader.png")));
        
        ScrollPanel scrollPanel = new ScrollPanel(categories);
        scrollPanel.setHeight("400px");
        
        VerticalPanel panel = new VerticalPanel();
        panel.addStyleName("tabPanel");
        panel.setWidth("100%");
        panel.add(top);
        panel.add(scrollPanel);
        
        return panel;
    }
    
    private Widget createMediaTabPanel() {
        TabLayoutPanel panel = new TabLayoutPanel(30, Unit.PX);
        panel.getElement().getStyle().setBackgroundColor("white");
        
        Image tabImage = new Image("images/tasks/new/TV_header.png");
        tabImage.setHeight("20px");
        panel.add(createMediaTab("Television", new Image("images/tasks/new/TV_header.png")), tabImage);
        
        tabImage = new Image("images/tasks/new/newspaper_header.png");
        tabImage.setHeight("20px");
        panel.add(createMediaTab("Newspaper", new Image("images/tasks/new/newspaper_header.png")), tabImage);
        
        tabImage = new Image("images/tasks/new/twitter_header.png");
        tabImage.setHeight("20px");
        panel.add(createMediaTab("Twitter", new Image("images/tasks/new/twitter_header.png")), tabImage);
        
        tabImage = new Image("images/tasks/new/blogs_header.png");
        tabImage.setHeight("20px");
        panel.add(createMediaTab("Blogs", new Image("images/tasks/new/blogs_header.png")), tabImage);
        
        panel.selectTab(0);
        panel.setWidth("100%");
        panel.setHeight("500px");
        
        return panel;
    }
    
    private ImageButton createPublishButton() {
        ImageButton publishButton = new ImageButton("images/tasks/new/publish_task_button.png",
                                        "images/tasks/new/publish_task_button_down.png",
                                        "images/tasks/new/publish_task_button_down.png");
        publishButton.setDisabledImage("images/tasks/new/publish_task_button_disabled.png");
        publishButton.setEnabled(false);
        publishButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                publishTask();
            } 
        });
        publishButtons.add(publishButton);
        
        return publishButton;
    }
    
    private Panel createSubcategoryPanel(String category, MediaTab mediaTab) {
        HorizontalPanel addPanel = new HorizontalPanel();
        addPanel.setSpacing(4);

        final FlexTable subcategories = new FlexTable();
        subcategories.setStyleName("subcategoriesPanel");
        subcategories.setWidget(0, 0, new Image("images/tasks/new/move_up_arrow_gray.png"));
        subcategories.getCellFormatter().setStyleName(0, 0, "arrowColumn");
        subcategories.setWidget(0, 1, addPanel);
        mediaTab.addCategory(category, subcategories);
        
        ClickHandler addClickHandler = new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                addSubcategory(subcategories, "");
            } 
        };
        
        Image plus = new Image("images/global/add.png");
        plus.setStyleName("addImage");
        plus.addClickHandler(addClickHandler);

        Label addLabel = new Label("Add subcategory");
        addLabel.setStyleName("addLabel");
        addLabel.addClickHandler(addClickHandler);
        
        addPanel.add(plus);
        addPanel.add(addLabel);

        categoryTables.add(subcategories);
        return subcategories;
    }
    
    private Panel createSaveClosePanel() {
        ImageButton saveButton = new ImageButton("images/global/save_button.png",
                                     "images/global/save_button_down.png",
                                     "images/global/save_button_down.png");
        saveButton.setDisabledImage("images/global/save_button_disabled.png");
        saveButton.setEnabled(false);
        saveButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                saveOrUpdate();
            } 
        });
        saveButtons.add(saveButton);
        
        ImageButton closeButton = new ImageButton("images/global/close_button.png",
                                                  "images/global/close_button_down.png",
                                                  "images/global/close_button_down.png");
        closeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                close();
                dirty = false;
            } 
        });
        closeButtons.add(closeButton);
        
        HorizontalPanel panel = new HorizontalPanel();
        panel.setSpacing(8);
        panel.add(saveButton);
        panel.add(closeButton);

        return panel;
    }
    
    private void createTask(TaskDTO task) {
        taskService.createTask(masthead.getSessionID(), task, new AsyncCallback<TaskDTO>() {
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to create new task: " + caught.getMessage());
            }

            public void onSuccess(TaskDTO task)
            {
                dirty = false;
                currentTask = task;
                reportIdLabel.setText("Report ID: " + task.getReportId());
            }            
        });
    }
    
    private Panel createTypeNamePanel() {
        eventTypeList = new ListBox();
        eventTypeList.setStyleName("dropDownFilter");
        eventTypeList.setVisibleItemCount(1);

        eventNameInput = new TextBox();
        eventNameInput.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event)
            {
                String eventName = eventNameInput.getValue();
                
                if (null == eventName || eventName.trim().length() == 0) {
                    for (ImageButton button: saveButtons)
                        button.setEnabled(false);
                    return;
                }
                
                markAsDirty(true);
            }            
        });
        
        reportIdLabel = new Label("Report ID:");
        
        HorizontalPanel panel = new HorizontalPanel();
        panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        panel.setSpacing(10);
        panel.add(createLabelledPanel("Event type:", eventTypeList));
        panel.add(createLabelledPanel("Event name:", eventNameInput)); 
        panel.add(reportIdLabel);

        return panel;        
    }
    
    private ImageButton createUpArrow(final FlexTable table, final int row, final int topRow) {
        final ImageButton upArrow = new ImageButton("images/tasks/new/move_up_arrow_button.png",
                                                    "images/tasks/new/move_up_arrow_button_down.png",
                                                    "images/tasks/new/move_up_arrow_button_down.png");
        upArrow.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                List<Widget> widgets = new ArrayList<Widget>(table.getCellCount(row));
                
                for (int col = 0; col < table.getCellCount(row); col++) 
                    widgets.add(table.getWidget(row, col));
                
                table.removeRow(row);
                int newRow = row - 1;
                table.insertRow(newRow);
               
                for (int col = 0; col < widgets.size(); col++) 
                    table.setWidget(newRow, col, widgets.get(col));
                
                table.setWidget(topRow, 0, new Image("images/tasks/new/move_up_arrow_gray.png"));
                
                for (int row = topRow + 1; row < table.getRowCount() - 1; row++) 
                    table.setWidget(row, 0, createUpArrow(table, row, topRow));                    
             }
        });

        return upArrow;
    }
    
    private MediaTab findMediaTabByName(String name) {
        for (MediaTab tab: mediaTabs)
            if (tab.getName().equals(name))
                return tab;
        
        return null;
    }
    
    private boolean isPublishable() {
        if (!isSavable())
            return false;
        
        for (MediaTab tab: mediaTabs) {
            String analyst = tab.getAnalyst();
            if (null == analyst)
                return false;
            
//            for (String category: tab.getCategories()) {
//                List<String> subcategories = tab.getSubcategories(category);
//                if (subcategories.size() == 0)
//                    return false;
//            }
            
//            FlexTable dataItems = tab.getDataTable();
//            if (dataItems.getRowCount() < 3)
//                return false;
        }
        
        return true;
    }
    
    private boolean isSavable() {
        String eventType = eventTypeList.getItemText(eventTypeList.getSelectedIndex());
        
        if (eventType.equals(SELECT_EVENT_TYPE) || eventType.equals(ADD_NEW))
            return false;
        
        String eventName = eventNameInput.getValue();
        
        if (null == eventName || eventName.trim().length() == 0)
            return false;
        
        return true;
    }
    
    private void markAsDirty(boolean flag) {
        dirty = flag;
        
        if (!isSavable())
            return;
        
        for (ImageButton button: saveButtons)
            button.setEnabled(flag);
        
        if (isPublishable()) 
            for (ImageButton button: publishButtons)
                button.setEnabled(true);
    }
    
    private void populate(TaskDTO task) {
        if (null == task) {
            reportIdLabel.setText("Report ID: ");
            return;
        }
        
        for (MediaRealmDTO realm: task.getMediaRealms()) {
            MediaTab tab = findMediaTabByName(realm.getTitle());
            ListBox analystList = tab.getAnalystList();
            for (int i = 0; i < analystList.getItemCount(); i++)
                if (analystList.getValue(i).equals(realm.getAnalyst().getUsername()))
                    analystList.setSelectedIndex(i);
            
            if (task.getStatus() != TaskStatus.InDevelopment)
                analystList.setEnabled(false);
            
            Map<String, FlexTable> categoryMap = tab.getCategoryMap();
            for (CategoryDTO category: realm.getCategories()) {
                FlexTable table = categoryMap.get(category.getTitle());
                for (SubcategoryDTO sub: category.getSubcategories())
                    addSubcategory(table, sub.getTitle());                
            }
            
            FlexTable dataItems = tab.getDataTable();
            for (DataItemDTO dataItem: realm.getData())
                addDataItem(dataItems, dataItem.getTitle(), dataItem.getDetail());
        }
        
        for (int i = 0; i < eventTypeList.getItemCount(); i++) 
            if (eventTypeList.getItemText(i).equals(task.getEventType()))
                eventTypeList.setSelectedIndex(i);
        
        
        eventNameInput.setText(task.getEventName());
        reportIdLabel.setText("Report ID: " + task.getReportId());
        eventTypeList.setEnabled(false);
        eventNameInput.setEnabled(false);
    }
    
    private void publishTask() {
        TaskDTO task = null == currentTask ? collectTaskData(new TaskDTO()) : 
                                             collectTaskData(currentTask);
        taskService.publishTask(masthead.getSessionID(), task, new AsyncCallback<TaskDTO>() {
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to publish task: " + caught.getMessage());
            }

            public void onSuccess(TaskDTO result)
            {
                markAsDirty(false);
                close();
            }            
        });
    }
    
    private void saveOrUpdate() {
        TaskDTO task = null == currentTask ? collectTaskData(new TaskDTO()) : 
                                             collectTaskData(currentTask);
        
        if (null == currentTask) 
            createTask(task);
        else
            updateTask(task);
        
        markAsDirty(false);
    }
    
    private void setAnalystLists(List<PersonDTO> list) {
        this.analystList = list;

        for (ListBox dropDown: analystDropDowns) {
            dropDown.clear();
            dropDown.addItem(SELECT_AN_ANALYST);
            for (PersonDTO person: analystList) {
                String name = person.getGivenName() + " " + person.getFamilyName();
                dropDown.addItem(name, person.getUsername());
            }
            dropDown.addChangeHandler(new ChangeHandler() {
                public void onChange(ChangeEvent event) {
                    ListBox listBox = (ListBox) event.getSource();
                    String value = listBox.getItemText(listBox.getSelectedIndex());
                    
                    if (value.equals(SELECT_AN_ANALYST))
                        return;
                 
                    markAsDirty(true);
                }
            });
        }
    }
    
    private void setEventTypeList(List<String> list) {
        eventTypeList.clear();
        eventTypeList.addItem(SELECT_EVENT_TYPE);
        
        for (String item: list)
            eventTypeList.addItem(item);
        
        eventTypeList.addItem(ADD_NEW);
        eventTypeList.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event)
            {
                String value = eventTypeList.getValue(eventTypeList.getSelectedIndex());
                
                if (value.equals(SELECT_EVENT_TYPE))
                    return;
                
                if (value.equals(ADD_NEW)) {
                    String newEventType = Window.prompt("Enter new event type", "");
                    
                    if (null == newEventType || newEventType.trim().length() == 0) {
                        eventTypeList.setItemSelected(0, true);
                        return;
                    }
                    
                    eventTypeList.insertItem(newEventType, 1);
                    eventTypeList.setItemSelected(1, true);
                    markAsDirty(true);
                    return;
                }
                
                markAsDirty(true);
            }            
        });
    }
    
    private void updateTask(TaskDTO task) {
        taskService.updateTask(masthead.getSessionID(), task, new AsyncCallback<TaskDTO>() {
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to update task: " + caught.getMessage());
            }

            public void onSuccess(TaskDTO task)
            {
                dirty = false;
                currentTask = task;
            }            
        });
    }
    
    private class MediaTab {
        private String name;
        private ListBox analystList;
        private Map<String, FlexTable> categoryMap = new HashMap<String, FlexTable>();
        private FlexTable dataTable;
        
        public MediaTab(String name) { 
            this.name = name;
        }

        public void addCategory(String categoryName, FlexTable table) {
            categoryMap.put(categoryName, table);
        }
        
        public void clear() {
            for (String key: categoryMap.keySet()) {
                FlexTable table = categoryMap.get(key);
                while (table.getRowCount() > 1)
                    table.removeRow(0);
            }
            while (dataTable.getRowCount() > 2)
                dataTable.removeRow(1);
        }
        
        public FlexTable getDataTable() {
            return this.dataTable;
        }
                
        public String getName()
        {
            return this.name;
        }

        public String getAnalyst()
        {
            if (analystList.getSelectedIndex() == 0)
                return null;
            
            return analystList.getValue(analystList.getSelectedIndex());
        }
        
        public ListBox getAnalystList() {
            return analystList;
        }
        
        public Map<String, FlexTable> getCategoryMap() {
            return this.categoryMap;
        }
        
        public Set<String> getCategories() {
            return categoryMap.keySet();
        }
        
        public List<String> getSubcategories(String category) {
            FlexTable table = categoryMap.get(category);
            List<String> subcategories = new ArrayList<String>();
            
            for (int row = 0; row < table.getRowCount() - 1; row++) {
                TextBox textBox = (TextBox) table.getWidget(row, 1);
                String subcategory = textBox.getText();
                if (!(null == subcategory || subcategory.trim().length() == 0))
                    subcategories.add(subcategory);
            }
            
            return subcategories;
        }

        public void setAnalystList(ListBox analystList)
        {
            this.analystList = analystList;
        }
        
        public void setDataTable(FlexTable table) {
            this.dataTable = table;
        }
        
    };
}
