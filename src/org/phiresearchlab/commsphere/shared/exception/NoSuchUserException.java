package org.phiresearchlab.commsphere.shared.exception;

/**
 *
 * @author Joel M. Rives
 * Oct 13, 2011
 */
public class NoSuchUserException extends CommSphereException {
	
	private static final long serialVersionUID = -5825435308362610108L;

	private String username;

	public NoSuchUserException(String username) {
		super("The user " + username + " does not exist.");
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}
}
