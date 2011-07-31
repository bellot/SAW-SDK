package conference.page;

import org.saw.compilations.* ;
import org.saw.elements.* ;
import org.saw.sessions.* ;
import org.saw.transaction.* ;

import org.site.conference.pages.* ;
import org.site.conference.web.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class TestPage extends WebTechPage
{
    public TestPage()
        throws Exception
    {
        super(new H1("Hello world !")) ;
    }
}

