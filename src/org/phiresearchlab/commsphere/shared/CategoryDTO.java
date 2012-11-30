package org.phiresearchlab.commsphere.shared;

import java.util.ArrayList;
import java.util.List;

public class CategoryDTO extends BaseDTO
{
    private int sequence;
    private String title;
    private List<SubcategoryDTO> subcategories;

    public CategoryDTO() { }
    
    public CategoryDTO(String title) {
        this.title = title;
    }
    
    public void addSubcategory(SubcategoryDTO subcategory) {
        getSubcategories().add(subcategory);
    }
    
    public int getSequence()
    {
        return this.sequence;
    }
    public String getTitle()
    {
        return this.title;
    }
    public List<SubcategoryDTO> getSubcategories()
    {
        if (null == subcategories)
            subcategories = new ArrayList<SubcategoryDTO>();
        return this.subcategories;
    }
    public void setSequence(int sequence)
    {
        this.sequence = sequence;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public void setSubcategories(List<SubcategoryDTO> subcategories)
    {
        this.subcategories = subcategories;
    }

}
