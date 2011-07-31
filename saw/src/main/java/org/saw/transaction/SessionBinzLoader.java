package org.saw.transaction ;

import java.io.* ;
import java.net.* ;

import org.saw.* ;
import org.saw.sessions.* ;
import org.saw.util.encoding.* ;
import org.saw.util.logs.* ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class SessionBinzLoader
{

    /** Returns Session Binz type according to Session Binz source file suffix
     *  as defined in <code>org.saw.sessions.SessionBinz</code>
     *  @see org.saw.sessions.SessionBinz
     */

    private static final int getSessionType(String suffix)
    {
	if (suffix.equals("class"))
	    return SessionBinz.CLASS_TYPE ;

	if (suffix.equals("html"))
	    return SessionBinz.HTML_TYPE ;

	if (suffix.equals("HTML"))
	    return SessionBinz.HTML_TYPE_NO_COMPACT ;

	if (suffix.equals("css"))
	    return SessionBinz.CSS_TYPE ;

	if (suffix.equals("CSS"))
	    return SessionBinz.CSS_TYPE_NO_COMPACT ;

	if (suffix.equals("js"))
	    return SessionBinz.JS_TYPE ;

	if (suffix.equals("JS"))
	    return SessionBinz.JS_TYPE_NO_COMPACT ;

	if (suffix.equals("java"))
	    return SessionBinz.JAVA_TYPE ;

	if (suffix.equals("ddl"))
	    return SessionBinz.DDL_TYPE ;

	return SessionBinz.DATA_TYPE ;
    }

    /** Returns the Session Binz record.
     *  @param requestPath the request path relative to <code>public_html</code>
     */

    protected static final SessionBinzRecord load(String requestPath)
	throws Exception
    {
	requestPath = "public_html" + requestPath ; 

	File file = new File(requestPath) ;

        String canonicalPath = file.getCanonicalPath() ;

        if (canonicalPath.indexOf("public_html") != -1) {

            if (file.exists() && file.canRead() && file.isFile()) {

                int dot = requestPath.lastIndexOf('.') ;

                if (dot != -1) {

                    String suffix   = requestPath.substring(dot + 1) ;
                    String mimeType = MimeTypes.get(suffix) ;

                    if (mimeType != null) {

                        int sessionType = getSessionType(suffix) ;

                        SessionBinz sessionBinz = load(file,requestPath,mimeType,sessionType) ;

                        if (sessionBinz != null) {
			
                            return new SessionBinzRecord(requestPath,file,mimeType,sessionBinz,sessionType) ;

                        } 

                    } else {
                        Logs.log(Logs.SERVER_ERROR,"Session binz source file with unknown mime type",
                                 Logs.FILE_TAG,requestPath) ;
                    }
		
                } else {
                    Logs.log(Logs.SERVER_ERROR,"Session binz source file without suffix",
                             Logs.FILE_TAG,requestPath) ;
                }

            } else {
                Logs.log(Logs.SERVER_ERROR,"Session binz source file does not exist",
                         Logs.FILE_TAG,requestPath) ;
            } 

        } else {
            Logs.log(Logs.SECURITY_WARNING,"Session binz not under public_html",
                     Logs.FILE_TAG,requestPath) ;
        }

	return null ;
    }

    /** Builds a Session Binz from the Session Binz source. 
     *  @param file the <code>File</code> object corresponding to the Session Binz source
     *  @param requestPath the full request path
     *  @param mimeType the mime type of the Session Binz source
     *  @param sessionType the Session Binz type (see <code>org.saw.sessions.SessionBinz</code>)
     *  @see org.saw.sessions.SessionBinz
     */

    protected static SessionBinz load(File   file,
				      String requestPath,
				      String mimeType,
				      int    sessionType)
	throws Exception
    {
	switch (sessionType) {
	case SessionBinz.CLASS_TYPE:
	    return loadSessionBinzClass(requestPath) ;
	case SessionBinz.HTML_TYPE:
	    if (file.length() > Parameters.HTML_UPPER_LIMIT_FOR_MEMORY_CACHING)
		return new HtmlFileSessionBinz(requestPath,true) ;
	    else
		return new HtmlMemorySessionBinz(requestPath,true) ;
	case SessionBinz.HTML_TYPE_NO_COMPACT:
	    if (file.length() > Parameters.HTML_UPPER_LIMIT_FOR_MEMORY_CACHING)
		return new HtmlFileSessionBinz(requestPath,false) ;
	    else
		return new HtmlMemorySessionBinz(requestPath,false) ;
	case SessionBinz.CSS_TYPE: 
	    if (file.length() > Parameters.CSS_UPPER_LIMIT_FOR_MEMORY_CACHING)
		return new CssFileSessionBinz(requestPath,true) ;
	    else
		return new CssMemorySessionBinz(requestPath,true) ;
	case SessionBinz.CSS_TYPE_NO_COMPACT: 
	    if (file.length() > Parameters.CSS_UPPER_LIMIT_FOR_MEMORY_CACHING)
		return new CssFileSessionBinz(requestPath,false) ;
	    else
		return new CssMemorySessionBinz(requestPath,false) ;
	case SessionBinz.JS_TYPE:
	    if (file.length() > Parameters.JS_UPPER_LIMIT_FOR_MEMORY_CACHING)
		return new JsFileSessionBinz(requestPath,true) ;
	    else
		return new JsMemorySessionBinz(requestPath,true) ;
	case SessionBinz.JS_TYPE_NO_COMPACT:
	    if (file.length() > Parameters.JS_UPPER_LIMIT_FOR_MEMORY_CACHING)
		return new JsFileSessionBinz(requestPath,false) ;
	    else
		return new JsMemorySessionBinz(requestPath,false) ;
	case SessionBinz.JAVA_TYPE:
	    if (file.length() > Parameters.DATA_UPPER_LIMIT_FOR_MEMORY_CACHING)
		return new JavaFileSessionBinz(requestPath) ;
	    else
		return new JavaMemorySessionBinz(requestPath) ;
	case SessionBinz.DATA_TYPE:
	    if (file.length() > Parameters.DATA_UPPER_LIMIT_FOR_MEMORY_CACHING)
		return new DataFileSessionBinz(requestPath,mimeType) ;
	    else
		return new DataMemorySessionBinz(requestPath,mimeType) ;
	case SessionBinz.DDL_TYPE:
	    return null ;
	default:
            Logs.log(Logs.SERVER_ERROR,"Session binz source file with unprocessed session type: " + sessionType,
                     Logs.FILE_TAG,requestPath) ;
	    return null ;
	}
    }


    private static final URL[] urls ;

    static {
	URL[] tmpUrls = null ;
	try {
	    File dir = new File(System.getProperty("user.dir") + File.separator + "public_html") ;
	    URL  url = dir.toURI().toURL() ;
	    tmpUrls = new URL[]{url} ;
	} catch (Exception e) {
	    try {
		Logs.log(Logs.SERVER_ERROR, "Cannot initialize SessionBinzLoader.urls", e) ;
	    } catch (Exception e2) {}
	}
	urls = tmpUrls ;
    }	

    private static final SessionBinz loadSessionBinzClass(String requestPath)
    {
	try {

	    ClassLoader cldr = new SawURLClassLoader(urls) ;
	    String      cln  = requestPath.substring(12,requestPath.length()-6).replace('/','.') ; // 12 = forget about public_html/
	    Class<?>    cls  = cldr.loadClass(cln) ;

	    return (SessionBinz)(cls.newInstance()) ;

	} catch (Throwable e) {
	    try {
		Logs.log(Logs.SERVER_ERROR, "Cannot load session binz \"" + requestPath + "\"", e) ;
	    } catch (Exception e2) {}
	}

	return null ;
    }

}
