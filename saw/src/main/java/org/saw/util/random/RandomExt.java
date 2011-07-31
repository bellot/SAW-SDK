package org.saw.util.random ;

import java.util.* ;

/** Extends random with function <code>nextRandomId()</code>
 *  and initializes the random generator.
 *  <br>
 *  An object of the class <code>RandomExt</code> is available as a static variable <code>randomExt</code> in
 *  <code>ord.saw.Global</code>.
 *  @author  Patrick Bellot, &copy; 2010 and later. */

public final class RandomExt extends Random
{
    static final long serialVersionUID = 20101023L ;

    /** Constructor.
     *  Initializes the random generator.
     */

    public RandomExt()
    {
	super(System.nanoTime()) ;
    }

    
    /** Returns a random value between <code>100,000,000</code> and <code>999,999,999</code>. */
    public final int nextRandomId()
    {
	return (100 * 1000 * 1000) + nextInt(800 * 1000 * 1000) ;
    }


}
