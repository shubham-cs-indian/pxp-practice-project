package com.cs.di.runtime.entity.idto;

import com.cs.core.technical.rdbms.idto.IRootDTO;

public interface IWorkflowStatusDTO extends IRootDTO{
  
  // added for Task status other than background process
  public enum WFTaskStatus
  {
    
    UNDEFINED, PENDING, RUNNING, ENDED_SUCCESS, ENDED_ERRORS, ENDED_EXCEPTION, PAUSED, INTERRUPTED,
    CANCELED;
    
    private static final WFTaskStatus[] values = values();
    
    public static WFTaskStatus valueOf(int ordinal)
    {
      return values[ordinal];
    }
  }
  
  public Long getInstanceIID();
  public void setInstanceIID(Long processInstanceIID);
  
  public Long getProcessInstanceID();
  public void setProcessInstanceID(Long processInstanceID);
  
  public String getProcessId();
  public void setProcessId(String definitionID);

  public String getTaskInstanceId();
  public void setTaskInstanceId(String taskInstanceId);
  
  public Long getParentIID();
  public void setParentIID(Long parentIID);
  
  public Long getJobID();
  public void setJobID(Long jobID);
  
  public Integer getStatus();
  public void setStatus(Integer status);
  
  public String getLabel();
  public void setLabel(String label);
  
  public String getEndpointId();
  public void setEndpointId(String endpointId);
  
  public String getSuccess();
  public void setSuccess(String success);
  
  public String getError();
  public void setError(String error);
  
  public String getInformation();
  public void setInformation(String information);
  
  public String getSummary();
  public void setSummary(String summary);
  
  public String getWarning();
  public void setWarning(String warning);
  
  public Long getCreateUserID();
  public void setCreateUserID(Long createUserID);
  
  public Long getStartTime();
  public void setStartTime(Long startTime);
  
  public Long getEndTime();
  public void setEndTime(Long endTime);
  
  public String getXmlDefinition();  
  public void setXmlDefinition(String definitionXml);
  
  public String getService();  
  public void setService(String service);
  
  public String getLogData();  
  public void setLogData(String logData);
  
  public String getPhysicalCatalogId();
  public void setPhysicalCatalogId(String physicalCatalogIds);
}
