package conference.page;

import org.saw.compilations.* ;
import org.saw.elements.* ;
import org.saw.sessions.* ;
import org.saw.transaction.* ;
import org.saw.util.bytes.* ;
import org.site.conference.web.* ;

/** @author  Patrick Bellot, &copy; 2011 and later. */

public class LoginLogout extends SessionBinz
{  
    private final byte[] loginOkGzipArray ;
    private final byte[] logoutOkGzipArray ;
    private final byte[] loginNoGzipArray ;
    private final byte[] logoutNoGzipArray ;

    public LoginLogout()
        throws Exception
    {
        loginNoGzipArray =
            (   "<html>"
              +   "<head>"
              +     "<title>WebTech 2012</title>"
              +     "<link rel='stylesheet' type='text/css' media='screen' href='../page/CSS.class' />"
              +     "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>"
              +   "</head>"
              +   "<body id='loginlogout'>"
              +     "<table>"
              +       "<tr>"
              +         "<td>"
              +           "<a href='../login/Login.class' target='_top'>Login</a>"
              +         "</td>"
              +       "</tr>"
              +       "<tr>"
              +         "<td>"
              +           "<a>Create account</a>"
              +         "</td>"
              +       "</tr>"
              +     "</table>"
              +   "</body>"
              + "</html>" ).getBytes() ;

        loginOkGzipArray = GzipBytes.zip(loginNoGzipArray) ;
        
        logoutNoGzipArray =
            (   "<html>"
              +   "<head>"
              +     "<title>WebTech 2012</title>"
              +     "<link rel='stylesheet' type='text/css' media='screen' href='../page/CSS.class' />"
              +     "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>"
              +   "</head>"
              +   "<body id='loginlogout'>"
              +     "<table>"
              +       "<tr>"
              +         "<td>"
              +           "<a href='../login/Logout.class' target='_top'>Logout</a>"
              +         "</td>"
              +       "</tr>"
              +       "<tr>"
              +         "<td>"
              +           "<a><a href='../admin/Console.class' target='_top'>Console</a>"
              +         "</td>"
              +       "</tr>"
              +     "</table>"
              +   "</body>"
              + "</html>" ).getBytes() ;

        logoutOkGzipArray = GzipBytes.zip(logoutNoGzipArray) ;
    }


    public void handle(Transaction transaction)
        throws Exception
    {
        if (transaction.getSessionEnvironment().getUser() == null) {
            if (transaction.getGzipOk()) {
                transaction.sendHtmlNoExpireHeaderOkGzip() ;
                transaction.write(loginOkGzipArray) ;
            } else {
                transaction.sendHtmlNoExpireHeaderNoGzip() ;
                transaction.write(loginNoGzipArray) ;
            }
        } else {
            if (transaction.getGzipOk()) {
                transaction.sendHtmlNoExpireHeaderOkGzip() ;
                transaction.write(logoutOkGzipArray) ;
            } else {
                transaction.sendHtmlNoExpireHeaderNoGzip() ;
                transaction.write(logoutNoGzipArray) ;
            }
        }
    }

}

