package com.thinkpc.huffmancoding;

import java.util.Arrays;

public class MinHeap <T extends Node>{
	private T[] data;
	private int size;
	private int capacity;
	
	public MinHeap() {
		this(10);
	}
	public MinHeap(int len) {
		this.size = 0;
		this.capacity = len;
		this.data = (T[]) new Node[this.capacity + 1];
	}
	public void insert(T item) {
		this.size++;
		resize();
		data[this.size] = item;
		shiftUp(this.size);
	}
	
	public T extractMin() {
		if (this.size == 0) return null;
		T ret = data[1];
		data[1] = data[size--];
		//data[size + 1] = null;
		shiftDown(1);
		return ret;
	}
	
	public boolean isEmpty() {
		return this.size <= 0;
	}
	private void shiftDown(int idx) {
		T item = data[idx];
		int i = idx;
		
		while(i * 2 <= this.size) {			
			int j = i * 2;
			if (j + 1 <= this.size && data[j].compareTo(data[j+1]) > 0) j = j + 1;
			if (data[j].compareTo(item) < 0) {
				data[i] = data[j];
				i = j;
			}
			else break;	
		}
		data[i] = item;
	}
	
	private void shiftUp(int idx) {
		T item = data[idx];
		int i = idx;
		
		while(i / 2 > 0) {
			int j = i / 2;
			if (data[j].compareTo(item) > 0) {
				data[i] = data[j];
				i = j;
			}
			else break;
		}
		data[i] = item;
	}
	
	private void resize() {
		if (this.size == this.capacity) {
			this.capacity *= 2;
			this.data = Arrays.copyOf(this.data, this.capacity + 1);
			return;
		}
	}
}
