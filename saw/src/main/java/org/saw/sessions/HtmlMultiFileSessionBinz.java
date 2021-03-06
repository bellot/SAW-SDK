package org.saw.sessions ;

import java.io.* ;

import org.saw.transaction.* ;
import org.saw.util.compacters.* ;
import org.saw.util.files.* ;

/** Delivers a HTML file with file caching.
 *  @author  Patrick Bellot, &copy; 2008 and later. 
 */

public class HtmlMultiFileSessionBinz extends SessionBinz
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

    /** @param requestPaths full request paths starting with <code>public_html/</code>. */

    public HtmlMultiFileSessionBinz(boolean doCompact, String... requestPaths)
	throws Exception
    {
	this.doCompact    = doCompact ;
	this.requestPaths = requestPaths ;
	this.count        = requestPaths.length ;

        String basePath
            = "public_html"
            + File.separatorChar
            + (this.getClass().getPackage().getName().replace('.',File.separatorChar))
            + File.separatorChar ;

        String pathName = "cache" + File.separatorChar + basePath ;

	(new File(pathName)).mkdirs() ;

        pathName += File.separatorChar ;

	htmlFileName = pathName + "cache.html" ;
	downFileName = pathName + "cache.down" ;
	cpctFileName = pathName + "cache.cpct" ;
	gzipFileName = pathName + "cache.gzip" ;

	this.files         = new File[count] ;
	this.cpctFilePaths = new String[count] ;

	for (int i = 0 ; i < count ; i++) {

	    this.requestPaths[i]  = basePath + (this.requestPaths[i].replace('/',File.separatorChar)) ;

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
                transaction.sendHtmlDownloadNoExpireNoCookieHeaderOkGzip(fileName) ;
                transaction.writeFile(downFileName) ;
            } else {
                transaction.sendHtmlDownloadNoExpireNoCookieHeaderNoGzip(fileName) ;
                transaction.writeFile(htmlFileName) ;
            }
        }
    }

}
