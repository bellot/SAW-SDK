package conference.server;

import org.saw.compilations.* ;
import org.saw.elements.* ;
import org.saw.sessions.* ;
import org.saw.transaction.* ;
import org.saw.users.* ;
import org.saw.util.logs.* ;

import org.site.conference.dates.* ;
import org.site.conference.pages.* ;
import org.site.conference.web.* ;

import org.site.users.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class FilterLogs extends WebTechPage
{
    public FilterLogs()
        throws Exception
    {
        super("../tools/datepicker/DatePicker.css",
              "../tools/datepicker/DatePicker.js",
              new Container(new H1("center","Logs filtering"),
                            new Form(null,"method='POST' action='ViewLogs.class'",
                                     new Table("center","style='margin-top:15px;margin-bottom:15px;'",
                                               new Tr(new Td("form_text",  new CDATA("From:")),
                                                      new Td("form_input", new DatePicker("logs_from"))),
                                               new Tr(new Td("form_text",  new CDATA("To:")),
                                                      new Td("form_input", new DatePicker("logs_to"))),
                                               new Tr(new Td("form_text",  new CheckBox("checkbox_input","server_access","X",true)),
                                                      new Td("form_input", new CDATA("Server access logs"))),
                                               new Tr(new Td("form_text",  new CheckBox("checkbox_input","server_log","X",true)),
                                                      new Td("form_input", new CDATA("Server logging logs"))),
                                               new Tr(new Td("form_text",  new CheckBox("checkbox_input","server_error","X",true)),
                                                      new Td("form_input", new CDATA("Server error logs"))),
                                               new Tr(new Td("form_text",  new CheckBox("checkbox_input","server_warning","X",true)),
                                                      new Td("form_input", new CDATA("Server warning logs"))),
                                               new Tr(new Td("form_text",  new CheckBox("checkbox_input","server_debug","X",true)),
                                                      new Td("form_input", new CDATA("Server debug logs"))),
                                               new Tr(new Td("form_text",  new CheckBox("checkbox_input","security_warning","X",true)),
                                                      new Td("form_input", new CDATA("Security warning logs"))),
                                               new Tr(new Td("form_text",  new CheckBox("checkbox_input","user_life","X",true)),
                                                      new Td("form_input", new CDATA("User life logs")))
                                               ),
                                     new P("center",
                                           new Submit("submit_input","filterLogs","Filterlogs","style='width:70px;'")),
                                     new JavaScript(new InputSetValue("logs_from", new JMAAAAOneWeekAgo()),
                                                    new InputSetValue("logs_to",   new JMAAAANow()))
                                     )
                            )
              ) ;
    }

    public void handle(Transaction transaction)
	throws Exception
    {
        User.checkPrivileges(transaction.getSessionEnvironment().getUser(),Privileges.ADMIN | Privileges.WEB_MASTER) ;
	super.handle(transaction) ;
    }

}

