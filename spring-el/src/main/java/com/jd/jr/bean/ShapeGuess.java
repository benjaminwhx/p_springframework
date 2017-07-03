package com.jd.jr.bean;

/**
 * User: 吴海旭
 * Date: 2017-07-03
 * Time: 下午7:29
 */
public class ShapeGuess {
	private double initialShapeSeed;

	public double getInitialShapeSeed() {
		return initialShapeSeed;
	}

	public void setInitialShapeSeed(double initialShapeSeed) {
		this.initialShapeSeed = initialShapeSeed;
	}

	@Override
	public String toString() {
		return "ShapeGuess{" +
				"initialShapeSeed=" + initialShapeSeed +
				'}';
	}
}

