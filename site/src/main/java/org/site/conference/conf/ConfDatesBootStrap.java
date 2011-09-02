package org.site.conference.conf ;

import java.io.* ;

import org.saw.entities.* ;
import org.saw.util.dates.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class ConfDatesBootStrap
{
    public final static void bootStrap()
    {
        ConfDates.entity.set("01/07/2011",   /* submissionOpeningJma */
                             "01/12/2011",   /* submissionDeadlineJma */
                             "",             /* extSubmissionDeadlineJma */
                             "31/01/2012",   /* notificationToAuthorsJma */
                             "29/02/2012",   /* cameraReadyJma */
                             "01/03/2012",   /* registrationRequiredJma */
                             "01/07/2011",   /* registrationOpeningJma */
                             "01/03/2012",   /* lateRegistrationJma */
                             "01/04/2012",   /* conferenceFirstDayJma */
                             "02/04/2012") ; /* conferenceLastDayJma */
    }
}
