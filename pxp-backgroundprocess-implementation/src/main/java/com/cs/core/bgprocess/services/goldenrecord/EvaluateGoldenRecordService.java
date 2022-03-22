package com.cs.core.bgprocess.services.goldenrecord;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;

import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.dto.EvaluateGoldenRecordServiceDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.idto.IEvaluateGoldenRecordBucketServiceDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForEvaluateGoldenRecordModel;
import com.cs.core.config.interactor.model.goldenrecord.IGoldenRecordRuleModel;
import com.cs.core.data.Text;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.GRBucketTagDTO;
import com.cs.core.rdbms.entity.dto.GoldenRecordBucketDTO;
import com.cs.core.rdbms.entity.dto.TagDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IGRBucketAttributeDTO;
import com.cs.core.rdbms.entity.idto.IGRBucketTagDTO;
import com.cs.core.rdbms.entity.idto.IGoldenRecordBucketDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.goldenrecordbucket.dao.GoldenRecordBucketDAO;
import com.cs.core.rdbms.goldenrecordbucket.idao.IGoldenRecordBucketDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.services.CSConfigServer;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;


@SuppressWarnings("unchecked")
public class EvaluateGoldenRecordService extends AbstractBGProcessJob implements IBGProcessJob {
  
  protected int                         nbBatches                    = 1;
  protected int                         batchSize;
  protected int                         currentBatchNo               = 0;
  private IEvaluateGoldenRecordBucketServiceDTO goldenRecordBucketServiceDTO = new EvaluateGoldenRecordServiceDTO();
  
  // To compare boolean tag with true value is been already present in tagsToSave list or not.
  private List<String>                  listOfTagsToSave             = new ArrayList<String>();
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    batchSize = CSProperties.instance()
        .getInt(propName("batchSize"));
    
    goldenRecordBucketServiceDTO.fromJSON(jobData.getEntryData()
        .toString());
    RDBMSLogger.instance()
        .debug("End of initialization BGP %s / iid=%d", getService(), getIID());
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException,
      PluginException, Exception
  {
    Long baseEntityIID = goldenRecordBucketServiceDTO.getBaseEntityIID();
    ILocaleCatalogDAO localeCatlogDAO = openUserSession().openLocaleCatalog(userSession, baseEntityIID);
    evaluateGoldenRecordBuckets(localeCatlogDAO);
    
    setCurrentBatchNo(++currentBatchNo);
    IBGProcessDTO.BGPStatus status = null;
    jobData.getProgress()
        .setPercentageCompletion(currentBatchNo * 100 / nbBatches);
    if (jobData.getProgress()
        .getPercentageCompletion() == 100) {
      status = IBGProcessDTO.BGPStatus.ENDED_SUCCESS;
    }
    else {
      status = IBGProcessDTO.BGPStatus.ENDED_ERRORS;
    }
    
    return status;
    
  }
  
  private void evaluateGoldenRecordBuckets(ILocaleCatalogDAO localeCatlogDAO) throws Exception
  {
    Long baseEntityIID = goldenRecordBucketServiceDTO.getBaseEntityIID();
    IGoldenRecordBucketDAO goldenRecordBucketDAO = new GoldenRecordBucketDAO();
    
    if(goldenRecordBucketServiceDTO.getIsBaseEntityDeleted())
    {
      handleMergedAndDeletedInstance(baseEntityIID, goldenRecordBucketDAO);
      return;
    }
    
    IBaseEntityDTO entity = localeCatlogDAO.getEntityByIID(baseEntityIID);
    if (entity != null) 
    {
      IBaseEntityDAO baseEntityDAO = localeCatlogDAO.openBaseEntity(entity);
      if (baseEntityDAO.getBaseEntityDTO().isMerged()) {
        handleMergedAndDeletedInstance(baseEntityIID, goldenRecordBucketDAO);
      }
      else {
        evaluateGoldenRecordBucketsForBaseEntity(baseEntityDAO, goldenRecordBucketDAO);
      }
    }
  }
  
  private void fillAttibutesToSave(List<Map<String, Object>> attributesToSave, Map<String, IValueRecordDTO> attributesMap,
      List<String> ruleAttributes)
  {
    for (String attributeId : ruleAttributes) {
      if (!attributesMap.containsKey(attributeId)) {
        break;
      }
      IValueRecordDTO attribute = attributesMap.get(attributeId);
      String attributeValue = attribute.getValue();
      if (attributeValue == null || attributeValue.trim().equals("")) {
        break;
      }
      Map<String, Object> attributeToSave = new HashMap<String, Object>();
      attributeToSave.put(IAttribute.ID, attributeId);
      attributeToSave.put(IGRBucketAttributeDTO.VALUE, attributeValue);
      attributesToSave.add(attributeToSave);
      
    }
  }
  
  private void fillTagsToSave(List<IGRBucketTagDTO> tagsToSave, Map<String, ITagsRecordDTO> tagsMap, List<String> ruleTags)
  {
    for (String tagId : ruleTags) {
      if (!tagsMap.containsKey(tagId)) {
        break;
      }
      ITagsRecordDTO tag = tagsMap.get(tagId);
      Set<ITagDTO> tagValues = tag.getTags();
      if (tagValues.isEmpty()) {
        break;
      }
      
      IGRBucketTagDTO tagToSave = null;
      Set<ITagDTO> tagValuesToSave = new HashSet<ITagDTO>();
      for (ITagDTO tagValue : tagValues) {
        Integer relevance = (Integer) tagValue.getRange();
        if (relevance != 0) {
          tagValuesToSave.add(tagValue);
        }
        tagToSave = new GRBucketTagDTO(tagId, tagValuesToSave);
      }
      tagsToSave.add(tagToSave);
      listOfTagsToSave.add(tagId);
    }
  }
  
  private void handleMergedAndDeletedInstance(Long baseEntityIid, IGoldenRecordBucketDAO goldenRecordBucketDAO) throws Exception
  {
    List<Long> goldenRecordBuckets = goldenRecordBucketDAO.getAllGoldenRecordBucketsOfBaseEntityIid(baseEntityIid);
    List<Long> bucketIdsToDelete = new ArrayList<>();
    for (Long goldenRecordBucketId : goldenRecordBuckets) {
      deleteBaseEntityIidsFromGoldenRecordBucket(bucketIdsToDelete, goldenRecordBucketId, goldenRecordBucketDAO, baseEntityIid);
    }
    goldenRecordBucketDAO.deleteGoldenRecordBucketByIds(bucketIdsToDelete);
  }
  
  private void evaluateGoldenRecordBucketsForBaseEntity(IBaseEntityDAO baseEntityDAO, IGoldenRecordBucketDAO goldenRecordBucketDAO)
      throws Exception
  {
    List<IGoldenRecordBucketDTO> goldenRecordBucketDTOsToCreate = new ArrayList<IGoldenRecordBucketDTO>();
    List<IGoldenRecordBucketDTO> goldenRecordBucketDTOsToUpdate = new ArrayList<IGoldenRecordBucketDTO>();
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    Long baseEntityIID = baseEntityDTO.getBaseEntityIID();
    IClassifierDTO natureClassifier = baseEntityDTO.getNatureClassifier();
    Set<IClassifierDTO> otherClassifiers = baseEntityDTO.getOtherClassifiers();
    List<String> types = new ArrayList<String>();
    List<String> taxonomyIds = new ArrayList<String>();
    types.add(natureClassifier.getClassifierCode());
    for (IClassifierDTO otherClassifier : otherClassifiers) {
      if (otherClassifier.getClassifierType().equals(ClassifierType.CLASS)) {
        types.add(otherClassifier.getCode());
      }
      else {
        taxonomyIds.add(otherClassifier.getCode());
      }
    }
    
    String endpointCode = baseEntityDTO.getEndpointCode();
    String catalogCode = baseEntityDTO.getCatalog().getCode();
    String organizationCode = baseEntityDTO.getCatalog().getOrganizationCode();
    
    Map<String, Object> configRequestModel = new HashMap<String, Object>();
    configRequestModel.put(IKlassInstanceTypeModel.TYPES, types);
    configRequestModel.put(IKlassInstanceTypeModel.TAXONOMY_IDS, taxonomyIds);
    configRequestModel.put(IKlassInstanceTypeModel.ENDPOINT_ID, endpointCode);
    configRequestModel.put(IKlassInstanceTypeModel.PHYSICAL_CATALOG_ID, catalogCode);
    configRequestModel.put(IKlassInstanceTypeModel.ORAGANIZATION_ID, organizationCode);
    /*klassInstanceTypeModel.setTypes(types);
    klassInstanceTypeModel.setTaxonomyIds(taxonomyIds);
    klassInstanceTypeModel.setEndpointId(endpointCode);
    klassInstanceTypeModel.setPhysicalCatalogId(catalogCode);
    klassInstanceTypeModel.setOrganizationId(organizationCode);*/
    String localeID = baseEntityDTO.getLocaleCatalog().getLocaleID();
    JSONObject detailsFromODB = CSConfigServer.instance().request(configRequestModel, "GetGoldenRecordRulesAssociatedWithInstance",
        localeID);
    List<Map<String, Object>> goldenRecordRules = (List<Map<String, Object>>) detailsFromODB.get("goldenRecordRules");
    Map<String, Object> referencedTags = (Map<String, Object>) detailsFromODB
        .get(IConfigDetailsForEvaluateGoldenRecordModel.REFERENCED_TAGS);
    List<String> booleanTagsToPreserve = (List<String>) detailsFromODB
        .get(IConfigDetailsForEvaluateGoldenRecordModel.BOOLEAN_TAGS_TO_PRESERVE);
    baseEntityDAO.loadAllPropertyRecords(baseEntityDTO.getPropertyRecords().toArray(new IPropertyDTO[] {}));
    Set<IPropertyRecordDTO> propertyRecords = baseEntityDTO.getPropertyRecords();
    Map<String, IValueRecordDTO> attributesMap = new HashMap<String, IValueRecordDTO>();
    Map<String, ITagsRecordDTO> tagsMap = new HashMap<String, ITagsRecordDTO>();
    
    for (IPropertyRecordDTO propertyRecord : propertyRecords) {
      if (propertyRecord instanceof IValueRecordDTO) {
        IValueRecordDTO record = (IValueRecordDTO) propertyRecord;
        IPropertyDTO property = record.getProperty();
        attributesMap.put(property.getCode(), record);
      }
      else if (propertyRecord instanceof ITagsRecordDTO) {
        ITagsRecordDTO record = (ITagsRecordDTO) propertyRecord;
        IPropertyDTO property = record.getProperty();
        tagsMap.put(property.getCode(), record);
      }
    }
    
    for (Map<String, Object> goldenRecordRule : goldenRecordRules) {
      List<String> ruleAttributes = (List<String>) goldenRecordRule.get(IGoldenRecordRuleModel.ATTRIBUTES);
      List<String> ruleTags = (List<String>) goldenRecordRule.get(IGoldenRecordRuleModel.TAGS);
      if (ruleAttributes.isEmpty() && ruleTags.isEmpty()) {
        continue;
      }
      
      List<Map<String, Object>> attributesToSave = new ArrayList<Map<String, Object>>();
      fillAttibutesToSave(attributesToSave, attributesMap, ruleAttributes);
      List<IGRBucketTagDTO> tagsToSave = new ArrayList<IGRBucketTagDTO>();
      fillTagsToSave(tagsToSave, tagsMap, ruleTags);
      
      List<String> booleanTagsToSave = fillFalseTagValueMapForBooleanTag(referencedTags,
          booleanTagsToPreserve, ruleTags);
      
      int tagsToSaveCount = tagsToSave.size() + booleanTagsToSave.size();
      
      if (!(attributesToSave.size() == ruleAttributes.size() && tagsToSaveCount == ruleTags.size())) {
        continue;
      }
      
      goldenRecordBucketDAO.evaluateGoldenRecordBucket(attributesToSave, tagsToSave, baseEntityDTO,
          (String) goldenRecordRule.get(IGoldenRecordRuleModel.ID), goldenRecordBucketDTOsToCreate,
          goldenRecordBucketDTOsToUpdate, booleanTagsToSave);
    }
    
    goldenRecordBucketDAO.createGoldenRecordBucket(goldenRecordBucketDTOsToCreate.toArray(new IGoldenRecordBucketDTO[] {}));
    goldenRecordBucketDAO.updateGoldenRecordBucket(goldenRecordBucketDTOsToUpdate.toArray(new GoldenRecordBucketDTO[] {}));
    
    List<Long> bucketIds = new ArrayList<Long>();
    bucketIds.addAll(goldenRecordBucketDTOsToCreate.stream().map(goldenRecordBucketDTO -> goldenRecordBucketDTO.getBucketId())
        .collect(Collectors.toList()));
    bucketIds.addAll(goldenRecordBucketDTOsToUpdate.stream().map(goldenRecordBucketDTO -> goldenRecordBucketDTO.getBucketId())
        .collect(Collectors.toList()));
    List<Long> unevaluatedBucketIds = new ArrayList<Long>();
    
    RDBMSConnectionManager.instance()
    .runTransaction((RDBMSConnection currentConn) -> {
      String QUERY_TO_GET_BUCKET_IDS  = "SELECT bucketid from pxp.goldenrecordbucketbaseentitylink WHERE";
      if (!bucketIds.isEmpty()) {
        QUERY_TO_GET_BUCKET_IDS = QUERY_TO_GET_BUCKET_IDS + " bucketid NOT IN ("+ Text.join(",", bucketIds) +") AND baseentityIID = ?";
      }
      else {
        QUERY_TO_GET_BUCKET_IDS = QUERY_TO_GET_BUCKET_IDS + "  baseentityIID = ?";
      }
      PreparedStatement stmt = currentConn.prepareStatement(QUERY_TO_GET_BUCKET_IDS);
      stmt.setLong(1, baseEntityIID);
      IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());
      while (result.next()) {
        unevaluatedBucketIds.add(result.getLong("bucketId"));
      }
    });
    
    List<Long> bucketIdsToDelete = new ArrayList<>();
    for (Long bucketId : unevaluatedBucketIds) {
      deleteBaseEntityIidsFromGoldenRecordBucket(bucketIdsToDelete, bucketId, goldenRecordBucketDAO, baseEntityIID);
    }
    goldenRecordBucketDAO.deleteGoldenRecordBucketByIds(bucketIdsToDelete);
  }
  
  private void deleteBaseEntityIidsFromGoldenRecordBucket(List<Long> bucketIdsToDelete, Long bucketId,
      IGoldenRecordBucketDAO goldenRecordBucketDAO, Long baseEntityIID) throws Exception
  {
    IGoldenRecordBucketDTO goldenRecordBucketDTO = goldenRecordBucketDAO.getGoldenRecordBucketById(bucketId);
    if (goldenRecordBucketDTO.getLinkedBaseEntities().size() == 1) {
      bucketIdsToDelete.add(bucketId);
    }
    else {
      goldenRecordBucketDTO.getLinkedBaseEntities().remove(baseEntityIID);
      if (goldenRecordBucketDTO.getLinkedBaseEntities().size() == 1) {
        goldenRecordBucketDTO.setIsSearchable(false);
      }
      goldenRecordBucketDAO.updateGoldenRecordBucket(goldenRecordBucketDTO);
    }
  }
  
  /**
   * This method will fill only boolean tags with FALSE value into booleanTagsToSave list.
   * For preparing the elastic query to find correct bucket instance.
   * 
   * @param referenceTags - reference tags to fetch the tag map.
   * @param booleanTagsToPreserve - 
   * @param ruleTags - to check whether tag is required to be add into booleanTagsToSave list.
   * @return boolean tags codes
   */
  private List<String> fillFalseTagValueMapForBooleanTag(Map<String, Object> referenceTags,
      List<String> booleanTagsToPreserve, List<String> ruleTags)
  {
    List<String> booleanTagsToSave = new ArrayList<String>();
    for (String tagId : ruleTags)
    {
      // listOfTagsToSave contains tagId
      // means boolean tag is set true in article and already present in tagsToSave list.
      if (!booleanTagsToPreserve.contains(tagId) || !referenceTags.containsKey(tagId)
          || listOfTagsToSave.contains(tagId))
      {
        break;
      }
      
      Map<String, Object> referenceTag = (Map<String, Object>) referenceTags.get(tagId);
      String tagType = (String) referenceTag.get(ITag.TAG_TYPE);
      
      if (tagType.equals(CommonConstants.BOOLEAN_TAG_TYPE_ID))
      {
        booleanTagsToSave.add(tagId);
      }
    }
    
    return booleanTagsToSave;
  }
}
