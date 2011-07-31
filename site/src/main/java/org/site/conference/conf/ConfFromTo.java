package org.site.conference.conf ;

import org.saw.elements.* ;
import org.saw.transaction.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class ConfFromTo extends DynamicElement
{
    public static final ConfFromTo element = new ConfFromTo() ;
    
    public final void writeTo(TransactionOutput transactionOutput)
            throws Exception
    {
        transactionOutput.write(ConfDates.entity.getConferenceFromToText()) ;
    }
}
