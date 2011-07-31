package org.saw.exceptions ;

import org.saw.users.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class ServiceUnavailableException extends Exception
{
    static final long serialVersionUID = 20101023L ;

    private final User user ;

    public User getUser()
    {
	return user ;
    }

    public ServiceUnavailableException(String msg)
    {
	this(msg,null) ;
    }

    public ServiceUnavailableException(String msg, User user)
    {
	super("Service unavailable: " + msg) ;
	this.user = user ;
    }

}
