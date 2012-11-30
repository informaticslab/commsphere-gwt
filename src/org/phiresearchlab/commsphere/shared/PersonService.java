package org.phiresearchlab.commsphere.shared;

import java.util.List;

import org.phiresearchlab.commsphere.shared.exception.CommSphereException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("services/personService")
public interface PersonService extends RemoteService {

    Boolean checkUsername(SessionID sid, String username);
    
	PersonDTO createUser(SessionID sid, PersonDTO person);
	
	List<PersonDTO> getAllAnalysts(SessionID sid);
	
	PersonDTO getCurrentUser(SessionID sid);
	
	List<PersonDTO> getUsersByRole(SessionID sid, String roleName);
	
	Boolean hasAdminUser();
	
	SessionID login(String username, String password) throws CommSphereException;
		
}
