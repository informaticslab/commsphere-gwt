package org.phiresearchlab.commsphere.shared;

import java.util.Date;
import java.util.List;

import org.phiresearchlab.commsphere.shared.exception.CommSphereException;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TaskServiceAsync
{
    void createTask(SessionID sid, TaskDTO task, AsyncCallback<TaskDTO> callback)  throws CommSphereException;
    
    void findTasksByType(SessionID sid, String eventTypeName, Date since, String status, AsyncCallback<List<TaskDTO>> callback);

    void getEventTypes(SessionID sid, AsyncCallback<List<String>> callback);
    
    void getMediaRealms(SessionID sid, long taskId, AsyncCallback<List<MediaRealmDTO>> callback);
    
    void markAsDone(SessionID sid, long taskId, MediaRealmDTO realm, AsyncCallback<MediaRealmDTO> callback)  throws CommSphereException;
    
    void publishTask(SessionID sid, TaskDTO dto, AsyncCallback<TaskDTO> callback)  throws CommSphereException;
    
    void updateMediaRealm(SessionID sid, long taskId, MediaRealmDTO dto, AsyncCallback<MediaRealmDTO> callback)  throws CommSphereException;
   
    void updateTask(SessionID sid, TaskDTO dto, AsyncCallback<TaskDTO> callback)  throws CommSphereException;

}
