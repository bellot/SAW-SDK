package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class Title extends XmlElement
{

    public Title(String title)
    {
	this(new CDATA(title)) ;
    }

    public Title(ElementInterface... elements)
    {
	super("title",null,null,elements) ;
    }

}
