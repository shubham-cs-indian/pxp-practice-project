package com.cs.core.workflow.get;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.articleimportcomponent.IResponseModelForProcessInstance;
import com.cs.core.config.interactor.model.articleimportcomponent.ResponseModelForProcessInstance;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.constants.application.DiConstants;
import com.cs.core.runtime.interactor.constants.application.ProcessConstants;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.runtime.entity.dao.IWorkflowStatusDAO;
import com.cs.di.runtime.entity.idto.IFilterWorkFlowStatusDTO;
import com.cs.di.runtime.entity.idto.IWorkflowStatusDTO;
import com.cs.di.runtime.model.processinstance.IGetProcessInstanceModel;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.model.executionstatus.OutputExecutionStatusModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  Get the workflow instances with their respective tasks on the basis of filter criteria.
 * Filter parameters : Workflow definition id, User Id, Start time, End time and message type.
 * 
 * @author mangesh.metkari
 *
 */
@Component
public class DownloadWFInstanceService extends AbstractRuntimeService<IGetProcessInstanceModel, IResponseModelForProcessInstance>
    implements IDownloadWFInstanceService {
  
  @Autowired
  protected RDBMSComponentUtils rdbmsComponentUtils;
  
  public static final String    ZIP_FILE_EXTENSION = "processStatusLogs.zip";
  public static final String    WORKFLOWSTATUS     = "workflowStatus";
  public static final String    TASKSTATUS         = "taskStatus";
  public static final String    CSV_FILE_EXTENTION = ".csv";
  
  @Override
  public IResponseModelForProcessInstance executeInternal(IGetProcessInstanceModel model)
      throws Exception
  {
    IWorkflowStatusDAO openWorkflowStatusDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openWorkflowStatusDAO();
    List<IWorkflowStatusDTO> workflowInstances = new ArrayList<>();
    if (model.getInstanceIIDs() != null && !model.getInstanceIIDs().isEmpty()) {
      model.setMessageTypes(
          new ArrayList<>(Arrays.asList(MessageType.SUCCESS, MessageType.ERROR, MessageType.WARNING, MessageType.INFORMATION)));
      workflowInstances = openWorkflowStatusDAO.getTaskForWorkFlowInstance(model.getInstanceIIDs().get(0));
    }else {
      // collect IWorkflowStatusDTOs
      workflowInstances = getWorkflowStatusDTOs(model, openWorkflowStatusDAO);
    }
    
    // if not workflow status is available
    if (workflowInstances == null || workflowInstances.isEmpty()) {
      return new ResponseModelForProcessInstance();
    }
    
    // sorting Workflow status and their respective task status based on processInstanceIds
    Map<Long, List<IWorkflowStatusDTO>> workflowTaskGroup = workflowInstances.stream()
        .collect(Collectors.groupingBy(IWorkflowStatusDTO::getProcessInstanceID));
    
    String zipFilepath = writeProcessStatusInFiles(workflowTaskGroup);
    IResponseModelForProcessInstance returnModel = generateResponseModel(zipFilepath);
    return returnModel;
  }
  
/**
 * 
 * @param messageTypes
 * @param workflowTaskGroup
 * @return
 * @throws IOException
 * @throws FileNotFoundException
 */
  private String writeProcessStatusInFiles(Map<Long, List<IWorkflowStatusDTO>> workflowTaskGroup)
      throws IOException, FileNotFoundException
  {
    List<File> files = new ArrayList<>();
    //prepering zip file path
    String path = new File(getZIPFolderPath()).getAbsoluteFile().getPath();
    String zipFilepath = path + "//" + ZIP_FILE_EXTENSION;
    for (Long pid : workflowTaskGroup.keySet()) {
      Map<String, Object> workflowAndTaskMap = new HashMap<String, Object>();
      List<IWorkflowStatusDTO> processInsances = workflowTaskGroup.get(pid);
      List<IWorkflowStatusDTO> taskList = new ArrayList<IWorkflowStatusDTO>();
      for (IWorkflowStatusDTO workflowInstance : processInsances) {
        
        if (workflowInstance.getParentIID().equals(0L)) {
          workflowAndTaskMap.put(WORKFLOWSTATUS, workflowInstance);
        }
        else {
          taskList.add(workflowInstance);
        }
      }
      workflowAndTaskMap.put(TASKSTATUS, taskList);
      // create .csv file of workflow status
      createFileOfStatusLogs(path, files, workflowAndTaskMap);
    }
    // make a zip file
    DiUtils.makeZipFiles(files, zipFilepath);
    return zipFilepath;
  }
  
/**
 * 
 * @param model
 * @return
 * @throws Exception
 * @throws RDBMSException
 */
  private List<IWorkflowStatusDTO> getWorkflowStatusDTOs(IGetProcessInstanceModel model, IWorkflowStatusDAO openWorkflowStatusDAO)
      throws Exception, RDBMSException
  {
    // Prepare IFilterWorkFlowStatusDTO to get get filtered IWorkflowStatusDTO
    IFilterWorkFlowStatusDTO filterWorkFlowStatusDTO = openWorkflowStatusDAO
        .newFilterWorkFlowStatusDTO();
    filterWorkFlowStatusDTO.setDefinationIds(model.getProcessEventIds());
    filterWorkFlowStatusDTO.setUserIds(model.getUserIds());
    filterWorkFlowStatusDTO.setStartTime(model.getFrom());
    filterWorkFlowStatusDTO.setEndTime(model.getTo());
    filterWorkFlowStatusDTO.setMessageTypes(getMessageTypeList((model.getMessageTypes())));
    
    List<IWorkflowStatusDTO> workflowInstances = openWorkflowStatusDAO
        .getWorkFlowInstancesBulkDownload(filterWorkFlowStatusDTO);
    return workflowInstances;
  }
  
  /**
   * 
   * @param list
   * @return
   */
  private List<String> getMessageTypeList(List<MessageType> list)
  {
    List<String> messageTypes = new ArrayList<String>();
    for (MessageType messageType : list) {
      messageTypes.add(messageType.name().toString().toLowerCase());
    }
    return messageTypes;
}

  /**
   * 
   * @param path
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   */
  private IResponseModelForProcessInstance generateResponseModel(String path)
      throws FileNotFoundException, IOException
  {
    File file = new File(path);
    if (!file.exists()) {
      throw new FileNotFoundException();
    }
    Path fileLocation = Paths.get(path);
    // Convert zip file to byte stream
    byte[] fileStream = Files.readAllBytes(fileLocation);
    IResponseModelForProcessInstance returnModel = new ResponseModelForProcessInstance();
    returnModel.setFileName(ZIP_FILE_EXTENSION);
    returnModel.setFileStream(fileStream);
    
    // Delete zip file
     file.delete();
    return returnModel;
  }
  /* 
   * @param messageTypes
   * @param path
   * @param zipFilepath
   * @param workflowInstances
   * @return
   * @throws IOException 
   */  
  @SuppressWarnings("unchecked")
  public void createFileOfStatusLogs(String path, List<File> files, Map<String, Object> workflowAndTaskMap) throws IOException
  {
    IWorkflowStatusDTO workflowStatus = (IWorkflowStatusDTO) workflowAndTaskMap
        .get(DownloadWFInstanceService.WORKFLOWSTATUS);
    List<IWorkflowStatusDTO> tasksStatus = (List<IWorkflowStatusDTO>) workflowAndTaskMap
        .get(DownloadWFInstanceService.TASKSTATUS);
    
    // Existing id in ProcessINstacne now replaced with instacneIID
    File file = new File(path + "//" + workflowStatus.getProcessInstanceID() + CSV_FILE_EXTENTION);
    FileWriter csvWriter = new FileWriter(file);
    // Append workflow  summary
    appendWorkflowSummary(csvWriter, workflowStatus);
    
    // Append tasks summary
    for (IWorkflowStatusDTO task : tasksStatus) {
      appendTaskSummary(csvWriter, task);
      // Append execution status table column heading
      appendColumnHeadings(csvWriter);
      // append execution status
      appendMessagesSummary(collectAllStatusMessages(task), csvWriter);
      
    }
    files.add(file);
    csvWriter.flush();
    csvWriter.close();
  }

  private List<OutputExecutionStatusModel> collectAllStatusMessages(IWorkflowStatusDTO task)
  {
    List<OutputExecutionStatusModel> messages = new ArrayList<OutputExecutionStatusModel>();
    if (task.getSuccess() != null && !task.getSuccess()
        .isEmpty()) {
      messages.addAll(getMessageList(task.getSuccess()));
    }
    if (task.getError() != null && !task.getError()
        .isEmpty()) {
      messages.addAll(getMessageList(task.getError()));
    }
    if (task.getWarning() != null && !task.getWarning()
        .isEmpty()) {
      messages.addAll(getMessageList(task.getWarning()));
    }
    if (task.getInformation() != null && !task.getInformation()
        .isEmpty()) {
      messages.addAll(getMessageList(task.getInformation()));
    }
    return messages;
  }
  
/**
 * 
 * @param messages
 * @param csvWriter
 * @throws IOException
 */
  private static void appendMessagesSummary(List<? extends IOutputExecutionStatusModel> messages,
      FileWriter csvWriter) throws IOException
  {
    for (IOutputExecutionStatusModel message : messages) {
      csvWriter.append(message.getMessageType());
      csvWriter.append(",");
      csvWriter.append(prepareObjectValues(message.getObjectCodes()));
      csvWriter.append(",");
      csvWriter.append(message.getMessageCode().name());
      csvWriter.append(",");
      csvWriter.append(message.toString());
      csvWriter.append("\n");
    }
    
  }
  
    
  /**
   * 
   * @param codes
   * @return
   */
  public static String prepareObjectValues(ObjectCode[] codes)
  {
    if (codes == null) {
      return "";
    }
    StringBuilder builder = new StringBuilder();
    for (ObjectCode code : codes) {
      if (builder.length() > 0) {
        builder.append("|");
      }
      builder.append(code);
    }
    return builder.toString();
  }

  /**
   * Append execution status table column heading i.e (Message Type, Message
   * Code, Object Codes, Message Text).
   * 
   * @param csvWriter
   * @throws IOException
   */
  public static void appendColumnHeadings(FileWriter csvWriter) throws IOException
  {
    csvWriter.append("\n");
    csvWriter.append(ProcessConstants.MESSAGE_TYPE);
    csvWriter.append(",");
    csvWriter.append(ProcessConstants.OBJECT_CODE);
    csvWriter.append(",");
    csvWriter.append(ProcessConstants.MESSAGE_CODE);
    csvWriter.append(",");
    csvWriter.append(ProcessConstants.MESSAGE_TEXT);
    csvWriter.append("\n");
  }

  /**
   * 
   * @param csvWriter
   * @param workflowInstance
   * @throws IOException
   */
  
  private static void appendWorkflowSummary(FileWriter csvWriter,
      IWorkflowStatusDTO workflowInstance) throws IOException
  {
    csvWriter.append("Process Summary");
    csvWriter.append("\n");
    csvWriter.append("Process Instance Id");
    csvWriter.append(",");
    csvWriter.append(String.valueOf(workflowInstance.getProcessInstanceID()));
    csvWriter.append("\n");
    csvWriter.append("Workfolow Id");
    csvWriter.append(",");
    csvWriter.append(workflowInstance.getProcessId());
    csvWriter.append("\n");
    csvWriter.append("Start Time");
    csvWriter.append(",");
    csvWriter.append(workflowInstance.getStartTime() == 0 ? null
        : getTimeStampForFormat(workflowInstance.getStartTime(), DiConstants.DATE_TIME_FORMAT_24HR));
    csvWriter.append("\n");
    csvWriter.append("End Time");
    csvWriter.append(",");
    csvWriter.append(workflowInstance.getEndTime() == 0 ? null
        : getTimeStampForFormat(workflowInstance.getEndTime(), DiConstants.DATE_TIME_FORMAT_24HR));
    csvWriter.append("\n");
    
  }
  
  /**
   * 
   * 
   * @param csvWriter
   * @param task
   * @throws IOException
   */
  public static void appendTaskSummary(FileWriter csvWriter, IWorkflowStatusDTO task)
      throws IOException
  {
    csvWriter.append("\n");
    csvWriter.append("Component Name");
    csvWriter.append(",");
    csvWriter.append(task.getLabel());
    csvWriter.append("\n");
    csvWriter.append("System Component Id");
    csvWriter.append(",");
    csvWriter.append(task.getLabel());
    csvWriter.append("\n");
    csvWriter.append("Component Instance Id");
    csvWriter.append(",");
    csvWriter.append(task.getTaskInstanceId());
    csvWriter.append("\n");
    csvWriter.append("Start Time");
    csvWriter.append(",");
    csvWriter.append(task.getStartTime() == 0 ? null
        : getTimeStampForFormat(task.getStartTime(), DiConstants.DATE_TIME_FORMAT_24HR));
    csvWriter.append("\n");
    csvWriter.append("End Time");
    csvWriter.append(",");
    csvWriter.append(task.getEndTime() == 0 ? null
        : getTimeStampForFormat(task.getEndTime(), DiConstants.DATE_TIME_FORMAT_24HR));
    csvWriter.append("\n");
   //TODO : add  Success Ids and Fail Ids"
  }
  
  /**
   * 
   * @param timeStamp
   * @param format
   * @return custom date
   */
  public static String getTimeStampForFormat(Long timeStamp, String format)
  {
    Date dateValue = new Date(timeStamp);
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    String customDate = dateFormat.format(dateValue);
    return customDate;
  }
  
  
  
 /**
  * 
  * @param dto
  * @return
  */
  public static Boolean ifStatusIsEmpty(IWorkflowStatusDTO dto)
  {
    return dto.getError().isEmpty()
        && dto.getSuccess().isEmpty()
        && dto.getInformation().isEmpty()
        && dto.getWarning().isEmpty();
  }
  
  /**
   * Get zip folder path
   * 
   * @return
   */
  public static String getZIPFolderPath()
  {
    try {
      return CSProperties.instance()
          .getString("nfs.file.path") + "/";
    }
    catch (CSInitializationException e) {
      RDBMSLogger.instance()
          .exception(e);
    }
    return null;
  }
  
  /**
   * 
   * @param json
   * @return
   */
  public List<OutputExecutionStatusModel> getMessageList(String json)
  {
    
    List<OutputExecutionStatusModel> list = new ArrayList<OutputExecutionStatusModel>();
    try {
      ObjectMapper mapper = new ObjectMapper();
      list = mapper.readValue(json, new TypeReference<List<OutputExecutionStatusModel>>()
      {
      });
    }
    
    catch (Exception jsonExp) {
      RDBMSLogger.instance()
          .exception(jsonExp);
      return list;
    }
    return list;
  }
}
