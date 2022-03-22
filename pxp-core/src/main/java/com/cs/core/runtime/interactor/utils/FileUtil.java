package com.cs.core.runtime.interactor.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import com.cs.core.rdbms.driver.RDBMSLogger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.exception.template.HeaderNotFoundException;
import com.cs.core.config.interactor.model.articleimportcomponent.IComponentParameterModel;
import com.cs.core.config.interactor.model.asset.IAssetServerDetailsModel;
import com.cs.core.config.interactor.model.assetstatus.AssetUploadStatusModel;
import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleTagMappingModel;
import com.cs.core.config.interactor.model.mapping.IGetTagValueFromColumnModel;
import com.cs.core.runtime.interactor.constants.application.OnboardingConstants;
import com.cs.core.runtime.interactor.constants.application.ProcessConstants;
import com.cs.core.runtime.interactor.exception.onboarding.notfound.RequiredFieldsNotFoundException;
import com.cs.core.runtime.interactor.exception.onboarding.notfound.SheetNotFoundException;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.fileinstance.CreateOnboardingFileInstanceModel;
import com.cs.core.runtime.interactor.model.fileinstance.ICreateOnboardingFileInstanceModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Service
@SuppressWarnings("unchecked")
public class FileUtil {
  
  @Autowired
  protected IAssetServerDetailsModel    assetServerDetails;
  
  @Resource(name = "assetUploadStatusMap")
  protected Map<String, String>         assetUploadStatusMap;
  
  /*@Autowired
  protected ISetAssetUploadStatusStrategy setAssetUploadStatusStrategy;*/
  
  /*@Autowired
  protected AssetUploadTaskExecutorImpl assetUploadTaskExecutorImpl;
  
  @Autowired
  protected BaseUploadAssetUtil         baseUploadAssetUtil;
  */
  @Autowired
  protected OnboardingUtils             onboardingUtils;
  
  @Autowired
  protected TransactionThreadData       transactionThread;
  
  @Autowired
  protected String                      hotFolderPath;
  
  @Autowired
  protected ISessionContext             context;
  
  // to add headers with specific position of row as per configuaration
  public static List<String> getHeaderAsList(File file, String sheetName, Integer headerRowNumber)
      throws FileNotFoundException, IOException, HeaderNotFoundException
  {
    FileInputStream fileInputStream = new FileInputStream(file);
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
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
      if (!getCellValueInString(cell).equals(""))
        headerAsList.add(getCellValueInString(cell));
    }
    fileInputStream.close();
    return headerAsList;
  }
  
  // to add headers with specific position of row as per configuaration
  public static List<String> getHeaderAsList(XSSFWorkbook workbook, String sheetName,
      Integer headerRowNumber) throws FileNotFoundException, IOException, HeaderNotFoundException
  {
    XSSFSheet sheet = getSheet(workbook, sheetName);
    XSSFRow headerRow = sheet.getRow(headerRowNumber - 1);
    if (headerRow == null) {
      throw new HeaderNotFoundException();
    }
    List<String> headerAsList = new ArrayList<>();
    Iterator<Cell> cellIterator = headerRow.iterator();
    while (cellIterator.hasNext()) {
      Cell cell = cellIterator.next();
      if (!getCellValueInString(cell).equals(""))
        headerAsList.add(getCellValueInString(cell));
    }
    
    return headerAsList;
  }
  
  // to read data with specific position of row as per configuaration
  public static List<Map<String, Object>> getKeyValuePairFromFile(File file, String sheetName,
      List<String> headersToRead, String idColumn, Integer dataRowNumber, Integer headerRowNumber)
      throws FileNotFoundException, IOException
  {
    int Rowf = 0;
    headersToRead.add(idColumn);
    Set<String> headers = new HashSet<>();
    headers.addAll(headersToRead);
    List<Map<String, Object>> listInFile = new ArrayList<>();
    FileInputStream fileInputStream = new FileInputStream(file);
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    XSSFSheet sheet = getSheet(workbook, sheetName);
    Integer lastRowNumber = sheet.getLastRowNum();
    lastRowNumber++;
    Row headerRow = sheet.getRow(headerRowNumber - 1);
    for (int i = dataRowNumber - 1; i < lastRowNumber; i++) {
      List<String> rowValues = new ArrayList<>();
      Boolean hasValue = false;
      Map<String, Object> item = new HashMap<>();
      Row itemRow = sheet.getRow(i);
      if (itemRow != null && Rowf < 6) {
        Rowf = 0;
        Iterator<Cell> headerCellIterator = headerRow.cellIterator();
        while (headerCellIterator.hasNext()) {
          Cell headerCell = headerCellIterator.next();
          String header = getCellValueInString(headerCell);
          if (!header.isEmpty() && headers.contains(header)) {
            Cell itemCell = itemRow.getCell(headerCell.getColumnIndex(), Row.RETURN_BLANK_AS_NULL);
            String cellValue = getCellValueInString(itemCell);
            rowValues.add(cellValue);
            item.put(header, cellValue);
            if (cellValue != null && !cellValue.isEmpty()) {
              hasValue = true;
            }
          }
        }
      }
      else if (Rowf > 5) {
        break;
      }
      else {
        Rowf++;
      }
      if (hasValue) {
        listInFile.add(item);
      }
    }
    fileInputStream.close();
    return listInFile;
  }
  
  public static Map<String, Object> getColumnValueFromFile(XSSFWorkbook workbook, String sheetName,
      List<String> classCoulmns, List<String> taxonomyCoulmns, Integer dataRowNumber,
      Integer headerRowNumber) throws FileNotFoundException, IOException, SheetNotFoundException
  {
    int Rowf = 0;
    XSSFSheet sheet = getSheet(workbook, sheetName);
    if (sheet == null) {
      throw new SheetNotFoundException();
    }
    Integer lastRowNumber = sheet.getLastRowNum();
    lastRowNumber++;
    Set<String> klassesFromSheet = new HashSet<>();
    Set<String> taxonomiesFromSheet = new HashSet<>();
    Row headerRow = sheet.getRow(headerRowNumber - 1);
    for (int i = dataRowNumber - 1; i < lastRowNumber; i++) {
      Row itemRow = sheet.getRow(i);
      if (itemRow != null && Rowf < 6) {
        Rowf = 0;
        Iterator<Cell> headerCellIterator = headerRow.cellIterator();
        while (headerCellIterator.hasNext()) {
          Cell headerCell = headerCellIterator.next();
          String header = getCellValueInString(headerCell);
          if (!header.isEmpty() && classCoulmns.contains(header)) {
            Cell itemCell = itemRow.getCell(headerCell.getColumnIndex(), Row.RETURN_BLANK_AS_NULL);
            String cellValue = getCellValueInString(itemCell);
            if (!cellValue.equals("")) {
              klassesFromSheet.addAll(OnboardingUtils.StringToList(cellValue));
            }
          }
          if (!header.isEmpty() && taxonomyCoulmns.contains(header)) {
            Cell itemCell = itemRow.getCell(headerCell.getColumnIndex(), Row.RETURN_BLANK_AS_NULL);
            String cellValue = getCellValueInString(itemCell);
            if (!cellValue.equals("")) {
              taxonomiesFromSheet.addAll(OnboardingUtils.StringToList(cellValue));
            }
          }
        }
      }
      else if (Rowf > 5) {
        break;
      }
      else {
        Rowf++;
      }
    }
    Map<String, Object> returnmap = new HashMap<>();
    returnmap.put(OnboardingConstants.KLASSES_FROM_SHEET, klassesFromSheet);
    returnmap.put(OnboardingConstants.TAXONOMIES_FROM_SHEET, taxonomiesFromSheet);
    return returnmap;
  }
  
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
  
  public static String getCellValueInString(Cell cell)
  {
    String value = "";
    if (cell != null) {
      if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
        if (HSSFDateUtil.isCellDateFormatted(cell)) {
          Date date = cell.getDateCellValue();
          SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
    
    return value.trim();
  }
  
  public static Map<String, Object> getMapForRelationshipFromFile(File file,
      List<String> headersToRead, Integer dataRowNumber, Integer headerRowNumber,
      IComponentParameterModel paramModel) throws FileNotFoundException, IOException
  {
    int Rowf = 0;
    Map<String, Object> mapToReturn = new HashMap<>();
    Set<String> relationshipCodes = new HashSet<>();
    mapToReturn.put(ProcessConstants.RELATIONSHIP_CODES, relationshipCodes);
    Set<String> contextCodes = new HashSet<>();
    mapToReturn.put(ProcessConstants.CONTEXT_CODES, contextCodes);
    String contextTags = paramModel.getContextTagsColumn();
    String fromDateColumnName = paramModel.getFromDateColumn();
    String toDateColumnName = paramModel.getToDateColumn();
    List<String> contextTagsList = OnboardingUtils.StringToList(contextTags);
    
    String sheetName = paramModel.getSheet();
    String idColumn = paramModel.getRelationshipSourceColumn();
    headersToRead.add(idColumn);
    Set<String> headers = new HashSet<>();
    headers.addAll(headersToRead);
    FileInputStream fileInputStream = new FileInputStream(file);
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    XSSFSheet sheet = getSheet(workbook, sheetName);
    Integer lastRowNumber = sheet.getLastRowNum();
    lastRowNumber++;
    Row headerRow = sheet.getRow(headerRowNumber - 1);
    for (int i = dataRowNumber - 1; i < lastRowNumber; i++) {
      Map<String, String> contextTagGroupIdVsTagValuesMap = new HashMap<>();
      Map<String, String> contextTimeRangeValueMap = new HashMap<>();
      String contextCode = null;
      Integer count = 1;
      List<String> rowValues = new ArrayList<>();
      Map<String, Object> item = new HashMap<>();
      Row itemRow = sheet.getRow(i);
      if (itemRow != null && Rowf < 6) {
        Rowf = 0;
        Iterator<Cell> headerCellIterator = headerRow.cellIterator();
        String destinationId = null;
        String relationshipCode = null;
        String sourceId = null;
        if (!paramModel.getIsTypeFromColumn()) {
          relationshipCode = paramModel.getRelationshipId();
        }
        while (headerCellIterator.hasNext()) {
          Cell headerCell = headerCellIterator.next();
          String header = getCellValueInString(headerCell);
          if (!header.isEmpty() && headers.contains(header)) {
            Cell itemCell = itemRow.getCell(headerCell.getColumnIndex(), Row.RETURN_BLANK_AS_NULL);
            String cellValue = getCellValueInString(itemCell);
            rowValues.add(cellValue);
            if (cellValue != null && !cellValue.isEmpty()) {
              
              if (header.equals(paramModel.getRelationshipDestinationColumn())) {
                destinationId = cellValue;
              }
              if (header.equals(paramModel.getRelationshipIdColumn())
                  && paramModel.getIsTypeFromColumn()) {
                relationshipCode = cellValue;
                relationshipCodes.add(relationshipCode);
              }
              if (header.equals(paramModel.getRelationshipSourceColumn())) {
                sourceId = cellValue;
              }
              else if (header.equals(paramModel.getContextIdColumn())) {
                contextCode = cellValue;
                contextCodes.add(contextCode);
              }
              else if (contextTagsList.contains(header)) {
                contextTagGroupIdVsTagValuesMap.put(header, cellValue);
              }
              else if (header.equals(fromDateColumnName) || header.equals(toDateColumnName)) {
                contextTimeRangeValueMap.put(header, cellValue);
              }
              item.put(header, cellValue);
            }
          }
        }
        Map<String, Object> relationshipIdToDestinationIdsMap = (Map<String, Object>) mapToReturn
            .get(sourceId);
        Map<String, Object> contextsMap = new HashMap<>();
        Map<String, Object> contextMap = null;
        if (contextCode != null && !contextCode.isEmpty()) {
          contextMap = new HashMap<>();
          contextMap.put(ProcessConstants.CONTEXT_CODE, contextCode);
          contextMap.put(ProcessConstants.CONTEXT_TAGS_MAP, contextTagGroupIdVsTagValuesMap);
          contextMap.put(ProcessConstants.CONTEXT_TIME_RANGE_MAP, contextTimeRangeValueMap);
        }
        contextsMap.put(ProcessConstants.CONTEXT, contextMap);
        contextsMap.put(ProcessConstants.COUNT, count);
        
        if (relationshipIdToDestinationIdsMap == null && sourceId != null) {
          relationshipIdToDestinationIdsMap = new HashMap<>();
          Map<String, Object> destinationIdsVsContextMap = new HashMap<>();
          destinationIdsVsContextMap.put(destinationId, contextsMap);
          relationshipIdToDestinationIdsMap.put(relationshipCode, destinationIdsVsContextMap);
          mapToReturn.put(sourceId, relationshipIdToDestinationIdsMap);
          
        }
        else if (sourceId != null) {
          Map<String, Object> destinationIdsVsContextMap = (Map<String, Object>) relationshipIdToDestinationIdsMap
              .get(relationshipCode);
          if (destinationIdsVsContextMap == null) {
            destinationIdsVsContextMap = new HashMap<>();
            relationshipIdToDestinationIdsMap.put(relationshipCode, destinationIdsVsContextMap);
          }
          destinationIdsVsContextMap.put(destinationId, contextsMap);
        }
      }
      else if (Rowf > 5) {
        break;
      }
      else {
        Rowf++;
      }
    }
    if (!paramModel.getIsTypeFromColumn()) {
      relationshipCodes.add(paramModel.getRelationshipId()); // Adding
                                                             // Relationship ID
                                                             // here
    }
    fileInputStream.close();
    return mapToReturn;
  }
  
  public static Map<String, Object> getMapForNatureRelationshipFromFile(File file,
      List<String> headersToRead, Integer dataRowNumber, Integer headerRowNumber,
      IComponentParameterModel paramModel) throws FileNotFoundException, IOException
  {
    int Rowf = 0;
    Map<String, Object> mapToReturn = new HashMap<>();
    Set<String> relationshipCodes = new HashSet<>();
    mapToReturn.put(ProcessConstants.RELATIONSHIP_CODES, relationshipCodes);
    Set<String> contextCodes = new HashSet<>();
    mapToReturn.put(ProcessConstants.CONTEXT_CODES, contextCodes);
    
    String contextTags = paramModel.getContextTagsColumn();
    String fromDateColumnName = paramModel.getFromDateColumn();
    String toDateColumnName = paramModel.getToDateColumn();
    List<String> contextTagsList = OnboardingUtils.StringToList(contextTags);
    
    String sheetName = paramModel.getSheet();
    String idColumn = paramModel.getRelationshipSourceColumn();
    headersToRead.add(idColumn);
    Set<String> headers = new HashSet<>();
    headers.addAll(headersToRead);
    FileInputStream fileInputStream = new FileInputStream(file);
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    XSSFSheet sheet = getSheet(workbook, sheetName);
    Integer lastRowNumber = sheet.getLastRowNum();
    lastRowNumber++;
    Row headerRow = sheet.getRow(headerRowNumber - 1);
    for (int i = dataRowNumber - 1; i < lastRowNumber; i++) {
      Map<String, String> contextTagGroupIdVsTagValuesMap = new HashMap<>();
      Map<String, String> contextTimeRangeValueMap = new HashMap<>();
      Row itemRow = sheet.getRow(i);
      if (itemRow != null && Rowf < 6) {
        Rowf = 0;
        Iterator<Cell> headerCellIterator = headerRow.cellIterator();
        Iterator<Cell> itemCellIterator = itemRow.cellIterator();
        String destinationId = null;
        String relationshipCode = null;
        String sourceId = null;
        String contextCode = null;
        Integer count = 1;
        if (!paramModel.getIsTypeFromColumn()) {
          relationshipCode = paramModel.getRelationshipId();
        }
        while (headerCellIterator.hasNext()) {
          Cell headerCell = headerCellIterator.next();
          if (itemCellIterator.hasNext()) {
            Cell itemCell = itemCellIterator.next();
            String header = getCellValueInString(headerCell);
            if (!header.equals("")) {
              if (headers.contains(header)) {
                String cellValue = getCellValueInString(itemCell);
                if (cellValue != null && !cellValue.equals("")) {
                  
                  if (header.equals(ProcessConstants.COUNT)) {
                    try {
                      count = Integer.parseInt(cellValue);
                    }
                    catch (NumberFormatException e) {
                      RDBMSLogger.instance().exception(e);
                    }
                  }
                  if (header.equals(paramModel.getRelationshipDestinationColumn())) {
                    destinationId = cellValue;
                  }
                  else if (header.equals(paramModel.getRelationshipIdColumn())
                      && paramModel.getIsTypeFromColumn()) {
                    relationshipCode = cellValue;
                    relationshipCodes.add(relationshipCode);
                  }
                  else if (header.equals(paramModel.getRelationshipSourceColumn())) {
                    sourceId = cellValue;
                    
                  }
                  else if (header.equals(paramModel.getContextIdColumn())) {
                    contextCode = cellValue;
                    contextCodes.add(contextCode);
                  }
                  else if (contextTagsList.contains(header)) {
                    contextTagGroupIdVsTagValuesMap.put(header, cellValue);
                  }
                  else if (header.equals(fromDateColumnName) || header.equals(toDateColumnName)) {
                    contextTimeRangeValueMap.put(header, cellValue);
                  }
                }
              }
            }
          }
        }
        Map<String, Object> relationshipIdToDestinationIdsMap = (Map<String, Object>) mapToReturn
            .get(sourceId);
        Map<String, Object> contextsMap = new HashMap<>();
        Map<String, Object> contextMap = null;
        if (contextCode != null && !contextCode.isEmpty()) {
          contextMap = new HashMap<>();
          contextMap.put(ProcessConstants.CONTEXT_CODE, contextCode);
          contextMap.put(ProcessConstants.CONTEXT_TAGS_MAP, contextTagGroupIdVsTagValuesMap);
          contextMap.put(ProcessConstants.CONTEXT_TIME_RANGE_MAP, contextTimeRangeValueMap);
        }
        contextsMap.put(ProcessConstants.CONTEXT, contextMap);
        contextsMap.put(ProcessConstants.COUNT, count);
        
        if (relationshipIdToDestinationIdsMap == null && sourceId != null) {
          relationshipIdToDestinationIdsMap = new HashMap<>();
          Map<String, Object> destinationIdToContextsMap = new HashMap<>();
          destinationIdToContextsMap.put(destinationId, contextsMap);
          relationshipIdToDestinationIdsMap.put(relationshipCode, destinationIdToContextsMap);
          mapToReturn.put(sourceId, relationshipIdToDestinationIdsMap);
        }
        else if (sourceId != null) {
          Map<String, Object> destinationIdToCountMap = (Map<String, Object>) relationshipIdToDestinationIdsMap
              .get(relationshipCode);
          if (destinationIdToCountMap == null) {
            destinationIdToCountMap = new HashMap<>();
            relationshipIdToDestinationIdsMap.put(relationshipCode, destinationIdToCountMap);
          }
          destinationIdToCountMap.put(destinationId, contextsMap);
        }
      }
      else {
        if (Rowf > 5) {
          break;
        }
        else {
          Rowf++;
        }
      }
    }
    fileInputStream.close();
    return mapToReturn;
  }
  
  public static List<String> getRuntimeMappingsFromFile(XSSFWorkbook workbook,
      Set<String> tagColumnNames, Map<String, IConfigRuleTagMappingModel> tagMappings,
      List<String> sheetsFromProcess)
      throws FileNotFoundException, IOException, HeaderNotFoundException
  {
    Map<String, List<String>> headerOfAllSheets = new HashMap<>();
    List<String> headerAsList = new ArrayList<>();
    for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
      List<String> heade1rAsList = new ArrayList<>();
      String sheetName = workbook.getSheetName(i);
      if (sheetsFromProcess.contains(sheetName)) {
        XSSFSheet sheet = getSheet(workbook, sheetName);
        XSSFRow row = sheet.getRow(0);
        if (row == null) {
          continue;
        }
        
        heade1rAsList.addAll(getHeaderAsList(workbook, sheetName, 1));
        headerOfAllSheets.put(sheetName, heade1rAsList);
        headerAsList.addAll(heade1rAsList);
      }
    }
    /*
    for (String sheetName : headerOfAllSheets.keySet()) {
      for (String tagColumnName : tagColumnNames) {
        if (headerOfAllSheets.get(sheetName)
            .contains(tagColumnName)) {
          Set<String> mappedTagValues = new HashSet<>();
          XSSFSheet sheet = getSheet(workbook, sheetName);
          XSSFRow row = sheet.getRow(0);
          if(row == null)
            continue;
          Iterator<Cell> headerCellIterator = row.cellIterator();
          int headerCellNumber = 0;
          while (headerCellIterator.hasNext()) {
            Cell headerCell = headerCellIterator.next();
            String header = getCellValueInString(headerCell);
            if (header.equals(tagColumnName)) {
              headerCellNumber = headerCell.getColumnIndex();
              List<IColumnValueTagValueMappingModel> tagValueMappings = tagMappings
                  .get(tagColumnName)
                  .getTagValueMappings();
              for (IColumnValueTagValueMappingModel tagValueMapping : tagValueMappings) {
                for (ITagValueMappingModel mapping : tagValueMapping.getMappings()) {
                  mappedTagValues.add(mapping.getTagValue());
                }
              }
              break;
            }
          }
    
          Set<String> tagValuesFromSheet = new HashSet<>();
          Integer lastRowNumber = sheet.getLastRowNum();
          lastRowNumber++;
          int rowCounter = 0;
          for (int i = 1; i < lastRowNumber; i++) {
            Row itemRow = sheet.getRow(i);
            if(itemRow != null && rowCounter < 6)
            {
              rowCounter = 0;
              Cell cell = itemRow.getCell(headerCellNumber);
              String cellValue = FileUtil.getCellValueInString(cell);
              if (!cellValue.equals("")) {
                tagValuesFromSheet.addAll(OnboardingUtils.StringToList(cellValue));
            }
           }
            else
            {
              if(rowCounter > 5)
              {
                break;
              }
              else
              {
                rowCounter++;
              }
            }
          }
          tagValuesFromSheet.removeAll(mappedTagValues);
          for (String unmappedTagValue : tagValuesFromSheet) {
            ITagValueMappingModel tagMapping = new TagValueMappingModel();
            tagMapping.setId(UUID.randomUUID().toString());
            tagMapping.setTagValue(unmappedTagValue);
            List<String> UnmappedmappedTagValueId = tagMappings.get(tagColumnName).getTagValueMappings().get(0).getTagValueIds();
            tagMapping.setMappedTagValueId(UnmappedmappedTagValueId.contains(unmappedTagValue)?unmappedTagValue:"");
            tagMappings
            .get(tagColumnName)
            .getTagValueMappings().get(0).getMappings().add(tagMapping);
          }
        }
      }
    }
    */
    return headerAsList;
  }
  
  public static Boolean isSheetAvailable(String sheet, File file) throws Exception
  {
    List<String> availableSheets = new ArrayList<>();
    FileInputStream fileInputStream = new FileInputStream(file);
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
      availableSheets.add(workbook.getSheetName(i));
    }
    if (!availableSheets.contains(sheet)) {
      throw new SheetNotFoundException("Sheet Not Found");
    }
    fileInputStream.close();
    return true;
  }
  
  public static Boolean isRequiredFieldsAvailable(List<String> requiredFields,
      List<String> fileHeaders) throws RequiredFieldsNotFoundException
  {
    // Removing empty String from Required Fields for comparison between
    // Required Fields and File
    // Headers
    requiredFields.removeAll(Arrays.asList(null, ""));
    
    if (!fileHeaders.containsAll(requiredFields)) {
      throw new RequiredFieldsNotFoundException();
    }
    
    return true;
  }
  
  public static List<String> getFileHeaders(String filePath, List<String> sheetsList)
      throws IOException, HeaderNotFoundException
  {
    File file = new File(filePath);
    FileInputStream fileInputStream = new FileInputStream(file);
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    Set<String> headerAsList = new HashSet<>();
    for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
      String sheetName = workbook.getSheetName(i);
      if (sheetsList.contains(sheetName)) {
        XSSFSheet sheet = getSheet(workbook, sheetName);
        XSSFRow headerRow = sheet.getRow(0);
        if (headerRow == null) {
          continue;
        }
        headerAsList.addAll(getHeaderAsList(workbook, sheetName, 1));
      }
    }
    fileInputStream.close();
    return new ArrayList<String>(headerAsList);
  }
  
  public Set<String> getTagValuesFromColumn(XSSFWorkbook workbook,
      IGetTagValueFromColumnModel model) throws Exception
  {
    String tagColumnName = model.getColumnName();
    Integer dataRowNumber = model.getDataRowNumber();
    Integer headerRowNumber = model.getHeaderRowNumber();
    dataRowNumber = 2;
    headerRowNumber = 1;
    model.setHeaderRowNumber(1);
    model.setDataRowNumber(2);
    
    TransactionData transactionData = transactionThread.getTransactionData();
    String endpointId = transactionData.getEndpointId();
    
    if (model.getSheetsFromProcess() == null) {
      List<String> sheetsFromProcess = new ArrayList<>();
      onboardingUtils.getKlassAndTaxonomyColumnInfoFromFile(workbook, endpointId,
          sheetsFromProcess);
      model.setSheetsFromProcess(sheetsFromProcess);
    }
    /*if (dataRowNumber == null && headerRowNumber == null) {
      Map<String, Object> flow = onboardingUtils.getRespectivePrcessFlow(endpointId);
      if (flow != null) {
        for (String componentInstanceId : flow.keySet()) {
          Map<String, Object> componentConfig = (Map<String, Object>) flow.get(componentInstanceId);
          Map<String, Object> parameters = (Map<String, Object>) componentConfig
              .get(Constants.PARAMETERS);
          List<Map<String, Object>> dataSources = (List<Map<String, Object>>) parameters
              .get(Constants.DATASOURCES);
          if (dataSources.size() > 0) {
            Map<String, Object> dataSource = dataSources.get(0);
            String sheet = (String) dataSource.get(IComponentParameterModel.SHEET);
            if (sheet != null && !sheet.isEmpty()) {
              headerRowNumber = (Integer) dataSource
                  .get(IComponentParameterModel.HEADER_ROW_NUMBER);
              model.setHeaderRowNumber(headerRowNumber);
               dataRowNumber = (Integer) dataSource.get(IComponentParameterModel.DATA_ROW_NUMBER);
               model.setDataRowNumber(dataRowNumber);
              break;
            }
          }
        }
        //Call came from controller
        if (model.getSheetsFromProcess() == null) {
          List<String> sheetsFromProcess = new ArrayList<>();
          onboardingUtils
              .getKlassAndTaxonomyColumnInfoFromFile(file, flow, sheetsFromProcess);
          model.setSheetsFromProcess(sheetsFromProcess);
        }
      }
    }*/
    Map<String, List<String>> headerOfAllSheets = new HashMap<>();
    
    for (String sheet : model.getSheetsFromProcess()) {
      List<String> headerAsList = new ArrayList<>();
      headerAsList.addAll(getHeaderAsList(workbook, sheet, model.getHeaderRowNumber()));
      headerOfAllSheets.put(sheet, headerAsList);
    }
    
    Set<String> tagValuesFromSheet = new HashSet<>();
    for (String sheetName : headerOfAllSheets.keySet()) {
      if (headerOfAllSheets.get(sheetName)
          .contains(tagColumnName)) {
        XSSFSheet sheet = getSheet(workbook, sheetName);
        XSSFRow row = sheet.getRow(headerRowNumber - 1);
        Iterator<Cell> headerCellIterator = row.cellIterator();
        int headerCellNumber = 0;
        while (headerCellIterator.hasNext()) {
          Cell headerCell = headerCellIterator.next();
          String header = getCellValueInString(headerCell);
          if (header.equals(tagColumnName)) {
            headerCellNumber = headerCell.getColumnIndex();
            break;
          }
        }
        
        Integer lastRowNumber = sheet.getLastRowNum();
        lastRowNumber++;
        for (int i = dataRowNumber - 1; i < lastRowNumber; i++) {
          Row itemRow = sheet.getRow(i);
          // TODO Fix Me
          if (itemRow == null) {
            continue;
          }
          Cell cell = itemRow.getCell(headerCellNumber);
          // String cellValue = cell.getStringCellValue();
          String cellValue = "";
          cellValue = getCellValueInString(cell);
          if (!cellValue.equals("") || !cellValue.equals(" ")) {
            tagValuesFromSheet.addAll(OnboardingUtils.StringToList(cellValue));
          }
        }
      }
    }
    return tagValuesFromSheet;
  }
  
  public void deleteFileInstanceFromLocalStorage(String fileId)
      throws NullPointerException, IOException, FileNotFoundException
  {
    File file = new File(fileId);
    FileUtils.forceDelete(file);
  }
  
  /* public void uploadTheDocument(IAssetFileModel assetFileModel, String assetKey) throws Exception
  {
    IAssetUploadDataModel originalUploadDataModel = new AssetUploadDataModel();
    originalUploadDataModel.setStorageUrl(assetServerDetails.getStorageURL());
    originalUploadDataModel.setContainer(CommonConstants.SWIFT_CONTAINER_DOCUMENT);
    originalUploadDataModel.setAssetBytes(assetFileModel.getBytes());
    originalUploadDataModel.setAssetKey(assetKey);
    
    Map<String, String> docDataMap = new HashMap<>();
    docDataMap.put(BaseUploadAssetUtil.REQUEST_HEADER_AUTH_TOKEN,
        assetServerDetails.getAuthToken());
    docDataMap.put(BaseUploadAssetUtil.REQUEST_HEADER_OBJECT_META_NAME,
        assetFileModel.getName() + OffboardingConstants.XLSX_FILE_EXTENSION);
    docDataMap.put(BaseUploadAssetUtil.REQUEST_HEADER_OBJECT_META_FORMAT,
        OffboardingConstants.XLSX_FILE_EXTENSION);
    String originalContentType = BaseUploadAssetUtil.CONTENT_TYPE_APPLICATION
        + OffboardingConstants.XLSX_FILE_EXTENSION;
    docDataMap.put(BaseUploadAssetUtil.REQUEST_HEADER_OBJECT_META_CONTENT_TYPE,
        originalContentType);
    docDataMap.put(BaseUploadAssetUtil.REQUEST_HEADER_CONTENT_TYPE, originalContentType);
    docDataMap.put(BaseUploadAssetUtil.REQUEST_HEADER_OBJECT_META_TYPE,
        BaseUploadAssetUtil.TYPE_ORIGINAL);
    originalUploadDataModel.setAssetDataMap(docDataMap);
    
    // setAssetUploadStatus(assetKey);
    File temporayFile = new File(assetFileModel.getPath());
    assetUploadTaskExecutorImpl.uploadAsset(temporayFile.getAbsolutePath(),
        originalUploadDataModel);
    System.out.println("\n\n\n >>>>>>" + assetKey);
  }*/
  
  private void setAssetUploadStatus(String assetKey) throws Exception
  {
    assetUploadStatusMap.put(assetKey, "0");
    IAssetUploadStatusModel assetUploadStatus = new AssetUploadStatusModel();
    assetUploadStatus.setId(assetKey);
    assetUploadStatus.setProgress("0");
    assetUploadStatus.setStatus("InProgress");
    // setAssetUploadStatusStrategy.execute(assetUploadStatus);
  }
  
  /*public IAssetFileModel getAssetFileModel(String filePath)
      throws FileNotFoundException, IOException
  {
    IAssetFileModel assetFileModel = null;
    if (filePath != null) {
      File file = new File(filePath);
      String absoluteFilePath = file.getAbsolutePath();
      Path path = Paths.get(absoluteFilePath);
      byte[] bytes = Files.readAllBytes(path);
      
      assetFileModel = new AssetFileModel(CommonConstants.SWIFT_CONTAINER_DOCUMENT, bytes,
          file.getName(), OffboardingConstants.XLSX_FILE_EXTENSION, absoluteFilePath, null, null,
          null, null, null, null);
      
      File writeAssetFileOnServer = baseUploadAssetUtil.writeAssetFileOnServer(file.getName());
      
      String sourcePath = writeAssetFileOnServer.getAbsolutePath();
      FileOutputStream fos = new FileOutputStream(writeAssetFileOnServer);
      fos.write(bytes);
      fos.close();
      assetFileModel.setPath(sourcePath);
    }
    return assetFileModel;
  }*/
  
  /* public void uploadFileToSwiftContainer(String assetkey, String filePath) throws Exception
  {
    IAssetFileModel assetFileModel = getAssetFileModel(filePath);
    uploadTheDocument(assetFileModel, assetkey);
  }
  */
  public String storeFileInDropZoneForTalendExport(MultipartHttpServletRequest file)
      throws IOException, FileNotFoundException
  {
    File serverFile = null;
    String fileName = "";
    for (Entry<String, MultipartFile> entry : file.getFileMap()
        .entrySet()) {
      
      byte[] bytes = entry.getValue()
          .getBytes();
      String originalName = entry.getValue()
          .getOriginalFilename();
      String baseName = FilenameUtils.getBaseName(originalName);
      String orignalFileName = baseName + getLocalDateAndTimeAsString() + "."
          + FilenameUtils.getExtension(originalName);
      String path = new File(hotFolderPath).getAbsoluteFile()
          .getPath();
      fileName = path + "//" + orignalFileName;
      serverFile = new File(fileName);
      BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
      stream.write(bytes);
      stream.close();
    }
    
    return fileName;
  }
  
  private String getLocalDateAndTimeAsString()
  {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    LocalDateTime now = LocalDateTime.now();
    return dtf.format(now);
  }
  
  public ICreateOnboardingFileInstanceModel createFileInstanceModel(String fileName) throws Exception
  {
    ICreateOnboardingFileInstanceModel fileInstanceModel = new CreateOnboardingFileInstanceModel();
    fileInstanceModel.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RANDOM.getPrefix()));
    fileInstanceModel.setUserId(context.getUserId());
    fileInstanceModel.setFile(new File(fileName));
    fileInstanceModel.setName(fileName);
    return fileInstanceModel;
  }
  
  public void createTheTransactionDataForTalendImportExport(String endpointId)
  {
    TransactionData data = new TransactionData();
    data.setEndpointId(endpointId);
    transactionThread.setTransactionData(data);
  }
  
  public void deleteFileFromServer(String fileName)
  {
    File file = new File(fileName);
    file.deleteOnExit();
  }
}
