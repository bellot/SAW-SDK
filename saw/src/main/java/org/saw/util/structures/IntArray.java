package org.saw.util.structures;

import java.io.* ;
import java.util.* ;

/** This is a extensible array of integers (<code>int</code>).
 *  @author Copyright Patrick Bellot, 2008 and later.
 *  @version 1.0
 */

public class IntArray
    implements Serializable
{
    public static final long serialVersionUID = 20101023L ;

    /** Current size of the internal array. */
    private int size ;

    /** Index after the last element used. */
    private int last ;

    /** @return index after the last element added. */
    public final int getLast() { return last ; }

    /** Internal table size increment. */
    private final int sizeIncrement ;

    /** Internal table. */
    private int[] table ;

    /** Constructor. 
     *  @param initialSize the initial size of the internal table
     *  @param sizeIncrement the internal table size increment when extension is neede
     */

    public IntArray(int initialSize, int sizeIncrement)
    {
	this.size          = initialSize ;
	this.last          = 0 ;
	this.sizeIncrement = sizeIncrement ;
	this.table         = new int[initialSize] ;
    }

    /** Assumes that <code>idx &lt; last</code>:<br>
     *  - If <code>idx</code> is greater that the internal table size, 
     *    an <code>IndexOutOfBoundsException</code> exception is raised.<br> 
     *  - Else if <code>idx &gt;= getLast()</code>, the result is unpredictable.<br>
     *  @see    #getLast()
     *  @param  idx an index in the table
     *  @return element of index <code>idx</code>, assumes that <code>idx &lt; last</code>. 
     */

    public final int get(int idx)
    {
	return table[idx] ;
    }

    /** Adds new element at the end of the table, extends it if necessary. 
     *  @param val the value to be added at the end of the table
     */

    public final void add(int val)
    {
	if (last == size)
	    table = Arrays.copyOf(table,size += sizeIncrement) ;

	table[last++] = val ;
    }

    /** Tests if the table contains the value <code>val</code>. 
     *  @param val the value to be searched in the table
     */

    public final boolean contains(int val)
    {
	for (int value : table) {
	    if (value == val)
		return true ;
	}

	return false ;
    }

}
