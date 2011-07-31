package welcome.tutorials.test ;

import org.saw.sessions.* ;
import org.saw.transaction.* ;

public class PHPTV2 extends SessionBinz
{
    public void handle(Transaction transaction)
        throws Exception
    {
        // Here we extract a variable value
        String person = transaction.getTransactionVariable("person") ;

        transaction.sendHtmlNoExpireHeader() ;

        transaction.beginOutput() ;

        transaction.write("<html>") ;
        transaction.write(   "<head>") ;
        transaction.write(      "<title>Using Transaction variables</title>") ;
        transaction.write(   "</head>") ;
        transaction.write(   "<body>") ;
        transaction.write(      "<h1>The person is " + person + "</h1>");
        transaction.write(   "</body>") ;
        transaction.write("</html>") ;

        transaction.endOutput() ;
    }

}
