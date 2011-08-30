package org.saw.server ;

import org.saw.util.logs.* ;

/** This thread is responsible for executing the 
 *  <code>OnExit.onExit()</code> at the shutdown of the server.
 *  @author  Patrick Bellot, &copy; 2008 and later. */

public class OnExitThread extends Thread
{

    public void run()
    {
	try {
	    Logs.log(Logs.SERVER_LOG_CAT,"Server on exit procedure start") ;
	} catch (Exception elog) {
	    System.out.println("Server on exit procedure start") ;
	}

	try {
	    OnExit.onExit() ;

	    try {
		Logs.log(Logs.SERVER_LOG_CAT,"Server on exit procedure done") ;
	    } catch (Exception elog) {
		System.out.println("Server on exit procedure done") ;
	    }
	} catch (Exception e) {
	    try {
		Logs.log(Logs.SERVER_ERROR_CAT,"Server on exit procedure error",e) ;
	    } catch (Exception elog) {
		System.out.println("Server on exit procedure error") ;
	    }
	}
    }

}

