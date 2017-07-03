package com.jd.jr.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * User: 吴海旭
 * Date: 2017-07-03
 * Time: 下午8:01
 */
public class LRBean {
	private Properties userInfo;
	private List<Job> job;

	public Properties getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(Properties userInfo) {
		this.userInfo = userInfo;
	}

	public List<Job> getJob() {
		return job;
	}

	public void setJob(List<Job> job) {
		this.job = job;
	}

	public static class Job {
		private String name;

		public Job(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}

