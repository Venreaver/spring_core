package com.yet.spring.core.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.*;

public class AwareBean implements ApplicationContextAware, BeanNameAware, ApplicationEventPublisherAware {

    private ApplicationEventPublisher eventPublisher;
    private String name;
    private ApplicationContext ctx;

    public void init() {
        System.out.println(this.getClass().getSimpleName() + " > My name is '"
                + name + "'");
        if (ctx != null) {
            System.out.println(this.getClass().getSimpleName()
                    + " > My context is " + ctx.getClass().toString());
        } else {
            System.out.println(this.getClass().getSimpleName()
                    + " > Context is not set");
        }
        if (eventPublisher != null) {
            System.out.println(this.getClass().getSimpleName()
                    + " > My eventPublisher is "
                    + eventPublisher.getClass().toString());
        } else {
            System.out.println(this.getClass().getSimpleName()
                    + " > EventPublisher is not set");
        }
    }

    @Override
    public void setBeanName(String s) {
        name = s;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        eventPublisher = applicationEventPublisher;
    }
}
