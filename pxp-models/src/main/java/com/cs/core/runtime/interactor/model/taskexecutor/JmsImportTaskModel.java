package com.cs.core.runtime.interactor.model.taskexecutor;

import com.cs.core.config.interactor.model.articleimportcomponent.ComponentModel;
import com.cs.core.config.interactor.model.articleimportcomponent.IComponentModel;
import com.cs.core.config.interactor.model.transaction.ITransactionDataModel;
import com.cs.core.runtime.interactor.model.component.klassinstance.IDiAttributeVariantModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JmsImportTaskModel implements IJmsImportTaskModel {
  
  private static final long           serialVersionUID = 1L;
  protected IComponentModel           componentModel   = new ComponentModel();
  protected Map<String, Object>       contentMap       = new HashMap<>();
  protected String                    sourceId;
  protected String                    userId;
  protected Boolean                   isLinked;
  protected Set<String>               failedIds;
  protected Map<String, Object>       codeToIdsMap;
  protected Set<String>               successIds;
  protected List<Map<String, String>> failureList;
  protected String                    masterArticleId;
  protected IDiAttributeVariantModel  attributeVariantModel;
  protected Set<String>               idsToTransfer;
  protected Map<String, String>       inProgressIds;
  protected Set<String>               importedIds;
  protected ITransactionDataModel     transactionData;
  
  @Override
  public IComponentModel getComponentModel()
  {
    return componentModel;
  }
  
  @Override
  public void setComponentModel(IComponentModel componentModel)
  {
    this.componentModel = componentModel;
  }
  
  @Override
  public Map<String, Object> getContentMap()
  {
    return contentMap;
  }
  
  @Override
  public void setContentMap(Map<String, Object> contentMap)
  {
    this.contentMap = contentMap;
  }
  
  @Override
  public ITransactionDataModel getTransactionDataModel()
  {
    return transactionData;
  }
  
  @Override
  public void setTransactionDataModel(ITransactionDataModel transactionData)
  {
    this.transactionData = transactionData;
  }
  
  @Override
  public String getSourceId()
  {
    return sourceId;
  }
  
  @Override
  public void setSourceId(String sourceId)
  {
    this.sourceId = sourceId;
  }
  
  @Override
  public String getUserId()
  {
    return userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  @Override
  public Boolean getIsLinked()
  {
    return isLinked;
  }
  
  @Override
  public void setIsLinked(Boolean isLinked)
  {
    this.isLinked = isLinked;
  }
  
  @Override
  public Set<String> getFailedIds()
  {
    return failedIds;
  }
  
  @Override
  public void setFailedIds(Set<String> failedIds)
  {
    this.failedIds = failedIds;
  }
  
  @Override
  public Map<String, Object> getCodeToIdsMap()
  {
    return codeToIdsMap;
  }
  
  @Override
  public void setCodeToIdsMap(Map<String, Object> codeToIdsMap)
  {
    this.codeToIdsMap = codeToIdsMap;
  }
  
  @Override
  public Set<String> getSuccessIds()
  {
    return successIds;
  }
  
  @Override
  public void setSuccessIds(Set<String> successIds)
  {
    this.successIds = successIds;
  }
  
  @Override
  public List<Map<String, String>> getFailureList()
  {
    return failureList;
  }
  
  @Override
  public void setFailureList(List<Map<String, String>> failureList)
  {
    this.failureList = failureList;
  }
  
  @Override
  public String getMasterArticleId()
  {
    return masterArticleId;
  }
  
  @Override
  public void setMasterArticleId(String masterArticleId)
  {
    this.masterArticleId = masterArticleId;
  }
  
  @Override
  public Set<String> getIdsToTransfer()
  {
    return idsToTransfer;
  }
  
  @Override
  public void setIdsToTransfer(Set<String> idsToTransfer)
  {
    this.idsToTransfer = idsToTransfer;
  }
  
  @Override
  public Map<String, String> getInProgressIds()
  {
    return inProgressIds;
  }
  
  @Override
  public void setInProgressIds(Map<String, String> inProgressIds)
  {
    this.inProgressIds = inProgressIds;
  }
  
  @Override
  public Set<String> getImportedIds()
  {
    return importedIds;
  }
  
  @Override
  public void setImportedIds(Set<String> importedIds)
  {
    this.importedIds = importedIds;
  }
}
