package com.cs.core.asset.services;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.SetUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.cs.constants.SystemLevelIds;
import com.cs.core.asset.services.PropertyRecordBuilder.PropertyRecordType;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.ITagValue;
import com.cs.core.config.interactor.entity.variantcontext.IDefaultTimeRange;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.ReferencedSectionTagModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextTagsModel;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.rdbms.revision.idao.IRevisionDAO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndBaseType;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IImageAttributeInstance;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.cs.core.runtime.interactor.exception.variants.DuplicateVariantExistsException;
import com.cs.core.runtime.interactor.exception.variants.EmptyMandatoryFieldsException;
import com.cs.core.runtime.interactor.exception.variants.InvalidTimeRangeException;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.variants.CreateImageVariantsModel;
import com.cs.core.runtime.interactor.model.variants.ICreateImageVariantsModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantModel;
import com.cs.core.runtime.interactor.model.variants.IVariantReferencedInstancesModel;
import com.cs.core.runtime.interactor.model.variants.VariantReferencedInstancesModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.BaseEntityUtils;
import com.cs.utils.ContextUtil;
import com.cs.utils.dam.AssetUtils;

public class VariantInstanceCreation {
  
  private static final String            PHYSICAL_CATALOG_ID = "physicalCatalogId";
  private static final String            USER_ID             = "userId";
  protected static String                sessionID           = "session#" + new Random().nextInt();
  private Map<String, Object>            transactionData;
  private ILocaleCatalogDAO              localeCatalogDao;
  private IRevisionDAO                   revisionDAO;
  
  private static VariantInstanceCreation instance            = null;
  
  private VariantInstanceCreation() {
    // private constructor for Singleton class.
  }
  
  /**
   * Use this method to get VariantInstanceCreation instance
   * @return VariantInstanceCreation instance
   */
  public static VariantInstanceCreation getInstance() {
    if(instance == null) {
      instance = new VariantInstanceCreation();
    }
    return instance;
  }
  
  public void initializeRDBMSComponents(Map<String, Object> transactionDataFromRequest) throws RDBMSException
  {
    this.transactionData = transactionDataFromRequest;
    String userId = (String) transactionDataFromRequest.get(USER_ID);
    String localeId = (String) transactionDataFromRequest.get("localeId");
    String physicalCatalogId = (String) transactionDataFromRequest.get(PHYSICAL_CATALOG_ID);
    String organizationCode = (String) transactionDataFromRequest.get(ITransactionData.ORGANIZATION_ID);
    
    
    IUserSessionDAO userSession = RDBMSAppDriverManager.getDriver()
        .newUserSessionDAO();
    IUserSessionDTO session = userSession.openSession(userId, sessionID);
    ILocaleCatalogDTO localeCatalogDto = userSession.newLocaleCatalogDTO(localeId, physicalCatalogId, organizationCode);
    // Get Locale CatelogDAO Interface
    localeCatalogDao = userSession.openLocaleCatalog(session, localeCatalogDto);
    //Get revisionDAO
    revisionDAO = RDBMSAppDriverManager.getDriver().newRevisionDAO(session);
  }
  
  public long createVariantInstance(ICreateVariantModel createVariantMap, Map<String, Object> transactionData) throws Exception
  {
    initializeRDBMSComponents(transactionData);
    this.transactionData = transactionData;
    
    long parentInstanceIid = Long.parseLong(createVariantMap.getParentId());
    List<String> types = createVariantMap.getTypes();
    String type = types.get(0);
    createVariantMap.setType(type);
    
    Map<String, Object> multiclassificationRequestModel = new HashMap<>();
    List<String> klassIds = new ArrayList<>();
    klassIds.add(type);
    
    IBaseEntityDAO parentEntityDAO = localeCatalogDao.openBaseEntity(localeCatalogDao.getEntityByIID(parentInstanceIid));
    multiclassificationRequestModel.put("klassIds", klassIds);
    multiclassificationRequestModel.put("endpointId", transactionData.get("endpointId"));
    multiclassificationRequestModel.put(PHYSICAL_CATALOG_ID, transactionData.get(PHYSICAL_CATALOG_ID));
    multiclassificationRequestModel.put("organizationId", transactionData.get(ITransactionData.ORGANIZATION_ID));
    multiclassificationRequestModel.put("parentKlassIds", BaseEntityUtils.getAllReferenceTypeFromBaseEntity(parentEntityDAO));
    multiclassificationRequestModel.put("ParentTaxonomyIds",BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(parentEntityDAO.getClassifiers()));
    multiclassificationRequestModel.put(USER_ID,transactionData.get(USER_ID));
    multiclassificationRequestModel.put("shouldSendTaxonomyHierarchies",false);
    multiclassificationRequestModel.put("selectedTaxonomyIds", new ArrayList<>());
    multiclassificationRequestModel.put("collectionIds", new ArrayList<>());
    multiclassificationRequestModel.put("languageCodes", new ArrayList<>());
    multiclassificationRequestModel.put("parentKlassIds", new ArrayList<>());
    multiclassificationRequestModel.put("parentTaxonomyIds", new ArrayList<>());
    multiclassificationRequestModel.put("shouldUseTagIdTagValueIdsMap", false);
    
    JSONObject configDetails = CSConfigServer.instance().request(multiclassificationRequestModel, "GetConfigDetailsWithoutPermissions", null);
    IGetConfigDetailsForCustomTabModel configDetailsModel = ObjectMapperUtil
        .convertValue(configDetails, GetConfigDetailsForCustomTabModel.class);
    IBaseEntityDTO baseEntityDTO = createVariantBaseEntity(configDetailsModel, createVariantMap, BaseType.ASSET);
    return baseEntityDTO.getBaseEntityIID();
  }
  
  public String getNewVariantNameForCreate(Map<String, Object> referencedKlasses, String typeId,
      Long counter)
  {
    IReferencedKlassDetailStrategyModel iReferencedKlassDetailStrategyModel = (IReferencedKlassDetailStrategyModel) referencedKlasses
        .get("typeId");
    return iReferencedKlassDetailStrategyModel.getLabel() + " " + counter;
  }
  
  @SuppressWarnings("unchecked")
  public IBaseEntityDTO createVariantBaseEntity(IGetConfigDetailsForCustomTabModel configDetails,
      ICreateVariantModel createInstanceModel, BaseType baseType) throws Exception
  {
    IAssetInformationModel assetInformation = null;
    String baseEntityID = createInstanceModel.getVariantInstanceId() != null
        ? createInstanceModel.getVariantInstanceId()
        : RDBMSAppDriverManager.getDriver()
        .newUniqueID(baseType.getPrefix());
    
    IReferencedKlassDetailStrategyModel natureKlass = configDetails.getReferencedKlasses().get(createInstanceModel.getType());
    
    IReferencedVariantContextModel variantContext = configDetails
        .getReferencedVariantContexts()
        .getEmbeddedVariantContexts()
        .get(createInstanceModel.getContextId());

    IConfigurationDAO configurationDAO = RDBMSAppDriverManager.getDriver()
        .newConfigurationDAO();
    ContextType contextType = ContextUtil.getContextTypeByType(variantContext.getType());
    IContextDTO contextDto = configurationDAO.createContext(variantContext.getCode(), contextType);
    IClassifierDTO classifierDTO = localeCatalogDao.newClassifierDTO(natureKlass.getClassifierIID(), natureKlass.getCode(), ClassifierType.CLASS);
    
    IBaseEntityDTO newBaseEntityDTO = localeCatalogDao
        .newBaseEntityDTOBuilder(baseEntityID, baseType, classifierDTO)
        .endpointCode((String) transactionData.get(ITransactionData.ENDPOINT_ID))
        .contextDTO(contextDto)
        .build();
    
    createInstanceModel.setVariantInstanceId(newBaseEntityDTO.getBaseEntityID());
    
    IBaseEntityDAO baseEntityDAO = localeCatalogDao.openBaseEntity(newBaseEntityDTO);
    
    if (BaseType.ASSET.equals(baseType) 
        && createInstanceModel.getClass().equals(CreateImageVariantsModel.class)) {
      assetInformation = ((ICreateImageVariantsModel) createInstanceModel).getAssetInformation();
    }
    
  	if (assetInformation != null) {
  		Map<String, Object> imageAttr = new HashMap<>();
  		imageAttr.put(IImageAttributeInstance.ASSET_OBJECT_KEY, assetInformation.getAssetObjectKey());
  		imageAttr.put(IImageAttributeInstance.FILENAME, assetInformation.getFileName());
  		imageAttr.put(IImageAttributeInstance.PROPERTIES, assetInformation.getProperties());
  		imageAttr.put(IImageAttributeInstance.THUMB_KEY, assetInformation.getThumbKey());
  		imageAttr.put(IImageAttributeInstance.TYPE, assetInformation.getType());
  		imageAttr.put(IImageAttributeInstance.PREVIEW_IMAGE_KEY, assetInformation.getPreviewImageKey());
  
  		IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
      baseEntityDTO.setHashCode(assetInformation.getHash());
  		baseEntityDTO.setEntityExtension(ObjectMapperUtil.writeValueAsString(imageAttr));
  	}
    
    IBaseEntityDTO parentDTO = localeCatalogDao.getEntityByIID(Long.parseLong(createInstanceModel.getParentId()));
    IBaseEntityDAO parentDAO = localeCatalogDao.openBaseEntity(parentDTO);
    parentDAO.loadPropertyRecords();

    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    
    Map<String, ITag> referencedTags = configDetails.getReferencedTags();

    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    
    fillContextualData(baseEntityDTO, variantContext, referencedTags, referencedElements,
        baseEntityDAO);
    
    List<Long> collect = createInstanceModel.getLinkedInstances()
        .stream()
        .map(x -> Long.parseLong(x.getId()))
        .collect(Collectors.toList());
    
    Map<String, Object> configDetailsMap = ObjectMapperUtil.convertValue(configDetails, HashMap.class);
    
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO,
        configDetailsMap, PropertyRecordType.CREATE);
    IContextualDataDTO contextualObject = baseEntityDTO.getContextualObject();
    
    boolean shouldUpdateEntity = false;
    contextualObject.getLinkedBaseEntityIIDs()
        .addAll(collect);
    List<IContentTagInstance> contextTags = createInstanceModel.getContextTags();
    List<Map<String, Object>> contextTagMaps = new ArrayList<>();
    for(IContentTagInstance tag : contextTags) 
    {
      contextTagMaps.add(ObjectMapperUtil.convertValue(tag, HashMap.class));
    }
    
    IInstanceTimeRange timeRangeModel = createInstanceModel.getTimeRange();
    Map<String, Object> timeRangeMap = ObjectMapperUtil.convertValue(timeRangeModel, HashMap.class);
    propertyRecordBuilder.setContextTimeRange(timeRangeMap, contextualObject);
    
    if (!contextTags.isEmpty()) {
      propertyRecordBuilder.createContextTags(contextTagMaps, contextualObject);
      shouldUpdateEntity = true;
    }
    if (shouldUpdateEntity) {
      String contextCode = contextualObject.getContext().getContextCode();
      IReferencedVariantContextModel referencedContext = configDetails.getReferencedVariantContexts()
          .getEmbeddedVariantContexts().get(contextCode);
      checkDuplicate(baseEntityDAO, referencedContext, true, Long.parseLong(createInstanceModel.getId()));
    }
    Map<String, Object> createInstanceModelMap = ObjectMapperUtil.convertValue(createInstanceModel, HashMap.class);
    IPropertyRecordDTO[] propertyRecords = CreateInstanceUtils
        .createPropertyRecordInstance(baseEntityDAO, configDetailsMap, createInstanceModelMap);

    List<IPropertyRecordDTO> propertyRecordsList = new ArrayList<>();
    if (assetInformation != null) {
      Map<String, Object> metadata = createInstanceModel.getMetadata();
      List<IPropertyRecordDTO> metaDataAttributes = addMetadataAttributesToAssetInstanceAttributes(
          metadata, baseEntityDAO, configDetailsMap);
      propertyRecordsList.addAll(metaDataAttributes);

      IValueRecordDTO downloadCountAttributeRecord = getDownloadCountAttributeRecord(configDetailsMap,
          baseEntityDAO);
      propertyRecordsList.add(downloadCountAttributeRecord);
    }
    propertyRecordsList.addAll(Arrays.asList(propertyRecords));
    
    baseEntityDAO.createPropertyRecords(propertyRecordsList.toArray(new IPropertyRecordDTO[] {}));
    // create revision
    revisionDAO.createNewRevision(baseEntityDTO, "", configDetails.getNumberOfVersionsToMaintain());
    parentDAO.addChildren(EmbeddedType.CONTEXTUAL_CLASS, newBaseEntityDTO);
    baseEntityDAO.updatePropertyRecords();
    
    if (assetInformation != null) {
      handleDuplicateDetection(assetInformation.getHash(), baseEntityDAO);
    }
    
    return localeCatalogDao.getEntityByIID(baseEntityDTO.getBaseEntityIID());
  }
  
  /**
   * This method marks the isDuplicate column if duplicate exists for passed hash value.
   * 
   * @param hash
   * @param baseEntityDAO
   * @throws RDBMSException
   */
  private void handleDuplicateDetection(String hash, IBaseEntityDAO baseEntityDAO) throws RDBMSException
  {
    long baseEntityIID = baseEntityDAO.getBaseEntityDTO().getBaseEntityIID();
    long duplicateAssetIId = AssetUtils.handleDuplicate(hash, baseEntityDAO, transactionData, baseEntityIID);
    AssetUtils.updateDuplicateStatus(duplicateAssetIId, baseEntityDAO, localeCatalogDao);
  }

  private IValueRecordDTO getDownloadCountAttributeRecord(Map<String, Object> configDetails,
      IBaseEntityDAO baseEntityDAO) throws Exception
  {
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO,
        configDetails, PropertyRecordType.DEFAULT);
    Map<String, Object> assetDownloadCountAttributeConfig = new HashMap<>();
    assetDownloadCountAttributeConfig.put("code", SystemLevelIds.ASSET_DOWNLOAD_COUNT_ATTRIBUTE);
    assetDownloadCountAttributeConfig.put("propertyIID", 0l);
    IValueRecordDTO downloadCountAttributeRecord = propertyRecordBuilder.buildValueRecord(0l, 0l,
        "0", "", null, null, assetDownloadCountAttributeConfig, PropertyType.NUMBER);
    downloadCountAttributeRecord.setAsNumber(0);
    return downloadCountAttributeRecord;
  }
  
  private void checkDuplicate(IBaseEntityDAO workingObject,
      IReferencedVariantContextModel referencedContext, boolean isContextModified, long contextParentId) throws Exception
  {
    if (referencedContext == null)
      return;
    
    Boolean isDuplicateVariantAllowed = referencedContext.getIsDuplicateVariantAllowed();
    
    IBaseEntityDTO baseEntityDTO = workingObject.getBaseEntityDTO();
    
    IContextualDataDTO context = baseEntityDTO.getContextualObject();
    
    // context field check
    checkContextFieldsExists(context.getContextTagValues(), context, referencedContext);
    
    if ((isDuplicateVariantAllowed != null && isDuplicateVariantAllowed) || !isContextModified) {
      return;
    }
    if (Boolean.FALSE.equals(referencedContext.getIsTimeEnabled()) && referencedContext.getTags().isEmpty()) {
      return;
    }
    checkDuplicateVariant(baseEntityDTO, referencedContext, contextParentId);
  }
  
  private void checkDuplicateVariant(IBaseEntityDTO baseEntityDTO,
      IReferencedVariantContextModel context, long contextParentId) throws Exception
  {
    IContextualDataDTO currentContextualVariantObject = baseEntityDTO.getContextualObject();
    validateTimeRange(currentContextualVariantObject.getContextStartTime(),
        currentContextualVariantObject.getContextEndTime());
    
    long currentVariantID = baseEntityDTO.getBaseEntityIID();
    List<IBaseEntityDTO> similarVariantInstances = getSimilarVariantInstances(baseEntityDTO,
        context, contextParentId);
    
    for (IBaseEntityDTO similarVariantInstance : similarVariantInstances) {
      long similarVariantInstanceId = similarVariantInstance.getBaseEntityIID();
      // if during save same tag is selected again
      if (currentVariantID != 0 && similarVariantInstanceId == currentVariantID) {
        continue;
      }
      IContextualDataDTO contextualObject = similarVariantInstance.getContextualObject();
      
      if (checkIfRangeIsSame(currentContextualVariantObject, contextualObject)) {
        throw new DuplicateVariantExistsException();
      }
      else if (checkIfSupersetRangeExist(currentContextualVariantObject, contextualObject)) {
        // If variant being created/updated is external and one which is being
        // found as superset is non external,
        // then do nothing..
        throw new DuplicateVariantExistsException();
      }
      else if (checkIfSubsetRangeExist(currentContextualVariantObject, contextualObject)) {
        // If variant being created/updated is external and one which is being
        // found as subset is non external
        // then add to subsets array to adapt properties values, else throw
        // exception..
        throw new DuplicateVariantExistsException();
      }
      else if (checkIfOverlapRangeExist(currentContextualVariantObject, contextualObject)) {
        // If variant being created/updated is external and one which is being
        // found as overlapping is non external,
        // then ignore overlapping variants as we cannot adapt the properties
        // values.
        throw new DuplicateVariantExistsException();
      }
      else {
        // Means totally different timeRange.. Do nothing..
        continue;
      }
    }
  }
  
  private Boolean checkIfDateIsInBetween(Long date1, Long checkDate, Long date2)
  {
    Date d1 = new Date(date1);
    Calendar calender1 = Calendar.getInstance();
    calender1.setTime(d1);
    int day1 = calender1.get(Calendar.DAY_OF_MONTH);
    int month1 = calender1.get(Calendar.MONTH);
    int year1 = calender1.get(Calendar.YEAR);
    
    Date d2 = new Date(date2);
    Calendar calender2 = Calendar.getInstance();
    calender2.setTime(d2);
    int day2 = calender2.get(Calendar.DAY_OF_MONTH);
    int month2 = calender2.get(Calendar.MONTH);
    int year2 = calender2.get(Calendar.YEAR);
    
    Date d3 = new Date(checkDate);
    Calendar calender3 = Calendar.getInstance();
    calender3.setTime(d3);
    int checkDay = calender3.get(Calendar.DAY_OF_MONTH);
    int checkMonth = calender3.get(Calendar.MONTH);
    int checkYear = calender3.get(Calendar.YEAR);
    
    if ((day1 == checkDay && month1 == checkMonth && year1 == checkYear)
        || (day2 == checkDay && month2 == checkMonth && year2 == checkYear)) {
      return false;
    }
    return (calender3.after(calender1) && calender3.before(calender2));
  }
  
  private boolean checkIfDateIsSame(Long date1, Long date2)
  {
    Date d1 = new Date(date1);
    Calendar calender1 = Calendar.getInstance();
    calender1.setTime(d1);
    int day1 = calender1.get(Calendar.DAY_OF_MONTH);
    int month1 = calender1.get(Calendar.MONTH);
    int year1 = calender1.get(Calendar.YEAR);
    
    Date d2 = new Date(date2);
    Calendar calender2 = Calendar.getInstance();
    calender2.setTime(d2);
    int day2 = calender2.get(Calendar.DAY_OF_MONTH);
    int month2 = calender2.get(Calendar.MONTH);
    int year2 = calender2.get(Calendar.YEAR);
    
    return (day1 == day2 && month1 == month2 && year1 == year2);
  }
  
  private boolean checkIfOverlapRangeExist(IContextualDataDTO timeRange1,
      IContextualDataDTO timeRange2) throws InvalidTimeRangeException
  {
    Long from1 = (Long) timeRange1.getContextStartTime();
    Long to1 = (Long) timeRange1.getContextEndTime();
    Long from2 = (Long) timeRange2.getContextStartTime();
    Long to2 = (Long) timeRange2.getContextEndTime();
    
    // if all are not null and any became null then they are not same
    if (from1 == 0 || from2 == 0 || to1 == 0 || to2 == 0) {
      return false;
    }
    validateTimeRange(from1, to1);
    validateTimeRange(from2, to2);
    
    return ((checkIfDateIsSame(from1, to2) || checkIfDateIsInBetween(from1, from2, to1))
        || (checkIfDateIsSame(from2, to1) || checkIfDateIsInBetween(from1, to2, to1)));
  }
  
  private boolean checkIfSubsetRangeExist(IContextualDataDTO timeRange1,
      IContextualDataDTO timeRange2) throws InvalidTimeRangeException
  {
    Long from1 = (Long) timeRange1.getContextStartTime();
    Long to1 = (Long) timeRange1.getContextEndTime();
    Long from2 = (Long) timeRange2.getContextStartTime();
    Long to2 = (Long) timeRange2.getContextEndTime();
    
    validateTimeRange(from1, to1);
    validateTimeRange(from2, to2);
    // if all are not null and any became null then they are not same
    if (from1 == 0 || from2 == 0 || to1 == 0 || to2 == 0) {
      return false;
    }
    
    return ((checkIfDateIsSame(from1, from2) || checkIfDateIsInBetween(from2, from1, to2))
        && (checkIfDateIsSame(to1, to2) || checkIfDateIsInBetween(from2, to1, to2)));
  }
  
  private boolean checkIfSupersetRangeExist(IContextualDataDTO timeRange1,
      IContextualDataDTO timeRange2) throws InvalidTimeRangeException
  {
    Long from1 = (Long) timeRange1.getContextStartTime();
    Long to1 = (Long) timeRange1.getContextEndTime();
    Long from2 = (Long) timeRange2.getContextStartTime();
    Long to2 = (Long) timeRange2.getContextEndTime();
    
    validateTimeRange(from1, to1);
    validateTimeRange(from2, to2);
    // if all are not 0 and any became 0 then they are not same
    if (from1 == 0 || from2 == 0 || to1 == 0 || to2 == 0) {
      return false;
    }
    
    return ((checkIfDateIsSame(from1, from2) || checkIfDateIsInBetween(from1, from2, to1))
        && (checkIfDateIsSame(to1, to2) || checkIfDateIsInBetween(from1, to2, to1)));
  }
  
  private boolean checkIfRangeIsSame(IContextualDataDTO timeRange1, IContextualDataDTO timeRange2)
  {
    Long from1 = (Long) timeRange1.getContextStartTime();
    Long to1 = (Long) timeRange1.getContextEndTime();
    Long from2 = (Long) timeRange2.getContextStartTime();
    Long to2 = (Long) timeRange2.getContextEndTime();
    // if all became null then time range is equal
    if (from1 == 0 && from2 == 0 && to1 == 0 && to2 == 0) {
      return true;
    }
    
    // if all are not null and any became null then they are not same
    if (from1 == 0 || from2 == 0 || to1 == 0 || to2 == 0) {
      return false;
    }
    
    return (checkIfDateIsSame(from1, from2) && checkIfDateIsSame(to1, to2));
  }
  
  private List<IBaseEntityDTO> getSimilarVariantInstances(IBaseEntityDTO baseEntityDTO,
      IReferencedVariantContextModel context, long contextParentId) throws RDBMSException
  {
    IContextualDataDTO contextualObject = baseEntityDTO.getContextualObject();
    Set<ITagDTO> contextTagValues = contextualObject.getContextTagValues();
    
    Set<String> relevantTagValueIds = get100RelevanceContextTagValueIDs(contextTagValues);
    
    long parentIID = baseEntityDTO.getParentIID() == 0 ? contextParentId : baseEntityDTO.getParentIID();
    String searchExpression = generateSearchExpressionForDuplicate(context.getCode(), parentIID);
    IRDBMSOrderedCursor<IBaseEntityDTO> contextualChildren = localeCatalogDao.getAllEntitiesBySearchExpression(searchExpression, true);
    
    Long count = contextualChildren.getCount();
    
    List<IBaseEntityDTO> similiarVariants = new ArrayList<>();
    List<IBaseEntityDTO> variants = contextualChildren.getNext(0, count.intValue());
    if (contextTagValues.isEmpty() && context.getTags()
        .isEmpty()) {
      return variants;
    }
    for (IBaseEntityDTO variant : variants) {
      Set<String> contextTagValueIDs = get100RelevanceContextTagValueIDs(
          variant.getContextualObject()
              .getContextTagValues());
      boolean isRelevanceSame = SetUtils.isEqualSet(relevantTagValueIds, contextTagValueIDs);
      if (isRelevanceSame) {
        similiarVariants.add(variant);
      }
    }
    return similiarVariants;
  }
  
  private String generateSearchExpressionForDuplicate(String contextId, Long parentIID)
  {
    StringBuilder baseQuery = getBaseQuery(BaseType.UNDEFINED);
    baseQuery.append(String.format(" $entity has [X> %s $iid=%d] ", contextId, parentIID));
    return baseQuery.toString();
  }
  
  private StringBuilder getBaseQuery(BaseType baseType)
  {
    StringBuilder searchExpression = new StringBuilder();
    //Locale and Base Type Scope Expression
    String baseTypeExpression = baseType.equals(BaseType.UNDEFINED) ? "" : String.format("$basetype = %s", getBaseTypeTokens(baseType));
    searchExpression.append(String.format(" select %s $catalog=%s $locale=%s ", baseTypeExpression, transactionData.get(PHYSICAL_CATALOG_ID), transactionData.get("dataLanguage")));
    return searchExpression;
  }
  
  private String getBaseTypeTokens(BaseType baseType)
  {
    switch(baseType) {
      case ARTICLE:
        return  "$article";
      case ASSET:
        return "$asset";
      case SUPPLIER:
        return "$supplier";
      case TARGET:
        return "$target";
      case TEXT_ASSET:
        return "$textasset";
      default:
        return "";
    }
  }
  
  private Set<String> get100RelevanceContextTagValueIDs(Set<ITagDTO> contextTagValues)
  {
    return contextTagValues.stream()
        .filter(x -> x.getRange() == 100)
        .map(x -> x.getTagValueCode())
        .collect(Collectors.toSet());
  }
  
  private void validateTimeRange(IContextualDataDTO context) throws InvalidTimeRangeException
  {
    if (context == null) {
      return;
    }
    Long from = (Long) context.getContextStartTime();
    Long to = (Long) context.getContextEndTime();
    
    if ((from != null && to != null && from > to) || (from == null && to != null)
        || (from != null && to == null)) {
      throw new InvalidTimeRangeException();
    }
  }
  
  private void validateTimeRange(Long from, Long to) throws InvalidTimeRangeException
  {
    if ((from != null && to != null && from > to) || (from == null && to != null)
        || (from != null && to == null)) {
      throw new InvalidTimeRangeException();
    }
  }
  
  private void checkContextFieldsExists(Set<ITagDTO> set, IContextualDataDTO context,
      IReferencedVariantContextModel referencedContext) throws Exception
  {
    Boolean isTimeEnabled = referencedContext.getIsTimeEnabled();
    List<IReferencedVariantContextTagsModel> tags = referencedContext.getTags();
    
    // is contextTags is empty then isContextTagSelecteds = true as no need to
    // check it
    Boolean isContextTagSelecteds = tags.isEmpty();
    
    List<String> relevance100TagValue = set.stream()
        .filter(x -> x.getRange() == 100)
        .map(x -> x.getTagValueCode())
        .collect(Collectors.toList());
    
    for (IReferencedVariantContextTagsModel tag : tags) {
      isContextTagSelecteds = !ListUtils.intersection(relevance100TagValue, tag.getTagValueIds())
          .isEmpty();
      break;
    }
    
    // If from date and to date column are empty then default values are set.
    IDefaultTimeRange defaultTimeRange = referencedContext.getDefaultTimeRange();
    if (defaultTimeRange != null) {
      Boolean isCurrentTime = defaultTimeRange.getIsCurrentTime();
      
      if (context.getContextStartTime() == 0) {
        if (isCurrentTime != null && isCurrentTime) {
          context.setContextStartTime(new Date().getTime());
        }
        else if (defaultTimeRange.getFrom() != null) {
          context.setContextEndTime(defaultTimeRange.getFrom());
        }
      }
      
      if (context.getContextEndTime() == 0 && defaultTimeRange.getTo() != null) {
        context.setContextEndTime(defaultTimeRange.getTo());
      }
    }
    // if context is not time enabled then no need to check context
    Boolean isContextTimeRangeEntered = !isTimeEnabled;
    if (isTimeEnabled && context.getContextStartTime() > 0 && context.getContextEndTime() > 0) {
      validateTimeRange(context);
      isContextTimeRangeEntered = true;
    }
    
    if (!isContextTagSelecteds || !isContextTimeRangeEntered) {
      throw new EmptyMandatoryFieldsException();
    }
  }
  
  private void fillContextualData(IBaseEntityDTO baseEntityDTO,
      IReferencedVariantContextModel variantContextModel, Map<String, ITag> referencedTags,
      Map<String, IReferencedSectionElementModel> referencedElements, IBaseEntityDAO baseEntityDAO)
      throws RDBMSException
  {
    IContextualDataDTO contextualObject = baseEntityDTO.getContextualObject();
    IDefaultTimeRange defaultTimeRange = variantContextModel.getDefaultTimeRange();
    if (defaultTimeRange != null) {
      contextualObject
          .setContextEndTime(defaultTimeRange.getTo() != null ? defaultTimeRange.getTo() : 0);
      contextualObject
          .setContextStartTime(defaultTimeRange.getFrom() != null ? defaultTimeRange.getFrom() : 0);
    }
    fillContextTagValues(variantContextModel, referencedTags, referencedElements, baseEntityDAO,
        contextualObject);
  }
  
  private void fillContextTagValues(IReferencedVariantContextModel variantContextModel,
      Map<String, ITag> referencedTags,
      Map<String, IReferencedSectionElementModel> referencedElements, IBaseEntityDAO baseEntityDAO,
      IContextualDataDTO contextualObject) throws RDBMSException
  {
    List<IReferencedVariantContextTagsModel> tags = variantContextModel.getTags();
    Set<ITagDTO> contextTagValues = new HashSet<>();
    for (IReferencedVariantContextTagsModel tag : tags) {
      String tagId = tag.getTagId();
      
      IReferencedSectionElementModel referencedElement = referencedElements.get(tagId);
      // if default value is not there no need to create contextual tag
      if (referencedElement == null) {
        continue;
      }
      List<IIdRelevance> defaultValue = ((ReferencedSectionTagModel) referencedElement)
          .getDefaultValue();
      
      if (defaultValue.isEmpty()) {
        continue;
      }
      
      List<String> defaultValueIds = defaultValue.stream()
          .map(defaultVal -> defaultVal.getId())
          .collect(Collectors.toList());
      
      ITag referencedTag = referencedTags.get(tagId);
      List<ITagValue> tagValues = referencedTag.getTagValues();
      
      for (ITagValue tagValue : tagValues) {
        int relevance = defaultValueIds.contains(tagValue.getId()) ? 100 : 0;
        contextTagValues.add(baseEntityDAO.newTagDTO(relevance, tagValue.getCode()));
      }
    }
    contextualObject.setContextTagValues(contextTagValues.toArray(new ITagDTO[contextTagValues.size()]));
  }
  
  
  public Map<String, IVariantReferencedInstancesModel> getReferencedInstances(
      IBaseEntityDAO baseEntityDAO, IContextInstance contextInstance) throws RDBMSException
  {
    // FIX ME: Change to get linked entities.
    Set<IBaseEntityDTO> contextualLinkedEntities = baseEntityDAO.getContextualLinkedEntities();
    List<IIdAndBaseType> linkedInstances = contextInstance.getLinkedInstances();
    Map<String, IVariantReferencedInstancesModel> referencedInstances = new HashMap<>();
    
    if (contextualLinkedEntities != null) {
      
      for (IBaseEntityDTO contextualLinkedEntity : contextualLinkedEntities) {
        VariantReferencedInstancesModel variantReferencedInstancesModel = new VariantReferencedInstancesModel();
        String id = String.valueOf(contextualLinkedEntity.getBaseEntityIID());
        referencedInstances.put(id, variantReferencedInstancesModel);
        
        BaseType baseType = contextualLinkedEntity.getBaseType();
        
        String baseTypeString = BaseEntityUtils.getBaseTypeString(baseType);
        variantReferencedInstancesModel.setBaseType(baseTypeString);
        variantReferencedInstancesModel.setId(id);
        variantReferencedInstancesModel.setTypes(getTypes(contextualLinkedEntity));
        variantReferencedInstancesModel.setName(contextualLinkedEntity.getBaseEntityName());
        IIdAndBaseType idModel = new IdAndBaseType(id, baseTypeString);
        linkedInstances.add(idModel);
      }
    }
    return referencedInstances;
  }
  
  private List<String> getTypes(IBaseEntityDTO contextualLinkedEntity)
  {
    List<String> types = contextualLinkedEntity.getOtherClassifiers()
        .stream()
        .filter(x -> x.getClassifierType()
            .equals(ClassifierType.CLASS))
        .map(x -> x.getCode())
        .collect(Collectors.toList());
    
    types.add(contextualLinkedEntity.getNatureClassifier()
        .getCode());
    return types;
  }
  
  @SuppressWarnings("unchecked")
  protected List<IPropertyRecordDTO> addMetadataAttributesToAssetInstanceAttributes(
      Map<String, Object> metadataMap, IBaseEntityDAO baseEntityDAO,
      Map<String, Object> configDetails) throws Exception
  {
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO,
        configDetails, PropertyRecordType.DEFAULT);
    List<String> metadataAttributeIds = Arrays.asList(SystemLevelIds.METADATA_ATTRIBUTES_IDS);
    Map<String, Object> referencedAttributes = (Map<String, Object>) configDetails
        .get("referencedAttributes");
    Set<String> referencedAttributeIds = referencedAttributes.keySet();
    
    if (metadataMap == null) {
      return new ArrayList<>();
    }
    
    InputStream stream = this.getClass()
        .getClassLoader()
        .getResourceAsStream("metadataPropertyMapping.json");
    
    JSONParser jsonParser = new JSONParser();
    Map<String, Object> metadataPropertyMapping = (JSONObject) jsonParser
        .parse(new InputStreamReader(stream, StandardCharsets.UTF_8));
    
    Map<String, Object> propertyMap = (Map<String, Object>) metadataPropertyMapping
        .get("propertyMap");
    List<String> priorityList = (List<String>) metadataPropertyMapping.get("priority");
    
    Map<String, Object> convertedMap = MetadataUtils.convertMetadataIntoMap(metadataMap,
        priorityList);
    
    List<IPropertyRecordDTO> metaDataAttributes = new ArrayList<>();
    for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
      String attributeId = entry.getKey();
      Map<String, Object> mapping = (Map<String, Object>) entry.getValue();
      String finalValue = null;
      Double finalValueNumber = null;
      boolean valueFoundInPriority = false;
      for (String metadataKey : priorityList) {
        List<String> keyList = (List<String>) mapping.get(metadataKey);
        Map<String, Object> metadataKeyMap = (Map<String, Object>) convertedMap.get(metadataKey);
        boolean valueFoundInKeyList = false;
        if (metadataKeyMap != null && keyList != null) {
          for (String key : keyList) {
            finalValue = (String) metadataKeyMap.get(key);
            if (finalValue != null && !finalValue.equals("")) {
              valueFoundInKeyList = true;
              try {
                finalValueNumber = Double.parseDouble(finalValue);
              }
              catch (Exception e) {
                finalValueNumber = null;
              }
              break;
            }
          }
        }
        if (valueFoundInKeyList) {
          valueFoundInPriority = true;
          break;
        }
      }
      
      if (valueFoundInPriority && metadataAttributeIds.contains(attributeId)
          && referencedAttributeIds.contains(attributeId)) {
        Map<String, Object> attributeConfig = (Map<String, Object>) referencedAttributes
            .get(attributeId);
        IValueRecordDTO attributeInstance = propertyRecordBuilder.buildValueRecord(0l, 0l,
            finalValue, "", null, null, attributeConfig, PropertyType.ASSET_ATTRIBUTE);
        attributeInstance.setValue(finalValue);
        if (finalValueNumber != null) {
          attributeInstance.setAsNumber(finalValueNumber);
        }
        metaDataAttributes.add(attributeInstance);
      }
    }
    return metaDataAttributes;
  }
}
