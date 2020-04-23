package kg.cs.mobileapp.security;

import kg.cs.mobileapp.SpringApplicationContext;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864000000; // 10 days
    public static final long PASSWORD_RESET_EXPIRATION_TIME = 3600000; // 1 hour
    public static final String TOKEN_PREFIX = "Messager ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
//    public static final String TOKEN_SECRET = "hjkfl9si447j9g0u083nfl0"; -> now we reading this value
//    from application.property file by using below method

    public static String getTokenSecret(){

        // because the "SecurityConstants" class is not a Bean (No Annotations used during class declaration),
        // we need get access to the environment where we can get access to the "AppProerties" @Component class
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.getTokenSecret();
    }
}