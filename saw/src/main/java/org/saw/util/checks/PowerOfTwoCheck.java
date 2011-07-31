package org.saw.util.checks ;

/** A function that checks that its argument is a power of two and
 *  raises an exception if not.
 *  @author  Patrick Bellot, &copy; 2008 and later.  
 */

public class PowerOfTwoCheck
{
    /** If argument <code>n</code> is not a power of two, an
     *  exception is raised with a message including the
     *  parameter <code>name</code>.
     *  @param name an informal name for the value
     *  @param n the value
     */

    static public final void check(String name, int n)
	throws PowerOfTwoException
    {
	if ((n > 0) && ((n & (n - 1)) == 0))
	    return ;

	throw new PowerOfTwoException(name + " (" + n + ")") ;
    }

}
