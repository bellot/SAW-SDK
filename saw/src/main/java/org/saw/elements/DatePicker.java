package org.saw.elements ;

import org.saw.util.dates.* ;

/** @author  Patrick Bellot, &copy; 2008 and later. */

public class DatePicker extends Container
{
    public DatePicker(String name)
    {
	super(new CDATA("<input type='text' " 
			+   "class='text_input' "
			+   "name='"     + name     + "' "
			+   "id='"       + name     + "' "
			+   "size='12' maxlength='12'/>"
			+   "&nbsp;&nbsp;"),
	      new CDATA("<input type='button' "
                        +   "class='button_input smaller80' "
                        +   "value='Cal' "
                        +   "onClick='" 
                        +       "javascript: "
                        +       "var cal = new CalendarPopup(\"" + name + "_div\");"
                        +       "cal.showNavigationDropdowns();"
                        +       "cal.select(document.getElementById(\"" + name + "\"),\"" + name + "\",\"d/M/yyyy\");"
                        +       "return false;"
                        +   "'/>"),
              new CDATA("<div "
                        +   "id='" + name + "_div' "
                        +   "style='position:absolute;visibility:hidden;background-color:white;'>"
                        + "</div>")) ;
    }

}
