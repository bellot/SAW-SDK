package org.saw.util.net ;

import java.net.* ;

import org.saw.util.logs.* ;

/** This class allows to define a server socket without encryption 
 *  waiting for connection.
 *  When a client connection is successful, the connection socket is
 *  pushed in a socket queue.
 *  This object is a thread, it must be created ans started.
 *  @author  Patrick Bellot, &copy; 2008 and later.  
 */

public final class HttpServer extends Thread
{

    /** Informal name for the server used for logging errors. */
    private final String serverName ;

    /** The socket waiting for client connection. */
    private final ServerSocket serverSocket ;

    /** The connection sockets queue. */
    private final SocketsQueue socketsQueue ;

    /** Constructor.
     *  @param serverName an informal name for the server used for logging errors
     *  @param bindInterface the interface where the server socket listens to incoming connection
     *  @param port the server port
     *  @param backlog the maximum length for the queue of pending connections
     *  @param socketsQueue the queue where connection sockets are
     *                      pushed when a conenction has been established with the client
     */

    public HttpServer(String       serverName, 
		      String       bindInterface, 
		      int          port, 
		      int          backlog, 
		      SocketsQueue socketsQueue)
    {
	this.serverName    = serverName ;
	this.socketsQueue  = socketsQueue ;

	ServerSocket serverSocket = null ;

	try {

	    InetAddress bindAddr = InetAddress.getByName(bindInterface) ;

	    serverSocket = new ServerSocket(port,backlog,bindAddr) ;

	} catch(Exception e) {
	    try {
		e.printStackTrace() ;
                System.exit(-1) ;
	    } catch (Exception e2) {}
	}

	this.serverSocket = serverSocket ;
    }

    public final void run()
    {
	do {

	    try {

		socketsQueue.push(serverSocket.accept()) ;

	    } catch (Exception e) {
		try {
		    Logs.log(Logs.SERVER_ERROR_CAT, serverName + ": http socket accept error", e) ;
		}  catch (Exception e2) {}
	    }

	} while (true) ;
    }

}
