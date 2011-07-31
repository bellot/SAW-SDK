package org.saw.util.bytes ;

import java.io.* ;
import java.util.zip.* ;

/** Static functions to gzip a byte array.
 *  @author Copyright Patrick Bellot, 2011 and later.
 */

public final class GzipBytes
{
    /** Gzip array <code>src</code> content and return the result. */

    static public final byte[] zip(byte[] src)
        throws Exception
    {
        BufferedInputStream   in  = new BufferedInputStream(new ByteArrayInputStream(src)) ;
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        GZIPOutputStream      out = new GZIPOutputStream(bos) ;
        
        byte[] buf = new byte[1024];
        int    len ;
	
        while ((len = in.read(buf)) > 0)
            out.write(buf, 0, len);
        
        in.close() ;
        
        out.finish() ;
        out.close() ;
        
        return bos.toByteArray() ;
    }

}
