package com.cs.di.workflow.tasks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cs.workflow.base.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.springframework.stereotype.Component;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.model.WorkflowTaskModel;

@Component("BGPTask")
@SuppressWarnings("unchecked")
public abstract class AbstractBGPTask extends AbstractTask {
  
  public static final String      SERVICE        = "SERVICE";
  
  public static final String      SERVICE_DATA   = "SERVICE_DATA";
  
  public static final String      PRIORITY       = "PRIORITY";
  
  public static final String      JOB_ID           = "JOB_ID";
  
  public static final String BGP_SHARED_DIRECTORY = getBGPSharedFolderpath();
  /**
   * Get shared folder path
   * 
   * @return
   */
  private static String getBGPSharedFolderpath()
  {
    try {
      return CSProperties.instance().getString("nfs.file.path")+"/";
    }
    catch (CSInitializationException e) {
      RDBMSLogger.instance().exception(e);
    }
    return null;
  }
  
  private final TaskType          TASK_TYPE      = TaskType.BGP_TASK;

  public final List<WorkflowType> WORKFLOW_TYPES = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  
  public final List<EventType>    EVENT_TYPES    = Arrays.asList(EventType.BUSINESS_PROCESS);

  /**
   * setting correlation parameters for Background Process
   *
   * @param taskModel
   * @param execution
   * @param reader
   */
  @Override
  protected void setInputParameters(WorkflowTaskModel taskModel, Object execution,
                                    IAbstractExecutionReader reader) {
    super.setInputParameters(taskModel, execution, reader);
    //String activityInstanceId = (String) taskModel.getInputParameters().get(WorkflowConstants.ACTIVITY_INSTANCE_ID);
    //reader.setVariable(execution, activityInstanceId+"_messenger", "done");
  }

  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = model.getExecutionStatusTable();
    try {
      executeBGPService(model);
    }
    catch (CSFormatException | CSInitializationException | RDBMSException | JsonProcessingException e) {
      RDBMSLogger.instance().exception(e);
      executionStatusTable.addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN500, new String[] {});
    }
  }
  
  /**
   * Method to Execute BGP Task
   * 
   * @param model
   * @return
   * @throws CSFormatException
   * @throws CSInitializationException
   * @throws RDBMSException
   * @throws JsonProcessingException
   */
  protected Long executeBGPService(WorkflowTaskModel model)
      throws CSFormatException, CSInitializationException, RDBMSException, JsonProcessingException
  {
    long startTime = System.currentTimeMillis();
    String service = (String) model.getInputParameters().get(SERVICE);
    BGPPriority priority = (BGPPriority) model.getInputParameters().get(PRIORITY);
    String user = model.getWorkflowModel().getTransactionData().getUserName();
    String dtoString = prepareInputDTO(model);
    if (dtoString != null) {
      long jobId = BGPDriverDAO.instance().submitBGPProcess(user, service,
          prepareCallbackURL(model.getWorkflowModel().getRootProcessInstanceId(), service), priority, new JSONContent(dtoString));
      RDBMSLogger.instance().debug("Component Completed :" + this.getClass().getSimpleName() + " in  %d ms",
          System.currentTimeMillis() - startTime);
      model.getOutputParameters().put(JOB_ID, jobId);
      return jobId;
    }
    model.getOutputParameters().put(JOB_ID, 0L);
    return 0L;
  }
  
  /**
   * This method will prepare DTO for PXON Export
   * 
   * @param model
   * @return
   * @throws CSFormatException
   * @throws RDBMSException
   * @throws JsonProcessingException
   */
  protected String prepareInputDTO(WorkflowTaskModel model)
      throws CSFormatException, RDBMSException, JsonProcessingException
  {
    Map<String, String> serviceDataMap = (Map<String, String>) model.getInputParameters().get(SERVICE_DATA);
    if (serviceDataMap != null) {
      return (String) serviceDataMap.get(SERVICE_DATA);
    }
    return null;
  }
  
  /**
   * Prepare the URL for callback handling
   * @param processInstanceId
   * @param service
   * @return Strung URL having placeholder for jobId, status, processInstanceId and service in params
   * @throws CSInitializationException
   */
  protected String prepareCallbackURL(String processInstanceId, String service)
      throws CSInitializationException
  {
    String tomcatURL = CSProperties.instance().getString("tomcat.server.url");
    String warName = CSProperties.instance().getString("tomcat.war.name");
    return String.format("%s/%s/bgp/callback?job=%s&status=%s&processInstanceId=%s&service=%s",
        tomcatURL, warName, "${job}", "${status}", processInstanceId, service);
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
    return TASK_TYPE;
  }
}
