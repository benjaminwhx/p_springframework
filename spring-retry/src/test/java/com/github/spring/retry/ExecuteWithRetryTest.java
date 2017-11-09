package com.github.spring.retry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * User: 吴海旭
 * Date: 2017-11-08
 * Time: 下午8:17
 */
@ContextConfiguration("classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ExecuteWithRetryTest  {
	@Resource
	private ExecuteWithRetry excuteWithRetry;

	@Test
	public void testExcute(){
		excuteWithRetry.query();
	}
}