package org.saw.util.bytes;

/** A few C string functions applied to array of bytes.
 *  @author Copyright Patrick Bellot, 2009 and later.
 */

public final class BytesArray
{
    /** To hide the constructor in java doc. */
    private BytesArray() {}

    /** <code>man stroi</code> */

    static public final int strtoi(byte[] arr, int beg, int end)
    {
	int val = arr[beg] - (byte)'0' ;

	for (int i = beg+1 ; i < end ; i++)
	    val = 10 * val + (arr[i] - (byte)'0') ;

	return val ;
    }

    /** <code>man strchr</code> */

    static public final int strchr(byte byt, byte[] arr, int beg, int end)
    {
	for (int i = beg ; i < end ; i++)
	    if (arr[i] == byt)
		return i ;
	return -1 ;
    }

    /** <code>man strstr</code> */

    static public final int strstr(byte[] byt, byte[] arr, int beg, int end)
    {
	int len = byt.length ;
	int lst = end - len ;
	int j ;

	for (int i = beg ; i < lst ; i++) {
	    if (arr[i] == byt[0]) {
		for (j = 1 ; j < len ; j++) {
		    if (arr[i + j] != byt[j])
			break ;
		}
		if (j == len)
		    return i ;
	    }
	}

	return -1 ;
    }

}