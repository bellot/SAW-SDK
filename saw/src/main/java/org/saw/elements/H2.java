package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class H2 extends XmlElement
{

    public H2(String className, String attr, String text)
    {
	super("h2",className,attr,new CDATA(text)) ;
    }

    public H2(String className, String text)
    {
	this(className,null,text) ;
    }

    public H2(String text)
    {
	this(null,null,text) ;
    }

    public H2(ElementInterface... elements)
    {
	super("h2",null,null,elements) ;
    }

}
