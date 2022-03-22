package com.cs.core.runtime.instancetree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.coupling.dto.ClassificationDataTransferDTO;
import com.cs.core.rdbms.coupling.idto.IClassificationDataTransferDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.goldenrecordbucket.dao.GoldenRecordBucketDAO;
import com.cs.core.rdbms.goldenrecordbucket.idao.IGoldenRecordBucketDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.runtime.interactor.entity.datarule.IConflictingValue;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.exception.goldenrecord.SourceRecordsUpdatedForBucketException;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.ICreateGoldenRecordRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IRelationshipIdSourceModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInfoModel;
import com.cs.core.runtime.interactor.model.templating.GetKlassInstanceForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForOverviewTabStrategy;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder.PropertyRecordType;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RelationshipInstanceUtil;
import com.cs.core.runtime.interactor.utils.klassinstance.RelationshipRecordBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.util.DataTransferBGPModelBuilder;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel.ApplicationActionType;

@SuppressWarnings("unchecked")
public abstract class AbstractCreateGoldenRecordService<P extends ICreateGoldenRecordRequestModel, R extends IGetKlassInstanceModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected RDBMSComponentUtils                         rdbmsComponentUtils;
  
  @Autowired
  protected IGetConfigDetailsWithoutPermissionsStrategy getConfigDetailsWithoutPermissionsStrategy;
  
  @Autowired
  protected KlassInstanceUtils                          klassInstanceUtils;
  
  @Autowired
  protected PermissionUtils                             permissionUtils;
  
  @Autowired
  protected RelationshipInstanceUtil                    relationshipInstanceUtil;
  
  @Autowired
  protected VariantInstanceUtils                        variantInstanceUtils;
  
  @Autowired
  protected IGetConfigDetailsForOverviewTabStrategy     getConfigDetailsForOverviewTabStrategy;
  
  @Autowired
  protected GoldenRecordUtils                           goldenRecordUtils;
  
  @Autowired
  protected DataTransferBGPModelBuilder                 dataTransferModelBuilder;
  
  private static final String                           CLASSIFICATION_DATA_TRANSFER_SERVICE                            = "CLASSIFICATION_DATA_TRANSFER";
  
  protected abstract Long getCounter();
  
  @Override
  public R executeInternal(P dataModel) throws Exception
  {
    List<String> sourceIds = dataModel.getSourceIds();
    String bucketId = dataModel.getBucketId();
    IGoldenRecordBucketDAO goldenRecordBucketDAO = new GoldenRecordBucketDAO();
    if (!ListUtils.isEqualList(sourceIds, goldenRecordBucketDAO.getGoldenRecordLinkedBaseEntityIidsById(bucketId))) {
      throw new SourceRecordsUpdatedForBucketException();
    }
    
    IMulticlassificationRequestModel multiclassificationRequestModel = getMulticlassificationRequestModelForConfigDetails(dataModel);
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsWithoutPermissionsStrategy.execute(multiclassificationRequestModel);
    
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    
    IBaseEntityDAO goldenRecordInstanceDAO = createKlassInstance(configDetails, dataModel, localeCatlogDAO);
    IBaseEntityDTO goldenRecordInstanceDTO = goldenRecordInstanceDAO.getBaseEntityDTO();
    
    addRelationshipsToGoldenRecordsInstance(goldenRecordInstanceDAO, configDetails, dataModel, localeCatlogDAO);
    
    goldenRecordUtils.handleDefaultImage(goldenRecordInstanceDAO);
    
    rdbmsComponentUtils.createNewRevision(goldenRecordInstanceDTO, configDetails.getNumberOfVersionsToMaintain());
    
    localeCatlogDAO.postUsecaseUpdate(goldenRecordInstanceDTO.getBaseEntityIID(), IEventDTO.EventType.ELASTIC_UPDATE);
    
    initiateClassificationDataTransfer(localeCatlogDAO, goldenRecordInstanceDTO.getBaseEntityIID(),
        goldenRecordInstanceDTO.getNatureClassifier());
    
    RuleHandler ruleHandler = new RuleHandler();
    ruleHandler.initiateRuleHandling(goldenRecordInstanceDAO, localeCatlogDAO, false, configDetails.getReferencedElements(),
        configDetails.getReferencedAttributes(), configDetails.getReferencedTags());
    
    goldenRecordUtils.initiateEvaluateGoldenRecordBucket(goldenRecordInstanceDTO);
    
    handleInitiatePropogationToBeMerged(sourceIds, localeCatlogDAO, goldenRecordBucketDAO);
    
    return (R) prepareDataForResponse(multiclassificationRequestModel, goldenRecordInstanceDAO);
  }
  
  private IBaseEntityDAO createKlassInstance(IGetConfigDetailsForCustomTabModel configDetails, P model, ILocaleCatalogDAO localeCatalogDAO)
      throws Exception
  {
    String goldenRecordId = model.getGoldenRecordId();
    String baseEntityID = goldenRecordId;
    if (goldenRecordId == null) {
      baseEntityID = RDBMSUtils.newUniqueID(this.getBaseType().getPrefix());
    }
    
    List<String> klassIds = model.getKlassIds();
    Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses = configDetails.getReferencedKlasses();
    String natureKlassId = klassIds.stream().filter(klassId -> referencedKlasses.get(klassId).getIsNature()).findFirst().get();
    klassIds.remove(natureKlassId);
    
    IClassifierDTO classifierDTO = ConfigurationDAO.instance().getClassifierByCode(natureKlassId);
    String creationLanguageInstance = model.getCreationLanguage();
    IBaseEntityDTO baseEntityDTO = localeCatalogDAO.createGoldenRecordArticle(creationLanguageInstance, classifierDTO, this.getBaseType(),
        baseEntityID, transactionThread.getTransactionData().getEndpointId());
    
    IBaseEntityDAO baseEntityDAO = localeCatalogDAO.openBaseEntity(baseEntityDTO);
    
    IPropertyRecordDTO[] propertyRecords = createPropertyRecordInstance(baseEntityDAO, configDetails, model);
    baseEntityDAO.createPropertyRecords(propertyRecords);
    Set<IPropertyDTO> properties = (Set<IPropertyDTO>) localeCatalogDAO.getAllEntityProperties(baseEntityDTO.getBaseEntityIID());
    baseEntityDAO.loadPropertyRecords(properties.toArray(new IPropertyDTO[0]));
    
    List<String> localeIds = new ArrayList<String>(model.getSelectedLanguageCodes());
    localeIds.remove(creationLanguageInstance);
    if (!localeIds.isEmpty()) {
      baseEntityDAO.createLanguageTranslation(localeIds);
    }
    localeCatalogDAO.loadLocaleIds(baseEntityDAO.getBaseEntityDTO());
    
    String defaultAssetInstanceId = model.getDefaultAssetInstanceId();
    if (StringUtils.isNotEmpty(defaultAssetInstanceId)) {
      baseEntityDAO.getBaseEntityDTO().setDefaultImageIID(Long.parseLong(defaultAssetInstanceId));
    }
    
    if(!klassIds.contains(SystemLevelIds.GOLDEN_ARTICLE_KLASS)) {
        klassIds.add(SystemLevelIds.GOLDEN_ARTICLE_KLASS);
    }
    
    goldenRecordUtils.addTypesAndTaxonomies(configDetails, klassIds, model.getTaxonomyIds(), new HashSet<>(), baseEntityDAO);
    return baseEntityDAO;
  }

  private void addRelationshipsToGoldenRecordsInstance(IBaseEntityDAO baseEntityDAO, IGetConfigDetailsForCustomTabModel configDetails,
      P dataModel, ILocaleCatalogDAO localeCatlogDAO) throws Exception
  {
    List<String> sourceIds = dataModel.getSourceIds();
    List<IRelationshipIdSourceModel> relationships = dataModel.getRelationships();
    List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel = new ArrayList<>();
    Map<String, List<Long>> relationIdVsAddedElements = new HashMap<>();
    
    IGetConfigDetailsForSaveRelationshipInstancesResponseModel relationshipConfigDetails = goldenRecordUtils
        .prepareRelationshipConfigDetails(configDetails);
    RelationshipRecordBuilder relationshipRecordBuilder = new RelationshipRecordBuilder(baseEntityDAO, relationshipConfigDetails,
        this.rdbmsComponentUtils);
    
    goldenRecordUtils.cloneSelectedRelationships(relationships, localeCatlogDAO, baseEntityDAO, relationshipDataTransferModel, relationIdVsAddedElements);
    
    goldenRecordUtils.createStandardArticleGoldenArticleRelationships(sourceIds, configDetails, relationshipDataTransferModel, relationshipRecordBuilder,
        baseEntityDAO);
    
    String userId = transactionThread.getTransactionData().getUserId();
    goldenRecordUtils.prepareDataForRelationshipDataTransfer(baseEntityDAO.getBaseEntityDTO(), relationships, userId, relationIdVsAddedElements, new HashMap<>());
    dataTransferModelBuilder.prepareDataForRelationshipInheritance(relationshipDataTransferModel, baseEntityDAO.getBaseEntityDTO().getBaseEntityIID(), false, ApplicationActionType.TRANSFER, baseEntityDAO);
  }
  
  private void initiateClassificationDataTransfer(ILocaleCatalogDAO localeCatlogDAO, Long baseEntityIID, IClassifierDTO classifierDTO)
      throws Exception
  {
    IClassificationDataTransferDTO classificationDataTransfer = new ClassificationDataTransferDTO();
    ILocaleCatalogDTO localeCatalogDTO = localeCatlogDAO.getLocaleCatalogDTO();
    classificationDataTransfer.setLocaleID(localeCatalogDTO.getLocaleID());
    classificationDataTransfer.setCatalogCode(localeCatalogDTO.getCatalogCode());
    classificationDataTransfer.setOrganizationCode(localeCatalogDTO.getOrganizationCode());
    classificationDataTransfer.setUserId(context.getUserId());
    classificationDataTransfer.setBaseEntityIID(baseEntityIID);
    classificationDataTransfer.getAddedOtherClassifiers().add(classifierDTO);
    
    BGPDriverDAO.instance().submitBGPProcess(context.getUserName(), CLASSIFICATION_DATA_TRANSFER_SERVICE, "", BGPPriority.HIGH,
        new JSONContent(classificationDataTransfer.toJSON()));
  }
  
  private IPropertyRecordDTO[] createPropertyRecordInstance(IBaseEntityDAO baseEntityDAO, IGetConfigDetailsForCustomTabModel configDetails,
      P model) throws Exception
  {
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO, configDetails, PropertyRecordType.CREATE,
        rdbmsComponentUtils.getLocaleCatlogDAO());
    
    List<String> localeIdsOfNamePropertyRecord = new ArrayList<>();
    List<IPropertyRecordDTO> propertyRecords = goldenRecordUtils.createAttributePropertyRecordInstance(
        propertyRecordBuilder, model.getAttributes(), model.getDependentAttributes(), localeIdsOfNamePropertyRecord);
    
    String natureKlassLabel = klassInstanceUtils.getDefaultInstanceNameByConfigdetails(configDetails, 
        SystemLevelIds.GOLDEN_ARTICLE_KLASS);
    
    List<String> selectedLanguageInstances = new ArrayList<String>(model.getSelectedLanguageCodes());
    selectedLanguageInstances.removeAll(localeIdsOfNamePropertyRecord);
    
    if(!selectedLanguageInstances.isEmpty())
    {
      goldenRecordUtils.addNameAttribute(propertyRecords, propertyRecordBuilder, getCounter(), selectedLanguageInstances,
          "CREATE", natureKlassLabel, configDetails.getReferencedAttributes().get(CommonConstants.NAME_ATTRIBUTE),
          (IReferencedSectionAttributeModel)configDetails.getReferencedElements().get(CommonConstants.NAME_ATTRIBUTE));
    }
    
    propertyRecords.addAll(this.createTagPropertyRecordInstance(propertyRecordBuilder, model));
    
    return propertyRecords.toArray(new IPropertyRecordDTO[propertyRecords.size()]);
  }
  
  private List<IPropertyRecordDTO> createTagPropertyRecordInstance(PropertyRecordBuilder propertyRecordBuilder, P model) throws Exception
  {
    Map<String, List<IConflictingValue>> tags = model.getTags();
    List<IPropertyRecordDTO> tagRecords = new ArrayList<>();
    if (tags.isEmpty())
      return tagRecords;
    
    for (String tagId : tags.keySet()) {
      ITagConflictingValue value = (ITagConflictingValue) tags.get(tagId).get(0);
      ITagInstance tag = prepareTagInstanceFromConflictingValue(value);
      tag.setTagId(tagId);
      
      if (tag.getContextInstanceId() == null) {
        IPropertyRecordDTO updateValueRecord = propertyRecordBuilder.updateTagsRecord(tag);
        if (updateValueRecord != null) {
          tagRecords.add(updateValueRecord);
        }
      }
    }
    return tagRecords;
  }
  
  private IMulticlassificationRequestModel getMulticlassificationRequestModelForConfigDetails(P dataModel)
  {
    List<String> klassIds = new ArrayList<>(dataModel.getKlassIds());
    klassIds.add(SystemLevelIds.GOLDEN_ARTICLE_KLASS);
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    multiclassificationRequestModel.setKlassIds(klassIds);
    multiclassificationRequestModel.setSelectedTaxonomyIds(dataModel.getTaxonomyIds());
    multiclassificationRequestModel.setShouldSendTaxonomyHierarchies(true);
    multiclassificationRequestModel.setShouldUseTagIdTagValueIdsMap(false);
    ITransactionData transactionData = transactionThread.getTransactionData();
    multiclassificationRequestModel.setOrganizationId(transactionData.getOrganizationId());
    multiclassificationRequestModel.setEndpointId(transactionData.getEndpointId());
    multiclassificationRequestModel.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    multiclassificationRequestModel.setLanguageCodes(dataModel.getSelectedLanguageCodes());
    multiclassificationRequestModel.setUserId(transactionData.getUserId());
    return multiclassificationRequestModel;
  }
  
  private ITagInstance prepareTagInstanceFromConflictingValue(ITagConflictingValue tagConflictingValue)
  {
    ITagInstance tag = new TagInstance();
    tag.setTagId(tagConflictingValue.getId());
    List<IIdRelevance> conflictingTagValues = tagConflictingValue.getTagValues();
    List<ITagInstanceValue> tagValues = new ArrayList<>();
    conflictingTagValues.forEach(conflictingTagValue -> {
      
      ITagInstanceValue tagInstanceValue = new TagInstanceValue();
      tagInstanceValue.setId(conflictingTagValue.getId());
      tagInstanceValue.setTagId(conflictingTagValue.getTagId());
      tagInstanceValue.setRelevance(conflictingTagValue.getRelevance());
      tagValues.add(tagInstanceValue);
    });
    tag.setTagValues(tagValues);
    return tag;
  }
  
  private void handleInitiatePropogationToBeMerged(List<String> sourceIds, ILocaleCatalogDAO localeCatlogDAO,
      IGoldenRecordBucketDAO goldenRecordBucketDAO) throws Exception
  {
    goldenRecordBucketDAO.setBaseEntityIidsToIsMerged(sourceIds);
    for (String sourceId : sourceIds) {
      Long baseEntityIid = Long.valueOf(sourceId);
      IBaseEntityDTO baseEntityDTO = localeCatlogDAO.getEntityByIID(baseEntityIid);
      localeCatlogDAO.postDeleteUpdate(baseEntityDTO);
      goldenRecordUtils.initiateEvaluateGoldenRecordBucket(baseEntityDTO);
    }
  }
  
  protected IGetKlassInstanceModel prepareDataForResponse(IMulticlassificationRequestModel multiclassificationRequestModel,
      IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IGetConfigDetailsModel configDetails = getConfigDetailsForOverviewTabStrategy.execute(multiclassificationRequestModel);
    IGetKlassInstanceModel returnModel = executeGetKlassInstance(configDetails, baseEntityDAO);
    returnModel.setVariantOfLabel(
        klassInstanceUtils.getVariantOfLabel(baseEntityDAO, ((IGetConfigDetailsForCustomTabModel) configDetails).getLinkedVariantCodes()));
    returnModel.setBranchOfLabel(KlassInstanceUtils.getBranchOfLabel(baseEntityDAO.getBaseEntityDTO(), rdbmsComponentUtils));
    permissionUtils.manageKlassInstancePermissions(returnModel);
    return returnModel;
  }
  
  protected IGetKlassInstanceModel executeGetKlassInstance(IGetConfigDetailsModel configDetails, IBaseEntityDAO baseEntityDAO)
      throws Exception
  {
    KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO, configDetails, this.rdbmsComponentUtils);
    IKlassInstance klassInstance = klassInstanceBuilder.getKlassInstance();
    IGetKlassInstanceModel model = new GetKlassInstanceForCustomTabModel();
    ((IGetKlassInstanceCustomTabModel) model).setAttributeVariantsStats(klassInstanceBuilder.getAttributeVariantsStats());
    if (!((IGetConfigDetailsForCustomTabModel) configDetails).getReferencedNatureRelationships().isEmpty()) {
      setNatureRelationship((IGetConfigDetailsForCustomTabModel) configDetails, (IGetKlassInstanceCustomTabModel) model);
    }
    relationshipInstanceUtil.executeGetRelationshipInstance((IGetKlassInstanceCustomTabModel) model,
        (IGetConfigDetailsForCustomTabModel) configDetails, baseEntityDAO, this.rdbmsComponentUtils);
    // on any tab we will get the context information
    model.setReferencedInstances(variantInstanceUtils.fillContextualData(configDetails, baseEntityDAO, klassInstance));
    if (klassInstance.getName().isEmpty()) {
      IPropertyDTO nameProperty = ConfigurationDAO.instance().getPropertyByCode(SystemLevelIds.NAME_ATTRIBUTE);
      baseEntityDAO.loadPropertyRecords(nameProperty);
      klassInstance.setName(((IValueRecordDTO) baseEntityDAO.getBaseEntityDTO()
          .getPropertyRecord(nameProperty.getPropertyIID())).getValue());
    }
    model.setKlassInstance((IContentInstance) klassInstance);
    model.setConfigDetails(configDetails);
    return model;
  }
  
  private void setNatureRelationship(IGetConfigDetailsForCustomTabModel configDetails, IGetKlassInstanceCustomTabModel returnModel)
      throws RDBMSException, Exception
  {
    IKlassInstanceRelationshipInstance klassInstanceRelationshipInstance = new KlassInstanceRelationshipInstance();
    Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships = configDetails.getReferencedNatureRelationships();
    
    Set<Entry<String, IGetReferencedNatureRelationshipModel>> natureRelationshipsEntrySet = referencedNatureRelationships.entrySet();
    for (Entry<String, IGetReferencedNatureRelationshipModel> natureRelationshipEntrySet : natureRelationshipsEntrySet) {
      IGetReferencedNatureRelationshipModel natureRelationship = natureRelationshipEntrySet.getValue();
      klassInstanceRelationshipInstance.setRelationshipId(natureRelationship.getId());
      klassInstanceRelationshipInstance.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RELATIONSHIP.getPrefix()));
      klassInstanceRelationshipInstance.setSideId(natureRelationship.getSide1().getElementId());
      returnModel.getNatureRelationships().add(klassInstanceRelationshipInstance);
    }
  }
  
  private BaseType getBaseType()
  {
    return BaseType.ARTICLE;
  }
  
}
