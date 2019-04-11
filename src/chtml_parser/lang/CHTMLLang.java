/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chtml_parser.lang;

import com.chtml_parse.core.CHTMLLexer;
import com.chtml_parse.core.CHTMLParser;
import com.chtml_parse.core.CHTMLReader;
import com.chtml_parse.core.CHTMLReaderFileTypeException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author MBCS
 */
public class CHTMLLang {

    public static final String V = "3.0.0";

    /* change log */
 /*
      v 2.6.12
    1. Command line
    2. Directory search
    3. css, chtml, html, js file parsing
      v 2.8.0
    1. Directory and sub directory search
     v3.0.0
    1. Supply additional arguments to the generated functions. A .property file 
       is required for each of the target file which will supply the additional arguments
       add_args = char *message [ to add additional arguments to the generated function]
       add_includes = <stdio.h>,dir/my_header.h[to add specific header files]
     */
    public CHTMLLang() {
        System.out.println("CHTML v" + CHTMLLang.V);
    }

    public void chtmlHelp() {
        System.out.println("************************************************");
        System.out.println("************     CHTML v" + V + "     **************");
        System.out.println("************************************************");
        System.out.println("1. <help> - List all available commands.");
        System.out.println("2. <compile> <path> <prefix> <postfix> [<buff_size>|<main>=[Y|N]|<header>=[Y|N]|<includes>|<fn_return>|<fn_args>]");
        System.out.println("\tCompile a file to *.C & *.H file.");
        System.out.println("\t<path> - it can be a file or directory.");
        System.out.print("\t\tCurently supports:[ ");
        for (String ext : CHTMLReader.extentions) {
            System.out.print("*." + ext + ",");
        }
        System.out.println("]\n\t\tIf directory all *.chtml file will be compiled inside this directory");
        System.out.println("\t<prefix> - the function prefix to print html into browser");
        System.out.println("\t<postfix> - the function postfix to print html into browser");
        System.out.println("\t<buff_size> - Generated string size in KB after which the string should break and generate a new print call.");
        System.out.println("\t\tRange 1KB to 7KB. Default 3.5KB");
        System.out.println("\t<main> - Generate main function. Default is N");
        System.out.println("\t<header> - Generate header file and create a function. Default is N");
        System.out.println("\t<includes> - Append additional includes seperated by comma (,)e.g. =<stdio.h>,dir/my_header.h");
        System.out.println("\t<fn_return> - Return type of generated function. Default is void");
        System.out.println("\t<fn_args> - Arguments of generated function. Default is void");
    }

    public CompileSetting parseCommand_compile(String[] args) {
        CompileSetting cs = new CompileSetting();
        String option_value = null, option = null;
        int length = args.length, ctr = 0, index = -1;
        for (ctr = 0; ctr < length; ctr++) {
            option = args[ctr];
            index = -1;
            /* scan path */
            if (option.startsWith("path")) {
                index = option.indexOf("=");
                option_value = option.substring(index + 1);
                if (option_value.isBlank()) {
                    System.err.println("<path> is required.");
                    System.exit(0);
                }
                cs.path = option_value.trim();
                cs.isDir = new File(cs.path).isDirectory();
            }

            /* scan prefix */
            if (option.startsWith("prefix")) {
                index = option.indexOf("=");
                option_value = option.substring(index + 1);
                if (option_value.isBlank()) {
                    System.err.println("<prefix> is required.");
                    System.exit(0);
                }
                cs.prefix = option_value.trim();
                continue;
            }

            /* scan postfix */
            if (option.startsWith("postfix")) {
                index = option.indexOf("=");
                option_value = option.substring(index + 1);
                if (option_value.isBlank()) {
                    System.err.println("<postfix> is required.");
                    System.exit(0);
                }
                cs.postfix = option_value.trim();
                continue;
            }

            /* scan header */
            if (option.startsWith("header")) {
                index = option.indexOf("=");
                option_value = option.substring(index + 1);
                if (option_value.isBlank()) {
                    System.err.println("<header> is wrong.");
                    System.exit(0);
                }
                cs.header = option_value.trim().equalsIgnoreCase("Y");
                continue;
            }

            /* scan fn_args */
            if (option.startsWith("fn_args")) {
                index = option.indexOf("=");
                option_value = option.substring(index + 1);
                if (option_value.isBlank()) {
                    System.err.println("<fn_args> is wrong.");
                    System.exit(0);
                }
                cs.fn_args = option_value.trim();
                continue;
            }

            /* scan fn_return */
            if (option.startsWith("fn_return")) {
                index = option.indexOf("=");
                option_value = option.substring(index + 1);
                if (option_value.isBlank()) {
                    System.err.println("<fn_return> is wrong.");
                    System.exit(0);
                }
                cs.fn_return = option_value.trim();
                continue;
            }
            /* scan buff_size */
            if (option.startsWith("buff_size")) {
                index = option.indexOf("=");
                option_value = option.substring(index + 1);
                if (option_value.isBlank()) {
                    System.err.println("<fn_return> is wrong.");
                    System.exit(0);
                }
                cs.buff_size = Float.parseFloat(option_value.trim());
                continue;
            }

            /* scan prepared_includes */
            if (option.startsWith("includes")) {
                index = option.indexOf("=");
                option_value = option.substring(index + 1);
                if (option_value.isBlank()) {
                    System.err.println("<includes> is wrong.");
                    System.exit(0);
                }
                String allIncludes[] = option_value.trim().split(",");
                for (String includeItem : allIncludes) {
                    if (!includeItem.trim().isEmpty()) {
                        if (!includeItem.startsWith("<")) {
                            includeItem = "\"" + includeItem + "\"";
                        }
                        cs.prepared_includes += "#include " + includeItem + "\n";
                    }
                }
                continue;
            }
        }

        if (cs.path.isEmpty()) {
            System.err.println("<path> is required.");
            System.exit(0);
        }
        if (cs.prefix.isEmpty()) {
            System.err.println("<prefix> is required.");
            System.exit(0);
        }
        if (cs.postfix.isEmpty()) {
            System.err.println("<postfix> is required.");
            System.exit(0);
        }
        return cs;
    }

    private Properties readProperties(String path, String fileName) {
        try (InputStream input = new FileInputStream(path + "/" + fileName + ".properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            return prop;

        } catch (IOException ex) {
            //ex.printStackTrace();
        }
        return null;
    }

    public void chtmlCompile(CompileSetting cs) {
        try {
            //path = "D:/Avalanchis_Project_Hub/JAVA/eclipse-wp/workspace/chtml_parse/src/main/resources/test.html";
            String path = cs.path;
            File srcFile = new File(path);
            String filenameOnly = srcFile.getName();

            if (filenameOnly.indexOf(".") > 0) {
                filenameOnly = filenameOnly.substring(0, filenameOnly.lastIndexOf("."));
            }
            Properties prop = this.readProperties(srcFile.getParent(), filenameOnly);

            // read properties
            final String FILE_NAME_ONLY_PROCESSED = filenameOnly.replaceAll("[^A-Za-z0-9 ]", "_");
            final String HASH_DEFINATION = FILE_NAME_ONLY_PROCESSED.toUpperCase() + "_H_";
            final String C_fileName = srcFile.getParent() + "/" + FILE_NAME_ONLY_PROCESSED + ".c";
            final String H_fileName = srcFile.getParent() + "/" + FILE_NAME_ONLY_PROCESSED + ".h";
            final String PreFix = cs.prefix + "\"";
            final String PostFix = "\"" + cs.postfix;

            final boolean IncludeMainFunction = cs.main;
            final String FunctionCallingArgumentsInMain = "";

            final boolean GenerateHeader = cs.header;
            final String HeaderIncludes = cs.prepared_includes + "\n";
            final String FunctionReturnType = cs.fn_return + " ";
            final String FunctionDeclearingArguments = cs.fn_args + " ";

            // Additional Properties : add_args
            String option_value = new String("");
            if (prop != null) {
                option_value = prop.getOrDefault("add_args", "").toString().trim();
                if (option_value.charAt(0) != ',') {
                    option_value = "," + option_value;
                }
            }
            final String AdditionalFunctionDeclearingArguments = option_value;

// Additional Properties : add_includes
            option_value = new String("");
            if (prop != null) {
                option_value = prop.getOrDefault("add_includes", "").toString().trim();
                String allIncludes[] = option_value.trim().split(",");
                option_value = new String("");
                for (String includeItem : allIncludes) {
                    if (!includeItem.trim().isEmpty()) {
                        if (!includeItem.startsWith("<")) {
                            includeItem = "\"" + includeItem + "\"";
                        }
                        option_value += "#include " + includeItem + "\n";
                    }
                }
            }
            final String AdditionalHeaderIncludes = option_value;

            final String BuildConfPath = "F:/int_soumen/Dev-Cpp/MinGW64/";

            final boolean BufferTracking = true;
            final float MaxBufferLimit_KB = cs.buff_size;

            CHTMLReader reader = new CHTMLReader(srcFile);
            CHTMLLexer tg = new CHTMLLexer(reader);
            CHTMLParser p = new CHTMLParser(tg, PreFix, PostFix, MaxBufferLimit_KB, BufferTracking) {
                File cFile;
                File hFile;
                BufferedWriter br;

                @Override
                public void outParsedString(String parsedCode) {
                    // TODO Auto-generated method stub
                    try {
                        /* Main Code outputs */
                        br.write(parsedCode);
                        br.flush();

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                public void parsingStart() {
                    System.out.println("CHTML Creating.... > " + FILE_NAME_ONLY_PROCESSED);
                    FileWriter fw;
                    cFile = new File(C_fileName);
                    hFile = new File(H_fileName);
                    try {
                        // creating *.C file
                        fw = new FileWriter(cFile);
                        br = new BufferedWriter(fw);

                        if (GenerateHeader) {
                            // start by generating header inclusion and function defination inside *.C file
                            br.write("#include \"" + FILE_NAME_ONLY_PROCESSED + ".h\"\n\n");
                            br.flush();

                            br.write(FunctionReturnType + FILE_NAME_ONLY_PROCESSED + "(" + FunctionDeclearingArguments + AdditionalFunctionDeclearingArguments + "){\n");
                            br.flush();
                        }

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                public void pursingEnd() {
                    FileWriter fw;
                    try {
                        if (GenerateHeader) {
                            // closing function defination inside *.C file
                            br.write("}\n");
                        }

                        if (IncludeMainFunction && GenerateHeader) {
                            // generating main() if required *.C file
                            br.write("int main(int argc, char *argv[]){\n" + FILE_NAME_ONLY_PROCESSED + "(" + FunctionCallingArgumentsInMain + ")" + ";\nreturn 0;\n}\n");
                            br.flush();
                        }

                        try {
                            // closing *.C file
                            br.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        // creating *.H file
                        fw = new FileWriter(hFile);
                        br = new BufferedWriter(fw);

                        if (!HeaderIncludes.isEmpty()) {
                            // writting includes
                            br.write(HeaderIncludes);
                            br.flush();
                        }
                        if (!AdditionalHeaderIncludes.isEmpty()) {
                            // writting additional includes
                            br.write(AdditionalHeaderIncludes);
                            br.flush();
                        }

                        if (GenerateHeader) {
                            //generating header syntax and function decleration inside *.H file
                            br.write("#ifndef " + HASH_DEFINATION + "\n");
                            br.flush();

                            br.write("#define " + HASH_DEFINATION + "\n\n");
                            br.flush();

                            // br.write("void " + FILE_NAME_ONLY_PROCESSED + "(" + FunctionCallingArgumentsInMain + AdditionalFunctionDeclearingArguments + ");" + "\n\n");
                            br.write(FunctionReturnType + FILE_NAME_ONLY_PROCESSED + "(" + FunctionDeclearingArguments + AdditionalFunctionDeclearingArguments + ");\n\n");
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
                        System.out.println("CHTML genaration done.");
                        /*
			compile(BuildConfPath,C_fileName);
			run(BuildConfPath,C_fileName );
                         */
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        System.err.println(e.getLocalizedMessage());
                    }
                }

                public void pursingException() {
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
            System.err.println(e.getLocalizedMessage());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e.getLocalizedMessage());
        }
    }
}
