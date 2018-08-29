package com.thinkpc.filter;

public class Convolver {
	private boolean normalize = false;
	
	public Convolver() {}
	
	public float[][] conv2d(float[][] img, float[][] kernel) {
		
		int kernelSize = kernel.length, kernelRadius = kernel.length / 2;
		int imgHeight = img.length, imgWidth = img[0].length;
		
		float[][] output = new float[imgHeight][imgWidth];
		float maxTmp = 0;
		
		for (int h = 0; h < imgHeight; h++) {
			for (int w = 0; w < imgWidth; w++) {
				float sum = 0;
				for (int i = -kernelRadius; i <= kernelRadius; i++) {
					for (int j = -kernelRadius; j <= kernelRadius; j++) {
						int x = w + j, y = h + i;
						float pixelVal = (y >= 0 && y < imgHeight && x >=0 && x < imgWidth) ? img[y][x] : 0;
						sum += pixelVal * kernel[kernelSize - 1 - (i + kernelRadius)][kernelSize - 1 - (j + kernelRadius)];   // rotate kernel by 180 degree
						
					}
				}
				output[h][w] = sum;
				maxTmp = Math.max(maxTmp, sum);
			}
		}
		if (normalize) {
			for (int h = 0; h < imgHeight; h++) {
				for (int w = 0; w < imgWidth; w++) 
					output[h][w] *= 255/maxTmp;
			}
		}
		return output;
	}
	
	
	public void setNormalize(boolean normalize) {
		this.normalize = normalize;
	}
}
