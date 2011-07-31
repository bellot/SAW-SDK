package org.saw.dynamic ;

import org.saw.elements.* ;
import org.saw.transaction.* ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class JSGiveFocus extends DynamicElement
{
    private final byte[] jsGiveFocus ;

    public JSGiveFocus(String elementId)
    {
	jsGiveFocus
            = ("<script type='text/javascript' src='../javascript/addLoadEvent.js'></script>"
               +"<script type='text/javascript'>"
               +   "function sawGiveFocus()"
               +   "{"
               +      "document.getElementById(\"" + elementId + "\").focus();"
               +   "}"
               +   "addLoadEvent(sawGiveFocus);"
               + "</script>").getBytes() ;
    }

    public final void writeTo(TransactionOutput transactionOuput) 
	throws Exception
    {
	transactionOuput.write(jsGiveFocus) ;
    }

}
