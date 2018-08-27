package com.thinkpc.huffmancoding;

public class Test {

	public static void main(String[] args) {
		HuffmanCoding hc = new HuffmanCoding("1.Never in the history of calm down has anyone calmed down by been told to calm down");
		String code = hc.encoding();
		System.out.println(hc.decoding(code));
		
	}

}
