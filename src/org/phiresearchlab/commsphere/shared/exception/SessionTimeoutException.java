package org.phiresearchlab.commsphere.shared.exception;

/**
 *
 * @author Joel M. Rives
 * Oct 13, 2011
 */
public class SessionTimeoutException extends CommSphereException {
	
	private static final long serialVersionUID = -5825435308362610108L;

	public SessionTimeoutException() {
		super("The session has timed out.");
	}

}
