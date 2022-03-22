package com.cs.api.estordbmsmigration.services;

import com.cs.api.estordbmsmigration.interactor.migration.VersionDocFields;
import com.cs.core.config.interactor.exception.relationship.RelationshipNotFoundException;
import com.cs.core.config.interactor.exception.user.UserNotFoundException;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.config.interactor.model.templating.IGetNumberOfVersionsToMaintainResponseModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.relationship.IGetRelationshipService;
import com.cs.core.config.user.IGetUserService;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.CatalogDTO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.dto.SessionDTO;
import com.cs.core.rdbms.dto.UserDTO;
import com.cs.core.rdbms.entity.dto.*;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.revision.idao.IRevisionDAO;
import com.cs.core.rdbms.tracking.dto.TimelineDTO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO.ChangeCategory;
import com.cs.core.rdbms.tracking.idto.ITrackingDTO.TrackingEvent;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO.LogoutType;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetNumberOfVersionsToMaintainStrategy;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.util.ConfigUtil;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Component
public class RevisionMigration {
  
  @Autowired
  protected IGetRelationshipService                     getRelationshipService;
  
  @Autowired
  protected IGetUserService                             getUserService;
  
  protected static final String                         GET_ENTITY_BY_ID = "select baseEntityIID from staging.baseEntity where id = ?";
  
  protected static final String                         GET_PROPERTY_CODE_BY_CID = "select code from staging.helper_config where cid= ?";
  
  protected static final String                         GET_RELATION_CODE_BY_CID = "select code from staging.helper_relationshipconfig where relationshipid = ?";

  protected static final Long   USER_IID  = 10L;
  protected static final String USER_NAME = "admin";

  @Autowired
  protected ConfigUtil                                  configUtil;
  
  @Autowired
  protected IGetNumberOfVersionsToMaintainStrategy getNumberOfVersionsToMaintainStrategy;
  
  public static final String ATTRIBUTE_CACHE = "attributecache";
  
  public IBaseEntityDTO prepareTimelineData(Map<String, Object> versionDocument, Map<String, Map<String, Object>> languageVersionDocument,
      IBaseEntityDTO previousVersion) throws Exception
  {
    ITimelineDTO changedData = new TimelineDTO();
    if (previousVersion == null) {
      previousVersion = new BaseEntityDTO();
    }
    
    int revisionNumber = (int) versionDocument.get(VersionDocFields.versionId.name());
    Map<String, Object> hiddenSummary = (Map<String, Object>) versionDocument.get(VersionDocFields.hiddenSummary.name());
    Map<String, Object> summary = (Map<String, Object>) versionDocument.get(VersionDocFields.summary.name());
    
    String organizationCode = (String) versionDocument.get(VersionDocFields.organizationId.name());
    String catalogCode = (String) versionDocument.get(VersionDocFields.physicalCatalogId.name());
    String localeID = (String) versionDocument.get(VersionDocFields.creationLanguage.name());
    ILocaleCatalogDAO localeCatalogDao = new LocaleCatalogDAO(new UserSessionDTO(),
        new LocaleCatalogDTO(localeID, new CatalogDTO(catalogCode, organizationCode)));
    String baseEntityID = (String) versionDocument.get(VersionDocFields.id.name());
    
    Long baseEntityIID = getEntityIIDByID(baseEntityID);
    if(baseEntityIID == 0l){
      RDBMSLogger.instance().info(" FAILED ENTITY: BASE ENTITY ID NOT PRESENT:" + baseEntityID);
      return null;
    }
    BaseEntityDTO latestEntity = (BaseEntityDTO) localeCatalogDao.getEntityByIID(baseEntityIID);
    if(latestEntity == null){
      RDBMSLogger.instance().info("FAILED ENTITY: BASE ENTITY NOT PRESENT" + baseEntityID);
      return null;
    }
    latestEntity.getOtherClassifierIIDs().clear();
    IBaseEntityDAO baseEntityDao = localeCatalogDao.openBaseEntity(latestEntity);

    ChangeCategory propertyEvent = revisionNumber == 0 ? ChangeCategory.CreatedRecord : ChangeCategory.UpdatedRecord;
    
    Set<IPropertyRecordDTO> tagRecords = prepareTags(summary, hiddenSummary, changedData, propertyEvent, versionDocument, baseEntityDao);
    Set<IPropertyRecordDTO> attributes = prepareAttributes(summary, hiddenSummary, changedData, propertyEvent, languageVersionDocument,
        baseEntityDao, versionDocument);
    
    Integer relationChangeCount = (Integer) summary.get(VersionDocFields.relationshipChanged.name());
    relationChangeCount = checkNull(relationChangeCount);
    Set<IRelationsSetDTO> currentRelations = getCurrentRelations(latestEntity, versionDocument, localeCatalogDao, VersionDocFields.relationships.name());
    prepareRelations(previousVersion, currentRelations, changedData, relationChangeCount);
    
    Integer natureRelationshipChangeCount = (Integer) summary.get(VersionDocFields.natureRelationshipChanged.name());
    natureRelationshipChangeCount = checkNull(natureRelationshipChangeCount);
    Set<IRelationsSetDTO> currentNatureRelations = getCurrentRelations(latestEntity, versionDocument, localeCatalogDao, VersionDocFields.natureRelationships.name());
    prepareRelations(previousVersion, currentNatureRelations, changedData, natureRelationshipChangeCount);
    
    Set<IClassifierDTO> classifiers = prepareClassifier(latestEntity, previousVersion, summary, changedData, versionDocument);
    
    List<String> locales = prepareLocale(versionDocument, latestEntity, previousVersion, changedData);
    
    Long defaultImageIID = prepareDefaultImageIID(latestEntity, changedData, summary, versionDocument);
    
    String pxonDelta = preparePxonDelta(latestEntity, tagRecords, attributes, currentRelations, currentNatureRelations, classifiers,
        locales, defaultImageIID);
    
    String trackingData = changedData.toJSON();
    TrackingEvent trackingEvent = revisionNumber == 0 ? TrackingEvent.CREATE : TrackingEvent.MODIFY;
    String userId = (String) versionDocument.get(VersionDocFields.lastModifiedBy.name());
    long userIID;
    String userName;

    try {
      IUserModel user = getUserService.execute(new IdParameterModel(userId));
      userIID = user.getUserIID();
      userName = user.getUserName();
    }
    catch (UserNotFoundException ex) {
      userIID = USER_IID;
      userName = USER_NAME;
    }

    RDBMSAppDriverManager.getDriver().newObjectTrackingDAO().createObjectTracking(latestEntity.getBaseEntityIID(),
        latestEntity.getNatureClassifier().getClassifierIID(), userIID, trackingData, trackingEvent, pxonDelta.getBytes(),
        latestEntity.getLastModifiedTrack().getWhen());
    createNewRevision(versionDocument, latestEntity, baseEntityDao, userIID, userName);
    return latestEntity;
  }

  private Integer checkNull(Integer relationChangeCount)
  {
    if(relationChangeCount == null)
      relationChangeCount = 0;
    return relationChangeCount;
  }
  
  private Set<IPropertyRecordDTO> prepareTags(Map<String, Object> summary, Map<String, Object> hiddenSummary, ITimelineDTO timelineDTO,
      ChangeCategory event, Map<String, Object> mainDocument, IBaseEntityDAO baseEntityDao) throws CSFormatException, RDBMSException
  {
    List<String> ids = new ArrayList<>();
    List<String> tagIds = (List<String>) summary.get(VersionDocFields.tagIds.name());
    List<String> hiddenSummaryTagIds = (List<String>) hiddenSummary.get(VersionDocFields.tagIds.name());
    addIds(ids, tagIds);
    addIds(ids, hiddenSummaryTagIds);

    Set<IPropertyRecordDTO> propertyRecords = new HashSet<>();
    Integer tagChanged = (Integer) summary.get(VersionDocFields.tagChanged.name());
    tagChanged = checkNull(tagChanged);
    if (tagChanged > 0 || !ids.isEmpty()) {
      List<Map<String, Object>> tags = (List<Map<String, Object>>) mainDocument.get(VersionDocFields.tags.name());
      for (Map<String, Object> tag : tags) {
        String tagID = (String) tag.get(VersionDocFields.tagId.name());
        if (ids.contains(tagID)) {
          IPropertyRecordDTO propertyRecord = getPropertyRecord(baseEntityDao, tagID);
          if (propertyRecord != null) {
            ITagsRecordDTO tagRecord = (ITagsRecordDTO) propertyRecord;
            setTagValue(propertyRecords, tag, tagRecord);
          }else {
            IPropertyDTO property = getPropertyDTO(tagID);
            if(property != null){
              ITagsRecordDTO tagRecord = baseEntityDao.newTagsRecordDTOBuilder(property).build();
              setTagValue(propertyRecords, tag, tagRecord);
            }
          }
        }
      }
      timelineDTO.register(event, propertyRecords.toArray(new IPropertyRecordDTO[0]));
    }
    return propertyRecords;
  }

  protected String preparePxonDelta(BaseEntityDTO latestEntity, Set<IPropertyRecordDTO> tagRecords, Set<IPropertyRecordDTO> attributes,
      Set<IRelationsSetDTO> currentRelations, Set<IRelationsSetDTO> currentNatureRelations, Set<IClassifierDTO> classifiers,
      List<String> locales, Long defaultImageIID) throws CSFormatException
  {
    latestEntity.setOtherClassifierIIDs(classifiers.toArray(IClassifierDTO[]::new));
    latestEntity.setDefaultImageIID(defaultImageIID);
    latestEntity.getPropertyRecords().addAll(attributes);
    latestEntity.getPropertyRecords().addAll(tagRecords);
    latestEntity.getPropertyRecords().addAll(currentRelations);
    latestEntity.getPropertyRecords().addAll(currentNatureRelations);
    return latestEntity.toPXON();
  }

  private void setTagValue(Set<IPropertyRecordDTO> propertyRecords, Map<String, Object> tag,
      ITagsRecordDTO tagRecord)
  {
    tagRecord.getTags().clear();
    List<Map<String, Object>> tagValues = (List<Map<String, Object>>) tag.get(VersionDocFields.tagValues.name());
    tagValues.forEach(t -> {
      int relevance = (int) t.get(VersionDocFields.relevance.name());
      if (relevance > 0) {
        ITagDTO tagDTO = new TagDTO((String) t.get(VersionDocFields.tagId.name()), relevance);
        tagRecord.getTags().add(tagDTO);
      }
    });
    propertyRecords.add(tagRecord);
  }
  
  private IPropertyRecordDTO getPropertyRecord(IBaseEntityDAO baseEntityDao, String code) throws RDBMSException, CSFormatException
  {
    IPropertyDTO property = getPropertyDTO(code);
    if(property == null){
      return null;
    }
    IBaseEntityDTO entity = baseEntityDao.loadPropertyRecords(property);
    return entity.getPropertyRecord(property.getPropertyIID());
  }

  private IPropertyDTO getPropertyDTO(String code) throws RDBMSException
  {
    IPropertyDTO property;
    try {
      property = ConfigurationDAO.instance().getPropertyByCode(code);
    }catch(RDBMSException e) {
      List<String> codes = getCodeByCid(code);
      if(codes.isEmpty()){
        return null;
      }

      property = ConfigurationDAO.instance().getPropertyByCode(codes.get(0));
    }
    return property;
  }

  private List<String> getCodeByCid(String code) throws RDBMSException
  {
    List<String> codes = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement statement = currentConn.prepareStatement(GET_PROPERTY_CODE_BY_CID);
      statement.setString(1, code);
      ResultSet executeQuery = statement.executeQuery();
      if (executeQuery.next())
        codes.add(executeQuery.getString("code"));
    });
    return codes;
  }
  
  private List<String> getRelationCodeByCid(String code) throws RDBMSException
  {
    List<String> codes = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement statement = currentConn.prepareStatement(GET_RELATION_CODE_BY_CID);
      statement.setString(1, code);
      ResultSet executeQuery = statement.executeQuery();
      if (executeQuery.next())
        codes.add(executeQuery.getString("code"));
    });
    return codes;
  }
  
  private  Set<IClassifierDTO> prepareClassifier(IBaseEntityDTO newEntity, IBaseEntityDTO oldEntity, Map<String, Object> summary, ITimelineDTO timelineDTO,
      Map<String, Object> versionDocument) throws CSFormatException, RDBMSException
  {
    Integer klassAdded = (Integer) summary.get(VersionDocFields.klassAdded.name());
    Integer klassRemoved = (Integer) summary.get(VersionDocFields.klassRemoved.name());
    Integer taxonomyAdded = (Integer) summary.get(VersionDocFields.taxonomyAdded.name());
    Integer taxonomyRemoved = (Integer) summary.get(VersionDocFields.taxonomyRemoved.name());
    klassAdded = checkNull(klassAdded);
    klassRemoved = checkNull(klassRemoved);
    taxonomyAdded = checkNull(taxonomyAdded);
    taxonomyRemoved = checkNull(taxonomyRemoved);
    Set<IClassifierDTO> createClassifiers = new HashSet<>();
    if (klassAdded > 0 || taxonomyAdded > 0 || klassRemoved > 0 || taxonomyRemoved > 0) {
      setTypesAndTaxonomies(versionDocument, newEntity);
      Set<IClassifierDTO> newOthClassifier = newEntity.getOtherClassifiers();
      Set<IClassifierDTO> oldOthClassifier = oldEntity.getOtherClassifiers();
      
      createClassifiers = new HashSet<>(newOthClassifier);
      if (klassAdded > 0 || taxonomyAdded > 0) {
        createClassifiers.removeAll(oldOthClassifier);
        timelineDTO.register(ChangeCategory.AddedClassifier, createClassifiers);
      }
      
      if (klassRemoved > 0 || taxonomyRemoved > 0) {
        Set<IClassifierDTO> removeClassifiers = new HashSet<>(oldOthClassifier);
        removeClassifiers.removeAll(newOthClassifier);
        timelineDTO.register(ChangeCategory.RemovedClassifier, removeClassifiers);
      }
    }
    return createClassifiers;
  }
  
  private void setTypesAndTaxonomies(Map<String, Object> versionDocument, IBaseEntityDTO currentVersion) throws RDBMSException
  {
    List<String> selectedTaxonomyIds = (List<String>) versionDocument.get(VersionDocFields.selectedTaxonomyIds.name());
    for (String selectedTaxonomyId : selectedTaxonomyIds) {
      List<String> codes = getCodeByCid(selectedTaxonomyId);
      IClassifierDTO classifierByCode = ConfigurationDAO.instance().doesClassifierExist(codes.get(0));
      if (classifierByCode != null) {
        currentVersion.getOtherClassifiers().add(classifierByCode);
      }
    }
    List<String> types = (List<String>) versionDocument.get(VersionDocFields.types.name());
    for (String type : types) {
      if (type.equals(currentVersion.getNatureClassifier().getCode())) {
        continue;
      }
      List<String> codes = getCodeByCid(type);
      IClassifierDTO classifierByCode = ConfigurationDAO.instance().doesClassifierExist(codes.get(0));
      if (classifierByCode != null) {
        currentVersion.getOtherClassifiers().add(classifierByCode);
      }
    }
  }
  
  private Long prepareDefaultImageIID(IBaseEntityDTO currentEntity, ITimelineDTO changedData, Map<String, Object> summary,
      Map<String, Object> mainDocumnet) throws RDBMSException
  {
    Boolean isDefaultAssetInstanceIdChanged = (Boolean) summary.get(VersionDocFields.isDefaultAssetInstanceIdChanged.name());
    String defaultAssetInstanceId = (String) mainDocumnet.get(VersionDocFields.defaultAssetInstanceId.name());
    if (StringUtils.isNotEmpty(defaultAssetInstanceId)) {
      Long IID = getEntityIIDByID(defaultAssetInstanceId);
      currentEntity.setDefaultImageIID(IID);
      Long newDefaultImageIID = currentEntity.getDefaultImageIID();
      if (isDefaultAssetInstanceIdChanged == null)
        isDefaultAssetInstanceIdChanged = false;
      if (isDefaultAssetInstanceIdChanged) {
        CSEObject defaultImage = new CSEObject(CSEObjectType.Entity);
        defaultImage.setCode(String.valueOf(newDefaultImageIID));
        defaultImage.setIID(newDefaultImageIID);
        changedData.register(ChangeCategory.NewDefaultImageIID, defaultImage);
      }
      return newDefaultImageIID;
    }
    return 0L;
  }
  
  private List<String> prepareLocale(Map<String, Object> document, IBaseEntityDTO latestEntity, IBaseEntityDTO previousEntity,
      ITimelineDTO changedData) throws CSFormatException
  {
    List<Map<String, Object>> languageInstances = (List<Map<String, Object>>) document.get(VersionDocFields.languageInstances.name());
    List<String> oldLocaleIds = previousEntity.getLocaleIds();
    List<String> languageCodes = (List<String>) document.get(VersionDocFields.languageCodes.name());
    latestEntity.getLocaleIds().clear();
    latestEntity.getLocaleIds().addAll(languageCodes);
    List<String> newLocaleIds = latestEntity.getLocaleIds();
    List<String> deletedLocaleIds = ListUtils.subtract(oldLocaleIds, newLocaleIds);
    List<String> addedLocaleIds = ListUtils.subtract(newLocaleIds, oldLocaleIds);
    
    String creationLanguage = (String) document.get(VersionDocFields.creationLanguage.name());
    // No event for Creation language
    addedLocaleIds.remove(creationLanguage);
    
    if (!addedLocaleIds.isEmpty()) {
      for (Map<String, Object> languageInstance : languageInstances) {
        String languageCode = (String) languageInstance.get(VersionDocFields.languageCode.name());
        int versionId = (int) languageInstance.get(VersionDocFields.versionId.name());
        if (addedLocaleIds.contains(languageCode) && versionId == 0) {
          ILanguageTranslationDTO translationDTO = new LanguageTranslationDTO(languageCode, latestEntity.getBaseEntityIID());
          changedData.register(ChangeCategory.CreatedTranslation, translationDTO);
        }
      }
    }
    
    for (String languageCode : deletedLocaleIds) {
      ILanguageTranslationDTO translationDTO = new LanguageTranslationDTO(languageCode, latestEntity.getBaseEntityIID());
      changedData.register(ChangeCategory.DeletedTranslation, translationDTO);
    }
    
    return addedLocaleIds;
  }
  
  private Set<IPropertyRecordDTO> prepareAttributes(Map<String, Object> summary, Map<String, Object> hiddenSummary,
      ITimelineDTO timelineDTO, ChangeCategory changePropertyCategory, Map<String, Map<String, Object>> languageDocuments,
      IBaseEntityDAO baseEntityDao, Map<String, Object> document) throws CSFormatException, RDBMSException, IOException, CSInitializationException
  {
    Integer attributeChanged = (Integer) summary.get(VersionDocFields.attributeChanged.name());
    attributeChanged = checkNull(attributeChanged);
    List<String> ids = new ArrayList<>();
    List<String> attributeIds = (List<String>) summary.get(VersionDocFields.attributeIds.name());
    List<String> hiddenSummaryAttributeIds = (List<String>) hiddenSummary.get(VersionDocFields.attributeIds.name());
    addIds(ids, attributeIds);
    addIds(ids, hiddenSummaryAttributeIds);
    Map<String, Object> dependentAttributeIds = (Map<String, Object>) summary.get(VersionDocFields.dependentAttributeIdsMap.name());
    if (dependentAttributeIds == null) {
      dependentAttributeIds = new HashMap<>();
    }
    
    Set<IPropertyRecordDTO> propertyRecords = new HashSet<>();
    if (attributeChanged > 0 || !ids.isEmpty() || !dependentAttributeIds.isEmpty()) {
      List<Map<String, Object>> attributes = (List<Map<String, Object>>) document.get(VersionDocFields.attributes.name());
      getValueRecords(baseEntityDao, propertyRecords, ids, attributes);

      for (Entry<String, Object> attribute : dependentAttributeIds.entrySet()) {
        String localeID = attribute.getKey();
        Map<String, Object> languageDocument = languageDocuments.get(localeID);
        List<String> attributeCodes = (List<String>) attribute.getValue();
        List<Map<String, Object>> dependentAttributes = (List<Map<String, Object>>) languageDocument
            .get(VersionDocFields.dependentAttributes.name());
        baseEntityDao.getLocaleCatalog().getLocaleCatalogDTO().setLocaleInheritanceSchema(Collections.singletonList(localeID));
        getValueRecords(baseEntityDao, propertyRecords, attributeCodes, dependentAttributes);
      }
      timelineDTO.register(changePropertyCategory, propertyRecords.toArray(new IPropertyRecordDTO[0]));
    }
    
    prepareAttributeVariants(baseEntityDao, document, propertyRecords, timelineDTO, changePropertyCategory);
    prepareDependantAttributeVarinats(baseEntityDao, document, propertyRecords, timelineDTO, changePropertyCategory, languageDocuments);
    return propertyRecords;
  }

  private void prepareDependantAttributeVarinats(IBaseEntityDAO baseEntityDao, Map<String, Object> document,
      Set<IPropertyRecordDTO> propertyRecords, ITimelineDTO timelineDTO, ChangeCategory changePropertyCategory,
      Map<String, Map<String, Object>> languageDocuments) throws IOException, CSInitializationException, RDBMSException, CSFormatException
  {
    Set<IPropertyRecordDTO> valueRecords = new HashSet<>();
    for (Map<String, Object> languageDocument : languageDocuments.values()) {
      String localeID = (String) languageDocument.get(VersionDocFields.language.name());
      List<Map<String, Object>> attributeVariants = (List<Map<String, Object>>) languageDocument
          .get(VersionDocFields.attributeVariants.name());
      if (attributeVariants != null) {
        List<String> attributeVraintIds = new ArrayList<>();
        for (Map<String, Object> attributeVarint : attributeVariants) {
          int version = (int) attributeVarint.get(VersionDocFields.versionId.name());
          String id = (String) attributeVarint.get(VersionDocFields.id.name());
          attributeVraintIds.add(id + "__" + localeID + "_" + version);
        }
        SearchResponse response = getAttributeVariantsHits(attributeVraintIds, ATTRIBUTE_CACHE + "__" + localeID);
        prepareAttributeVarinatsFromHits(baseEntityDao, valueRecords, response);
      }
    }
    if (!valueRecords.isEmpty()) {
      timelineDTO.register(changePropertyCategory, valueRecords.toArray(new IPropertyRecordDTO[0]));
      propertyRecords.addAll(valueRecords);
    }
  }

  private void prepareAttributeVariants(IBaseEntityDAO baseEntityDao, Map<String, Object> document, Set<IPropertyRecordDTO> propertyRecords,
      ITimelineDTO timelineDTO, ChangeCategory changePropertyCategory)
      throws IOException, CSInitializationException, RDBMSException, CSFormatException
  {
    List<Map<String, Object>> attributeVariants = (List<Map<String, Object>>) document.get(VersionDocFields.attributeVariants.name());
    Set<IPropertyRecordDTO> valueRecords = new HashSet<>();
    if (attributeVariants != null) {
      List<String> attributeVraintIds = new ArrayList<>();
      for (Map<String, Object> attributeVarint : attributeVariants) {
        int version = (int) attributeVarint.get(VersionDocFields.versionId.name());
        String id = (String) attributeVarint.get(VersionDocFields.id.name());
        attributeVraintIds.add(id + "_" + version);
      }
      
      SearchResponse response = getAttributeVariantsHits(attributeVraintIds, ATTRIBUTE_CACHE);
      prepareAttributeVarinatsFromHits(baseEntityDao, valueRecords, response);
    }
    
    if (!valueRecords.isEmpty()) {
      timelineDTO.register(changePropertyCategory, valueRecords.toArray(new IPropertyRecordDTO[0]));
      propertyRecords.addAll(valueRecords);
    }
  }

  private void prepareAttributeVarinatsFromHits(IBaseEntityDAO baseEntityDao, Set<IPropertyRecordDTO> valueRecords, SearchResponse response)
      throws RDBMSException, CSFormatException
  {
    for (SearchHit searchHit : response.getHits().getHits()) {
      Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
      String attributeId = (String) sourceAsMap.get(VersionDocFields.attributeId.name());
      IValueRecordDTO propertyRecord = (IValueRecordDTO) getPropertyRecord(baseEntityDao, attributeId);
      if(propertyRecord == null){
        continue;
      }
      checkContentConsistency(propertyRecord, sourceAsMap);
      Map<String, Object> context = (Map<String, Object>) sourceAsMap.get(VersionDocFields.context.name());
      IContextualDataDTO contextualObject = propertyRecord.getContextualObject();
      Map<String, Object> timeRange = (Map<String, Object>) context.get(VersionDocFields.timeRange.name());
      contextualObject.setContextStartTime((long) timeRange.get(VersionDocFields.from.name()));
      contextualObject.setContextEndTime((long) timeRange.get(VersionDocFields.to.name()));
      contextualObject.getContextTagValues().clear();
      List<Map<String, Object>> tags = (List<Map<String, Object>>) sourceAsMap.get(VersionDocFields.tags.name());
      for (Map<String, Object> tag : tags) {
        List<Map<String, Object>> tagValues = (List<Map<String, Object>>) tag.get(VersionDocFields.tagValues.name());
        tagValues.forEach(t -> {
          int relevance = (int) t.get(VersionDocFields.relevance.name());
          if (relevance > 0) {
            ITagDTO tagDTO = new TagDTO((String) t.get(VersionDocFields.tagId.name()), relevance);
            contextualObject.getContextTagValues().add(tagDTO);
          }
        });
      }
      valueRecords.add(propertyRecord);
    }
  }

  private SearchResponse getAttributeVariantsHits(List<String> attributeVraintIds, String doctype)
      throws IOException, CSInitializationException
  {
    IdsQueryBuilder getLanguageDocQuery = QueryBuilders.idsQuery().addIds(attributeVraintIds.toArray(String[]::new));
    SearchSourceBuilder source = new SearchSourceBuilder();
    source.query(getLanguageDocQuery);
    source.size(1000);
    SearchRequest sr = new SearchRequest("cs_version", "csarchive_version");
    sr.source(source);
    sr.types(doctype);
    SearchResponse response = RequestHandler.getRestClient().search(sr, RequestOptions.DEFAULT);
    return response;
  }
  
  private void addIds(List<String> ids, List<String> checkList)
  {
    if (checkList != null)
      ids.addAll(checkList);
  }
  
  private void getValueRecords(IBaseEntityDAO baseEntityDao, Set<IPropertyRecordDTO> propertyRecords, List<String> attributeCodes,
      List<Map<String, Object>> attributes) throws RDBMSException, CSFormatException
  {
    if (!attributeCodes.isEmpty()) {
      for (Map<String, Object> attribute : attributes) {
        String attributeCode = (String) attribute.get(VersionDocFields.code.name());
        if (attributeCodes.contains(attributeCode)) {
          IPropertyRecordDTO propertyRecord = getPropertyRecord(baseEntityDao, attributeCode);
          if (propertyRecord != null) {
            IValueRecordDTO valueRecord = (IValueRecordDTO) propertyRecord;
            checkContentConsistency(valueRecord, attribute);
            propertyRecords.add(valueRecord);
          }
          else {
            IPropertyDTO property = getPropertyDTO(attributeCode);
            if(property!=null){

              String value = (String) attribute.get(VersionDocFields.value.name());
              IValueRecordDTO valueRecord = baseEntityDao.newValueRecordDTOBuilder(property, value).build();
              checkContentConsistency(valueRecord, attribute);
              propertyRecords.add(valueRecord);
            }
          }
        }
      }
    }
  }
  
  private void prepareRelations(IBaseEntityDTO previousVersion, Set<IRelationsSetDTO> currentProperties, ITimelineDTO changedData,
      int relationshipChanged) throws CSFormatException
  {
    if (relationshipChanged > 0) {
      
      for (IPropertyRecordDTO currentProperty : currentProperties) {
        RelationsSetDTO currentRelation = (RelationsSetDTO) currentProperty;
        IPropertyRecordDTO previousProperty = previousVersion.getPropertyRecord(currentProperty.getProperty().getIID());
        
        if (previousProperty == null) {
          previousProperty = new RelationsSetDTO();
        }
        IRelationsSetDTO previousRelations = (RelationsSetDTO) previousProperty;
        Set<EntityRelationDTO> missingRelationsInRecord = currentRelation.identifyMissingRelations(previousRelations.getRelations());
        if (!missingRelationsInRecord.isEmpty()) {
          RelationsSetDTO cloneWithMissingRelations = currentRelation.cloneWithRelations(missingRelationsInRecord);
          changedData.register(ChangeCategory.RemovedRelation, cloneWithMissingRelations);
          changedData.register(ChangeCategory.RemovedRelation, IPXON.PXONMeta.Content, cloneWithMissingRelations.joinSideBaseEntityIIDs());
        }
        Set<EntityRelationDTO> addedRelationsInRecord = currentRelation.identifyAdditionalRelations(previousRelations.getRelations());
        if (!addedRelationsInRecord.isEmpty()) {
          RelationsSetDTO cloneWithAddedRelations = currentRelation.cloneWithRelations(addedRelationsInRecord);
          changedData.register(ChangeCategory.AddedRelation, currentRelation);
          changedData.register(ChangeCategory.AddedRelation, IPXON.PXONMeta.Content, cloneWithAddedRelations.joinSideBaseEntityIIDs());
        }
        
      }
      
      if (currentProperties.isEmpty()) {
        Set<IPropertyRecordDTO> previousProperty = previousVersion.getPropertyRecords();
        Set<IPropertyRecordDTO> deletedRelation = previousProperty.stream()
            .filter(r -> r.getProperty().getPropertyType().equals(PropertyType.RELATIONSHIP)).collect(Collectors.toSet());
        for (IPropertyRecordDTO relation : deletedRelation) {
          RelationsSetDTO previousRelations = (RelationsSetDTO) relation;
          changedData.register(ChangeCategory.RemovedRelation, previousRelations);
          changedData.register(ChangeCategory.RemovedRelation, IPXON.PXONMeta.Content, previousRelations.joinSideBaseEntityIIDs());
        }
      }
    }
  }
  
  private Set<IRelationsSetDTO> getCurrentRelations(IBaseEntityDTO latestEntity, Map<String, Object> versionDocument,
      ILocaleCatalogDAO localeCatalogDao, String key) throws Exception
  {
    Set<IRelationsSetDTO> currentRelations = new HashSet<>();
    
    List<String> commonRelationshipIds = (List<String>) versionDocument.get(key);
    String instanceId = (String) versionDocument.get(VersionDocFields.id.name());
    
    TermsQueryBuilder termsQuery = QueryBuilders.termsQuery("commonRelationshipInstanceId", commonRelationshipIds.toArray(new String[0]));
    SearchSourceBuilder source = new SearchSourceBuilder();
    source.query(termsQuery);
    source.size(1000);
    SearchRequest sr = new SearchRequest("cs_version", "csarchive","csarchive_version");
    sr.source(source);
    SearchResponse response = RequestHandler.getRestClient().search(sr, RequestOptions.DEFAULT);
    
    for (SearchHit hit : response.getHits().getHits()) {
      Map<String, Object> sourceAsMap = hit.getSourceAsMap();
      String side1InstanceId = (String) sourceAsMap.get("side1InstanceId");
      String side2InstanceId = (String) sourceAsMap.get("side2InstanceId");
      String relationshipId = (String) sourceAsMap.get("relationshipId");
      String sideId = (String) sourceAsMap.get("sideId");

      IGetRelationshipModel relationshipModel = null;
      try {
        relationshipModel = getRelationshipService.execute(new IdParameterModel(relationshipId));
      }
      catch(RelationshipNotFoundException ex){
        //invalid relationship not present in orient data
        continue;
      }
      if (instanceId.equals(side1InstanceId)) {
        List<String> relationCodeByCid = getRelationCodeByCid(relationshipId);
        IPropertyDTO propertyByCode = ConfigurationDAO.instance().getPropertyByCode(relationCodeByCid.get(0));
        Long iidFromInstanceId = getEntityIIDByID(instanceId);
        IPropertyDTO.RelationSide side;
        if (relationshipModel.getEntity().getSide1().getId().equals(sideId)) {
          side = IPropertyDTO.RelationSide.SIDE_1;
        }
        else {
          side = IPropertyDTO.RelationSide.SIDE_2;
        }
        RelationsSetDTO relation = new RelationsSetDTO(iidFromInstanceId, propertyByCode, side);
        Long otherSideIID = getEntityIIDByID(side2InstanceId);
        relation.getRelations().clear();
        String otherSideID = localeCatalogDao.getEntityIdByIID(otherSideIID);
        if(otherSideID == null)
          otherSideID = "";
        relation.getRelations().add(new EntityRelationDTO(otherSideID, otherSideIID));
        currentRelations.add(relation);
      }
    }
    return currentRelations;
  }
  
  /**
   *
   * @param versionDocument elastic version document
   * @param latestEntity entity made of latest version
   * @param baseEntityDao dao to access methods
   * @throws Exception exception
   */
  private void createNewRevision(Map<String, Object> versionDocument, BaseEntityDTO latestEntity, IBaseEntityDAO baseEntityDao,
      Long userIID, String userName) throws Exception
  {
    String revisionComment = (String) versionDocument.get(VersionDocFields.saveComment.toString());
    List<String> classifiers = new ArrayList<>();
    classifiers.add(latestEntity.getNatureClassifier().getClassifierCode());
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    multiclassificationRequestModel.setKlassIds(classifiers);
    IGetNumberOfVersionsToMaintainResponseModel configDetails = getNumberOfVersionsToMaintainStrategy.execute(multiclassificationRequestModel);
    IRevisionDAO revisionDAO = RDBMSAppDriverManager.getDriver().newRevisionDAO(
        new UserSessionDTO(new SessionDTO(), new UserDTO(userIID, userName), LogoutType.NORMAL, 0L, ""));
    revisionDAO.createNewRevision(latestEntity, revisionComment, configDetails.getNumberOfVersionsToMaintain());
  }
  
  private Long getEntityIIDByID(String baseEntityID) throws RDBMSException
  {
    List<Long> baseEntityIID = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement statement = currentConn.prepareStatement(GET_ENTITY_BY_ID);
      statement.setString(1, baseEntityID);
      ResultSet executeQuery = statement.executeQuery();
      if (executeQuery.next())
        baseEntityIID.add(executeQuery.getLong("baseEntityIID"));
    });
    if(baseEntityIID.isEmpty())
      return 0l;
    return baseEntityIID.get(0);
  }
  
  private void checkContentConsistency(IValueRecordDTO valueRecord, Map<String, Object> attribute) throws RDBMSException
  {
    String value = (String) attribute.get(VersionDocFields.value.name());
    valueRecord.setValue(value);
    valueRecord.setAsHTML("");
    valueRecord.setAsNumber(0);
    PropertyType propertyType = valueRecord.getProperty().getPropertyType();
    switch (propertyType) {
      case AUTO:
      case CALCULATED:
      case CONCATENATED:
      case ASSET_ATTRIBUTE:
        setNumberValue(valueRecord, attribute);
        setHTMLValue(valueRecord, attribute);
        return; 
        
      case DATE:
      case MEASUREMENT:
      case NUMBER:
      case PRICE:
      case BOOLEAN:
        setNumberValue(valueRecord, attribute);
        break;
      
      case HTML:
        setHTMLValue(valueRecord, attribute);
        break;
        
      default:
        break;
    }
  }

  private void setHTMLValue(IValueRecordDTO valueRecord, Map<String, Object> attribute)
  {
    Object valueAsHtml = attribute.get(VersionDocFields.valueAsHtml.name());
    if (StringUtils.isNotEmpty((String) valueAsHtml))
      valueRecord.setAsHTML((String) valueAsHtml);
  }

  private void setNumberValue(IValueRecordDTO valueRecord, Map<String, Object> attribute)
  {
    Object valuAsNumber = attribute.get(VersionDocFields.valueAsNumber.name());
    if (valuAsNumber != null)
      valueRecord.setAsNumber(((Number) valuAsNumber).doubleValue());
  }
  
}
