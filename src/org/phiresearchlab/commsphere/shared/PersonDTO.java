/**
 * 
 */
package org.phiresearchlab.commsphere.shared;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Joel M. Rives
 * Apr 26, 2011
 */
public class PersonDTO extends BaseDTO {

	private static final long serialVersionUID = -1701874097194130617L;

	private String givenName;
	private String familyName;
    private String username;
    private String password;
    private String emailAddress;
    private List<Role> roles = new ArrayList<Role>();
	
	public PersonDTO() { }
	
	public PersonDTO(Long id) {
		setId(id);
	}
	
	public void addRole(Role role) {
	    this.roles.add(role);
	}
	
	public boolean hasRole(Role role) {
	    for (Role r: roles)
	        if (r == role)
	            return true;
	    
	    return false;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getEmailAddress()
    {
        return this.emailAddress;
    }

    public void setEmailAddress(String email)
    {
        this.emailAddress = email;
    }
	
    public List<Role> getRoles() {
        return this.roles;
    }
    
}
