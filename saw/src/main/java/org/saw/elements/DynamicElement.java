package org.saw.elements ;

import org.saw.compilations.* ;

/** The base class of all dynamic compilations.
 *  @author  Patrick Bellot, &copy; 2008 and later.
 */

public abstract class DynamicElement extends AtomicCompilation
    implements ElementInterface
{

    /** Returns a compilation with itself as single atomic compilation. */

    public final Compilation compile()
    {
	return new Compilation(this) ;
    }

}
