package com.chtml_parse.core;

import java.io.File;

public class CHTMLReader {
	
	private File SourceCHTMLFile;
	
	public CHTMLReader(File path) throws Exception, CHTMLReaderFileTypeException{
		String received_path = path.getName();
		try{
			int index = received_path.lastIndexOf(".");
			String extention = received_path.substring(index + 1);
			if(!extention.equalsIgnoreCase("CHTML") && !extention.equalsIgnoreCase("HTML"))
			{
				throw new CHTMLReaderFileTypeException("File format not supported.");
			}
			
		}
		catch (StringIndexOutOfBoundsException e) {
			throw new CHTMLReaderFileTypeException("File format not supported.");
		}
		catch (Exception e) {
			throw e;
		}
		
		this.setSourceCHTMLFile(path);
	}
	/**
	 * @return the sourceCHTMLFile
	 */
	public File getSourceCHTMLFile() {
		return SourceCHTMLFile;
	}
	
	/**
	 * @param sourceCHTMLFile the sourceCHTMLFile to set
	 */
	public void setSourceCHTMLFile(File sourceCHTMLFile) {
		SourceCHTMLFile = sourceCHTMLFile;
	}
	
}
