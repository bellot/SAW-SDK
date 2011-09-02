package conference.admin;

import org.saw.compilations.* ;
import org.saw.dynamic.* ;
import org.saw.elements.* ;
import org.saw.exceptions.* ;
import org.saw.sessions.* ;
import org.saw.transaction.* ;
import org.saw.users.* ;
import org.saw.util.logs.* ;

import org.site.conference.pages.* ;
import org.site.conference.web.* ;
import org.site.users.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class Console extends WebTechPage
{
    private final static byte[] web_master_table
        = ("<table class='console'>"
           + "<tr><th class='console'>WebMaster</th></tr>"
           + "<tr><td class='console'><a href='../server/FilterLogs.class' target='_top'>View logs</a></td></tr>"
           + "</table>").getBytes() ;

    private static class ConsoleElement extends DynamicElement
    {
        public final void writeTo(TransactionOutput transactionOutput)
            throws Exception
        {
            User user = transactionOutput.getSessionEnvironment().getUser() ;

            if (user.hasPrivileges(Privileges.ADMIN | Privileges.WEB_MASTER))
                transactionOutput.write(web_master_table) ;
        }
    }

    public Console()
        throws Exception
    {
        super("Console.css",
              new Container(new H1("Console"),
                            new ConsoleElement())) ;
    }

    public void handle(Transaction transaction)
	throws Exception
    {
        if (transaction.getSessionEnvironment().getUser() == null)
            throw new BadRequestException(Logs.SECURITY_WARNING_CAT, "Attempt to access console while not logged") ;

	super.handle(transaction) ;
    }

}

