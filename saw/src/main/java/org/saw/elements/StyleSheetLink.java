package org.saw.elements ;

/** Class for a link to a CSS file inside the HEAD tag.
 *  @author &copy; Patrick Bellot, 2009 and later. 
 */

public class StyleSheetLink extends XmlElement 
{
    public StyleSheetLink(String url)
    {
	super("link"
	      ,null,
	      "rel='stylesheet' type='text/css' media='screen' href='" + url + "'") ;
    }

    private static final long serialVersionUID = 20040623 ;
}
