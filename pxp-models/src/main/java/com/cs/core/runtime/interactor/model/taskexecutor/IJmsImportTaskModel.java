package com.cs.core.runtime.interactor.model.taskexecutor;

import com.cs.core.config.interactor.model.articleimportcomponent.IComponentModel;
import com.cs.core.config.interactor.model.transaction.ITransactionDataModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IJmsImportTaskModel extends IModel {
  
  public static final String COMPONENT_MODEL  = "componentModel";
  public static final String CONTENT_MAP      = "contentsMap";
  public static final String TRANSACTION_DATA = "transactionData";
  public static final String FAILED_IDS       = "failedIds";
  public static final String CODE_TO_IDS_MAP  = "codeToIdsMap";
  public static final String FAILURE_LIST     = "failureList";
  public static final String SOURCE_ID        = "sourceId";
  public static final String IDS_TO_TRANSFER  = "idsToTransfer";
  public static final String SUCCESS_IDS      = "successIds";
  public static final String INPROGRESS_IDS   = "inProgressIds";
  public static final String IMPORTED_IDS     = "importedIds";
  public static final String USER_ID          = "userId";
  
  public Map<String, Object> getContentMap();
  
  public void setContentMap(Map<String, Object> contentMap);
  
  public IComponentModel getComponentModel();
  
  public void setComponentModel(IComponentModel componentModel);
  
  public ITransactionDataModel getTransactionDataModel();
  
  public void setTransactionDataModel(ITransactionDataModel transactionData);
  
  public String getSourceId();
  
  public void setSourceId(String sourceId);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public Boolean getIsLinked();
  
  public void setIsLinked(Boolean isLinked);
  
  public Set<String> getFailedIds();
  
  public void setFailedIds(Set<String> failedIds);
  
  public Map<String, Object> getCodeToIdsMap();
  
  public void setCodeToIdsMap(Map<String, Object> codeToIdsMap);
  
  public Set<String> getSuccessIds();
  
  public void setSuccessIds(Set<String> successIds);
  
  public List<Map<String, String>> getFailureList();
  
  public void setFailureList(List<Map<String, String>> failureList);
  
  public String getMasterArticleId();
  
  public void setMasterArticleId(String masterArticleId);
  
  public Set<String> getIdsToTransfer();
  
  public void setIdsToTransfer(Set<String> idsToTransfer);
  
  public Map<String, String> getInProgressIds();
  
  public void setInProgressIds(Map<String, String> inProgressIds);
  
  public Set<String> getImportedIds();
  
  public void setImportedIds(Set<String> importedIds);
}
