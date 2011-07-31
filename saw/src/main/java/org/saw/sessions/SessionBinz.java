package org.saw.sessions ;

import org.saw.transaction.* ;

/** The base class of Session Binz.
 *  @author  Patrick Bellot, &copy; 2009 and later. 
*/

public abstract class SessionBinz
{
    public static final int CLASS_TYPE           = 0 ;
    public static final int HTML_TYPE            = 1 ;
    public static final int HTML_TYPE_NO_COMPACT = 2 ;
    public static final int CSS_TYPE             = 3 ;
    public static final int CSS_TYPE_NO_COMPACT  = 4 ;
    public static final int JS_TYPE              = 5 ;
    public static final int JS_TYPE_NO_COMPACT   = 6 ;
    public static final int JAVA_TYPE            = 7 ;
    public static final int DDL_TYPE             = 8 ;
    public static final int DATA_TYPE            = 9 ;

    /** The business logic method. */

    public abstract void handle(Transaction transaction)
	throws Exception ;

}
