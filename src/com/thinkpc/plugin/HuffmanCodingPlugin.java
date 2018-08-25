package com.thinkpc.plugin;

import java.util.Map;

import com.thinkpc.huffmancoding.HuffmanCoding;

import ij.IJ;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;

public class HuffmanCodingPlugin implements PlugIn {
	
	private String sourceSymbols;
	private HuffmanCoding hc;
	
	@Override
	public void run(String arg) {
		if (!showDialog()) return;
		
		hc = new HuffmanCoding(sourceSymbols);
		
		printCodeTable();
		String code = hc.encoding();
		
		IJ.log("source : " + sourceSymbols);
		IJ.log("after encoding : " + code);
		IJ.log("decoded as : " + hc.decoding(code));

	}
	
	private boolean showDialog() {
		GenericDialog gd = new GenericDialog("Huffman Coding");
		gd.addStringField("source ", "type here", 20);
		
		gd.showDialog();
		
		sourceSymbols = gd.getNextString();
		
		if (gd.wasCanceled()) return false;
		return true;
	}
	
	private void printCodeTable() {
		Map<Integer, String> codeTable = hc.getCodeTable();
		if (codeTable == null) return;
		
		IJ.log("=============================");
		for (Map.Entry<Integer, String> entry : codeTable.entrySet()) {
			IJ.log("source symbol : " + (char) (entry.getKey().intValue()) + " -> code : " + entry.getValue());
		}
		IJ.log("=============================");
	}
}
