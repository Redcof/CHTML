/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chtml_parser.lang;

/**
 *
 * @author MBCS
 */
public class CompileSetting {

    public String path;
    public boolean isDir;
    public String prefix;
    public String postfix;

    public float buff_size;
    public boolean main;
    public boolean header;
    public String prepared_includes;
    public String fn_args;
    public String fn_return;
    
    public boolean autoloadRequire;

    public CompileSetting() {
        path = "";
        isDir = false;
        prefix = "";
        postfix = "";
        buff_size = 3.5f;
        main = false;
        header = false;
        prepared_includes = "";
        fn_args = "void";
        fn_return = "void";
        autoloadRequire = false;
    }
public CompileSetting(CompileSetting s) {
        path = s.path;
        isDir = s.isDir;
        prefix = s.prefix;
        postfix = s.postfix;
        buff_size = s.buff_size;
        main = s.main;
        header = s.header;
        prepared_includes = s.prepared_includes;
        fn_args = s.fn_args;
        fn_return = s.fn_return;
        autoloadRequire = s.autoloadRequire;
    }

}
