package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class Body extends XmlElement
{

    public Body(String className, String attr, ElementInterface... elements)
    {
	super("body",className,attr,elements) ;
    }

    public Body(String className, ElementInterface... elements)
    {
	super("body",className,null,elements) ;
    }

    public Body(ElementInterface... elements)
    {
	super("body",null,null,elements) ;
    }

}
