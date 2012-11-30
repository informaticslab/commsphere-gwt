package org.phiresearchlab.commsphere.shared;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Nov 9, 2011
 *
 */
public class MediaRealmDTO extends BaseDTO
{
    private String title;
    private PersonDTO analyst;
    private List<CategoryDTO> categories;
    private List<DataItemDTO> data;
    private Date started;
    private Date completed;
    private Boolean locked = false;
    
    public MediaRealmDTO() { }

    public MediaRealmDTO(String title, PersonDTO analyst) {
        this.title = title;
        this.analyst = analyst;
    }
    
    public void addCategory(CategoryDTO category) {
        getCategories().add(category);
    }
    
    public void addDataItem(DataItemDTO dataItem) {
        getData().add(dataItem);
    }
    
    public CategoryDTO getCategory(String title) {
        for (CategoryDTO category: categories) 
            if (category.getTitle().equals(title))
                return category;
        return null;
    }
    
    public String getTitle()
    {
        return this.title;
    }
    public PersonDTO getAnalyst()
    {
        return this.analyst;
    }
    public List<CategoryDTO> getCategories()
    {
        if (null == categories)
            categories = new ArrayList<CategoryDTO>();
        return this.categories;
    }
    public List<DataItemDTO> getData()
    {
        if (null == data)
            data = new ArrayList<DataItemDTO>();
        return this.data;
    }
    public Date getStarted()
    {
        return this.started;
    }
    public Date getCompleted()
    {
        return this.completed;
    }
    public Boolean getLocked()
    {
        return this.locked;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public void setAnalyst(PersonDTO analyst)
    {
        this.analyst = analyst;
    }
    public void setCategories(List<CategoryDTO> categories)
    {
        this.categories = categories;
    }
    public void setData(List<DataItemDTO> data)
    {
        this.data = data;
    }
    public void setStarted(Date started)
    {
        this.started = started;
    }
    public void setCompleted(Date completed)
    {
        this.completed = completed;
    }
    public void setLocked(Boolean locked)
    {
        this.locked = locked;
    }

}
