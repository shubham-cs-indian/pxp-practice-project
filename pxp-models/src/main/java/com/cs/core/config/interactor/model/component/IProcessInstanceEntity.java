package com.cs.core.config.interactor.model.component;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IProcessInstanceEntity extends IEntity {
  
  public static final String START_TIME          = "startTime";
  public static final String END_TIME            = "endTime";
  public static final String LABEL               = "label";
  public static final String PROCESS_EVENT_ID    = "processEventId"; 
  public static final String SAVE_COMMENT        = "saveComment";
  public static final String FILE_INSTANCE_ID    = "fileInstanceId";
  public static final String ENDPOINT_ID         = "endpointId";
  public static final String SYSTEM_ID           = "systemId";
  public static final String PHYSICAL_CATALOG_ID = "physicalCatalogId";
  public static final String ORGANIZATION_ID     = "organizationId";
  public static final String USER_ID             = "userId";
  public static final String STATUS              = "status";
  public static final String PROCESSINSTANCEID   = "processInstanceId";
  public static final String TASKINSTANCEID      = "taskInstanceId";
  public static final String PROCESSDEFINITION   = "processDefinition";
  public static final String INSTANCE_IID        = "instanceIID";
  
  public Long getStartTime();
  
  public void setStartTime(Long startTime);
  
  public Long getEndTime();
  
  public void setEndTime(Long endTime);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getProcessEventId();
  
  public void setProcessEventId(String processId);
  
  public String getSaveComment();
  
  public void setSaveComment(String saveComment);
  
  public String getFileInstanceId();
  
  public void setFileInstanceId(String fileInstanceId);
  
  public String getSystemId();
  
  public void setSystemId(String systemId);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public Long getUserId();
  
  public void setUserId(Long userId);
  
  public Integer getStatus();
  
  public void setStatus(Integer status);
  
  public Long getProcessInstanceId();
  
  public void setProcessInstanceId(Long processInstanceId);
  
  public Long getInstanceIID();

  public void setInstanceIID(Long instanceIID);

  // PXPFDEV-15368 : added for getting task details from workflow
  /** STARTS **/
  
  public String getProcessDefinition();
  
  public void setProcessDefinition(String definitionXml);
  /** ENDS **/

 
}
