package org.phiresearchlab.commsphere.server;

import org.phiresearchlab.commsphere.domain.Person;
import org.phiresearchlab.commsphere.shared.Role;
import org.phiresearchlab.commsphere.shared.SessionID;
import org.phiresearchlab.commsphere.shared.exception.PermissionDeniedException;

/**
 * 
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Nov 2, 2011
 *
 */
public abstract class BaseService
{
    protected SessionMap sessionMap = SessionMap.getInstance();
    
    protected Person validateSession(SessionID sid) {
        return sessionMap.getSessionUser(sid);
    }
    
    protected Person validateUser(SessionID sid, Role role) {
        Person user = sessionMap.getSessionUser(sid);
        
        if (!user.hasRole(role))
            throw new PermissionDeniedException();
        
        return user;
    }
    
    protected Person validateUser(SessionID sid, Role[] roles) {
        Person user = sessionMap.getSessionUser(sid);
        
        for (Role role: roles)
            if (user.hasRole(role))
                return user;
        
        throw new PermissionDeniedException();
    }

}
