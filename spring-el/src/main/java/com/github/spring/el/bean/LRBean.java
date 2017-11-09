package com.github.spring.el.bean;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * User: 吴海旭
 * Date: 2017-07-03
 * Time: 下午8:01
 */
public class LRBean {
	private Properties userInfo;
	private List<City> cities;
	private Job[] job;
	private Map<Integer, String> favourite;

	public Properties getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(Properties userInfo) {
		this.userInfo = userInfo;
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	public Job[] getJob() {
		return job;
	}

	public void setJob(Job[] job) {
		this.job = job;
	}

	public Map<Integer, String> getFavourite() {
		return favourite;
	}

	public void setFavourite(Map<Integer, String> favourite) {
		this.favourite = favourite;
	}

	public static class City {
		private String name;

		public City(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "City{" +
					"name='" + name + '\'' +
					'}';
		}
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

		@Override
		public String toString() {
			return "Job{" +
					"name='" + name + '\'' +
					'}';
		}
	}
}

