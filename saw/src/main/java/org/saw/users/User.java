package org.saw.users ;

import java.io.* ;

import org.saw.exceptions.* ;
import org.saw.util.logs.* ;

/** @author  Patrick Bellot, &copy; 2009 and later. */

public final class User
    implements Serializable
{
    public static final long serialVersionUID = 20110620L ;

    public final int userId ;

    private String loginName ;
    private String encryptedPassword ;
    private int    privileges ;

    protected User(int userId)
    {
	this.userId = userId ;
    }

    public final String getLoginName()
    {
	return loginName ;
    }

    public final String getEncryptedPassword()
    {
	return encryptedPassword ;
    }

    public final int getPrivileges()
    {
        return privileges ;
    }

    /** This method cannot be used, it is <code>protected</code>: one 
     *  must use 
     *  <br>
     * <code>UsersCatalog.entity.setLoginName(User user, int userId)</code>
     */

    protected final void setLoginName(String loginName)
    {
	this.loginName = loginName ;
    }

    public final void setEncryptedPassword(String encryptedPassword)
    {
	this.encryptedPassword = encryptedPassword ;
    }

    public final void setPrivileges(int privileges)
    {
	this.privileges = privileges ;
    }

    /** Checking privileges: return <code>true</code> or <code>false</code>. */

    public final boolean hasPrivileges(int privileges)
    {
        return ((this.privileges & privileges) != 0) ;
    }

    /** Checking privileges: return or raises a <code>BadRequestException</code> exception. */

    private final void checkPrivileges(int privileges)
        throws Exception
    {
        if ((this.privileges & privileges) == 0)
            throw new BadRequestException(Logs.SECURITY_WARNING_CAT,"Not enough privileges") ;
    }

    /** Checking privileges: return or raises a <code>BadRequestException</code> exception. */

    public final static void checkPrivileges(User user, int privileges)
        throws Exception
    {
        if (user == null)
            throw new BadRequestException(Logs.SECURITY_WARNING_CAT, "Logged user required") ;
        
        user.checkPrivileges(privileges) ;
    }
   
}
