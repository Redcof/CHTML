package com.chtml_parse.core;

import java.io.File;

public class CHTMLReader {

    private File SourceCHTMLFile;

    public static final String[] extentions = {"chtml", "html", "htm", "css", "js"};

    public static boolean isAllowedExtension(String file_path) {
        try {
            int index = file_path.lastIndexOf(".");

            String extention = file_path.substring(index + 1);
            for (String ext : CHTMLReader.extentions) {
                if (extention.equalsIgnoreCase(ext)) {
                    return true;
                }
            }

        } catch (Exception e) {
        }
        return false;
    }

    public CHTMLReader(File file) throws Exception, CHTMLReaderFileTypeException {
        String received_path = file.getName();

        if (!CHTMLReader.isAllowedExtension(received_path)) {
            throw new CHTMLReaderFileTypeException("File format not supported.");
        }
        this.setSourceCHTMLFile(file);
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
