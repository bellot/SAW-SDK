package org.saw.util.encoding ;

import org.saw.util.structures.* ;

/** A function to get mime type from file suffix.
 *  Requires <code>org.saw.util.structures.ObjObjHashTable</code>.
 *  @author Copyright Patrick Bellot, 2009 and later.
 */


/** @author  Patrick Bellot, &copy; 2008 and later. */

public final class MimeTypes
{

    private static final ObjObjHashTable<String,String> mimeTypes 
	= new ObjObjHashTable<String,String>(32) ; // Must be a power of 2.

    static {
	    mimeTypes.put("class", "java/class") ;
	    mimeTypes.put("html",  "text/html") ;
	    mimeTypes.put("HTML",  "text/html") ;
	    mimeTypes.put("css",   "text/css") ;
	    mimeTypes.put("CSS",   "text/css") ;
	    mimeTypes.put("js",    "text/javascript") ;
	    mimeTypes.put("JS",    "text/javascript") ;
	    mimeTypes.put("ico",   "image/x-icon") ;
	    mimeTypes.put("gif",   "image/gif") ;
	    mimeTypes.put("jpg",   "image/jpeg") ;
	    mimeTypes.put("jpeg",  "image/jpeg") ;
	    mimeTypes.put("png",   "image/png") ;
	    mimeTypes.put("pdf",   "application/pdf") ;
	    mimeTypes.put("zip",   "application/zip") ;
	    mimeTypes.put("doc",   "application/msword") ;
	    mimeTypes.put("dot",   "application/msword") ;
	    mimeTypes.put("docx",  "application/msword") ;
	    mimeTypes.put("xls",   "application/excel") ;
	    mimeTypes.put("xlsx",  "application/excel") ;
	    mimeTypes.put("java",  "text/java") ;
            mimeTypes.put("ddl",   "file/doawload") ;
    }

    /** Returns mime type from file suffix or <code>null</code> if not recognized. 
     *  @param suffix a file suffix
     */

    public static final String get(String suffix)
    {
	return mimeTypes.get(suffix) ;
    }

}
