/**
 * 
 */
package com.chtml_parse.core;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Arijit
 *
 */
public abstract class CHTMLParser{
	enum CHTMLTag{
		Started,
		Ended,
		InScope,
		OutOfScope
	};
	private CHTMLLexer Tree;
	private ArrayList<CHTMLToken> Tokens;
	private CHTMLTag Status;
	
	private String OUT_HTML_STRING_PREFIX = "printf";
	private String OUT_HTML_STRING_SUFIX = ";";
	
	private float MaxBufferLimit_KB = 3;
	private float MaxBufferLimit_B = 0;
	private boolean BufferTracking = false;
	
	/**
	 * @author int_soumen
	 * 
	 */
	public CHTMLParser(CHTMLLexer tree, String Prefix, String sufix) {
		// TODO Auto-generated constructor stub
		setTree(tree);
		OUT_HTML_STRING_PREFIX = Prefix;
		OUT_HTML_STRING_SUFIX = sufix;
		this.Status = CHTMLTag.OutOfScope;
		this.MaxBufferLimit_KB = 3;
		this.MaxBufferLimit_B = MaxBufferLimit_KB * 1024;
		this.BufferTracking = false;
	}
	
	public CHTMLParser(CHTMLLexer tree, String Prefix, String sufix,float MaxBufferLimit_KB, boolean bufferTracking) {
		// TODO Auto-generated constructor stub
		setTree(tree);
		OUT_HTML_STRING_PREFIX = Prefix;
		OUT_HTML_STRING_SUFIX = sufix;
		this.Status = CHTMLTag.OutOfScope;	
		this.MaxBufferLimit_KB = MaxBufferLimit_KB;
		this.MaxBufferLimit_B = MaxBufferLimit_KB * 1024;
		this.BufferTracking = bufferTracking;
	}
	

	public String getOUT_HTML_STREAM() {
		return OUT_HTML_STRING_PREFIX;
	}


	public void setOUT_HTML_STREAM(String oUT_HTML_STREAM) {
		OUT_HTML_STRING_PREFIX = oUT_HTML_STREAM;
	}


	public void parse() throws IOException, CHTMLParseException{
		this.Tree.generate();
		this.Tokens = this.Tree.getTree();
		String strBuff = new String();
		pursingStart();
		for (final CHTMLToken token : Tokens) {
			if(token.getToken().trim().equalsIgnoreCase(CHTMLLexer.CHTML_TAG_START)){
				if(this.Status == CHTMLTag.InScope){
					pursingException();
					 new CHTMLParserExceptionHandeller(this.Tree.getReader().getSourceCHTMLFile(),
							token.getLineNumber(),
							token.getColumnNumber(),
							"Expecting '" + CHTMLLexer.CHTML_TAG_END + "' but '" +
							CHTMLLexer.CHTML_TAG_START + "' found at line " +
							token.getLineNumber() + ":" + token.getColumnNumber());
				}
				this.Status = CHTMLTag.Started;
				this.Status = CHTMLTag.InScope;
				/** Out anything before starting scope */
				if(!strBuff.isEmpty()){
					outParsedString("/* Starting a scope*/\n");
					htmlOut(strBuff);
					strBuff = new String();
				}
				outParsedString("\n");
				continue;
			}
			else if(token.getToken().trim().equalsIgnoreCase(CHTMLLexer.CHTML_TAG_END)){
				if(this.Status == CHTMLTag.OutOfScope){
					pursingException();
					new CHTMLParserExceptionHandeller(this.Tree.getReader().getSourceCHTMLFile(),
							token.getLineNumber(),
							token.getColumnNumber(),
							"Expecting '" + CHTMLLexer.CHTML_TAG_START + "' but '" + CHTMLLexer.CHTML_TAG_END + "' found at line " +
				token.getLineNumber() + ":" + token.getColumnNumber());					
				}
				this.Status = CHTMLTag.Ended;
				this.Status = CHTMLTag.OutOfScope;
				/** Out anything before ending scope */
				if(!strBuff.isEmpty()){
					outParsedString("/* Ending a scope*/\n");
					htmlOut(strBuff);
					strBuff = new String();
				}
				outParsedString("\n");
				continue;
			}
			
			if(Status == CHTMLTag.InScope){
				// TODO working with valid C/C++ code
				outParsedString(token.getToken());
				
			}
			
			if(Status == CHTMLTag.OutOfScope){
				//TODO plain string return
				if(token.getToken().equals("\n") || token.getToken().equals("\r")){					
					strBuff = strBuff + "{{_CHTML_NEW_LINE_}}"; //TODO Optimized this string when outs					
				}
				else
				{
					strBuff += token.getToken();
				}				
			}
			
			/** Out anything if buffer length encounter almost 3KB */
			
			if((strBuff.length() > MaxBufferLimit_B) && BufferTracking == true){
				outParsedString("\n/*" + MaxBufferLimit_KB + " KB Limit */\n");
				htmlOut(strBuff);
				strBuff = new String();
			}
		
		}
		if(!strBuff.isEmpty()){
			outParsedString("/*Else before exiting loop*/\n");
			htmlOut(strBuff);
			strBuff = new String();
		}
		pursingEnd();
	}
	
	private String rectifyString(String str)
	{
		
		final String BackSlash = "\\";
		final String BackSlashReplace = "\\\\";		
		str = str.replace(BackSlash, BackSlashReplace);	//Back slash (\) => (\\) modification
		
		
		str = str.replace("{{_CHTML_NEW_LINE_}}", "\\n\\"+"\n");
		/*
		final String BackSlashQuote = "\\'";
		final String BackSlashQuoteReplace = "\\\\'";
		
		str = str.replace(BackSlashQuote, BackSlashQuoteReplace); 		//Single quote (\') => (\\') modification
		
		
		final String QuoteBackSlash = "'\\";
		final String QuoteBackSlashReplace = "'\\\\";
		
		str = str.replace(QuoteBackSlash, QuoteBackSlashReplace); 		//Single quote ('\) => ('\\) modification
		*/
		
		final String DoubleQuote = "\"";
		final String DoubleQuoteReplace = "\\\"";
		
		str = str.replace(DoubleQuote, DoubleQuoteReplace);		//Double quote (") => (\") modification
		
		
		return str;
	}
	
	private void htmlOut(String str){
		str = rectifyString(str);		

		if(!str.trim().isEmpty()){
			outParsedString(this.OUT_HTML_STRING_PREFIX + str + OUT_HTML_STRING_SUFIX + "\n");
			str = new String();
		}
	}
	
	public CHTMLLexer getTree() {
		return Tree;
	}
	
	public void setTree(CHTMLLexer tree) {
		Tree = tree;
	}
	
	public abstract void outParsedString(String parsedCode);
	public abstract void pursingStart();
	public abstract void pursingEnd();
	public abstract void pursingException();


}
