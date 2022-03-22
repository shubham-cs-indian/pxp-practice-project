package com.cs.core.runtime.interactor.utils;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.opencsv.CSVWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class OffboardingFileUtils {
  
  /*public static void prepareExcelWorkbookWrite(String filePath, String workbookName,
      String sheetName) throws Exception
  {
    File fileDirectory = new File(filePath);
    getOrCreateFileDirectory(fileDirectory);
    getOrCreateFile(fileDirectory, workbookName);
    XSSFWorkbook workbook = new XSSFWorkbook(fileDirectory + "/" + workbookName);
    XSSFSheet sheet = getOrCreateSheet(workbook, sheetName);
  
  }*/
  
  public static XSSFSheet getOrCreateSheet(XSSFWorkbook workbook, String sheetName)
  {
    XSSFSheet sheet = workbook.getSheet(sheetName);
    if (sheet == null) {
      sheet = workbook.createSheet(sheetName);
      // String sn =
      // workbook.getSheetName(workbook.getSheetIndex(sheet.getSheetName()));
      
    }
    return sheet;
  }
  
  public static XSSFWorkbook getOrCreateFileXLSX(String fileName)
      throws IOException, InvalidFormatException
  {
    File file = new File(fileName);
    XSSFWorkbook workbook = null;
    if (!file.exists()) {
      XSSFWorkbook createWorkbook = new XSSFWorkbook();
      FileOutputStream out = new FileOutputStream(file);
      createWorkbook.write(out);
      out.close();
    }
    InputStream inputStream = new FileInputStream(fileName);
    workbook = new XSSFWorkbook(inputStream);
    return workbook;
  }
  
  public static void getOrCreateFile(File fileDirectory, String workbookName) throws IOException
  {
    String filePath = fileDirectory + "/" + workbookName;
    File file = new File(filePath);
    
    if (!file.exists()) {
      file.createNewFile();
    }
  }
  
  public static void getOrCreateFileDirectory(File fileDirectory)
  {
    if (fileDirectory.isDirectory() && !fileDirectory.exists()) {
      fileDirectory.mkdir();
    }
  }
  
  public static void writeDataToXLSXSheet(int headerRowNumber, int dataRowNumber, String[] header,
      List<String[]> dataToWrite, Sheet sheet)
  {
    int coulumnCnt = 0;
    
    Row row = sheet.createRow(headerRowNumber++);
    for (String column : header) {
      Cell cell = row.createCell(coulumnCnt++);
      cell.setCellValue(column);
    }
    
    for (String[] line : dataToWrite) {
      row = sheet.createRow(dataRowNumber++);
      coulumnCnt = 0;
      for (String attributeData : line) {
        Cell cell = row.createCell(coulumnCnt++);
        cell.setCellValue(attributeData);
      }
    }
  }
  
  public static void writeDataToCSVFile(int headerRowNumber, int dataRowNumber, String outputFile,
      String[] headerToWrite, List<String[]> dataToWrite) throws Exception
  {
    
    CSVWriter writer = null;
    BufferedReader br = null;
    
    try {
      
      File file = new File(outputFile);
      
      if (!file.exists()) {
        file.createNewFile();
      }
      
      Writer bw = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile()), "UTF8"));
      
      writer = new CSVWriter(bw, ',');
      br = new BufferedReader(new FileReader(outputFile));
      
      if (br.readLine() == null) {
        writer.writeNext(headerToWrite);
      }
      writer.writeAll(dataToWrite);
    }
    catch (Exception e) {
      RDBMSLogger.instance().info(e.toString());
      throw new Exception(e);
    }
    finally {
      writer.flush();
      writer.close();
      br.close();
    }
  }
}
