package org.saw.transaction ;

import java.net.* ;

import org.saw.server.* ;

/** A class loader tryig to load classes from the table of url given in argument
 *  or from the application classpath. This has been necessary since the
 *  we start to use Maven.
 *  @author  Patrick Bellot, &copy; 2011 and later. */

public class SawURLClassLoader extends URLClassLoader
{

	public SawURLClassLoader(URL[] urls)
	{
		super(urls) ;
	}
	
	public Class<?> loadClass(String name)
		throws ClassNotFoundException
	{
		try {
			return super.loadClass(name) ;
		} catch(Throwable e) {}
		
		return Main.class.getClassLoader().loadClass(name) ;
	}
	
}
