package org.saw.transaction ;

import org.saw.util.logs.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class SessionBinzCleaner extends Thread
{

    public static final int CLEANING_INTERVAL_MILLIS = 24 * 60 * 60 * 1000 ; // 1 day

    public final void run()
    {
	setPriority(Thread.MIN_PRIORITY) ;

	while (true) {
	    try { 

		sleep(CLEANING_INTERVAL_MILLIS) ; 
		SessionBinzFinder.cleanup() ;
		
	    } catch (Exception e) {
		try {
		    Logs.log(Logs.SERVER_ERROR_CAT,"Session binz cleanup failed",e) ;
		} catch (Exception e2) {}
	    }
	}
    }
}

    
