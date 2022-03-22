package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.config.interactor.model.configdetails.IValueInheritancePropagationModel;
import com.cs.core.config.interactor.model.klass.*;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesADMPropagationModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesToInheritModel;
import com.cs.core.runtime.interactor.model.context.ContextualValueInheritancePropagationModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInfoModel;
import com.cs.core.runtime.interactor.model.relationship.RelationshipDataTransferInfoModel;
import com.cs.core.runtime.interactor.model.relationship.RelationshipPropertiesADMPropagationModel;
import com.cs.core.runtime.interactor.model.relationship.RelationshipPropertiesToInheritModel;
import com.cs.core.runtime.interactor.model.variants.ContextualDataPreparationModel;
import com.cs.core.runtime.interactor.model.variants.IContextualDataPreparationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentDiffModelToPrepareDataForBulkPropagation
    implements IContentDiffModelToPrepareDataForBulkPropagation {
  
  private static final long                                                  serialVersionUID = 1L;
  protected List<String>                                                     deletedRelationshipIds;
  protected List<String>                                                     deletedNatureRelationshipIds;
  protected List<String>                                                     deletedKlassIds;
  protected IPropertyDiffModelForBulkPropagation                             propertyDiffModelForBulkPropagation;
  protected IPropertyDiffModelForBulkApply                                   propertyDiffModelForBulkApply;
  protected IRelationshipPropertiesADMPropagationModel                       relationshipPropertiesADM;
  protected Map<String, List<String>>                                        deletedPropertiesFromSource;
  protected IValueInheritancePropagationModel                                valueInheritancePropagationModel;
  protected IRelationshipPropertiesToInheritModel                            propertiesDetailAddedToRelationship;
  protected IContentClassificationRuleViolotionModelForBulkPropagation       contentClassificationRuleViolationModel;
  protected IDeletedCollectionAssociatedInstancesInfoForBulkPropagationModel deletedCollectionAssociatedInstancesInfo;
  protected IAddedInstancesToCollectionsModel                                addedInstancesToCollectionsModel;
  protected List<IContentsDeleteFromRelationshipDataPreparationModel>        deletedContentsFromRelationshipInfo;
  protected ITypesListModel                                                  typeIdsAssociatedWithDatarules;
  protected List<String>                                                     deletedDataRuleIds;
  protected IDeleteContentsDataPreparationModel                              deletedContentsInfo;
  protected IRemovedInstancesFromCollectionModel                             removedInstancesFromCollectionModel;
  protected IContextualDataPreparationModel                                  propagableContextualData;
  protected IContextKlassSavePropertiesToInheritModel                        contextKlassSavePropertiesToInheritModel;
  protected IAddedOrDeletedVariantsDataPreparationModel                      addedOrDeletedVariantsInKlassInstance;
  protected List<String>                                                     instancesToUpdateIsMerged;
  protected Boolean                                                          shouldSleep;
  protected IDeletedTranslationsInfoModel                                    deletedTranslationsInfoModel;
  protected List<IRelationshipDataTransferInfoModel>                         updateCriidAndDefaultAssetInstanceIdInfoModel;
  protected Boolean                                                          isIdentifierAttributeEvaluationNeeded;
  
  @Override
  public List<String> getDeletedRelationshipIds()
  {
    if (deletedRelationshipIds == null) {
      deletedRelationshipIds = new ArrayList<>();
    }
    return deletedRelationshipIds;
  }
  
  @Override
  public void setDeletedRelationshipIds(List<String> deletedRelationshipIds)
  {
    this.deletedRelationshipIds = deletedRelationshipIds;
  }
  
  @Override
  public List<String> getDeletedNatureRelationshipIds()
  {
    if (deletedNatureRelationshipIds == null) {
      deletedNatureRelationshipIds = new ArrayList<>();
    }
    return deletedNatureRelationshipIds;
  }
  
  @Override
  public void setDeletedNatureRelationshipIds(List<String> deletedNatureRelationshipIds)
  {
    this.deletedNatureRelationshipIds = deletedNatureRelationshipIds;
  }
  
  @Override
  public List<String> getDeletedKlassIds()
  {
    if (deletedKlassIds == null) {
      deletedKlassIds = new ArrayList<>();
    }
    return deletedKlassIds;
  }
  
  @Override
  public void setDeletedKlassIds(List<String> deletedKlassIds)
  {
    this.deletedKlassIds = deletedKlassIds;
  }
  
  @Override
  public IPropertyDiffModelForBulkPropagation getPropertyDiffModelForBulkPropagation()
  {
    return propertyDiffModelForBulkPropagation;
  }
  
  @Override
  @JsonDeserialize(as = PropertyDiffModelForBulkPropagation.class)
  public void setPropertyDiffModelForBulkPropagation(
      IPropertyDiffModelForBulkPropagation propertyDiffModelForBulkPropagation)
  {
    this.propertyDiffModelForBulkPropagation = propertyDiffModelForBulkPropagation;
  }
  
  @Override
  public IRelationshipPropertiesADMPropagationModel getRelationshipPropertiesADM()
  {
    return relationshipPropertiesADM;
  }
  
  @Override
  @JsonDeserialize(as = RelationshipPropertiesADMPropagationModel.class)
  public void setRelationshipPropertiesADM(
      IRelationshipPropertiesADMPropagationModel relationshipPropertiesADM)
  {
    this.relationshipPropertiesADM = relationshipPropertiesADM;
  }
  
  @Override
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
  public IValueInheritancePropagationModel getValueInheritancePropagationModel()
  {
    return valueInheritancePropagationModel;
  }
  
  @Override
  @JsonDeserialize(as = ValueInheritancePropagationModel.class)
  public void setValueInheritancePropagationModel(
      IValueInheritancePropagationModel valueInheritancePropagationModel)
  {
    this.valueInheritancePropagationModel = valueInheritancePropagationModel;
  }
  
  @Override
  public IRelationshipPropertiesToInheritModel getPropertiesDetailAddedToRelationship()
  {
    return propertiesDetailAddedToRelationship;
  }
  
  @Override
  @JsonDeserialize(as = RelationshipPropertiesToInheritModel.class)
  public void setPropertiesDetailAddedToRelationship(
      IRelationshipPropertiesToInheritModel propertiesDetailAddedToRelationship)
  {
    this.propertiesDetailAddedToRelationship = propertiesDetailAddedToRelationship;
  }
  
  @Override
  public IContentClassificationRuleViolotionModelForBulkPropagation getContentClassificationRuleViolationModel()
  {
    return contentClassificationRuleViolationModel;
  }
  
  @Override
  @JsonDeserialize(as = ContentClassificationRuleViolotionModelForBulkPropagation.class)
  public void setContentClassificationRuleViolationModel(
      IContentClassificationRuleViolotionModelForBulkPropagation contentClassificationRuleViolationModel)
  {
    this.contentClassificationRuleViolationModel = contentClassificationRuleViolationModel;
  }
  
  @Override
  public IDeletedCollectionAssociatedInstancesInfoForBulkPropagationModel getDeletedCollectionAssociatedInstancesInfo()
  {
    return deletedCollectionAssociatedInstancesInfo;
  }
  
  @Override
  @JsonDeserialize(as = DeletedCollectionAssociatedInstancesInfoForBulkPropagationModel.class)
  public void setDeletedCollectionAssociatedInstancesInfo(
      IDeletedCollectionAssociatedInstancesInfoForBulkPropagationModel deletedCollectionAssociatedInstancesInfo)
  {
    this.deletedCollectionAssociatedInstancesInfo = deletedCollectionAssociatedInstancesInfo;
  }
  
  @Override
  public IAddedInstancesToCollectionsModel getAddedInstancesToCollectionsModel()
  {
    return addedInstancesToCollectionsModel;
  }
  
  @Override
  @JsonDeserialize(as = AddedInstancesToCollectionsModel.class)
  public void setAddedInstancesToCollectionsModel(
      IAddedInstancesToCollectionsModel addedInstancesToCollectionsModel)
  {
    this.addedInstancesToCollectionsModel = addedInstancesToCollectionsModel;
  }
  
  public ITypesListModel getTypeIdsAssociatedWithDatarules()
  {
    return typeIdsAssociatedWithDatarules;
  }
  
  @Override
  @JsonDeserialize(as = TypesListModel.class)
  public void setTypeIdsAssociatedWithDatarules(ITypesListModel typeIdsAssociatedWithDatarules)
  {
    this.typeIdsAssociatedWithDatarules = typeIdsAssociatedWithDatarules;
  }
  
  public List<IContentsDeleteFromRelationshipDataPreparationModel> getDeletedContentsFromRelationshipInfo()
  {
    if (deletedContentsFromRelationshipInfo == null) {
      deletedContentsFromRelationshipInfo = new ArrayList<>();
    }
    return deletedContentsFromRelationshipInfo;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContentsDeleteFromRelationshipDataPreparationModel.class)
  public void setDeletedContentsFromRelationshipInfo(
      List<IContentsDeleteFromRelationshipDataPreparationModel> deletedContentsFromRelationshipInfo)
  {
    this.deletedContentsFromRelationshipInfo = deletedContentsFromRelationshipInfo;
  }
  
  @Override
  public List<String> getDeletedDataRuleIds()
  {
    if (deletedDataRuleIds == null) {
      deletedDataRuleIds = new ArrayList<>();
    }
    return deletedDataRuleIds;
  }
  
  @Override
  public void setDeletedDataRuleIds(List<String> deletedDataRuleIds)
  {
    this.deletedDataRuleIds = deletedDataRuleIds;
  }
  
  @Override
  public IDeleteContentsDataPreparationModel getDeletedContentsInfo()
  {
    return deletedContentsInfo;
  }
  
  @Override
  @JsonDeserialize(as = DeleteContentsDataPreparationModel.class)
  public void setDeletedContentsInfo(IDeleteContentsDataPreparationModel deletedContentsInfo)
  {
    this.deletedContentsInfo = deletedContentsInfo;
  }
  
  @Override
  public IRemovedInstancesFromCollectionModel getRemovedInstancesFromCollectionModel()
  {
    return removedInstancesFromCollectionModel;
  }
  
  @Override
  public void setRemovedInstancesFromCollectionModel(
      IRemovedInstancesFromCollectionModel removedInstancesFromCollectionModel)
  {
    this.removedInstancesFromCollectionModel = removedInstancesFromCollectionModel;
  }
  
  @Override
  public IContextualDataPreparationModel getPropagableContextualData()
  {
    return propagableContextualData;
  }
  
  @JsonDeserialize(as = ContextualDataPreparationModel.class)
  @Override
  public void setPropagableContextualData(IContextualDataPreparationModel propagableContextualData)
  {
    this.propagableContextualData = propagableContextualData;
  }
  
  @Override
  public IContextKlassSavePropertiesToInheritModel getContextKlassSavePropertiesToInheritModel()
  {
    return contextKlassSavePropertiesToInheritModel;
  }
  
  @JsonDeserialize(as = ContextualValueInheritancePropagationModel.class)
  @Override
  public void setContextKlassSavePropertiesToInheritModel(
      IContextKlassSavePropertiesToInheritModel contextKlassSavePropertiesToInheritModel)
  {
    this.contextKlassSavePropertiesToInheritModel = contextKlassSavePropertiesToInheritModel;
  }
  
  public IAddedOrDeletedVariantsDataPreparationModel getAddedOrDeletedVariantsInKlassInstance()
  {
    return addedOrDeletedVariantsInKlassInstance;
  }
  
  @JsonDeserialize(contentAs = AddedOrDeletedVariantsDataPreparationModel.class)
  @Override
  public void setAddedOrDeletedVariantsInKlassInstance(
      IAddedOrDeletedVariantsDataPreparationModel addedOrDeletedVariantsInKlassInstance)
  {
    this.addedOrDeletedVariantsInKlassInstance = addedOrDeletedVariantsInKlassInstance;
  }
  
  @Override
  public List<String> getInstancesToUpdateIsMerged()
  {
    if (instancesToUpdateIsMerged == null) {
      instancesToUpdateIsMerged = new ArrayList<>();
    }
    return instancesToUpdateIsMerged;
  }
  
  @Override
  public void setInstancesToUpdateIsMerged(List<String> instancesToUpdateIsMerged)
  {
    this.instancesToUpdateIsMerged = instancesToUpdateIsMerged;
  }
  
  @Override
  public IPropertyDiffModelForBulkApply getPropertyDiffModelForBulkApply()
  {
    return propertyDiffModelForBulkApply;
  }
  
  @Override
  @JsonDeserialize(as = PropertyDiffModelForBulkApply.class)
  public void setPropertyDiffModelForBulkApply(
      IPropertyDiffModelForBulkApply propertyDiffModelForBulkApply)
  {
    this.propertyDiffModelForBulkApply = propertyDiffModelForBulkApply;
  }
  
  public Boolean getShouldSleep()
  {
    if (shouldSleep == null) {
      shouldSleep = false;
    }
    return shouldSleep;
  }
  
  @Override
  public void setShouldSleep(Boolean shouldSleep)
  {
    this.shouldSleep = shouldSleep;
  }
  
  @Override
  public IDeletedTranslationsInfoModel getDeletedTranslationsInfoModel()
  {
    return deletedTranslationsInfoModel;
  }
  
  @JsonDeserialize(as = DeletedTranslationsInfoModel.class)
  @Override
  public void setDeletedTranslationsInfoModel(
      IDeletedTranslationsInfoModel deletedTranslationsInfoModel)
  {
    this.deletedTranslationsInfoModel = deletedTranslationsInfoModel;
  }
  
  @JsonDeserialize(contentAs = RelationshipDataTransferInfoModel.class)
  @Override
  public List<IRelationshipDataTransferInfoModel> getUpdateCriidAndDefaultAssetInstanceIdInfoModel()
  {
    if (updateCriidAndDefaultAssetInstanceIdInfoModel == null) {
      updateCriidAndDefaultAssetInstanceIdInfoModel = new ArrayList<>();
    }
    return updateCriidAndDefaultAssetInstanceIdInfoModel;
  }
  
  @Override
  public void setUpdateCriidAndDefaultAssetInstanceIdInfoModel(
      List<IRelationshipDataTransferInfoModel> updateCriidAndDefaultAssetInstanceIdInfoModel)
  {
    this.updateCriidAndDefaultAssetInstanceIdInfoModel = updateCriidAndDefaultAssetInstanceIdInfoModel;
  }
  
  @Override
  public Boolean getIsIdentifierAttributeEvaluationNeeded()
  {
    return isIdentifierAttributeEvaluationNeeded;
  }
  
  @Override
  public void setIsIdentifierAttributeEvaluationNeeded(
      Boolean isIdentifierAttributeEvaluationNeeded)
  {
    this.isIdentifierAttributeEvaluationNeeded = isIdentifierAttributeEvaluationNeeded;
  }
}
