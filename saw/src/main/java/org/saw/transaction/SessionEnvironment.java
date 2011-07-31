package org.saw.transaction ;

import org.saw.* ;
import org.saw.users.* ;

/** This is the class modeling the session environment.
 *  It can be extended according to the Web application needs.
 *  Here we extend it with the session variables attribute as an example.
 *  Extensions are not required by the server.
 *  Only session id and session ip address are required by the server.
 *  @author  Patrick Bellot, &copy; 2008 and later. */

public class SessionEnvironment
{
    /** Session id. */
    public final int id ;

    /** Session ip address (used against fraud). */
    public final String ip ;

    /** Constructor. 
     *  @param id session id
     *  @param ip session ip address
     */

    public SessionEnvironment(int id, String ip)
    {
	this.id               = id ;
	this.ip               = ip ;
	this.lastAccessTime   = System.currentTimeMillis() ;
    }

    /** Session user. */
    private User user ;

    /** Returns session user or <code>null</code> if client is not logged. */
    public final User getUser() { return user ; }

    /** Sets session user. */
    public final void setUser(User user) { this.user = user ; }

    /** Session variables. */
    private final SessionVariables sessionVariables = new SessionVariables() ;

    /** Get session variables object. */
    public final SessionVariables getSessionVariables() { return sessionVariables ; }

    /** Last acces time in millis (used for session timeout). */
    private long lastAccessTime ;

    /** Has the session expired ?
     *  @see org.saw.Parameters#SESSION_TIMEOUT
     */

    public final boolean expired()
    {
	return (System.currentTimeMillis() - lastAccessTime > Parameters.SESSION_TIMEOUT) ;
    }

    /** Has the session expired ? If not expired, the last access time is updated. 
     *  @see org.saw.Parameters#SESSION_TIMEOUT
     */

    public final boolean notExpiredTouch()
    {
	long currentTime = System.currentTimeMillis() ;

	if (currentTime - lastAccessTime <= Parameters.SESSION_TIMEOUT) {
	    lastAccessTime = currentTime ;
	    return true ;
	}
	
	return false ;
    }
   
}
