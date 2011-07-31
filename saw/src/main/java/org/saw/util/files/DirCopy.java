package org.saw.util.files;

import java.io.* ;

/** Static functions to recursively copy a directory, requires <code>FileCopy</code>.
 *  @author Copyright Patrick Bellot, 2009 and later.
 */

public final class DirCopy
{
    /** Copies <code>src</code> on <code>dst</code>, 
     *  overwrite <code>dst</code> if it exists. 
     */

    static public void cp(String src, String dst)
	throws IOException 
    {    
	cp(new File(src),new File(dst)) ;
    }

    /** Copies <code>src</code> on <code>dst</code>, 
     *  overwrite <code>dst</code> if it exists. 
     */

    static public void cp(File src , File dst)
	throws IOException 
    {    
	dst.delete() ;
	cpAux(src,dst) ;
    }

    static private void cpAux(File src , File dst)
	throws IOException 
    {    
        if (src.isDirectory()) {
	    dst.mkdir() ;
            for (String child : src.list())
                cpAux(new File(src,child),new File(dst,child));
        } else {
	    FileCopy.cp(src,dst) ;
	}
    }

}