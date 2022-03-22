package com.cs.core.config.interactor.model.klass;

import java.util.List;
import java.util.Map;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesToInheritModel;
import com.cs.core.config.interactor.model.relationship.ISideInfoForRelationshipInheritanceModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.contextdatatransfer.IContextInfoForContextualDataTransferModel;
import com.cs.core.runtime.interactor.model.relationship.ISideInfoForRelationshipDataTransferModel;

public interface IGetKlassEntityWithoutKPStrategyResponseModel
    extends IModel, IConfigResponseWithAuditLogModel {
  
  public static final String KLASS                                          = "klass";
  public static final String DEFAULT_VALUES_DIFF                            = "defaultValuesDiff";
  public static final String DELETED_NATURE_RELATIONSHIP_IDS                = "deletedNatureRelationshipIds";
  public static final String DELETED_RELATIONSHIP_IDS                       = "deletedRelationshipIds";
  public static final String DELETED_PROPERTIES_FROM_SOURCE                 = "deletedPropertiesFromSource";
  public static final String ADDED_ELEMENTS                                 = "addedElements";
  public static final String DELETED_ELEMENTS                               = "deletedElements";
  public static final String MODIFIED_ELEMENTS                              = "modifiedElements";
  public static final String CONFIG_DETAILS                                 = "configDetails";
  public static final String CONTEXT_KLASS_SAVE_PROPERTIES_TO_INHERIT       = "contextKlassSavePropertiesToInherit";
  public static final String CONTEXT_KLASS_SAVE_PROPERTIES_TO_DATA_TRANSFER = "contextKlassSavePropertiesToDataTransfer";
  public static final String IS_INDENTIFIER_ATTRIBUTE_CHANGED               = "isIndentifierAttributeChanged";
  public static final String RELATIONSHIP_DATA_FOR_TRANSFER                 = "relationshipDataForTransfer";
  public static final String RELATIONSHIP_DATA_FOR_RELATIONSHIP_INHERITANCE = "relationshipDataForRelationshipInheritance";
  public static final String IS_REMOVE_TAXONOMY_CONFLICTS_REQUIRED          = "isRemoveTaxonomyConflictsRequired";
  public static final String REMOVED_ATTRIBUTE_VARIANT_CONTEXTS             = "removedAttributeVariantContexts";
  public static final String REMOVED_CONTEXT_CLASSIFIER_IIDS                = "removedContextClassifierIIDs";
  public static final String UPDATED_MANDATORY_PROPERTY_IIDS                = "updatedMandatoryPropertyIIDs";
  public static final String PROPERTY_IIDS_TO_EVALUATE_PRODUCT_IDENTIFIER   = "propertyIIDsToEvaluateProductIdentifier";
  public static final String PROPERTY_IIDS_TO_REMOVE_PRODUCT_IDENTIFIER     = "propertyIIDsToRemoveProductIdentifier";
  public static final String ADDED_CALCULATED_ATTRIBUTE_IDS                 = "addedCalculatedAttributeIds";
  
  public IKlass getEntity();
  
  public void setEntity(IKlass klass);
  
  public List<IDefaultValueChangeModel> getDefaultValuesDiff();
  
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff);
  
  public List<String> getDeletedNatureRelationshipIds();
  
  public void setDeletedNatureRelationshipIds(List<String> deletedNatureRelationshipIds);
  
  public List<String> getDeletedRelationshipIds();
  
  public void setDeletedRelationshipIds(List<String> deletedRelationshipIds);
  
  public Map<String, List<String>> getDeletedPropertiesFromSource();
  
  public void setDeletedPropertiesFromSource(Map<String, List<String>> deletedPropertiesFromSource);
  
  public IRelationshipPropertiesToInheritModel getAddedElements();
  
  public void setAddedElements(IRelationshipPropertiesToInheritModel addedElements);
  
  public IRelationshipPropertiesToInheritModel getModifiedElements();
  
  public void setModifiedElements(IRelationshipPropertiesToInheritModel modifiedElements);
  
  public IRelationshipPropertiesToInheritModel getDeletedElements();
  
  public void setDeletedElements(IRelationshipPropertiesToInheritModel deletedElements);
  
  public IGetKlassEntityConfigDetailsModel getConfigDetails();
  
  public void setConfigDetails(IGetKlassEntityConfigDetailsModel configDetails);
  
  public IContextKlassSavePropertiesToInheritModel getContextKlassSavePropertiesToInherit();
  
  public void setContextKlassSavePropertiesToInherit(
      IContextKlassSavePropertiesToInheritModel contextKlassSavePropertiesToInherit);
  
  public Boolean getIsIndentifierAttributeChanged();
  
  public void setIsIndentifierAttributeChanged(Boolean isIndentifierAttributeChanged);
  
  public List<IContextInfoForContextualDataTransferModel> getContextKlassSavePropertiesToDataTransfer();
  
  public void setContextKlassSavePropertiesToDataTransfer(
      List<IContextInfoForContextualDataTransferModel> contextKlassSavePropertiesToDataTransfer);
  
  public List<ISideInfoForRelationshipDataTransferModel> getRelationshipDataForTransfer();
  
  public void setRelationshipDataForTransfer(
      List<ISideInfoForRelationshipDataTransferModel> relationshipDataForTransfer);
  
  public List<ISideInfoForRelationshipInheritanceModel> getRelationshipDataForRelationshipInheritance();
  
  public void setRelationshipDataForRelationshipInheritance(
      List<ISideInfoForRelationshipInheritanceModel> relationshipDataForRelationshipInheritance);
  
  public Boolean getIsRemoveTaxonomyConflictsRequired();
  
  public void setIsRemoveTaxonomyConflictsRequired(Boolean isRemoveTaxonomyConflictsRequired);
  
  public List<Long> getRemovedContextClassifierIIDs();
  
  public void setRemovedContextClassifierIIDs(List<Long> removedContextClassifierIIDs);
  
  public IRemoveAttributeVariantContextModel getRemovedAttributeVariantContexts();
  
  public void setRemovedAttributeVariantContexts(
      IRemoveAttributeVariantContextModel removedAttributeVariantContexts);
  
  public List<Long> getUpdatedMandatoryPropertyIIDs();
  
  public void setUpdatedMandatoryPropertyIIDs(List<Long> updatedMandatoryPropertyIIDs);
 
  public List<Long> getPropertyIIDsToEvaluateProductIdentifier();
  
  public void setPropertyIIDsToEvaluateProductIdentifier(List<Long> propertyIIDsToEvaluateProductIdentifier);
  
  public List<Long> getPropertyIIDsToRemoveProductIdentifier();
  
  public void setPropertyIIDsToRemoveProductIdentifier(List<Long> propertyIIDsToRemoveProductIdentifier);
  
  public List<String> getAddedCalculatedAttributeIds();
  
  public void setAddedCalculatedAttributeIds(List<String> addedCalculatedAttributeIds);  
}
