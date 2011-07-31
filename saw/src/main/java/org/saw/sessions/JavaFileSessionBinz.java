package org.saw.sessions ;

import java.io.* ; 

import org.saw.transaction.* ;
import org.saw.util.beautifiers.* ;
import org.saw.util.compacters.* ;
import org.saw.util.files.* ;

/** Delivers a Java file with file caching.
 *  @author  Patrick Bellot, &copy; 2008 and later. 
 */

public final class JavaFileSessionBinz extends SessionBinz
{
    
    private final String  requestPath ;
    private final String  javaFileName ;
    private final String  downFileName ;
    private final String  cpctFileName ;
    private final String  gzipFileName ;
    private final File    file ;

    /** @param requestPath full request path starting with <code>public_html/</code>. */

    public JavaFileSessionBinz(String requestPath)
	throws Exception
    {
	this.requestPath = requestPath.replace('/',File.separatorChar) ;
	this.file        = new File(this.requestPath) ;

	String pathName = "cache" + File.separatorChar + this.requestPath ;

	(new File(pathName)).mkdirs() ;

	javaFileName = pathName + File.separatorChar + "cache.java" ;
	downFileName = pathName + File.separatorChar + "cache.down" ;
	cpctFileName = pathName + File.separatorChar + "cache.cpct" ;
	gzipFileName = pathName + File.separatorChar + "cache.gzip" ;
    }

    private long lastAccessMillis = 0L ;

    public final  void handle(Transaction transaction)
	throws Exception
    {
	if (lastAccessMillis <= file.lastModified()) {

	    synchronized (this) {
		if (lastAccessMillis <= file.lastModified()) {

		    JavaBeautifier.beautify(requestPath,javaFileName) ;
                    HtmlCompacter.compact(javaFileName,cpctFileName) ;

		    GZipFile.zip(cpctFileName,gzipFileName) ;
		    GZipFile.zip(requestPath, downFileName) ;

		    lastAccessMillis = System.currentTimeMillis() ;
		}
	    }
	}

        String fileName = transaction.getTransactionVariable("download_as") ;

        if (fileName == null) {
            if (transaction.getGzipOk()) {
                transaction.sendHtmlNoExpireNoCookieHeaderOkGzip() ;
                transaction.writeFile(gzipFileName) ;
            } else {
                transaction.sendHtmlNoExpireNoCookieHeaderNoGzip() ;
                transaction.writeFile(cpctFileName) ;
            }
        } else {
            if (transaction.getGzipOk()) {
                transaction.sendMimeDownloadNoExpireNoCookieHeaderOkGzip(textJavaMimeType,fileName) ;
                transaction.writeFile(downFileName) ;
            } else {
                transaction.sendMimeDownloadNoExpireNoCookieHeaderNoGzip(textJavaMimeType,fileName) ;
                transaction.writeFile(requestPath) ;
            }
        }
    }

    private static final byte[] textJavaMimeType = "text/java".getBytes() ;

}
