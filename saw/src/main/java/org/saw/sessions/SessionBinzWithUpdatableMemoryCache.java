package org.saw.sessions ;

import java.io.* ;

import org.saw.transaction.* ;
import org.saw.util.bytes.* ;
import org.saw.util.files.* ;
import org.saw.util.updatable.* ;

/** This a HTML Session Binz that caches its output in memory.
 *  @author  Patrick Bellot, &copy; 2010 and later. */

public abstract class SessionBinzWithUpdatableMemoryCache extends SessionBinz
    implements UpdatableFriend
{
    private final String cpctFileName ;
    private final String gzipFileName ;
    private       byte[] cpctByteArray ;
    private       byte[] gzipByteArray ;

    public SessionBinzWithUpdatableMemoryCache()
    {
	String pathName 
	    = "cache" + File.separatorChar
	    + "public_html" + File.separatorChar
	    + (getClass().getName().replace('.',File.separatorChar)) ;

	(new File(pathName)).mkdirs() ;

	cpctFileName = pathName + File.separatorChar + "cache.cpct" ;
	gzipFileName = pathName + File.separatorChar + "cache.gzip" ;
    }
   
    private boolean needsUpdate = true ;

    /** Requires cache update at next request. */
    public void requiresUpdate()
    {
	needsUpdate = true ;
    }

    /** To be implemented to produce the page content, that is everything that is normally
     *  between the <code>beginOutput()</code> and <code>endOutput()</code> calls in the
     *  <code>handle()</code> method of the Session Binz.
     */
    public abstract void writeContent(TransactionOutput transactionOutput)
	throws Exception ;

    /** Renews the cache if necessary, sends a HTML no expire header and then the cached content. */
    public void handle(Transaction transaction)
	throws Exception
    {
	if (needsUpdate) {
	    synchronized (this) {
		if (needsUpdate) {
		    TransactionOutputFile transactionOutput = new TransactionOutputFile(cpctFileName) ;
		    transactionOutput.beginOutput() ;
		    writeContent(transactionOutput) ;
		    transactionOutput.endOutput() ;

		    GZipFile.zip(cpctFileName,gzipFileName) ;

		    cpctByteArray = FileBytes.getBytesFromFile(cpctFileName) ;
		    gzipByteArray = FileBytes.getBytesFromFile(gzipFileName) ;
		
		    needsUpdate = false ;
		}
	    }
	}

	transaction.sendHtmlNoExpireHeader() ;
	transaction.write((transaction.getGzipOk()) ? gzipByteArray : cpctByteArray) ;
    }

}
