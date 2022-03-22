package com.cs.core.runtime.interactor.usecase.taskexecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.utils.klassinstance.PropertyInstanceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.klass.IAttributeIdValueModel;
import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.config.interactor.model.klass.IContentsPropertyDiffModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.configdetails.ConfigDetailsForBulkPropagationRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForBulkPropagationRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForBulkPropagationResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.relationship.GetReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForBulkPropagationStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.strategy.usecase.klassinstance.IGetKlassInstanceTypeStrategy;
import com.cs.core.runtime.strategy.usecase.klassinstance.IPropagateChangesToInstancesStrategy;
import com.cs.core.runtime.strategy.utils.BulkPropagationUtil;

@Component
public class PropagateValuesHandlerTask
    extends AbstractRuntimeInteractor<IContentsPropertyDiffModel, IIdParameterModel>
    implements IPropagateValuesHandlerTask {
  
  /*@Autowired
  protected IPropagateChangesToInstancesStrategy        propagateChangesToArticleInstancesStrategy;
  
  @Autowired
  protected IPropagateChangesToInstancesStrategy        propagateChangesToMarketInstancesStrategy;
  
  @Autowired
  protected IPropagateChangesToInstancesStrategy        propagateChangesToAssetInstancesStrategy;
  
  @Autowired
  protected IPropagateChangesToInstancesStrategy        propagateChangesToSupplierInstancesStrategy;
  
  @Autowired
  protected IPropagateChangesToInstancesStrategy        propagateChangesToTextAssetInstancesStrategy;
  
  @Autowired
  protected IPropagateChangesToInstancesStrategy        propagateChangesToVirtualCatalogInstancesStrategy;*/
  
  @Autowired
  protected IGetConfigDetailsForBulkPropagationStrategy getConfigDetailsForBulkPropagationStrategy;
  
  @Autowired
  protected KlassInstanceUtils                          klassInstanceUtils;
  
  /*  @Value("${mdm.propagation.handlerlog.path}")
  public String                                         bulkPropagationLogPath;*/
  
  @Autowired
  protected Boolean                                     isKafkaLoggingEnabled;
  
  /*@Autowired
  protected IBulkPropagationStrategy                    bulkPropagationStrategy; */
  
  @Autowired
  protected BulkPropagationUtil                         bulkPropagationUtil;
  
  @Override
  protected IIdParameterModel executeInternal(IContentsPropertyDiffModel contentPropertyDiffModel)
      throws Exception
  {
    try {
      IdParameterModel idModel = new IdParameterModel(contentPropertyDiffModel.getContentId());
      String baseType = contentPropertyDiffModel.getBaseType();
      
      IGetKlassInstanceTypeStrategy typesStrategy = klassInstanceUtils
          .getKlassInstanceTypeStrategy(baseType);
      IKlassInstanceTypeModel typesModel = typesStrategy.execute(idModel);
      List<String> klassIds = new ArrayList<>(typesModel.getTypes());
      klassIds.removeAll(contentPropertyDiffModel.getDeletedKlassIds());
      List<String> taxonomyIds = typesModel.getSelectedTaxonomyIds();
      taxonomyIds.removeAll(contentPropertyDiffModel.getDeletedTaxonomyIds());
      
      IConfigDetailsForBulkPropagationRequestModel configDetailsRequestModel = new ConfigDetailsForBulkPropagationRequestModel();
      configDetailsRequestModel.setKlassIds(klassIds);
      configDetailsRequestModel.setSelectedTaxonomyIds(taxonomyIds);
      configDetailsRequestModel.setShouldUseTagIdTagValueIdsMap(false);
      configDetailsRequestModel.setOrganizationId(typesModel.getOrganizationId());
      configDetailsRequestModel.setEndpointId(typesModel.getEndpointId());
      configDetailsRequestModel.setPhysicalCatalogId(typesModel.getPhysicalCatalogId());
      configDetailsRequestModel.setParentKlassIds(typesModel.getParentKlassIds());
      configDetailsRequestModel.setParentTaxonomyIds(typesModel.getParentTaxonomyIds());
      /*  IAddedDeletedTypesModel addedDeletedTypes  = contentPropertyDiffModel.getAddedDeletedTypes();
      if(addedDeletedTypes != null) {
        configDetailsRequestModel.getKlassIds().addAll(addedDeletedTypes.getAddedKlassIds());
        configDetailsRequestModel.getSelectedTaxonomyIds().addAll(addedDeletedTypes.getAddedTaxonomyIds());
        configDetailsRequestModel.setAddedTaxonomyIds(addedDeletedTypes.getAddedTaxonomyIds());
        configDetailsRequestModel.setAddedKlassIds(addedDeletedTypes.getAddedKlassIds());
      }*/
      
      IConfigDetailsForBulkPropagationResponseModel configDetails = getConfigDetailsForBulkPropagationStrategy
          .execute(configDetailsRequestModel);
      fillConfigDetailsForBulkPropagation(configDetails, contentPropertyDiffModel);
      List<IDefaultValueChangeModel> defaultValuesDiff = contentPropertyDiffModel
          .getDefaultValuesDiff();
      List<IDefaultValueChangeModel> diffListForIsSkipped = new ArrayList<>();
      List<IDefaultValueChangeModel> dependentDiffListForIsSkipped = new ArrayList<>();
      if (!defaultValuesDiff.isEmpty()) {
        PropertyInstanceUtils.fillContentDiffFromDefaultValueDiffAndGetDiffListForIsSkipped(
            contentPropertyDiffModel, defaultValuesDiff, klassIds, taxonomyIds,
            typesModel.getLanguageCodes(), diffListForIsSkipped, dependentDiffListForIsSkipped);
      }
      contentPropertyDiffModel.setDefaultValuesDiff(diffListForIsSkipped);
      contentPropertyDiffModel.setDependentDefaultValuesDiff(dependentDiffListForIsSkipped);
      
      List<IAttributeIdValueModel> attributeInstancesValueToApply = contentPropertyDiffModel
          .getAttributeInstancesValueToApply();
      fillAttributeValuesToApply(contentPropertyDiffModel, configDetails,
          attributeInstancesValueToApply);
    }
    catch (Throwable ex) {
      RDBMSLogger.instance().exception(ex);
      if (isKafkaLoggingEnabled) {
        //TODO: BGP
      }
    }
    return new IdParameterModel();
  }
  
  private void fillAttributeValuesToApply(IContentsPropertyDiffModel contentPropertyDiffModel,
      IConfigDetailsForBulkPropagationResponseModel configDetails,
      List<IAttributeIdValueModel> attributeInstancesValueToApply)
  {
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    for (IAttributeIdValueModel attributeIdValueModel : attributeInstancesValueToApply) {
      IAttribute referencedAttribute = referencedAttributes
          .get(attributeIdValueModel.getAttributeId());
      if (referencedAttribute == null) {
        continue;
      }
      if (referencedAttribute.getIsTranslatable()) {
        contentPropertyDiffModel.getDependentAttributeInstancesValueToApply()
            .add(attributeIdValueModel);
      }
      else {
        contentPropertyDiffModel.getIndependentAttributeInstancesValueToApply()
            .add(attributeIdValueModel);
      }
    }
  }
  
  private void evaluateGoldenRecordRuleBucket(String baseType, IKlassInstanceTypeModel typesModel,
      String klassInstanceId) throws Exception
  {
    if (baseType.equals(Constants.ARTICLE_INSTANCE_BASE_TYPE) && typesModel.getParentKlassIds()
        .isEmpty()
        && typesModel.getParentTaxonomyIds()
            .isEmpty()) {
      IIdParameterModel idParameterModel = new IdParameterModel(klassInstanceId);
      //TODO: BGP
    }
  }
  
  public IPropagateChangesToInstancesStrategy getPropagationStrategy(String baseType)
  {
    switch (baseType) {
      /*case Constants.ARTICLE_INSTANCE_BASE_TYPE:
        return propagateChangesToArticleInstancesStrategy;
      case Constants.ASSET_INSTANCE_BASE_TYPE:
        return propagateChangesToAssetInstancesStrategy;
      case Constants.MARKET_INSTANCE_BASE_TYPE:
        return propagateChangesToMarketInstancesStrategy;
      case Constants.TEXTASSET_INSTANCE_BASE_TYPE:
        return propagateChangesToTextAssetInstancesStrategy;
      case Constants.SUPPLIER_INSTANCE_BASE_TYPE:
        return propagateChangesToSupplierInstancesStrategy;
      case Constants.VIRTUAL_CATALOG_INSTANCE_BASE_TYPE:
        return propagateChangesToVirtualCatalogInstancesStrategy; */
      default:
        return null;
    }
  }
  
  public void fillConfigDetailsForBulkPropagation(
      IConfigDetailsForBulkPropagationResponseModel configDetailsForBulkPropagationModel,
      IContentsPropertyDiffModel contentPropertyDiffModel)
  {
    Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipPropertiesMap = configDetailsForBulkPropagationModel
        .getReferencedRelationshipProperties();
    IGetReferencedRelationshipPropertiesModel getReferencedRelationshipPropertiesModel = new GetReferencedRelationshipPropertiesModel();
    getReferencedRelationshipPropertiesModel
        .setReferencedRelationshipProperties(referencedRelationshipPropertiesMap);
    contentPropertyDiffModel
        .setReferencedRelationshipProperties(getReferencedRelationshipPropertiesModel);
    Map<String, IAttribute> referencedAttributesMap = configDetailsForBulkPropagationModel
        .getReferencedAttributes();
    contentPropertyDiffModel.setReferencedAttributes(referencedAttributesMap);
    Map<String, IDataRuleModel> referencedDataRulesMap = configDetailsForBulkPropagationModel
        .getReferencedDataRules();
    List<IDataRuleModel> listOfDataRuleModel = new ArrayList<>();
    referencedDataRulesMap.forEach((dataRuleId, dataRuleModel) -> {
      listOfDataRuleModel.add(dataRuleModel);
    });
    contentPropertyDiffModel.setReferencedDataRules(listOfDataRuleModel);
    Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships = configDetailsForBulkPropagationModel
        .getReferencedNatureRelationships();
    contentPropertyDiffModel.setReferencedNatureRelationships(referencedNatureRelationships);
    Map<String, IReferencedSectionElementModel> referencedElementsMap = configDetailsForBulkPropagationModel
        .getReferencedElements();
    contentPropertyDiffModel.setReferencedElements(referencedElementsMap);
    /* List<IDefaultValueChangeModel> defaultValuesDiff = configDetailsForBulkPropagationModel.getDefaultValuesDiff();
    contentPropertyDiffModel.getDefaultValuesDiff().addAll(defaultValuesDiff);
    IAddedDeletedTypesModel addedDeletedTypes = contentPropertyDiffModel.getAddedDeletedTypes();
    if(addedDeletedTypes != null) {
      Map<String, Object> referencedTaxonomies = addedDeletedTypes.getReferencedTaxonomies();
      referencedTaxonomies.putAll(configDetailsForBulkPropagationModel.getReferencedTaxonomies());
    }*/
    contentPropertyDiffModel.setTypeIdIdentifierAttributeIds(
        configDetailsForBulkPropagationModel.getTypeIdIdentifierAttributeIds());
    // contentPropertyDiffModel.setReferencedParentChildContextProperties(configDetailsForBulkPropagationModel.getReferencedParentChildContextProperties());
    contentPropertyDiffModel.setNumberOfVersionsToMaintain(
        configDetailsForBulkPropagationModel.getNumberOfVersionsToMaintain());
    contentPropertyDiffModel
        .setReferencedTags(configDetailsForBulkPropagationModel.getReferencedTags());
  }
  
  private void evaluateStatisticsForInstance(IContentTypeIdsInfoModel contentTypeIdsInfoModel)
      throws Exception
  {
    List<IContentTypeIdsInfoModel> listOfContentTypeIdsInfoModel = new ArrayList<>();
    listOfContentTypeIdsInfoModel.add(contentTypeIdsInfoModel);
    //TODO: BGP
  }
}
