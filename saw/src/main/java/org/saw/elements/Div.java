package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class Div extends XmlElement
{

    public Div(String className, String attr, ElementInterface... elements)
    {
	super("div",className,attr,elements) ;
    }

    public Div(String className, ElementInterface... elements)
    {
	super("div",className,null,elements) ;
    }

}
