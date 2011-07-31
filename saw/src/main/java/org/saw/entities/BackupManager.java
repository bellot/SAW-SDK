package org.saw.entities ;

import java.io.* ;

import org.saw.* ;
import org.saw.util.* ;
import org.saw.util.files.* ;
import org.saw.util.logs.* ;
import org.saw.util.structures.* ;

/** In its static part, this class can register Entity Binz and the method 
 *  <code>hibernateAll()</code> saves all these Entity Binz in the workspace.
 *  An object of this class is a thread that does a regular backup of the 
 *  registered Entity Binz.
 *  @author  Patrick Bellot, &copy; 2009 and later. 
 */

public final class BackupManager extends Thread
{
    private static final ObjArray<EntityBinz> entities = new ObjArray<EntityBinz>(128,128) ;

    public static final void registerEntity(EntityBinz entity)
    {
        entities.add(entity) ;
    }

    public static final void unregisterEntity(EntityBinz entity)
    {
        entities.remove(entity) ;
    }

    public static final void hibernateAll()
    {
	synchronized (entities) {
	    int count = entities.getLast() ;
	    for (int i = 0 ; i < count ; i++) {
		entities.get(i).hibernate() ;
	    }
	}
    }

    public final void run()
    {
	setPriority(Thread.MIN_PRIORITY) ;

	while (true) {

	    try { 
		sleep(Parameters.BACKUP_INTERVAL) ; 
		backup() ;
		System.gc() ;	
		try {
		    Logs.log(Logs.SERVER_ERROR,"Backup manager done.") ;
		} catch (Exception e2) {
		    e2.printStackTrace() ;
		}
	    } catch (Exception e) {
		try {
		    Logs.log(Logs.SERVER_ERROR,"Backup manager failed.",e) ;
		} catch (Exception e2) {
		    e.printStackTrace() ;
		}
	    }
	}
    }

    private final void backup()
	throws Exception
    {
	hibernateAll() ;
	DirCopy.cp("workspace","backups" + File.separatorChar + System.currentTimeMillis() + ".workspace") ;
	deleteOldFiles("backups") ;
    }

    private final static FilenameFilter binzFilter 
	= new FilenameFilter() { 
		public boolean accept(File dir,String fileName) 
		{
		    return fileName.endsWith(".workspace") ;
		} 
	    } ; 

    private final void deleteOldFiles(String dirName)
    {
	String binzFiles[] = new File(dirName).list(binzFilter); 

	int count = binzFiles.length ;

	if (count < 1024)
	    return ;

	java.util.Arrays.sort(binzFiles) ; // Older first : file names begin with the time stamp

	for (int i = 0 ; i < 128 ; i++) {
	    try {
		int fn = Global.randomExt.nextInt(3 * 256) ;
		DirRm.rm(dirName + File.separatorChar + binzFiles[fn]) ;
	    } catch (Exception e) {}
	}
    }

   
}
