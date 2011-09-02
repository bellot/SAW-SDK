package conference.server;

import java.io.* ;

import org.saw.compilations.* ;
import org.saw.elements.* ;
import org.saw.sessions.* ;
import org.saw.transaction.* ;
import org.saw.users.* ;
import org.saw.util.bytes.* ;
import org.saw.util.dates.* ;
import org.saw.util.logs.* ;

import org.site.conference.dates.* ;
import org.site.conference.pages.* ;
import org.site.conference.web.* ;
import org.site.users.* ;

import org.site.users.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class ViewLogs extends WebTechPage
{
    private static class ViewLogsElement extends DynamicElement
    {
        public final void writeTo(TransactionOutput transactionOutput)
            throws Exception
        {
            TransactionVariables transactionVariables = transactionOutput.getTransactionVariables() ;

            long fromMillis = 0 ;
            try {
                fromMillis = DatesUtils.millisFromJMAAAA(transactionVariables.get("logs_from"),0,0,0) ;
            } catch (Exception e) {} ;

            long toMillis = Long.MAX_VALUE ;
            try {
                toMillis = DatesUtils.millisFromJMAAAA(transactionVariables.get("logs_to"),23,59,59) ;
            } catch (Exception e) {} ;
            
            boolean server_debug     = (transactionVariables.get("server_debug") != null) ;
            boolean server_warning   = (transactionVariables.get("server_warning") != null) ;
            boolean server_error     = (transactionVariables.get("server_error") != null) ;
            boolean server_log       = (transactionVariables.get("server_log") != null) ;
            boolean server_access    = (transactionVariables.get("server_access") != null) ;
            boolean security_warning = (transactionVariables.get("security_warning") != null) ;
            boolean user_life        = (transactionVariables.get("user_life") != null) ;

            String  session_id_filter = transactionVariables.get("session_id") ;
            if (session_id_filter.length() == 0)
                session_id_filter = null ;
            
            String  user_id_filter = transactionVariables.get("user_id") ;
            if (user_id_filter.length() == 0)
                user_id_filter = null ;
            
            BufferedReader log = null ;
            String         line ;
            byte[]         bline ;
            int            cat  ;
            long           time ;
            int            founds = 0 ;
            String         msg ;

            transactionOutput.write("<table style='border-collapse:collapse;'>") ;

            try {
                log = new BufferedReader(new FileReader("SAW.log")) ;

                while (true) {
                    do { line = log.readLine() ; } while (line != null && ! BytesArray.streq(line.getBytes(),Logs.TAG_LOGB)) ;
                    if (line == null) break ;

                    line = log.readLine() ; if (line == null) break ;
                    cat = Integer.parseInt(line) ;

                    boolean ok = true ;

                    switch (cat) {
                    case  0: ok = server_debug ; break ;
                    case  1: ok = server_warning ; break ;
                    case  2: ok = server_error ; break ;
                    case  3: ok = server_log ; break ;
                    case  4: ok = server_access ; break ;
                    case  5: ok = security_warning ; break ;
                    case  6: ok = user_life ; break ;
                    }

                    if (ok) {

                        line = log.readLine() ; if (line == null) break ;
                        time = Long.parseLong(line) ;
                
                        if (fromMillis <= time && time <= toMillis) {

                            msg = log.readLine() ; if (msg == null) break ;

                            ok = true ;

                            String any = null ;
                            String ip = null ;
                            String user_id_str = null ;
                            String request_path = null ;
                            String file = null ;
                            String email = null ;
                            String session_id_str = null ;
                            String unknown = null ;
                            String exception = null ;

                            while (true) { 
                                line  = log.readLine() ; if (line == null) { ok = false ; break ; }
                                bline = (line + "\n").getBytes() ;

                                if (BytesArray.streq(bline,Logs.LOGE))
                                    break ;

                                if (BytesArray.streq(bline,Logs.ANY_TAG)) {
                                    any = log.readLine() ; if (any == null) { ok = false ; break ; }
                                } else if (BytesArray.streq(bline,Logs.IP_TAG)) {
                                    ip = log.readLine() ; if (ip == null) { ok = false ; break ; }
                                } else if (BytesArray.streq(bline,Logs.USER_ID_TAG)) {
                                    user_id_str = log.readLine() ; if (user_id_str == null) { ok = false ; break ; }
                                } else if (BytesArray.streq(bline,Logs.REQUEST_PATH_TAG)) {
                                    request_path = log.readLine() ; if (request_path == null) { ok = false ; break ; }
                                } else if (BytesArray.streq(bline,Logs.FILE_TAG)) {
                                    file = log.readLine() ; if (file == null) { ok = false ; break ; }
                                } else if (BytesArray.streq(bline,Logs.EMAIL_TAG)) {
                                    email = log.readLine() ; if (email == null) { ok = false ; break ; }
                                } else if (BytesArray.streq(bline,Logs.SESSION_ID_TAG)) {
                                    session_id_str = log.readLine() ; if (session_id_str == null) { ok = false ; break ; }
                                } else if (BytesArray.streq(bline,Logs.EXCB)) {
                                    exception = "<span class='red'>" ;
                                    while (true) {
                                        String str = log.readLine() ; if (str == null) { ok = false ; break ; }
                                        str += "\n" ;
                                        if (BytesArray.streq(str.getBytes(),Logs.EXCE))
                                            break ;
                                        exception += str + "<br>" ;
                                    }
                                    exception += "</span>" ;
                                    if (!ok)
                                        break ;
                                } else {
                                    unknown = log.readLine() ; if (unknown == null) { ok = false ; break ; }
                                }
                            }

                            if (!ok)
                                break ;

                            if (session_id_filter != null) {
                                if (session_id_str == null || !session_id_str.equals(session_id_filter))
                                    ok = false ;
                            }

                            if (user_id_filter != null) {
                                if (user_id_str == null || !user_id_str.equals(user_id_filter))
                                    ok = false ;
                            }

                            if (ok) {
                                founds++ ;

                                String date = DatesUtils.fullLitteralDateFromMillis(time) ;

                                transactionOutput.write("<tr>") ;
                                transactionOutput.write(  "<td colspan='2' class='logshdr logs" + cat + "'>") ;
                                transactionOutput.write(    date) ;
                                transactionOutput.write(    " : ") ;
                                transactionOutput.write(    msg) ;
                                transactionOutput.write(  "</td>") ;
                                transactionOutput.write("</tr>") ;

                                if (ip != null)
                                    writeParameters(transactionOutput,"IP:&nbsp;",ip) ;
                                if (request_path != null)
                                    writeParameters(transactionOutput,"Path:&nbsp;",request_path) ;

                                if (user_id_str != null) {
                                    User user = UsersCatalog.entity.getByUserId(Integer.parseInt(user_id_str)) ;
                                    if (user != null)
                                        user_id_str += " - " + user.getLoginName() + " - " + Privileges.getDescription(user.getPrivileges()) ;
                                    writeParameters(transactionOutput,"User:&nbsp;",user_id_str) ;
                                }

                                if (email != null)
                                    writeParameters(transactionOutput,"Email:&nbsp;",email) ;
                                if (file != null)
                                    writeParameters(transactionOutput,"File:&nbsp;",file) ;
                                if (session_id_str != null)
                                    writeParameters(transactionOutput,"SId:&nbsp;",session_id_str) ;
                                if (any != null)
                                    writeParameters(transactionOutput,"Par:&nbsp;",any) ;
                                if (unknown != null)
                                    writeParameters(transactionOutput,"?:&nbsp;",unknown) ;
                                if (exception != null)
                                    writeParameters(transactionOutput,"Error:&nbsp;",exception) ;
                            }

                        } else {
                            do { line = log.readLine() ; } while (line != null && ! BytesArray.streq(line.getBytes(),Logs.TAG_LOGE)) ;
                            if (line == null) break ;
                        }
                    } else {
                        do { line = log.readLine() ; } while (line != null && ! BytesArray.streq(line.getBytes(),Logs.TAG_LOGE)) ;
                        if (line == null) break ;
                    }
                } // end while (true)

            } catch (Exception e) {
                // traiter l'erreur
                System.out.println("Error: " + e.getMessage()) ;

            } 

            transactionOutput.write("</table>") ;

            transactionOutput.write("<p class='center'>" + founds + " entries found</p>") ;

            if (log != null) try { log.close() ; } catch (Exception e2) {} ;

     
                    
        }

        private final void writeParameters(TransactionOutput transactionOutput, String name, String value)
            throws Exception
        {
            transactionOutput.write("<tr>") ;
            transactionOutput.write(  "<td>") ;
            transactionOutput.write(    name) ;
            transactionOutput.write(  "</td>") ;
            transactionOutput.write(  "<td>") ;
            transactionOutput.write(    value) ;
            transactionOutput.write(  "</td>") ;
            transactionOutput.write("</tr>") ;
        }
    }

    public ViewLogs()
        throws Exception
    {
        super("ViewLogs.css",
              new Container(new H1("center","Filtered logs"),
                            new Form(null,"method='GET' style='margin-top:15px;margin-bottom:15px;' action='javascript: history.go(-1)'",
                                     new P("center",
                                           new Submit("submit_input","backtop","Back","style='width:70px;'"))),
                            new ViewLogsElement(),
                            new Form(null,"method='GET' style='margin-top:15px;margin-bottom:15px;' action='javascript: history.go(-1)'",
                                     new P("center",
                                           new Submit("submit_input","backbottom","Back","style='width:70px;'"))))) ;
    }

    public void handle(Transaction transaction)
	throws Exception
    {
        User user = transaction.getSessionEnvironment().getUser() ;
        User.checkPrivileges(user,Privileges.ADMIN | Privileges.WEB_MASTER) ;
        
        super.handle(transaction) ;

        Logs.log(Logs.USER_LIFE_CAT, "Logs access",
                 Logs.USER_ID_TAG,   Integer.toString(user.userId)) ;
    }

}

