package com.cs.core.runtime.instancetree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.standard.IStandardConfig.StandardProperty;
import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.tag.ModifiedTagInstanceModel;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.goldenrecordbucket.dao.GoldenRecordBucketDAO;
import com.cs.core.rdbms.goldenrecordbucket.idao.IGoldenRecordBucketDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.runtime.interactor.entity.datarule.IAttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.datarule.IConflictingValue;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IPropertyInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.exception.goldenrecord.SourceRecordsUpdatedForBucketException;
import com.cs.core.runtime.interactor.exception.variants.DuplicateVariantExistsException;
import com.cs.core.runtime.interactor.model.attribute.IModifiedAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.attribute.ModifiedAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.goldenrecord.IGoldenRecordTypeInfoModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IIdBucketIdModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IRelationshipIdSourceModel;
import com.cs.core.runtime.interactor.model.goldenrecord.ISaveGoldenRecordRequestModel;
import com.cs.core.runtime.interactor.model.instance.ContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
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
import com.cs.core.util.ConfigUtil;
import com.cs.core.util.DataTransferBGPModelBuilder;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel.ApplicationActionType;
import com.cs.utils.BaseEntityUtils;

public abstract class AbstractSaveGoldenRecordService<P extends ISaveGoldenRecordRequestModel, R extends IGetKlassInstanceModel>
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
  protected ConfigUtil                                  configUtil;
  
  @Autowired
  protected TransactionThreadData                       controllerThread;
  
  @Autowired
  protected ISessionContext                            context;
  
  @Autowired
  protected GoldenRecordUtils                           goldenRecordUtils;
  
  @Autowired
  protected IGetConfigDetailsForOverviewTabStrategy     getConfigDetailsForOverviewTabStrategy;
  
  @Autowired
  protected DataTransferBGPModelBuilder                 dataTransferModelBuilder;
  
  private static final String                           INDEPENDENT                        = "independent";
  
  protected abstract Long getCounter();
  
  protected abstract IGoldenRecordTypeInfoModel getTypeIdFromBucketId(IIdBucketIdModel idModel)
      throws Exception;
  
  protected abstract BaseType getBaseType();
  
  @SuppressWarnings("unchecked")
  @Override
  public R executeInternal(P dataModel) throws Exception
  {
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    List<String> languageInheritance = configUtil.getLanguageInheritance();
    
    long goldenRecordId = Long.parseLong(dataModel.getGoldenRecordId());
    
    IBaseEntityDAO goldenRecordDAO = rdbmsComponentUtils.getBaseEntityDAO(goldenRecordId,
        languageInheritance);
    IBaseEntityDTO goldenRecordDTO = goldenRecordDAO.getBaseEntityDTO();
    
    List<String> addedLanguageCodes = getAddedLanguageCodes(goldenRecordDTO, dataModel);
    
    if(!addedLanguageCodes.isEmpty()) {
      goldenRecordDAO.createLanguageTranslation(addedLanguageCodes);
    }

    IMulticlassificationRequestModel multiclassificationRequestModel = getMulticlassificationRequestModelForConfigDetails(
        dataModel);
    
    long startTime = System.currentTimeMillis();
    
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsWithoutPermissionsStrategy
        .execute(multiclassificationRequestModel);
    
    RDBMSLogger.instance().debug("NA|OrientDB|" + this.getClass().getSimpleName() + "|executeInternal|ConfigDetailsForSaveGoldenRecord| %d ms",
        System.currentTimeMillis() - startTime);
    
    List<String> sourceIds = dataModel.getSourceIds();
    String bucketId = dataModel.getBucketId();
    boolean newSourceAdded = bucketId != null && !sourceIds.isEmpty();
    IGoldenRecordBucketDAO goldenRecordBucketDAO = new GoldenRecordBucketDAO();
   
    if(newSourceAdded) {
      if (!ListUtils.isEqualList(sourceIds, goldenRecordBucketDAO.getGoldenRecordLinkedBaseEntityIidsById(bucketId))) {
        throw new SourceRecordsUpdatedForBucketException();
      }
      addNewTypesAndTaxonomies(dataModel, goldenRecordDAO, configDetails, localeCatlogDAO);
    }
    
    handleProperties(dataModel, localeCatlogDAO, goldenRecordId, goldenRecordDAO, configDetails, addedLanguageCodes);
    
    handleRelationships(goldenRecordDAO, dataModel.getRelationships(), configDetails, sourceIds, newSourceAdded);

    goldenRecordUtils.handleDefaultImage(goldenRecordDAO);
    
    rdbmsComponentUtils.createNewRevision(goldenRecordDTO,configDetails.getNumberOfVersionsToMaintain());
    
    localeCatlogDAO.postUsecaseUpdate(goldenRecordDTO.getBaseEntityIID(), IEventDTO.EventType.ELASTIC_UPDATE);
    
    RuleHandler ruleHandler = new RuleHandler();
    ruleHandler.initiateRuleHandling(goldenRecordDAO, localeCatlogDAO, false,
        configDetails.getReferencedElements(), configDetails.getReferencedAttributes(),
        configDetails.getReferencedTags());
    
    goldenRecordUtils.initiateEvaluateGoldenRecordBucket(goldenRecordDTO);
    
    if(newSourceAdded) {
      handleInitiatePropogationToBeMerged(sourceIds, localeCatlogDAO, goldenRecordBucketDAO);
    }
    
    return (R) prepareDataForResponse(multiclassificationRequestModel,
        rdbmsComponentUtils.getBaseEntityDAO(goldenRecordDTO));
  }

  @SuppressWarnings("unchecked")
  private void addNewTypesAndTaxonomies(P dataModel, IBaseEntityDAO goldenRecordDAO,
      IGetConfigDetailsForCustomTabModel configDetails, ILocaleCatalogDAO localeCatlogDAO)
      throws RDBMSException, SourceRecordsUpdatedForBucketException, Exception
  {
    List<String> existingKlasses = BaseEntityUtils.getAllReferenceTypeFromBaseEntity(goldenRecordDAO);
    List<String> existingTaxonomies = BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(goldenRecordDAO.getClassifiers());
    
    List<String> klassesToAdd = ListUtils.subtract(dataModel.getKlassIds(), existingKlasses);
    List<String> taxonomiesToAdd = ListUtils.subtract(dataModel.getTaxonomyIds(), existingTaxonomies);
    
    if(!klassesToAdd.isEmpty() || !taxonomiesToAdd.isEmpty()) {
      Set<IClassifierDTO> addedClassifiers = new HashSet<>();
      goldenRecordUtils.addTypesAndTaxonomies(configDetails, klassesToAdd, taxonomiesToAdd, addedClassifiers, goldenRecordDAO);
      dataTransferModelBuilder.initiateClassificationDataTransfer(localeCatlogDAO, goldenRecordDAO.getBaseEntityDTO().getBaseEntityIID(), addedClassifiers, new HashSet<>());
    }
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

  private void handleProperties(P dataModel, ILocaleCatalogDAO localeCatlogDAO, long goldenRecordId,
      IBaseEntityDAO goldenRecordDAO, IGetConfigDetailsForCustomTabModel configDetails, List<String> addedLanguageCodes)
      throws RDBMSException, Exception
  {
    Map<String, List<IConflictingValue>> attributes = dataModel.getAttributes();
    Map<String, Map<String, List<IConflictingValue>>> dependentAttributes = dataModel.getDependentAttributes();
    Map<String, List<IConflictingValue>> tags = dataModel.getTags();
    
    Map<String, List<IConflictingValue>> modifiedAttributes = new HashMap<>();
    Map<String, List<IConflictingValue>> addedAttributes = new HashMap<>();
    
    Map<String, Map<String, List<IConflictingValue>>> modifiedDependentAttributes = new HashMap<>();
    Map<String, Map<String, List<IConflictingValue>>> addedDependentAttributes = new HashMap<>();
    
    Map<String, List<IConflictingValue>> modifiedTags = new HashMap<>();
    Map<String, List<IConflictingValue>> addedTags = new HashMap<>();
    
    Map<String, Map<String, String>> attrIdVsMergedId = new HashMap<>();
    attrIdVsMergedId.put(INDEPENDENT, new HashMap<>());

    fillAllProperties(localeCatlogDAO, goldenRecordId, attributes, dependentAttributes, tags,
        modifiedAttributes, addedAttributes, modifiedDependentAttributes, addedDependentAttributes,
        modifiedTags, addedTags, attrIdVsMergedId);
    
    manageAddedProperties(goldenRecordDAO, configDetails, addedAttributes, addedDependentAttributes, addedTags, addedLanguageCodes);
    manageModifiedProperties(configDetails, goldenRecordDAO, modifiedAttributes, modifiedDependentAttributes, modifiedTags, attrIdVsMergedId);
  }

  private void fillAllProperties(ILocaleCatalogDAO localeCatlogDAO, long goldenRecordId,
      Map<String, List<IConflictingValue>> attributes,
      Map<String, Map<String, List<IConflictingValue>>> dependentAttributes,
      Map<String, List<IConflictingValue>> tags,
      Map<String, List<IConflictingValue>> modifiedAttributes,
      Map<String, List<IConflictingValue>> addedAttributes,
      Map<String, Map<String, List<IConflictingValue>>> modifiedDependentAttributes,
      Map<String, Map<String, List<IConflictingValue>>> addedDependentAttributes,
      Map<String, List<IConflictingValue>> modifiedTags,
      Map<String, List<IConflictingValue>> addedTags, Map<String, Map<String, String>> attrIdVsMergedId)
      throws Exception
  {
    List<IValueRecordDTO> existingValueRecords = localeCatlogDAO.getAllValueRecords(goldenRecordId).get(goldenRecordId);
    List<ITagsRecordDTO> existingTagsRecords = localeCatlogDAO.getAllTagsRecords(goldenRecordId).get(goldenRecordId);
    
    Map<String, List<IValueRecordDTO>> existingAttributes = new HashMap<>();
    Map<String, ITagsRecordDTO> existingTags = new HashMap<>();
    
    if(existingValueRecords != null) {
      for(IValueRecordDTO valueRecord : existingValueRecords) {
        String code = valueRecord.getProperty().getPropertyCode();
        List<IValueRecordDTO> list = existingAttributes.get(code);
        if(list == null) {
          list = new ArrayList<>();
          existingAttributes.put(code, list);
        }
        list.add(valueRecord);
      }
    }
    
    if(existingTagsRecords != null) {
      for(ITagsRecordDTO tagRecord : existingTagsRecords) {
        existingTags.put(tagRecord.getProperty().getPropertyCode(), tagRecord);
      }
    }
    
    Map<String, String> independentAttrVsMergedId = attrIdVsMergedId.get(INDEPENDENT);
    
    fillIndependentAttributes(attributes, modifiedAttributes, addedAttributes, existingAttributes,
        independentAttrVsMergedId);
    
    fillDependentAttributes(dependentAttributes, modifiedDependentAttributes,
        addedDependentAttributes, attrIdVsMergedId, existingAttributes);
    
    fillTags(tags, modifiedTags, addedTags, existingTags);
  }

  private void fillTags(Map<String, List<IConflictingValue>> tags,
      Map<String, List<IConflictingValue>> modifiedTags,
      Map<String, List<IConflictingValue>> addedTags, Map<String, ITagsRecordDTO> existingTags)
  {
    if (tags != null) {
      for (String tagId : tags.keySet()) {
        if (existingTags.get(tagId) != null) {
          modifiedTags.put(tagId, tags.get(tagId));
        }
        else {
          addedTags.put(tagId, tags.get(tagId));
        }
      }
    }
  }

  private void fillDependentAttributes(
      Map<String, Map<String, List<IConflictingValue>>> dependentAttributes,
      Map<String, Map<String, List<IConflictingValue>>> modifiedDependentAttributes,
      Map<String, Map<String, List<IConflictingValue>>> addedDependentAttributes,
      Map<String, Map<String, String>> attrIdVsMergedId,
      Map<String, List<IValueRecordDTO>> existingAttributes)
  {
    for(String localeId : dependentAttributes.keySet()) {
      
      Map<String, List<IConflictingValue>> attributeMap = dependentAttributes.get(localeId);
      Map<String, String> dependentAttrVsMergedId = attrIdVsMergedId.get(localeId);
      
      if(dependentAttrVsMergedId == null) {
        dependentAttrVsMergedId = new HashMap<>();
        attrIdVsMergedId.put(localeId, dependentAttrVsMergedId);
      }
      
      Map<String, List<IConflictingValue>> modifiedMap = new HashMap<>();
      Map<String, List<IConflictingValue>> addedMap = new HashMap<>();
      
      for(String id : attributeMap.keySet()) {
        List<IValueRecordDTO> list = existingAttributes.get(id);   // here we can get multiple value records of same attribute ID in different locales
        if(list != null) {   
          for(IValueRecordDTO valueRecord : list) {
            if(valueRecord.getLocaleID().equals(localeId)) {
              String mergedId = KlassInstanceBuilder.getAttributeID(valueRecord);  
              dependentAttrVsMergedId.put(id, mergedId);
              modifiedMap.put(id, attributeMap.get(id));
            }
          }
        }
        else {
          addedMap.put(id, attributeMap.get(id));
        }
      }
      if(!modifiedMap.isEmpty())
          modifiedDependentAttributes.put(localeId, modifiedMap);
      
      if(!addedMap.isEmpty())
          addedDependentAttributes.put(localeId, addedMap);
    }
  }

  private void fillIndependentAttributes(Map<String, List<IConflictingValue>> attributes,
      Map<String, List<IConflictingValue>> modifiedAttributes,
      Map<String, List<IConflictingValue>> addedAttributes,
      Map<String, List<IValueRecordDTO>> existingAttributes,
      Map<String, String> independentAttrVsMergedId)
  {
    for(String attributeId : attributes.keySet()) {
     
      List<IValueRecordDTO> valueRecordDTOs = existingAttributes.get(attributeId); 
      if(valueRecordDTOs != null) {
        // Independent attributes will not have multiple value records. Hence, fetching 0th index.
        IValueRecordDTO valueRecord = valueRecordDTOs.get(0);
        String mergedId = KlassInstanceBuilder.getAttributeID(valueRecord);  
        independentAttrVsMergedId.put(attributeId, mergedId);
        modifiedAttributes.put(attributeId, attributes.get(attributeId));
      }
      else {
        addedAttributes.put(attributeId, attributes.get(attributeId));
      }
    }
  }

  protected void manageModifiedProperties(IGetConfigDetailsModel configDetails,
      IBaseEntityDAO baseEntityDAO, Map<String, List<IConflictingValue>> modifiedAttributes,
      Map<String, Map<String, List<IConflictingValue>>> modifiedDependentAttributes,
      Map<String, List<IConflictingValue>> modifiedTags,
      Map<String, Map<String, String>> attrIdVsmergedId) throws Exception
  {
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO, configDetails, PropertyRecordType.UPDATE,
        rdbmsComponentUtils.getLocaleCatlogDAO());
    
    List<IPropertyRecordDTO> propertyRecordDTOs = this.manageModifiedAttributes(configDetails, propertyRecordBuilder,
        baseEntityDAO, modifiedAttributes, modifiedDependentAttributes, attrIdVsmergedId);
    
    propertyRecordDTOs.addAll(this.createTagPropertyRecordInstance(propertyRecordBuilder, modifiedTags, false));
    
    long starTime = System.currentTimeMillis();
    baseEntityDAO.updatePropertyRecords(propertyRecordDTOs.toArray(new IPropertyRecordDTO[0]));
    RDBMSLogger.instance().debug("NA|RDBMS|" + this.getClass().getSimpleName() + "|manageModifiedProperties|updatePropertyRecords| %d ms",
        System.currentTimeMillis() - starTime);
  }
  
  protected List<IPropertyRecordDTO> manageModifiedAttributes(IGetConfigDetailsModel configDetails,
      PropertyRecordBuilder propertyRecordBuilder, IBaseEntityDAO baseEntityDAO,
      Map<String, List<IConflictingValue>> modifiedAttributes,
      Map<String, Map<String, List<IConflictingValue>>> modifiedDependentAttributes,
      Map<String, Map<String, String>> attrIdVsmergedId) throws Exception
  {
    if (modifiedAttributes.isEmpty() && modifiedDependentAttributes.isEmpty())
      return new ArrayList<IPropertyRecordDTO>();
    
    List<IPropertyRecordDTO> valueRecords = new ArrayList<>();
    
    updatePropertyRecordsFromAttributes(propertyRecordBuilder, valueRecords, "", modifiedAttributes,
        baseEntityDAO, configDetails, attrIdVsmergedId.get(INDEPENDENT));
    
    for (String localeId : modifiedDependentAttributes.keySet()) {
        Map<String, List<IConflictingValue>> dependentAttributes = modifiedDependentAttributes.get(localeId);
        updatePropertyRecordsFromAttributes(propertyRecordBuilder, valueRecords, localeId, dependentAttributes, baseEntityDAO, configDetails, attrIdVsmergedId.get(localeId));
    }
    return valueRecords;
  }
  
  private List<IPropertyRecordDTO> createAttributePropertyRecordInstance(
      PropertyRecordBuilder propertyRecordBuilder, Map<String, List<IConflictingValue>> addedAttributes,
      Map<String, Map<String, List<IConflictingValue>>> addedDependentAttributes, 
      List<String> localeIdsOfNamePropertyRecord) 
          throws Exception
  {
    if (addedAttributes.isEmpty() && addedDependentAttributes.isEmpty())
      return new ArrayList<IPropertyRecordDTO>();
    
    List<IPropertyRecordDTO> valueRecords = new ArrayList<>();
    
    preparePropertyRecordsFromAttributes(propertyRecordBuilder, valueRecords, "", addedAttributes);
    
    for (String localeId : addedDependentAttributes.keySet()) {
        Map<String, List<IConflictingValue>> dependentAttributes = addedDependentAttributes.get(localeId);
        if(dependentAttributes.keySet().contains(StandardProperty.nameattribute.name()))
        {
          localeIdsOfNamePropertyRecord.add(localeId);
        }
        preparePropertyRecordsFromAttributes(propertyRecordBuilder, valueRecords, localeId, dependentAttributes);
    }
    return valueRecords;
  }

  private void manageAddedProperties(IBaseEntityDAO baseEntityDAO,
      IGetConfigDetailsForCustomTabModel configDetails,
      Map<String, List<IConflictingValue>> addedAttributes,
      Map<String, Map<String, List<IConflictingValue>>> addedDependentAttributes,
      Map<String, List<IConflictingValue>> addedTags, List<String> addedLanguageCodes) throws Exception
  {
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO, configDetails, PropertyRecordType.CREATE,
        rdbmsComponentUtils.getLocaleCatlogDAO());
    
    List<String> localeIdsOfNamePropertyRecord = new ArrayList<>();
    List<IPropertyRecordDTO> propertyRecordDTOs = this.createAttributePropertyRecordInstance(propertyRecordBuilder, 
        addedAttributes, addedDependentAttributes,localeIdsOfNamePropertyRecord);
    
    addedLanguageCodes.removeAll(localeIdsOfNamePropertyRecord);
    if(!addedLanguageCodes.isEmpty())
    {
      goldenRecordUtils.addNameAttribute(propertyRecordDTOs, propertyRecordBuilder, getCounter(), addedLanguageCodes,
          "SAVE",  "Golden ", configDetails.getReferencedAttributes().get(CommonConstants.NAME_ATTRIBUTE), 
          (IReferencedSectionAttributeModel)configDetails.getReferencedElements().get(CommonConstants.NAME_ATTRIBUTE));
    }
    
    propertyRecordDTOs.addAll(this.createTagPropertyRecordInstance(propertyRecordBuilder, addedTags, true));
    
    IPropertyRecordDTO[] propertyRecords = propertyRecordDTOs.toArray(new IPropertyRecordDTO[0]);
    
    long starTime = System.currentTimeMillis();
    baseEntityDAO.createPropertyRecords(propertyRecords);
    RDBMSLogger.instance().debug("NA|RDBMS|" + this.getClass().getSimpleName() + "|manageAddedProperties|createPropertyRecords| %d ms",
        System.currentTimeMillis() - starTime);
  }
  
  private List<IPropertyRecordDTO> createTagPropertyRecordInstance(
      PropertyRecordBuilder propertyRecordBuilder, Map<String, List<IConflictingValue>> addedTags, boolean isNewlyAdded)
      throws Exception
  {
    List<IPropertyRecordDTO> tagRecords = new ArrayList<>();
    if (addedTags.isEmpty())
      return tagRecords;
    
    for (String tagId : addedTags.keySet()) {
      
      IPropertyInstance tagInstance = null;
      String contextInstanceId = "";
      ITagConflictingValue value = (ITagConflictingValue) addedTags.get(tagId).get(0);

      if (isNewlyAdded) {
        ITagInstance tag = prepareTagInstanceFromConflictingValue(value);
        tag.setTagId(tagId);
        
        tagInstance = tag;
        contextInstanceId = tag.getContextInstanceId();
      }
      else {
        IModifiedContentTagInstanceModel tag = prepareModifiedTagInstanceFromConflictingValue(value);
        tag.setTagId(tagId);
        
        tagInstance = tag;
        contextInstanceId = ((ITagInstance) tag.getEntity()).getContextInstanceId();
      }
      
      if (contextInstanceId == null) {
        IPropertyRecordDTO updateValueRecord = propertyRecordBuilder.updateTagsRecord(tagInstance);
        if (updateValueRecord != null) {
          tagRecords.add(updateValueRecord);
        }
      }
    }
    return tagRecords;
  }
  
  private void preparePropertyRecordsFromAttributes(PropertyRecordBuilder propertyRecordBuilder, List<IPropertyRecordDTO> valueRecords,
      String localeId, Map<String, List<IConflictingValue>> attributes) throws Exception
  {
    for (String attributeId : attributes.keySet()) {
      IAttributeConflictingValue value = (IAttributeConflictingValue) attributes.get(attributeId).get(0);
      IAttributeInstance attribute = prepareAttributeInstanceFromConflictingValue(value, attributeId);
      attribute.setLanguage(localeId);
      IPropertyRecordDTO updateValueRecord = propertyRecordBuilder.updateValueRecord(attribute);
      valueRecords.add(updateValueRecord);
    }
  }
  
  private void updatePropertyRecordsFromAttributes(PropertyRecordBuilder propertyRecordBuilder, List<IPropertyRecordDTO> valueRecords,
      String localeId, Map<String, List<IConflictingValue>> attributes, IBaseEntityDAO baseEntityDAO, IGetConfigDetailsModel configDetails, Map<String, String> attrIdVsmergedId) throws Exception
  {
    for (String attributeId : attributes.keySet()) {
      IAttributeConflictingValue value = (IAttributeConflictingValue) attributes.get(attributeId).get(0);
      IAttributeInstance attribute = prepareAttributeInstanceFromConflictingValue(value, attributeId);
      attribute.setLanguage(localeId);
      attribute.setId(attrIdVsmergedId.get(attributeId));
      
      if (!attribute.getAttributeId().equals(Constants.ASSET_COVERFLOW_ATTRIBUTE_ID)) {
        IPropertyRecordDTO updateValueRecord = propertyRecordBuilder.updateValueRecord(attribute);
        if ((((IModifiedAttributeInstanceModel) attribute).getModifiedContext() != null)
            && (baseEntityDAO.isPropertyRecordDuplicate(updateValueRecord))) {
          
          throw new DuplicateVariantExistsException();
        }
        
        IContextualDataDTO context = ((IValueRecordDTO) updateValueRecord).getContextualObject();
        String contextCode = context.getContextCode();
        if (!StringUtils.isEmpty(contextCode)) {
          variantInstanceUtils.checkContextFieldsExists(context.getContextTagValues(), context,
              configDetails.getReferencedVariantContexts().getEmbeddedVariantContexts().get(contextCode));
        }
        
        if (updateValueRecord != null) {
          valueRecords.add(updateValueRecord);
        }
      }
    }
  }

  private List<String> getAddedLanguageCodes(IBaseEntityDTO baseEntityDTO, P dataModel)
  {
    List<String> existingLanguageCodes = baseEntityDTO.getLocaleIds();
    List<String> selectedLanguageCodes = dataModel.getSelectedLanguageCodes();
    
    return selectedLanguageCodes.stream()
        .filter(selectedLanguageCode -> !existingLanguageCodes.contains(selectedLanguageCode))
        .collect(Collectors.toList());
  }
  
  private IMulticlassificationRequestModel getMulticlassificationRequestModelForConfigDetails(P dataModel)
  {
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    multiclassificationRequestModel.setKlassIds(dataModel.getKlassIds());
    
    if(!dataModel.getKlassIds().contains(SystemLevelIds.GOLDEN_ARTICLE_KLASS)) {
      multiclassificationRequestModel.getKlassIds().add(SystemLevelIds.GOLDEN_ARTICLE_KLASS);
    }
    
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
  
  private IAttributeInstance prepareAttributeInstanceFromConflictingValue(
      IAttributeConflictingValue attributeConflictingValue, String attributeId)
  {
    IAttributeInstance attribute = new ModifiedAttributeInstanceModel();
    attribute.setAttributeId(attributeId);
    attribute.setValue(attributeConflictingValue.getValue());
    attribute.setValueAsHtml(attributeConflictingValue.getValueAsHtml());
    return attribute;
  }
  
  private ITagInstance prepareTagInstanceFromConflictingValue(
      ITagConflictingValue tagConflictingValue)
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
  
  protected IGetKlassInstanceModel prepareDataForResponse(
      IMulticlassificationRequestModel multiclassificationRequestModel,
      IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IGetConfigDetailsModel configDetails = getConfigDetailsForOverviewTabStrategy
        .execute(multiclassificationRequestModel);
    IGetKlassInstanceModel returnModel = executeGetKlassInstance(configDetails, baseEntityDAO);
    returnModel.setVariantOfLabel(klassInstanceUtils.getVariantOfLabel(baseEntityDAO,
        ((IGetConfigDetailsForCustomTabModel) configDetails).getLinkedVariantCodes()));
    returnModel.setBranchOfLabel(
        KlassInstanceUtils.getBranchOfLabel(baseEntityDAO.getBaseEntityDTO(), rdbmsComponentUtils));
    permissionUtils.manageKlassInstancePermissions(returnModel);
    return returnModel;
  }
  
  protected IGetKlassInstanceModel executeGetKlassInstance(IGetConfigDetailsModel configDetails,
      IBaseEntityDAO baseEntityDAO) throws Exception
  {
    KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO,
        configDetails, this.rdbmsComponentUtils);
    IKlassInstance klassInstance = klassInstanceBuilder.getKlassInstance();
    IGetKlassInstanceModel model = new GetKlassInstanceForCustomTabModel();
    ((IGetKlassInstanceCustomTabModel) model)
        .setAttributeVariantsStats(klassInstanceBuilder.getAttributeVariantsStats());
    if (!((IGetConfigDetailsForCustomTabModel) configDetails).getReferencedNatureRelationships()
        .isEmpty()) {
      setNatureRelationship((IGetConfigDetailsForCustomTabModel) configDetails,
          (IGetKlassInstanceCustomTabModel) model);
    }
    relationshipInstanceUtil.executeGetRelationshipInstance((IGetKlassInstanceCustomTabModel) model,
        (IGetConfigDetailsForCustomTabModel) configDetails, baseEntityDAO,
        this.rdbmsComponentUtils);
    // on any tab we will get the context information
    model.setReferencedInstances(
        variantInstanceUtils.fillContextualData(configDetails, baseEntityDAO, klassInstance));
    
    model.setKlassInstance((IContentInstance) klassInstance);
    model.setConfigDetails(configDetails);
    return model;
  }
  
  private void setNatureRelationship(IGetConfigDetailsForCustomTabModel configDetails,
      IGetKlassInstanceCustomTabModel returnModel) throws RDBMSException, Exception
  {
    IKlassInstanceRelationshipInstance klassInstanceRelationshipInstance = new KlassInstanceRelationshipInstance();
    Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships = configDetails
        .getReferencedNatureRelationships();
    
    Set<Entry<String, IGetReferencedNatureRelationshipModel>> natureRelationshipsEntrySet = referencedNatureRelationships
        .entrySet();
    for (Entry<String, IGetReferencedNatureRelationshipModel> natureRelationshipEntrySet : natureRelationshipsEntrySet) {
      IGetReferencedNatureRelationshipModel natureRelationship = natureRelationshipEntrySet
          .getValue();
      klassInstanceRelationshipInstance.setRelationshipId(natureRelationship.getId());
      klassInstanceRelationshipInstance
          .setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RELATIONSHIP.getPrefix()));
      klassInstanceRelationshipInstance.setSideId(natureRelationship.getSide1()
          .getElementId());
      returnModel.getNatureRelationships()
          .add(klassInstanceRelationshipInstance);
    }
  }
  
  protected void handleRelationships(
      IBaseEntityDAO goldenRecordDAO, List<IRelationshipIdSourceModel> relationships,
      IGetConfigDetailsForCustomTabModel configDetails, List<String> sourceIds, boolean newSourceAdded) throws Exception
  {
    String baseEntityIID = String.valueOf(goldenRecordDAO.getBaseEntityDTO().getBaseEntityIID());
    List<IRelationshipIdSourceModel> relationsToProcess = new ArrayList<>();
    
    for(IRelationshipIdSourceModel relationship : relationships) {
      if(relationship.getSourceKlassInstanceId().isEmpty() || !relationship.getSourceKlassInstanceId().equals(baseEntityIID)) {
        relationsToProcess.add(relationship);
      }
    }
    
    IGetConfigDetailsForSaveRelationshipInstancesResponseModel relationshipConfigDetails = goldenRecordUtils
        .prepareRelationshipConfigDetails(configDetails);
    
    RelationshipRecordBuilder relationshipRecordBuilder = new RelationshipRecordBuilder(
        goldenRecordDAO, relationshipConfigDetails, this.rdbmsComponentUtils);
    
    List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel = new ArrayList<>();
    
    if(newSourceAdded) {
      sourceIds.remove(String.valueOf(goldenRecordDAO.getBaseEntityDTO().getBaseEntityIID()));

      goldenRecordUtils.createStandardArticleGoldenArticleRelationships(sourceIds, configDetails, relationshipDataTransferModel, relationshipRecordBuilder,
          goldenRecordDAO);
    }
    
    if (!relationsToProcess.isEmpty()) {
      
      Map<String, List<Long>> relationIdVsDeletedElements = new HashMap<>();
      Map<String, List<Long>> relationIdVsAddedElements = new HashMap<>();

      deleteRelationships(relationsToProcess, rdbmsComponentUtils.getLocaleCatlogDAO(),
          goldenRecordDAO, relationshipRecordBuilder, relationshipDataTransferModel, relationIdVsDeletedElements);
      
      goldenRecordUtils.cloneSelectedRelationships(relationsToProcess, rdbmsComponentUtils.getLocaleCatlogDAO(),
          goldenRecordDAO, relationshipDataTransferModel, relationIdVsAddedElements);
      
      String userId = transactionThread.getTransactionData().getUserId();
      goldenRecordUtils.prepareDataForRelationshipDataTransfer(goldenRecordDAO.getBaseEntityDTO(), relationsToProcess, userId, relationIdVsAddedElements, relationIdVsDeletedElements);
      dataTransferModelBuilder.prepareDataForRelationshipInheritance(relationshipDataTransferModel, goldenRecordDAO.getBaseEntityDTO().getBaseEntityIID(), false, ApplicationActionType.TRANSFER, goldenRecordDAO);
    }
  }
  
  private void deleteRelationships(List<IRelationshipIdSourceModel> relationships,
      ILocaleCatalogDAO localeCatlogDAO, IBaseEntityDAO goldenRecordDAO,
      RelationshipRecordBuilder relationshipRecordBuilder,
      List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel,
      Map<String, List<Long>> relationIdVsDeletedElements) throws Exception
  {
    List<IPropertyRecordDTO> relationshipPropertyRecords = new ArrayList<>();
    String goldenRecordIId = String.valueOf(goldenRecordDAO.getBaseEntityDTO()
        .getBaseEntityIID());
    
    for (IRelationshipIdSourceModel relationship : relationships) {
      IContentRelationshipInstanceModel modifiedRelationship = new ContentRelationshipInstanceModel();
      
      String relationshipId = relationship.getRelationshipId();
      String sideId = relationship.getSideId();
      IPropertyDTO relationshipProperty = ConfigurationDAO.instance().getPropertyByCode(relationshipId);

      // Delete all existing relationships against golden record article
      List<Long> deletedElements = localeCatlogDAO
          .getOtherSideInstanceIIds(CommonConstants.RELATIONSHIP_SIDE_1, goldenRecordIId,
              relationshipProperty.getPropertyIID());
      
      if(relationshipProperty.getPropertyType() == PropertyType.NATURE_RELATIONSHIP) {
        relationIdVsDeletedElements.put(PropertyType.NATURE_RELATIONSHIP.name() + "__" + relationshipId, deletedElements);
      }
      else {
        relationIdVsDeletedElements.put(PropertyType.RELATIONSHIP.name() + "__" + relationshipId, deletedElements);
      }
      
      modifiedRelationship.setRelationshipId(relationshipId);
      modifiedRelationship.setSideId(sideId);
      modifiedRelationship.setDeletedElements(deletedElements.stream().map(String::valueOf).collect(Collectors.toList()));
      
      IPropertyRecordDTO relationshipRecord = relationshipRecordBuilder
          .updateRelationshipRecord(modifiedRelationship, relationshipDataTransferModel);
      
      if (relationshipRecord != null) {
        relationshipPropertyRecords.add(relationshipRecord);
      }
    }
    
    if (!relationshipPropertyRecords.isEmpty()) {
      goldenRecordDAO.updatePropertyRecords(relationshipPropertyRecords.toArray(new IPropertyRecordDTO[0]));
    }
  }
  
  private IModifiedContentTagInstanceModel prepareModifiedTagInstanceFromConflictingValue(
      ITagConflictingValue tagConflictingValue)
  {
    List<IIdRelevance> conflictingTagValues = tagConflictingValue.getTagValues();
    List<ITagInstanceValue> addedTagValues = new ArrayList<>();
    List<ITagInstanceValue> modifiedTagValues = new ArrayList<>();
    List<String> deletedTagValues = new ArrayList<>();
    
    conflictingTagValues.forEach(conflictingTagValue -> {
      
      ITagInstanceValue tagInstanceValue = new TagInstanceValue();
      tagInstanceValue.setId(conflictingTagValue.getId());
      tagInstanceValue.setTagId(conflictingTagValue.getTagId());
      tagInstanceValue.setCode(conflictingTagValue.getCode());
      tagInstanceValue.setRelevance(conflictingTagValue.getRelevance());

      if (!conflictingTagValue.getId().equals(conflictingTagValue.getTagId())) {
        // When a tag value newly added to tag, UI generates random id
        addedTagValues.add(tagInstanceValue);
      }
      else {
        // When a tag value is modified or deleted, UI sends similar id as tagId
        // So depending upon relevance we can decide whether tag value is
        // deleted or updated
        if (conflictingTagValue.getRelevance() == 0) {
          deletedTagValues.add(conflictingTagValue.getCode());
        }
        else {
          modifiedTagValues.add(tagInstanceValue);
        }
      }
    });
    
    IModifiedContentTagInstanceModel modifiedTagInstance = new ModifiedTagInstanceModel();
    modifiedTagInstance.setAddedTagValues(addedTagValues);
    modifiedTagInstance.setModifiedTagValues(modifiedTagValues);
    modifiedTagInstance.setDeletedTagValues(deletedTagValues);
    
    return modifiedTagInstance;
  }
  
}
