package com.github.spring.mvc.bean;

import org.springframework.stereotype.Service;

/**
 * @author 吴海旭
 * Date: 2017-12-11
 * Time: 下午5:02
 */
@Service
public class UserRpcImpl implements UserRpc {

	@Override
	public void queryUser() {
		System.out.println("query user");
	}
}
