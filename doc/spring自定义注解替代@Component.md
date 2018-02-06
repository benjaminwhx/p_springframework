# 1、问题起源
项目中有很多包，但是部署的时候要分开部署，例如有a-web、a-worker、a-service、a-dao等包。a-web和a-worker是独立的两个包，如果我们在a-web的spring配置文件里面配置了注解扫描：

```java
<context:component-scan base-package="com.jd.a" />
```

可是a-worker上面我也有注解啊，这时候怎么解决不让spring扫描a-worker包下面的注解呢？

简单的方式就是在a-web下面多配置几个包，比如：

```java
<context:component-scan base-package="com.jd.a.web" />
<context:component-scan base-package="com.jd.a.service" />
<context:component-scan base-package="com.jd.a.dao" />
```

但是，我不想加一个包配置一行，有什么办法呢？

# 2、解决方法
我们可以通过自定义注解的方式，来让两个包解耦。  
首先，我们可以在common里面定义一个注解@Worker，然后把worker所有加了@Component的类上换成@Worker，这样web在扫描的时候就不会去扫描了。我们在worker的spring配置文件里面加上下面的方式使注解生效：

```java
<context:component-scan base-package="com.jd.a.worker">
    <context:include-filter type="annotation" expression="com.jd.a.common.Worker" />
</context:component-scan>
```

这样子是不是更简单也更清晰呢？