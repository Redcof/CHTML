package com.chtml_parse.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public abstract class CHTMLLexerFileHandler {

    public CHTMLLexerFileHandler( File file) throws IOException {
        // replace this with a known encoding if possible
        Charset encoding = Charset.defaultCharset();       
        
        handleFile(file, encoding);       
    }

    private  void handleFile(File file, Charset encoding)
            throws IOException {    	
           	InputStream isr = new FileInputStream(file);
            Reader reader = new InputStreamReader(isr, encoding);            
            BufferedReader in = new BufferedReader(reader);
            
			/*
				int r;
				while ((r = reader.read()) != -1) {
				    //char ch = (char) r;
				    handleCharacters(r);
				}
			*/
            
            String line = null;
            int lineNo = 0;
            
            while((line = in.readLine()) != null){
            	lineNo += 1;
            	handleLine(lineNo, line);
            } 
            in.close();
    }

    public abstract void handleLine(int lineNumber, String line);
    
    
    /*
     * public abstract void handleCharacters(int ch)
            throws IOException {
        int r;
        while ((r = reader.read()) != -1) {
            char ch = (char) r;
            System.out.println("Do something with " + ch);
        }
    }
    */

}
