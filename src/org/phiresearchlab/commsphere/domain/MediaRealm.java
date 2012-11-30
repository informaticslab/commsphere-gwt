/**
 * 
 */
package org.phiresearchlab.commsphere.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import org.phiresearchlab.commsphere.shared.CategoryDTO;
import org.phiresearchlab.commsphere.shared.DataItemDTO;
import org.phiresearchlab.commsphere.shared.MediaRealmDTO;

/**
 *
 * @author Joel M. Rives
 * Aug 19, 2011
 */

@Entity
public class MediaRealm extends DomainObject {
	
	private static final long serialVersionUID = 1792656847483150585L;
	
	@Column(length = 32, nullable = false)
	private String title;

	@ManyToOne
	@JoinColumn(name = "analyst_id")
	private Person analyst;
	
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "sequence")
	private List<Category> categories;
	
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "sequence")
    private List<DataItem> data;
    
	private Date started;
	
	private Date completed;
	
	private Boolean locked = false;
	
	public MediaRealm() { }
	
	public MediaRealm(String title) {
	    this.title = title;
	}
	
	public MediaRealm(MediaRealmDTO dto) {
	    this.title = dto.getTitle();
	    this.analyst = new Person(dto.getAnalyst());
	    this.categories = null;
	    for (CategoryDTO cat: dto.getCategories())
	        addCategory(new Category(cat));
	    this.data = null;
	    for (DataItemDTO di: dto.getData())
	        addData(new DataItem(di));
	    this.started = dto.getStarted();
	    this.completed =dto.getCompleted();
	    this.locked = dto.getLocked();
	}
	
	public void addCategory(Category category) {
	    getCategories().add(category);
	}
	
	public void addData(DataItem item) {
	    getData().add(item);
	}

    public String getTitle()
    {
        return this.title;
    }

    public Person getAnalyst()
    {
        return this.analyst;
    }

    public List<Category> getCategories()
    {
        if (null == categories)
            categories = new ArrayList<Category>();
        return this.categories;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setAnalyst(Person analyst)
    {
        this.analyst = analyst;
    }

    public void setCategories(List<Category> categories)
    {
        this.categories = categories;
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

    public List<DataItem> getData()
    {
        if (null == data)
            data = new ArrayList<DataItem>();
        return this.data;
    }

    public void setData(List<DataItem> data)
    {
        this.data = data;
    }
	
    @SuppressWarnings("unchecked")
    public MediaRealmDTO toDTO() {
        MediaRealmDTO dto = new MediaRealmDTO(title, analyst.toDTO());
        dto.setCategories((List<CategoryDTO>)toSerializableList(categories));
        dto.setCompleted(completed);
        dto.setData((List<DataItemDTO>)toSerializableList(data));
        dto.setId(getId());
        dto.setLocked(locked);
        dto.setStarted(started);
        
        return dto;
    }
    
    protected Object toSerializable() {
        return toDTO();
    }
    
}
