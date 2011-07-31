package org.site.conference.conf ;

import org.saw.elements.* ;
import org.saw.transaction.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class ConfPlace extends DynamicElement
{
    public static final ConfPlace element = new ConfPlace() ;
    
    public final void writeTo(TransactionOutput transactionOutput)
            throws Exception
    {
        transactionOutput.write(ConfInfo.entity.getPlace()) ;
    }
}
