package org.site.conference.web ;

import java.io.* ;

import org.saw.entities.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class PageTexts extends EntityBinzWithUpdatableFriends
        implements Serializable
{
    public static final long serialVersionUID = 20110730L ;

    /** The unique entity of this class. */

    public static final PageTexts entity  ;

    static {
	entity = (PageTexts)wakeUp(PageTexts.class) ;
        BackupManager.registerEntity(entity) ;
    }

    /* Internal data. */

    private String pageTitle   ;
    private String headerLine1 ;
    private String headerLine2 ;
    private String headerLine3 ;

    public final void set(String pageTitle,
                          String headerLine1,
                          String headerLine2,
                          String headerLine3) {
        this.pageTitle   = pageTitle ;
        this.headerLine1 = headerLine1 ;
        this.headerLine2 = headerLine2 ;
        this.headerLine3 = headerLine3 ;

        notifyUpdatableFriends() ;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public String getHeaderLine1() {
        return headerLine1;
    }

    public String getHeaderLine2() {
        return headerLine2;
    }

    public String getHeaderLine3() {
        return headerLine3;
    }

}
