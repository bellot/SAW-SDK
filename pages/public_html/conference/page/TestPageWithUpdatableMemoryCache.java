package conference.page ;

import org.saw.compilations.* ;
import org.saw.elements.* ;
import org.saw.sessions.* ;
import org.saw.transaction.* ;

import org.site.conference.pages.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class TestPageWithUpdatableMemoryCache extends WebTechPageWithUpdatableMemoryCache
{
    public TestPageWithUpdatableMemoryCache()
        throws Exception
    {
        super(new H1("Hello world !")) ;
    }
}

