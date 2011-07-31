package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class Head extends XmlElement
{

    public Head(ElementInterface... elements)
    {
	super("head",null,null,elements) ;
        addInnerElements(new CDATA("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>")) ;
    }

}
