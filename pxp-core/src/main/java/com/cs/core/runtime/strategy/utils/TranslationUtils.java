package com.cs.core.runtime.strategy.utils;

import com.cs.core.config.interactor.model.translations.IGetPropertyTranslationsModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsResponseModel;
import com.cs.core.config.interactor.model.translations.IStandardTranslationModel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class TranslationUtils {
  
  public static final List<String>         TRANSLATION_HEADER          = Arrays.asList("EntityType",
      "EntityId", "EntityCode", "Label", "Description", "PlaceHolder", "Tooltip", "Label",
      "Description", "PlaceHolder", "Tooltip", "Label", "Description", "PlaceHolder", "Tooltip",
      "Label", "Description", "PlaceHolder", "Tooltip", "Label", "Description", "PlaceHolder",
      "Tooltip");
  public static final Map<Integer, String> TRANSLATION_HEADER_LANGUAGE = new HashMap<Integer, String>();
  
  static {
    TRANSLATION_HEADER_LANGUAGE.put(3, "English (US)");
    TRANSLATION_HEADER_LANGUAGE.put(7, "English (UK)");
    TRANSLATION_HEADER_LANGUAGE.put(11, "German");
    TRANSLATION_HEADER_LANGUAGE.put(15, "French");
    TRANSLATION_HEADER_LANGUAGE.put(19, "Spanish");
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
  
  public static XSSFSheet getOrCreateSheet(XSSFWorkbook workbook, String sheetName)
  {
    XSSFSheet sheet = workbook.getSheet(sheetName);
    if (sheet == null) {
      sheet = workbook.createSheet(sheetName);
    }
    return sheet;
  }
  
  public void prepareDataForExportOfStaticTranslation(String entityType, String sheetName,
      IGetTranslationsResponseModel response) throws InvalidFormatException, IOException
  {
    List<IGetPropertyTranslationsModel> staticTranslation = response.getData();
    String filePath = new String();
    XSSFWorkbook workbook = getOrCreateFileXLSX(filePath);
    XSSFSheet sheet = getOrCreateSheet(workbook, sheetName);
    
    int rowCounter = 1;
    for (IGetPropertyTranslationsModel iGetPropertyTranslationsModel : staticTranslation) {
      XSSFRow createRow = sheet.createRow(rowCounter++);
      
      XSSFCell createCell = createRow.createCell(0);
      createCell.setCellValue(entityType);
      
      XSSFCell createCellEnityId = createRow.createCell(1);
      createCellEnityId.setCellValue(iGetPropertyTranslationsModel.getId());
      
      fillTheTranslationDependOnLanguage(iGetPropertyTranslationsModel, createRow);
    }
    
    FileOutputStream outputStream = new FileOutputStream(filePath);
    workbook.write(outputStream);
    outputStream.close();
  }
  
  public Integer prepareDataForExportOfStaticTranslation(String entityType, String sheetName,
      IGetTranslationsResponseModel response, Integer rowCounter, String filePath,
      Boolean createHeader) throws InvalidFormatException, IOException
  {
    List<IGetPropertyTranslationsModel> staticTranslation = response.getData();
    XSSFWorkbook workbook = getOrCreateFileXLSX(filePath);
    XSSFSheet sheet = getOrCreateSheet(workbook, sheetName);
    
    if (createHeader) {
      prepareHeader(sheet);
    }
    for (IGetPropertyTranslationsModel iGetPropertyTranslationsModel : staticTranslation) {
      XSSFRow createRow = sheet.createRow(rowCounter++);
      
      XSSFCell createCell = createRow.createCell(0);
      createCell.setCellValue(entityType);
      
      XSSFCell createCellEnityId = createRow.createCell(1);
      createCellEnityId.setCellValue(iGetPropertyTranslationsModel.getId());
      
      XSSFCell createCellEnityCode = createRow.createCell(2);
      createCellEnityCode.setCellValue(iGetPropertyTranslationsModel.getCode());
      
      fillTheTranslationDependOnLanguage(iGetPropertyTranslationsModel, createRow);
    }
    
    FileOutputStream outputStream = new FileOutputStream(filePath);
    workbook.write(outputStream);
    outputStream.close();
    return rowCounter;
  }
  
  private void prepareHeader(XSSFSheet sheet)
  {
    Set<Integer> keySet = TRANSLATION_HEADER_LANGUAGE.keySet();
    XSSFRow createRowLanguage = sheet.createRow(0);
    for (Integer headerIndex : keySet) {
      XSSFCell createCell = createRowLanguage.createCell(headerIndex);
      createCell.setCellValue(TRANSLATION_HEADER_LANGUAGE.get(headerIndex));
      sheet.addMergedRegion(new CellRangeAddress(0, 0, headerIndex, headerIndex + 0));
    }
    
    XSSFRow createRow = sheet.createRow(1);
    int cellCount = 0;
    for (String headerColumn : TRANSLATION_HEADER) {
      XSSFCell createCell = createRow.createCell(cellCount++);
      createCell.setCellValue(headerColumn);
    }
  }
  
  private void fillTheTranslationDependOnLanguage(
      IGetPropertyTranslationsModel iGetPropertyTranslationsModel, XSSFRow createRow)
  {
    Map<String, IStandardTranslationModel> translations = iGetPropertyTranslationsModel
        .getTranslations();
    Set<String> keySet = translations.keySet();
    
    for (String languageKey : keySet) {
      
      Integer counterForLanguageKey = getCounterForLanguageKey(languageKey);
      IStandardTranslationModel iTranslationModel = translations.get(languageKey);
      
      XSSFCell createlabel = createRow.createCell(counterForLanguageKey);
      createlabel.setCellValue(iTranslationModel.getLabel());
      
      XSSFCell createDescription = createRow.createCell(counterForLanguageKey + 1);
      createDescription.setCellValue(iTranslationModel.getDescription());
      
      XSSFCell createPlaceholder = createRow.createCell(counterForLanguageKey + 2);
      createPlaceholder.setCellValue(iTranslationModel.getPlaceholder());
      
      XSSFCell createTooltip = createRow.createCell(counterForLanguageKey + 3);
      createTooltip.setCellValue(iTranslationModel.getTooltip());
    }
  }
  
  private Integer getCounterForLanguageKey(String languageKey)
  {
    switch (languageKey) {
      case "de_DE":
        return 11;
      
      case "en_UK":
        return 7;
      
      case "en_US":
        return 3;
      
      case "es_ES":
        return 19;
      
      case "fr_FR":
        return 15;
    }
    return null;
  }
}
