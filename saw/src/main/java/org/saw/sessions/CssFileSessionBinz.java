package org.saw.sessions ;

import java.io.* ;

import org.saw.transaction.* ;
import org.saw.util.compacters.* ;
import org.saw.util.files.* ;

/** Delivers a CSS file with file caching.
 *  @author  Patrick Bellot, &copy; 2008 and later. 
 */

public final class CssFileSessionBinz extends SessionBinz
{
    private final String  requestPath ;
    private final String  cpctFileName ;
    private final String  gzipFileName ;
    private final String  downFileName ;
    private final File    file ;
    private final boolean doCompact ;

    /** @param requestPath full request path starting with <code>public_html/</code>. */

    public CssFileSessionBinz(String requestPath, boolean doCompact)
	throws Exception
    {
	this.requestPath = requestPath.replace('/',File.separatorChar) ;
	this.file        = new File(this.requestPath) ;
	this.doCompact   = doCompact ;

	String pathName = "cache" + File.separatorChar + this.requestPath ;

	(new File(pathName)).mkdirs() ;

	cpctFileName = pathName + File.separatorChar + "cache.cpct" ;
	gzipFileName = pathName + File.separatorChar + "cache.gzip" ;
	downFileName = pathName + File.separatorChar + "cache.down" ;
    }

    private long lastAccessMillis = 0L ;

    public final  void handle(Transaction transaction)
	throws Exception
    {
	if (lastAccessMillis <= file.lastModified()) {

	    synchronized (this) {
		if (lastAccessMillis <= file.lastModified()) {

		    if (doCompact) {
			CssCompacter.compact(requestPath,cpctFileName) ;
		    } else {
			FileCopy.cp(requestPath,cpctFileName) ;
		    }

		    GZipFile.zip(cpctFileName,gzipFileName) ;
		    GZipFile.zip(requestPath,downFileName) ;

		    lastAccessMillis = System.currentTimeMillis() ;
		}
	    }
	}

        String fileName = transaction.getTransactionVariable("download_as") ;

        if (fileName == null) {
            if (transaction.getGzipOk()) {
                transaction.sendCssNoExpireNoCookieHeaderOkGzip() ;
                transaction.writeFile(gzipFileName) ;
            } else {
                transaction.sendCssNoExpireNoCookieHeaderNoGzip() ;
                transaction.writeFile(cpctFileName) ;
            }
        } else {
            if (transaction.getGzipOk()) {
                transaction.sendCssDownloadNoExpireNoCookieHeaderOkGzip(fileName) ;
                transaction.writeFile(downFileName) ;
            } else {
                transaction.sendCssDownloadNoExpireNoCookieHeaderNoGzip(fileName) ;
                transaction.writeFile(requestPath) ;
            }
        }
    }

}
