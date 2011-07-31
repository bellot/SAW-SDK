package org.saw.elements ;

/** Class for a link to a CSS file inside the HEAD tag.
 *  @author &copy; Patrick Bellot, 2009 and later. 
 */

public class FaviconLink extends XmlElement 
{
    public FaviconLink(String mimeType, String url)
    {
	super("link"
	      ,null,
	      "rel='shortcut icon' type='" + mimeType + "' href='" + url + "'") ;
    }

    private static final long serialVersionUID = 20110623 ;
}
