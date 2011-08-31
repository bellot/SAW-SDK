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

public class SSLLogin extends WebTechPageWithUpdatableMemoryCache
{
    public SSLLogin()
        throws Exception
    {
        super(new Container(JSLoginReload.element,
                            new JSGiveFocus("loginName"),
                            new H1("Login name and password"),
                            new Form(null,"method='POST' action='SSLLoginCheck.class' style='margin:15px;'",
                                     new P("center",
                                           new TextField("text_input","loginName","200px")),
                                     new P("center",
                                           new PasswordField("text_input","password","200px")),
                                     new P("center",
                                           new Submit("submit_input","loginButton","Login","style='width:70px;'"))))) ;
    }

    public final void handle(Transaction transaction)
        throws Exception
    {
        SessionEnvironment sessionEnvironment = transaction.getSessionEnvironment() ;

        if (sessionEnvironment.getUser() != null) {

            Logs.log(Logs.SECURITY_WARNING_CAT, "User tries to log while logged.",
                     Logs.USER_ID_TAG,          Integer.toString(sessionEnvironment.getUser().userId)) ;

            sessionEnvironment.setUser(null) ;
        }

        super.handle(transaction) ;
    }
}

