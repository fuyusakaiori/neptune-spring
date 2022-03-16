package com.org.simpleframework.core.io;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * <h3>注: 不仅提过获取资源的方法, 还有其他很多修改资源的方法</h3>
 * <h3>注: 我算是看出来了, 这三种其实都是加载文件, 就是用的 API 不同而已</h3>
 */
public class FileSystemResource implements Resource{

    private final String path;

    private final File file;

    /**
     * <h3>这里主要用来获取文件对应的输入流</h3>
     * <h3>注: JDK 7 之后提供的工具类</h3>
     */
    private final Path filePath;


    public FileSystemResource(String path) {
        this.path = path;
        this.file = new File(path);
        this.filePath = this.file.toPath();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        try {
            return Files.newInputStream(this.filePath);
        }
        catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

    public String getPath()
    {
        return path;
    }

    public File getFile()
    {
        return file;
    }

    public Path getFilePath()
    {
        return filePath;
    }
}
