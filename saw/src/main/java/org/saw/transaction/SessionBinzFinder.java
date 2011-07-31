package org.saw.transaction ;

import org.saw.* ;
import org.saw.sessions.* ;
import org.saw.util.structures.* ;

/** Allows to find session binz according to the request path.
 *  @author  Patrick Bellot, &copy; 2010 and later.
 */

public final class SessionBinzFinder
{
    /** Runs the session binz cleaner. */
    static {
	new SessionBinzCleaner().start() ;
    }
	
    private static final ObjObjHashTable<String,SessionBinzRecord> hashTable 
	= new ObjObjHashTable<String,SessionBinzRecord>(2 * Parameters.MAX_PAGES_COUNT) ; // Must be a power of 2

    public static final SessionBinz getSessionBinz(String requestPath)
	throws Exception
    {
	SessionBinzRecord pageRecord = hashTable.get(requestPath) ;

	if (pageRecord != null) {
	    
	    // The page already exists

	    return pageRecord.getUpdatedSessionBinz() ;

	} else { 

	    // The page does not exist

	    pageRecord = SessionBinzLoader.load(requestPath) ;
	
	    if (pageRecord != null) {
		
		hashTable.put(requestPath,pageRecord) ;
		
		return pageRecord.getSessionBinz() ;
	    }
	}

	return null ;
    }

    /** Removes session binz that depassed the time out.
     *  @see org.saw.Parameters#PAGE_TIMEOUT
     */

    public static final void cleanup()
    {
	long now = System.currentTimeMillis() ;

	hashTable.resetIteration() ;

	ObjObjHashTable<String,SessionBinzRecord>.KeyValueEntry entry ;

	while ((entry = hashTable.nextIteration()) != null) {
	    SessionBinzRecord sessionBinzRecord = entry.getValue() ;
	    if (sessionBinzRecord.getSessionType() != SessionBinz.CLASS_TYPE) {
		if (now - entry.getValue().getLastAccess() > Parameters.PAGE_TIMEOUT)
		hashTable.remove(entry) ;
	    }
	}
    }

}
