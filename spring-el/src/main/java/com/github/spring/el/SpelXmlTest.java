package com.github.spring.el;

import com.github.spring.el.bean.NumberGuess;
import com.github.spring.el.bean.ShapeGuess;
import com.github.spring.el.bean.TaxCalculator;
import com.github.spring.el.bean.FieldValueTestBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: 吴海旭
 * Date: 2017-07-03
 * Time: 下午5:30
 */
public class SpelXmlTest {

	private ApplicationContext applicationContext;

	public SpelXmlTest() {
		applicationContext = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
	}

	private void testXmlConfig() {
		NumberGuess numberGuess = (NumberGuess) applicationContext.getBean("numberGuess");
		System.out.println(numberGuess.getRandomNumber());	// random number
		ShapeGuess shapeGuess = applicationContext.getBean("shapeGuess", ShapeGuess.class);
		System.out.println(shapeGuess.getInitialShapeSeed());	// random number
		TaxCalculator taxCalculator = applicationContext.getBean("taxCalculator", TaxCalculator.class);
		System.out.println(taxCalculator.getDefaultLocale());	// beijing
	}

	private void testAnnoConfig() {
		FieldValueTestBean fieldValueTestBean = applicationContext.getBean(FieldValueTestBean.class);
		System.out.println(fieldValueTestBean.getDefaultLocale());	// beijing
	}

	public static void main(String[] args) {
		SpelXmlTest test = new SpelXmlTest();
//		test.testXmlConfig();

		test.testAnnoConfig();
	}
}
