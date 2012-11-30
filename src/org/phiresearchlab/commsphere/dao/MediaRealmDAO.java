/**
 * 
 */
package org.phiresearchlab.commsphere.dao;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import org.phiresearchlab.commsphere.domain.MediaRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Joel M. Rives
 * Aug 19, 2011
 */

@Repository("mediaRealmDAO")
public class MediaRealmDAO extends JpaDAO<Long, MediaRealm> {
	@Autowired
	EntityManagerFactory entityManagerFactory;
	
	@PostConstruct
	public void init() {
		super.setEntityManagerFactory(entityManagerFactory);
	}
		
}
