package com.org.simpleframework.core.covert.converter;

/**
 * <h2>类型转换器工厂</h2>
 */
public interface ConverterFactory<S, R> {

    /**
     * <h3>注: R 就是用来限制能够产生哪些类型转换器 </h3>
     */
    <T extends R> Converter<S, T> getConverter(Class<T> targetType);

}
