/**
 * 
 */
package org.phiresearchlab.commsphere.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.phiresearchlab.commsphere.shared.DataItemDTO;

/**
 *
 * @author Joel M. Rives
 * Aug 19, 2011
 */

@Entity
public class DataItem extends DomainObject {
	
	private static final long serialVersionUID = 1792656847483150585L;
	
    private int sequence;
    
	private String title;
	
	private String detail;

	private Integer value;
	
    @Column(nullable = false)
    private Date timestamp;
    
	public DataItem() { }
	
	public DataItem(String title) {
	    this.title = title;
	}
	
	public DataItem(DataItemDTO dto) {
	    this.sequence = dto.getSequence();
	    this.timestamp = dto.getTimestamp();
	    this.title = dto.getTitle();
	    this.detail = dto.getDetail();
	    this.value = dto.getValue();
	}

    public String getTitle()
    {
        return this.title;
    }

    public String getDetail()
    {
        return this.detail;
    }

    public Integer getValue()
    {
        return this.value;
    }

    public Date getTimestamp()
    {
        return this.timestamp;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public void setValue(Integer value)
    {
        this.value = value;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public int getSequence()
    {
        return this.sequence;
    }

    public void setSequence(int sequence)
    {
        this.sequence = sequence;
    }
    
    public DataItemDTO toDTO() {
        DataItemDTO dto = new DataItemDTO(title, detail);
        dto.setId(getId());
        dto.setSequence(sequence);
        dto.setTimestamp(timestamp);
        dto.setValue(value);
        
        return dto;
    }
    
    protected Object toSerializable() {
        return toDTO();
    }

}
