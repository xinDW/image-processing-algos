package com.thinkpc.egde;

import java.util.Vector;

import ij.plugin.filter.Convolver;
import ij.plugin.filter.GaussianBlur;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

public class CannyEdgeDetector {
	private ImageProcessor image; // source image
	
	private FloatProcessor partialDerivativeX, partialDerivativeY;
	private FloatProcessor gradMag;
	private float[][] gradOrientation;  // direction of gradient, [-pi/2, pi/2]
	
	private float[][] gradMagNonMaxSup; // gradient magnitude after non-maximum suppression
	
	private float[][] edges; // final determined edges
	private float markingColor = 255.f; // color used to mark final edges
	
	private int width, height; // size of the source image
	
	private float sigma = 2.f;
	
	private float pi = (float) Math.PI;
	
	private float thresholdLow = 0.05f, thresholdHigh = 0.2f; // two different threshold values used for "hysteresis thresholding"
	
	private float gradMagMax; // maximum gradient magnitude
	public CannyEdgeDetector(ImageProcessor image) {
		this.image = image;
		this.width = image.getWidth();
		this.height = image.getHeight();
	}
	
	public synchronized void findEdges() {
		if (this.image == null) return;
		
		computeGradientMag();
		
		nonMaxSuppression();
		
		detectAndThreshold();
	}
	
	private void computeGradientMag() {
		float[] filterSobelX = {-1,0,1,-2,0,2,-1,0,1};
		float[] filterSobelY = {-1,-2,-1,0,0,0,1,2,1};
		
		partialDerivativeX = (this.image instanceof FloatProcessor) ?
				(FloatProcessor) this.image.duplicate() :
				this.image.convertToFloatProcessor();
				
		new GaussianBlur().blurGaussian(partialDerivativeX, this.sigma);	
		partialDerivativeY = (FloatProcessor) partialDerivativeX.duplicate();
		
		Convolver convolver = new Convolver();
		convolver.setNormalize(false);
		convolver.convolve(partialDerivativeX, filterSobelX, 3, 3);
		convolver.convolve(partialDerivativeY, filterSobelY, 3, 3);
		
		// calculate magnitude and orientation of gradients
		gradMag = new FloatProcessor(width, height);
		gradOrientation = new float[width][height];
		
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				float dx = partialDerivativeX.getf(w, h);
				float dy = partialDerivativeY.getf(w, h);
				float mag = (float) Math.sqrt(dx * dx + dy * dy);
				gradOrientation[w][h] = (float) Math.atan(dy / dx);
				gradMag.setf(w, h, mag);
				
				gradMagMax = Math.max(gradMagMax, mag);
			}
		}
		
		// normalize gradient magnitude
		//gradMag.multiply(255 / gradMagMax); // if normalized, two thresholds must adjust as well.
		
	}
	
	private void nonMaxSuppression() {
		this.gradMagNonMaxSup = new float[width][height];
		
		for (int u = 1; u < width - 1; u++) {
			for (int v = 1; v < height - 1; v++) {
				if (isLocalMax(u, v))
					gradMagNonMaxSup[u][v] = gradMag.getf(u, v);
			}
		}
	}
	
	private boolean isLocalMax(int u, int v) {
		float angleRad = gradOrientation[u][v];
				
		float gradLow, gradHigh; // two neighbor gradients
		
		if (angleRad <= pi/8 && angleRad > -pi/8) {
			gradLow = gradMag.getf(u-1, v);  // left
			gradHigh = gradMag.getf(u+1, v); // right
		} 
		else if (angleRad > pi/8 && angleRad <= 3*pi/8) {
			gradLow = gradMag.getf(u-1, v+1);  // bottom left
			gradHigh = gradMag.getf(u+1, v-1); // up right
		}
		else if (angleRad > -3*pi/8 && angleRad <= -pi/8) {
			gradLow = gradMag.getf(u+1, v+1);  // bottom right
			gradHigh = gradMag.getf(u-1, v-1); // up left
		}
		else {// (angleRad > 3*pi/8 || angleRad < -3*pi/8)
			gradLow = gradMag.getf(u, v+1);  // bottom 
			gradHigh = gradMag.getf(u, v-1); // up 
		}
		
		return gradMag.getf(u, v) >= gradHigh && gradMag.getf(u, v) >= gradLow;
		
	}
	
	/**
	 * hysteresis threshold
	 * after non-maximum suppress, pixels with gradient magnitude larger than thresholdHigh is marked as an edge point.
	 * pixels with gradient magnitude larger than thresholdLow and is in 8-neighborhoods of an marked edge point are also edge points.
	 */
	private void detectAndThreshold() {
		// determine two thresholds according to the maximum gradient magnitude
		thresholdHigh *= gradMagMax; 
		thresholdLow *= gradMagMax;
		
		this.edges = new float[width][height];
		
		for (int u = 0; u < width; u++) {
			for (int v = 0; v < height; v++) {
				if (gradMagNonMaxSup[u][v] > thresholdHigh && edges[u][v] == 0) {
					edges[u][v] = markingColor;
					traceEdges(u, v);
					
				}
				
				
			}
		}
	}
	private void traceEdges(int u, int v) {
		int ul = Math.max(u - 1, 0);
		int ur = Math.min(u + 1, width - 1);
		int vl = Math.max(v - 1, 0);
		int vh = Math.min(v + 1, height - 1);
		
		for (int i = ul; i <= ur; i++) {
			for (int j = vl; j <= vh; j++) {
				if (edges[i][j] == 0 && edges[i][j] > thresholdLow) {
					edges[i][j] = markingColor;
					traceEdges(i, j);
				}
			}
		}
	}
	
	private void setThresholdsAndRedetectEdges(float thresLow, float thresHigh) {
		this.thresholdHigh = thresHigh;
		this.thresholdLow = thresLow;
		detectAndThreshold();
	}
	
	public void setParameters(float sigma, float thresHigh, float thresLow) {
		this.sigma = sigma;
		this.thresholdHigh = thresHigh;
		this.thresholdLow = thresLow;
	}
	public ImageProcessor getGradMag() { return this.gradMag; }
	
	public float[][] getGradMagAfterNonMaxSuppression() { return this.gradMagNonMaxSup; }
	
	public float[][] getEdges() { return this.edges; }
}
