package org.saw.util.net ;

/** This class contains a static method to do SSL initialization, this procedure
 *  must be executed once before creating SSL sockets.
 *  @author  Patrick Bellot, &copy; 2008 and later.  
 */

public final class SSLInit
{

    /** Static method to be called once before SSL sockets creation. 
     *  @param key_store name of the key store file
     *  @param key_store_password password associated with the key store file
     */

    public final static void init(String key_store, String key_store_password)
    {
	System.setProperty("javax.net.ssl.trustStore",       key_store) ;
	System.setProperty("javax.net.ssl.keyStore",         key_store) ;
	System.setProperty("javax.net.ssl.keyStorePassword", key_store_password) ;
    }

}
