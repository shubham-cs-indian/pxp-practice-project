package com.cs.core.config.interactor.model.articleimportcomponent;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;

import java.util.List;
import java.util.Map;

public interface IComponentModel extends IIdsListParameterModel, IModel, IIdParameterModel {
  
  public static final String ID_TRANSACTION              = "idTransaction";
  public static final String TRANSACTION_THREAD_DATA     = "transactionThreadData";
  public static final String PROCESS_CONTEXT             = "processContext";
  public static final String PARAMNETERS                 = "paramneters";
  public static final String COMPONENT_ID                = "componentId";
  public static final String ORCHESTRATOR_INSTANCE_MODEL = "orchestratorInstanceModel";
  public static final String COMPONENT_MAP               = "componentMap";
  public static final String COMPONENT_INSTANCE_ID       = "componentInstanceId";
  public static final String PROCESS_INSTANCE_ID         = "processInstanceId";
  public static final String FILE_NAME_FOR_EXPORT        = "fileNameForExport";
  public static final String COLLECTION_ID               = "collectionId";
  public static final String FILTER_RESULTS_TO_SAVE      = "filterResultsToSave";
  public static final String BOOKMARK_ID                 = "bookmarkId";
  public static final String ENDPOINT_ID                 = "endpointId";
  public static final String LOGICAL_CATALOG_ID          = "logicalCatalogId";
  public static final String ORGANIZATION_ID             = "organizationId";
  public static final String SYSTEM_ID                   = "systemId";
  public static final String PHYSICAL_CATALOG_ID         = "physicalCatalogId";
  public static final String FILE_INSTANCE_ID_FOR_EXPORT = "fileInstanceIdForExport";
  public static final String SYSTEM_COMPONENT_ID         = "systemComponentId";
  public static final String COMPONENT_LABEL             = "componentLabel";
  public static final String TRIGGER_EVENT               = "triggerEvent";
  public static final String PROTAL_ID                   = "portalId";
  public static final String ARTICLE_IDS_TO_TRANSFER     = "articleIdsToTransfer";
  public static final String ASSET_IDS_TO_TRANSFER       = "assetIdsToTransfer";
  
  public IProcessContext getProcessContext();
  
  public void setProcessContext(IProcessContext processContext);
  
  public Map<String, Object> getParameters();
  
  public void setParameters(Map<String, Object> paramneters);
  
  public String getComponentId();
  
  public void setComponentId(String componentId);
  
  public IOrchestratorInstanceModel getOrchestratorInstanceModel();
  
  public void setOrchestratorInstanceModel(IOrchestratorInstanceModel orchestratorInstanceModel);
  
  public IOrchestratorModel getComponentMap();
  
  public void setComponentMap(IOrchestratorModel componentMap);
  
  public String getComponentInstanceId();
  
  public void setComponentInstanceId(String componentInstanceId);
  
  public String getProcessInstanceId();
  
  public void setProcessInstanceId(String processInstanceId);
  
  public String getFileNameForExport();
  
  public void setFileNameForExport(String fileNameForExport);
  
  public String getCollectionId();
  
  public void setCollectionId(String collectionId);
  
  public IGetKlassInstanceTreeStrategyModel getFilterResultsToSave();
  
  public void setFilterResultsToSave(IGetKlassInstanceTreeStrategyModel filterResultsToSave);
  
  public String getBookmarkId();
  
  public void setBookmarkId(String bookmarkId);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public String getLogicalCatalogId();
  
  public void setLogicalCatalogId(String logicalCatalogId);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public String getSystemId();
  
  public void setSystemId(String systemId);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getFileInstanceIdForExport();
  
  public void setFileInstanceIdForExport(String fileInstanceIdForExport);
  
  public String getIdTransaction();
  
  public void setIdTransaction(String idTransaction);
  
  public String getSystemComponentId();
  
  public void setSystemComponentId(String systemComponentId);
  
  public String getComponentLabel();
  
  public void setComponentLabel(String componentLabel);
  
  public String getDataLanguage();
  
  public void setDataLanguage(String dataLanguage);
  
  public String getUiLanguage();
  
  public void setUiLanguage(String uiLanguage);
  
  public boolean getTriggerEvent();
  
  public void setTriggerEvent(boolean triggerEvent);
  
  public String getPortalId();
  
  public void setPortalId(String portalId);
  
  public List<String> getArticleIdsToTransfer();
  
  public void setArticleIdsToTransfer(List<String> articleIdsToTransfer);
  
  public List<String> getAssetIdsToTransfer();
  
  public void setAssetIdsToTransfer(List<String> list);
}
