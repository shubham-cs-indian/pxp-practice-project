package com.cs.di.runtime.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.cs.config.standard.IStandardConfig.StandardProperty;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.util.CollectionUtils;

import com.cs.core.asset.services.CommonConstants;
import com.cs.core.asset.services.MetadataUtils;
import com.cs.core.config.interactor.model.asset.AssetDocumentKeysModel;
import com.cs.core.config.interactor.model.asset.AssetImageKeysModel;
import com.cs.core.config.interactor.model.asset.AssetVideoKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetDocumentKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetImageKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetVideoKeysModel;
import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.config.interactor.model.asset.MultiPartFileInfoModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleAttributeMappingModel;
import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.TagDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.runtime.interactor.entity.propertyinstance.IImageAttributeInstance;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.config.interactor.model.task.IGetConfigDetailsByCodesResponseModel;
import com.cs.di.workflow.constants.DiConstants;
import com.cs.di.workflow.constants.DiDataType;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.tasks.AbstractFromPXONTask;
import com.cs.di.workflow.tasks.ITransformationTask;

@SuppressWarnings("unchecked")
public class DiTransformationUtils {
  
  public static final String DEFAULT_MEASUREMENT_UNIT = "mm";
  public static final String DEFAULT_CURRENCY         = "EUR";
   /**
   * This method first check the whether property code exist in orientDB or not,
   * if yes then load the property record.
   *
   * @param propertyCodes
   * @param validpropertyRecords
   * @param baseEntityDAO
   * @param executionStatusTable
   * @throws Exception
   */
  public static void loadPropertyRecords(List<String> propertyCodes, Set<IPropertyDTO> validpropertyRecords, IBaseEntityDAO baseEntityDAO,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable) throws Exception
  {
    for (String property : propertyCodes) {
      try {
        IPropertyDTO propertyDTO = RDBMSUtils.getPropertyByCode(property);
        validpropertyRecords.add(propertyDTO);
      }
      catch (Exception e) {
        executionStatusTable.addWarning(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN015, new String[] { property });
      }
    }
    Set<IPropertyRecordDTO> existingPropertyRecords = baseEntityDAO.getBaseEntityDTO().getPropertyRecords();
    Set<IPropertyRecordDTO> propertyRecords = new HashSet<>(existingPropertyRecords);
    baseEntityDAO.loadPropertyRecords(validpropertyRecords.toArray(new IPropertyDTO[0]));
    existingPropertyRecords.addAll(propertyRecords);
  }
  
  /**
   * Prepare DTO for tags.
   * 
   * @param entity
   * @param tags
   * @param baseEntityDAO
   * @param validpropertyRecords
   * @throws Exception
   */
  public static void prepareTagRecord(IBaseEntityDTO entity, Map<String, Object> tags, IBaseEntityDAO baseEntityDAO,
      Set<IPropertyDTO> validpropertyRecords) throws Exception
  {
    for (IPropertyDTO tag : validpropertyRecords) {
      IPropertyRecordDTO propertyRecord = entity.getPropertyRecord(tag.getIID());
      if (propertyRecord == null && tags.containsKey(tag.getCode())) {
        Object values = tags.get(tag.getCode());
        ITagsRecordDTO tagRecord = baseEntityDAO.newTagsRecordDTOBuilder(tag).build();
        createOrUpdateTagRecord(entity, tagRecord, values, baseEntityDAO, tag);
      }
      else if (propertyRecord instanceof ITagsRecordDTO) {
        ITagsRecordDTO tagRecord = (ITagsRecordDTO) propertyRecord;
        tagRecord.getTags().clear();
        Object values = tags.get(tag.getCode());
        createOrUpdateTagRecord(entity, tagRecord, values, baseEntityDAO, tag);
      }
      
    }
  }
  
  /**
   * Create Or Update tag record
   * 
   * @param entity
   * @param tagRecord
   * @param values
   * @param baseEntityDAO
   * @throws RDBMSException
   */
  private static void createOrUpdateTagRecord(IBaseEntityDTO entity, ITagsRecordDTO tagRecord, Object values, IBaseEntityDAO baseEntityDAO,IPropertyDTO tag)
      throws Exception
  {
    if (values instanceof List<?>) {
      String propertyType = tag.getPropertyType().name();
      if (PropertyType.BOOLEAN.name().equals(propertyType) || StandardProperty.isbaseunittag.name().equals(tag.getPropertyCode())
          || StandardProperty.isordertag.name().equals(tag.getPropertyCode()) || StandardProperty.issalestag.name().equals(tag.getPropertyCode())) {
        if(!((List) values).isEmpty()) {
        tagRecord.getTags()
                .add(baseEntityDAO.newTagDTO(Integer.parseInt(((List<String>) values).get(0)) == 0 ? 0 : 100,
                        RDBMSUtils.newConfigurationDAO().getTagValueByIID(tagRecord.getProperty().getPropertyIID()).get(0)));
        }     
      } else {
        for (String value : (List<String>) values) {
          tagRecord.getTags().add(baseEntityDAO.newTagDTO(100, value));
        }
      }
    }
    entity.getPropertyRecords().add(tagRecord);
  }
  
  /**
   * Prepare DTO for attributes.
   * 
   * @param entity
   * @param attributesMap
   * @param language
   * @param baseEntityDAO
   * @param validpropertyRecords
   * @param languageDependentAttribute 
   */
  public static void prepareValueRecord(IBaseEntityDTO entity, Map<String, String> attributesMap, String localeLanguage,
      IBaseEntityDAO baseEntityDAO, Set<IPropertyDTO> validpropertyRecords, Map<String, Boolean> languageDependentAttribute, IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    for (IPropertyDTO attribute : validpropertyRecords) {
      try {
      // Added check to skip tag type property
      if (attribute.getPropertyType().equals(PropertyType.TAG) || attribute.getPropertyType().equals(PropertyType.BOOLEAN)) {
        continue;
      }
      // Check for language dependent attributes
      Boolean isTranslatable = languageDependentAttribute.get(attribute.getCode());
      String language = isTranslatable ? localeLanguage : null;
      IPropertyRecordDTO propertyRecord = entity.getPropertyRecord(attribute.getIID());
      String attributeValue = attributesMap.get(attribute.getCode());
      if (propertyRecord == null && attributesMap.containsKey(attribute.getCode())) {
        propertyRecord = createValueRecord(baseEntityDAO, attribute, attributeValue, language);
        if(propertyRecord==null) {
          continue;
        }
        entity.getPropertyRecords().add(propertyRecord);
      }
      else if (propertyRecord instanceof IValueRecordDTO) {
        IValueRecordDTO value = (IValueRecordDTO) propertyRecord;
        // Handling to create a property record in different language
        if (isTranslatable && !language.equals(value.getLocaleID())) {
          propertyRecord = createValueRecord(baseEntityDAO, attribute, attributeValue, language);
          entity.getPropertyRecords().add(propertyRecord);
          continue;
        }
        if ((attributeValue.equals(""))) {
          value.setAsHTML(attributeValue);
        }
        else {
          switch (value.getProperty().getPropertyType()) {
            case DATE:
              attributeValue = Long.toString(DiUtils.getLongValueOfDateString(attributeValue));
              value.setAsNumber(Double.parseDouble(attributeValue));
              break;
            case HTML:
              value.setAsHTML(attributeValue);
              break;
            case PRICE:
            case MEASUREMENT:
            case NUMBER:
              value.setAsNumber(Double.parseDouble(attributeValue));
              break;
            default:
              break;
          }
        }
        value.setValue(attributeValue);
      }
      } catch(Exception e)
      {
        executionStatusTable.addWarning(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GENO51, new String[] { attribute.getCode() });
      }
    }
  }
  
  /**
   * Prepare DTO for classifiers.
   * 
   * @param entity
   * @param nonNatureKlassList
   * @param baseEntityDAO
   * @param configDataForKlassAndTaxonomy
   * @param executionStatusTable
   * @throws Exception
   */
  public static void prepareClassifiersForNonNatureKlass(IBaseEntityDTO entity, List<String> nonNatureKlassList,
      IBaseEntityDAO baseEntityDAO, IGetConfigDetailsByCodesResponseModel configDataForKlassAndTaxonomy,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable) throws Exception
  {
    Map<String, Object> klassConfigDetails = configDataForKlassAndTaxonomy.getKlass();
    for (String nonNatureKlass : nonNatureKlassList) {
      if (!klassConfigDetails.containsKey(nonNatureKlass)) {
        executionStatusTable.addWarning(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN015, new String[] { nonNatureKlass });
        continue;
      }
      Map<String, Object> klassMap = (Map<String, Object>) klassConfigDetails.get(nonNatureKlass);
      Long klassIID = Long.parseLong(klassMap.get(CommonConstants.CLASSIFIER_IID).toString());
      IClassifierDTO klassDTO = baseEntityDAO.newClassifierDTO(klassIID, nonNatureKlass, ClassifierType.CLASS);
      entity.getOtherClassifiers().add(klassDTO);
    }
  }
  
  /**
   * Prepare DTO for classifiers.
   * 
   * @param entity
   * @param taxonomyIds
   * @param baseEntityDAO
   * @param configDataForKlassAndTaxonomy
   * @param executionStatusTable
   * @throws Exception
   */
  public static void prepareClassifiersForTaxonomy(IBaseEntityDTO entity, List<String> taxonomyIds, IBaseEntityDAO baseEntityDAO,
      IGetConfigDetailsByCodesResponseModel configDataForKlassAndTaxonomy,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable) throws Exception
  {
    Map<String, String> taxonomyConfigDetails = configDataForKlassAndTaxonomy.getTaxonomy();
    entity.getOtherClassifiers().clear(); //this line clears all existing taxonomy
    for (String taxonomyId : taxonomyIds) {
      if (!taxonomyConfigDetails.containsKey(taxonomyId)) {
        executionStatusTable.addWarning(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN015, new String[] { taxonomyId });
        continue;
      }
      Long taxonomyIID = Long.parseLong(taxonomyConfigDetails.get(taxonomyId));
      IClassifierDTO taxonomyDTO = baseEntityDAO.newClassifierDTO(taxonomyIID, taxonomyId, ClassifierType.TAXONOMY);
      entity.getOtherClassifiers().add(taxonomyDTO);
    }
  }
  
  /**
   * Fetch asset data from given path.
   * 
   * @param filePath
   * @param sourceOfFile
   * @return
   * @throws Exception
   */
  public static IMultiPartFileInfoModel getAssetDataFromFile(String filePath, String sourceOfFile) throws Exception
  {
    String folderName = null;
    
    if (sourceOfFile.equals(ITransformationTask.FROM_URL)) {
      URL url = new URL(filePath);
      String fileName = FilenameUtils.getName(url.getPath());
      folderName = downloadFile(url, "", fileName);
      filePath = folderName + fileName;
    }
    
    MultiPartFileInfoModel multiPartFileInfoModel = new MultiPartFileInfoModel();
    if (StringUtils.isNotBlank(filePath)) {
      File imageFile = new File(filePath);
      Path path = Paths.get(filePath);
      byte[] bytes = Files.readAllBytes(path);
      multiPartFileInfoModel.setKey(UUID.randomUUID().toString());
      multiPartFileInfoModel.setBytes(bytes);
      multiPartFileInfoModel.setOriginalFilename(imageFile.getName());
    }
    if (StringUtils.isNotBlank(folderName)) {
      deleteDownloadedImage(sourceOfFile, folderName);
    }
    return multiPartFileInfoModel;
  }
  
  /**
   * Convert asset metadata into the map.
   * 
   * @param assetKeysModel
   * @return
   * @throws Exception
   */
  public static Map<String, String> convertAssetMetadataToMap(IAssetKeysModel assetKeysModel) throws Exception
  {
    Map<String, String> assetMetadataAttributes = new HashMap<>();
    Map<String, Object> metadataMap = assetKeysModel.getMetadata();
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    InputStream stream = loader.getResourceAsStream("metadataPropertyMapping.json");
    JSONParser jsonParser = new JSONParser();
    Map<String, Object> metadataPropertyMapping = (JSONObject) jsonParser.parse(new InputStreamReader(stream, "UTF-8"));
    Map<String, Object> propertyMap = (Map<String, Object>) metadataPropertyMapping.get(ITransformationTask.PROPERTY_MAP);
    List<String> priorityList = (List<String>) metadataPropertyMapping.get(ITransformationTask.PRIORITY);
    Map<String, Object> convertedMap = MetadataUtils.convertMetadataIntoMap(metadataMap, priorityList);
    for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
      String attributeId = entry.getKey();
      Map<String, Object> mapping = (Map<String, Object>) entry.getValue();
      String finalValue = null;
      for (String metadataKey : priorityList) {
        List<String> keyList = (List<String>) mapping.get(metadataKey);
        Map<String, Object> metadataKeyMap = (Map<String, Object>) convertedMap.get(metadataKey);
        if (metadataKeyMap != null && keyList != null) {
          for (String key : keyList) {
            finalValue = (String) metadataKeyMap.get(key);
            if (finalValue != null && !finalValue.equals("")) {
              assetMetadataAttributes.put(attributeId, finalValue);
              break;
            }
          }
        }
      }
    }
    return assetMetadataAttributes;
  }
  
  /**
   * Prepare asset extension data.
   * 
   * @param entity
   * @param assetKeysModel
   * @param assetUploadType
   * @return
   * @throws Exception
   */
  public static void prepareAssetExtenstionData(IBaseEntityDTO entity, IAssetKeysModel assetKeysModel, String assetUploadType)
      throws Exception
  {
    Map<String, Object> imageAttr = new HashMap<>();
    Map<String, Object> properties = new HashMap<>();
    if (assetKeysModel instanceof AssetDocumentKeysModel) {
      imageAttr.put(IImageAttributeInstance.PREVIEW_IMAGE_KEY, ((IAssetDocumentKeysModel) assetKeysModel).getPreviewKey());
      imageAttr.put(IImageAttributeInstance.FILENAME, ((AssetDocumentKeysModel) assetKeysModel).getFileName());
    }
    else if (assetKeysModel instanceof AssetImageKeysModel) {
      properties.put(ITransformationTask.WIDTH, ((IAssetImageKeysModel) assetKeysModel).getWidth().toString());
      properties.put(ITransformationTask.HEIGHT, ((IAssetImageKeysModel) assetKeysModel).getHeight().toString());
      imageAttr.put(IImageAttributeInstance.FILENAME, ((AssetImageKeysModel) assetKeysModel).getFileName());
      imageAttr.put(IAssetImageKeysModel.FILE_PATH, ((AssetImageKeysModel) assetKeysModel).getFilePath());
      imageAttr.put(IAssetImageKeysModel.THUMBNAIL_PATH, ((AssetImageKeysModel) assetKeysModel).getThumbnailPath());
    }
    else if (assetKeysModel instanceof AssetVideoKeysModel) {
      properties.put("mp4", ((IAssetVideoKeysModel) assetKeysModel).getMp4Key());
      imageAttr.put(IImageAttributeInstance.PREVIEW_IMAGE_KEY, ((IAssetVideoKeysModel) assetKeysModel).getMp4Key());
      imageAttr.put(IImageAttributeInstance.FILENAME, ((AssetVideoKeysModel) assetKeysModel).getFileName());
    }
    properties.put(ITransformationTask.STATUS, "0");
    properties.put(ITransformationTask.EXTENSION, assetKeysModel.getMetadata().get(ITransformationTask.FILE_TYPE_EXTENSION));
    imageAttr.put(IImageAttributeInstance.ASSET_OBJECT_KEY, assetKeysModel.getImageKey());
    imageAttr.put(IImageAttributeInstance.PROPERTIES, properties);
    imageAttr.put(IImageAttributeInstance.THUMB_KEY, assetKeysModel.getThumbKey());
    imageAttr.put(IImageAttributeInstance.TYPE, getAssetType(assetUploadType)); 
    entity.setEntityExtension(ObjectMapperUtil.writeValueAsString(imageAttr));
    entity.setHashCode(assetKeysModel.getHash());
  }
  
  /**
   * Get Asset type
   * 
   * @param assetUploadType
   * @return
   */
  private static String getAssetType(String assetUploadType)
  {
    switch (assetUploadType) {
      case ITransformationTask.IMAGE_ASSET:
        return CommonConstants.SWIFT_CONTAINER_IMAGE;
      case ITransformationTask.VIDEO_ASSET:
        return CommonConstants.SWIFT_CONTAINER_VIDEO;
      case ITransformationTask.DOCUMENT_ASSET:
        return CommonConstants.SWIFT_CONTAINER_DOCUMENT;
    }
    return null;
  }
  
  /**
   * Download File from given path
   * 
   * @param url
   * @param destinationPath
   * @param fileName
   * @return
   * @throws Exception
   */
  private static String downloadFile(URL url, String destinationPath, String fileName) throws Exception
  {
    String folderName = new File(destinationPath).getAbsolutePath() + "\\" + UUID.randomUUID().toString() + "\\";
    new File(folderName).mkdir();
    String destName = folderName + fileName;
    downloadFromURL(url, destName);
    return folderName;
  }
  
  /**
   * Download file from URL
   * 
   * @param url
   * @param file
   * @throws Exception
   */
  private static void downloadFromURL(URL url, String file) throws Exception
  {
    try (ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        FileOutputStream fileOutStream = new FileOutputStream(file)) {
      fileOutStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
      throw new Exception();
    }
  }
  
  /**
   * Delete downloaded image directory
   * 
   * @param fileSource
   * @param folderName
   * @throws IOException
   */
  private static void deleteDownloadedImage(String fileSource, String folderName) throws IOException
  {
    if (fileSource != null && fileSource.equals(ITransformationTask.FROM_URL)) {
      File file = new File(folderName);
      FileUtils.deleteDirectory(file);
    }
  }
  
 
  /**
   * Prepares contextual object
   * 
   * @param contextualInputData
   * @param contextualObject
   * @param diDataType
   * @throws Exception
   */
  public static void prepareContexualInfo(Map<String, Object> contextualInputData, IContextualDataDTO contextualObject,
      DiDataType diDataType) throws Exception
  {
    if (!CollectionUtils.isEmpty(contextualInputData)) {
      Map<String, Object> contextTimeRange = DiDataType.EXCEL.equals(diDataType) ? contextualInputData
          : (Map<String, Object>) contextualInputData.get(ITransformationTask.TIME_RANGE);
      if (!CollectionUtils.isEmpty(contextTimeRange)) {
        String from = (String) contextTimeRange
            .get(DiDataType.EXCEL.equals(diDataType) ? ITransformationTask.FROM_DATE_COLUMN : ITransformationTask.FROM);
        if (StringUtils.isNotBlank(from)) {
          contextualObject.setContextStartTime(DiUtils.getLongValueOfDateString(from));
        }
        String to = (String) contextTimeRange
            .get(DiDataType.EXCEL.equals(diDataType) ? ITransformationTask.TO_DATE_COLUMN : ITransformationTask.TO);
        if (StringUtils.isNotBlank(to)) {
          to = DiUtils.appendTimeToToDate(to);
          contextualObject.setContextEndTime(DiUtils.getLongValueOfDateWithTimestamp(to));
        }
      }
      
      Map<String, Object> tags = DiDataType.EXCEL.equals(diDataType) ? getContextTagsFromExcel(contextualInputData)
          : (Map<String, Object>) contextualInputData.get(ITransformationTask.TAGS);
      if (!CollectionUtils.isEmpty(tags)) {
        for (Entry<String, Object> entry : tags.entrySet()) {
          Object value = entry.getValue();
          if (value instanceof List<?>) {
            List<ITagDTO> tagValues = new ArrayList<>();
            for (String item : (List<String>) value) {
              if (!item.equals("")) {
                tagValues.add(new TagDTO(item,100));
              }
            }
            contextualObject.setContextTagValues(tagValues.toArray(new ITagDTO[0]));
          }
          else {
            contextualObject.setContextTagValues(new TagDTO(entry.getKey(), Integer.parseInt(value.toString()) == 0 ? 0 : 100));
          }
        }
      }
    }
  }
  
  /**
   * @param typeIds
   * @param taxonomyIds
   * @param baseEntityDTO
   */
  public static void convertClassifierPXONToJSON(Set<String> typeIds, Set<String> taxonomyIds, IBaseEntityDTO baseEntityDTO, WorkflowTaskModel workflowTaskModel)
  {
    typeIds.add(baseEntityDTO.getNatureClassifier().getCode());
    getSecondaryKlassesAndTaxonomies(typeIds, taxonomyIds, baseEntityDTO, workflowTaskModel);
  }
  
  /**
   * Get Secondary Klasses and Taxonomies
   * 
   * @param typeIds
   * @param taxonmyIds
   * @param baseEntityDTO
   * @param workflowTaskModel 
   */
  public static void getSecondaryKlassesAndTaxonomies(Set<String> typeIds, Set<String> taxonmyIds, IBaseEntityDTO baseEntityDTO, WorkflowTaskModel workflowTaskModel)
  {
    IOutBoundMappingModel outboundMapping = (IOutBoundMappingModel) workflowTaskModel
        .getInputParameters()
        .get(AbstractFromPXONTask.OUTBOUND_MAPPING);
    Map<String, IConfigRuleAttributeMappingModel> klassIdMappingMap = (Map<String, IConfigRuleAttributeMappingModel>) workflowTaskModel
        .getInputParameters()
        .get(AbstractFromPXONTask.KLASS_ID_MAPPING_MAP);
    Map<String, IConfigRuleAttributeMappingModel> taxonomyIdMappingMap = (Map<String, IConfigRuleAttributeMappingModel>) workflowTaskModel
        .getInputParameters()
        .get(AbstractFromPXONTask.TAXONOMY_ID_MAPPING_MAP);
        
    for (IClassifierDTO classifierDTO : baseEntityDTO.getOtherClassifiers()) {
      switch (classifierDTO.getClassifierType()) {
        case CLASS:
          if (outboundMapping == null || outboundMapping.getIsAllClassesSelected() || klassIdMappingMap.containsKey(classifierDTO.getCode())) {
            typeIds.add(classifierDTO.getCode());
          }
          break;
        case TAXONOMY:
          if(outboundMapping == null || outboundMapping.getIsAllTaxonomiesSelected() || taxonomyIdMappingMap.containsKey(classifierDTO.getCode())) {
            taxonmyIds.add(classifierDTO.getCode());
          }
          break;
        //TODO: PXPFDEV-21215: Deprecate - Taxonomy Hierarchies 
        /*case HIERARCHY:
          if (outboundMapping == null || outboundMapping.getIsAllTaxonomiesSelected() || taxonomyIdMappingMap.containsKey(classifierDTO.getCode())) {
            taxonmyIds.add(classifierDTO.getCode());
          }*/
    
        default:
          break;
      }
    }
  }
  
  /**
   * Prepares classifiers
   * 
   * @param taxonomyIds
   * @param nonNatureKlasses
   * @param configDataForKlassAndTaxonomy
   * @param baseEntityDTO
   * @param baseEntityDAO
   * @param executionStatusTable
   * @throws Exception
   */
  public static void prepareClassifiers(List<String> taxonomyIds, List<String> nonNatureKlasses,
      IGetConfigDetailsByCodesResponseModel configDataForKlassAndTaxonomy, IBaseEntityDTO baseEntityDTO, IBaseEntityDAO baseEntityDAO,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable) throws Exception
  {
    // attach taxonomy
    DiTransformationUtils.prepareClassifiersForTaxonomy(baseEntityDTO, taxonomyIds, baseEntityDAO, configDataForKlassAndTaxonomy,
        executionStatusTable);
    
    // attach reference non nature classes
    DiTransformationUtils.prepareClassifiersForNonNatureKlass(baseEntityDTO, nonNatureKlasses, baseEntityDAO, configDataForKlassAndTaxonomy,
        executionStatusTable);
  }
  
  /**
   * @param attributesMap
   * @param propertyRecord
   * @param propertyType
   * @param valueRecoredDTO
   * @author sopan.talekar
   */
  public static void prepareAttributeToExport(Map<String, Object> attributesMap, IPropertyRecordDTO propertyRecord,
      IValueRecordDTO valueRecoredDTO)
  {
    String propertyType = propertyRecord.getProperty().getPropertyType().name();
    String attributeCode = propertyRecord.getProperty().getCode();
    String attributeValue = valueRecoredDTO.getValue();
    if (PropertyType.HTML.name().equals(propertyType) && StringUtils.isNotBlank(attributeValue)) {
      attributeValue = valueRecoredDTO.getAsHTML();
    }
    else if (PropertyType.DATE.name().equals(propertyType) && StringUtils.isNotBlank(attributeValue)) {
      attributeValue = valueRecoredDTO.getValue();
      long longAttributeValue = Long.parseLong(attributeValue);
      attributeValue = getTimeStampForFormat(longAttributeValue, DiConstants.DATE_FORMAT);
    }
    // TODO What about number type properties.
    attributesMap.put(attributeCode, attributeValue);
  }
  
  /**
   * Return formated date from given timestamp, if timestamp = 0 pass null will
   * return empty string as date
   * 
   * @param timeStamp
   * @param format
   * @return
   */
  public static String getTimeStampForFormat(Long timeStamp, String format)
  {
    if (timeStamp == null) {
      return null;
    }
    Date dateValue = new Date(timeStamp);
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    String customDate = dateFormat.format(dateValue);
    return customDate;
  }
  
  /**
   * @param tagsMap
   * @param propertyRecord
   * @param propertyType
   * @author sopan.talekar
   */
  public static void prepareTagToExport(Map<String, Object> tagsMap, ITagsRecordDTO tagRecord, String propertyType)
  {
    Set<ITagDTO> tagValuesDTO = tagRecord.getTags();
    List<String> tagValues = new ArrayList<>();
    String tagCode = tagRecord.getProperty().getCode();
    tagsMap.put(tagCode, tagValues);
    if (propertyType.equals(PropertyType.BOOLEAN.name())) {
      if (tagValuesDTO.isEmpty()) {
        tagValues.add("0");
      }else
        tagValuesDTO.forEach(tagValueDTO -> tagValues.add(tagValueDTO.getRange() == 100 ? "1" : "0"));
      return;
    }
   
    tagValuesDTO.stream().filter(tagValueDTO -> tagValueDTO.getRange() == 100).collect(Collectors.toList())
    .forEach(tagValueDTO -> tagValues.add(tagValueDTO.getTagValueCode()));    
  }
  
  /**
   * prepare context tags
   * 
   * @param relation
   * @return
   */
  
  public static Map<String, Object> getContextTagsFromExcel(Map<String, Object> relation)
  {
    return relation.entrySet().stream().filter(map -> map.getKey().startsWith(ITransformationTask.CONTEXT_TAG_PREFIX))
        .collect(Collectors.toMap(map -> map.getKey().substring(ITransformationTask.CONTEXT_TAG_PREFIX.length()),
            map -> getListFromString((String) map.getValue())));
  }
  
  /**
   * create list of string
   * 
   * @param commaSeperatedString
   * @return
   */
  private static List<String> getListFromString(String commaSeperatedString)
  {
    if (commaSeperatedString == null) {
      return new ArrayList<>();
    }
    String[] splitedArray = commaSeperatedString.split(",");
    return new ArrayList<>(Arrays.asList(splitedArray));
  }
  
  /**
   * create a Value Record
   *
   * 
   * @param baseEntityDAO
   * @param attribute
   * @param attributeValue
   * @param language
   * @return
   */
  public static IPropertyRecordDTO createValueRecord(IBaseEntityDAO baseEntityDAO, IPropertyDTO attribute, String attributeValue,
      String language)
  {
    IPropertyRecordDTO propertyRecord = null;
    if (attributeValue == null || attributeValue.isEmpty()) {
      return propertyRecord;
    }
    switch (attribute.getPropertyType()) {
      case DATE:
        String dateValue = Long.toString(DiUtils.getLongValueOfDateString(attributeValue));
        propertyRecord = baseEntityDAO.newValueRecordDTOBuilder(attribute, dateValue).asNumber(Double.parseDouble(dateValue)).build();
        break;
      case HTML:
        propertyRecord = baseEntityDAO.newValueRecordDTOBuilder(attribute, attributeValue).localeID(language).asHTML(attributeValue)
            .build();
        break;
      case PRICE:
        propertyRecord = baseEntityDAO.newValueRecordDTOBuilder(attribute, attributeValue)
            .asNumber(Double.parseDouble(attributeValue)).unitSymbol(DEFAULT_CURRENCY).build();
        break;
      case MEASUREMENT:
        propertyRecord = baseEntityDAO.newValueRecordDTOBuilder(attribute, attributeValue)
            .asNumber(Double.parseDouble(attributeValue)).unitSymbol(DEFAULT_MEASUREMENT_UNIT).build();
        break;
      case NUMBER:
        propertyRecord = baseEntityDAO.newValueRecordDTOBuilder(attribute, attributeValue)
            .asNumber(Double.parseDouble(attributeValue)).build();
      case CALCULATED: 
        propertyRecord = baseEntityDAO.newValueRecordDTOBuilder(attribute, attributeValue).build();
      case TEXT:
      case CONCATENATED: //added for PXPFDEV-18613
        propertyRecord = baseEntityDAO.newValueRecordDTOBuilder(attribute, attributeValue).localeID(language).build();
        break;
        case ASSET_ATTRIBUTE: //added for PXPFDEV-18613
        propertyRecord = baseEntityDAO.newValueRecordDTOBuilder(attribute, attributeValue).build();
        break;
      default:
        break;
    }
    return propertyRecord;
  }
  
  /**
   * to Supports Assign and Delete action 
   * for taxonomy
   * Prepare DTO for classifiers.
   * @param entity
   * @param taxonomyIds
   * @param baseEntityDAO
   * @param configDataForKlassAndTaxonomy
   * @param executionStatusTable
   * @throws Exception
   */
  public static void prepareClassifiersForTaxonomy(IBaseEntityDTO entity, Map<String, List<String>> taxonomyMap,
      IBaseEntityDAO baseEntityDAO, IGetConfigDetailsByCodesResponseModel configDataForKlassAndTaxonomy,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable) throws Exception
  {
    Map<String, String> taxonomyConfigDetails = configDataForKlassAndTaxonomy.getTaxonomy();    
    //Clear existing data if TAXONOMIES_COLUMN is has values to replace/blank column
    if (taxonomyMap.containsKey("REPLACE")) {
      entity.getOtherClassifiers().removeAll(entity.getOtherClassifiers().stream()
          .filter(p -> ClassifierType.TAXONOMY.equals(p.getClassifierType())).collect(Collectors.toList()));
    }
    taxonomyMap.forEach((k, v) -> {
      v.stream().forEach(p -> {
        if (!taxonomyConfigDetails.containsKey(p)) {
          executionStatusTable.addWarning(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN015, new String[] { p });
          return;
        }
        Long taxonomyIID = Long.parseLong(taxonomyConfigDetails.get(p));
        IClassifierDTO taxonomyDTO;
        try {
          taxonomyDTO = baseEntityDAO.newClassifierDTO(taxonomyIID, p, ClassifierType.TAXONOMY);
          if ("REPLACE".equals(k) || "ASSIGN".equals(k)) {
            entity.getOtherClassifiers().add(taxonomyDTO);
          }
          else if ("DELETE".equals(k)) {
            entity.getOtherClassifiers().remove(taxonomyDTO);
          }
        }
        catch (RDBMSException e) {
          RDBMSLogger.instance().exception(e);
        }        
      });
    });
  }
  
 /**
  * to Supports Assign and Delete action 
  * for nonNatureKlasses
  * @param entity
  * @param nonNatureKlassMap
  * @param baseEntityDAO
  * @param configDataForKlassAndTaxonomy
  * @param executionStatusTable
  * @throws Exception
  */
  public static void prepareClassifiersForNonNatureKlass(IBaseEntityDTO entity, Map<String, List<String>> nonNatureKlassMap,
      IBaseEntityDAO baseEntityDAO, IGetConfigDetailsByCodesResponseModel configDataForKlassAndTaxonomy,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable) throws Exception
  {
    Map<String, Object> klassConfigDetails = configDataForKlassAndTaxonomy.getKlass();
    //Clear existing data if SECONDARY_KLASSES has values to replace/blank column
    if (nonNatureKlassMap.containsKey("REPLACE")) {
      entity.getOtherClassifiers().removeAll(entity.getOtherClassifiers().stream()
          .filter(p -> ClassifierType.CLASS.equals(p.getClassifierType())).collect(Collectors.toList()));
    }
    nonNatureKlassMap.forEach((k, v) -> {
      v.stream().forEach(p -> {
        if (!klassConfigDetails.containsKey(p)) {
          executionStatusTable.addWarning(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN015, new String[] { p });
          return;
        }
        Map<String, Object> klassMap = (Map<String, Object>) klassConfigDetails.get(p);
        Long klassIID = Long.parseLong(klassMap.get(CommonConstants.CLASSIFIER_IID).toString());
        IClassifierDTO klassDTO;
        try {
          klassDTO = baseEntityDAO.newClassifierDTO(klassIID, p, ClassifierType.CLASS);
          if ("REPLACE".equals(k) || "ASSIGN".equals(k)) {
            entity.getOtherClassifiers().add(klassDTO);
          }
          else if ("DELETE".equals(k)) {
            entity.getOtherClassifiers().remove(klassDTO);
          }
        }
        catch (RDBMSException e) {
          RDBMSLogger.instance().exception(e);
        }
      });
    });
  }
  
  /**
   * to Supports Assign and Delete action 
   * for taxonomy and nonNatureKlasses
   * @param taxonomyMap
   * @param nonNatureKlasses
   * @param configDataForKlassAndTaxonomy
   * @param baseEntityDTO
   * @param baseEntityDAO
   * @param executionStatusTable
   * @throws Exception
   */
  public static void prepareClassifiers(Map<String, List<String>> taxonomyMap, Map<String, List<String>> nonNatureKlasses,
      IGetConfigDetailsByCodesResponseModel configDataForKlassAndTaxonomy, IBaseEntityDTO baseEntityDTO, IBaseEntityDAO baseEntityDAO,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable) throws Exception
  {
    // attach taxonomy
    DiTransformationUtils.prepareClassifiersForTaxonomy(baseEntityDTO, taxonomyMap, baseEntityDAO, configDataForKlassAndTaxonomy,
        executionStatusTable);
    
    // attach reference non nature classes
    DiTransformationUtils.prepareClassifiersForNonNatureKlass(baseEntityDTO, nonNatureKlasses, baseEntityDAO, configDataForKlassAndTaxonomy,
        executionStatusTable);
  }
  
  /**
   * This method return List for tag
   * 
   * @param tagValue
   * @return
   */
  public static Object getTagValues(String tagValue)
  {
    String[] tagValues = tagValue.split(",");
    return new ArrayList<>(Arrays.asList(tagValues));
  }
  
  /**
   * Get coupling type
   * 
   * @param couplingType
   * @return
   */
  public static String getCouplingType(String couplingType)
  {
    switch (couplingType) {
      case "looselyCoupled":
        return "No Coupling";
      case "tightlyCoupled":
        return "Tight Coupling";
      case "dynamicCoupled":
        return "Dynamic Coupling";
      case "readOnlyCoupled":
        return "readOnlyCoupled";
      default:
        return "No Coupling";
    }
  }
  
  /**
   * Get code for given coupling type
   *  
   * @param couplingType
   * @return
   */
  public static String convertCouplingType(String couplingType)
  {
    switch (couplingType.toLowerCase()) {
      case "no coupling":
        return "looselyCoupled";
      case "tight coupling":
        return "tightlyCoupled";
      case "dynamic coupling":
        return "dynamicCoupled";
      case "readOnlyCoupled":
        return "readOnlyCoupled";
      default:
        return "invalid";
    }
  }
}
