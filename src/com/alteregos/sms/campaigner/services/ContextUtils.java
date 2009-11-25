package com.alteregos.sms.campaigner.services;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author john.emmanuel
 * @version
 * @since
 *
 */
public class ContextUtils {

    private static ApplicationContext context = null;

    static {
        context = new ClassPathXmlApplicationContext("classpath:campaignerContext-core.xml");
    }

    public ContextUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) context.getBean(name);
    }
}
