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
public class TaskDTO extends BaseDTO
{
     private String eventName;
     private String eventType;
     private String reportId;
     private String coordinator;
     private List<MediaRealmDTO> mediaRealms;
     private List<DataItemDTO> additionalData;    
     private Date created;
     private Date lastActivity;    
     private Date started;
     private Date ended;
     private TaskStatus status;
     private String overview;
     private List<ContactsDTO> contacts;
     private boolean report;
    
     public TaskDTO() { }
     
     public TaskDTO(String eventName, String eventType) {
         this.eventName = eventName;
         this.eventType = eventType;
     }
     
     public void addMediaRealm(MediaRealmDTO realm) {
         getMediaRealms().add(realm);
     }
     
     public MediaRealmDTO getMediaRealm(String title) {
         for (MediaRealmDTO realm: mediaRealms)
             if (realm.getTitle().equals(title))
                 return realm;
         return null;
     }
     
    public boolean hasReport()
    {
        return this.report;
    }

    public void setReport(boolean report)
    {
        this.report = report;
    }

    public String getEventName()
    {
        return this.eventName;
    }
    public String getEventType()
    {
        return this.eventType;
    }
    public String getReportId()
    {
        return this.reportId;
    }
    public String getCoordinator()
    {
        return this.coordinator;
    }
    public List<MediaRealmDTO> getMediaRealms()
    {
        if (null == mediaRealms)
            mediaRealms = new ArrayList<MediaRealmDTO>();
        return this.mediaRealms;
    }
    public List<DataItemDTO> getAdditionalData()
    {
        return this.additionalData;
    }
    public Date getCreated()
    {
        return this.created;
    }
    public Date getLastActivity()
    {
        return this.lastActivity;
    }
    public Date getStarted()
    {
        return this.started;
    }
    public Date getEnded()
    {
        return this.ended;
    }
    public TaskStatus getStatus()
    {
        return this.status;
    }
    public String getOverview()
    {
        return this.overview;
    }
    public List<ContactsDTO> getContacts()
    {
        return this.contacts;
    }
    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }
    public void setEventType(String eventType)
    {
        this.eventType = eventType;
    }
    public void setReportId(String reportId)
    {
        this.reportId = reportId;
    }
    public void setCoordinator(String coordinator)
    {
        this.coordinator = coordinator;
    }
    public void setMediaRealms(List<MediaRealmDTO> mediaRealms)
    {
        this.mediaRealms = mediaRealms;
    }
    public void setAdditionalData(List<DataItemDTO> additionalData)
    {
        this.additionalData = additionalData;
    }
    public void setCreated(Date created)
    {
        this.created = created;
    }
    public void setLastActivity(Date lastActivity)
    {
        this.lastActivity = lastActivity;
    }
    public void setStarted(Date started)
    {
        this.started = started;
    }
    public void setEnded(Date ended)
    {
        this.ended = ended;
    }
    public void setStatus(TaskStatus status)
    {
        this.status = status;
    }
    public void setOverview(String overview)
    {
        this.overview = overview;
    }
    public void setContacts(List<ContactsDTO> contacts)
    {
        this.contacts = contacts;
    }
     
}
