package org.saw.sessions ;

import java.io.* ;

import org.saw.transaction.* ;
import org.saw.util.files.* ;
import org.saw.util.updatable.* ;

/** This a Session Binz that caches its output in a disk file.
 *  @author  Patrick Bellot, &copy; 2010 and later. */

public abstract class SessionBinzWithUpdatableFileCache extends SessionBinz
    implements UpdatableFriend
{
    private final String cpctFileName ;
    private final String gzipFileName ;

    public SessionBinzWithUpdatableFileCache()
    {
	String pathName 
	    = "cache" + File.separatorChar
	    + "public_html" +File.separatorChar 
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
		
		    needsUpdate = false ;
		}
	    }
	}

	transaction.sendHtmlNoExpireHeader() ;
	transaction.writeFile((transaction.getGzipOk()) ? gzipFileName : cpctFileName) ;
    }

}
