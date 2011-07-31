package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class Submit extends Input
{

    public Submit(String className, String name, String value)
    {
	super(className,
	      "type='submit'"
	      + " name='" + name + "'"
	      + " value='" + value  +"'") ;
    }  

}
