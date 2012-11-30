package org.phiresearchlab.commsphere.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.phiresearchlab.commsphere.shared.MediaRealmDTO;
import org.phiresearchlab.commsphere.shared.TaskDTO;
import org.phiresearchlab.commsphere.shared.TaskStatus;

@Entity
public class Task extends DomainObject
{

    private static final long serialVersionUID = -333793557645634534L;

    @Column(nullable = false, length = 64)
    private String eventName;

    private String eventType;

    @Column(nullable = false, length = 12)
    private String reportId;

    private String coordinator;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MediaRealm> mediaRealms;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DataItem> additionalData;

    private Date created;

    private Date lastActivity;

    private Date started;

    private Date ended;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(columnDefinition = "LONGTEXT")
    private String overview;

    @OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private List<Contacts> contacts;

    private Report report;

    public Task()
    {
    }

    public Task(String eventName, String eventType)
    {
        this.eventName = eventName;
        this.eventType = eventType;
    }

    public void addMediaRealm(MediaRealm realm)
    {
        getMediaRealms().add(realm);
    }

    public String getCoordinator()
    {
        return this.coordinator;
    }

    public void setCoordinator(String coordinator)
    {
        this.coordinator = coordinator;
    }

    public String getEventName()
    {
        return this.eventName;
    }

    public String getEventType()
    {
        return this.eventType;
    }

    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }

    public void setEventType(String eventType)
    {
        this.eventType = eventType;
    }

    public String getReportId()
    {
        return this.reportId;
    }

    public List<MediaRealm> getMediaRealms()
    {
        if (null == mediaRealms)
            mediaRealms = new ArrayList<MediaRealm>();
        return this.mediaRealms;
    }

    public void setReportId(String reportId)
    {
        this.reportId = reportId;
    }

    public void setMediaRealms(List<MediaRealm> mediaRealms)
    {
        this.mediaRealms = mediaRealms;
    }

    public List<DataItem> getAdditionalData()
    {
        if (null == additionalData)
            additionalData = new ArrayList<DataItem>();
        return this.additionalData;
    }

    public void setAdditionalData(List<DataItem> data)
    {
        this.additionalData = data;
    }

    public Date getCreated()
    {
        return this.created;
    }

    public TaskStatus getStatus()
    {
        return this.status;
    }

    public void setCreated(Date created)
    {
        this.created = created;
    }

    public void setStatus(TaskStatus status)
    {
        this.status = status;
    }

    public Report getReport()
    {
        return this.report;
    }

    public void setReport(Report report)
    {
        this.report = report;
    }

    public Date getStarted()
    {
        return this.started;
    }

    public Date getEnded()
    {
        return this.ended;
    }

    public String getOverview()
    {
        return this.overview;
    }

    public List<Contacts> getContacts()
    {
        if (null == contacts)
            contacts = new ArrayList<Contacts>();
        return this.contacts;
    }

    public void setStarted(Date started)
    {
        this.started = started;
    }

    public void setEnded(Date ended)
    {
        this.ended = ended;
    }

    public void setOverview(String overview)
    {
        this.overview = overview;
    }

    public void setContacts(List<Contacts> contacts)
    {
        this.contacts = contacts;
    }

    public Date getLastActivity()
    {
        return this.lastActivity;
    }

    public void setLastActivity(Date lastActivity)
    {
        this.lastActivity = lastActivity;
    }

    @SuppressWarnings("unchecked")
    public TaskDTO toDTO()
    {
        TaskDTO dto = new TaskDTO(eventName, eventType);
        dto.setCoordinator(coordinator);
        dto.setCreated(created);
        dto.setEnded(ended);
        dto.setId(getId());
        dto.setLastActivity(lastActivity);
        dto.setMediaRealms((List<MediaRealmDTO>) toSerializableList(mediaRealms));
        dto.setOverview(overview);
        dto.setReportId(reportId);
        dto.setStarted(started);
        dto.setStatus(status);
        dto.setReport(null != report);

        return dto;
    }

    protected Object toSerializable()
    {
        return toDTO();
    }

}
