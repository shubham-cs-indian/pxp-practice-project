package com.cs.core.config.interactor.model.transfer;

import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.transaction.ITransactionDataModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetInstanceTypesResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;

import java.util.List;
import java.util.Map;

public interface ITransferDataRequestModel extends IModel {
  
  public static final String IDS                            = "ids";
  public static final String COMPONENT_ID                   = "componentId";
  public static final String DESTINATION_ENDPOINT_ID        = "destinationEndpointId";
  public static final String DESTINATION_ORGANIZATION_ID    = "destinationOrganizationId";
  public static final String DESTINATION_CATALOG_ID         = "destinationCatalogId";
  public static final String DATA_RULES                     = "dataRules";
  public static final String BOARDING_TYPE                  = "boardingType";
  public static final String DATA                           = "data";
  public static final String USECASE                        = "useCase";
  public static final String KLASS_DATA_RULES               = "klassDataRules";
  public static final String REFERENCED_DATA_RULES          = "referencedDataRules";
  public static final String TYPEID_IDENTIFIER_ATTRIBUTEIDS = "typeIdIdentifierAttributeIds";
  public static final String IS_JMS_EXPORT                  = "isJMSExport";
  public static final String OLD_AND_NEW_IDS_MAP            = "oldAndNewIdsMap";
  public static final String MODULE_ID                      = "moduleId";
  public static final String TYPES_MODEL                    = "typesModel";
  public static final String TRIGGER_EVENT                  = "triggerEvent";
  public static final String CONFIG_DETAILS                 = "configDetails";
  public static final String PROCESS_INSTANCE_ID            = "processInstanceId";
  public static final String SYSTEM_COMPONENT_ID            = "systemComponentId";
  public static final String TRANSACTION_DATA               = "transactionData";
  public static final String SOURCE_DESTINATION_IDS         = "sourceDestinationIds";
  public static final String PARENT_ID                      = "parentId";
  public static final String KLASS_INSTANCE_ID              = "klassInstanceId";
  public static final String VARIANT_INSTANCE_ID            = "variantInstanceId";
  public static final String ENTITY_TO_TRANSFER             = "entityToTransfer";
  public static final String FILTER_INFO                    = "filterInfo";
  public static final String ARTICLE_IDS_TO_TRANSFER        = "articleIdsToTransfer";
  public static final String ASSET_IDS_TO_TRANSFER          = "assetIdsToTransfer";
  
  public List<String> getIds();
  
  public void setIds(List<String> ids);
  
  public String getComponentId();
  
  public void setComponentId(String componentId);
  
  public String getDestinationEndpointId();
  
  public void setDestinationEndpointId(String endpointId);
  
  public List<IDataRuleModel> getDataRules();
  
  public void setDataRules(List<IDataRuleModel> dataRulesResponse);
  
  public String getDestinationOrganizationId();
  
  public void setDestinationOrganizationId(String destinationOrganizationId);
  
  public String getDestinationCatalogId();
  
  public void setDestinationCatalogId(String destinationCatalogId);
  
  public String getBoardingType();
  
  public void setBoardingType(String boardingType);
  
  public Object getData();
  
  public void setData(Object dataRequestSaveModel);
  
  public String getUseCase();
  
  public void setUseCase(String useCase);
  
  // key:klassId/taxonomyId
  public Map<String, List<String>> getTypeIdIdentifierAttributeIds();
  
  public void setTypeIdIdentifierAttributeIds(
      Map<String, List<String>> typeIdIdentifierAttributeIds);
  
  Map<String, List<String>> getKlassDataRules();
  
  void setKlassDataRules(Map<String, List<String>> klassDataRules);
  
  Map<String, IDataRuleModel> getReferencedDataRules();
  
  void setReferencedDataRules(Map<String, IDataRuleModel> referencedDataRules);
  
  public Boolean getIsJMSExport();
  
  public void setIsJMSExport(Boolean isJMSExport);
  
  public Map<String, String> getOldAndNewIdsMap();
  
  public void setOldAndNewIdsMap(Map<String, String> oldAndNewIdsMap);
  
  public String getModuleId();
  
  public void setModuleId(String moduleId);
  
  public IGetConfigDetailsModel getConfigDetails();
  
  public void setConfigDetails(IGetConfigDetailsModel configDetails);
  
  public IGetInstanceTypesResponseModel getTypesModel();
  
  public void setTypesModel(IGetInstanceTypesResponseModel typesModel);
  
  public boolean getTriggerEvent();
  
  public void setTriggerEvent(boolean triggerEvent);
  
  public String getProcessInstanceId();
  
  public void setProcessInstanceId(String processInstanceId);
  
  public String getSystemComponentId();
  
  public void setSystemComponentId(String systemComponentId);
  
  public ITransactionDataModel getTransactionData();
  
  public void setTransactionData(ITransactionDataModel transactionData);
  
  public Map<String, String> getSourceDestinationIds();
  
  public void setSourceDestinationIds(Map<String, String> sourceDestinationIds);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getVariantInstanceId();
  
  public void setVariantInstanceId(String entityToTransfer);
  
  public String getEntityToTransfer();
  
  public void setEntityToTransfer(String entityToTransfer);
  
  public IGetKlassInstanceTreeStrategyModel getFilterInfo();
  
  public void setFilterInfo(IGetKlassInstanceTreeStrategyModel filterInfo);
  
  public List<String> getArticleIdsToTransfer();
  
  public void setArticleIdsToTransfer(List<String> articleIdsToTransfer);
  
  public List<String> getAssetIdsToTransfer();
  
  public void setAssetIdsToTransfer(List<String> list);
  
  public String getSourceCatalogId();
  
  public void setSourceCatalogId(String sourceCatalogId);
}
