package com.thinkpc.huffmancoding;

import java.util.HashMap;
import java.util.Map;

public class HuffmanTree {
	private MinHeap<Node> nodeHeap;
	private Node root;
	
	private Map<Integer, String> codeTable;
	
	public HuffmanTree() {}
	
	public HuffmanTree(int[] frequency) {
		nodeHeap = new MinHeap<>(frequency.length);
		for (int i = 0; i < frequency.length; i++) {
			if (frequency[i] > 0) {
				nodeHeap.insert(new Node((char)i, frequency[i]));
			}
		}
		
		while(!nodeHeap.isEmpty()) {
			Node n1 = nodeHeap.extractMin();
			Node n2 = nodeHeap.extractMin();
			if (n2 != null) nodeHeap.insert(new Node(n1, n2));
			else this.root = n1;
		}
		
		generateCode();
	}
	
	/** 
	 * DFS to generate Huffman code for each source symbol
	 */	
	private void generateCode() {
		this.codeTable = new HashMap<>();
		DFS(this.root);
	}
	private void DFS(Node n) {
		
		if (n.left() != null) {
			n.left().setCode(n.getCode() + "0");
			DFS(n.left());
		}
		if (n.right() != null) {
			n.right().setCode(n.getCode() + "1");
			DFS(n.right());
		}
		else {
			codeTable.put((int) n.getChar(), n.getCode());
			//System.out.println(n.getChar() + " : " + n.getCode());
		}
	}
	
	public String encoding(String s) {
		StringBuilder sBuilder = new StringBuilder();
		for (char c : s.toCharArray()) 
			sBuilder.append(codeTable.get((int)c));
		
		System.out.println(s + " -> " + sBuilder.toString());
		return sBuilder.toString();
	}
	
	public String decoding(String code) {
		StringBuilder sBuilder = new StringBuilder();
		Node marker = this.root;
		for (char c : code.toCharArray()) {
			if (marker.isLeaf()) {
				sBuilder.append(marker.getChar());
				marker = this.root;
			}
			if (c == '0') marker = marker.left();
			else marker = marker.right();
		}
		sBuilder.append(marker.getChar());	
		return sBuilder.toString();
	}
	public Map<Integer, String> getCodeTable() {
		return this.codeTable;
	}
}
