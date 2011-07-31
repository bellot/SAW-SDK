package conference.login;

import org.saw.compilations.* ;
import org.saw.dynamic.* ;
import org.saw.elements.* ;
import org.saw.exceptions.* ;
import org.saw.sessions.* ;
import org.saw.transaction.* ;

import org.site.conference.pages.* ;
import org.site.conference.web.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class LoginSuccess extends WebTechPageWithUpdatableMemoryCache
{
    public LoginSuccess()
        throws Exception
    {
        super(new Container(JSLoginReload.element,
                            new H1("Successfull login"),
                            new Form(null,"method='POST' action='../admin/Console.class' style='margin:15px;'",
                                     new P("center",
                                           new Submit("submit_input","loginButton","Console","style='width:70px;'"))))) ;
    }

 }

