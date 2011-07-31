package org.saw.util.compacters ;

import java.io.* ;
import java.nio.charset.* ;

import org.saw.exceptions.* ;
import org.saw.util.logs.* ;

import org.mozilla.javascript.* ;

import com.yahoo.platform.yui.compressor.* ;

/** Static function to compact JavaScript files.
 *  Requires <code>org.saw.util.bytes.BytesArray</code>.
 *  @author  Patrick Bellot, &copy; 2008 and later. 
 */

public final class JsCompacter
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
 
	    JavaScriptCompressor compressor = new JavaScriptCompressor(in, new YuiCompressorErrorReporter());
	    
	    out = new OutputStreamWriter(new FileOutputStream(outputFilename),Options.charset);
	    compressor.compress(out,Options.lineBreakPos,Options.munge,Options.verbose,Options.preserveAllSemiColons,Options.disableOptimizations);
	    in.close() ;
            out.close() ;

	} catch (Exception e) {
	    
	    try { in.close()  ; } catch(Exception e2) {} ;
	    try { out.close() ; } catch(Exception e2) {} ;
	    
	    Logs.log(Logs.SERVER_ERROR,"Failed to compact \"" + inputFilename + "\"",e) ;
	    throw new InternalErrorException(e.getMessage()) ;
	} 
    }

    private static class Options 
    {
	public static final Charset charset = Charset.forName("UTF-8");
	public static final int     lineBreakPos = -1;
	public static final boolean munge = true;
	public static final boolean verbose = false;
	public static final boolean preserveAllSemiColons = false;
	public static final boolean disableOptimizations = false;
    }

    private static class YuiCompressorErrorReporter 
	implements ErrorReporter 
    {
    
	public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) 
	{
	    try {
		if (line < 0) {
		    Logs.log(Logs.SERVER_WARNING, message);
		} else {
		    Logs.log(Logs.SERVER_WARNING, line + ':' + lineOffset + ':' + message);
		}
	    } catch (Exception e) {}
	}
 
	public void error(String message, String sourceName, int line, String lineSource, int lineOffset) 
	{
	    try {
		if (line < 0) {
		    Logs.log(Logs.SERVER_ERROR, message);
		} else {
		    Logs.log(Logs.SERVER_ERROR, line + ':' + lineOffset + ':' + message);
		}
	    } catch (Exception e) {}
	}
 
	public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) 
	{
	    error(message, sourceName, line, lineSource, lineOffset);
	    return new EvaluatorException(message);
	}
    }
}