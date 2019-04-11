/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chtml_parser;

import chtml_parser.lang.CHTMLLang;
import chtml_parser.lang.CompileSetting;
import chtml_parser.lang.DirectorySearch;
import com.chtml_parse.core.CHTMLReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author MBCS
 */
public class Main {

    public static void main(String[] args) throws IOException {
        
        CHTMLLang lang = new CHTMLLang();

        int argLen = args.length;
        boolean commandReceived = false;
        do {
            if (argLen == 0) {
                break;
            }
            String first_arg = args[0];
            if (first_arg.equalsIgnoreCase("compile")) {
                commandReceived = true;
            } else {
                break;
            }
        } while (false);
        if (commandReceived == false) {
            lang.chtmlHelp();
        } else {
            /** parse arguments */
            CompileSetting cs = lang.parseCommand_compile(args);
            if (cs.isDir == true) { 
                cs.autoloadRequire = true;
                // directory found. Search inside directory
                new DirectorySearch(cs.path) {
                    @Override
                    public void file(String parentDir, String fileName) {
                        if (CHTMLReader.isAllowedExtension(fileName)) {
                            CompileSetting cs2 = new CompileSetting(cs);
                            cs2.path = parentDir + "\\" + fileName;                            
                            lang.chtmlCompile(cs2);
//                            String relativePath = new String(parentDir);
//                            relativePath = relativePath.replace(root+"\\", "");
//                            autoload_includes += relativePath + "/" + fileName + "\n";
                        }
                    }
                };
            } else {
                lang.chtmlCompile(cs);
            }
        }
    }

    static void compile(String BuildConfPath, String filePath) {
        /**
         * * ** ** * ** ** ** ** ** ** ** ** ** ** ** ** ** *
         */
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
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p1.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(p1.getErrorStream()));

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

        } catch (IOException e) {
            System.err.println("exception happened - here's what I know: ");
            e.printStackTrace();

        }
    }

    static void run(String BuildConfPath, String filePath) {
        /**
         * * ** ** * ** ** ** ** ** ** ** ** ** ** ** ** ** *
         */
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
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p1.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(p1.getErrorStream()));

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
        } catch (IOException e) {
            System.err.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
