package com.cs.di.workflow.tasks;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.jms.JMSException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.usecase.component.jms.JmsProducerComponent;
import com.cs.di.jms.IJMSAckListenerService;
import com.cs.di.runtime.utils.DiFileUtils;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.DeliveryType;
import com.cs.di.workflow.constants.DiConstants;
import com.cs.di.workflow.constants.DiDataType;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.IAbstractExecutionReader;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * This task simply delivers the files on JMS queue or HOTFOLDER.
 *
 *
 */
@Component("deliveryTask")
public class DeliveryTask extends AbstractTask {
  
  @Autowired
  JmsProducerComponent                   jmsProducerComponent;
  
  @Autowired
  IJMSAckListenerService                   jmsAckListener;
  
  public static final String             OUTPUT_METHOD              = "OUTPUT_METHOD";
  // For Hot folder
  public static final String             DESTINATION_PATH           = "DESTINATION_PATH";
  // For JMS
  public static final String             CLASS_PATH_IP              = "CLASS_PATH_IP";
  public static final String             PORT                       = "PORT";
  public static final String             QUEUE_NAME                 = "QUEUE_NAME";
  public static final String             ACKNOWLEDGEMENT_QUEUE_NAME = "ACKNOWLEDGEMENT_QUEUE_NAME";
  public static final String             ACKNOWLEDGEMENT            = "DELIVERY_TASK_ACKNOWLEDGEMENT";
  public static final String             MESSAGE_TYPE_LIST          = "MESSAGE_TYPE_LIST";
  public static final String             DATA_TO_EXPORT             = "DATA_TO_EXPORT";
  public static final String             EXPORTED_DATA              = "EXPORTED_DATA";
  public static final String             IS_JMS_SERVICE_AVAILABLE   = "IS_JMS_SERVICE_AVAILABLE";
  public static final String             DATE_TIME_PATTERN          = "yyyy-MM-dd_HH.mm.ss.mmm";
  public static final String             FOLDER_PRIFIX              = "File_delivery_";
  // retry Mechanism
  public static final String             CORRELATION_ID             = "CORRELATION_ID";
  public static final String             IS_LAST_RETRY              = "IS_LAST_RETRY";
  public static final String             RETRY_AHEAD                = "RETRY_AHEAD";
  public static final String             EXPORT_FILE_PREFIX         = "Export_";
  public static final String             XLSX_FILE_EXTENSION        = ".xlsx";
  public static final String             JSON_FILE_EXTENSION        = ".json";
  public static final String             FILE_PREFIX                = "FILE_PREFIX";
  public static final String             EXPORTED_FILE_PATH         = "EXPORTED_FILE_PATH";
  
  public static final List<String>       INPUT_LIST                 = Arrays.asList(DATA_TO_EXPORT, CLASS_PATH_IP, PORT, QUEUE_NAME,
      ACKNOWLEDGEMENT, ACKNOWLEDGEMENT_QUEUE_NAME, OUTPUT_METHOD, DESTINATION_PATH, MESSAGE_TYPE_LIST, RETRY_AHEAD, IS_LAST_RETRY,FILE_PREFIX);
  
  public static final List<String>       OUTPUT_LIST                = Arrays.asList(IS_JMS_SERVICE_AVAILABLE, EXPORTED_DATA, EXECUTION_STATUS, EXPORTED_FILE_PATH);
  
  public static final List<WorkflowType> WORKFLOW_TYPES             = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  
  public static final List<EventType>    EVENT_TYPES                = Arrays.asList(EventType.BUSINESS_PROCESS,
      EventType.INTEGRATION);
  
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
    return TaskType.SEND_TASK;
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
    String type = (String) inputFields.get(OUTPUT_METHOD);
    if (!DiValidationUtil.isBlank(type)) {
      try {
        switch (type) {
          case DiConstants.JMS:
            validateJms(inputFields, returnList);
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
      returnList.add(OUTPUT_METHOD);
    }
    return returnList;
  }
  
  /**
   * Validate input parameters for HotFolders
   * 
   * @param inputFields
   */
  private void validateHotFolder(Map<String, Object> inputFields, List<String> returnList)
  {
    // Validate required FOLDER_PATH
    String folderpath = (String) inputFields.get(DESTINATION_PATH);
    if (DiValidationUtil.isBlank(folderpath)
        || !DiValidationUtil.validateRelativePath(folderpath)) {
      returnList.add(DESTINATION_PATH);
    }
    // Validate required receiver_message
    @SuppressWarnings("unchecked")
    List<String> messagetypes = (List<String>) inputFields.get(MESSAGE_TYPE_LIST);
    if (messagetypes.isEmpty()) {
      returnList.add(MESSAGE_TYPE_LIST);
    }
    // Validate required DATA_TO_EXPORT
    String dataToExport = (String) inputFields.get(DATA_TO_EXPORT);
    if (DiValidationUtil.isBlank(dataToExport)) {
      returnList.add(DATA_TO_EXPORT);
    }
    // Note: Validation not required for EXPORTED_DATA and EXECUTION_STATUS as
    // they are output parameters
  }
  
  /**
   * Validate input parameters for JMS
   * 
   * @param inputFields
   */
  private void validateJms(Map<String, Object> inputFields, List<String> returnList)
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
    // Validate required datatoexport
    String dataToExport = (String) inputFields.get(DATA_TO_EXPORT);
    if (DiValidationUtil.isBlank(dataToExport)) {
      returnList.add(DATA_TO_EXPORT);
    }
    // Validate required receiver_message
    List<String> messagetypes = (List<String>) inputFields.get(MESSAGE_TYPE_LIST);
    if (messagetypes.isEmpty()) {
      returnList.add(MESSAGE_TYPE_LIST);
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
    // Validation not required for
    // 1.EXPORTED DATA and EXECUTION STATUS as it is output parameters.
    // 2.RETRY_AHEAD as it can only be true or false.
  }
  
  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    String dataToExport = DiValidationUtil.validateAndGetRequiredString(model, DATA_TO_EXPORT);
    List<?> allowedDataTypes = (List<?>) DiValidationUtil.validateAndGetRequiredCollection(model, MESSAGE_TYPE_LIST);
    List<DiDataType> allowedDiDataTypes = allowedDataTypes.stream().map(dataType -> Enum.valueOf(DiDataType.class, (String) dataType))
        .collect(Collectors.toList());
    DeliveryType outputMethod = DiValidationUtil.validateAndGetRequiredEnum(model, OUTPUT_METHOD, DeliveryType.class);
    if (model.getExecutionStatusTable().isErrorOccurred()) {
      return;
    }
    Map<String, Object> successFileMap = new HashMap<>();
    try {
      switch (outputMethod) {
        case HOTFOLDER:
          writeToHotFolder(model, allowedDiDataTypes, dataToExport, successFileMap);
          break;
        case JMS:
          prepareMessageBodyAndSendToQueue(model, allowedDiDataTypes, dataToExport, successFileMap);
          break;
        default:
          break;
      }
      model.getOutputParameters().put(EXPORTED_DATA, successFileMap);
    }
    catch (Exception e) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.CAMUNDA_TASK, ObjectCode.CAMUNDA_TASK_ID }, MessageCode.GEN022,
          new String[] { DeliveryTask.class.getName(), model.getTaskId() });
    }
  }
  
  /**
   * Responsible to write input file streams to a hotfolder
   * 
   * @param model
   * @param allowedDiDataTypes
   * @param dataToExport
   * @param successFileMap
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  private void writeToHotFolder(WorkflowTaskModel model, List<DiDataType> allowedDiDataTypes,
      String dataToExport, Map<String, Object> successFileMap) throws Exception
  {
    int totalCount = 1;
    String filePrefix = DiValidationUtil.validateAndGetOptionalString(model, FILE_PREFIX);
    String destinationPath = DiValidationUtil.validateAndGetOptionalString(model, DESTINATION_PATH);
    destinationPath = destinationPath.replace("\\", "/");
    String path = getOrCreateProcessingPath(model, destinationPath);
    if (model.getExecutionStatusTable().isErrorOccurred()) {
      return;
    }
    
    File hotFolder = new File(path);
    for (DiDataType diDataType : allowedDiDataTypes) {
       String filename = null;
        InputStream inputStream = null;
        switch (diDataType) {
          case JSON:
            inputStream = new ByteArrayInputStream(dataToExport.getBytes());
            filename = DiFileUtils.generateFileName(EXPORT_FILE_PREFIX) + JSON_FILE_EXTENSION;
            break;
          case EXCEL:
            inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(dataToExport.getBytes()));
            filename = generateExcelFile(filePrefix!=null?filePrefix.toLowerCase():null);
            break;
          default:
            model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN001,
                new String[] { diDataType.name() });
            continue;
        }
        String filePath = hotFolder.getPath() + File.separator + filename;
        File destinationFile = new File(filePath);
        FileUtils.copyInputStreamToFile(inputStream, destinationFile);
        successFileMap.put(filename, dataToExport);
        model.getOutputParameters().put(EXPORTED_FILE_PATH, destinationPath+filename);
    }
    if (!model.getExecutionStatusTable().isErrorOccurred() && successFileMap.isEmpty()) {
      model.getExecutionStatusTable().addWarning(MessageCode.GEN002);
    }
    
    model.getExecutionStatusTable().addSummary(new ObjectCode[] { ObjectCode.TOTAL_COUNT, ObjectCode.SUCCESS_COUNT }, MessageCode.GEN000,
        new String[] { String.valueOf(totalCount), String.valueOf(successFileMap.isEmpty() ? 0 : successFileMap.size())});
  }
  
  /**
   * Validate and prepare the message to send in JMS Queue.json and excel file
   * only supported . Responsible to set EXPORTED_DATA property of this
   * component which contain list of files those are successfully delivered in
   * queue .
   * 
   * @param model This is the model to get and set the input and output property
   *        of component.
   * @param allowedDiDataTypes supported file format.
   * @param dataToExport input data
   * @param successFileMap
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  private void prepareMessageBodyAndSendToQueue(WorkflowTaskModel model,
      List<DiDataType> allowedDiDataTypes, String dataToExport,
      Map<String, Object> successFileMap) throws Exception
  {
    int totalCount = 1;
    Map<String, String> errorFileList = new HashMap<String, String>();
    String classPathIP = DiValidationUtil.validateAndGetRequiredString(model, CLASS_PATH_IP);
    String filePrefix = DiValidationUtil.validateAndGetOptionalString(model, FILE_PREFIX);
    if (classPathIP == null) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005,
          new String[] { CLASS_PATH_IP });
    }
    String port = DiValidationUtil.validateAndGetRequiredString(model, PORT);
    if (port == null) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005, new String[] { PORT });
    }
    String queueName = DiValidationUtil.validateAndGetRequiredString(model, QUEUE_NAME);
    if (model.getExecutionStatusTable().isErrorOccurred()) {
      return;
    }
    
    boolean isJMSActive = true;
    for (DiDataType diDataType : allowedDiDataTypes) {
      try {
        sendMessage(model, diDataType, classPathIP, port, queueName, dataToExport);
        String filename ;
        if (diDataType == diDataType.JSON)
          filename = DiFileUtils.generateFileName(EXPORT_FILE_PREFIX) + JSON_FILE_EXTENSION;
        else 
          filename = generateExcelFile(filePrefix!=null?filePrefix.toLowerCase():null);       
          successFileMap.put(filename, dataToExport);
      }
      catch (Exception exception) {
        errorFileList.put(diDataType.name(), dataToExport);
        model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.SERVICE }, MessageCode.GENO52, new String[] { "JMS(ActiveMQ)" });
        if (exception.getCause() instanceof JMSException && exception.getCause().getCause() instanceof ConnectException) {
          isJMSActive = false;
          break;
        }
      }
    }
    
    model.getOutputParameters().put(IS_JMS_SERVICE_AVAILABLE, isJMSActive);
    
    if (!model.getExecutionStatusTable().isErrorOccurred() && successFileMap.isEmpty()) {
      model.getExecutionStatusTable().addWarning(MessageCode.GEN002);
    }
    
    model.getExecutionStatusTable().addSummary(new ObjectCode[] { ObjectCode.TOTAL_COUNT, ObjectCode.SUCCESS_COUNT }, MessageCode.GEN000,
        new String[] { String.valueOf(totalCount), String.valueOf(successFileMap.isEmpty() ? 0 : successFileMap.size())});
  }
  
  /**
   * For Config Export File name generated will be with Config Type Prefix 
   * For Runtime Export File name generated will be with Export Prefix
   * @param model
   * @return
   */
	private String generateExcelFile(String filePrefix) {
		String filename;
		if (filePrefix != null) {
			filePrefix = filePrefix.substring(0, 1).toUpperCase() + filePrefix.substring(1);
			filename = DiFileUtils.generateFileName(filePrefix + "_") + XLSX_FILE_EXTENSION;
		} else {
			filename = DiFileUtils.generateFileName(EXPORT_FILE_PREFIX) + XLSX_FILE_EXTENSION;
		}
		return filename;
	}

  private void sendMessage(WorkflowTaskModel model, DiDataType diDataType,
      String classPathIP, String port, String queueName,
     String jsonString) throws JsonProcessingException, Exception
  {
    // for JMS Retry and Acknowledgement mechanism in workflow
    String correlationId = getCorrelationId(DiValidationUtil.validateAndGetOptionalString(model, CORRELATION_ID));
    model.getOutputParameters().put(CORRELATION_ID, correlationId);
    Boolean isLastRetry = DiValidationUtil.validateAndGetOptionalBoolean(model, IS_LAST_RETRY);
    Boolean retryAhead = DiValidationUtil.validateAndGetOptionalBoolean(model, RETRY_AHEAD);
    isLastRetry = retryAhead == null || !retryAhead ? Boolean.TRUE : (isLastRetry == null ? Boolean.FALSE : isLastRetry);
    
    // Produce the messAGE
    String acknowledgequeuename = DiValidationUtil.validateAndGetOptionalString(model, ACKNOWLEDGEMENT_QUEUE_NAME);
    
    jmsProducerComponent.send(classPathIP + ":" + port, queueName, jsonString, correlationId, isLastRetry, 
        jmsAckListener.getDestination(classPathIP, port, acknowledgequeuename), diDataType.name());

  }
  
  /**
   * Create transaction id to identify each message uniquely.
   * 
   * @param correlationId
   * @return
   */
  private String getCorrelationId(String correlationId)
  {
    if (correlationId != null) {
      return correlationId;
    }
    else {
      return UUID.randomUUID().toString();
    }
  }
  
  @Override
  protected void setOutputParameters(WorkflowTaskModel taskModel, Object execution,
      IAbstractExecutionReader reader)
  {
    super.setOutputParameters(taskModel, execution, reader);
    reader.setVariable(execution, CORRELATION_ID, taskModel.getOutputParameters().get(CORRELATION_ID));
  }
  
  @Override
  protected void setInputParameters(WorkflowTaskModel model, Object execution,
      IAbstractExecutionReader reader)
  {
    super.setInputParameters(model, execution, reader);
    model.getInputParameters().put(CORRELATION_ID, reader.getVariable(execution, CORRELATION_ID));
  }
}