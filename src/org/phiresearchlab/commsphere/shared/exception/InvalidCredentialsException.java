package org.phiresearchlab.commsphere.shared.exception;

/**
 *
 * @author Joel M. Rives
 * Oct 13, 2011
 */
public class InvalidCredentialsException extends CommSphereException {
	
	private static final long serialVersionUID = -5825435308362610108L;

	public InvalidCredentialsException() {
		super("The credentials supplied are invalid.");
	}

}
