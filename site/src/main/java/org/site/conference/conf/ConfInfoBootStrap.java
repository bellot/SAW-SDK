package org.site.conference.conf ;

import java.io.* ;

import org.saw.entities.* ;
import org.saw.util.dates.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class ConfInfoBootStrap
{
    public final static void bootStrap()
    {
        ConfInfo.entity.set("Web Technologies & Internet Applications",
                            "WebTech 2012",
                            "Hotel Fort Canning, Singapore",
                            "bellot@telecom-paristech.fr") ;
    }
}
