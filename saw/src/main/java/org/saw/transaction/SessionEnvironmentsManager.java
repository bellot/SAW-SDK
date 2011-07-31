package org.saw.transaction ;

import org.saw.* ;
import org.saw.util.* ;
import org.saw.util.random.* ;
import org.saw.util.structures.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class SessionEnvironmentsManager
{
    /** Runs the session environment cleaner. */
    static {
	new SessionEnvironmentsCleaner().start() ;
    }
	
    /** The hash table holding all session environments where keys are session environment id. */

    private final static IntObjHashTable<SessionEnvironment> hashTable 
	= new IntObjHashTable<SessionEnvironment>(2 * Parameters.MAX_USERS_COUNT) ; // Must be a power of 2

    /** Returns session environment from session environment id. */

    public final static SessionEnvironment getSessionEnvironment(int id)
    {
	return hashTable.get(id) ;
    }

    /** Returns a new session environment (with a free session id. */

    public final static SessionEnvironment create(String ip)
    {
	RandomExt          randomExt = Global.randomExt ;
	SessionEnvironment result ;

	synchronized (hashTable) {

	    int       sessionId ;

	    while (hashTable.get(sessionId = randomExt.nextRandomId()) != null) { 
		try {
		    Thread.sleep(10) ;
		} catch (Exception e) {}
	    }

	    result = new SessionEnvironment(sessionId,ip) ;

	    hashTable.put(sessionId,result) ;
	}

	return result ;
    }

    /** Recovers session environment if it exists and ip corresponds, returns <code>null</code> otherwise. */

    public final static SessionEnvironment get(int sessionId, String ip)
    {
	synchronized (hashTable) {

	    SessionEnvironment sessionEnvironment = hashTable.get(sessionId) ;

	    if (sessionEnvironment != null && sessionEnvironment.ip.equals(ip))
		    return sessionEnvironment ;

	    return null ;
	}
    }

    /** Removes expired sesssions. */

    public final static void cleanup()
    {
	synchronized (hashTable) {

	    hashTable.resetIteration() ;

	    IntObjHashTable<SessionEnvironment>.KeyValueEntry entry ;

	    while ((entry = hashTable.nextIteration()) != null) {

		SessionEnvironment sessionEnvironment = entry.getValue() ;

		if (sessionEnvironment != null && sessionEnvironment.expired())
		    hashTable.remove(entry) ;
	    }
	}
    }


}
