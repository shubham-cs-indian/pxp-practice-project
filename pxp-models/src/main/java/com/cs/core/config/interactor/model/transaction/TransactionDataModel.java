package com.cs.core.config.interactor.model.transaction;

import java.util.List;

import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.InteractorData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;

public class TransactionDataModel implements ITransactionDataModel, ITransactionData {
  
  private static final long serialVersionUID = 1L;
  
  protected TransactionData transactionData;
  
  public TransactionDataModel(ITransactionData transactionData) throws Exception
  {
    this.transactionData = ObjectMapperUtil
        .readValue(ObjectMapperUtil.writeValueAsString(transactionData), TransactionData.class);
  }
  
  public TransactionData getTransactionData()
  {
    return this.transactionData;
  }
  
  @Override
  public InteractorData getInteractorData()
  {
    return this.transactionData.getInteractorData();
  }
  
  @Override
  @Deprecated
  public void setInteractorData(InteractorData interactorData)
  {
    this.transactionData.setInteractorData(interactorData);
  }
  
  @Override
  public String getRequestId()
  {
    return this.transactionData.getRequestId();
  }
  
  @Override
  @Deprecated
  public void setRequestId(String requestId)
  {
    this.transactionData.setRequestId(requestId);
  }
  
  @Override
  public String getUserId()
  {
    return this.transactionData.getUserId();
  }
  
  @Override
  @Deprecated
  public void setUserId(String userId)
  {
    this.transactionData.setUserId(userId);
  }
  
  @Override
  public Throwable getException()
  {
    return this.transactionData.getException();
  }
  
  @Override
  @Deprecated
  public void setException(Throwable exception)
  {
    this.transactionData.setException(exception);
  }
  
  @Override
  public String getUserName()
  {
    return this.transactionData.getUserName();
  }
  
  @Override
  @Deprecated
  public void setUserName(String userName)
  {
    this.transactionData.setUserName(userName);
  }
  
  @Override
  public List<String> getSuccessSubJobIds()
  {
    return this.transactionData.getSuccessSubJobIds();
  }
  
  @Override
  @Deprecated
  public void setSuccessSubJobIds(List<String> successSubJobIds)
  {
    this.transactionData.setSuccessSubJobIds(successSubJobIds);
  }
  
  @Override
  public List<String> getSubJobIds()
  {
    return this.transactionData.getSubJobIds();
  }
  
  @Override
  @Deprecated
  public void setSubJobIds(List<String> subJobIds)
  {
    this.transactionData.setSubJobIds(subJobIds);
  }
  
  @Override
  public String getPhysicalCatalogId()
  {
    return this.transactionData.getPhysicalCatalogId();
  }
  
  @Override
  @Deprecated
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    this.transactionData.setPhysicalCatalogId(physicalCatalogId);
  }
  
  @Override
  public String getOrganizationId()
  {
    return this.transactionData.getOrganizationId();
  }
  
  @Override
  @Deprecated
  public void setOrganizationId(String organizationId)
  {
    this.transactionData.setOrganizationId(organizationId);
  }
  
  @Override
  public String getEndpointId()
  {
    return this.transactionData.getEndpointId();
  }
  
  @Override
  @Deprecated
  public void setEndpointId(String endpointId)
  {
    this.transactionData.setEndpointId(endpointId);
  }
  
  @Override
  public String getLogicalCatalogId()
  {
    return this.transactionData.getLogicalCatalogId();
  }
  
  @Override
  @Deprecated
  public void setLogicalCatalogId(String logicalCatalogId)
  {
    this.transactionData.setLogicalCatalogId(logicalCatalogId);
  }
  
  @Override
  public String getSystemId()
  {
    return this.transactionData.getSystemId();
  }
  
  @Override
  @Deprecated
  public void setSystemId(String systemId)
  {
    this.transactionData.setSystemId(systemId);
  }
  
  @Override
  public String getEndpointType()
  {
    return this.transactionData.getEndpointType();
  }
  
  @Override
  @Deprecated
  public void setEndpointType(String systemId)
  {
    this.transactionData.setEndpointType(systemId);
  }
  
  @Override
  public String getUrlForTalend()
  {
    return this.transactionData.getUrlForTalend();
  }
  
  @Override
  @Deprecated
  public void setUrlForTalend(String urlForTalend)
  {
    this.transactionData.setUrlForTalend(urlForTalend);
  }
  
  @Override
  public String getRequestMethod()
  {
    return this.transactionData.getRequestMethod();
  }
  
  @Override
  @Deprecated
  public void setRequestMethod(String requestMethod)
  {
    this.transactionData.setRequestMethod(requestMethod);
  }
  
  @Override
  public String getUseCase()
  {
    return this.transactionData.getUseCase();
  }
  
  @Override
  @Deprecated
  public void setUseCase(String useCase)
  {
    this.transactionData.setUseCase(useCase);
  }
  
  @Override
  public Long getStartTime()
  {
    return this.transactionData.getStartTime();
  }
  
  @Override
  @Deprecated
  public void setStartTime(Long startTime)
  {
    this.transactionData.setStartTime(startTime);
  }
  
  @Override
  public Long getEndTime()
  {
    return this.transactionData.getEndTime();
  }
  
  @Override
  @Deprecated
  public void setEndTime(Long endTime)
  {
    this.transactionData.setEndTime(endTime);
  }
  
  @Override
  public Long getTurnAroundTime()
  {
    return this.transactionData.getTurnAroundTime();
  }
  
  @Override
  public String getExecutionStatus()
  {
    return this.transactionData.getExecutionStatus();
  }
  
  @Override
  @Deprecated
  public void setExecutionStatus(String executionStatus)
  {
    this.transactionData.setExecutionStatus(executionStatus);
  }
  
  @Override
  public String getId()
  {
    return this.transactionData.getId();
  }
  
  @Override
  @Deprecated
  public void setId(String id)
  {
    this.transactionData.setId(id);
  }
  
  @Override
  public String getType()
  {
    return this.transactionData.getType();
  }
  
  @Override
  @Deprecated
  public void setType(String type)
  {
    this.transactionData.setType(type);
  }
  
  @Override
  public String getParentTransactionId()
  {
    return this.transactionData.getParentTransactionId();
  }
  
  @Override
  @Deprecated
  public void setParentTransactionId(String parentTransactionId)
  {
    this.transactionData.setParentTransactionId(parentTransactionId);
  }
  
  @Override
  @Deprecated
  public String getUiLanguage()
  {
    return this.transactionData.getUiLanguage();
  }
  
  @Override
  @Deprecated
  public void setUiLanguage(String uiLanguage)
  {
    this.transactionData.setUiLanguage(uiLanguage);
  }
  
  @Override
  @Deprecated
  public String getDataLanguage()
  {
    return this.transactionData.getDataLanguage();
  }
  
  @Override
  @Deprecated
  public void setDataLanguage(String dataLanguage)
  {
    this.transactionData.setDataLanguage(dataLanguage);
  }
  
  @Override
  public String getPortalId()
  {
    return transactionData.getPortalId();
  }
  
  @Override
  public void setPortalId(String portalId)
  {
    this.transactionData.setPortalId(portalId);
  }
  
  @Override
  public Long getLevel()
  {
    return transactionData.getLevel();
  }
  
  @Override
  public void setLevel(Long level)
  {
    this.transactionData.setLevel(level);
  }
}
