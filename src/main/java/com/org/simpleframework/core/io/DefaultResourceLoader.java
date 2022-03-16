package com.org.simpleframework.core.io;

import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * <h2>默认的资源实例加载器</h2>
 * <h3>1. {@code spring} 没有针对每种资源实例都设计相应的加载器</h3>
 * <h3>2. 基本统一使用如下的资源实例加载器, 所以这里的设计可能存在相应的问题</h3>
 * <h3>3. 这个问题实际上就是不使用工厂方法带来的问题, 如果资源实例的种类增加, 那么就会违反开闭原则</h3>
 * <h3>4. 可能 {@code spring} 本身觉得资源实例的种类不会再发生变化, 所以没有采用这种设计</h3>
 */
@Slf4j
public class DefaultResourceLoader implements ResourceLoader {

    /**
     * <h3>貌似里面没有使用到采用文件路径加载的方式</h3>
     * @param location XML 文件路径
     */
    @Override
    public Resource getResource(String location) {
        // 1. 如果以绝对路径开头, 就需要单独处理
        if (location.startsWith("/")){
            log.debug("暂时不支持以这种方式加载...");
            return null;
        }else if (location.startsWith(CLASSPATH_URL_PREFIX)){
            // 2. 如果以 classpath 的路径开头, 那么就按照 classpath 方式处理
            return new ClassPathResource(location);
        }else{
            // 3. 如果都不是, 那么就是采用 url 的方式加载
            try {
                return new UrlResource(new URL(location));
            }
            catch (MalformedURLException e) {
                // TODO spring 貌似不是这么处理的, 那种方式没太看明白
                return new FileSystemResource(location);
            }
        }
    }
}
