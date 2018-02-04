下面是spring-test 4.2以前的写法。使用`TransactionConfiguration` 来配置事务管理器和是否执行完进行回滚操作。
```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-dao-test.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class Test {
    
    @Test
    public void testXXX() {}
}
```

下面是spring-test 4.2以后的写法。`@Rollback` 的value代表执行完方法后要不要回滚，因为测试类默认是回滚的，这样不会把数据弄脏数据库，所以得单独配置。
```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-dao-test.xml")
@Rollback(value = false)
@Transactional(transactionManager = "transactionManager")
public class Test {
    
    @Test
    public void testXXX() {}
}
```