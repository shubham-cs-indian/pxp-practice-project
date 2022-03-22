package com.cs.core.runtime.interactor.model.logger;

import java.util.ArrayList;
import java.util.List;

public class TransactionData implements ITransactionData {
  
  protected String         id;
  protected String         requestId;
  protected String         useCase;
  protected String         requestMethod;
  protected String         executionStatus;
  protected String         userId;
  protected String         userName;
  protected String         parentTransactionId;
  protected String         type;
  protected Long           startTime;
  protected Long           endTime;
  protected Long           turnAroundTime;
  protected Throwable      exception;
  protected InteractorData interactorData;
  protected List<String>   subJobIds;
  protected List<String>   successSubJobIds;
  protected String         uiLanguage;
  protected String         dataLanguage;
  protected String         physicalCatalogId;
  protected String         portalId;
  protected String         organizationId;
  protected String         endpointId;
  protected String         logicalCatalogId;
  protected String         systemId;
  protected String         endpointType;
  protected String         urlForTalend;
  protected Long           level = new Long(0l);
  
  public InteractorData getInteractorData()
  {
    return interactorData;
  }
  
  public void setInteractorData(InteractorData interactorData)
  {
    this.interactorData = interactorData;
  }
  
  public Long getLevel()
  {
    return level;
  }
  
  public void setLevel(Long level)
  {
    this.level = level;
  }
  
  public String getRequestMethod()
  {
    return requestMethod;
  }
  
  public void setRequestMethod(String requestMethod)
  {
    this.requestMethod = requestMethod;
  }
  
  public String getUseCase()
  {
    return useCase;
  }
  
  public void setUseCase(String useCase)
  {
    this.useCase = useCase;
  }
  
  public Long getStartTime()
  {
    return startTime;
  }
  
  public void setStartTime(Long startTime)
  {
    this.startTime = startTime;
  }
  
  public Long getEndTime()
  {
    return endTime;
  }
  
  public void setEndTime(Long endTime)
  {
    this.endTime = endTime;
  }
  
  public Long getTurnAroundTime()
  {
    if (endTime != null && startTime != null) {
      return endTime - startTime;
    }
    return null;
  }
  
  public String getExecutionStatus()
  {
    return executionStatus;
  }
  
  public void setExecutionStatus(String executionStatus)
  {
    this.executionStatus = executionStatus;
  }
  
  public String getId()
  {
    return id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }
  
  public String getRequestId()
  {
    return requestId;
  }
  
  public void setRequestId(String requestId)
  {
    this.requestId = requestId;
  }
  
  public String getUserId()
  {
    return userId;
  }
  
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  public Throwable getException()
  {
    return exception;
  }
  
  public void setException(Throwable exception)
  {
    if (exception != null) {
      this.exception = new Throwable();
      this.exception.setStackTrace(exception.getStackTrace());
    }
  }
  
  public String getUserName()
  {
    return userName;
  }
  
  public void setUserName(String userName)
  {
    this.userName = userName;
  }
  
  public String getParentTransactionId()
  {
    return parentTransactionId;
  }
  
  public void setParentTransactionId(String parentTransactionId)
  {
    this.parentTransactionId = parentTransactionId;
  }
  
  public String getType()
  {
    return "transaction";
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public List<String> getSuccessSubJobIds()
  {
    if (successSubJobIds == null) {
      successSubJobIds = new ArrayList<>();
    }
    return successSubJobIds;
  }
  
  @Override
  public void setSuccessSubJobIds(List<String> successSubJobIds)
  {
    this.successSubJobIds = successSubJobIds;
  }
  
  @Override
  public List<String> getSubJobIds()
  {
    if (subJobIds == null) {
      subJobIds = new ArrayList<>();
    }
    return subJobIds;
  }
  
  @Override
  public void setSubJobIds(List<String> subJobIds)
  {
    this.subJobIds = subJobIds;
  }
  
  @Override
  public String getUiLanguage()
  {
    return uiLanguage;
  }
  
  @Override
  public void setUiLanguage(String uiLanguage)
  {
    this.uiLanguage = uiLanguage;
  }
  
  @Override
  public String getDataLanguage()
  {
    return dataLanguage;
  }
  
  @Override
  public void setDataLanguage(String dataLanguage)
  {
    this.dataLanguage = dataLanguage;
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
  
  @Override
  public String getPortalId()
  {
    return portalId;
  }
  
  @Override
  public void setPortalId(String portalId)
  {
    this.portalId = portalId;
  }
  
  @Override
  public String getOrganizationId()
  {
    return organizationId;
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    this.organizationId = organizationId;
  }
  
  @Override
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
  public String getLogicalCatalogId()
  {
    return logicalCatalogId;
  }
  
  @Override
  public void setLogicalCatalogId(String logicalCatelogId)
  {
    this.logicalCatalogId = logicalCatelogId;
  }
  
  @Override
  public String getSystemId()
  {
    return systemId;
  }
  
  @Override
  public void setSystemId(String systemId)
  {
    this.systemId = systemId;
  }
  
  @Override
  public String getEndpointType()
  {
    return endpointType;
  }
  
  @Override
  public void setEndpointType(String endpointType)
  {
    this.endpointType = endpointType;
  }
  
  @Override
  public String getUrlForTalend()
  {
    return urlForTalend;
  }
  
  @Override
  public void setUrlForTalend(String urlForTalend)
  {
    this.urlForTalend = urlForTalend;
  }
}
