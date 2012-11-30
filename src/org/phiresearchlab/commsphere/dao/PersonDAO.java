/**
 * 
 */
package org.phiresearchlab.commsphere.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import org.phiresearchlab.commsphere.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Joel M. Rives
 * Aug 19, 2011
 */

@Repository("personDAO")
public class PersonDAO extends JpaDAO<Long, Person> {
	@Autowired
	EntityManagerFactory entityManagerFactory;
	
	@PostConstruct
	public void init() {
		super.setEntityManagerFactory(entityManagerFactory);
	}
	
	public Person findByUsername(String name) {
		@SuppressWarnings({ "unchecked", "deprecation" })
		List<Person> results = getJpaTemplate().find("select p from Person p where p.username = ?1", name);		
		
		if (results.size() == 0)
			return null;
		
		return results.get(0);
	}
	
}
