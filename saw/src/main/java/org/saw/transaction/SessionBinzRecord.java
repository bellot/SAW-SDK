package org.saw.transaction ;

import java.io.* ;

import org.saw.sessions.* ;

/** A record object for each loaded class.
 *  @author  Patrick Bellot, &copy; 2008 and later. 
 */

public class SessionBinzRecord
{
    private final String       requestPath ;
    private final File         file ;
    private final String       mimeType ;
    private       SessionBinz  sessionBinz ;
    private       long         lastUpdated ;
    private final int          sessionType ;
    private       long         lastAccess ;

    /** Constructor.
     *  @param requestPath the request path corresponding to the Session Binz
     *  @param file        the <code>File</code> object corresponding to the Session Binz source
     *  @param mimeType    the mime type of the Session Binz source
     *  @param sessionBinz the Session Binz (may be renewed later)
     *  @param sessionType as defined in <code>org.saw.sessions.SessionBinz</code>
     *  @see org.saw.sessions.SessionBinz
     */

    public SessionBinzRecord(String      requestPath,
			     File        file,
			     String      mimeType,
			     SessionBinz sessionBinz,
			     int         sessionType)
    {
	this.requestPath = requestPath ;
	this.file        = file ;
	this.mimeType    = mimeType ;
	this.sessionBinz = sessionBinz ;
	this.sessionType = sessionType ;

	this.lastUpdated 
	    = this.lastAccess  
	    = System.currentTimeMillis() ;
    }

    /** Returns the session type. */

    public final int getSessionType()
    {
	return sessionType ;
    }

    /** Returns the Session Binz and update the last access time. */

    public final SessionBinz getSessionBinz()
    {
	lastAccess  = System.currentTimeMillis() ;
	return sessionBinz ;
    }

    /** Returns the last access time. */

    public final long getLastAccess()
    {
	return lastAccess ;
    }

    /** Updates the Session Binz if source has been modified, 
     *  update the last access time and returns the Session Binz. 
     */

    public final SessionBinz getUpdatedSessionBinz()
	throws Exception
    {
        if (file.exists()) {
        
            if (sessionType == SessionBinz.CLASS_TYPE) {

                if (lastUpdated > file.lastModified()) {
                    lastAccess  = System.currentTimeMillis() ;
                    return sessionBinz ;
                }

                sessionBinz = SessionBinzLoader.load(file,requestPath,mimeType,sessionType) ;

                lastUpdated 
                    = lastAccess
                    = System.currentTimeMillis() ;
            }
                
            return sessionBinz ;
        }

        sessionBinz = null ;

	lastUpdated 
	    = lastAccess
	    = 0 ;

	return null ;
    }

}
