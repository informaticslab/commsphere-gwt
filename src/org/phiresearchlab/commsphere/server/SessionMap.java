/**
 * 
 */
package org.phiresearchlab.commsphere.server;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.phiresearchlab.commsphere.domain.Person;
import org.phiresearchlab.commsphere.shared.SessionID;
import org.phiresearchlab.commsphere.shared.exception.InvalidSessionException;
import org.phiresearchlab.commsphere.shared.exception.SessionTimeoutException;

/**
 *
 * @author Joel M. Rives
 * Oct 13, 2011
 */
public class SessionMap {
	
	private static long DEFAULT_TIMEOUT = 1000 * 60 * 30; // 30 minutes
	
	private static SessionMap THE_INSTANCE = null;
	
	public static SessionMap getInstance() {
	    if (null == THE_INSTANCE)
	        THE_INSTANCE = new SessionMap();
	    return THE_INSTANCE;
	}
	
	private Map<SessionID, Person> personMap = new HashMap<SessionID, Person>();
	private Map<SessionID, Date> dateMap = new HashMap<SessionID, Date>();
	
	public SessionID establishSession(String realm, Person person) {
	    UUID uuid = UUID.randomUUID();
	    SessionID sid = new SessionID(realm, uuid.toString());
	    personMap.put(sid, person);
	    dateMap.put(sid, new Date());
	    return sid;
	}
	
	public Person getSessionUser(SessionID sid) {
		Person person = personMap.get(sid);
		
		if (null == person)
			throw new InvalidSessionException();
		
		Date lastUsed = dateMap.get(sid);
		Date now = new Date();
		
		if ((now.getTime() - lastUsed.getTime()) > DEFAULT_TIMEOUT) {
			personMap.remove(sid);
			dateMap.remove(sid);
			throw new SessionTimeoutException();
		}
		
		dateMap.put(sid, now);
		
		return person;
	}
	
	private SessionMap() {
	    // To enforce singleton
	}
}
