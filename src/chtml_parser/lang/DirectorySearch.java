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
import java.io.File;
import java.io.IOException;

public abstract class DirectorySearch {

    public String root;
    private StringBuilder IndentLevel = new StringBuilder(" ");

    public DirectorySearch(String dir) {
        File currentDir = new File(dir); // current directory
        root = dir;
        this.search(currentDir);
    }

    public abstract void file(String parentDir, String fileName);

    private void search(File dir) {
        try {
            File[] files = dir.listFiles();
            for (File dirItem : files) {
                if (dirItem.isDirectory()) {
                    // this is a directory
                    System.out.println(IndentLevel + "DIR :" + dirItem.getCanonicalPath());
                    IndentLevel.append(" ");
                    this.search(dirItem);
                } else {
                    // this is a file
                    System.out.println(IndentLevel + "FILE:" + dirItem.getCanonicalPath());
                    try {
                        this.file(dirItem.getParent(), dirItem.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IndentLevel.charAt(IndentLevel.length() - 1);
        }
    }

}
