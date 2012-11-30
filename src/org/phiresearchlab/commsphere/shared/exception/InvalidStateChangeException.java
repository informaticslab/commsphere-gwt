package org.phiresearchlab.commsphere.shared.exception;

/**
 *
 * @author Joel M. Rives
 * Oct 13, 2011
 */
public class InvalidStateChangeException extends CommSphereException {
	
	private static final long serialVersionUID = -5825435308362610108L;

	public InvalidStateChangeException() {
		super("Attempt to change task status to an invalid state.");
	}

}
