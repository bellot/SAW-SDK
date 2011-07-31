package org.saw.util.checks ;

/** Exception raised when a value must be a j/m/aaaa date and it is not.
 *  @author  Patrick Bellot, &copy; 2008 and later.  
 */

public class JMAAAADateException extends Exception
{
    public static final long serialVersionUID = 20101023L ;

    public JMAAAADateException(String description)
    {
	super(description) ;
    }

}
