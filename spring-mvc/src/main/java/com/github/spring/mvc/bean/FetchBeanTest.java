package com.github.spring.mvc.bean;

import com.github.spring.mvc.bean.manager.RpcManager;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author 吴海旭
 * Date: 2017-12-11
 * Time: 下午5:05
 */
public class FetchBeanTest {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
//		UserRpc userRpc = (UserRpc) context.getBean("userRpcImpl");
//		boolean aopProxy = AopUtils.isAopProxy(userRpc);
//		boolean jdkAopProxy = AopUtils.isJdkDynamicProxy(userRpc);
//		boolean cglibProxy = AopUtils.isCglibProxy(userRpc);
//		System.out.println("userRpc: " + userRpc.getClass() + " isAop: " + aopProxy + ", isJdkAopProxy: " + jdkAopProxy + ", isCglibProxy: " + cglibProxy);
//		userRpc.queryUser();

		RpcManager rpcManager = (RpcManager) context.getBean("rpcManager");
		UserRpc userRpc = rpcManager.getRpc("userRpcImpl");
		userRpc.queryUser();
	}
}
