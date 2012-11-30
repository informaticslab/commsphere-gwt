package org.phiresearchlab.commsphere.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.phiresearchlab.commsphere.domain.Person;
import org.phiresearchlab.commsphere.domain.RoleType;
import org.phiresearchlab.commsphere.shared.Role;
import org.phiresearchlab.util.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;

public class PersonDAOTest extends BaseDaoTest {

	@Autowired PersonDAO personDAO;
    @Autowired RoleDAO roleDAO;
    
    private List<RoleType> allRoles = null;
	
	@Test
	public void testCreatePerson() {
	    RoleType role = getRole(Role.Administrator);
	    
		Person person = new Person();
		person.setEmailAddress("someone@somwhere.com");
		person.setEnabled(true);
		person.setFamilyName("Jones");
		person.setGivenName("Bob");
		person.setPassword("password");
		person.setUsername("username");
		person.addRole(role);
		
		personDAO.persist(person);
		
		Assert.assertTrue("The person entity shold have a valid database ID", person.getId() > 0);
		
		person = personDAO.findById(person.getId());
		Assert.assertEquals("username", person.getUsername());
	}
	
	@Test
	public void testForDefaultAdminUser() {
	    Person admin = personDAO.findByUsername("admin");
	    
	    Assert.assertNotNull("We should have a default admin user", admin);
	    
	    String password = "S3cr3t";
        
        Assert.assertTrue(BCrypt.checkpw(password, admin.getPassword()));
	}
	
	private RoleType getRole(Role role) {
	    if (null == allRoles) 
	        allRoles = roleDAO.findAll();
	    
	    for (RoleType roleType: allRoles)
	        if (roleType.getRole() == role)
	            return roleType;
	    
	    return null;
	}
}
