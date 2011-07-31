package org.saw.exceptions ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

import org.saw.users.* ;

public class PostTooBigException extends Exception
{
    static final long serialVersionUID = 20101023L ;

    private final User user ;

    public User getUser()
    {
	return user ;
    }

    public PostTooBigException(int size)
    {
	this(size,null) ;
    }

    public PostTooBigException(int size, User user)
    {
	super("POST too big: " + size + " bytes") ;
	this.user = user ;
    }

}
