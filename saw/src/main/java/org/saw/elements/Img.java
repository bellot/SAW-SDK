package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class Img extends XmlElement
{

    public Img(String className, String attr)
    {
	super("img",className,attr) ;
	addInnerElements(Empty.unique) ;
    }

    public Img(String attr)
    {
	super("img",null,attr) ;
	addInnerElements(Empty.unique) ;
    }

}
