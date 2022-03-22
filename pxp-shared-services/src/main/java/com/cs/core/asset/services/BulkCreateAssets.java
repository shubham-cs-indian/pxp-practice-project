package com.cs.core.asset.services;

import com.cs.constants.Constants;
import com.cs.constants.DAMConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.asset.iservices.IBulkCreateAssets;
import com.cs.core.asset.services.PropertyRecordBuilder.PropertyRecordType;
import com.cs.core.config.interactor.model.asset.*;
import com.cs.core.config.interactor.model.variantcontext.GetConfigDetailsForAutoCreateTIVResponseModel;
import com.cs.core.config.interactor.model.variantcontext.IGetConfigDetailsForAutoCreateTIVResponseModel;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.collection.idao.ICollectionDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.ICollectionDTO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.configuration.ContentIdentifierModel;
import com.cs.core.runtime.interactor.model.configuration.IContentIdentifierModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCreateVariantModel;
import com.cs.core.runtime.interactor.model.variants.ICreateImageVariantsModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.dam.AssetUtils;
import com.cs.utils.dam.CreateImageVariantUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SuppressWarnings("unchecked")
public class BulkCreateAssets implements IBulkCreateAssets {
  
  private static final String ASSET_OBJECT_KEY = "assetObjectKey";
  private static final String  ENDPOINT_ID         = "endpointId";
  private static final String  PORTAL_ID           = "portalId";
  private static final String  PHYSICAL_CATALOG_ID = "physicalCatalogId";
  private static final String  ORGANIZATION_CODE   = "organizationCode";
  private static final String  USER_ID             = "userId";
  private static final String  LOCALE_ID           = "localeId";
  private static final String  EXTENSION           = "extension";
  private static final String  KLASS_ID            = "klassId";
  private static final String  FILE_NAME           = "fileName";
  protected static String      sessionID           = "session#" + new Random().nextInt();
  private static final Integer assetCounter        = 0;
  private ILocaleCatalogDAO    localeCatalogDao;
  
  private String               userId;
  private String               localeId;
  private String               physicalCatalogId;
  private String               organizationCode;
  private String               endpointId;
  private String               systemId;
  private String               logicalCatalogId;
  private String               portalId;
  private String               parentTransactionId;
  private List<String>         collectionIds = new ArrayList<>();
  
  public void initializeRDBMSComponents() throws RDBMSException
  {
    IUserSessionDAO userSession = RDBMSAppDriverManager.getDriver()
        .newUserSessionDAO();
    IUserSessionDTO session = userSession.openSession(userId, sessionID);
    ILocaleCatalogDTO localeCatalogDto = userSession.newLocaleCatalogDTO(localeId, physicalCatalogId, organizationCode);
    // Get Locale CatelogDAO Interface
    localeCatalogDao = userSession.openLocaleCatalog(session, localeCatalogDto);
  }
  
  public BulkCreateAssets(String userId, String localeId, String physicalCatalogId,
      String organizationId, String endpointId, String systemId,
      String logicalCatalogId, String portalId, String parentTransactionId, List<String> collectionIds)
  {
    this.userId = userId;
    this.localeId = localeId;
    this.physicalCatalogId = physicalCatalogId;
    this.organizationCode = organizationId;
    this.endpointId = endpointId;
    this.systemId = systemId;
    this.logicalCatalogId = logicalCatalogId;
    this.portalId = portalId;
    this.parentTransactionId = parentTransactionId;
    this.collectionIds = collectionIds;
  }
  
  /**
   * Fill up entity information from asset content...
   * 
   * @param baseEntityDAO
   * @param modifiedContentAttributeInstance
   * @throws RDBMSException
   * @throws CSFormatException
   */
  protected void fillEntityExtension(IBaseEntityDAO baseEntityDAO,
      Map<String, Object> modifiedContentAttributeInstance) throws RDBMSException, CSFormatException
  {
    String assetObjectKey = (String) modifiedContentAttributeInstance.get(IAssetKeysModel.IMAGE_KEY);
    String fileName = (String) modifiedContentAttributeInstance.get(FILE_NAME);
    String fileExtension = FilenameUtils.getExtension(fileName);
    String hash = (String) modifiedContentAttributeInstance.get("hash");
    
    Map<String, String> properties = new HashMap<>();
    String type = (String) modifiedContentAttributeInstance.get("type");
    if (type.equals("Image")) {
      if(modifiedContentAttributeInstance.get("height")!= null) {
        properties.put("height", modifiedContentAttributeInstance.get("height")
            .toString());
      }
      if(modifiedContentAttributeInstance.get("width")!= null) {
        properties.put("width", modifiedContentAttributeInstance.get("width")
            .toString());
      }
      
    }
    else if (type.equals("Video")) {
      String mp4Key = (String) modifiedContentAttributeInstance.get(IAssetVideoKeysModel.MP4_KEY);
      if (mp4Key != null) {
        properties.put("mp4", mp4Key);
      }
      else {
        properties.put("mp4", assetObjectKey);
      }
      
    }
    properties.put(EXTENSION, fileExtension);
    properties.put("status", Integer.toString(0));
    String thumbKey = (String) modifiedContentAttributeInstance.get(IAssetKeysModel.THUMB_KEY);
    
    Map<String, Object> imageAttr = new HashMap<>();
    imageAttr.put(ASSET_OBJECT_KEY, assetObjectKey);
    imageAttr.put(FILE_NAME, fileName);
    imageAttr.put("properties", properties);
    imageAttr.put("thumbKey", thumbKey);
    imageAttr.put("type", type);
    imageAttr.put("previewImageKey",
        (String) modifiedContentAttributeInstance.get(IAssetDocumentKeysModel.PREVIEW_KEY));
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    baseEntityDTO.setHashCode(hash);
    baseEntityDTO.setEntityExtension(JSONObject.toJSONString(imageAttr));
    baseEntityDAO.updatePropertyRecords(new IPropertyRecordDTO[] {});
  }
  
  public Map<String, Object> bulkCreate(Map<String, Object> bulklistModel)
      throws Exception
  {
    initializeRDBMSComponents();
    
    Set<Long> duplicateIIdSet = new HashSet<Long>();
    Map<String, Boolean> isHashDuplicatedMap = (Map<String, Boolean>) bulklistModel.get("isHashDuplicatedMap");
    Map<String, Map<String, Object>> typeIdConfigDetailsMapping = new HashMap<>();
    List<String> contextIds = new ArrayList<>();
    Map<String, IGetConfigDetailsForCreateVariantModel> configDetailsMap = new HashMap<>();
    Map<Long, Object> tivDuplicateDetectionInfoMap = new HashMap<>();
    
    List<Map<String, Object>> assetInstances = (List<Map<String, Object>>) bulklistModel
        .get("assetInstances");
    fillAssetInstanceConfigDetails(typeIdConfigDetailsMapping, assetInstances, contextIds);
    
    if(!contextIds.isEmpty()) {
      configDetailsMap = getConfigDetailsForTIVCreation(contextIds);
    }
    
    //Get configured extensions list from orient.
    Map<String, Object> assetConfigDetails = CSConfigServer.instance()
        .request(new HashMap<>(), FETCH_ASSET_CONFIGURATION_DETAILS, null);
    IAssetConfigurationDetailsResponseModel assetConfigurationDetailsResponseModel = ObjectMapperUtil
        .readValue(ObjectMapperUtil.writeValueAsString(assetConfigDetails),
            AssetConfigurationDetailsResponseModel.class);
    
    List<String> failure = new ArrayList<>();
    List<String> success = new ArrayList<>();
    List<String> tivSuccess = new ArrayList<>();
    List<String> tivFailure = new ArrayList<>();
    List<String> tivWarning = new ArrayList<>();
    List<Long> successInstanceIIds = new ArrayList<>();
    
    for (Map<String, Object> model : assetInstances) {
      try {
        String klassType = (String) model.get(KLASS_ID);
        String newInstanceName = FilenameUtils.getBaseName((String) model.get(FILE_NAME));
        boolean isDuplicate = false;
        if(isHashDuplicatedMap != null) {
          isDuplicate = isHashDuplicatedMap.get(model.get("hash")) == null ? false : isHashDuplicatedMap.get(model.get("hash"));
        }
        
        String id = model.get("id") != null ? (String) model.get("id")
            : RDBMSAppDriverManager.getDriver()
                .newUniqueID(BaseType.ASSET.getPrefix());
        model.put("id", id);
        
        Map<String, Object> configDetails = typeIdConfigDetailsMapping.get(klassType);
        if (StringUtils.isEmpty(newInstanceName)) {
          newInstanceName = getDefaultInstanceNameByConfigDetails(configDetails, klassType);
          newInstanceName = newInstanceName + " " + assetCounter;
        }
        model.put("name", newInstanceName);
        IBaseEntityDTO assetInstance = createAssetInstance(configDetails, model, id);
        // image asset attribute
        
        IBaseEntityDAO assetInstanceDao = localeCatalogDao.openBaseEntity(assetInstance);
        fillEntityExtension(assetInstanceDao, model);
        
        //Auto-create TIV Creation
        long baseEntityIID = assetInstance.getBaseEntityIID();
        List<Map<String, Object>> contextsWithAutoCreateEnableList = (List<Map<String, Object>>) configDetails
            .get("technicalImageVariantContextWithAutoCreateEnable");
        createVariantForAssets(Long.toString(baseEntityIID), model, contextsWithAutoCreateEnableList,
            assetConfigurationDetailsResponseModel, configDetailsMap, Long.toString(baseEntityIID),
            tivDuplicateDetectionInfoMap, tivFailure, tivSuccess, tivWarning);
        if (isDuplicate) {
          duplicateIIdSet.add(baseEntityIID);
        }
        successInstanceIIds.add(baseEntityIID);
        success.add(id);
        
        localeCatalogDao.postUsecaseUpdate(baseEntityIID, IEventDTO.EventType.ELASTIC_UPDATE);
      }
      catch (Exception e) {
        failure.add((String) model.get("id"));
      }
    }
    
    addInstancesToCollections(successInstanceIIds);
    
    Map<String, Object> informationModel = new HashMap<>();
    informationModel.put("success", success);
    informationModel.put("successInstanceIIds", successInstanceIIds);
    informationModel.put("failure", failure);
    informationModel.put("tivDuplicateDetectionInfoMap", tivDuplicateDetectionInfoMap);
    informationModel.put("tivFailure", tivFailure);
    informationModel.put("tivSuccess", tivSuccess);
    informationModel.put("tivWarning", tivWarning);
    informationModel.put("duplicateIIds", duplicateIIdSet);
    return informationModel;
  }

  private void addInstancesToCollections(List<Long> instanceIdsForCollection) throws RDBMSException
  {
    ICollectionDAO collectionDAO = localeCatalogDao.openCollection();
    for(String collectionId : collectionIds) {
      long collectionIId = Long.parseLong(collectionId);
      ICollectionDTO collectionDTO = collectionDAO.getCollection(collectionIId, CollectionType.staticCollection);
      collectionDAO.updateCollectionRecords(collectionIId, collectionDTO, instanceIdsForCollection, new ArrayList<>());
    }
  }

  private Map<String, IGetConfigDetailsForCreateVariantModel> getConfigDetailsForTIVCreation(List<String> contextIds)
      throws Exception
  {
    Map<String, Object> getConfigDetailsForAutoCreateTIVRequestMap = new HashMap<>();
    getConfigDetailsForAutoCreateTIVRequestMap.put("contextIds", contextIds);
    getConfigDetailsForAutoCreateTIVRequestMap.put(ORGANIZATION_CODE, organizationCode);
    getConfigDetailsForAutoCreateTIVRequestMap.put(ENDPOINT_ID, endpointId);
    getConfigDetailsForAutoCreateTIVRequestMap.put(PHYSICAL_CATALOG_ID, physicalCatalogId);
    getConfigDetailsForAutoCreateTIVRequestMap.put(PORTAL_ID, portalId);
    getConfigDetailsForAutoCreateTIVRequestMap.put("baseType", Constants.ASSET_INSTANCE_BASE_TYPE);
    
    Map<String, Object> configDetailsResponse = CSConfigServer.instance()
        .request(getConfigDetailsForAutoCreateTIVRequestMap, GET_CONFIG_DETAILS_FOR_AUTO_CREATE_TIV, null);
    
    IGetConfigDetailsForAutoCreateTIVResponseModel configDetailsForAutoCreateTIVResponseModel = ObjectMapperUtil
        .readValue(ObjectMapperUtil.writeValueAsString(configDetailsResponse),
            GetConfigDetailsForAutoCreateTIVResponseModel.class);
    return configDetailsForAutoCreateTIVResponseModel.getConfigDetailsMap();
  }
  
  private void createVariantForAssets(String id, Map<String, Object> imageInfo,
      List<Map<String, Object>> contextsWithAutoCreateEnableList,
      IAssetConfigurationDetailsResponseModel assetConfigDetails,
      Map<String, IGetConfigDetailsForCreateVariantModel> configDetailsMap, String parentId,
      Map<Long, Object> tivDuplicateDetectionInfoMap, List<String> tivFailure, List<String> tivSuccess, 
      List<String> tivWarning)
  {
    String path = null;
    String imageFileName = (String) imageInfo.get(FILE_NAME);
    String fileExtension = FilenameUtils.getExtension(imageFileName);
    Map<String, Object> transactionData = new HashMap<>();
    transactionData.put(ENDPOINT_ID, endpointId);
    transactionData.put(PHYSICAL_CATALOG_ID, physicalCatalogId);
    transactionData.put(ITransactionData.ORGANIZATION_ID, organizationCode);
    transactionData.put(USER_ID, userId);
    transactionData.put(LOCALE_ID, localeId);
    
    IContentIdentifierModel requestTransactionData = new ContentIdentifierModel(organizationCode,
        physicalCatalogId, portalId, logicalCatalogId, systemId, endpointId);
    
    List<IAssetExtensionConfigurationModel> imageConfiguredExtensions = assetConfigDetails.getExtensionConfiguration().get("imageAsset");
    if (contextsWithAutoCreateEnableList.isEmpty()) return;
    if (!"Image".equals(imageInfo.get("type"))) return;
    IAssetExtensionConfigurationModel extensionConfiguration = AssetUtils.getExtensionConfigurationFromList(
        imageConfiguredExtensions, fileExtension);
    if (extensionConfiguration != null && !extensionConfiguration.getExtractRendition()) {
      tivWarning.add("The renditions could not be processed for the uploaded asset " + imageFileName);
      return;
    }
    
    String thumbnailPath = (String) imageInfo.get(DAMConstants.THUMBNAIL_PATH);
    try {
      for (Map<String, Object> contextsWithAutoCreateEnable : contextsWithAutoCreateEnableList) {
        if (CommonConstants.IMAGE_VARIANT.equals(contextsWithAutoCreateEnable.get("type"))) {
          String fileName = FilenameUtils.getBaseName(imageFileName);
          try {
            path = (String) imageInfo.get(IAssetImageKeysModel.FILE_PATH);
            Map<String, Object> assetInformation = new HashMap<>();
            assetInformation.put(IAssetInformationModel.THUMB_KEY, imageInfo.get(IAssetInformationModel.THUMB_KEY));
            assetInformation.put(IAssetInformationModel.ASSET_OBJECT_KEY, imageInfo.get(IAssetKeysModel.IMAGE_KEY));
            assetInformation.put(IAssetInformationModel.TYPE, imageInfo.get(IAssetInformationModel.TYPE));
            assetInformation.put(IAssetInformationModel.FILENAME, imageInfo.get(IAssetInformationModel.FILENAME));
            assetInformation.put(IAssetInformationModel.PREVIEW_IMAGE_KEY, imageInfo.get(IAssetInformationModel.PREVIEW_IMAGE_KEY));
            
            List<ICreateImageVariantsModel> createVariantModelList = CreateImageVariantUtils
                .createVariantInstances(contextsWithAutoCreateEnable, assetInformation, path, id,
                    parentTransactionId, fileName, configDetailsMap.get(contextsWithAutoCreateEnable.get("id")),
                    requestTransactionData, parentId, assetConfigDetails, tivFailure, tivWarning, thumbnailPath);
            
            for (ICreateImageVariantsModel createVariantModel : createVariantModelList) {
              try {
                //variant entity creation
                long baseEntityIId = VariantInstanceCreation.getInstance()
                    .createVariantInstance(createVariantModel, transactionData);
                
                Map<String, String> tivInfo = new HashMap<>();
                tivInfo.put("hash", createVariantModel.getAssetInformation().getHash());
                tivInfo.put("name", createVariantModel.getName());
                tivDuplicateDetectionInfoMap.put(baseEntityIId, tivInfo);
                tivSuccess.add("TIV generated successfully for " + createVariantModel.getName());
              }
              catch (Exception ex) {
                tivFailure.add("TIV generation failed for " + createVariantModel.getName());
              }
            }
          }
          catch (Exception e) {
            tivFailure.add("TIV generation failed for " + imageFileName);
          }
          finally {
            AssetUtils.deleteFileAndDirectory(path);
          }
        }
      } 
    }
    finally {
      if (thumbnailPath != null) {
        AssetUtils.deleteFileAndDirectory(thumbnailPath);
      }
    }
  }


  private void fillAssetInstanceConfigDetails(
      Map<String, Map<String, Object>> typeIdConfigDetailsMapping,
      List<Map<String, Object>> assetInstances, List<String> contextIds) throws CSFormatException, CSInitializationException
  {
    for (Map<String, Object> assetInstance : assetInstances) {
      String type = (String) assetInstance.get(KLASS_ID);
      if (typeIdConfigDetailsMapping.get(type) == null) {
        Map<String, Object> multiclassificationRequestModel = new HashMap<>();
        fillMultiClassificationDetails(type, multiclassificationRequestModel);
        Map<String, Object> detailsFromODB = CSConfigServer.instance()
            .request(multiclassificationRequestModel, GET_CONFIG_DETAILS_WITHOUT_PERMISSIONS, null);
        
        List<Map<String, Object>> autoCreateConfigInfoList = (List<Map<String, Object>>) detailsFromODB
            .get("technicalImageVariantContextWithAutoCreateEnable");
        autoCreateConfigInfoList.forEach(autoCreateConfigInfo -> {
          contextIds.add((String) autoCreateConfigInfo.get("id"));
        });
        
        typeIdConfigDetailsMapping.put(type, detailsFromODB);
      }
    }
  }
  
  private void fillMultiClassificationDetails(String type,
      Map<String, Object> multiclassificationRequestModel)
  {
    // TODO: Currently hard coded, after implementation of transaction data
    // pass the parameters.
    multiclassificationRequestModel.put(ENDPOINT_ID, endpointId);
    multiclassificationRequestModel.put(PHYSICAL_CATALOG_ID, physicalCatalogId);
    multiclassificationRequestModel.put(ORGANIZATION_CODE, organizationCode);
    multiclassificationRequestModel.put("userId", userId);
    multiclassificationRequestModel.put("klassIds", Arrays.asList(type));
    multiclassificationRequestModel.put("shouldSendTaxonomyHierarchies", false);
    multiclassificationRequestModel.put("selectedTaxonomyIds", new ArrayList<>());
    multiclassificationRequestModel.put("isUnlinkedRelationships", false);
    multiclassificationRequestModel.put("taxonomyIdsForDetails", new ArrayList<>());
    multiclassificationRequestModel.put("isMdm", false);
    multiclassificationRequestModel.put("tagIdTagValueIdsMap", new HashMap<>());
    multiclassificationRequestModel.put("shouldUseTagIdTagValueIdsMap", true);
    multiclassificationRequestModel.put("isGridEditable", false);
    multiclassificationRequestModel.put("collectionIds", new ArrayList<>());
    multiclassificationRequestModel.put("languageCodes", new ArrayList<>());
    
    multiclassificationRequestModel.put("parentKlassIds", new ArrayList<>());
    multiclassificationRequestModel.put("parentTaxonomyIds", new ArrayList<>());
  }
  
  private IBaseEntityDTO createAssetInstance(Map<String, Object> configDetails,
      Map<String, Object> createInstanceModel, String baseEntityID) throws Exception
  {
    Map<String, Object> referencedKlasses = (Map<String, Object>) configDetails
        .get("referencedKlasses");
    Map<String, Object> referencedNatureKlassDetail = (Map<String, Object>) referencedKlasses
        .get(createInstanceModel.get(KLASS_ID));
    
    long classifierIID = (Long) referencedNatureKlassDetail.get("classifierIID");
    String code = (String) referencedNatureKlassDetail.get("code");
    
    IClassifierDTO classifierDTO = localeCatalogDao.newClassifierDTO(classifierIID, code, ClassifierType.CLASS);
    IBaseEntityDTO baseEntityDTO = localeCatalogDao.newBaseEntityDTOBuilder(baseEntityID,
        BaseType.ASSET, classifierDTO).build();
    
    IBaseEntityDAO baseEntityDAO = localeCatalogDao.openBaseEntity(baseEntityDTO);
    
    IPropertyRecordDTO[] propertyRecords = CreateInstanceUtils.createPropertyRecordInstance(
        baseEntityDAO, configDetails, createInstanceModel);
    
    List<IPropertyRecordDTO> propertyRecordsList = Arrays.asList(propertyRecords);
    Map<String, Object> metadata = (Map<String, Object>) createInstanceModel.get("metadata");
    List<IPropertyRecordDTO> metaDataAttributes = addMetadataAttributesToAssetInstanceAttributes(
        metadata, baseEntityDAO, configDetails);
    
    IValueRecordDTO downloadCountAttributeRecord = getDownloadCountAttributeRecord(configDetails,
        baseEntityDAO);
    
    metaDataAttributes.add(downloadCountAttributeRecord);
    metaDataAttributes.addAll(propertyRecordsList);
    baseEntityDAO.createPropertyRecords(metaDataAttributes.toArray(new IPropertyRecordDTO[] {}));
    return baseEntityDTO;
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
        .get(PROPERTY_MAP);
    List<String> priorityList = (List<String>) metadataPropertyMapping.get(PRIORITY);
    
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
  
  public String getDefaultInstanceNameByConfigDetails(Map<String, Object> configDetails,
      String typeId)
  {
    Map<String, Object> object = (Map<String, Object>) configDetails.get("referencedKlasses");
    Map<String, Object> jsonParser = (Map<String, Object>) object.get(typeId);
    return (String) jsonParser.get("label");
  }
}
