package org.phiresearchlab.commsphere.shared.exception;

/**
 *
 * @author Joel M. Rives
 * Oct 13, 2011
 */
public class CommSphereException extends RuntimeException {
	
	private static final long serialVersionUID = -5825435308362610108L;

    public CommSphereException()
    {
        super();
    }

    public CommSphereException(String arg0, Throwable arg1)
    {
        super(arg0, arg1);
    }

    public CommSphereException(String arg0)
    {
        super(arg0);
    }

    public CommSphereException(Throwable arg0)
    {
        super(arg0);
    }

}
