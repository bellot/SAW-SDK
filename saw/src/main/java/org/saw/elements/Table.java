package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class Table extends XmlElement
{

    public Table(String className, ElementInterface... elements)
    {
	super("table",className,null,elements) ;
    }

    public Table(ElementInterface... elements)
    {
	this(null,elements) ;
    }

}
