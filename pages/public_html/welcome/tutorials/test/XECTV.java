package welcome.tutorials.test ;

import org.saw.dynamic.* ;
import org.saw.elements.* ;
import org.saw.compilations.* ;
import org.saw.sessions.* ;
import org.saw.transaction.* ;

public class XECTV extends SessionBinz
{
    private final Compilation compilation ;

    public XECTV() 
    {
        compilation
            = new Html(new Head(new Title("XEC and Transaction variables")),
                       new Body(new P(new CDATA("The person is "),
                                      new TransactionVariable("person"), // Here it is !
                                      new CDATA(" !")))).compile() ;
    }

    public void handle(Transaction transaction)
        throws Exception
    {
        transaction.sendHtmlExpireHeader() ;

        transaction.beginOutput() ;
        compilation.writeTo(transaction) ;
        transaction.endOutput() ;
    }

}
