package com.thinkpc.plugin;

import java.awt.AWTEvent;
import java.awt.Scrollbar;
import java.util.Iterator;
import java.util.Vector;

import com.thinkpc.egde.CannyEdgeDetector;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

public class CannyEdgeDetectionPlugin implements ExtendedPlugInFilter, DialogListener {
	private final int FLAGS = DOES_ALL|KEEP_PREVIEW;
	
	private CannyEdgeDetector cannyDetector;
	
	private double sigma = 2;
	private double thresholdHigh = 0.2, thresholdLow = 0.05;
	
	private GenericDialog gd;
	private ImagePlus imagePlus;
	
	private static boolean exists = false;
	private static int ID;
	
	@Override
	public int setup(String arg, ImagePlus imp) {
		this.imagePlus = imp;
		cannyDetector = new CannyEdgeDetector(imp.getProcessor());
		return FLAGS;
	}

	@Override
	public void run(ImageProcessor ip) {
		
		cannyDetector.findEdges();
		
		/*
		ImagePlus gradMag = new ImagePlus("gradient magnitude", cannyDetector.getGradMag());
		gradMag.show();
		
		
		ImageProcessor nmsProcessor = new FloatProcessor(cannyDetector.getGradMagAfterNonMaxSuppression());
		ImagePlus gradMagNonMaxSup = new ImagePlus("grad magnitude after non-maximum suppress", 
				nmsProcessor);
		
		gradMagNonMaxSup.show();
		if (!exists) {
			ImageProcessor edgeProcessor = new FloatProcessor(cannyDetector.getEdges());
			ImagePlus finalEdgePlus = new ImagePlus("final edges", edgeProcessor);
			finalEdgePlus.show();
			exists = true;
			this.ID = finalEdgePlus.getID();
		}
		else {
			WindowManager.getImage(ID).getProcessor().setFloatArray(cannyDetector.getEdges());
			WindowManager.getImage(ID).updateAndDraw();
		}
		*/
		imagePlus.getProcessor().setFloatArray(cannyDetector.getEdges());
		imagePlus.updateAndDraw();
		
		
	}

	@Override
	public boolean dialogItemChanged(GenericDialog arg0, AWTEvent arg1) {
		this.sigma = ((Scrollbar) (gd.getSliders().get(0))).getValue();
		this.thresholdHigh = ((Scrollbar) (gd.getSliders().get(1))).getValue() / 20.;
		this.thresholdLow = ((Scrollbar) (gd.getSliders().get(2))).getValue() / 20.;
		
		cannyDetector.setParameters((float)sigma, (float)thresholdHigh, (float)thresholdLow);
		//IJ.log("sigma: " + sigma + "   high: " + thresholdHigh + "   low: " + thresholdLow);
		return true;
	}

	@Override
	public void setNPasses(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr) {
		gd = new GenericDialog(command);
		gd.addSlider("sigma", 0.0, 10.0, sigma);
		gd.addSlider("high threshold", 0, 1.0, thresholdHigh);
		gd.addSlider("low threshold", 0.0, 1, thresholdLow);
		gd.addPreviewCheckbox(pfr);
		
		gd.addDialogListener(this);
		gd.showDialog();
		
		
		if (gd.wasCanceled()) return DONE;
		return IJ.setupDialog(imp, FLAGS);
	}
	
	

}
