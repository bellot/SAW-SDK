package org.saw.sessions ;

import java.io.* ;

import org.saw.transaction.* ;
import org.saw.util.bytes.* ;
import org.saw.util.files.* ;

/** This a HTML Session Binz that caches its output in memory.
 *  @author  Patrick Bellot, &copy; 2010 and later. */

public abstract class SessionBinzWithMemoryCache extends SessionBinz
{
    private       byte[] cpctByteArray ;
    private       byte[] gzipByteArray ;

    public SessionBinzWithMemoryCache()
        throws Exception
    {
	String pathName 
	    = "cache" + File.separatorChar
	    + "public_html" + File.separatorChar
	    + (getClass().getName().replace('.',File.separatorChar)) ;

	(new File(pathName)).mkdirs() ;

	String cpctFileName = pathName + File.separatorChar + "cache.cpct" ;
	String gzipFileName = pathName + File.separatorChar + "cache.gzip" ;

        TransactionOutputFile transactionOutput = new TransactionOutputFile(cpctFileName) ;
        transactionOutput.beginOutput() ;
        writeContent(transactionOutput) ;
        transactionOutput.endOutput() ;
        
        GZipFile.zip(cpctFileName,gzipFileName) ;
        
        cpctByteArray = FileBytes.getBytesFromFile(cpctFileName) ;
        gzipByteArray = FileBytes.getBytesFromFile(gzipFileName) ;
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
	transaction.sendHtmlNoExpireHeader() ;
	transaction.write((transaction.getGzipOk()) ? gzipByteArray : cpctByteArray) ;
    }

}
