package com.thinkpc.filter;

public class Gaussian {
	public static float[][] generateGaussian2D(float sigma) {
		int diameter = (int) (sigma * 4) + 1;
		if (diameter % 2 == 0) diameter++; // make diameter of the Gaussian kernel an odd number
		int center = diameter / 2;
		
		float[][] kernel = new float[diameter][diameter];
		
		for (int i = 0; i < diameter; i++) {
			for (int j = 0; j < diameter; j++) {
				int u = i - center, v = j - center;
				kernel[i][j] = evalGaussianFunction(u, v, sigma);
			}
		}
		return kernel;
	}
	
	private static float evalGaussianFunction(float x, float y, float sigma) {
		double value = 1 / (2 * Math.PI * sigma * sigma) * Math.exp(-(x*x + y*y) / (2 * sigma * sigma));
		
		return (float) value;
	}
}
