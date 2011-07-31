package org.site.conference.web ;

import org.saw.elements.* ;
import org.saw.transaction.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class HeaderLine2 extends DynamicElement
{
    public static final HeaderLine2 element = new HeaderLine2() ;
    
    public final void writeTo(TransactionOutput transactionOutput)
            throws Exception
    {
        transactionOutput.write(PageTexts.entity.getHeaderLine2()) ;
    }
}
