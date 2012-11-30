package org.phiresearchlab.commsphere.shared.exception;

/**
 *
 * @author Joel M. Rives
 * Oct 13, 2011
 */
public class AlreadyExistsException extends CommSphereException {
	
	private static final long serialVersionUID = -5825435308362610108L;

	private String username;

	public AlreadyExistsException(String username) {
		super("The user " + username + " already exists.");
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}
}
