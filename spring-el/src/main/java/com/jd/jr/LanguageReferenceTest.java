package com.jd.jr;

import com.jd.jr.bean.LRBean;
import com.sun.org.apache.xalan.internal.extensions.ExpressionContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * User: 吴海旭
 * Date: 2017-07-03
 * Time: 下午7:53
 */
public class LanguageReferenceTest {

	private void testLiteral() {
		ExpressionParser parser = new SpelExpressionParser();

		// evals to "Hello World"
		String helloWorld = (String) parser.parseExpression("'Hello World'").getValue();

		double avogadrosNumber = (Double) parser.parseExpression("6.0221415E+23").getValue();

		// evals to 2147483647
		int maxValue = (Integer) parser.parseExpression("0x7FFFFFFF").getValue();

		boolean trueValue = (Boolean) parser.parseExpression("true").getValue();

		Object nullValue = parser.parseExpression("null").getValue();
	}

	private void testPropertiesAndList() {
		ExpressionParser parser = new SpelExpressionParser();

		LRBean lrBean = new LRBean();
		Properties userInfo = new Properties();
		userInfo.setProperty("name", "benjamin");
		userInfo.setProperty("age", "25");
		List<LRBean.Job> jobs = new ArrayList<LRBean.Job>();
		jobs.add(new LRBean.Job("beijing"));
		jobs.add(new LRBean.Job("hangzhou"));
		lrBean.setUserInfo(userInfo);
		lrBean.setJob(jobs);
		StandardEvaluationContext context = new StandardEvaluationContext(lrBean);

		// show property
		String name = parser.parseExpression("userInfo['name']").getValue(context, String.class);
		System.out.println(name);	// benjamin

		// show list
		String job = parser.parseExpression("job[0].name").getValue(context, String.class);
		System.out.println(job);	// beijing
	}

	public static void main(String[] args) {
		LanguageReferenceTest test = new LanguageReferenceTest();
//		test.testLiteral();
		test.testPropertiesAndList();
	}
}
