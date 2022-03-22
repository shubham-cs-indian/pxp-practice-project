package com.cs.di.workflow.tasks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.usecase.tag.IGetAllAssetExtensions;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.di.runtime.utils.DiFileUtils;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.DiConstants;
import com.cs.di.workflow.constants.DiDataType;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * This task simply receives the files and pass on to the next task.
 *
 * 2 ways to receive the files. 1.JMS 2.HOTFOLDER. 2 types of formats. 1.json
 * 2.excel..
 *
 * @author sopan.talekar
 *
 */
@Component("receiverTask")
public class ReceiverTask extends AbstractTask {
  
  @Autowired
  IGetAllAssetExtensions                 getAllAssetExtensions;
  
  private static final String            EXCEPTION_MESSAGE          = "Some of the required parameters are not provided. Please check execution status for details";
  
  public static final String             INPUT_METHOD               = "INPUT_METHOD";
  
  // For JMS
  public static final String             CLASS_PATH_IP              = "CLASS_PATH_IP";
  public static final String             PORT                       = "PORT";
  public static final String             QUEUE_NAME                 = "QUEUE_NAME";
  public static final String             ACKNOWLEDGEMENT_QUEUE_NAME = "ACKNOWLEDGEMENT_QUEUE_NAME";
  public static final String             ACKNOWLEDGEMENT            = "ACKNOWLEDGEMENT";
  
  // For Hot folder
  public static final String             HOT_FOLDER_PATH            = "HOT_FOLDER_PATH";
  public static final String             ALLOW_SUB_FOLDER_ACCESS    = "ALLOW_SUB_FOLDER_ACCESS";
  public static final String             ARCHIVAL_PATH              = "ARCHIVAL_PATH";
  public static final String             ERROR_PATH                 = "ERROR_PATH";
  
  // common
  public static final String             RECEIVER_MESSAGE_TYPE_LIST = "RECEIVER_MESSAGE_TYPE_LIST";
  
  // output parameters
  public static final String             RECEIVED_DATA              = "RECEIVED_DATA";
  public static final String             JMS_DATA                   = "JMS_DATA";
  
  public static final String             ALLEXTENSIONS              = "allExtensions";
  public static final List<String>       INPUT_LIST                 = Arrays.asList(INPUT_METHOD, CLASS_PATH_IP, PORT, QUEUE_NAME,
      ACKNOWLEDGEMENT, ACKNOWLEDGEMENT_QUEUE_NAME, HOT_FOLDER_PATH, ALLOW_SUB_FOLDER_ACCESS, ARCHIVAL_PATH, ERROR_PATH,
      RECEIVER_MESSAGE_TYPE_LIST, JMS_DATA);
  
  public static final List<String>       OUTPUT_LIST                = Arrays.asList(RECEIVED_DATA, EXECUTION_STATUS);
  public static final List<WorkflowType> WORKFLOW_TYPES             = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  public static final List<EventType>    EVENT_TYPES                = Arrays.asList(EventType.BUSINESS_PROCESS);
  
  @Override
  @SuppressWarnings("unchecked")
  public void executeTask(WorkflowTaskModel model)
  {
    Map<String, Object> receivedDataMap = new HashMap<>();
    List<String> allowedDataTypes = (List<String>) DiValidationUtil.validateAndGetRequiredCollection(model, RECEIVER_MESSAGE_TYPE_LIST);
    List<DiDataType> allowedDiDataTypes = allowedDataTypes.stream().map(dataType -> Enum.valueOf(DiDataType.class, dataType))
        .collect(Collectors.toList());
    String inputMethod = DiValidationUtil.validateAndGetRequiredString(model, INPUT_METHOD);
    try {
      switch (inputMethod) {
        case DiConstants.JMS:
          readJms(model, allowedDiDataTypes, receivedDataMap);
          break;
        
        case DiConstants.HOTFOLDER:
          readHotfolder(model, allowedDiDataTypes, receivedDataMap);
          break;
      }
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
    if (!model.getExecutionStatusTable().isErrorOccurred()) {
      if (receivedDataMap.isEmpty()) {
        model.getExecutionStatusTable().addWarning(MessageCode.GEN002);
      }
      
      model.getOutputParameters().put(RECEIVED_DATA, receivedDataMap);
      model.getOutputParameters().put(EXECUTION_STATUS, model.getExecutionStatusTable());
    }
  }
  
  /**
   * Read the jms message and seperate by the message type
   *
   * @param model
   * @param messageTypes
   * @param outputMap
   * @throws Exception
   * @throws JsonProcessingException
   */
  void readJms(WorkflowTaskModel model, List<DiDataType> messageTypes, Map<String, Object> outputMap)
      throws Exception, JsonProcessingException
  {
    int successCount = 0;
    Map<?, ?> jmsData = (Map<?, ?>) DiValidationUtil.validateAndGetRequiredMap(model, JMS_DATA);
    if (model.getExecutionStatusTable().isErrorOccurred()) {
      throw new Exception(EXCEPTION_MESSAGE);
    }
    Map<?, ?> body = ObjectMapperUtil.readValue((String) jmsData.get(DiConstants.BODY), Map.class);
    List<?> messageList = (List<?>) body.get(DiConstants.JMS_INPUT);
    
    for (Object object : messageList) {
      Map<?, ?> message = (Map<?, ?>) object;
      String extension = (String) message.get(DiConstants.MESSAGE_TYPE);
      DiDataType extensionDataType = DiDataType.getDiDataTypeForExtension(extension);
      if (messageTypes.contains(extensionDataType) && extensionDataType == DiDataType.JSON) {
        Map<String, Object> jsonDataMap = new HashMap<>();
        addMessage(jsonDataMap, message, extension);
        outputMap.put(DiDataType.JSON.name(), jsonDataMap);
        successCount = successCount + 1;
      }
      else if (messageTypes.contains(extensionDataType) && extensionDataType == DiDataType.EXCEL) {
        Map<String, Object> excelDataMap = new HashMap<>();
        addMessage(excelDataMap, message, extension);
        outputMap.put(DiDataType.EXCEL.name(), excelDataMap);
        successCount = successCount + 1;
      }
      else {
        model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN001,
            new String[] { extension });
      }
    } 
    model.getExecutionStatusTable().addSummary(new ObjectCode[] { ObjectCode.TOTAL_COUNT, ObjectCode.SUCCESS_COUNT }, MessageCode.GEN000,
        new String[] { String.valueOf(messageList.isEmpty() ? 0 : messageList.size()), String.valueOf(successCount)});
  }
  
  /**
   *
   * @param dataMap
   * @param inputMessage
   * @param extension
   * @throws JsonProcessingException
   */
  private void addMessage(Map<String, Object> dataMap, Map<?, ?> inputMessage, String extension) throws JsonProcessingException
  {
    int fileCounter = 1;
    List<?> messages = (List<?>) inputMessage.get(DiConstants.MESSAGE);
    for (Object message : messages) {
      dataMap.put(fileCounter + "." + extension, ObjectMapperUtil.writeValueAsString(message));
      fileCounter += 1;
    }
  }
  
  /**
   * Read the files from given folder path. After reading the file move to the
   * given archival folder. If file is invalid move to the error Folder path.
   * 
   * @param model
   * @param messageTypes
   * @param outputMap
   * @throws Exception
   * @throws IOException
   */
  void readHotfolder(WorkflowTaskModel model, List<DiDataType> messageTypes, Map<String, Object> outputMap) throws Exception, IOException
  {
    Properties properties = new Properties();
    properties.load(getClass().getClassLoader().getResourceAsStream("diconfig.properties"));
    String path = (String) DiValidationUtil.validateAndGetRequiredFolder(model, HOT_FOLDER_PATH);
    String archivalPath = getOrCreateProcessingPath(model, DiValidationUtil.validateAndGetOptionalString(model, ARCHIVAL_PATH));
    String errorPath = getOrCreateProcessingPath(model, DiValidationUtil.validateAndGetOptionalString(model, ERROR_PATH));
    Boolean allowSubfolderAccess = DiValidationUtil.validateAndGetOptionalBoolean(model, ALLOW_SUB_FOLDER_ACCESS);
    
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = model.getExecutionStatusTable();
    if (executionStatusTable.isErrorOccurred()) {
      return;
    }
    List<File> files = DiFileUtils.readFilesFromPath(path, allowSubfolderAccess, null);
    if (files.isEmpty()) {
      executionStatusTable.addWarning(MessageCode.GEN002);
    }
    DiDataType messageType = messageTypes.get(0);
    List<String> allExtensions = getSupportedExtentions(messageType, getAllAssetExtensions);
    
    for (File file : files) {
      final String fileName = file.getName();
      String extension = FilenameUtils.getExtension(fileName);
      extension = extension != null ? extension.toLowerCase() : "";
      if (!(allExtensions.contains(extension) || allExtensions.contains("." + extension))) {
        processDefault(errorPath, executionStatusTable, file, extension);
        continue;
      }
      
      switch (messageType) {
        case JSON:
          processJSONFile(archivalPath, outputMap, file, fileName);
          break;
        
        case EXCEL:
          processExcelFile(archivalPath, outputMap, file, fileName);
          break;
        
        case ASSET:
          processAssetFile(archivalPath, executionStatusTable, outputMap, getOrCreateProcessingTempFolder(model), file);
          break;
        
        case UNSUPPORTED:
          processDefault(errorPath, executionStatusTable, file, extension);
          break;
      }
    }
    
    model.getExecutionStatusTable().addSummary(new ObjectCode[] { ObjectCode.TOTAL_COUNT, ObjectCode.SUCCESS_COUNT }, MessageCode.GEN000,
        new String[] { String.valueOf(String.valueOf(files.isEmpty() ? 0 : files.size())), String.valueOf(outputMap.size())});
  }


  /**
   * Fetch supported extension for asset type
   * 
   * @param getAllAssetExtensions
   * @return List of extensions supported by asset
   * @throws Exception
   * 
   * @author bhagwat.bade
   */
  public List<String> getSupportedExtentions(DiDataType messageType, IGetAllAssetExtensions getAllAssetExtensions) throws Exception
  {
    if (messageType.isDynamic()) {
      switch (messageType) {
        case ASSET:
          Map<String, List<String>> assetExtensions = getAllAssetExtensions.execute(new IdsListParameterModel()).getAssetExtensions();
          return assetExtensions.get(ALLEXTENSIONS);
        default:
          return messageType.getSupportedExtentions();
      }
    }
    else {
      return messageType.getSupportedExtentions();
    }
  }
  
  /**
   * this method will move file to error directory if file is not supported.
   * 
   * @param errorPath
   * @param executionStatusTable
   * @param file
   * @param extension
   */
  private void processDefault(String errorPath, IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable, File file,
      String extension)
  {
    if (errorPath == null) {
      file.delete();
    }
    else {
      DiFileUtils.moveFile(errorPath, file);
    }
    executionStatusTable.addWarning(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN001, new String[] { extension });
  }
  
  /**
   * this method will copy file in temp directory and move it to archive
   * 
   * @param archivalPath
   * @param executionStatusTable
   * @param assetDataMap
   * @param assetProcessingPath
   * @param file
   */
  private void processAssetFile(String archivalPath, IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable,
      Map<String, Object> assetDataMap, String assetProcessingPath, File file)
  {
    String assetId = getAssetInstanceId(file, executionStatusTable);
    if (assetDataMap.containsKey(assetId)) {
      executionStatusTable.addInformation(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN012,
          new String[] { "file Name already Processed." });
      ;
      return;
    }
    
    File tempFile = new File(assetProcessingPath);
    assetDataMap.put(assetId, tempFile.getPath() + File.separator + file.getName());
    DiFileUtils.copyFile(tempFile.getPath(), file);
    if (archivalPath == null || archivalPath.isBlank()) {
      file.delete();
      return;
    }
    
    DiFileUtils.moveFile(archivalPath, file);
  }
  
  /**
   * it will read excel file bytes and move it to archive
   * 
   * @param archivalPath
   * @param excelDataMap
   * @param file
   * @param fileName
   * @throws IOException
   */
  private void processExcelFile(String archivalPath, Map<String, Object> excelDataMap, File file, String fileName) throws IOException
  {
    Path fileLocation = Paths.get(file.getPath());
    excelDataMap.put(fileName, Base64.getEncoder().encodeToString(Files.readAllBytes(fileLocation)));
    
    if (archivalPath == null || archivalPath.isBlank()) {
      file.delete();
      return;
    }
    DiFileUtils.moveFile(archivalPath, file);
  }
  
  /**
   * it will read json file bytes and move it to archive
   * 
   * @param archivalPath
   * @param jsonDataMap
   * @param file
   * @param fileName
   * @throws IOException
   */
  private void processJSONFile(String archivalPath, Map<String, Object> jsonDataMap, File file, String fileName) throws IOException
  {
    jsonDataMap.put(fileName, new String(Files.readAllBytes(Paths.get(file.getPath()))));
    if (archivalPath == null) {
      file.delete();
      return;
    }
    DiFileUtils.moveFile(archivalPath, file);
  }
  
  /**
   * 
   * @param assetFile
   * @param executionStatusTables
   * @return
   */
  private String getAssetInstanceId(File assetFile, IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTables)
  {
    String fileNameWithoutExtension = FilenameUtils.removeExtension(assetFile.getName());
    if (fileNameWithoutExtension.contains("\\s")) {
      executionStatusTables.addInformation(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN005,
          new String[] { "File Name contains white space , White spaces will be removed as Id" });
    }
    return fileNameWithoutExtension.replaceAll("\\s", "");
    
  }
  
  @Override
  public List<String> getInputList()
  {
    return INPUT_LIST;
  }
  
  @Override
  public List<String> getOutputList()
  {
    return OUTPUT_LIST;
  }
  
  @Override
  public List<WorkflowType> getWorkflowTypes()
  {
    return WORKFLOW_TYPES;
  }
  
  @Override
  public List<EventType> getEventTypes()
  {
    return EVENT_TYPES;
  }
  
  @Override
  public TaskType getTaskType()
  {
    return TaskType.RECEIVE_TASK;
  }
  
  /**
   * Validate input parameters
   * 
   * @param inputFields
   */
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> returnList = new ArrayList<String>();
    String type = (String) inputFields.get(INPUT_METHOD);
    if (!DiValidationUtil.isBlank(type)) {
      try {
        switch (type) {
          case DiConstants.JMS:
            validateJMS(inputFields, returnList);
            break;
          
          case DiConstants.HOTFOLDER:
            validateHotFolder(inputFields, returnList);
            break;
        }
      }
      catch (Exception e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
    }
    else {
      returnList.add(INPUT_METHOD);
    }
    return returnList;
  }
  
  /**
   * Validate input parameters for HotFolders
   * 
   * @param inputFields
   * @param returnList
   */
  @SuppressWarnings("unchecked")
  private void validateHotFolder(Map<String, Object> inputFields, List<String> returnList)
  {
    // Validate required FOLDER_PATH
    String folderpath = (String) inputFields.get(HOT_FOLDER_PATH);
    if (DiValidationUtil.isBlank(folderpath) || !DiValidationUtil.validateDirectoryPath(folderpath)) {
      returnList.add(HOT_FOLDER_PATH);
    }
    // Validate optional ARCHIVAL_PATH
    String archivalpath = (String) inputFields.get(ARCHIVAL_PATH);
    if (!DiValidationUtil.isBlank(archivalpath) && (!DiValidationUtil.validateRelativePath(archivalpath)
        || DiValidationUtil.checkForEquqlRelativePath(folderpath, archivalpath))) {
      returnList.add(ARCHIVAL_PATH);
    }
    // Validate optional ERROR_PATH
    String errorpath = (String) inputFields.get(ERROR_PATH);
    if (!DiValidationUtil.isBlank(errorpath)
        && (!DiValidationUtil.validateRelativePath(errorpath) 
            || DiValidationUtil.checkForEquqlRelativePath(archivalpath, errorpath))) {
      returnList.add(ERROR_PATH);
    }
    // Validate required receiver_message
    List<String> messagetypes = (List<String>) inputFields.get(RECEIVER_MESSAGE_TYPE_LIST);
    if (messagetypes.isEmpty()) {
      returnList.add(RECEIVER_MESSAGE_TYPE_LIST);
    }
    // Note: Validation not required for
    // 1. ALLOW_SUB_FOLDER_ACCESS as it can only be true or false
    // 2. RECEIVED_DATA and EXECUTION_STATUS as it is output parameter
  }
  
  /**
   * Validate input parameters for JMS
   * 
   * @param inputFields
   * @param returnList
   */
  private void validateJMS(Map<String, Object> inputFields, List<String> returnList)
  {
    // Validate required CLASS_PATH_IP
    String classpathip = (String) inputFields.get(CLASS_PATH_IP);
    if (DiValidationUtil.isBlank(classpathip) || !(DiValidationUtil.isValidFormatOfIpAddress(classpathip))) {
      returnList.add(CLASS_PATH_IP);
    }
    // Validate required PORT
    String port = (String) inputFields.get(PORT);
    if (DiValidationUtil.isBlank(port) || !(DiValidationUtil.isValidPort(port))) {
      returnList.add(PORT);
    }
    // Validate required queuename
    String queuename = (String) inputFields.get(QUEUE_NAME);
    if (DiValidationUtil.isBlank(queuename)) {
      returnList.add(QUEUE_NAME);
    }
    // Validate required jmsdata
    String jmsData = (String) inputFields.get(JMS_DATA);
    if (DiValidationUtil.isBlank(jmsData)) {
      returnList.add(JMS_DATA);
    }
    // Validate required receiver_message
    @SuppressWarnings("unchecked")
    List<String> messagetypes = (List<String>) inputFields.get(RECEIVER_MESSAGE_TYPE_LIST);
    if (messagetypes.isEmpty()) {
      returnList.add(RECEIVER_MESSAGE_TYPE_LIST);
    }
    // Validate required acknowledement
    String acknowledge = (String) inputFields.get(ACKNOWLEDGEMENT);
    if (acknowledge.contains("true")) {
      String acknowledgequeuename = (String) inputFields.get(ACKNOWLEDGEMENT_QUEUE_NAME);
      if (DiValidationUtil.isBlank(acknowledgequeuename)) {
        returnList.add(ACKNOWLEDGEMENT_QUEUE_NAME);
        returnList.add(ACKNOWLEDGEMENT);
        
      }
    }
    // Validation not required for RECEIVED_DATA and EXECUTION STATUS as it is
    // output parameters.
  }
}
