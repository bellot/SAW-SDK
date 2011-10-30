package org.saw.util.encoding ;

import  java.security.* ;

/** To encrypt a string in a string using MD5 and then base64, cannot be decrypted.
 *  @author &copy; Patrick Bellot, 2009 and later.
 */

public final class PasswordEncrypt
{

    /** Encyption function. */

    public final static String encrypt(String text)
	throws Exception
    { 

	MessageDigest md = MessageDigest.getInstance("MD5") ;

	md.update(text.getBytes()) ;
	
	return Base64Coder.bytesToString(md.digest()) ;

    }
	
}