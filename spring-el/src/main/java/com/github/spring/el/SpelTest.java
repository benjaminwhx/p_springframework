package com.github.spring.el;

import com.github.spring.el.bean.Inventor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * User: 吴海旭
 * Date: 2017-07-02
 * Time: 下午5:38
 */
public class SpelTest {

    private void testStringEL() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello World'");
        String message = (String) exp.getValue();
        System.out.println(message);    // Hello World
    }

    private void testCallMethod() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello World'.concat('!')");
        String message = (String) exp.getValue();
        System.out.println(message);    // Hello World!
    }

    private void testCallProperty() {
        ExpressionParser parser = new SpelExpressionParser();
        // invokes 'getBytes()'
        Expression exp = parser.parseExpression("'Hello World'.bytes");
        byte[] bytes = (byte[]) exp.getValue();
        String message = new String(bytes);
        System.out.println(message);    // Hello World
    }

    private void testCallProperty2() {
        ExpressionParser parser = new SpelExpressionParser();
        // invokes 'getBytes().length'
        Expression exp = parser.parseExpression("'Hello World'.bytes.length");
        int length = (Integer) exp.getValue();
        System.out.println(length);    // 11
    }

    private void testCallConstructor() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("new String('hello world').toUpperCase()");
        String message = exp.getValue(String.class);
        System.out.println(message);    // HELLO WORLD
    }

    private void testEvaluationContext() {
        // Create and set a calendar
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, 7, 9);

        // The constructor arguments are name, birthday, and nationality.
        Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("name");

        EvaluationContext context = new StandardEvaluationContext(tesla);
        String name = (String) exp.getValue(context);
        System.out.println(name);   // Nikola Tesla
    }

    private void testEvaluationContext2() {
        // Create and set a calendar
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, 7, 9);

        // The constructor arguments are name, birthday, and nationality.
        Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("name");

        String name = (String) exp.getValue(tesla);
        System.out.println(name);   // Nikola Tesla

        Expression exp2 = parser.parseExpression("name == 'Nikola Tesla'");
        boolean equ = exp2.getValue(tesla, Boolean.class);
        System.out.println(equ);    // true
    }

	private void testEvaluationContext3() {
		Simple simple = new Simple();
		simple.booleanList.add(true);

		StandardEvaluationContext simpleContext = new StandardEvaluationContext(simple);

		ExpressionParser parser = new SpelExpressionParser();
		//
		parser.parseExpression("booleanList[0]").setValue(simpleContext, "false");
		Boolean b = simple.booleanList.get(0);
		System.out.println(b);	// false
	}

	private void testConfiguration1() {
		SpelParserConfiguration configuration = new SpelParserConfiguration(true, true);
		ExpressionParser parser = new SpelExpressionParser(configuration);
		// grow size
		Expression expression = parser.parseExpression("list[3]");
		Demo demo = new Demo();
		Object o = expression.getValue(demo);	// object not null
		System.out.println(o != null);
		System.out.println(demo.list.size());	// 4
	}

	/**
	 * change configuration mode
	 */
	private void testConfiguration2() {
		SpelParserConfiguration configuration = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, this.getClass().getClassLoader());
		ExpressionParser parser = new SpelExpressionParser(configuration);
		Inventor inventor = new Inventor("benjamin", new Date(), "china");
		Expression expression = parser.parseExpression("nationality");
		String nation = expression.getValue(inventor, String.class);
		System.out.println(nation);
	}

    public static void main(String[] args) {
        SpelTest test = new SpelTest();
//        test.testStringEL();
//        test.testCallMethod();
//        test.testCallProperty();
//        test.testCallProperty2();
//        test.testCallConstructor();
//        test.testEvaluationContext();
//        test.testEvaluationContext2();

		// 10.3.1 The EvaluationContext interface Type Conversion
//		test.testEvaluationContext3();
		// 10.3.2 Parser configuration
//		test.testConfiguration1();
		// 10.3.3 SpEL compilation Compiler configuration
		test.testConfiguration2();
    }

	class Simple {
		public List<Boolean> booleanList = new ArrayList<Boolean>();
	}

	class Demo {
		public List<String> list;
	}
}
