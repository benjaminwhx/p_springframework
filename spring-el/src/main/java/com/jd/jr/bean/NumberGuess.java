package com.jd.jr.bean;

/**
 * User: 吴海旭
 * Date: 2017-07-03
 * Time: 下午5:33
 */
public class NumberGuess {

	private double randomNumber;

	public double getRandomNumber() {
		return randomNumber;
	}

	public void setRandomNumber(double randomNumber) {
		this.randomNumber = randomNumber;
	}

	@Override
	public String toString() {
		return "NumberGuess{" +
				"randomNumber=" + randomNumber +
				'}';
	}
}
