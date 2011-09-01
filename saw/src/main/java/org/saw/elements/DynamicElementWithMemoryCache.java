package org.saw.elements ;

import java.io.* ;

import org.saw.transaction.* ;
import org.saw.sessions.* ;
import org.saw.util.bytes.* ;
import org.saw.util.updatable.* ;

/** This is a dynamic element which output is cached into memory,
 *  implements <code>UpdatableFriend</code>.
 *  @author  Patrick Bellot, &copy; 2010 and later.
 */

public abstract class DynamicElementWithMemoryCache extends DynamicElement
    implements UpdatableFriend
{
    /** Method to be implemented by concrete cached elements. 
     *  @param out where to write the output, must not be closed.
     */
    public abstract void updateCache(TransactionOutputFile out)
	throws Exception ;

    /** Name of the file for caching. */
    private final String fileName ;

    /* Updatable friend */
    protected boolean needsUpdate = true ;

    /* Updatable friend */
    public final void requiresUpdate() { needsUpdate = true ; }

    /** Constructor. */
    protected DynamicElementWithMemoryCache()
    {
	String pathName = "cache" + File.separatorChar + (getClass().getName().replace('.',File.separatorChar)) ;

	(new File(pathName)).mkdirs() ;

	this.fileName = pathName + File.separatorChar + "cache" ;
    }

    /* Output function */

    private byte[] byteArray ;

    public void writeTo(TransactionOutput transactionOutput) 
	throws Exception
    {
	synchronized(this) {

	    if (needsUpdate) {

		TransactionOutputFile out = new TransactionOutputFile(fileName) ;
                out.beginOutput() ;
		updateCache(out) ;
                out.endOutput() ;

		byteArray = FileBytes.getBytesFromFile(fileName) ;

		needsUpdate = false ;
	    }

	    transactionOutput.write(byteArray) ;
	}
    }

}
