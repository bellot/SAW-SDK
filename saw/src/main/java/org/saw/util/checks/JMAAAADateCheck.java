package org.saw.util.checks ;

/** A function that checks that its argument is a J/M/AAAA date and
 *  raises an exception if not.
 *  @author  Patrick Bellot, &copy; 2008 and later.  
 */

public class JMAAAADateCheck
{
    /** Checks that a string is a j/m/aaaa date.<br>
     *  - j must be between 1 and 31<br>
     *  - m must be between 1 and 12<br>
     *  - aaaa must be between 1500 and 2999<br>
     *  @param strJMAAAA string to be checked
     *  @throws DateException is the string is not a j/m/aaaa date.
     */

    public static final void check(String strJMAAAA)
	throws JMAAAADateException
    {
	try {

	    String[] arrayJMAAAADate = strJMAAAA.split("/") ;

	    int day = Integer.parseInt(arrayJMAAAADate[0]) ;
	    if (day >= 1 && day <= 31) {

		int month = Integer.parseInt(arrayJMAAAADate[1]) ;
		if (month >= 1 && month <= 12) {

		    int year = Integer.parseInt(arrayJMAAAADate[1]) ;
		    if (year >= 1500 && year <= 2999)
			return ;
		}
	    }

	} catch (Exception e) {}

	throw new JMAAAADateException(strJMAAAA) ;
    }


}
