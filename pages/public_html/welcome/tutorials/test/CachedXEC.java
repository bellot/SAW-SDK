package welcome.tutorials.test ;

import org.saw.elements.* ;
import org.saw.compilations.* ;
import org.saw.sessions.* ;
import org.saw.transaction.* ;

public class CachedXEC extends SessionBinzWithFileCache
{
    private final Compilation compilation ;

    public CachedXEC() 
        throws Exception
    {
        compilation
            = new Html(new Head(new Title("A Cached XEC page")),
                       new Body(new H1("Hello World"))).compile() ;
    }

    public void writeContent(TransactionOutput transactionOutput)
        throws Exception
    {
        compilation.writeTo(transactionOutput) ;
    }

}
