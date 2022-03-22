package com.cs.di.workflow.tasks;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.cs.di.runtime.utils.DiFileUtils;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;

@Component("zipExtractionTask")
public class ZipExtractionTask extends AbstractTask {
  
  // Input Parameters
  public static final String             IS_RECURSIVE       = "IS_RECURSIVE";
  public static final String             INPUT_FOLDER_PATH  = "INPUT_FOLDER_PATH";
  public static final String             OUTPUT_FOLDER_PATH = "OUTPUT_FOLDER_PATH";
  public static final String             ARCHIVAL_PATH      = "ARCHIVAL_PATH";
  
  public static final List<String>       INPUT_LIST         = Arrays.asList(IS_RECURSIVE, INPUT_FOLDER_PATH, OUTPUT_FOLDER_PATH,
      ARCHIVAL_PATH);
  public static final List<String>       OUTPUT_LIST        = Arrays.asList(EXECUTION_STATUS);

  public static final List<WorkflowType> WORKFLOW_TYPES     = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  public static final List<EventType>    EVENT_TYPES        = Arrays.asList(EventType.BUSINESS_PROCESS,
      EventType.INTEGRATION);
  
  public static final String             DATE_TIME_PATTERN  = "yyyy-MM-dd_HH.mm.ss.mmm";
  public static final String             FOLDER_PRIFIX      = "Zip_Extraction_";
  private static final int               BUFFER_SIZE        = 1024;
  
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
   * 
   * @param inputFields
   */
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> invalidInputParameters = new ArrayList<>();
    // Validate required INPUT_FOLDER_PATH
    String inputFolderPath = (String) inputFields.get(INPUT_FOLDER_PATH);
    if (StringUtils.isBlank(inputFolderPath)
        || (!StringUtils.isBlank(inputFolderPath) && !DiValidationUtil.validateDirectoryPath(inputFolderPath))) {
      invalidInputParameters.add(INPUT_FOLDER_PATH);
    }
    
    // Validate required OUTPUT_FOLDER_PATH
    String outputFolderPath = (String) inputFields.get(OUTPUT_FOLDER_PATH);
    if (StringUtils.isBlank(outputFolderPath)
        || (!StringUtils.isBlank(outputFolderPath) && (!DiValidationUtil.validateDirectoryPath(outputFolderPath)
            || DiValidationUtil.checkForEqualDirectoryPaths(inputFolderPath, outputFolderPath)))) {
      invalidInputParameters.add(OUTPUT_FOLDER_PATH);
    }
    
    // Validate optional ARCHIVAL_PATH
    String archieveFolderPath = (String) inputFields.get(ARCHIVAL_PATH);
    if (!StringUtils.isBlank(archieveFolderPath) && (!DiValidationUtil.validateDirectoryPath(archieveFolderPath)
        || DiValidationUtil.checkForEqualDirectoryPaths(archieveFolderPath, inputFolderPath)
        || DiValidationUtil.checkForEqualDirectoryPaths(archieveFolderPath, outputFolderPath))) {
      invalidInputParameters.add(ARCHIVAL_PATH);
    }
    
    // Note: Validation not required for
    // 1. ARCHIVAL_PATH as it can only be true or false
    // 2. EXECUTION_STATUS as it is optional output parameter
    
    return invalidInputParameters;
  }
  
  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    try {
      
      String inputFolderPath = DiValidationUtil.validateAndGetRequiredString(model, INPUT_FOLDER_PATH);
      Boolean isRecursive = DiValidationUtil.validateAndGetOptionalBoolean(model, IS_RECURSIVE);
      String outputFolderPath = DiValidationUtil.validateAndGetRequiredString(model, OUTPUT_FOLDER_PATH);
      String archivalPath = DiValidationUtil.validateAndGetOptionalString(model, ARCHIVAL_PATH);
      if (model.getExecutionStatusTable().isErrorOccurred()) {
        return;
      }
      
      // Caution!!! Be careful while using these local variables as their
      // values mean to change with iterations
      File inputFolder = new File(inputFolderPath);
      File outputFolder = new File(outputFolderPath);
      File archivalFolder = new File(archivalPath);
      boolean doArchive = true;
      
      if (!inputFolder.exists() || !inputFolder.isDirectory()) {
        model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005,
            new String[] { INPUT_FOLDER_PATH });
      }
      if (!outputFolder.exists() || !outputFolder.isDirectory()) {
        model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005,
            new String[] { OUTPUT_FOLDER_PATH });
      }
      if (inputFolder.equals(outputFolder)) {
        model.getExecutionStatusTable().addError(
            new ObjectCode[] { ObjectCode.CAMUNDA_TASK, ObjectCode.INPUT_PARAM, ObjectCode.INPUT_PARAM, ObjectCode.INPUT_PARAM_VALUE,
                ObjectCode.INPUT_PARAM_VALUE },
            MessageCode.GEN031,
            new String[] { DeliveryTask.class.getName(), INPUT_FOLDER_PATH, OUTPUT_FOLDER_PATH, inputFolderPath, outputFolderPath });
      }
      if (!archivalFolder.exists() || !archivalFolder.isDirectory()) {
        model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005,
            new String[] { ARCHIVAL_PATH });
        doArchive = false;
      }
      
      List<File> zipFiles = null;
      do {
        zipFiles = DiFileUtils.readFilesFromPath(inputFolder.getPath(), true, new String[] { "*.zip" });
        for (File zipFile : zipFiles) {
          unzip(zipFile, outputFolder);
          if (doArchive) {
            String zipExtrationArchiveInstance = File.separator + FOLDER_PRIFIX + DateTime.now().toString(DATE_TIME_PATTERN)
                + File.separator;
            File archivedFile = new File(archivalFolder.getPath() + zipExtrationArchiveInstance + zipFile.getName());
            FileUtils.copyFile(zipFile, archivedFile);
          }
          zipFile.delete();
        }
        inputFolder = outputFolder;
        doArchive = false;
      }
      while (isRecursive && !zipFiles.isEmpty());
    }
    catch (Exception e) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.CAMUNDA_TASK, ObjectCode.CAMUNDA_TASK_ID }, MessageCode.GEN022,
          new String[] { ZipExtractionTask.class.getName(), model.getTaskId() });
    }
    
  }
  
  public void unzip(File zipFile, File destinationFolder) throws IOException
  {
    ZipInputStream zipIn;
    if (isWindows()) {
      zipIn = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)), Charset.forName("Cp437"));
    }
    else {
      zipIn = new ZipInputStream(new FileInputStream(zipFile));
    }
    ZipEntry entry = zipIn.getNextEntry();
    // iterates over entries in the zip file
    while (entry != null) {
      if (!entry.isDirectory()) {
        // if the entry is a file, extracts it
        int lastIndexOf = entry.getName().lastIndexOf("/");
        String unzipedfileName = lastIndexOf == -1 ? entry.getName() : entry.getName().substring(lastIndexOf + 1, entry.getName().length());
        String filePath = destinationFolder.getPath() + File.separator + unzipedfileName;
        extractFile(zipIn, filePath);
      }
      zipIn.closeEntry();
      entry = zipIn.getNextEntry();
    }
    zipIn.close();
  }
  
  private boolean isWindows()
  {
    boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
    return isWindows;
  }
  
  private void extractFile(ZipInputStream zipIn, String filePath) throws IOException
  {
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
    byte[] bytesIn = new byte[BUFFER_SIZE];
    int read = 0;
    while ((read = zipIn.read(bytesIn)) != -1) {
      bos.write(bytesIn, 0, read);
    }
    bos.close();
  }
  
}
