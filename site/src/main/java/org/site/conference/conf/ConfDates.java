package org.site.conference.conf ;

import java.io.* ;

import org.saw.entities.* ;
import org.saw.util.dates.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class ConfDates extends EntityBinzWithUpdatableFriends
        implements Serializable
{
    public static final long serialVersionUID = 20110730L ;

    /** The unique entity of this class. */

    public static final ConfDates entity  ;

    static {
	entity = (ConfDates)wakeUp(ConfDates.class) ;
        BackupManager.registerEntity(entity) ;
    }

    /* Submission opening date */

    private String submissionOpeningJma,     submissionOpeningText ;
    private String submissionDeadlineJma,    submissionDeadlineText ;
    private String extSubmissionDeadlineJma, extSubmissionDeadlineText ;
    private String notificationToAuthorsJma, notificationToAuthorsText ;
    private String cameraReadyJma,           cameraReadyText ;
    private String registrationRequiredJma,  registrationRequiredText ;  
    private String registrationOpeningJma,   registrationOpeningText ;
    private String lateRegistrationJma,      lateRegistrationText ;
    private String conferenceFirstDayJma ;
    private String conferenceLastDayJma ;
    private String conferenceFromToText ;

    public final void set(String submissionOpeningJma,
                          String submissionDeadlineJma,
                          String extSubmissionDeadlineJma,
                          String notificationToAuthorsJma,
                          String cameraReadyJma,
                          String registrationRequiredJma,
                          String registrationOpeningJma,
                          String lateRegistrationJma,
                          String conferenceFirstDayJma,
                          String conferenceLastDayJma)
    {
	this.submissionOpeningJma      = submissionOpeningJma ;
	this.submissionOpeningText     = DatesUtils.litteralDateFromJMAAAA(submissionOpeningJma) ;
	this.submissionDeadlineJma     = submissionDeadlineJma ;
	this.submissionDeadlineText    = DatesUtils.litteralDateFromJMAAAA(submissionDeadlineJma) ;
        if (extSubmissionDeadlineJma.length() == 0) {
            this.extSubmissionDeadlineJma  = null ;
            this.extSubmissionDeadlineText = null ;
        } else {
            this.extSubmissionDeadlineJma  = extSubmissionDeadlineJma ;
            this.extSubmissionDeadlineText = DatesUtils.litteralDateFromJMAAAA(extSubmissionDeadlineJma) ;
        }
	this.notificationToAuthorsJma  = notificationToAuthorsJma ;
	this.notificationToAuthorsText = DatesUtils.litteralDateFromJMAAAA(notificationToAuthorsJma) ;
        this.cameraReadyJma            = cameraReadyJma ;
	this.cameraReadyText           = DatesUtils.litteralDateFromJMAAAA(cameraReadyJma) ;
	this.registrationRequiredJma   = registrationRequiredJma ;
	this.registrationRequiredText  = DatesUtils.litteralDateFromJMAAAA(registrationRequiredJma) ;
	this.registrationOpeningJma    = registrationOpeningJma ;
	this.registrationOpeningText   = DatesUtils.litteralDateFromJMAAAA(registrationOpeningJma) ;
	this.lateRegistrationJma       = lateRegistrationJma ;
	this.lateRegistrationText      = DatesUtils.litteralDateFromJMAAAA(lateRegistrationJma) ;
	this.conferenceFirstDayJma     = conferenceFirstDayJma ;
	this.conferenceLastDayJma      = conferenceLastDayJma ;
	this.conferenceFromToText      = DatesUtils.litteralFromToJMAAAAs(conferenceFirstDayJma,conferenceLastDayJma) ;

	notifyUpdatableFriends() ;
    }

    public final String getSubmissionOpeningJma()       { return submissionOpeningJma ; }
    public final String getSubmissionOpeningText()      { return submissionOpeningText ; }
    public final String getSubmissionDeadlineJma()      { return submissionDeadlineJma ; }
    public final String getSubmissionDeadlineText()     { return submissionDeadlineText ; }
    public final String getExtSubmissionDeadlineJma()   { return extSubmissionDeadlineJma ; }
    public final String getExtSubmissionDeadlineText()  { return extSubmissionDeadlineText ; }
    public final String getNotificationToAuthorsJma()   { return notificationToAuthorsJma ; }
    public final String getNotificationToAuthorsText()  { return notificationToAuthorsText ; }
    public final String getCameraReadyJma()             { return cameraReadyJma ; }
    public final String getCameraReadyText()            { return cameraReadyText ; }
    public final String getRegistrationRequiredJma()    { return registrationRequiredJma ; }
    public final String getRegistrationRequiredText()   { return registrationRequiredText ; }
    public final String getRegistrationOpeningJma()     { return registrationOpeningJma ; }
    public final String getRegistrationOpeningText()    { return registrationOpeningText ; }
    public final String getConferenceFromToText()       { return conferenceFromToText ; }

}
