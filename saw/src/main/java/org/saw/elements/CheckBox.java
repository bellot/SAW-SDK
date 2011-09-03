package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class CheckBox extends Input
{

    public CheckBox(String className, String name, String value, boolean checked)
    {
	super(className,
	      "type='checkbox'" 
	      + " name='" + name + "'"
	      + " id='" + name + "'"
	      + " value='" + value +"'" 
	      + ((checked)?" checked":"")) ;
    }  

}
