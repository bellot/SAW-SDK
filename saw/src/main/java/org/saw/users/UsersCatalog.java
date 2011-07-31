package org.saw.users ;

import java.io.* ;

import org.saw.* ;
import org.saw.entities.* ;
import org.saw.util.* ;
import org.saw.util.structures.* ;

/** This is the catalog of all users. Each user is described by the
 *  class <code>User</code> describing the minimal set of user information
 *  that we keep in memory.
 *  @author  Patrick Bellot, &copy; 2009 and later. 
 */

public final class UsersCatalog extends EntityBinz
    implements Serializable
{
    public static final long serialVersionUID = 20110704L ;

    /** The unique entity of this class. */

    public static final UsersCatalog entity  ;

    static {
	entity = (UsersCatalog)wakeUp(UsersCatalog.class) ;
        BackupManager.registerEntity(entity) ;
    }

    /** Hash table indexing by user email. */

    private final ObjObjHashTable<String,User> loginNameHashTable
	= new ObjObjHashTable<String,User>(2 * Parameters.MAX_USERS_COUNT) ;

    /** Hash table indexing by user id. */

    private final IntObjHashTable<User> idHashTable
	= new IntObjHashTable<User>(2 * Parameters.MAX_USERS_COUNT) ;

    /** Adding a user. */

    private final void put(User user)
    {
	loginNameHashTable .put (user.getLoginName(), user) ;
	idHashTable        .put (user.userId,         user) ;
    }
    
    /** Gets user by email. */

    public final User getByLoginName(String loginName)
    {
	return loginNameHashTable.get(loginName) ;
    }

    /** Gets user by id. */

    public final User getByUserId(int userId)
    {
	return idHashTable.get(userId) ;
    }

    /** Creates a user from login name and encrypted password
     *  and returns the <code>User</code>.
     *  @throws LoginNameInUseException if the login name is already in use
     *  @throws Exception if the system cannot allocate a user id
     */

    public final synchronized User createUser(String loginName, String encryptedPassword)
        throws LoginNameInUseException, Exception
    {
        if (getByLoginName(loginName) != null)
            throw new LoginNameInUseException() ;

        int userId = 0 ;
        int tries  = 800000 ;

        do {

            if (--tries <= 0)
                throw new Exception() ;

            userId = Global.randomExt.nextRandomId() ;

        } while (getByUserId(userId) != null) ;

        User user = new User(userId) ;

        user.setLoginName(loginName) ;
        user.setEncryptedPassword(encryptedPassword) ;
        
        put(user) ;
        return user ;
    }

    /** Modifies the login name of the user.
     *  @throws LoginNameInUseException if the login name is already in use
     */

    public final synchronized void setLoginName(User user, String loginName)
        throws LoginNameInUseException
    {
        if (getByLoginName(loginName) != null)
            throw new LoginNameInUseException() ;

        user.setLoginName(loginName) ;
    }

}
