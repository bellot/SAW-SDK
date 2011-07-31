package org.site.conference.conf ;

import java.io.* ;

import org.saw.entities.* ;
import org.saw.util.dates.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class ConfInfo extends EntityBinzWithUpdatableFriends
        implements Serializable
{
    public static final long serialVersionUID = 20110730L ;

    /** The unique entity of this class. */

    public static final ConfInfo entity  ;

    static {
	entity = (ConfInfo)wakeUp(ConfInfo.class) ;
        BackupManager.registerEntity(entity) ;
    }

    private String longName ;
    private String shortName ;
    private String place ;
    private String contactEmail ;

    public final void set(String longName,
                          String shortName,
                          String place,
                          String contactEmail)
    {
        this.longName     = longName ;
        this.shortName    = shortName ;
        this.place        = place ;
        this.contactEmail = contactEmail ;

	notifyUpdatableFriends() ;
    }

    public final String getLongName()     { return longName ; }
    public final String getShortName()    { return shortName ; }
    public final String getPlace()        { return place ; }
    public final String getContactEmail() { return contactEmail ; }
}
