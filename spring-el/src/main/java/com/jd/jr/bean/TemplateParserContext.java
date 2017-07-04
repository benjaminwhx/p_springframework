package com.jd.jr.bean;

import org.springframework.expression.ParserContext;

/**
 * User: 吴海旭
 * Date: 2017-07-04
 * Time: 下午2:04
 */
public class TemplateParserContext implements ParserContext {

	public String getExpressionPrefix() {
		return "#{";
	}

	public String getExpressionSuffix() {
		return "}";
	}

	public boolean isTemplate() {
		return true;
	}
}
