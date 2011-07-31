package org.saw.util.files ;

import java.io.* ;
import java.nio.channels.* ;

/** A function to concat several files into one.
 *  @author  Patrick Bellot, &copy; 2008 and later. 
 */

public final class FilesConcat
{
    private final static boolean USE_NIO = true ;

    /** Function to concat several files into one. */

    static public final void concat(String[] srcFilesNames,String dstFileName)
	throws Exception
    {
	if (USE_NIO) {

	    FileChannel destination = new FileOutputStream(dstFileName).getChannel();
	    long        position    = 0 ;

	    for (String srcFileName : srcFilesNames) {
		FileChannel source = new FileInputStream (srcFileName).getChannel();
		long        size   = source.size() ;
		destination.transferFrom(source, position, size);
		position += size ;
		source.close() ;
	    }

	    destination.close() ;

	} else {

	    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dstFileName)) ;

	    byte[] buf = new byte[1024];
	    int    len ;

	    for (String srcFileName : srcFilesNames) {

		BufferedInputStream  in  = new BufferedInputStream(new FileInputStream(srcFileName)) ;

		while ((len = in.read(buf)) > 0)
		    out.write(buf,0,len);
		
		in.close() ;
	    }

	    out.close() ;

	}
    }


}