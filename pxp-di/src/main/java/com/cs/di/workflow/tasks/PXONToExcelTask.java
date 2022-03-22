package com.cs.di.workflow.tasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.mapping.IConfigRuleAttributeMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleAttributeOutBoundMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRulePropertyMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleTagOutBoundMappingModel;
import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;
import com.cs.core.config.interactor.model.mapping.ITagValueMappingModel;
import com.cs.core.config.interactor.model.mapping.OutBoundMappingModel;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.runtime.interactor.model.klassinstance.IWriteInstancesToXLSXFileModel;
import com.cs.core.runtime.interactor.model.klassinstanceexport.WriteInstancesToXLSXFileModel;
import com.cs.di.config.interactor.mappings.IGetOutBoundMappingForExport;
import com.cs.di.config.interactor.model.configdetails.IGetConfigDataForTransformResponseModel;
import com.cs.di.runtime.utils.DiFileUtils;
import com.cs.di.runtime.utils.DiTransformationUtils;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.DiConstants;
import com.cs.di.workflow.constants.ExportTypeEnum;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;

/**
 * Conversion from PXON to Excel for Outbound/Export Transformation
 * 
 * @author jamil.ahmad
 *
 */
@Component("PXONToExcelTask")
@SuppressWarnings("unchecked")
public class PXONToExcelTask extends AbstractFromPXONTask {
  
  
  @Value("${di.valueSeparator}")
  private String             separator;
  
  @Value("${excelExportBatchSize}")
  private Integer            excelExportBatchSize;
  
  @Autowired
  IGetOutBoundMappingForExport     getOutBoundMappingForExport;
  
  public static final String HEADERS             = "headers";
  public static final String DATA                = "data";
  public static final String XLSX_FILE_EXTENSION = ".xlsx";
  public static final String TRUE                = "1";
  public static final String FALSE               = "0";
  public static final List<String>       INPUT_LIST           = Arrays.asList(PXON_FILE_PATH);
  public static final List<String>       OUTPUT_LIST          = Arrays.asList(TRANSFORMED_DATA, EXECUTION_STATUS, FAILED_FILES,EXPORTED_TYPE);
  

  @Override
  public List<String> getInputList() {
    return INPUT_LIST;
  }

  @Override
  public List<String> getOutputList() {
    return OUTPUT_LIST;
  }
  
   /**
   * Generate PXON from Excel files.
   * 
   * @param workflowTaskModel
   */
  @Override
  public void generateFromPXON(WorkflowTaskModel workflowTaskModel)
  {
    Map<String, Object> transformedExcelMap = new LinkedHashMap<>();
    final String filename = DiFileUtils.generateFileName(EXPORT_FILE_PREFIX) + XLSX_FILE_EXTENSION;
    final String processingFolder = getOrCreateProcessingTempFolder(workflowTaskModel);
    final String exportedExcelFilePath = processingFolder + filename;
    intializeStandardHeader(transformedExcelMap);
    intializeExcelMap(transformedExcelMap);
    String path = (String) DiValidationUtil.validateAndGetRequiredFilePath(workflowTaskModel, PXON_FILE_PATH);
    List<String> failedfiles = new ArrayList<>();
    try {
      if (workflowTaskModel.getExecutionStatusTable().isErrorOccurred()) {
        return;
      }
      IGetConfigDataForTransformResponseModel configDetails = getConfigDetails();
      PXONFileParser pxonFileParser = new PXONFileParser(path);
      PXONFileParser.PXONBlock blockInfo = null;
      Integer counter = 0;
      while ((blockInfo = pxonFileParser.getNextBlock()) != null) {
        IBaseEntityDTO baseEntityDTO = new BaseEntityDTO();
        baseEntityDTO.fromPXON(blockInfo.getData());
        switch (baseEntityDTO.getBaseType()) {
          case ARTICLE:
            if (EmbeddedType.CONTEXTUAL_CLASS.equals(baseEntityDTO.getEmbeddedType())) {
              generateExcelForVariants(baseEntityDTO, transformedExcelMap, configDetails, workflowTaskModel);
            }
            else {
              generateExcelForArticles(baseEntityDTO, transformedExcelMap, configDetails, workflowTaskModel);
            }
            break;
          case ASSET:
            if (EmbeddedType.CONTEXTUAL_CLASS.equals(baseEntityDTO.getEmbeddedType())) {
              generateExcelForVariants(baseEntityDTO, transformedExcelMap, configDetails, workflowTaskModel);
            }
            else {
              generateExcelForAssets(baseEntityDTO, transformedExcelMap, configDetails, workflowTaskModel);
            }
            break;
          default:
            break;
        }
        counter++;
        if (counter == excelExportBatchSize) {
          writeIntoExcel(workflowTaskModel, exportedExcelFilePath, transformedExcelMap);
          intializeExcelMap(transformedExcelMap);
          counter = 0;
        }
      }
    }
    catch (Exception e) {
      failedfiles.add(path);
      workflowTaskModel.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN009,
          new String[] { path });
    }
    
    workflowTaskModel.getOutputParameters().put(FAILED_FILES, failedfiles);
    
    writeIntoExcel(workflowTaskModel, exportedExcelFilePath, transformedExcelMap);
    Path fileLocation = Paths.get(exportedExcelFilePath);
    try {
      workflowTaskModel.getOutputParameters().put(TRANSFORMED_DATA, Base64.getEncoder().encodeToString(Files.readAllBytes(fileLocation)));
      workflowTaskModel.getOutputParameters().put(EXPORTED_TYPE,ExportTypeEnum.PRODUCT.getExportType());
    }
    catch (IOException e) {
      workflowTaskModel.getExecutionStatusTable().addError(MessageCode.GEN028);
    }

    DiFileUtils.deleteDirectory(Paths.get(processingFolder), workflowTaskModel.getExecutionStatusTable());
  }
  
  /**
   * @param workflowTaskModel
   * @param exportedExcelFilePath
   * @param transformedExcelMap
   */
  private void writeIntoExcel(WorkflowTaskModel workflowTaskModel, String exportedExcelFilePath,
      Map<String, Object> transformedExcelMap)
  {
    for (Map.Entry<String, Object> entity : transformedExcelMap.entrySet()) {
            Map<String, Object> entityValue =(Map<String, Object>) entity.getValue();
            LinkedHashSet<String> headerSet = (LinkedHashSet<String>) entityValue.get(HEADERS);
      String[] headerToWrite = headerSet.toArray(new String[headerSet.size()]);
      List<String[]> dataToWrite = new ArrayList<>();
      try {
        dataToWrite = prepareInstancesDataToWrite(headerSet, (List<Map<String, String>>) entityValue.get(DATA));
        if (!dataToWrite.isEmpty()) {
          applyOutboundMapping(workflowTaskModel, headerToWrite, dataToWrite);
          prepareDataModelAndExport(headerToWrite, dataToWrite, entity.getKey(), exportedExcelFilePath);
        }
      }
      catch (Exception e) {
        // failing Excel formation
        workflowTaskModel.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN018,
            new String[] { entity.getKey() });
      }
    }
  }
  
  /**
   * intialize data of each entity
   * 
   * @param transformedExcelMap
   */
  private void intializeExcelMap(Map<String, Object> transformedExcelMap)
  {
    ((Map<String, Object>) transformedExcelMap.get(ARTICLE_SHEET_NAME)).put(DATA, new ArrayList<>());
    ((Map<String, Object>) transformedExcelMap.get(ASSET_SHEET_NAME)).put(DATA, new ArrayList<>());
    ((Map<String, Object>) transformedExcelMap.get(VARIANT_SHEET_NAME)).put(DATA, new ArrayList<>());
    ((Map<String, Object>) transformedExcelMap.get(RELATIONSHIP_SHEET_NAME)).put(DATA, new ArrayList<>());
  }
  
  /**
   * intialize headers
   * 
   * @param transformedExcelMap
   */
  private void intializeStandardHeader(Map<String, Object> transformedExcelMap)
  {
    LinkedHashSet<String> articleHeaders = new LinkedHashSet<>();
    articleHeaders.add(ID_COLUMN);
    articleHeaders.add(KLASS_COLUMN);
    articleHeaders.add(TAXONOMIES_COLUMN);
    articleHeaders.add(SECONDARY_KLASSES_COLUMN);
    articleHeaders.add(LANGUAGE_CODE_COLUMN);
    transformedExcelMap.put(ARTICLE_SHEET_NAME, new HashMap<>());
    Map<String, Object> articleMap = (Map<String, Object>) transformedExcelMap.get(ARTICLE_SHEET_NAME);
    articleMap.put(HEADERS, articleHeaders);
    
    LinkedHashSet<String> assetHeaders = new LinkedHashSet<>();
    assetHeaders.add(ID_COLUMN);
    assetHeaders.add(KLASS_COLUMN);
    assetHeaders.add(TAXONOMIES_COLUMN);
    assetHeaders.add(SECONDARY_KLASSES_COLUMN);
    assetHeaders.add(LANGUAGE_CODE_COLUMN);
    transformedExcelMap.put(ASSET_SHEET_NAME, new HashMap<>());
    Map<String, Object> assetMap = (Map<String, Object>) transformedExcelMap.get(ASSET_SHEET_NAME);
    assetMap.put(HEADERS, assetHeaders);
    
    LinkedHashSet<String> variantHeaders = new LinkedHashSet<>();
    variantHeaders.add(ID_COLUMN);
    variantHeaders.add(KLASS_COLUMN);
    variantHeaders.add(MASTER_ID_COLUMN);
    variantHeaders.add(PARENT_ID_COLUMN);
    variantHeaders.add(TAXONOMIES_COLUMN);
    variantHeaders.add(SECONDARY_KLASSES_COLUMN);
    variantHeaders.add(IS_ATTRIBUTE_VARIANT_COLUMN);
    variantHeaders.add(LANGUAGE_CODE_COLUMN);
    transformedExcelMap.put(VARIANT_SHEET_NAME, new HashMap<>());
    Map<String, Object> variantsMap = (Map<String, Object>) transformedExcelMap.get(VARIANT_SHEET_NAME);
    variantsMap.put(HEADERS, variantHeaders);
    
    LinkedHashSet<String> relationshipHeaders = new LinkedHashSet<>();
    relationshipHeaders.add(SIDE1_ID_COLUMN);
    relationshipHeaders.add(SIDE2_ID_COLUMN);
    relationshipHeaders.add(RELATIONSHIP_CODES_COLUMN);
    relationshipHeaders.add(IS_NATURE_RELATIONSHIP_COLUMN);
    transformedExcelMap.put(RELATIONSHIP_SHEET_NAME, new HashMap<>());
    Map<String, Object> relationshipMap = (Map<String, Object>) transformedExcelMap.get(RELATIONSHIP_SHEET_NAME);
    relationshipMap.put(HEADERS, relationshipHeaders);
  }
  
  /**
   * Generate Excel for Articles
   * 
   * @param baseEntityDTO
   * @param transformedExcelMap
   * @param configDetails
   */
  
  protected void generateExcelForArticles(IBaseEntityDTO baseEntityDTO, Map<String, Object> transformedExcelMap,
            IGetConfigDataForTransformResponseModel configDetails, WorkflowTaskModel workflowTaskModel)
  {
    Map<String, String> transformedArticle = new HashMap<>();
    transformEntity(baseEntityDTO, transformedArticle, configDetails, transformedExcelMap, ARTICLE_SHEET_NAME, workflowTaskModel);
    Map<String, Object> articleMap = (Map<String, Object>) transformedExcelMap.get(ARTICLE_SHEET_NAME);
    List<Map<String, String>> articlesList = (List<Map<String, String>>) articleMap.get(DATA);
    articlesList.add(transformedArticle);
  }
  
  /**
   * Generate Excel for Assets.
   * 
   * @param baseEntityDTO
   * @param transformedExcelMap
   * @param configDetails
   */
  protected void generateExcelForAssets(IBaseEntityDTO baseEntityDTO, Map<String, Object> transformedExcelMap,
      IGetConfigDataForTransformResponseModel configDetails, WorkflowTaskModel workflowTaskModel)
  {
    Map<String, String> transformedAsset = new HashMap<>();
    transformEntity(baseEntityDTO, transformedAsset, configDetails, transformedExcelMap, ASSET_SHEET_NAME, workflowTaskModel);
    Map<String, Object> assetMap = (Map<String, Object>) transformedExcelMap.get(ASSET_SHEET_NAME);
    List<Map<String, String>> assetsList = (List<Map<String, String>>) assetMap.get(DATA);
    assetsList.add(transformedAsset);
  }
  
  /**
   * Generate PXON For Variants
   * 
   * @param baseEntityDTO
   * @param transformedExcelMap
   * @param configDetails
   */
  protected void generateExcelForVariants(IBaseEntityDTO baseEntityDTO, Map<String, Object> transformedExcelMap,
      IGetConfigDataForTransformResponseModel configDetails, WorkflowTaskModel workflowTaskModel)
  {
    Map<String, Object> variantMap = (Map<String, Object>) transformedExcelMap.get(VARIANT_SHEET_NAME);
    LinkedHashSet<String> headersForVariant = (LinkedHashSet<String>) variantMap.get(HEADERS);
    Map<String, String> transformedVariantMap = new HashMap<>();
    
    transformedVariantMap.put(MASTER_ID_COLUMN, baseEntityDTO.getTopParentID());
    transformedVariantMap.put(PARENT_ID_COLUMN, baseEntityDTO.getParentID());
    transformedVariantMap.put(IS_ATTRIBUTE_VARIANT_COLUMN, FALSE);
    
    // Preparing context map
    prepareContextMap(baseEntityDTO.getContextualObject(), configDetails, transformedVariantMap, headersForVariant);
    transformEntity(baseEntityDTO, transformedVariantMap, configDetails, transformedExcelMap, VARIANT_SHEET_NAME, workflowTaskModel);
    
    ((List<Object>) variantMap.get(DATA)).add(transformedVariantMap);
  }
  
  /**
   * This method is common for article and asset transform.
   * 
   * @param baseEntityDTO
   * @param transformedEntity
   * @param configDetails
   * @param transformedExcelMap
   * @param entityName
   * @param workflowTaskModel 
   */
  private void transformEntity(IBaseEntityDTO baseEntityDTO, Map<String, String> transformedEntity,
      IGetConfigDataForTransformResponseModel configDetails, Map<String, Object> transformedExcelMap, String entityName, WorkflowTaskModel workflowTaskModel)
  {
    transformedEntity.put(ID_COLUMN, baseEntityDTO.getBaseEntityID());
    transformedEntity.put(LANGUAGE_CODE_COLUMN, baseEntityDTO.getBaseLocaleID());
    
    // Mapping info
    IOutBoundMappingModel outboundMapping = (IOutBoundMappingModel) workflowTaskModel
        .getInputParameters()
        .get(OUTBOUND_MAPPING);
    Map<String, IConfigRuleAttributeMappingModel> klassIdMappingMap = (Map<String, IConfigRuleAttributeMappingModel>) workflowTaskModel
        .getInputParameters()
        .get(AbstractFromPXONTask.KLASS_ID_MAPPING_MAP);
    
    //Prepare taxonomies and klasses data
    Set<String> taxonomyIds = new HashSet<>();
    Set<String> secondaryKlassIds = new HashSet<>();
    DiTransformationUtils.getSecondaryKlassesAndTaxonomies(secondaryKlassIds, taxonomyIds, baseEntityDTO, workflowTaskModel);
    
    transformedEntity.put(TAXONOMIES_COLUMN, String.join(separator, taxonomyIds));
    transformedEntity.put(SECONDARY_KLASSES_COLUMN, String.join(separator, secondaryKlassIds));
    String klassId = baseEntityDTO.getNatureClassifier().getClassifierCode();
    transformedEntity.put(KLASS_COLUMN, klassIdMappingMap.containsKey(klassId) || outboundMapping.getIsAllClassesSelected() ? klassId : "");
        
    //Prepare properties data
    Map<String, Object> attributesMap = new HashMap<>();
    Map<String, Object> tagsMap = new HashMap<>();
    List<Map<String, Object>> attributeVariantsList = new ArrayList<>();
    convertPropertiesFromPXON(attributesMap, tagsMap, attributeVariantsList, baseEntityDTO, configDetails, transformedExcelMap, workflowTaskModel);
    Map<String, Object> transformedEntityMap = (Map<String, Object>) transformedExcelMap
        .get(entityName);
    LinkedHashSet<String> headers = (LinkedHashSet<String>) transformedEntityMap.get(HEADERS);
    for (Map.Entry<String, Object> attribute : attributesMap.entrySet()) {
      headers.add(attribute.getKey());
      transformedEntityMap.put(attribute.getKey(), (String) attribute.getValue());
      transformedEntity.put(attribute.getKey(), (String) attribute.getValue());
    }
    for (Map.Entry<String, Object> tag : tagsMap.entrySet()) {
      headers.add(tag.getKey());
      transformedEntityMap.put(tag.getKey(), String.join(separator, (List<String>) tag.getValue()));
      transformedEntity.put(tag.getKey(), String.join(separator, (List<String>) tag.getValue()));
    }
    
    headers.add(LAST_MODIFIED_BY_COLUMN);
    headers.add(LAST_MODIFIED_COLUMN);
    headers.add(CREATED_BY_COLUMN);
    headers.add(CREATED_ON_COLUMN);
    transformedEntity.put(LAST_MODIFIED_BY_COLUMN, baseEntityDTO.getLastModifiedTrack().getWho());
    transformedEntity.put(LAST_MODIFIED_COLUMN,
        DiTransformationUtils.getTimeStampForFormat(baseEntityDTO.getLastModifiedTrack().getWhen(), DiConstants.DATE_FORMAT));
    transformedEntity.put(CREATED_BY_COLUMN, baseEntityDTO.getCreatedTrack().getWho());
    transformedEntity.put(CREATED_ON_COLUMN, DiTransformationUtils.getTimeStampForFormat(baseEntityDTO.getCreatedTrack().getWhen(), DiConstants.DATE_FORMAT));
  }
  
  /**
   * @param propertyRecord
   * @param valueRecoredDTO
   * @param baseEntityDTO
   * @param contextDTO
   * @param attributeVariantsList
   * @param configDetails
   * @param transformedExcelMap
   */
  @Override
  void transformAttributeVariantFromPXON(IPropertyRecordDTO propertyRecord,
      IValueRecordDTO valueRecoredDTO, IBaseEntityDTO baseEntityDTO, IContextualDataDTO contextDTO, List<Map<String, Object>> attributeVariantsList,
      IGetConfigDataForTransformResponseModel configDetails, Map<String, Object> transformedExcelMap) {
    Map<String, Object> variantMap = (Map<String, Object>) transformedExcelMap.get(VARIANT_SHEET_NAME);
    LinkedHashSet<String> headersForVariant = (LinkedHashSet<String>) variantMap.get(HEADERS);
    Map<String, String> attributeVariantMap = new HashMap<>();
    attributeVariantMap.put(IS_ATTRIBUTE_VARIANT_COLUMN, TRUE);
    attributeVariantMap.put(LANGUAGE_CODE_COLUMN, valueRecoredDTO.getLocaleID());
    attributeVariantMap.put(ID_COLUMN, baseEntityDTO.getBaseEntityID());
    attributeVariantMap.put(PARENT_ID, baseEntityDTO.getBaseEntityID());
    if (contextDTO != null && contextDTO.getContextCode() != null && !contextDTO.getContextCode().isBlank()) {
      //Preparing context map
      prepareContextMap(valueRecoredDTO.getContextualObject(), configDetails, attributeVariantMap, headersForVariant);
    }
    
    Map<String, Object> attributesMap = new HashMap<>();
    //Preparing attribute to export
    DiTransformationUtils.prepareAttributeToExport(attributesMap, propertyRecord, valueRecoredDTO);
    for (Map.Entry<String, Object> attribute : attributesMap.entrySet()) {
      headersForVariant.add(attribute.getKey());
      attributeVariantMap.put(attribute.getKey(), (String) attribute.getValue());
    }
    ((List<Object>) variantMap.get(DATA)).add(attributeVariantMap);
  }
  
  /**
   * Generate nature and non nature relationshipMap Using reletionshipDTO and
   * Side1 Id of entity.
   * 
   * @param relationshipDTO
   * @param side1Id
   * @param transformedExcelMap
   * @param configDetails
   */
  @Override
  void transformRelationshipFromPXON(IRelationsSetDTO relationshipDTO, String side1Id,
      Map<String, Object> transformedExcelMap,
      IGetConfigDataForTransformResponseModel configDetails)
  {
    Map<String, Object> transformedMap = (Map<String, Object>) transformedExcelMap.get(RELATIONSHIP_SHEET_NAME);
    LinkedHashSet<String> headers = (LinkedHashSet<String>) transformedMap.get(HEADERS);
    String relationshipId = relationshipDTO.getProperty().getCode();
    Boolean isNature = (Boolean) configDetails.getRelationshipMap().get(relationshipId);
    for (IEntityRelationDTO relation : relationshipDTO.getRelations()) {
      Map<String, String> relationshipMap = new HashMap<String, String>();
      relationshipMap.put(SIDE1_ID_COLUMN, side1Id);
      relationshipMap.put(SIDE2_ID_COLUMN, relation.getOtherSideEntityID());
      relationshipMap.put(RELATIONSHIP_CODES_COLUMN, relationshipId);
      relationshipMap.put(IS_NATURE_RELATIONSHIP_COLUMN, (isNature) ? TRUE : FALSE);
      IContextualDataDTO contextDTO = relation.getContextualObject();
      if (contextDTO != null && contextDTO.getContextCode() != null && !contextDTO.getContextCode().isBlank()) {
        //Preparing context map
        prepareContextMap(contextDTO, configDetails, relationshipMap, headers);
      }
      ((List<Object>) transformedMap.get(DATA)).add(relationshipMap);
    }
  }
  
  /**
   * Prepare Context Data Map
   * 
   * @param contextDTO
   * @param configDetails
   * @param headers
   */
  private void prepareContextMap(IContextualDataDTO contextDTO, IGetConfigDataForTransformResponseModel configDetails,
      Map<String, String> entityMap, LinkedHashSet<String> headers)
  {
    headers.add(CONTEXT_ID_COLUMN);
    headers.add(FROM_DATE_COLUMN);
    headers.add(TO_DATE_COLUMN);
    entityMap.put(CONTEXT_ID_COLUMN, contextDTO.getContextCode());
    entityMap.put(FROM_DATE_COLUMN, DiTransformationUtils
        .getTimeStampForFormat(contextDTO.getContextStartTime() != 0 ? contextDTO.getContextStartTime() : null, DiConstants.DATE_FORMAT));
    entityMap.put(TO_DATE_COLUMN, DiTransformationUtils
        .getTimeStampForFormat(contextDTO.getContextEndTime() != 0 ? contextDTO.getContextEndTime() : null, DiConstants.DATE_FORMAT));
    
    Set<ITagDTO> contextTagValues = contextDTO.getContextTagValues();
    Map<String, Object> referenceContext = (Map<String, Object>) configDetails.getContextMap().get(contextDTO.getContextCode());
    List<Map<String, Object>> referenceTags = (List<Map<String, Object>>) referenceContext.get(CONTEXT_TAGS);
    
    fillContextTagsValues(contextTagValues, entityMap, referenceTags, headers);
  }
  
  /**
   * Add Context tag group vs value
   * 
   * @param contextTagValues
   * @param entityMap
   * @param referenceTags
   * @param headers
   */
  private void fillContextTagsValues(Set<ITagDTO> contextTagValues, Map<String, String> entityMap, List<Map<String, Object>> referenceTags,
      LinkedHashSet<String> headers)
  {
    for (ITagDTO tagDTO : contextTagValues) {
      if (tagDTO.getRange() != 0) {
        for (Map<String, Object> referenceTag : referenceTags) {
          List<String> tagValues = (List<String>) referenceTag.get(TAG_VALUE_IDS);
          if (tagValues.contains(tagDTO.getTagValueCode())) {
            String tagGroupKey = CONTEXT_TAG_PREFIX + (String) referenceTag.get(ID_PROPERTY);
            if (entityMap.containsKey(tagGroupKey)) {
              String tagValuesString = entityMap.get(tagGroupKey);
              tagValuesString += separator + tagDTO.getTagValueCode();
              entityMap.put(tagGroupKey, tagValuesString);
            }
            else {
              entityMap.put(tagGroupKey, tagDTO.getTagValueCode());
              headers.add(tagGroupKey);
            }
          }
        }
      }
    }
  }
  
  /**
   * Prepare String array using headers and entities
   * 
   * @param header
   * @param klassInstanceDataMap
   * @return
   * @throws Exception
   */
  public List<String[]> prepareInstancesDataToWrite(LinkedHashSet<String> header, List<Map<String, String>> klassInstanceDataMap)
      throws Exception
  {
    List<String[]> dataToWrite = new ArrayList<>();
    
    for (Map<String, String> instanceDataMap : klassInstanceDataMap) {
      LinkedList<String> instanceDataList = new LinkedList<>();
      for (String columnName : header) {
        instanceDataList.add(instanceDataMap.get(columnName));
      }
      String[] instanceDataToWrite = instanceDataList.toArray(new String[instanceDataList.size()]);
      dataToWrite.add(instanceDataToWrite);
    }
    
    return dataToWrite;
  }
  
  /**
   * @param headerToWrite
   * @param dataToWrite
   * @param sheet
   * @param filePath
   * @throws Exception
   */
  public void prepareDataModelAndExport(String[] headerToWrite, List<String[]> dataToWrite, String sheet, String filePath) throws Exception
  {
    IWriteInstancesToXLSXFileModel exportXLSXDataModel = new WriteInstancesToXLSXFileModel();
    exportXLSXDataModel.setSheetName(sheet);
    exportXLSXDataModel.setHeaderRowNumber(1);
    exportXLSXDataModel.setDataRowNumber(2);
    exportXLSXDataModel.setHeaderToWrite(headerToWrite);
    exportXLSXDataModel.setDataToWrite(dataToWrite);
    exportXLSXDataModel.setfilePath(filePath);
    DiFileUtils.writeToXLSX(exportXLSXDataModel);
  }
    
  /**
   * @param workflowTaskModel
   * @param headerToWrite
   * @param dataToWrite
   * @throws Exception
   */
  private void applyOutboundMapping(WorkflowTaskModel workflowTaskModel, String[] headerToWrite,
      List<String[]> dataToWrite) throws Exception
  {
    IOutBoundMappingModel outboundMapping = (IOutBoundMappingModel) workflowTaskModel.getInputParameters().get(OUTBOUND_MAPPING);
    
    List<String> headers = new ArrayList<String>();
    CollectionUtils.addAll(headers, headerToWrite);
    
    for (IConfigRuleAttributeOutBoundMappingModel attributeMapping : outboundMapping.getAttributeMappings()) {
      applyMappingOnHeader(headerToWrite, headers, attributeMapping);
    }
    
    Map<String, ITag> tagsConfigDetails = null;
    if (outboundMapping.getConfigDetails() == null) {
      tagsConfigDetails = new HashMap<String, ITag>();
    }
    else {
      tagsConfigDetails = outboundMapping.getConfigDetails().getTags();
    }
    for (IConfigRuleTagOutBoundMappingModel tagMapping : outboundMapping.getTagMappings()) {
      int headerIndex = applyMappingOnHeader(headerToWrite, headers, tagMapping);
      applyTagVlueMappingOnRows(headerIndex, dataToWrite, tagMapping, tagsConfigDetails);
    }

    Map<String, IConfigRuleAttributeMappingModel> klassIdMappingMap = (Map<String, IConfigRuleAttributeMappingModel>) workflowTaskModel
        .getInputParameters()
        .get(KLASS_ID_MAPPING_MAP);
    applyKlassMappingsOnRows(dataToWrite, klassIdMappingMap, headers);
    
    Map<String, IConfigRuleAttributeMappingModel> taxonomyIdMappingMap = (Map<String, IConfigRuleAttributeMappingModel>) workflowTaskModel
        .getInputParameters()
        .get(TAXONOMY_ID_MAPPING_MAP);
    applyTaxonomyMappingsOnRows(dataToWrite, taxonomyIdMappingMap, headers);
    
    if (headers.contains(RELATIONSHIP_CODES_COLUMN)) {
      Map<String, IConfigRuleAttributeMappingModel> relationshipIdMappingMap = (Map<String, IConfigRuleAttributeMappingModel>) workflowTaskModel
          .getInputParameters()
          .get(RELATIONSHIP_ID_MAPPING_MAP);
      applyRelationshipMappingsOnRows(dataToWrite, relationshipIdMappingMap, headers);
    }
  }
  
  /**
   * @param headerToWrite
   * @param headers
   * @param mapping
   * @return
   */
  private int applyMappingOnHeader(String[] headerToWrite, List<String> headers,
      IConfigRulePropertyMappingModel mapping)
  {
    int headerIndex = headers.indexOf(mapping.getMappedElementId());
    if (headerIndex != -1) {
      headerToWrite[headerIndex] = mapping.getColumnNames().get(0);
    }
    return headerIndex;
  }

  /**
   * @param headerIndex
   * @param dataToWrite
   * @param tagMapping
   * @param tagsConfigDetails
   */
  private void applyTagVlueMappingOnRows(int headerIndex, List<String[]> dataToWrite,
      IConfigRuleTagOutBoundMappingModel tagMapping, Map<String, ITag> tagsConfigDetails)
  {
    Map<String, ITagValueMappingModel> tagValueMappingMap;
    if (headerIndex == -1) {
      return;
    }
    if (tagMapping.getTagValueMappings().isEmpty()) {
      tagValueMappingMap = new HashMap<String, ITagValueMappingModel>();
    }
    else {
     tagValueMappingMap = tagMapping.getTagValueMappings()
        .get(0)
        .getMappings()
        .stream()
        .map(tagValueMapping -> tagValueMapping)
        .collect(Collectors.toMap(x -> x.getMappedTagValueId(), x -> x));
    
    for (String[] row : dataToWrite) {
      String tagValueIds = row[headerIndex];
        if (tagValueIds != null && tagValueIds.contains(separator)
            && tagsConfigDetails.get(tagMapping.getMappedElementId())
                .getIsMultiselect()) {
        String[] tagValueArray = StringUtils.tokenizeToStringArray(tagValueIds, separator, true, true);
        int index = -1;
        while (++index < tagValueArray.length) {
          tagValueArray[index] = tagValueMappingMap.get(tagValueArray[index]).getTagValue();
        }
        row[headerIndex] = StringUtils.arrayToCommaDelimitedString(tagValueArray);
      }
      else if (tagValueIds != null && !tagValueIds.isBlank()) {
        row[headerIndex] = tagValueMappingMap.get(tagValueIds).getTagValue();
      }
    }
  }
  }

  /**
   * @param dataToWrite
   * @param klassIdMappingMap
   * @param headers
   */
  private void applyKlassMappingsOnRows(List<String[]> dataToWrite,
      Map<String, IConfigRuleAttributeMappingModel> klassIdMappingMap, List<String> headers)
  {    
    int klassIndex = headers.indexOf(KLASS_COLUMN);
    
    if (klassIndex == -1) {
        return;
     }
    
    for (String[] row : dataToWrite) {
      IConfigRuleAttributeMappingModel klassMapping = klassIdMappingMap.get(row[klassIndex]);
      if(klassMapping!=null) {
        row[klassIndex] = klassMapping.getColumnNames().get(0);
      }
    }
    
    applyMappingOnCommaSeparatedList(dataToWrite, headers, SECONDARY_KLASSES_COLUMN, klassIdMappingMap);
  }
  
  private void applyRelationshipMappingsOnRows(List<String[]> dataToWrite,
      Map<String, IConfigRuleAttributeMappingModel> relationshipIdMappingMap, List<String> headers)
  {    
    int relationshipIndex = headers.indexOf(RELATIONSHIP_CODES_COLUMN);
    
    if (relationshipIndex == -1) {
        return;
     }
    
    for (String[] row : dataToWrite) {
      IConfigRuleAttributeMappingModel relationshipMapping = relationshipIdMappingMap.get(row[relationshipIndex]);
      if(relationshipMapping!=null) {
        row[relationshipIndex] = relationshipMapping.getColumnNames().get(0);
      }
    }
  }
  
  /**
   * @param dataToWrite
   * @param taxonomyIdMappingMap
   * @param headers
   */
  private void applyTaxonomyMappingsOnRows(List<String[]> dataToWrite,
      Map<String, IConfigRuleAttributeMappingModel> taxonomyIdMappingMap, List<String> headers)
  { 
    applyMappingOnCommaSeparatedList(dataToWrite, headers, TAXONOMIES_COLUMN, taxonomyIdMappingMap);
  }
  
  /**
   * @param dataToWrite
   * @param headers
   * @param collumn
   * @param idMappingMap
   */
  private void applyMappingOnCommaSeparatedList(List<String[]> dataToWrite, List<String> headers,
      String collumn, Map<String, IConfigRuleAttributeMappingModel> idMappingMap)
  {
    int configEntityIndex = headers.indexOf(collumn);
    
    if (configEntityIndex == -1) {
        return;
     }
    
    for (String[] row : dataToWrite) {
      String commaSeparatedString = row[configEntityIndex];
      String[] configEntityArray = StringUtils.tokenizeToStringArray(commaSeparatedString, separator, true, true);
      int index = -1;
      while (++index < configEntityArray.length) {
        IConfigRuleAttributeMappingModel mapping = idMappingMap.get(configEntityArray[index]);
        if (mapping != null) {
          configEntityArray[index] = mapping.getColumnNames().get(0);
        }
      }
      row[configEntityIndex] = StringUtils.arrayToCommaDelimitedString(configEntityArray);
    }
  }
  
  /**
   * Validate input parameters
   * 
   * @param inputFields
   */
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> invalidParameters = new ArrayList<>();
    // Validate required PXON_FILE_PATH
    String filePath = (String) inputFields.get(PXON_FILE_PATH);
    if (!DiValidationUtil.validateFilePath(filePath)) {
      invalidParameters.add(PXON_FILE_PATH);
    }
    
    return invalidParameters;
  }
  
  /**
   * This method checks if mapping id mentioned in the task input variables
   * exists or not.
   *
   * @param mappingId
   * @return
   */
  public Boolean validateMappingId(String mappingId)
  {
    if (!DiValidationUtil.isRuntimeValue(mappingId)) {
      // return false for invalid conditions here
      IOutBoundMappingModel dataModel = new OutBoundMappingModel();
      dataModel.setId(mappingId);
      try {
        IOutBoundMappingModel outboundMappingModel = getOutBoundMappingForExport.execute(dataModel);
        if (outboundMappingModel == null || !outboundMappingModel.getMappingType().equalsIgnoreCase("outboundmapping")) {
          return false;
        }
      }
      catch (Exception e) {
        return false;
      }
    }
    return true;
  }
  
}
