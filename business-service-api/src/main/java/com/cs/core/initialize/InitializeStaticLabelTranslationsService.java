package com.cs.core.initialize;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.translations.CreateStaticLabelTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ICreateStaticLabelTranslationsRequestModel;
import com.cs.core.config.strategy.usecase.translations.IGetOrCreateStaticLabelTranslationsStrategy;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@SuppressWarnings("unchecked")
public class InitializeStaticLabelTranslationsService implements IInitializeStaticLabelTranslationsService {
  
  @Autowired
  protected IGetOrCreateStaticLabelTranslationsStrategy getOrCreateStaticLabelTranslationsStrategy;
  
  @Autowired
  protected String                                      dictionaryFolderPath;
  
  @Override
  public void execute() throws Exception
  {
    getOrCreateStaticLabelsFromFiles();
  }
  
  private void getOrCreateStaticLabelsFromFiles() throws Exception
  {
    int lastPeriodPos;
    Map<String, Map<String, Object>> translationMap = new HashMap<>();
    
    ClassLoader cl = this.getClass()
        .getClassLoader();
    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
    Resource[] resources = resolver.getResources("classpath*:/UITranslations/*.*");
    for (Resource resource : resources) {
      File file = resource.getFile();
      String fileName = file.getName();
      lastPeriodPos = fileName.lastIndexOf('.');
      if (lastPeriodPos > 0) {
        fileName = fileName.substring(0, lastPeriodPos);
      }
      Integer _Index = fileName.indexOf(Seperators.FIELD_LANG_SEPERATOR);
      String[] splitFileName = fileName.split(Seperators.FIELD_LANG_SEPERATOR);
      getKeyValuePairFromJsonFile(file, translationMap, fileName.substring(_Index + 2),
          splitFileName[0]);
    }
    
    ICreateStaticLabelTranslationsRequestModel requestModel = new CreateStaticLabelTranslationsRequestModel();
    requestModel.setStaticTranslations(translationMap);
    getOrCreateStaticLabelTranslationsStrategy.execute(requestModel);
  }
  
  private void getKeyValuePairFromJsonFile(File file,
      Map<String, Map<String, Object>> translationMap, String suffix, String type)
      throws IOException
  {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    InputStream translationFileStream = new FileInputStream(file);
    Map<String, Object> translationFileMap = mapper.readValue(translationFileStream,
        new TypeReference<Map<String, Object>>()
        {
        });
    getStaticTranslationMap(translationMap, suffix, type, translationFileMap);
  }
  
  private void getStaticTranslationMap(Map<String, Map<String, Object>> translationMap,
      String suffix, String type, Map<String, Object> translationFileMap)
  {
    for (String labelKey : translationFileMap.keySet()) {
      Map<String, Object> translations = translationMap.get(labelKey);
      String translatedValue = (String) translationFileMap.get(labelKey);
      if (translations != null) {
        translations.put("label" + Seperators.FIELD_LANG_SEPERATOR + suffix, translatedValue);
        ((Set<String>) translations.get(CommonConstants.SCREENS)).add(type);
      }
      else {
        translations = new HashMap<>();
        translations.put("label" + Seperators.FIELD_LANG_SEPERATOR + suffix, translatedValue);
        Set<String> types = new HashSet<String>();
        types.add(type);
        translations.put(CommonConstants.SCREENS, types);
        translationMap.put(labelKey, translations);
      }
    }
  }
  
  /*private static void getKeyValuePairFromFile(File file,
      Map<String, Map<String, String>> translationMap, String suffix)
      throws FileNotFoundException, IOException
  {
    int rowCount = 0;
  
    FileInputStream fileInputStream = new FileInputStream(file);
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    XSSFSheet sheet = workbook.getSheetAt(0);
    Row headerRow = sheet.getRow(0);
    Integer lastRowNumber = sheet.getLastRowNum();
    lastRowNumber++;
    for (int i = 1; i < lastRowNumber; i++) {
      Row itemRow = sheet.getRow(i);
      if (itemRow != null && rowCount < 6) {
        rowCount = 0;
        Iterator<Cell> headerCellIterator = headerRow.cellIterator();
        while (headerCellIterator.hasNext()) {
          Cell keyCell = itemRow.getCell(0, Row.RETURN_BLANK_AS_NULL);
          Cell translatedValueCell = itemRow.getCell(1, Row.RETURN_BLANK_AS_NULL);
          String keyCellValue = getCellValueInString(keyCell);
          String translatedValue = getCellValueInString(translatedValueCell);
          if (keyCellValue != null && !keyCellValue.isEmpty() && translatedValue != null) {
            Map<String, String> translations = translationMap.get(keyCellValue);
            if (translations != null) {
              translations.put("label_" + suffix, translatedValue);
            }
            else {
              translations = new HashMap<>();
              translations.put("label_" + suffix, translatedValue);
              translationMap.put(keyCellValue, translations);
            }
          }
          headerCellIterator.next();
        }
      }
      else if (rowCount > 5) {
        break;
      }
      else {
        rowCount++;
      }
    }
  }
  
  private static String getCellValueInString(Cell cell)
  {
    String value = "";
    if (cell != null) {
      if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
        value = cell.getStringCellValue();
      }
    }
  
    return value.trim();
  }*/
}
