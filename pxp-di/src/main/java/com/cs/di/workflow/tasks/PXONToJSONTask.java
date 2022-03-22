package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;
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
import com.cs.core.runtime.interactor.entity.propertyinstance.IImageAttributeInstance;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.di.config.interactor.model.configdetails.IGetConfigDataForTransformResponseModel;
import com.cs.di.runtime.utils.DiTransformationUtils;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.DiConstants;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

/**
 * Conversion from PXON to JSON for Outbound/Export Transformation
 * 
 * @author jamil.ahmad
 *
 */
@Component("PXONToJSONTask")
@SuppressWarnings("unchecked")
public class PXONToJSONTask extends AbstractFromPXONTask {
  
  public static final List<String>       INPUT_LIST           = Arrays.asList(PXON_FILE_PATH);
  public static final List<String>       OUTPUT_LIST          = Arrays.asList(TRANSFORMED_DATA, EXECUTION_STATUS, FAILED_FILES);
  

  @Override
  public List<String> getInputList() {
    return INPUT_LIST;
  }

  @Override
  public List<String> getOutputList() {
    return OUTPUT_LIST;
  }
  
  /**
   * Generate JSON from baseEntityDTOs present in Model.
   * 
   * @param workflowTaskModel
   */
  @Override
  public void generateFromPXON(WorkflowTaskModel workflowTaskModel)
  {
    Map<String, Object> transformedJSONMap = new HashMap<>();
    transformedJSONMap.put(ARTICLE, new ArrayList<>());
    transformedJSONMap.put(ASSETS, new ArrayList<>());
    transformedJSONMap.put(EMBEDDED_VARIANTS, new ArrayList<>());
    transformedJSONMap.put(RELATIONSHIPS, new ArrayList<>());
    transformedJSONMap.put(NATURE_RELATIONSHIPS, new ArrayList<>());
    
    String path = (String) DiValidationUtil.validateAndGetRequiredFilePath(workflowTaskModel, PXON_FILE_PATH);
    List<String> failedfiles = new ArrayList<>();
    try {
      if (workflowTaskModel.getExecutionStatusTable().isErrorOccurred()) {
        return;
      }
      IGetConfigDataForTransformResponseModel configDetails = getConfigDetails();
      PXONFileParser pxonFileParser = new PXONFileParser(path);
      PXONFileParser.PXONBlock blockInfo = null;
      while ((blockInfo = pxonFileParser.getNextBlock()) != null) {
        IBaseEntityDTO baseEntityDTO = new BaseEntityDTO();
        baseEntityDTO.fromPXON(blockInfo.getData());
        switch (baseEntityDTO.getBaseType()) {
          case ARTICLE:
            if (EmbeddedType.CONTEXTUAL_CLASS.equals(baseEntityDTO.getEmbeddedType())) {
              generateJSONForVariants(baseEntityDTO, transformedJSONMap, configDetails, workflowTaskModel);
            }
            else {
              generateJSONForArticles(baseEntityDTO, transformedJSONMap, configDetails, workflowTaskModel);
            }
            break;
          case ASSET:
            if (EmbeddedType.CONTEXTUAL_CLASS.equals(baseEntityDTO.getEmbeddedType())) {
              generateJSONForVariants(baseEntityDTO, transformedJSONMap, configDetails, workflowTaskModel);
            }
            else {
              generateJSONForAssets(baseEntityDTO, transformedJSONMap, configDetails, workflowTaskModel);
            }
            break;
          default:
            break;
        }
      }
    }
    catch (Exception e) {
      failedfiles.add(path);
      workflowTaskModel.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN009, new String[] { path });
    }
    
    workflowTaskModel.getOutputParameters().put(FAILED_FILES, failedfiles);
    
    Gson gson = new Gson();
    String json = gson.toJson(transformedJSONMap);
    workflowTaskModel.getOutputParameters().put(TRANSFORMED_DATA, json);
  }
  
    /**
   * Generate JSON for Articles
   * 
   * @param baseEntityDTO
     * @param transformedJSONMap 
     * @param configDetails 
   * @param executionStatusTable
   */
  protected void generateJSONForArticles(IBaseEntityDTO baseEntityDTO,
      Map<String, Object> transformedJSONMap, IGetConfigDataForTransformResponseModel configDetails, WorkflowTaskModel workflowTaskModel)
  {
    Map<String, Object> transformedArticle = new HashMap<>();
    transformedArticle.put(ID_PROPERTY, baseEntityDTO.getBaseEntityID());
    transformEntity(baseEntityDTO, transformedArticle, transformedJSONMap, configDetails, workflowTaskModel);
    ((List<Object>) transformedJSONMap.get(ARTICLE)).add(transformedArticle);
  }
  
  /**
   * Generate JSON for Assets.
   * 
   * @param baseEntityDTO
   * @param transformedJSONMap 
   * @param configDetails 
   * @param executionStatusTable
   */
  protected void generateJSONForAssets(IBaseEntityDTO baseEntityDTO,
      Map<String, Object> transformedJSONMap, IGetConfigDataForTransformResponseModel configDetails, WorkflowTaskModel workflowTaskModel)
  {
    Map<String, Object> transformedAsset = new HashMap<>();
    transformedAsset.put(ID_PROPERTY, baseEntityDTO.getBaseEntityID());
    transformEntity(baseEntityDTO, transformedAsset, transformedJSONMap, configDetails, workflowTaskModel);
    IJSONContent entityExtension = baseEntityDTO.getEntityExtension();
    Map<String, String> imageDetails = null;
    if (entityExtension != null && entityExtension.isEmpty()) {
      imageDetails = new HashMap<>();
      imageDetails.put(IImageAttributeInstance.ASSET_OBJECT_KEY,
          entityExtension.getInitField(IImageAttributeInstance.ASSET_OBJECT_KEY, ""));
      imageDetails.put(IImageAttributeInstance.FILENAME,
          entityExtension.getInitField(IImageAttributeInstance.FILENAME, ""));
      imageDetails.put(IImageAttributeInstance.THUMB_KEY,
          entityExtension.getInitField(IImageAttributeInstance.THUMB_KEY, ""));
      imageDetails.put(IImageAttributeInstance.TYPE,
          entityExtension.getInitField(IImageAttributeInstance.TYPE, ""));
    }
    transformedAsset.put("imageDetails", imageDetails);
    ((List<Object>) transformedJSONMap.get(ASSETS)).add(transformedAsset);
  }
  
  /**
   * This method is common for article and asset transform.
   * 
   * @param baseEntityDTO
   * @param transformedEntity
   * @param transformedJSONMap 
   * @param configDetails 
   * @param workflowTaskModel 
   */
  private void transformEntity(IBaseEntityDTO baseEntityDTO, Map<String, Object> transformedEntity,
      Map<String, Object> transformedJSONMap, IGetConfigDataForTransformResponseModel configDetails, WorkflowTaskModel workflowTaskModel)
  {    

    transformedEntity.put(LANGUAGE_CODE, baseEntityDTO.getBaseLocaleID());
    
    Set<String> taxonmyIds = new HashSet<>();
    Set<String> typeIds = new HashSet<>();
    DiTransformationUtils.convertClassifierPXONToJSON(typeIds, taxonmyIds, baseEntityDTO, workflowTaskModel);
    
    transformedEntity.put(ITransformationTask.TAXONOMY_IDS, taxonmyIds);
    transformedEntity.put(ITransformationTask.TYPE_IDS, typeIds);
    
    Map<String, Object> attributesMap = new HashMap<>();
    Map<String, Object> tagsMap = new HashMap<>();
    List<Map<String, Object>> attributeVariantsList = new ArrayList<>();
    convertPropertiesFromPXON(attributesMap, tagsMap, attributeVariantsList, baseEntityDTO, configDetails, transformedJSONMap, workflowTaskModel);
    
    Map<String, Object> propertiesMap = new HashMap<>();
    propertiesMap.put(ITransformationTask.ATTRIBUTES, attributesMap);
    propertiesMap.put(ITransformationTask.TAGS, tagsMap);
    propertiesMap.put(ITransformationTask.ATTRIBUTE_VARIANTS, attributeVariantsList);
    
    transformedEntity.put(LABEL, attributesMap.get(CommonConstants.NAME_ATTRIBUTE));
    transformedEntity.put(ITransformationTask.PROPERTIES, propertiesMap);
    transformedEntity.put(LAST_MODIFIED_BY, baseEntityDTO.getLastModifiedTrack().getWho());
    transformedEntity.put(LAST_MODIFIED,
        DiTransformationUtils.getTimeStampForFormat(baseEntityDTO.getLastModifiedTrack().getWhen(), DiConstants.DATE_FORMAT));
    transformedEntity.put(CREATED_BY, baseEntityDTO.getCreatedTrack().getWho());
    transformedEntity.put(CREATED_ON, DiTransformationUtils.getTimeStampForFormat(baseEntityDTO.getCreatedTrack().getWhen(), DiConstants.DATE_FORMAT));
  }
  
  /**
   * Generate JSON For Variants
   * 
   * @param baseEntityDTO
   * @param transformedJSONMap 
   * @param configDetails 
   * @param executionStatusTable
   * @throws JsonProcessingException 
   */
  protected void generateJSONForVariants(IBaseEntityDTO baseEntityDTO, Map<String, Object> transformedJSONMap,
      IGetConfigDataForTransformResponseModel configDetails, WorkflowTaskModel workflowTaskModel)
  {
    Map<String, Object> embeddedVariantMap = new HashMap<>();
    
    embeddedVariantMap.put(VARIANT_ID, baseEntityDTO.getBaseEntityID());
    embeddedVariantMap.put(PARENT_ID,baseEntityDTO.getParentID());
    embeddedVariantMap.put(CONTENT_ID, baseEntityDTO.getTopParentID());
    
    embeddedVariantMap.put(CONTEXT, prepareContextMap(baseEntityDTO.getContextualObject(), configDetails));
    
    transformEntity(baseEntityDTO, embeddedVariantMap, transformedJSONMap, configDetails, workflowTaskModel);
    
    ((List<Object>) transformedJSONMap.get(EMBEDDED_VARIANTS)).add(embeddedVariantMap);
  }
  
  
  /**
   * @param propertyRecord
   * @param valueRecoredDTO
   * @param baseEntityDTO
   * @param contextDTO
   * @param attributeVariantsList
   * @param configDetails 
   */
  @Override
  void transformAttributeVariantFromPXON(IPropertyRecordDTO propertyRecord,
      IValueRecordDTO valueRecoredDTO, IBaseEntityDTO baseEntityDTO, IContextualDataDTO contextDTO,
      List<Map<String, Object>> attributeVariantsList,
      IGetConfigDataForTransformResponseModel configDetails,
      Map<String, Object> transformedMap)
  {
    Map<String, Object> attributeVariantMap =  new HashMap<>();
    
    if (EmbeddedType.CONTEXTUAL_CLASS.equals(baseEntityDTO.getEmbeddedType())) {
      attributeVariantMap.put(CONTENT_ID, baseEntityDTO.getTopParentID());
    }
    else {
      attributeVariantMap.put(CONTENT_ID, baseEntityDTO.getBaseEntityID());
    }
    attributeVariantMap.put(PARENT_ID, baseEntityDTO.getBaseEntityID());
    if (contextDTO != null && contextDTO.getContextCode() != null && !contextDTO.getContextCode().isBlank()) {
      attributeVariantMap.put(CONTEXT, prepareContextMap(contextDTO, configDetails));
    }
    
    Map<String, Object> properties = new HashMap<>();
    Map<String, Object> attributeMap = new HashMap<>();
    
    DiTransformationUtils.prepareAttributeToExport(attributeMap, propertyRecord, valueRecoredDTO);
    
    properties.put(ITransformationTask.ATTRIBUTES, attributeMap);
    attributeVariantMap.put(PROPERTIES, properties);
    attributeVariantsList.add(attributeVariantMap);
  }


  /** 
   * Generate nature and non nature relationshipMap Using reletionshipDTO and Side1 Id of entity.
   * 
   * @param relationshipDTO
   * @param side1Id
   * @param transformedJSONMap
   * @param configDetails
   */
  @Override
  void transformRelationshipFromPXON(IRelationsSetDTO relationshipDTO, String side1Id,
      Map<String, Object> transformedJSONMap,
      IGetConfigDataForTransformResponseModel configDetails)
  {
    String relationshipId = relationshipDTO.getProperty().getCode();
    Boolean isNature = (Boolean) configDetails.getRelationshipMap().get(relationshipId);
    for (IEntityRelationDTO relation : relationshipDTO.getRelations()) {
      Map<String, Object> relationshipMap = new HashMap<String, Object>();
      relationshipMap.put(SIDE1_ID, side1Id);
      relationshipMap.put(SIDE2_ID, relation.getOtherSideEntityID());
      relationshipMap.put(RELATIONSHIP_ID, relationshipId);
      Map<String, Object> optionalDataMap = new HashMap<String, Object>();
      IContextualDataDTO contextDTO = relation.getContextualObject();
      if (contextDTO != null && contextDTO.getContextCode() != null && !contextDTO.getContextCode().isBlank()) {
        optionalDataMap.put(CONTEXT, prepareContextMap(contextDTO, configDetails));
      }
      relationshipMap.put(OPTIONAL, optionalDataMap);
      if (isNature) {
        ((List<Object>) transformedJSONMap.get(NATURE_RELATIONSHIPS)).add(relationshipMap);
      }
      else {
        ((List<Object>) transformedJSONMap.get(RELATIONSHIPS)).add(relationshipMap);
      }
    }
  }
  
  /** Prepare Context Data Map
   * @param contextDTO
   * @param configDetails 
   * @param contextMap
   */
  private Map<String, Object> prepareContextMap(IContextualDataDTO contextDTO, IGetConfigDataForTransformResponseModel configDetails)
  {
    Map<String, Object> contextMap = new HashMap<>();
    contextMap.put(CONTEXT_ID, contextDTO.getContextCode());
    
    Map<String, Object> timeRangeMap = new HashMap<>();
    timeRangeMap.put(FROM, DiTransformationUtils
        .getTimeStampForFormat(contextDTO.getContextStartTime() != 0 ? contextDTO.getContextStartTime() : null, DiConstants.DATE_FORMAT));
    timeRangeMap.put(TO, DiTransformationUtils
        .getTimeStampForFormat(contextDTO.getContextEndTime() != 0 ? contextDTO.getContextEndTime() : null, DiConstants.DATE_FORMAT));
    contextMap.put(TIME_RANGE, timeRangeMap);
    
    Set<ITagDTO> tags = contextDTO.getContextTagValues();
    Map<String, List<String>> tagMap = new HashMap<String, List<String>>();
    Map<String, Object> refrenceContext = (Map<String, Object>) configDetails.getContextMap().get(contextDTO.getContextCode());
    
    List<Map<String, Object>> referenceTags = (List<Map<String, Object>>) refrenceContext.get(CONTEXT_TAGS);
    for (Map<String, Object> referenceTag : referenceTags) {
      tagMap.put((String) referenceTag.get(ID_PROPERTY), new ArrayList<String>());
    }
    fillContextTagsValues(tags, tagMap, referenceTags);
    contextMap.put(TAGS, tagMap);
    
    return contextMap;
  }

  /**
   * Context Tag value
   * @param tagDTOs
   * @param tagMap
   * @param refrenceTags
   */
  private void fillContextTagsValues(Set<ITagDTO> tagDTOs, Map<String, List<String>> tagMap, List<Map<String, Object>> referenceTags)
  {
    for (ITagDTO tagDTO : tagDTOs) {
      if (tagDTO.getRange() != 0) {
        for (Map<String, Object> refrenceTag : referenceTags) {
          List<String> tagvalues = (List<String>) refrenceTag.get(TAG_VALUE_IDS);
          if (tagvalues.contains(tagDTO.getTagValueCode())) {
            tagMap.get(refrenceTag.get(ID_PROPERTY)).add(tagDTO.getTagValueCode());
          }
        }
      }
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
    // Validate required PXON_FILE_PATH
    String filePath = (String) inputFields.get(PXON_FILE_PATH);
    if (!DiValidationUtil.validateFilePath(filePath)) {
      return List.of(PXON_FILE_PATH);
    }
    return null;
  }
}
