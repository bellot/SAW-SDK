package org.saw.util.net ;

import java.net.* ;

import org.saw.util.checks.* ;
import org.saw.util.logs.* ;

/** This is a synchronized sockets queue used by the server sockets
 *  of class <code>HttpServer</code> and <code>HttpsServer</code>.
 *  When a conection is establised, the connection socket is pushed
 *  in the queue.
 *  @author  Patrick Bellot, &copy; 2008 and later.
 */

public final class SocketsQueue
{
    /** A name for error messages. */
    private final String queueName ;

    /** Internal array of sockets. */
    private final Socket sockets[] ;

    /** Where to take the next popped socket. */
    private       int    nextSocketIdx = 0;

    /** Where to put the next pushed socket. */
    private       int    lastSocketIdx = 0 ;

    /** The mast to be applied instead of modulo. */
    private final int    mask ;

    /** Constructor,
     *  @param queueName unformal name used for logging errors,
     *  @param length length of the queue, must be a power of 2
     */

    public SocketsQueue(String queueName, int length)
    {
	try {
	    PowerOfTwoCheck.check(queueName + " sockets queue length", length) ;
	} catch (PowerOfTwoException e) {
	    e.printStackTrace() ;
            System.exit(-1) ;
	}

	this.queueName = queueName ;
	this.mask       = length - 1 ;
	this.sockets    = new Socket[length] ;
    }

    /** Pops a socket from the queue (blocking),
     *  returns next socket in the queue
     */

    public final synchronized Socket pop()
    {
	Socket result = null ;

	try {

	    do {
		if (nextSocketIdx == lastSocketIdx) {
		    wait() ; // The queue is empty
		} else {
		    result = sockets[nextSocketIdx] ;
		    nextSocketIdx = ((nextSocketIdx + 1) & mask) ;
		    notify() ;
		    break ;
		}
	    } while(true) ;

	} catch (Exception e) {
	    try {
		Logs.log(Logs.SERVER_ERROR, queueName + " sockets queue pop error", e) ;
	    } catch (Exception e2) {}
	}

	return result ;
    }

    /** Pushes a socket in the queue (blocking). 
     *  @param socket a socket to be pushed in the queue
     */

    public final synchronized void push(Socket socket)
    {
	try {

	    do {
		int futureIdx = ((lastSocketIdx + 1) & mask) ;
		if (futureIdx == nextSocketIdx) {
		    wait() ; // The queue is full
		} else {
		    sockets[lastSocketIdx] = socket ;
		    lastSocketIdx = futureIdx ;
		    notify() ;
		    return ;
		}
	    } while (true) ;

	} catch (Exception e) {
	    try {
		Logs.log(Logs.SERVER_ERROR,queueName + " sockets queue push error",e) ;
	    } catch (Exception e2) {}
	}
    }

}