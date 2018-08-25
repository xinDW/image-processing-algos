package com.thinkpc.huffmancoding;

import java.util.Map;

public class HuffmanCoding {
	private String raw;
	private int len;
	private int[] frequency;
	private HuffmanTree hufTree;
	
	public HuffmanCoding() {}
	
	public HuffmanCoding(String raw) { 
		this.raw = raw; 
		this.len = raw.length();
		this.frequency = new int[256];
		initFreq();
		this.hufTree = new HuffmanTree(frequency);
		
	}
	
	public String encoding() {
		return this.hufTree.encoding(raw);
	}
	public String decoding(String code) {
		return this.hufTree.decoding(code);
	}
	public Map<Integer, String> getCodeTable () {
		return hufTree.getCodeTable();
	}
	private void initFreq() {
		for(char c : raw.toCharArray()) {
			frequency[c]++;
		}
	}
}
