package org.saw.util.logs;

import java.io.* ;

import org.saw.* ;
import org.saw.util.files.* ;

/** This is a class of static functions for logging.<br>
 *  The log writer must be created once using the <code>open</code> method before any logging.<br>
 *  In the log functions:
 *  <ul>
 *    <li><code>category</code> must be chosen amongst the static constant log categories of this class.</li>
 *    <li><code>message</code> is any one-line string.</li>
 *    <li><code>tag</code>'s parameters must be chosen amonst the static constant tags of this class.</li>
 *    <li><code>val</code>'s parameters are any one-line string, must be in accordance with the tag.</li>
 *  </ul>
 *  Logs are flushed after each entry.
 *  @author Copyright Patrick Bellot, 2009 and later.
 */

public final class Logs
{
    /** Log category. */
    static public final byte[] SERVER_DEBUG      = "#LOGB\n0\n".getBytes() ;
    /** Log category. */
    static public final byte[] SERVER_WARNING    = "#LOGB\n1\n".getBytes() ;
    /** Log category. */
    static public final byte[] SERVER_ERROR      = "#LOGB\n2\n".getBytes() ;
    /** Log category. */
    static public final byte[] SERVER_LOG        = "#LOGB\n3\n".getBytes() ;
    /** Log category. */
    static public final byte[] SERVER_ACCESS     = "#LOGB\n4\n".getBytes() ;
    /** Log category. */
    static public final byte[] SECURITY_WARNING  = "#LOGB\n5\n".getBytes() ;
    /** Log category. */
    static public final byte[] USER_LIFE         = "#LOGB\n6\n".getBytes() ;


    /** Parameters tags, value can be one-line string. */
    static public final byte[] ANY_TAG = "#ANY\n".getBytes() ;
    /** Parameters tags, value is an IP address. */
    static public final byte[] IP_TAG = "#IPA\n".getBytes() ;
    /** Parameters tags, value is a user id. */
    static public final byte[] USERID_TAG = "#UID\n".getBytes() ;
    /** Parameters tags, value is a request path. */
    static public final byte[] REQUESTPATH_TAG = "#RQP\n".getBytes() ;
    /** Parameters tags, value is a file name. */
    static public final byte[] FILE_TAG = "#FIL\n".getBytes() ;
    /** Parameters tags, value is a email name. */
    static public final byte[] EMAIL_TAG = "#EMA\n".getBytes() ;
    /** Parameters tags, value is a session id. */
    static public final byte[] SESSIONID_TAG = "#SID\n".getBytes() ;
    /** Parameters tags, value is a country id. */
    static public final byte[] COUNTRYID_TAG = "#CID\n".getBytes() ;
    /** Parameters tags, value is a status code. */
    static public final byte[] STATUS_TAG = "#STA\n".getBytes() ;

    /** Lock to protect logStream operations. */
    private static final Object lock = new Object() ;

    /** The character stream where logs are written. */
    private static PrintStream logStream = null ;

    /** The log file name. */
    private static String logFileName ;

    /** The log file File object. */
    private static File logFile ;

    /** Opens the log writer. 
     *  Must be done once before the first logging. 
     *  In case of failure, it exits the program.
     */

    public final static void open(String fileName)
    {
	try {
	    synchronized (lock) {
		logFileName = fileName ;
                logFile     = new File(fileName) ;
		logStream   = new PrintStream(new FileOutputStream(fileName,true),false,Parameters.ENCODING) ;
	    }
	} catch (Exception e) {
	    e.printStackTrace() ;
	    System.exit(-1) ;
	}
    }

    /** Closes the log writer. */

    public final static void close()
    {
	synchronized(lock) { 
	    logStream.close() ; 
	    logStream = null ;
	}
    }

    /** Archives log file. */

    public final static void archiveIfNecessary()
    {
	synchronized(lock) { 

	    try {

                if (logFile.length() < Parameters.MAX_LOG_SIZE)
                    return ;
            
                String archiveFileName = System.currentTimeMillis() + "." + logFileName ;

                close() ;

		File archiveFile = new File(archiveFileName) ;
		FileCopy.cp(logFile,archiveFile) ;
		logFile.delete() ;

	    } catch (Exception e) {
		e.printStackTrace() ;
	    }
		
	    open(logFileName) ;		
	}
    }

    /** Beginning of a log entry. */

    static private final void logBegin(byte[] category, String message)
	throws IOException
    {
	// Opening tag is included in the category
	logStream.write(category) ;
	logStream.println(System.currentTimeMillis()) ;
	logStream.println(message) ;
    }

    /** Log entry end tag. */
    private static final byte[] LOGE = "#LOGE\n".getBytes() ;

    /** End of a log entry. */

    static private final void logEnd()
	throws IOException
    {
	logStream.write(LOGE) ;
	logStream.flush() ;
    }

    /** Exception entry begin tag. */
    private static final byte[] EXCB = "#EXCB\n".getBytes() ;

    /** Exception entry end tag. */
    private static final byte[] EXCE = "#EXCE\n".getBytes() ;

    /** Logging an exception. */

    static private final void logException(Exception e)
	throws IOException
    {
	logStream.write(EXCB) ;
	e.printStackTrace(logStream) ;
	logStream.write(EXCE) ;
    }

    /** Logging a throwable. */

    static private final void logThrowable(Throwable e)
	throws IOException
    {
	logStream.write(EXCB) ;
	e.printStackTrace(logStream) ;
	logStream.write(EXCE) ;
    }

    /** Logging a parameter. */

    static private final void logParameter(byte[] tag, String value)
	throws IOException
    {
	logStream.write(tag) ;
	logStream.println(value) ;
    }

    /** Logging function. */

    static public final void log(byte[] category, String message)
	throws IOException
    {
	synchronized (lock) {
	    logBegin(category,message) ;
	    logEnd() ;
	}
    }

    /** Logging function. */

    static public final void log(byte[] category, String message, Exception e)
	throws IOException
    {
	synchronized (lock) {
	    logBegin(category,message) ;
	    logException(e) ;
	    logEnd() ;
	}
    }

    /** Logging function. */

    static public final void log(byte[] category, String message, Throwable e)
	throws IOException
    {
	synchronized (lock) {
	    logBegin(category,message) ;
	    logThrowable(e) ;
	    logEnd() ;
	}
    }

    /** Logging function. */

    static public final void log(byte[] category, String message,
				 byte[] tag1, String val1)
	throws IOException
    {
	synchronized (lock) {
	    logBegin(category,message) ;
	    logParameter(tag1,val1) ;
	    logEnd() ;
	}
    }

    /** Logging function. */

    static public final void log(byte[] category, String message,
				 byte[] tag1, String val1, 
				 Exception e)
	throws IOException
    {
	synchronized (lock) {
	    logBegin(category,message) ;
	    logParameter(tag1,val1) ;
	    logException(e) ;
	    logEnd() ;
	}
    }


    /** Logging function. */

    static public final void log(byte[] category, String message,
				 byte[] tag1, String val1, 
				 byte[] tag2, String val2)
	throws IOException
    {
	synchronized (lock) {
	    logBegin(category,message) ;
	    logParameter(tag1,val1) ;
	    logParameter(tag2,val2) ;
	    logEnd() ;
	}
    }


    /** Logging function. */

    static public final void log(byte[] category, String message,
				 byte[] tag1, String val1, 
				 byte[] tag2, String val2,
				 Exception e)
	throws IOException
    {
	synchronized (lock) {
	    logBegin(category,message) ;
	    logParameter(tag1,val1) ;
	    logParameter(tag2,val2) ;
	    logException(e) ;
	    logEnd() ;
	}
    }

    /** Logging function. */

    static public final void log(byte[] category, String message,
				 byte[] tag1, String val1, 
				 byte[] tag2, String val2,
				 byte[] tag3, String val3)
	throws IOException
    {
	synchronized (lock) {
	    logBegin(category,message) ;
	    logParameter(tag1,val1) ;
	    logParameter(tag2,val2) ;
	    logParameter(tag3,val3) ;
	    logEnd() ;
	}
    }

    /** Logging function. */

    static public final void log(byte[] category, String message,
				 byte[] tag1, String val1, 
				 byte[] tag2, String val2,
				 byte[] tag3, String val3,
				 Exception e)
	throws IOException
    {
	synchronized (lock) {
	    logBegin(category,message) ;
	    logParameter(tag1,val1) ;
	    logParameter(tag2,val2) ;
	    logParameter(tag3,val3) ;
	    logException(e) ;
	    logEnd() ;
	}
    }

    /** Logging function. */

    static public final void log(byte[] category, String message,
				 byte[] tag1, String val1, 
				 byte[] tag2, String val2,
				 byte[] tag3, String val3,
				 byte[] tag4, String val4)
	throws IOException
    {
	synchronized (lock) {
	    logBegin(category,message) ;
	    logParameter(tag1,val1) ;
	    logParameter(tag2,val2) ;
	    logParameter(tag3,val3) ;
	    logParameter(tag4,val4) ;
	    logEnd() ;
	}
    }

    /** Logging function. */

    static public final void log(byte[] category, String message,
				 byte[] tag1, String val1, 
				 byte[] tag2, String val2,
				 byte[] tag3, String val3,
				 byte[] tag4, String val4,
				 Exception e)
	throws IOException
    {
	synchronized (lock) {
	    logBegin(category,message) ;
	    logParameter(tag1,val1) ;
	    logParameter(tag2,val2) ;
	    logParameter(tag3,val3) ;
	    logParameter(tag4,val4) ;
	    logException(e) ;
	    logEnd() ;
	}
    }

    /** Logging function. */

    static public final void log(byte[] category, String message,
				 byte[] tag1, String val1, 
				 byte[] tag2, String val2,
				 byte[] tag3, String val3,
				 byte[] tag4, String val4,
				 byte[] tag5, String val5)
	throws IOException
    {
	synchronized (lock) {
	    logBegin(category,message) ;
	    logParameter(tag1,val1) ;
	    logParameter(tag2,val2) ;
	    logParameter(tag3,val3) ;
	    logParameter(tag4,val4) ;
	    logParameter(tag5,val5) ;
	    logEnd() ;
	}
    }

    /** Logging function. */

    static public final void log(byte[] category, String message,
				 byte[] tag1, String val1, 
				 byte[] tag2, String val2,
				 byte[] tag3, String val3,
				 byte[] tag4, String val4,
				 byte[] tag5, String val5,
				 Exception e)
	throws IOException
    {
	synchronized (lock) {
	    logBegin(category,message) ;
	    logParameter(tag1,val1) ;
	    logParameter(tag2,val2) ;
	    logParameter(tag3,val3) ;
	    logParameter(tag4,val4) ;
	    logParameter(tag5,val5) ;
	    logException(e) ;
	    logEnd() ;
	}
    }

}
