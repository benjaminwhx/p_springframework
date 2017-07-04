# Spring表达式语言（SpEL）

## 1、介绍

The Spring Expression Language（简称SpEL）是一种强大的支持在运行时查询和操作对象图的表达式语言。语言语法类似于Unified EL，但提供了额外的功能，特别是方法调用和基本字符串模板功能。

尽管还有其他几种Java表达式语言，比如OGNL，MVEL和JBoss EL，但SpEL只是为了向Spring社区提供一种支持良好的表达式语言，你可以在所有使用Spring框架的产品中使用SpEL。 其语言特性是由使用Spring框架的项目的需求所驱动的，包括Eclipse中基础Spring工具套件中的代码完成支持功能的工具要求。 也就是说，SpEL基于一种抽象实现的技术API，允许在需要时集成其他表达式语言来实现。

虽然SpEL作为Spring产品组合中的表达式运算操作的基础，但它并不直接与Spring有关，可以独立使用。 为了自包含，本章中的很多例子都使用SpEL，就像它是一种独立的表达语言。 这就需要创建一些引导作用的基础实现类，如解析器。 大多数Spring用户将不需要处理这种基础实现类，并且只会将表达式字符串作为运算操作。 这个典型用途的一个例子是将Spel集成到创建XML或基于注释的bean定义中，如表达式支持定义bean的定义所示。

本章将介绍表达式语言的特点及其API及其语言语法。 在好几个地方，使用Inventor和Inventor's Society类作为表达式运算操作的目标对象。 这些类声明和用于填充它们的数据在本章末尾列出。

---

## 2、功能概述

表达式语言支持以下功能

* Literal expressions(文字表达)
* Boolean and relational operators(布尔和关系运算)
* Regular expressions(正则表达式)
* Class expressions(类表达式)
* Accessing(访问) properties, arrays, lists, maps
* Method invocation(方法调用)
* Relational operators(关系运算)
* Assignment(分配)
* Calling constructors(调用构造函数)
* Bean references(Bean引用)
* Array construction(数组构造)
* Inline lists(内联集合)
* Inline maps(内联映射)
* Ternary operator(三元操作)
* Variables(变量)
* User defined functions(用户定义的功能)
* Collection projection(集合投影)
* Collection selection(集合选择)
* Templated expressions(模板表达式)

---

## 3、使用Spring表达式接口的表达式运算操作

本节介绍了SpEL接口及其表达式语言的简单使用。完整的语言参考可以在“语言参考”一节中找到。

以下代码介绍了SpEL API来运算操作文字字符串表达式“Hello World”。

```
ExpressionParser parser = new SpelExpressionParser();
Expression exp = parser.parseExpression("'Hello World'");
String message = (String) exp.getValue();
```

变量message的值只是 'Hello World'.

您最有可能使用的SpEL类和接口位于包 `org.springframework.expression` 及其子包和包`spel.support`接口`ExpressionParser`负责解析表达式字符串。 在此示例中，表达式字符串是由周围的单引号表示的字符串文字。接口`Expression`负责运算操作先前定义的表达式字符串。 当分别调用`parser.parseExpression`和`exp.getValue`时，有两个可以抛出的异常，`ParseException`和`EvaluationException`。

SpEL支持各种功能，如调用方法，访问属性和调用构造函数。

作为方法调用的一个例子，我们在字符串文字中调用concat方法。

```
ExpressionParser parser = new SpelExpressionParser();
Expression exp = parser.parseExpression("'Hello World'.concat('!')");
String message = (String) exp.getValue();
```

变量message的值现在是 'Hello World!'.

作为调用JavaBean属性的示例，可以调用String属性Bytes，如下所示。

```
ExpressionParser parser = new SpelExpressionParser();

// invokes 'getBytes()'
Expression exp = parser.parseExpression("'Hello World'.bytes");
byte[] bytes = (byte[]) exp.getValue();
```

Spel还支持嵌套属性，使用标准点符号，即prop1.prop2.prop3链式写法和属性值的设置

Public fields may also be accessed.

```
ExpressionParser parser = new SpelExpressionParser();

// invokes 'getBytes().length'
Expression exp = parser.parseExpression("'Hello World'.bytes.length");
int length = (Integer) exp.getValue();
```

可以调用String的构造函数，而不是使用字符串文字。

```
ExpressionParser parser = new SpelExpressionParser();
Expression exp = parser.parseExpression("new String('hello world').toUpperCase()");
String message = exp.getValue(String.class);
```

注意使用通用方法`public（T）T getValue（Class <T> desiredResultType）`。 使用此方法不需要将表达式的值转换为所需的结果类型。 如果该值不能转换为类型T或使用注册的类型转换器转换，则将抛出`EvaluationException`。

Spel的更常见的用法是提供一个针对特定对象实例（称为根对象）进行运算操作的表达式字符串。 这里有两个选项 ，并且选择哪个由反对当前被验证的表示式的对象是否在每次调用后而改变再验证表达式来决定。在以下示例中，我们从`Inventor`类的实例中检索`name`属性。

```
// 创建并对日历对象设值
GregorianCalendar c = new GregorianCalendar();
c.set(1856, 7, 9);

// 构造函数的参数是姓名，生日和国籍。
Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");

ExpressionParser parser = new SpelExpressionParser();
Expression exp = parser.parseExpression("name");

EvaluationContext context = new StandardEvaluationContext(tesla);
String name = (String) exp.getValue(context);
```

在最后一行，字符串变量name的值将被设置为“Nikola Tesla”。 `StandardEvaluationContext`类可以指定哪个对象的“name”属性将被运算操作。 如果根对象不太可能改变，这是使用的机制，可以在运算操作上下文中简单地设置一次。 如果根对象可能会重复更改，则可以在每次调用getValue时提供该对象，如下例所示：

```
// 创建并对日历对象设值
GregorianCalendar c = new GregorianCalendar();
c.set(1856, 7, 9);

// 构造函数的参数是姓名，生日和国籍。
Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");

ExpressionParser parser = new SpelExpressionParser();
Expression exp = parser.parseExpression("name");
String name = (String) exp.getValue(tesla);
```

在这种情况下，目标对象`tesla`已经直接提供给`getValue`，表达式运算操作的基础动作即在内部创建和管理默认运算操作的上下文 - 它不需要被额外提供。

`StandardEvaluationContext`构建起来代价相对昂贵，并在重复使用时构建高速缓存的状态，使得能够更快地执行后续的表达式运算操作。 因此，最好在可能的情况下缓存和重新使用它们，而不是为每个表达式运算操作构建一个新的。

在某些情况下，可能需要使用配置的运算操作上下文，但在每次调用`getValue`时仍会提供不同的根对象。 `getValue`允许在同一个调用中指定两者。 在这些情况下，传递给该调用的根对象被认为覆盖在任何（可能为null）指定的运算操作的上下文中。

> 在独立使用SpEL中，需要创建解析器，解析表达式，并可能提供运算操作上下文和根上下文对象。 但是，更常见的用法是仅将SpEL表达式字符串作为配置文件的一部分，例如Spring bean或Spring Web Flow定义。 在这种情况下，解析器，运算操作上下文，根对象和任何预定义的变量都是隐式设置的，要求用户除表达式之外不要指定。

作为最后的介绍性示例，使用上一个示例中的Inventor对象来显示布尔运算符的使用。

```
Expression exp = parser.parseExpression("name == 'Nikola Tesla'");
boolean result = exp.getValue(context, Boolean.class); // evaluates to true
```

### 3.1、EvaluationContext 接口

运算表达式以解析属性，方法，字段并帮助执行类型转换时使用EvaluationContext接口。 开箱即用的实现StandardEvaluationContext，使用反射来操纵对象，并缓存java.lang.reflect.Method，java.lang.reflect.Field和java.lang.reflect.Constructor实例以提高性能。

StandardEvaluationContext是您可以通过方法setRootObject（）或将根对象传递到构造函数中来指定要对其进行运算操作的根对象。 您还可以使用setVariable（）和registerFunction（）方法指定将在表达式中使用的变量和函数。 变量和函数的使用在语言参考部分变量和函数中有所描述。 

StandardEvaluationContext还可以注册自定义ConstructorResolvers，MethodResolvers和PropertyAccessors，以扩展SpEL如何运算操作表达式。 请参考这些类的JavaDoc了解更多详细信息。

**类型转换**

默认情况下，SpEL使用Spring核心（org.springframework.core.convert.ConversionService）中提供的转换服务。 此转换服务附带许多转换器，内置于常用转换，但也可完全扩展，因此可以添加类型之间的自定义转换。 此外，它具有泛型感知的关键功能。 这意味着在使用表达式中的泛型类型时，SpEL将尝试转换以维护遇到的任何对象的类型正确性。

这在实践中意味着什么？ 假设使用setValue（）的赋值被用于设置List属性。 属性的类型实际上是List <Boolean>。 Spel将会认识到列表的元素需要在被放置在其中之前被转换为布尔值。 一个简单的例子：

```
class Simple {
    public List<Boolean> booleanList = new ArrayList<Boolean>();
}

Simple simple = new Simple();

simple.booleanList.add(true);

StandardEvaluationContext simpleContext = new StandardEvaluationContext(simple);

// false允许作为字符串， SpEL和转换服务将正确识别它需要是一个布尔值并对其进行转换
parser.parseExpression("booleanList[0]").setValue(simpleContext, "false");

// b为布尔值false
Boolean b = simple.booleanList.get(0);
```

### 3.2、解析器配置

可以使用解析器配置对象`（org.springframework.expression.spel.SpelParserConfiguration）`来配置SpEL表达式解析器。 该配置对象控制一些表达式组件的行为。 例如，如果索引到数组或集合中，并且指定索引处的元素为空，其将自动创建元素。 当使用由一组属性引用组成的表达式时，这是非常有用的。 如果索引到数组或列表中，并指定超出数组或列表的当前大小的结尾的索引，其将自动提高数组或列表大小以适应该索引。

```
class Demo {
    public List<String> list;
}

// 开启如下功能:
// - 空引用自动初始化
// - 集合大小自动增长
SpelParserConfiguration config = new SpelParserConfiguration(true,true);

ExpressionParser parser = new SpelExpressionParser(config);

Expression expression = parser.parseExpression("list[3]");

Demo demo = new Demo();

Object o = expression.getValue(demo);

// demo.list现在被实例化为拥有四个元素的集合
// 每个元素都是一个新的空字符串
```

还可以配置SpEL表达式编译器的行为。

### 3.3、SpEL编译

Spring Framework 4.1包含一个基本的表达式编译器。 表达式通常因为在运算操作过程中提供了大量的动态灵活性被解释，但不能提供最佳性能。 对于偶尔的表达式使用这是很好的，但是当被其他并不真正需要动态灵活性的组件（如Spring Integration）使用时，性能可能非常重要。

新的SpEL编译器旨在满足这一需求。 编译器将在体现了表达行为的运算操作期间即时生成一个真正的Java类，并使用它来实现更快的表达式求值。 由于缺少对表达式按类型归类，编译器在执行编译时会使用在表达式解释运算期间收集的信息来编译。 例如，它不仅仅是从表达式中知道属性引用的类型，而是在第一个解释运算过程中会发现它是什么。 当然，如果各种表达式元素的类型随着时间的推移而变化，那么基于此信息的编译可能会导致问题产生。 因此，编译最适合于重复运算操作时类型信息不会改变的表达式。

对于这样的基本表达式：

    someArray[0].someProperty.someOtherProperty < 0.1
    
这涉及到数组访问，某些属性的取消和数字操作，性能增益可以非常显着。 在50000次迭代的微型基准运行示例中，只使用解释器需要75ms，而使用编译版本的表达式仅需3ms。

**编译器配置**

默认情况下，编译器未打开，但有两种方法可以打开它。 可以使用先前讨论的解析器配置过程或者当将SpEL使用嵌入到另一个组件中时通过系统属性打开它。 本节讨论这两个选项。

重要的是要明白，编译器可以运行几种模式，在枚举`（org.springframework.expression.spel.SpelCompilerMode）`中捕获。 模式如下：

* OFF - 编译器关闭; 这是默认值。
* IMMEDIATE - 在即时模式下，表达式将尽快编译。 这通常是在第一次解释运算之后。 如果编译的表达式失败（通常是由于类型更改，如上所述）引起的，则表达式运算操作的调用者将收到异常。
* MIXED - 在混合模式下，表达式随着时间的推移在解释模式和编译模式之间静默地切换。 经过一些解释运行后，它们将切换到编译模式，如果编译后的表单出现问题（如上所述改变类型），表达式将自动重新切换回解释模式。 稍后，它可能生成另一个编译表单并切换到它。 基本上，用户进入即时模式的异常是内部处理的。

存在IMMEDIATE模式，因为混合模式可能会导致具有副作用的表达式的问题。 如果一个编译的表达式在部分成功之后崩掉，它可能已经完成了影响系统状态的事情。 如果发生这种情况，调用者可能不希望它在解释模式下静默地重新运行，因为表达式的一部分可能运行两次。

选择模式后，使用`SpelParserConfiguration`配置解析器：

```
SpelParserConfiguration config = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE,
    this.getClass().getClassLoader());

SpelExpressionParser parser = new SpelExpressionParser(config);

Expression expr = parser.parseExpression("payload");

MyMessage message = new MyMessage();

Object payload = expr.getValue(message);
```

当指定编译器模式时，也可以指定一个类加载器（允许传递null）。 编译表达式将在任何提供的子类加载器中被定义。 重要的是确保是否指定了类加载器，它可以看到表达式运算操作过程中涉及的所有类型。 如果没有指定，那么将使用默认的类加载器（通常是在表达式计算期间运行的线程的上下文类加载器）。

配置编译器的第二种方法是将SpEL嵌入其他组件内部使用，并且可能无法通过配置对象进行配置。 在这些情况下，可以使用系统属性。 属性spring.expression.compiler.mode可以设置为SpelCompilerMode枚举值之一（关闭，即时或混合）。

**编译器限制**

虽然Spring Framework 4.1的基本编译框架已经存在， 但是，框架还不支持编译各种表达式。 最初的重点是在可能在性能要求高的关键环境中使用的常见表达式。 这些表达方式目前无法编译：

* expressions involving assignment(涉及转让的表达)
* expressions relying on the conversion service(依赖转换服务的表达式)
* expressions using custom resolvers or accessors(使用自定义解析器或访问器的表达式)
* expressions using selection or projection(使用选择或投影的表达式)

越来越多的类型的表达式将在未来可编译。

---

## 4、具体bean定义的表达式支持

Spel表达式可以与XML或基于注释的配置元数据一起使用，用于定义`BeanDefinitions`。 在这两种情况下，定义表达式的语法格式为`＃{<expression string>}`。

### 4.1、基于XML的配置

可以使用如下所示的表达式设置属性或构造函数参数arg的值。

```
<bean id="numberGuess" class="org.spring.samples.NumberGuess">
    <property name="randomNumber" value="#{ T(java.lang.Math).random() * 100.0 }"/>

    <!-- other properties -->
</bean>
```

变量`systemProperties`是预定义的，因此可以在您的表达式中使用它，如下所示。 请注意，您不必在此上下文中使用＃符号作为预定义变量的前缀。

```
<bean id="taxCalculator" class="org.spring.samples.TaxCalculator">
    <property name="defaultLocale" value="#{ systemProperties['user.region'] }"/>

    <!-- other properties -->
</bean>
```

您也可以通过名称引用其他bean属性。

```
<bean id="numberGuess" class="org.spring.samples.NumberGuess">
    <property name="randomNumber" value="#{ T(java.lang.Math).random() * 100.0 }"/>

    <!-- other properties -->
</bean>

<bean id="shapeGuess" class="org.spring.samples.ShapeGuess">
    <property name="initialShapeSeed" value="#{ numberGuess.randomNumber }"/>

    <!-- other properties -->
</bean>
```

### 4.2、基于注释的配置

`@Value`注释可以放置在字段，方法和方法/构造函数参数上以指定默认值。

这是一个设置字段变量默认值的示例。

```
public static class FieldValueTestBean

    @Value("#{ systemProperties['user.region'] }")
    private String defaultLocale;

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public String getDefaultLocale() {
        return this.defaultLocale;
    }

}
```

等效的属性设置方法如下所示。

```
public static class PropertyValueTestBean

    private String defaultLocale;

    @Value("#{ systemProperties['user.region'] }")
    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public String getDefaultLocale() {
        return this.defaultLocale;
    }

}
```

使用`@Autowired`注解的方法和构造函数也可以使用`@Value`注释。

```
public class SimpleMovieLister {

    private MovieFinder movieFinder;
    private String defaultLocale;

    @Autowired
    public void configure(MovieFinder movieFinder,
            @Value("#{ systemProperties['user.region'] }") String defaultLocale) {
        this.movieFinder = movieFinder;
        this.defaultLocale = defaultLocale;
    }

    // ...
}
public class MovieRecommender {

    private String defaultLocale;

    private CustomerPreferenceDao customerPreferenceDao;

    @Autowired
    public MovieRecommender(CustomerPreferenceDao customerPreferenceDao,
            @Value("#{systemProperties['user.country']}") String defaultLocale) {
        this.customerPreferenceDao = customerPreferenceDao;
        this.defaultLocale = defaultLocale;
    }

    // ...
}
```

---

## 5、语言参考

### 5.1、字面常量表达式

支持的字面常量表达式的类型是字符串，数值（int，real，hex），boolean和null。 字符串由单引号分隔。 要将一个单引号本身放在字符串中，请使用两个单引号。

以下列表显示了字面常量的简单用法。 通常，它们不会像这样使用，而是作为更复杂表达式的一部分，例如在逻辑比较运算符的一侧使用字面常量。

```
ExpressionParser parser = new SpelExpressionParser();

// evals to "Hello World"
String helloWorld = (String) parser.parseExpression("'Hello World'").getValue();

double avogadrosNumber = (Double) parser.parseExpression("6.0221415E+23").getValue();

// evals to 2147483647
int maxValue = (Integer) parser.parseExpression("0x7FFFFFFF").getValue();

boolean trueValue = (Boolean) parser.parseExpression("true").getValue();

Object nullValue = parser.parseExpression("null").getValue();
```

数字支持使用负号，指数符号和小数点。 默认情况下，使用Double.parseDouble（）解析实数。

### 5.2、Properties, Arrays, Lists, Maps, Indexers

使用属性引用进行导航很简单：只需使用句点来指示嵌套的属性值。 Inventor类，pupin和tesla的实例使用示例中使用的类中列出的数据进行填充。 为了导航“down”，并获得Tesla的出生年份和Pupin的出生城市，使用以下表达式。

```
// evals to 1856
int year = (Integer) parser.parseExpression("Birthdate.Year + 1900").getValue(context);

String city = (String) parser.parseExpression("placeOfBirth.City").getValue(context);
```

属性名称的第一个字母不区分大小写。 数组和列表的内容使用方括号表示法获得。

```
ExpressionParser parser = new SpelExpressionParser();

// Inventions Array
StandardEvaluationContext teslaContext = new StandardEvaluationContext(tesla);

// evaluates to "Induction motor"
String invention = parser.parseExpression("inventions[3]").getValue(
        teslaContext, String.class);

// Members List
StandardEvaluationContext societyContext = new StandardEvaluationContext(ieee);

// evaluates to "Nikola Tesla"
String name = parser.parseExpression("Members[0].Name").getValue(
        societyContext, String.class);

// List and Array navigation
// evaluates to "Wireless communication"
String invention = parser.parseExpression("Members[0].Inventions[6]").getValue(
        societyContext, String.class);
```
        
map映射的内容是通过在括号内指定字面常量键值得到的。 在这种情况下，因为Officers映射的键是字符串，我们可以指定字符串字面常量。

```
// Officer's Dictionary

Inventor pupin = parser.parseExpression("Officers['president']").getValue(
        societyContext, Inventor.class);

// evaluates to "Idvor"
String city = parser.parseExpression("Officers['president'].PlaceOfBirth.City").getValue(
        societyContext, String.class);

// setting values
parser.parseExpression("Officers['advisors'][0].PlaceOfBirth.Country").setValue(
        societyContext, "Croatia");
```

### 5.3、内联列表

List列表可以使用{}表示法直接在表达式中表示。

```
// evaluates to a Java list containing the four numbers
List numbers = (List) parser.parseExpression("{1,2,3,4}").getValue(context);

List listOfLists = (List) parser.parseExpression("{{'a','b'},{'x','y'}}").getValue(context);
```

{}本身就是一个空列表。 出于性能原因，如果列表本身完全由固定字面常量组成，则会创建一个常量列表来表示表达式，而不是在每个运算操作上构建一个新列表。

### 5.4、内联映射

也可以使用`{key：value}`表示法直接在表达式中表示Map映射。

```
// evaluates to a Java map containing the two entries
Map inventorInfo = (Map) parser.parseExpression("{name:'Nikola',dob:'10-July-1856'}").getValue(context);

Map mapOfMaps = (Map) parser.parseExpression("{name:{first:'Nikola',last:'Tesla'},dob:{day:10,month:'July',year:1856}}").getValue(context);
```

`{：}`本身就是一个空的Map映射。 出于性能原因，如果Map映射本身由固定字面常量或其他嵌套常量结构（List列表或Map映射）组成，则会创建一个常量Map映射来表示表达式，而不是在每个运算操作上构建一个新的Map映射。 引用Map映射键是可选的，上面的示例不使用引用的键。

### 5.5、阵列构造

可以使用熟悉的Java语法构建数组，可选择提供一个初始化器，以便在构建时填充数组。

```
int[] numbers1 = (int[]) parser.parseExpression("new int[4]").getValue(context);

// Array with initializer
int[] numbers2 = (int[]) parser.parseExpression("new int[]{1,2,3}").getValue(context);

// Multi dimensional array
int[][] numbers3 = (int[][]) parser.parseExpression("new int[4][5]").getValue(context);
```

当构造多维数组时，它当前不允许提供初始化器。

### 5.6、方法

使用典型的Java编程语法调用方法。 您也可以调用字面常量的方法。

```
// string literal, evaluates to "bc"
String c = parser.parseExpression("'abc'.substring(2, 3)").getValue(String.class);

// evaluates to true
boolean isMember = parser.parseExpression("isMember('Mihajlo Pupin')").getValue(
        societyContext, Boolean.class);
```

### 5.7、运算符

**关系运算符**

关系运算符 小于，小于等于，大于，大于等于，等于，不等于，使用标准运算符符号表示。

```
// evaluates to true
boolean trueValue = parser.parseExpression("2 == 2").getValue(Boolean.class);

// evaluates to false
boolean falseValue = parser.parseExpression("2 < -5.0").getValue(Boolean.class);

// evaluates to true
boolean trueValue = parser.parseExpression("'black' < 'block'").getValue(Boolean.class);
```

> 大于/小于与null的比较遵循一个简单的规则：null在这里被视为没有（不是0）。 因此，任何其他值始终大于null（X> null始终为真），并且没有其他值比它小（X <null始终为false）。如果您更喜欢数字比较，请避免基于数字的null比较，这有利于与零比较（例如X> 0或X <0）。

除标准关系运算符外，SpEL还支持`instanceof` 和基于正则表达式的匹配运算符。

```
// evaluates to false
boolean falseValue = parser.parseExpression(
        "'xyz' instanceof T(Integer)").getValue(Boolean.class);

// evaluates to true
boolean trueValue = parser.parseExpression(
        "'5.00' matches '\^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);

//evaluates to false
boolean falseValue = parser.parseExpression(
        "'5.0067' matches '\^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);
```

> 请谨慎使用原始类型，因为它们会立即包装到包装器类型中，因此，如果预期的话，`1 instanceof T(int)`将计算为`false`，而`1 instanceof T(Integer)`的计算结果为`true`。

每个符号操作符也可以被指定为纯粹的字母等价物。 这避免了所使用的符号对嵌入表达式的文档类型（例如XML文档）具有特殊含义的问题。 文本等价物如下所示：lt（<），gt（>），le（⇐），ge（> =），eq（==），ne（！=），div（/），mod（％） not（！）。 这些不区分大小写。

**逻辑运算符**

支持的逻辑运算符是and, or, and not。 它们的用途如下所示。

```
// -- AND --

// evaluates to false
boolean falseValue = parser.parseExpression("true and false").getValue(Boolean.class);

// evaluates to true
String expression = "isMember('Nikola Tesla') and isMember('Mihajlo Pupin')";
boolean trueValue = parser.parseExpression(expression).getValue(societyContext, Boolean.class);

// -- OR --

// evaluates to true
boolean trueValue = parser.parseExpression("true or false").getValue(Boolean.class);

// evaluates to true
String expression = "isMember('Nikola Tesla') or isMember('Albert Einstein')";
boolean trueValue = parser.parseExpression(expression).getValue(societyContext, Boolean.class);

// -- NOT --

// evaluates to false
boolean falseValue = parser.parseExpression("!true").getValue(Boolean.class);

// -- AND and NOT --
String expression = "isMember('Nikola Tesla') and !isMember('Mihajlo Pupin')";
boolean falseValue = parser.parseExpression(expression).getValue(societyContext, Boolean.class);
```

**算术运算符**

加法运算符可以用于数字和字符串。 减法，乘法和除法只能用于数字。 支持的其他算术运算符是模数（％）和指数幂（^）。 执行标准运算符优先级。 这些操作符将在下面展示。

```
// Addition
int two = parser.parseExpression("1 + 1").getValue(Integer.class); // 2

String testString = parser.parseExpression(
        "'test' + ' ' + 'string'").getValue(String.class); // 'test string'

// Subtraction
int four = parser.parseExpression("1 - -3").getValue(Integer.class); // 4

double d = parser.parseExpression("1000.00 - 1e4").getValue(Double.class); // -9000

// Multiplication
int six = parser.parseExpression("-2 * -3").getValue(Integer.class); // 6

double twentyFour = parser.parseExpression("2.0 * 3e0 * 4").getValue(Double.class); // 24.0

// Division
int minusTwo = parser.parseExpression("6 / -3").getValue(Integer.class); // -2

double one = parser.parseExpression("8.0 / 4e0 / 2").getValue(Double.class); // 1.0

// Modulus
int three = parser.parseExpression("7 % 4").getValue(Integer.class); // 3

int one = parser.parseExpression("8 / 5 % 2").getValue(Integer.class); // 1

// Operator precedence
int minusTwentyOne = parser.parseExpression("1+2-3*8").getValue(Integer.class); // -21
```

### 5.8、赋值

通过使用赋值运算符来完成属性的设置。 这通常在调用setValue之前完成，但也可以在对getValue的调用中完成。

```
Inventor inventor = new Inventor();
StandardEvaluationContext inventorContext = new StandardEvaluationContext(inventor);

parser.parseExpression("Name").setValue(inventorContext, "Alexander Seovic2");

// alternatively

String aleks = parser.parseExpression(
        "Name = 'Alexandar Seovic'").getValue(inventorContext, String.class);
```

### 5.9、类型运算符

特殊的T运算符可用于指定`java.lang.Class`（类型）的实例。 也可以使用此运算符调用静态方法。`TheStandardEvaluationContext`使用`TypeLocator`来查找类型，并且可以使用对java.lang包的理解来构建`StandardTypeLocator`（可以被替换）。 这意味着对`java.lang`中的类型的引用`T（）`不需要是完全限定的，但是所有其他类型引用必须是。

```
Class dateClass = parser.parseExpression("T(java.util.Date)").getValue(Class.class);

Class stringClass = parser.parseExpression("T(String)").getValue(Class.class);

boolean trueValue = parser.parseExpression(
        "T(java.math.RoundingMode).CEILING < T(java.math.RoundingMode).FLOOR")
        .getValue(Boolean.class);
```

### 5.10、构造函数

可以使用新的运算符调用构造函数。 除了原始类型和字符串（可以使用int，float等）之外，所有标准类名称都应该被使用。

```
Inventor einstein = p.parseExpression(
        "new org.spring.samples.spel.inventor.Inventor('Albert Einstein', 'German')")
        .getValue(Inventor.class);

//create new inventor instance within add method of List
p.parseExpression(
        "Members.add(new org.spring.samples.spel.inventor.Inventor(
            'Albert Einstein', 'German'))").getValue(societyContext);
```

### 5.11、变量

可以使用语法`#variableName`在表达式中引用变量。 使用`StandardEvaluationContext`上的`setVariable`方法设置变量。

```
Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
StandardEvaluationContext context = new StandardEvaluationContext(tesla);
context.setVariable("newName", "Mike Tesla");

parser.parseExpression("Name = #newName").getValue(context);

System.out.println(tesla.getName()) // "Mike Tesla"
```

**#this 和 #root 变量**

变量#this始终被定义，并且引用当前的运算操作对象（针对被解析的那个非限定引用）。 变量#root始终被定义，并引用根上下文对象。 虽然#this可能因为表达式的组件被运算操作而变化，但是#root总是引用根。

```
// create an array of integers
List<Integer> primes = new ArrayList<Integer>();
primes.addAll(Arrays.asList(2,3,5,7,11,13,17));

// create parser and set variable 'primes' as the array of integers
ExpressionParser parser = new SpelExpressionParser();
StandardEvaluationContext context = new StandardEvaluationContext();
context.setVariable("primes",primes);

// all prime numbers > 10 from the list (using selection ?{...})
// evaluates to [11, 13, 17]
List<Integer> primesGreaterThanTen = (List<Integer>) parser.parseExpression(
        "#primes.?[#this>10]").getValue(context);
```

### 5.12、功能

您可以通过注册能够在表达式字符串中调用的用户定义的函数来扩展SpEL。 该功能通过方法使用`StandardEvaluationContext`进行注册。

```
public void registerFunction(String name, Method m)
```

对Java方法的引用提供了该函数的实现。 例如，一个反转字符串的实用方法如下所示。

```
public abstract class StringUtils {

    public static String reverseString(String input) {
        StringBuilder backwards = new StringBuilder();
        for (int i = 0; i < input.length(); i++)
            backwards.append(input.charAt(input.length() - 1 - i));
        }
        return backwards.toString();
    }
}
```

然后将该方法注册到运算操作的上下文中，并可在表达式字符串中使用。

```
ExpressionParser parser = new SpelExpressionParser();
StandardEvaluationContext context = new StandardEvaluationContext();

context.registerFunction("reverseString",
    StringUtils.class.getDeclaredMethod("reverseString", new Class[] { String.class }));

String helloWorldReversed = parser.parseExpression(
    "#reverseString('hello')").getValue(context, String.class);
```

### 5.13、Bean引用

如果使用bean解析器配置了运算操作上下文，则可以使用（@）符号从表达式中查找bean。

```
ExpressionParser parser = new SpelExpressionParser();
StandardEvaluationContext context = new StandardEvaluationContext();
context.setBeanResolver(new MyBeanResolver());

// This will end up calling resolve(context,"foo") on MyBeanResolver during evaluation
Object bean = parser.parseExpression("@foo").getValue(context);
```

要访问工厂bean本身，bean名称应改为带有（＆）符号的前缀。

```
ExpressionParser parser = new SpelExpressionParser();
StandardEvaluationContext context = new StandardEvaluationContext();
context.setBeanResolver(new MyBeanResolver());

// This will end up calling resolve(context,"&foo") on MyBeanResolver during evaluation
Object bean = parser.parseExpression("&foo").getValue(context);
```

### 5.14、三元运算符 (If-Then-Else)

您可以使用三元运算符在表达式中执行if-then-else条件逻辑。 一个最小的例子是：

```
String falseString = parser.parseExpression(
        "false ? 'trueExp' : 'falseExp'").getValue(String.class);
```

在这种情况下，布尔值false会返回字符串值“falseExp”。 一个更现实的例子如下所示。

```
parser.parseExpression("Name").setValue(societyContext, "IEEE");
societyContext.setVariable("queryName", "Nikola Tesla");

expression = "isMember(#queryName)? #queryName + ' is a member of the ' " +
        "+ Name + ' Society' : #queryName + ' is not a member of the ' + Name + ' Society'";

String queryResultString = parser.parseExpression(expression)
        .getValue(societyContext, String.class);
// queryResultString = "Nikola Tesla is a member of the IEEE Society"
```

另请参阅Elvis操作员的下一部分，为三元运算符提供更短的语法。

### 5.15、Elvis操作符

Elvis操作符缩短了三元操作符语法，并以Groovy语言使用。 通过三元运算符语法，您通常必须重复一次变量两次，例如：

```
String name = "Elvis Presley";
String displayName = name != null ? name : "Unknown";
```

相反，您可以使用Elvis操作符，命名与Elvis的发型相似。

```
ExpressionParser parser = new SpelExpressionParser();

String name = parser.parseExpression("name?:'Unknown'").getValue(String.class);

System.out.println(name); // 'Unknown'
```

这里是一个更复杂的例子。

```
ExpressionParser parser = new SpelExpressionParser();

Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
StandardEvaluationContext context = new StandardEvaluationContext(tesla);

String name = parser.parseExpression("Name?:'Elvis Presley'").getValue(context, String.class);

System.out.println(name); // Nikola Tesla

tesla.setName(null);

name = parser.parseExpression("Name?:'Elvis Presley'").getValue(context, String.class);

System.out.println(name); // Elvis Presley
```

### 5.16、安全导航运算符

安全导航运算符用于避免`NullPointerException`并来自Groovy语言。 通常当您对对象的引用时，您可能需要在访问对象的方法或属性之前验证它不为空。 为了避免这种情况，安全导航运算符将简单地返回null而不是抛出异常。

```
ExpressionParser parser = new SpelExpressionParser();

Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
tesla.setPlaceOfBirth(new PlaceOfBirth("Smiljan"));

StandardEvaluationContext context = new StandardEvaluationContext(tesla);

String city = parser.parseExpression("PlaceOfBirth?.City").getValue(context, String.class);
System.out.println(city); // Smiljan

tesla.setPlaceOfBirth(null);

city = parser.parseExpression("PlaceOfBirth?.City").getValue(context, String.class);

System.out.println(city); // null - does not throw NullPointerException!!!
```

> Elvis操作符可用于在表达式中设置默认值，例如 在@Value表达式中：`@Value("#{systemProperties['pop3.port'] ?: 25}")`这将注入系统属性`pop3.port`（如果已定义）或25（如果未定义）。

### 5.17、集合选择

选择是强大的表达式语言功能，允许您通过从其条目中选择将一些源集合转换为另一个。
选择使用语法`.?[selectionExpression]`。 这将过滤收集并返回一个包含原始元素子集的新集合。 例如，选择将使我们能够轻松得到Serbian发明家名单：

```
List<Inventor> list = (List<Inventor>) parser.parseExpression(
        "Members.?[Nationality == 'Serbian']").getValue(societyContext);
```

list列表和map映射都可以进行选择。 在前一种情况下，根据每个单独列表元素，同时针对map映射，根据每个map映射条目（Java类型Map.Entry的对象）运算操作作为选择标准。 map映射条目的键和值可以作为选择中使用的属性访问。

此表达式将返回一个由原始map映射的元素组成的新map映射，其中条目值小于27。

```
Map newMap = parser.parseExpression("map.?[value<27]").getValue();
```

除了返回所有选定的元素之外，还可以检索第一个或最后一个值。 要获得与选择匹配的第一个条目，语法为`^[…]`，同时获取最后匹配的选择，语法为`$[…]`。

### 5.18、集合投影

投影允许集合驱动子表达式的运算操作，结果是一个新的集合。 投影的语法是![projectionExpression]。 最容易理解的例子，假设我们有一个发明家的名单，但希望得到他们出生的城市的名单。 有效地，我们要对发明人列表中的每个条目进行“placeOfBirth.city”运算操作。 使用投影：

```
// returns ['Smiljan', 'Idvor' ]
List placesOfBirth = (List)parser.parseExpression("Members.![placeOfBirth.city]");
```

map映射也可以用于驱动投影，在这种情况下，投影表达式将针对map映射中的每个条目进行运算操作（表示为Java Map.Entry）。 跨map映射投影的结果是由对每个map映射条目的投影表达式的运算操作组成的列表。

### 5.19、表达式模板

表达式模板允许文字文本与一个或多个运算操作块进行混合。 每个运算操作块都用您可以定义的前缀和后缀字符进行分隔，常用的选择是使用`#{ }`作为分隔符。 例如，

```
String randomPhrase = parser.parseExpression(
        "random number is #{T(java.lang.Math).random()}",
        new TemplateParserContext()).getValue(String.class);

// evaluates to "random number is 0.7038186818312008"
```

字符串通过连接文本文本'random number is'与运算操作＃{}分隔符中的表达式的结果进行运算操作，在这种情况下是调用`random（）`方法的结果。 `parseExpression（）`方法的第二个参数是ParserContext类型。 `ParserContext`接口用于决定表达式如何被解析以支持表达式模板功能。 `TemplateParserContext`的定义如下所示。

```
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
```

---

## 6、示例中使用的类

Inventor.java

```
package org.spring.samples.spel.inventor;

import java.util.Date;
import java.util.GregorianCalendar;

public class Inventor {

    private String name;
    private String nationality;
    private String[] inventions;
    private Date birthdate;
    private PlaceOfBirth placeOfBirth;

    public Inventor(String name, String nationality) {
        GregorianCalendar c= new GregorianCalendar();
        this.name = name;
        this.nationality = nationality;
        this.birthdate = c.getTime();
    }

    public Inventor(String name, Date birthdate, String nationality) {
        this.name = name;
        this.nationality = nationality;
        this.birthdate = birthdate;
    }

    public Inventor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public PlaceOfBirth getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(PlaceOfBirth placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public void setInventions(String[] inventions) {
        this.inventions = inventions;
    }

    public String[] getInventions() {
        return inventions;
    }
}
```

PlaceOfBirth.java

```
package org.spring.samples.spel.inventor;

public class PlaceOfBirth {

    private String city;
    private String country;

    public PlaceOfBirth(String city) {
        this.city=city;
    }

    public PlaceOfBirth(String city, String country) {
        this(city);
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String s) {
        this.city = s;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
```

Society.java

```
package org.spring.samples.spel.inventor;

import java.util.*;

public class Society {

    private String name;

    public static String Advisors = "advisors";
    public static String President = "president";

    private List<Inventor> members = new ArrayList<Inventor>();
    private Map officers = new HashMap();

    public List getMembers() {
        return members;
    }

    public Map getOfficers() {
        return officers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMember(String name) {
        for (Inventor inventor : members) {
            if (inventor.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

}
```