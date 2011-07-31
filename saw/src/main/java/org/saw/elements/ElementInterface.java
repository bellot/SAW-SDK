package org.saw.elements ;

import org.saw.compilations.* ;

/** The interface of all elements, they all have a method for compiling them. 
 *  It is implemented by:<br>
 *  - all XML elements (subclasses of <code>XMLElement</code>)<br>
 *  - all dynamic elements (subclasses of <code>DynamicElement</code>)<br>
 *  - CDATA element<br>
 *  @author  Patrick Bellot, &copy; 2010 and later.
 */

public interface ElementInterface
{

    /** Self compilation. */

    public Compilation compile() ;

}
