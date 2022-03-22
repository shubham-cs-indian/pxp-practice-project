package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.config.interactor.model.configdetails.IValueInheritancePropagationModel;
import com.cs.core.config.interactor.model.klass.*;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesADMPropagationModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesToInheritModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInfoModel;
import com.cs.core.runtime.interactor.model.variants.IContextualDataPreparationModel;

import java.util.List;
import java.util.Map;

public interface IContentDiffModelToPrepareDataForBulkPropagation extends IModel {
  
  public static final String DELETED_RELATIONSHIP_IDS                              = "deletedRelationshipIds";
  public static final String DELETED_NATURE_RELATIONSHIP_IDS                       = "deletedNatureRelationshipIds";
  public static final String DELETED_KLASS_IDS                                     = "deletedKlassIds";
  public static final String PROPERTY_DIFF_MODEL_FOR_BULK_PROPAGATION              = "propertyDiffModelforBulkPropagation";
  public static final String PROPERTY_DIFF_MODEL_FOR_BULK_APPLY                    = "propertyDiffModelforBulkApply";
  public static final String RELATIONSHIP_PROPERTIES_ADM                           = "relationshipPropertiesADM";
  public static final String DELETED_PROPERTIES_FROM_SOURCE                        = "deletedPropertiesFromSource";
  public static final String VALUE_INHERITANCE_PROPAGATION_MODEL                   = "valueInheritancePropagationModel";
  public static final String PROPERTIES_DETAIL_ADDED_TO_RELATIONSHIP               = "propertiesDetailAddedToRelationship";
  public static final String CONTENT_CLASSIFICATION_RULE_VIOLATION_MODEL           = "contentClassificationRuleViolationModel";
  public static final String DELETED_COLLECTION_ASSOCIATED_INSTANCES_INFO          = "deletedCollectionAssociatedInstancesInfo";
  public static final String ADDED_INSTANCES_TO_COLLECTION_MODEL                   = "addedInstancesToCollectionsModel";
  public static final String DELETED_CONTENTS_FROM_RELATIONSHIP_INFO               = "deletedContentsFromRelationshipInfo";
  public static final String TYPE_IDS_ASSOCIATED_WITH_DATARULES                    = "typeIdsAssociatedWithDatarules";
  public static final String DELETED_DATA_RULE_IDS                                 = "deletedDataRuleIds";
  public static final String DELETED_CONTENTS_INFO                                 = "deletedContentsInfo";
  public static final String DELETED_INSTANCES_FROM_COLLECTION_MODEL               = "removedInstancesFromCollectionModel";
  public static final String PROPAGABLE_CONTEXTUAL_DATA                            = "propagableContextualData";
  public static final String CONTEXT_KLASS_SAVE_PROPERTIES_TO_INHERIT_MODEL        = "contextKlassSavePropertiesToInheritModel";
  public static final String ADDED_OR_DELETED_VARIANTS_IN_KLASS_INSTANCE           = "addedOrDeletedVariantsInKlassInstance";
  public static final String INSTANCES_TO_UPDATE_IS_MERGED                         = "instancesToUpdateIsMerged";
  public static final String SHOULD_SLEEP                                          = "shouldSleep";
  public static final String DELETED_TRANSLATIONS_INFO_MODEL                       = "deletedTranslationsInfoModel";
  public static final String UPDATE_CRIID_AND_DEFAULT_ASSET_INSTANCE_ID_INFO_MODEL = "updateCriidAndDefaultAssetInstanceIdInfoModel";
  public static final String IS_IDENTIFIER_ATTRIBUTE_EVALUATION_NEEDED             = "isIdentifierAttributeEvaluationNeeded";
  
  public List<String> getDeletedRelationshipIds();
  
  public void setDeletedRelationshipIds(List<String> deletedRelationshipIds);
  
  public List<String> getDeletedNatureRelationshipIds();
  
  public void setDeletedNatureRelationshipIds(List<String> deletedNatureRelationshipIds);
  
  public List<String> getDeletedKlassIds();
  
  public void setDeletedKlassIds(List<String> deletedKlassIds);
  
  public IPropertyDiffModelForBulkPropagation getPropertyDiffModelForBulkPropagation();
  
  public void setPropertyDiffModelForBulkPropagation(
      IPropertyDiffModelForBulkPropagation propertyDiffModelForBulkPropagation);
  
  public IPropertyDiffModelForBulkApply getPropertyDiffModelForBulkApply();
  
  public void setPropertyDiffModelForBulkApply(
      IPropertyDiffModelForBulkApply propertyDiffModelForBulkApply);
  
  public IRelationshipPropertiesADMPropagationModel getRelationshipPropertiesADM();
  
  public void setRelationshipPropertiesADM(
      IRelationshipPropertiesADMPropagationModel relationshipPropertiesADM);
  
  public Map<String, List<String>> getDeletedPropertiesFromSource();
  
  public void setDeletedPropertiesFromSource(Map<String, List<String>> deletedPropertiesFromSource);
  
  public IValueInheritancePropagationModel getValueInheritancePropagationModel();
  
  public void setValueInheritancePropagationModel(
      IValueInheritancePropagationModel valueInheritancePropagationModel);
  
  public IRelationshipPropertiesToInheritModel getPropertiesDetailAddedToRelationship();
  
  public void setPropertiesDetailAddedToRelationship(
      IRelationshipPropertiesToInheritModel propertiesDetailAddedToRelationship);
  
  public IContentClassificationRuleViolotionModelForBulkPropagation getContentClassificationRuleViolationModel();
  
  public void setContentClassificationRuleViolationModel(
      IContentClassificationRuleViolotionModelForBulkPropagation contentClassificationRuleViolationModel);
  
  public IDeletedCollectionAssociatedInstancesInfoForBulkPropagationModel getDeletedCollectionAssociatedInstancesInfo();
  
  public void setDeletedCollectionAssociatedInstancesInfo(
      IDeletedCollectionAssociatedInstancesInfoForBulkPropagationModel deletedCollectionAssociatedInstancesInfo);
  
  public IAddedInstancesToCollectionsModel getAddedInstancesToCollectionsModel();
  
  public void setAddedInstancesToCollectionsModel(
      IAddedInstancesToCollectionsModel addedInstancesToCollectionsModel);
  
  public List<IContentsDeleteFromRelationshipDataPreparationModel> getDeletedContentsFromRelationshipInfo();
  
  public void setDeletedContentsFromRelationshipInfo(
      List<IContentsDeleteFromRelationshipDataPreparationModel> deletedContentsFromRelationshipInfo);
  
  public ITypesListModel getTypeIdsAssociatedWithDatarules();
  
  public void setTypeIdsAssociatedWithDatarules(ITypesListModel typeidsAssociatedWithDatarules);
  
  public List<String> getDeletedDataRuleIds();
  
  public void setDeletedDataRuleIds(List<String> deletedDataRuleIds);
  
  public IDeleteContentsDataPreparationModel getDeletedContentsInfo();
  
  public void setDeletedContentsInfo(IDeleteContentsDataPreparationModel deletedContentsInfo);
  
  public IRemovedInstancesFromCollectionModel getRemovedInstancesFromCollectionModel();
  
  public void setRemovedInstancesFromCollectionModel(
      IRemovedInstancesFromCollectionModel removedInstancesFromCollectionModel);
  
  public IContextualDataPreparationModel getPropagableContextualData();
  
  public void setPropagableContextualData(IContextualDataPreparationModel propagableContextualData);
  
  public IContextKlassSavePropertiesToInheritModel getContextKlassSavePropertiesToInheritModel();
  
  public void setContextKlassSavePropertiesToInheritModel(
      IContextKlassSavePropertiesToInheritModel contextKlassSavePropertiesToInheritModel);
  
  public IAddedOrDeletedVariantsDataPreparationModel getAddedOrDeletedVariantsInKlassInstance();
  
  public void setAddedOrDeletedVariantsInKlassInstance(
      IAddedOrDeletedVariantsDataPreparationModel addedOrDeletedVariantsInKlassInstance);
  
  public List<String> getInstancesToUpdateIsMerged();
  
  public void setInstancesToUpdateIsMerged(List<String> instancesToUpdateIsMerged);
  
  public Boolean getShouldSleep();
  
  public void setShouldSleep(Boolean shouldSleep);
  
  public IDeletedTranslationsInfoModel getDeletedTranslationsInfoModel();
  
  public void setDeletedTranslationsInfoModel(
      IDeletedTranslationsInfoModel deletedTranslationsInfoModel);
  
  public List<IRelationshipDataTransferInfoModel> getUpdateCriidAndDefaultAssetInstanceIdInfoModel();
  
  public void setUpdateCriidAndDefaultAssetInstanceIdInfoModel(
      List<IRelationshipDataTransferInfoModel> updateCriidAndDefaultAssetInstanceIdInfoModel);
  
  public Boolean getIsIdentifierAttributeEvaluationNeeded();
  
  public void setIsIdentifierAttributeEvaluationNeeded(
      Boolean isIdentifierAttributeEvaluationNeeded);
}
