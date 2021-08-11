package com.fintech.insurance.components.cache;

import java.io.Serializable;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/13 14:43
 */
public class TestVO implements Serializable {

    private String name;

    private int age;

    public TestVO(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
