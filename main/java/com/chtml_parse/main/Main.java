package com.chtml_parse.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.chtml_parse.core.CHTMLParser;
import com.chtml_parse.core.CHTMLReader;
import com.chtml_parse.core.CHTMLReaderFileTypeException;
import com.chtml_parse.core.CHTMLLexer;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		@SuppressWarnings("unused")
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Enter *.chtml/*.html path:");
		String path /* = br.readLine().trim()*/;
		
		/**
		 * F:/int_soumen/_works_/eclipse/workspace/chtml_parse/src/main/resources/test.chtml 
		 * C:\Users\soumen\Desktop\chtml_parse\src\main\resources\test.chtml
		 * path = "F:/int_soumen/_works_/eclipse/workspace/chtml_parse/src/main/resources/test.chtml";
		 * F:\int_soumen\_works_\template3-webstorm\WebstormLayout\index-webstorm.chtml
		 * path = "D:/Avalanchis_Project_Hub/JAVA/eclipse-wp/workspace/chtml_parse/src/main/resources/test.chtml"
		 * */
		
		try {			
			//path = "D:/Avalanchis_Project_Hub/JAVA/eclipse-wp/workspace/chtml_parse/src/main/resources/test.html";
			path = "F:/int_soumen/_works_/eclipse/workspace/chtml_parse/src/main/resources/test.html";
			File src = new File(path);
			String filename = src.getName();
			
			CHTMLReader reader = new CHTMLReader(src);
			
			if (filename.indexOf(".") > 0) {
			    filename = filename.substring(0, filename.lastIndexOf("."));
			}
			
			filename = filename.replaceAll("[^A-Za-z0-9 ]", "_");
			
			final String FILE_NAME = filename;
			final String HASH_DEFINATION = FILE_NAME.toUpperCase() +"_H_";
			final String C_fileName = src.getParent()+"/"+filename + ".c";
			final String H_fileName = src.getParent()+"/"+filename + ".h";
			final String PreFix = "IP_WEBS_SendString(pOutput,\"";
			final String PostFix = "\");";
			
			final String FunctionReturnType = "void ";
			final String FunctionDeclearingArguments = "";
			final String FunctionCallingArguments = "";
			
//			final String OtherIncludes = "#include <stdio.h>\n#include <stdlib.h>\n";
			final String OtherIncludes = "";
			final String BuildConfPath = "F:/int_soumen/Dev-Cpp/MinGW64/";
			final String IncludeMainFunction = "N";
			
			final boolean BufferTracking = true;
			final float MaxBufferLimit_KB = 3.5f;
			
			final boolean GenerateHeader = false;		
			
			CHTMLLexer tg = new CHTMLLexer(reader);			
			CHTMLParser p = new  CHTMLParser(tg, PreFix, PostFix,MaxBufferLimit_KB, BufferTracking) {
				
				File cFile;
				File hFile;
				BufferedWriter br;
				
				@Override
				public void outParsedString(String parsedCode) {
					// TODO Auto-generated method stub
					try {
						/**
						 * Main Code outputs
						 * */
						br.write(parsedCode);
						br.flush();						
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				public void pursingStart(){
					System.out.println("Parsing...");
					FileWriter fw;
					cFile = new File(C_fileName);
					hFile = new File(H_fileName);
					try {
						fw = new FileWriter(cFile);
						br = new BufferedWriter(fw);
						
						if(!OtherIncludes.isEmpty()){
							br.write(OtherIncludes);
							br.flush();	
						}
						if(GenerateHeader){
							br.write("#include \""+ FILE_NAME +".h\"\n\n");
							br.flush();							
												
							br.write(FunctionReturnType + FILE_NAME + "(" + FunctionDeclearingArguments + "){\n");
							br.flush();
						}
						
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}
				
				@SuppressWarnings("unused")
				public void pursingEnd(){
					FileWriter fw;
					try {
						if(GenerateHeader){
							br.write("}\n");
						}
						
						if(IncludeMainFunction.equalsIgnoreCase("Y") && GenerateHeader){
							br.write("int main(int argc, char *argv[]){\n" + FILE_NAME + "(" + FunctionCallingArguments + ")" + ";\nreturn 0;\n}\n");
							br.flush();
						}
						
						
						System.out.println("Generating files...");
						try {
							br.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
							fw = new FileWriter(hFile);
							br = new BufferedWriter(fw);
							if(GenerateHeader){
								br.write("#ifndef " + HASH_DEFINATION + "\n");
								br.flush();
								
								br.write("#define " + HASH_DEFINATION + "\n\n");
								br.flush();
								
								br.write("void " + FILE_NAME + "();" + "\n\n");
								br.flush();
								
								br.write("#endif" + "\n");
								br.flush();
							}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						br.close();
						System.out.println("All Done");
						
						/*
						compile(BuildConfPath,C_fileName);
						run(BuildConfPath,C_fileName );
						*/
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				public void pursingException(){
					try {
						br.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			p.parse();
		} catch (CHTMLReaderFileTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	static void compile(String BuildConfPath, String filePath){
		/*** ** ** * ** ** ** ** ** ** ** ** ** ** ** ** ** **/ 
		String s = null;

        
         try {
            
	    // run the Unix "ps -ef" command
            // using the Runtime exec method:
        	 /*
         = BuildConfPath + "bin/gcc.exe -c " + filePath + " -o main.o -I" + BuildConfPath + "include" + " -I "+ BuildConfPath + 
         			"x86_64-w64-mingw32/include"+ " -I " + BuildConfPath+"lib/gcc/x86_64-w64-mingw32/4.9.2/include";
        	 */
        	 
        	String cmd = BuildConfPath + "bin/gcc.exe -c " + filePath + " -o \"C:/main.o\"";         	
            Process p1 = Runtime.getRuntime().exec(cmd);
           
            /*
            // Get output stream to write from it
            OutputStream out = p1.getOutputStream();

          	out.write("time".getBytes());
            out.flush();	           
            out.close();
            */
            BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(p1.getInputStream()));	            

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p1.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
            
            // read any errors from the attempted command
            System.err.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.err.println(s);
            }
            
            Runtime.getRuntime().exec(BuildConfPath + "bin/gcc.exe \"C:/main.o\" -o \"C:/main.exe\"");
            
        }
        catch (IOException e) {
            System.err.println("exception happened - here's what I know: ");
            e.printStackTrace();
           
        }
	}


	static void run(String BuildConfPath, String filePath){
		/*** ** ** * ** ** ** ** ** ** ** ** ** ** ** ** ** **/ 
		String s = null;

        
         try {
            
	    // run the Unix "ps -ef" command
            // using the Runtime exec method:
            Process p1 = Runtime.getRuntime().exec("C:/main");
           
            /*
            // Get output stream to write from it
            OutputStream out = p1.getOutputStream();

          	out.write("time".getBytes());
            out.flush();	           
            out.close();
            */
            BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(p1.getInputStream()));	            

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p1.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);                
            }
            
            // read any errors from the attempted command
            System.err.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.err.println(s);
            }
            
            System.exit(0);
        }
        catch (IOException e) {
            System.err.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
	}

}
