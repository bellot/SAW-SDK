package org.saw.sessions ;

import java.io.* ;

import org.saw.transaction.* ;

/** This class simulates the output functions of a Transaction object
 *  but the output goes to a file. This is used by memory and file caching
 *  Session Binz.
 *  @author  Patrick Bellot, &copy; 2010 and later. 
 */

public class TransactionOutputFile extends TransactionOutput
{
    private final String fileName ;

    public TransactionOutputFile(String fileName)
    {
	this.fileName  = fileName ;
    }

    private BufferedOutputStream out ;

    public final void beginOutput()
	throws Exception
    {
	out = new BufferedOutputStream(new FileOutputStream(fileName)) ;
    }

    public final void endOutput()
	throws Exception
    {
        out.flush() ;
	out.close() ;
    }

    /** Flushing the output. */

    public final void flush()
        throws Exception
    {
        out.flush() ;
    }

    /** Writing bytes to the output. */

    public final void write(byte [] b)
	throws Exception
    {
	out.write(b) ;
    }

    /** Writing bytes to the output. */

    public final void write(byte [] b, int off, int len)
	throws Exception
    {
	out.write(b,off,len) ;
    }

    /** Writing a String to the output. */

    public final void write(String s)
	throws Exception
    {
	out.write(s.getBytes()) ;
    }

    /** Writing an integer to the output. */

    public final void write(int i)
	throws Exception
    {
	out.write(Integer.toString(i).getBytes()) ;
    }

    /** Writes a file content to the output. */

    public final void writeFile(String fileName)
	throws Exception
    {
	BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileName)) ;

	byte[] buf = new byte[1024];
	int    len ;

	while ((len = in.read(buf)) > 0)
	    out.write(buf,0,len);
           
	in.close() ;
    }

}
