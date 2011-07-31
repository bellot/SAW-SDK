package conference.login;

import org.saw.compilations.* ;
import org.saw.dynamic.* ;
import org.saw.elements.* ;
import org.saw.exceptions.* ;
import org.saw.sessions.* ;
import org.saw.transaction.* ;
import org.saw.users.* ;
import org.saw.util.encoding.* ;

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

    private final byte[] HTTP_LOGIN_SUCCESS  = (Transaction.HTTP_WEB_SITE  + "/conference/login/LoginSuccess.class").getBytes() ;
    private final byte[] HTTPS_LOGIN_SUCCESS = (Transaction.HTTPS_WEB_SITE + "/conference/login/LoginSuccess.class").getBytes() ;


    public void handle(Transaction transaction)
        throws Exception
    {
        TransactionVariables transactionVariables = transaction.getTransactionVariables() ;

        String loginName = transactionVariables.get("loginName") ;
        String password  = transactionVariables.get("password") ;

        if (loginName == null || password == null)
            throw new BadRequestException("Try to access login check without Transaction variables.") ;

        User user = UsersCatalog.entity.getByLoginName(loginName) ;

        if (user != null) {

            if (user.getEncryptedPassword().equals(PasswordEncrypt.encrypt(password))) {

                SessionEnvironment sessionEnvironment = transaction.getSessionEnvironment() ;
                sessionEnvironment.setUser(user) ;

                SessionVariables sessionVariables = sessionEnvironment.getSessionVariables() ;

                if (sessionVariables.isSet("isSSL")) {
                    sessionVariables.remove("isSSL") ;
                    transaction.sendHtmlRedirection(HTTPS_LOGIN_SUCCESS) ;
                } else {
                    transaction.sendHtmlRedirection(HTTP_LOGIN_SUCCESS) ;
                }

                // Log good login

                return ;

            } else {

                // Log wrong password

            }

        } else {

            // Log wrong user

        }

        super.handle(transaction) ;
    }

}

