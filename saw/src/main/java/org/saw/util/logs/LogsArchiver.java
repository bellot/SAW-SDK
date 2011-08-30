package org.saw.util.logs ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class LogsArchiver extends Thread
{

    public static final int CHECKING_INTERVAL_MILLIS = 3 * 60 * 60 * 1000 ; // 3 hours

    public final void run()
    {
	setPriority(Thread.MIN_PRIORITY) ;

	while (true) {
	    try { 

		sleep(CHECKING_INTERVAL_MILLIS) ; 
		Logs.archiveIfNecessary() ;
		
	    } catch (Exception e) {
		try {
		    Logs.log(Logs.SERVER_ERROR_CAT, "Logs archiver failed",e) ;
		} catch (Exception e2) {}
	    }
	}
    }
}

    
