package com.org.simpleframework.beans.factory;

/**
 * <h3>负责容器分层</h3>
 * <h3>注: 创建原始对象的工厂可以有父工厂, 父工厂的设置方法是在其子接口中</h3>
 */
public interface HierarchicalBeanFactory extends BeanFactory {

    BeanFactory getParentBeanFactory();

    /**
     * <h3>本地工厂是否包含该原始对象</h3>
     */
    boolean containsLocalBean(String beanName);
}
