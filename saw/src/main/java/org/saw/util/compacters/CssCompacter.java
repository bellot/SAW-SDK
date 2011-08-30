package org.saw.util.compacters ;

import java.io.* ;
import java.nio.charset.* ;

import org.saw.exceptions.* ;
import org.saw.util.logs.* ;

import com.yahoo.platform.yui.compressor.* ;

/** Static function to compact JavaScript files.
 *  Requires <code>org.saw.util.bytes.BytesArray</code>.
 *  @author  Patrick Bellot, &copy; 2008 and later. 
 */

public final class CssCompacter
{
    /** Compacts file <code>inputFilename</code>, puts the result in file <code>outputFilename</code>. 
     *  @param inputFilename a file name
     *  @param outputFilename a file name
     *  @throws IOException
     */

    public static final void compact(String inputFilename, String outputFilename) 
	throws Exception 
    {
	Reader in = null;
	Writer out = null;

	try {

	    in = new InputStreamReader(new FileInputStream(inputFilename),Options.charset);
 
	    CssCompressor compressor = new CssCompressor(in);
 
	    out = new OutputStreamWriter(new FileOutputStream(outputFilename),Options.charset);
	    compressor.compress(out,Options.lineBreakPos);

            in.close() ;
            out.close() ;

	} catch (Exception e) {
	    
	    try { in.close()  ; } catch(Exception e2) {} ;
	    try { out.close() ; } catch(Exception e2) {} ;
	    
	    Logs.log(Logs.SERVER_ERROR_CAT, "Failed to compact \"" + inputFilename + "\"", e) ;
	    throw new InternalErrorException(e.getMessage()) ;
	} 
    }

    private static class Options 
    {
	public static final Charset charset = Charset.forName("UTF-8");
	public static final int     lineBreakPos = -1;
    }

}