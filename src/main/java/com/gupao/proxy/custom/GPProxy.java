package com.gupao.proxy.custom;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class GPProxy {

    private static final String newLine = "\r\n";

    public static Object newInstance(GPClassLoader classLoader,
                                     Class<?>[] interfaces,
                                     GPInvocationHandler h) {
        try {

            //1. 生成代理类, 存到磁盘里去
            String proxySrc = generateSrc("ProxyClass", interfaces, h);
            String filePath = GPProxy.class.getResource("").getPath();
            File file = new File(filePath, "ProxyClass.java");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(proxySrc);
            fileWriter.flush();
            fileWriter.close();

            //2. 编译源代码, 生成.class字节码文件
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
            Iterable<? extends JavaFileObject> iterable = manager.getJavaFileObjects(file);
            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, iterable);
            task.call();
            manager.close();

            //3. 返回被代理后的代理对象
            Class proxyClass = classLoader.findClass("ProxyClass");
            Class<?> aClass = h.getClass().getInterfaces()[0];

            Constructor proxyClassConstructor = proxyClass.getConstructor(aClass);
            /*file.delete();*/
            return proxyClassConstructor.newInstance(h);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String generateSrc(String className, Class<?>[] interfaces, GPInvocationHandler h) {
        StringBuffer src = new StringBuffer();
        src.append("package com.gupao.proxy.custom;" + newLine);
        src.append("import java.lang.reflect.Method;" + newLine);
        src.append("public class " + className + " implements " + interfaces[0].getName() + "{" + newLine);
        src.append("private " + h.getClass().getInterfaces()[0].getName() + " h;" + newLine);
        src.append("public " + className + "(" + h.getClass().getInterfaces()[0].getName() + " h){" + newLine);
        src.append("this.h = h;" + newLine);
        src.append("}" + newLine);
        for (Method m : interfaces[0].getMethods()) {
            src.append("public " + m.getReturnType() + " " + m.getName() + "() {" + newLine);
            src.append("try{");
            src.append("Method m = " + interfaces[0].getName() + ".class.getMethod(\"" + m.getName() + "\", new Class[]{});" + newLine);
            src.append("this.h.invoke(this,m,null);" + newLine);
            src.append("}catch(Throwable e) {" + newLine);
            src.append("e.printStackTrace();" + newLine);
            src.append("}" + newLine);
            src.append("}" + newLine);
        }
        src.append("}" + newLine);
        return src.toString();
    }
}
