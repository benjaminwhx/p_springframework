package com.jd.jr;

import com.jd.jr.bean.Inventor;
import com.jd.jr.bean.LRBean;
import com.jd.jr.bean.SomeCustomObject;
import com.sun.org.apache.xalan.internal.extensions.ExpressionContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.*;

/**
 * User: 吴海旭
 * Date: 2017-07-03
 * Time: 下午7:53
 */
public class LanguageReferenceTest {

	private ExpressionParser parser = new SpelExpressionParser();

	private void testLiteral() {
		// evals to "Hello World"
		String helloWorld = (String) parser.parseExpression("'Hello World'").getValue();

		double avogadrosNumber = (Double) parser.parseExpression("6.0221415E+23").getValue();

		// evals to 2147483647
		int maxValue = (Integer) parser.parseExpression("0x7FFFFFFF").getValue();

		boolean trueValue = (Boolean) parser.parseExpression("true").getValue();

		Object nullValue = parser.parseExpression("null").getValue();
	}

	/**
	 * 属性名称的第一个字母不区分大小写。 数组和列表的内容使用方括号表示法获得。
	 * 1、properties
	 * 2、array
	 * 3、list
	 * 4、map
	 */
	private void testParameterTypes() {
		LRBean lrBean = new LRBean();
		Properties userInfo = new Properties();
		userInfo.setProperty("name", "benjamin");
		userInfo.setProperty("age", "25");
		List<LRBean.City> cities = new ArrayList<LRBean.City>();
		cities.add(new LRBean.City("beijing"));
		cities.add(new LRBean.City("hangzhou"));
		LRBean.Job[] job = new LRBean.Job[2];
		job[0] = new LRBean.Job("java");
		job[1] = new LRBean.Job("php");
		Map<Integer, String> favourites = new HashMap<>();
		favourites.put(1, "ball");
		favourites.put(2, "car");

		lrBean.setUserInfo(userInfo);
		lrBean.setCities(cities);
		lrBean.setJob(job);
		lrBean.setFavourite(favourites);
		StandardEvaluationContext context = new StandardEvaluationContext(lrBean);

		// show property
		String name = parser.parseExpression("userInfo['name']").getValue(context, String.class);
		System.out.println(name);	// benjamin

		// show list
		String city = parser.parseExpression("cities[0].name").getValue(context, String.class);
		System.out.println(city);	// beijing

		// show array
		String jobName = parser.parseExpression("job[0].name").getValue(context, String.class);
		System.out.println(jobName);	// java

		// show map
		String favourite = parser.parseExpression("Favourite[2]").getValue(context, String.class);
		System.out.println(favourite);	// car
	}

	/**
	 * 内联列表
	 * List列表可以使用{}表示法直接在表达式中表示。
	 */
	private void testInlineList() {
		LRBean lrBean = new LRBean();
		StandardEvaluationContext context = new StandardEvaluationContext(lrBean);
		List numbers  = parser.parseExpression("{1, 2, 3, 4}").getValue(context, List.class);
		System.out.println(numbers);

		List listOfLists = parser.parseExpression("{{'a','b'},{'x','y'}}").getValue(List.class);
		System.out.println(listOfLists);
	}

	/**
	 * 内联映射
	 * 也可以使用{key：value}表示法直接在表达式中表示Map映射。
	 */
	private void testInlineMap() {
		StandardEvaluationContext context = new StandardEvaluationContext();
		Map inventorInfo   = parser.parseExpression("{name:'Nikola',dob:'10-July-1856'}").getValue(context, Map.class);
		System.out.println(inventorInfo );

		Map mapOfMaps  = parser.parseExpression("{name:{first:'Nikola',last:'Tesla'},dob:{day:10,month:'July',year:1856}}").getValue(Map.class);
		System.out.println(mapOfMaps);
	}

	private void testArrayConstruction() {
		StandardEvaluationContext context = new StandardEvaluationContext();
		int[] numbers1 = (int[]) parser.parseExpression("new int[4]").getValue(context);

		// Array with initializer
		int[] numbers2 = (int[]) parser.parseExpression("new int[]{1,2,3}").getValue(context);

		// Multi dimensional array
		int[][] numbers3 = (int[][]) parser.parseExpression("new int[4][5]").getValue(context);
	}

	/**
	 * 方法要是public的
	 * static方法调用要加上T()
	 */
	private void testMethod() {
		// string literal, evaluates to "bc"
		String c = parser.parseExpression("'abc'.substring(2, 3)").getValue(String.class);
		System.out.println(c);

		StandardEvaluationContext context = new StandardEvaluationContext(this);
		// evaluates to true
		boolean isMember = parser.parseExpression("isMember('Mihajlo Pupin')").getValue(
				context, Boolean.class);
		System.out.println(isMember);

		Double random = parser.parseExpression("T(Math).random()").getValue(double.class);
		System.out.println(random);
	}

	/**
	 * 关系运算符
	 * 关系运算符 小于，小于等于，大于，大于等于，等于，不等于，使用标准运算符符号表示。
	 */
	private void testOperate() {
		// evaluates to true
		boolean trueValue = parser.parseExpression("2 == 2").getValue(Boolean.class);

		// evaluates to false
		boolean falseValue = parser.parseExpression("2 < -5.0").getValue(Boolean.class);

		// evaluates to true
		boolean trueValue2 = parser.parseExpression("'black' < 'block'").getValue(Boolean.class);
	}

	/**
	 * instanceof 和 正则
	 * 请谨慎使用原始类型，因为它们会立即包装到包装器类型中，因此，如果预期的话，1 instanceof T(int)将计算为false，而1 instanceof T(Integer)的计算结果为true。
	 */
	private void testInstanceOfAndRegex() {
		// evaluates to false
		boolean falseValue = parser.parseExpression(
				"'xyz' instanceof T(Integer)").getValue(Boolean.class);
		System.out.println(falseValue);

		// evaluates to true
		boolean trueValue = parser.parseExpression(
				"1 instanceof T(Integer)").getValue(Boolean.class);
		System.out.println(trueValue);

		// evaluates to false
		boolean falseValue2 = parser.parseExpression(
				"1 instanceof T(int)").getValue(Boolean.class);
		System.out.println(falseValue2);

		// evaluates to true
		boolean trueValue2 = parser.parseExpression(
				"'5.00' matches '^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);
		System.out.println(trueValue2);

		//evaluates to false
		boolean falseValue3 = parser.parseExpression(
				"'5.0067' matches '^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);
		System.out.println(falseValue3);
	}

	private void testLogic() {
		// -- AND --
		// evaluates to false
		boolean falseValue = parser.parseExpression("true and false").getValue(Boolean.class);
		System.out.println(falseValue);

		StandardEvaluationContext context = new StandardEvaluationContext(this);
		// evaluates to true
		String expression = "isMember('Nikola Tesla') and isMember('Mihajlo Pupin')";
		boolean trueValue = parser.parseExpression(expression).getValue(context, Boolean.class);
		System.out.println(trueValue);

		// -- OR --
		// evaluates to true
		boolean trueValue2 = parser.parseExpression("true or false").getValue(Boolean.class);
		System.out.println(trueValue2);

		// evaluates to true
		String expression2 = "isMember('Nikola Tesla') or isMember('Albert Einstein')";
		boolean trueValue3 = parser.parseExpression(expression).getValue(context, Boolean.class);
		System.out.println(trueValue3);

		// -- NOT --
		// evaluates to false
		boolean falseValue2 = parser.parseExpression("!true").getValue(Boolean.class);
		System.out.println(falseValue2);

		// -- AND and NOT --
		String expression3 = "isMember('Nikola Tesla') and !isMember('Mihajlo Pupin')";
		boolean falseValue3 = parser.parseExpression(expression3).getValue(context, Boolean.class);
		System.out.println(falseValue3);
	}

	/**
	 * 赋值
	 * 通过使用赋值运算符来完成属性的设置。 这通常在调用setValue之前完成，但也可以在对getValue的调用中完成。
	 */
	private void testAssignment() {
		Inventor inventor = new Inventor();
		StandardEvaluationContext inventorContext = new StandardEvaluationContext(inventor);

		parser.parseExpression("Name").setValue(inventorContext, "Alexander Seovic2");
		System.out.println(inventor.getName());	// Alexander Seovic2

		// alternatively

		parser.parseExpression("Name = 'Alexandar Seovic'").getValue(inventorContext, String.class);
		System.out.println(inventor.getName());	// Alexandar Seovic
	}

	/**
	 * 类型运算符
	 * 特殊的T运算符可用于指定java.lang.Class（类型）的实例。 也可以使用此运算符调用静态方法。TheStandardEvaluationContext使用TypeLocator来查找类型，并且可以使用对java.lang包的理解来构建StandardTypeLocator（可以被替换）。 这意味着对java.lang中的类型的引用T（）不需要是完全限定的，但是所有其他类型引用必须是。
	 */
	private void testTypes() {
		Class dateClass = parser.parseExpression("T(java.util.Date)").getValue(Class.class);

		Class stringClass = parser.parseExpression("T(String)").getValue(Class.class);

		boolean trueValue = parser.parseExpression(
				"T(java.math.RoundingMode).CEILING < T(java.math.RoundingMode).FLOOR")
				.getValue(Boolean.class);
		System.out.println(trueValue);
	}

	/**
	 * 构造函数
	 * 可以使用新的运算符调用构造函数。 除了原始类型和字符串（可以使用int，float等）之外，所有标准类名称都应该被使用。
	 */
	private void testConstructor() {
		Inventor einstein = parser.parseExpression(
				"new com.jd.jr.bean.Inventor('Albert Einstein', new java.util.Date(), 'German')")
				.getValue(Inventor.class);
		System.out.println(einstein);
	}

	/**
	 * 变量
	 * 可以使用语法#variableName在表达式中引用变量。 使用StandardEvaluationContext上的setVariable方法设置变量
	 */
	private void testContextVariable() {
		Inventor tesla = new Inventor("Nikola Tesla", new Date(), "Serbian");
		StandardEvaluationContext context = new StandardEvaluationContext(tesla);
		context.setVariable("newName", "Mike Tesla");

		parser.parseExpression("Name = #newName").getValue(context);

		System.out.println(tesla.getName()); // "Mike Tesla"
	}

	/**
	 * #this 和 #root变量
	 * 变量#this始终被定义，并且引用当前的运算操作对象（针对被解析的那个非限定引用）。 变量#root始终被定义，并引用根上下文对象。 虽然#this可能因为表达式的组件被运算操作而变化，但是#root总是引用根。
	 */
	private void testThisAndRoot() {
		// create an array of integers
		List<Integer> primes = new ArrayList<Integer>();
		primes.addAll(Arrays.asList(2,3,5,7,11,13,17));

		// create parser and set variable 'primes' as the array of integers
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariable("primes",primes);

		// all prime numbers > 10 from the list (using selection ?[...])
		// evaluates to [11, 13, 17]
		List<Integer> primesGreaterThanTen = (List<Integer>) parser.parseExpression(
				"#primes.?[#this>10]").getValue(context);
		System.out.println(primesGreaterThanTen);

		SomeCustomObject someObject = new SomeCustomObject();
		StandardEvaluationContext context2 = new StandardEvaluationContext();
		context2.setRootObject(someObject);
		context2.setVariable("name", "kocko");
		String expression = "#root.stringLength(#name) == 5";
		Boolean compareLength = parser.parseExpression(expression).getValue(context2, Boolean.class);
		System.out.println(compareLength);
	}

	public static void main(String[] args) {
		LanguageReferenceTest test = new LanguageReferenceTest();
//		test.testLiteral();
//		test.testParameterTypes();
//		test.testInlineList();
//		test.testInlineMap();
//		test.testArrayConstruction();
//		test.testMethod();
//		test.testOperate();
//		test.testInstanceOfAndRegex();
//		test.testLogic();
//		test.testAssignment();
//		test.testTypes();
//		test.testConstructor();
//		test.testContextVariable();
		test.testThisAndRoot();
	}

	public boolean isMember(String name) {
		return true;
	}
}
