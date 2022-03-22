package com.cs.utils.dam;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.variantcontext.IDefaultTimeRange;
import com.cs.core.config.interactor.model.asset.AssetConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.AssetServerDetailsModel;
import com.cs.core.config.interactor.model.asset.GetAssetDetailsStrategyModel;
import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetFileModel;
import com.cs.core.config.interactor.model.asset.IAssetImageKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetServerDetailsModel;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsRequestModel;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsStrategyModel;
import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.config.interactor.model.asset.MultiPartFileInfoModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedUniqueSelectorModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextTagsModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.config.interactor.model.variantcontext.TechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.cs.core.runtime.interactor.exception.assetserver.AssetFileTypeNotSupportedException;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IContentIdentifierModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCreateVariantModel;
import com.cs.core.runtime.interactor.model.variants.CreateImageVariantsModel;
import com.cs.core.runtime.interactor.model.variants.ICreateImageVariantsModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSDAMServer;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.dam.asset.processing.ImageConverter;

public class CreateImageVariantUtils {
  
  public static final String                 TAG_INSTANCE_PROPERTY_TYPE       = "com.cs.core.runtime.interactor.entity.tag.TagInstance";
  public static final String                 ASSET_INSTANCE_BASE_TYPE         = "com.cs.core.runtime.interactor.entity.klassinstance.AssetInstance";
  public static final String                 ASSET_COVERFLOW_ATTRIBUTE_ID     = "assetcoverflowattribute";
  public static final String                 ATTRIBUTE_INSTANCE_PROPERTY_TYPE = "com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance";
  public static final String                 RESOLUTION_TAG                   = "resolutiontag";
  public static final String                 IMAGE_EXTENSION_TAG              = "imageextensiontag";
  public static final String                 IMAGE_EXTENSION_ORIGINAL         = "original";
  
  //ThreadPoolExecutorUtil                     threadPoolTaskExecutorUtil;
  
  @SuppressWarnings("unchecked")
  public static IGetAssetDetailsStrategyModel getAsset(IGetAssetDetailsRequestModel dataModel)
      throws CSInitializationException, IOException, PluginException
  {
    Map<String, Object> requestMap = new HashMap<>();
    
    IAssetServerDetailsModel assetServerDetails = dataModel.getAssetServerDetails();
    Map<String, Object> assetServerDetailsMap = new HashMap<>();
    assetServerDetailsMap.put("storageUrl", assetServerDetails.getStorageURL());
    assetServerDetailsMap.put("authToken", assetServerDetails.getAuthToken());

    requestMap.put(IGetAssetDetailsRequestModel.ASSET_KEY, dataModel.getAssetKey());
    requestMap.put(IGetAssetDetailsRequestModel.ASSET_SERVER_DETAILS, assetServerDetailsMap);
    requestMap.put(IGetAssetDetailsRequestModel.CONTAINER, dataModel.getContainer());
    requestMap.put(IGetAssetDetailsRequestModel.REQUEST_HEADERS, dataModel.getRequestHeaders());
    
    Map<String, Object> responseMap = CSDAMServer.instance().getAsset(requestMap);
    
    IGetAssetDetailsStrategyModel responseModel = new GetAssetDetailsStrategyModel();
    responseModel.setInputStream((InputStream) responseMap.get(IGetAssetDetailsStrategyModel.INPUT_STREAM));
    responseModel.setResponseCode((Integer) responseMap.get(IGetAssetDetailsStrategyModel.RESPONSE_CODE));
    responseModel.setResponseHeaders((Map<String, String>) responseMap.get(IGetAssetDetailsStrategyModel.RESPONSE_HEADERS));
    return responseModel;
  }
  
  public static IGetAssetDetailsStrategyModel getAssetFromServer(IGetAssetDetailsRequestModel dataModel)
      throws CSInitializationException, PluginException, IOException
  {
    IAssetServerDetailsModel assetServerDetails = new AssetServerDetailsModel();
    IJSONContent authenticateSwiftServer = CSDAMServer.instance().getServerInformation();
    assetServerDetails.setStorageURL(authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_STORAGE_URL, ""));
    assetServerDetails.setAuthToken(authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_AUTH_TOKEN, ""));
    dataModel.setAssetServerDetails(assetServerDetails);
    return getAsset(dataModel);
  }
  
  public static Integer getResolution(String tagValueId, Map<String, ITag> tagValueMap)
  {
    Integer resolution = 0;
    ITag tag = tagValueMap.get(tagValueId);
    if (tag != null) {
      resolution = tag.getImageResolution();
    }
    return resolution;
  }
  
  public static List<IContentTagInstance> getContextualTagInstancesFromUniqueSelector(
      IReferencedUniqueSelectorModel uniqueSelector) throws Exception
  {
    List<IContentTagInstance> contextualTagInstances = new ArrayList<>();
    List<IReferencedVariantContextTagsModel> uniqueSelectorTags = uniqueSelector.getTags();
    for (IReferencedVariantContextTagsModel uniqueSelectorTag : uniqueSelectorTags) {
      String tagId = uniqueSelectorTag.getTagId();
      
      List<ITagInstanceValue> tagValues = new ArrayList<>();
      List<String> tagValueIds = uniqueSelectorTag.getTagValueIds();
      for (String tagValueId : tagValueIds) {
        ITagInstanceValue newTagValue = new TagInstanceValue();
        newTagValue.setId(RDBMSAppDriverManager.getDriver().newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix()));
        newTagValue.setTagId(tagValueId);
        newTagValue.setRelevance(100);
        tagValues.add(newTagValue);
      }
      
      IContentTagInstance contxtualTagInstance = new TagInstance();
      contxtualTagInstance.setId(RDBMSAppDriverManager.getDriver().newUniqueID(IStandardConfig.UniquePrefix.CONTEXT.getPrefix()));
      contxtualTagInstance.setBaseType(TAG_INSTANCE_PROPERTY_TYPE);
      contxtualTagInstance.setTagId(tagId);
      contxtualTagInstance.setTagValues(tagValues);
      contextualTagInstances.add(contxtualTagInstance);
    }
    return contextualTagInstances;
  }
  
  public static List<ICreateImageVariantsModel> createVariantInstances(
      Map<String, Object> contextsWithAutoCreateEnable, Map<String, Object> assetInformation,
      String path, String klassInstanceId, String transactionId, String instanceName,
      IGetConfigDetailsForCreateVariantModel configDetails,
      IContentIdentifierModel requestTransactionData, String parentId,
      IAssetConfigurationDetailsResponseModel assetConfigurationModel, List<String> tivFailure, List<String> tivWarning,
      String thumbnailPath) throws Exception
  {
    ITechnicalImageVariantWithAutoCreateEnableModel tivWithAutoCreateEnableModel = ObjectMapperUtil
        .readValue(ObjectMapperUtil.writeValueAsString(contextsWithAutoCreateEnable),
            TechnicalImageVariantWithAutoCreateEnableModel.class);
    
    IAssetInformationModel assetInformationModel = ObjectMapperUtil
        .readValue(ObjectMapperUtil.writeValueAsString(assetInformation),
            AssetInformationModel.class);
    
    return createVariantInstances(tivWithAutoCreateEnableModel, assetInformationModel, path,
        klassInstanceId, transactionId, instanceName, configDetails, requestTransactionData,
        parentId, assetConfigurationModel, tivFailure, tivWarning, thumbnailPath);
  }
  
  
  public static List<ICreateImageVariantsModel> createVariantInstances(
      ITechnicalImageVariantWithAutoCreateEnableModel contextsWithAutoCreateEnableModel,
      IAssetInformationModel coverFlowAttribute, String path, String klassInstanceId,
      String transactionId, String instanceName,
      IGetConfigDetailsForCreateVariantModel configDetails,
      IContentIdentifierModel requestTransactionData, String parentId,
      IAssetConfigurationDetailsResponseModel assetConfigurationModel, List<String> tivFailure, List<String> tivWarning,
      String thumbnailPath) throws Exception
  {
    List<ICreateImageVariantsModel> createVariantModelList = new ArrayList<>();
    Map<String, ITag> tagValueMap = contextsWithAutoCreateEnableModel.getTagValueMap();
    String destinationFilePath = null;
    List<String> successIds = new ArrayList<>();
    List<String> attributeIds = contextsWithAutoCreateEnableModel.getAttributeIds();
    List<IReferencedUniqueSelectorModel> uniqueSelectors = contextsWithAutoCreateEnableModel
        .getUniqueSelectors();
    String baseType = ASSET_INSTANCE_BASE_TYPE;
    for (IReferencedUniqueSelectorModel uniqueSelector : uniqueSelectors) {
      Integer resolution = null;
      String imageVariantExtension = null;
      try {
        List<IContentTagInstance> contextualTagInstances = getContextualTagInstancesFromUniqueSelector(
            uniqueSelector);
        String label = getLabel(instanceName, contextualTagInstances, tagValueMap);
        for (IContentTagInstance contextualTagInstance : contextualTagInstances) {
          switch (contextualTagInstance.getTagId()) {
            case RESOLUTION_TAG:
              resolution = getResolution(contextualTagInstance, tagValueMap);
              break;
            case IMAGE_EXTENSION_TAG:
              imageVariantExtension = getImageExtension(contextualTagInstance, tagValueMap);
              break;
            
            default:
              break;
          }
        }
        String variantInstanceId = RDBMSAppDriverManager.getDriver().newUniqueID(IStandardConfig.UniquePrefix.CONTEXT.getPrefix());
        
        ICreateImageVariantsModel createVaraintModel = new CreateImageVariantsModel();
        createVaraintModel.setVariantInstanceId(variantInstanceId);
        createVaraintModel.setTypes(new ArrayList<>(configDetails.getReferencedKlasses()
            .keySet()));
        createVaraintModel.setContextId(contextsWithAutoCreateEnableModel.getId());
        createVaraintModel.setBaseType(baseType);
        createVaraintModel.setName(label);
        createVaraintModel.setContextTags(contextualTagInstances);
        createVaraintModel.setId(klassInstanceId);
        createVaraintModel.setAttributeIds(attributeIds);
        createVaraintModel.setParentId(parentId);
        createVaraintModel.setEndpointId(requestTransactionData.getEndpointId());
        
        IReferencedContextModel referencedVariantContexts = configDetails
            .getReferencedVariantContexts();
        Map<String, IReferencedVariantContextModel> embeddedVariantContexts = referencedVariantContexts
            .getEmbeddedVariantContexts();
        IReferencedVariantContextModel referencedVariantContextModel = embeddedVariantContexts
            .get(contextsWithAutoCreateEnableModel.getId());
        if (referencedVariantContextModel.getIsTimeEnabled() && checkIsDefaultTimeRangeEmpty(
            contextsWithAutoCreateEnableModel.getDefaultTimeRange())) {
          return null;
        }
        IInstanceTimeRange timeRange = getTimeRange(contextsWithAutoCreateEnableModel);
        createVaraintModel.setTimeRange(timeRange);
        
        createAndFillMetaAttributes(attributeIds, createVaraintModel);
        
        String basename = FilenameUtils.getBaseName(coverFlowAttribute.getFileName()) + "_"+ resolution;
        
        if (imageVariantExtension.equals(IMAGE_EXTENSION_ORIGINAL)) {
          imageVariantExtension = FilenameUtils.getExtension(coverFlowAttribute.getFileName())
              .toLowerCase();
        }
        
        String name = basename + "." + imageVariantExtension;
        File newFile = AssetUtils.writeAssetFileOnServer(name);
        destinationFilePath = newFile.getPath();
        
        byte[] bytes = ImageConverter.changeResolution(path, destinationFilePath, resolution);
        String key = RDBMSAppDriverManager.getDriver().newUniqueID(IStandardConfig.UniquePrefix.RANDOM.getPrefix());
        IAssetFileModel assetFileModel = getAssetFileModel(configDetails, assetConfigurationModel,name, bytes, key, thumbnailPath);
        IAssetImageKeysModel keysModel = (IAssetImageKeysModel) AssetUtils.handleFile(assetFileModel);
        IAssetInformationModel assetInformation = createImageAttribute(coverFlowAttribute,variantInstanceId, imageVariantExtension, name, keysModel);
        createVaraintModel.setAssetInformation(assetInformation);
        createVaraintModel.setMetadata(keysModel.getMetadata());
        createVaraintModel.setShouldAutoCreate(false);
        createVaraintModel.setThumbnailPath(keysModel.getThumbnailPath());
        successIds.add(variantInstanceId);
        createVariantModelList.add(createVaraintModel);
      }
      catch (AssetFileTypeNotSupportedException e) {
        tivWarning.add("Upload for asset Variant "
            + coverFlowAttribute.getFileName() + " Resolution::" + resolution + " Extension::"
            + imageVariantExtension + " is restricted.");
      }
      catch (Exception e) {
        tivFailure.add("Error create variant : " + coverFlowAttribute.getFileName()
            + " Resolution::" + resolution + " Extension::" + imageVariantExtension);
      }
      finally {
        AssetUtils.deleteFileAndDirectory(destinationFilePath);
      }
    }
    return createVariantModelList;
  }
  
  private static IAssetFileModel getAssetFileModel(IGetConfigDetailsForCreateVariantModel configDetails,
      IAssetConfigurationDetailsResponseModel assetConfigurationModel, String name,
      byte[] bytes, String key, String thumbnailPath) throws Exception
  {
    String klassId = null;
    String natureType = null;
    for(String id : configDetails.getReferencedKlasses().keySet()){
      IReferencedKlassDetailStrategyModel model = configDetails.getReferencedKlasses().get(id);
      if(model.getIsNature()){
        natureType = model.getNatureType();
        klassId = id;
        break;
      }
    }
    
    IMultiPartFileInfoModel multiPartFileInfoModel = new MultiPartFileInfoModel();
    multiPartFileInfoModel.setBytes(bytes);
    multiPartFileInfoModel.setKey(key);
    multiPartFileInfoModel.setOriginalFilename(name);
    IAssetConfigurationDetailsResponseModel assetModel = new AssetConfigurationDetailsResponseModel();
    assetModel.setExtensionConfiguration(assetConfigurationModel.getExtensionConfiguration());
    assetModel.setDetectDuplicate(assetConfigurationModel.getDetectDuplicate());
    assetModel.setKlassId(klassId);
    assetModel.setNatureType(natureType);
    IAssetFileModel assetFileModel = AssetUtils.getFileModel(multiPartFileInfoModel, null, new ArrayList<>(), assetModel);
    assetFileModel.setThumbnailPath(thumbnailPath);
    return assetFileModel;
  }
  
  private static IInstanceTimeRange getTimeRange(
      ITechnicalImageVariantWithAutoCreateEnableModel contextsWithAutoCreateEnableModel)
  {
    IInstanceTimeRange timeRange = new InstanceTimeRange();
    IDefaultTimeRange defaultTimeRange = contextsWithAutoCreateEnableModel.getDefaultTimeRange();
    if (defaultTimeRange != null) {
      Boolean isCurrentTime = defaultTimeRange.getIsCurrentTime();
      Long defaultFrom = defaultTimeRange.getFrom();
      Long defaultTo = defaultTimeRange.getTo();
      if (defaultFrom == null && defaultTo != null && isCurrentTime != null && isCurrentTime) {
        defaultFrom = System.currentTimeMillis();
        defaultTimeRange.setFrom(defaultFrom);
      }
      timeRange.setFrom(defaultFrom);
      timeRange.setTo(defaultTo);
    }
    return timeRange;
  }
  
  /*  private Integer setResolution(List<IContentTagInstance> contextualTagInstances)
  {
    Integer resolution = null;
    for (IContentTagInstance contextualTagInstance : contextualTagInstances) {
      switch (contextualTagInstance.getTagId()) {
        case SystemLevelIds.RESOLUTION_TAG:
          resolution = getResolution(contextualTagInstance);
          break;
  
        default:
          break;
      }
    }
    return resolution;
  }*/
  
  private static void createAndFillMetaAttributes(List<String> attributeIds,
      ICreateVariantModel createVaraintModel) throws Exception
  {
    for (String attributeId : attributeIds) {
      IAttributeInstance metaDataAttribute = new AttributeInstance();
      metaDataAttribute.setId(RDBMSAppDriverManager.getDriver().newUniqueID(IStandardConfig.UniquePrefix.ATTRIBTUE.getPrefix()));
      metaDataAttribute.setAttributeId(attributeId);
      metaDataAttribute.setBaseType(ATTRIBUTE_INSTANCE_PROPERTY_TYPE);
      metaDataAttribute.setVariantInstanceId(createVaraintModel.getVariantInstanceId());
      metaDataAttribute.setValue("");
      createVaraintModel.getAttributes()
          .add(metaDataAttribute);
    }
  }
  
  private static IAssetInformationModel createImageAttribute(IAssetInformationModel coverFlowAttribute,
      String variantInstanceId, String ext, String name, IAssetImageKeysModel keysModel) throws Exception
  {
    IAssetInformationModel assetInformation = new AssetInformationModel();
    assetInformation.setAssetObjectKey(keysModel.getImageKey());
    assetInformation.setFileName(name);
    assetInformation.setType(coverFlowAttribute.getType());
    assetInformation.setHash(keysModel.getHash());
    
    HashMap<String, String> properties = new HashMap<>();
    String height = keysModel.getHeight()
        .toString();
    String width = keysModel.getWidth()
        .toString();
    properties.put("extension", ext);
    properties.put("height", height);
    properties.put("width", width);
    
    assetInformation.setProperties(properties);
    if (keysModel.getThumbKey() != null) {
      assetInformation.setThumbKey(keysModel.getThumbKey());
    }
    return assetInformation;
  }
  
  private static String getImageExtension(IContentTagInstance contextualTagInstance,
      Map<String, ITag> tagValueMap)
  {
    List<ITagInstanceValue> tagValues = contextualTagInstance.getTagValues();
    for (ITagInstanceValue iTagInstanceValue : tagValues) {
      if (iTagInstanceValue.getRelevance() == 100) {
        return getImageExtension(iTagInstanceValue.getTagId(), tagValueMap);
      }
    }
    
    return null;
  }
  
  private static String getImageExtension(String tagId, Map<String, ITag> tagValueMap)
  {
    String imageExtension = null;
    ITag iTag = tagValueMap.get(tagId);
    if (iTag != null) {
      imageExtension = iTag.getImageExtension();
    }
    return imageExtension;
  }
  
  private static Integer getResolution(IContentTagInstance contextualTagInstance,
      Map<String, ITag> tagValueMap)
  {
    List<ITagInstanceValue> tagValues = contextualTagInstance.getTagValues();
    for (ITagInstanceValue iTagInstanceValue : tagValues) {
      if (iTagInstanceValue.getRelevance() == 100) {
        return getResolution(iTagInstanceValue.getTagId(), tagValueMap);
      }
    }
    
    return null;
  }
  
  public static String getLabel(String instanceName, List<IContentTagInstance> contextualTagInstances,
      Map<String, ITag> tagValueMap)
  {
    String label = instanceName;
    int count = 0;
    for (IContentTagInstance contentTagInstance : contextualTagInstances) {
      List<ITagInstanceValue> tagValues = contentTagInstance.getTagValues();
      for (ITagInstanceValue tagValue : tagValues) {
        if (tagValue.getRelevance() == 0) {
          continue;
        }
        if (count == 2) {
          break;
        }
        ITag tag = tagValueMap.get(tagValue.getTagId());
        String tagValueLabel = tag.getLabel();
        label = label + "_" + tagValueLabel;
        count++;
      }
      if (count == 2) {
        break;
      }
    }
    return label;
  }
  
  private static boolean checkIsDefaultTimeRangeEmpty(IDefaultTimeRange defaultTimeRange)
  {
    if (defaultTimeRange == null || defaultTimeRange.getTo() == null) {
      return true;
    }
    if ((defaultTimeRange.getIsCurrentTime() == null || !defaultTimeRange.getIsCurrentTime())
        && defaultTimeRange.getFrom() == null) {
      return true;
    }
    return false;
  }
}
