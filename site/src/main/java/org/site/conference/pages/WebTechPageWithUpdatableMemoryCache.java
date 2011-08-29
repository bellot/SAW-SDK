package org.site.conference.pages ;

import org.saw.compilations.* ;
import org.saw.elements.* ;
import org.saw.sessions.* ;
import org.saw.transaction.* ;
import org.site.conference.web.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public abstract class WebTechPageWithUpdatableMemoryCache extends SessionBinzWithUpdatableMemoryCache
{
    private final Compilation compilation ;

    public WebTechPageWithUpdatableMemoryCache(ElementInterface mainDivElement) 
        throws Exception
    {
        compilation
            = new Html(new Head(new Title(PageTitle.element),
                                new StyleSheetLink("../page/CSS.class"),
                                new FaviconLink("text/png","../page/favicon.png")), 
                       new Body(null,"id='main'",
                                new Div(null, "id='container'",
                                        new Iframe(null, "id='header'      allowtransparency='true' src='../page/Header.class'"),
                                        new Iframe(null, "id='announces'   allowtransparency='true' src='../page/Announces.html'"),
                                        new Iframe(null, "id='loginlogout' allowtransparency='true' src='../page/LoginLogout.class'"),
                                        new Iframe(null, "id='navigation'  allowtransparency='true' src='../page/Navigation.html'"),
                                        new Div   (null, "id='main'",      mainDivElement),
                                        new Iframe(null, "id='footer'      allowtransparency='true' src='../page/Footer.html'")))).compile() ;

        PageTexts .entity.addUpdatableFriend(this) ; // Because of title
    }

    public WebTechPageWithUpdatableMemoryCache(String cssFile, ElementInterface mainDivElement) 
        throws Exception
    {
        compilation
            = new Html(new Head(new Title(PageTitle.element),
                                new StyleSheetLink("../page/CSS.class"),
                                new StyleSheetLink(cssFile),
                                new FaviconLink("text/png","../page/favicon.png")), 
                       new Body(null,"id='main'",
                                new Div(null, "id='container'",
                                        new Iframe(null, "id='header'      allowtransparency='true' src='../page/Header.class'"),
                                        new Iframe(null, "id='announces'   allowtransparency='true' src='../page/Announces.html'"),
                                        new Iframe(null, "id='loginlogout' allowtransparency='true' src='../page/LoginLogout.class'"),
                                        new Iframe(null, "id='navigation'  allowtransparency='true' src='../page/Navigation.html'"),
                                        new Div   (null, "id='main'",      mainDivElement),
                                        new Iframe(null, "id='footer'      allowtransparency='true' src='../page/Footer.html'")))).compile() ;
    }

    public final void writeContent(TransactionOutput transactionnOutput)
	throws Exception
    {
        compilation.writeTo(transactionnOutput) ;
    }

}

