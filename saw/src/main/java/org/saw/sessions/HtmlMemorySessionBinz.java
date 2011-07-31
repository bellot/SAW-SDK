package org.saw.sessions ;

import java.io.* ;

import org.saw.transaction.* ;
import org.saw.util.bytes.* ;
import org.saw.util.beautifiers.* ;
import org.saw.util.compacters.* ;
import org.saw.util.files.* ;

/** Delivers a HTML file with memory caching.
 *  @author  Patrick Bellot, &copy; 2008 and later. 
 */

public final class HtmlMemorySessionBinz extends SessionBinz
{
    private final String requestPath ;
    private final String cpctFileName ;
    private final String gzipFileName ;
    private final String htmlFileName ;
    private final String downFileName ;
    private final String tempFileName ;
    private final File   file ;
    private final boolean doCompact ;
    private       byte[] cpctByteArray ;
    private       byte[] gzipByteArray ;
	    
    /** @param requestPath full request path starting with <code>public_html/</code>. */

    public HtmlMemorySessionBinz(String requestPath, boolean doCompact)
	throws Exception
    {
	this.requestPath = requestPath.replace('/',File.separatorChar) ;
	this.file        = new File(this.requestPath) ;
	this.doCompact   = doCompact ;

	String pathName = "cache" + File.separatorChar + this.requestPath ;

	(new File(pathName)).mkdirs() ;

	cpctFileName = pathName + File.separatorChar + "cache.cpct" ;
	gzipFileName = pathName + File.separatorChar + "cache.gzip" ;
	htmlFileName = pathName + File.separatorChar + "cache.html" ;
	downFileName = pathName + File.separatorChar + "cache.down" ;
	tempFileName = pathName + File.separatorChar + "cache.temp" ;
    }

    private long lastAccessMillis = 0L ;

    public final  void handle(Transaction transaction)
	throws Exception
    {
	if (lastAccessMillis <= file.lastModified()) {
	    synchronized (this) {

		if (lastAccessMillis <= file.lastModified()) {

		    if (doCompact) {
			HtmlCompacter.compact(requestPath,cpctFileName) ;
		    } else {
			FileCopy.cp(requestPath,cpctFileName) ;
		    }

		    GZipFile.zip(cpctFileName,gzipFileName) ;
		    GZipFile.zip(requestPath,downFileName) ;

		    cpctByteArray = FileBytes.getBytesFromFile(cpctFileName) ;
		    gzipByteArray = FileBytes.getBytesFromFile(gzipFileName) ;

		    lastAccessMillis = System.currentTimeMillis() ;
		}
	    }
	}

        String fileName = transaction.getTransactionVariable("download_as") ;

        if (fileName == null) {
            if (transaction.getGzipOk()) {
                transaction.sendHtmlNoExpireNoCookieHeaderOkGzip() ;
                transaction.write(gzipByteArray) ;
            } else {
                transaction.sendHtmlNoExpireNoCookieHeaderNoGzip() ;
                transaction.write(cpctByteArray) ;
            }
        } else {
            if (fileName.equals("display")) {
                HtmlBeautifier.beautify(requestPath,tempFileName) ;
                if (doCompact) {
                    HtmlCompacter.compact(tempFileName,htmlFileName) ;
                } else {
                    FileCopy.cp(tempFileName,htmlFileName) ;
                }
                if (transaction.getGzipOk()) {
                    GZipFile.zip(htmlFileName,downFileName) ;
                    transaction.sendHtmlNoExpireNoCookieHeaderOkGzip() ;
                    transaction.writeFile(downFileName) ;
                } else {
                    transaction.sendHtmlNoExpireNoCookieHeaderNoGzip() ;
                    transaction.writeFile(htmlFileName) ;
                }
            } else {
                if (transaction.getGzipOk()) {
                    transaction.sendHtmlDownloadNoExpireNoCookieHeaderOkGzip(fileName) ;
                    transaction.writeFile(downFileName) ;
                } else {
                    transaction.sendHtmlDownloadNoExpireNoCookieHeaderNoGzip(fileName) ;
                    transaction.writeFile(requestPath) ;
                }
            }
        }
    }

}
