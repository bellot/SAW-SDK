package conference.login;

import org.saw.compilations.* ;
import org.saw.elements.* ;
import org.saw.exceptions.* ;
import org.saw.sessions.* ;
import org.saw.transaction.* ;
import org.saw.util.logs.* ;

import org.site.conference.conf.* ;
import org.site.conference.web.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class Login extends SessionBinz
{

    private final byte[] SSLLogin = (Transaction.HTTPS_WEB_SITE + "/conference/login/SSLLogin.class").getBytes() ;

    public final void handle(Transaction transaction)
        throws Exception
    {
        SessionEnvironment sessionEnvironment = transaction.getSessionEnvironment() ;

        if (sessionEnvironment.getUser() != null) {

            Logs.log(Logs.SECURITY_WARNING,"User tries to login  while logged.",
                     Logs.USERID_TAG,      Integer.toString(sessionEnvironment.getUser().userId)) ;

            sessionEnvironment.setUser(null) ;
        }

        SessionVariables sessionVariables = sessionEnvironment.getSessionVariables() ;

        if (transaction.isSSL()) {
            sessionVariables.put("isSSL","") ;
        } else {
            sessionVariables.remove("isSSL") ;
        }
            
        transaction.sendHttpRedirection(SSLLogin) ;
    }

}
