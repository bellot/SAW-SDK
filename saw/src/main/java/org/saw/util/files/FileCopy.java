package org.saw.util.files;

import java.io.* ;
import java.nio.channels.* ;

/** Static functions to copy a plain file.
 *  @author Copyright Patrick Bellot, 2009 and later.
 */

public final class FileCopy
{
    private final static boolean USE_NIO = true ;

    /** Copies <code>src</code> on <code>dst</code>, 
     *  overwrite <code>dst</code> if it exists. 
     */

    public final static void cp(String src, String dst)
	throws IOException
    {
	cp(new File(src), new File(dst)) ;
    }

    /** Copies <code>src</code> on <code>dst</code>, 
     *  overwrite <code>dst</code> if it exists. 
     */

    public final static void cp(File src, File dst)
	throws IOException
    {
	if (USE_NIO) {

	    FileChannel source      = new FileInputStream (src).getChannel();
	    FileChannel destination = new FileOutputStream(dst).getChannel();

	    destination.transferFrom(source, 0, source.size());

	    source.close();
	    destination.close();

	} else {

	    BufferedInputStream  in  = new BufferedInputStream(new FileInputStream(src)) ;
	    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dst));
	    
	    byte[] buf = new byte[1024];
	    int    len ;

	    while ((len = in.read(buf)) > 0)
		out.write(buf,0,len);
	    
	    in.close();
	    out.close();
	}
    }

}