package org.saw.util.beautifiers ;

import java.io.* ;

import org.saw.exceptions.* ;
import org.saw.util.logs.* ;

import com.uwyn.jhighlight.renderer.*;

/** Static function to compact JavaScript files.
 *  Requires <code>org.saw.util.bytes.BytesArray</code>.
 *  @author  Patrick Bellot, &copy; 2008 and later. 
 */

public final class JavaBeautifier
{
    /** Beautify <code>inputFilename</code> Java source, puts the result in file <code>outputFilename</code>. 
     *  @param inputFilename a file name
     *  @param outputFilename a file name
     *  @throws IOException
     */

    public static final void beautify(String inputFilename, String outputFilename) 
	throws Exception 
    {
	try {
            XhtmlRendererFactory.getRenderer("java")
                .highlight(inputFilename,
                           new File(inputFilename).toURI().toURL().openStream(),
                           new FileOutputStream(new File(outputFilename)),
                           "UTF-8",
                           false);
	} catch (Exception e) {
	    Logs.log(Logs.SERVER_ERROR_CAT, "Failed to beautify \"" + inputFilename + "\"",e) ;
	    throw new InternalErrorException(e.getMessage()) ;
	} 
    }

}
