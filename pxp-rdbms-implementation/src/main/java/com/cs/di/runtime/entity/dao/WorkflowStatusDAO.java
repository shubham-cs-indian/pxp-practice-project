package com.cs.di.runtime.entity.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.cs.core.bgprocess.dao.BGProcessDAS;
import com.cs.core.bgprocess.dto.BGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.data.Text;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.runtime.entity.dto.FilterWorkFlowStatusDTO;
import com.cs.di.runtime.entity.dto.WorkflowStatusDTO;
import com.cs.di.runtime.entity.idto.IFilterWorkFlowStatusDTO;
import com.cs.di.runtime.entity.idto.IWorkflowStatusDTO;

/**
 * DAO for operations in Workflow Status table
 * 
 * @author jamil.ahmad
 *
 */
public class WorkflowStatusDAO implements IWorkflowStatusDAO {
  
  RDBMSAbstractDriver driver;
  private long        userIID;

  private String Q_GET_WORKFLOW_STATUS    = " select * from pxp.workflowtaskstatus where parentIID is null ";
  
  private String Q_GET_TASK_STATUS_FOR_WORKFLOW = " select * from pxp.workflowtaskstatus where processInstanceID is not null "; //Task

  static final String Q_CREATE_WORKFLOW_STATUS = "INSERT INTO pxp.workflowtaskstatus (processInstanceID, definitionID, label, createUserID, "
      + "startTime, status, endpointId, physicalCatalogId) VALUES (?,?,?,?,?,?,?,?) RETURNING instanceIID";
  
  static final String Q_CREATE_TASK_STATUS     = "INSERT INTO pxp.workflowtaskstatus (processInstanceID, taskInstanceId, parentIID, "
      + "label, createUserID, startTime,status) VALUES (?,?,?,?,?,?,?) RETURNING instanceIID";
  
  static final String Q_UPDATE_STATUS          = "UPDATE  pxp.workflowtaskstatus set status =?, success = ?::JSON, error = ?::JSON, information = ?::JSON,  "
      + " summary = ?::JSON, warning = ?::JSON, endTime =?, jobID = ? where instanceIID = ?";
  
  static final String INSTANCE_IID = "instanceIID";
  
  private String Q_GET_FILTERED_WORKFLOW_STATUS    = "  SELECT instanceIID, processInstanceID,  definitionID, jobID, taskInstanceId, parentIID, status, label, endpointId, summary, createUserID, startTime, endTime";
  
  static final String Q_UPDATE_STATUS_OF_INSTANCES = "UPDATE pxp.workflowtaskstatus SET status =? WHERE parentIID is null AND status!=? AND processinstanceid IN ";
 
  public WorkflowStatusDAO()
  {
    
  }
  
  public WorkflowStatusDAO(long userIID)
  {
    this.userIID = userIID;
  }
  
  /**
   * Insert entry into the table when Workflow instance get created.
   * 
   * @param workflowDTO
   * @throws RDBMSException
   */
  @Override
  public void createWorkflowInstance(IWorkflowStatusDTO workflowDTO) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement statements = currentConn.prepareStatement(Q_CREATE_WORKFLOW_STATUS);
      statements.setLong(1, workflowDTO.getProcessInstanceID());
      statements.setString(2, workflowDTO.getProcessId());
      statements.setString(3, workflowDTO.getLabel());
      statements.setLong(4, workflowDTO.getCreateUserID());
      statements.setLong(5, workflowDTO.getStartTime());
      statements.setLong(6, workflowDTO.getStatus());
      statements.setString(7, workflowDTO.getEndpointId());
      statements.setString(8, workflowDTO.getPhysicalCatalogId());
      IResultSetParser resultParser = currentConn.getDriver().getResultSetParser(statements.executeQuery());
      while (resultParser != null && resultParser.next()) {
        workflowDTO.setInstanceIID(resultParser.getLong(INSTANCE_IID));
      }
    });
  }
  
  /**
   * Insert entry into the table when task instance get created.
   * 
   * @param workflowDTO
   * @throws RDBMSException
   */
  
  @Override
  public void createTaskInstance(IWorkflowStatusDTO workflowDTO) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement statements = currentConn.prepareStatement(Q_CREATE_TASK_STATUS);
      statements.setLong(1, workflowDTO.getProcessInstanceID());
      statements.setString(2, workflowDTO.getTaskInstanceId());
      statements.setLong(3, workflowDTO.getParentIID());
      statements.setString(4, workflowDTO.getLabel());
      statements.setLong(5, workflowDTO.getCreateUserID());
      statements.setLong(6, workflowDTO.getStartTime());
      statements.setLong(7, workflowDTO.getStatus());
      IResultSetParser resultParser = currentConn.getDriver().getResultSetParser(statements.executeQuery());
      while (resultParser != null && resultParser.next()) {
        workflowDTO.setInstanceIID(resultParser.getLong(INSTANCE_IID));
      }
    });
  }
  
  /**
   * Update entry in the table after Workflow or Task instance completion 
   * by using processInstanceIID.
   * @param workflowDTO
   * @throws RDBMSException
   */
  @Override
  public void updateWFOrTaskStatus(IWorkflowStatusDTO workflowDTO) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement statements = currentConn.prepareStatement(Q_UPDATE_STATUS);
      statements.setLong(1, workflowDTO.getStatus());
      statements.setObject(2, workflowDTO.getSuccess());
      statements.setObject(3, workflowDTO.getError());
      statements.setObject(4, workflowDTO.getInformation());
      statements.setObject(5, workflowDTO.getSummary());
      statements.setObject(6, workflowDTO.getWarning());
      statements.setLong(7, workflowDTO.getEndTime());
      statements.setLong(8, workflowDTO.getJobID());
      statements.setLong(9, workflowDTO.getInstanceIID());
      statements.executeUpdate();
    });
  }
  
  @Override
  public void updateWFStatusForStuckInstances(List<String> processInstanceIds) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement statements = currentConn.prepareStatement(Q_UPDATE_STATUS_OF_INSTANCES + quoteIt(processInstanceIds));
      statements.setLong(1, 9); // 9 for status : Stuck due to restart
      statements.setLong(2, 9);
      statements.executeUpdate();
    });
  }

  @Override
  public void updateWFStatusForFailedInstances(List<String> processInstanceIds) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement statements = currentConn.prepareStatement(Q_UPDATE_STATUS_OF_INSTANCES + quoteIt(processInstanceIds));
      statements.setLong(1, 8); // 8 for status : failed
      statements.setLong(2, 8);
      statements.executeUpdate();
    });
  }
  
  public static String quoteIt(Collection<String> listOfStrings)
  {
    StringBuilder classesArrayInString = new StringBuilder();
    classesArrayInString.append("(");
    int arraySize = listOfStrings.size();
    for (String listItem : listOfStrings) {
      arraySize--;
      classesArrayInString.append("'" + listItem + "'");
      if (arraySize != 0) {
        classesArrayInString.append(", ");
      }
    }
    classesArrayInString.append(")");
    return classesArrayInString.toString();
  }
  
  /**
   * Get all task instances of a workflow instance.
   * 
   * @param workflowInstanceIID
   */
  @Override
  public List<IWorkflowStatusDTO> getAllTaskInstanceByWF(Long workflowInstanceIID)
  {
    return null;
  }
  
  /**
   * This method return the workflow instances on the basis of parameter
   * (workflowDefinationId, userId,timeStamp).
   *
   * @param filterWorkflowDTO
   * @throws RDBMSException
   */
  @Override
  public List<IWorkflowStatusDTO> getFilteredWorkFlowInstance(IFilterWorkFlowStatusDTO filterWorkflowDTO) throws RDBMSException
  {
    List<IWorkflowStatusDTO> workflowStatusDTOs = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      List<String> endpointIds = filterWorkflowDTO.getEndpointIds();
      if (endpointIds != null && !endpointIds.isEmpty()) {
        String conditionStr = toSQLSearchExpression(endpointIds, false);
        Q_GET_WORKFLOW_STATUS += " and endpointId " + conditionStr;
      }
      
      List<String> physicalCatalogIds = filterWorkflowDTO.getPhysicalCatalogIds();
      if (physicalCatalogIds != null && !physicalCatalogIds.isEmpty()) {
        String conditionStr = toSQLSearchExpression(physicalCatalogIds, false);
        Q_GET_WORKFLOW_STATUS += " and physicalCatalogId " + conditionStr;
      }
      
      List<String> definationIDs = filterWorkflowDTO.getDefinationIds();
      if (definationIDs != null && !definationIDs.isEmpty()) {
        String conditionStr = toSQLSearchExpression(definationIDs, false);
        Q_GET_WORKFLOW_STATUS += " and definitionId " + conditionStr;
      }
      
      List<Long> createUserIDs = filterWorkflowDTO.getUserIds();
      if (createUserIDs != null && !createUserIDs.isEmpty()) {
        String conditionStr = toSQLSearchExpression(createUserIDs, false);
        Q_GET_WORKFLOW_STATUS += " and createUserID " + conditionStr;
      }
      
      Long startTime = filterWorkflowDTO.getStartTime();
      if (startTime != null) {
        Q_GET_WORKFLOW_STATUS += " and startTime >= " + startTime;
      }
      
      Long endTime = filterWorkflowDTO.getEndTime();
      if (endTime != null) {
        Q_GET_WORKFLOW_STATUS += " and endTime <=" + endTime;
      }
      
      Q_GET_WORKFLOW_STATUS += " ORDER BY startTime DESC";
      
      PreparedStatement prepareStatements = currentConn.prepareStatement(Q_GET_WORKFLOW_STATUS);
      IResultSetParser resultParser = currentConn.getDriver().getResultSetParser(prepareStatements.executeQuery());
      while (resultParser != null && resultParser.next()) {
        WorkflowStatusDTO workflowStatusDTO = new WorkflowStatusDTO(resultParser);
        workflowStatusDTOs.add(workflowStatusDTO);
      }
    });
    return workflowStatusDTOs;
  }
  
  /**
   * Get Task and workflow details on ProcessInstanceID
   * @param parentiid 
   * @param filterWorkflowDTO
   * @throws RDBMSException
   */
  @Override
  public List<IWorkflowStatusDTO> getTaskForWorkFlowInstance(String instanceIID) throws RDBMSException
  {
    List<IWorkflowStatusDTO> workflowStatusDTOs = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      Q_GET_TASK_STATUS_FOR_WORKFLOW += " and ( parentIID =" + instanceIID + " or instanceIID="+ instanceIID + ")";
      PreparedStatement prepareStatements = currentConn.prepareStatement(Q_GET_TASK_STATUS_FOR_WORKFLOW);
      IResultSetParser resultParser = currentConn.getDriver().getResultSetParser(prepareStatements.executeQuery());
      while (resultParser != null && resultParser.next()) {
        WorkflowStatusDTO workflowStatusDTO = new WorkflowStatusDTO(resultParser);
        if (workflowStatusDTO.getJobID() > 0l) {
          // add status details for Backgroud process
          getBGPTaskStatus(workflowStatusDTO.getJobID(), workflowStatusDTO);
        }
        workflowStatusDTOs.add(workflowStatusDTO);
      }
    });
    return workflowStatusDTOs;
  }
  
  /**
   * get task status using jobIID
   * @param jobIID
   * @throws RDBMSException
   * @throws CSFormatException 
   */
  private void getBGPTaskStatus(Long jobIID, IWorkflowStatusDTO taskDto) throws RDBMSException, CSFormatException
  {
    BGProcessDAS bgpDAS = new BGProcessDAS();
    BGProcessDTO taskStatus = bgpDAS.getBGPProcess(jobIID);
    taskDto.setJobID(taskStatus.getJobIID());
    taskDto.setStatus(BGPStatus.valueOf(taskStatus.getStatus().toString()).ordinal());
    taskDto.setService(taskStatus.getService());
    taskDto.setLogData(((BGProcessDTO) taskStatus).getLog().toString());
    taskDto.setSummary(taskStatus.getSummary().toJSON());
  }
  
  /**
   * Transform a list of parameters into a SQL condition in / not in ( x, y, z)
   * @param parameters
   * @param notFilter
   * @return the SQL expression
   */
  public <T> String toSQLSearchExpression(Collection<T> parameters, boolean notFilter)
  {
    if (parameters.size() == 1) {
      return String.format("%s '%s'", notFilter ? "<> " : "= ", parameters.iterator().next());
    } // else
    return String.format("%s (%s)", notFilter ? "not in " : "in ", Text.join(",",
        parameters.stream().map(parameter -> Text.escapeStringWithQuotes(parameter.toString())).collect(Collectors.toSet())));
  }
  

  @Override
  public long getUserIID()
  {
    return userIID;
  }
  
  @Override
  public IFilterWorkFlowStatusDTO newFilterWorkFlowStatusDTO()
  {
    return new FilterWorkFlowStatusDTO();
  }
  
  @Override
  public IWorkflowStatusDTO newWorkflowStatusDTO()
  {
    return new WorkflowStatusDTO();
  }
  
  /**
   * This method return the workflow instances on the basis of filter criteria given
   * (workflowDefinationId, userId,timeStamp, message type)
   * basically it is used for bulk download.
   *
   * @param filterWorkflowDTO
   * @throws RDBMSException
   */
  @Override
  public List<IWorkflowStatusDTO> getWorkFlowInstancesBulkDownload(
      IFilterWorkFlowStatusDTO filterWorkflowDTO) throws RDBMSException
  {
    List<IWorkflowStatusDTO> workflowStatusDTOs = new ArrayList<>();
    List<String> messageTypes = filterWorkflowDTO.getMessageTypes();
    for (String messageType : messageTypes) {
      Q_GET_FILTERED_WORKFLOW_STATUS += ", " + messageType;
    }
    Q_GET_FILTERED_WORKFLOW_STATUS += " FROM pxp.workflowtaskstatus WHERE instanceIID is NOT null";
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          
          List<String> definationIDs = filterWorkflowDTO.getDefinationIds();
          if (definationIDs != null && !definationIDs.isEmpty()) {
            String conditionStr = toSQLSearchExpression(definationIDs, false);
            Q_GET_FILTERED_WORKFLOW_STATUS += " and processInstanceID in (SELECT processInstanceID FROM  pxp.workflowtaskstatus  WHERE definitionID "
                + conditionStr + ")";
          }
          
          
          List<Long> createUserIDs = filterWorkflowDTO.getUserIds();
          if (createUserIDs != null && !createUserIDs.isEmpty()) {
            String conditionStr = toSQLSearchExpression(createUserIDs, false);
            Q_GET_FILTERED_WORKFLOW_STATUS += " and createUserID " + conditionStr;
          }
          
          Long startTime = filterWorkflowDTO.getStartTime();
          if (startTime != null) {
            Q_GET_FILTERED_WORKFLOW_STATUS += " and startTime >= " + startTime;
          }
          
          Long endTime = filterWorkflowDTO.getEndTime();
          if (endTime != null) {
            Q_GET_FILTERED_WORKFLOW_STATUS += " and endTime <=" + endTime;
          }
          
          PreparedStatement prepareStatements = currentConn.prepareStatement(Q_GET_FILTERED_WORKFLOW_STATUS);
          IResultSetParser resultParser = currentConn.getDriver().getResultSetParser(prepareStatements.executeQuery());
          while (resultParser != null && resultParser.next()) {
            WorkflowStatusDTO workflowStatusDTO = new WorkflowStatusDTO();
            workflowStatusDTO.setInstanceIID(resultParser.getLong("instanceIID"));
            workflowStatusDTO.setProcessInstanceID(resultParser.getLong("processInstanceID"));
            workflowStatusDTO.setProcessId(resultParser.getString("definitionID"));
            workflowStatusDTO.setTaskInstanceId(resultParser.getString("taskInstanceId"));
            workflowStatusDTO.setParentIID(resultParser.getLong("parentIID"));
            workflowStatusDTO.setJobID(resultParser.getLong("jobID"));
            workflowStatusDTO.setCreateUserID(resultParser.getLong("createUserID"));
            workflowStatusDTO.setStatus(resultParser.getInt("status"));
            workflowStatusDTO.setEndpointId(resultParser.getString("endpointId"));
            workflowStatusDTO.setSummary(resultParser.getString("summary"));
            workflowStatusDTO.setStartTime(resultParser.getLong("startTime"));
            workflowStatusDTO.setEndTime(resultParser.getLong("endTime"));
            workflowStatusDTO.setLabel(resultParser.getString("label"));
            
            for (String messageType : messageTypes) {
              if (messageType.equals("success")) {
                workflowStatusDTO.setSuccess(resultParser.getString(messageType));
              }
              else if (messageType.equals("error")) {
                workflowStatusDTO.setError(resultParser.getString(messageType));
              }
              else if (messageType.equals("warning")) {
                workflowStatusDTO.setWarning(resultParser.getString(messageType));
              }
              else if (messageType.equals("information")) {
                workflowStatusDTO.setInformation(resultParser.getString(messageType));
              }
            }
            workflowStatusDTOs.add(workflowStatusDTO);
          }
        });
    return workflowStatusDTOs;
  }
}
