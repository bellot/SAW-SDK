package org.saw.entities ;

import java.io.* ;
import org.saw.util.logs.* ;

/** This is the base class of all Entity Binz. It contains the functions for 
 *  loading and saving Entity Binz in file using serialization.
 *  <strong>
 *     Note that the inheriting class must have a constructor with no argument or no constructor.
 *  </strong>
 *  @author  Patrick Bellot, &copy; 2009 and later. 
 */

public abstract class EntityBinz
    implements Serializable
{
    static final long serialVersionUID = 20101023L ;

    /** Computes the workspace file name for saving. */

    private static final String getFileName(Class<?> theClass)
    {
        String pathName = "workspace" + File.separatorChar + (theClass.getName().replace('.',File.separatorChar)) ;

        (new File(pathName)).mkdirs() ;

        return pathName + File.separatorChar + "binz" ;
    }

    /** The workspace file name. */

    private String workspaceFileName ;

    /** Sets the file name. */

    public void setFileName(String workspaceFileName)
    {
        this.workspaceFileName = workspaceFileName ;
    }

    /** The constructor initializes the workspace file name. */

    public EntityBinz()
    {
        setFileName(getFileName(getClass())) ;
    } 

    /** Tries to load the Entity Binz, returns it, if load fails,
     *  then it tries to create a new instance of the class.
     *  returns <code>null</code> if creation fails.
     */

    public static EntityBinz wakeUp(Class<?> theClass)
    {
	ObjectInputStream in = null ;

	String fileName = getFileName(theClass) ;

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
	    } catch(Exception e2) {} ;
	}

	try {

	    EntityBinz unique = (EntityBinz)(theClass.newInstance()) ;

	    BackupManager.registerEntity(unique) ;

	    return unique ;

	} catch (Exception e) {
	    try { 
		Logs.log(Logs.SERVER_ERROR,"Cannot create entity binz \"" + fileName + "\"",e) ;
	    } catch(Exception e2) {} ;
	}

	return null ;
    }

    /** Saves the Entity Binz under serialized form. */

    public final synchronized void hibernate()
    { 
	ObjectOutputStream out = null ;

	try {
	    
	    // In Java doc, they do not use buffered stream
	    out = new ObjectOutputStream(new FileOutputStream(workspaceFileName)) ;
	    out.writeObject(this) ;
	    out.close() ;

	} catch (Exception e) {
	    try { out.close() ; } catch (Exception e2) {} ;
	    try { 
		Logs.log(Logs.SERVER_ERROR,"Cannot save entity binz \"" + workspaceFileName + "\"",e) ;
	    } catch(Exception e2) {} ;
	}
    }

}
