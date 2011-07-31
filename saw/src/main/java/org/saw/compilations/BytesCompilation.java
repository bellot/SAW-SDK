package org.saw.compilations ;

import org.saw.transaction.* ;

/** An atomic compilation containing an array of bytes ready to be output. 
 *  @author  Patrick Bellot, &copy; 2008 and later. 
 */

public final class BytesCompilation extends AtomicCompilation
{
    /** The bytes array ready to be output. */
    private final byte[] content ;

    /** Constructor.
     *  @param content the bytes array ready to be output
     */
    public BytesCompilation(byte[] content)
    {
	this.content = content ;
    }

    /** Constructor.
     *  @param content a string to be converted in a bytes array ready to be output
     */
    public BytesCompilation(String content)
    {
	this(content.getBytes()) ;
    }

    /** Writes the array of bytes to the output. */
    public final void writeTo(TransactionOutput transactionOutput)
	throws Exception
    {
	transactionOutput.write(content) ;
    }

    /** Returns <code>true</code>, redefined from <code>AtomicCompilation</code>.
     *  @see org.saw.compilations.AtomicCompilation
     */
    public final boolean isBytesCompilation()
    {
	return true ;
    }

    /** Redefined from <code>AtomicCompilation</code>. */
    public final AtomicCompilation concat(AtomicCompilation other)
    {
	return other.concatBytesAfter(this) ;
    }

    /** Returns other+this, redefined from <code>AtomicCompilation</code>. */
    public final BytesCompilation concatBytesAfter(BytesCompilation other)
    {
	/** Returns other + this. */
	
	byte[] otherContent = other.content ;
	int    otherLength  = otherContent.length ;

	byte[] thisContent  = content ;
	int    thisLength   = content.length ;

	byte[] newContent = new byte[otherLength + thisLength] ;

	System.arraycopy(otherContent, 0, newContent,           0,  otherLength) ;
	System.arraycopy(thisContent,  0, newContent, otherLength,  thisLength) ;

	return new BytesCompilation(newContent) ;	
    }

}
