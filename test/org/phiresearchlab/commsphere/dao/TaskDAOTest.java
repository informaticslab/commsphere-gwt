package org.phiresearchlab.commsphere.dao;

import java.util.Date;

import org.hibernate.collection.PersistentBag;
import org.junit.Assert;
import org.junit.Test;
import org.phiresearchlab.commsphere.domain.Category;
import org.phiresearchlab.commsphere.domain.MediaRealm;
import org.phiresearchlab.commsphere.domain.Subcategory;
import org.phiresearchlab.commsphere.domain.Task;
import org.phiresearchlab.commsphere.shared.TaskStatus;
import org.phiresearchlab.util.GWTUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskDAOTest extends BaseDaoTest {

    @Autowired MediaRealmDAO mediaRealmDAO;
	@Autowired TaskDAO taskDAO;
	
	@Test
	public void testCreateTask() {
	    MediaRealm realm = createMediaRealm("Television");
	    mediaRealmDAO.persist(realm);
	    
	    Task task = createTask("Test Task", "Test");
	    task.addMediaRealm(realm);
        
        taskDAO.persist(task);
	    
        Assert.assertTrue("The Task entity should have a valid database ID", task.getId() > 0);
	}
	
	@Test
	public void testSerializingTask() {
        MediaRealm realm = createMediaRealm("Television");
        mediaRealmDAO.persist(realm);
        
        Task task = createTask("Test Task", "Test");
        task.addMediaRealm(realm);

        taskDAO.persist(task);
        task = taskDAO.findById(task.getId());
        
        task = (Task) GWTUtil.makeGWTSerializable(task);
        
        Assert.assertFalse(task.getAdditionalData() instanceof PersistentBag);
	}
	
    private Category createCategory(MediaRealm parent, String name, int subcategoryCount) {
        Category category = new Category(name);
        
        for (int i = 0; i < subcategoryCount; i++) {
            Subcategory sub = new Subcategory("Subcategory " + (i + 1));
            category.addSubcategory(sub);
        }
        
        return category;
    }
    
	private MediaRealm createMediaRealm(String name) {
        MediaRealm realm = new MediaRealm("Television");
        realm.addCategory(createCategory(realm, "First Category", 3));
        realm.addCategory(createCategory(realm, "Second Category", 1));
        realm.addCategory(createCategory(realm, "Third Category", 2));
        
        return realm;
	}
	
	private Task createTask(String name, String type) {
	    Task task = new Task(name, type);
        task.setCoordinator("guest");
        task.setReportId("TT1");
        task.setCreated(new Date());
        task.setLastActivity(task.getCreated());
        task.setStatus(TaskStatus.NotStarted);
	    
	    return task;
	}
	
}
