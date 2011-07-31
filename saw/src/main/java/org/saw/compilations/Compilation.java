package org.saw.compilations ;

import org.saw.transaction.* ;
import org.saw.util.structures.* ;

/** A Compilation is a sequence of atomic compilations.
 *  @author  Patrick Bellot, &copy; 2008 and later.
 */

public final class Compilation extends ObjArray<AtomicCompilation>
{
    public static final long serialVersionUID = 20101023L ;

    /** Constructor. */
    public Compilation(AtomicCompilation... atomicCompilations)
    {
	super(8,8) ;

	for (AtomicCompilation atomicCompilation : atomicCompilations)
	    add(atomicCompilation) ;
    }

    /** Asks each atomic compilation of the sequence to write its output. */
    public final void writeTo(TransactionOutput transactionOuput)
	throws Exception
    {
	int count = getLast() ;

	for (int i = 0 ; i < count ; i++)
	    get(i).writeTo(transactionOuput) ;
    }

    /** Appends the <code>other</code> sequence of atomic compilations to its sequence. */
    public final void append(Compilation other)
    {
	int last = other.getLast() ;

	for (int i = 0 ; i < last ; i++)
	    add(other.get(i)) ;
    }

    /** Compacts the sequence of atomic compilations by concatenating adjacent arrays of bytes. */
    public final void compact()
    {
	int count = getLast() ;

	if (count > 0) {

	    AtomicCompilation c1 ;
	    AtomicCompilation c2 = get(0) ;

	    boolean b1 ;
	    boolean b2 = c2.isBytesCompilation() ;

	    int i = 1 ;

	    while (i < count) {

		c1 = c2 ;
		b1 = b2 ;

		c2 = get(i) ;
		b2 = c2.isBytesCompilation() ;

		if (b1 && b2) {
		    c2 = c1.concat(c2) ;
		    set(i-1,c2) ;
		    remove(i) ;
		    count-- ;
		} else {
		    i++ ;
		}
	    }
	}
    }

}
