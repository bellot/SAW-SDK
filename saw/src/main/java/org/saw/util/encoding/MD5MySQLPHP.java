package org.saw.util.encoding ;

import  java.security.* ;
import  java.math.* ;

/** A MD5 function compatible with MySQL and PHP md5 functions.
 *  Returns a string.
 *  @author &copy; Patrick Bellot, 2009 and later.
 */

public final class MD5MySQLPHP
{
	public static String md5(String input) throws NoSuchAlgorithmException {
		String result = input;
		if(input != null) {
			MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
			md.update(input.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			result = hash.toString(16);
			while(result.length() < 32) {
				result = "0" + result;
			}
		}
		return result;
	}
	
}