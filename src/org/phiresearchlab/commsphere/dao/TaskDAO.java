/**
 * 
 */
package org.phiresearchlab.commsphere.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import org.phiresearchlab.commsphere.domain.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Joel M. Rives
 * Aug 19, 2011
 */

@Repository("taskDAO")
public class TaskDAO extends JpaDAO<Long, Task> {
	@Autowired
	EntityManagerFactory entityManagerFactory;
	
	@PostConstruct
	public void init() {
		super.setEntityManagerFactory(entityManagerFactory);
	}
	
	@SuppressWarnings("unchecked")
    public List<Task> findSince(Date timestamp) {
        @SuppressWarnings("rawtypes")
        List list = find("select t from Task t where t.lastActivity >= ?1", timestamp);	    
	    return (List<Task>) list;
	}
	
    @SuppressWarnings("unchecked")
	public List<Task> findReportIdStartsWith(String text) {
        @SuppressWarnings("rawtypes")
        List list = find("select t from Task t where t.reportId like ?1", text + "%");     
        return (List<Task>) list;
	}
    
    @SuppressWarnings("unchecked")
    public List<String> getAllEventTypes() {
        @SuppressWarnings("rawtypes")
        List list = find("select distinct eventType from Task");
        return (List<String>) list;
    }
}
