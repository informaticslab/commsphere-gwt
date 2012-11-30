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

import org.phiresearchlab.commsphere.shared.BulletDTO;
import org.phiresearchlab.commsphere.shared.SubcategoryDTO;

/**
 *
 * @author Joel M. Rives
 * Aug 19, 2011
 */

@Entity
public class Subcategory extends DomainObject {
	
	private static final long serialVersionUID = 1792656847483150585L;

	@Column(length = 64, nullable = false)
	private String title;
	
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Bullet> bullets;
	
	public Subcategory() { }
	
	public Subcategory(String title) {
	    this.title = title;
	}
	
	public Subcategory(SubcategoryDTO dto) {
	    this.title = dto.getTitle();
	    this.bullets = null;
	    for (BulletDTO bullet: dto.getBullets())
	        addBullet(new Bullet(bullet));
	}
	
	public void addBullet(Bullet bullet) {
	    if (null == bullets) 
	        bullets = new ArrayList<Bullet>();
	    bullets.add(bullet);
	}

    public String getTitle()
    {
        return this.title;
    }

    public List<Bullet> getBullets()
    {
        return this.bullets;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setBullets(List<Bullet> bullets)
    {
        this.bullets = bullets;
    }
    
    @SuppressWarnings("unchecked")
    public SubcategoryDTO toDTO() {
        SubcategoryDTO dto = new SubcategoryDTO(title);
        dto.setBullets((List<BulletDTO>)toSerializableList(bullets));
        return dto;
    }
    
    protected Object toSerializable() {
        return toDTO();
    }

}
