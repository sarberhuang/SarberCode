package com.qa.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//对比文本工具
public class diffText {
    private static final String FILE_PATH = "C:/Users/Doctor/Desktop/70库里的表.txt"; //参考文本路径
     private static final String COMPARED_FILE_PATH = "C:/Users/Doctor/Desktop/200库里的表.txt";  //需要比较文件的路径
    private static final String RESULT_FILE_PATH = "C:/Users/Doctor/Desktop/70库里有的200库里没有的表.xlsx";  //输入结果比对文件，文件名可以修改
    public static void main(String[] args) {
        System.out.println("======开始比对文本=======");
        long startTime = System.currentTimeMillis();
        // Read first file
        File file = new File(FILE_PATH);
        File comparedFile = new File(COMPARED_FILE_PATH);
        BufferedReader br = null;
        BufferedReader cbr = null;
        BufferedWriter rbw = null;
        try {
            br = new BufferedReader(new FileReader(file));
            cbr = new BufferedReader(new FileReader(comparedFile));
            cbr.mark(90000000);
            rbw = new BufferedWriter(new FileWriter(RESULT_FILE_PATH));
            String lineText = null;
            while ((lineText = br.readLine()) != null) {
                String searchText = lineText.trim();
                searchAndSignProcess(searchText, cbr, rbw);
            }
            long endTime = System.currentTimeMillis();
            System.out.println("======比对文本结束!=======");
            System.out.println("耗时:" + ((endTime - startTime) / 1000D) + "s");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭文件读写流操作
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (cbr != null && rbw != null) {
                        try {
                            cbr.close();
                            rbw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    public static void searchAndSignProcess(String searchText, BufferedReader comparedReader, BufferedWriter rbw)
            throws IOException {
        String lineStr = "-\n";
        if (searchText == null) {
            return;
        }
        if ("".equals(searchText)) {
            rbw.write(lineStr);
            return;
        }
        String lineText = null;
        int lineNum = 1;
        while ((lineText = comparedReader.readLine()) != null) {
            String comparedLine = lineText.trim();
            if (searchText.equals(comparedLine)) {
                lineStr = "";
                break;
            }else {
                lineStr = searchText+"\n";
                lineNum++;
            }

        }
        if(!lineStr.equals("")){   //选择你输出是需要相同还是不同数据的,根据一些表示，达到将一些相同/不同的过滤掉
        rbw.write(lineStr);
        //在结果文件中写数据操作
        }
        comparedReader.reset();
    }
}