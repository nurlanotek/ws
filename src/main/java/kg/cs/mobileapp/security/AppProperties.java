package kg.cs.mobileapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

// a class that will read values from property file.
@Component
public class AppProperties {

    @Autowired
    private Environment environment;

    public String getTokenSecret(){
        return environment.getProperty("tokenSecret");
    }
}
