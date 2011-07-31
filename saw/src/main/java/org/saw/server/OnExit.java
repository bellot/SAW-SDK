package org.saw.server ;

import org.saw.entities.* ;

/** The static method <code>onExit()</code> is executed at the shutdown of the server. 
 *  @author  Patrick Bellot, &copy; 2009 and later. 
 */

public class OnExit 
{

    public static final void onExit()
	throws Exception
    {
	BackupManager.hibernateAll() ;
    }

}
