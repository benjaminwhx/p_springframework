package com.github.spring.el.bean;

/**
 * User: 吴海旭
 * Date: 2017-07-03
 * Time: 下午11:02
 */
public class SomeCustomObject {

    public int stringLength(String input) {
        if (input == null) return 0;
        return input.length();
    }
}
