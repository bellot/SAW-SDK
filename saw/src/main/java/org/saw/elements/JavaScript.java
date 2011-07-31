package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class JavaScript extends XmlElement
{

    public JavaScript(String url)
    {
	super("script",null,"type='text/javascript' src='" + url +"'",Empty.unique) ; // to get the closing tag
    }

    public JavaScript(ElementInterface... elements)
    {
	super("script",null,"type='text/javascript'",elements) ; 

	addInnerElements(Empty.unique) ;
    }

}
