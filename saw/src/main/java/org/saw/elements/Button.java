package org.saw.elements ;

/** Class for a button input.
 *  @author  Patrick Bellot, &copy; 2010 and later.
 */

public class Button extends Input
{

    public Button(String className, String name, String value, String attr)
    {
	super(className,
	      "type='button' "
	      + "name='" + name + "' "
	      + "value='" + value  +"' "
	      + attr) ;
    }  

}
