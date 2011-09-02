package org.saw.transaction ;

import java.util.regex.* ;

import org.apache.commons.lang.* ;

import org.saw.util.dates.* ;
import org.saw.util.structures.* ;

/** This class is designed to contain the GET and POST variables excepted
 *  those which name ends with "[]". These variables are stored in a
 *  <code>TransactionArrayVariables</code>.
 *  <br>
 *  This is a hash table which keys and values are strings. It inherits
 *  the methods of the <code>ObjObjHashTable&lt;String,String&gt;</code>.
 *  <br>
 *  The <code>Transaction</code> object fills this structure.
 *  <br>
 *  @see org.saw.util.structures.ObjObjHashTable
 *  @author  Patrick Bellot, &copy; 2008 and later.
 */

public final class TransactionVariables extends ObjObjHashTable<String,String>
{
    static final long serialVersionUID = 20101023L ;

    /** Constructor. */

    public TransactionVariables()
    {
	super(32) ;  // Must be a power of 2
    }

    /** Stores either <code>"true"</code> or <code>"false"</code> depending on <code>val</code>.
     *  @param var name of a variable
     *  @param val a boolean
     */ 

    public final void putBoolean(String var, boolean val)
    {
	put(var,(val)?"true":"false") ;
    }

    /** Returns true if the value associated to the variable <code>var</code>
     *  is <code>true</code>, return <code>false</code> otherwise..
     *  @param var name of a variable
     */

    public final boolean getBoolean(String var)
    {
	return  get(var).equals("true") ;
    }

    /** Stores the string value of an <code>int</code>.
     *  @param var name of a variable
     *  @param val a <code>int</code> value
     */

    public final void putInt(String var, int val)
    {
	put(var,Integer.toString(val)) ;
    }

    /** Assumes that the value of <code>var</code> is a decimal integer string,
     *  returns the <code>int</code> value of the string
     *  @param var name of a variable
     */

    public final int getInt(String var)
    {
	return Integer.parseInt(get(var)) ;
    }

    /** Returns the stripped value of a variable.
     *  @param var name of a variable
     *  @see StringUtils
     */

    public final String getStrippedString(String var)
    {
	String val = get(var) ;
	if (val != null)
	    return StringUtils.strip(val) ;
	return null ;
    }

    /** Assumes that the variable value is j/m/aa date,
     *  returns the millis time corresponding to the jj/mm/aa date.
     *  @param var name of a variable
     *  @see org.saw.util.dates.DatesUtils
     */

    public final long getMillisFromJMAAAA(String var) 
    {
	return DatesUtils.millisFromJMAAAA(get(var),0,0,0) ;
    }

    /** Assumes that the variable value is a reg exp,
     *  returns the compiled <code>Pattern</code> with case insensitive mode, 
     *  <code>null</code> if the value is <code>null</code> or <code>".*"</code>.
     *  @param var name of a variable
     *  @see java.util.regex.Pattern
     */

    public final Pattern getPattern(String var)
    {
	String regexp = StringUtils.strip(get(var)) ;

	if (regexp == null || regexp.equals(".*"))
	    return null ;

	return Pattern.compile(regexp,Pattern.CASE_INSENSITIVE) ;
    }

}
