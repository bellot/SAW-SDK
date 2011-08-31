package conference.login;

import org.saw.compilations.* ;
import org.saw.dynamic.* ;
import org.saw.elements.* ;
import org.saw.exceptions.* ;
import org.saw.sessions.* ;
import org.saw.transaction.* ;
import org.saw.util.logs.* ;

import org.site.conference.pages.* ;
import org.site.conference.web.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class Logout extends WebTechPage
{
    public Logout()
        throws Exception
    {
        super(new Container(JSLoginReload.element,
                            new H1("Successfull logout"))) ;
    }

     public final void handle(Transaction transaction)
        throws Exception
    {
        SessionEnvironment sessionEnvironment = transaction.getSessionEnvironment() ;

        if (sessionEnvironment.getUser() == null)
            Logs.log(Logs.SECURITY_WARNING_CAT, "User tries to logout while not logged.") ;
           
        sessionEnvironment.setUser(null) ;

        super.handle(transaction) ;

        Logs.log(Logs.USER_LIFE_CAT, "Logout.",
                 Logs.USER_ID_TAG,   Integer.toString(sessionEnvironment.getUser().userId)) ;
    }
}

