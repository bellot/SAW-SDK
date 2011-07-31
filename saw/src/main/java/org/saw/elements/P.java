package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class P extends XmlElement
{

    public P(String className, String attr, ElementInterface... elements)
    {
	super("p",className,attr,elements) ;
    }

    public P(String className, ElementInterface... elements)
    {
	super("p",className,null,elements) ;
    }

    public P(ElementInterface... elements)
    {
	super("p",null,null,elements) ;
    }

}
