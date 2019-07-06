package com.gupao.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class EatingAgent implements InvocationHandler {

    private EatFruit eatFruit;

    public Object getInstance(EatFruit eatFruit){
        this.eatFruit = eatFruit;
        Class<? extends EatFruit> fruitClass = eatFruit.getClass();
        Class<?>[] interfaces = fruitClass.getInterfaces();
        ClassLoader classLoader = fruitClass.getClassLoader();
        return Proxy.newProxyInstance(classLoader, interfaces, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("buy fruit from supermarket");
        method.invoke(eatFruit, null);
        return null;
    }
}
