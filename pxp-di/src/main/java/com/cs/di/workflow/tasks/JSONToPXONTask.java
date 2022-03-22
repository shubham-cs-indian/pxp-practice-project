package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.ContextualDataDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.di.config.interactor.model.task.IGetConfigDetailsByCodesResponseModel;
import com.cs.di.runtime.utils.DiTransformationUtils;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.DiDataType;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Conversion of JSON into PXON, which can be used by background process for
 * import.
 * 
 * @author sangram.shelar
 */
@SuppressWarnings("unchecked")
@Component("JSONToPXONTask")
public class JSONToPXONTask extends AbstractToPXONTask {
  
  public static final String             ASSETS               = "assets";
  public static final String             ARTICLE              = "content";
  public static final String             EMBEDDED_VARIANTS    = "embeddedVariants";
  public static final String             RELATIONSHIPS        = "relationships";
  public static final String             NATURE_RELATIONSHIPS = "natureRelationships";
  
  public static final List<String>       INPUT_LIST           = Arrays.asList(RECEIVED_DATA);
  public static final List<String>       OUTPUT_LIST          = Arrays.asList(PXON, FAILED_FILES, EXECUTION_STATUS);
  public static final List<WorkflowType> WORKFLOW_TYPES       = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  public static final List<EventType>    EVENT_TYPES          = Arrays.asList(EventType.INTEGRATION);
  
  @Override
  public List<String> getInputList()
  {
    return INPUT_LIST;
  }
  
  @Override
  public List<String> getOutputList()
  {
    return OUTPUT_LIST;
  }
  
  @Override
  public List<WorkflowType> getWorkflowTypes()
  {
    return WORKFLOW_TYPES;
  }
  
  @Override
  public List<EventType> getEventTypes()
  {
    return EVENT_TYPES;
  }
  
  @Override
  public TaskType getTaskType()
  {
    return TaskType.SERVICE_TASK;
  }
  
  @Override
  public DiDataType getDataType()
  {
    return DiDataType.JSON;
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
    // Validate required RECEIVED_DATA
    String receivedData = (String) inputFields.get(RECEIVED_DATA);
    if (DiValidationUtil.isBlank(receivedData)) {
      invalidParameters.add(RECEIVED_DATA);
    }
    return invalidParameters;
    
    // Note: Validation not required for
    // 1. PXON as it is optional output parameter
    // 2. FAILED_FILES as it is optional output parameter
    // 2. EXECUTION_STATUS as it is optional output parameter
  }
  
  /**
   * Generate PXON from Json files.
   * 
   * @param workflowTaskModel
   */
  @Override
  public void generatePXON(WorkflowTaskModel workflowTaskModel)
  {
    ILocaleCatalogDAO localeCatalogDAO = DiUtils.createLocaleCatalogDAO(
        (IUserSessionDTO) workflowTaskModel.getWorkflowModel().getUserSessionDto(),
        (ITransactionData) workflowTaskModel.getWorkflowModel().getTransactionData());
    Map<String, Object> jsonFiles = (Map<String, Object>) DiValidationUtil.validateAndGetRequiredMap(workflowTaskModel, RECEIVED_DATA);
    if (workflowTaskModel.getExecutionStatusTable().isErrorOccurred()) {
      return;
    }
    List<String> entities = new ArrayList<>();
    List<String> failedfiles = new ArrayList<>();
    ITransactionData transactionData = (ITransactionData) workflowTaskModel.getWorkflowModel().getTransactionData();
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = workflowTaskModel.getExecutionStatusTable();
    for (Entry<String, Object> entry : jsonFiles.entrySet()) {
      try {
        // Generate PXON for each JSON file
        generatePXON(localeCatalogDAO, entities, transactionData, executionStatusTable, entry, workflowTaskModel);
      }
      catch (Exception e) {
        failedfiles.add(entry.getKey());
        executionStatusTable.addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN009, new String[] { entry.getKey() });
      }
    }
    if (!CollectionUtils.isEmpty(entities)) {
      // Write entities to file
      String filename = writePXONToFile(entities, executionStatusTable);
      
      // Add generated file name
      workflowTaskModel.getOutputParameters().put(PXON, filename);
    }
    // Add failed files in output variable
    workflowTaskModel.getOutputParameters().put(FAILED_FILES, failedfiles);
  }
  
  /**
   * Generate PXON
   * 
   * @param localeCatalogDAO
   * @param entities
   * @param transactionData
   * @param executionStatusTable
   * @param entry
   * @param workflowTaskModel
   * @throws Exception
   */
  private void generatePXON(ILocaleCatalogDAO localeCatalogDAO, List<String> entities, ITransactionData transactionData,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable, Entry<String, Object> entry,
      WorkflowTaskModel workflowTaskModel) throws Exception
  {
    Map<String, Object> jsonFileData = (entry.getValue() instanceof Map )? (Map<String, Object>) entry.getValue() :
      ObjectMapperUtil.readValue(entry.getValue().toString(), new TypeReference<Map<String, Object>>()
      {
      });
    
    Map<String, ILocaleCatalogDAO> localCatalogDAOMap = new ConcurrentHashMap<>();
    // Prepare relationships map
    Map<String, List<Map<String, Object>>> relationshipsMap = new HashMap<>();
    prepareNormalRelationshipsMap(jsonFileData, relationshipsMap);
    prepareNatureRelationshipsMap(jsonFileData, relationshipsMap);
    List<String> entitiesToBeCreated = new ArrayList<>();
    
    // Generate PXON for article
    generatePXONForArticles(entities, jsonFileData, relationshipsMap, localCatalogDAOMap, entitiesToBeCreated, executionStatusTable,
        defaultLanguageCode, workflowTaskModel);
    // Generate PXON for asset
    generatePXONForAssets(entities, jsonFileData, relationshipsMap, localCatalogDAOMap, 
        entitiesToBeCreated,  executionStatusTable, defaultLanguageCode,
        workflowTaskModel);
    
    // Generate PXON for relationships
    generatePXONForRelationships(entities, relationshipsMap, executionStatusTable, workflowTaskModel, localCatalogDAOMap,
        defaultLanguageCode, getDataType());
    
    // Generate PXON for variants
    generatePXONForVariants(entities, jsonFileData, localCatalogDAOMap, entitiesToBeCreated, executionStatusTable, defaultLanguageCode,
        workflowTaskModel);
    
    // Add success file name in execution status
    executionStatusTable.addSuccess(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN011, new String[] { entry.getKey() });
  }
  
  /**
   * Generate PXON for Articles
   * 
   * @param entities
   * @param jsonFileData
   * @param relationshipsMap
   * @param localCatalogDAOMap
   * @param articlesToBeCreated
   * @param executionStatusTable
   * @param defaultLanguageCode
   * @param workflowTaskModel
   */
  private void generatePXONForArticles(List<String> entities, Map<String, Object> jsonFileData,
      Map<String, List<Map<String, Object>>> relationshipsMap, Map<String, ILocaleCatalogDAO> localCatalogDAOMap,
      List<String> articlesToBeCreated, IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable,
      String defaultLanguageCode, WorkflowTaskModel workflowTaskModel)
  {
    List<Map<String, Object>> articles = (List<Map<String, Object>>) jsonFileData.get(ARTICLE);
    generatePXONForEntity(entities, relationshipsMap, localCatalogDAOMap,articlesToBeCreated, executionStatusTable, articles, defaultLanguageCode,
        workflowTaskModel, IBaseEntityIDDTO.BaseType.ARTICLE);

  }
  
  /**
   * Generate PXON for Assets.
   * 
   * @param entities
   * @param jsonFileData
   * @param relationshipsMap
   * @param localCatalogDAOMap
   * @param executionStatusTable
   * @param defaultLanguageCode
   * @param workflowTaskModel
   */
  private void generatePXONForAssets(List<String> entities, Map<String, Object> jsonFileData,
      Map<String, List<Map<String, Object>>> relationshipsMap, Map<String, ILocaleCatalogDAO> localCatalogDAOMap,List<String> assetsToBeCreated,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable, String defaultLanguageCode,
      WorkflowTaskModel workflowTaskModel)
  {
    List<Map<String, Object>> assets = (List<Map<String, Object>>) jsonFileData.get(ASSETS);
    generatePXONForEntity(entities, relationshipsMap, localCatalogDAOMap,assetsToBeCreated, executionStatusTable, assets, defaultLanguageCode,
        workflowTaskModel, IBaseEntityIDDTO.BaseType.ASSET);
  }
  
  /**
   * Generate PXON for Entity.
   * 
   * @param entities
   * @param relationshipsMap
   * @param localCatalogDAOMap
   * @param executionStatusTable
   * @param entityList
   * @param defaultLanguageCode
   * @param workflowTaskModel
   * @param baseType
   */
  
  private void generatePXONForEntity(List<String> entities, Map<String, List<Map<String, Object>>> relationshipsMap,
      Map<String, ILocaleCatalogDAO> localCatalogDAOMap,List<String> entitiesToBeCreated, IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable,
      List<Map<String, Object>> entityList, String defaultLanguageCode, WorkflowTaskModel workflowTaskModel, BaseType baseType)
  {
    if (entityList != null) {
      for (Map<String, Object> entity : entityList) {
        try {
          String id = (String) entity.get(ID_PROPERTY);
          String languageCode = (String) entity.get(LANGUAGE_CODE);
          languageCode = StringUtils.isBlank(languageCode) ? defaultLanguageCode : languageCode;
          String label = (String) entity.get(LABEL);
          List<String> typeIds = new ArrayList<>();
          List<String> nonNatureKlasses = new ArrayList<>();
          List<String> natureKlasses = new ArrayList<>();
          List<String> taxonomyIds = new ArrayList<>();
          Map<String, List<String>> taxonomyMap =  getTaxonomyAndNonNatureClasses(entity, taxonomyIds, true);
          Map<String, List<String>> nonNatureKlassesMap = getTaxonomyAndNonNatureClasses(entity, typeIds, false);
          
          // Separate Nature and nonNature classes
          IGetConfigDetailsByCodesResponseModel configDataForKlassAndTaxonomy = getConfigDataForKlassAndTaxonomy(typeIds, taxonomyIds,
              executionStatusTable, nonNatureKlasses, natureKlasses);
          // Get or Create baseEntity
          IClassifierDTO natureKlass = RDBMSUtils.newConfigurationDAO().createClassifier(natureKlasses.get(0), ClassifierType.CLASS);
          ILocaleCatalogDAO localeCatalogDAO = DiUtils.getLocaleCatalogDAO(localCatalogDAOMap, languageCode, workflowTaskModel);
          
          IBaseEntityDTO baseEntity = getOrCreateBaseEntity(workflowTaskModel, baseType, id, natureKlass, localeCatalogDAO);
          baseEntity.setBaseLocaleID(languageCode);
          IBaseEntityDAO baseEntityDAO = localeCatalogDAO.openBaseEntity(baseEntity);
          
          Map<String, String> assetMetadataAttributes = null;
          if (IBaseEntityIDDTO.BaseType.ASSET.equals(baseType)) {
           assetMetadataAttributes = uploadAssetOnServer(natureKlasses, baseEntity, (String) entity.get(FILE_SOURCE), (String) entity.get(FILE_PATH));
          }
          // Add properties (attributes & tags & attribute variants)
          prepareAttributesAndTags(baseEntity, languageCode, label, (Map<String, Object>) entity.get(PROPERTIES),
              assetMetadataAttributes, baseEntityDAO, executionStatusTable, getDataType());
          
          // Add taxonomy and nonNatureKlasses
          DiTransformationUtils.prepareClassifiers(taxonomyMap, nonNatureKlassesMap, configDataForKlassAndTaxonomy, baseEntity, baseEntityDAO,
              executionStatusTable);
          
          List<Map<String, Object>> side1Relations = relationshipsMap.get(baseEntity.getBaseEntityID());
          if (side1Relations != null) {
            updateDTORelations(baseEntity, side1Relations, baseEntityDAO, getDataType(), workflowTaskModel, localCatalogDAOMap,
                executionStatusTable);
            relationshipsMap.remove(baseEntity.getBaseEntityID());
          }
          entitiesToBeCreated.add(baseEntity.getBaseEntityID());
          entities.add(baseEntity.toPXON());
          baseEntityIDWithBaseType.put(baseEntity.getBaseEntityID(), baseType);
        }
        catch (Exception e) {
          RDBMSLogger.instance().exception(e);
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
              new String[] { (String) entity.get(ID_PROPERTY) });
        }
      }
    }
  }
 
  /**
   * Prepare relationship map for Normal relationships
   * 
   * @param jsonFileData
   * @param relationships
   */
  private void prepareNormalRelationshipsMap(Map<String, Object> jsonFileData, Map<String, List<Map<String, Object>>> relationships)
  {
    prepareRelationshipsMap((List<Map<String, Object>>) jsonFileData.get(RELATIONSHIPS), relationships, getDataType());
  }
  
  /**
   * Prepare relationship map for non nature relationships
   * 
   * @param jsonFileData
   * @param relationships
   */
  private void prepareNatureRelationshipsMap(Map<String, Object> jsonFileData, Map<String, List<Map<String, Object>>> relationships)
  {
    prepareRelationshipsMap((List<Map<String, Object>>) jsonFileData.get(NATURE_RELATIONSHIPS), relationships, getDataType());
  }
  
  /**
   * Generate PXON For Variants
   * 
   * @param entities
   * @param jsonFileData
   * @param localCatalogDAOMap
   * @param articlesToBeCreated
   * @param executionStatusTable
   * @param defaultLanguageCode
   * @param workflowTaskModel
   */
  private void generatePXONForVariants(List<String> entities, Map<String, Object> jsonFileData,
      Map<String, ILocaleCatalogDAO> localCatalogDAOMap, List<String> entitiesToBeCreated,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable, String defaultLanguageCode,
      WorkflowTaskModel workflowTaskModel)
  {
    if (!CollectionUtils.isEmpty(jsonFileData) && jsonFileData.containsKey(EMBEDDED_VARIANTS)) {
      List<Map<String, Object>> inputEmbeddedVariants = (List<Map<String, Object>>) jsonFileData.get(EMBEDDED_VARIANTS);
      for (Map<String, Object> embeddedVariant : inputEmbeddedVariants) {
        try {
          String baseEntityID = (String) embeddedVariant.get(VARIANT_ID);
          String label = (String) embeddedVariant.get(LABEL);
          String languageCode = (String) embeddedVariant.get(LANGUAGE_CODE);
          
          List<String> nonNatureKlasses = new ArrayList<>();
          List<String> natureKlasses = new ArrayList<>();
          List<String> typeIds = new ArrayList<>();
          List<String> taxonomyIds = new ArrayList<>();
          Map<String, List<String>> taxonomyMap =  getTaxonomyAndNonNatureClasses(embeddedVariant, taxonomyIds, true);
          Map<String, List<String>> nonNatureKlassesMap = getTaxonomyAndNonNatureClasses(embeddedVariant, typeIds, false);
          
          languageCode = StringUtils.isBlank(languageCode) ? defaultLanguageCode : languageCode;
          
          ILocaleCatalogDAO localeCatalogDAO = DiUtils.getLocaleCatalogDAO(localCatalogDAOMap, languageCode, workflowTaskModel);
          IGetConfigDetailsByCodesResponseModel configDataForKlassAndTaxonomy = getConfigDataForKlassAndTaxonomy(typeIds, taxonomyIds,
              executionStatusTable, nonNatureKlasses, natureKlasses);
          // Get embedded variant
          
          // Prepare nature class classifier DTO
          IClassifierDTO natureKlassDTO = RDBMSUtils.newConfigurationDAO().createClassifier(natureKlasses.get(0), ClassifierType.CLASS);
          // Get or Create embedded baseEntity
          IBaseEntityDTO baseEntity = getEmbeddedBaseEntityDTO(new HashMap<String, List<Map<String, Object>>>(), embeddedVariant,
              executionStatusTable, entitiesToBeCreated, natureKlassDTO, localeCatalogDAO, getDataType(), workflowTaskModel.getWorkflowModel(), configDataForKlassAndTaxonomy);
          if (baseEntity == null) {
            continue;
          }
          // Get DAO
          IBaseEntityDAO baseEntityDAO = localeCatalogDAO.openBaseEntity(baseEntity);
          
          // prepare contextual info
          IContextualDataDTO contextualObject = baseEntity.getContextualObject();
          Map<String, Object> contextualInputData = (Map<String, Object>) embeddedVariant.get(CONTEXT);
          DiTransformationUtils.prepareContexualInfo(contextualInputData, contextualObject, getDataType());
          
          // add taxonomy and non nature classes
          DiTransformationUtils.prepareClassifiers(taxonomyMap, nonNatureKlassesMap, configDataForKlassAndTaxonomy, baseEntity, baseEntityDAO,
              executionStatusTable);
          
          // get attributes and tags and attribute variants
          prepareAttributesAndTags(baseEntity, languageCode, label,
              (Map<String, Object>) embeddedVariant.get(PROPERTIES), null, baseEntityDAO, executionStatusTable, getDataType());
          entitiesToBeCreated.add(baseEntityID);
          entities.add(baseEntity.toPXON());
          baseEntityIDWithBaseType.put(baseEntity.getBaseEntityID(), baseEntity.getBaseType());
        }
        catch (Exception e) {
          RDBMSLogger.instance().exception(e);
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
              new String[] { (String) embeddedVariant.get(VARIANT_ID) });
        }
      }
    }
  }

  /**
   * Prepare attribute, tags and attribute variant
   * 
   * @param baseEntityDTO
   * @param language
   * @param label
   * @param properties
   * @param assetMetadataAttributes
   * @param baseEntityDAO
   * @param executionStatusTable
   * @param diDataType
   */
  public void prepareAttributesAndTags(IBaseEntityDTO baseEntityDTO, String language, String label,
      Map<String, Object> properties, Map<String, String> assetMetadataAttributes, IBaseEntityDAO baseEntityDAO,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable, DiDataType diDataType)
  {
    if(properties == null) return;
    List<String> propertiesToLoad = new ArrayList<>();
    Map<String, String> attributes = (Map<String, String>) getProperties(properties, ITransformationTask.ATTRIBUTES);
    Map<String, Object> tags = (Map<String, Object>) getProperties(properties,  ITransformationTask.TAGS);
    if (StringUtils.isNotBlank(label)) {
      attributes.put(ITransformationTask.NAME_ATTRIBUTE, label);
    }
    if (!CollectionUtils.isEmpty(assetMetadataAttributes)) {
      attributes.putAll(assetMetadataAttributes);
    }

    if (baseEntityDTO.getBaseEntityIID() == 0) {
      // add BaseEntityID as value for nameattribute if not provided
      if (StringUtils.isBlank(attributes.get(NAME_ATTRIBUTE))) {
        attributes.put(NAME_ATTRIBUTE, baseEntityDTO.getBaseEntityID());
      }
      // add default tags for Article base type while creating new article
      // if default values are not present in given input data
      if (baseEntityDTO.getBaseType().equals(IBaseEntityIDDTO.BaseType.ARTICLE)
          && baseEntityDTO.getEmbeddedType().equals(IBaseEntityIDDTO.EmbeddedType.UNDEFINED)) {
        if (tags.get(LIFESTATUSTAG) == null) {
          tags.put(LIFESTATUSTAG, DiTransformationUtils.getTagValues(LIFE_STATUS_INBOX));
        }
        if (tags.get(LISTINGSTATUSTAG) == null) {
          tags.put(LISTINGSTATUSTAG, DiTransformationUtils.getTagValues(LISTING_STATUS_CATLOG));
        }
      }
    }
    propertiesToLoad.addAll(attributes.keySet());
    propertiesToLoad.addAll(tags.keySet());
    Set<IPropertyDTO> validpropertyRecords = new HashSet<>();
    try {
      // load all valid attributes and tags
      if (!CollectionUtils.isEmpty(propertiesToLoad)) {
        DiTransformationUtils.loadPropertyRecords(propertiesToLoad, validpropertyRecords, baseEntityDAO, executionStatusTable);
      }
      
      Map<String, Boolean> languageDependentAttribute = new HashMap<>();
      prepareLanguageDependentMapforAttribute(languageDependentAttribute, attributes);
      // add attributes
      if (!CollectionUtils.isEmpty(attributes)) {
        DiTransformationUtils.prepareValueRecord(baseEntityDTO, attributes, language,
            baseEntityDAO, validpropertyRecords, languageDependentAttribute,executionStatusTable);

      }
      
      // add tags
      if (!CollectionUtils.isEmpty(tags)) {
        DiTransformationUtils.prepareTagRecord(baseEntityDTO, tags, baseEntityDAO, validpropertyRecords);
      }
      
      // add attribute variant information
      List<Map<String, Object>> attributeVariantsInput = (List<Map<String, Object>>) properties.get(ITransformationTask.ATTRIBUTE_VARIANTS);
      if (!CollectionUtils.isEmpty(attributeVariantsInput)) {
        prepareAttributeVariants(attributeVariantsInput, baseEntityDTO, baseEntityDAO,
            executionStatusTable, language, diDataType);
      }
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
      executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
          new String[] { baseEntityDTO.getBaseEntityID() });
    }
  }

  /**
   * @param properties
   * @param propertyLable
   * @return
   */
  private static Object getProperties(Map<String, Object> properties, String propertyLable)
  {
    if (properties.get(propertyLable) != null) {
      return properties.get(propertyLable);
    }
    return new HashMap<>();
  }
  
  /**
   * Prepare attribute variants
   * 
   * @param attributeVariantsInput
   * @param baseEntityDTO
   * @param baseEntityDAO
   * @param executionStatusTable
   * @param language
   * @param diDataType
   * @throws Exception
   */
  public static void prepareAttributeVariants(List<Map<String, Object>> attributeVariantsInput, IBaseEntityDTO baseEntityDTO,
      IBaseEntityDAO baseEntityDAO, IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable, String language,
      DiDataType diDataType) throws Exception
  {
    Set<IPropertyDTO> validpropertyRecords = new HashSet<>();
    for (Map<String, Object> attributeVariant : attributeVariantsInput) {
      Map<String, Object> contextualInputData = (Map<String, Object>) attributeVariant.get(ITransformationTask.CONTEXT);
      IContextDTO contextDto = (IContextDTO) DiUtils.getContextDTO((String) contextualInputData.get(ITransformationTask.CONTEXT_ID),
          ContextType.ATTRIBUTE_CONTEXT);
      ContextualDataDTO contextualDataDTO = new ContextualDataDTO();
      contextualDataDTO.setContextCode(contextDto.getCode());
      DiTransformationUtils.prepareContexualInfo(contextualInputData, contextualDataDTO, diDataType);
      Map<String, Object> properties = (Map<String, Object>) attributeVariant.get(ITransformationTask.PROPERTIES);
      Map<String, String> attributes = (Map<String, String>) getProperties(properties, ITransformationTask.ATTRIBUTES);
      if (CollectionUtils.isEmpty(attributes)) {
        continue;
      }
      validpropertyRecords.clear();
      DiTransformationUtils.loadPropertyRecords(attributes.keySet().stream().collect(Collectors.toList()), validpropertyRecords,
          baseEntityDAO, executionStatusTable);
      
      // Get or set the action
      String action = (String) attributeVariant.get("action");
      if (action != null) {
        action = action.toUpperCase();
        // check for invalid action in action column.
        if (!VALID_ACTION_TYPES.contains(action)) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN050,
              new String[] { action, baseEntityDTO.getBaseEntityID() });
          return;
        }
      }
      
      for (IPropertyDTO attribute : validpropertyRecords) {
        IPropertyRecordDTO propertyRecord = baseEntityDTO.getPropertyRecord(attribute.getIID());
        if (propertyRecord == null && attributes.containsKey(attribute.getCode())) {
          if ((action == null || CREATE.equals(action))) {
            propertyRecord = baseEntityDAO.newValueRecordDTOBuilder(attribute, attributes.get(attribute.getCode())).localeID(language)
                .build();
            ((ValueRecordDTO) propertyRecord).setContextualData(contextualDataDTO);
            baseEntityDTO.getPropertyRecords().add(propertyRecord);
          }
          else if (UPDATE.equals(action)) {
            executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN049,
                new String[] { attribute.getCode() });
            return;
          }
        }
        else if (propertyRecord instanceof IValueRecordDTO) {
          if (CREATE.equals(action)) {
            executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN048,
                new String[] { attribute.getCode() });
            return;
          }
          else if (action == null || UPDATE.equals(action)) {
            IValueRecordDTO valueRecordDTO = (IValueRecordDTO) propertyRecord;
            ContextualDataDTO contextualObject = (ContextualDataDTO) valueRecordDTO.getContextualObject();
            List<String> oldContextTagValueCodes = contextualObject.getContextTagValueCodes();
            Collections.sort(oldContextTagValueCodes);
            List<String> newContextTagValueCodes = contextualDataDTO.getContextTagValueCodes();
            Collections.sort(newContextTagValueCodes);
            // Check if contextual object is same
            if (oldContextTagValueCodes.equals(newContextTagValueCodes)) {
              // If same then simply update values
              ((ValueRecordDTO) propertyRecord).setContextualData(contextualDataDTO);
              valueRecordDTO.setValue(attributes.get(attribute.getCode()));
            }
            else {
              // Else add new record
              propertyRecord = baseEntityDAO.newValueRecordDTOBuilder(attribute, attributes.get(attribute.getCode())).localeID(language)
                  .build();
              ((ValueRecordDTO) propertyRecord).setContextualData(contextualDataDTO);
              baseEntityDTO.getPropertyRecords().add(propertyRecord);
            }
          }
        }
      }
    }
  }
  
  /**
   * To Supports Assign and Delete action for taxonomy and secondary classes
   * (non nature classes)
   * 
   * @param entity
   * @param listForConfig
   * @param isTaxonomy
   * @return
   */
  private Map<String, List<String>> getTaxonomyAndNonNatureClasses(Map<String, Object> entity, List<String> listForConfig,
      boolean isTaxonomy)
  {
    Map<String, List<String>> actionMap = new LinkedHashMap<String, List<String>>();
    List<String> ids = null;
    String classifierType;
    if (isTaxonomy) {
      ids = (List<String>) entity.remove(TAXONOMY_IDS);
      classifierType = TAXONOMIES;
    }
    else {
      ids = (List<String>) entity.remove(TYPE_IDS);
      classifierType = TYPES;
    }
    if (ids != null) {
      actionMap.put(REPLACE, ids);// Existing replace feature
      listForConfig.addAll(ids);
    }
    actionMap.put(ASSIGN, new ArrayList<String>());
    actionMap.put(DELETE, new ArrayList<String>());
    Map<String, Object> actions = (Map<String, Object>) entity.get(ACTIONS);
    if (actions == null || actions.isEmpty()) {
      return actionMap;
    }
    Map<String, List<String>> additions = (Map<String, List<String>>) actions.get(ADD_ACTION);
    Map<String, List<String>> deletions = (Map<String, List<String>>) actions.get(DELETE_ACTION);
    List<String> addValues = (List<String>) additions.get(classifierType);
    List<String> deleteValues = (List<String>) deletions.get(classifierType);
    if (addValues != null) {
      actionMap.get(ASSIGN).addAll(addValues);// Explicit Assign
      listForConfig.addAll(addValues);
    }
    if (deleteValues != null) {
      actionMap.get(DELETE).addAll(deleteValues);// Explicit Delete
      listForConfig.addAll(deleteValues);
    }
    return actionMap;
  }
  
}
