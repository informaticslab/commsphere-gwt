/**
 * 
 */
package org.phiresearchlab.commsphere.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.phiresearchlab.commsphere.shared.BulletDTO;

/**
 *
 * @author Joel M. Rives
 * Aug 19, 2011
 */

@Entity
public class Bullet extends DomainObject {
	
	private static final long serialVersionUID = 1792656847483150585L;

	@Column(nullable = false, columnDefinition = "LONGTEXT")
	private String text;
	
	private Boolean enabled = true;
	
	public Bullet() { }
	
	public Bullet(String text) {
	    this.text = text;
	}
	
	public Bullet(BulletDTO dto) {
	    this.text = dto.getText();
	    this.enabled = dto.getEnabled();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public BulletDTO toDTO() {
	    BulletDTO dto = new BulletDTO();
	    dto.setText(text);
	    dto.setEnabled(enabled);
	    return dto;
	}
	
    protected Object toSerializable() {
        return toDTO();
    }

}
