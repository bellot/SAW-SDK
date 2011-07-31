package org.saw.elements ;

import org.saw.compilations.* ;
import org.saw.util.structures.* ;

/** The base class of all XML elements with a tag, aclass name, attributes and inner elements.
 *  @author  Patrick Bellot, &copy; 2010 and later. 
 */

public class XmlElement 
    implements ElementInterface
{

    private final String tag ;
    private final String className ;
    private final String attributes ;

    private ObjArray<ElementInterface> innerElements = new ObjArray<ElementInterface>(8,8) ;

    /** Constructor. */

    public XmlElement(String tag, String className, String attributes, ElementInterface... elements)
    {
	this.tag = tag ;
	this.className = className ;
	this.attributes = attributes ;

	addInnerElements(elements) ;
    }
    
    /** Self-compilation. */

    public final Compilation compile()
    {
	String header = "<" + tag ;

	if (className != null)
	    header += " class='" + className + "'" ;

	if (attributes != null)
	    header += " " + attributes ;

	header += ">" ;

	Compilation result = new Compilation() ;

	result.add(new BytesCompilation(header)) ;
	
	int last = innerElements.getLast() ;

	for (int i = 0 ; i < last ; i++)
	    result.append(innerElements.get(i).compile()) ;

	result.add(new BytesCompilation("</" + tag + ">")) ;

	result.compact() ;

	return result ;
    }

    /** Adds inner elements. */

    public final void addInnerElements(ElementInterface... elements)
    {
	for (ElementInterface element : elements)
	    innerElements.add(element) ;
    }

    /** Adds inner element. */

    public final void addInnerElement(ElementInterface element)
    {
        innerElements.add(element) ;
    }

}
