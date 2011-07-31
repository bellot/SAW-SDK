package org.saw.util.structures;

import java.util.* ;

import java.io.* ;
import java.lang.reflect.* ;

import org.saw.util.checks.* ;

/** This is a generic hash table where keys are object and are given.
 *  The structure is its own iterator (must be used with synchronization).
 *  To use the iterator:
<pre>
    ObjObjHashTable&lt;KeyType,ValueType&gt; hashTable = new ObjObjHashTable&lt;KeyType,ValueType&gt;(128) ;
    ...
    synchronized (hashTable) {
       hashTable.resetIteration() ;
       ObjObjHashTable&lt;KeyType,ValueType&gt;.KeyValueEntry entry ;
       while ((entry = hashTable.nextIteration()) != null) {
          KeyType   key = entry.getKey() ;
          ValueType val = entry.getValue() ;
          ...
       }
    }
</pre>
 *  @author Copyright Patrick Bellot, 2008 and later.
 */

public class ObjObjHashTable<KeyType,ValueType>
    implements Serializable
{
    static final long serialVersionUID = 20101023L ;

    /** Linked hash table entries.
     *  @author  Patrick Bellot, &copy; 2008 and later. 
     */

    public final class KeyValueEntry
	implements Serializable
    {
	static final long serialVersionUID = 20101023L ;

	private final KeyType   key ;
	private       ValueType value ;

	private KeyValueEntry next ;

	private KeyValueEntry(KeyType key, ValueType value, KeyValueEntry next)
	{
	    this.key = key ;
	    this.value = value ;
	    this.next   = next ;
	}

	/** Returns the key of the <code>IntObjHashTable</code> entry. */

	public final KeyType getKey() 
	{ 
	    return key ; 
	}

	/** Returns the value of the <code>ObjObjHashTable</code> entry. */

	public final ValueType getValue() 
	{ 
	    return value ; 
	}

	/** Sets the value asssociated to the key of the <code>ObjObjHashTable</code> entry. */

	private final void setValue(ValueType value) 
	{ 
	    this.value = value ;
	}

	private final KeyValueEntry getNext() { return next ; }

	private final void setNext(KeyValueEntry next) { this.next = next ; }

    }

    private final int innerSize ;
    private final int innerMask ;

    private final KeyValueEntry[] table ;

    /** Constructor, <b><code>innerSize</code> must be a power of 2</b>. 
     *  @param innerSize must be a power of 2
     */

    public ObjObjHashTable(int innerSize)
    {
	try {
	    PowerOfTwoCheck.check("ObjObjHashTable inner size",innerSize) ;
	} catch (PowerOfTwoException e) {
	    e.printStackTrace() ;
	    System.exit(-1) ;
	}

	this.innerSize = innerSize ;
	this.innerMask = innerSize - 1 ;
	this.table     = (KeyValueEntry[])Array.newInstance(new KeyValueEntry(null,null,null).getClass(),
									     innerSize) ;
    }

    /** Puts the pair key-value in the hash table, if a value was
     *  already associated with the key then it is replaced.
     *  @param key a key
     *  @param value a value
     */

    public final void put(KeyType key, ValueType value)
    {
	int index = (key.hashCode() & innerMask) ;

	KeyValueEntry oldEntry = table[index] ;
	KeyValueEntry entry    = oldEntry ;

	while (entry != null) {
	    if (entry.getKey().equals(key)) {
		entry.setValue(value) ;
		return ;
	    }
	    entry = entry.getNext() ;
	}

	table[index] = new KeyValueEntry(key,value,oldEntry) ;
    }

    /** Returns the value associated with the key or <code>null</code> if it does not exist. 
     *  @param key a key
     */

    public final ValueType get(KeyType key)
    {
	KeyValueEntry entry = table[key.hashCode() & innerMask] ;

	while (entry != null) {
	    if (entry.getKey().equals(key))
		return entry.getValue() ;
	    entry = entry.getNext() ;
	}

	return null ;
    }

    /** Returns true if the value associated with the key exists. 
     *  @param key a key
     */

    public final boolean isSet(KeyType key)
    {
	KeyValueEntry entry = table[key.hashCode() & innerMask] ;

	while (entry != null) {
	    if (entry.getKey().equals(key))
		return true ;
	    entry = entry.getNext() ;
	}

	return false ;
    }

    /** Removes the value associated with the key if any. 
     *  @param key a key
     */

    public final void remove(KeyType key)
    {
	int index = (key.hashCode() & innerMask) ;

	KeyValueEntry table_entry    = table[index] ;
	KeyValueEntry previous_entry = null ;

	do {
	    
	    if (table_entry == null)
		return ;

	    if (table_entry.getKey().equals(key))
		break ;

	    previous_entry = table_entry ;
	    table_entry    = table_entry.getNext() ;
	} while (true) ;

	if (previous_entry == null) {
	    table[index] = table_entry.getNext() ;
	} else {
	    previous_entry.setNext(table_entry.getNext()) ;
	}
    }

    /** Removes the entry from the hash table, 
     *  the entry has been computed by <code>nextIteration()</code>. 
     *  @param entry an entry got from the iterator
     */

    public final void remove(KeyValueEntry entry)
    {
	int index = (entry.getKey().hashCode() & innerMask) ;

	KeyValueEntry table_entry    = table[index] ;
	KeyValueEntry previous_entry = null ;

	while (table_entry != entry) {

	    if (table_entry == null)
		return ;

	    previous_entry = table_entry ;
	    table_entry    = table_entry.getNext() ;
	}

	if (previous_entry == null) {
	    table[index] = table_entry.getNext() ;
	} else {
	    previous_entry.setNext(entry.getNext()) ;
	}
    }

    /** Removes all entries in the hash table. */

    public final void removeAll()
    {
	Arrays.fill(table,null) ;
    }

    /** Iterator data. */
    private int iterationIndex ;

    /** Iterator data. */
    private KeyValueEntry iterationEntry ;

    /** Resets the iterator for a new ride. */

    public final void resetIteration()
    {
	iterationIndex = 0 ;
	iterationEntry = null ;
    }

    /** Returns next iteration value or <code>null</code> if finished. */

    public final KeyValueEntry nextIteration()
    {
	while (iterationEntry == null) {
	    if (iterationIndex < innerSize) {
		iterationEntry = table[iterationIndex++] ;
	    } else {
		return null ;
	    }
	}

	KeyValueEntry result = iterationEntry ;
	iterationEntry = iterationEntry.getNext() ;
	return result ;
    }

}
