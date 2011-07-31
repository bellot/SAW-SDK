package org.saw.elements ;

import org.saw.compilations.* ;

/** A constant string element.
 *  @author  Patrick Bellot, &copy; 2008 and later. 
 */

public class CDATA 
    implements ElementInterface
{

    private byte[] content ;

    public CDATA(String content)
    {
	this(content.getBytes()) ;
    }

    public CDATA (byte[] content)
    {
	this.content = content ;
    }

    public final Compilation compile()
    {
	return new Compilation(new BytesCompilation(content)) ;
    }

}
