package org.site.conference.dates ;

import org.saw.elements.* ;
import org.saw.transaction.* ;
import org.saw.util.dates.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class JMAAAANow extends DynamicElement
{
    public final void writeTo(TransactionOutput transactionOutput)
            throws Exception
    {
        transactionOutput.write(DatesUtils.jmaaaaDateFromMillis(System.currentTimeMillis())) ;
    }
}
