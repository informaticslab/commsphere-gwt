package org.phiresearchlab.commsphere.server;

import java.util.ArrayList;
import java.util.List;

import org.phiresearchlab.commsphere.dao.PersonDAO;
import org.phiresearchlab.commsphere.dao.RoleDAO;
import org.phiresearchlab.commsphere.domain.Person;
import org.phiresearchlab.commsphere.domain.RoleType;
import org.phiresearchlab.commsphere.shared.PersonDTO;
import org.phiresearchlab.commsphere.shared.PersonService;
import org.phiresearchlab.commsphere.shared.Role;
import org.phiresearchlab.commsphere.shared.SessionID;
import org.phiresearchlab.commsphere.shared.exception.AlreadyExistsException;
import org.phiresearchlab.commsphere.shared.exception.CommSphereException;
import org.phiresearchlab.commsphere.shared.exception.InvalidCredentialsException;
import org.phiresearchlab.util.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("personService")
public class PersonServiceImpl extends BaseService implements PersonService {
    
    private static final String REALM = "edemo.phiresearchlab.org";
    
    private Role[] adminCoordinatorRoles = { 
            Role.Administrator,
            Role.Coordinator
    };
	
	@Autowired private PersonDAO personDAO;
    @Autowired private RoleDAO roleDAO;
	
	private SessionMap sessionMap = SessionMap.getInstance();

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Boolean checkUsername(SessionID sid, String username) {
        validateUser(sid, adminCoordinatorRoles);
        
        Person person = personDAO.findByUsername(username);
        
        return null == person;
    }
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public PersonDTO createUser(SessionID sid, PersonDTO newPerson) {
	    validateUser(sid, adminCoordinatorRoles);
	    
        Person person = personDAO.findByUsername(newPerson.getUsername());
        
        if (null != person)
            throw new AlreadyExistsException(person.getUsername());
        
        String password = BCrypt.hashpw(newPerson.getPassword(), BCrypt.gensalt());

        person = new Person();
		person.setUsername(newPerson.getUsername());
		person.setPassword(password);
		person.setGivenName(newPerson.getGivenName());
		person.setFamilyName(newPerson.getFamilyName());
		person.setEmailAddress(newPerson.getEmailAddress());
		
		for (Role role: newPerson.getRoles()) {
		    RoleType roleType = roleDAO.findByRole(role);
		    person.addRole(roleType);
		}
		
		personDAO.persist(person);
		personDAO.flush(person);
		
		return person.toDTO();
	}
	
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<PersonDTO> getAllAnalysts(SessionID sid) {
        validateUser(sid, adminCoordinatorRoles);
	    
        List<Person> all = personDAO.findAll();
        List<PersonDTO> analysts = new ArrayList<PersonDTO>();
        
        for (Person person: all) {
            if (person.hasRole(Role.Analyst))
                analysts.add(person.toDTO());
        }
        
        return analysts;
	}
	
	public PersonDTO getCurrentUser(SessionID sid) {
	    Person person = sessionMap.getSessionUser(sid);
		return person.toDTO();
	}
	
	public List<PersonDTO> getUsersByRole(SessionID sid, String roleName) {
	    validateUser(sid, adminCoordinatorRoles);
	    
	    List<Person> allUsers = personDAO.findAll();
	    List<PersonDTO> dtoList = new ArrayList<PersonDTO>();
	    
	    for (Person person: allUsers) {
	        if (roleName.equalsIgnoreCase("All")) {
	            dtoList.add(person.toDTO());
	        } else {
	            Role role = Role.valueOf(roleName);
	            if (person.hasRole(role))
	                dtoList.add(person.toDTO());
	        }
	    }
	 
	    return dtoList;
	}
	
	public Boolean hasAdminUser() {
	    List<Person> allUsers = personDAO.findAll();
	    
	    for (Person person: allUsers)
	        if (person.hasRole(Role.Administrator))
	            return true;
	    
	    return false;
	}
		
    public SessionID login(String username, String password) throws CommSphereException {
        if (null == username || username.trim().length() == 0)
            throw new InvalidCredentialsException();
        
        if (null == password || password.trim().length() == 0)
            throw new InvalidCredentialsException();
                
        Person person = personDAO.findByUsername(username);
        
        if (null == person)
            throw new InvalidCredentialsException();
        
        if (!BCrypt.checkpw(password, person.getPassword()))
            throw new InvalidCredentialsException();
        
        SessionID sid = sessionMap.establishSession(REALM, person);
        return sid;
    }
    
}
