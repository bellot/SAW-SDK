package org.saw.util.bytes ;

import java.io.* ;

/** A function to read a file in an array of bytes.
 *  @author  Patrick Bellot, &copy; 2008 and later. 
 */

public final class FileBytes
{

    /** Reads the bytes in the file and return the array of bytes.
     *  @param fileName a file name
     *  @throws IOException
     */

    public static byte[] getBytesFromFile(String fileName) 
	throws IOException 
    {
	File file = new File(fileName) ;

        int length = (int)file.length() ;
    
        InputStream is = new BufferedInputStream(new FileInputStream(file)) ;
    
        byte[] bytes = new byte[length] ;
    
        int offset  = 0 ;
        int numRead = 0 ;

        while (offset < length && (numRead=is.read(bytes, offset, length-offset)) >= 0)
            offset += numRead;
    
        if (offset < bytes.length)
            throw new IOException("Could not completely read file \"" + fileName + "\"");
    
        is.close();

        return bytes;
    }
}