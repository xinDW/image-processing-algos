package com.thinkpc.plugin;

import com.thinkpc.egde.CannyEdgeDetector;

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

public class CannyEdgeDetectionPlugin implements PlugInFilter {

	@Override
	public int setup(String arg, ImagePlus imp) {
		return DOES_ALL;
	}

	@Override
	public void run(ImageProcessor ip) {
		CannyEdgeDetector cannyDetector = new CannyEdgeDetector(ip);
		cannyDetector.findEdges();
		ImagePlus gradMag = new ImagePlus("gradient magnitude", cannyDetector.getGradMag());
		gradMag.show();
		
		
		ImageProcessor nmsProcessor = new FloatProcessor(cannyDetector.getGradMagAfterNonMaxSuppression());
		ImagePlus gradMagNonMaxSup = new ImagePlus("grad magnitude after non-maximum suppress", 
				nmsProcessor);
		
		gradMagNonMaxSup.show();
		
		ImageProcessor edgeProcessor = new FloatProcessor(cannyDetector.getEdges());
		new ImagePlus("final edges", edgeProcessor).show();
	}
	
	

}
