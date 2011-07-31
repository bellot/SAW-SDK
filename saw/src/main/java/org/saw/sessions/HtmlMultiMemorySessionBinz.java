package org.saw.sessions ;

import java.io.* ;

import org.saw.transaction.* ;
import org.saw.util.bytes.* ;
import org.saw.util.compacters.* ;
import org.saw.util.files.* ;

/** Delivers a HTML file with memory caching.
 *  @author  Patrick Bellot, &copy; 2008 and later. 
 */

public class HtmlMultiMemorySessionBinz extends SessionBinz
{
    private final String[]  requestPaths ;
    private final String[]  cpctFilePaths ;
    private final int       count ;
    private final String    htmlFileName ;
    private final String    downFileName ;
    private final String    cpctFileName ;
    private final String    gzipFileName ;
    private final File[]    files ;
    private final boolean   doCompact ;
    private       byte[]    cpctByteArray ;
    private       byte[]    gzipByteArray ;

    /** @param requestPaths full request paths starting with <code>public_html/</code>. */

    public HtmlMultiMemorySessionBinz(boolean doCompact, String... requestPaths)
	throws Exception
    {
	this.doCompact    = doCompact ;
	this.requestPaths = requestPaths ;
	this.count        = requestPaths.length ;

	String pathName = 
	    "cache" 
	    + File.separatorChar + "public_html"
	    + File.separatorChar + (getClass().getName().replace('.',File.separatorChar)) ;

	(new File(pathName)).mkdirs() ;

	htmlFileName = pathName + File.separatorChar + "cache.html" ;
	downFileName = pathName + File.separatorChar + "cache.down" ;
	cpctFileName = pathName + File.separatorChar + "cache.cpct" ;
	gzipFileName = pathName + File.separatorChar + "cache.gzip" ;

	this.files         = new File[count] ;
	this.cpctFilePaths = new String[count] ;

	for (int i = 0 ; i < count ; i++) {

	    this.requestPaths[i]  = this.requestPaths[i].replace('/',File.separatorChar) ;

	    this.files[i]         = new File(this.requestPaths[i]) ;

	    pathName              =  "cache" + File.separatorChar + this.requestPaths[i] ;
	    (new File(pathName)).mkdirs() ;

	    this.cpctFilePaths[i] =  pathName + File.separatorChar + "cache.cpct" ;
	}
    }

    private long lastAccessMillis = 0L ;

    public final  void handle(Transaction transaction)
	throws Exception
    {
	boolean needsUpdate = false ;

	for (int i = 0 ; i < count ; i++) {
	    if (lastAccessMillis <= files[i].lastModified()) {
		needsUpdate = true ;
		break ;
	    }
	}

	if (needsUpdate) {
	    synchronized (this) {

		needsUpdate = false ;
		
                for (int i = 0 ; i < count ; i++) {
		    if (lastAccessMillis <= files[i].lastModified()) {
			HtmlCompacter.compact(requestPaths[i],cpctFilePaths[i]) ;
			needsUpdate = true ;
		    }
		}

		if (needsUpdate) {

		    FilesConcat.concat(cpctFilePaths,htmlFileName) ;

		    if (doCompact) {
			HtmlCompacter.compact(htmlFileName,cpctFileName) ;
		    } else {
			FileCopy.cp(htmlFileName,cpctFileName) ;
		    }

		    GZipFile.zip(cpctFileName,gzipFileName) ;
		    GZipFile.zip(htmlFileName,downFileName) ;

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
            if (transaction.getGzipOk()) {
                transaction.sendHtmlDownloadNoExpireNoCookieHeaderOkGzip(fileName) ;
                transaction.writeFile(downFileName) ;
            } else {
                transaction.sendHtmlDownloadNoExpireNoCookieHeaderNoGzip(fileName) ;
                transaction.writeFile(htmlFileName) ;
            }
        }
    }

}
