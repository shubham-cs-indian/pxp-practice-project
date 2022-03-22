
package com.cs.di.workflow.tasks;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.cs.core.config.interactor.model.endpoint.GetMappingForImportResponseModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetMappingForImportResponseModel;
import com.cs.core.config.interactor.model.mapping.GetMappingForImportRequestModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleTagMappingModel;
import com.cs.core.config.interactor.model.mapping.IGetMappingForImportRequestModel;
import com.cs.core.config.strategy.usecase.endpoint.IGetMappingForImportStrategy;
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
import com.cs.core.runtime.interactor.utils.OnboardingUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.di.config.interactor.model.task.IGetConfigDetailsByCodesResponseModel;
import com.cs.di.runtime.utils.DiFileUtils;
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

/**
 * @author sangram.shelar
 *
 *         This class convert excel file into the PXON.
 */
@SuppressWarnings("unchecked")
@Component("excelToPXONTask")
public class ExcelToPXONTask extends AbstractToPXONTask {
  
  public static final Integer               DATA_ROW_NUMBER       = 2;
  public static final Integer               HEADER_ROW_NUMBER     = 1;
  public static final String                ATTRIBUTE_TYPE        = "ATTRIBUTE";
  public static final String                TAG_TYPE              = "TAGS";
  protected static final List<String>       INPUT_LIST            = Arrays.asList(RECEIVED_DATA);
  protected static final List<String>       OUTPUT_LIST           = Arrays.asList(PXON, FAILED_FILES, EXECUTION_STATUS);
  protected static final List<WorkflowType> WORKFLOW_TYPES        = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  protected static final List<EventType>    EVENT_TYPES           = Arrays.asList(EventType.INTEGRATION);
  
  protected static final List<String>       DEFAULT_HEADERS    = Arrays.asList(
      ID_COLUMN, FILE_SOURCE_COLUMN, FILE_PATH_COLUMN,
      SIDE1_ID_COLUMN, SIDE2_ID_COLUMN, CONTEXT_ID_COLUMN,
      FROM_DATE_COLUMN, TO_DATE_COLUMN, MASTER_ID_COLUMN, IS_ATTRIBUTE_VARIANT_COLUMN,
      VARIANT_TYPE_COLUMN, PARENT_ID_COLUMN, IS_NATURE_RELATIONSHIP_COLUMN, LANGUAGE_CODE_COLUMN
      );
  
  @Autowired
  protected IGetMappingForImportStrategy    getMappingForImportStrategy;
  
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
    return DiDataType.EXCEL;
  }
  
  /**
   * Validate input parameters
   * 
   * @param inputFields
   * @return
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
   * This method checks if mapping id mentioned in the task input variables
   * exists or not.
   *
   * @param mappingId
   * @return
   */
  public Boolean validateMappingId(String mappingId)
  {
    if (!DiValidationUtil.isRuntimeValue(mappingId)) {
      //return false for invalid conditions here
      IGetMappingForImportRequestModel dataModel = new GetMappingForImportRequestModel();
      dataModel.setMappingId(mappingId);
      try {
        IGetMappingForImportResponseModel mappingModel = getMappingForImportStrategy.execute(dataModel);
        if(mappingModel==null || !mappingModel.getMappingType().equalsIgnoreCase("inboundmapping")) {
          return false;
        }
      }
      catch (Exception e) {
        return false;
      }
    }
    return true;
  }
  
  /**
   * convert excel file into PXON.
   */
  @Override
  public void generatePXON(WorkflowTaskModel workflowTaskModel)
  {
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = workflowTaskModel
        .getExecutionStatusTable();
    Map<String, String> receivedData = (Map<String, String>) DiValidationUtil.validateAndGetRequiredMap(workflowTaskModel,
        RECEIVED_DATA);
    String inboundMappingId = (String) workflowTaskModel.getWorkflowModel().getWorkflowParameterMap().get(IEndpointModel.MAPPINGS);
  
    if (workflowTaskModel.getExecutionStatusTable()
        .isErrorOccurred()) {
      return;
    }
    
    List<String> entities = new ArrayList<>();
    List<String> failedfiles = new ArrayList<>();
    List<String> entitiesToBeCreated = new ArrayList<>();
    
    Map<String, ILocaleCatalogDAO> localCatalogDAOMap = new ConcurrentHashMap<>();
    
    for (Entry<String, String> excelFile : receivedData.entrySet()) {
      try (InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder()
          .decode(excelFile.getValue()));
          XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);) {
        // Prepare relationships map
        Map<String, List<Map<String, Object>>> relationshipsMap = new TreeMap<>();
        prepareMapOfRecordsHavingSameId(workbook, excelFile.getKey(), RELATIONSHIP_SHEET_NAME,
            SIDE1_ID_COLUMN, relationshipsMap, executionStatusTable, inboundMappingId);
        
        // Prepare variants map
        Map<String, List<Map<String, Object>>> variantsMap = new TreeMap<>();
        prepareMapOfRecordsHavingSameId(workbook, excelFile.getKey(), VARIANT_SHEET_NAME, ID_COLUMN,
            variantsMap, executionStatusTable, inboundMappingId);
        
        // Generate PXON for article
        generatePXONForArticles(workbook, entities, excelFile.getKey(), executionStatusTable, workflowTaskModel, localCatalogDAOMap,
            relationshipsMap, variantsMap, entitiesToBeCreated, defaultLanguageCode, inboundMappingId);
        
        // Generate PXON for asset
        generatePXONForAssets(workbook, entities, excelFile.getKey(), executionStatusTable,
            workflowTaskModel, localCatalogDAOMap, relationshipsMap, variantsMap, entitiesToBeCreated, defaultLanguageCode,
            inboundMappingId);
        
        // Generate PXON for relationships
        generatePXONForRelationships(entities, relationshipsMap, executionStatusTable,
            workflowTaskModel, localCatalogDAOMap, defaultLanguageCode, getDataType());
        
        // Generate PXON for variants
        generatePXONForVariants(variantsMap, entities, excelFile.getKey(), executionStatusTable,
            workflowTaskModel, entitiesToBeCreated, localCatalogDAOMap, defaultLanguageCode);
        // Add success file name in execution status
        executionStatusTable.addSuccess(new ObjectCode[] { ObjectCode.MESSAGE_TYPE },
            MessageCode.GEN011, new String[] { excelFile.getKey() });
        
      }
      catch (Exception e) {
        failedfiles.add(excelFile.getKey());
        executionStatusTable.addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE },
            MessageCode.GEN009, new String[] { excelFile.getKey() });
      }
    }
    if (!CollectionUtils.isEmpty(entities)) {
      // Write entities to file
      String filename = writePXONToFile(entities, executionStatusTable);
      
      // Add generated dtos in output variable
      workflowTaskModel.getOutputParameters().put(PXON, filename);
    }
    // Add failed files in output variable
    workflowTaskModel.getOutputParameters().put(FAILED_FILES, failedfiles);
  }
  
  /**
   * Prepare map of records having same id
   * 
   * @param workbook
   * @param fileName
   * @param sheetname
   * @param idColumnName
   * @param recordsHavingSameId
   * @param executionStatusTable
   * @param inboundMappingId
   */
  protected void prepareMapOfRecordsHavingSameId(XSSFWorkbook workbook, String fileName,
      String sheetname, String idColumnName,
      Map<String, List<Map<String, Object>>> recordsHavingSameId,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable,
      String inboundMappingId)
  {
    List<Map<String, Object>> sheetDataMap = prepareMapfromSheet(workbook, sheetname, fileName, executionStatusTable, inboundMappingId);
    if (!CollectionUtils.isEmpty(sheetDataMap)) {
      for (Map<String, Object> data : sheetDataMap) {
        Object idColumnValue = data.get(idColumnName);
        if (idColumnValue == null) {
          continue;
        }
        List<Map<String, Object>> records = recordsHavingSameId.get((String) idColumnValue);
        if (records == null) {
          records = new ArrayList<>();
          recordsHavingSameId.put((String) idColumnValue, records);
        }
        records.add(data);
      }
    }
  }
  
  /**
   * generate PXON For Assets
   * 
   * @param workbook
   * @param entities
   * @param fileName
   * @param executionStatusTable
   * @param workflowTaskModel
   * @param localCatalogDAOMap
   * @param relationshipsMap
   * @param assetsToBeCreated 
   * @param variantsMap 
   * @param defaultLanguageCode
   * @param inboundMappingId
   * @throws Exception
   */
  
  protected void generatePXONForAssets(XSSFWorkbook workbook, List<String> entities,
      String fileName, IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable,
      WorkflowTaskModel workflowTaskModel, Map<String, ILocaleCatalogDAO> localCatalogDAOMap,
      Map<String, List<Map<String, Object>>> relationshipsMap,
      Map<String, List<Map<String, Object>>> variantsMap, List<String> assetsToBeCreated,
      String defaultLanguageCode, String inboundMappingId) throws Exception
  {
    List<Map<String, Object>> entityList = prepareMapfromSheet(workbook, ASSET_SHEET_NAME, fileName,
        executionStatusTable, inboundMappingId);
    generatePXONForEntity(entityList, entities, fileName, executionStatusTable, workflowTaskModel,
        localCatalogDAOMap, relationshipsMap, variantsMap, assetsToBeCreated, defaultLanguageCode,
        getDataType(), IBaseEntityIDDTO.BaseType.ASSET);
  }
  
  /**
   * generate PXON For Articles
   * 
   * @param workbook
   * @param entities
   * @param fileName
   * @param executionStatusTable
   * @param workflowTaskModel
   * @param localCatalogDAOMap
   * @param relationshipsMap
   * @param variantsMap 
   * @param articlesToBeCreated
   * @param defaultLanguageCode
   * @param inboundMappingId
   * @throws Exception
   */
  protected void generatePXONForArticles(XSSFWorkbook workbook, List<String> entities, String fileName,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable, WorkflowTaskModel workflowTaskModel,
      Map<String, ILocaleCatalogDAO> localCatalogDAOMap, Map<String, List<Map<String, Object>>> relationshipsMap,
      Map<String, List<Map<String, Object>>> variantsMap, List<String> articlesToBeCreated, String defaultLanguageCode,
      String inboundMappingId) throws Exception
  {
    List<Map<String, Object>> entityList = prepareMapfromSheet(workbook, ARTICLE_SHEET_NAME, fileName, executionStatusTable,
        inboundMappingId);
    generatePXONForEntity(entityList, entities, fileName, executionStatusTable, workflowTaskModel, localCatalogDAOMap, relationshipsMap,
        variantsMap, articlesToBeCreated, defaultLanguageCode, getDataType(), IBaseEntityIDDTO.BaseType.ARTICLE);
  }
  
  /**
   * Generate PXON for Entity.
   * 
   * @param entityList
   * @param entities
   * @param fileName
   * @param executionStatusTable
   * @param workflowTaskModel
   * @param localCatalogDAOMap
   * @param relationshipsMap
   * @param variantsMap
   * @param entitiesToBeCreated 
   * @param defaultLanguageCode
   * @param diDataType
   * @param baseType
   * @throws Exception
   */
  private void generatePXONForEntity(List<Map<String, Object>> entityList, List<String> entities, String fileName,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable, WorkflowTaskModel workflowTaskModel,
      Map<String, ILocaleCatalogDAO> localCatalogDAOMap, Map<String, List<Map<String, Object>>> relationshipsMap,
      Map<String, List<Map<String, Object>>> variantsMap, List<String> entitiesToBeCreated, String defaultLanguageCode,
      DiDataType diDataType, BaseType baseType) throws Exception
  {
    if (!CollectionUtils.isEmpty(entityList)) {
      for (Map<String, Object> entity : entityList) {
        String id = (String) entity.remove(ID_COLUMN);
        try {
          String languageCode = (String) entity.remove(LANGUAGE_CODE_COLUMN);
          languageCode = StringUtils.isBlank(languageCode) ? defaultLanguageCode : languageCode;
          ILocaleCatalogDAO localeCatalogDAO = DiUtils.getLocaleCatalogDAO(localCatalogDAOMap,
              languageCode, workflowTaskModel);
         
          String natureKlass = (String) entity.remove(KLASS_COLUMN);          
          List<String> nonNatureKlasses = new ArrayList<>();
          List<String> natureKlasses = new ArrayList<>();
          List<String> typeIds = new ArrayList<>();
          typeIds.add(natureKlass);
          
          List<String> taxonomyListForConfig = new ArrayList<>();
          Map<String, List<String>> taxonomyMap = fetchTaxonomyActionList(entity,taxonomyListForConfig);
          Map<String, List<String>> nonNatureKlassesMap = fetchNonNatureActionList(entity,typeIds);
          
          IGetConfigDetailsByCodesResponseModel configDataForKlassAndTaxonomy = getConfigDataForKlassAndTaxonomy(
              typeIds, taxonomyListForConfig, executionStatusTable, nonNatureKlasses, natureKlasses);
          
          // Get or Create baseEntity
          IClassifierDTO natureKlassDTO = RDBMSUtils.newConfigurationDAO().createClassifier(natureKlasses.get(0), ClassifierType.CLASS);
          
          IBaseEntityDTO baseEntity = getOrCreateBaseEntity(workflowTaskModel, baseType, id, natureKlassDTO, localeCatalogDAO);
          
          baseEntity.setBaseLocaleID(languageCode);
          IBaseEntityDAO baseEntityDAO = localeCatalogDAO.openBaseEntity(baseEntity);
          // Upload Asset to server and get uploaded asset details
          if (IBaseEntityIDDTO.BaseType.ASSET.equals(baseType)) {
            Map<String, String> assetMetadataAttributes = uploadAssetOnServer(natureKlasses,
                baseEntity, (String) entity.remove(FILE_SOURCE_COLUMN),
                (String) entity.remove(FILE_PATH_COLUMN));
            // Add asset metadata properties to attributes.
            if (!CollectionUtils.isEmpty(assetMetadataAttributes)) {
              entity.putAll(assetMetadataAttributes);
            }
          }
          prepareAttributesTagsAndClassifiers(executionStatusTable, entity, languageCode, taxonomyMap,
              nonNatureKlassesMap, configDataForKlassAndTaxonomy, baseEntity, baseEntityDAO);
          
          List<Map<String, Object>> variants = variantsMap.get(baseEntity.getBaseEntityID());
          if (variants != null) {
            prepareAttributeVariants(baseEntity, variants, baseEntityDAO, executionStatusTable, defaultLanguageCode);
            variantsMap.remove(baseEntity.getBaseEntityID());
          }
          
          List<Map<String, Object>> side1Relations = relationshipsMap
              .get(baseEntity.getBaseEntityID());
          if (side1Relations != null) {
            updateDTORelations(baseEntity, side1Relations, baseEntityDAO, getDataType(), workflowTaskModel, localCatalogDAOMap,
                executionStatusTable);
            relationshipsMap.remove(baseEntity.getBaseEntityID());
          }
          
          entitiesToBeCreated.add(id);
          entities.add(baseEntity.toPXON());
          baseEntityIDWithBaseType.put(baseEntity.getBaseEntityID(), baseType);
        }
        catch (Exception e) {
          e.printStackTrace();
          RDBMSLogger.instance().exception(e);
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID },
              MessageCode.GEN014, new String[] { id });
        }
      }
    }
  }
  
  /**
   * generate PXON For Variants
   * 
   * @param variantsMap
   * @param entities
   * @param fileName
   * @param executionStatusTable
   * @param workflowTaskModel
   * @param entitiesToBeCreated
   * @param localCatalogDAOMap
   * @param defaultLanguageCode
   */
  protected void generatePXONForVariants(Map<String, List<Map<String, Object>>> variantsMap,
      List<String> entities, String fileName,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable,
      WorkflowTaskModel workflowTaskModel, List<String> entitiesToBeCreated,
      Map<String, ILocaleCatalogDAO> localCatalogDAOMap, String defaultLanguageCode)
  {
    if (!CollectionUtils.isEmpty(variantsMap)) {
      for (Entry<String, List<Map<String, Object>>> variants : variantsMap.entrySet()) {
        for (Map<String, Object> variant : variants.getValue()) {
          try {
            // Check if attribute variant
            if (variant.get(IS_ATTRIBUTE_VARIANT_COLUMN)
                .toString()
                .equals(TRUE)) {
              continue;
            }
            String languageCode = (String) variant.remove(LANGUAGE_CODE_COLUMN);
            String natureKlass = (String) variant.remove(KLASS_COLUMN);
            List<String> nonNatureKlasses = new ArrayList<>();
            List<String> natureKlasses = new ArrayList<>();
            
            languageCode = StringUtils.isBlank(languageCode) ? defaultLanguageCode : languageCode;
            ILocaleCatalogDAO localeCatalogDAO = DiUtils.getLocaleCatalogDAO(localCatalogDAOMap,
                languageCode, workflowTaskModel);
            List<String> typeIds =  new ArrayList<String>();
            typeIds.add(natureKlass);
            List<String> taxonomyListForCofig = new ArrayList<>();            
            Map<String, List<String>> taxonomyMap =  fetchTaxonomyActionList(variant, taxonomyListForCofig);
            Map<String, List<String>> nonNatureKlassMap =  fetchNonNatureActionList(variant, typeIds);
            IGetConfigDetailsByCodesResponseModel configDataForKlassAndTaxonomy = getConfigDataForKlassAndTaxonomy(
                typeIds, taxonomyListForCofig, executionStatusTable, nonNatureKlasses, natureKlasses);
            // Prepare nature class classifier DTO
            IClassifierDTO natureKlassDTO = RDBMSUtils.newConfigurationDAO()
                .createClassifier(natureKlasses.get(0), ClassifierType.CLASS);
            
            // Get or Create embedded baseEntity
            IBaseEntityDTO baseEntity = getEmbeddedBaseEntityDTO(variantsMap, variant,
                executionStatusTable, entitiesToBeCreated, natureKlassDTO, localeCatalogDAO,
                getDataType(), workflowTaskModel.getWorkflowModel(), configDataForKlassAndTaxonomy);
            if (baseEntity == null) {
              continue;
            }
            
            // prepare contextual info
            IContextualDataDTO contextualObject = baseEntity.getContextualObject();
            DiTransformationUtils.prepareContexualInfo(variant, contextualObject, getDataType());
            // Get DAO
            IBaseEntityDAO baseEntityDAO = localeCatalogDAO.openBaseEntity(baseEntity);
            prepareAttributesTagsAndClassifiers(executionStatusTable, variant, languageCode, taxonomyMap,
                nonNatureKlassMap, configDataForKlassAndTaxonomy, baseEntity, baseEntityDAO);
            
            // Add attribute variants
            List<Map<String, Object>> attributeVariants = variantsMap
                .get(baseEntity.getBaseEntityID());
            if (attributeVariants != null) {
              prepareAttributeVariants(baseEntity, attributeVariants, baseEntityDAO,
                  executionStatusTable, defaultLanguageCode);
            }
            
            entitiesToBeCreated.add(variants.getKey());
            baseEntityIDWithBaseType.put(baseEntity.getBaseEntityID(), baseEntity.getBaseType());
            entities.add(baseEntity.toPXON());
          }
          catch (Exception e) {
            entitiesToBeCreated.add(variants.getKey());
            executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID },
                MessageCode.GEN014, new String[] { (String) variant.get(ID_COLUMN) });
          }
        }
      }
      
      // Remove all processed attribute variants and embedded variants
      variantsMap.keySet()
          .removeAll(entitiesToBeCreated);
      
      // Add remaining attribute variants
      addRemainingAttributeVariants(variantsMap, entities, executionStatusTable, workflowTaskModel,
          localCatalogDAOMap, defaultLanguageCode);
    }
  }
  
  /**
   * Prepares attribute variant
   * 
   * @param baseEntity
   * @param variants
   * @param baseEntityDAO
   * @param executionStatusTable
   * @param defaultLanguageCode
   */
  private void prepareAttributeVariants(IBaseEntityDTO baseEntity,
      List<Map<String, Object>> variants, IBaseEntityDAO baseEntityDAO,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable,
      String defaultLanguageCode)
  {
    for (Map<String, Object> variant : variants) {
      try {
        String isAttributeVariant = (String) variant.get(IS_ATTRIBUTE_VARIANT_COLUMN);
        if (!StringUtils.isBlank(isAttributeVariant) && isAttributeVariant.toString().equals(TRUE)) {
          String languageCode = (String) variant.remove(LANGUAGE_CODE_COLUMN);
          languageCode = StringUtils.isBlank(languageCode) ? defaultLanguageCode : languageCode;
          IContextDTO contextDto = DiUtils.getContextDTO((String) variant.get(CONTEXT_ID_COLUMN), ContextType.ATTRIBUTE_CONTEXT);
          ContextualDataDTO contextualDataDTO = new ContextualDataDTO();
          contextualDataDTO.setContextCode(contextDto.getCode());
          DiTransformationUtils.prepareContexualInfo(variant, contextualDataDTO, getDataType());
          Set<IPropertyDTO> validpropertyRecords = new HashSet<>();
          Map<String, String> attributes = new HashMap<>();
          Map<String, Object> tags = new HashMap<>();
          
          // Get or set the action
          String action = (String) variant.get(ACTION);
          if (action != null) {
            action = action.toUpperCase();
            // check for invalid action in action column.
            if (!VALID_ACTION_TYPES.contains(action)) {
              executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN050,
                  new String[] { action, baseEntity.getBaseEntityID() });
              return;
            }
          }
          
          // load property records and separate attributes and tags
          loadPropertyRecords(variant, baseEntityDAO, validpropertyRecords, attributes, tags, executionStatusTable);
          
          // add attribute variant and attribute values to all attributes
          for (IPropertyDTO attribute : validpropertyRecords) {
            try {
            IPropertyRecordDTO propertyRecord = baseEntity.getPropertyRecord(attribute.getIID());
            String attributeValue = attributes.get(attribute.getCode());
            if (propertyRecord == null && attributes.containsKey(attribute.getCode())) {
              if ((action == null || CREATE.equals(action))) {
                propertyRecord = DiTransformationUtils.createValueRecord(baseEntityDAO, attribute, attributeValue, languageCode);
                ((ValueRecordDTO) propertyRecord).setContextualData(contextualDataDTO);
                baseEntity.getPropertyRecords().add(propertyRecord);
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
                  
                  switch (valueRecordDTO.getProperty().getPropertyType()) {
                    case DATE:
                      attributeValue = Long.toString(DiUtils.getLongValueOfDateString(attributeValue));
                      valueRecordDTO.setAsNumber(Double.parseDouble(attributeValue));
                      break;
                    case HTML:
                      valueRecordDTO.setAsHTML(attributeValue);
                      break;
                    case PRICE:
                    case MEASUREMENT:
                    case NUMBER:
                      valueRecordDTO.setAsNumber(Double.parseDouble(attributeValue));
                      break;
                    default:
                      break;
                  }
                  valueRecordDTO.setValue(attributeValue);
                }
                else {
                  // Else add new record
                  propertyRecord = DiTransformationUtils.createValueRecord(baseEntityDAO, attribute, attributeValue, languageCode);
                  ((ValueRecordDTO) propertyRecord).setContextualData(contextualDataDTO);
                  baseEntity.getPropertyRecords().add(propertyRecord);
                }
              }
            }
          }
          catch(Exception e)
          {
            executionStatusTable.addWarning(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GENO51, new String[] { attribute.getCode() });
          }
          }
        }
      }
      catch (Exception e) {
        executionStatusTable.addWarning(MessageCode.GEN030);
      }
    }
  }
  
  /**
   * Add attribute variants
   * 
   * @param variantsMap
   * @param entities
   * @param executionStatusTable
   * @param workflowTaskModel
   * @param localCatalogDAOMap
   * @param defaultLanguageCode
   */
  private void addRemainingAttributeVariants(Map<String, List<Map<String, Object>>> variantsMap,
      List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable,
      WorkflowTaskModel workflowTaskModel, Map<String, ILocaleCatalogDAO> localCatalogDAOMap,
      String defaultLanguageCode)
  {
    for (Entry<String, List<Map<String, Object>>> attributeVariants : variantsMap.entrySet()) {
      try {
        Map<String, Object> attributeVariant = attributeVariants.getValue().get(0);
        String id = (String) attributeVariant.remove(ID_COLUMN);
        String languageCode = (String) attributeVariant.remove(LANGUAGE_CODE_COLUMN);
        languageCode = StringUtils.isBlank(languageCode) ? defaultLanguageCode : languageCode;
        ILocaleCatalogDAO localeCatalogDAO = DiUtils.getLocaleCatalogDAO(localCatalogDAOMap, languageCode, workflowTaskModel);
        IBaseEntityDTO baseEntity = null;
        if (!id.isBlank()) {
          baseEntity = localeCatalogDAO.getEntityByID(id);
        }
        IBaseEntityDAO baseEntityDAO = localeCatalogDAO.openBaseEntity(baseEntity);
        prepareAttributeVariants(baseEntity, attributeVariants.getValue(), baseEntityDAO, executionStatusTable, defaultLanguageCode);
        entities.add(baseEntity.toPXON());
      }
      catch (Exception e) {
        executionStatusTable.addWarning(MessageCode.GEN030);
      }
    }
  }
  
  /**
   * This method will create map from excel sheet.
   * 
   * @param workbook
   * @param sheetName
   * @param fileName
   * @param executionStatusTable
   * @param inboundMappingId
   * @return
   */
  private List<Map<String, Object>> prepareMapfromSheet(XSSFWorkbook workbook, String sheetName,
      String fileName, IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable,
      String inboundMappingId)
  {
    List<Map<String, Object>> entityList = new ArrayList<>();
    try {
      XSSFSheet sheet = DiFileUtils.getSheet(workbook, sheetName);
      if (sheet == null) {
        throw new Exception();
      }
      List<String> headersToRead = DiFileUtils.getheaders(sheet, HEADER_ROW_NUMBER);
      IGetMappingForImportResponseModel mapping = new GetMappingForImportResponseModel();
      IGetMappingForImportRequestModel model = new GetMappingForImportRequestModel();
      if (inboundMappingId != null) 
        mapping = getMappings(inboundMappingId, headersToRead, mapping, model);
      
      HashMap<String, HashMap<String, Object>> tagValuesMapping = new HashMap<>();
      List<IConfigRuleTagMappingModel> tagMappings = mapping.getTagMappings();
      OnboardingUtils.prepareTagValuesMappings(tagValuesMapping, tagMappings);
      Integer emptyRowNumber = 0;
      Integer lastRowNumber = sheet.getLastRowNum();
      lastRowNumber++;
      for (int rowCount = DATA_ROW_NUMBER - 1; rowCount < lastRowNumber; rowCount++) {
        Row itemRow = sheet.getRow(rowCount);
        if (itemRow != null && emptyRowNumber < 6) {
          emptyRowNumber = 0;
          Map<String, Object> entityMap = prepareMapfromRow(itemRow, headersToRead, mapping,
              tagValuesMapping);
          if (!CollectionUtils.isEmpty(entityMap)) {
            entityList.add(entityMap);
          }
        }
        else if (emptyRowNumber > 5) {
          // If there are more than 5 empty rows then stop the processing
          break;
        }
        else {
          emptyRowNumber++;
        }
      }
      
    }
    catch (Exception e) {
      executionStatusTable.addError(new ObjectCode[] {}, MessageCode.GEN007,
          new String[] { sheetName, fileName });
    }
    return entityList;
  }

  private IGetMappingForImportResponseModel getMappings(String mappingId,
      List<String> headersToRead, IGetMappingForImportResponseModel mapping,
      IGetMappingForImportRequestModel model) throws Exception
  {
    model.setMappingId(mappingId);
    model.setFileHeaders(headersToRead);
    mapping = getMappingForImportStrategy.execute(model);
    return mapping;
  }
  
  /**
   * This Method reads a row in excel file and creates a map which can then be
   * used to prepare PXON
   * 
   * @param itemRow
   * @param headersToRead
   * @param mapping
   * @param tagValuesMapping
   * @return
   */
  private Map<String, Object> prepareMapfromRow(Row itemRow, List<String> headersToRead,
      IGetMappingForImportResponseModel mapping,
      HashMap<String, HashMap<String, Object>> tagValuesMapping)
  {
    Map<String, Object> entityMap = new HashMap<>();
    int cellNumber = 0;
    for (String header : headersToRead) {
      Cell itemCell = itemRow.getCell(cellNumber);
      if (StringUtils.isNoneBlank(header)) {
        String cellValueInString = DiFileUtils.getCellValueInString(itemCell)
            .trim();
        if (!StringUtils.isBlank(cellValueInString)) {
          if (DEFAULT_HEADERS.contains(header)) {
            entityMap.put(header, cellValueInString);
          }
          else {
            prepareEntityMapAcordingToMapping(header, cellValueInString, entityMap, mapping,
                tagValuesMapping);
          }
        }
        // needed If Column is present check
        // PXPFDEV-20123 :+PXPFDEV-20122 Excel Import Support Assign and Delete
        // Action
        else if (TAXONOMIES_COLUMN.equals(header) || SECONDARY_KLASSES_COLUMN.equals(header)) {
          entityMap.put(header, new ArrayList<String>());
        }
        else {
          entityMap.put(header, "");
        }
      }
      cellNumber++;
    }
    return entityMap;
  }
  
  /**
   *  prepareEntityMapAcordingToMapping 
   * @param header
   * @param cellValueInString
   * @param entityMap
   * @param mapping
   * @param tagValuesMapping
   */
  private void prepareEntityMapAcordingToMapping(String header, String cellValueInString,
      Map<String, Object> entityMap, IGetMappingForImportResponseModel mapping,
      HashMap<String, HashMap<String, Object>> tagValuesMapping)
  {
    switch (header) {
      case KLASS_COLUMN:
        putNatureKlassWithMapping(header, cellValueInString, entityMap, mapping.getClasses());
        break;
      
      case SECONDARY_KLASSES_COLUMN:
      case ASSIGN_NONNATUREKLASS_COLUMN:
      case DELETE_NONNATUREKLASS_COLUMN:
        putClassificationWithMapping(header, cellValueInString, entityMap, mapping.getClasses());
        break;
      
      case TAXONOMIES_COLUMN:
      case ASSIGN_TAXONOMIES_COLUMN:
      case DELETE_TAXONOMIES_COLUMN:
        putClassificationWithMapping(header, cellValueInString, entityMap, mapping.getTaxonomies());
        break;
      
      case RELATIONSHIP_CODES_COLUMN:
        putRelationshipWithMapping(header, cellValueInString, entityMap, mapping.getRelationships());
        break;
        
      default:
        putPropertiesMapping(header, cellValueInString, entityMap, mapping, tagValuesMapping);
    }
  }
  
  /**
   * put data acording to mapping of tags and taxonomy
   * @param header
   * @param cellValueInString
   * @param entityMap
   * @param mapping
   * @param tagValuesMapping
   */
  private void putPropertiesMapping(String header, String cellValueInString,
      Map<String, Object> entityMap, IGetMappingForImportResponseModel mapping,
      HashMap<String, HashMap<String, Object>> tagValuesMapping)
  {
    Map<String, List<String>> attributeMapping = mapping.getAttributes();
    Map<String, String> tagMapping = mapping.getTags();
    
    if (attributeMapping.containsKey(header)) {
      for (String attributeId : attributeMapping.get(header)) {
        entityMap.put(attributeId, cellValueInString);
      }
    }
    else if (tagMapping.containsKey(header)) {
      String tagId = tagMapping.get(header);
      entityMap.put(tagId,
          getTagValuesWithMapping(cellValueInString, tagValuesMapping.get(tagId)));
    }
    else {
      entityMap.put(header, cellValueInString);
    }
  }
  
  /**
   * put data with mapping of tagValues
   * @param cellValueInString
   * @param tagValueMapping
   * @return
   */
  private Object getTagValuesWithMapping(String cellValueInString,
      HashMap<String, Object> tagValueMapping)
  {
    List<String> tagValues = getListFromString(cellValueInString);
    List<String> tagVaueIds = new ArrayList<String>();
    for(String tagValue: tagValues) {
      if(tagValueMapping == null || !tagValueMapping.containsKey(tagValue)) {
        tagVaueIds.add(tagValue);
      }
      else {
        tagVaueIds.add((String) tagValueMapping.get(tagValue));
      }
    }
    return String.join(",", tagVaueIds);
  }

  /**
   * put mapping for classess and taxonomy
   * @param header
   * @param cellValueInString
   * @param entityMap
   * @param classifierMapping
   */
  private void putClassificationWithMapping(String header, String cellValueInString,
      Map<String, Object> entityMap, Map<String, String> classifierMapping)
  {
    List<String> classifiers = getListFromString(cellValueInString);
    List<String> classifierIds = new ArrayList<String>();
    for (String klass : classifiers) {
      if (classifierMapping.containsKey(klass)) {
        classifierIds.add(classifierMapping.get(klass));
      }
      else {
        classifierIds.add(klass);
      }
    }
    entityMap.put(header, classifierIds);
  }
  
  /**
   * put mapping for relationship
   * @param header
   * @param cellValueInString
   * @param entityMap
   * @param relationshipMapping
   */
  private void putRelationshipWithMapping(String header, String cellValueInString,
      Map<String, Object> entityMap, Map<String, String> relationshipMapping)
  {
    if (relationshipMapping != null && !relationshipMapping.isEmpty() ) {
      if (relationshipMapping.containsKey(cellValueInString)) {
        entityMap.put(header, relationshipMapping.get(cellValueInString));
      }
    }
    else {
      entityMap.put(header, cellValueInString);
    }
  }
  
  /**
   * putNatureKlassWithMapping
   * @param header
   * @param cellValueInString
   * @param entityMap
   * @param klassMapping
   */

  private void putNatureKlassWithMapping(String header, String cellValueInString,
      Map<String, Object> entityMap, Map<String, String> klassMapping)
  {
    if (klassMapping.containsKey(cellValueInString)) {
      entityMap.put(header, klassMapping.get(cellValueInString));
    }
    else {
      entityMap.put(header, cellValueInString);
    }
  }
  
  /**
   * load all the valid property records and separate the attributes and tags.
   * 
   * @param entity
   * @param baseEntityDAO
   * @param validpropertyRecords
   * @param attributes
   * @param tags
   * @param executionStatusTable
   * @throws Exception
   */
  private void loadPropertyRecords(Map<String, Object> entity, IBaseEntityDAO baseEntityDAO, 
      Set<IPropertyDTO> validpropertyRecords, Map<String, String> attributes, Map<String, Object> tags,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable) throws Exception
  {
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();

    if (baseEntityDTO.getBaseEntityIID() == 0) {
      // add BaseEntityID as value for nameattribute if not provided
      if (StringUtils.isBlank((String) entity.get(NAME_ATTRIBUTE))) {
        entity.put(NAME_ATTRIBUTE, baseEntityDTO.getBaseEntityID());
      }
      // add default tags for Article base type while creating new article
      // if default values are not present in given input data
      if (baseEntityDTO.getBaseType().equals(IBaseEntityIDDTO.BaseType.ARTICLE)
          && baseEntityDTO.getEmbeddedType().equals(IBaseEntityIDDTO.EmbeddedType.UNDEFINED)) {
        if (entity.get(LIFESTATUSTAG) == null) {
          entity.put(LIFESTATUSTAG, LIFE_STATUS_INBOX);
        }
        if (entity.get(LISTINGSTATUSTAG) == null) {
          entity.put(LISTINGSTATUSTAG, LISTING_STATUS_CATLOG);
        }
      }
    }
    List<String> propertiesToLoad = new ArrayList<>();
    propertiesToLoad.addAll(entity.keySet());
    for (String property : propertiesToLoad) {
      try {
        IPropertyDTO propertyDTO = RDBMSUtils.getPropertyByCode(property);
        validpropertyRecords.add(propertyDTO);
        filterPropertiesBaseOnType(entity, attributes, tags, propertyDTO);
      }
      catch (Exception e) {
        executionStatusTable.addWarning(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN015, new String[] { property });
      }
    }
    Set<IPropertyRecordDTO> existingPropertyRecords = baseEntityDTO.getPropertyRecords();
    Set<IPropertyRecordDTO> propertyRecords = new HashSet<>(existingPropertyRecords);
    baseEntityDAO.loadPropertyRecords(validpropertyRecords.toArray(new IPropertyDTO[0]));
    existingPropertyRecords.addAll(propertyRecords);
  }
  
  /**
   * filter Properties BaseOn Type
   * 
   * @param asset
   * @param attributes
   * @param tags
   * @param propertyDTO
   */
  private void filterPropertiesBaseOnType(Map<String, Object> asset, Map<String, String> attributes,
      Map<String, Object> tags, IPropertyDTO propertyDTO)
  {
    String propertyValueString = (String) asset.get(propertyDTO.getCode());
    if (propertyDTO.getPropertyType().getSuperType().toString().equals(ATTRIBUTE_TYPE)) {
      attributes.put(propertyDTO.getCode(), propertyValueString);
    }
    else if (propertyDTO.getPropertyType().getSuperType().toString().equals(TAG_TYPE)) {
      tags.put(propertyDTO.getCode(),
          !propertyValueString.equals("") ? DiTransformationUtils.getTagValues(propertyValueString) : new ArrayList<>());
    }
  }
  
  /**
   * This method return List for LOV tag and returns string for boolean tag
   * 
   * @param tagValue
   * @return
   */
  private Object getTagValues(String tagValue)
  {
    String[] tagValues = tagValue.split(",");
    if (tagValues.length == 1 && (tagValues[0].equals(FALSE) || tagValues[0].equals(TRUE))) {
      return tagValues[0];
    }
    else {
      return new ArrayList<>(Arrays.asList(tagValues));
    }
  }
  
  /**
   * Convert comma separated string to list.
   * 
   * @param commaSeperatedString
   * @return
   */
  private List<String> getListFromString(String commaSeperatedString)
  {
    if (commaSeperatedString == null) {
      return new ArrayList<>();
    }
    String[] splitedArray = commaSeperatedString.split(",");
    return new ArrayList<>(Arrays.asList(splitedArray));
  }
  
  /**
   * prepare Attributes And Tags
   * 
   * @param executionStatusTable
   * @param entity
   * @param languageCode
   * @param taxonomyList
   * @param nonNatureKlasses
   * @param configDataForKlassAndTaxonomy
   * @param baseEntity
   * @param baseEntityDAO
   * @throws Exception
   */
  
  private void prepareAttributesTagsAndClassifiers(
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable,
      Map<String, Object> entity, String languageCode, Map<String, List<String>> taxonomyMap,
      Map<String, List<String>> nonNatureKlasses,
      IGetConfigDetailsByCodesResponseModel configDataForKlassAndTaxonomy,
      IBaseEntityDTO baseEntity, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    Set<IPropertyDTO> validpropertyRecords = new HashSet<>();
    Map<String, String> attributes = new HashMap<>();
    Map<String, Object> tags = new HashMap<>();
    
    // load property records and separate attributes and tags
    loadPropertyRecords(entity, baseEntityDAO, validpropertyRecords, attributes,
        tags, executionStatusTable);
    
    Map<String, Boolean> languageDependentAttribute = new HashMap<>();
    prepareLanguageDependentMapforAttribute(languageDependentAttribute,attributes);
    // add attributes
    DiTransformationUtils.prepareValueRecord(baseEntity, attributes, languageCode, baseEntityDAO,
        validpropertyRecords, languageDependentAttribute,executionStatusTable);
    
    // add tags
    DiTransformationUtils.prepareTagRecord(baseEntity, tags, baseEntityDAO, validpropertyRecords);
    
    // Add taxonomy and nonNatureKlasses
    DiTransformationUtils.prepareClassifiers(taxonomyMap, nonNatureKlasses, configDataForKlassAndTaxonomy, baseEntity, baseEntityDAO,
        executionStatusTable);
    
  }
  
  /**
   * to Supports Assign and Delete action 
   * for taxonomy
   * @param entity
   * @param classifierListForConfig
   * @return
   */
  private Map<String, List<String>> fetchTaxonomyActionList(Map<String, Object> entity, List<String> classifierListForConfig)
  {
    Map<String, List<String>> taxonomyMap = new LinkedHashMap<String, List<String>>();
    List<String> taxonomies = null;
    List<String> assignTaxonomies  = null;
    if (entity.keySet().contains(ASSIGN_TAXONOMIES_COLUMN)
        && !(entity.get(ASSIGN_TAXONOMIES_COLUMN) == null || entity.get(ASSIGN_TAXONOMIES_COLUMN) == "")) {
      assignTaxonomies = (List<String>) entity.remove(ASSIGN_TAXONOMIES_COLUMN);
    }
    List<String> deletedTaxonomies = null;
    if (entity.keySet().contains(DELETE_TAXONOMIES_COLUMN)
        && !(entity.get(DELETE_TAXONOMIES_COLUMN) == null || entity.get(DELETE_TAXONOMIES_COLUMN) == "")) {
      deletedTaxonomies = (List<String>) entity.remove(DELETE_TAXONOMIES_COLUMN);
    }
    taxonomyMap.put(ASSIGN, new ArrayList<String>());
    taxonomyMap.put(DELETE, new ArrayList<String>());
    // TAXONOMIES_COLUMN present check
    if (entity.keySet().contains(TAXONOMIES_COLUMN)
        && !(entity.get(TAXONOMIES_COLUMN) == null || entity.get(TAXONOMIES_COLUMN) == "")) {
      taxonomies = (List<String>) entity.remove(TAXONOMIES_COLUMN);
      // TAXONOMIES_COLUMN is blank/Default
      taxonomyMap.put(REPLACE, new ArrayList<String>());
      // TAXONOMIES_COLUMN is not blank
      if (taxonomies != null) {
        if (deletedTaxonomies != null) {
          taxonomies.removeAll(deletedTaxonomies);//If Explicit Delete matches Replace Taxonomies
        }
        taxonomyMap.get(REPLACE).addAll(taxonomies);// Existing Column Replace
        classifierListForConfig.addAll(taxonomies);
      }
    }
    if (assignTaxonomies != null) {
      taxonomyMap.get(ASSIGN).addAll(assignTaxonomies);// Explicit Assign
      classifierListForConfig.addAll(assignTaxonomies);
    }
    if (deletedTaxonomies != null) {
      taxonomyMap.get(DELETE).addAll(deletedTaxonomies);// Explicit Delete
      classifierListForConfig.addAll(deletedTaxonomies);
    }
    return taxonomyMap;
  }
  
  /**
   * to Supports Assign and Delete action 
   * for nonNature
   * @param entity
   * @param classifierListForConfig
   * @return
   */
  private Map<String, List<String>> fetchNonNatureActionList(Map<String, Object> entity, List<String> classifierListForConfig)
  {
    Map<String, List<String>> nonNatureMap = new LinkedHashMap<String, List<String>>();
    List<String> nonNature = null;
    List<String>  assignNonNature = null;
    if (entity.keySet().contains(ASSIGN_NONNATUREKLASS_COLUMN)
        && !(entity.get(ASSIGN_NONNATUREKLASS_COLUMN) == null || entity.get(ASSIGN_NONNATUREKLASS_COLUMN) == "")) {
      assignNonNature =(List<String>) entity.remove(ASSIGN_NONNATUREKLASS_COLUMN);
    }
    List<String>  deletedNonNature = null;
    if(entity.keySet().contains(DELETE_NONNATUREKLASS_COLUMN)
        && !(entity.get(DELETE_NONNATUREKLASS_COLUMN) == null || entity.get(DELETE_NONNATUREKLASS_COLUMN) == "")) {
     deletedNonNature = (List<String>) entity.remove(DELETE_NONNATUREKLASS_COLUMN);
    }
    nonNatureMap.put(ASSIGN, new ArrayList<String>());
    nonNatureMap.put(DELETE, new ArrayList<String>());
    // TAXONOMIES_COLUMN present check
    if (entity.keySet().contains(SECONDARY_KLASSES_COLUMN)
        && !(entity.get(SECONDARY_KLASSES_COLUMN) == null || entity.get(SECONDARY_KLASSES_COLUMN) == "")) {
      nonNature = (List<String>) entity.remove(SECONDARY_KLASSES_COLUMN);
      // TAXONOMIES_COLUMN is blank/Default
      nonNatureMap.put(REPLACE, new ArrayList<String>());
      // TAXONOMIES_COLUMN is not blank
      if (nonNature != null) {
        if (deletedNonNature != null) {
          nonNature.removeAll(deletedNonNature);//If Explicit Delete matches Replace nonNature
        }
        nonNatureMap.get(REPLACE).addAll(nonNature);// Existing Column Replace
        classifierListForConfig.addAll(nonNature);
      }
    }
    if (assignNonNature != null) {
      nonNatureMap.get(ASSIGN).addAll(assignNonNature);// Explicit Assign
      classifierListForConfig.addAll(assignNonNature);
    }
    if (deletedNonNature != null) {
      nonNatureMap.get(DELETE).addAll(deletedNonNature);// Explicit Delete
      classifierListForConfig.addAll(deletedNonNature);
    }
    return nonNatureMap;
  }
}
