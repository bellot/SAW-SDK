package org.saw.exceptions ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class InternalErrorException extends Exception
{
    static final long serialVersionUID = 20101023L ;

    public InternalErrorException(String msg)
    {
	super("Internal error: " + msg) ;
    }

}
