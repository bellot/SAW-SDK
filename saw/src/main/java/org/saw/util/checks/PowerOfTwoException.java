package org.saw.util.checks ;

/** Exception raised when a value must be a power of 2 and it is not.
 *  @author  Patrick Bellot, &copy; 2008 and later.  
 */

public class PowerOfTwoException extends Exception
{
    public static final long serialVersionUID = 20101023L ;

    public PowerOfTwoException(String description)
    {
	super(description) ;
    }

}
