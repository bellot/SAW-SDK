package org.saw.elements ;

import org.saw.compilations.* ;
import org.saw.util.structures.* ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class Container 
    implements ElementInterface
{

    private ObjArray<ElementInterface> innerElements = new ObjArray<ElementInterface>(8,8) ;

    public Container(ElementInterface... elements)
    {
	addInnerElements(elements) ;
    }
    
    public Compilation compile()
    {
	Compilation result = new Compilation() ;

	int last = innerElements.getLast() ;

	for (int i = 0 ; i < last ; i++)
	    result.append(innerElements.get(i).compile()) ;

	result.compact() ;

	return result ;
    }

    public final void addInnerElements(ElementInterface... elements)
    {
	for (ElementInterface element : elements)
	    innerElements.add(element) ;
    }

}
