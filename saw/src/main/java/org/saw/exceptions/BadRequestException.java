package org.saw.exceptions ;

import org.saw.util.logs.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class BadRequestException extends Exception
{
    static final long serialVersionUID = 20101023L ;

    private final byte[] logCategory ;

    public BadRequestException(byte[] logCategory, String msg)
    {
	super("Bad request: " + msg) ;

        this.logCategory = logCategory ;
    }

    public BadRequestException(String msg)
    {
	super("Bad request: " + msg) ;

        this.logCategory = Logs.SERVER_LOG_CAT ;
    }

    public final byte[] getLogCategory()
    {
        return logCategory ;
    }

}
