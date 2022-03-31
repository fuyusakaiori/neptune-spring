package com.org.simpleframework.aop.framework.autoproxy;


import com.org.simpleframework.aop.*;
import com.org.simpleframework.aop.aspectj.AspectJExpressionPointCutAdvisor;
import com.org.simpleframework.aop.framework.DefaultAopProxyFactory;
import com.org.simpleframework.beans.factory.BeanFactory;
import com.org.simpleframework.beans.factory.BeanFactoryAware;
import com.org.simpleframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <h2>负责获取所有低级切面</h2>
 */
public class DefaultAdvisorAutoProxyCreator implements BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;


    /**
     * <h3>负责获取所有的低级切面, 然后创建相应的代理类</h3>
     * @return 代理类
     */
    protected Object wrapIfNecessary(Object bean, String beanName){
        // TODO
        if(isInfrastructureClass(bean.getClass()))
            return bean;

        // 1. 获取容器进而获取所有的切面对象
        Collection<AspectJExpressionPointCutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointCutAdvisor.class).values();
        for (AspectJExpressionPointCutAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointCut().getClassFilter();
            if (classFilter.matches(bean.getClass())){
                TargetSource target = new TargetSource(bean);
                MethodMatcher methodMatcher = advisor.getPointCut().getMethodMatcher();
                Advice advice = advisor.getAdvice();
                AdvisedSupport advisorSupport = new AdvisedSupport();
                advisorSupport.setTargetSource(target);
                advisorSupport.setMethodMatcher(methodMatcher);
                if (advice instanceof MethodInterceptor)
                    advisorSupport.setMethodInterceptor((MethodInterceptor) advice);
                return new DefaultAopProxyFactory(advisorSupport).getProxy();
            }
        }
        return bean;
    }

    /**
     * <h3>代理的目标对象不可以是通知, 切点, 切面的实现类</h3>
     */
    private boolean isInfrastructureClass(Class<?> clazz) {
        return Advice.class.isAssignableFrom(clazz)
                       || Advisor.class.isAssignableFrom(clazz)
                       || PointCut.class.isAssignableFrom(clazz);

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)){
            throw new IllegalArgumentException();
        }
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }
}
