package org.site.conference.dates ;

import org.saw.elements.* ;
import org.saw.transaction.* ;
import org.saw.util.dates.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class JMAAAAOneWeekAgo extends DynamicElement
{
    public final void writeTo(TransactionOutput transactionOutput)
            throws Exception
    {
        transactionOutput.write(DatesUtils.jmaaaaDateFromMillis(System.currentTimeMillis() - (7L * 24L * 60L * 60L * 1000L))) ;
    }
}
