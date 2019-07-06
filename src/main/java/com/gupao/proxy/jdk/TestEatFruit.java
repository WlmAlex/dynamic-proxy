package com.gupao.proxy.jdk;

public class TestEatFruit {

    public static void main(String[] args) {

        EatFruit eatFruit = (EatFruit) new EatingAgent().getInstance(new EatPeach());
        System.out.println(eatFruit.getClass());
        eatFruit.eatPeach();
    }
}
