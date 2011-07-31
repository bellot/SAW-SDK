package org.saw.dynamic ;

import org.saw.elements.* ;
import org.saw.transaction.* ;

/** A dynamic element that outputs the value of a Transaction variable.
 *  @author  Patrick Bellot, &copy; 2008 and later.
 */

public class TransactionVariable extends DynamicElement
{
    private final String variableName ;

    public TransactionVariable(String variableName)
    {
	this.variableName = variableName ;
    }

    public final void writeTo(TransactionOutput transactionOuput) 
	throws Exception
    {
	transactionOuput.write(transactionOuput.getTransactionVariables().get(variableName)) ;
    }

}
