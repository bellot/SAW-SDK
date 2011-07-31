package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class H3 extends XmlElement
{

    public H3(String className, String attr, String text)
    {
	super("h3",className,attr,new CDATA(text)) ;
    }

    public H3(String className, String text)
    {
	this(className,null,text) ;
    }

    public H3(String text)
    {
	this(null,null,text) ;
    }

    public H3(ElementInterface... elements)
    {
	super("h3",null,null,elements) ;
    }

}
