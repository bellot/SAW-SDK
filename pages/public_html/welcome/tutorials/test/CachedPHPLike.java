package welcome.tutorials.test ;

import org.saw.sessions.* ;
import org.saw.transaction.* ;

public class CachedPHPLike extends SessionBinzWithMemoryCache
{
    // Empty constructor required to declare that it may throw an Exception

    public CachedPHPLike()
        throws Exception
    {}

    public void writeContent(TransactionOutput transactionOutput)
        throws Exception
    {
        transactionOutput.write("<html>") ;
        transactionOutput.write(   "<head>") ;
        transactionOutput.write(      "<title>A Cached PHP-like Session Binz</title>") ;
        transactionOutput.write(   "</head>") ;
        transactionOutput.write(   "<body>") ;
        transactionOutput.write(      "<h1>Hello World</h1>") ;
        transactionOutput.write(   "</body>") ;
        transactionOutput.write("</html>") ;
    }
}