package org.saw.elements ;

import org.saw.util.dates.* ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class InputSetValue extends Container
{
    public InputSetValue(String name, ElementInterface value)
    {
	super(new CDATA("document.getElementById(\"" + name + "\").value=\""),
              value,
              new CDATA("\";")) ;
    }

    public InputSetValue(String name, String value)
    {
        this(name,new CDATA(value)) ;
    }
}
