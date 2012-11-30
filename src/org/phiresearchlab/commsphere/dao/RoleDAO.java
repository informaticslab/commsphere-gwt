/**
 * 
 */
package org.phiresearchlab.commsphere.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import org.phiresearchlab.commsphere.domain.RoleType;
import org.phiresearchlab.commsphere.shared.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Joel M. Rives
 * Aug 19, 2011
 */

@Repository("roleDAO")
public class RoleDAO extends JpaDAO<Long, RoleType> {
	@Autowired
	EntityManagerFactory entityManagerFactory;
	
	@PostConstruct
	public void init() {
		super.setEntityManagerFactory(entityManagerFactory);
	}
		
    public RoleType findByRole(Role role) {
        @SuppressWarnings({ "unchecked", "deprecation" })
        List<RoleType> results = getJpaTemplate().find("select r from RoleType r where r.role = ?1", role);     
        
        if (results.size() == 0)
            return null;
        
        return results.get(0);
    }
    
}
