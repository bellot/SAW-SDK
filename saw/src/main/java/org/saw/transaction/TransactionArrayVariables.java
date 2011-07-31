package org.saw.transaction ;

import org.saw.util.structures.* ;

/** This class is designed to store the POST variables which names end with "[]".
 *  Such variables appear many times and their values are stored in a 
 *  <code>StringArray</code>.
 *  @author  Patrick Bellot, &copy; 2008 and later.
 */

public class TransactionArrayVariables extends ObjObjHashTable<String,ObjArray<String>>
{
    static final long serialVersionUID = 20101023L ;

    /** Constructor. */

    public TransactionArrayVariables()
    {
	super(16) ;
    }

    /** Adds a value to the array of string values of a variable.
     */

   public final void add(String var, String val)
    {
	ObjArray<String> varList = get(var) ;

	if (varList == null) 
	    put(var,varList = new ObjArray<String>(32,32)) ;
	    
	varList.add(val) ;
    }

    /** Assumes that all the string values of the variable are
     *  strings representing integers. This fucntion returns
     *  a <code>IntArray</code> containing the values of the
     *  integers. 
     */

    public final IntArray getIntArray(String var)
    {
	ObjArray<String> strArray = get(var) ;

	if (strArray != null) {

	    int last = strArray.getLast() ;

	    IntArray intArray = new IntArray(last,1) ;

	    for (int i = 0 ; i < last ; i++)
		intArray.add(Integer.parseInt(strArray.get(i))) ;
	    
	    return intArray ;
	}

	return null ;
    }

}
