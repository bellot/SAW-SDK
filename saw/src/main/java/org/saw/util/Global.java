package org.saw.util ;

import org.saw.util.random.* ;

/** Some global values.
 *  @author  Patrick Bellot, &copy; 2008 and later. 
 */

public class Global
{
    /** Extended and initializer random generator. */
    static public final RandomExt randomExt = new RandomExt() ;

    /** The runtime object. */
    static public final Runtime runtime = Runtime.getRuntime() ;
}
