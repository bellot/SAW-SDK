package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class Empty extends CDATA
{
    public final static Empty unique = new Empty() ;

    private Empty()
    {
	super("") ;
    }

}
