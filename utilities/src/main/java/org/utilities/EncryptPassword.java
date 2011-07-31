package org.utilities ;

import java.io.* ;

import org.saw.util.encoding.* ;

public class EncryptPassword
{

    private static final void encryptPassword()
    {
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in)) ;

            System.out.println("SAW Password Encryption (MD5 + base64)") ;
            System.out.print("Enter password: ") ;
            System.out.flush() ;

            String password = in.readLine() ;

            String encrypted = PasswordEncrypt.encrypt(password) ;

            System.out.println("Encrypted password: " + encrypted) ;
            System.out.println("Bye !") ;

        } catch (Exception e) {

             System.out.println("Soemthing went wrong :") ;
             e.printStackTrace() ;
             System.out.println("Bye !") ;
        }
    }

    public static void main(String[] args)
    {
	encryptPassword() ;
    }

}
