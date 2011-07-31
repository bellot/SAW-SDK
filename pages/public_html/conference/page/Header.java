package conference.page;

import org.saw.compilations.* ;
import org.saw.elements.* ;
import org.saw.sessions.* ;
import org.saw.transaction.* ;
import org.site.conference.conf.* ;
import org.site.conference.web.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class Header extends SessionBinzWithUpdatableMemoryCache
{
    private final Compilation compilation ;

    public Header() 
        throws Exception
    {
        compilation
            = new Html(new Head(new Title(PageTitle.element),
                                new StyleSheetLink("../page/CSS.class")),
                       new Body(null,"id='header'",
                                new Table(new Tr(new Td(new H2(HeaderLine1.element),
                                                        new H1(HeaderLine2.element),
                                                        new H2(HeaderLine3.element),
                                                        new H3(ConfFromTo.element,
                                                               new CDATA(" &mdash; "),
                                                               ConfPlace.element)))))).compile() ;
        
        PageTexts .entity.addUpdatableFriend(this) ; // Because of title and header lines 1,2,3
        ConfDates .entity.addUpdatableFriend(this) ; // Because of 4th header line
        ConfInfo  .entity.addUpdatableFriend(this) ; // Because of 4th header line
    }

    public void writeContent(TransactionOutput transactionOutput)
        throws Exception
    {
        compilation.writeTo(transactionOutput) ;
    }

}

