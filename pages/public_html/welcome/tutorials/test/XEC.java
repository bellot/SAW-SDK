package welcome.tutorials.test ;

import org.saw.elements.* ;
import org.saw.compilations.* ;
import org.saw.sessions.* ;
import org.saw.transaction.* ;

public class XEC extends SessionBinz
{
    private final Compilation compilation ;

    public XEC() 
    {
        compilation
            = new Html(new Head(new Title("A XEC page")),
                       new Body(new H1("Hello World"))).compile() ;
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
