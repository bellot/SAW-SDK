package org.saw.compilations ;

import org.saw.transaction.* ;

/** The base class of all atomic compilation.
 *  An atomic compilation can be:<br>
 *  - an array of bytes<br>
 *  - a call to a dynamic element method<br>
 *  @see org.saw.compilations.BytesCompilation
 *  @see org.saw.elements.DynamicElement
 *  @author  Patrick Bellot, &copy; 2010 and later. 
 */

public abstract class AtomicCompilation
{
    /** Writes its output. */
    public abstract void writeTo(TransactionOutput transactionOuput) 
	throws Exception ;

    /** Returns <code>false</code>, redefined in <code>DynamicElement</code>. */
    public boolean isBytesCompilation()
    {
	return false ;
    }

    /** Returns <code>null</code>, redefined in <code>BytesCompilation</code>. */
    public AtomicCompilation concat(AtomicCompilation other) 
    { 
	return null ; 
    }

    /** Returns <code>null</code>, redefined in <code>BytesCompilation</code>. */
    public BytesCompilation concatBytesAfter(BytesCompilation other) 
    { 
	return null ; 
    }

}
