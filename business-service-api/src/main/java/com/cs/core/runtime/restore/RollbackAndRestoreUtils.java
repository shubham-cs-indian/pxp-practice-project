package com.cs.core.runtime.restore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.bds.config.usecase.taxonomy.ITypeSwitchInstance;
import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.BaseEntityPlanDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBaseEntityPlanDTO;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.propertycollection.ISectionRelationship;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.usecase.versionrollback.IGetConfigDetailForVersionRollbackStrategy;
import com.cs.core.elastic.Index;
import com.cs.core.elastic.das.ElasticServiceDAS;
import com.cs.core.eventqueue.idto.IEventDTO.EventType;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.coupling.dto.ContextualDataTransferDTO;
import com.cs.core.rdbms.coupling.dto.ContextualDataTransferGranularDTO;
import com.cs.core.rdbms.coupling.idto.IContextualDataTransferDTO;
import com.cs.core.rdbms.coupling.idto.IContextualDataTransferGranularDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.PropertyRecordDTO;
import com.cs.core.rdbms.entity.dto.RelationsSetDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.runtime.instancetree.GoldenRecordUtils;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRelationshipVersion;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.relationship.RelationshipVersion;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.cs.core.runtime.interactor.exception.configuration.NatureKlassNotFoundException;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.instance.ContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.ArticleInstanceSaveModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.relationship.SaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForVersionRollbackModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.TranslationInstanceUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.util.ConfigUtil;
import com.cs.dam.runtime.assetinstance.ISaveAssetInstanceRelationshipsService;
import com.cs.pim.runtime.articleinstance.ISaveArticleInstanceRelationshipsService;
import com.cs.pim.runtime.targetinstance.market.ISaveMarketInstanceRelationshipsService;
import com.cs.utils.BaseEntityUtils;

@SuppressWarnings("unchecked")
@Component
public class RollbackAndRestoreUtils {
  
  @Autowired
  protected ISessionContext                                context;
  
  @Autowired
  protected ConfigUtil                                     configUtil;
  
  @Autowired
  protected TransactionThreadData                          controllerThread;
  
  @Autowired
  protected IGetConfigDetailForVersionRollbackStrategy     getConfigDetailsForVersionRollbackStrategy;
  
  @Autowired
  protected ITypeSwitchInstance                            typeSwitchInstance;
  
  @Autowired
  protected TranslationInstanceUtils                       translationInstanceUtils;
  
  @Autowired
  protected RDBMSComponentUtils                            rdbmsComponentUtils;
  
  @Autowired
  protected ISaveArticleInstanceRelationshipsService        saveArticleInstanceRelationshipsService;
  
  @Autowired
  protected ISaveAssetInstanceRelationshipsService          saveAssetInstanceRelationshipsService;
  
  @Autowired
  protected ISaveMarketInstanceRelationshipsService         saveMarketInstanceRelationshipsService;
  
  @Autowired
  protected GoldenRecordUtils                               goldenRecordUtils;
  
  private static final String                               SERVICE_FOR_CDT                = "CONTEXTUAL_DATA_TRANSFER_TASK";
  private static final String                               SERVICE_FOR_RESTORE            = "RESTORE_INSTANCE_TASK";

  public IGetConfigDetailsForVersionRollbackModel getConfigDetailsForVersionRollback(IMulticlassificationRequestModel fillMultiClassificationRequestModel) throws Exception
  {
    return getConfigDetailsForVersionRollbackStrategy.execute(fillMultiClassificationRequestModel);
  }
  
  public IMulticlassificationRequestModel fillMultiClassificationRequestModel(IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    configUtil.setBaseEntityDetails(baseEntityDAO, multiclassificationRequestModel);

    ITransactionData transactionData = controllerThread.getTransactionData();
    multiclassificationRequestModel.setEndpointId(transactionData.getEndpointId());
    multiclassificationRequestModel.setOrganizationId(transactionData.getOrganizationId());
    multiclassificationRequestModel.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    multiclassificationRequestModel.setUserId(transactionData.getUserId());
    
    return multiclassificationRequestModel;
  }
  
  public List<IClassifierDTO> getClassifiersFromDTO(IBaseEntityDTO rollbackBaseEntityDTO)
  {
    List<IClassifierDTO> classifierDTO = new ArrayList<>();
    classifierDTO.add(rollbackBaseEntityDTO.getNatureClassifier());
    classifierDTO.addAll(rollbackBaseEntityDTO.getOtherClassifiers());
    
    return classifierDTO;
  }
  
  private void fillTypesAndTaxonomyIds(List<IClassifierDTO> addedClassifiers,
      List<String> addedSecondaryTypes, List<String> addedTaxonomyIds)
  {
    addedClassifiers.forEach(classifier -> {
      ClassifierType classifierType = classifier.getClassifierType();
      if (classifierType.equals(ClassifierType.CLASS)) {
        addedSecondaryTypes.add(classifier.getCode());
      }
      else {
        addedTaxonomyIds.add(classifier.getCode());
      }
    });
  }
  
  public IKlassInstanceTypeSwitchModel fillClassifiersAndCallTypeSwitchRequest(IBaseEntityDTO baseEntityDTO,
      List<IClassifierDTO> addedClassifiers, List<IClassifierDTO> removedClassifiers)
      throws Exception
  {
    IKlassInstanceTypeSwitchModel typeSwitchModel = new KlassInstanceTypeSwitchModel();
    if (!addedClassifiers.isEmpty() || !removedClassifiers.isEmpty()) {
      List<String> addedSecondaryTypes = new ArrayList<String>();
      List<String> deletedSecondaryTypes = new ArrayList<String>();
      List<String> addedTaxonomyIds = new ArrayList<String>();
      List<String> deletedTaxonomyIds = new ArrayList<String>();
      
      fillTypesAndTaxonomyIds(addedClassifiers, addedSecondaryTypes, addedTaxonomyIds);
      
      fillTypesAndTaxonomyIds(removedClassifiers, deletedSecondaryTypes, deletedTaxonomyIds);
      
      typeSwitchModel.setKlassInstanceId(Long.toString(baseEntityDTO.getBaseEntityIID()));
      typeSwitchModel.setAddedSecondaryTypes(addedSecondaryTypes);
      typeSwitchModel.setAddedTaxonomyIds(addedTaxonomyIds);
      typeSwitchModel.setDeletedSecondaryTypes(deletedSecondaryTypes);
      typeSwitchModel.setDeletedTaxonomyIds(deletedTaxonomyIds);
      typeSwitchInstance.execute(typeSwitchModel);
    }
    return typeSwitchModel;
  }
  
  public List<IClassifierDTO> validateClassifiers(IBaseEntityDTO baseEntityDTO,
      IGetConfigDetailsForVersionRollbackModel configDetails)
  {
    List<IClassifierDTO> eligibleClassifiers = new ArrayList<>();
    List<IClassifierDTO> classifiers = getClassifiersFromDTO(baseEntityDTO);
    Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses = configDetails.getReferencedKlasses();
    Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies = configDetails.getReferencedTaxonomies();
    for (IClassifierDTO classifier : classifiers) {
      String classifierCode = classifier.getClassifierCode();
      if (referencedKlasses.containsKey(classifierCode) || referencedTaxonomies.containsKey(classifierCode)) {
        eligibleClassifiers.add(classifier);
      }
    }
    return eligibleClassifiers;
  }
  
  public void handleAddedLocaleIds(IBaseEntityDTO currentBaseEntityDTO, long baseEntityIID,
      List<String> addedLocaleIds)
  {
    IKlassInstanceSaveModel saveTranslationModel = new ArticleInstanceSaveModel();
    saveTranslationModel.setName(currentBaseEntityDTO.getBaseEntityName());
    saveTranslationModel.setId(String.valueOf(baseEntityIID));
    addedLocaleIds.forEach(addedLocaleId -> {
      try {
        saveTranslationModel.setLanguageCodes(currentBaseEntityDTO.getLocaleIds());
        translationInstanceUtils.handleTranslationCreation(saveTranslationModel, null);
      }
      catch (Exception e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
    });
  }
  
  public void evaluateRules(IBaseEntityDAO currentBaseEntityDAO,
      IGetConfigDetailsForVersionRollbackModel configDetails) throws RDBMSException, Exception
  {
    RuleHandler ruleHandler = new RuleHandler();
    ruleHandler.initiateRuleHandling(currentBaseEntityDAO, rdbmsComponentUtils.getLocaleCatlogDAO(), true,
        configDetails.getReferencedElements(), configDetails.getReferencedAttributes(), configDetails.getReferencedTags());
  }
  
  public void executeSaveRelation(ISaveRelationshipInstanceModel dataModel, BaseType baseType) throws Exception
  {
    switch(baseType){
      case ARTICLE :
        saveArticleInstanceRelationshipsService.executeSaveRelationship(dataModel);
        break;
      case ASSET : 
        saveAssetInstanceRelationshipsService.executeSaveRelationship(dataModel);
        break;
      case TARGET:
        saveMarketInstanceRelationshipsService.executeSaveRelationship(dataModel);
        break;
      default:
        break;
    }
  }
  
  public IContentRelationshipInstanceModel getRelationshipInstanceModel(Set<String> natureRelationIds,
      Map<String, Object> relationIdVsSideIdMap, Map<String, IReferencedRelationshipModel> referencedRelationships,
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships, Map<String, IReferencedSectionElementModel> referencedElements, String relationId,
      Set<IEntityRelationDTO> relations) throws RDBMSException, Exception
  {
    IContentRelationshipInstanceModel relationship = new ContentRelationshipInstanceModel();
    relationship.setRelationshipId(relationId);
    relationship.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RELATIONSHIP.getPrefix()));

    if (natureRelationIds.contains(relationId)) {
      if (relationIdVsSideIdMap.get(relationId).equals(RelationSide.SIDE_1))
        relationship.setSideId(referencedNatureRelationships.get(relationId).getSide1().getElementId());
      else
        relationship.setSideId(referencedNatureRelationships.get(relationId).getSide2().getElementId());
    }
    else {
      if (relationIdVsSideIdMap.get(relationId).equals(RelationSide.SIDE_1))
        relationship.setSideId(referencedRelationships.get(relationId).getSide1().getElementId());
      else
        relationship.setSideId(referencedRelationships.get(relationId).getSide2().getElementId());
    }

    ISectionRelationship referencedElement = (ISectionRelationship) referencedElements.get(relationship.getSideId());
    relationship.setBaseType(BaseEntityUtils.getBaseTypeByKlassType(referencedElement.getRelationshipSide().getTargetType()));

    return relationship;
  }
  
  public void fillRelationsAndSideIdMap(List<IPropertyRecordDTO> relationSetDTOs, Map<String, Object> relationIdVsRelationsMap,
      Set<String> natureRelationIds, Map<String, Object> relationIdVsSideIdMap)
  {
    for (IPropertyRecordDTO property : relationSetDTOs) {
      IRelationsSetDTO relationSet = (IRelationsSetDTO) property;
      IPropertyDTO relationshipProperty = relationSet.getProperty();
      String relationshipCode = relationshipProperty.getCode();

      if (relationshipCode.equals(SystemLevelIds.STANDARD_ARTICLE_GOLDEN_ARTICLE_RELATIONSHIP_ID)) {
        continue;
      }

      if (relationshipProperty.getPropertyType().equals(PropertyType.NATURE_RELATIONSHIP))
        natureRelationIds.add(relationSet.getProperty().getCode());

      relationIdVsRelationsMap.put(relationshipCode, relationSet.getRelations());
      relationIdVsSideIdMap.put(relationshipCode, relationSet.getSide());
    }
  }
  
  public IRelationshipVersion getRelationshipElement(IEntityRelationDTO relation, Map<String, ITag> referencedTags)
  {
    IRelationshipVersion relationshipElement = new RelationshipVersion();

    relationshipElement.setId(String.valueOf(relation.getOtherSideEntityIID()));

    IContextualDataDTO sideContextualObject = relation.getContextualObject();
    if (sideContextualObject != null && sideContextualObject.getContextualObjectIID() != 0) {

      relationshipElement.setContextId(sideContextualObject.getContextCode());
      // Tag values
      List<IContentTagInstance> contentTagInstances = getContextTagInstances(sideContextualObject, referencedTags);
      relationshipElement.setTags(contentTagInstances);

      // Time range
      IInstanceTimeRange instanceTimeRange = new InstanceTimeRange();
      instanceTimeRange.setFrom(sideContextualObject.getContextStartTime());
      instanceTimeRange.setTo(sideContextualObject.getContextEndTime());
      relationshipElement.setTimeRange(instanceTimeRange);
    }
    return relationshipElement;
  }

  private List<IContentTagInstance> getContextTagInstances(IContextualDataDTO contextualDataDTO, Map<String, ITag> referencedTags)
  {
    List<IContentTagInstance> tagInstances = new ArrayList<>();
    Map<String, IContentTagInstance> tagInstanceType = new HashMap<>();
    contextualDataDTO.getContextTagValues().forEach(tagRecordDTO -> {
      try {
        if (tagRecordDTO.getRange() != 0) {
          String tagCode = tagRecordDTO.getTagValueCode();
          for (ITag referencedTag : referencedTags.values()) {
            List<ITreeEntity> treeEntities = (List<ITreeEntity>) referencedTag.getChildren();
            for (ITreeEntity treeEntity : treeEntities) {
              if (treeEntity.getId().equals(tagCode)) {
                String tagInstanceId = String.valueOf(contextualDataDTO.getContextualObjectIID()) + "-"
                    + String.valueOf(referencedTag.getCode());
                ITagInstance tagInstance = (ITagInstance) tagInstanceType.get(tagInstanceId);

                if (tagInstance == null) {
                  tagInstance = new TagInstance();
                  tagInstance.setId(tagInstanceId);
                  tagInstance.setContextInstanceId(Long.toString(contextualDataDTO.getContextualObjectIID()));
                  tagInstance.setBaseType(tagInstance.getClass().getName());
                  tagInstance.setTagId(referencedTag.getCode());
                  tagInstanceType.put(tagInstanceId, tagInstance);
                  tagInstances.add(tagInstance);
                }

                ITagInstanceValue tagInstanceValue = new TagInstanceValue();
                String tagValueInstanceId = String.valueOf(contextualDataDTO.getContextualObjectIID()) + "-" + String.valueOf(tagCode);
                tagInstanceValue.setId(tagValueInstanceId);
                tagInstanceValue.setTagId(tagCode);
                tagInstanceValue.setCode(tagCode);
                tagInstanceValue.setRelevance(tagRecordDTO.getRange());
                tagInstance.getTagValues().add(tagInstanceValue);
              }
            }
          }
        }
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    return tagInstances;
  }
  
  public void initiateContextualDataTransfer(IBaseEntityDTO baseEntityDTO) throws Exception {
    long parentIID = baseEntityDTO.getParentIID();
    if(parentIID == 0l) {
      return;
    }
    IContextualDataTransferDTO dataTransferDTO = new ContextualDataTransferDTO();
    IContextualDataTransferGranularDTO bgpCouplingDTO = new ContextualDataTransferGranularDTO();
    bgpCouplingDTO.setParentBaseEntityIID(parentIID);
    bgpCouplingDTO.setVariantBaseEntityIID(baseEntityDTO.getBaseEntityIID());
    bgpCouplingDTO.setContextID(String.valueOf(baseEntityDTO.getNatureClassifier().getClassifierIID()));
    dataTransferDTO.setBGPCouplingDTOs(Arrays.asList(bgpCouplingDTO));
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.HIGH;
    dataTransferDTO.setLocaleID(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID());
    dataTransferDTO.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
    dataTransferDTO.setOrganizationCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
    dataTransferDTO.setUserId(controllerThread.getTransactionData().getUserId());
    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE_FOR_CDT, "", userPriority,
        new JSONContent(dataTransferDTO.toJSON()));
  }
  
  public void fillPropertyRecordsInfo(Set<IPropertyRecordDTO> currentPropertyRecords,
      Map<String, IReferencedSectionElementModel> referencedElements,
      List<String> localeIdsToEvaluate, Map<String, Object> currentPropertyRecordsMap,
      Set<String> propertyCodes)
  {
    for (IPropertyRecordDTO propertyRecord : currentPropertyRecords) {
      IPropertyDTO property = propertyRecord.getProperty();
      if(propertyRecord.isCoupled()) {
        ((PropertyRecordDTO)propertyRecord).clearCoupling();
      }
      String propertyCode = property.getPropertyCode();
      SuperType superType = property.getSuperType();
      if (referencedElements.containsKey(propertyCode) && !superType.equals(SuperType.RELATION_SIDE)) {
        if (propertyRecord instanceof IValueRecordDTO) {
          evaluateAttributeValue((IValueRecordDTO) propertyRecord, (IReferencedSectionAttributeModel) referencedElements.get(propertyCode),
              propertyCode, currentPropertyRecordsMap, propertyCodes, localeIdsToEvaluate);
        }
        else {
          currentPropertyRecordsMap.put(propertyCode, propertyRecord);
          propertyCodes.add(propertyCode);
        }
      }
      // Asset Specific attributes which are not part of property collection.
      else if (propertyCode.equals(SystemLevelIds.START_DATE_ATTRIBUTE)
          || propertyCode.equals(SystemLevelIds.END_DATE_ATTRIBUTE)
          || propertyCode.equals(SystemLevelIds.ASSET_DOWNLOAD_COUNT_ATTRIBUTE)) {
        currentPropertyRecordsMap.put(propertyCode, propertyRecord);
        propertyCodes.add(propertyCode);
      }
    }
  }
  
  private void evaluateAttributeValue(IValueRecordDTO propertyRecord, IReferencedSectionAttributeModel referencedElement,
      String propertyCode, Map<String, Object> propertyRecords, Set<String> propertyCodes, List<String> localeIdsToEvaluate)
  {
    if (propertyRecord.getLocaleID().isEmpty()) {
      if (referencedElement.getAttributeVariantContext() != null) {
        evaluateContext(propertyRecord, referencedElement, propertyCode, propertyRecords, propertyCodes, false);
      }
      else {
        propertyRecords.put(propertyCode, propertyRecord);
        propertyCodes.add(propertyCode);
      }
    }
    else if (localeIdsToEvaluate.contains(propertyRecord.getLocaleID())) {
      if (referencedElement.getAttributeVariantContext() != null) {
        evaluateContext(propertyRecord, referencedElement, propertyCode, propertyRecords, propertyCodes, true);
      }
      else {
        Map<String, IPropertyRecordDTO> propertyRecordMap = propertyRecords.containsKey(propertyCode)
            ? (HashMap<String, IPropertyRecordDTO>) propertyRecords.get(propertyCode)
            : new HashMap<>();
        propertyRecordMap.put(propertyRecord.getLocaleID(), propertyRecord);
        propertyRecords.put(propertyCode, propertyRecordMap);
        propertyCodes.add(propertyCode);
      }
    }
  }
  
  private void evaluateContext(IValueRecordDTO propertyRecord, IReferencedSectionAttributeModel referencedElement, String propertyCode,
      Map<String, Object> propertyRecords, Set<String> propertyCodes, Boolean isLanguageDependent)
  {
    String propertyRecordAttributeContext = propertyRecord.getContextualObject().getContext().getCode();
    long contextualObjectIid = propertyRecord.getContextualObject().getContextualObjectIID();

    StringBuilder propertyIndexBuilder = new StringBuilder(propertyCode);
    propertyIndexBuilder.append(contextualObjectIid);
    String propertyIndex = propertyIndexBuilder.toString();

    String referencedAttributeContext = referencedElement.getAttributeVariantContext() == null ? ""
        : referencedElement.getAttributeVariantContext();

    if (propertyRecordAttributeContext.equals(referencedAttributeContext)) {
      if (isLanguageDependent) {
        Map<String, IPropertyRecordDTO> propertyRecordMap = propertyRecords.containsKey(propertyIndex)
            ? (HashMap<String, IPropertyRecordDTO>) propertyRecords.get(propertyIndex)
            : new HashMap<>();
        propertyRecordMap.put(propertyRecord.getLocaleID(), propertyRecord);
        propertyRecords.put(propertyIndex, propertyRecordMap);
        propertyCodes.add(propertyIndex);

      }
      else {
        propertyRecords.put(propertyIndex, propertyRecord);
        propertyCodes.add(propertyIndex);
      }
    }
  }
  
  public void fillPropertyRecords(IPropertyRecordDTO currentPropertyRecord, IPropertyRecordDTO rollbackPropertyRecord,
      List<IPropertyRecordDTO> propertyRecordsToBeDeleted, List<IPropertyRecordDTO> propertyRecordsToBeCreated,
      List<IPropertyRecordDTO> propertyRecordsToBeUpdated)
  {
    if (currentPropertyRecord != null && rollbackPropertyRecord != null) {
      boolean isChanged = true;
      if(rollbackPropertyRecord instanceof ValueRecordDTO) {
        String value = ((ValueRecordDTO)rollbackPropertyRecord).getValue();
        isChanged = !value.equals(((ValueRecordDTO)currentPropertyRecord).getValue());
      }
      rollbackPropertyRecord.setChanged(isChanged);
      propertyRecordsToBeUpdated.add(rollbackPropertyRecord);
    }
    else if (currentPropertyRecord != null) {
      propertyRecordsToBeDeleted.add(currentPropertyRecord);
    }
    else if (rollbackPropertyRecord != null) {
      propertyRecordsToBeCreated.add(rollbackPropertyRecord);
    }
  }
  
  public void fillpropertyRecordsFromMap(Map<String, Object> currentPropertyRecordsMap, Map<String, Object> rollbackPropertyRecordsMap,
      List<IPropertyRecordDTO> propertyRecordsToBeDeleted, List<IPropertyRecordDTO> propertyRecordsToBeCreated,
      List<IPropertyRecordDTO> propertyRecordsToBeUpdated, String propertyCode, List<String> localeIdsToEvaluate)
  {
    Map<String, IPropertyRecordDTO> currentPropertyRecord = currentPropertyRecordsMap.containsKey(propertyCode)
        ? (Map<String, IPropertyRecordDTO>) currentPropertyRecordsMap.get(propertyCode)
        : null;
    Map<String, IPropertyRecordDTO> rollbackPropertyRecord = rollbackPropertyRecordsMap.containsKey(propertyCode)
        ? (Map<String, IPropertyRecordDTO>) rollbackPropertyRecordsMap.get(propertyCode)
        : null;
    if (rollbackPropertyRecord != null && currentPropertyRecord != null) {
      for (String localeId : localeIdsToEvaluate) {
        fillPropertyRecords(currentPropertyRecord.get(localeId), rollbackPropertyRecord.get(localeId), propertyRecordsToBeDeleted,
            propertyRecordsToBeCreated, propertyRecordsToBeUpdated);
      }
    }
    else if (rollbackPropertyRecord != null) {
      for (String localeId : localeIdsToEvaluate) {
        if (rollbackPropertyRecord.containsKey(localeId)) {
          propertyRecordsToBeCreated.add(rollbackPropertyRecord.get(localeId));
        }
      }
    }
    else if (currentPropertyRecord != null) {
      for (String localeId : localeIdsToEvaluate) {
        if (currentPropertyRecord.containsKey(localeId)) {
          propertyRecordsToBeDeleted.add(currentPropertyRecord.get(localeId));
        }
      }
    }
  }

  public IBaseEntityDAO handleBaseEntityRestore(List<String> baseEnitityIIDs) throws RDBMSException, Exception, NatureKlassNotFoundException
  {
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    List<IBaseEntityDTO> baseEntitiesFromArchive = localeCatlogDAO.getBaseEntitiesFromArchive(baseEnitityIIDs);
    IBaseEntityDTO baseEntityDTO = !baseEntitiesFromArchive.isEmpty() ? baseEntitiesFromArchive.get(0): new BaseEntityDTO() ;
    IBaseEntityDAO baseEntityDAO = localeCatlogDAO.openBaseEntity(baseEntityDTO);
    IMulticlassificationRequestModel configDetailRequestModel = fillMultiClassificationRequestModel(baseEntityDAO);
    IGetConfigDetailsForVersionRollbackModel configDetails = getConfigDetailsForVersionRollback(configDetailRequestModel);
    
    validateBaseEntityDAO(configDetails, baseEntityDAO);
    
    baseEntityDAO.createBaseEntityWithPreDefiniedIID();
    restoreTypesAndTaxonomies(baseEntityDTO, configDetails);
    restoreLocaleIds(baseEntityDTO, configDetails);
    restoreRelationships(baseEntityDTO, configDetails);
    restoreProperties(baseEntityDAO, configDetails);
    restoreEmbeddedVariants(baseEntityDTO, configDetails);
    
    evaluateRules(baseEntityDAO, configDetails);
    rdbmsComponentUtils.createNewRevision(baseEntityDTO, configDetails.getNumberOfVersionsToMaintain());
    
    initiateContextualDataTransfer(baseEntityDTO);
    
    goldenRecordUtils.initiateEvaluateGoldenRecordBucket(baseEntityDTO);
    
    ElasticServiceDAS.instance().deleteDocument(((Long)baseEntityDTO.getBaseEntityIID()).toString(), Index.getArchiveIndexByBaseType(baseEntityDTO.getBaseType()).name());
    localeCatlogDAO.postUsecaseUpdate(baseEntityDTO.getBaseEntityIID(), EventType.ELASTIC_UPDATE);
 
    localeCatlogDAO.deleteEntityFromArchive(baseEntityDTO.getBaseEntityIID());
    
    return baseEntityDAO;

  }

  private void restoreProperties(IBaseEntityDAO baseEntityDAO,
      IGetConfigDetailsForVersionRollbackModel configDetails) throws RDBMSException
  {
    List<IPropertyDTO> properties = BaseEntityUtils.getReferenceAttributesTagsProperties(configDetails.getReferencedAttributes(), configDetails.getReferencedTags(), baseEntityDAO);
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();

    Map<String, Object> propertyRecordsMap = new HashMap<>();
    Set<String> propertyCodes = new HashSet<>();
    List<IPropertyRecordDTO> propertyRecordsToBeCreated = new ArrayList<>();

    List<String> localeIds = baseEntityDTO.getLocaleIds();
    Set<IPropertyRecordDTO> propertyRecords = baseEntityDTO.getPropertyRecords();
    fillPropertyRecordsInfo(propertyRecords, configDetails.getReferencedElements(), localeIds,
        propertyRecordsMap, propertyCodes);
    
    for (String propertyCode : propertyCodes) {
      if (propertyRecordsMap.get(propertyCode) instanceof IPropertyRecordDTO) {
        propertyRecordsToBeCreated.add((IPropertyRecordDTO)propertyRecordsMap.get(propertyCode));
      }
      else {
        Map<String, IPropertyRecordDTO> languageVsPropertyRecord = (Map<String, IPropertyRecordDTO>) propertyRecordsMap.get(propertyCode);
        for (String localeId : localeIds) {
          propertyRecordsToBeCreated.add((IPropertyRecordDTO)languageVsPropertyRecord.get(localeId));
        }
      }
    }
    baseEntityDAO.createPropertyRecords(propertyRecordsToBeCreated.toArray(new IPropertyRecordDTO[0]));
  }

  private void restoreEmbeddedVariants(IBaseEntityDTO baseEntityDTO,
      IGetConfigDetailsForVersionRollbackModel configDetails) throws Exception
  {
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.LOW;
    baseEntityDTO.getChildren().forEach(child-> {
      
      //TODO: Fetch child PXON from archive and validate context
      try {
        long childrenIID = child.getChildrenIID(); 
        List<String> baseEnitityIIDs = new ArrayList<>();
        baseEnitityIIDs.add(Long.toString(childrenIID));
        List<IBaseEntityDTO> baseEntitiesFromArchive = rdbmsComponentUtils.getLocaleCatlogDAO().getBaseEntitiesFromArchive(baseEnitityIIDs);
        IBaseEntityDTO variantBaseEntityDTO = !baseEntitiesFromArchive.isEmpty() ? baseEntitiesFromArchive.get(0): new BaseEntityDTO() ;
        IContextualDataDTO contextualObject = variantBaseEntityDTO.getContextualObject();
        IReferencedContextModel referencedVariantContexts = configDetails.getReferencedVariantContexts();
        IReferencedVariantContextModel iReferencedVariantContextModel = referencedVariantContexts.getEmbeddedVariantContexts().get(contextualObject.getContextCode());
        if (contextualObject == null || contextualObject.getContextCode() == null || contextualObject.getContextCode().isBlank()
            && iReferencedVariantContextModel == null)
        {
          return;
        }

        long parentIID = variantBaseEntityDTO.getParentIID();
        if(parentIID != 0) {
          IBaseEntityDAO parentDAO = rdbmsComponentUtils.getBaseEntityDAO(parentIID);
          parentDAO.addChildren(EmbeddedType.CONTEXTUAL_CLASS, variantBaseEntityDTO);
        }
        IBaseEntityPlanDTO dto = new BaseEntityPlanDTO();
        dto.setBaseEntityIIDs(Arrays.asList(childrenIID));
        dto.setLocaleID(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID());
        dto.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
        dto.setOrganizationCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
        dto.setUserId(context.getUserId());
        BGPDriverDAO.instance().submitBGPProcess(context.getUserName(), SERVICE_FOR_RESTORE, "", userPriority,
            new JSONContent(dto.toJSON()));
      }
      catch (Exception e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
    });
  }

  private void restoreRelationships(IBaseEntityDTO baseEntityDTO,
      IGetConfigDetailsForVersionRollbackModel configDetails) throws RDBMSException, Exception
  {
    Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships = configDetails.getReferencedNatureRelationships();
    Map<String, IReferencedRelationshipModel> referencedRelationships = configDetails.getReferencedRelationships();
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    Map<String, ITag> referencedTags = configDetails.getReferencedTags();
    
    BaseType baseType = baseEntityDTO.getBaseType();
    ISaveRelationshipInstanceModel saveRelationshipModel = new SaveRelationshipInstanceModel();
    saveRelationshipModel.setId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    saveRelationshipModel.setBaseType(BaseEntityUtils.getBaseTypeString(baseType));
    saveRelationshipModel.setTabType(CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE);
    saveRelationshipModel.setTabId(SystemLevelIds.RELATIONSHIP_TAB);

    List<IContentRelationshipInstanceModel> modifiedRelationships = new ArrayList<>();
    List<IContentRelationshipInstanceModel> modifiedNatureRelationships = new ArrayList<>();

    List<IPropertyRecordDTO> relationSetDTOs = baseEntityDTO.getPropertyRecords().stream()
        .filter(property -> property instanceof RelationsSetDTO).collect(Collectors.toList());

    Set<String> natureRelationIds = new HashSet<String>();
    Map<String, Object> relationIdVsRelationsMap = new HashMap<String, Object>();
    Map<String, Object> relationIdVsSideIdMap = new HashMap<String, Object>();
    fillRelationsAndSideIdMap(relationSetDTOs, relationIdVsRelationsMap, natureRelationIds, relationIdVsSideIdMap);
    for (Entry<String, Object> mapEntry : relationIdVsRelationsMap.entrySet()) {

      String relationId = mapEntry.getKey();
      Set<IEntityRelationDTO> relations = (Set<IEntityRelationDTO>) mapEntry.getValue();
      IContentRelationshipInstanceModel relationship = getRelationshipInstanceModel(natureRelationIds, relationIdVsSideIdMap,
          referencedRelationships, referencedNatureRelationships, referencedElements, relationId, relations);
      for (IEntityRelationDTO relation : relations) {
        if (rdbmsComponentUtils.getBaseEntityDTO(relation.getOtherSideEntityIID()) != null) {
          relationship.getAddedElements().add(getRelationshipElement(relation, referencedTags));
        }
      }
      
      if (natureRelationIds.contains(relationId))
        modifiedNatureRelationships.add(relationship);
      else
        modifiedRelationships.add(relationship);
    }
    saveRelationshipModel.setModifiedRelationships(modifiedRelationships);
    saveRelationshipModel.setModifiedNatureRelationships(modifiedNatureRelationships);
    if(!modifiedRelationships.isEmpty() || !modifiedNatureRelationships.isEmpty()) {
      executeSaveRelation(saveRelationshipModel, baseType);
    }
  }

  private void restoreLocaleIds(IBaseEntityDTO baseEntityDTO,
      IGetConfigDetailsForVersionRollbackModel configDetails)
  {
    handleAddedLocaleIds(baseEntityDTO, baseEntityDTO.getBaseEntityIID(), baseEntityDTO.getLocaleIds());
  }

  private void validateBaseEntityDAO(IGetConfigDetailsForVersionRollbackModel configDetails,
      IBaseEntityDAO baseEntityDAO) throws NatureKlassNotFoundException
  {
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    boolean isNatureClassPresent = configDetails.getReferencedKlasses().entrySet().stream().anyMatch(entry-> entry.getKey().equals(String.valueOf(baseEntityDTO.getNatureClassifier().getClassifierCode())));
    if(!isNatureClassPresent) {
      throw new NatureKlassNotFoundException();
    }
    List<String> validLocaleIds = baseEntityDTO.getLocaleIds().stream().filter(locale-> configDetails.getReferencedLanguages().containsKey(locale)).collect(Collectors.toList());
    baseEntityDTO.setLocaleIds(validLocaleIds);
  }

  private void restoreTypesAndTaxonomies(IBaseEntityDTO baseEntityDTO,
      IGetConfigDetailsForVersionRollbackModel configDetails) throws Exception
  {
    try {
      List<IClassifierDTO> classifiers = new ArrayList<>();
      classifiers.addAll(baseEntityDTO.getOtherClassifiers().stream()
          .filter(classifier -> !classifier.getCode().equals(SystemLevelIds.GOLDEN_ARTICLE_KLASS))
          .collect(Collectors.toList()));
      fillClassifiersAndCallTypeSwitchRequest(baseEntityDTO, classifiers, new ArrayList<>());
    }catch(Exception e) {
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
  }
}