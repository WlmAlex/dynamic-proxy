package com.gupao.proxy.custom;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Objects;

public class GPClassLoader extends ClassLoader {

    private File baseDir;

    public GPClassLoader() {
        String basePath = GPClassLoader.class.getResource("").getPath();
        baseDir = new File(basePath);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        String className = GPClassLoader.class.getPackage().getName() + "." + name;

        if (Objects.nonNull(baseDir)) {
            FileInputStream fis = null;
            File classFile = new File(baseDir, name.replaceAll("\\.", "\\") + ".class");
            if (classFile.exists()) {
                try {
                    fis = new FileInputStream(classFile);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buff = new byte[1024];
                    int len;
                    while ((len = fis.read(buff)) != -1) {
                        out.write(buff, 0, len);
                    }
                    fis.close();
                    //classFile.delete();
                    return defineClass(className, out.toByteArray(), 0, out.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
