package org.phiresearchlab.commsphere.shared.exception;

/**
 *
 * @author Joel M. Rives
 * Oct 13, 2011
 */
public class PermissionDeniedException extends CommSphereException {
	
	private static final long serialVersionUID = -5825435308362610108L;

	public PermissionDeniedException() {
		super("The user does not have sufficient priviledges for this request.");
	}

}
