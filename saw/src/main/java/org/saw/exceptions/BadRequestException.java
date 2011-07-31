package org.saw.exceptions ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class BadRequestException extends Exception
{
    static final long serialVersionUID = 20101023L ;

    public BadRequestException(String msg)
    {
	super("Bad request: " + msg) ;
    }

}
