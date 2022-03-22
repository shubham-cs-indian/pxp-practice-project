package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.config.interactor.model.endpoint.IGetMappingForImportResponseModel;
import com.cs.core.config.interactor.model.transaction.ITransactionDataModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IVariantOnboardingModel extends IModel {
  
  public static final String MAPPINGS                    = "mappings";
  public static final String USERID                      = "userId";
  public static final String KLASSINSTANCEID             = "klassInstanceId";
  public static final String IMPORTEDIDS                 = "importedIds";
  public static final String IMPORTEDIDSTOTRANSFER       = "importedIdsToTransfer";
  public static final String CONTEXT                     = "context";
  public static final String INDEXNAME                   = "indexName";
  public static final String FAILEDIDS                   = "failedIds";
  public static final String INPROGRESSIDS               = "inProgressIds";
  public static final String ISATTRIBUTEVARIANT          = "isAttributeVariant";
  public static final String ATTRIBUTECOLUMN             = "attributeColumn";
  public static final String ATTRIBUTEVALUECOLUMN        = "attributeValueColumn";
  public static final String VARIANTTYPE                 = "variantType";
  public static final String CONTEXTID                   = "contextId";
  public static final String VARIANTSDETAILS             = "variantsDetails";
  public static final String TAGVALUESMAPPING            = "tagValuesMapping";
  public static final String ATTRIBUTESMAPPING           = "attributesMapping";
  public static final String PARENTVARIANTINSTANCECOLUMN = "parentVariantInstanceColumn";
  public static final String PRIMARYKEYCOLUMN            = "primaryKeyColumn";
  public static final String TAGFORVARIENT               = "tagForVarient";
  public static final String PARENTCONTEXTID             = "parentContextId";
  public static final String ORIGINAL_INSTANCE_ID        = "originalInstanceId";
  public static final String TRANSACTION_DATA            = "transactionData";
  public static final String DATE_ATTRIBUTE_IDS          = "dateAttributeIds";
  public static final String FROM_DATE_COLUMN_NAME       = "fromDateColumnName";
  public static final String TO_DATE_COLUMN_NAME         = "toDateColumnName";
  public static final String PROCESS_INSTANCE_ID         = "processInstanceId";
  public static final String COMPONENT_ID                = "componentId";
  
  public IGetMappingForImportResponseModel getMappings();
  
  public void setMappings(IGetMappingForImportResponseModel mappings);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public Set<String> getImportedIds();
  
  public void setImportedIds(Set<String> importedIds);
  
  public String getIndexName();
  
  public void setIndexName(String indexName);
  
  public Set<String> getFailedIds();
  
  public void setFailedIds(Set<String> failedIds);
  
  public Map<String, String> getInProgressIds();
  
  public void setInProgressIds(Map<String, String> inProgressIds);
  
  public Boolean getIsAttributeVariant();
  
  public void setIsAttributeVariant(Boolean isAttributeVariant);
  
  public String getAttributeColumn();
  
  public void setAttributeColumn(String attributeColumn);
  
  public String getAttributeValueColumn();
  
  public void setAttributeValueColumn(String attributeValueColumn);
  
  public String getVariantType();
  
  public void setVariantType(String variantType);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public List<Map<String, Object>> getVariantsDetails();
  
  public void setVariantsDetails(List<Map<String, Object>> variantsDetails);
  
  public HashMap<String, HashMap<String, Object>> getTagValuesMapping();
  
  public void setTagValuesMapping(HashMap<String, HashMap<String, Object>> tagValuesMapping);
  
  public Map<String, List<String>> getAttributesMapping();
  
  public void setAttributesMapping(Map<String, List<String>> attributesMapping);
  
  public List<String> getParentVariantInstanceColumn();
  
  public void setParentVariantInstanceColumn(List<String> parentVariantInstanceColumn);
  
  public List<String> getPrimaryKeyColumn();
  
  public void setPrimaryKeyColumn(List<String> primaryKeyColumn);
  
  public List<String> getTagForVarient();
  
  public void setTagForVarient(List<String> tagForVarient);
  
  public String getParentContextId();
  
  public void setParentContextId(String parentContextId);
  
  public String getOriginalInstanceId();
  
  public void setOriginalInstanceId(String originalInstanceId);
  
  public Set<String> getImportedIdsToTransfer();
  
  public void setImportedIdsToTransfer(Set<String> importedIdsToTransfer);
  
  public ITransactionDataModel getTransactionDataModel();
  
  public void setTransactionDataModel(ITransactionDataModel transactionData);
  
  public List<String> getDateAttributeIds();
  
  public void setDateAttributeIds(List<String> dateAttributeIds);
  
  public String getFromDateColumnName();
  
  public void setFromDateColumnName(String fromDateColumnName);
  
  public String getToDateColumnName();
  
  public void setToDateColumnName(String toDateColumnName);
  
  public String getProcessInstanceId();
  
  public void setProcessInstanceId(String processInstanceId);
  
  public String getComponentId();
  
  public void setComponentId(String componentId);
}
