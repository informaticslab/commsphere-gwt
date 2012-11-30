package org.phiresearchlab.commsphere.shared.exception;

/**
 *
 * @author Joel M. Rives
 * Oct 13, 2011
 */
public class InvalidSessionException extends CommSphereException {
	
	private static final long serialVersionUID = -5825435308362610108L;

	public InvalidSessionException() {
		super("The session is invalid.");
	}

}
