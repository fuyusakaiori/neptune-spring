package com.org.simpleframework.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ClassPathResource implements Resource{

    private final String classpath;

    public ClassPathResource(String classpath) {
        this.classpath = classpath;
    }

    /**
     * <h3>1. 可以使用类加载器对资源进行获取, 也可以直接使用 Class 对象获取资源文件</h3>
     * <h3>2. 两者的获取方式在于: </h3>
     * <h3>2.1 前者默认就是从根目录开始, 路径不需要加 "/"</h3>
     * <h3>2.2 后者需要自己传入路径, 可以使用绝对路径或者相对路径</h3>
     */
    @Override
    public InputStream getInputStream() throws IOException {
        // 注: 这里的路径不能够写成绝对路径的形式, 因为采用的是类加载器加载的方式
        InputStream inputStream = this.getClass().getClassLoader()
                                          .getResourceAsStream(classpath);
        if (inputStream == null)
            throw new FileNotFoundException();
        return inputStream;
    }
}
