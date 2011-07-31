package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class H1 extends XmlElement
{

    public H1(String className, String attr, String text)
    {
	super("h1",className,attr,new CDATA(text)) ;
    }

    public H1(String className, String text)
    {
	this(className,null,text) ;
    }

    public H1(String text)
    {
	this(null,null,text) ;
    }

    public H1(ElementInterface... elements)
    {
	super("h1",null,null,elements) ;
    }

}
