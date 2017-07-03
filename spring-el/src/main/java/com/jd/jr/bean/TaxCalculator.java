package com.jd.jr.bean;

/**
 * User: 吴海旭
 * Date: 2017-07-03
 * Time: 下午7:42
 */
public class TaxCalculator {
	private String defaultLocale;

	public String getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(String defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	@Override
	public String toString() {
		return "TaxCalculator{" +
				"defaultLocale='" + defaultLocale + '\'' +
				'}';
	}
}
