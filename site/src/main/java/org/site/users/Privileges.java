package org.site.users ;

public final class Privileges
{
    public static final int ADMIN      = (1 <<  0) ;
    public static final int WEB_MASTER = (1 <<  1) ;


    public static final String getDescription(int privileges)
    {
        String result = null ;
        result = addDescription(result, privileges, ADMIN,      "Admin") ; 
        result = addDescription(result, privileges, WEB_MASTER, "Web master") ; 
        if (result == null)
            result = "No privileges" ;
        return result ;
    }

    private static final String addDescription(String result,int privileges,int mask,String description)
    {
        if ((privileges & mask) != 0) {
            if (result == null)
                return description ;
            return result + ", " + description ;
        }
        return result ;
    }
}

