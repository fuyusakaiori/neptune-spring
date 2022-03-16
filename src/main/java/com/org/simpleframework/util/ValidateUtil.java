package com.org.simpleframework.util;

import com.org.simpleframework.beans.exception.BeanDefinitionException;

/**
 * <h2>验证</h2>
 */
public class ValidateUtil {


    public static void isBeanNameEmpty(String beanName){
        if (beanName == null)
            throw new BeanDefinitionException("对象名称为空");
    }
}
