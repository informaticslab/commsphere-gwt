package org.phiresearchlab.commsphere.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Oct 26, 2011
 *
 */
public class SortableFlexTable extends AbsolutePanel
{
    private static final String SORT_UP_IMAGE = "images/tasks/table_sort_arrow_up.png";
    private static final String SORT_DOWN_IMAGE = "images/tasks/table_sort_arrow_down.png";

    private Image HEADER_LEFT_EDGE = new Image("images/tasks/table_head_left_edge.png");
    private Image HEADER_RIGHT_EDGE = new Image("images/tasks/table_head_right_edge.png");
    
    private HorizontalPanel headers = new HorizontalPanel();
    private FlexTable theGrid = new FlexTable();
    private List<TableRow> data = new ArrayList<TableRow>();
    private int pageSize = 50;
    private int currentPage = 1;
    private boolean selectable = false;
    
    private Image lastSortImage;
    private int currentSortColumn = 0;
    private List<TableHeader> tableHeaders = new ArrayList<TableHeader>();
    
    public SortableFlexTable() {
        this.setStyleName("sortableFlexTable");
        ScrollPanel scrollPanel = new ScrollPanel(theGrid);
        theGrid.setStyleName("tableGrid");
        scrollPanel.setStyleName("tableScrollPanel");
        
        headers.setStyleName("tableHeaders");
        headers.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        
        HorizontalPanel wrapper = new HorizontalPanel();
        wrapper.add(HEADER_LEFT_EDGE);
        wrapper.setCellWidth(HEADER_LEFT_EDGE, "5px");
        wrapper.add(headers);
        wrapper.add(HEADER_RIGHT_EDGE);
        wrapper.setCellWidth(HEADER_RIGHT_EDGE, "5px");
        wrapper.setWidth("100%");
        
        add(wrapper);
        add(scrollPanel);
    }
    
    public int addColumn(String text, int width) {
        int index = tableHeaders.size();
        TableHeader header = new TableHeader(index, text, width);
        addHeader(index, width, header);
        return index;
    }
    
    public int addColumn(String text, int width, Comparator<Object> comparator) {
        int index = tableHeaders.size();
        TableHeader header = new TableHeader(index, text, width, comparator);
        addHeader(index, width, header);
        return index;
    }
    
    public int addColumn(String text, Image image, int width) {
        int index = tableHeaders.size();
        TableHeader header = new TableHeader(index, text, image, width);
        addHeader(index, width, header);
        return index;
    }
    
    public int addColumn(String text, Image image, int width, Comparator<Object> comparator) {
        int index = tableHeaders.size();
        TableHeader header = new TableHeader(index, text, image, width, comparator);
        addHeader(index, width, header);
        return index;
    }
    
    public void addRow(List<Object> items) {
        if (items.size() != tableHeaders.size())
            throw new RuntimeException("Wrong number of items");
        
        data.add(new TableRow(items));
    }
    
    public void clear() {
        data.clear();
        theGrid.removeAllRows();
    }
    
    public void deleteRow(int row) {
        theGrid.removeRow(row);
        data.remove(row);
    }
    
    public int getCurrentPage() {
        return currentPage;
    }
    
    public void makeSelectable(boolean flag) {
        selectable= flag;
        CheckBox selector = new CheckBox();
        
        if (headers.getWidgetCount() > 0) {
            headers.insert(selector, 0);
            headers.insert(new Image("images/tasks/table_head_divider.png"), 1);
        } else {
            headers.add(selector);
            headers.add(new Image("images/tasks/table_head_divider.png"));        
        }
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public int getRowCount() {
        return data.size();
    }
    
    public boolean isSelected(int row) {
        if (!selectable)
            return false;
        
        CheckBox cb = (CheckBox) theGrid.getWidget(row, 0);
        return cb.getValue();
    }
    
    public void render() {
        sortByColumn(currentSortColumn);
    }
    
    public void setCurrentPage(int page) {
        this.currentPage = page;
    }
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    private void addHeader(int index, int width, TableHeader header) {
        if (index > 0) 
            headers.add(new Image("images/tasks/table_head_divider.png"));
            
        headers.add(header);
        headers.setCellWidth(header, width + "%");
        
        tableHeaders.add(header);
        
        List<Object> items = new ArrayList<Object>();
        for (TableHeader th: tableHeaders)
            items.add("   ");
        clear();
        addRow(items);
        render();
    }
    
    private void sortByColumn(int column) {
        TableHeader header = (TableHeader) tableHeaders.get(column);
        Comparator<Object> c = header.getComparator();
        data = sort(data, column, c, header.getSortUp());
        
        theGrid.clear();
        int currentRow = 0;
   
        for (TableRow row: data) {
            if (selectable) {
                CheckBox selector = new CheckBox();
                theGrid.setWidget(currentRow, 0, selector);
                String styleName = (currentRow % 2) == 0 ? "evenRow" : "oddRow";
                theGrid.getRowFormatter().setStyleName(currentRow, styleName);
                theGrid.getCellFormatter().setStyleName(currentRow, 0, "tableCell");
                theGrid.getColumnFormatter().setWidth(0, "12px");
            }
            
            for (int index = 0; index < tableHeaders.size(); index++) {
                Object item = row.getItem(index);
                int col = index + (int) (selectable ? 1 : 0);
                if (item instanceof String)
                    theGrid.setText(currentRow, col, (String) item);
                else
                    theGrid.setWidget(currentRow, col, (Widget) item);
                String styleName = (currentRow % 2) == 0 ? "evenRow" : "oddRow";
                theGrid.getRowFormatter().setStyleName(currentRow, styleName);
                theGrid.getCellFormatter().setStyleName(currentRow, col, "tableCell");
                header = tableHeaders.get(index);
                int width = header.getWidth(); 
                theGrid.getColumnFormatter().setWidth(col, width + "%");
            }
            currentRow++;
        }
    }
    
    private  List<TableRow> sort(List<TableRow> list, int column, Comparator comparator, boolean sortUp) {
        SortedMap<Object, List<TableRow>> map = new TreeMap<Object, List<TableRow>>(comparator);

        for (TableRow row: data) {
            Object key = row.getItem(column);
            
            if (map.containsKey(key)) {
                List<TableRow> rows = map.get(key);
                rows.add(row);
            } else {
                List<TableRow> rows = new ArrayList<TableRow>();
                rows.add(row);
                map.put(key, rows);
            }
        }
        
        int index = sortUp ? 0 : list.size() - 1;
        int size = list.size();
        list = new ArrayList<TableRow>(size);
        
        for (int i = 0; i < size; i++)
            list.add(null);
        
        for (Object key: map.keySet()) {
            List<TableRow> rows = map.get(key);
            for (TableRow row: rows) {
                list.set(index, row);
                index += sortUp ? 1 : -1;
            }
        }

        return list;
    }
        
    private class TableHeader extends HorizontalPanel {
        private int column;
        private int width;
        private Image sortImage;
        private boolean sortUp = true;
        private Comparator<Object> comparator;
        
        public TableHeader(int index, String text, int width) {
            this.column = index;
            this.width = width;
            createHeader(text);
        }
        
        public TableHeader(int index, String text, Image image, int width) {
            this.column = index;
            this.width = width;
            createHeader(text, image);
        }
        
        public TableHeader(int index, Widget widget, int width) {
            this.column = index;
            this.width = width;
            createHeader(widget);
        }
        
        public TableHeader(int column, String text, int width, Comparator<Object> comparator) {
            this.column = column;
            this.width = width;
            this.comparator = comparator;
            createHeader(text);
        }
        
        public TableHeader(int column, String text, Image image, int width, Comparator<Object> comparator) {
            this.column = column;
            this.comparator = comparator;
            createHeader(text, image);
        }
        
        public int getColumn() {
            return this.column;
        }
        
        public Comparator<Object> getComparator() {
            return this.comparator;
        }
        
        public boolean getSortUp() {
            return this.sortUp;
        }
        
        public int getWidth() {
            return this.width;
        }
        
        private void createHeader(String text) {
            this.setVerticalAlignment(ALIGN_MIDDLE);
            this.setHorizontalAlignment(ALIGN_LEFT);
            add(createLabel(text));
            add(createSortImage());
            this.setStyleName("tableHeader");
        }
        
        private void createHeader(Widget widget) {
            this.setVerticalAlignment(ALIGN_MIDDLE);
            this.setHorizontalAlignment(ALIGN_LEFT);
            widget.setStyleName("tableHeaderLabel");
            add(widget);
            this.setStyleName("tableHeader");
        }
        
        private void createHeader(String text, Image image) {
            this.setVerticalAlignment(ALIGN_MIDDLE);
            this.setHorizontalAlignment(ALIGN_LEFT);
            add(createLabel(text));
            add(image);
            add(createSortImage());
            this.setCellHorizontalAlignment(sortImage, HasHorizontalAlignment.ALIGN_RIGHT);
            this.setStyleName("tableHeader");
        }
        
        private Label createLabel(String text) {
            Label label = new Label(text);
            label.setStyleName("tableHeaderLabel");
            label.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    if (null != lastSortImage)
                        lastSortImage.setVisible(false);
                    
                    lastSortImage = sortImage;
                    sortImage.setVisible(true);
                    
                    if (sortUp) {
                        sortImage.setUrl(SORT_DOWN_IMAGE);
                        sortUp = false;
                    }
                    else {
                        sortImage.setUrl(SORT_UP_IMAGE);
                        sortUp = true;
                    }
                    sortByColumn(column);
                }
            });
            return label;
        }
        
        private Image createSortImage() {
            sortImage = new Image(SORT_UP_IMAGE);
            sortImage.setVisible(false);
            return sortImage;
        }
    };
    
    private class TableRow {
      
        private List<Object> items;
        
        public TableRow(List<Object> items) {
            this.items = items;
        }
        
        public Object getItem(int column) {
            return items.get(column);
        }
        
    };
}
