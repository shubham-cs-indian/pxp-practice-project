package com.cs.core.config.interactor.model.attributiontaxonomy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.attributiontaxonomy.MasterTaxonomy;
import com.cs.core.config.interactor.model.klass.DefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IRemoveAttributeVariantContextModel;
import com.cs.core.config.interactor.model.klass.RemoveAttributeVariantContextModel;
import com.cs.core.runtime.interactor.model.contextdatatransfer.ContextInfoForContextualDataTransferModel;
import com.cs.core.runtime.interactor.model.contextdatatransfer.IContextInfoForContextualDataTransferModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetMasterTaxonomyWithoutKPStrategyResponseModel extends ConfigResponseWithAuditLogModel
    implements IGetMasterTaxonomyWithoutKPStrategyResponseModel {
  
  private static final long                                  serialVersionUID                        = 1L;
  protected IMasterTaxonomy                                  entity;
  protected IGetMasterTaxonomyConfigDetailsModel             configDetails;
  protected List<IDefaultValueChangeModel>                   defaultValuesDiff;
  protected Map<String, List<String>>                        deletedPropertiesFromSource;
  protected List<IContextInfoForContextualDataTransferModel> contextKlassSavePropertiesToDataTransfer;
  protected Boolean                                          isIndentifierAttributeChanged;
  protected Boolean                                          isImmediateChildPresent;
  protected List<Long>                                       updatedMandatoryPropertyIIDs            = new ArrayList<>();
  protected List<Long>                                       propertyIIDsToEvaluateProductIdentifier = new ArrayList<Long>();
  protected List<Long>                                       propertyIIDsToRemoveProductIdentifier   = new ArrayList<Long>();
  protected List<Long>                                       removedContextClassifierIIDs;
  protected IRemoveAttributeVariantContextModel              removedAttributeVariantContexts; 
  protected List<String>                                     addedCalculatedAttributeIds;
  
  @Override
  public IMasterTaxonomy getEntity()
  {
    return entity;
  }
  
  @JsonDeserialize(as = MasterTaxonomy.class)
  @Override
  public void setEntity(IMasterTaxonomy entity)
  {
    this.entity = entity;
  }
  
  @Override
  public IGetMasterTaxonomyConfigDetailsModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = GetMasterTaxonomyConfigDetailsModel.class)
  @Override
  public void setConfigDetails(IGetMasterTaxonomyConfigDetailsModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public List<IDefaultValueChangeModel> getDefaultValuesDiff()
  {
    if (defaultValuesDiff == null) {
      defaultValuesDiff = new ArrayList<>();
    }
    return defaultValuesDiff;
  }
  
  @JsonDeserialize(contentAs = DefaultValueChangeModel.class)
  @Override
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff)
  {
    this.defaultValuesDiff = defaultValuesDiff;
  }
  
  public Map<String, List<String>> getDeletedPropertiesFromSource()
  {
    if (deletedPropertiesFromSource == null) {
      deletedPropertiesFromSource = new HashMap<>();
    }
    return deletedPropertiesFromSource;
  }
  
  @Override
  public void setDeletedPropertiesFromSource(Map<String, List<String>> deletedPropertiesFromSource)
  {
    this.deletedPropertiesFromSource = deletedPropertiesFromSource;
  }
  
  @Override
  public Boolean getIsIndentifierAttributeChanged()
  {
    return isIndentifierAttributeChanged;
  }
  
  @Override
  public void setIsIndentifierAttributeChanged(Boolean isIndentifierAttributeChanged)
  {
    this.isIndentifierAttributeChanged = isIndentifierAttributeChanged;
  }
  
  @Override
  public List<IContextInfoForContextualDataTransferModel> getContextKlassSavePropertiesToDataTransfer()
  {
    if (contextKlassSavePropertiesToDataTransfer == null) {
      contextKlassSavePropertiesToDataTransfer = new ArrayList<>();
    }
    return contextKlassSavePropertiesToDataTransfer;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContextInfoForContextualDataTransferModel.class)
  public void setContextKlassSavePropertiesToDataTransfer(
      List<IContextInfoForContextualDataTransferModel> contextKlassSavePropertiesToDataTransfer)
  {
    this.contextKlassSavePropertiesToDataTransfer = contextKlassSavePropertiesToDataTransfer;
  }

  @Override
  public Boolean getIsImmediateChildPresent()
  {
    return isImmediateChildPresent;
  }

  @Override
  public void setIsImmediateChildPresent(Boolean isImmediateChildPresent)
  {
    this.isImmediateChildPresent = isImmediateChildPresent;
  } 
  
  @Override
  public List<Long> getUpdatedMandatoryPropertyIIDs()
  {
    return updatedMandatoryPropertyIIDs;
  }

  @Override
  public void setUpdatedMandatoryPropertyIIDs(List<Long> updatedMandatoryPropertyIIDs)
  {
    this.updatedMandatoryPropertyIIDs = updatedMandatoryPropertyIIDs;
  }

  @Override
  public List<Long> getPropertyIIDsToEvaluateProductIdentifier()
  {
    return propertyIIDsToEvaluateProductIdentifier;
  }

  @Override
  public void setPropertyIIDsToEvaluateProductIdentifier(
      List<Long> propertyIIDsToEvaluateProductIdentifier)
  {
    this.propertyIIDsToEvaluateProductIdentifier = propertyIIDsToEvaluateProductIdentifier;
  }

  @Override
  public List<Long> getPropertyIIDsToRemoveProductIdentifier()
  {
    return propertyIIDsToRemoveProductIdentifier;
  }

  @Override
  public void setPropertyIIDsToRemoveProductIdentifier(
      List<Long> propertyIIDsToRemoveProductIdentifier)
  {
    this.propertyIIDsToRemoveProductIdentifier = propertyIIDsToRemoveProductIdentifier;
  }
  
  public List<Long> getRemovedContextClassifierIIDs()
  {
    if(removedContextClassifierIIDs == null) {
      removedContextClassifierIIDs = new ArrayList<>();
    }
    return removedContextClassifierIIDs;
  }

  @Override
  public void setRemovedContextClassifierIIDs(List<Long> removedContextClassifierIIDs)
  {
    this.removedContextClassifierIIDs = removedContextClassifierIIDs;
  }
  
  @Override
  public IRemoveAttributeVariantContextModel getRemovedAttributeVariantContexts()
  {
    return removedAttributeVariantContexts;
  }

  @JsonDeserialize(as = RemoveAttributeVariantContextModel.class)
  @Override
  public void setRemovedAttributeVariantContexts(
      IRemoveAttributeVariantContextModel removedAttributeVariantContexts)
  {
    this.removedAttributeVariantContexts = removedAttributeVariantContexts;
  }

  @Override
  public List<String> getAddedCalculatedAttributeIds()
  {
    if(addedCalculatedAttributeIds == null) {
      addedCalculatedAttributeIds = new ArrayList<>();
    }
    return addedCalculatedAttributeIds;
  }
  
  @Override
  public void setAddedCalculatedAttributeIds(List<String> addedCalculatedAttributeIds)
  {
    this.addedCalculatedAttributeIds = addedCalculatedAttributeIds;
  }
}
