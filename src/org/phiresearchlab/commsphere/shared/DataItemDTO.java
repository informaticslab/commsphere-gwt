package org.phiresearchlab.commsphere.shared;

import java.util.Date;

public class DataItemDTO extends BaseDTO
{
    private int sequence;    
    private String title;
    private String detail;
    private Integer value;
    private Date timestamp;
    
    public DataItemDTO() { }
    
    public DataItemDTO(String title, String detail) {
        this.title = title;
        this.detail = detail;
        this.timestamp = new Date();
    }
    
    public int getSequence()
    {
        return this.sequence;
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
    public void setSequence(int sequence)
    {
        this.sequence = sequence;
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

}
