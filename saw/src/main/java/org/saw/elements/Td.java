package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class Td extends XmlElement
{

    public Td(String className, ElementInterface... elements)
    {
	super("td",className,null,elements) ;
    }

    public Td(ElementInterface... elements)
    {
	super("td",null,null,elements) ;
    }

}
