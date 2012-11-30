package org.phiresearchlab.commsphere.shared;

import java.util.ArrayList;
import java.util.List;

import org.phiresearchlab.commsphere.domain.Bullet;

public class SubcategoryDTO extends BaseDTO
{
    private String title;
    private List<BulletDTO> bullets;
    
    public SubcategoryDTO() { }
    
    public SubcategoryDTO(String title) {
        this.title = title;
    }
    
    public void addBullet(BulletDTO bullet) {
        getBullets().add(bullet);
    }
    
    public String getTitle()
    {
        return this.title;
    }
    public List<BulletDTO> getBullets()
    {
        if (null == bullets)
            bullets = new ArrayList<BulletDTO>();
        return this.bullets;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public void setBullets(List<BulletDTO> bullets)
    {
        this.bullets = bullets;
    }

}
