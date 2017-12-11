package com.github.spring.mvc.bean.manager;

import com.github.spring.mvc.bean.UserRpc;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 吴海旭
 * Date: 2017-12-11
 * Time: 下午5:03
 */
@Component
public class RpcManager implements BeanPostProcessor, ApplicationContextAware {
	private ApplicationContext applicationContext;
	private Map<String, Object> map = new HashMap<>();

	/**
	 * 返回结果：
	 * postProcessBeforeInitialization: class com.github.spring.mvc.bean.UserRpcImpl, className: userRpcImpl, isAop: false
	 * postProcessBeforeInitialization single object: com.github.spring.mvc.bean.UserRpcImpl@604f2bd2, isAop: true
	 * postProcessAfterInitialization: class com.github.spring.mvc.bean.UserRpcImpl, className: userRpcImpl, isAop: false
	 * postProcessAfterInitialization single object: com.github.spring.mvc.bean.UserRpcImpl@604f2bd2, isAop: true
	 * queryUser userAspect doSomething
	 * query user
	 *
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof UserRpc) {
			System.out.println("postProcessBeforeInitialization: " + bean.getClass() + ", className: " + beanName + ", isAop: " + AopUtils.isAopProxy(bean));
			// 这里一定要取出spring context里的对象，并放入map里，这样aop才会执行
			Object theSingletonObject = getTheSingletonObject(beanName);
			System.out.println("postProcessBeforeInitialization single object: " + theSingletonObject + ", isAop: " + AopUtils.isAopProxy(theSingletonObject));
			map.put(beanName, theSingletonObject);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof UserRpc) {
			System.out.println("postProcessAfterInitialization: " + bean.getClass() + ", className: " + beanName + ", isAop: " + AopUtils.isAopProxy(bean));
			Object theSingletonObject = getTheSingletonObject(beanName);
			System.out.println("postProcessAfterInitialization single object: " + theSingletonObject + ", isAop: " + AopUtils.isAopProxy(theSingletonObject));
		}
		return bean;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private Object getTheSingletonObject(String beanName){
		return applicationContext.getAutowireCapableBeanFactory().getBean(beanName);
	}

	public <T> T getRpc(String str) {
		return (T) map.get(str);
	}
}
