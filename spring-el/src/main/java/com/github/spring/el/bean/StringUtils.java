package com.github.spring.el.bean;

/**
 * User: 吴海旭
 * Date: 2017-07-04
 * Time: 上午11:15
 */
public class StringUtils {

	public static String reverseString(String input) {
		StringBuilder backwards = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			backwards.append(input.charAt(input.length() - 1 - i));
		}
		return backwards.toString();
	}
}
