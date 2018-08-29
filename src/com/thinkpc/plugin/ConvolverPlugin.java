package com.thinkpc.plugin;

import java.awt.AWTEvent;

import com.thinkpc.filter.Convolver;
import com.thinkpc.filter.Gaussian;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

public class ConvolverPlugin implements ExtendedPlugInFilter, DialogListener{
	
	private Convolver convolver;
	
	private double sigma = 2.0;
	private boolean normalize = false; // whether to normalize convolution output or not
	private boolean showPSF = false;
	
	private final int FLAGS = DOES_ALL|KEEP_PREVIEW;
	@Override
	public int setup(String arg, ImagePlus imp) {
		this.convolver = new Convolver();
		return FLAGS;
	}

	@Override
	public void run(ImageProcessor ip) {
		convolver.setNormalize(normalize);
		float[][] convolution = convolver.conv2d(ip.getFloatArray(), Gaussian.generateGaussian2D((float)sigma));
		ip.setFloatArray(convolution);
		
		displayPSFImage(showPSF);
		
		
	}
	
	@Override
	public boolean dialogItemChanged(GenericDialog gd, AWTEvent arg1) {
		sigma = gd.getNextNumber();
		if (sigma < 0 || gd.invalidNumber()) return false;
		
		normalize = gd.getNextBoolean();
		showPSF = gd.getNextBoolean();
		return true;
	}

	@Override
	public void setNPasses(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr) {
		GenericDialog gd = new GenericDialog(command);
		gd.addNumericField("sigma", sigma, 3);
		gd.addCheckbox("normalize", normalize);
		gd.addCheckbox("show PSF", showPSF);
		gd.addPreviewCheckbox(pfr);
		gd.addDialogListener(this);
		gd.showDialog();
		
		sigma = gd.getNextNumber();
		normalize = gd.getNextBoolean();
		showPSF = gd.getNextBoolean();
		
		if (gd.wasCanceled()) return DONE;
		return IJ.setupDialog(imp, FLAGS);

	}
	
	private void displayPSFImage(boolean showPSF) {
		if (!showPSF) return;
		
		ImageProcessor psfProcessor = new FloatProcessor(Gaussian.generateGaussian2D((float)sigma));
		new ImagePlus("PSF sigma = " + this.sigma , psfProcessor).show();
	}

}
