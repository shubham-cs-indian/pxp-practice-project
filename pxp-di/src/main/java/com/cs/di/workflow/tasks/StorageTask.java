package com.cs.di.workflow.tasks;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.DiDataType;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;

/*
 * This task responsible to store files in specified folder path.
 * 
 * @author priyaranjan.kumar
 *
 */
@Component("storageTask")
public class StorageTask extends AbstractTask {
  
  // for input
  public static final String                FILES_TO_BE_STORE = "FILES_TO_BE_STORE";
  public static final String                FOLDER_PATH       = "FOLDER_PATH";
  // for output
  public static final String                SUCCESS_FILES     = "SUCCESS_FILES";
  public static final String                UNSUCCESS_FILES   = "UNSUCCESS_FILES";
  public static final String                MESSAGE_TYPE_LIST = "MESSAGE_TYPE_LIST";

  
  protected static final List<String>       INPUT_LIST        = Arrays.asList(FILES_TO_BE_STORE, FOLDER_PATH);
  protected static final List<String>       OUTPUT_LIST       = Arrays.asList(SUCCESS_FILES,
      UNSUCCESS_FILES, EXECUTION_STATUS);
  protected static final List<WorkflowType> WORKFLOW_TYPES    = Arrays
      .asList(WorkflowType.STANDARD_WORKFLOW);
  protected static final List<EventType>    EVENT_TYPES       = Arrays
      .asList(EventType.BUSINESS_PROCESS);
  public static final String                FOLDER_PRIFIX       = "File_storageaction_";
  public static final String                DATE_TIME_PATTERN   = "yyyy-MM-dd_HH.mm.ss.mmm";
  public static final String                EXPORT_FILE_PREFIX  = "Export_";
  public static final String                XLSX_FILE_EXTENSION = ".xlsx";
  public static final String                JSON_FILE_EXTENSION = ".json";
  
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
    return TaskType.SERVICE_TASK;
  }
  
  /**
   * Validate input parameters
   * @param inputFields
   */
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> invalidInputParameters = new ArrayList<>();
    // Validate required INPUT_FOLDER_PATH
    String inputFolderPath = (String) inputFields.get(FOLDER_PATH);
    if (DiValidationUtil.isBlank(inputFolderPath)
        ||  !DiValidationUtil.validateDirectoryPath(inputFolderPath)) {
      invalidInputParameters.add(FOLDER_PATH);
    }
    // Validate required FILES_TO_BE_STORE
    String fileToBeStore = (String) inputFields.get(FILES_TO_BE_STORE);
    if (DiValidationUtil.isBlank(fileToBeStore))  {
      invalidInputParameters.add(FILES_TO_BE_STORE);
    }
    return invalidInputParameters;
  }
  
  /**
   * This method do validation on required parameter which are hold in
   * WorkflowTaskModel. if validation is fail then pass the proper error message
   * .
   * 
   * @param workflowTaskModel
   */
  @SuppressWarnings("unchecked")
  @Override
  public void executeTask(WorkflowTaskModel workflowTaskModel)
  {
    Map<String, String> filesToBeStore =  (Map<String, String>) DiValidationUtil.validateAndGetRequiredMap(workflowTaskModel, FILES_TO_BE_STORE);
    String folderPath = (String) DiValidationUtil.validateAndGetRequiredFolder(workflowTaskModel,FOLDER_PATH);
    if (filesToBeStore == null || folderPath == null) {
      workflowTaskModel.getExecutionStatusTable()
          .addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004,
              new String[] { filesToBeStore == null ? FILES_TO_BE_STORE : FOLDER_PATH });
      return;
    }
    writeToFolder(filesToBeStore, folderPath, workflowTaskModel);
  }
  
  /**
   * Responsible to write a files in folder.
   * 
   * @param filesToBeStore
   * @param folderPath
   * @param outputParameterMap
   * @param executionStatusTable
   */
  private void writeToFolder(Map<String, String> filesToBeStore, String folderPath,
      WorkflowTaskModel workflowTaskModel)
  {
    List<String> successFiles = new ArrayList<String>();
    List<String> failureFiles = new ArrayList<String>();
    String storageFolderInstanace = File.separator + FOLDER_PRIFIX + DateTime.now()
        .toString(DATE_TIME_PATTERN) + File.separator;
    File storageFolder = new File(folderPath);
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = workflowTaskModel
        .getExecutionStatusTable();    
    Map<String, Object> outputParameterMap = workflowTaskModel.getOutputParameters();
    for (Map.Entry<String, String> fileToBeStore : filesToBeStore.entrySet()) { 
      String fileName = fileToBeStore.getKey();
      String extension = FilenameUtils.getExtension(fileName);
      DiDataType diDataType = DiDataType.getDiDataTypeForExtension(extension);
      InputStream inputStream = null;
      switch (diDataType) {
        case JSON:
          inputStream = new ByteArrayInputStream(fileToBeStore.getValue().getBytes());
          break;
        case EXCEL:
          inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(fileToBeStore.getValue().getBytes()));
          break;
        default:
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN001,
              new String[] { diDataType.name() });
          failureFiles.add(fileName);
      }
      try {     
        File destinationFile = new File(storageFolder.getPath() + storageFolderInstanace + fileName);
        FileUtils.copyInputStreamToFile(inputStream, destinationFile);
        successFiles.add(fileName);
      }
      catch (Exception exception) {
        executionStatusTable.addError(new ObjectCode[] { ObjectCode.FILE_TYPE }, MessageCode.GEN018, new String[] { fileName });
        failureFiles.add(fileName);
      }
    }
    if (!executionStatusTable.isErrorOccurred() && successFiles.isEmpty()) {
      executionStatusTable.addWarning(MessageCode.GEN002);
    }
    outputParameterMap.put(SUCCESS_FILES, successFiles);
    outputParameterMap.put(UNSUCCESS_FILES, failureFiles);
  }
}
