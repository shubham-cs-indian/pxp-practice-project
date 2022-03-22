package com.cs.core.config.interactor.model.articleimportcomponent;

import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComponentModel extends IdsListParameterModel implements IComponentModel {
  
  private static final long                    serialVersionUID     = 1L;
  
  protected IProcessContext                    processContext;
  protected Map<String, Object>                parameters;
  protected String                             componentId;
  protected IOrchestratorInstanceModel         orchestratorInstanceModel;
  protected IOrchestratorModel                 componentMap;
  protected String                             componentInstanceId;
  protected String                             processInstanceId;
  protected String                             id;
  protected String                             fileNameForExport;
  protected String                             collectionId;
  protected IGetKlassInstanceTreeStrategyModel filterResultsToSave;
  protected String                             bookmarkId;
  protected String                             endpointId;
  protected String                             systemId;
  protected String                             physicalCatalogId;
  protected String                             organizationId;
  protected String                             logicalCatalogId;
  protected String                             fileInstanceIdForExport;
  protected String                             systemComponentId;
  protected String                             componentLabel;
  protected String                             dataLanguage;
  protected String                             uiLanguage;
  protected boolean                            triggerEvent;
  protected String                             portalId;
  protected List<String>                       articleIdsToTransfer = new ArrayList<>();
  protected List<String>                       assetIdsToTransfer   = new ArrayList<>();
  private String                               idTransaction;
  
  @Override
  public String getBookmarkId()
  {
    return bookmarkId;
  }
  
  @Override
  public void setBookmarkId(String bookmarkId)
  {
    this.bookmarkId = bookmarkId;
  }
  
  @Override
  public String getCollectionId()
  {
    return collectionId;
  }
  
  @Override
  public void setCollectionId(String collectionId)
  {
    this.collectionId = collectionId;
  }
  
  @Override
  public IGetKlassInstanceTreeStrategyModel getFilterResultsToSave()
  {
    return filterResultsToSave;
  }
  
  @Override
  @JsonDeserialize(as = GetKlassInstanceTreeStrategyModel.class)
  public void setFilterResultsToSave(IGetKlassInstanceTreeStrategyModel filterResultsToSave)
  {
    this.filterResultsToSave = filterResultsToSave;
  }
  
  @Override
  public IProcessContext getProcessContext()
  {
    return processContext;
  }
  
  @Override
  @JsonDeserialize(as = ProcessContext.class)
  public void setProcessContext(IProcessContext processContext)
  {
    this.processContext = processContext;
  }
  
  @Override
  public Map<String, Object> getParameters()
  {
    return parameters;
  }
  
  @Override
  public void setParameters(Map<String, Object> paramneters)
  {
    this.parameters = paramneters;
  }
  
  @Override
  public String getComponentId()
  {
    return componentId;
  }
  
  @Override
  public void setComponentId(String componentId)
  {
    this.componentId = componentId;
  }
  
  @Override
  public IOrchestratorInstanceModel getOrchestratorInstanceModel()
  {
    return orchestratorInstanceModel;
  }
  
  @Override
  public void setOrchestratorInstanceModel(IOrchestratorInstanceModel orchestratorInstanceModel)
  {
    this.orchestratorInstanceModel = orchestratorInstanceModel;
  }
  
  @Override
  public IOrchestratorModel getComponentMap()
  {
    return componentMap;
  }
  
  @Override
  public void setComponentMap(IOrchestratorModel componentMap)
  {
    this.componentMap = componentMap;
  }
  
  @Override
  public String getComponentInstanceId()
  {
    return componentInstanceId;
  }
  
  @Override
  public void setComponentInstanceId(String componentInstanceId)
  {
    this.componentInstanceId = componentInstanceId;
  }
  
  @Override
  public String getProcessInstanceId()
  {
    return processInstanceId;
  }
  
  @Override
  public void setProcessInstanceId(String processInstanceId)
  {
    this.processInstanceId = processInstanceId;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public List<IRoleModel> getRoles()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setRoles(List<IRoleModel> roles)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getCurrentUserId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setCurrentUserId(String currentUserId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setType(String type)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getFileNameForExport()
  {
    return fileNameForExport;
  }
  
  @Override
  public void setFileNameForExport(String fileNameForExport)
  {
    this.fileNameForExport = fileNameForExport;
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
  public String getLogicalCatalogId()
  {
    return logicalCatalogId;
  }
  
  @Override
  public void setLogicalCatalogId(String logicalCatalogId)
  {
    this.logicalCatalogId = logicalCatalogId;
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
  public String getFileInstanceIdForExport()
  {
    return fileInstanceIdForExport;
  }
  
  @Override
  public void setFileInstanceIdForExport(String fileInstanceIdForExport)
  {
    this.fileInstanceIdForExport = fileInstanceIdForExport;
  }
  
  @Override
  public String getIdTransaction()
  {
    return idTransaction;
  }
  
  @Override
  public void setIdTransaction(String idTransaction)
  {
    this.idTransaction = idTransaction;
  }
  
  @Override
  public String getSystemComponentId()
  {
    return systemComponentId;
  }
  
  @Override
  public void setSystemComponentId(String systemComponentId)
  {
    this.systemComponentId = systemComponentId;
  }
  
  @Override
  public String getComponentLabel()
  {
    return componentLabel;
  }
  
  @Override
  public void setComponentLabel(String componentLabel)
  {
    this.componentLabel = componentLabel;
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
  public String getUiLanguage()
  {
    return uiLanguage;
  }
  
  @Override
  public void setUiLanguage(String uiLanguage)
  {
    this.uiLanguage = uiLanguage;
  }
  
  public boolean getTriggerEvent()
  {
    return triggerEvent;
  }
  
  @Override
  public void setTriggerEvent(boolean triggerEvent)
  {
    this.triggerEvent = triggerEvent;
  }
  
  public String getPortalId()
  {
    return portalId;
  }
  
  @Override
  public void setPortalId(String portalId)
  {
    this.portalId = portalId;
  }
  
  public List<String> getArticleIdsToTransfer()
  {
    return articleIdsToTransfer;
  }
  
  public void setArticleIdsToTransfer(List<String> articleIdsToTransfer)
  {
    this.articleIdsToTransfer = articleIdsToTransfer;
  }
  
  public List<String> getAssetIdsToTransfer()
  {
    return assetIdsToTransfer;
  }
  
  public void setAssetIdsToTransfer(List<String> assetIdsToTransfer)
  {
    this.assetIdsToTransfer = assetIdsToTransfer;
  }
}
