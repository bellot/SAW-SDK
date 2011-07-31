package org.saw.sessions ;

import org.saw.transaction.* ;

/** This a HTML Session Binz that does a HTTP temporary redirection (307).
 *  @author  Patrick Bellot, &copy; 2010 and later. */

public abstract class SessionBinzForRedirection extends SessionBinz
{
    public abstract byte[] getLocation() ;

    public void handle(Transaction transaction)
	throws Exception
    {
	transaction.sendHttpRedirection(getLocation()) ;
    }

}
