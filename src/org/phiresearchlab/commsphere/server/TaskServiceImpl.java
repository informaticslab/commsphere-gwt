package org.phiresearchlab.commsphere.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.phiresearchlab.commsphere.dao.MediaRealmDAO;
import org.phiresearchlab.commsphere.dao.PersonDAO;
import org.phiresearchlab.commsphere.dao.TaskDAO;
import org.phiresearchlab.commsphere.domain.Category;
import org.phiresearchlab.commsphere.domain.MediaRealm;
import org.phiresearchlab.commsphere.domain.Person;
import org.phiresearchlab.commsphere.domain.Task;
import org.phiresearchlab.commsphere.shared.CategoryDTO;
import org.phiresearchlab.commsphere.shared.MediaRealmDTO;
import org.phiresearchlab.commsphere.shared.Role;
import org.phiresearchlab.commsphere.shared.SessionID;
import org.phiresearchlab.commsphere.shared.TaskDTO;
import org.phiresearchlab.commsphere.shared.TaskService;
import org.phiresearchlab.commsphere.shared.TaskStatus;
import org.phiresearchlab.commsphere.shared.exception.CommSphereException;
import org.phiresearchlab.commsphere.shared.exception.DataValidationException;
import org.phiresearchlab.commsphere.shared.exception.DoesNotExistException;
import org.phiresearchlab.commsphere.shared.exception.InvalidStateChangeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * Created on Oct 6, 2011
 *
 */
@Service("taskService")
public class TaskServiceImpl extends BaseService implements TaskService
{
    @Autowired private MediaRealmDAO mediaRealmDAO;
    @Autowired private PersonDAO personDAO;
    @Autowired private TaskDAO taskDAO;
    
    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
    public TaskDTO createTask(SessionID sid, TaskDTO dto)  throws CommSphereException {
        Person user = validateUser(sid, Role.Coordinator);
        validate(dto);
                
        Task task = new Task(dto.getEventName(), dto.getEventType());
        task.setCoordinator(user.getUsername());
        task.setReportId(generateReportId(task.getEventName()));
        task.setCreated(new Date());
        task.setLastActivity(task.getCreated());
        task.setStatus(TaskStatus.InDevelopment);
        
        for (MediaRealmDTO realmDTO: dto.getMediaRealms()) {
            MediaRealm realm = new MediaRealm(realmDTO);
            task.addMediaRealm(realm);
            Person person = personDAO.findById(realm.getAnalyst().getId());
            realm.setAnalyst(person);
        }
                
        taskDAO.persist(task);      
        return task.toDTO();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<TaskDTO> findTasksByType(SessionID sid, String eventTypeName, Date since, String status)
    throws CommSphereException {
        Person user = validateSession(sid);
        
        List<Task> allTasks = taskDAO.findSince(since);
        List<TaskDTO> eventTasks = new ArrayList<TaskDTO>();
        
        for (Task task: allTasks) {
            String eventType = task.getEventType();
            if ((eventTypeName.equals("All") || eventType.equals(eventTypeName)) && 
                (status.equals("All") || status.equals(task.getStatus().name()))) {
                if (user.getUsername().equals(task.getCoordinator())) {
                    eventTasks.add(task.toDTO());
                } else {
                    if (task.getStatus() == TaskStatus.InDevelopment)
                        continue;
                    
                    for (MediaRealm mediaRealm: task.getMediaRealms())
                        if (user.getUsername().equals(mediaRealm.getAnalyst().getUsername()))
                            eventTasks.add(task.toDTO());
                }
            }
        }
        
        return eventTasks;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<String> getEventTypes(SessionID sid) {
        validateSession(sid);
                
        return taskDAO.getAllEventTypes();
    }
    
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<MediaRealmDTO> getMediaRealms(SessionID sid, long taskId) {
        validateSession(sid);
        
        Task task = getTask(taskId);
        List<MediaRealmDTO> list = new ArrayList<MediaRealmDTO>();
        for (MediaRealm realm: task.getMediaRealms())
            list.add(realm.toDTO());
        
        return list;
    }
    
    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
    public MediaRealmDTO markAsDone(SessionID sid, long taskId, MediaRealmDTO realm)  throws CommSphereException {
        validateUser(sid, Role.Analyst);
        
        realm.setCompleted(new Date());
        realm.setLocked(true);
        
        updateMediaRealm(sid, taskId, realm);
        
        Task task = getTask(taskId);
        task.setLastActivity(new Date());
        
        return realm;
    }
    
    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
    public TaskDTO publishTask(SessionID sid, TaskDTO dto)  throws CommSphereException {
        validateUser(sid, Role.Coordinator);
        validate(dto);
        
        if (null == dto.getId())
            dto = createTask(sid, dto);
        
        Task task = getTask(dto.getId());
        
        if (task.getStatus() != TaskStatus.InDevelopment)
            throw new InvalidStateChangeException();
        
        dto.setStatus(TaskStatus.NotStarted);
        task.setStatus(TaskStatus.NotStarted);
        task.setCreated(new Date());
        
        return updateTask(sid, dto);
    }

    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
    public MediaRealmDTO updateMediaRealm(SessionID sid, long taskId, MediaRealmDTO dto)  throws CommSphereException {
        validateUser(sid, Role.Analyst);
        
        MediaRealm realm = getMediaRealm(dto.getId());
        Person person = personDAO.findById(dto.getAnalyst().getId());
        realm.setAnalyst(person);
        realm.setLocked(dto.getLocked());
        if (null == realm.getStarted())
            realm.setStarted(new Date());
        realm.setCompleted(dto.getCompleted());
        realm.getCategories().clear();
        
        for (CategoryDTO dtoCategory: dto.getCategories()) {
            Category category = new Category(dtoCategory);
            realm.addCategory(category);
        }
        
        Task task = getTask(taskId);
        task.setStatus(TaskStatus.InProgress);
        
        return realm.toDTO();
    }
    
    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
    public TaskDTO updateTask(SessionID sid, TaskDTO dto)  throws CommSphereException {
        validateUser(sid, Role.Coordinator);
        validate(dto);
        
        Task task = getTask(dto.getId());
        task.setStatus(dto.getStatus());
        task.getMediaRealms().clear();
        
        for (MediaRealmDTO dtoRealm: dto.getMediaRealms()) {
            MediaRealm realm = new MediaRealm(dtoRealm);
            task.addMediaRealm(realm);
            Person person = personDAO.findById(realm.getAnalyst().getId());
            realm.setAnalyst(person);
        }
        
        task.setLastActivity(new Date());
        
        return task.toDTO();
    }
    
    /////////////////////////////////////////////////////////////////////////////////
    
    private String generateReportId(String eventName) {
        String[] words = eventName.split(" ");
        StringBuffer buffer = new StringBuffer();
        
        for (String word: words)
            buffer.append(word.toUpperCase().charAt(0));
        
        String prefix = buffer.toString();        
        List<Task> tasks = taskDAO.findReportIdStartsWith(prefix);
        int largest = 0;
        
        for (Task task: tasks) {
            String post = task.getReportId().substring(prefix.length());
            
            if (null == post || post.trim().length() == 0)
                continue;
            
            try
            {
                int number = Integer.parseInt(post);
                if (number > largest)
                    largest = number;
            }
            catch (NumberFormatException e)
            {
                // We don't care about this
            }
        }
        
        largest++;
        return prefix + largest;
    }
    
    private MediaRealm getMediaRealm(long id) {
        MediaRealm realm;
        try
        {
            realm = mediaRealmDAO.findById(id);
        }
        catch (Exception e)
        {
            throw new DoesNotExistException("The data you are trying to access does not exist.");
        }
        
        if (null == realm)
            throw new DoesNotExistException("The data you are trying to access does not exist.");

        return realm;
    }
    
    private Task getTask(long id) {
        Task task;
        try
        {
            task = taskDAO.findById(id);
        }
        catch (Exception e)
        {
            throw new DoesNotExistException("The data you are trying to access does not exist.");
        }
        
        if (null == task)
            throw new DoesNotExistException("The data you are trying to access does not exist.");

        return task;
    }
    
    private void validate(TaskDTO task) {
        String eventName = task.getEventName();
        String eventType = task.getEventType();
        
        if (null == eventName || eventName.trim().length() == 0 ||
            null == eventType || eventType.trim().length() == 0)
            throw new DataValidationException();            
    }
}
