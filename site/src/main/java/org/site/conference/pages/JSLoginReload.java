package org.site.conference.pages ;

import org.saw.elements.* ;
import org.saw.transaction.* ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class JSLoginReload extends DynamicElement
{
    public static final JSLoginReload element = new JSLoginReload() ;

    private final byte[] jsLoginReload ;

    public JSLoginReload()
    {
	jsLoginReload
            = ("<script type='text/javascript' src='../javascript/addLoadEvent.js'></script>"
               +"<script type='text/javascript'>"
               +   "function sawLoginReload()"
               +   "{"
               +      "document.getElementById('loginlogout').contentDocument.location.reload(true);"
               +   "}"
               +   "addLoadEvent(sawLoginReload);"
               + "</script>").getBytes() ;
    }

    public final void writeTo(TransactionOutput transactionOuput) 
	throws Exception
    {
	transactionOuput.write(jsLoginReload) ;
    }

}
