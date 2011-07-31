package org.saw.sessions ;

import java.io.* ;

import org.saw.transaction.* ;
import org.saw.util.files.* ;

/** Delivers a DATA file with file caching.
 *  @author  Patrick Bellot, &copy; 2008 and later. 
 */

public final class DataFileSessionBinz extends SessionBinz
{
    private final String requestPath ;
    private final byte[] mimeType ;
    private final String gzipFileName ;
    private final File   file ;

    /** @param requestPath full request path starting with <code>public_html/</code>. */

    public DataFileSessionBinz(String requestPath, String mimeType)
	throws Exception
    {
	this.requestPath = requestPath.replace('/',File.separatorChar) ;
	this.file        = new File(this.requestPath) ;
	this.mimeType    = mimeType.getBytes() ;

	String pathName = "cache" + File.separatorChar + this.requestPath ;

	(new File(pathName)).mkdirs() ;

	gzipFileName = pathName + File.separatorChar + "cache.gzip" ;
    }

    private long lastAccessMillis = 0L ;

    public final  void handle(Transaction transaction)
	throws Exception
    {
	if (lastAccessMillis <= file.lastModified()) {
	    synchronized (this) {

		if (lastAccessMillis <= file.lastModified()) {

		    GZipFile.zip(requestPath,gzipFileName) ;

		    lastAccessMillis = System.currentTimeMillis() ;
		}
	    }
	}

        String fileName = transaction.getTransactionVariable("download_as") ;

        if (fileName == null) {
            if (transaction.getGzipOk()) {
                transaction.sendMimeNoExpireNoCookieHeaderOkGzip(mimeType) ;
                transaction.writeFile(gzipFileName) ;
            } else {
                transaction.sendMimeNoExpireNoCookieHeaderNoGzip(mimeType) ;
                transaction.writeFile(requestPath) ;
            }
        } else {
            if (transaction.getGzipOk()) {
                transaction.sendMimeDownloadNoExpireNoCookieHeaderOkGzip(mimeType,fileName) ;
                transaction.writeFile(gzipFileName) ;
            } else {
                transaction.sendMimeDownloadNoExpireNoCookieHeaderNoGzip(mimeType,fileName) ;
                transaction.writeFile(requestPath) ;
            }
        }
    }

}
