package org.saw.transaction ;

import java.io.* ;
import java.net.* ;
import java.util.* ;
import java.util.zip.* ;

import org.saw.* ;
import org.saw.exceptions.* ;
import org.saw.sessions.* ;
import org.saw.util.bytes.* ;
import org.saw.util.logs.* ;
import org.saw.util.net.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public final class Transaction extends TransactionOutput
    implements Runnable
{
    /** The HTTP web site. */
    public static final String HTTP_WEB_SITE 
        = "http://" + Parameters.WEB_SITE + ":" + Parameters.EXT_HTTP_SERVER_PORT + Parameters.SITE_PREFIX ;

    /** The HTTPS web site. */
    public static final String HTTPS_WEB_SITE 
        = "https://" + Parameters.WEB_SITE + ":" + Parameters.EXT_HTTPS_SERVER_PORT + Parameters.SITE_PREFIX ;

    /** The initial root redirection. */
    private static final byte[] ROOT_REDIRECTION 
        = (HTTP_WEB_SITE + Parameters.ROOT_REDIRECTION).getBytes() ;

    /** The queue where to pop incoming connection sockets. */
    private final SocketsQueue socketsQueue ;

    /** Constructor.
     *  @param socketsQueue the queue where to pop incoming connection sockets
     */
    public Transaction(SocketsQueue socketsQueue)
    {
	this.socketsQueue = socketsQueue ;
    }

    /** The transaction variables object. */
    private final TransactionVariables transactionVariables = new TransactionVariables() ;

    /** Returns the transaction variables object. */
    public final TransactionVariables getTransactionVariables() { return transactionVariables ; }

    /** Returns the value of a transaction variable. */
    public final String  getTransactionVariable(String name) { return transactionVariables.get(name) ; }

    /** The transaction array variables object. */
    private final TransactionArrayVariables transactionArrayVariables  = new TransactionArrayVariables() ;

    /** Returns the transaction array variables object. */
    public final TransactionArrayVariables getTransactionArrayVariables() { return transactionArrayVariables ; }

    /** Holds sockets popped from the sockets queue. */
    private Socket httpSocket = null ;

    /** Input steram from client. */
    private BufferedInputStream httpInput  ;

    /** Output stream to client. 
     *  FilterOutputStream is used because it can be GZipOutputStream or BufferedOutputStream
     */
    private FilterOutputStream  httpOutput ;

    /** Client ip. */
    private String ip ;

    /** Returns client ip. */
    public final String getIp() { return ip ; }

    /** Max HTTP header length (source: Internet). */
    private static final int HEADER_LINE_LENGTH = 6 * 1024 ;

    /** Buffer for reading header. */
    private final byte[] header_line     = new byte[HEADER_LINE_LENGTH] ;

    /** Current index of reading in <code>header_line</code>. */
    private int header_line_cur ;

    /** End index of reading in <code>header_line</code>. */
    private int header_line_end ;

    /** Client request path. */
    private String requestPath ;

    /** Returns client request path. */
    public final String getRequestPath() { return requestPath ; }

    /** Client request type. */
    public final static int DELETE_REQUEST = 0 ;
    /** Client request type. */
    public final static int POST_REQUEST = 1 ;
    /** Client request type. */
    public final static int PUT_REQUEST = 2 ;
    /** Client request type. */
    public final static int GET_REQUEST = 3 ;

    /** Client request type. */
    private int requestType ;

    /** Returns client request type. */
    public final int getRequestType() { return requestType ; }

    /** Does client accepts zipped content ? */
    private boolean gzipOk ;

    /** Does client accepts zipped content ? */
    public final boolean getGzipOk() { return gzipOk ; }

    /** Forces clent accepting zipped content flag. */
    public final void setGzipOk(boolean gzipOk) { this.gzipOk = gzipOk ; }

    /** Client session id. */
    private int sessionId ;

    /** Client session environment. */
    private SessionEnvironment sessionEnvironment ;

    /** Returns client session environment. */
    public final SessionEnvironment getSessionEnvironment() { return sessionEnvironment ; }

    /** For file upload. */
    public  String file_mime ; 

    /** For file upload. */
    public  byte[] file_content ; 

    /** Is this a SSL connexion ? */

    public final boolean isSSL()
    {
        return (httpSocket.getLocalPort() == Parameters.HTTPS_SERVER_PORT) ;
    }

    /** Tries to read input stream bytes into the header buffer, 
     *  returns the count of read bytes,
     *  throws an bad request exception after 30 sec.
     */
    private final int readLineOrMore()
	throws Exception
    {
	int loop_count = 3 * 400 ; // * 25 ms * loop_count = 30 sec.

	do {

	    int ct_read = httpInput.read(header_line,header_line_end,HEADER_LINE_LENGTH - header_line_end) ;

	    if (ct_read > 0)
		return ct_read ;

	    if (ct_read == -1)
		throw new BadRequestException(Logs.SERVER_LOG_CAT,"HTTP Unexpected end of transaction") ;

	    if (--loop_count == 0)
		throw new BadRequestException(Logs.SERVER_LOG_CAT,"HTTP Session Timeout") ;

	    try {
		Thread.sleep(25) ;
	    } catch (InterruptedException e) {}

	} while (true) ;
    }

    /** Reinitializes all fields for next requests. */

    private final void reinitData()
    {
	try {
	    httpOutput.flush() ;
	    httpSocket.close() ; // also close httpInput and httpOutput
	} catch (Exception e) {} ;
	
	// httpSocket = null ; // useless

	header_line_cur = header_line_end = 0 ;

	//requestPath = null ; // for GC but requestPath is small

	gzipOk = false ;

	sessionId = -1 ;
	sessionEnvironment = null ;

	transactionVariables.removeAll() ;
	transactionArrayVariables.removeAll() ;

	//file_mime = null ; // for GC but file_mime is small
	file_content = null ; // for GC (may be big)
    }

    /** The core of the thread, too difficult to describe. */

    public final void run()
    {

	while (true) { // Outer loop with a try-catch 

	    try {

		while (true) { // Inner loop without try-catch
		    
		    reinitData() ;

		    httpSocket = socketsQueue.pop() ; // Blocking

		    if (httpSocket == null)
			throw new TransactionMustExitException() ;

		    httpInput   = new BufferedInputStream (httpSocket.getInputStream()) ;
		    httpOutput  = new BufferedOutputStream(httpSocket.getOutputStream()) ;
			
		    ip = httpSocket.getInetAddress().getHostAddress() ;
		    
		    header_line_end = readLineOrMore() ; // may raise an exception and exit

		    int header_line_eol ;

		    while ((header_line_eol = BytesArray.strchr((byte)'\n',header_line,header_line_cur,header_line_end)) < 0)
			header_line_end += readLineOrMore() ; // may raise an exception and exit
			   
		    // Decoding the request method. Only GET, POST, PUT and DELETE are processed in this version
		    // Sets header_line_cur to the request_path

		    switch (header_line[0]) {
		    case (byte)'D':
			header_line_cur = 7 ; // DELETE
			requestType = DELETE_REQUEST ;
			break ;
		    case (byte)'G':
			header_line_cur = 4 ; // GET
			requestType = GET_REQUEST ;
			break ;
		    case (byte)'P':
			switch (header_line[1]) {
			case (byte)'O':
			    header_line_cur = 5 ; // POST
			    requestType = POST_REQUEST ;
			    break ;
			case (byte)'U':
			    header_line_cur = 4 ; // PUT
			    requestType = PUT_REQUEST ;
			    break ;
			default:
			    throw new BadRequestException(Logs.SERVER_WARNING_CAT,
                                                          "Unsupported request method: " + new String(header_line,0,header_line_eol)) ;
			}
			break ;
		    default:
			throw new BadRequestException(Logs.SERVER_WARNING_CAT,
                                                      "Unsupported request method: " + new String(header_line,0,header_line_eol)) ;
		    }

		    // header_line[header_line_cur..header_line_end[ = /path HTTP/1.1 or path?x=y&... HTTP/1.x

		    if (header_line[header_line_cur] != '/')
			throw new BadRequestException(Logs.SERVER_WARNING_CAT,
                                                      "Malformed request path: " + new String(header_line,0,header_line_eol)) ;

		    // Search for the space before HTTP/1.x

		    int header_line_spc = BytesArray.strchr((byte)' ',header_line,header_line_cur+1,header_line_end) ;

		    if (header_line_spc == -1)
			throw new BadRequestException(Logs.SERVER_WARNING_CAT,
                                                      "No space after request path: " + new String(header_line,0,header_line_eol)) ;

		    // Search for ? if any

		    int header_line_mrk = BytesArray.strchr((byte)'?',header_line,header_line_cur+1,header_line_spc) ;

		    // Extract request path

		    if (header_line_mrk == -1) { // there is no GET variables

			requestPath = new String(header_line,header_line_cur,header_line_spc-header_line_cur) ;

		    } else { // there is GET variables

			requestPath = new String(header_line,header_line_cur,header_line_mrk-header_line_cur) ;

			// Decode GET variables
			
			header_line_cur = header_line_mrk + 1 ;

			do {

			    int header_line_equ = BytesArray.strchr((byte)'=',header_line,header_line_cur,header_line_spc) ;

			    if (header_line_equ == -1)
				throw new BadRequestException(Logs.SERVER_WARNING_CAT,
                                                              "Malformed GET variables: " + new String(header_line,0,header_line_eol)) ;

			    int header_line_amp = BytesArray.strchr((byte)'&',header_line,header_line_equ,header_line_spc) ;

			    if (header_line_amp == -1)
				header_line_amp = header_line_spc ;

			    String var = new String(header_line,header_line_cur,header_line_equ-header_line_cur) ;

			    header_line_cur = header_line_equ + 1 ;

			    String val = new String(header_line,header_line_cur,header_line_amp-header_line_cur) ;

			    transactionVariables.put(var,URLDecoder.decode(val,Parameters.ENCODING)) ;

			    header_line_cur = header_line_amp + 1 ;

			} while (header_line_cur < header_line_spc) ;
		    }

		    // Decode next header lines

		    header_line_cur = header_line_eol + 1 ;

		    boolean isMultiParts = false ;
		    int     inLength     = 0 ;

		    while (true) { // Loop on header lines

			// Get next end of line

			while ((header_line_eol = BytesArray.strchr((byte)'\n',header_line,header_line_cur,header_line_end)) < 0)
			    header_line_end += readLineOrMore() ;
			   
			// If empty line, then exit loop

			if (header_line_eol < header_line_cur + 3) {
			    header_line_cur += 2 ;
			    break ;
			}

			// Seacrh for keywords: 
			// - Accept-Encoding: gzip
			// - Cookie: 
			// - Content-Length:
			// - Content-Type:

			switch (header_line[header_line_cur]) {

			case (byte)'A':
			    if (header_line[header_line_cur+7] == (byte)'E') { // Accept-Encoding:
				if (BytesArray.strstr(gzipBytesArray,header_line,header_line_cur+16,header_line_eol) != -1)
				    gzipOk = true ;
			    }
			    break ;

			case (byte)'C':

			    if (header_line[header_line_cur+1] == (byte)'o') {

				switch (header_line[header_line_cur+2]) {
				case (byte)'o': // Cookie:
				    int cookie_idx = BytesArray.strstr(Parameters.COOKIE_NAME_BYTES_ARRAY,
								       header_line,
								       header_line_cur+8,
								       header_line_eol) ;
				    if (cookie_idx != -1) {
					int cookie_equ = BytesArray.strchr((byte)'=',header_line,cookie_idx,header_line_eol) ;
					if (cookie_equ != -1) {
					    int cookie_end = BytesArray.strchr((byte)';',header_line,cookie_equ+1,header_line_eol) ;
					    if (cookie_end != -1) {
						sessionId = BytesArray.strtoi(header_line,cookie_equ+1,cookie_end) ;
					    } else {
						sessionId = BytesArray.strtoi(header_line,cookie_equ+1,header_line_eol-1) ;
					    }
					}
				    }
				    break ;

				case (byte)'n':
				    switch (header_line[header_line_cur+8]) {
				    case (byte)'L': // Content-Length:
					int header_line_eon =
					    (header_line[header_line_eol - 1] == (byte)'\r')
					    ? (header_line_eol - 1)
					    : header_line_eol ;
					inLength = BytesArray.strtoi(header_line,header_line_cur+16,header_line_eon) ;
					break ;
				    case (byte)'T': // Content-Type:
					if (header_line[header_line_cur+14] == (byte)'m' && header_line[header_line_cur+15] == (byte)'u')
					    isMultiParts = true ;
					break ;
				    }
				    break ;
				}
			    }
			    break ;
			}

			header_line_cur = header_line_eol + 1 ;

		    } // End of loop on header lines

		    // Recover session

		    if (sessionId == -1) { // No cookie, need to create a session
			sessionEnvironment = SessionEnvironmentsManager.create(ip) ;
			sessionId = sessionEnvironment.id ;
		    } else {               // There is a cookie, try to recover session
			sessionEnvironment = SessionEnvironmentsManager.get(sessionId,ip) ;
			if (sessionEnvironment == null) {
			    sessionEnvironment = SessionEnvironmentsManager.create(ip) ;
			    sessionId = sessionEnvironment.id ;
			}
		    }

		    // Log access

		    Logs.log(Logs.SERVER_ACCESS_CAT,  "Page access",
			     Logs.IP_TAG,             ip,
			     Logs.SESSION_ID_TAG,     Integer.toString(sessionId),
			     Logs.REQUEST_PATH_TAG,   requestPath) ;

		    // Decondig POST content

		    if (inLength > 0) {

			if (inLength > Parameters.HTTP_MAX_POST_SIZE)
			    throw new PostTooBigException(inLength) ;

			byte[] post_content = new byte[inLength] ;
			int    post_content_cur = 0 ;
			int    post_content_end = 0 ;

			// Copy the rest of header line buffer

			for (int i = header_line_cur ; i < header_line_end ; i++)
			    post_content[post_content_end++] = header_line[i] ;

			int to_read = inLength - post_content_end ;
			
			if (httpInput.read(post_content,post_content_end,to_read) != to_read)
			    throw new BadRequestException(Logs.SERVER_WARNING_CAT,"Cannot read post content") ;

			post_content_end = inLength ;

			// Two types of decoding:
			// - multi parts decoding
			// - POST variables decoding

			if (isMultiParts) {
			    
			    // Decoding multi parts content

			    //content-type: multipart/form-data; boundary=---------------------------114782935826962
			    //-----------------------------114782935826962
			    //Content-Disposition: form-data; name="file_name"; filename="Submit.html"
			    //Content-Type: text/html
			    //
			    //... file content
			    //-----------------------------114782935826962
			    //Content-Disposition: form-data; name="submit_button.x"
			    //
			    //21
			    //-----------------------------114782935826962

			    // Recover boundary line

			    int post_content_eol = BytesArray.strchr((byte)'\n',post_content,0,post_content_end) ;

			    if (post_content_eol == -1)
				throw new BadRequestException(Logs.SERVER_WARNING_CAT,
                                                              "Cannot find end of boundary in multipart POST") ;

			    int    boundary_len = post_content_eol ;
			    byte[] boundary     = Arrays.copyOfRange(post_content,0,boundary_len) ;

			    post_content_cur = post_content_eol + 1 ;

			    int post_content_bnd ;
			    int eol_count ;
			    int mark1 ;
			    int mark2 ;

			    while (true) {

				// Looks for next boundary
				post_content_bnd = BytesArray.strstr(boundary,post_content,post_content_cur,post_content_end) ;

				// If no next boundary, exit loop
				if (post_content_bnd == -1)
				    break ;

				// Count eol until next boundary

				eol_count = 0 ;
				post_content_eol = post_content_cur ;
				while ((post_content_eol = BytesArray.strchr((byte)'\n',post_content,post_content_eol,post_content_bnd)) != -1)
				    eol_count ++ ;

				if (eol_count > 3) {
				
				    //Content-Disposition: form-data; name="file_name"; filename="Submit.html"
				    //Content-Type: text/html
				    //
				    //... file content
				    //-----------------------------114782935826962

				    // Get file name (naem of the upload file widget)

				    mark1 = BytesArray.strchr((byte)'"',post_content,post_content_cur,post_content_bnd) ;
				    if (mark1 < 0)
					throw new BadRequestException(Logs.SERVER_WARNING_CAT,
                                                                      "Cannot find quote (1) in multipart POST") ;
				    mark1++ ;

				    mark2 = BytesArray.strchr((byte)'"',post_content,mark1,post_content_bnd) ;
				    if (mark2 < 0)
					throw new BadRequestException(Logs.SERVER_WARNING_CAT,
                                                                      "Cannot find quote (2) in multipart POST") ;

                    // Name of the uploaded file
				    // String name = new String(post_content,mark1,mark2-mark1) ;

				    // Get mime type and store it in transaction attributes
				    
				    post_content_eol = BytesArray.strchr((byte)'\n',post_content,mark2+1,post_content_bnd) ;
				    if (post_content_eol < 0)
					throw new BadRequestException(Logs.SERVER_WARNING_CAT,
                                                                      "Cannot find EOL (1) in multipart POST") ;
				    mark1 = post_content_eol + ( 1 + 14) ;
				    post_content_eol = BytesArray.strchr((byte)'\n',post_content,mark1+1,post_content_bnd) ;
				    if (post_content_eol < 0)
					throw new BadRequestException(Logs.SERVER_WARNING_CAT,
                                                                      "Cannot find EOL (2) in multipart POST") ;
				    mark2 = post_content_eol - 1 ; // POURQUOI -1

				    this.file_mime = new String(post_content,mark1,mark2-mark1) ;

				    // Get content and store it in transaction attributes

				    post_content_eol = BytesArray.strchr((byte)'\n',post_content,post_content_eol+1,post_content_bnd) ;
				    if (post_content_eol < 0)
					throw new BadRequestException(Logs.SERVER_WARNING_CAT,
                                                                      "Cannot find EOL (3) in multipart POST") ;
				    mark1 = post_content_eol + 1 ;

				    this.file_content = Arrays.copyOfRange(post_content,mark1,post_content_bnd) ;

				} else {

				    //Content-Disposition: form-data; name="submit_button.x"
				    //
				    //21
				    //-----------------------------114782935826962

				    // Looks for var name

				    mark1 = BytesArray.strchr((byte)'"',post_content,post_content_cur,post_content_bnd) ;
				    if (mark1 < 0)
					throw new BadRequestException(Logs.SERVER_WARNING_CAT,
                                                                      "Cannot find quote (3) in multipart POST") ;
				    mark1++ ;
				    mark2 = BytesArray.strchr((byte)'"',post_content,mark1,post_content_bnd) ;
				    if (mark1 < 0)
					throw new BadRequestException(Logs.SERVER_WARNING_CAT,
                                                                      "Cannot find quote (4) in multipart POST") ;

				    String var = new String(post_content,mark1,mark2-mark1) ;

				    // Looks for value

				    post_content_eol = BytesArray.strchr((byte)'\n',post_content,mark2+1,post_content_bnd) ;
				    if (post_content_eol < 0)
					throw new BadRequestException(Logs.SERVER_WARNING_CAT,
                                                                      "Cannot find EOL (4) in multipart POST") ;
				    post_content_eol = BytesArray.strchr((byte)'\n',post_content,post_content_eol,post_content_bnd) ;
				    if (post_content_eol < 0)
					throw new BadRequestException(Logs.SERVER_WARNING_CAT,
                                                                      "Cannot find EOL (5) in multipart POST") ;
				    mark1 = post_content_eol + 1 ;
				    mark2 = BytesArray.strchr((byte)'\n',post_content,mark1,post_content_bnd) ;
				    if (mark2 < 0)
					throw new BadRequestException(Logs.SERVER_WARNING_CAT,
                                                                      "Cannot find EOL (6) in multipart POST") ;

				    String val = new String(post_content,mark1,mark2-mark1) ;

				    // Store (var,val)

				    if (var.endsWith("%5B%5D")) { // []
					var = var.substring(0,var.length()-6) ;
					transactionArrayVariables.add(var,URLDecoder.decode(val,Parameters.ENCODING)) ;
				    } else {
					transactionVariables.put(var,URLDecoder.decode(val,Parameters.ENCODING)) ;
				    }

				} // End of ordinary multi part post variable

				// Go after the next boundary...

				post_content_cur = post_content_bnd + boundary_len + 1 ;

			    }

			    // End of Decoding multi parts content

			} else { // Not multi part

			    // Decoding POST variables
			    
			    while (post_content_cur < post_content_end) {

				int post_content_equ = BytesArray.strchr((byte)'=',post_content,post_content_cur,post_content_end) ;

				if (post_content_equ == -1)
				    throw new BadRequestException(Logs.SERVER_WARNING_CAT,
                                                                  "Malformed POST variables: " + new String(post_content,0,post_content_end)) ;

				int post_content_amp = BytesArray.strchr((byte)'&',post_content,post_content_equ,post_content_end) ;

				if (post_content_amp == -1)
				    post_content_amp = post_content_end ;

				String var = new String(post_content,post_content_cur,post_content_equ-post_content_cur) ;

				post_content_cur = post_content_equ + 1 ;

				String val = new String(post_content,post_content_cur,post_content_amp-post_content_cur) ;


				if (var.endsWith("%5B%5D")) { // []
				    var = var.substring(0,var.length()-6) ;
				    transactionArrayVariables.add(var,URLDecoder.decode(val,Parameters.ENCODING)) ;
				} else {
				    transactionVariables.put(var,URLDecoder.decode(val,Parameters.ENCODING)) ;
				}

				post_content_cur = post_content_amp + 1 ;
			    }

			    // End of Decoding POST variables
			}

		    } // End of  Decondig POST content

		    //
		    // Everything has been decoded and stored somewhere.
		    //

		    // If request path is "/", then redirect to the default path

		    if (requestPath.length() <= 1) {

			sendHttpRedirection(ROOT_REDIRECTION) ;

			break ;
		    }

		    // Looking for the page

		    SessionBinz sessionBinz = SessionBinzFinder.getSessionBinz(requestPath) ;

		    if (sessionBinz == null) {

			replyNotFound() ;

			Logs.log(Logs.SERVER_WARNING_CAT,  "Page not found",
				 Logs.IP_TAG,              ip,
				 Logs.SESSION_ID_TAG,      Integer.toString(sessionId),
				 Logs.REQUEST_PATH_TAG,    requestPath) ;

			break ;
		    }

		    sessionBinz.handle(this) ;

		    // TBD : remove session environment ?

		} // End of inner loop without try-catch

	    } catch (BadRequestException e) {

		try {
		    replyBadRequest() ;
		    logError(e.getLogCategory(),"Bad request",e) ;
		} catch (Exception e2) {}

	    } catch (ServiceUnavailableException e) {

		try {
		    replyServiceUnavailable() ;
		    logError(Logs.SERVER_ERROR_CAT,"Service unavailable",e) ;
		} catch (Exception e2) {}

	    } catch (PostTooBigException e) {

		try {
		    replyPostTooBig() ;
		    logError(Logs.SERVER_LOG_CAT,"Post too big",e) ;
		} catch (Exception e2) {}

	    } catch (TransactionMustExitException e) {

		try {
		    logError(Logs.SERVER_LOG_CAT,"Must exit",e) ;
		    // Exit run() method, terminates thread
		    return ; 
		} catch (Exception e2) {} 

	    } catch (InternalErrorException e) {

		try {
		    replyInternalError() ;
		    logError(Logs.SERVER_ERROR_CAT,"Internal error",e) ;
		} catch (Exception e2) {} 

	    } catch (Exception e) {

		try {
		    replyInternalError() ;
		    logError(Logs.SERVER_ERROR_CAT,"Internal error",e) ;
		} catch (Exception e2) {} 

	    }

	} // End of outer loop with a try-catch
	
    }

    private final void logError(byte[] logCategory, String error, Exception e)
	throws Exception
    {
        if (sessionEnvironment == null || sessionEnvironment.getUser() == null) {
            if (requestPath == null) {
                Logs.log(logCategory,         error,
                         Logs.IP_TAG,         ip,
                         Logs.SESSION_ID_TAG, Integer.toString(sessionId),
                         e) ;
            } else {
                Logs.log(logCategory,           error,
                         Logs.IP_TAG,           ip,
                         Logs.SESSION_ID_TAG,   Integer.toString(sessionId),
                         Logs.REQUEST_PATH_TAG, requestPath,
                         e) ;
            }
        } else {
            if (requestPath == null) {
                Logs.log(logCategory,         error,
                         Logs.IP_TAG,         ip,
                         Logs.SESSION_ID_TAG, Integer.toString(sessionId),
                         Logs.USER_ID_TAG,    Integer.toString(sessionEnvironment.getUser().userId),
                         e) ;
            } else {
                Logs.log(logCategory,           error,
                         Logs.IP_TAG,           ip,
                         Logs.SESSION_ID_TAG,   Integer.toString(sessionId),
                         Logs.REQUEST_PATH_TAG, requestPath,
                         Logs.USER_ID_TAG,      Integer.toString(sessionEnvironment.getUser().userId),
                         e) ;
            }
        }
    }

    /** Not found HTTP response content. */

    static private final byte[] notFoundResponse 
	= "HTTP/1.1 404 OK\nContent-Type: text/html\n\n<html><body>404: Not Found</body></html>".getBytes() ;
	
    /** Send Not Found HTTP response. */

    private final void replyNotFound()
	throws Exception
    {
	httpOutput.write(notFoundResponse) ;
    }

    /** Internal Error HTTP response content. */

    static private final byte[] internalErrorResponse 
	= "HTTP/1.1 500 OK\nContent-Type: text/html\n\n<html><body>500: Internal Error</body></html>".getBytes() ;
	
    /** Send Internal Error HTTP response. */

    private final void replyInternalError()
	throws Exception
    {
	httpOutput.write(internalErrorResponse) ;
    }

    /** Bad Request HTTP response content. */

    static private final byte[] badRequestResponse 
	= "HTTP/1.1 400 OK\nContent-Type: text/html\n\n<html><body>400: Bad request</body></html>".getBytes() ;

    /** Send Bad Request HTTP response. */

    private final void replyBadRequest()
	throws Exception
    {
	httpOutput.write(badRequestResponse) ; 
    }

    /** Service Unavailable HTTP response content. */

    static private final byte[] serviceUnavailableResponse 
	= "HTTP/1.1 500 OK\nContent-Type: text/html\n\n<html><body>503: Service unavailable</body></html>".getBytes() ;

    /** Send Service Unavailable HTTP response. */

    private final void replyServiceUnavailable()
	throws Exception
    {
	httpOutput.write(serviceUnavailableResponse) ; 
    }

    /** Post Too Big HTTP response content. */

    static private final byte[] postTooBigResponse 
	= "HTTP/1.1 413 OK\nContent-Type: text/html\n\n<html><body>413: Request Entity Too Large</body></html>".getBytes() ;

    /** Send Post Too Big HTTP response. */

    private final void replyPostTooBig()
	throws Exception
    {
	httpOutput.write(postTooBigResponse) ;
    }

    /** The word "gzip" as an array of bytes. */

    private static final byte[] gzipBytesArray
	= { (byte)'g', (byte)'z', (byte)'i', (byte) 'p' } ;

 




    /** Redirect to another Session Binz by requestPath. */

    private final void reditrectToSessionBinz(String requestPath)
        throws Exception
    {
        SessionBinz sessionBinz = SessionBinzFinder.getSessionBinz(requestPath) ;

        if (sessionBinz == null) {

            replyNotFound() ;

            Logs.log(Logs.SERVER_WARNING_CAT, "Page not found",
                     Logs.IP_TAG,             ip,
                     Logs.SESSION_ID_TAG,     Integer.toString(sessionId),
                     Logs.REQUEST_PATH_TAG,   requestPath) ;
            
            return ;
        }

        sessionBinz.handle(this) ;
    }






    /** Used by sendHtmlRedirection. */
 
    private static byte[] htmlRedirect_1
	= "<html><head><meta http-equiv='refresh' content='0; URL=".getBytes() ;

    /** Used by sendHtmlRedirection. */

    private static byte[] htmlRedirect_2 
	= "'></head><body></body></html>".getBytes() ;
    
    /** Redirection using HTML and JavaScript. */

    public final void sendHtmlRedirection(byte[] url)
	throws Exception
    {
	gzipOk = false ; // Because we send clear HTML

	sendHtmlExpireHeader() ;
	httpOutput.write(htmlRedirect_1) ;
	httpOutput.write(url) ;
	httpOutput.write(htmlRedirect_2) ;
    }






    /** Used by sendHttpRedirection(). */

    private static final byte[] http_redirection_1 =
	("HTTP/1.1 303 See Other\n"
	 + "Location: ").getBytes() ;

    /** Used by sendHttpRedirection(). */

    private static final byte[] http_redirection_2 =
	("\n"
	 + "Content-Type: text/html\n"
	 + "Cache-control: no-cache\n"
	 + "Set-Cookie: " + new String(Parameters.COOKIE_NAME_BYTES_ARRAY) + "=").getBytes() ;

    /** Used by sendHttpRedirection. */
    private static final byte[] http_redirection_3 = 
	(";PATH=/\n\n"
	 + "<html><body></body></html>").getBytes() ;

    /** HTTP redirection. */

    public final void sendHttpRedirection(byte[] location)
	throws Exception
    {
	httpOutput.write(http_redirection_1) ;
	httpOutput.write(location) ;
	httpOutput.write(http_redirection_2) ;
	httpOutput.write(Integer.toString(sessionId).getBytes()) ;
	httpOutput.write(http_redirection_3) ;
    }








    /** Used by sendHtmlExpireHeader. */
    private static final byte[] html_expire_1 = 
	("HTTP/1.1 200 OK\n"
	 + "Content-Type: text/html\n"
	 + "Expires: 0\n"
	 + "Cache-control: no-cache\n"
	 + "Set-Cookie: " + new String(Parameters.COOKIE_NAME_BYTES_ARRAY) + "=").getBytes() ;

    /** Used by sendHtmlExpireHeader. */
    private static final byte[] html_expire_2_nogzip = ";PATH=/\n\n".getBytes() ;

    /** Used by sendHtmlExpireHeader. */
    private static final byte[] html_expire_2_okgzip = ";PATH=/\nContent-Encoding: gzip\n\n".getBytes() ;

    /** Send a request header for HTML with immediate expiration, Ok Gzip. */

    public final void sendHtmlExpireHeaderOkGzip()
	throws Exception
    {
	httpOutput.write(html_expire_1) ;
	httpOutput.write(Integer.toString(sessionId).getBytes()) ;
	httpOutput.write(html_expire_2_okgzip) ;
    }

    /** Send a request header for HTML with immediate expiration, No Gzip. */

    public final void sendHtmlExpireHeaderNoGzip()
	throws Exception
    {
	httpOutput.write(html_expire_1) ;
	httpOutput.write(Integer.toString(sessionId).getBytes()) ;
	httpOutput.write(html_expire_2_nogzip) ;
    }

    /** Send a request header for HTML with immediate expiration. */

    public final void sendHtmlExpireHeader()
	throws Exception
    {
	httpOutput.write(html_expire_1) ;
	httpOutput.write(Integer.toString(sessionId).getBytes()) ;
	httpOutput.write((gzipOk) ? html_expire_2_okgzip : html_expire_2_nogzip) ;
    }







    /** Used by sendHtmlNoExpireHeader. */

    private static final byte[] html_no_expire_1 = 
	("HTTP/1.1 200 OK\nContent-Type: text/html\nExpires: Thu, 31 Dec 2037 23:59:59 GMT\nSet-Cookie: "
	 + new String(Parameters.COOKIE_NAME_BYTES_ARRAY)
	 + "=").getBytes() ;

    private static final byte[] html_no_expire_2_nogzip = ";PATH=/\n\n".getBytes() ;
    private static final byte[] html_no_expire_2_okgzip = ";PATH=/\nContent-Encoding: gzip\n\n".getBytes() ;

    /** Send a request header for HTML with immediate expiration, Ok Gzip. */

    public final void sendHtmlNoExpireHeaderOkGzip()
	throws Exception
    {
	httpOutput.write(html_no_expire_1) ;
	httpOutput.write(Integer.toString(sessionId).getBytes()) ;
	httpOutput.write(html_no_expire_2_okgzip) ;
    }

    /** Send a request header for HTML with immediate expiration, No Gzip. */

    public final void sendHtmlNoExpireHeaderNoGzip()
	throws Exception
    {
	httpOutput.write(html_no_expire_1) ;
	httpOutput.write(Integer.toString(sessionId).getBytes()) ;
	httpOutput.write(html_no_expire_2_nogzip) ;
    }

    /** Send a request header for HTML with immediate expiration. */

    public final void sendHtmlNoExpireHeader()
	throws Exception
    {
	httpOutput.write(html_no_expire_1) ;
	httpOutput.write(Integer.toString(sessionId).getBytes()) ;
	httpOutput.write((gzipOk) ? html_no_expire_2_okgzip : html_no_expire_2_nogzip) ;
    }







    /** Used by sendHtmlNoExpireNoCookieHeader. */

    private static final byte[] html_no_expire_no_cookie_no_gzip = 
	("HTTP/1.1 200 OK\n"
	 + "Content-Type: text/html\n"
	 + "Expires: Thu, 31 Dec 2037 23:59:59 GMT\n\n").getBytes() ;

    private static final byte[] html_no_expire_no_cookie_ok_gzip = 
	("HTTP/1.1 200 OK\n"
	 + "Content-Type: text/html\n"
	 + "Expires: Thu, 31 Dec 2037 23:59:59 GMT\n"
	 + "Content-Encoding: gzip\n\n").getBytes() ;

    /** Send a request header for Html with immediate expiration and no cookie and gzip content. */

    public final void sendHtmlNoExpireNoCookieHeaderOkGzip()
	throws Exception
    {
	httpOutput.write(html_no_expire_no_cookie_ok_gzip) ;
    }

    /** Send a request header for Html with immediate expiration and no cookie and no gzip content. */

    public final void sendHtmlNoExpireNoCookieHeaderNoGzip()
	throws Exception
    {
	httpOutput.write(html_no_expire_no_cookie_no_gzip) ;
    }

    /** Send a request header for Html with immediate expiration and no cookie. */

    public final void sendHtmlNoExpireNoCookieHeader()
	throws Exception
    {
	httpOutput.write((gzipOk) ? html_no_expire_no_cookie_ok_gzip : html_no_expire_no_cookie_no_gzip) ;
    }







    /** Used by sendHtmlDownloadNoExpireNoCookieHeader. */

    private static final byte[] html_download_no_expire_1 = 
	("HTTP/1.1 200 OK\n"
	 + "Content-Type: text/html\n"
         + "Content-Disposition: attachment ; filename=").getBytes() ;

    private static final byte[] html_download_no_expire_no_cookie_no_gzip_2 = 
	("\n"
         + "Expires: Thu, 31 Dec 2037 23:59:59 GMT\n\n").getBytes() ;

    private static final byte[] html_download_no_expire_no_cookie_ok_gzip_2 = 
	("\n"
	 + "Expires: Thu, 31 Dec 2037 23:59:59 GMT\n"
	 + "Content-Encoding: gzip\n\n").getBytes() ;

    /** Send a request header for Html with immediate expiration and no cookie. */

    public final void sendHtmlDownloadNoExpireNoCookieHeader(String fileName)
	throws Exception
    {
        httpOutput.write(html_download_no_expire_1) ;
        httpOutput.write(fileName.getBytes()) ;
	httpOutput.write((gzipOk) ? html_download_no_expire_no_cookie_ok_gzip_2 : html_download_no_expire_no_cookie_no_gzip_2) ;
    }

    /** Send a request header for Html with immediate expiration and no cookie, gzip content. */

    public final void sendHtmlDownloadNoExpireNoCookieHeaderOkGzip(String fileName)
	throws Exception
    {
        httpOutput.write(html_download_no_expire_1) ;
        httpOutput.write(fileName.getBytes()) ;
	httpOutput.write(html_download_no_expire_no_cookie_ok_gzip_2) ;
    }

    /** Send a request header for Html with immediate expiration and no cookie, no gzip content. */

    public final void sendHtmlDownloadNoExpireNoCookieHeaderNoGzip(String fileName)
	throws Exception
    {
        httpOutput.write(html_download_no_expire_1) ;
        httpOutput.write(fileName.getBytes()) ;
	httpOutput.write(html_download_no_expire_no_cookie_no_gzip_2) ;
    }







    /** Used by sendJsNoExpireNoCookieHeader. */

    private static final byte[] js_no_expire_no_cookie_no_gzip = 
	("HTTP/1.1 200 OK\n"
	 + "Content-Type: text/js\n"
	 + "Expires: Thu, 31 Dec 2037 23:59:59 GMT\n\n").getBytes() ;

    private static final byte[] js_no_expire_no_cookie_ok_gzip = 
	("HTTP/1.1 200 OK\n"
	 + "Content-Type: text/js\n"
	 + "Expires: Thu, 31 Dec 2037 23:59:59 GMT\n"
	 + "Content-Encoding: gzip\n\n").getBytes() ;

    /** Send a request header for JS with immediate expiration. */

    public final void sendJsNoExpireNoCookieHeader()
	throws Exception
    {
	httpOutput.write((gzipOk) ? js_no_expire_no_cookie_ok_gzip : js_no_expire_no_cookie_no_gzip) ;
    }

    /** Send a request header for JS with immediate expiration, gzip ok. */

    public final void sendJsNoExpireNoCookieHeaderOkGzip()
	throws Exception
    {
	httpOutput.write(js_no_expire_no_cookie_ok_gzip) ;
    }

    /** Send a request header for JS with immediate expiration, no gzip. */

    public final void sendJsNoExpireNoCookieHeaderNoGzip()
	throws Exception
    {
	httpOutput.write(js_no_expire_no_cookie_no_gzip) ;
    }







    /** Used by sendJsDownloadNoExpireNoCookieHeader. */

    private static final byte[] js_download_no_expire_1 = 
	("HTTP/1.1 200 OK\n"
	 + "Content-Type: text/javascript\n"
         + "Content-Disposition: attachment ; filename=").getBytes() ;

    private static final byte[] js_download_no_expire_no_cookie_no_gzip_2 = 
	("\n"
         + "Expires: Thu, 31 Dec 2037 23:59:59 GMT\n\n").getBytes() ;

    private static final byte[] js_download_no_expire_no_cookie_ok_gzip_2 = 
	("\n"
	 + "Expires: Thu, 31 Dec 2037 23:59:59 GMT\n"
	 + "Content-Encoding: gzip\n\n").getBytes() ;

    /** Send a request header for Js with immediate expiration and no cookie. */

    public final void sendJsDownloadNoExpireNoCookieHeader(String fileName)
	throws Exception
    {
        httpOutput.write(js_download_no_expire_1) ;
        httpOutput.write(fileName.getBytes()) ;
	httpOutput.write((gzipOk) ? js_download_no_expire_no_cookie_ok_gzip_2 : js_download_no_expire_no_cookie_no_gzip_2) ;
    }

    /** Send a request header for Js with immediate expiration and no cookie, gzip content. */

    public final void sendJsDownloadNoExpireNoCookieHeaderOkGzip(String fileName)
	throws Exception
    {
        httpOutput.write(js_download_no_expire_1) ;
        httpOutput.write(fileName.getBytes()) ;
	httpOutput.write(js_download_no_expire_no_cookie_ok_gzip_2) ;
    }

    /** Send a request header for Js with immediate expiration and no cookie, no gzip content. */

    public final void sendJsDownloadNoExpireNoCookieHeaderNoGzip(String fileName)
	throws Exception
    {
        httpOutput.write(js_download_no_expire_1) ;
        httpOutput.write(fileName.getBytes()) ;
	httpOutput.write(js_download_no_expire_no_cookie_no_gzip_2) ;
    }










    /** Used by sendCssNoExpireNoCookieHeader. */

    private static final byte[] css_no_expire_no_cookie_no_gzip = 
	("HTTP/1.1 200 OK\n"
	 + "Content-Type: text/css\n"
	 + "Expires: Thu, 31 Dec 2037 23:59:59 GMT\n\n").getBytes() ;

    private static final byte[] css_no_expire_no_cookie_ok_gzip = 
	("HTTP/1.1 200 OK\n"
	 + "Content-Type: text/css\n"
	 + "Expires: Thu, 31 Dec 2037 23:59:59 GMT\n"
	 + "Content-Encoding: gzip\n\n").getBytes() ;

    /** Send a request header for CSS with immediate expiration. */

    public final void sendCssNoExpireNoCookieHeader()
	throws Exception
    {
	httpOutput.write((gzipOk) ? css_no_expire_no_cookie_ok_gzip : css_no_expire_no_cookie_no_gzip) ;
    }

    /** Send a request header for CSS with immediate expiration, gzip content. */

    public final void sendCssNoExpireNoCookieHeaderOkGzip()
	throws Exception
    {
	httpOutput.write(css_no_expire_no_cookie_ok_gzip) ;
    }

    /** Send a request header for CSS with immediate expiration, no gzip content. */

    public final void sendCssNoExpireNoCookieHeaderNoGzip()
	throws Exception
    {
	httpOutput.write(css_no_expire_no_cookie_no_gzip) ;
    }








    /** Used by sendCssDownloadNoExpireNoCookieHeader. */

    private static final byte[] css_download_no_expire_1 = 
	("HTTP/1.1 200 OK\n"
	 + "Content-Type: text/css\n"
         + "Content-Disposition: attachment ; filename=").getBytes() ;

    private static final byte[] css_download_no_expire_no_cookie_no_gzip_2 = 
	("\n"
         + "Expires: Thu, 31 Dec 2037 23:59:59 GMT\n\n").getBytes() ;

    private static final byte[] css_download_no_expire_no_cookie_ok_gzip_2 = 
	("\n"
	 + "Expires: Thu, 31 Dec 2037 23:59:59 GMT\n"
	 + "Content-Encoding: gzip\n\n").getBytes() ;

    /** Send a request header for Css with immediate expiration and no cookie. */

    public final void sendCssDownloadNoExpireNoCookieHeader(String fileName)
	throws Exception
    {
        httpOutput.write(css_download_no_expire_1) ;
        httpOutput.write(fileName.getBytes()) ;
	httpOutput.write((gzipOk) ? css_download_no_expire_no_cookie_ok_gzip_2 : css_download_no_expire_no_cookie_no_gzip_2) ;
    }

    /** Send a request header for Css with immediate expiration and no cookie, gzip content. */

    public final void sendCssDownloadNoExpireNoCookieHeaderOkGzip(String fileName)
	throws Exception
    {
        httpOutput.write(css_download_no_expire_1) ;
        httpOutput.write(fileName.getBytes()) ;
	httpOutput.write(css_download_no_expire_no_cookie_ok_gzip_2) ;
    }

    /** Send a request header for Css with immediate expiration and no cookie, no gzip content. */

    public final void sendCssDownloadNoExpireNoCookieHeaderNoGzip(String fileName)
	throws Exception
    {
        httpOutput.write(css_download_no_expire_1) ;
        httpOutput.write(fileName.getBytes()) ;
	httpOutput.write(css_download_no_expire_no_cookie_no_gzip_2) ;
    }








    /** Used by sendMimeNoExpireNoCookieHeader. */

    private static final byte[] mime_no_expire_no_cookie_1 = 
	("HTTP/1.1 200 OK\n"
	 + "Expires: Thu, 31 Dec 2037 23:59:59 GMT\n"
	 + "Content-Type: ").getBytes() ;

    /** Used by sendMimeNoExpireMimeHeader. */

    private static final byte[] mime_no_expire_no_cookie_no_gzip_2 = "\n\n".getBytes() ;

    /** Used by sendMimeNoExpireMimeHeader. */

    private static final byte[] mime_no_expire_no_cookie_ok_gzip_2 = "\nContent-Encoding: gzip\n\n".getBytes() ;

    /** Send a request header for file with immediate expiration. */

    public final void sendMimeNoExpireNoCookieHeader(byte[] mimeType)
	throws Exception
    {
	httpOutput.write(mime_no_expire_no_cookie_1) ;
	httpOutput.write(mimeType) ;
	httpOutput.write((gzipOk) ? mime_no_expire_no_cookie_ok_gzip_2 : mime_no_expire_no_cookie_no_gzip_2) ;
    }

    /** Send a request header for file with immediate expiration, gzip ok. */

    public final void sendMimeNoExpireNoCookieHeaderOkGzip(byte[] mimeType)
	throws Exception
    {
	httpOutput.write(mime_no_expire_no_cookie_1) ;
	httpOutput.write(mimeType) ;
	httpOutput.write(mime_no_expire_no_cookie_ok_gzip_2) ;
    }

    /** Send a request header for file with immediate expiration, no gzip. */

    public final void sendMimeNoExpireNoCookieHeaderNoGzip(byte[] mimeType)
	throws Exception
    {
	httpOutput.write(mime_no_expire_no_cookie_1) ;
	httpOutput.write(mimeType) ;
	httpOutput.write(mime_no_expire_no_cookie_no_gzip_2) ;
    }









    /** Used by sendMimeDownloadNoExpireNoCookieHeader. */

    private static final byte[] mime_download_no_expire_no_cookie_1 = 
	("HTTP/1.1 200 OK\n"
	 + "Expires: Thu, 31 Dec 2037 23:59:59 GMT\n"
	 + "Content-Type: ").getBytes() ;

    /** Used by sendMimeDownloadNoExpireMimeHeader. */

    private static final byte[] mime_download_no_expire_no_cookie_2 = 
        ("\n"
         + "Content-Disposition: attachment ; filename=").getBytes() ;

    /** Used by sendMimeDownloadNoExpireMimeHeader. */

    private static final byte[] mime_download_no_expire_no_cookie_no_gzip_3 = "\n\n".getBytes() ;

    /** Used by sendMimeDownloadNoExpireMimeHeader. */

    private static final byte[] mime_download_no_expire_no_cookie_ok_gzip_3 = "\nContent-Encoding: gzip\n\n".getBytes() ;

    /** Send a request header for file with immediate expiration and force download. */

    public final void sendMimeDownloadNoExpireNoCookieHeader(byte[] mimeType, String fileName)
	throws Exception
    {
	httpOutput.write(mime_download_no_expire_no_cookie_1) ;
	httpOutput.write(mimeType) ;
	httpOutput.write(mime_download_no_expire_no_cookie_2) ;
	httpOutput.write(fileName.getBytes()) ;
	httpOutput.write((gzipOk) ? mime_download_no_expire_no_cookie_ok_gzip_3 : mime_download_no_expire_no_cookie_no_gzip_3) ;
    }

    /** Send a request header for file with immediate expiration, gzip ok. */

    public final void sendMimeDownloadNoExpireNoCookieHeaderOkGzip(byte[] mimeType, String fileName)
	throws Exception
    {
	httpOutput.write(mime_download_no_expire_no_cookie_1) ;
	httpOutput.write(mimeType) ;
	httpOutput.write(mime_download_no_expire_no_cookie_2) ;
	httpOutput.write(fileName.getBytes()) ;
	httpOutput.write(mime_download_no_expire_no_cookie_ok_gzip_3) ;
    }

    /** Send a request header for file with immediate expiration, no gzip. */

    public final void sendMimeDownloadNoExpireNoCookieHeaderNoGzip(byte[] mimeType, String fileName)
	throws Exception
    {
	httpOutput.write(mime_download_no_expire_no_cookie_1) ;
	httpOutput.write(mimeType) ;
	httpOutput.write(mime_download_no_expire_no_cookie_2) ;
	httpOutput.write(fileName.getBytes()) ;
	httpOutput.write(mime_download_no_expire_no_cookie_no_gzip_3) ;
    }









    /** Used by sendMimeExpireNoCookieHeader. */

    private static final byte[] mime_expire_no_cookie_1 = 
	("HTTP/1.1 200 OK\n"
	 + "Expires: 0\n"
	 + "Content-Type: ").getBytes() ;

    /** Used by sendMimeExpireMimeHeader. */

    private static final byte[] mime_expire_no_cookie_no_gzip_2 = "\n\n".getBytes() ;

    /** Used by sendMimeExpireMimeHeader. */

    private static final byte[] mime_expire_no_cookie_ok_gzip_2 = "\nContent-Encoding: gzip\n\n".getBytes() ;

    /** Send a request header for HTML with immediate expiration. */

    public final void sendMimeExpireNoCookieHeader(byte[] mimeType)
	throws Exception
    {
	httpOutput.write(mime_expire_no_cookie_1) ;
	httpOutput.write(mimeType) ;
	httpOutput.write((gzipOk) ? mime_expire_no_cookie_ok_gzip_2 : mime_expire_no_cookie_no_gzip_2) ;
    }







    /** Flushing the output. */

    public final void flush()
	throws Exception
    {
	httpOutput.flush() ;
    }

    /** Writing bytes to the output. */

    public final void write(byte [] b)
	throws Exception
    {
	httpOutput.write(b) ;
    }

    /** Writing bytes to the output. */

    public final void write(byte [] b, int off, int len)
	throws Exception
    {
	httpOutput.write(b,off,len) ;
    }

    /** Writing a String to the output. */

    public final void write(String s)
	throws Exception
    {
	httpOutput.write(s.getBytes()) ;
    }

    /** Writing an integer to the output. */

    public final void write(int i)
	throws Exception
    {
	httpOutput.write(Integer.toString(i).getBytes()) ;
    }

    /** Writes a file content to the output. */

    public final void writeFile(String fileName)
	throws Exception
    {
	BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileName)) ;

	byte[] buf = new byte[1024];
	int    len ;

	while ((len = in.read(buf)) > 0)
	    httpOutput.write(buf,0,len);
           
	in.close() ;
    }








    /** For dynamic GZIP. */

    public final void beginOutput()
	throws Exception
    {
	if (gzipOk)
	    httpOutput = new GZIPOutputStream(httpOutput) ;
    }

    public final void endOutput()
	throws Exception
    {
	if (gzipOk)
	    ((GZIPOutputStream)httpOutput).finish() ;
    }





}
