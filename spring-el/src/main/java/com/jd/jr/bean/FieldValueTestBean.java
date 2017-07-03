package com.jd.jr.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * User: 吴海旭
 * Date: 2017-07-03
 * Time: 下午7:34
 */
@Component
public class FieldValueTestBean {

	@Value("#{ cityProperties['user.region'] }")
	private String defaultLocale;

	public void setDefaultLocale(String defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	public String getDefaultLocale() {
		return this.defaultLocale;
	}

}
