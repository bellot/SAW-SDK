package org.saw.elements ;

/** This is the class for the tag<br>
 *  <code>&lt;a style='...' target='...' href='...'&gt;...&lt;/a&gt;</code>
 *  @author  Patrick Bellot, &copy; 2010 and later.
 */

public class ALink extends XmlElement
{

    public ALink(String href, String text)
    {
	this(null,href,new CDATA(text)) ;
    }

    public ALink(String className, String href, String text)
    {
	this(className,href,new CDATA(text)) ;
    }

    public ALink(String className, String href, ElementInterface... elements)
    {
	super("a",
	      className,
	      "href='" + href + "'",
	      elements) ;
    }

    public ALink(String className, String target, String href, String text)
    {
	this(className,target,href,new CDATA(text)) ;
    }

    public ALink(String className, String target, String href, ElementInterface... elements)
    {
	super("a",
	      className,
	      "target='" + target + "' href='" + href + "'",
	      elements) ;
    }

}
