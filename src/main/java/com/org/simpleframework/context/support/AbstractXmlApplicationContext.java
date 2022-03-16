package com.org.simpleframework.context.support;

import com.org.simpleframework.beans.factory.support.DefaultListableBeanFactory;
import com.org.simpleframework.beans.factory.xml.XmlBeanDefinitionReader;

public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {

    /**
     * <h3>创建 Reader 读取 XML 文件创建 BeanDefinition 实例</h3>
     * @param beanFactory 内部容器
     */
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        // 1. 创建 BeanDefinitionReader 读取 XML 配置文件
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory, this);
        // 2. 获取所有配置文件的路径
        String[] configLocations = getDefaultConfigLocations();
        // 3. 根据配置文件路径加载资源
        if (configLocations != null){
            beanDefinitionReader.loadBeanDefinitions(configLocations);
        }
    }

    protected abstract String[] getDefaultConfigLocations();


}
