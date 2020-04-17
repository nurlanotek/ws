package kg.cs.mobileapp;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContext implements ApplicationContextAware {
    private static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringApplicationContext.CONTEXT = context;
    }

    public static Object getBean(String beanName){
        return CONTEXT.getBean(beanName);
    }
}
