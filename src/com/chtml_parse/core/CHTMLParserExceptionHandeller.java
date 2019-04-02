package com.chtml_parse.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class CHTMLParserExceptionHandeller{
	 public CHTMLParserExceptionHandeller(File src, int LineNumber, int colNo, String errorTxt) throws IOException, CHTMLParseException{
		 InputStream isr = new FileInputStream(src);
		 Charset encoding = Charset.defaultCharset();			
         Reader reader = new InputStreamReader(isr, encoding);            
         BufferedReader in = new BufferedReader(reader);
         String line = null;
         int lineNo = 0;         
         while((line = in.readLine()) != null){
         	lineNo += 1;
         	if(lineNo == LineNumber){
         		
         		StringBuffer sb = new StringBuffer(Math.abs(colNo));
         		for (int i = 0; i < colNo; i++){
         			sb.append(" ");
         		}         		
         		sb.append('^'); 
         		in.close();
				throw new CHTMLParseException("\n"+line +"\n"+ sb.toString()+"\n"+errorTxt);
			}
         }
         in.close();
	 }
}
