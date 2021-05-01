package com.objects;

import java.util.List;

// [START bookshelf_result]
public class Result<K> {
	private String cursor;
	private List<K> result;

	public String getCursor() {
		return cursor;
	}

	public void setCursor(String cursor) {
		this.cursor = cursor;
	}

	public List<K> getResult() {
		return result;
	}

	public void setResult(List<K> result) {
		this.result = result;
	}

	public Result(List<K> result, String cursor) {
		this.result = result;
		this.cursor = cursor;
	}

	public Result(List<K> result) {
		this.result = result;
		this.cursor = null;
	}
}
// [END bookshelf_result]
