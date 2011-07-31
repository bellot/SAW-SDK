package org.saw.transaction ;

import org.saw.exceptions.* ;

/** This is the base class for page content output functions.<br>
 *  Concrete subclasses are:<br>
 *  - <code>Transaction</code> : to output a page to user agent<br>
 *  - <code>TransactionOutputFile</code> : to output a page in a file</br>
 *  @author  Patrick Bellot, &copy; 2008 and later. 
 */

public abstract class TransactionOutput 
{
    /** To be called before output begins. */

    public abstract void beginOutput()
	throws Exception ;

    /** To be called after output. */

    public abstract void endOutput()
	throws Exception ;

    /** Flushing the output. */

    public abstract void flush()
        throws Exception ;

    /** Writing bytes to the output. */

    public abstract void write(byte [] b)
	throws Exception ;

    /** Writing bytes to the output. */

    public abstract void write(byte [] b, int off, int len)
	throws Exception ;

    /** Writing a String to the output. */

    public abstract void write(String s)
	throws Exception ;

    /** Writing an integer to the output. */

    public abstract void write(int i)
	throws Exception ;

    /** Writing a file to the output. */

    public abstract void writeFile(String f)
	throws Exception ;

    /** Returns the Transaction variables, can only be used with the real Transaction object. */
    public TransactionVariables getTransactionVariables() 
        throws InternalErrorException
    {
        throw new InternalErrorException("Call to getTransactionVariables() when caching") ;
    }

    /** Returns the value of a Transaction variable, can only be used with the real Transaction object. */
    public String getTransactionVariable(String name) 
        throws InternalErrorException
    {
        throw new InternalErrorException("Call to getTransactionVariable(String name) when caching") ;
    }

    /** Returns the Transaction variables, can only be used with the real Transaction object. */
    public TransactionArrayVariables getTransactionArrayVariables()
        throws InternalErrorException
    {
        throw new InternalErrorException("Call to getTransactionArrayVariables() when caching") ;
    }

    /** Returns the Session environment, can only be used with the real Transaction object. */
    public SessionEnvironment getSessionEnvironment() 
        throws InternalErrorException
    {
        throw new InternalErrorException("Call to getSessionEnvironment() when caching") ;
    }

    /** Returns the client ip, can only be used with the real Transaction object. */

    public String getIp()
        throws InternalErrorException
    {
        throw new InternalErrorException("Call to getIp() when caching") ;
    }

    /** Returns the requestPath, can only be used with the real Transaction object. */

    public String getRequestPath()
        throws InternalErrorException
    {
        throw new InternalErrorException("Call to getRequestPath() when caching") ;
    }

    /** Returns the request type, can only be used with the real Transaction object. */

    public int getRequestType()
        throws InternalErrorException
    {
        throw new InternalErrorException("Call to getRequestType() when caching") ;
    }

    /** Returns <b>true</code> if it is a SSL request, can only be used with the real Transaction object. */

    public boolean isSSL()
        throws InternalErrorException
    {
        throw new InternalErrorException("Call to isSSL() when caching") ;
    }

}
