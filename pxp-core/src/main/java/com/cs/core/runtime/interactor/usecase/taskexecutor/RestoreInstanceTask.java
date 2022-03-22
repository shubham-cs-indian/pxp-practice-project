package com.cs.core.runtime.interactor.usecase.taskexecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.AddedDeletedTypesModel;
import com.cs.core.config.interactor.model.klass.ContentTypeIdsInfoModel;
import com.cs.core.config.interactor.model.klass.IAddedDeletedTypesModel;
import com.cs.core.config.interactor.model.klass.IAddedOrDeletedVariantsDataPreparationModel;
import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.config.interactor.model.klass.ITypeInfoWithContentIdentifiersModel;
import com.cs.core.config.interactor.model.language.IKlassInstanceDiffForLanguageInheritanceModel;
import com.cs.core.config.interactor.model.language.KlassInstanceDiffForLanguageInheritanceModel;
import com.cs.core.config.interactor.model.relationship.IRestoreRelationshipInstancesRequestModel;
import com.cs.core.config.interactor.usecase.versionrollback.IGetConfigDetailForVersionRollbackStrategy;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndBaseType;
import com.cs.core.runtime.interactor.model.bulkpropagation.AddedOrDeletedVariantsDataPreparationModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.ContentClassificationRuleViolotionModelForBulkPropagation;
import com.cs.core.runtime.interactor.model.bulkpropagation.ContentDiffModelToPrepareDataForBulkPropagation;
import com.cs.core.runtime.interactor.model.bulkpropagation.IContentClassificationRuleViolotionModelForBulkPropagation;
import com.cs.core.runtime.interactor.model.bulkpropagation.IContentDiffModelToPrepareDataForBulkPropagation;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndTypeModel;

import com.cs.core.runtime.interactor.model.configuration.InstanceTypeGetRequestModel;
import com.cs.core.runtime.interactor.model.context.ContextualDataTransferInputModel;
import com.cs.core.runtime.interactor.model.context.IContextualDataTransferInputModel;
import com.cs.core.runtime.interactor.model.datarule.IApplyEffectModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.propagation.EvaluateIdentifierAttributesInstanceModel;
import com.cs.core.runtime.interactor.model.propagation.IEvaluateIdentifierAttributesInstanceModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInputModel;
import com.cs.core.runtime.interactor.model.relationship.RelationshipDataTransferInputModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForVersionRollbackModel;
import com.cs.core.runtime.interactor.model.transfer.DataTransferInputModel;
import com.cs.core.runtime.interactor.model.transfer.IDataTransferInputModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.variants.IRestoreVariantRequestModel;
import com.cs.core.runtime.interactor.model.variants.RestoreVariantRequestModel;
import com.cs.core.runtime.interactor.model.version.IKlassInstanceVersionTypeModel;
import com.cs.core.runtime.interactor.model.versionrollback.IRollbackInstanceStrategyResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.strategy.usecase.klassinstance.IRestoreKlassInstanceStrategy;

@Component
public class RestoreInstanceTask extends AbstractRuntimeInteractor<IIdAndTypeModel, IModel>
    implements IRestoreInstanceTask {
  
  @Autowired
  protected TransactionThreadData                             controllerThread;
  
  @Autowired
  protected IGetConfigDetailForVersionRollbackStrategy        getConfigDetailForVersionRollbackStrategy;
  
  /*@Autowired
  protected IRestoreKlassInstanceStrategy              restoreArticleInstanceStrategy;
  
  @Autowired
  protected IRestoreKlassInstanceStrategy              restoreAssetInstanceStrategy;
  
  @Autowired
  protected IRestoreKlassInstanceStrategy              restoreMarketInstanceStrategy;
  
  @Autowired
  protected IRestoreKlassInstanceStrategy              restoreSupplierInstanceStrategy;
  
  @Autowired
  protected IRestoreKlassInstanceStrategy              restoreTextAssetInstanceStrategy;
  
  @Autowired
  protected IRestoreKlassInstanceStrategy              restoreVirtualCatalogInstanceStrategy;*/
  
  
  @Autowired
  protected KlassInstanceUtils                                klassInstanceUtils;
  
  /*@Autowired
  protected IGetInstanceVersionTypeStrategy            getInstanceVersionTypeStrategy;*/
  
  @Autowired
  protected Boolean                                           isKafkaLoggingEnabled;
  
  /*@Autowired
  protected IGetInstanceVersionTypeWithPresetInfoStrategy     getInstanceVersionTypeWithPresetInfoForMakeupStrategy;
  
  @Autowired
  protected IGetInstanceVersionTypeWithPresetInfoStrategy     getInstanceVersionTypeWithPresetInfoStrategy;*/
  
  @Override
  protected IModel executeInternal(IIdAndTypeModel instanceToResore) throws Exception
  {
    try {
      ITransactionData transactionData = controllerThread.getTransactionData();
      String klassInstanceId = instanceToResore.getId();
      String baseType = instanceToResore.getType();
      
      InstanceTypeGetRequestModel typeGetRequestModel = new InstanceTypeGetRequestModel();
      typeGetRequestModel.setId(klassInstanceId);
      typeGetRequestModel.setShouldFetchFromArchive(true);
      typeGetRequestModel.setBaseType(baseType);
      
      IKlassInstanceVersionTypeModel typesModel = null;
      IRollbackInstanceStrategyResponseModel strategyResponseModel = null;
      IGetConfigDetailsForVersionRollbackModel configDetails = null;
      try {
        typesModel = getInstanceVersionType(typeGetRequestModel, baseType);
        
        IMulticlassificationRequestModel requestModel = fillMulticlassificationRequestModel(
            transactionData, typesModel);
        
        configDetails = getConfigDetails(requestModel, baseType);
        IRestoreVariantRequestModel variantRestoreModel = new RestoreVariantRequestModel();
        variantRestoreModel.setConfigDetails(configDetails);
        variantRestoreModel.setId(klassInstanceId);
        
        IRestoreKlassInstanceStrategy instanceRestoreStrategy = getRestoreInstanceStrategy(
            baseType);
        strategyResponseModel = instanceRestoreStrategy.execute(variantRestoreModel);
        
      }
      catch (Throwable e) {
        removeVariantIdsFromParentKlassInstance(klassInstanceId, typesModel);
        throw e;
      }
      
      evaluateGoldenRecord(baseType, klassInstanceId);
      
      restoreRelationships(klassInstanceId,
          strategyResponseModel.getRestoreRelationshipInstancesData());
      
      restoreReferences(klassInstanceId, strategyResponseModel.getContentInfo());
      
      klassInstanceUtils.updateAssociatedSearchableInstances(
          strategyResponseModel.getUpdateSearchableDocumentData());
      
      restoreVariants(strategyResponseModel.getVariantIdsToRestore(), baseType);
      
      evaluateStatisticsForInstance(strategyResponseModel.getContentInfo());
      
      evaluateClassificationRules(strategyResponseModel);
      
      evaluateIdentifierAttributes(strategyResponseModel, typesModel, configDetails);
      
      initiateRelationshipDataTransfer(strategyResponseModel);
      
      initiateContextualDataTranfer(strategyResponseModel, configDetails);
      
      initiateLanguageInheritance(strategyResponseModel, configDetails);
    }
    catch (Throwable ex) {
      if (isKafkaLoggingEnabled) {
        //TODO: BGP
      }
    }
    
    return null;
  }
  
  private void initiateLanguageInheritance(
      IRollbackInstanceStrategyResponseModel rollbackInstanceResponseModel,
      IGetConfigDetailsForVersionRollbackModel configDetails) throws Exception
  {
    IDataTransferInputModel dataForDataTransfer = rollbackInstanceResponseModel
        .getDataForDataTransfer();
    IKlassInstanceDiffForLanguageInheritanceModel klassInstanceDiffForLanguageInheritance = new KlassInstanceDiffForLanguageInheritanceModel();
    klassInstanceDiffForLanguageInheritance.setContentId(dataForDataTransfer.getContentId());
    klassInstanceDiffForLanguageInheritance.setBaseType(dataForDataTransfer.getBaseType());
    
    List<String> languageCodes = rollbackInstanceResponseModel.getContentInfo()
        .getLanguageCodes();
    
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    
    List<String> dependentAttributeIds = new ArrayList<>();
    referencedAttributes.forEach((attributeId, referenceAttribute) -> {
      Boolean isTranslatable = referenceAttribute.getIsTranslatable();
      if (isTranslatable) {
        dependentAttributeIds.add(attributeId);
      }
    });
    
    languageCodes.forEach(languageCode -> {
      klassInstanceDiffForLanguageInheritance.getModifiedAttributes()
          .put(languageCode, dependentAttributeIds);
    });
    //TODO: BGP
  }
  
  private void initiateContextualDataTranfer(
      IRollbackInstanceStrategyResponseModel strategyResponseModel,
      IGetConfigDetailsForVersionRollbackModel configDetails) throws Exception
  {
    ITypeInfoWithContentIdentifiersModel contentInfo = strategyResponseModel.getContentInfo();
    IDataTransferInputModel rollbackInstanceDataForDataTransfer = strategyResponseModel
        .getDataForDataTransfer();
    List<String> languageCodes = contentInfo.getLanguageCodes();
    
    IDataTransferInputModel dataTransfer = new DataTransferInputModel();
    dataTransfer.setContentId(contentInfo.getContentId());
    dataTransfer.setBaseType(contentInfo.getBaseType());
    dataTransfer.setModifiedLanguageCodes(languageCodes);
    
    fillAttributeIds(configDetails, dataTransfer, languageCodes);
    fillTags(configDetails, dataTransfer);
    
    fillChangedAttributeAndTags(rollbackInstanceDataForDataTransfer, dataTransfer);
    
    IContextualDataTransferInputModel contextualDataTransfer = new ContextualDataTransferInputModel();
    contextualDataTransfer.setDataTransfer(dataTransfer);
    List<String> variantIdsToExcludeForDataTransfer = strategyResponseModel.getVariantIdsToDelete();
    variantIdsToExcludeForDataTransfer.addAll(strategyResponseModel.getVariantIdsToRestore());
    contextualDataTransfer
        .setvariantIdsToExcludeForDataTransfer(variantIdsToExcludeForDataTransfer);
    
    //TODO: BGP
  }
  
  private void fillChangedAttributeAndTags(
      IDataTransferInputModel rollbackInstanceDataForDataTransfer,
      IDataTransferInputModel dataTransfer)
  {
    dataTransfer
        .setChangedAttributeIds(rollbackInstanceDataForDataTransfer.getChangedAttributeIds());
    dataTransfer.setChangedDependentAttributeIdsMap(
        rollbackInstanceDataForDataTransfer.getChangedDependentAttributeIdsMap());
    dataTransfer.setChangedTagsIds(rollbackInstanceDataForDataTransfer.getChangedTagsIds());
  }
  
  private void fillTags(IGetConfigDetailsForVersionRollbackModel configDetails,
      IDataTransferInputModel dataTransfer)
  {
    Map<String, ITag> referencedTags = configDetails.getReferencedTags();
    Set<String> referenceTagIds = referencedTags.keySet();
    dataTransfer.getAddedTagIds()
        .addAll(referenceTagIds);
  }
  
  private void fillAttributeIds(IGetConfigDetailsForVersionRollbackModel configDetails,
      IDataTransferInputModel dataTransfer, List<String> languageCodes)
  {
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    
    List<String> dependentAttributeIds = new ArrayList<>();
    referencedAttributes.forEach((attributeId, referenceAttribute) -> {
      Boolean isTranslatable = referenceAttribute.getIsTranslatable();
      if (isTranslatable) {
        dependentAttributeIds.add(attributeId);
      }
      else {
        dataTransfer.getAddedAttributeIds()
            .add(attributeId);
      }
    });
    
    languageCodes.forEach(languageCode -> {
      dataTransfer.getAddedDependentAttributeIdsMap()
          .put(languageCode, dependentAttributeIds);
    });
  }
  
  private void removeVariantIdsFromParentKlassInstance(String klassInstanceId,
      IKlassInstanceVersionTypeModel typesModel) throws Exception
  {
    String parentId = typesModel.getParentId();
    if (parentId != null) {
      IContentDiffModelToPrepareDataForBulkPropagation diffModelToPrepareBulkPropagationData = new ContentDiffModelToPrepareDataForBulkPropagation();
      IAddedOrDeletedVariantsDataPreparationModel addedOrDeletedVariantsData = new AddedOrDeletedVariantsDataPreparationModel();
      
      IContentTypeIdsInfoModel contentTypeIdsInfoModel = new ContentTypeIdsInfoModel();
      contentTypeIdsInfoModel.setContentId(parentId);
      contentTypeIdsInfoModel.setBaseType(typesModel.getBaseType());
      
      List<String> deletedVariantIds = new ArrayList<>();
      deletedVariantIds.add(klassInstanceId);
      addedOrDeletedVariantsData.setDeletedVariantsIds(deletedVariantIds);
      addedOrDeletedVariantsData.setKlassInstanceToUpdate(contentTypeIdsInfoModel);
      diffModelToPrepareBulkPropagationData
          .setAddedOrDeletedVariantsInKlassInstance(addedOrDeletedVariantsData);
      //TODO: BGP
    }
  }
  
  private void restoreVariants(List<String> variantIdsToRestore, String baseType) throws Exception
  {
    for (String idToRestore : variantIdsToRestore) {
      IIdAndTypeModel idTypeModel = new IdAndTypeModel();
      idTypeModel.setId(idToRestore);
      idTypeModel.setType(baseType);
    //TODO: BGP
    }
  }
  
  private void evaluateStatisticsForInstance(IContentTypeIdsInfoModel contentTypeIdsInfoModel)
      throws Exception
  {
    List<IContentTypeIdsInfoModel> listOfContentTypeIdsInfoModel = new ArrayList<>();
    listOfContentTypeIdsInfoModel.add(contentTypeIdsInfoModel);
    //TODO: BGP
  }
  
  private void evaluateClassificationRules(
      IRollbackInstanceStrategyResponseModel rollbackInstanceResponseModel) throws Exception
  {
    IApplyEffectModel applyEffect = rollbackInstanceResponseModel.getApplyEffect();
    List<String> typesToApply = applyEffect.getTypesToApply();
    List<String> taxonomiesToApply = applyEffect.getTaxonomiesToApply();
    IContentDiffModelToPrepareDataForBulkPropagation diffModelToPrepareBulkPropagationData = new ContentDiffModelToPrepareDataForBulkPropagation();
    if (!typesToApply.isEmpty() || !taxonomiesToApply.isEmpty()) {
      IContentClassificationRuleViolotionModelForBulkPropagation ruleViolationModelForBulkPropagation = new ContentClassificationRuleViolotionModelForBulkPropagation();
      IIdAndBaseType idBaseType = new IdAndBaseType();
      idBaseType.setId(rollbackInstanceResponseModel.getContentInfo()
          .getContentId());
      idBaseType.setBaseType(rollbackInstanceResponseModel.getContentInfo()
          .getBaseType());
      ruleViolationModelForBulkPropagation.setContentIdAndBaseTypeDetails(idBaseType);
      
      IAddedDeletedTypesModel addedDeletedTypes = new AddedDeletedTypesModel();
      addedDeletedTypes.setAddedKlassIds(typesToApply);
      addedDeletedTypes.setAddedTaxonomyIds(taxonomiesToApply);
      ruleViolationModelForBulkPropagation.setAddedDeletedTypes(addedDeletedTypes);
      
      diffModelToPrepareBulkPropagationData
          .setContentClassificationRuleViolationModel(ruleViolationModelForBulkPropagation);
      //TODO: BGP
    }
  }
  
  private void evaluateIdentifierAttributes(
      IRollbackInstanceStrategyResponseModel klassInstancesRollbackModel,
      IKlassInstanceVersionTypeModel klassInstanceTypeModel,
      IGetConfigDetailsForVersionRollbackModel configDetails) throws Exception
  {
    IEvaluateIdentifierAttributesInstanceModel dataForIdentifierAttributeUniquenessEvaluation = new EvaluateIdentifierAttributesInstanceModel();
    dataForIdentifierAttributeUniquenessEvaluation
        .setKlassInstanceId(klassInstancesRollbackModel.getContentInfo()
            .getContentId());
    dataForIdentifierAttributeUniquenessEvaluation
        .setTypeIdIdentifierAttributeIds(configDetails.getTypeIdIdentifierAttributeIds());
    dataForIdentifierAttributeUniquenessEvaluation
        .setBaseType(klassInstanceTypeModel.getBaseType());
    dataForIdentifierAttributeUniquenessEvaluation
        .setChangedAttributeIds(klassInstancesRollbackModel.getDataForDataTransfer()
            .getChangedAttributeIds());
    //TODO: BGP
  }
  
  private void restoreRelationships(String klassInstanceId,
      IRestoreRelationshipInstancesRequestModel restoreRelationshipInstancesRequestModel)
      throws Exception
  {
    //TODO: BGP
  }
  
  private void evaluateGoldenRecord(String baseType, String instanceId) throws Exception
  {
    if (baseType.equals(Constants.ARTICLE_INSTANCE_BASE_TYPE)) {
      //TODO: BGP
    }
  }
  
  public IRestoreKlassInstanceStrategy getRestoreInstanceStrategy(String baseType)
  {
    switch (baseType) {
      /*
      case Constants.ARTICLE_INSTANCE_BASE_TYPE:
        return restoreArticleInstanceStrategy;
      case Constants.ASSET_INSTANCE_BASE_TYPE:
        return restoreAssetInstanceStrategy;
      case Constants.MARKET_INSTANCE_BASE_TYPE:
        return restoreMarketInstanceStrategy;
      case Constants.TEXTASSET_INSTANCE_BASE_TYPE:
        return restoreTextAssetInstanceStrategy;
      case Constants.SUPPLIER_INSTANCE_BASE_TYPE:
        return restoreSupplierInstanceStrategy;
      case Constants.VIRTUAL_CATALOG_INSTANCE_BASE_TYPE:
        return restoreVirtualCatalogInstanceStrategy;
        */
      default:
        return null;
    }
  }
  
  private void evaluateStatisticsForInstance(
      ITypeInfoWithContentIdentifiersModel contentTypeIdsInfoModel) throws Exception
  {
    List<ITypeInfoWithContentIdentifiersModel> listOfContentTypeIdsInfoModel = new ArrayList<>();
    listOfContentTypeIdsInfoModel.add(contentTypeIdsInfoModel);
    //TODO: BGP
  }
  
  private void initiateRelationshipDataTransfer(
      IRollbackInstanceStrategyResponseModel rollbackInstanceResponseModel) throws Exception
  {
    IDataTransferInputModel dataTransfer = rollbackInstanceResponseModel.getDataForDataTransfer();
    if (!dataTransfer.getChangedAttributeIds()
        .isEmpty()
        || !dataTransfer.getChangedTagsIds()
            .isEmpty()
        || !dataTransfer.getChangedDependentAttributeIdsMap()
            .isEmpty()) {
      IRelationshipDataTransferInputModel relationshipDataTransferModel = new RelationshipDataTransferInputModel();
      relationshipDataTransferModel.setDataTransfer(dataTransfer);
      //TODO: BGP
    }
  }
  
  private IKlassInstanceVersionTypeModel getInstanceVersionType(
      InstanceTypeGetRequestModel typeGetRequestModel, String baseType) throws Exception
  {
    return null;
  }
  
  private IGetConfigDetailsForVersionRollbackModel getConfigDetails(
      IMulticlassificationRequestModel requestModel, String baseType) throws Exception
  {
    return getConfigDetailForVersionRollbackStrategy.execute(requestModel);
  }
  
  private IMulticlassificationRequestModel fillMulticlassificationRequestModel(
      ITransactionData transactionData, IKlassInstanceVersionTypeModel typesModel)
  {
    IMulticlassificationRequestModel requestModel = new MulticlassificationRequestModel();
    requestModel.setSelectedTaxonomyIds(typesModel.getSelectedTaxonomyIds());
    requestModel.setKlassIds(new ArrayList<>(typesModel.getTypes()));
    requestModel.setEndpointId(transactionData.getEndpointId());
    requestModel.setOrganizationId(transactionData.getOrganizationId());
    requestModel.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    requestModel.setLanguageCodes(typesModel.getLanguageCodes());
    requestModel.setUserId(transactionData.getUserId());
    
    return requestModel;
  }
  
  private void restoreReferences(String klassInstanceId,
      ITypeInfoWithContentIdentifiersModel contentInfoModel) throws Exception
  {
    //TODO: BGP
  }
}
