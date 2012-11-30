package org.phiresearchlab.commsphere.client;

import java.util.ArrayList;
import java.util.List;

import org.phiresearchlab.commsphere.shared.BulletDTO;
import org.phiresearchlab.commsphere.shared.CategoryDTO;
import org.phiresearchlab.commsphere.shared.MediaRealmDTO;
import org.phiresearchlab.commsphere.shared.PersonDTO;
import org.phiresearchlab.commsphere.shared.SessionID;
import org.phiresearchlab.commsphere.shared.SubcategoryDTO;
import org.phiresearchlab.commsphere.shared.TaskDTO;
import org.phiresearchlab.commsphere.shared.TaskService;
import org.phiresearchlab.commsphere.shared.TaskServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Nov 18, 2011
 *
 */
public class DataCollectionPopup extends PopupPanel implements WorkArea
{
    private static final String TELEVISION = "Television";
    private static final String NEWSPAPER = "Newspaper";
    private static final String TWITTER = "Twitter";
    private static final String BLOGS = "Blogs";
    
    private TaskServiceAsync taskService = GWT.create(TaskService.class);
    
    private CommSphere parent;
    private Masthead masthead;   
    private List<ImageButton> saveButtons = new ArrayList<ImageButton>();
    private List<ImageButton> closeButtons = new ArrayList<ImageButton>();
    private List<ImageButton> doneButtons = new ArrayList<ImageButton>();
    private Label taskLabel = new Label("");
    private Image mediaHeader = new Image("images/data/tv_analysis.png");
    
    private TaskDTO currentTask;
    private MediaRealmDTO currentMediaRealm;
    private boolean dirty = false;
    private List<CategoryTab> categoryTabs = new ArrayList<CategoryTab>();
    
    public DataCollectionPopup(CommSphere parent) {
        super(false);
        setGlassEnabled(true);
        
        this.parent = parent;
        this.masthead = new Masthead(this);
        
        Image header = new Image("images/data/data_collection_header.png");
        taskLabel.setStyleName("dataCollectionTaskLabel");
        mediaHeader.setStyleName("dataCollectionMediaHeader");
        
        HorizontalPanel headerPanel = new HorizontalPanel();
        headerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        headerPanel.add(header);
        headerPanel.add(taskLabel);
        
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setStyleName("mainPanel");
        mainPanel.setWidth("1000px");
        mainPanel.add(masthead);
        mainPanel.add(createButtonPanel());
        mainPanel.add(headerPanel);
        mainPanel.setCellHorizontalAlignment(headerPanel, HasHorizontalAlignment.ALIGN_LEFT);
        mainPanel.add(mediaHeader);
        mainPanel.setCellHorizontalAlignment(mediaHeader, HasHorizontalAlignment.ALIGN_LEFT);
        mainPanel.add(createCategoryTabPanel());
        mainPanel.add(createButtonPanel());

        this.addStyleName("dataCollectionPopup");
        this.getElement().getStyle().setBorderWidth(0, Unit.PX);
        this.setWidget(mainPanel);
    }
    
    public void clear() {
        if (close())
            parent.clear();
    }
    
    public void launchSettings() {
        parent.launchSettings();
    }
    
    void show(SessionID sid, PersonDTO user, TaskDTO task) {
        MediaRealmDTO realm = getMediaRealm(task, user);
        currentTask = task;
        currentMediaRealm = realm;
        masthead.setContext(sid, user);
        taskLabel.setText(task.getReportId() + ", " + task.getEventName());
        setMediaHeader(realm);
        populate(realm);
        show();
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
        
    private Panel createBulletPanel(String text, boolean locked) {
        Image bullet = new Image("images/data/bullet_icon.png");
        
        TextArea textArea = new TextArea();
        textArea.setCharacterWidth(100);
        textArea.setVisibleLines(5);
        textArea.setText(text);
        textArea.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event)
            {
                markAsDirty(true);
            }            
        });
        textArea.setEnabled(!locked);
        
        HorizontalPanel bulletEntryPanel = new HorizontalPanel();
        bulletEntryPanel.add(bullet);
        bulletEntryPanel.add(textArea);

        return bulletEntryPanel;
    }
    
    private Panel createButtonPanel() {
        HorizontalPanel inner = new HorizontalPanel();
        inner.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        Panel saveClosePanel = createSaveClosePanel();
        saveClosePanel.getElement().getStyle().setPaddingRight(14, Unit.PX);
        inner.add(saveClosePanel);
        inner.add(createDoneButton());

        HorizontalPanel panel = new HorizontalPanel();
        panel.setWidth("100%");
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        panel.add(inner);
        
        return panel;
    }
    
    private Panel createCategoryTab(String name, Image icon) {
        CategoryTab tab = new CategoryTab(name);
        categoryTabs.add(tab);
        
        VerticalPanel subPanel = new VerticalPanel();
        subPanel.setStyleName("subcategoryPanel");
        tab.setSubcategoryPanel(subPanel);
        
        ScrollPanel scrollPanel = new ScrollPanel(subPanel);
        scrollPanel.setHeight("400px");
        
        VerticalPanel panel = new VerticalPanel();
        panel.setWidth("100%");
        panel.add(icon);
        panel.add(scrollPanel);
        
        return panel;
    }
    
    private Widget createCategoryTabPanel() {
        TabLayoutPanel panel = new TabLayoutPanel(30, Unit.PX);
        panel.getElement().getStyle().setBackgroundColor("white");
        
        Image tabImage = new Image("images/data/info_gaps_header.png");
        tabImage.setHeight("20px");
        panel.add(createCategoryTab("Information Gaps", new Image("images/data/info_gaps_header.png")), tabImage);
        
        tabImage = new Image("images/data/PPOC_header.png");
        tabImage.setHeight("20px");
        panel.add(createCategoryTab("Possible Points of Confusion", new Image("images/data/PPOC_header.png")), "Possible Points of Confusion...");
        
        tabImage = new Image("images/data/emerging_themes_header.png");
        tabImage.setHeight("20px");
        panel.add(createCategoryTab("Emerging Themes", new Image("images/data/emerging_themes_header.png")), tabImage);
        
        tabImage = new Image("images/data/major_themes_header.png");
        tabImage.setHeight("20px");
        panel.add(createCategoryTab("Major Themes", new Image("images/data/major_themes_header.png")), "Major Themes");
        
        tabImage = new Image("images/data/USG_CDC_mentions_header.png");
        tabImage.setHeight("20px");
        panel.add(createCategoryTab("USG/CDC Mentions", new Image("images/data/USG_CDC_mentions_header.png")), tabImage);
        
        tabImage = new Image("images/data/data_header.png");
        tabImage.setHeight("20px");
        panel.add(createCategoryTab("Data", new Image("images/data/data_header.png")), tabImage);
               
        panel.selectTab(0);
        panel.setWidth("100%");
        panel.setHeight("500px");
        
        return panel;
    }
    
    private ImageButton createDoneButton() {
        ImageButton doneButton = new ImageButton("images/data/flag_as_done_button.png",
                                        "images/data/flag_as_done_button_down.png",
                                        "images/data/flag_as_done_button_down.png");
        doneButton.setDisabledImage("images/data/flag_as_done_button_disabled.png");
        doneButton.setEnabled(false);
        doneButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                flagAsDone();
            } 
        });
        doneButtons.add(doneButton);
        
        return doneButton;
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
    
    private Panel createSubcategoryPanel(SubcategoryDTO sub, final boolean locked) {
        Label label = new Label(sub.getTitle());
        label.setStyleName("subcategoryLabel");
        
        final VerticalPanel panel = new VerticalPanel();
        panel.add(label);
        
        for (BulletDTO bullet: sub.getBullets()) 
            panel.add(createBulletPanel(bullet.getText(), locked));
        
        if (panel.getWidgetCount() < 2)
            panel.add(createBulletPanel("", locked));
        else
            for (ImageButton button: doneButtons)
                button.setEnabled(!locked);

        ImageButton newBulletButton = new ImageButton("images/data/bullet_button.png",
                                                      "images/data/bullet_button_down.png",
                                                      "images/data/bullet_button_down.png");
        newBulletButton.addStyleName("newBulletButton");
        newBulletButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                panel.insert(createBulletPanel("", locked), panel.getWidgetCount() - 1);
            }            
        });
        newBulletButton.setEnabled(!locked);
        
        panel.add(newBulletButton);
        
        return panel;
    }
    
    private CategoryTab findCategoryTab(String title) {
        for (CategoryTab tab: categoryTabs) 
            if (title.startsWith(tab.getTitle()))
                return tab;
        return null;
    }
    
    private void flagAsDone() {
        taskService.markAsDone(masthead.getSessionID(), currentTask.getId(), currentMediaRealm, new AsyncCallback<MediaRealmDTO>() {
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to mark as done: " + caught.getMessage());
            }

            public void onSuccess(MediaRealmDTO result)
            {
                markAsDirty(false);
                close();
            }            
        });
    }
    
    private MediaRealmDTO getMediaRealm(TaskDTO task, PersonDTO user) {
        List<MediaRealmDTO> realms = new ArrayList<MediaRealmDTO>();
        
        for (MediaRealmDTO realm: task.getMediaRealms())
            if (realm.getAnalyst().getUsername().equals(user.getUsername()))
                realms.add(realm);
        
        if (realms.size() > 1) {
            // TODO: show mediaSelectPopup
        }
        
        return realms.get(0);
    }
    
    private void markAsDirty(boolean flag) {
        dirty = flag;
                
        for (ImageButton button: saveButtons)
            button.setEnabled(flag);
        
        if (true == flag)
            for (ImageButton button: doneButtons)
                button.setEnabled(true);
    }
    
    private void populate(MediaRealmDTO realm) {
        for (CategoryDTO category: realm.getCategories()) {
            CategoryTab tab = findCategoryTab(category.getTitle());
            VerticalPanel subPanel = tab.getSubCategoryPanel();
            subPanel.clear();
            
            for (SubcategoryDTO sub: category.getSubcategories()) {
                subPanel.add(createSubcategoryPanel(sub, realm.getLocked()));
            }
        }
    }
    
    private void saveOrUpdate() {
        for (CategoryDTO category: currentMediaRealm.getCategories()) {
            CategoryTab tab = findCategoryTab(category.getTitle());
            VerticalPanel subPanel = tab.getSubCategoryPanel();
            for (int s = 0; s < category.getSubcategories().size(); s++) {
                SubcategoryDTO subcategory = category.getSubcategories().get(s);
                subcategory.getBullets().clear();
                VerticalPanel vp = (VerticalPanel) subPanel.getWidget(s);
                for (int w = 0; w < vp.getWidgetCount(); w++) {
                    Widget widget = vp.getWidget(w);
                    
                    if (widget instanceof HorizontalPanel) {
                        HorizontalPanel bulletPanel = (HorizontalPanel) widget;
                        TextArea ta = (TextArea) bulletPanel.getWidget(1);
                        String bulletText = ta.getText();
                        if (null != bulletText && bulletText.trim().length() > 0)
                            subcategory.addBullet(new BulletDTO(bulletText.trim()));
                    }
                }
            }
        }
        
        taskService.updateMediaRealm(masthead.getSessionID(), currentTask.getId(), currentMediaRealm, new AsyncCallback<MediaRealmDTO>() {
            public void onFailure(Throwable caught)
            {
                Window.alert("Failed to update task: " + caught.getMessage());
            }

            public void onSuccess(MediaRealmDTO result)
            {
                markAsDirty(false);
            } 
        });
    }
    
    private void setMediaHeader(MediaRealmDTO realm) {
        if (realm.getTitle().equals(TELEVISION))
            mediaHeader.setUrl("images/data/tv_analysis.png");
        else if (realm.getTitle().equals(NEWSPAPER))
            mediaHeader.setUrl("images/data/newspaper_analysis.png");
        else if (realm.getTitle().equals(TWITTER))
            mediaHeader.setUrl("images/data/twitter_analysis.png");
        else 
            mediaHeader.setUrl("images/data/blog_analysis.png");
    }
    
    private class CategoryTab {
        private String title;
        private VerticalPanel subcategoryPanel;
        
        public CategoryTab(String title) {
            this.title = title;
        }
        
        public String getTitle() {
            return this.title;
        }
        
        public VerticalPanel getSubCategoryPanel() {
            return this.subcategoryPanel;
        }
        
        public void setSubcategoryPanel(VerticalPanel panel) {
            this.subcategoryPanel = panel;
        }
        
    };

}
