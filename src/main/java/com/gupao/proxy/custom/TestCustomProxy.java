package com.gupao.proxy.custom;

public class TestCustomProxy {

    public static void main(String[] args) {
        GPEatingMeat instance = (GPEatingMeat) new EatingAgent().getInstance(new GPEatingPork());
        System.out.println(instance.getClass());
        instance.eatMeat();
    }
}
