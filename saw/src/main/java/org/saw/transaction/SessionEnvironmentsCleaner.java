package org.saw.transaction ;

import org.saw.util.logs.* ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class SessionEnvironmentsCleaner extends Thread
{

    public static final int CLEANING_INTERVAL_MILLIS = 15 * 60 * 1000 ; // 15 min.

    public final void run()
    {
	setPriority(Thread.MIN_PRIORITY) ;

	while (true) {
	    try { 

		sleep(CLEANING_INTERVAL_MILLIS) ; 
		SessionEnvironmentsManager.cleanup() ;
		
	    } catch (Exception e) {
		try {
		    Logs.log(Logs.SERVER_ERROR_CAT, "Session cleanup failed", e) ;
		} catch (Exception e2) {}
	    }
	}
    }
}

    
