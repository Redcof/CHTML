package com.chtml_parse.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CHTMLTree {
	public static final String CHTML_TAG_START = "<?chtml";
	public static final String CHTML_TAG_END = "?>";
	public static final String CHTML_TAG_REGEX = "(\\?>)|(<\\?[cC][hH][tT][mM][lL])";
	public static final int TAG_OPEN_GROUP_IN_REGEX = 1;
	public static final int TAG_CLOSE_GROUP_IN_REGEX = 0;
	public static final int TAG_NOT_GROUP_IN_REGEX = -1;
	private ArrayList<CHTMLToken> Tree;	
	private CHTMLReader Reader;
	
	public CHTMLTree(CHTMLReader reader) {
		// TODO Auto-generated constructor stub
		this.setReader(reader);
		setTree(new ArrayList<CHTMLToken>());
	}
	
	public void generate() throws IOException{
		
		new FileHandler(this.Reader.getSourceCHTMLFile()) {
			//int LastChar;
			int CurrentChar;
			String token = new String();
			
			@Override
			public void handleLine(int lineNumber, String line) {
				int charIndex = 0;
				int columnIndex = charIndex;
				if(token == null) token = new String();
				
				for (; charIndex < line.length() ; charIndex++) 
				{
					int ch = line.charAt(charIndex);
					CurrentChar = ch;					
					if(!isWhiteSpace(CurrentChar)){
						token += (char)CurrentChar;
					}
					else
					{
						int colNo = columnIndex;
						columnIndex = charIndex + 2;
						if( colNo < 0){
							colNo = 0;
						}
						
						if(!token.isEmpty()){							
							appendToken(token,lineNumber,colNo);
							token = new String();
						}						
						appendToken((char)CurrentChar+"",lineNumber,  colNo);						
					}
					//LastChar = ch;
				}
				
				if(!token.isEmpty()){
					appendToken(token, lineNumber, columnIndex - 1);
					token = new String();
				}
				appendToken("\n",lineNumber, columnIndex);
			}
		};
		
	}
	
	private boolean isWhiteSpace(int ch){
		return (ch == 9 || ch == 10 || ch == 11 || ch == 12 || ch == 13 || ch == 32 || ch == 133 || ch == 160);
	}
	
	private void appendToken(String token, int lineNumber, int columnNumber){		
		Matcher matcher = Pattern.compile(CHTML_TAG_REGEX).matcher(token);
		boolean found = false;
		int  index = 0;
		while(matcher.find()){			
			found = true;
			int group = matcher.group(TAG_CLOSE_GROUP_IN_REGEX) == null ? 
					( matcher.group(TAG_OPEN_GROUP_IN_REGEX) == null ? TAG_OPEN_GROUP_IN_REGEX : TAG_NOT_GROUP_IN_REGEX ) : 
						TAG_CLOSE_GROUP_IN_REGEX;
			if(group != TAG_NOT_GROUP_IN_REGEX ){
				String matchedSequence = matcher.group(group);
				
				int start = matcher.start(group);
							
				if(start == 0){
					this.Tree.add(new CHTMLToken(matchedSequence, lineNumber, columnNumber+index));
					index += matchedSequence.length();
				}
				else{
					if(index != start){
						this.Tree.add(new CHTMLToken(token.substring(index, start), lineNumber, columnNumber+index));
						index += start - index;
					}
					this.Tree.add(new CHTMLToken(matchedSequence, lineNumber, columnNumber+index));
					index += matchedSequence.length();
				}
			}
		}
		
				
		if(found){
			if(index < token.length()){
				this.Tree.add(new CHTMLToken(token.substring(index), lineNumber, columnNumber+index));
			}
		}
		else
		{
			this.Tree.add(new CHTMLToken(token, lineNumber, columnNumber));
		}
		
	}
	/**
	 * @return the reader
	 */
	public CHTMLReader getReader() {
		return Reader;
	}
	/**
	 * @param reader the reader to set
	 */
	public void setReader(CHTMLReader reader) {
		Reader = reader;
	}

	/**
	 * @return the tree
	 */
	public ArrayList<CHTMLToken> getTree() {
		return Tree;
	}

	/**
	 * @param tree the tree to set
	 */
	public void setTree(ArrayList<CHTMLToken> tree) {
		Tree = tree;
	}

}
