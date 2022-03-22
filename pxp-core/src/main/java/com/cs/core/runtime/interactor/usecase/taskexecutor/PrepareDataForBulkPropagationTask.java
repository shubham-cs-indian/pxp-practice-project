package com.cs.core.runtime.interactor.usecase.taskexecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.configdetails.IValueInheritancePropagationModel;
import com.cs.core.config.interactor.model.klass.ContentsDeleteFromRelationshipModel;
import com.cs.core.config.interactor.model.klass.ContentsPropertyDiffModel;
import com.cs.core.config.interactor.model.klass.IAddedDeletedTypesModel;
import com.cs.core.config.interactor.model.klass.IAddedOrDeletedVariantsDataPreparationModel;
import com.cs.core.config.interactor.model.klass.IAttributeIdValueModel;
import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.config.interactor.model.klass.IContentsDeleteFromRelationshipDataPreparationModel;
import com.cs.core.config.interactor.model.klass.IContentsDeleteFromRelationshipModel;
import com.cs.core.config.interactor.model.klass.IContentsPropertyDiffModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IDeleteContentsDataPreparationModel;
import com.cs.core.config.interactor.model.klass.IDeletedContentTypeIdsInfoModel;
import com.cs.core.config.interactor.model.klass.IInheritanceDataModel;
import com.cs.core.config.interactor.model.klass.ITagIdValueModel;
import com.cs.core.config.strategy.usecase.staticcollection.IGetAddedPropertiesDiffStrategy;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.model.bulkpropagation.AddedOrDeletedVariantsDataPreparationModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IAddedInstancesToCollectionsModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IContentClassificationRuleViolotionModelForBulkPropagation;
import com.cs.core.runtime.interactor.model.bulkpropagation.IContentDiffModelToPrepareDataForBulkPropagation;
import com.cs.core.runtime.interactor.model.bulkpropagation.IDeletedCollectionAssociatedInstancesInfoForBulkPropagationModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IDeletedTranslationsInfoModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IPropertyDiffModelForBulkApply;
import com.cs.core.runtime.interactor.model.bulkpropagation.IRemovedInstancesFromCollectionModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IUpdateCRIIDInfoModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.UpdateCRIIDInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInfoModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.bulkpropagation.IPrepareDataForBulkPropagationTask;

@Component
public class PrepareDataForBulkPropagationTask
    extends AbstractRuntimeInteractor<IContentDiffModelToPrepareDataForBulkPropagation, IModel>
    implements IPrepareDataForBulkPropagationTask {
  
  /*@Autowired
  protected IDeleteRelationshipInstancesStrategy                          deleteRelationshipInstancesStrategy;*/
  
  /*@Autowired
  protected IGetKlassInstancesByKlassAndTaxonomyIdsStrategy               getAllInstancesByKlassAndTaxonomyIdsStrategy;
  
  @Autowired
  protected IGetPropagationDataForAddedRelationshipElementsStrategy       getPropagationDataForAddedRelationshipElementsStrategy;*/
  
  @Autowired
  protected IGetAddedPropertiesDiffStrategy getAddedPropertiesDiffStrategy;
  
  
  /*@Autowired
  protected IGetInstancesOfDeletedDataRuleStrategy                        getKlassInstancesOfDeletedDataRuleStrategy;
  
  @Autowired
  protected IGetImmediateVariantsDetailStrategy                           getImmediateVariantsDetailStrategy;
  
  @Autowired
  protected IGetContextualDataTransferForValueInheritanceStrategy         getContextualDataTransferForValueInheritanceStrategy;*/
  
  @Autowired
  protected Boolean                         isKafkaLoggingEnabled;
  
  @Override
  protected IModel executeInternal(IContentDiffModelToPrepareDataForBulkPropagation model)
      throws Exception
  {
    try {
      // to make document searchable
      if (model.getShouldSleep()) {
        Thread.sleep(1000);
        model.setShouldSleep(false);
      }
      
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel = new HashMap<>();
      
      // Prepare data for deleted relationships and nature relationships
      prepareContentDiffModelUponDeleteOfRelationshipsAndNatureRelationships(model,
          contentToUpdateIdVsPropagationDiffModel);
      
      // Prepare data for deleted klasses
      prepareContentDiffModelUponDeleteKlasses(model, contentToUpdateIdVsPropagationDiffModel);
      
      // Prepare data from default value diff list upon save klass, taxonomy,
      // bulk edit
      prepareContentDiffModelFromDefaultValueDiffList(model,
          contentToUpdateIdVsPropagationDiffModel);
      
      // Prepare data from default value diff List on bulk apply
      prepareContentDiffModelFromDefaultValueDiffListForBulkApply(model,
          contentToUpdateIdVsPropagationDiffModel);
      
      // Prepare data for relationship properties on updating coupling type or
      // removing old elements
      // selected in relationship for propagation
      prepareContentDiffModelFromRelationshipPropertiesDiff(model,
          contentToUpdateIdVsPropagationDiffModel);
      
      // Prepare data when properties are removed from property collection , or
      // section is removed
      // from klass or taxonomy
      prepareContentDiffModelForDeletedPropertiesFromSource(model,
          contentToUpdateIdVsPropagationDiffModel);
      
      // Prepare data for relationship value propagation model
      prepareContentDiffModelForValueInheritancePropagationModelFromRelationship(model,
          contentToUpdateIdVsPropagationDiffModel);
      
      // Prepare data when properties are added with coupling to relationship
      prepareContentDiffModelForAddedPropertiesToRelationshpRelationship(model,
          contentToUpdateIdVsPropagationDiffModel);
      
      // Prepare data for classification rule violation of instance
      prepareContentDiffModelForClassificationRuleViolationPropagation(model,
          contentToUpdateIdVsPropagationDiffModel);
      
      // Prepare data for delete of static collection
      prepareContentDiffModelForDeletedCollections(model, contentToUpdateIdVsPropagationDiffModel);
      
      // Prepare data for contents added to collection on create instance
      prepareContentDiffModelForAddedInstancesToCollection(model,
          contentToUpdateIdVsPropagationDiffModel);
      
      // Prepare data for Save Data Rule
      prepareContentDiffModelForSaveDataRule(model, contentToUpdateIdVsPropagationDiffModel);
      
      // Prepare data for removing elements from relationships
      prepareContentDiffModelForDeletedContentFromRelationship(model,
          contentToUpdateIdVsPropagationDiffModel);
      
      // Prepare data to reevaluate data rules on data rule delete
      prepareContentDiffModelOnDataRuleDelete(model, contentToUpdateIdVsPropagationDiffModel);
      
      // Prepare data for deleted contents
      prepareContentDiffModelForDeletedContents(model, contentToUpdateIdVsPropagationDiffModel);
      
      /*Prepare data for added or deleted variants*/
      prepareContentDiffModelForAddedOrDeletedVariantsInKlassInstance(model,
          contentToUpdateIdVsPropagationDiffModel);
      
      // Prepare data to reevalute content when it is removed from collection
      prepareContentDiffModelForContentsRemovedFromCollection(model,
          contentToUpdateIdVsPropagationDiffModel);
      
      // Prepare data for value propagation through contexts
      prepareContentDiffModelForContextualDataTransfer(model,
          contentToUpdateIdVsPropagationDiffModel);
      
      // Prepare data for Contextual data flow
      prepareContentDiffForContextualValueInheritance(model,
          contentToUpdateIdVsPropagationDiffModel);
      
      // prepare data to update isMerged for variants of merged instances
      prepareContentDiffForUpdatingIsMerged(model, contentToUpdateIdVsPropagationDiffModel);
      
      // prepare data to remove conflicts due to deleted translations
      prepareContentDiffForDeletedTranslations(model, contentToUpdateIdVsPropagationDiffModel);
      
      // prepare data to update CRIID and defaultAssetInstanceId
      prepareContentDiffForUpdatingCriidAndDefaultAssetInstanceId(model,
          contentToUpdateIdVsPropagationDiffModel);
      
      // prepare data for relationship data transfer on configuration change
      prepareRelationshipDataPropertiesForTransferOnConfigChange(model,
          contentToUpdateIdVsPropagationDiffModel);
      
      /** ******* Prepare all data before this ************** */
      putContentDiffListToQueueForBulkPropagation(contentToUpdateIdVsPropagationDiffModel);
      
    }
    catch (Throwable ex) {
      if (isKafkaLoggingEnabled) {
        //TODO: BGP
      }
    }
    
    return null;
  }
  
  private void prepareRelationshipDataPropertiesForTransferOnConfigChange(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    /*IRelationshipDataTransferOnConfigChangeModel relationshipDataPropertiesForTransfer = model.getRelationshipDataPropertiesForTransfer();
    IListModel<IContentsPropertyDiffModel> contentDiffListModel = prepareDataForRelationshipDataTransferOnConfigChangeStrategy.execute(relationshipDataPropertiesForTransfer);
    contentDiffListModel.getList().forEach(contentDiff -> {
      String contentId = contentDiff.getContentId();
      IContentsPropertyDiffModel contentsPropertyDiffModel = contentToUpdateIdVsPropagationDiffModel.get(contentId);
      if(contentsPropertyDiffModel == null) {
        contentToUpdateIdVsPropagationDiffModel.put(contentId, contentDiff);
      }
      else {
        contentsPropertyDiffModel.getDependentAttributesConflictMap().putAll(contentDiff.getDependentAttributesConflictMap());
        contentsPropertyDiffModel.getAttributesConflictMap().putAll(contentDiff.getAttributesConflictMap());
        contentsPropertyDiffModel.getTagsConflictMap().putAll(contentDiff.getTagsConflictMap());
      }
    });*/
  }
  
  private void prepareContentDiffForContextualValueInheritance(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    /*
      IContextKlassSavePropertiesToInheritModel contextKlassSavePropertiesToInheritModel = model.getContextKlassSavePropertiesToInheritModel();
      if(contextKlassSavePropertiesToInheritModel == null) {
        return;
      }
      IContextualValueInheritancePropagationModel contextualValuesToInheritance = getContextualDataTransferForValueInheritanceStrategy
          .execute(contextKlassSavePropertiesToInheritModel);
      List<IContentInfoForContextualValueInheritanceModel> contentsInfoForContextualValueInheritance = contextualValuesToInheritance.getContentInfoForContextualValueInheritance();
      if (contentsInfoForContextualValueInheritance != null && !contentsInfoForContextualValueInheritance.isEmpty()) {
        for (IContentInfoForContextualValueInheritanceModel contentInfo : contentsInfoForContextualValueInheritance) {
          String contentId = contentInfo.getContentId();
          IContentsPropertyDiffModel contentsDiffModel = contentToUpdateIdVsPropagationDiffModel.get(contentId);
          if (contentsDiffModel == null) {
            contentsDiffModel = new ContentsPropertyDiffModel();
            contentToUpdateIdVsPropagationDiffModel.put(contentId, contentsDiffModel);
            contentsDiffModel.setContentId(contentId);
            contentsDiffModel.setBaseType(contentInfo.getBaseType());
          }
          contentsDiffModel.setContentInfoForContextualValueInheritance(contentInfo);
        }
      }
    */
  }
  
  private void prepareContentDiffModelForContentsRemovedFromCollection(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
  {
    IRemovedInstancesFromCollectionModel removedInstancesFromCollectionModel = model
        .getRemovedInstancesFromCollectionModel();
    if (removedInstancesFromCollectionModel != null) {
      List<IIdAndTypeModel> contentIdBaseTypeInfo = removedInstancesFromCollectionModel
          .getContentIdBaseTypeInfo();
      for (IIdAndTypeModel iIdAndTypeModel : contentIdBaseTypeInfo) {
        IContentsPropertyDiffModel contentsDiffModel = contentToUpdateIdVsPropagationDiffModel
            .get(iIdAndTypeModel.getId());
        if (contentsDiffModel == null) {
          contentsDiffModel = new ContentsPropertyDiffModel();
          contentToUpdateIdVsPropagationDiffModel.put(iIdAndTypeModel.getId(), contentsDiffModel);
          contentsDiffModel.setContentId(iIdAndTypeModel.getId());
          contentsDiffModel.setBaseType(iIdAndTypeModel.getType());
        }
        contentsDiffModel.setRemovedCollectionIds(
            removedInstancesFromCollectionModel.getIntancesRemovedFromCollectionIds());
      }
    }
  }
  
  private void prepareContentDiffModelOnDataRuleDelete(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    /*
      List<String> deletedDataRuleIds = model.getDeletedDataRuleIds();
      if (!deletedDataRuleIds.isEmpty()) {
        IIdsListParameterModel dataModel = new IdsListParameterModel();
        dataModel.setIds(deletedDataRuleIds);
        IListModel<IContentTypeIdsInfoModel> iContentKlassIdsAndTaxonomyIdsInfoListModel = getKlassInstancesOfDeletedDataRuleStrategy.execute(dataModel);
        for (IContentTypeIdsInfoModel iContentKlassIdsAndTaxonomyIdsInfoModel : iContentKlassIdsAndTaxonomyIdsInfoListModel.getList()) {
          IContentsPropertyDiffModel contentsDiffModel = contentToUpdateIdVsPropagationDiffModel.get(iContentKlassIdsAndTaxonomyIdsInfoModel.getContentId());
          if (contentsDiffModel == null) {
            contentsDiffModel = new ContentsPropertyDiffModel();
            contentToUpdateIdVsPropagationDiffModel.put(iContentKlassIdsAndTaxonomyIdsInfoModel.getContentId(), contentsDiffModel);
            contentsDiffModel.setBaseType(iContentKlassIdsAndTaxonomyIdsInfoModel.getBaseType());
            contentsDiffModel.setContentId(iContentKlassIdsAndTaxonomyIdsInfoModel.getContentId());
          }
        }
      }
    */
  }
  
  private void putContentDiffListToQueueForBulkPropagation(
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    Collection<IContentsPropertyDiffModel> values = contentToUpdateIdVsPropagationDiffModel
        .values();
    List<IContentsPropertyDiffModel> list = new ArrayList<>(values);
    //TODO: BGP
  }
  
  @SuppressWarnings("unchecked")
  private void prepareContentDiffModelForAddedInstancesToCollection(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    IAddedInstancesToCollectionsModel addedInstancesToCollectionsModel = model
        .getAddedInstancesToCollectionsModel();
    if (addedInstancesToCollectionsModel != null) {
      List<String> collectionIds = addedInstancesToCollectionsModel
          .getIntancesAddedToCollectionIds();
      if (!collectionIds.isEmpty()) {
        IIdsListParameterModel idsListParameterModel = new IdsListParameterModel();
        idsListParameterModel.setIds(collectionIds);
        IListModel<IDefaultValueChangeModel> addedPropertiesDiffListModel = getAddedPropertiesDiffStrategy
            .execute(idsListParameterModel);
        List<IDefaultValueChangeModel> listOfAddedPropertiesDiff = (List<IDefaultValueChangeModel>) addedPropertiesDiffListModel
            .getList();
        List<IIdAndTypeModel> contentIdBaseTypeInfo = addedInstancesToCollectionsModel
            .getContentIdBaseTypeInfo();
        for (IIdAndTypeModel idAndTypeModel : contentIdBaseTypeInfo) {
          IContentsPropertyDiffModel contentDiffModel = contentToUpdateIdVsPropagationDiffModel
              .get(idAndTypeModel.getId());
          if (contentDiffModel == null) {
            contentDiffModel = new ContentsPropertyDiffModel();
            contentToUpdateIdVsPropagationDiffModel.put(idAndTypeModel.getId(), contentDiffModel);
            contentDiffModel.setContentId(idAndTypeModel.getId());
            contentDiffModel.setBaseType(idAndTypeModel.getType());
          }
          contentDiffModel.setDefaultValuesDiff(listOfAddedPropertiesDiff);
        }
      }
    }
  }
  
  @SuppressWarnings("unchecked")
  private void prepareContentDiffModelForSaveDataRule(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    /*
      ITypesListModel typeidsAssociatedWithDatarules = model.getTypeIdsAssociatedWithDatarules();
      if (typeidsAssociatedWithDatarules != null) {
        IListModel<IContentTypeIdsInfoModel> contentKlassIdsAndTaxonomysModel = getAllInstancesByKlassAndTaxonomyIdsStrategy.execute(typeidsAssociatedWithDatarules);
        List<IContentTypeIdsInfoModel> contentKlassIdsAndTaxonomyList = (List<IContentTypeIdsInfoModel>) contentKlassIdsAndTaxonomysModel.getList();
        for (IContentTypeIdsInfoModel iContentTypeIdsInfoModel : contentKlassIdsAndTaxonomyList) {
          String contentId = iContentTypeIdsInfoModel.getContentId();
          IContentsPropertyDiffModel contentDiffModel = contentToUpdateIdVsPropagationDiffModel.get(contentId);
          if (contentDiffModel == null) {
            contentDiffModel = new ContentsPropertyDiffModel();
            contentToUpdateIdVsPropagationDiffModel.put(contentId, contentDiffModel);
            contentDiffModel.setContentId(contentId);
            contentDiffModel.setBaseType(iContentTypeIdsInfoModel.getBaseType());
          }
        }
      }
    */
  }
  
  private void prepareContentDiffModelForDeletedCollections(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
  {
    IDeletedCollectionAssociatedInstancesInfoForBulkPropagationModel deletedCollectionAssociatedInstancesInfo = model
        .getDeletedCollectionAssociatedInstancesInfo();
    if (deletedCollectionAssociatedInstancesInfo != null) {
      List<IContentTypeIdsInfoModel> contentTypeInfoList = deletedCollectionAssociatedInstancesInfo
          .getContentTypeInfoList();
      if (!contentTypeInfoList.isEmpty()) {
        for (IContentTypeIdsInfoModel iContentTypeIdsInfoModel : contentTypeInfoList) {
          List<String> deletedCollectionIds = deletedCollectionAssociatedInstancesInfo
              .getDeletedCollectionIds();
          IContentsPropertyDiffModel contentDiffModel = contentToUpdateIdVsPropagationDiffModel
              .get(iContentTypeIdsInfoModel.getContentId());
          if (contentDiffModel == null) {
            contentDiffModel = new ContentsPropertyDiffModel();
            contentToUpdateIdVsPropagationDiffModel.put(iContentTypeIdsInfoModel.getContentId(),
                contentDiffModel);
            contentDiffModel.setContentId(iContentTypeIdsInfoModel.getContentId());
            contentDiffModel.setBaseType(iContentTypeIdsInfoModel.getBaseType());
          }
          contentDiffModel.setRemovedCollectionIds(deletedCollectionIds);
        }
      }
    }
  }
  
  private void prepareContentDiffModelForClassificationRuleViolationPropagation(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
  {
    IContentClassificationRuleViolotionModelForBulkPropagation contentClassificationRuleViolationModel = model
        .getContentClassificationRuleViolationModel();
    if (contentClassificationRuleViolationModel != null) {
      IAddedDeletedTypesModel addedDeletedTypes = contentClassificationRuleViolationModel
          .getAddedDeletedTypes();
      List<String> addedKlassIds = addedDeletedTypes.getAddedKlassIds();
      List<String> addedTaxonomyIds = addedDeletedTypes.getAddedTaxonomyIds();
      if (!addedKlassIds.isEmpty() || !addedTaxonomyIds.isEmpty()) {
        IIdAndBaseType contentIdAndBaseTypeDetails = contentClassificationRuleViolationModel
            .getContentIdAndBaseTypeDetails();
        IContentsPropertyDiffModel contentDiffModel = contentToUpdateIdVsPropagationDiffModel
            .get(contentIdAndBaseTypeDetails.getId());
        if (contentDiffModel == null) {
          contentDiffModel = new ContentsPropertyDiffModel();
          contentToUpdateIdVsPropagationDiffModel.put(contentIdAndBaseTypeDetails.getId(),
              contentDiffModel);
          contentDiffModel.setContentId(contentIdAndBaseTypeDetails.getId());
          contentDiffModel.setBaseType(contentIdAndBaseTypeDetails.getBaseType());
        }
        contentDiffModel.setAddedDeletedTypes(addedDeletedTypes);
      }
    }
  }
  
  private void prepareContentDiffModelForAddedPropertiesToRelationshpRelationship(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    /*
      IRelationshipPropertiesToInheritModel propertiesDetailAddedToRelationship = model.getPropertiesDetailAddedToRelationship();
      if (propertiesDetailAddedToRelationship != null) {
        List<IDefaultValueChangeModel> addedAttributes = propertiesDetailAddedToRelationship.getAttributes();
        List<IDefaultValueChangeModel> addedTags = propertiesDetailAddedToRelationship.getTags();
        if (!addedAttributes.isEmpty() || !addedTags.isEmpty()) {
          IValueInheritancePropagationModel valueInheritancePropagationModel = getPropagationDataForAddedRelationshipElementsStrategy.execute(propertiesDetailAddedToRelationship);
          prepareContentDiffModelForValueInheritancePropagationModelFromRelationship(
              valueInheritancePropagationModel, contentToUpdateIdVsPropagationDiffModel);
        }
      }
    */
  }
  
  private void prepareContentDiffModelForValueInheritancePropagationModelFromRelationship(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    IValueInheritancePropagationModel valueInheritancePropagationModel = model
        .getValueInheritancePropagationModel();
    prepareContentDiffModelForValueInheritancePropagationModelFromRelationship(
        valueInheritancePropagationModel, contentToUpdateIdVsPropagationDiffModel);
  }
  
  private void prepareContentDiffModelForValueInheritancePropagationModelFromRelationship(
      IValueInheritancePropagationModel valueInheritancePropagationModel,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    if (valueInheritancePropagationModel != null
        && !valueInheritancePropagationModel.getInheritanceData()
            .isEmpty()) {
      List<IInheritanceDataModel> inheritedDatum = valueInheritancePropagationModel
          .getInheritanceData();
      for (IInheritanceDataModel inheritedData : inheritedDatum) {
        IContentsPropertyDiffModel contentDiffModel = contentToUpdateIdVsPropagationDiffModel
            .get(inheritedData.getTargetContentId());
        if (contentDiffModel == null) {
          contentDiffModel = new ContentsPropertyDiffModel();
          contentToUpdateIdVsPropagationDiffModel.put(inheritedData.getTargetContentId(),
              contentDiffModel);
          contentDiffModel.setContentId(inheritedData.getTargetContentId());
          contentDiffModel.setBaseType(inheritedData.getBaseType());
        }
        Boolean isMerged = contentDiffModel.getIsMerged() == true ? true
            : inheritedData.getIsMerged();
        contentDiffModel.setRelationshipProperties(inheritedData.getRelationshipProperties());
        contentDiffModel.setUpdatedContentIdUponAddingContentsToRelationship(
            inheritedData.getSourceContentId());
        contentDiffModel.setIsMerged(isMerged);
      }
    }
  }
  
  @SuppressWarnings("unchecked")
  private void prepareContentDiffModelForDeletedPropertiesFromSource(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    /*
      Set<String> typeIds = new HashSet<>();
      Map<String, List<String>> deletedPropertiesFromSource = model.getDeletedPropertiesFromSource();
      if (!deletedPropertiesFromSource.isEmpty()) {
        deletedPropertiesFromSource.forEach((propertyIds, associatedTypeIds) -> {
          typeIds.addAll(associatedTypeIds);
        });
        ITypesListModel klassAndTaxonomyListModel = new TypesListModel();
        klassAndTaxonomyListModel.setKlassIds(new ArrayList<>(typeIds));
        klassAndTaxonomyListModel.setTaxonomyIds(new ArrayList<>(typeIds));
    
        IListModel<IContentTypeIdsInfoModel> contentKlassIdsAndTaxonomysModel = getAllInstancesByKlassAndTaxonomyIdsStrategy.execute(klassAndTaxonomyListModel);
        List<IContentTypeIdsInfoModel> contentTypeIdsInfoList = (List<IContentTypeIdsInfoModel>) contentKlassIdsAndTaxonomysModel.getList();
        for (IContentTypeIdsInfoModel iContentTypeIdsInfoModel : contentTypeIdsInfoList) {
          IContentsPropertyDiffModel contentDiffModel = contentToUpdateIdVsPropagationDiffModel.get(iContentTypeIdsInfoModel.getContentId());
          if(contentDiffModel == null) {
            contentDiffModel = new ContentsPropertyDiffModel();
            contentToUpdateIdVsPropagationDiffModel.put(iContentTypeIdsInfoModel.getContentId(), contentDiffModel);
            contentDiffModel.setContentId(iContentTypeIdsInfoModel.getContentId());
            contentDiffModel.setBaseType(iContentTypeIdsInfoModel.getBaseType());
          }
          contentDiffModel.setDeletedPropertiesFromSource(deletedPropertiesFromSource);
          contentDiffModel.setIsIdentifierAttributeEvaluationNeeded(model.getIsIdentifierAttributeEvaluationNeeded());
        }
      }
    */
  }
  
  @SuppressWarnings("unchecked")
  private void prepareContentDiffModelFromRelationshipPropertiesDiff(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    /*
      Set<String> typeIds = new HashSet<>();
      IRelationshipPropertiesADMPropagationModel relationshipPropertiesADM = model.getRelationshipPropertiesADM();
      if(relationshipPropertiesADM != null) {
        IRelationshipPropertiesToInheritModel modifiedProperties = relationshipPropertiesADM.getModifiedProperties();
        List<IDefaultValueChangeModel> modifiedAttributes = modifiedProperties.getAttributes();
        for (IDefaultValueChangeModel modifiedAttribute : modifiedAttributes) {
          typeIds.addAll(modifiedAttribute.getKlassAndChildrenIds());
        }
        List<IDefaultValueChangeModel> modifiedTags = modifiedProperties.getTags();
        for (IDefaultValueChangeModel modifiedTag : modifiedTags) {
          typeIds.addAll(modifiedTag.getKlassAndChildrenIds());
        }
        IRelationshipPropertiesToInheritModel removedProperties = relationshipPropertiesADM.getRemovedProperties();
        List<IDefaultValueChangeModel> removedAttributes = removedProperties.getAttributes();
        for (IDefaultValueChangeModel removeAttribute : removedAttributes) {
          typeIds.addAll(removeAttribute.getKlassAndChildrenIds());
        }
        List<IDefaultValueChangeModel> removedTags = removedProperties.getTags();
        for (IDefaultValueChangeModel modifiedTag : removedTags) {
          typeIds.addAll(modifiedTag.getKlassAndChildrenIds());
        }
        ITypesListModel klassAndTaxonomyListModel = new TypesListModel();
        klassAndTaxonomyListModel.setKlassIds(new ArrayList<>(typeIds));
        klassAndTaxonomyListModel.setTaxonomyIds(new ArrayList<>(typeIds));
    
        IListModel<IContentTypeIdsInfoModel> contentKlassIdsAndTaxonomysModel = getAllInstancesByKlassAndTaxonomyIdsStrategy.execute(klassAndTaxonomyListModel);
        List<IContentTypeIdsInfoModel> contentTypeIdsInfoList = (List<IContentTypeIdsInfoModel>) contentKlassIdsAndTaxonomysModel.getList();
        for (IContentTypeIdsInfoModel iContentTypeIdsInfoModel : contentTypeIdsInfoList) {
          IContentsPropertyDiffModel contentDiffModel = contentToUpdateIdVsPropagationDiffModel.get(iContentTypeIdsInfoModel.getContentId());
          if(contentDiffModel == null) {
            contentDiffModel = new ContentsPropertyDiffModel();
            contentToUpdateIdVsPropagationDiffModel.put(iContentTypeIdsInfoModel.getContentId(), contentDiffModel);
            contentDiffModel.setContentId(iContentTypeIdsInfoModel.getContentId());
            contentDiffModel.setBaseType(iContentTypeIdsInfoModel.getBaseType());
          }
          contentDiffModel.setRelationshipPropertiesADM(relationshipPropertiesADM);
        }
      }
    */
  }
  
  private void prepareContentDiffModelFromDefaultValueDiffListForBulkApply(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    IPropertyDiffModelForBulkApply propertyDiffModelForBulkApply = model
        .getPropertyDiffModelForBulkApply();
    if (propertyDiffModelForBulkApply != null) {
      List<IAttributeIdValueModel> attributesToApply = propertyDiffModelForBulkApply
          .getAttributesToApply();
      List<IIdAndBaseType> instanceInfoList = propertyDiffModelForBulkApply.getInstanceInfoList();
      List<ITagIdValueModel> tagsToApply = propertyDiffModelForBulkApply.getTagsToApply();
      if (!instanceInfoList.isEmpty()) {
        for (IIdAndBaseType iIdAndBaseType : instanceInfoList) {
          IContentsPropertyDiffModel contentDiffModel = contentToUpdateIdVsPropagationDiffModel
              .get(iIdAndBaseType.getId());
          if (contentDiffModel == null) {
            contentDiffModel = new ContentsPropertyDiffModel();
            contentToUpdateIdVsPropagationDiffModel.put(iIdAndBaseType.getId(), contentDiffModel);
            contentDiffModel.setContentId(iIdAndBaseType.getId());
            contentDiffModel.setBaseType(iIdAndBaseType.getBaseType());
          }
          contentDiffModel.setAttributeInstancesValueToApply(attributesToApply);
          contentDiffModel.setTagInstancesValueToApply(tagsToApply);
          contentDiffModel
              .setLanguageForBulkApply(propertyDiffModelForBulkApply.getLanguageForBulkApply());
        }
      }
    }
  }
  
  @SuppressWarnings("unchecked")
  private void prepareContentDiffModelFromDefaultValueDiffList(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    /*
      IPropertyDiffModelForBulkPropagation propertyDiffModelForBulkPropagation = model.getPropertyDiffModelForBulkPropagation();
      if (propertyDiffModelForBulkPropagation != null) {
        List<IDefaultValueChangeModel> defaultValueDiffList = propertyDiffModelForBulkPropagation.getDefaultValueDiffList();
        List<IIdAndBaseType> instanceInfoList = propertyDiffModelForBulkPropagation.getInstanceInfoList();
        new ObjectMapper().writeValueAsString(defaultValueDiffList);
        if(!instanceInfoList.isEmpty()) {
          for (IIdAndBaseType iIdAndBaseType : instanceInfoList) {
            IContentsPropertyDiffModel contentDiffModel = contentToUpdateIdVsPropagationDiffModel.get(iIdAndBaseType.getId());
            if(contentDiffModel == null) {
              contentDiffModel = new ContentsPropertyDiffModel();
              contentToUpdateIdVsPropagationDiffModel.put(iIdAndBaseType.getId(), contentDiffModel);
              contentDiffModel.setContentId(iIdAndBaseType.getId());
              contentDiffModel.setBaseType(iIdAndBaseType.getBaseType());
            }
            contentDiffModel.setDefaultValuesDiff(defaultValueDiffList);
          }
        }
        else {
          Set<String> klassIds = new HashSet<>();
          Set<String> taxonomyIds = new HashSet<>();
          Set<String> collectionIds = new HashSet<>();
          for (IDefaultValueChangeModel defaultValueDiff : defaultValueDiffList) {
            List<String> klassAndchildrenIds = defaultValueDiff.getKlassAndChildrenIds();
            String sourceType = defaultValueDiff.getSourceType();
            switch (sourceType) {
              case CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE:
                klassIds.addAll(klassAndchildrenIds);
                break;
              case CommonConstants.TAXONOMY_CONFLICTING_SOURCE_TYPE:
                taxonomyIds.addAll(klassAndchildrenIds);
                break;
              case CommonConstants.COLLECTION_CONFLICTING_SOURCE_TYPE:
                collectionIds.addAll(klassAndchildrenIds);
                break;
              default:
                break;
            }
          }
          ITypesListModel klassAndTaxonomyListModel = new TypesListModel();
          klassAndTaxonomyListModel.setKlassIds(new ArrayList<>(klassIds));
          klassAndTaxonomyListModel.setTaxonomyIds(new ArrayList<>(taxonomyIds));
          klassAndTaxonomyListModel.setCollectionIds(new ArrayList<>(collectionIds));
    
          IListModel<IContentTypeIdsInfoModel> contentKlassIdsAndTaxonomysModel = getAllInstancesByKlassAndTaxonomyIdsStrategy.execute(klassAndTaxonomyListModel);
          List<IContentTypeIdsInfoModel> contentTypeIdsInfoList = (List<IContentTypeIdsInfoModel>) contentKlassIdsAndTaxonomysModel.getList();
          for (IContentTypeIdsInfoModel iContentTypeIdsInfoModel : contentTypeIdsInfoList) {
            IContentsPropertyDiffModel contentDiffModel = contentToUpdateIdVsPropagationDiffModel.get(iContentTypeIdsInfoModel.getContentId());
            if(contentDiffModel == null) {
              contentDiffModel = new ContentsPropertyDiffModel();
              contentToUpdateIdVsPropagationDiffModel.put(iContentTypeIdsInfoModel.getContentId(), contentDiffModel);
              contentDiffModel.setContentId(iContentTypeIdsInfoModel.getContentId());
              contentDiffModel.setBaseType(iContentTypeIdsInfoModel.getBaseType());
            }
            contentDiffModel.setDefaultValuesDiff(defaultValueDiffList);
            contentDiffModel.setIsIdentifierAttributeEvaluationNeeded(model.getIsIdentifierAttributeEvaluationNeeded());
          }
        }
      }
    */
  }
  
  @SuppressWarnings("unchecked")
  private void prepareContentDiffModelUponDeleteKlasses(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    /*
      List<String> deletedKlassIds = model.getDeletedKlassIds();
      if(!deletedKlassIds.isEmpty()) {
        ITypesListModel klassAndTaxonomyListModel = new TypesListModel();
        klassAndTaxonomyListModel.setKlassIds(deletedKlassIds);
        IListModel<IContentTypeIdsInfoModel> contentKlassIdsAndTaxonomysModel = getAllInstancesByKlassAndTaxonomyIdsStrategy.execute(klassAndTaxonomyListModel);
        List<IContentTypeIdsInfoModel> contentTypeIdsInfoList = (List<IContentTypeIdsInfoModel>) contentKlassIdsAndTaxonomysModel.getList();
        for (IContentTypeIdsInfoModel iContentTypeIdsInfoModel : contentTypeIdsInfoList) {
          IContentsPropertyDiffModel contentDiffModel = contentToUpdateIdVsPropagationDiffModel.get(iContentTypeIdsInfoModel.getContentId());
          if(contentDiffModel == null) {
            contentDiffModel = new ContentsPropertyDiffModel();
            contentToUpdateIdVsPropagationDiffModel.put(iContentTypeIdsInfoModel.getContentId(), contentDiffModel);
            contentDiffModel.setContentId(iContentTypeIdsInfoModel.getContentId());
            contentDiffModel.setBaseType(iContentTypeIdsInfoModel.getBaseType());
          }
          contentDiffModel.setDeletedKlassIds(deletedKlassIds);
        }
      }
    */
  }
  
  private void prepareContentDiffModelUponDeleteOfRelationshipsAndNatureRelationships(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    /*
      List<String> deletedRelationshipIds = model.getDeletedRelationshipIds();
      List<String> deletedNatureRelationshipIds = model.getDeletedNatureRelationshipIds();
      if (!deletedRelationshipIds.isEmpty() || !deletedNatureRelationshipIds.isEmpty()) {
        IDeleteRelationshipInstancesStrategyModel deleteRelationshipInstancesStrategyModel = new DeleteRelationshipInstancesStrategyModel();
        deleteRelationshipInstancesStrategyModel.setDeletedRelationshipIds(deletedRelationshipIds);
        deleteRelationshipInstancesStrategyModel.setDeletedNatureRelationshipIds(deletedNatureRelationshipIds);
        IRelationshipInstanceDeleteResponseModel relationshipInstancesDeleteResponse = deleteRelationshipInstancesStrategy.execute(deleteRelationshipInstancesStrategyModel);
        IRelationshipInstanceDeleteSuccessResponseModel successResponse = (IRelationshipInstanceDeleteSuccessResponseModel) relationshipInstancesDeleteResponse.getSuccess();
        List<IContentTypeIdsInfoModel> contentKlassAndTaxonomyListModel = successResponse.getModifiedInstances();
        for (IContentTypeIdsInfoModel iContentTypeIdsInfoModel : contentKlassAndTaxonomyListModel) {
          IContentsPropertyDiffModel contentDiffModel = contentToUpdateIdVsPropagationDiffModel.get(iContentTypeIdsInfoModel.getContentId());
          if(contentDiffModel == null) {
            contentDiffModel = new ContentsPropertyDiffModel();
            contentToUpdateIdVsPropagationDiffModel.put(iContentTypeIdsInfoModel.getContentId(), contentDiffModel);
            contentDiffModel.setContentId(iContentTypeIdsInfoModel.getContentId());
            contentDiffModel.setBaseType(iContentTypeIdsInfoModel.getBaseType());
          }
          contentDiffModel.setDeletedRelationshipIds(deletedRelationshipIds);
          contentDiffModel.setDeletedNatureRelationshipIds(deletedNatureRelationshipIds);
        }
      }
    */
  }
  
  public void prepareContentDiffModelForDeletedContentFromRelationship(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    List<IContentsDeleteFromRelationshipDataPreparationModel> deletedContentsFromRelationshipInfo = model
        .getDeletedContentsFromRelationshipInfo();
    for (IContentsDeleteFromRelationshipDataPreparationModel infoModel : deletedContentsFromRelationshipInfo) {
      String sourceContentId = infoModel.getSourceContentId();
      String relationshipId = infoModel.getRelationshipId();
      String baseType = infoModel.getBaseType();
      List<String> deletedElements = infoModel.getDeletedElements();
      deletedElements.forEach(deletedElement -> {
        IContentsPropertyDiffModel contentPropertyDiffModel = contentToUpdateIdVsPropagationDiffModel
            .get(deletedElement);
        if (contentPropertyDiffModel == null) {
          contentPropertyDiffModel = new ContentsPropertyDiffModel();
          contentPropertyDiffModel.setContentId(deletedElement);
          contentPropertyDiffModel.setBaseType(baseType);
          contentToUpdateIdVsPropagationDiffModel.put(deletedElement, contentPropertyDiffModel);
        }
        IContentsDeleteFromRelationshipModel deleteContentFrom = new ContentsDeleteFromRelationshipModel();
        deleteContentFrom.setRelationshipId(relationshipId);
        deleteContentFrom.setSourceContentId(sourceContentId);
        deleteContentFrom.setPropertyIds(infoModel.getPropertyIds());
        contentPropertyDiffModel.setDeleteContentFromRelationship(deleteContentFrom);
      });
    }
  }
  
  private void prepareContentDiffModelForDeletedContents(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    IDeleteContentsDataPreparationModel deletedContentsInfo = model.getDeletedContentsInfo();
    if (deletedContentsInfo != null) {
      List<IDeletedContentTypeIdsInfoModel> klassInstancesToUpdate = deletedContentsInfo
          .getKlassInstancesToUpdate();
      List<String> deletedContentIds = deletedContentsInfo.getDeletedContentIds();
      klassInstancesToUpdate.forEach(contentKlassIdsAnTaxonomyIds -> {
        String contentId = contentKlassIdsAnTaxonomyIds.getContentId();
        String baseType = contentKlassIdsAnTaxonomyIds.getBaseType();
        String criidToRemove = contentKlassIdsAnTaxonomyIds.getCriidToRemove();
        String natureCriidToRemove = contentKlassIdsAnTaxonomyIds.getNatureCriidToRemove();
        IContentsPropertyDiffModel contentPropertyDiffModel = contentToUpdateIdVsPropagationDiffModel
            .get(contentId);
        if (contentPropertyDiffModel == null) {
          contentPropertyDiffModel = new ContentsPropertyDiffModel();
          contentPropertyDiffModel.setContentId(contentId);
          contentPropertyDiffModel.setBaseType(baseType);
          contentToUpdateIdVsPropagationDiffModel.put(contentId, contentPropertyDiffModel);
        }
        if (criidToRemove != null || natureCriidToRemove != null) {
          IUpdateCRIIDInfoModel criidInfoModel = new UpdateCRIIDInfoModel();
          criidInfoModel.setContentId(contentId);
          criidInfoModel.setBaseType(baseType);
          if (criidToRemove != null) {
            criidInfoModel.setRelationshipCriidsToRemove(Arrays.asList(criidToRemove));
            criidInfoModel.setChangedRelationshipIds(new HashSet<>(
                Arrays.asList(contentKlassIdsAnTaxonomyIds.getChangedRelationshipId())));
          }
          if (natureCriidToRemove != null) {
            criidInfoModel.setNatureRelationshipCriidsToRemove(Arrays.asList(natureCriidToRemove));
            criidInfoModel.setChangedNatureRelationshipIds(new HashSet<>(
                Arrays.asList(contentKlassIdsAnTaxonomyIds.getChangedNatureRelationshipId())));
          }
          contentPropertyDiffModel.setUpdateCriidAndDefaulAssetInstanceIdInfo(criidInfoModel);
        }
        contentPropertyDiffModel.setDeletedContentIds(deletedContentIds);
        contentPropertyDiffModel.setIsIdentifierAttributeEvaluationNeeded(true);
      });
    }
  }
  
  @SuppressWarnings("unchecked")
  private void prepareContentDiffModelForContextualDataTransfer(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    /*
      IContextualDataPreparationModel prepModel = model.getPropagableContextualData();
      if(prepModel != null) {
        Map<String, IPropagableContextualDataModel> propagableContextualData = prepModel.getContextualData();
        List<String> contextIds = new ArrayList<>(propagableContextualData.keySet());
        if (!contextIds.isEmpty()) {
          String sourceContentId = prepModel.getSourceContentId();
          String klassInstanceId = prepModel.getKlassInstanceId();
          IGetVariantInfoForDataTransferRequestModel requestModel = new GetVariantInfoForDataTransferRequestModel();
          requestModel.setBaseType(prepModel.getBaseType());
          //requestModel.setContextIds(contextIds);
          if(klassInstanceId == null ) {
            requestModel.setKlassInstanceId(sourceContentId);
          }
          else {
            requestModel.setParentVariantInstanceId(sourceContentId);
          }
          List<IGetVariantInfoForDataTransferResponseModel> variants = (List<IGetVariantInfoForDataTransferResponseModel>) getImmediateVariantsDetailStrategy.execute(requestModel).getList();
          for (IGetVariantInfoForDataTransferResponseModel variantDetail : variants) {
            String contextId = variantDetail.getContextId();
            IPropagableContextualDataModel contextualData = propagableContextualData.get(contextId);
            if(contextualData == null) {
              continue;
            }
            String variantInstanceId = variantDetail.getContentId();
            IContentsPropertyDiffModel contentPropertyDiffModel = contentToUpdateIdVsPropagationDiffModel.get(variantInstanceId);
            if(contentPropertyDiffModel == null) {
              contentPropertyDiffModel = new ContentsPropertyDiffModel();
              contentPropertyDiffModel.setContentId(variantInstanceId);
              contentPropertyDiffModel.setBaseType(variantDetail.getBaseType());
              contentToUpdateIdVsPropagationDiffModel.put(variantInstanceId, contentPropertyDiffModel);
            }
            IContextualDataForPropagationModel propagationModel = new ContextualDataForPropagationModel();
            propagationModel.setContextId(contextId);
            propagationModel.setSourceContentId(sourceContentId);
            propagationModel.setAttributes(contextualData.getAttributes());
            propagationModel.setTags(contextualData.getTags());
            contentPropertyDiffModel.setContextualDataForPropagation(propagationModel);
          }
        }
      }
    */
  }
  
  private void prepareContentDiffModelForAddedOrDeletedVariantsInKlassInstance(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    IAddedOrDeletedVariantsDataPreparationModel addedOrDeletedVariants = model
        .getAddedOrDeletedVariantsInKlassInstance();
    if (addedOrDeletedVariants != null) {
      IContentTypeIdsInfoModel klassInstanceToUpdate = addedOrDeletedVariants
          .getKlassInstanceToUpdate();
      List<String> deletedVariantsIds = addedOrDeletedVariants.getDeletedVariantsIds();
      List<String> addedVariantsIds = addedOrDeletedVariants.getAddedVariantsIds();
      IAddedOrDeletedVariantsDataPreparationModel addedOrDeletedVariantsInKlassInstance = new AddedOrDeletedVariantsDataPreparationModel();
      addedOrDeletedVariantsInKlassInstance.setAddedVariantsIds(addedVariantsIds);
      addedOrDeletedVariantsInKlassInstance.setDeletedVariantsIds(deletedVariantsIds);
      
      String contentId = klassInstanceToUpdate.getContentId();
      IContentsPropertyDiffModel contentPropertyDiffModel = contentToUpdateIdVsPropagationDiffModel
          .get(contentId);
      if (contentPropertyDiffModel == null) {
        contentPropertyDiffModel = new ContentsPropertyDiffModel();
        contentPropertyDiffModel.setContentId(contentId);
        contentPropertyDiffModel.setBaseType(klassInstanceToUpdate.getBaseType());
        contentToUpdateIdVsPropagationDiffModel.put(contentId, contentPropertyDiffModel);
      }
      contentPropertyDiffModel
          .setAddedOrDeletedVariantsInKlassInstance(addedOrDeletedVariantsInKlassInstance);
    }
  }
  
  private void prepareContentDiffForUpdatingIsMerged(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    List<String> instancesToUpdateIsMerged = model.getInstancesToUpdateIsMerged();
    if (instancesToUpdateIsMerged == null || instancesToUpdateIsMerged.isEmpty()) {
      return;
    }
    for (String contentId : instancesToUpdateIsMerged) {
      IContentsPropertyDiffModel contentsDiffModel = contentToUpdateIdVsPropagationDiffModel
          .get(contentId);
      if (contentsDiffModel == null) {
        contentsDiffModel = new ContentsPropertyDiffModel();
        contentToUpdateIdVsPropagationDiffModel.put(contentId, contentsDiffModel);
        contentsDiffModel.setContentId(contentId);
        contentsDiffModel.setBaseType(Constants.ARTICLE_INSTANCE_BASE_TYPE);
      }
      contentsDiffModel.setIsMerged(true);
    }
  }
  
  private void prepareContentDiffForDeletedTranslations(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    IDeletedTranslationsInfoModel deletedTranslationsInfoModel = model
        .getDeletedTranslationsInfoModel();
    if (deletedTranslationsInfoModel == null) {
      return;
    }
    String sourceContentId = deletedTranslationsInfoModel.getContentId();
    List<String> languageCodesForDeletedTranslations = deletedTranslationsInfoModel
        .getLanguageCodes();
    List<IIdAndBaseType> relatedContentsInfo = deletedTranslationsInfoModel
        .getRelatedContentsInfo();
    for (IIdAndBaseType relatedContent : relatedContentsInfo) {
      String contentId = relatedContent.getId();
      IContentsPropertyDiffModel contentsDiffModel = contentToUpdateIdVsPropagationDiffModel
          .get(contentId);
      if (contentsDiffModel == null) {
        contentsDiffModel = new ContentsPropertyDiffModel();
        contentToUpdateIdVsPropagationDiffModel.put(contentId, contentsDiffModel);
        contentsDiffModel.setContentId(contentId);
        contentsDiffModel.setBaseType(relatedContent.getBaseType());
      }
      Map<String, List<String>> deletedTranslationsInfo = contentsDiffModel
          .getDeletedTranslationsInfo();
      if (deletedTranslationsInfo == null) {
        deletedTranslationsInfo = new HashMap<>();
        contentsDiffModel.setDeletedTranslationsInfo(deletedTranslationsInfo);
      }
      deletedTranslationsInfo.put(sourceContentId, languageCodesForDeletedTranslations);
    }
  }
  
  private void prepareContentDiffForUpdatingCriidAndDefaultAssetInstanceId(
      IContentDiffModelToPrepareDataForBulkPropagation model,
      Map<String, IContentsPropertyDiffModel> contentToUpdateIdVsPropagationDiffModel)
      throws Exception
  {
    List<IRelationshipDataTransferInfoModel> updateCriidAndDefaultAssetInstanceIdInfoModel = model
        .getUpdateCriidAndDefaultAssetInstanceIdInfoModel();
    for (IRelationshipDataTransferInfoModel updateCriidInfoModel : updateCriidAndDefaultAssetInstanceIdInfoModel) {
      //String contentId = updateCriidInfoModel.getContentId();
      /*   IContentsPropertyDiffModel contentsDiffModel = contentToUpdateIdVsPropagationDiffModel
          .get(contentId);
      if (contentsDiffModel == null) {
        contentsDiffModel = new ContentsPropertyDiffModel();
        contentToUpdateIdVsPropagationDiffModel.put(contentId, contentsDiffModel);
        contentsDiffModel.setContentId(contentId);
        contentsDiffModel.setBaseType(updateCriidInfoModel.getBaseType());
      }*/
      IUpdateCRIIDInfoModel criidInfo = new UpdateCRIIDInfoModel();
    //  criidInfo.setDefaultAssetInstanceId(updateCriidInfoModel.getDefaultAssetInstanceId());
      /*     criidInfo.setNatureRelationshipCriidsToAdd(
          updateCriidInfoModel.getNatureRelationshipCriidsToAdd());
      criidInfo.setNatureRelationshipCriidsToRemove(
          updateCriidInfoModel.getNatureRelationshipCriidsToRemove());
      criidInfo.setRelationshipCriidsToAdd(updateCriidInfoModel.getRelationshipCriidsToAdd());
      criidInfo.setRelationshipCriidsToRemove(updateCriidInfoModel.getRelationshipCriidsToRemove());
      criidInfo.setRemovedAssetInstanceIds(updateCriidInfoModel.getRemovedAssetInstanceIds());
      criidInfo.setChangedRelationshipIds(updateCriidInfoModel.getChangedRelationshipIds());
      criidInfo
          .setChangedNatureRelationshipIds(updateCriidInfoModel.getChangedNatureRelationshipIds());
      contentsDiffModel.setUpdateCriidAndDefaulAssetInstanceIdInfo(criidInfo);*/
    }
  }
  
}
