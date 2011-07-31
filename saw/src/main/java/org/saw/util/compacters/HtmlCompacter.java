package org.saw.util.compacters ;

import java.io.* ;

import org.saw.exceptions.* ;
import org.saw.util.files.* ;
import org.saw.util.logs.* ;

import com.googlecode.htmlcompressor.compressor.* ;
//import com.google.javascript.jscomp.* ;

/** Static function to compact HTML files.
 *  Requires <code>org.saw.util.bytes.BytesArray</code>.
 *  @author  Patrick Bellot, &copy; 2008 and later. 
 */

public final class HtmlCompacter
{
    /** Compacts file <code>inputFilename</code>, puts the result in file <code>outputFilename</code>. 
     *  @param inputFilename a file name
     *  @param outputFilename a file name
     *  @throws IOException
     */

    static public final void compact(String inputFilename, String outputFilename)
	throws Exception
    {
	String dst = "" ;

	try {

	    HtmlCompressor compressor = new HtmlCompressor();

	    //compressor.setEnabled(true);                   //if false all compression is off (default is true)
	    //compressor.setRemoveComments(true);            //if false keeps HTML comments (default is true)
	    //compressor.setRemoveMultiSpaces(true);         //if false keeps multiple whitespace characters (default is true)
	    compressor.setRemoveIntertagSpaces(true);        //removes iter-tag whitespace characters
	    compressor.setRemoveQuotes(false);               //removes unnecessary tag attribute quotes
	    compressor.setSimpleDoctype(false);              //simplify existing doctype
	    compressor.setRemoveScriptAttributes(false);     //remove optional attributes from script tags
	    compressor.setRemoveStyleAttributes(false);      //remove optional attributes from style tags
	    compressor.setRemoveLinkAttributes(false);       //remove optional attributes from link tags
	    compressor.setRemoveFormAttributes(false);       //remove optional attributes from form tags
	    compressor.setRemoveInputAttributes(false);      //remove optional attributes from input tags
	    compressor.setSimpleBooleanAttributes(false);    //remove values from boolean tag attributes
	    compressor.setRemoveJavaScriptProtocol(false);   //remove "javascript:" from inline event handlers
	    compressor.setRemoveHttpProtocol(false);         //replace "http://" with "//" inside tag attributes
	    compressor.setRemoveHttpsProtocol(false);        //replace "https://" with "//" inside tag attributes
	    compressor.setPreserveLineBreaks(false);         //preserves original line breaks

	    compressor.setCompressCss(true);               //compress inline css 
	    compressor.setCompressJavaScript(true);        //compress inline javascript
	    compressor.setYuiCssLineBreak(80);             //--line-break param for Yahoo YUI Compressor 
	    compressor.setYuiJsDisableOptimizations(true); //--disable-optimizations param for Yahoo YUI Compressor 
	    compressor.setYuiJsLineBreak(-1);              //--line-break param for Yahoo YUI Compressor 
	    compressor.setYuiJsNoMunge(true);              //--nomunge param for Yahoo YUI Compressor 
	    compressor.setYuiJsPreserveAllSemiColons(true);//--preserve-semi param for Yahoo YUI Compressor 

	    dst = compressor.compress(FileToString.read(inputFilename)) ;

	    StringToFile.write(outputFilename,dst) ;

	} catch (Exception e) {
	    
	    Logs.log(Logs.SERVER_ERROR,"Failed to compact \"" + inputFilename + "\"",e) ;
	    throw new InternalErrorException(e.getMessage()) ;
	} 
    }

}
