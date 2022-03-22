package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.config.interactor.model.endpoint.GetMappingForImportResponseModel;
import com.cs.core.config.interactor.model.endpoint.IGetMappingForImportResponseModel;
import com.cs.core.config.interactor.model.transaction.ITransactionDataModel;

import java.util.*;

public class VariantOnboardingModel implements IVariantOnboardingModel {
  
  private static final long                          serialVersionUID = 1L;
  protected String                                   userId;
  protected String                                   klassInstanceId;
  protected Set<String>                              importedIds;
  protected Set<String>                              importedIdsToTransfer;
  protected String                                   indexName;
  protected Set<String>                              failedIds;
  protected Map<String, String>                      inProgressIds;
  protected Boolean                                  isAttributeVariant;
  protected String                                   attributeColumn;
  protected String                                   attributeValueColumn;
  protected String                                   variantType;
  protected String                                   contextId;
  protected List<Map<String, Object>>                variantsDetails;
  protected IGetMappingForImportResponseModel        mappings;
  protected List<String>                             parentVariantInstanceColumn;
  protected List<String>                             primaryKeyColumn;
  protected List<String>                             tagForVarient;
  protected HashMap<String, HashMap<String, Object>> tagValuesMapping;
  protected Map<String, List<String>>                attributesMapping;
  protected String                                   parentContextId;
  protected String                                   originalInstanceId;
  protected ITransactionDataModel                    transactionData;
  protected List<String>                             dateAttributeIds;
  protected String                                   fromDateColumnName;
  protected String                                   toDateColumnName;
  protected String                                   processInstanceId;
  protected String                                   componentId;
  
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
  public Set<String> getImportedIds()
  {
    return importedIds;
  }
  
  @Override
  public void setImportedIds(Set<String> importedIds)
  {
    if (importedIds == null) {
      importedIds = new HashSet<>();
    }
    this.importedIds = importedIds;
  }
  
  @Override
  public String getIndexName()
  {
    return indexName;
  }
  
  @Override
  public void setIndexName(String indexName)
  {
    this.indexName = indexName;
  }
  
  @Override
  public Set<String> getFailedIds()
  {
    return failedIds;
  }
  
  @Override
  public void setFailedIds(Set<String> failedIds)
  {
    if (failedIds == null) {
      failedIds = new HashSet<>();
    }
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
    if (inProgressIds == null) {
      inProgressIds = new HashMap<>();
    }
    this.inProgressIds = inProgressIds;
  }
  
  @Override
  public Boolean getIsAttributeVariant()
  {
    return isAttributeVariant;
  }
  
  @Override
  public void setIsAttributeVariant(Boolean isAttributeVariant)
  {
    this.isAttributeVariant = isAttributeVariant;
  }
  
  @Override
  public String getAttributeColumn()
  {
    return attributeColumn;
  }
  
  @Override
  public void setAttributeColumn(String attributeColumn)
  {
    this.attributeColumn = attributeColumn;
  }
  
  @Override
  public String getAttributeValueColumn()
  {
    return attributeValueColumn;
  }
  
  @Override
  public void setAttributeValueColumn(String attributeValueColumn)
  {
    this.attributeValueColumn = attributeValueColumn;
  }
  
  @Override
  public String getVariantType()
  {
    return variantType;
  }
  
  @Override
  public void setVariantType(String variantType)
  {
    this.variantType = variantType;
  }
  
  @Override
  public String getContextId()
  {
    return contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
  
  @Override
  public List<Map<String, Object>> getVariantsDetails()
  {
    return variantsDetails;
  }
  
  @Override
  public void setVariantsDetails(List<Map<String, Object>> variantsDetails)
  {
    this.variantsDetails = variantsDetails;
  }
  
  @Override
  public IGetMappingForImportResponseModel getMappings()
  {
    return mappings;
  }
  
  @Override
  public void setMappings(IGetMappingForImportResponseModel mappings)
  {
    if (mappings == null) {
      mappings = new GetMappingForImportResponseModel();
    }
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
    if (tagValuesMapping == null) {
      tagValuesMapping = new HashMap<>();
    }
    this.tagValuesMapping = tagValuesMapping;
  }
  
  @Override
  public Map<String, List<String>> getAttributesMapping()
  {
    return attributesMapping;
  }
  
  @Override
  public void setAttributesMapping(Map<String, List<String>> attributesMapping)
  {
    if (attributesMapping == null) {
      attributesMapping = new HashMap<>();
    }
    this.attributesMapping = attributesMapping;
  }
  
  @Override
  public List<String> getParentVariantInstanceColumn()
  {
    return parentVariantInstanceColumn;
  }
  
  @Override
  public void setParentVariantInstanceColumn(List<String> parentVariantInstanceColumn)
  {
    if (parentVariantInstanceColumn == null) {
      parentVariantInstanceColumn = new ArrayList<>();
    }
    this.parentVariantInstanceColumn = parentVariantInstanceColumn;
  }
  
  @Override
  public List<String> getPrimaryKeyColumn()
  {
    return primaryKeyColumn;
  }
  
  @Override
  public void setPrimaryKeyColumn(List<String> primaryKeyColumn)
  {
    if (primaryKeyColumn == null) {
      primaryKeyColumn = new ArrayList<>();
    }
    this.primaryKeyColumn = primaryKeyColumn;
  }
  
  @Override
  public List<String> getTagForVarient()
  {
    return tagForVarient;
  }
  
  @Override
  public void setTagForVarient(List<String> tagForVarient)
  {
    if (tagForVarient == null) {
      tagForVarient = new ArrayList<>();
    }
    this.tagForVarient = tagForVarient;
  }
  
  @Override
  public String getParentContextId()
  {
    return parentContextId;
  }
  
  @Override
  public void setParentContextId(String parentContextId)
  {
    this.parentContextId = parentContextId;
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
  public String getFromDateColumnName()
  {
    return fromDateColumnName;
  }
  
  @Override
  public void setFromDateColumnName(String fromDateColumnName)
  {
    this.fromDateColumnName = fromDateColumnName;
  }
  
  @Override
  public String getToDateColumnName()
  {
    return toDateColumnName;
  }
  
  @Override
  public void setToDateColumnName(String toDateColumnName)
  {
    this.toDateColumnName = toDateColumnName;
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
  public String getComponentId()
  {
    return componentId;
  }
  
  @Override
  public void setComponentId(String componentId)
  {
    this.componentId = componentId;
  }
}
