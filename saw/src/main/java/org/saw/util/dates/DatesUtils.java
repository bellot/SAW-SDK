package org.saw.util.dates ;

import java.util.* ;

/** Dates utilites.
 *  @author &copy; Patrick Bellot, 2009 and later. 
 */

public class DatesUtils
{
    private final static String month[] = {
	"Jan.", "Feb.", "March", "Apr.", "May",  "June",
	"July", "Aug.", "Sept.", "Oct.", "Nov.", "Dec."
    } ;

    private final static String day[] = {
	"Sat.", "Sun.", "Mon.", "Tue.", "Wed.", "Thru.", "Fri.", "Sat.", "Sun."
    } ;


    /** Builds a readable date string from a J/M/AAAA string. Return the empty string in case of failure. */

    public static final String litteralDateFromJMAAAA(String jmaaaaDate)
    {
	return litteralDateFromJMAAAA(jmaaaaDate.split("/")) ;
    }

    /** Builds a readable date string from a J/M/AAAA array of strings. Return the empty string in case of failure. */

    public static final String litteralDateFromJMAAAA(String[] jmaaaaDate)
    {
	try {

	    return 
		month[Integer.parseInt(jmaaaaDate[1])-1] 
		+ " "  
		+ Integer.parseInt(jmaaaaDate[0]) 
		+ ", " 
		+ Integer.parseInt(jmaaaaDate[2]) ;

	} catch(Exception e) {
	    return "" ;
	}
    }

    /** Builds a readable from-to string from two J/M/AAAA strings. Return the empty string in case of failure. */

    public static final String litteralFromToJMAAAAs(String a, String b)
    {
	return litteralFromToJMAAAAs(a.split("/"),b.split("/")) ;
    }

    /** Builds a readable from-to string from two J/M/AAAA arrays of strings. Return the empty string in case of failure. */

    public static final String litteralFromToJMAAAAs(String[] a, String[] b)
    {
	try {
	    if (Integer.parseInt(a[2]) != Integer.parseInt(b[2])) // [2] is year
		return litteralDateFromJMAAAA(a) + " to " + litteralDateFromJMAAAA(b) ;
	    
	    int ma = Integer.parseInt(a[1]) ;
	    int mb = Integer.parseInt(b[1]) ;
	    
	    if (ma != mb)
		return month[ma-1] + " " + a[0] + " to " + month[mb] + " " + b[0] + ", " + a[2] ; // Jan. 28 to Feb. 03, 2010
	
	    return month[ma-1] + " " + a[0] + "-" + b[0] + ", " + a[2] ; // Nov. 01-04, 2010
	} catch (Exception e) {
	    return "" ;
	}
    }


    private final static Calendar calendar1 = new GregorianCalendar() ;
    private final static Calendar calendar2 = new GregorianCalendar() ;

    /** Builds a readable date string from a millis date. */

    public static final String litteralDateFromMillis(long timeMillis)
    {
	synchronized (calendar1) {

	    calendar1.setTimeInMillis(timeMillis) ;
	
	    return 
		month[calendar1.get(Calendar.MONTH)]
		+" " +calendar1.get(Calendar.DAY_OF_MONTH)
		+", "+calendar1.get(Calendar.YEAR) ;
	}
    }

    /** Builds a readable date and time string from a millis date. */

    public static final String fullLitteralDateFromMillis(long timeMillis)
    {
	synchronized (calendar1) {

	    calendar1.setTimeInMillis(timeMillis) ;
	
	    return 
		month[calendar1.get(Calendar.MONTH)]
		+" " +calendar1.get(Calendar.DAY_OF_MONTH)
		+", "+calendar1.get(Calendar.YEAR)
		+" - "+day[calendar1.get(Calendar.DAY_OF_WEEK)]
		+" "+calendar1.get(Calendar.HOUR_OF_DAY)
		+":"+calendar1.get(Calendar.MINUTE) ;
	}
    }

    /** Builds a J/M/AAAA string from a millis date. */

    public static final String jmaaaaDateFromMillis(long timeMillis)
    {
	synchronized (calendar1) {

	    calendar1.setTimeInMillis(timeMillis) ;
	
	    return 
		""  + calendar1.get(Calendar.DAY_OF_MONTH) +
		"/" + (calendar1.get(Calendar.MONTH) + 1) +
		"/" + calendar1.get(Calendar.YEAR) ;
	}
    }

    /** Returns a millis date from  a J/M/AAAA string. */

    public static final long millisFromJMAAAA(String jmaaaaDate)
    {
	return millisFromJMAAAA(jmaaaaDate.split("/")) ;
    }

    /** Returns a millis date from  a J/M/AAAA array of strings. */

    public static final long millisFromJMAAAA(String[] jmaaaaDate)
    {
	synchronized (calendar1) {

	    calendar1.set(Integer.parseInt(jmaaaaDate[2]),
			  Integer.parseInt(jmaaaaDate[1])-1,
			  Integer.parseInt(jmaaaaDate[0]),
			  0,0,0) ;

	    return calendar1.getTimeInMillis() ;
	}
    }

    /** Checks if we are after a J/M/AAAA string. */

    public static final boolean todayAfter(String jmaaaaDate)
    {
	return todayAfter(jmaaaaDate.split("/")) ;
    }

    /** Checks if we are after a J/M/AAAA array of strings. */

    public static final boolean todayAfter(String[] jmaaaaDate)
    {
	synchronized (calendar1) {

	    calendar1.setTimeInMillis(System.currentTimeMillis()) ;

	    calendar2.set(Integer.parseInt(jmaaaaDate[2]),Integer.parseInt(jmaaaaDate[1])-1,Integer.parseInt(jmaaaaDate[0]),23,59,59) ;

	    return calendar1.after(calendar2) ;
	}
    }

    /** Checks if we are before a J/M/AAAA string. */

    public static final boolean todayBefore(String jmaaaaDate)
    {
	return todayBefore(jmaaaaDate.split("/")) ;
    }

    /** Checks if we are before a J/M/AAAA array of strings. */

    public static final boolean todayBefore(String[] jmaaaaDate)
    {
	synchronized (calendar1) {

	    calendar1.setTimeInMillis(System.currentTimeMillis()) ;

	    calendar2.set(Integer.parseInt(jmaaaaDate[2]),Integer.parseInt(jmaaaaDate[1])-1,Integer.parseInt(jmaaaaDate[0]),0,0,0) ;

	    return calendar1.before(calendar2) ;
	}
    }

}
