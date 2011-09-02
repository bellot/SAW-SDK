package org.saw.util.bytes;

/** A few C string functions applied to array of bytes.
 *  @author Copyright Patrick Bellot, 2009 and later.
 */

public final class BytesArray
{
    /** To hide the constructor in java doc. */
    private BytesArray() {}

    /** <code>streq(arr1,arr2) = (strcmp(arr1,arr2) == 0) ; man strcmp</code> */

    static public final boolean streq(byte[] arr1, byte[] arr2)
    {
	int len = arr1.length ;

        if (len != arr2.length)
            return false ;

        for (int i = 0 ; i < len ; i++)
            if (arr1[i] != arr2[i])
                return false ;

        return true ;
    }

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