package welcome.tutorials.test ;

import org.saw.dynamic.* ;
import org.saw.elements.* ;
import org.saw.compilations.* ;
import org.saw.sessions.* ;
import org.saw.transaction.* ;

public class XECTV2 extends SessionBinz
{
    private final Compilation compilation ;

    public XECTV2() 
    {
        compilation
            = new Html(new Head(new Title("XEC and Transaction variables")),
                       new Body(new P(new CDATA("The person is "),
                                      new TransactionVariable("person"), // Here it is !
                                      new CDATA(" !"))),
                       new Body(new P(new CDATA("His place is "),
                                      new TransactionVariable("place"), // Another one !
                                      new CDATA(" !")))).compile() ;
    }

    public void handle(Transaction transaction)
        throws Exception
    {
        // Here, we get the transaction variable object
        TransactionVariables transactionVariables
            = transaction.getTransactionVariables() ;

        // Setting the value of place
        transactionVariables.put("place","Paris, France") ;

        transaction.sendHtmlExpireHeader() ;

        transaction.beginOutput() ;
        compilation.writeTo(transaction) ;
        transaction.endOutput() ;
    }

}
