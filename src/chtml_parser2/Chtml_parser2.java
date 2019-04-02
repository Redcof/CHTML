/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chtml_parser2;

import com.chtml_parse.core.CHTMLLexer;
import com.chtml_parse.core.CHTMLParser;
import com.chtml_parse.core.CHTMLReader;
import com.chtml_parse.core.CHTMLReaderFileTypeException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

class CompileSetting {

    String path;
    boolean isDir;
    String prefix;
    String postfix;

    float buff_size;
    boolean main;
    boolean header;
    String includes;
    String fn_args;
    String fn_return;

    public CompileSetting() {
        path = "";
        isDir = false;
        prefix = "";
        postfix = "";
        buff_size = 3.5f;
        main = false;
        header = false;
        includes = "";
        fn_args = "void";
        fn_return = "void";
    }

}

/**
 *
 * @author MBCS
 */
public class Chtml_parser2 {

    static final String V = "2.5.11";

    static void chtmlHelp() {
        System.out.println("************************************************");
        System.out.println("************     CHTML v" + V + "     **************");
        System.out.println("************************************************");
        System.out.println("1. <help> - List all available commands.");
        System.out.println("2. <compile> <path> <prefix> <postfix> [<buff_size>|<main>=[Y|N]|<header>=[Y|N]|<includes>|<fn_return>|<fn_args>]");
        System.out.println("\tCompile a *.chtml file to *.C & *.H file.");
        System.out.println("\t<path> - it can be a file or directory.");
        System.out.println("\t\tCurently supports " + CHTMLReader.extentions.toString());
        System.out.println("\t\tIf directory all *.chtml file will be compiled inside this directory");
        System.out.println("\t<prefix> - the function prefix to print html into browser");
        System.out.println("\t<postfix> - the function postfix to print html into browser");
        System.out.println("\t<buff_size> - Generated string size in KB after which the string should break and generate a new print call.");
        System.out.println("\t\tRange 1KB to 7KB. Default 3.5KB");
        System.out.println("\t<main> - Generate main function. Default is N");
        System.out.println("\t<header> - Generate header file and create a function. Default is N");
        System.out.println("\t<includes> - Append additional includes seperated by comma (,)");
        System.out.println("\t<fn_return> - Return type of generated function. Default is void");
        System.out.println("\t<fn_args> - Arguments of generated function. Default is void");
    }

    static CompileSetting parseCommand_compile(String[] args) {
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

            /* scan includes */
            if (option.startsWith("includes")) {
                index = option.indexOf("=");
                option_value = option.substring(index + 1);
                if (option_value.isBlank()) {
                    System.err.println("<includes> is wrong.");
                    System.exit(0);
                }
                String allIncludes[] = option_value.trim().split(",");
                for (String allInclude : allIncludes) {
                    if (!allInclude.startsWith("<")) {
                        allInclude = "\"" + allInclude + "\"";
                    }
                    cs.includes += "#include " + allInclude + "\n";
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

    public static void main(String[] args) throws IOException {
        System.out.println("************************************************");
        System.out.println("************     CHTML v" + V + "     **************");
        System.out.println("************************************************");
        int argLen = args.length;
        boolean command = false;
        do {
            if (argLen == 0) {
                break;
            }
            String first_arg = args[0];
            if (first_arg.equalsIgnoreCase("compile")) {
                command = true;
            } else {
                break;
            }
        } while (false);
        if (command == false) {
            chtmlHelp();
        } else {
            CompileSetting cs = parseCommand_compile(args);
            if (cs.isDir == true) {
                String files[] = new File(cs.path).list();
                for (String fileName : files) {
                    String filePath = cs.path + "/" + fileName;
//                    int dotIndex = fileName.lastIndexOf('.');
//                    String ext = fileName.substring(dotIndex + 1);
                    if (CHTMLReader.isAllowedExtension(fileName)) {                        
                        CompileSetting cs2 = new CompileSetting();
                        cs2.path = filePath;
                        cs2.prefix = cs.prefix;
                        cs2.postfix = cs.postfix;
                        cs2.buff_size = cs.buff_size;
                        cs2.main = cs.main;
                        cs2.header = cs.header;
                        cs2.includes = cs.includes;
                        cs2.fn_args = cs.fn_args;
                        cs2.fn_return = cs.fn_return;
                        chtmlCompile(cs2);
                    }
                }
            } else {
                chtmlCompile(cs);
            }
        }
    }

    static void chtmlCompile(CompileSetting cs) {
        try {
            //path = "D:/Avalanchis_Project_Hub/JAVA/eclipse-wp/workspace/chtml_parse/src/main/resources/test.html";
            String path = cs.path;
            File src = new File(path);
            String filename = src.getName();

            if (filename.indexOf(".") > 0) {
                filename = filename.substring(0, filename.lastIndexOf("."));
            }

            filename = filename.replaceAll("[^A-Za-z0-9 ]", "_");

            final String FILE_NAME = filename;
            final String HASH_DEFINATION = FILE_NAME.toUpperCase() + "_H_";
            final String C_fileName = src.getParent() + "/" + filename + ".c";
            final String H_fileName = src.getParent() + "/" + filename + ".h";
            final String PreFix = cs.prefix + "\"";
            final String PostFix = "\"" + cs.postfix;

            final boolean IncludeMainFunction = cs.main;
            final boolean GenerateHeader = cs.header;
            final String OtherIncludes = cs.includes + "\n";
            final String FunctionReturnType = cs.fn_return + " ";
            final String FunctionDeclearingArguments = cs.fn_args + " ";

            final String FunctionCallingArguments = "";

            final String BuildConfPath = "F:/int_soumen/Dev-Cpp/MinGW64/";

            final boolean BufferTracking = true;
            final float MaxBufferLimit_KB = cs.buff_size;

            CHTMLReader reader = new CHTMLReader(src);
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
                    System.out.println("Parsing.... > " + FILE_NAME);
                    FileWriter fw;
                    cFile = new File(C_fileName);
                    hFile = new File(H_fileName);
                    try {
                        fw = new FileWriter(cFile);
                        br = new BufferedWriter(fw);

                        if (!OtherIncludes.isEmpty()) {
                            br.write(OtherIncludes);
                            br.flush();
                        }
                        if (GenerateHeader) {
                            br.write("#include \"" + FILE_NAME + ".h\"\n\n");
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
                public void pursingEnd() {
                    FileWriter fw;
                    try {
                        if (GenerateHeader) {
                            br.write("}\n");
                        }

                        if (IncludeMainFunction && GenerateHeader) {
                            br.write("int main(int argc, char *argv[]){\n" + FILE_NAME + "(" + FunctionCallingArguments + ")" + ";\nreturn 0;\n}\n");
                            br.flush();
                        }

                        System.out.println("Creating... > " + C_fileName);
                        try {
                            br.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        fw = new FileWriter(hFile);
                        br = new BufferedWriter(fw);
                        if (GenerateHeader) {
                            br.write("#ifndef " + HASH_DEFINATION + "\n");
                            br.flush();

                            br.write("#define " + HASH_DEFINATION + "\n\n");
                            br.flush();

                            br.write("void " + FILE_NAME + "(" + FunctionCallingArguments + ");" + "\n\n");
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
                        System.out.println("OK.");
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
