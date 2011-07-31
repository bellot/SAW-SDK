package org.saw.plugins;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.* ;

/**
 * Goal which touches a timestamp file.
 *
 * @goal clean
 * 
 * @phase clean
 */
public class PagesClean
    extends AbstractMojo
{
    /**
     * @parameter basedir
     * @required
     */
    private String basedir ;    

    public void execute()
        throws MojoExecutionException
    {
        try {
            rmDir(new File(basedir + File.separatorChar + "target")) ;
        } catch (Exception e) {}

        try {
            rmDir(new File(basedir + File.separatorChar + "public_html" + File.separatorChar + "welcome" + File.separatorChar + "tutorials" + File.separatorChar + "javadoc")) ;
        } catch (Exception e) {}

        try {
            rmClasses(new File(basedir + File.separatorChar + "public_html")) ;
        } catch (Exception e) {}

        try {
            rmContent(new File(basedir + File.separatorChar + "cache")) ;
        } catch (Exception e) {}

    }
    
    public final void rmContent(File dirFile)
	throws IOException 
    {    
	if (dirFile.isDirectory()) {
	    for (String child : dirFile.list())
                rmDir(new File(dirFile,child)) ;
	}
    }
    
    public final void rmClasses(File dirFile)
	throws IOException 
    {    
	if (dirFile.isDirectory()) {
	    for (String child : dirFile.list()) {
                File childFile = new File(dirFile,child) ;
                if (child.endsWith(".class")) {
                    childFile.delete() ; 
                } else {
                    rmClasses(childFile) ;
                }
	    }
	}
    }
    
    public final void rmDir(File dirFile)
	throws IOException 
    {    
	if (dirFile.isDirectory()) {
	    for (String child : dirFile.list()) {
		rmDir(new File(dirFile,child)) ;
	    }
	}

	dirFile.delete() ;
    }

}
