package org.site.conference.pages ;

import org.saw.compilations.* ;
import org.saw.elements.* ;
import org.saw.sessions.* ;
import org.saw.transaction.* ;
import org.site.conference.web.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public abstract class WebTechPage extends SessionBinz
{
    private final Compilation compilation ;

    public WebTechPage(ElementInterface mainElement) 
        throws Exception
    {
        compilation
            = new Html(new Head(new Title(PageTitle.element),
                                new StyleSheetLink("../page/CSS.class"),
                                new FaviconLink("text/png","../page/favicon.png")), 
                       new Body(null,"id='main'",
                                new Div(null, "id='container'",
                                        new Iframe(null, "id='header'      src='../page/Header.class'"),
                                        new Iframe(null, "id='announces'   src='../page/Announces.html'"),
                                        new Iframe(null, "id='loginlogout' src='../page/LoginLogout.class'"),
                                        new Iframe(null, "id='navigation'  src='../page/Navigation.html'"),
                                        new Div   (null, "id='main'",      mainElement),
                                        new Iframe(null, "id='footer'      src='../page/Footer.html'")))).compile() ;
    }

    public void handle(Transaction transaction)
	throws Exception
    {
	transaction.sendHtmlExpireHeader() ;
        transaction.beginOutput() ;
        compilation.writeTo(transaction) ;
        transaction.endOutput() ;
    }

}

