package org.saw.transaction ;

import org.saw.util.structures.* ;

/** This class is the session variable container held in the
 *  <code>SessionEnvironment</code>.
 *  <br>
 *  This is a hash table which keys and values are strings. It inherits
 *  the methods of the <code>ObjObjHashTable&lt;String,Object&gt;</code>.
 *  <br>
 *  @see ObjObjHashTable
 *  @author  Patrick Bellot, &copy; 2008 and later.
 */

public final class SessionVariables extends ObjObjHashTable<String,Object>
{
    static final long serialVersionUID = 20101023L ;

    /** Constructor. */

    public SessionVariables()
    {
	super(16) ; // Must be a power of 2
    }

}
