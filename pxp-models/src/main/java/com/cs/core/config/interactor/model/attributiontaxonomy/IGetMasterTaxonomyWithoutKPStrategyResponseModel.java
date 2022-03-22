package com.cs.core.config.interactor.model.attributiontaxonomy;

import java.util.List;
import java.util.Map;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IRemoveAttributeVariantContextModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.contextdatatransfer.IContextInfoForContextualDataTransferModel;

public interface IGetMasterTaxonomyWithoutKPStrategyResponseModel
    extends IModel, IConfigResponseWithAuditLogModel {
  
  public static final String DEFAULT_VALUES_DIFF                            = "defaultValuesDiff";
  public static final String DELETED_PROPERTIES_FROM_SOURCE                 = "deletedPropertiesFromSource";
  public static final String ENTITY                                         = "entity";
  public static final String CONFIG_DETAILS                                 = "configDetails";
  public static final String CONTEXT_KLASS_SAVE_PROPERTIES_TO_DATA_TRANSFER = "contextKlassSavePropertiesToDataTransfer";
  public static final String IS_INDENTIFIER_ATTRIBUTE_CHANGED               = "isIndentifierAttributeChanged";
  public static final String IS_IMMEDIATE_CHILD_PRESENT                     = "isImmediateChildPresent";
  public static final String UPDATED_MANDATORY_PROPERTY_IIDS                = "updatedMandatoryPropertyIIDs";
  public static final String PROPERTY_IIDS_TO_EVALUATE_PRODUCT_IDENTIFIER   = "propertyIIDsToEvaluateProductIdentifier";
  public static final String PROPERTY_IIDS_TO_REMOVE_PRODUCT_IDENTIFIER     = "propertyIIDsToRemoveProductIdentifier";
  public static final String REMOVED_CONTEXT_CLASSIFIER_IIDS                = "removedContextClassifierIIDs";
  public static final String REMOVED_ATTRIBUTE_VARIANT_CONTEXTS             = "removedAttributeVariantContexts";
  public static final String ADDED_CALCULATED_ATTRIBUTE_IDS                 = "addedCalculatedAttributeIds";
  
  public IMasterTaxonomy getEntity();
  
  public void setEntity(IMasterTaxonomy entity);
  
  public IGetMasterTaxonomyConfigDetailsModel getConfigDetails();
  
  public void setConfigDetails(IGetMasterTaxonomyConfigDetailsModel configDetails);
  
  public Map<String, List<String>> getDeletedPropertiesFromSource();
  
  public void setDeletedPropertiesFromSource(Map<String, List<String>> deletedPropertiesFromSource);
  
  public List<IDefaultValueChangeModel> getDefaultValuesDiff();
  
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff);
  
  public List<IContextInfoForContextualDataTransferModel> getContextKlassSavePropertiesToDataTransfer();
  
  public void setContextKlassSavePropertiesToDataTransfer(
      List<IContextInfoForContextualDataTransferModel> contextKlassSavePropertiesToDataTransfer);
  
  public Boolean getIsIndentifierAttributeChanged();
  
  public void setIsIndentifierAttributeChanged(Boolean isIndentifierAttributeChanged);
  
  public Boolean getIsImmediateChildPresent();
  
  public void setIsImmediateChildPresent(Boolean isImmediateChildPresent);
  
  public List<Long> getUpdatedMandatoryPropertyIIDs();
  
  public void setUpdatedMandatoryPropertyIIDs(List<Long> updatedMandatoryPropertyIIDs);

  public List<Long> getPropertyIIDsToEvaluateProductIdentifier();
  
  public void setPropertyIIDsToEvaluateProductIdentifier(List<Long> propertyIIDsToEvaluateProductIdentifier);
  
  public List<Long> getPropertyIIDsToRemoveProductIdentifier();
  
  public void setPropertyIIDsToRemoveProductIdentifier(List<Long> propertyIIDsToRemoveProductIdentifier);

  public List<Long> getRemovedContextClassifierIIDs();
  
  public void setRemovedContextClassifierIIDs(List<Long> removedContextClassifierIIDs);
  
  public IRemoveAttributeVariantContextModel getRemovedAttributeVariantContexts();
  
  public void setRemovedAttributeVariantContexts(
      IRemoveAttributeVariantContextModel removedAttributeVariantContexts);

  public List<String> getAddedCalculatedAttributeIds();
  
  public void setAddedCalculatedAttributeIds(List<String> addedCalculatedAttributeIds);
}
