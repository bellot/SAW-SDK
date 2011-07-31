package org.saw.util.files;

import java.io.* ;

/** Static functions to recursively delete a directory.
 *  @author Copyright Patrick Bellot, 2009 and later.
 */

public final class DirRm
{
    /** Removes directory <code>dir</code>. 
     */

    static public final void rm(String dirName)
	throws IOException 
    {    
	File dirFile = new File(dirName) ;

	if (dirFile.isDirectory()) {
	    for (String child : dirFile.list()) {
		rm(dirName + File.separatorChar + child) ;
	    }
	}

	dirFile.delete() ;
    }
}