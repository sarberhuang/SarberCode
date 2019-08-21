package com.qa.util;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtils {
    //遍历excel
    private static FileInputStream fileInputStream = null;
    private static FileOutputStream fileOutputStream = null;
    private static XSSFWorkbook workbook = null;
    private static XSSFSheet sheet = null;
    private static XSSFRow row = null;
    private static XSSFCell cell = null;
    private static XSSFCellStyle style = null;
    private static XSSFFont font = null;

    public static Object[][] dtt(String filePath) throws IOException {

        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);

        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sh = wb.getSheetAt(0);
        int numberrow = sh.getPhysicalNumberOfRows();
        int numbercell = sh.getRow(1).getLastCellNum();

        Object[][] dttData = new Object[numberrow][numbercell];
        for(int i=0;i<numberrow;i++){
            if(null==sh.getRow(i)||"".equals(sh.getRow(i))){
                continue;
            }
            for(int j=0;j<numbercell;j++) {
                if(null==sh.getRow(i).getCell(j)||"".equals(sh.getRow(i).getCell(j))){
                    continue;
                }
                XSSFCell cell = sh.getRow(i).getCell(j);
                cell.setCellType(CellType.STRING);
                dttData[i][j] = cell.getStringCellValue();
            }
        }

        return dttData;//返回表格数据 目前直接从Excele表格从第2行2列开始
    }

    //遍历excel，sheet参数
    public static Object[][] dtt(String filePath,int sheetId) throws IOException{

        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);

        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sh = wb.getSheetAt(sheetId);
        int numberrow = sh.getPhysicalNumberOfRows();    //excel表里行的总数
        int numbercell = sh.getRow(0).getLastCellNum();  //excel表里列的总数

        Object[][] dttData = new Object[numberrow][numbercell];
        for(int i=0;i<numberrow;i++){
            if(null==sh.getRow(i)||"".equals(sh.getRow(i))){
                continue;
            }
            for(int j=0;j<numbercell;j++) {
                if(null==sh.getRow(i).getCell(j)||"".equals(sh.getRow(i).getCell(j))){
                    continue;
                }
                XSSFCell cell = sh.getRow(i).getCell(j);
                cell.setCellType(CellType.STRING);
                dttData[i][j] = cell.getStringCellValue();
            }
        }

        return dttData;//将excel表里的数据返回给自动化脚本调用方法
    }
//    /**
//     *  将测试结果写入excel
//     * @param result 测试结果
//     * @param rownum 行（从0开始）
//     * @param cellnum 列（从0开始）
//     * @param path excel文件路径
//     * @param sheetName sheet名
//     */
    public static void setCellData(String sheetName,String result,int rowNum,int cellNum,String outputPath) {
        try {
            sheet = workbook.getSheet(sheetName);
            font = workbook.createFont();
            style = workbook.createCellStyle();
            row = sheet.getRow(rowNum);
            cell = row.getCell(cellNum);
            if (cell == null) {
                cell = row.createCell(cellNum);
                cell.setCellValue(result);
                if("true".equals(result)||"PASS".equals(result)) {
                    font.setColor((short) 3);
                    style.setFont(font);
                    cell.setCellStyle(style);
                }else if("FAIL".equals(result)) {
                    font.setColor((short) 2);
                    style.setFont(font);
                    cell.setCellStyle(style);
                }else if("SKIP".equals(result)){
                    font.setColor(new XSSFColor(new java.awt.Color(178, 178, 178)));
                    style.setFont(font);
                    cell.setCellStyle(style);
                }else if("WARNING".equals(result)) {
                    font.setColor(new XSSFColor(new java.awt.Color(255, 192, 0)));
                    style.setFont(font);
                    cell.setCellStyle(style);
                }
            } else {
                cell.setCellValue(result);
                if("PASS".equals(result)) {
                    font.setColor((short) 3);
                    style.setFont(font);
                    cell.setCellStyle(style);
                }else if("FAIL".equals(result)) {
                    font.setColor((short) 2);
                    style.setFont(font);
                    cell.setCellStyle(style);
                }else if("SKIP".equals(result)){
                    font.setColor(new XSSFColor(new java.awt.Color(178, 178, 178)));
                    style.setFont(font);
                    cell.setCellStyle(style);
                }else if("WARNING".equals(result)) {
                    font.setColor(new XSSFColor(new java.awt.Color(255, 192, 0)));
                    style.setFont(font);
                    cell.setCellStyle(style);
                }
            }
            fileOutputStream = new FileOutputStream(outputPath);
            workbook.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
