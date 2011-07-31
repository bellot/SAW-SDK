package org.saw.elements ;

import java.io.* ;

import org.saw.transaction.* ;
import org.saw.util.updatable.* ;

/** This is a dynamic element which output is cached into a file,
 *  implements <code>UpdatableFriend</code>.
 *  @author  Patrick Bellot, &copy; 2010 and later.
 */

public abstract class DynamicElementWithFileCache extends DynamicElement
    implements UpdatableFriend
{
    /** Method to be implemented by concrete cached elements. 
     *  @param out where to write the output, must not be closed.
     */
    public abstract void updateCache(PrintWriter out)
	throws Exception ;

    /** Name of the file for caching. */
    private final String fileName ;

    /* Updatable friend */
    private boolean needsUpdate = true ;

    /* Updatable friend */
    public final void requiresUpdate() { needsUpdate = true ; }

    /** Constructor. */
    protected DynamicElementWithFileCache()
    {
	String pathName = "cache" + File.separatorChar + (getClass().getName().replace('.',File.separatorChar)) ;

	(new File(pathName)).mkdirs() ;

	this.fileName = pathName + File.separatorChar + "cache" ;
    }

    /** Output function, renew the cache if necessary and output it. */
    public void writeTo(TransactionOutput transactionOutput) 
	throws Exception
    {
	synchronized(this) {

	    if (needsUpdate) {

		PrintWriter out = new PrintWriter(new FileOutputStream(fileName),false) ;
		updateCache(out) ;
		out.close() ;

		needsUpdate = false ;
	    }

	    transactionOutput.writeFile(fileName) ;
	}
    }

}
