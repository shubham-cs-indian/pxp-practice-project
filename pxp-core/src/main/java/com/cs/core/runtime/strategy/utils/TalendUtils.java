/*
 * package com.cs.core.runtime.strategy.utils;
 * 
 * import com.cs.core.config.interactor.model.imprt.EntityImportTalendModel;
 * import com.cs.core.config.interactor.model.imprt.IEntityImportTalendModel;
 * import com.cs.core.runtime.interactor.exception.talend.TalendInputException;
 * import com.cs.core.runtime.interactor.exception.talend.TalendOutputException;
 * import org.apache.commons.io.FileUtils; import
 * org.apache.poi.ss.usermodel.Cell; import org.apache.poi.ss.usermodel.Row;
 * import org.apache.poi.xssf.usermodel.XSSFSheet; import
 * org.apache.poi.xssf.usermodel.XSSFWorkbook; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Component;
 * 
 * import java.io.*; import java.lang.reflect.Method; import
 * java.nio.file.Files; import java.nio.file.Path; import java.nio.file.Paths;
 * 
 * @Component public class TalendUtils {
 * 
 * private static final String TEMPORARY = "temporary"; private static final
 * String TEMPORARY2 = "temporary2"; private static final int PARNET_COL = 3;
 * private static final int LEVEL_COL = 8; private static final int CHILD_COL =
 * 0;
 * 
 * @Autowired protected String hotFolderPath;
 * 
 * @Autowired protected String talendProtocol;
 * 
 * @Autowired protected String talendHost;
 * 
 * @Autowired protected String talendPort;
 * 
 * @Autowired protected String talendContext;
 * 
 * @Autowired ClassLoader talendClassloader;
 * 
 * @Autowired String importExportLogFolderPath;
 * 
 * private static Integer getParent(String parent, Integer level, String
 * fileName, String sheetName) throws Exception { FileInputStream file = new
 * FileInputStream(new File(fileName)); XSSFWorkbook workbook = new
 * XSSFWorkbook(file); XSSFSheet sheet = workbook.getSheet(sheetName); for (Row
 * row : sheet) { String stringCellValue = row.getCell(CHILD_COL)
 * .getStringCellValue(); if (stringCellValue.equals(parent)) { String
 * parentCellValue = row.getCell(PARNET_COL) .getStringCellValue(); if
 * (parentCellValue.equals("-1")) { return ++level; } else { ++level; level =
 * getParent(parentCellValue, level, fileName, sheetName); } } } return level; }
 * 
 * public IEntityImportTalendModel getFileStream(String fileName) throws
 * Exception { String path = new File(hotFolderPath).getAbsoluteFile()
 * .getPath(); path = path + "//" + fileName;
 * 
 * File file = new File(path); if (!file.exists()) { throw new
 * FileNotFoundException(); } Path fileLocation = Paths.get(path); byte[]
 * fileStream = Files.readAllBytes(fileLocation); IEntityImportTalendModel
 * returnModel = new EntityImportTalendModel();
 * returnModel.setFileName(fileName); returnModel.setFileStream(fileStream);
 * 
 * return returnModel; }
 * 
 * public String getFileName(String fileName) { String path = new
 * File(hotFolderPath).getAbsoluteFile() .getPath(); path = path + "//" +
 * fileName; return path; }
 * 
 * public String getLogFileName(String fileName) { String path = new
 * File(importExportLogFolderPath).getAbsoluteFile() .getPath(); path = path +
 * "//" + fileName; return path; }
 * 
 * public void checkExitCodeForImport(String[][] runJob) throws
 * TalendInputException { String exitCode = runJob[0][0]; if (exitCode != null
 * && !exitCode.equals("0")) { throw new TalendInputException(); } }
 * 
 * public void checkExitCodeForExport(String[][] runJob) throws
 * TalendOutputException { String exitCode = runJob[0][0]; if (exitCode != null
 * && !exitCode.equals("0")) { throw new TalendOutputException(); } }
 * 
 * public String getUrlForTalend() { StringBuilder applicationInternalUrl = new
 * StringBuilder(); applicationInternalUrl .append(talendProtocol + "://" +
 * talendHost + ":" + talendPort + "/" + talendContext + "/"); return
 * applicationInternalUrl.toString(); }
 * 
 * public String[][] getExitCode(String classNameLoad, String[] context) throws
 * ClassNotFoundException, NoSuchMethodException, SecurityException { String[][]
 * runJob = new String[0][0]; Class<?> loadedClass =
 * talendClassloader.loadClass(classNameLoad); Method methodToInvoke =
 * loadedClass.getDeclaredMethod("runJob", String[].class);
 * Thread.currentThread() .setContextClassLoader(talendClassloader); try {
 * Object objectOfClasssToInvoke = loadedClass.newInstance(); runJob =
 * (String[][]) methodToInvoke.invoke(objectOfClasssToInvoke, (Object) context);
 * } catch (Throwable e) {
 * System.out.println("\n\n\n\n\n ********* Class loader Call*****************"
 * ); e.printStackTrace(); } return runJob; }
 * 
 * public File makeCopyOfFile(String fileName) throws Exception { String path =
 * new File(hotFolderPath).getAbsoluteFile() .getPath(); String temporarayPath =
 * path + "//" + TEMPORARY + fileName; String temporarayPath2 = path + "//" +
 * TEMPORARY2 + fileName; path = path + "//" + fileName;
 * 
 * File file = new File(path); File fileTemporary = new File(temporarayPath);
 * File fileTemporary2 = new File(temporarayPath2); FileUtils.copyFile(file,
 * fileTemporary); FileUtils.copyFile(fileTemporary, fileTemporary2); return
 * file; }
 * 
 * public Boolean deleteTemporaryFile(String fileName) throws Exception { String
 * path = new File(hotFolderPath).getAbsoluteFile() .getPath(); path = path +
 * "//" + TEMPORARY + fileName; File fileTemporary = new File(path); return
 * fileTemporary.delete(); }
 * 
 * public void setTheLevelDetails(String fileName, String sheetName) throws
 * FileNotFoundException, IOException, Exception { String path = new
 * File(hotFolderPath).getAbsoluteFile() .getPath(); String pathForWrite = path
 * + "//" + TEMPORARY2 + fileName;
 * 
 * path = path + "//" + TEMPORARY + fileName;
 * 
 * FileInputStream fileWrite = new FileInputStream(new File(pathForWrite));
 * XSSFWorkbook writeWorkbook = new XSSFWorkbook(fileWrite); XSSFSheet
 * writeSheet = writeWorkbook.getSheet(sheetName);
 * 
 * FileInputStream file = new FileInputStream(new File(path)); XSSFWorkbook
 * workbook = new XSSFWorkbook(file); XSSFSheet sheet =
 * workbook.getSheet(sheetName);
 * 
 * for (int i = 1; i < sheet.getLastRowNum(); i++) { Row row = sheet.getRow(i);
 * String parentId = row.getCell(PARNET_COL) .toString(); Integer parentLevel =
 * getParent(parentId, 0, path, sheetName); Row writeRow = writeSheet.getRow(i);
 * Cell cell = writeRow.getCell(LEVEL_COL); cell.setCellValue(parentLevel); }
 * 
 * FileOutputStream fileOut = new FileOutputStream(pathForWrite);
 * writeWorkbook.write(fileOut); fileWrite.close(); file.close();
 * fileOut.close(); } }
 */