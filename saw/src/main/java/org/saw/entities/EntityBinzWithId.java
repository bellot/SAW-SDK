package org.saw.entities ;

import java.io.* ;
import org.saw.util.logs.* ;

/** This is the base class of all Entity Binz with an Id. It contains the functions for 
 *  loading and saving Entity Binz in file using serialization.
 *  <strong>
 *     Note that the inheriting class must have a constructor with no argument or no constructor.
 *  </strong>
 *  @author  Patrick Bellot, &copy; 2009 and later. 
 */

public abstract class EntityBinzWithId
    implements Serializable
{
    static final long serialVersionUID = 20101023L ;

    /** Returns the entity id. */

    public abstract int getId() ;

    /** Computes the workspace file name for saving. */

    private static final String getFileName(Class<?> theClass, int id)
    {
	String pathName = "workspace" + File.separatorChar + (theClass.getName().replace('.',File.separatorChar)) ;

	(new File(pathName)).mkdirs() ;

	return pathName + File.separatorChar + id + "binz" ;
    }

    /** The workspace file name. */

    private String workspaceFileName ;

    /** Sets the file name. */

    public void setFileName(String workspaceFileName)
    {
	this.workspaceFileName = workspaceFileName ;
    }

    /** The constructor initializes the workspace file name. */

    public EntityBinzWithId()
    {
	setFileName(getFileName(getClass(),getId())) ;
    } 

    /** Tries to load the Entity Binz, returns it, if load fails,
     *  then it tries to create a new instance of the class.
     *  returns <code>null</code> if creation fails.
     */

    public static EntityBinz wakeUp(Class<?> theClass, int id)
    {
	ObjectInputStream in = null ;

	String fileName = getFileName(theClass,id) ;

	try {

	    // In Java doc, they do not use buffered stream
	    in = new ObjectInputStream(new FileInputStream(fileName)) ;
	    EntityBinz unique = (EntityBinz)(theClass.cast(in.readObject())) ;
	    unique.setFileName(fileName) ;
	    in.close() ;

	    return unique ;

	} catch (Exception e) {
	    try { in.close() ; } catch(Exception e2) {} ;
	    try { 
		Logs.log(Logs.SERVER_ERROR,"Cannot load entity binz \"" + fileName + "\"",e) ;
	    } catch(Exception e2) {
		e2.printStackTrace() ;
	    } ;
	}

	try {
	    return (EntityBinz)(theClass.newInstance()) ;
	} catch (Exception e) {
	    try { 
		Logs.log(Logs.SERVER_ERROR,"Cannot create entity binz \"" + fileName + "\"",e) ;
	    } catch(Exception e2) {
		e2.printStackTrace() ;
	    } ;
	}

	return null ;
    }

    /** Saves the Entity Binz under serialized form. */

    public final synchronized void hibernate()
    { 
	ObjectOutputStream out      = null ;

	try {
	    
	    // In Java doc, they do not use buffered stream
	    out = new ObjectOutputStream(new FileOutputStream(workspaceFileName)) ;
	    out.writeObject(this) ;
	    out.close() ;

	} catch (Exception e) {
	    try { out.close() ; } catch (Exception e2) {} ;
	    try { 
		Logs.log(Logs.SERVER_ERROR,"Cannot save entity binz \"" + workspaceFileName + "\"",e) ;
	    } catch(Exception e2) {
		e2.printStackTrace() ;
	    } ;
	}
    }

}
