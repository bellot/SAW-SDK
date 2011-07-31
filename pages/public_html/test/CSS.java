package test ;

import org.saw.sessions.* ;

public final class CSS extends CssMultiMemorySessionBinz
{
    public CSS()
        throws Exception
    {
        super(true,
              "public_html/test/position.css",
              "public_html/test/header.css",
              "public_html/test/login.css",
              "public_html/test/navigation.css",
              "public_html/test/announces.css",
              "public_html/test/main.css",
              "public_html/test/footer.css") ;
    }
}
