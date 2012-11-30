package org.phiresearchlab.commsphere.shared;


/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Nov 17, 2011
 *
 */
public class BulletDTO extends BaseDTO
{
    private String text;    
    private Boolean enabled = true;
    
    public BulletDTO() { }
    
    public BulletDTO(String text) {
        this.text = text;
    }
    
    public String getText()
    {
        return this.text;
    }
    public Boolean getEnabled()
    {
        return this.enabled;
    }
    public void setText(String text)
    {
        this.text = text;
    }
    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
    }
    
}
