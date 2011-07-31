package org.utilities ;

import java.io.* ;
import java.util.* ;

public class ListBackups
{
    private final static String month[] = {
	"Jan.", "Feb.", "Mar.", "Apr.", "May",  "June", "July", "Aug.", "Sep.", "Oct.", "Nov.", "Dec."
    } ;

    private final static String day[] = {
	"Sat.", "Sun.", "Mon.", "Tue.", "Wed.", "Thr.", "Fri.", "Sat.", "Sun."
    } ;

    private final static Calendar calendar = new GregorianCalendar() ;

    private static final void writeDateFromMillis(long timeMillis)
    {
	calendar.setTimeInMillis(timeMillis) ;
	
	System.out.print(month[calendar.get(Calendar.MONTH)]) ;
	System.out.print(" ") ;
	int mday = calendar.get(Calendar.DAY_OF_MONTH) ;
	System.out.print((mday < 10)?("0" + mday):("" + mday)) ;
	System.out.print(", ") ;
	System.out.print(calendar.get(Calendar.YEAR)) ;
	System.out.print(" - ") ;
	System.out.print(day[calendar.get(Calendar.DAY_OF_WEEK)]) ;
	System.out.print(" ") ;
	int hour = calendar.get(Calendar.HOUR_OF_DAY) ;
	System.out.print((hour < 10)?("0" + hour):("" + hour)) ;
	System.out.print(":") ;
	int mins = calendar.get(Calendar.MINUTE) ;
	System.out.print((mins < 10)?("0" + mins):("" + mins)) ;
	System.out.print(":") ;
	int secs = calendar.get(Calendar.SECOND) ;
	System.out.print((secs < 10)?("0" + secs):("" + secs)) ;
    }

    private final static FilenameFilter binzFilter 
	= new FilenameFilter() { 
		public boolean accept(File dir,String fileName) 
		{
		    return fileName.endsWith(".workspace") ;
		} 
	    } ; 

    private static final void listBackups()
    {
	String files[] = new File("backups").list(binzFilter); 
	int    count   = files.length ;

	if (count == 0) {
	    System.out.println("No backup found !") ;
	    return ;
	}

	java.util.Arrays.sort(files) ; // Older last : file names begin with the time stamp

	String fileName       = files[count-1] ;
	String timeHead       = fileName.substring(0,fileName.indexOf('.')) ;
	long   lastTimeMillis = Long.parseLong(timeHead) ;

	for (int i = 0 ; i < count ; i++) {

	    fileName   = files[i] ;
	    timeHead   = fileName.substring(0,fileName.indexOf('.')) ;

	    long   timeMillis = Long.parseLong(timeHead) ;

	    writeDateFromMillis(timeMillis) ;
	    System.out.print("     ") ;
	    System.out.print(fileName) ;

	    System.out.print("     ") ;
	    System.out.print(millisToTime(lastTimeMillis - timeMillis)) ;
	    System.out.print("      [") ;
	    System.out.print(i) ;
	    System.out.print("]") ;

	    System.out.println() ;
	}

	System.out.println("Total: " + count + " backups") ;
    }

    private static final String millisToTime(long millis)
    {
        long mil = millis ;
        long sec = mil / 1000 ; mil -= 1000 * sec ;
        long min = sec / 60   ; sec -= 60   * min ;
        long hou = min / 60   ; min -= 60   * hou ;
        long day = hou / 24   ; hou -= 24   * day ;
        long mon = day / 30   ; day -= 30   * mon ;
        long yea = mon / 12   ; mon -= 12   * yea ;

        String time = "" ;

        if (yea > 9) {
            time += yea + " yr " ;
        } else if (yea > 0) {
            time += " " + yea + " yr " ;
        } else {
            time += "      " ;
        }

        if (mon > 9) {
            time += mon + " mn " ;
        } else if (mon > 0) {
            time += " " + mon + " mo " ;
        } else {
            time += "      " ;
        }
            
        if (day > 9) {
            time += day + " dy " ;
        } else if (day > 0) {
            time += " " + day + " dy " ;
        } else {
            time += "      " ;
        }
            
        if (hou > 9) {
            time += hou + " hr " ;
        } else if (hou > 0) {
            time += " " + hou + " hr " ;
        } else {
            time += "      " ;
        }
            
        if (min > 9) {
            time += min + " min " ;
        } else if (min > 0) {
            time += " " + min + " min " ;
        } else {
            time += "        " ;
        }
            
        if (sec > 9) {
            time += sec + " sec " ;
        } else if (sec > 0) {
            time += " " + sec + " sec " ;
        } else {
            time += "       " ;
        }
            
        if (mil > 99) {
            time += mil + " ms " ;
        } else if (mil > 9) {
            time += " " + mil + " ms " ;
        } else if (mil > 0) {
            time += "  " + mil + " ms " ;
        } else {
            time += "       " ;
        }

        return time ;
    }

    public static void main(String[] args)
    {
	listBackups() ;
    }

}
