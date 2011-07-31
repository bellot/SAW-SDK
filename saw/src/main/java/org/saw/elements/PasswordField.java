package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class PasswordField extends Input
{

    public PasswordField(String className, String name, String width)
    {
	super(className,
	      "type='password'"
	      + " name='" + name + "'"
	      + " id='" + name + "'"
	      + " style='width:" + width + ";'"
	      + " maxlength='255'") ;
    }  

}
