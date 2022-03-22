package com.cs.di.runtime.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;

import com.cs.core.config.interactor.exception.template.HeaderNotFoundException;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.constants.application.DiConstants;
import com.cs.core.runtime.interactor.exception.onboarding.notfound.SheetNotFoundException;
import com.cs.core.runtime.interactor.model.klassinstance.IWriteInstancesToXLSXFileModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.tasks.AbstractTask;
import com.cs.di.workflow.tasks.ITransformationTask;

public class DiFileUtils {
  
  public static final Integer DATA_ROW_NUMBER   = 2;
  public static final Integer HEADER_ROW_NUMBER = 1;
  public static final Properties properties ;
  public static final String DEFAULT_FILE_SUFFIX         = "yyyy_MM_dd-HH_mm_ss";
  
  static {
    InputStream is = null;
    properties = new Properties();
    try {
      is = AbstractTask.class.getClassLoader()
          .getResourceAsStream("diconfig.properties");
      properties.load(is);
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
    catch (IOException e) {
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
    
  }

  /**
   * This function simply read the file from path which is provided as input.
   * 
   * @param dirPath
   * @param allowSubfolderAccess
   * @param extension
   * @return
   */
  public static List<File> readFilesFromPath(String dirPath, Boolean allowSubfolderAccess, String[] extension) throws Exception
  {
    File folderFile = new File(dirPath);
    List<File> directories = new ArrayList<>();
    directories.add(folderFile);
    List<File> files = new ArrayList<>();
    if (extension == null || extension.length == 0) {
      extension = new String[] { "*.*" };
    }
    List<String> filterWildcards = Arrays.asList(extension);
    FileFilter typeFilter = new WildcardFileFilter(filterWildcards);
    
    if (!allowSubfolderAccess) {
      files.addAll(Arrays.asList(folderFile.listFiles(typeFilter)));
    }
    else {
      while (directories.isEmpty() == false) {
        List<File> subDirectories = new ArrayList<>();
        for (File directory : directories) {
          subDirectories.addAll(Arrays.asList(directory.listFiles((FileFilter) DirectoryFileFilter.INSTANCE)));
          files.addAll(Arrays.asList(directory.listFiles(typeFilter)));
        }
        directories.clear();
        directories.addAll(subDirectories);
      }
    }
    return files;
  }
  
  /**
   * @param filePath
   * @param file
   */
  public static String moveFile(String filePath, File file)
  {
    filePath = (filePath != null && !filePath.endsWith(File.separator)) ? filePath + File.separator : filePath;
    String fileName =  file.getName();
    try {
      FileUtils.copyFile(file, new File(filePath + File.separator + fileName));
      file.delete();
    }
    catch (IOException e) {
      RDBMSLogger.instance().exception(e);
    }
    
    return fileName;
  }
  
  /**
   * @param sheetName
   * @param inputStream
   * @return
   * @throws Exception
   */
  public static Boolean isSheetAvailable(String sheetName, InputStream inputStream) throws Exception
  {
    List<String> availableSheets = new ArrayList<>();
    // Reset the reading position to 0
    inputStream.reset();
    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
    for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
      availableSheets.add(workbook.getSheetName(i));
    }
    workbook.close();
    if (!availableSheets.contains(sheetName)) {
      throw new SheetNotFoundException("Sheet Not Found");
    }
    return true;
  }
  
  /**
   * Get excel sheet
   * 
   * @param workbook
   * @param sheetName
   * @return
   */
  public static XSSFSheet getSheet(XSSFWorkbook workbook, String sheetName)
  {
    XSSFSheet sheet;
    if (sheetName == null || sheetName.equals("")) {
      sheet = workbook.getSheetAt(0);
    }
    else {
      sheet = workbook.getSheet(sheetName);
    }
    return sheet;
  }
  
  /**
   * Adds headers with specific position of row as per configuration
   * 
   * @param inputStream
   * @param sheetName
   * @param headerRowNumber
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   * @throws HeaderNotFoundException
   */
  public static List<String> getHeaderAsList(InputStream inputStream, String sheetName, Integer headerRowNumber)
      throws FileNotFoundException, IOException, HeaderNotFoundException
  {
    // Reset the reading position to 0
    inputStream.reset();
    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
    XSSFSheet sheet = getSheet(workbook, sheetName);
    if (sheet == null) {
      return new ArrayList<>();
    }
    XSSFRow headerRow = sheet.getRow(headerRowNumber - 1);
    if (headerRow == null) {
      throw new HeaderNotFoundException();
    }
    List<String> headerAsList = new ArrayList<>();
    Iterator<Cell> cellIterator = headerRow.iterator();
    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      headerAsList.add(getCellValueInString(cell).trim());
    }
    workbook.close();
    return headerAsList;
  }
  
  /**
   * Get cell value in string format
   * 
   * @param cell
   * @return
   */
  public static String getCellValueInString(Cell cell)
  {
    String value = "";
    if (cell != null) {
      if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
        if (HSSFDateUtil.isCellDateFormatted(cell)) {
          Date date = cell.getDateCellValue();
          SimpleDateFormat dateFormat = new SimpleDateFormat(DiConstants.DATE_FORMAT);
          value = dateFormat.format(date);
        }
        else {
          value = NumberToTextConverter.toText(cell.getNumericCellValue());
        }
      }
      else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
        value = cell.getStringCellValue();
      }
      else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
        Boolean booleanCellValue = cell.getBooleanCellValue();
        value = booleanCellValue.toString();
      }
    }
    return value;
  }
  
  public static Properties loadProperties(final String resource)
  {
    InputStream is = null;
    Properties props = new Properties();
    try {
      is = DiFileUtils.class.getClassLoader().getResourceAsStream(resource);
      props.load(is);
    }
    catch (FileNotFoundException e) {
      RDBMSLogger.instance().exception(e);
    }
    catch (IOException e) {
      RDBMSLogger.instance().exception(e);
    }
    return props;
  }
  
  /**
   * @param sheet
   * @param headerRowNumber
   * @return
   * @throws Exception
   */
  public static List<String> getheaders(XSSFSheet sheet, int headerRowNumber) throws Exception
  {
    XSSFRow headerRow = sheet.getRow(headerRowNumber - 1);
    if (headerRow == null) {
      throw new HeaderNotFoundException();
    }
    List<String> headerAsList = new ArrayList<>();
    Iterator<Cell> cellIterator = headerRow.iterator();
    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      headerAsList.add(getCellValueInString(cell).trim());
    }
    return headerAsList;
  }
  
  /**
   * @param model
   * @throws Exception
   */
  public static void writeToXLSX(IWriteInstancesToXLSXFileModel model) throws Exception
  {
    String sheetName = model.getSheetName();
    Integer headerRowNumber = model.getHeaderRowNumber() - 1;
    Integer dataRowNumber = model.getDataRowNumber() - 1;
    String[] headerToWrite = model.getHeaderToWrite();
    List<String[]> dataToWrite = model.getDataToWrite();
    String filePath = model.getfilePath();
    
    XSSFWorkbook workbook = getOrCreateFileXLSX(filePath);
    // if sheet does not exists then flag will be set
    Boolean flagToGetRowNumber = checkIfSheetExists(workbook, sheetName);
    XSSFSheet sheet = getOrCreateSheet(workbook, sheetName);
    
    if (!flagToGetRowNumber) {
      dataRowNumber = sheet.getPhysicalNumberOfRows(); // this is done for
                                                       // writing in batches.
    }
    
    writeDataToXLSXSheet(headerRowNumber, dataRowNumber, headerToWrite, dataToWrite, sheet);
    FileOutputStream outputStream = new FileOutputStream(filePath);
    workbook.write(outputStream);
    outputStream.close();
    workbook.close();
  }
  
  /**
   * @param workbook
   * @param sheetName
   * @return
   */
  private static boolean checkIfSheetExists(XSSFWorkbook workbook, String sheetName)
  {
    XSSFSheet sheet = workbook.getSheet(sheetName);
    if (sheet == null) {
      return true;
    }
    return false;
  }
  
  /**
   * @param workbook
   * @param sheetName
   * @return
   */
  public static XSSFSheet getOrCreateSheet(XSSFWorkbook workbook, String sheetName)
  {
    XSSFSheet sheet = workbook.getSheet(sheetName);
    if (sheet == null) {
      sheet = workbook.createSheet(sheetName);
    }
    return sheet;
  }
  
  /**
   * @param fileName
   * @return
   * @throws IOException
   * @throws InvalidFormatException
   */
  private static XSSFWorkbook getOrCreateFileXLSX(String fileName) throws IOException, InvalidFormatException
  {
    File file = new File(fileName);
    if (!file.exists()) {
      XSSFWorkbook createWorkbook = new XSSFWorkbook();
      FileOutputStream out = new FileOutputStream(file);
      createWorkbook.write(out);
      out.close();
      return createWorkbook;
    }
    InputStream inputStream = new FileInputStream(fileName);
    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
    inputStream.close();
    return workbook;
  }
  
  /**
   * @param headerRowNumber
   * @param dataRowNumber
   * @param header
   * @param dataToWrite
   * @param sheet
   */
  public static void writeDataToXLSXSheet(int headerRowNumber, int dataRowNumber, String[] header, List<String[]> dataToWrite, Sheet sheet)
  {
    Integer coulumnCnt = 0;
    
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
  
  /**
   * This method will copy file in specified filePath
   * 
   * @param filePath
   * @param file
   */
  public static String copyFile(String filePath, File file)
  {
    filePath = (filePath != null && !filePath.endsWith("/")) ? filePath : filePath + File.separator;
    String fileName = file.getName();
    try {
      FileUtils.copyFile(file, new File(filePath + File.separator + fileName));
    }
    catch (IOException e) {
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
    
    return fileName;
  }
  
  /**
   * This method will delete directory with it's contents
   * 
   * @param rootPath
   * @param executionStatusTable
   * @param executionStatusTable
   * @throws IOException
   */
  public static void deleteDirectory(Path rootPath, IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    try {
      if (Files.exists(rootPath))
        Files.walk(rootPath, FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
    }
    catch (Exception exception) {
      RDBMSLogger.instance().exception(exception);
      executionStatusTable.addError(MessageCode.GEN012);
    }
  }
  
  /**
   * This method will create map from excel sheet.
   * 
   * @param workbook
   * @param sheetName
   * @param fileName
   * @param executionStatusTable
   * @return
   */
  public static List<Map<String, Object>> prepareMapfromSheet(XSSFWorkbook workbook, String sheetName, String fileName,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable, Map<String, String> codeVsTaxonomyTypeMap)
  {
    List<Map<String, Object>> entityList = new ArrayList<>();
    try {
      XSSFSheet sheet = DiFileUtils.getSheet(workbook, sheetName);
      if (sheet == null) {
        throw new Exception();
      }
      List<String> headersToRead = DiFileUtils.getheaders(sheet, HEADER_ROW_NUMBER);
      Integer emptyRowNumber = 0;
      Integer lastRowNumber = sheet.getLastRowNum();
      lastRowNumber++;
      for (int rowCount = DATA_ROW_NUMBER - 1; rowCount < lastRowNumber; rowCount++) {
        Row itemRow = sheet.getRow(rowCount);
        if (itemRow != null && emptyRowNumber < 6) {
          emptyRowNumber = 0;
          Map<String, Object> entityMap = prepareMapfromRow(itemRow, headersToRead);
          if (!CollectionUtils.isEmpty(entityMap)) {
            entityList.add(entityMap);
            if (codeVsTaxonomyTypeMap != null) {
              codeVsTaxonomyTypeMap.put((String) entityMap.get(ITransformationTask.CODE_COLUMN_CONFIG),
                  entityMap.get(ITransformationTask.TYPE_COLUMN) == null
                      ? codeVsTaxonomyTypeMap.get(entityMap.get(ITransformationTask.PARENT_CODE_COLUMN))
                      : (String) entityMap.get(ITransformationTask.TYPE_COLUMN));
            }
          }
        }
        else if (emptyRowNumber > 5) {
          // If there are more than 5 empty rows then stop the processing
          break;
        }
        else {
          emptyRowNumber++;
        }
      }
      
    }
    catch (Exception e) {
      executionStatusTable.addError(new ObjectCode[] {}, MessageCode.GEN007, new String[] { sheetName, fileName });
    }
    return entityList;
  }
  
  /**
   * This Method reads a row in excel file and creates a map which can then be
   * used to prepare PXON
   * 
   * @param itemRow
   * @param headersToRead
   * @return
   */
  public static Map<String, Object> prepareMapfromRow(Row itemRow, List<String> headersToRead)
  {
    Map<String, Object> entityMap = new HashMap<>();
    int cellNumber = 0;
    for (String header : headersToRead) {
      Cell itemCell = itemRow.getCell(cellNumber);
      if (StringUtils.isNoneBlank(header)) {
        String cellValueInString = DiFileUtils.getCellValueInString(itemCell);
        if (header.equals(ITransformationTask.VALUE_OF_PROPERTY_CONCATENATED) || header.equals(ITransformationTask.DEFAULT_VALUE)) {
          entityMap.put(header, cellValueInString);
        }
        else {
          if (!StringUtils.isBlank(cellValueInString)) {
            entityMap.put(header, cellValueInString.trim());
          }
        }
      }
      cellNumber++;
    }
    return entityMap;
  }
  
  public static String getProperty(String prooertyName) {
    
    return properties.getProperty(prooertyName);
  }
  
  /**
   * Get the Name of file
   * @return fileName
   */
  
  public static String generateFileName(String prefix)
  {
    DateFormat dateFormat = new SimpleDateFormat(DEFAULT_FILE_SUFFIX);
    Date date = new Date();
    return prefix + dateFormat.format(date);
  }
  
  public static boolean isDirectoryExist(String directoryPath)
  {
    Path path = Paths.get(directoryPath);
    return Files.isDirectory(path);
  }
  
  public static boolean isDirectoryEmpty(String directoryPath)
  {
    File file = new File(directoryPath);
    if (file != null && file.isDirectory()) {
      return (file.list().length == 0 ? true : false);
    }
    return false;
  }
  
  public static String getDiProcessingPath()
  {
    String diProcessingPath = getProperty("di.processingPath");
    // Check root folder is created or not
    // If not present then create new
    File dir = new File(diProcessingPath);
    if (!dir.exists()) {
      dir.mkdir();
    }
    return diProcessingPath;
  }
  
  public static String getImportedFilesFolderName(String endpointId, String  organizationId)
  {
    String diPprocessingPath = getProperty("di.processingPath");
    String folderName = endpointId + "_" + organizationId;
    String endpointPathname = diPprocessingPath + File.separator + folderName + File.separator;
    return endpointPathname;
  }
}