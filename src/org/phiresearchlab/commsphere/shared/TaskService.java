package org.phiresearchlab.commsphere.shared;

import java.util.Date;
import java.util.List;

import org.phiresearchlab.commsphere.shared.exception.CommSphereException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("services/taskService")
public interface TaskService extends RemoteService
{
    TaskDTO createTask(SessionID sid, TaskDTO task)  throws CommSphereException;
    
    List<TaskDTO> findTasksByType(SessionID sid, String eventTypeName, Date since, String status);
    
    List<String> getEventTypes(SessionID sid);
    
    List<MediaRealmDTO> getMediaRealms(SessionID sid, long taskId);
    
    MediaRealmDTO markAsDone(SessionID sid, long taskId, MediaRealmDTO realm)  throws CommSphereException;
    
    TaskDTO publishTask(SessionID sid, TaskDTO dto)  throws CommSphereException;
    
    MediaRealmDTO updateMediaRealm(SessionID sid, long taskId, MediaRealmDTO dto)  throws CommSphereException;
    
    TaskDTO updateTask(SessionID sid, TaskDTO dto)  throws CommSphereException;
    
}
