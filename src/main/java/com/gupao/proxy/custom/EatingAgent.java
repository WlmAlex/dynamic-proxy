package com.gupao.proxy.custom;

import java.lang.reflect.Method;

public class EatingAgent implements  GPInvocationHandler {

    private GPEatingMeat eatingMeat;

    public Object getInstance(GPEatingMeat eatingMeat) {
        this.eatingMeat = eatingMeat;
        Class<? extends GPEatingMeat> meatClass = eatingMeat.getClass();
        return GPProxy.newInstance(new GPClassLoader(), meatClass.getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("start eating~~~");
        method.invoke(eatingMeat, null);
        return null;
    }
}
