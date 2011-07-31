package org.site.conference.web ;

import org.saw.elements.* ;
import org.saw.transaction.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class PageTitle extends DynamicElement
{
    public static final PageTitle element = new PageTitle() ;
    
    public final void writeTo(TransactionOutput transactionOutput)
            throws Exception
    {
        transactionOutput.write(PageTexts.entity.getPageTitle()) ;
    }
}
