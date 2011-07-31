package org.site.conference.web ;

import org.saw.elements.* ;
import org.saw.transaction.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class HeaderLine1 extends DynamicElement
{
    public static final HeaderLine1 element = new HeaderLine1() ;
    
    public final void writeTo(TransactionOutput transactionOutput)
            throws Exception
    {
        transactionOutput.write(PageTexts.entity.getHeaderLine1()) ;
    }
}
