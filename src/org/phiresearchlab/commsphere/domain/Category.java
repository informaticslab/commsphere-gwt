/**
 * 
 */
package org.phiresearchlab.commsphere.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.phiresearchlab.commsphere.shared.CategoryDTO;
import org.phiresearchlab.commsphere.shared.SubcategoryDTO;

/**
 *
 * @author Joel M. Rives
 * Aug 19, 2011
 */

@Entity
public class Category extends DomainObject {
	
	private static final long serialVersionUID = 1792656847483150585L;

	private int sequence;
	
	@Column(length = 64, nullable = false)
	private String title;
	
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Subcategory> subcategories;
	
	public Category() { }
	
	public Category(String title) {
	    this.title = title;
	}
	
	public Category(CategoryDTO dto) {
	    this.sequence = dto.getSequence();
	    this.title = dto.getTitle();
	    this.subcategories = null;
	    for (SubcategoryDTO sub: dto.getSubcategories())
	        addSubcategory(new Subcategory(sub));
	}
	
	public void addSubcategory(Subcategory subcategory) {
	    getSubcategories().add(subcategory);
	}

    public String getTitle()
    {
        return this.title;
    }

    public List<Subcategory> getSubcategories()
    {
        if (null == subcategories)
            subcategories = new ArrayList<Subcategory>();
        return this.subcategories;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setSubcategories(List<Subcategory> subcategories)
    {
        this.subcategories = subcategories;
    }

    public int getSequence()
    {
        return this.sequence;
    }

    public void setSequence(int sequence)
    {
        this.sequence = sequence;
    }
    
    @SuppressWarnings("unchecked")
    public CategoryDTO toDTO() {
        CategoryDTO dto = new CategoryDTO(title);
        dto.setId(getId());
        dto.setSequence(sequence);
        dto.setSubcategories((List<SubcategoryDTO>)toSerializableList(subcategories));
        
        return dto;
    }
    
    protected Object toSerializable() {
        return toDTO();
    }
    
}
