package org.saw.sessions ;

import java.io.* ;

import org.saw.transaction.* ;
import org.saw.util.compacters.* ;
import org.saw.util.files.* ;

/** Delivers a JS file with file caching.
 *  @author  Patrick Bellot, &copy; 2008 and later. 
 */

public class JsMultiFileSessionBinz extends SessionBinz
{
    private final String[]  requestPaths ;
    private final String[]  cpctFilePaths ;
    private final int       count ;
    private final String    jsFileName ;
    private final String    downFileName ;
    private final String    cpctFileName ;
    private final String    gzipFileName ;
    private final File[]    files ;
    private final boolean   doCompact ;

    /** @param requestPaths full request paths starting with <code>public_js/</code>. */

    public JsMultiFileSessionBinz(boolean doCompact, String... requestPaths)
	throws Exception
    {
	this.doCompact    = doCompact ;
	this.requestPaths = requestPaths ;
	this.count        = requestPaths.length ;

	String pathName = 
	    "cache" 
	    + File.separatorChar + "public_js"
	    + File.separatorChar + (getClass().getName().replace('.',File.separatorChar)) ;

	(new File(pathName)).mkdirs() ;

	jsFileName = pathName + File.separatorChar + "cache.js" ;
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
			JsCompacter.compact(requestPaths[i],cpctFilePaths[i]) ;
			needsUpdate = true ;
		    }
		}

		if (needsUpdate) {
		    FilesConcat.concat(cpctFilePaths,jsFileName) ;

		    if (doCompact) {
			JsCompacter.compact(jsFileName,cpctFileName) ;
		    } else {
			FileCopy.cp(jsFileName,cpctFileName) ;
		    }

		    GZipFile.zip(cpctFileName,gzipFileName) ;
		    GZipFile.zip(jsFileName,downFileName) ;

		    lastAccessMillis = System.currentTimeMillis() ;
		}
	    }
	}
 
        String fileName = transaction.getTransactionVariable("download_as") ;

        if (fileName == null) {
            if (transaction.getGzipOk()) {
                transaction.sendJsNoExpireNoCookieHeaderOkGzip() ;
                transaction.writeFile(gzipFileName) ;
            } else {
                transaction.sendJsNoExpireNoCookieHeaderNoGzip() ;
                transaction.writeFile(cpctFileName) ;
            }
        } else {
            if (transaction.getGzipOk()) {
                transaction.sendJsDownloadNoExpireNoCookieHeaderOkGzip(fileName) ;
                transaction.writeFile(downFileName) ;
            } else {
                transaction.sendJsDownloadNoExpireNoCookieHeaderNoGzip(fileName) ;
                transaction.writeFile(jsFileName) ;
            }
        }
    }

}
