package org.saw.util.files;

import java.io.* ;
import java.util.zip.* ;

/** Static functions to gzip a plain file.
 *  @author Copyright Patrick Bellot, 2009 and later.
 */

public final class GZipFile
{
    /** Gzip file <code>src</code> content and write it to file <code>dst</code>. */

    static public final void zip(String src, String dst)
	throws Exception
    {
	BufferedInputStream in  = new BufferedInputStream(new FileInputStream(src)) ;
	GZIPOutputStream    out = new GZIPOutputStream(new FileOutputStream(dst)) ;

	byte[] buf = new byte[1024];
	int    len ;
	    
	while ((len = in.read(buf)) > 0)
	    out.write(buf, 0, len);
	    
	in.close() ;

	out.finish() ;
	out.close() ;
    }

}
