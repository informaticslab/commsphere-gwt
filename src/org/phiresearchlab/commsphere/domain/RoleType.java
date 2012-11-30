package org.phiresearchlab.commsphere.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.phiresearchlab.commsphere.shared.Role;

@Entity
public class RoleType extends DomainObject {

	private static final long serialVersionUID = -333793557645634534L;
	
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Role role;
	
	public RoleType() { }
	
	public RoleType(Role role) {
		this.role = role;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
}
