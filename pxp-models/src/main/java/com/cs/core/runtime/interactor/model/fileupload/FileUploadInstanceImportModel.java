package com.cs.core.runtime.interactor.model.fileupload;

import com.cs.core.config.interactor.model.articleimportcomponent.IComponentParameterModel;
import com.cs.core.config.interactor.model.asset.IAssetKeysModel;
import com.cs.core.config.interactor.model.endpoint.IGetMappingForImportResponseModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.runtime.interactor.model.configuration.ContentIdentifierModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileUploadInstanceImportModel extends ContentIdentifierModel
    implements IFileUploadInstanceImportModel {
  
  private static final long                          serialVersionUID = 1L;
  
  protected IGetMappingForImportResponseModel        mappings;
  protected HashMap<String, HashMap<String, Object>> tagValuesMapping;
  protected Map<String, Object>                      itemMap;
  protected IComponentParameterModel                 componentParameterModel;
  protected String                                   userId;
  protected Set<String>                              importedIds;
  protected Set<String>                              importedIdsToTransfer;
  protected Set<String>                              failedIds;
  protected Map<String, String>                      inProgressIds;
  protected String                                   componentId;
  protected List<String>                             dateAttributeIds;
  protected IMappingModel                            propertyMapping;
  protected String                                   endpointId;
  protected String                                   systemId;
  protected String                                   organizationId;
  protected String                                   physicalCatalogId;
  protected String                                   processInstanceId;
  protected String                                   klassInstanceId;
  protected String                                   type;
  protected IAssetKeysModel                          assetKeysModel;
  protected String                                   filePath;
  protected String                                   originalInstanceId;
  protected String                                   dataLanguage;
  protected String                                   uiLanguage;
  
  @Override
  public IGetMappingForImportResponseModel getMappings()
  {
    return mappings;
  }
  
  @Override
  public void setMappings(IGetMappingForImportResponseModel mappings)
  {
    this.mappings = mappings;
  }
  
  @Override
  public HashMap<String, HashMap<String, Object>> getTagValuesMapping()
  {
    return tagValuesMapping;
  }
  
  @Override
  public void setTagValuesMapping(HashMap<String, HashMap<String, Object>> tagValuesMapping)
  {
    this.tagValuesMapping = tagValuesMapping;
  }
  
  @Override
  public Map<String, Object> getItemMap()
  {
    return itemMap;
  }
  
  @Override
  public void setItemMap(Map<String, Object> itemMap)
  {
    this.itemMap = itemMap;
  }
  
  @Override
  public IComponentParameterModel getComponentParameterModel()
  {
    return componentParameterModel;
  }
  
  @Override
  public void setComponentParameterModel(IComponentParameterModel componentParameterModel)
  {
    this.componentParameterModel = componentParameterModel;
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
  public Set<String> getImportedIds()
  {
    return importedIds;
  }
  
  @Override
  public void setImportedIds(Set<String> importedIds)
  {
    this.importedIds = importedIds;
  }
  
  @Override
  public Set<String> getImportedIdsToTransfer()
  {
    return importedIdsToTransfer;
  }
  
  @Override
  public void setImportedIdsToTransfer(Set<String> importedIdsToTransfer)
  {
    this.importedIdsToTransfer = importedIdsToTransfer;
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
  public List<String> getDateAttributeIds()
  {
    return dateAttributeIds;
  }
  
  @Override
  public void setDateAttributeIds(List<String> dateAttributeIds)
  {
    this.dateAttributeIds = dateAttributeIds;
  }
  
  @Override
  public IMappingModel getPropertyMapping()
  {
    return propertyMapping;
  }
  
  @Override
  public void setPropertyMapping(IMappingModel propertyMapping)
  {
    this.propertyMapping = propertyMapping;
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
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public IAssetKeysModel getAssetKeysModel()
  {
    return assetKeysModel;
  }
  
  @Override
  public void setAssetKeysModel(IAssetKeysModel assetKeysModel)
  {
    this.assetKeysModel = assetKeysModel;
  }
  
  @Override
  public String getFilePath()
  {
    return filePath;
  }
  
  @Override
  public void setFilePath(String filePath)
  {
    this.filePath = filePath;
  }
  
  @Override
  public String getOriginalInstanceId()
  {
    return originalInstanceId;
  }
  
  @Override
  public void setOriginalInstanceId(String originalInstanceId)
  {
    this.originalInstanceId = originalInstanceId;
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
}
