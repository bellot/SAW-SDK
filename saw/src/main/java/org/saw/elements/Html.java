package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class Html extends Container
{
        
    public Html(ElementInterface... elements)
    {
	super(new XmlElement("html",null,null,elements)) ;
    }

}
