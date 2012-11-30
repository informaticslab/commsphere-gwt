package org.phiresearchlab.commsphere.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PersonServiceAsync {
	
    void checkUsername(SessionID sid, String username, AsyncCallback<Boolean> callback);
    
	void createUser(SessionID sid, PersonDTO person, AsyncCallback<PersonDTO> callback);
	
	void getAllAnalysts(SessionID sid, AsyncCallback<List<PersonDTO>> callback);
	
	void getCurrentUser(SessionID sid, AsyncCallback<PersonDTO> callback);
	
	void getUsersByRole(SessionID sid, String roleName, AsyncCallback<List<PersonDTO>> callback);
	
	void hasAdminUser(AsyncCallback<Boolean> callback);

	void login(String username, String password, AsyncCallback<SessionID> callback);
	
}
