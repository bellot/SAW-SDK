package welcome.tutorials.test ;

import org.saw.sessions.* ;
import org.saw.transaction.* ;

public class PHPLike extends SessionBinz
{
    public void handle(Transaction transaction)
        throws Exception
    {
        transaction.sendHtmlNoExpireHeader() ;

        transaction.beginOutput() ;

        transaction.write("<html>") ;
        transaction.write(   "<head>") ;
        transaction.write(      "<title>A PHP-like Session Binz</title>") ;
        transaction.write(   "</head>") ;
        transaction.write(   "<body>") ;
        transaction.write(      "<h1>Hello World</h1>") ;
        transaction.write(   "</body>") ;
        transaction.write("</html>") ;

        transaction.endOutput() ;
    }
}