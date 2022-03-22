package com.cs.core.runtime.instancetree;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.standard.IStandardConfig.StandardProperty;
import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.EntityRelationshipInfoDTO;
import com.cs.core.bgprocess.dto.EvaluateGoldenRecordServiceDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IEntityRelationshipInfoDTO;
import com.cs.core.bgprocess.idto.IEvaluateGoldenRecordBucketServiceDTO;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.entity.relationship.IRelationshipInstance;
import com.cs.core.config.interactor.entity.relationship.RelationshipInstance;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.IdRelevance;
import com.cs.core.config.interactor.model.goldenrecord.GetConfigDetailsForComparisonRequestModel;
import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForGetKlassInstancesToMergeModel;
import com.cs.core.config.interactor.model.goldenrecord.IGetConfigDetailsForComparisonRequestModel;
import com.cs.core.config.interactor.model.goldenrecord.IMatchPropertiesModel;
import com.cs.core.config.interactor.model.goldenrecord.MatchPropertiesModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionRelationshipModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.data.Text;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.coupling.dto.BGPCouplingDTO;
import com.cs.core.rdbms.coupling.dto.RelationshipDataTransferDTO;
import com.cs.core.rdbms.coupling.idto.IBGPCouplingDTO;
import com.cs.core.rdbms.coupling.idto.IRelationshipDataTransferDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dto.GoldenRecordDTO;
import com.cs.core.rdbms.entity.dto.TagsRecordDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IGRBucketAttributeDTO;
import com.cs.core.rdbms.entity.idto.IGRBucketTagDTO;
import com.cs.core.rdbms.entity.idto.IGoldenRecordBucketDTO;
import com.cs.core.rdbms.entity.idto.IGoldenRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.goldenrecordbucket.dao.GoldenRecordBucketDAO;
import com.cs.core.rdbms.goldenrecordbucket.idao.IGoldenRecordBucketDAO;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.entity.datarule.AttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.datarule.IAttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.datarule.IConflictingValue;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRelationshipVersion;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.relationship.RelationshipVersion;
import com.cs.core.runtime.interactor.entity.tag.ITagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.exception.goldenrecord.MultipleGoldenRecordsFoundInBucket;
import com.cs.core.runtime.interactor.model.configdetails.GetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.goldenrecord.AttributeRecommendationModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IAttributeRecommendationModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGoldenRecordRuleKlassInstancesMergeViewResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IRecommendationModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IRelationshipIdSourceModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IRelationshipRecommendationModel;
import com.cs.core.runtime.interactor.model.goldenrecord.ITagRecommendationModel;
import com.cs.core.runtime.interactor.model.goldenrecord.RelationshipIdSourceModel;
import com.cs.core.runtime.interactor.model.goldenrecord.TagRecommendationModel;
import com.cs.core.runtime.interactor.model.instance.ContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInfoModel;
import com.cs.core.runtime.interactor.model.relationship.RelationshipDataTransferInfoModel;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCreateKlassInstanceCloneModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RelationshipRecordBuilder;
import com.cs.core.runtime.strategy.usecase.goldenrecord.IGetConfigDetailsForGetKlassInstancesToMergeStrategy;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.util.ConfigUtil;
import com.cs.utils.BaseEntityUtils;

@Component
public class GoldenRecordUtils {
  
  @Autowired
  protected ISessionContext                                      context;
  
  @Autowired
  protected ConfigUtil                                           configUtil;
  
  @Autowired
  protected RDBMSComponentUtils                                  rdbmsComponentUtils;
  
  @Autowired
  protected IGetConfigDetailsForGetKlassInstancesToMergeStrategy getConfigDetailsForGetKlassInstancesToMergeStrategy;
  
  private static final String                                    EVALUATE_GOLDEN_RECORD_SERVICE                                  = "EVALUATE_GOLDEN_RECORD_SERVICE";
  
  private static final String                                    RELATIONSHIP_DATA_TRANSFER_SERVICE                              = "RELATIONSHIP_DATA_TRANSFER";
  
  private static final String                                    SEPERATOR                                                       = "__";
  
  public static final String                                     SUPPLIER_TAGS                                                   = "supplierTags";
  public static final String                                     SUPPLIER_ATTRIBUTES                                             = "supplierAttributes";
  public static final String                                     SUPPLIER_DEPENDENT_ATTRIBUTES                                   = "supplierDependentAttributes";
  
  public static final String                                     LAST_MODIFIED_TAGS                                              = "lastModifiedTags";
  public static final String                                     LAST_MODIFIED_ATTRIBUTES                                        = "lastModifiedAttributes";
  public static final String                                     LAST_MODIFIED_DEPENDENT_ATTRIBUTES                              = "lastModifiedDependentAttributes";
  
  public static final String                                     DEPENDENT_ATTRIBUTES_RECOMMENDATION                             = "dependentAttributesRecommendation";
  
  public IRelationshipInstance getRelationshipElementInstance(IRelationsSetDTO relationsSetDTO,
      IReferencedSectionRelationshipModel referencedElement, IBaseEntityDTO baseEntityDTO,
      IBaseEntityDTO side2BaseEntityDTO)
  {
    IRelationshipInstance relationshipInstance = new RelationshipInstance();
    relationshipInstance.setId(relationsSetDTO.getProperty().getPropertyCode());
    relationshipInstance.setRelationshipId(relationsSetDTO.getProperty().getPropertyCode());
    relationshipInstance.setSide1InstanceId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    relationshipInstance.setSide1BaseType(BaseEntityUtils.getBaseTypeString(baseEntityDTO.getBaseType()));
    relationshipInstance.setSide2InstanceId(String.valueOf(side2BaseEntityDTO.getBaseEntityIID()));
    relationshipInstance.setSide2BaseType(BaseEntityUtils.getBaseTypeString(side2BaseEntityDTO.getBaseType()));
    relationshipInstance.setSideId(referencedElement.getId());
    
    return relationshipInstance;
  }
  
  public void cloneSelectedRelationships(List<IRelationshipIdSourceModel> relationships,
      ILocaleCatalogDAO localeCatlogDAO, IBaseEntityDAO goldenRecordDAO,
      List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel,
      Map<String, List<Long>> relationIdVsAddedElements) throws Exception
  {
    Map<String, List<String>> sourceIdVsrelationshipIds = prepareSourceIdVsrelationshipIdsMap(relationships);
    List<IPropertyDTO> clonedProperties = new ArrayList<>();
    
    for (String sourceId : sourceIdVsrelationshipIds.keySet()) {
      
      List<IPropertyDTO> propertiesTobeCloned = new ArrayList<>();
      Long entityIid = Long.parseLong(sourceId);
      List<String> sourceRelationshipIds = sourceIdVsrelationshipIds.get(sourceId);
      
      for (String relationshipId : sourceRelationshipIds) {
        propertiesTobeCloned.add(RDBMSUtils.getPropertyByCode(relationshipId));
      }
      
      IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(entityIid);

      IBaseEntityDTO baseEntityDTO = baseEntityDAO.loadAllPropertyRecords(propertiesTobeCloned.toArray(new IPropertyDTO[0]));
      
      ((LocaleCatalogDAO)localeCatlogDAO).cloneProperties(baseEntityDAO, goldenRecordDAO, "", null, propertiesTobeCloned.toArray(new IPropertyDTO[0]));
      List<IPropertyRecordDTO> relations = baseEntityDTO.getPropertyRecords()
                                                        .stream()
                                                        .filter(record -> record.getProperty().getSuperType() == SuperType.RELATION_SIDE)
                                                        .collect(Collectors.toList());
      
      fillDataForRelationshipInheritance(relationshipDataTransferModel, relations, relationIdVsAddedElements);
      clonedProperties.addAll(propertiesTobeCloned);
    }
    
    if(!clonedProperties.isEmpty()) {
      goldenRecordDAO.loadAllPropertyRecords(clonedProperties.toArray(new IPropertyDTO[0]));
    }
  }
  
  private void fillDataForRelationshipInheritance(List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel,
       List<IPropertyRecordDTO> propertyRecords, Map<String, List<Long>> relationIdVsAddedElements)
  {
    for(IPropertyRecordDTO propertyRecord : propertyRecords) {

      IRelationsSetDTO relationsSetDTO = (IRelationsSetDTO) propertyRecord;
      Boolean isNature = relationsSetDTO.getProperty().getPropertyType() == PropertyType.NATURE_RELATIONSHIP;
      String relationshipId = relationsSetDTO.getProperty().getCode();
      Long[] otherSideEntityIIDs = relationsSetDTO.getReferencedBaseEntityIIDs();
      RelationSide relationSide = relationsSetDTO.getProperty().getRelationSide();
      
      IRelationshipDataTransferInfoModel dataTransferModel = new RelationshipDataTransferInfoModel();
      dataTransferModel.setContentId(relationsSetDTO.getEntityIID());
    
      if (isNature) {
        relationIdVsAddedElements.put(PropertyType.NATURE_RELATIONSHIP.name() + SEPERATOR + relationshipId, Arrays.asList(otherSideEntityIIDs));
        dataTransferModel.getChangedNatureRelationshipIds().add(relationshipId);
      }
      else {
        relationIdVsAddedElements.put(PropertyType.RELATIONSHIP.name() + SEPERATOR + relationshipId, Arrays.asList(otherSideEntityIIDs));
        dataTransferModel.getChangedRelationshipIds().add(relationshipId);
      }
  
      Map<String, IEntityRelationshipInfoDTO> relationshipIdAddedDeletedElementsMap = dataTransferModel
          .getRelationshipIdAddedDeletedElementsMap();
      IEntityRelationshipInfoDTO addedRemovedElements = relationshipIdAddedDeletedElementsMap
          .get(relationsSetDTO.getProperty().getPropertyIID() + SEPERATOR + relationSide);
     
      if (addedRemovedElements == null) {
        addedRemovedElements = new EntityRelationshipInfoDTO();
        relationshipIdAddedDeletedElementsMap.put(relationshipId + SEPERATOR + relationSide, addedRemovedElements);
      }
      
      addedRemovedElements.getAddedElements().addAll(Arrays.asList(otherSideEntityIIDs));
      relationshipDataTransferModel.add(dataTransferModel);
    }
  }
  
  private Map<String, List<String>> prepareSourceIdVsrelationshipIdsMap(List<IRelationshipIdSourceModel> relationships)
  {
    Map<String, List<String>> sourceIdVsrelationshipIds = new HashMap<>();
    for (IRelationshipIdSourceModel relationship : relationships) {
      String sourceId = relationship.getSourceKlassInstanceId();
      
      if (sourceId.isEmpty()) {
        continue;
      }
      
      if (sourceIdVsrelationshipIds.containsKey(sourceId)) {
        sourceIdVsrelationshipIds.get(sourceId).add(relationship.getRelationshipId());
      }
      else {
        List<String> sourceRelationshipIds = new ArrayList<>();
        sourceRelationshipIds.add(relationship.getRelationshipId());
        sourceIdVsrelationshipIds.put(sourceId, sourceRelationshipIds);
      }
    }
    return sourceIdVsrelationshipIds;
  }
  
  public void initiateEvaluateGoldenRecordBucket(IBaseEntityDTO baseEntityDTO, boolean isBaseEntityDeleted) throws RDBMSException, CSFormatException
  {
    if (!baseEntityDTO.getBaseType().equals(BaseType.ARTICLE)) {
      return;
    }
    
    IEvaluateGoldenRecordBucketServiceDTO evaluateGoldenRecordServiceDTO = new EvaluateGoldenRecordServiceDTO();
    evaluateGoldenRecordServiceDTO.setBaseEntityIID(baseEntityDTO.getBaseEntityIID());
    evaluateGoldenRecordServiceDTO.setIsBaseEntityDeleted(isBaseEntityDeleted);
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.MEDIUM;
    BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), EVALUATE_GOLDEN_RECORD_SERVICE, "", userPriority,
        new JSONContent(evaluateGoldenRecordServiceDTO.toJSON()));
  }
  
  public void initiateEvaluateGoldenRecordBucket(IBaseEntityDTO baseEntityDTO) throws Exception
  {
    initiateEvaluateGoldenRecordBucket(baseEntityDTO, false);
  }

  public void handleDefaultImage(IBaseEntityDAO goldenRecordDAO) throws RDBMSException
  {
    IBaseEntityDTO baseEntityDTO = goldenRecordDAO.getBaseEntityDTO();
    
    if(baseEntityDTO.getDefaultImageIID() == 0l) {
      Long newDefaultImage = goldenRecordDAO.getNewDefaultImage();
      if(newDefaultImage != 0l) {
        baseEntityDTO.setDefaultImageIID(newDefaultImage);
      }
    }
    
    if(baseEntityDTO.getDefaultImageIID() != 0l) {
      goldenRecordDAO.updateBaseEntity(context.getUserSessionDTO().getUserIID()); 
    }
  }
  
  public List<IPropertyRecordDTO> createAttributePropertyRecordInstance(
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
  
  public void preparePropertyRecordsFromAttributes(PropertyRecordBuilder propertyRecordBuilder, List<IPropertyRecordDTO> valueRecords,
      String localeId, Map<String, List<IConflictingValue>> attributes) throws Exception
  {
    for (String attributeId : attributes.keySet()) {
      IAttributeConflictingValue value = (IAttributeConflictingValue) attributes.get(attributeId).get(0);
      IAttributeInstance attribute = prepareAttributeInstanceFromConflictingValue(value, attributeId);
      attribute.setLanguage(localeId);
      IPropertyRecordDTO updatedValueRecordDTO = propertyRecordBuilder.updateValueRecord(attribute);
      valueRecords.add(updatedValueRecordDTO);
    }
  }
  
  public IAttributeInstance prepareAttributeInstanceFromConflictingValue(IAttributeConflictingValue attributeConflictingValue,
      String attributeId)
  {
    IAttributeInstance attribute = new AttributeInstance();
    attribute.setAttributeId(attributeId);
    attribute.setValue(attributeConflictingValue.getValue());
    attribute.setValueAsHtml(attributeConflictingValue.getValueAsHtml());
    return attribute;
  }
  
  public IGetConfigDetailsForSaveRelationshipInstancesResponseModel prepareRelationshipConfigDetails(
      IGetConfigDetailsForCustomTabModel configDetails) throws RDBMSException, SQLException
  {
    IGetConfigDetailsForSaveRelationshipInstancesResponseModel relationshipConfigDetails = new GetConfigDetailsForSaveRelationshipInstancesResponseModel();
    
    relationshipConfigDetails.setReferencedElements(configDetails.getReferencedElements());
    relationshipConfigDetails.setReferencedNatureRelationships(configDetails.getReferencedNatureRelationships());
    relationshipConfigDetails.setReferencedRelationships(configDetails.getReferencedRelationships());
    relationshipConfigDetails.setReferencedRelationshipProperties(configDetails.getReferencedRelationshipProperties());
    relationshipConfigDetails.setReferencedTags(configDetails.getReferencedTags());
    relationshipConfigDetails.setSide2LinkedVariantKrIds(configDetails.getSide2LinkedVariantKrIds());
    relationshipConfigDetails.setReferencedVariantContexts(configDetails.getReferencedVariantContexts());
    
    List<String> linkedVariantCodes = configDetails.getLinkedVariantCodes();
    if (!linkedVariantCodes.isEmpty()) {
      List<Long> propertyIids = ConfigurationDAO.instance().getPropertyIIDsFromPropertyCodes(linkedVariantCodes);
      relationshipConfigDetails.setLinkedVariantPropertyIids(propertyIids);
    }
    
    return relationshipConfigDetails;
  }
  
  public void createStandardArticleGoldenArticleRelationships(List<String> sourceIds, IGetConfigDetailsForCustomTabModel configDetails,
      List<IRelationshipDataTransferInfoModel> relationshipDataTransferModel, RelationshipRecordBuilder relationshipRecordBuilder,
      IBaseEntityDAO baseEntityDAO) throws Exception
  {
    
    IReferencedRelationshipModel referencedGoldenRecordArticleRelationship = configDetails.getReferencedRelationships()
        .get(SystemLevelIds.STANDARD_ARTICLE_GOLDEN_ARTICLE_RELATIONSHIP_ID);
    
    IContentRelationshipInstanceModel relationshipModel = new ContentRelationshipInstanceModel();
    relationshipModel.setId(referencedGoldenRecordArticleRelationship.getId());
    relationshipModel.setRelationshipId(referencedGoldenRecordArticleRelationship.getCode());
    relationshipModel.setSideId(referencedGoldenRecordArticleRelationship.getSide2().getElementId());
    relationshipModel.setBaseType(Constants.ARTICLE_INSTANCE_BASE_TYPE);
    List<IPropertyRecordDTO> relationshipPropertyRecords = new ArrayList<>();
    
    for (String sourceid : sourceIds) {
      List<IRelationshipVersion> addedElements = new ArrayList<>();
      IRelationshipVersion relationshipVersion = new RelationshipVersion();
      relationshipVersion.setId(sourceid);
      relationshipVersion.setCount(1);// need to remove check
      addedElements.add(relationshipVersion);
      relationshipModel.setAddedElements(addedElements);
      IPropertyRecordDTO relationshipRecord = relationshipRecordBuilder.updateRelationshipRecord(relationshipModel,
          relationshipDataTransferModel);
      
      if (relationshipRecord != null) {
        relationshipPropertyRecords.add(relationshipRecord);
      }
    }
    
    baseEntityDAO.createPropertyRecords(relationshipPropertyRecords.toArray(new IPropertyRecordDTO[relationshipPropertyRecords.size()]));
    
    Set<IPropertyDTO> properties = (Set<IPropertyDTO>) rdbmsComponentUtils.getLocaleCatlogDAO()
        .getAllEntityProperties(baseEntityDAO.getBaseEntityDTO().getBaseEntityIID());
    baseEntityDAO.loadPropertyRecords(properties.toArray(new IPropertyDTO[0]));
  }
  
  public void prepareDataForRelationshipDataTransfer(IBaseEntityDTO baseEntityDTO,
      List<IRelationshipIdSourceModel> relations, String userId,
      Map<String, List<Long>> relationIdVsAddedElements,
      Map<String, List<Long>> relationIdVsDeletedElements) throws RDBMSException, CSFormatException
  {
    List<IBGPCouplingDTO> bgpCouplingDTOs = new ArrayList<>();
    long baseEntityIID = baseEntityDTO.getBaseEntityIID();
   
    for (IRelationshipIdSourceModel relation : relations) {
      IBGPCouplingDTO bgpCouplingDTO = new BGPCouplingDTO();
      String id = relation.getRelationshipId();
      Boolean isNature = getIsNature(id, relationIdVsAddedElements, relationIdVsDeletedElements);
     
      List<Long> addedElements = null;
      List<Long> deletedElements = null;
      
      if(isNature) {
        bgpCouplingDTO.setNatureRelationshipId(id);

        addedElements = relationIdVsAddedElements.get(PropertyType.NATURE_RELATIONSHIP.name() + SEPERATOR + id);
        deletedElements = relationIdVsDeletedElements.get(PropertyType.NATURE_RELATIONSHIP.name() + SEPERATOR + id);
      }
      
      else {
        bgpCouplingDTO.setRelationshipId(relation.getRelationshipId());
       
        addedElements = relationIdVsAddedElements.get(PropertyType.RELATIONSHIP.name() + SEPERATOR + id);
        deletedElements = relationIdVsDeletedElements.get(PropertyType.RELATIONSHIP.name() + SEPERATOR + id);
      }
      
      bgpCouplingDTO.setAddedEntityIIDs(addedElements != null ? addedElements : new ArrayList<>());
      bgpCouplingDTO.setDeletedEntityIIDs(deletedElements != null ? deletedElements : new ArrayList<>());
      bgpCouplingDTO.setSourceBaseEntityIIDs(Arrays.asList(baseEntityIID));
      bgpCouplingDTO.setSideId(relation.getSideId());
      
      bgpCouplingDTOs.add(bgpCouplingDTO);
    }
    
    if(!bgpCouplingDTOs.isEmpty()) {
      IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.HIGH;
      IRelationshipDataTransferDTO entryData = new RelationshipDataTransferDTO();
      entryData.setBGPCouplingDTOs(bgpCouplingDTOs);
      entryData.setLocaleID(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID());
      entryData.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
      entryData.setOrganizationCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
      entryData.setUserId(userId);
      BGPDriverDAO.instance()
          .submitBGPProcess(rdbmsComponentUtils.getUserName(), RELATIONSHIP_DATA_TRANSFER_SERVICE, "", userPriority, new JSONContent(entryData.toJSON()));
    }
  }

  private Boolean getIsNature(String relationshipId,
      Map<String, List<Long>> relationIdVsAddedElements,
      Map<String, List<Long>> relationIdVsDeletedElements)
  {
    return relationIdVsAddedElements.get(PropertyType.NATURE_RELATIONSHIP.name() + SEPERATOR + relationshipId) != null
        || relationIdVsDeletedElements.get(PropertyType.NATURE_RELATIONSHIP.name() + SEPERATOR + relationshipId) != null;
  }

  public void addTypesAndTaxonomies(IGetConfigDetailsForCustomTabModel configDetails,
      List<String> klassIds, List<String> taxonomyIds, Set<IClassifierDTO> addedClassifiers, IBaseEntityDAO baseEntityDAO) throws RDBMSException
  {
    for (String klassId : klassIds) {
      IReferencedKlassDetailStrategyModel referencedKlass = configDetails.getReferencedKlasses().get(klassId);
      IClassifierDTO newClassifierDTO = baseEntityDAO.newClassifierDTO(referencedKlass.getClassifierIID(), referencedKlass.getCode(),
          IClassifierDTO.ClassifierType.CLASS);
      addedClassifiers.add(newClassifierDTO);
      baseEntityDAO.addClassifiers(newClassifierDTO);
    }
    
    for (String taxonomyId : taxonomyIds) {
      IReferencedArticleTaxonomyModel referencedTaxonomy = configDetails.getReferencedTaxonomies().get(taxonomyId);
      IClassifierDTO newClassifierDTO = baseEntityDAO.newClassifierDTO(referencedTaxonomy.getClassifierIID(), referencedTaxonomy.getCode(),
          IClassifierDTO.ClassifierType.TAXONOMY);
      addedClassifiers.add(newClassifierDTO);
      baseEntityDAO.addClassifiers(newClassifierDTO);
    }
  }
  
  public IGoldenRecordDTO getGoldenRecordDTO(String bucketId, String goldenRecordId)
      throws Exception
  {
    GoldenRecordBucketDAO dao = new GoldenRecordBucketDAO();
    return dao.getGoldenRecordRuleAndBaseEntityIIDs(bucketId, goldenRecordId);
  }
  
  public Map<String, Map<String, List<IPropertyRecordDTO>>> getPropertyVsPropertyRecordList(
      Map<Long, List<IPropertyRecordDTO>> iidVsPropertyRecordDTO)
  {
    Map<String, Map<String, List<IPropertyRecordDTO>>> returnMap = new HashMap<String, Map<String, List<IPropertyRecordDTO>>>();
    
    for (Map.Entry<Long, List<IPropertyRecordDTO>> entry : iidVsPropertyRecordDTO.entrySet()) {
      
      Map<String, List<IPropertyRecordDTO>> propertyVsPropertyRecordList = new HashMap<String, List<IPropertyRecordDTO>>();
      returnMap.put(String.valueOf(entry.getKey()), propertyVsPropertyRecordList);
      
      List<IPropertyRecordDTO> propertyRecordList = entry.getValue();
      
      for (IPropertyRecordDTO propertyRecord : propertyRecordList) {
        String propertyCode = propertyRecord.getProperty().getPropertyCode();
        
        if (propertyVsPropertyRecordList.containsKey(propertyCode)) {
          propertyVsPropertyRecordList.get(propertyCode).add(propertyRecord);
        }
        else {
          propertyVsPropertyRecordList.put(propertyCode, Stream.of(propertyRecord).collect(Collectors.toList()));
        }
      }
    }
    return returnMap;
  }
  
  public Map<Long, List<IPropertyRecordDTO>> getPropertyRecord(IPropertyDTO property,
      Long... baseEntityIIDs) throws RDBMSException
  {
    Map<Long, List<IPropertyRecordDTO>> iidVSPropertyRecords = new HashMap<>();
    
    String tableName = "";
    switch (property.getPropertyType()
        .getSuperType()) {
      case ATTRIBUTE:
        tableName = "pxp.allvaluerecord";
        break;
      case TAGS:
        tableName = "pxp.alltagsrecord";
        break;
      default:
        break;
    }
    
    if (tableName.isBlank()) {
      return iidVSPropertyRecords;
    }
    
    String GET_ALL_RECORDS = "SELECT * FROM %TABLE_NAME% WHERE entityiid IN (%s) ";
    String query = String.format(GET_ALL_RECORDS.replace("%TABLE_NAME%", tableName),
        Text.join(",", baseEntityIIDs)) + " and propertyiid = " + property.getPropertyIID();
    
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          PreparedStatement stmt = currentConn.prepareStatement(query);
          IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());
          
          while (result.next()) {
            
            IPropertyRecordDTO propertyRecordDTO = null;
            switch (property.getPropertyType().getSuperType()) {
              
              case ATTRIBUTE:
                propertyRecordDTO = new ValueRecordDTO(result, property);
                break;
              
              case TAGS:
                propertyRecordDTO = new TagsRecordDTO(result, property);
                break;
              
              default:
                break;
            }
            
            long entityIID = result.getLong("entityiid");
            List<IPropertyRecordDTO> propertyRecords = iidVSPropertyRecords.get(entityIID);
            if (propertyRecords == null) {
              propertyRecords = new ArrayList<>();
              iidVSPropertyRecords.put(entityIID, propertyRecords);
            }
            propertyRecords.add(propertyRecordDTO);
          }
        });
    return iidVSPropertyRecords;
  }
  
  public void getKlassIdsAndTaxonomyIdsList(IBaseEntityDTO baseEntityDTO, Set<String> klassIds, Set<String> taxonomyIds)
  {
    klassIds.add(baseEntityDTO.getNatureClassifier().getCode());
    for (IClassifierDTO otherClassifierDTO : baseEntityDTO.getOtherClassifiers()) {
      if (otherClassifierDTO.getClassifierType().equals(IClassifierDTO.ClassifierType.CLASS)) {
        klassIds.add(otherClassifierDTO.getCode());
      }
      else {
        taxonomyIds.add(otherClassifierDTO.getCode());
      }
    }
  }
  
  public void fillMatchPropertiesModelAndRule(IGoldenRecordRuleKlassInstancesMergeViewResponseModel responseModel, String bucketInstanceId)
      throws RDBMSException, Exception, MultipleGoldenRecordsFoundInBucket
  {
    IMatchPropertiesModel matchPropertiesModel = new MatchPropertiesModel();
    String ruleId = new String();
    
    if (bucketInstanceId != null) {
      IGoldenRecordBucketDAO goldenRecordBucketDAO = new GoldenRecordBucketDAO();
      IGoldenRecordBucketDTO goldenRecordBucketDTO = goldenRecordBucketDAO.getGoldenRecordBucketById(Long.parseLong(bucketInstanceId));
      
      List<IAttributeInstance> attributes = new ArrayList<>();
      for (IGRBucketAttributeDTO bucketAttribute : goldenRecordBucketDTO.getBucketAttributes()) {
        IAttributeInstance attributeInstance = new AttributeInstance();
        attributeInstance.setAttributeId(bucketAttribute.getAttributeId());
        attributeInstance.setValue(bucketAttribute.getValue());
        attributes.add(attributeInstance);
      }
      List<ITagInstance> tags = new ArrayList<>();
      for (IGRBucketTagDTO bucketTag : goldenRecordBucketDTO.getBucketTags()) {
        ITagInstance tagInstance = new TagInstance();
        tagInstance.setTagId(bucketTag.getTagId());
        tagInstance.setId(bucketTag.getTagId());
        List<String> tagValueCodes = bucketTag.getTagValueCodes();
        List<ITagInstanceValue> tagInstanceValues = new ArrayList<>();
        for (String tagValueCode : tagValueCodes) {
          ITagInstanceValue tagInstanceValue = new TagInstanceValue();
          tagInstanceValue.setId(tagValueCode);
          tagInstanceValue.setTagId(tagValueCode);
          tagInstanceValue.setRelevance(100);
          tagInstanceValues.add(tagInstanceValue);
        }
        tagInstance.setTagValues(tagInstanceValues);
        tags.add(tagInstance);
      }
      matchPropertiesModel.setAttributes(attributes);
      matchPropertiesModel.setTags(tags);
      ruleId = goldenRecordBucketDTO.getRuleId();
    }
    
    responseModel.setRuleId(ruleId);
    responseModel.setMatchPropertiesModel(matchPropertiesModel);
  }
  
  public IGoldenRecordDTO prepareGoldenRecordDTO(IGoldenRecordRule goldenRecordRule, Set<Long> baseEntityIids) throws RDBMSException
  {
    ILocaleCatalogDTO localeCatalogDTO = rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO();
    IGoldenRecordDTO goldenRecordDTO = new GoldenRecordDTO();
    goldenRecordDTO.setRuleId(goldenRecordRule.getId());
    goldenRecordDTO.setLinkedBaseEntities(new ArrayList<Long>(baseEntityIids));
    goldenRecordDTO.setCatalogCode(localeCatalogDTO.getCatalogCode());
    goldenRecordDTO.setLocaleID(localeCatalogDTO.getLocaleID());
    goldenRecordDTO.setOrganizationCode(localeCatalogDTO.getOrganizationCode());
    goldenRecordDTO.setUserId(context.getUserId());
    return goldenRecordDTO;
  }
  
  public IConfigDetailsForGetKlassInstancesToMergeModel getConfigDetails(String ruleId, List<String> taxonomyIds, List<String> types,
      List<String> languageCodes) throws Exception
  {
    IGetConfigDetailsForComparisonRequestModel requestModel = new GetConfigDetailsForComparisonRequestModel();
    requestModel.setSelectedTaxonomyIds(taxonomyIds);
    requestModel.setKlassIds(types);
    requestModel.setRuleId(ruleId);
    requestModel.setLanguageCodes(languageCodes);
    IConfigDetailsForGetKlassInstancesToMergeModel configDetails = getConfigDetailsForGetKlassInstancesToMergeStrategy
        .execute(requestModel);
    return configDetails;
  }
  
  public IGetConfigDetailsModel prepareConfigRequestmodelForKlassInstanceBuilder(
      IConfigDetailsForGetKlassInstancesToMergeModel configDetails)
  {
    IGetConfigDetailsModel config = new GetConfigDetailsForCreateKlassInstanceCloneModel();
    config.setReferencedAttributes(configDetails.getReferencedAttributes());
    config.setReferencedElements(configDetails.getReferencedElements());
    config.setReferencedTags(configDetails.getReferencedTags());
    config.setReferencedTaxonomies(configDetails.getReferencedTaxonomies());
    return config;
  }

  public void prepareHelperMap(Map<String, Object> helperMap)
  {
    helperMap.put(SUPPLIER_TAGS, new HashMap<String, List<ITagRecommendationModel>>());
    helperMap.put(SUPPLIER_ATTRIBUTES, new HashMap<String, List<IAttributeRecommendationModel>>());
    helperMap.put(SUPPLIER_DEPENDENT_ATTRIBUTES, new HashMap<String, Map<String, List<IAttributeRecommendationModel>>>());
    
    helperMap.put(LAST_MODIFIED_TAGS, new HashMap<String, List<ITagRecommendationModel>>());
    helperMap.put(LAST_MODIFIED_ATTRIBUTES, new HashMap<String, List<IAttributeRecommendationModel>>());
    helperMap.put(LAST_MODIFIED_DEPENDENT_ATTRIBUTES, new HashMap<String, Map<String, List<IAttributeRecommendationModel>>>());
    
    helperMap.put(DEPENDENT_ATTRIBUTES_RECOMMENDATION, new HashMap<String, Map<String, IRecommendationModel>>());
  }
  
  public IAttributeRecommendationModel getRecommendedAttribute(IContentAttributeInstance attribute,
      String supplierId, String klassInstanceId) {
    IAttributeInstance attributeInstance = (IAttributeInstance) attribute;
    String attributeId = attribute.getAttributeId();
    String value = attributeInstance.getValue();
    String valueAsHtml = attributeInstance.getValueAsHtml();
    List<IConcatenatedOperator> valueAsExpression = attributeInstance.getValueAsExpression();
    Long lastModified = (Long) attribute.getLastModified();

    IAttributeRecommendationModel attributeRecommendations = new AttributeRecommendationModel();
    attributeRecommendations.setValue(value);
    attributeRecommendations.setValueAsHtml(valueAsHtml);
    attributeRecommendations.setValueAsExpression(valueAsExpression);
    attributeRecommendations.setPropertyId(attributeId);
    attributeRecommendations.setPropertyType(CommonConstants.ATTRIBUTE);
    attributeRecommendations.setcontentId(klassInstanceId);
    attributeRecommendations.setSupplierId(supplierId);
    attributeRecommendations.setLastModified(lastModified);

    return attributeRecommendations;
  }
  
  public ITagRecommendationModel getRecommendedTag(IContentTagInstance tag, String supplierId,
      String klassInstanceId) {
    List<IIdRelevance> tagValues = getTagValueAsIdRelevence(tag);
    Long lastModified = (Long) tag.getLastModified();
    ITagRecommendationModel tagRecommendation = new TagRecommendationModel();
    tagRecommendation.setcontentId(klassInstanceId);
    tagRecommendation.setTagValues(tagValues);
    tagRecommendation.setPropertyType(CommonConstants.TAG);
    tagRecommendation.setcontentId(klassInstanceId);
    tagRecommendation.setSupplierId(supplierId);
    tagRecommendation.setLastModified(lastModified);

    return tagRecommendation;
  }
  
  private List<IIdRelevance> getTagValueAsIdRelevence(IContentTagInstance tag) {
    List<ITagInstanceValue> values = tag.getTagValues();
    List<IIdRelevance> newTagValues = new ArrayList<>();

    values.stream().filter(value -> !value.getRelevance().equals(0)).forEach(value -> {
      IIdRelevance idRelevence = new IdRelevance();
      idRelevence.setTagId(value.getTagId());
      idRelevence.setRelevance(value.getRelevance());
      newTagValues.add(idRelevence);
    });

    return newTagValues;
  }
  
  public void fillMatchedAttributesAndTagsConflictingMap(
      Map<String, List<IConflictingValue>> attributes, Map<String, List<IConflictingValue>> tags, IMatchPropertiesModel matchPropertiesModel)
  {
    List<IAttributeInstance> attributesInstances = matchPropertiesModel.getAttributes();
    List<ITagInstance> tagInstances = matchPropertiesModel.getTags();
    
    attributesInstances.stream().filter(attributesInstance -> !(attributesInstance.getAttributeId()
            .equals(SystemLevelIds.CREATED_ON_ATTRIBUTE)
            || attributesInstance.getAttributeId().equals(SystemLevelIds.LAST_MODIFIED_ATTRIBUTE)))
        .forEach(attributesInstance -> {

          List<IConflictingValue> conflictingValues = new ArrayList<>();
          IAttributeConflictingValue conflictingValue = new AttributeConflictingValue();
          conflictingValue.setId(attributesInstance.getAttributeId());
          conflictingValue.setValue(attributesInstance.getValue());
          conflictingValue.setValueAsHtml(attributesInstance.getValueAsHtml());
          conflictingValues.add(conflictingValue);
          attributes.put(attributesInstance.getAttributeId(), conflictingValues);
        
        });
    
    
    tagInstances.forEach(tagInstance -> {
      List<IConflictingValue> conflictingTagValues = new ArrayList<>();
      ITagConflictingValue conflictingTagValue = new TagConflictingValue();
      conflictingTagValue.setId(tagInstance.getTagId());
      List<IIdRelevance> tagValues = new ArrayList<>();
      List<ITagInstanceValue> tagInstanceValues = tagInstance.getTagValues();
      tagInstanceValues.forEach(tagInstanceValue -> {
        IIdRelevance idRelevance = new IdRelevance();
        idRelevance.setTagId(tagInstanceValue.getTagId());
        idRelevance.setRelevance(tagInstanceValue.getRelevance());
        idRelevance.setId(UUID.randomUUID().toString());
        tagValues.add(idRelevance);
      });
      
      conflictingTagValue.setTagValues(tagValues);
      conflictingTagValues.add(conflictingTagValue);
      tags.put(tagInstance.getTagId(), conflictingTagValues);
      
    });
  }
  
  public void fillRecommendedAttributesAndTagsConflictingMap(
      Map<String, List<IConflictingValue>> attributes, Map<String, List<IConflictingValue>> tags,
      List<IRelationshipIdSourceModel> relationshipIdSourceModels,
      IRecommendationModel recommendation, String id)
  {
    switch (recommendation.getPropertyType()) {
      case Constants.ATTRIBUTE:
        List<IConflictingValue> conflictingValues = new ArrayList<>();
        IAttributeConflictingValue conflictingValue = new AttributeConflictingValue();
        conflictingValue.setId(recommendation.getPropertyId());
        conflictingValue.setValue(((IAttributeRecommendationModel) recommendation).getValue());
        conflictingValue.setValueAsHtml(((IAttributeRecommendationModel) recommendation).getValueAsHtml());
        conflictingValues.add(conflictingValue);
        attributes.put(id, conflictingValues);
        break;
      case CommonConstants.TAG:
        List<IConflictingValue> conflictingTagValues = new ArrayList<>();
        ITagConflictingValue conflictingTagValue = new TagConflictingValue();
        conflictingTagValue.setId(recommendation.getPropertyId());
        List<IIdRelevance> tagValues = ((ITagRecommendationModel) recommendation).getTagValues();
        conflictingTagValue.setTagValues(tagValues);
        conflictingTagValues.add(conflictingTagValue);
        tags.put(id, conflictingTagValues);
        break;
      case CommonConstants.RELATIONSHIP:
        IRelationshipIdSourceModel sourceModel = new RelationshipIdSourceModel();
        IRelationshipRecommendationModel relationshipRecommendation = (IRelationshipRecommendationModel) recommendation;
        sourceModel.setRelationshipId(relationshipRecommendation.getRelationshipId());
        sourceModel.setSideId(relationshipRecommendation.getPropertyId());
        sourceModel.setSourceKlassInstanceId(relationshipRecommendation.getcontentId());
        relationshipIdSourceModels.add(sourceModel);
        break;
      default:
      
    }
  }
  
  public void fillRecommendedLanguageDependentAttributes(
      Map<String, Map<String, List<IConflictingValue>>> dependentAttributes,
     Map<String, Map<String, IRecommendationModel>> recommendedDependentAttributes)
  {
    if(recommendedDependentAttributes == null) {
      return;
    }
    recommendedDependentAttributes.forEach((languageCode, recommandedAttributes) -> {
      Map<String, List<IConflictingValue>> attributes = new HashMap<>();
       recommandedAttributes.forEach((id, recommendation) -> {
            List<IConflictingValue> conflictingValues = new ArrayList<>();
            IAttributeConflictingValue conflictingValue = new AttributeConflictingValue();
            conflictingValue.setId(((IAttributeRecommendationModel) recommendation).getPropertyId());
            conflictingValue.setValue(((IAttributeRecommendationModel) recommendation).getValue());
            conflictingValue.setValueAsHtml(((IAttributeRecommendationModel) recommendation).getValueAsHtml());
            conflictingValues.add(conflictingValue);
            attributes.put(id, conflictingValues);
          });
      dependentAttributes.put(languageCode, attributes);
    });
  }
  
  public void addNameAttribute(List<IPropertyRecordDTO> propertyRecordsDTO, PropertyRecordBuilder propertyRecordBuilder, 
      Long counter, List<String> languageCodes, String useCase, String natureKlassLabel, IAttribute attributeConfig,
      IReferencedSectionAttributeModel attributeElement)
      throws Exception
  {
    for (String languageCode : languageCodes) 
    {
      String name = useCase.equals("CREATE") ? natureKlassLabel + " " + counter : natureKlassLabel + languageCode + " " + counter;
      IValueRecordDTO valueRecordDTO = propertyRecordBuilder.buildValueRecord(0l, 0L, name, languageCode, null, attributeElement,
          attributeConfig, PropertyType.TEXT);
      
      propertyRecordsDTO.add(valueRecordDTO);
    }
  }
  
}

