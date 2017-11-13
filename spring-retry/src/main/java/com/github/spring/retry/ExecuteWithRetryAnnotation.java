package com.github.spring.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * User: 吴海旭
 * Date: 2017-11-08
 * Time: 下午8:14
 */
@Service
public class ExecuteWithRetryAnnotation {
	private static final Logger logger = LoggerFactory.getLogger(ExecuteWithRetryAnnotation.class);
	/**
	 * 重试次数
	 */
	private static final int TRY_COUNT = 3;
	/**
	 * 暂停1s
	 */
	private static final long SLEEP_TIME = 1000;

	/**
	 * maxAttempts，尝试次数
	 * backoff，可以指定暂停时间
	 * value和inclue一样，当出现这些异常时，进行重试
	 */
	@Retryable(maxAttempts = TRY_COUNT,
			backoff = @Backoff(delay = SLEEP_TIME),
			value = {RuntimeException.class})
	public void query() {
		logger.info("execute query,time={}", System.currentTimeMillis());
		if (true) {
			throw new RuntimeException("error");
		}
	}

	@Recover
	public void recover(RuntimeException e) {
		logger.info("recover exception: " + e.getMessage());
	}
}