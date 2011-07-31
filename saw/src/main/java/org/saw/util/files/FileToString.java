package org.saw.util.files;

import java.io.* ;

/** Static function to read a file into a String.
 *  @author Copyright Patrick Bellot, 2009 and later.
 */

public final class FileToString
{
 
    public static String read(String fileName) 
	throws IOException 
    {
	BufferedReader reader = new BufferedReader(new FileReader(fileName));

	String line  = null;
	StringBuilder stringBuilder = new StringBuilder();
	String ls = System.getProperty("line.separator");

	while( ( line = reader.readLine() ) != null ) {
	    stringBuilder.append( line );
	    stringBuilder.append( ls );
	}
	return stringBuilder.toString();
    }
    
}