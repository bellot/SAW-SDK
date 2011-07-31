package org.saw.util.files;

import java.io.* ;

/** Static function to read a file into a String.
 *  @author Copyright Patrick Bellot, 2009 and later.
 */

public final class StringToFile
{
 
    public static void write(String fileName, String src) 
	throws IOException 
    {
	BufferedWriter bw = new BufferedWriter(new FileWriter(fileName,false)) ;
	bw.write(src) ;
	bw.close() ;
    }
    
}