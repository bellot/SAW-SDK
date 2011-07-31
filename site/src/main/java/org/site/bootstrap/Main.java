package org.site.bootstrap ;

import org.saw.* ;
import org.saw.entities.* ;
import org.saw.server.* ;
import org.saw.util.* ;
import org.saw.util.logs.* ;

/** This is the main class to run the bootstrap.
 *  @author  Patrick Bellot, &copy; 2010 and later.
 */

public class Main
{

    public static void main(String[] args) 
	throws Exception
    {
	System.setProperty("java.net.preferIPv4Stack","true") ;
	System.setProperty("file.encoding",Parameters.ENCODING);

	Logs.open("SAW.log") ;
	new LogsArchiver().start() ;
	
	Logs.log(Logs.SERVER_LOG,"BootStrap starts") ;

	try {

	    Global.runtime.addShutdownHook(new OnExitThread()) ;

	    new BackupManager().start() ;

            BootStrap.bootStrap() ;

	} catch (Exception e) {

	    Logs.log(Logs.SERVER_ERROR,"BootStrap error",e) ;

	}

	Logs.log(Logs.SERVER_LOG,"BooStrap stops") ;
	Logs.close() ;

	System.exit(0) ;
    }

}
