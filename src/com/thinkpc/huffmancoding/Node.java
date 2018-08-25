package com.thinkpc.huffmancoding;

public class Node implements Comparable<Node>{
	private Node leftChild;
	private Node rightChild;
	
	private int weight;
	
	private char c;
	private String code = "";
	
	public Node() {}
	
	public Node(char c, int weight) {
		this.c = c;
		this.weight = weight;
	}
	public Node(Node l, Node r) {
		this.leftChild = l;
		this.rightChild = r;
		this.weight = l.weight + r.weight;
	}
	
	@Override
	public int compareTo(Node n) {
		return this.weight > n.weight ? 1 : -1;
	}
	
	public String toString() {
		return "char : " + this.c + " freq : " + this.weight;		
	}
	
	public Node left() {
		return this.leftChild;
	}
	
	public Node right() {
		return this.rightChild;
	}
	
	public boolean isLeaf() {
		return this.leftChild == null && this.rightChild == null;
	}
	public char getChar() { return this.c; }
	
	public void setCode(String s) { this.code = s; }
	
	public String getCode() { return this.code; }
	
}
