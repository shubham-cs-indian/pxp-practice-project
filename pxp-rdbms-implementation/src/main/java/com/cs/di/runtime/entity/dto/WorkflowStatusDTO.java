package com.cs.di.runtime.entity.dto;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.di.runtime.entity.idto.IWorkflowStatusDTO;

public class WorkflowStatusDTO implements IWorkflowStatusDTO {
  
  private static final long   serialVersionUID     = 1L;
  
  private static final String INSTANCE_IID        = "instanceIID";
  private static final String PROCESS_INSTANCE_ID  = "processInstanceID";
  private static final String DEFINITION_ID        = "definitionID";
  private static final String TASK_INSTANCE_ID     = "taskInstanceId";
  private static final String PARENT_IID           = "parentIID";
  private static final String JOB_ID               = "jobID";
  private static final String STATUS               = "status";
  private static final String LABEL                = "label";
  private static final String ENDPOINT_ID          = "endpointId";
  private static final String SUCCESS              = "success";
  private static final String ERROR                = "error";
  private static final String INFORMATION          = "information";
  private static final String SUMMARY              = "summary";
  private static final String WARNING              = "warning";
  private static final String CREATE_USER_ID       = "createUserID";
  private static final String START_TIME           = "startTime";
  private static final String END_TIME             = "endTime";
  private static final String PHYSICAL_CATALOG_ID  = "physicalCatalogId";
  
  private Long                instanceIID          = 0L;
  private Long                processInstanceID    = 0L;
  private String              processId            = "";
  private String              taskInstanceId       = "";
  private Long                parentIID            = 0L;
  private Long                jobID                = 0L;
  private Integer             status               = 0;
  private String              label                = "";
  private String              endpointId           = "";
  private String              success;
  private String              error;
  private String              information;
  private String              summary;
  private String              warning;
  private Long                createUserID;
  private Long                startTime;          
  private Long                endTime; 
  private String              physicalCatalogId;
  private String              definitionXml       = "";
  private String              service             = "";
  private String              logData             = "";
  
  public WorkflowStatusDTO(IResultSetParser parser) throws SQLException, CSFormatException {
    this.instanceIID = parser.getLong("instanceIID");
    this.processInstanceID = parser.getLong("processInstanceID");
    this.processId = parser.getString("definitionID");
    this.taskInstanceId = parser.getString("taskInstanceId");
    this.parentIID = parser.getLong("parentIID");
    this.jobID = parser.getLong("jobID");
    this.createUserID = parser.getLong("createUserID");
    this.status = parser.getInt("status");
    this.endpointId = parser.getString("endpointId");
    this.success = parser.getString("success");
    this.error = parser.getString("error");
    this.information = parser.getString("information");
    this.summary = parser.getString("summary");
    this.warning = parser.getString("warning");
    this.startTime = parser.getLong("startTime");
    this.endTime = parser.getLong("endTime");
    this.label = parser.getString("label");
    this.physicalCatalogId = parser.getString("physicalCatalogId");
  }
  
  public WorkflowStatusDTO()
  {
    // TODO Auto-generated constructor stub
  }

  public Long getInstanceIID()
  {
    return instanceIID;
  }
  
  public void setInstanceIID(Long instanceIID)
  {
    this.instanceIID = instanceIID;
  }
  
  @Override
  public Long getProcessInstanceID()
  {
    return processInstanceID;
  }
  
  @Override
  public void setProcessInstanceID(Long processInstanceID)
  {
    this.processInstanceID = processInstanceID;
  }
  
  @Override
  public String getProcessId()
  {
    return processId;
  }
  
  @Override
  public void setProcessId(String definitionID)
  {
    this.processId = definitionID;
  }
  
  @Override
  public String getTaskInstanceId()
  {
    return taskInstanceId;
  }
  
  @Override
  public void setTaskInstanceId(String taskInstanceId)
  {
    this.taskInstanceId = taskInstanceId;
  }
  
  @Override
  public Long getParentIID()
  {
    return parentIID;
  }
  
  @Override
  public void setParentIID(Long parentIID)
  {
    this.parentIID = parentIID;
  }
  
  @Override
  public Long getJobID()
  {
    return jobID;
  }
  
  @Override
  public void setJobID(Long jobID)
  {
    this.jobID = jobID;
  }
  
  @Override
  public Integer getStatus()
  {
    return status;
  }
  
  @Override
  public void setStatus(Integer status)
  {
    this.status = status;
  }
  
  public String getLabel()
  {
    return label;
  }
  
  public void setLabel(String label)
  {
    this.label = label;
  }
  

  public String getEndpointId()
  {
    return endpointId;
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    this.endpointId = endpointId;
  }
  
  @Override
  public String getSuccess()
  {
    return success;
  }
  
  @Override
  public void setSuccess(String success)
  {
    this.success = success;
  }
  
  @Override
  public String getError()
  {
    return error;
  }
  
  @Override
  public void setError(String error)
  {
    this.error = error;
  }
  
  @Override
  public String getInformation()
  {
    return information;
  }
  
  @Override
  public void setInformation(String information)
  {
    this.information = information;
  }
  
  @Override
  public String getSummary()
  {
    return summary;
  }
  
  @Override
  public void setSummary(String summary)
  {
    this.summary = summary;
  }
  
  @Override
  public String getWarning()
  {
    return warning;
  }
  
  @Override
  public void setWarning(String warning)
  {
    this.warning = warning;
  }
  
  public Long getCreateUserID()
  {
    return createUserID;
  }
  
  public void setCreateUserID(Long createUserID)
  {
    this.createUserID = createUserID;
  }
  
  @Override
  public Long getStartTime()
  {
    return startTime;
  }
  
  @Override
  public void setStartTime(Long startTime)
  {
    this.startTime = startTime;
  }
  
  @Override
  public Long getEndTime()
  {
    return endTime;
  }
  
  @Override
  public void setEndTime(Long endTime)
  {
    this.endTime = endTime;
  }

  @Override public void setExportOfIID(boolean status)
  {

  }

  @Override
  public boolean isNull()
  {
    // TODO Auto-generated method stub
    return false;
  }
  
  @Override
  public boolean isChanged()
  {
    // TODO Auto-generated method stub
    return false;
  }
  
  @Override
  public void setChanged(boolean status)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void fromPXON(String json) throws CSFormatException
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public StringBuffer toPXONBuffer() throws CSFormatException
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String getXmlDefinition()
  {
    return definitionXml;
  }

  @Override
  public void setXmlDefinition(String definitionXml)
  {
    this.definitionXml = definitionXml;
    
  }

  @Override
  public String getService()
  {
    return service;
  }

  @Override
  public void setService(String service)
  {
    this.service = service;
  }

  @Override
  public String getLogData()
  {
    return logData;
  }
  
  @Override
  public void setLogData(String logData)
  {
    this.logData = logData;
    
  }

  @Override
  public String getPhysicalCatalogId()
  {
    return physicalCatalogId;
  }

  @Override
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    this.physicalCatalogId = physicalCatalogId;
  } 
}
