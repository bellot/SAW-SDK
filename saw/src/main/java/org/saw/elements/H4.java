package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class H4 extends XmlElement
{

    public H4(String className, String attr, String text)
    {
	super("h4",className,attr,new CDATA(text)) ;
    }

    public H4(String className, String text)
    {
	this(className,null,text) ;
    }

    public H4(String text)
    {
	this(null,null,text) ;
    }

}
