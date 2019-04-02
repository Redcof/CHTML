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
public abstract class CHTMLParser {

    enum CHTMLTag {
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
    private int MaxBufferLimit_B = 0;
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
        this.MaxBufferLimit_B = (int) (MaxBufferLimit_KB * 1024);
        this.BufferTracking = false;
    }

    public CHTMLParser(CHTMLLexer tree, String Prefix, String sufix, float MaxBufferLimit_KB, boolean bufferTracking) {
        // TODO Auto-generated constructor stub
        setTree(tree);
        OUT_HTML_STRING_PREFIX = Prefix;
        OUT_HTML_STRING_SUFIX = sufix;
        this.Status = CHTMLTag.OutOfScope;
        this.MaxBufferLimit_KB = MaxBufferLimit_KB;
        this.MaxBufferLimit_B = (int) (MaxBufferLimit_KB * 1024);
        this.BufferTracking = bufferTracking;
    }

    public String getOUT_HTML_STREAM() {
        return OUT_HTML_STRING_PREFIX;
    }

    public void setOUT_HTML_STREAM(String oUT_HTML_STREAM) {
        OUT_HTML_STRING_PREFIX = oUT_HTML_STREAM;
    }

    //private boolean FlushDueToBufferExceed = false;
    public void parse() throws IOException, CHTMLParseException {
        this.Tree.generate();
        this.Tokens = this.Tree.getTree();
        String strBuff = new String();
        parsingStart();
        for (final CHTMLToken token : Tokens) {
            if (token.getToken().trim().equalsIgnoreCase(CHTMLLexer.CHTML_TAG_START)) {
                if (this.Status == CHTMLTag.InScope) {
                    pursingException();
                    new CHTMLParserExceptionHandeller(this.Tree.getReader().getSourceCHTMLFile(),
                            token.getLineNumber(),
                            token.getColumnNumber(),
                            "Expecting '" + CHTMLLexer.CHTML_TAG_END + "' but '"
                            + CHTMLLexer.CHTML_TAG_START + "' found at line "
                            + token.getLineNumber() + ":" + token.getColumnNumber());
                }
                this.Status = CHTMLTag.Started;
                this.Status = CHTMLTag.InScope;
                /**
                 * Out anything before starting scope
                 */
                if (!strBuff.isEmpty()) {
                    outParsedString("/* Starting a scope*/\n");
                    htmlOut(strBuff);
                    strBuff = new String();
                }
                outParsedString("\n");
                continue;
            } else if (token.getToken().trim().equalsIgnoreCase(CHTMLLexer.CHTML_TAG_END)) {
                if (this.Status == CHTMLTag.OutOfScope) {
                    pursingException();
                    new CHTMLParserExceptionHandeller(this.Tree.getReader().getSourceCHTMLFile(),
                            token.getLineNumber(),
                            token.getColumnNumber(),
                            "Expecting '" + CHTMLLexer.CHTML_TAG_START + "' but '" + CHTMLLexer.CHTML_TAG_END + "' found at line "
                            + token.getLineNumber() + ":" + token.getColumnNumber());
                }
                this.Status = CHTMLTag.Ended;
                this.Status = CHTMLTag.OutOfScope;
                /**
                 * Out anything before ending scope
                 */
                if (!strBuff.isEmpty()) {
                    outParsedString("/* Ending a scope*/\n");
                    htmlOut(strBuff);
                    strBuff = new String();
                }
                outParsedString("\n");
                continue;
            }

            if (Status == CHTMLTag.InScope) {
                // TODO working with valid C/C++ code
                outParsedString(token.getToken());

            }

            if (Status == CHTMLTag.OutOfScope) {
                //TODO plain string return
                strBuff += token.getToken();
//                if (token.getToken().equals("\n") || token.getToken().equals("\r")) {
//                    strBuff = strBuff + "{{_CHTML_NEW_LINE_}}"; //TODO Optimized this string when outs					
//                } else {
//                    strBuff += token.getToken();
//                }
            }

            /**
             * Out anything if buffer length encounter almost 3KB
             */
            if ((strBuff.length() > MaxBufferLimit_B) && BufferTracking == true) {
                //FlushDueToBufferExceed = true;                
                htmlOut(strBuff);
                strBuff = new String();
            }

        }
        if (!strBuff.isEmpty()) {
            outParsedString("/*Else before exiting loop*/\n");
            htmlOut(strBuff);
            strBuff = new String();
        }
        pursingEnd();
    }

    private String rectifyNewLine(String str) {
        str = str.replace("\n", "\\n\\" + "\n");
        str = str.replace("\r", "\\r\\" + "\r");
        return str;
    }

    private String removeLastEscape(String str) {
        // remove last and 2nd last (\) char
        try {
            int length = str.length();
            int cc = str.charAt(length - 2);
            if (cc == (int) '\\')//92 is escape(\)
            {
                str = str.substring(0, str.length() - 2);
            }
        } catch (Exception e) {
        }
        return str;
    }

    private String rectifyDoubleQuotes(String str) {

        final String DoubleQuote = "\"";
        final String DoubleQuoteReplace = "\\\"";

        str = str.replace(DoubleQuote, DoubleQuoteReplace);		//Double quote (") => (\") modification

        return str;
    }

    private String rectifyEscape(String str) {

        final String BackSlash = "\\";
        final String BackSlashReplace = "\\\\";
        str = str.replace(BackSlash, BackSlashReplace);	//Back slash (\) => (\\) modification

        return str;
    }

    private void out(String str) {
        /* rectify escape */
        str = rectifyEscape(str);
        /* replace new line tokens */
        str = rectifyNewLine(str);
        /* rectify double quote */
        str = rectifyDoubleQuotes(str);
        /* remove last escape */
       // str = removeLastEscape(str);

        //out the string with prefix and postfix
        outParsedString(this.OUT_HTML_STRING_PREFIX + str + this.OUT_HTML_STRING_SUFIX + "\n");
    }

    private void htmlOut(String html) {
        if (!html.trim().isEmpty()) {

            StringBuilder strBuildr = new StringBuilder(html);
            int ctr = 0, len = strBuildr.length(), cutLen = this.MaxBufferLimit_B;
            if (len > this.MaxBufferLimit_B) {
                for (ctr = 0; ctr < len;) {
                    if (len < cutLen) {
                        cutLen = len;
                        outParsedString("\n/* "+len+" Byte(s) final parts. */\n");
                    }else{
                        outParsedString("\n/*" + MaxBufferLimit_B + " Bytes Limit */\n");
                    }
                    String str = strBuildr.substring(ctr, cutLen);
                    strBuildr.delete(0, cutLen);
                    len = strBuildr.length();                   
                    this.out(str);
                }
            } else {
                this.out(html);
            }
            html = new String();
        }
    }

    public CHTMLLexer getTree() {
        return Tree;
    }

    public void setTree(CHTMLLexer tree) {
        Tree = tree;
    }

    public abstract void outParsedString(String parsedCode);

    public abstract void parsingStart();

    public abstract void pursingEnd();

    public abstract void pursingException();

}
