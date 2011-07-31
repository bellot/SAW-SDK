package org.saw.elements ;

/** The base class of all input widgets.
 *  @author  Patrick Bellot, &copy; 2010 and later.
 */

public abstract class Input extends XmlElement
{

    public Input(String className, String attributes)
    {
	super("input",className,attributes) ;
    }

}
