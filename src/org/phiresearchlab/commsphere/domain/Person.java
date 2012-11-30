package org.phiresearchlab.commsphere.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import org.phiresearchlab.commsphere.shared.PersonDTO;
import org.phiresearchlab.commsphere.shared.Role;

@Entity
public class Person extends DomainObject {

	private static final long serialVersionUID = -1420787524719485936L;
	
	@Column(length = 32)
	private String givenName;
	
	@Column(length = 32)
	private String familyName;

	@Column(nullable = false, length = 32)
    private String username;

	@Column(nullable = false, length = 128)
    private String password;
    
    @Column(length = 128)
    private String emailAddress;

	@Column(nullable = false)
    private Boolean enabled = true;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<RoleType> roles;
    
    public Person() { }
    
    public Person(PersonDTO dto) {
        this.setId(dto.getId());
        this.givenName = dto.getGivenName();
        this.familyName = dto.getFamilyName();
        this.username = dto.getUsername();
        this.emailAddress = dto.getEmailAddress();
    }
    
    public void addRole(RoleType role) {
        getRoles().add(role);
    }
    
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Set<RoleType> getRoles() {
	    if (null == roles)
	        roles = new HashSet<RoleType>();
		return roles;
	}

	public void setRoles(Set<RoleType> roles) {
		this.roles = roles;
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
	
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public boolean hasRole(Role role) {
		for (RoleType roleType: roles)
			if (roleType.getRole() == role)
				return true;
		
		return false;
	}
    
	public PersonDTO toDTO() {
		PersonDTO dto = new PersonDTO(getId());
		dto.setGivenName(givenName);
		dto.setFamilyName(familyName);
		dto.setUsername(username);
		dto.setEmailAddress(emailAddress);
		
		for (RoleType type: roles)
		    dto.addRole(type.getRole());
		
		return dto;
	}
    
}
