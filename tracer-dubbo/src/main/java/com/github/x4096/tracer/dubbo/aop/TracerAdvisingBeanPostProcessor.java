package com.github.x4096.tracer.dubbo.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.AbstractAdvisingBeanPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: tracer
 * @DateTime: 2019-11-22 00:17
 * @Description:
 */
public class TracerAdvisingBeanPostProcessor extends AbstractAdvisingBeanPostProcessor implements BeanFactoryAware {


    private MethodInterceptor interceptor;

    public TracerAdvisingBeanPostProcessor(MethodInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        setBeforeExistingAdvisors(true);
        setExposeProxy(true);
        this.advisor = new TracerAnnotationClassAdvisor(this.interceptor);
    }

}
