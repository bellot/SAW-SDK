package conference.login;

import org.saw.compilations.* ;
import org.saw.dynamic.* ;
import org.saw.elements.* ;
import org.saw.exceptions.* ;
import org.saw.sessions.* ;
import org.saw.transaction.* ;
import org.saw.users.* ;
import org.saw.util.encoding.* ;
import org.saw.util.logs.* ;

import org.site.conference.pages.* ;
import org.site.conference.web.* ;

/** @author  Patrick Bellot, &copy; 2010 and later. */

public class SSLLoginCheck extends WebTechPage
{
    public SSLLoginCheck()
        throws Exception
    {
        super(new Container(JSLoginReload.element,
                            new H1("Login failed"),
                            new P("center", 
                                  new CDATA("Your credentials have not been recognized.")),
                            new Form(null,"method='POST' action='javascript: history.go(-1);' style='margin:15px;'",
                                     new P("center",
                                           new Submit("submit_input","loginButton","Back","style='width:70px;'"))))) ;
    }

    private final byte[] HTTP_LOGIN_SUCCESS = (Transaction.HTTP_WEB_SITE  +"/conference/login/LoginSuccess.class").getBytes() ;

    public void handle(Transaction transaction)
        throws Exception
    {
        TransactionVariables transactionVariables = transaction.getTransactionVariables() ;

        String loginName = transactionVariables.get("loginName") ;
        String password  = transactionVariables.get("password") ;

        if (loginName == null || password == null) 
            throw new BadRequestException(Logs.SECURITY_WARNING_CAT,"Try to access login check without Transaction variables.") ;

        User user = UsersCatalog.entity.getByLoginName(loginName) ;

        if (user != null) {

            if (user.getEncryptedPassword().equals(PasswordEncrypt.encrypt(password))) {

                SessionEnvironment sessionEnvironment = transaction.getSessionEnvironment() ;
                sessionEnvironment.setUser(user) ;

                SessionVariables sessionVariables = sessionEnvironment.getSessionVariables() ;

                transaction.sendHttpRedirection(HTTP_LOGIN_SUCCESS) ;

                Logs.log(Logs.USER_LIFE_CAT, "Login.",
                         Logs.USER_ID_TAG,   Integer.toString(user.userId)) ;

                return ;

            } else {

                Logs.log(Logs.SECURITY_WARNING_CAT, "Wrong password.",
                         Logs.USER_ID_TAG,          Integer.toString(user.userId)) ;

            }

        } else {

            Logs.log(Logs.SECURITY_WARNING_CAT, "Wrong login name.",
                     Logs.EMAIL_TAG,            loginName) ;

        }

        super.handle(transaction) ;
    }

}

