package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.di.runtime.utils.DiTalendUtils;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;

@Component("talendTask")
public class TalendTask extends AbstractTask {
  
  /*  retrieve the variables provided in context of the Talend Job and that
   are also set in talendconfig.properties
  */
  @Value("${talend.sourceFile.path}")
  private String                         sourceFilePath;
  
  @Value("${talend.outputFile.path}")
  private String                         outputFilePath;
  
  @Value("${talend.file.extention}")
  private String                         fileExtention;
  
  @Value("${talend.jobs.path}")
  String                                 talendJobsPath;
  
  @Value("${talend.jobs.libs.path}")
  String                                 externalJars;
  
  @Value("${talend.targetJsonFolder.path}")
  private String                         targetJsonFolder;
  
  @Value("${talend.targetXmlfolder.path}")
  private String                         targetXmlFolder;
  
  @Value("${talend.archiveSuccessFile.path}")
  private String                         archiveSuccessFile;
  
  @Value("${talend.archiveFailureFile.path}")
  private String                         archiveFailureFile;
  
  @Value("${talend.errorLog.path}")
  private String                         errorLogPath;
  
  public static final String             INPUT_DATA           = "INPUT_DATA";
  public static final String             TALEND_EXECUTABLE    = "TALEND_EXECUTABLE";
  public static final List<String>       INPUT_LIST           = Arrays.asList(INPUT_DATA,
      TALEND_EXECUTABLE);
  public static final String             OUTPUT_DATA          = "OUTPUT_DATA";
  public static final List<String>       OUTPUT_LIST          = Arrays.asList(OUTPUT_DATA,
      EXECUTION_STATUS);
  public static final String             TALEND_JOB_PATH      = "talendJobsPath";
  public static final String             EXTERNAL_JARS        = "externalJars";
  public static final String             SOURCE_FILE_PATH     = "sourceFilePath";
  public static final String             FILE_NAME            = "fileName";
  public static final String             FILE_EXTENSION       = "fileExtention";
  public static final String             OUTPUT_FILE_PATH     = "outputFilePath";
  public static final String             TARGET_JSON_FOLDER   = "targetJsonFolder";
  public static final String             TARGET_XML_FOLDER    = "targetXmlFolder";
  public static final String             ARCHIVE_SUCCESS_FILE = "archiveSuccessFile";
  public static final String             ARCHIVE_FAILURE_FILE = "archiveFailureFile";
  public static final String             ERROR_LOG_PATH       = "errorLogPath";
  
  public static final List<WorkflowType> WORKFLOW_TYPES       = Arrays
      .asList(WorkflowType.STANDARD_WORKFLOW);
  public static final List<EventType>    EVENT_TYPES          = Arrays
      .asList(EventType.BUSINESS_PROCESS, EventType.INTEGRATION);
  
  @Override
  @SuppressWarnings("unchecked")
  public void executeTask(WorkflowTaskModel model)
  {
    
    try {
      String fileName = UUID.randomUUID()
          .toString();
      Map<String, Object> contextVariables = new HashMap<String, Object>();
      contextVariables.put(SOURCE_FILE_PATH, sourceFilePath);
      contextVariables.put(OUTPUT_FILE_PATH, outputFilePath);
      contextVariables.put(TALEND_JOB_PATH, talendJobsPath);
      contextVariables.put(EXTERNAL_JARS, externalJars);
      contextVariables.put(FILE_NAME, fileName);
      contextVariables.put(FILE_EXTENSION, fileExtention);
      contextVariables.put(ARCHIVE_SUCCESS_FILE, archiveSuccessFile);
      contextVariables.put(ARCHIVE_FAILURE_FILE, archiveFailureFile);
      contextVariables.put(ERROR_LOG_PATH, errorLogPath);
      contextVariables.put(TARGET_JSON_FOLDER, targetJsonFolder);
      contextVariables.put(TARGET_XML_FOLDER, targetXmlFolder);
      
      model.getInputParameters()
          .putAll(contextVariables);
      String dataToTransform = (String) DiValidationUtil.validateAndGetRequiredString(model,
          INPUT_DATA);
      if (model.getExecutionStatusTable()
          .isErrorOccurred())
        return;
      model.getInputParameters()
          .put(INPUT_DATA, dataToTransform);
      DiTalendUtils.runTalendExecutable(model);
    }
    catch (Exception exception) {
      RDBMSLogger.instance()
          .exception(exception);
    }
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
    return TaskType.SERVICE_TASK;
  }
  
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> invalidParameters = new ArrayList<>();
    // Validate if a mandatory jar file is selected or not
    String fileName = (String) inputFields.get(TALEND_EXECUTABLE);
    if (DiValidationUtil.isBlank(fileName)) {
      invalidParameters.add(fileName);
    }
    // Validate mandatory input map
    String inputJSON = (String) inputFields.get(INPUT_DATA);
    if (DiValidationUtil.isBlank(inputJSON)) {
      invalidParameters.add(inputJSON);
    }
    return invalidParameters;
    
    /* Note: Validation not required for the following:
     1.   OUTPUT_DATA as it is optional output parameter
     2. EXECUTION_STATUS as it is optional output parameter
    */
  }
}
