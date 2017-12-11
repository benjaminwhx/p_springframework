package com.github.spring.mvc.bean;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author 吴海旭
 * Date: 2017-12-11
 * Time: 下午5:06
 */
@Aspect
@Component
public class UserAspect {

	public UserAspect() {
	}

	@Around(
			value = "execution(* com.github.spring.mvc.bean.*.*(..))",
			argNames = "pjp"
	)
	public Object doSomething(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println(pjp.getSignature().getName() + " userAspect doSomething");
		return pjp.proceed();
	}
}
