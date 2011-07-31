package org.site.bootstrap ;

import org.saw.users.* ;
import org.site.users.* ;

import org.site.conference.conf.* ;
import org.site.conference.web.* ;

/** This is the place where to put the bootstrap code for Entity Binz initializations.
 *  @author  Patrick Bellot, &copy; 2010 and later. 
 */

public class BootStrap 
{

    public final static void bootStrap()
        throws Exception
    {
        initRootUser() ;
        
        ConfDatesBootStrap .bootStrap() ;
        ConfInfoBootStrap  .bootStrap() ;
        PageTextsBootStrap .bootStrap() ;
    }

    private final static void initRootUser()
        throws Exception
    {
        UsersCatalog usersCatalog = UsersCatalog.entity ;

        User user = usersCatalog.createUser("bellot@telecom-paristech.fr","4Uorubt3NVVAeOw/pNL/OQ==") ;

        user.setPrivileges(Privileges.ADMIN) ;
    }

}
