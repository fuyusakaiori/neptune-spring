package com.org.simpleframework.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import com.org.simpleframework.beans.PropertyValue;
import com.org.simpleframework.beans.exception.BeansException;
import com.org.simpleframework.beans.factory.config.BeanDefinition;
import com.org.simpleframework.beans.factory.config.BeanReference;
import com.org.simpleframework.beans.factory.config.RuntimeBeanNameReference;
import com.org.simpleframework.beans.factory.support.BeanDefinitionRegistry;
import com.org.simpleframework.beans.factory.support.GenericBeanDefinition;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.List;

public class DefaultBeanDefinitionDocumentReader implements BeanDefinitionDocumentReader {

    /**
     * <h3>需要解析的属性</h3>
     */
    public static final String BEAN_ELEMENT = "bean";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String ID_ATTRIBUTE = "id";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String REF_ATTRIBUTE = "ref";
    public static final String INIT_METHOD_ATTRIBUTE = "init-method";
    public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
    public static final String SCOPE_ATTRIBUTE = "scope";
    public static final String BASE_PACKAGE_ATTRIBUTE = "base-package";
    public static final String COMPONENT_SCAN_ELEMENT = "component-scan";


    @Override
    public void registerBeanDefinition(Document document, BeanDefinitionRegistry beanDefinitionRegistry) {
        doRegisterBeanDefinitions(document.getRootElement(), beanDefinitionRegistry);
    }

    private void doRegisterBeanDefinitions(Element root, BeanDefinitionRegistry beanDefinitionRegistry) {

        Element componentScan = root.element(COMPONENT_SCAN_ELEMENT);
        // TODO 注解扫描暂时不支持

        List<Element> beanList = root.elements(BEAN_ELEMENT);
        for (Element bean : beanList) {
            String beanId = bean.attributeValue(ID_ATTRIBUTE);
            String beanName = bean.attributeValue(NAME_ATTRIBUTE);
            String className = bean.attributeValue(CLASS_ATTRIBUTE);
            String initMethodName = bean.attributeValue(INIT_METHOD_ATTRIBUTE);
            String destroyMethodName = bean.attributeValue(DESTROY_METHOD_ATTRIBUTE);
            String beanScope = bean.attributeValue(SCOPE_ATTRIBUTE);

            Class<?> clazz;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new BeansException("Cannot find class [" + className + "]");
            }
            //id优先于name
            beanName = StrUtil.isNotEmpty(beanId) ? beanId : beanName;
            if (StrUtil.isEmpty(beanName)) {
                //如果id和name都为空，将类名的第一个字母转为小写后作为bean的名称
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }

            BeanDefinition beanDefinition = new GenericBeanDefinition();
            // 注: 模仿注解直接在加载配置文件的时候就配置好 Class 对象
            beanDefinition.setBeanClass(clazz);
            beanDefinition.setInitMethodName(initMethodName);
            beanDefinition.setDestroyMethodName(destroyMethodName);
            if (StrUtil.isNotEmpty(beanScope)) {
                beanDefinition.setScope(beanScope);
            }

            List<Element> propertyList = bean.elements(PROPERTY_ELEMENT);
            for (Element property : propertyList) {
                String propertyNameAttribute = property.attributeValue(NAME_ATTRIBUTE);
                String propertyValueAttribute = property.attributeValue(VALUE_ATTRIBUTE);
                String propertyRefAttribute = property.attributeValue(REF_ATTRIBUTE);

                if (StrUtil.isEmpty(propertyNameAttribute)) {
                    throw new BeansException("The name attribute cannot be null or empty");
                }

                Object value = propertyValueAttribute;
                if (StrUtil.isNotEmpty(propertyRefAttribute)) {
                    value = new RuntimeBeanNameReference(propertyRefAttribute);
                }
                PropertyValue propertyValue = new PropertyValue(propertyNameAttribute, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
            if (beanDefinitionRegistry.containsBeanDefinition(beanName)) {
                //beanName不能重名
                throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
            }
            //注册BeanDefinition
            beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
        }

    }
}
