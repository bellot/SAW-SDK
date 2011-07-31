package org.saw.entities ;

import java.io.* ;
import org.saw.util.structures.* ;
import org.saw.util.updatable.* ;

/** This is the base class of all Entity Binz. It contains the functions for 
 *  loading and saving Entity Binz in file using serialization.
 *  <strong>
 *     Note that the inheriting class must have a constructor with no argument or no constructor.
 *  </strong>
 *  @author  Patrick Bellot, &copy; 2009 and later. 
 */

public abstract class EntityBinzWithUpdatableFriends extends EntityBinz
    implements Serializable
{
    static final long serialVersionUID = 20101023L ;

    private transient ObjArray<UpdatableFriend> updatableFriends = new ObjArray<UpdatableFriend>(8,8) ;

    private final void setUpdatableFriends(ObjArray<UpdatableFriend> updatableFriends)
    {
	this.updatableFriends = updatableFriends ;
    }

    public final void addUpdatableFriend(UpdatableFriend updatableFriend)
    {
	updatableFriends.add(updatableFriend) ;
    }

    public synchronized final void notifyUpdatableFriends()
    {
	int count = updatableFriends.getLast() ;
	for (int i = 0 ; i < count ; i++)
	    updatableFriends.get(i).requiresUpdate() ;
    }

    public final static EntityBinzWithUpdatableFriends wakeUp(Class<?> theClass)
    {
	EntityBinzWithUpdatableFriends entityBinz = (EntityBinzWithUpdatableFriends)(EntityBinz.wakeUp(theClass)) ;
	entityBinz.setUpdatableFriends(new ObjArray<UpdatableFriend>(8,8)) ;
	return entityBinz ;
    }

}
