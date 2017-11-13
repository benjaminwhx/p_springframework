package com.github.spring.retry;

import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Collections;

/**
 * User: 吴海旭
 * Date: 2017-11-13
 * Time: 下午8:17
 */
public class ExecuteWithRetry {

	public static void main(String[] args) throws Exception {
		SimpleRetryPolicy policy = new SimpleRetryPolicy(5, Collections.singletonMap(Exception.class, true));
		FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
		fixedBackOffPolicy.setBackOffPeriod(2000l);

		RetryTemplate template = new RetryTemplate();
		template.setRetryPolicy(policy);
		template.setBackOffPolicy(fixedBackOffPolicy);
		template.execute(new RetryCallback<Void, Exception>() {
			public Void doWithRetry(RetryContext context) throws Exception {
				// business logic here
				System.out.println("business logic");
				throw new Exception("12");
			}
		}, new RecoveryCallback<Void>() {
			@Override
			public Void recover(RetryContext context) throws Exception {
				System.out.println("retry...");
				return null;
			}
		});
	}
}
