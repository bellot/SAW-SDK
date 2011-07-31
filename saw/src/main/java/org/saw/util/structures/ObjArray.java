package org.saw.util.structures;

import java.io.* ;
import java.util.* ;

/** This is a extensible array of objects of a given type.
 *  @author Copyright Patrick Bellot, 2008 and later.
 */

public class ObjArray<ValueType>
    implements Serializable
{
    static final long serialVersionUID = 20101023L ;
    
    /** Current size of the internal array. */
    private int size ;

    /** Index after the last element used. */
    private int last ;

    /** Returns index after the last element added. */
    public final int getLast() { return last ; }

    /** Internal table size increment. */
    private final int sizeIncrement ;

    /** Internal table. */
    private Object[] table ;

    /** Constructor. 
     *  @param initialSize the initial size of the internal table
     *  @param sizeIncrement the internal table size increment when extension is neede
     */

    public ObjArray(int initialSize, int sizeIncrement)
    {
	this.size          = initialSize ;
	this.last          = 0 ;
	this.sizeIncrement = sizeIncrement ;
	this.table         = new Object[initialSize] ;
    }

    /** Adds new element at the end of the table, extends it if necessary. 
     *  @param val the value to be added at the end of the table
     */

    public final void add(ValueType val)
    {
	if (last == size)
	    table = Arrays.copyOf(table,size += sizeIncrement) ;

	table[last++] = val ;
    }

    /** Adds new element if not already present (tested with <code>==</code>)
     *  at the end of the table, extends internal table it if necessary. 
     *  @param val the value to be added at the end of the table
     */

    public final void addIfNotPresent(ValueType val)
    {
	for (Object value : table) {
	    if (value == val)
		return ;
	}

	if (last == size)
	    table = Arrays.copyOf(table,size += sizeIncrement) ;

	table[last++] = val ;
    }

    /** Assumes that <code>idx &lt; last</code>:<br>
     *  - If <code>idx</code> is greater that the internal table size, 
     *    an <code>IndexOutOfBoundsException</code> exception is raised.<br> 
     *  - Else if <code>idx &gt;= getLast()</code>, the result is unpredictable.<br>
     *  @see    #getLast()
     *  @param  idx an index in the table
     *  @return element of index <code>idx</code>, assumes that <code>idx &lt; last</code>. 
     */

    @SuppressWarnings({"unchecked"})
    public final ValueType get(int idx)
    {
	return (ValueType)table[idx] ;
    }

    /** Sets element of index <code>idx</code> to <code>val</code>, assumes that <code>idx &lt; last</code>. 
     *  - If <code>idx</code> is greater that the internal table size, 
     *    an <code>IndexOutOfBoundsException</code> exception is raised.<br> 
     *  - Else if <code>idx &gt;= getLast()</code>, the result is unpredictable.<br>
     *  @see    #getLast()
     *  @param idx an index in the table
     *  @param val value to be stored in the table
     */

    public final void set(int idx,ValueType val)
    {
	table[idx] = val ;
    }

    /** Removes element of index <code>idx</code> and downshift upper elements, 
     *  assumes that <code>idx &lt; last</code>. 
     *  - If <code>idx</code> is greater that the internal table size, 
     *    an <code>IndexOutOfBoundsException</code> exception is raised.<br> 
     *  - Else if <code>idx &lt;= getLast()</code>, the result is unpredictable.<br>
     *  @see   #getLast()
     *  @param idx an index in the table
     */

    public final void remove(int idx)
    {
	while (++idx < last)
	    table[idx - 1] = table[idx] ;
	table[--last] = null ;
    }	    

    /** Removes element <code>val</code> if presentand downshift upper elements.
     *  @param val an index in the table
     */

    public final void remove(ValueType val)
    {
        int idx = 0 ;
        int lst = getLast() ;

        while (idx < lst) {
            if (table[idx] == val) {
                while (++idx < last)
                    table[idx - 1] = table[idx] ;
                table[--last] = null ;
                return ;
            }
        }
    }	    

}


