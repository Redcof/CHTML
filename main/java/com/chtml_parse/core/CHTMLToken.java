package com.chtml_parse.core;

public class CHTMLToken {
	private String Token;
	private int LineNumber;
	private int ColumnNumber;
	public CHTMLToken(java.lang.String string, int lineNumber, int columnNumber) {
		super();
		Token = string;
		LineNumber = lineNumber;
		ColumnNumber = columnNumber;
	}
	public String getToken() {
		return Token;
	}
	public int getLineNumber() {
		return LineNumber;
	}
	public int getColumnNumber() {
		return ColumnNumber;
	}
	
	
}
