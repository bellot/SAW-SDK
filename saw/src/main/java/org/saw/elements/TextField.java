package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class TextField extends Input
{

    public TextField(String className, String name, String width)
    {
	super(className,
	      "type='text'"
	      + " name='" + name + "'"
	      + " id='" + name + "'"
	      + " style='width:" + width + ";'"
	      + " maxlength='255'") ;
    }  

}
