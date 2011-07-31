package org.saw.elements ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class Iframe extends XmlElement
{

    public Iframe(String className, String attr)
    {
	super("iframe",className,attr,new CDATA("")) ;
    }

}
