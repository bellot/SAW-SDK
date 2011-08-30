package org.saw.server ;

import org.saw.* ;
import org.saw.entities.* ;
import org.saw.util.* ;
import org.saw.util.logs.* ;
import org.saw.util.net.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class Main
{

    public static void main(String[] args) 
	throws Exception
    {
	System.setProperty("java.net.preferIPv4Stack","true") ;
	System.setProperty("file.encoding",Parameters.ENCODING);

	Logs.open("SAW.log") ;
	new LogsArchiver().start() ;

	Logs.log(Logs.SERVER_LOG_CAT, "Server starts") ;

	try {

	    //OnStart.onStart() ;

	    Global.runtime.addShutdownHook(new OnExitThread()) ;

	    new BackupManager().start() ;
	    
	    SocketsQueue socketsQueue = new SocketsQueue("Main",256) ;

	    TransactionsPool transactionsPool = new TransactionsPool(socketsQueue) ;
	    transactionsPool.start() ;

	    if (Parameters.HTTP_SERVER_PORT != 0) 
		new HttpServer("HTTP server",
			       Parameters.BIND_INTERFACE,
			       Parameters.HTTP_SERVER_PORT,
			       8,
			       socketsQueue).start() ;

	    if (Parameters.HTTPS_SERVER_PORT != 0) {

		SSLInit.init(Parameters.KEYSTORE,
			     Parameters.KEYSTORE_PASSWORD) ;

		new HttpsServer("HTTPS server",
			       Parameters.BIND_INTERFACE,
			       Parameters.HTTPS_SERVER_PORT,
			       8,
			       socketsQueue).start() ;
	    }

	    transactionsPool.join() ;

	} catch (Exception e) {

	    Logs.log(Logs.SERVER_ERROR_CAT, "Server main thread error", e) ;

	}

	Logs.log(Logs.SERVER_LOG_CAT,"Server stops") ;
	Logs.close() ;

	System.exit(0) ;
    }

}
