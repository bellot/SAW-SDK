package org.saw.exceptions ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class TransactionMustExitException extends Exception
{
    static final long serialVersionUID = 20101023L ;

    public TransactionMustExitException()
    {
	super("Transaction must exit (null socket provided)") ;
    }

}
