package com.cs.core.config.interactor.model.transfer;

import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.config.interactor.model.transaction.ITransactionDataModel;
import com.cs.core.runtime.interactor.model.klassinstance.GetInstanceTypesResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetInstanceTypesResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.model.templating.AbstractGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferDataRequestModel implements ITransferDataRequestModel {
  
  private static final long                    serialVersionUID     = 1L;
  protected List<String>                       ids                  = new ArrayList<>();
  protected List<IDataRuleModel>               dataRules            = new ArrayList<>();
  protected String                             componentId;
  protected String                             destinationEndpointId;
  protected String                             destinationOrganizationId;
  protected String                             destinationCatalogId;
  protected String                             sourceCatalogId;
  protected String                             boardingType;
  protected Map<String, List<String>>          typeIdIdentifierAttributeIds;
  protected Map<String, IDataRuleModel>        referencedDataRules;
  protected Map<String, List<String>>          klassDataRules;
  protected Object                             data;
  protected String                             useCase;
  protected Boolean                            isJMSExport          = false;
  protected Map<String, String>                oldAndNewIdsMap;
  protected String                             moduleId;
  protected IGetInstanceTypesResponseModel     typesModel;
  protected boolean                            triggerEvent         = true;
  protected IGetConfigDetailsModel             configDetails;
  protected String                             processInstanceId;
  protected String                             systemComponentId;
  protected ITransactionDataModel              transactionData;
  protected IGetKlassInstanceTreeStrategyModel filterInfo;
  protected List<String>                       articleIdsToTransfer = new ArrayList<>();
  protected List<String>                       assetIdsToTransfer   = new ArrayList<>();
  private Map<String, String>                  sourceDestinationIds;
  private String                               parentId;
  private String                               klassInstanceId;
  private String                               VariantInstanceId;
  private String                               entityToTransfer;
  
  // Default Constructor
  public TransferDataRequestModel()
  {
  }
  
  // Copy Constructor
  public TransferDataRequestModel(ITransferDataRequestModel model)
  {
    this.ids = model.getIds();
    this.dataRules = model.getDataRules();
    this.componentId = model.getComponentId();
    this.destinationEndpointId = model.getDestinationEndpointId();
    this.destinationOrganizationId = model.getDestinationOrganizationId();
    this.destinationCatalogId = model.getDestinationCatalogId();
    this.boardingType = model.getBoardingType();
    this.typeIdIdentifierAttributeIds = model.getTypeIdIdentifierAttributeIds();
    this.referencedDataRules = model.getReferencedDataRules();
    this.klassDataRules = model.getKlassDataRules();
    this.data = model.getData();
    this.useCase = model.getUseCase();
    this.isJMSExport = model.getIsJMSExport();
    this.oldAndNewIdsMap = model.getOldAndNewIdsMap();
    this.moduleId = model.getModuleId();
    this.typesModel = model.getTypesModel();
    this.triggerEvent = model.getTriggerEvent();
    this.configDetails = model.getConfigDetails();
    this.processInstanceId = model.getProcessInstanceId();
    this.systemComponentId = model.getSystemComponentId();
    this.transactionData = model.getTransactionData();
  }
  
  @Override
  public Boolean getIsJMSExport()
  {
    return isJMSExport;
  }
  
  @Override
  public void setIsJMSExport(Boolean isJMSExport)
  {
    this.isJMSExport = isJMSExport;
  }
  
  @Override
  public Object getData()
  {
    return data;
  }
  
  @Override
  public void setData(Object data)
  {
    this.data = data;
  }
  
  @Override
  public String getComponentId()
  {
    return this.componentId;
  }
  
  @Override
  public void setComponentId(String componentId)
  {
    this.componentId = componentId;
  }
  
  @Override
  public String getDestinationEndpointId()
  {
    return this.destinationEndpointId;
  }
  
  @Override
  public void setDestinationEndpointId(String destinationEndpointId)
  {
    this.destinationEndpointId = destinationEndpointId;
  }
  
  @Override
  public String getDestinationOrganizationId()
  {
    return this.destinationOrganizationId;
  }
  
  @Override
  public void setDestinationOrganizationId(String destinationOrganizationId)
  {
    this.destinationOrganizationId = destinationOrganizationId;
  }
  
  @Override
  public String getDestinationCatalogId()
  {
    return this.destinationCatalogId;
  }
  
  @Override
  public void setDestinationCatalogId(String destinationCatalogId)
  {
    this.destinationCatalogId = destinationCatalogId;
  }
  
  @Override
  public List<String> getIds()
  {
    return this.ids;
  }
  
  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
  
  @Override
  public List<IDataRuleModel> getDataRules()
  {
    return this.dataRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = DataRuleModel.class)
  public void setDataRules(List<IDataRuleModel> dataRules)
  {
    this.dataRules = dataRules;
  }
  
  @Override
  public String getBoardingType()
  {
    return boardingType;
  }
  
  @Override
  public void setBoardingType(String boardingType)
  {
    this.boardingType = boardingType;
  }
  
  @Override
  public String getUseCase()
  {
    return this.useCase;
  }
  
  @Override
  public void setUseCase(String useCase)
  {
    this.useCase = useCase;
  }
  
  @Override
  public Map<String, List<String>> getTypeIdIdentifierAttributeIds()
  {
    return typeIdIdentifierAttributeIds;
  }
  
  @Override
  public void setTypeIdIdentifierAttributeIds(
      Map<String, List<String>> typeIdIdentifierAttributeIds)
  {
    this.typeIdIdentifierAttributeIds = typeIdIdentifierAttributeIds;
  }
  
  @Override
  public Map<String, List<String>> getKlassDataRules()
  {
    return klassDataRules;
  }
  
  @Override
  public void setKlassDataRules(Map<String, List<String>> klassDataRules)
  {
    this.klassDataRules = klassDataRules;
  }
  
  @Override
  public Map<String, IDataRuleModel> getReferencedDataRules()
  {
    return referencedDataRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = DataRuleModel.class)
  public void setReferencedDataRules(Map<String, IDataRuleModel> referencedDataRules)
  {
    this.referencedDataRules = referencedDataRules;
  }
  
  @Override
  public Map<String, String> getOldAndNewIdsMap()
  {
    if (oldAndNewIdsMap == null) {
      oldAndNewIdsMap = new HashMap<String, String>();
    }
    return oldAndNewIdsMap;
  }
  
  @Override
  public void setOldAndNewIdsMap(Map<String, String> oldAndNewIdsMap)
  {
    this.oldAndNewIdsMap = oldAndNewIdsMap;
  }
  
  @Override
  public String getModuleId()
  {
    return moduleId;
  }
  
  @Override
  public void setModuleId(String moduleId)
  {
    this.moduleId = moduleId;
  }
  
  @Override
  public IGetInstanceTypesResponseModel getTypesModel()
  {
    return typesModel;
  }
  
  @Override
  @JsonDeserialize(as = GetInstanceTypesResponseModel.class)
  public void setTypesModel(IGetInstanceTypesResponseModel typesModel)
  {
    this.typesModel = typesModel;
  }
  
  @Override
  public boolean getTriggerEvent()
  {
    return triggerEvent;
  }
  
  @Override
  public void setTriggerEvent(boolean triggerEvent)
  {
    this.triggerEvent = triggerEvent;
  }
  
  @Override
  public IGetConfigDetailsModel getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  @JsonDeserialize(as = AbstractGetConfigDetailsModel.class)
  public void setConfigDetails(IGetConfigDetailsModel configDetails)
  {
    this.configDetails = configDetails;
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
  public ITransactionDataModel getTransactionData()
  {
    return transactionData;
  }
  
  @Override
  public void setTransactionData(ITransactionDataModel transactionData)
  {
    this.transactionData = transactionData;
  }
  
  @Override
  public Map<String, String> getSourceDestinationIds()
  {
    return sourceDestinationIds;
  }
  
  @Override
  public void setSourceDestinationIds(Map<String, String> sourceDestinationIds)
  {
    this.sourceDestinationIds = sourceDestinationIds;
  }
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
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
  public String getVariantInstanceId()
  {
    return VariantInstanceId;
  }
  
  @Override
  public void setVariantInstanceId(String variantInstanceId)
  {
    VariantInstanceId = variantInstanceId;
  }
  
  @Override
  public String getEntityToTransfer()
  {
    return entityToTransfer;
  }
  
  @Override
  public void setEntityToTransfer(String entityToTransfer)
  {
    this.entityToTransfer = entityToTransfer;
  }
  
  public IGetKlassInstanceTreeStrategyModel getFilterInfo()
  {
    return filterInfo;
  }
  
  @JsonDeserialize(as = GetKlassInstanceTreeStrategyModel.class)
  public void setFilterInfo(IGetKlassInstanceTreeStrategyModel filterInfo)
  {
    this.filterInfo = filterInfo;
  }
  
  @Override
  public List<String> getArticleIdsToTransfer()
  {
    return articleIdsToTransfer;
  }
  
  @Override
  public void setArticleIdsToTransfer(List<String> articleIdsToTransfer)
  {
    this.articleIdsToTransfer = articleIdsToTransfer;
  }
  
  @Override
  public List<String> getAssetIdsToTransfer()
  {
    return assetIdsToTransfer;
  }
  
  @Override
  public void setAssetIdsToTransfer(List<String> assetIdsToTransfer)
  {
    this.assetIdsToTransfer = assetIdsToTransfer;
  }

  @Override
  public String getSourceCatalogId()
  {
    return this.sourceCatalogId;
  }

  @Override
  public void setSourceCatalogId(String sourceCatalogId)
  {
    this.sourceCatalogId = sourceCatalogId;
  }
}
