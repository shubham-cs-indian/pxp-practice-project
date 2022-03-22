package com.cs.di.workflow.tasks;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.cs.config.dto.ConfigAttributeDTO;
import com.cs.config.dto.ConfigClassifierDTO;
import com.cs.config.dto.ConfigContextDTO;
import com.cs.config.dto.ConfigDataRuleDTO;
import com.cs.config.dto.ConfigDataRuleIntermediateEntitysDTO;
import com.cs.config.dto.ConfigDataRuleTagRuleDTO;
import com.cs.config.dto.ConfigDataRuleTagsDTO;
import com.cs.config.dto.ConfigEmbeddedRelationDTO;
import com.cs.config.dto.ConfigGlobalPermissionDTO;
import com.cs.config.dto.ConfigGoldenRecordRuleDTO;
import com.cs.config.dto.ConfigHeaderPermisionDTO;
import com.cs.config.dto.ConfigLanguageDTO;
import com.cs.config.dto.ConfigMergeEffectDTO;
import com.cs.config.dto.ConfigMergeEffectTypeDTO;
import com.cs.config.dto.ConfigNatureRelationshipDTO;
import com.cs.config.dto.ConfigNormalizationDTO;
import com.cs.config.dto.ConfigOrganizationDTO;
import com.cs.config.dto.ConfigPermissionDTO;
import com.cs.config.dto.ConfigPropertyCollectionDTO;
import com.cs.config.dto.ConfigPropertyPermissionDTO;
import com.cs.config.dto.ConfigRelationshipDTO;
import com.cs.config.dto.ConfigRelationshipPermissionDTO;
import com.cs.config.dto.ConfigRoleDTO;
import com.cs.config.dto.ConfigRuleEntityDTO;
import com.cs.config.dto.ConfigSectionElementDTO;
import com.cs.config.dto.ConfigTabDTO;
import com.cs.config.dto.ConfigTagDTO;
import com.cs.config.dto.ConfigTagValueDTO;
import com.cs.config.dto.ConfigTaskDTO;
import com.cs.config.dto.ConfigTranslationDTO;
import com.cs.config.dto.ConfigUserDTO;
import com.cs.config.idto.IConfigClassifierDTO;
import com.cs.config.idto.IConfigDataRuleIntermediateEntitysDTO;
import com.cs.config.idto.IConfigDataRuleTagRuleDTO;
import com.cs.config.idto.IConfigDataRuleTagsDTO;
import com.cs.config.idto.IConfigEmbeddedRelationDTO;
import com.cs.config.idto.IConfigGoldenRecordRuleDTO;
import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.idto.IConfigMergeEffectDTO;
import com.cs.config.idto.IConfigMergeEffectTypeDTO;
import com.cs.config.idto.IConfigNatureRelationshipDTO;
import com.cs.config.idto.IConfigNormalizationDTO;
import com.cs.config.idto.IConfigPermissionDTO;
import com.cs.config.idto.IConfigPropertyPermissionDTO;
import com.cs.config.idto.IConfigRelationshipDTO;
import com.cs.config.idto.IConfigRelationshipPermissionDTO;
import com.cs.config.idto.IConfigRuleEntityDTO;
import com.cs.config.idto.IConfigSectionElementDTO;
import com.cs.config.idto.IConfigSideRelationshipDTO;
import com.cs.config.idto.IConfigTabDTO;
import com.cs.config.idto.IConfigTagValueDTO;
import com.cs.config.idto.IConfigTaskDTO;
import com.cs.config.idto.IConfigTranslationDTO.EntityType;
import com.cs.config.idto.IConfigUserDTO;
import com.cs.config.standard.IConfigClass;
import com.cs.config.standard.IConfigMap;
import com.cs.config.standard.IStandardConfig.TagType;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.role.Role;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.runtime.utils.DiFileUtils;
import com.cs.di.runtime.utils.DiTransformationUtils;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.CalculatedAttributeTypes;
import com.cs.di.workflow.constants.ConfigNodeType;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ruplesh.pawar
 *
 * This class convert config excel file into the PXON.
 */
@SuppressWarnings("unchecked")
@Component("configToPXONTask")
public class ConfigToPXONTask extends AbstractConfigToPXONTask {
  
  public static final List<String>       INPUT_LIST                                     = Arrays.asList(RECEIVED_DATA, NODE_TYPE, PERMISSIONS_TYPE, ROLE_IDS);
  public static final List<String>       OUTPUT_LIST                                    = Arrays.asList(PXON, FAILED_FILES,
      EXECUTION_STATUS);
  public static final List<WorkflowType> WORKFLOW_TYPES                                 = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  public static final List<EventType>    EVENT_TYPES                                    = Arrays.asList(EventType.INTEGRATION);
  public static final String             MAJOR_TAXONOMY                                 = "majorTaxonomy";
  public static final String             EXCEL                                          = "EXCEL";
  public static final Integer            DATA_ROW_NUMBER                                = 2;
  public static final Integer            HEADER_ROW_NUMBER                              = 1;
  public static final String             POSITION                                       = "position";
  public static final String             X_COORDINATE                                   = "x";
  public static final String             Y_COORDINATE                                   = "y";
  public static final String             COUPLING_TYPE                                  = "couplingType";
  public static final String             SIDE1_COUPLING_PROPERTIES                      = "side1CouplingProperties";
  public static final String             SIDE2_COUPLING_PROPERTIES                      = "side2CouplingProperties";
  public static final String             ID                                             = "id";
  public static final String             ATTRIBUTE_TYPE_CONCATENATED                    = "CONCATENATED";
  public static final String             ATTRIBUTE_TYPE_CALCULATED                      = "CALCULATED";
  public static final String             UNIT                                           = "unit";
  public static final String             PROMOTIONAL_COLLECTION                         = "promotionalCollection";
  public static final String             THRESHOLD_BUNDLE                               = "looseBundle";
  public static final String             UNIT_TAG                                       = "tag_type_unit";
  public static final String             EMPTY_STRING                                   = "";
  public static final String             TECHNICAL_IMAGE_CLASSES                        = "Technical_Image_Classes";
  private static final String            PERMISSION                                     = "PERMISSION";
  
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
    if (DiValidationUtil.isBlank((String) inputFields.get(RECEIVED_DATA))) {
      invalidParameters.add(RECEIVED_DATA);
    }
    
    // Validate required NODE_TYPE
    if (DiValidationUtil.isBlank((String) inputFields.get(NODE_TYPE))) {
      invalidParameters.add(NODE_TYPE);
    }
    // Validate Role_Ids and Permission_Type
    if (((String) inputFields.get(NODE_TYPE)).equals(PERMISSION)) {
      List<String> roleIds = new ArrayList<String>(), permissionType = new ArrayList<String>();
      try {
        roleIds = ObjectMapperUtil.readValue((String) inputFields.get(ROLE_IDS), List.class);
      }
      catch (Exception e) {
      }
      try {
        permissionType = ObjectMapperUtil.readValue((String) inputFields.get(PERMISSIONS_TYPE), List.class);
      }
      catch (Exception e) {
      }
      if (roleIds.isEmpty()) {
        invalidParameters.add(ROLE_IDS);
      }
      if (permissionType.isEmpty()) {
        invalidParameters.add(PERMISSIONS_TYPE);
      }
    }
    return invalidParameters;
    
    // Note: Validation not required for
    // 1. PXON as it is optional output parameter
    // 2. FAILED_FILES as it is optional output parameter
    // 3. EXECUTION_STATUS as it is optional output parameter
    
  }
  
  /**
   * convert excel file into PXON.
   */
  @Override
  public void generatePXON(WorkflowTaskModel workflowTaskModel)
  {
    IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = workflowTaskModel.getExecutionStatusTable();
    Map<String, String> receivedData = (Map<String, String>) DiValidationUtil.validateAndGetRequiredMap(workflowTaskModel, RECEIVED_DATA);
    List<String> roleIds = null;
    List<String> permissionTypes = null;
    ConfigNodeType nodeType = DiValidationUtil.validateAndGetRequiredEnum(workflowTaskModel, NODE_TYPE, ConfigNodeType.class);
    if (PERMISSION.equalsIgnoreCase(nodeType.name())) {
      roleIds = (List<String>) getCollectionFromString(workflowTaskModel, ROLE_IDS);
      permissionTypes = (List<String>) getCollectionFromString(workflowTaskModel, PERMISSIONS_TYPE);
    }
    DiValidationUtil.validateAndGetOptionalString(workflowTaskModel,"IS_CONFIG_TYPE_PERMISSION");
    if (workflowTaskModel.getExecutionStatusTable().isErrorOccurred()) {
      return;
    }
    List<String> entities = new ArrayList<>();
    List<String> failedfiles = new ArrayList<>();
    
    for (Entry<String, String> excelFile : receivedData.entrySet()) {
      try (InputStream fileInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(excelFile.getValue()));
          XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);) {
        switch (nodeType) {
          case ARTICLE:
            generatePXONForArticle(workbook, excelFile.getKey(), entities, executionStatusTable, workflowTaskModel);
            break;
          case ASSET:
            generatePXONForAsset(workbook, excelFile.getKey(), entities, executionStatusTable, workflowTaskModel);
            break;
          case TEXTASSET:
            generatePXONForTextAsset(workbook, excelFile.getKey(), entities, executionStatusTable, workflowTaskModel);
            break;
          // TODO: PXPFDEV-21451: Deprecate Virtual Catalog  
          /*case VIRTUALCATALOG:
            generatePXONForVirtualCatalog(workbook, excelFile.getKey(), entities, executionStatusTable, workflowTaskModel);
            break;*/
          case SUPPLIER:
            generatePXONForSupplier(workbook, excelFile.getKey(), entities, executionStatusTable, workflowTaskModel);
            break;
          case ATTRIBUTE:
            generatePXONForAttribute(workbook, excelFile.getKey(), entities, executionStatusTable, workflowTaskModel);
            break;
          case TAG:
            generatePXONForTags(workbook, excelFile.getKey(), entities, executionStatusTable, workflowTaskModel);
            break;
          case RELATIONSHIP:
            generatePXONForRelationship(workbook, excelFile.getKey(), entities, executionStatusTable);
            break;
          case CONTEXT:
            generatePXONForContext(workbook, excelFile.getKey(), entities, executionStatusTable);
            break;
          /* PXPFDEV-21215 : Deprecate - Taxonomy Hierarchies 
           * case HIERARCHY:
            generatePXONForHierarchy(workbook, excelFile.getKey(), entities, executionStatusTable);
            break;*/
          case MASTERTAXONOMY:
            generatePXONForMasterTaxonomy(workbook, excelFile.getKey(), entities, executionStatusTable);
            break;
          case PROPERTY_COLLECTION:
            generatePXONForPropertyCollection(workbook, excelFile.getKey(), entities, executionStatusTable);
            break;
          case USER:
            generatePXONForUsers(workbook, excelFile.getKey(), entities, executionStatusTable);
            break;
          case TASK:
            generatePXONForTasks(workbook, excelFile.getKey(), entities, executionStatusTable);
            break;
          case RULE:
            generatePXONForDataRules(workbook, excelFile.getKey(), entities, executionStatusTable);
            break;
          case GOLDENRECORDRULE:
            generatePXONForGoldenRecordRules(workbook, excelFile.getKey(), entities, executionStatusTable);
            break;
          case TAB:
            generatePXONForTabs(workbook, excelFile.getKey(), entities, executionStatusTable);
            break;
          case PARTNER:
            generatePXONForPartners(workbook, excelFile.getKey(), entities, executionStatusTable);
            break;
          case TRANSLATION:
            generatePXONForTranslations(workbook, excelFile.getKey(), entities, executionStatusTable);
            break;
          case LANGUAGE:
            generatePXONForLanguages(workbook, excelFile.getKey(), entities, executionStatusTable);
         
          case PERMISSION:
            generatePXONForPermissions(workbook, excelFile.getKey(), entities, executionStatusTable, permissionTypes, roleIds);
           
            
            break;
          default:
            break;
        }
        executionStatusTable.addSuccess(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN011,
            new String[] { excelFile.getKey() });
      }
      catch (Exception e) {
        failedfiles.add(excelFile.getKey());
        executionStatusTable.addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN009,
            new String[] { excelFile.getKey() });
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

  private Collection<?> getCollectionFromString(WorkflowTaskModel workflowTaskModel, String paramName)
  {
    Object param = workflowTaskModel.getInputParameters().get(paramName);
    if(param == null) {
      workflowTaskModel.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003, new String[] { paramName });
      return null;
    }else if(param instanceof Collection<?>) {
      return (Collection<?>) param;
    }
    try {
       return new ObjectMapper().readValue(param.toString(), List.class);
    }
    catch (JsonProcessingException e) {
      workflowTaskModel.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005,
          new String[] { paramName });
    }
    
    return null;
  }

  private void generatePXONForPermissions(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable, List<?> permissionTypes, List<?> roleIds)
  {
    List<Map<String, Object>> mainPermissionList = DiFileUtils.prepareMapfromSheet(workbook, MAIN_PERMISSIONS_SHEET_NAME, fileName,
        executionStatusTable, null);
    List<Map<String, Object>> generalInformationPermissions = DiFileUtils.prepareMapfromSheet(workbook, GENERALI_NFORMATION_PERMISSIONS_SHEET_NAME, 
         fileName, executionStatusTable, null);
    List<Map<String, Object>> propertyPermissions = DiFileUtils.prepareMapfromSheet(workbook, PROPERTY_PERMISSIONS_SHEET_NAME, fileName,
        executionStatusTable, null);
    List<Map<String, Object>> relationshipPermissions = DiFileUtils.prepareMapfromSheet(workbook, RELATIONSHIP_PERMISSIONS_SHEET_NAME,
        fileName, executionStatusTable, null);
    
    Map<String, List<Map<String, Object>>> klassCodeVsGenralInfoPermisions = prepareKlassCodeVsPermissionEntityMap(generalInformationPermissions);
    Map<String, List<Map<String, Object>>> klassCodeVsPropertyPermisions = prepareKlassCodeVsPermissionEntityMap(propertyPermissions);
    Map<String, List<Map<String, Object>>> klassCodeVsRelationshipPermisions = prepareKlassCodeVsPermissionEntityMap(relationshipPermissions);
    
    if (!CollectionUtils.isEmpty(mainPermissionList)) {
      for (Map<String, Object> entity : mainPermissionList) {
        try {
          String permissionType = (String) entity.get(PERMISSION_TYPE);
          if (permissionTypes.contains(permissionType)) {
           
            String klassCode = (String) entity.get(KLASS_CODE);
            //global permission transformation
            ConfigPermissionDTO mainPermissionDTO = new ConfigPermissionDTO();
            mainPermissionDTO.setPermissionType(permissionType);
            mainPermissionDTO.setEntityId(klassCode);
            ConfigGlobalPermissionDTO configGlobalPermissionDTO = new ConfigGlobalPermissionDTO();
            configGlobalPermissionDTO.setCanCreate(getBooleanValue(entity.get(PERMISSION_CREATE)));
            configGlobalPermissionDTO.setCanDelete(getBooleanValue(entity.get(PERMISSION_DELETE)));
            if (permissionType.equals(CommonConstants.ASSET_ENTITY)) {
              configGlobalPermissionDTO.setCanDownload(getBooleanValue(entity.get(PERMISSION_DOWNLOAD)));
            }
            else if (permissionType.equals(CommonConstants.TASK)) {
              configGlobalPermissionDTO.setCanRead(getBooleanValue(entity.get(PERMISSION_READ)));
              configGlobalPermissionDTO.setCanEdit(getBooleanValue(entity.get(PERMISSION_UPDATE)));
            }
            mainPermissionDTO.setGlobalPermissionDTO(configGlobalPermissionDTO);
            
            //Header permission transformation.
            List<Map<String, Object>> generalInfoPermissionsList = klassCodeVsGenralInfoPermisions.get(klassCode);
            ConfigHeaderPermisionDTO headerPermissionDTO = new ConfigHeaderPermisionDTO();
            headerPermissionDTO.setEntityId(klassCode);
            if (generalInfoPermissionsList != null) {
              for (Map<String, Object> generalInfoPermission : generalInfoPermissionsList) {
                
                switch ((String) generalInfoPermission.get(GENERAL_INFO_LABEL)) {
                  case GENERAL_INFO_IMAGE:
                    headerPermissionDTO.setViewIcon(getBooleanValue(generalInfoPermission.get(VISIBLE)));
                    headerPermissionDTO.setCanChangeIcon(getBooleanValue(generalInfoPermission.get(CAN_EDIT)));
                    break;
                  case GENERAL_INFO_NAME:
                    headerPermissionDTO.setViewName(getBooleanValue(generalInfoPermission.get(VISIBLE)));
                    headerPermissionDTO.setCanEditName(getBooleanValue(generalInfoPermission.get(CAN_EDIT)));
                    break;
                  case GENERAL_INFO_NATURE_TYPE:
                    headerPermissionDTO.setViewPrimaryType(getBooleanValue(generalInfoPermission.get(VISIBLE)));
                    break;
                  case GENERAL_INFO_ADDITIONAL_CLASSES:
                    headerPermissionDTO.setViewAdditionalClasses(getBooleanValue(generalInfoPermission.get(VISIBLE)));
                    headerPermissionDTO.setCanAddClasses(getBooleanValue(generalInfoPermission.get(CAN_ADD)));
                    headerPermissionDTO.setCanDeleteClasses(getBooleanValue(generalInfoPermission.get(CAN_REMOVE)));
                    break;
                  case GENERAL_INFO_TAXONOMIES:
                    headerPermissionDTO.setViewTaxonomies(getBooleanValue(generalInfoPermission.get(VISIBLE)));
                    headerPermissionDTO.setCanAddTaxonomy(getBooleanValue(generalInfoPermission.get(CAN_ADD)));
                    headerPermissionDTO.setCanDeleteTaxonomy(getBooleanValue(generalInfoPermission.get(CAN_REMOVE)));
                    break;
                  case GENERAL_INFO_LIFECYCLE_STATUS_TAGS:
                    headerPermissionDTO.setViewStatusTags(getBooleanValue(generalInfoPermission.get(VISIBLE)));
                    headerPermissionDTO.setCanEditStatusTag(getBooleanValue(generalInfoPermission.get(CAN_EDIT)));
                    break;
                  case GENERAL_INFO_CREATED_AND_LAST_MODIFIED_INFO:
                    headerPermissionDTO.setViewLastModifiedBy(getBooleanValue(generalInfoPermission.get(VISIBLE)));
                    headerPermissionDTO.setViewCreatedOn(getBooleanValue(generalInfoPermission.get(VISIBLE)));
                    break;
                }
                
                mainPermissionDTO.setHeaderPermissionDTO(headerPermissionDTO);
              }
            }
            
            // Property Permission transformation
            List<Map<String, Object>> propertyPermissionsList = klassCodeVsPropertyPermisions.get(klassCode);
            if (propertyPermissionsList != null) {
              List<IConfigPropertyPermissionDTO> propertyPermissionDTOs = new ArrayList<>();
              for (Map<String, Object> propertyPermission : propertyPermissionsList) {
                IConfigPropertyPermissionDTO propertyPermissionsDTO = new ConfigPropertyPermissionDTO();
                propertyPermissionsDTO.setEntityId(klassCode);
                propertyPermissionsDTO.setPropertyId((String) propertyPermission.get(ATTRIBUTE_TAG_CODE));
                propertyPermissionsDTO.setCanEdit(getBooleanValue(propertyPermission.get(CAN_EDIT)));
                propertyPermissionsDTO.setIsVisible(getBooleanValue(propertyPermission.get(VISIBLE)));
                propertyPermissionsDTO.setPropertyType((String) propertyPermission.get(PROPERTY_TYPE_COLOUMN));
                propertyPermissionDTOs.add(propertyPermissionsDTO);
              }
              
              mainPermissionDTO.getPropertyPermission().addAll(propertyPermissionDTOs);
            }
            
            // Relationship permission transformation
            List<Map<String, Object>> relationshipPermissionsList = klassCodeVsRelationshipPermisions.get(klassCode);
            if (relationshipPermissionsList  != null) {
              List<IConfigRelationshipPermissionDTO> relationshipPermissionDTOs = new ArrayList<>();
              for (Map<String, Object> relationshipPermission : relationshipPermissionsList) {
                IConfigRelationshipPermissionDTO relationshipPermissionDTO = new ConfigRelationshipPermissionDTO();
                relationshipPermissionDTO.setEntityId(klassCode);
                relationshipPermissionDTO.setRelationshipId((String) relationshipPermission.get(PERMISSION_RELATIONSHIPS_COLOUMN));
                relationshipPermissionDTO.setIsVisible(getBooleanValue(relationshipPermission.get(VISIBLE)));
                relationshipPermissionDTO.setCanAdd(getBooleanValue(relationshipPermission.get(CAN_ADD)));
                relationshipPermissionDTO.setCanDelete(getBooleanValue(relationshipPermission.get(CAN_REMOVE)));
                relationshipPermissionDTOs.add(relationshipPermissionDTO);
              }
              
              mainPermissionDTO.getRelationshipPermission().addAll(relationshipPermissionDTOs);
            }
            
            //add roleId in the permission DTOs
            addRoleIds(mainPermissionDTO, roleIds, entities);
          }
        }
        catch (Exception e) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
              new String[] { (String) entity.get(KLASS_CODE) });
        }
        
      }
    }
  }
  
  /**
   * Generate PXON for Languages
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   */
  protected void generatePXONForLanguages(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    List<Map<String, Object>> entityList = DiFileUtils.prepareMapfromSheet(workbook, LANGUAGE_SHEET_NAME, fileName, executionStatusTable,
        null);
    if (!CollectionUtils.isEmpty(entityList)) {
      for (Map<String, Object> entity : entityList) {
        try {
          String code = (String) entity.get(CODE);
          String label = (String) entity.get(LABEL_COLUMN);
          String abbrevation = (String) entity.get(ABBREVIATION);
          String localId = (String) entity.get(LOCALE_ID);
          checkforMandatoryColumns(code, label, abbrevation, localId);
          
          ConfigLanguageDTO languageDTO = new ConfigLanguageDTO();
          languageDTO.setCode(code);
          languageDTO.setLabel(label);
          languageDTO.setParentCode((String) entity.get(PARENTCODE));
          languageDTO.setAbbreviation(abbrevation);
          languageDTO.setLocaleId(localId);
          languageDTO.setNumberFormat((String) entity.get(NUMBER_FORMAT));
          languageDTO.setDateFormat((String) entity.get(DATE_FORMAT));
          languageDTO.setIcon(entity.get(ICON) == null ? "" : (String) entity.get(ICON));
          languageDTO.setIsDataLanguage(getBooleanValue(entity.get(IS_DATA_LANGUAGE)));
          languageDTO.setIsUserInterfaceLanguage(getBooleanValue(entity.get(IS_UI_LANGUAGE)));
          languageDTO.setIsDefaultLanguage(getBooleanValue(entity.get(IS_DEFULT_LANGUAGE)));
          entities.add(languageDTO.toPXON());
        }
        catch (Exception e) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
              new String[] { (String) entity.get(CODE) });
        }
      }
    }
  
  }

  /**
   * Generate PXON for Translations
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   */
  protected void generatePXONForTranslations(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    Map<String, ConfigTranslationDTO> configTranslationDTOMap = prepareTranslationDTOMap(workbook, fileName, executionStatusTable);
    for (ConfigTranslationDTO configTranslationDTO : configTranslationDTOMap.values()) {
      try {
        entities.add(configTranslationDTO.toPXON());
      }
      catch (Exception e) {
        executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
            new String[] { (String) configTranslationDTO.getCode() });
      }
    }
  }

  /**
   * Generate PXON for Partners
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   */
  protected void generatePXONForPartners(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    Map<String, ConfigOrganizationDTO> configDTOMap = preparePartnerDTOMapfromExcelSheet(workbook, PARTNER_SHEET_NAME, fileName,
        executionStatusTable);
    for (ConfigOrganizationDTO configOrganizationDTO : configDTOMap.values()) {
      try {
        entities.add(configOrganizationDTO.toPXON());
      }
      catch (Exception e) {
        executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
            new String[] { (String) configOrganizationDTO.getCode() });
      }
    }
  }

  /**
   * Generate PXON for Tabs
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   */
    protected void generatePXONForTabs(XSSFWorkbook workbook, String fileName, List<String> entities,
        IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    List<Map<String, Object>> entityList = DiFileUtils.prepareMapfromSheet(workbook, TAB_SHEET_NAME, fileName, executionStatusTable, null);
    if (!CollectionUtils.isEmpty(entityList)) {
      for (Map<String, Object> entity : entityList) {
        try {
          String code = (String) entity.get(CODE_COLUMN_CONFIG);
          String label = (String) entity.get(LABEL_COLUMN_CONFIG);
          checkforMandatoryColumns(code, label);
          
          IConfigTabDTO tabDTO = new ConfigTabDTO();
          tabDTO.setCode(code);
          tabDTO.setLabel(label);
          tabDTO.setIcon(entity.get(ICON_KEY) == null ? "" : (String) entity.get(ICON_KEY));
          String sequence = (String) entity.get(SEQUENCE);
          if (sequence != null) {
            tabDTO.setSequence(Integer.parseInt(sequence));
          }
          tabDTO.setPropertySequenceList(getListFromString((String) entity.get(PROPERTY_COLLECTION_SHEET_NAME)));
          entities.add(tabDTO.toPXON());
        }
        catch (Exception e) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
              new String[] { (String) entity.get(CODE_COLUMN_CONFIG) });
        }
      }
    }
    
  }
  
  /**
   * Generate PXON for Article
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   * @param workflowTaskModel
   */
  protected void generatePXONForArticle(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable, WorkflowTaskModel workflowTaskModel)
  {
    List<Map<String, Object>> entityList = DiFileUtils.prepareMapfromSheet(workbook, KLASS_SHEET_NAME, fileName, executionStatusTable,
        null);
    if (!CollectionUtils.isEmpty(entityList)) {
      for (Map<String, Object> entity : entityList) {
        try {
          if (!DiValidationUtil.isBlank((String) entity.get(PARENT_CODE_COLUMN))) {
            ConfigClassifierDTO configClassDTO = new ConfigClassifierDTO();
            String code = (String) entity.get(CODE_COLUMN_CONFIG);
            String natureType = (String)entity.get(NATURE_TYPE);
            configClassDTO.setCode(code);
            configClassDTO.setClassifierDTO(code, IClassifierDTO.ClassifierType.CLASS);
            configClassDTO.setSubType(IConfigClass.ClassifierClass.PROJECT_KLASS_TYPE.name());
            if (natureType != null && (natureType.equals(UNIT) || natureType.equals(PROMOTIONAL_COLLECTION) || natureType.equals(THRESHOLD_BUNDLE))) {
              continue;
            }
            generatePXONforKlass(workbook, fileName, executionStatusTable, entity, configClassDTO, code);
            entities.add(configClassDTO.toPXON());
          }
        }
        catch (Exception e) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
              new String[] { (String) entity.get(CODE) });
        }
      }
    }
  }
  
  /**
   * Generate PXON for Asset
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   * @param workflowTaskModel
   */
  
  protected void generatePXONForAsset(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable, WorkflowTaskModel workflowTaskModel)
  {
    List<Map<String, Object>> entityList = DiFileUtils.prepareMapfromSheet(workbook, KLASS_SHEET_NAME, fileName, executionStatusTable,
        null);
    if (!CollectionUtils.isEmpty(entityList)) {
      for (Map<String, Object> entity : entityList) {
        try {
          if (!DiValidationUtil.isBlank((String) entity.get(PARENT_CODE_COLUMN))) {
            ConfigClassifierDTO configClassDTO = new ConfigClassifierDTO();
            String code = (String) entity.get(CODE_COLUMN_CONFIG);
            configClassDTO.setCode(code);
            configClassDTO.setClassifierDTO(code, IClassifierDTO.ClassifierType.CLASS);
            configClassDTO.setSubType(IConfigClass.ClassifierClass.ASSET_KLASS_TYPE.name());
            configClassDTO.isUploadZip(getBooleanValue((String) entity.get(ITransformationTask.EXTRACT_ZIP)));
            configClassDTO.isDetectDuplicate(getBooleanValue((String) entity.get(ITransformationTask.DETECT_DUPLICATE)));
            configClassDTO.isTrackDownloads(getBooleanValue((String) entity.get(ITransformationTask.TRACKDOWNLOAD)));
            generatePXONforKlass(workbook, fileName, executionStatusTable, entity, configClassDTO, code);
            prepareTechnicalImageVariants(entity, configClassDTO);
            entities.add(configClassDTO.toPXON());
          }
        }
        catch (Exception e) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
              new String[] { (String) entity.get(CODE) });
        }
      }
    }
  }

  private void prepareTechnicalImageVariants(Map<String, Object> entity, ConfigClassifierDTO configClassDTO)
  {
    String tecnicalImageVaraintCodes = (String) entity.get(TECHNICAL_IMAGE_CLASSES);
    if(StringUtils.isNotEmpty(tecnicalImageVaraintCodes)) {
      List<String> tecnicalImagesVaraint = getListFromString(tecnicalImageVaraintCodes);
      Map<String, IConfigEmbeddedRelationDTO> variantElementDTOs = new HashMap<>();
      tecnicalImagesVaraint.forEach(tecnicalImageVaraint -> {
        IConfigEmbeddedRelationDTO variantElementDTO = new ConfigEmbeddedRelationDTO();
        variantElementDTO.setCode(tecnicalImageVaraint);
        variantElementDTO.setLabel(tecnicalImageVaraint);
        variantElementDTOs.put(tecnicalImageVaraint, variantElementDTO);
      });
      configClassDTO.getEmbeddedClasses().putAll(variantElementDTOs);
    }
  }
  
  /**
   * Generate PXON for TextAsset
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   * @param workflowTaskModel
   */
  
  protected void generatePXONForTextAsset(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable,WorkflowTaskModel workflowTaskModel)
  {
    List<Map<String, Object>> entityList = DiFileUtils.prepareMapfromSheet(workbook, KLASS_SHEET_NAME, fileName, executionStatusTable,
        null);
    if (!CollectionUtils.isEmpty(entityList)) {
      for (Map<String, Object> entity : entityList) {
        try {
          if (!DiValidationUtil.isBlank((String) entity.get(PARENT_CODE_COLUMN))) {
            ConfigClassifierDTO configClassDTO = new ConfigClassifierDTO();
            String code = (String) entity.get(CODE_COLUMN_CONFIG);
            configClassDTO.setCode(code);
            configClassDTO.setClassifierDTO(code, IClassifierDTO.ClassifierType.CLASS);
            configClassDTO.setSubType(IConfigClass.ClassifierClass.TEXT_ASSET_KLASS_TYPE.name());
            generatePXONforKlass(workbook, fileName, executionStatusTable, entity, configClassDTO, code);
            entities.add(configClassDTO.toPXON());
          }
        }
        catch (Exception e) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
              new String[] { (String) entity.get(CODE) });
        }
      }
    }    
  }
  
  /**
   * Generate PXON for VirtualCatalog
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   * @param workflowTaskModel
   */
  //TODO: PXPFDEV-21451: Deprecate Virtual Catalog
  /*protected void generatePXONForVirtualCatalog(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable,WorkflowTaskModel workflowTaskModel)
  {
    List<Map<String, Object>> entityList = DiFileUtils.prepareMapfromSheet(workbook, KLASS_SHEET_NAME, fileName, executionStatusTable,
        null);
    if (!CollectionUtils.isEmpty(entityList)) {
      for (Map<String, Object> entity : entityList) {
        try {
          if (!DiValidationUtil.isBlank((String) entity.get(PARENT_CODE_COLUMN))) {
            ConfigClassifierDTO configClassDTO = new ConfigClassifierDTO();
            String code = (String) entity.get(CODE_COLUMN_CONFIG);
            configClassDTO.setCode(code);
            configClassDTO.setClassifierDTO(code, IClassifierDTO.ClassifierType.CLASS);
            configClassDTO.setSubType(IConfigClass.ClassifierClass.VIRTUAL_CATALOG_KLASS_TYPE.name());
            generatePXONforKlass(workbook, fileName, executionStatusTable, entity, configClassDTO, code);
            entities.add(configClassDTO.toPXON());
          }
        }
        catch (Exception e) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
              new String[] { (String) entity.get(CODE) });
        }
      }
    }    
  }*/

  /**
   * Generate PXON for Supplier
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   * @param workflowTaskModel
   */
  
  protected void generatePXONForSupplier(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable, WorkflowTaskModel workflowTaskModel)
  {
    List<Map<String, Object>> entityList = DiFileUtils.prepareMapfromSheet(workbook, KLASS_SHEET_NAME, fileName, executionStatusTable,
        null);
    if (!CollectionUtils.isEmpty(entityList)) {
      for (Map<String, Object> entity : entityList) {
        try {
          if (!DiValidationUtil.isBlank((String) entity.get(PARENT_CODE_COLUMN))) {
            ConfigClassifierDTO configClassDTO = new ConfigClassifierDTO();
            String code = (String) entity.get(CODE_COLUMN_CONFIG);
            configClassDTO.setCode(code);
            configClassDTO.setClassifierDTO(code, IClassifierDTO.ClassifierType.CLASS);
            configClassDTO.setSubType(IConfigClass.ClassifierClass.SUPPLIER_KLASS_TYPE.name());
            generatePXONforKlass(workbook, fileName, executionStatusTable, entity, configClassDTO, code);
            entities.add(configClassDTO.toPXON());
          }
        }
        catch (Exception e) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
              new String[] { (String) entity.get(CODE) });
        }
      }
    }
  }
  
  private void generatePXONforKlass(XSSFWorkbook workbook, String fileName,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable, Map<String, Object> entity,
      ConfigClassifierDTO configClassDTO, String code) throws NumberFormatException, RDBMSException
  {
    String propertycollectionsheet = (String) entity.get(SECTION_SHEET_NAME);
    String variantsheetname = (String) entity.get(VARIANT_SHEET_NAME_FOR_KLASS);
    String relationshipsheetname = (String) entity.get(RELATIONSHIP_SHEET_NAME_FOR_KLASS);
    configClassDTO.setLabel((String) entity.get(LABEL_COLUMN_CONFIG));
    configClassDTO.setParentCode((String) entity.get(PARENT_CODE_COLUMN));
    boolean isNature = getBooleanValue(entity.get(IS_NATURE));
    configClassDTO.setIsNature(isNature);
    if (isNature == true) {
      configClassDTO.setNatureType((String) entity.get(NATURE_TYPE));
      configClassDTO.setIsDefault(getBooleanValue(entity.get(IS_DEFAULT)));
    }
    else {
      configClassDTO.setIsDefault(false);
    }
    //added fix for Business Partners(Supplier class) with valid Max Revision is not getting updated
    if(entity.get(MAX_VERSION)!=null)
	configClassDTO.setNumberOfVersionsToMaintain((Integer.parseInt((String) entity.get(MAX_VERSION))));
    
    configClassDTO.setStatusTag(getListFromString((String) entity.get(LIFE_CYCLE_STATUS_TAG)));
    configClassDTO.setIsAbstract(getBooleanValue(entity.get(IS_ABSTRACT)));
    configClassDTO.setIcon(entity.get(ICON_COLUMN) == null ? "" : (String) entity.get(ICON_COLUMN));
    configClassDTO.setPreviewImage((String) entity.get(PREVIEW_IMAGE));
    configClassDTO.setTasks(getListFromString((String) entity.get(TASK_CODES)));
    if (!DiValidationUtil.isBlank(propertycollectionsheet)) {
      preparePropertyCollection(workbook, fileName, configClassDTO.getSections(), propertycollectionsheet, executionStatusTable,
          KLASS_SHEET_NAME);
    }
    if (!DiValidationUtil.isBlank(variantsheetname)) {
      configClassDTO.getEmbeddedClasses()
          .putAll(prepareVariantForKlass(workbook, fileName, configClassDTO.getEmbeddedClasses(), variantsheetname, executionStatusTable));
    }
    if (!DiValidationUtil.isBlank(relationshipsheetname)) {
      prepareRelationshipforKlass(workbook, fileName, code, configClassDTO.getRelationships(), relationshipsheetname, executionStatusTable);
      prepareContextforKlass(workbook, fileName, configClassDTO, relationshipsheetname, executionStatusTable);
    }
  }
  
  /**
   * Prepare Context for Klass
   * 
   * @param workbook
   * @param fileName
   * @param executionStatusTable
   * @param workflowTaskModel
   */
  private void prepareContextforKlass(XSSFWorkbook workbook, String fileName, IConfigClassifierDTO configclassifier,
      String relationshipsheetname, IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    List<String> productvariantrelationship = (List<String>) configclassifier.getProductVariantContextCode();
    List<String> promtionalversioncontext = (List<String>) configclassifier.getPromotionalVersionContextCode();
    List<String> relationshipsheetnames = getListFromString(relationshipsheetname);
    for (String relationshipsheet : relationshipsheetnames) {
      List<Map<String, Object>> relationshipList = DiFileUtils.prepareMapfromSheet(workbook, relationshipsheet, fileName,
          executionStatusTable, null);
      for (Map<String, Object> relationships : relationshipList) {    
        String contextCode = (String) relationships.get(CONTEXT_CODE);
        String type = (String) relationships.get(RELATIONSHIP_TYPE);

        if (type.equals(PRODUCTVARIANTRELATIONSIP)) {
          productvariantrelationship.add(contextCode);
        }
        if (type.equals(PROMOTIONALVERSIONCONTEXTCODE)) {
          promtionalversioncontext.add(contextCode);
        }
      }
    }
    configclassifier.setProductVariantContextCode(productvariantrelationship);
    configclassifier.setPromotionalVersionContextCode(promtionalversioncontext);    
  }
  
  protected void generatePXONForRelationship(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    Map<String,Map<String, Object>> entityList = prepareAggregateMapfromSheet(workbook, RELATIONSHIP_SHEET_NAME_FOR_CONFIG, fileName,
        executionStatusTable);
    if (!CollectionUtils.isEmpty(entityList)) {
      for (Map<String, Object> entity : entityList.values()) {
        try {
          // to do change to Interface.
          IConfigRelationshipDTO relationshipDTO = new ConfigRelationshipDTO();
          relationshipDTO.setIsNature(Boolean.FALSE);
          relationshipDTO.setIsStandard(Boolean.FALSE);
          String relationshipCode = (String) entity.get(CODE);
          String side1ClassCode = (String) entity.get(S1_CLASS_CODE);
          String side2ClassCode = (String) entity.get(S2_CLASS_CODE);
          String side1Label = (String) entity.get(S1_LABEL);
          String side2Label = (String) entity.get(S2_LABEL);
          String icon = (String) entity.get(ICON);
          
          
          checkforMandatoryColumns(relationshipCode, side1ClassCode, side2ClassCode, side1Label, side2Label);
          
          relationshipDTO.setPropertyDTO(relationshipCode, IPropertyDTO.PropertyType.RELATIONSHIP);
          relationshipDTO.setLabel((String) entity.get(LABEL_COLUMN));
          relationshipDTO.setTab((String) entity.get(TAB_CODE_COLUMN));
          relationshipDTO.setIcon(icon != null ? icon : "");
          relationshipDTO.setIsLite(getBooleanValue(entity.get(IS_LITE)));
          relationshipDTO.isEnableAfterSave(getBooleanValue(entity.get(ALLOW_AFTERSAVE_EVENT)));

          // Preparing side1 data
          IConfigSideRelationshipDTO side1 = relationshipDTO.getSide1();
          side1.setClassCode(side1ClassCode);
          side1.setLabel(side1Label);
          side1.setCardinality((String) entity.get(S1_CARDINALITY));
          side1.setIsVisible(getBooleanValue(entity.get(S1_EDITABLE)));
          side1.setContextCode((String) entity.get(S1_CONTEXT_CODE));
          if (entity.get(SIDE1_COUPLING_PROPERTIES) != null) {
            side1.setCouplings((List<IJSONContent>) entity.get(SIDE1_COUPLING_PROPERTIES));
          }
                    
          // Preparing side2 data
          IConfigSideRelationshipDTO side2 = relationshipDTO.getSide2();
          side2.setClassCode(side2ClassCode);
          side2.setLabel(side2Label);
          side2.setCardinality((String) entity.get(S2_CARDINALITY));
          side2.setIsVisible(getBooleanValue(entity.get(S2_EDITABLE)));
          side2.setContextCode((String) entity.get(S2_CONTEXT_CODE));
          if (entity.get(SIDE2_COUPLING_PROPERTIES) != null) {
            side2.setCouplings((List<IJSONContent>) entity.get(SIDE2_COUPLING_PROPERTIES));
          }
          entities.add(relationshipDTO.toPXON());
        }
        catch (Exception e) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
              new String[] { (String) entity.get(CODE) });
        }
      }
    }
  }
  
  /**
   * Generate PXON for attribute
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   * @param workflowTaskModel
   */
  protected void generatePXONForAttribute(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable, WorkflowTaskModel workflowTaskModel)
  {
    List<Map<String, Object>> entityList = DiFileUtils.prepareMapfromSheet(workbook, ATTRIBUTE_SHEET_NAME, fileName, executionStatusTable,
        null);
    if (!CollectionUtils.isEmpty(entityList)) {
      for (Map<String, Object> entity : entityList) {
        try {
          ConfigAttributeDTO attributeDTO = new ConfigAttributeDTO();
          attributeDTO.setAllowedStyles(getListFromString((String) entity.get(ALLOWED_STYLES)));
          attributeDTO.setAvailability(getListFromString((String) entity.get(AVAILABILITY)));
          String code = (String) entity.get(CODE);
          attributeDTO.setCode(code);
          attributeDTO.setIsTranslatable(getBooleanValue(entity.get(TRANSLATABLE)));
          attributeDTO.setDefaultUnit(entity.get(DEFAULT_UNIT) == null ? "" : (String) entity.get(DEFAULT_UNIT));
          String propertType = (String) entity.get(TYPE);
          attributeDTO.setDescription(entity.get(DESCRIPTION) == null ? "" : (String) entity.get(DESCRIPTION));
          attributeDTO.setIsDisabled(getBooleanValue(entity.get(READONLY)));
          attributeDTO.setIsFilterable(getBooleanValue(entity.get(FILTERABLE)));
          // Handling of Grid Editable for Calculated and Concatenated
          // attributes as it is not supported by these attributes
          Boolean isGridEditable = (propertType.equals(CALCULATED) || propertType.equals(CONCATENATED)) ? false
              : getBooleanValue(entity.get(GRID_EDITABLE));
          attributeDTO.setIsGridEditable(isGridEditable);
          attributeDTO.setIsMandatory(getBooleanValue(entity.get(MANDATORY)));
          attributeDTO.setIsSearchable(getBooleanValue(entity.get(SEARCHABLE)));
          attributeDTO.setIsSortable(getBooleanValue(entity.get(SORTABLE)));
          attributeDTO.setIsStandard(getBooleanValue(entity.get(STANDARD)));
          attributeDTO.SetIsVersionable(getBooleanValue(entity.get(REVISIONABLE)));
          attributeDTO.setHideSeparator(getBooleanValue(entity.get(HIDE_SEPARATOR)));
          attributeDTO.setLabel((String) entity.get(NAME));
          attributeDTO.setPlaceHolder(entity.get(PLACEHOLDER) == null ? "" : (String) entity.get(PLACEHOLDER));
          attributeDTO.setIcon(entity.get(ICON) == null ? "" : (String) entity.get(ICON));   
          String defaultValue = (String) entity.get(DEFAULT_VALUE);
          String precision = (String) entity.get(PRECISION);
          if (precision != null && Integer.parseInt(precision) <= 9) {
            attributeDTO.setPrecision(Integer.parseInt((precision)));
          }
          if (PropertyType.DATE.name().equalsIgnoreCase(propertType)) {
            defaultValue = String.valueOf(DiUtils.getLongValueOfDateString(defaultValue));
            attributeDTO.setDefaultValue(defaultValue);
          }
          else if (PropertyType.NUMBER.name().equalsIgnoreCase(propertType)) {
            try {
              if(StringUtils.isNotBlank(defaultValue)) {
                defaultValue = String.valueOf(Long.parseLong(defaultValue));
              }
              attributeDTO.setDefaultValue(defaultValue);
            }
            catch (NumberFormatException e) {
              RDBMSLogger.instance();
            }
          }
          else if (PropertyType.PRICE.name().equalsIgnoreCase(propertType) && precision != null) {
            try {
              if(StringUtils.isNotBlank(defaultValue)) {
                defaultValue = String.valueOf(Double.parseDouble((String) entity.get(DEFAULT_VALUE)));
              }
              attributeDTO.setDefaultValue(defaultValue);
            }
            catch (NumberFormatException e) {
              RDBMSLogger.instance();
            }
          }
          else {
            attributeDTO.setDefaultValue(defaultValue);
          }
          attributeDTO.setDefaultValueAsHTML((TRANSFORMATION_TYPE_VALUE_HTML_PARAGRAPH_START + (String) entity.get(DEFAULT_VALUE)
              + TRANSFORMATION_TYPE_VALUE_HTML_PARAGRAPH_END));
          IPropertyDTO.PropertyType propertyType = IPropertyDTO.PropertyType.valueOf((propertType).toUpperCase());
          attributeDTO.setPropertyDTO(code, propertyType);
          attributeDTO.setSubType(((String) entity.get(SUB_TYPE)).toUpperCase());
          attributeDTO.setToolTip(entity.get(TOOLTIP) == null ? "" : (String) entity.get(TOOLTIP));
          attributeDTO.setIsCodeVisible(getBooleanValue(entity.get(SHOWCODETAG)));
          attributeDTO.setAttributeConcatenatedList(setFormulaForConcatenated(entity));
          attributeDTO.setCalculatedAttributeUnit((String) entity.get(CALCULATED_ATTRIBUTE_UNIT));
          attributeDTO.setCalculatedAttributeType(IConfigClass.PropertyClass
              .valueOf(CalculatedAttributeTypes.valueOfClassPath((String) entity.get(CALCULATED_ATTRIBUTE_TYPE)).name()).toString());
          attributeDTO.setAttributeOperatorList(setFormulaForCalculated(entity));
          entities.add(attributeDTO.toPXON());
        }
        catch (Exception e) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
              new String[] { (String) entity.get(CODE) });
        }
      }
    }
    
  }
  
  /**
   * Generate PXON for tag
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   * @param workflowTaskModel
   */
  protected void generatePXONForTags(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable, WorkflowTaskModel workflowTaskModel)
  {
    List<Map<String, Object>> entityList = DiFileUtils.prepareMapfromSheet(workbook, TAG_SHEET_NAME, fileName, executionStatusTable, null);
    if (!CollectionUtils.isEmpty(entityList)) {
      for (Map<String, Object> entity : entityList) {
        try {
          ConfigTagDTO tagDTO = new ConfigTagDTO();
          List<IConfigTagValueDTO> configchildTagValueDTO = new ArrayList<>();
          String code = (String) entity.get(CODE);
          String parentcode = (String) entity.get(PARENT_CODE);
          String type = (String) entity.get(TYPE);
          if (type != null && type.equals(UNIT_TAG)) {
            continue;
          }
          // String image_extension = (String) entity.get(IMAGE_EXTENSION);
          // String image_resolution = (String) entity.get(IMAGE_RESOLUTION);
          if (parentcode.equals("-1") && type != null) {
            configchildTagValueDTO = preparechildTag(code, entityList);
            for (IConfigTagValueDTO childDTO : configchildTagValueDTO) {
              tagDTO.getChildren().add(childDTO);
            }
            tagDTO.setCode(code);
            tagDTO.setLabel((String) entity.get(NAME));
            tagDTO.setDescription(entity.get(DESCRIPTION) == null ? "" : (String) entity.get(DESCRIPTION));
            tagDTO.setColor(entity.get(COLOR)== null ? "" : (String) entity.get(COLOR));
            tagDTO.setDefaultValue(entity.get(DEFAULT_VALUE) == null ? "" : (String) entity.get(DEFAULT_VALUE));
            tagDTO.setIsMultiSelect(getBooleanValue(entity.get(MULTISELECT)));
            tagDTO.setToolTip(entity.get(TOOLTIP) == null ? "" : (String) entity.get(TOOLTIP));
            tagDTO.setIsFilterable(getBooleanValue(entity.get(FILTERABLE)));
            tagDTO.setLinkedMasterTag((String) entity.get(LINKED_MASTER_TAG_CODE));
            tagDTO.setIcon(entity.get(ICON) == null ? "" : (String) entity.get(ICON));
            tagDTO.setAvailability(getListFromString((String) entity.get(AVAILABILITY)));
            tagDTO.setIsGridEditable(getBooleanValue(entity.get(GRID_EDITABLE)));
            tagDTO.setIsVersionable(getBooleanValue(entity.get(REVISIONABLE)));
            tagDTO.setIsStandard(getBooleanValue(entity.get(STANDARD)));
            tagDTO.setIsdisabled(getBooleanValue(entity.get(ITransformationTask.READ_ONLY_COLUMN)));
            tagDTO.setPropertyDTO(code, type.toLowerCase().equals(TagType.tag_type_boolean.toString()) ? IPropertyDTO.PropertyType.BOOLEAN : IPropertyDTO.PropertyType.TAG);
            tagDTO.setTagType(type.toLowerCase());
            entities.add(tagDTO.toPXON());
          }
        }
        catch (Exception e) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
              new String[] { (String) entity.get(CODE) });
        }
      }
    }
  }
  
  /**
   * Generate DTO for childtag
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   * @param workflowTaskModel
   */
  protected List<IConfigTagValueDTO> preparechildTag(String code, List<Map<String, Object>> entitylist)
  {
    List<IConfigTagValueDTO> childrenDTOs = new ArrayList<>();
    for (Map<String, Object> entity : entitylist) {
      if (code.equals(entity.get(PARENT_CODE))) {
        IConfigTagValueDTO configTagValueDTO = new ConfigTagValueDTO();
        configTagValueDTO.setColor((String) entity.get(COLOR));
        configTagValueDTO.setLabel((String) entity.get(NAME));
        configTagValueDTO.setLinkedMasterTag((String) entity.get(LINKED_MASTER_TAG_CODE));
        configTagValueDTO.setTagValueDTO((String) entity.get(CODE));
        configTagValueDTO.setIcon((String) entity.get(ICON));
        childrenDTOs.add(configTagValueDTO);
      }
    }
    return childrenDTOs;
  }
  
  /**
   * Generate PXON for property collection
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   * @param workflowTaskModel
   */
  protected void generatePXONForPropertyCollection(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    String code, label;
    Map<String, Map<String, Object>> entityList = prepareAggregateMapfromSheet(workbook, PROPERTY_COLLECTION_SHEET_NAME, fileName,
        executionStatusTable);
    if (!CollectionUtils.isEmpty(entityList)) {
      for (Map<String, Object> entity : entityList.values()) {
        try {
          code = (String) entity.get(CODE_COLUMN_CONFIG);
          label = (String) entity.get(LABEL_COLUMN_CONFIG);
          // Check mandatory columns.
          checkforMandatoryColumns(code, label);
          ConfigPropertyCollectionDTO propertyCollectionDTO = new ConfigPropertyCollectionDTO();
          propertyCollectionDTO.setCode(code);
          propertyCollectionDTO.setLabel(label);
          propertyCollectionDTO.setIcon(entity.get(ICON_KEY) == null ? "" : (String) entity.get(ICON_KEY));
          if (entity.get(TAB_COLUMN) != null) {
              String tab = (String) entity.get(TAB_COLUMN);
              propertyCollectionDTO.setTab(tab);
          }
          propertyCollectionDTO.setIsForXRay(getBooleanValue((String) entity.get(IS_FOR_XRAY_COLUMN)));
          propertyCollectionDTO.setIsDefaultForXRay(getBooleanValue((String) entity.get(IS_DEFAULT_FOR_XRAY_COLUMN)));
          
          // handling elements
          Collection<IJSONContent> jsonElements = new ArrayList<>();
          if (entity.get(ENTITY_CODE_COLUMN) != null) {
            if (entity.get(ENTITY_CODE_COLUMN) instanceof String) {
              String entityCode = (String) entity.get(ENTITY_CODE_COLUMN);
              String entityType = (String) entity.get(ENTITY_TYPE_COLUMN);
              jsonElements.add(prepareJSONElement(entityCode, entityType, 1));
              propertyCollectionDTO.setElements(jsonElements);
            }
            else {
              // if not a string then fetch the list.
              List<String> entityCodeList = new ArrayList<>();
              List<String> entityTypeList = new ArrayList<>();
              List<Integer> indexList = new ArrayList<>();
              entityCodeList.addAll((Collection<? extends String>) entity.get(ENTITY_CODE_COLUMN));
              entityTypeList.addAll((Collection<? extends String>) entity.get(ENTITY_TYPE_COLUMN));
              indexList.addAll((Collection<? extends Integer>) entity.get(INDEX_COLUMN));
              int runningIndex = 0;
              for (int index = 0; index < indexList.size(); index++) {
               
                  
                  if (runningIndex < entityCodeList.size()) {
                    String entityType = entityTypeList.get(runningIndex);
                    if (entityType.equals(TAXONOMY)) {
                      continue;
                    }
                    IJSONContent element = prepareJSONElement(entityCodeList.get(runningIndex), entityType,indexList.get(index));
                    jsonElements.add(element);
                    
                    if (entityType.equals(TAXONOMY)) {
                      runningIndex++;
                      break;
                    }
                  }
                  runningIndex++;
               
              }
              propertyCollectionDTO.setElements(jsonElements);
            }
          }
          else {
            // in case of empty elements
            propertyCollectionDTO.setElements(new ArrayList<IJSONContent>());
          }
          entities.add(propertyCollectionDTO.toPXON());
        }
        catch (Exception e) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
              new String[] { (String) entity.get(CODE_COLUMN_CONFIG) });
        }
      }
    }
  }
  
  /**
   * Prepare JSON element for property collection
   * 
   * @param entityCode
   * @param entityType
   * @param row
   * @param col
   * @return
   */
  private IJSONContent prepareJSONElement(String entityCode, String entityType, Integer index)
  {
    IJSONContent element = new JSONContent();
    element.setField(CODE_PROPERTY, entityCode);
    element.setField(ID_PROPERTY, entityCode);
    element.setField(TYPE_PROPERTY, entityType);
    element.setField(INDEX_PROPERTY, index);
    return element;
    
  }
  
  /**
   * Prepare JSON element for Klass import
   * 
   * @param entityCode
   * @param entityType
   * @param row
   * @param col
   * @return
   */
  private IJSONContent prepareJSONElementforKlass(String code, String coupling, String id, String type)
  {
    IJSONContent element = new JSONContent();
    element.setField(PROPERTY_CODE, code);
    element.setField(COUPLING, coupling);
    element.setField(PROPERTY_CODE, id);
    element.setField(PROPERTY_TYPE, type);
    return element;
    
  }

  /**
   * Generate PXON for Master Taxonomy
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   */
  protected void generatePXONForMasterTaxonomy(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    Map<String, String> codeVsTaxonomyTypeMap = new HashMap<>();
    List<Map<String, Object>> entityList = DiFileUtils.prepareMapfromSheet(workbook, MASTER_TAXONOMY_SHEET_NAME, fileName,
        executionStatusTable, codeVsTaxonomyTypeMap);
    for (Map<String, Object> masterTaxo : entityList) {
      try {
        String code = (String) masterTaxo.get(CODE_COLUMN_CONFIG);
        String parentCode = (String) masterTaxo.get(PARENT_CODE_COLUMN);
        String type = codeVsTaxonomyTypeMap.get(code);
        String propertyCollectionSheetName = (String) masterTaxo.get(PROPERTY_COLLECTION_COLUMN);
        
        // Check mandatory columns.
        checkforMandatoryColumns(code, parentCode, type);
        
        IConfigClassifierDTO masterTaxonomyDTO = new ConfigClassifierDTO();
        masterTaxonomyDTO.setClassifierDTO(code, IClassifierDTO.ClassifierType.TAXONOMY);
        masterTaxonomyDTO.setConfigTaxonomyType(type);
        masterTaxonomyDTO.setSubType(IConfigClass.ClassifierClass.TAXONOMY_KLASS_TYPE.name());
        masterTaxonomyDTO.getLevelCodes().addAll(getListFromString((String) masterTaxo.get(LEVEL_CODES_COLUMN)));
        masterTaxonomyDTO.getLevelLables().addAll(getListFromString((String) masterTaxo.get(LEVEL_LABEL_COLUMN)));
        masterTaxonomyDTO.setParentCode(parentCode);
        masterTaxonomyDTO.setIcon((String)masterTaxo.get(ICON_COLUMN));
        masterTaxonomyDTO.setEvents(getListFromString((String) masterTaxo.get(EVENTS_COLUMN)));
        masterTaxonomyDTO.setTasks(getListFromString((String) masterTaxo.get(TASK_COLUMN)));
        masterTaxonomyDTO.setIsNewlyCreated(getIsNewlyCreatedList((String) masterTaxo.get(IS_NEWLY_CREATED_LEVEL_COLUMN)));
        String levelIndex = (String) masterTaxo.get(LEVEL_INDEX_COLUMN);
        if(levelIndex != null && !levelIndex.isBlank()) {
          masterTaxonomyDTO.setLevelIndex(Integer.parseInt(levelIndex));
        }
        if (masterTaxo.get(MASTER_TAG_PARENT_CODE_COLUMN) != null) {
          masterTaxonomyDTO.setMasterParentTag((String) masterTaxo.get(MASTER_TAG_PARENT_CODE_COLUMN));
        }
        if (masterTaxo.get(LABEL_COLUMN_CONFIG) != null) {
          masterTaxonomyDTO.setLabel((String) masterTaxo.get(LABEL_COLUMN_CONFIG));
        }
        if (masterTaxo.get(EMBEDDED_CLASS_COLUMN) != null) {
          masterTaxonomyDTO.getEmbeddedClasses().putAll(prepareVariantForKlass(workbook, fileName, masterTaxonomyDTO.getEmbeddedClasses(),
              (String) masterTaxo.get(EMBEDDED_CLASS_COLUMN), executionStatusTable));
        }
        if (!DiValidationUtil.isBlank(propertyCollectionSheetName)) {
          preparePropertyCollection(workbook, fileName, masterTaxonomyDTO.getSections(), propertyCollectionSheetName, executionStatusTable,
              MASTER_TAXONOMY_SHEET_NAME);
        }
        entities.add(masterTaxonomyDTO.toPXON());
      }
      catch (Exception e) {
        executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
            new String[] { (String) masterTaxo.get(CODE_COLUMN_CONFIG) });
      }
    }
  }
  
  /**
   * Prepare property collection required for taxonomy import and  klassimport
   * 
   * @param workbook
   * @param fileName
   * @param sections
   * @param sections
   * @param propertyCollectionSheetName
   * @param executionStatusTable
   */
  private void preparePropertyCollection(XSSFWorkbook workbook, String fileName, Map<String, Collection<IConfigSectionElementDTO>> sections,
      String propertyCollectionSheetName, IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable,String entitytype)
  {
    List<String> propertyCollectionSheetNames = getListFromString(propertyCollectionSheetName);
    for (String propertyCollectionsheet : propertyCollectionSheetNames) {
      List<Map<String, Object>> propertyCollectionList = DiFileUtils.prepareMapfromSheet(workbook, propertyCollectionsheet, fileName,
          executionStatusTable, null);
      for (Map<String, Object> propertyCollection : propertyCollectionList) {
        IConfigSectionElementDTO sectionElementDTO = new ConfigSectionElementDTO();
        sectionElementDTO.setCode((String) propertyCollection.get(PROPERTY_COLLECTION_CODE));

        if (!DiValidationUtil.isBlank((String) propertyCollection.get(PROPERTY_CODE_COLUMN))) {
          sectionElementDTO.setCode((String) propertyCollection.get(PROPERTY_CODE_COLUMN));
        }
        if(!DiValidationUtil.isBlank((String) propertyCollection.get(IS_INHERTIED))) {
        sectionElementDTO.setIsInherited(getBooleanValue(propertyCollection.get(IS_INHERTIED)));
        }
        sectionElementDTO.setIsSkipped(getBooleanValue(propertyCollection.get(SKIPPED_COLUMN)));
        if (!DiValidationUtil.isBlank((String) propertyCollection.get(PREFIX_COLUMN))) {
          sectionElementDTO.setPrefix((String) propertyCollection.get(PREFIX_COLUMN));
        }
        if (!DiValidationUtil.isBlank((String) propertyCollection.get(SUFFIX_COLUMN))) {
          sectionElementDTO.setSuffix((String) propertyCollection.get(SUFFIX_COLUMN));
        }
        
        String propertyType = (String) propertyCollection.get(PROPERTY_TYPE_COLUMN);
        sectionElementDTO.setType(propertyType);
        if (propertyType.equals(TAXONOMY)) {
          sectionElementDTO.setIsMultiSelect(getBooleanValue(propertyCollection.get(MULTISELECT_COLUMN)));
        }
        else {
          if (!DiValidationUtil.isBlank((String) propertyCollection.get(DEFAULT_VALUE))) {
            sectionElementDTO.setDefaultValue((String) propertyCollection.get(DEFAULT_VALUE));
          }
          if (!DiValidationUtil.isBlank((String) propertyCollection.get(COUPLING_COLUMN))) {
            sectionElementDTO.setCouplingType(DiTransformationUtils.convertCouplingType((String) propertyCollection.get(COUPLING_COLUMN)));
          }
          sectionElementDTO.setIsVersionable(getBooleanValue(propertyCollection.get(REVISIONABLE_COLUMN)));
          
          if (propertyType.equals(ENTITY_ATTRIBUTE)) {
            sectionElementDTO.setISIdentifier(getBooleanValue(propertyCollection.get(PRODUCT_IDENTIFIER_COLUMN)));
            sectionElementDTO.setIsTranslatable(getBooleanValue(propertyCollection.get(LANGUAGE_DEPENDENT_COLUMN)));
            if (!DiValidationUtil.isBlank((String) propertyCollection.get(ATTRIBUTE_CONTEXT_COLUMN))) {
              sectionElementDTO.setAttributeVariantContextCode((String) propertyCollection.get(ATTRIBUTE_CONTEXT_COLUMN));
            }
            if (propertyCollection.get(PRECISION_COLUMN) != null) {
              sectionElementDTO.setPrecision(Integer.parseInt((String) propertyCollection.get(PRECISION_COLUMN)));
            }
          }
          else if (propertyType.equals(ENTITY_TAG)) {
            sectionElementDTO.setIsMultiSelect(getBooleanValue(propertyCollection.get(MULTISELECT_COLUMN)));
            sectionElementDTO.setSelectedTagValues(getListFromString((String) propertyCollection.get(ALLOWED_VALUES_COLUMN)));
          }
        }
        String propertyCollectionId = (String) propertyCollection.get(PROPERTY_COLLECTION_CODE);
        if (sections.keySet().contains(propertyCollectionId)) {
          sections.get(propertyCollectionId).add(sectionElementDTO);
        }
        else {
          sections.put(propertyCollectionId, new ArrayList<>(Arrays.asList(sectionElementDTO)));
        }
      }
    }
  }

  /**
   * Prepare Variant required for klass import
   * 
   * @param workbook
   * @param fileName
   * @param sections
   * @param sections
   * @param propertyCollectionSheetName
   * @param executionStatusTable
   */
  protected Map<String, IConfigEmbeddedRelationDTO> prepareVariantForKlass(XSSFWorkbook workbook, String fileName,
      Map<String, IConfigEmbeddedRelationDTO> variantmap, String variantsheetname,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    List<String> variantSheetNames = getListFromString(variantsheetname);
    for (String variantSheetName : variantSheetNames) {
      List<Map<String, Object>> variantList = DiFileUtils.prepareMapfromSheet(workbook, variantSheetName, fileName, executionStatusTable,
          null);
      for (Map<String, Object> variant : variantList) {
        IConfigEmbeddedRelationDTO variantElementDTO = new ConfigEmbeddedRelationDTO();
        Collection<IJSONContent> jsonElements = new ArrayList<>();
        variantElementDTO.setType((String) variant.get(CLASS_TYPE));
        String code = (String) variant.get(CLASS_CODE);
        variantElementDTO.setCode(code);
        variantElementDTO.setLabel(code);
        String id = (String) variant.get(PROPERTY_CODE);
        String type = (String) variant.get(PROPERTY_TYPE);
        String coupling = (String) variant.get(COUPLING);
        String propertycode = (String) variant.get(PROPERTY_CODE);
        if (!DiValidationUtil.isBlank(coupling)) {
          jsonElements.add(prepareJSONElementforKlass(propertycode, DiTransformationUtils.convertCouplingType(coupling), id, type));
        }
        variantElementDTO.setCouplings(jsonElements);
        variantmap.put(code, variantElementDTO);
      }
      
    }
    
    return variantmap;
  }
  
  /**
   * Prepare Relationship required for klass import
   * 
   * @param workbook
   * @param fileName
   * @param sections
   * @param sections
   * @param propertyCollectionSheetName
   * @param executionStatusTable
   * @throws RDBMSException 
   */
  private void prepareRelationshipforKlass(XSSFWorkbook workbook, String fileName, String code,
      Collection<IConfigNatureRelationshipDTO> collection, String relationshipsheetname,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable) throws RDBMSException
  {
    List<String> relationshipsheetnames = getListFromString(relationshipsheetname);
    for (String relationshipsheet : relationshipsheetnames) {
      List<Map<String, Object>> relationshipList = DiFileUtils.prepareMapfromSheet(workbook, relationshipsheet, fileName,
          executionStatusTable, null);
      for (Map<String, Object> relationships : relationshipList) {
        IConfigNatureRelationshipDTO relationshipDTO = new ConfigNatureRelationshipDTO();
       // relationshipDTO.setPropertyDTO(code, IPropertyDTO.PropertyType.RELATIONSHIP);
        relationshipDTO.setIsNature(true);
        relationshipDTO.setRelationshipType((String) relationships.get(RELATIONSHIP_TYPE));
        relationshipDTO.setLabel(code);
        relationshipDTO.getSide1().setClassCode(code);
        relationshipDTO.getSide1().setLabel(code);
        String side2classcode = (String) relationships.get(SIDE2_CLASS_CODE);
        relationshipDTO.setPropertyDTO(RDBMSUtils.newUniqueID("REL"), IPropertyDTO.PropertyType.RELATIONSHIP);
        relationshipDTO.getSide2().setClassCode(side2classcode);
        relationshipDTO.getSide2().setLabel(side2classcode);
        List<IJSONContent> couplingPropertiesListOfSide1 = new ArrayList<>();
        if((relationships.get(ATTRIBUTES_SIDE1) != null  && relationships.get(ATTRIBUTESCOUPLING_SIDE1) != null )  ) {
          IJSONContent dataTransferProperty = new JSONContent();
          dataTransferProperty.setField(COUPLING,
              DiTransformationUtils.convertCouplingType((String) relationships.get(ATTRIBUTESCOUPLING_SIDE1)));
          dataTransferProperty.setField(ID_COLUMN, (String)relationships.get(ATTRIBUTES_SIDE1) );
          dataTransferProperty.setField(KLASS_TYPE,ENTITY_ATTRIBUTE);
          couplingPropertiesListOfSide1.add(dataTransferProperty);
        }
        if((relationships.get(TAGS_SIDE1) != null  && relationships.get(TAGSCOUPLING_SIDE1) != null ) ) {
          IJSONContent dataTransferProperty = new JSONContent();
          dataTransferProperty.setField(COUPLING,
              DiTransformationUtils.convertCouplingType((String) relationships.get(TAGSCOUPLING_SIDE1)));
          dataTransferProperty.setField(ID_COLUMN, (String)relationships.get(TAGS_SIDE1) );
          dataTransferProperty.setField(KLASS_TYPE, ENTITY_TAG);
          couplingPropertiesListOfSide1.add(dataTransferProperty);
        }
        relationshipDTO.getSide1().setCouplings(couplingPropertiesListOfSide1);
        List<IJSONContent> couplingPropertiesListOfSide2 = new ArrayList<>();
        if((relationships.get(ATTRIBUTES_SIDE2) != null  && relationships.get(ATTRIBUTESCOUPLING_SIDE2) != null ) ) {
          IJSONContent dataTransferProperty = new JSONContent();
          dataTransferProperty.setField(COUPLING, DiTransformationUtils.convertCouplingType((String)relationships.get(ATTRIBUTESCOUPLING_SIDE2)));
          dataTransferProperty.setField(ID_COLUMN, (String)relationships.get(ATTRIBUTES_SIDE2) );
          dataTransferProperty.setField(KLASS_TYPE,ENTITY_ATTRIBUTE);
          couplingPropertiesListOfSide2.add(dataTransferProperty);
        }
        if( (relationships.get(TAGS_SIDE2) != null  && relationships.get(TAGSCOUPLING_SIDE2) != null )  ) {
          IJSONContent dataTransferProperty = new JSONContent();
          dataTransferProperty.setField(COUPLING, DiTransformationUtils.convertCouplingType((String)relationships.get(TAGSCOUPLING_SIDE2)));
          dataTransferProperty.setField(ID_COLUMN, (String)relationships.get(TAGS_SIDE2) );
          dataTransferProperty.setField(KLASS_TYPE, ENTITY_TAG);
          couplingPropertiesListOfSide2.add(dataTransferProperty);
        } 
        relationshipDTO.getSide2().setCouplings(couplingPropertiesListOfSide2);
        relationshipDTO.setTab((String) relationships.get(TAB_CODE));
        relationshipDTO.isEnableAfterSave(getBooleanValue(relationships.get(ALLOW_AFTERSAVE_EVENT_TO_BE_TRIGGERED_FOR_CHANGES)));
        relationshipDTO.setTaxonomyInheritanceSetting((String) relationships.get(TAXONOMY_INHERITANCE));
        collection.add(relationshipDTO);
        
      }
    }
  }
  
  /**
   * Generate PXON for Hierarchy
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   */
  /* PXPFDEV-21215 : Deprecate - Taxonomy Hierarchies -- Remove it after usage checking
   * protected void generatePXONForHierarchy(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    Map<String, String> codeVsTaxonomyTypeMap = new HashMap<>();
    List<Map<String, Object>> entityList = DiFileUtils.prepareMapfromSheet(workbook, HIERARCHY_SHEET_NAME, fileName, executionStatusTable,
        codeVsTaxonomyTypeMap);
    for (Map<String, Object> hierarchy : entityList) {
      try {
        String code = (String) hierarchy.get(CODE_COLUMN_CONFIG);
        String parentCode = (String) hierarchy.get(PARENT_CODE_COLUMN);
        String type = codeVsTaxonomyTypeMap.get(code);
        String label = (String) hierarchy.get(LABEL_COLUMN_CONFIG);
        
        // Check mandatory columns.
        checkforMandatoryColumns(code, parentCode, type);
        
        IConfigClassifierDTO hierarchyDTO = new ConfigClassifierDTO();
        hierarchyDTO.setClassifierDTO(code, IClassifierDTO.ClassifierType.HIERARCHY);
        hierarchyDTO.setConfigTaxonomyType(type);
        hierarchyDTO.getLevelCodes().addAll(getListFromString((String) hierarchy.get(LEVEL_CODES_COLUMN)));
        hierarchyDTO.getLevelLables().addAll(getListFromString((String) hierarchy.get(LEVEL_LABEL_COLUMN)));
        hierarchyDTO.setParentCode(parentCode);
        hierarchyDTO.setLabel(label);
        hierarchyDTO.setIcon((String) hierarchy.get(ICON_COLUMN));
        hierarchyDTO.setSubType(IConfigClass.ClassifierClass.HIERARCHY_KLASS_TYPE.name());
        if (hierarchy.get(MASTER_TAG_PARENT_CODE_COLUMN) != null) {
          hierarchyDTO.setMasterParentTag((String) hierarchy.get(MASTER_TAG_PARENT_CODE_COLUMN));
        }
        hierarchyDTO.setIsNewlyCreated(getIsNewlyCreatedList((String) hierarchy.get(IS_NEWLY_CREATED_LEVEL_COLUMN)));
        String levelIndex = (String) hierarchy.get(LEVEL_INDEX_COLUMN);
        if(levelIndex != null && !levelIndex.isBlank()) {
          hierarchyDTO.setLevelIndex(Integer.parseInt(levelIndex));
        }
        entities.add(hierarchyDTO.toPXON());
      }
      catch (Exception e) {
        executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
            new String[] { (String) hierarchy.get(CODE_COLUMN_CONFIG) });
      }
    }
  }*/
  

  /**
   * Convert string type of list having 0 and 1 value to boolean list having
   * true and false values.
   *
   * @param isNewlyCreatedList
   * @return
   */
  private List<Boolean> getIsNewlyCreatedList(String isNewlyCreatedList)
  {
    List<Boolean> isNewlyCreated = new ArrayList<>();
    if (isNewlyCreatedList != null) {
      getListFromString(isNewlyCreatedList).stream().forEach(value -> {
        if (value.equals(TRUE)) {
          isNewlyCreated.add(true);
        }
        else if (value.equals(FALSE)) {
          isNewlyCreated.add(false);
        }
        // NOTE: Do not add default else statement.
      });
    }
    return isNewlyCreated;
  }

  /**
   * Convert comma separated string to list.
   * 
   * @param commaSeperatedString
   * @return
   */
  private List<String> getListFromString(String commaSeperatedString)
  {
    return commaSeperatedString == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(commaSeperatedString.split(";", -1)));
  }
  
  /**
   * Get boolean value
   * 
   * @param object
   * @return
   */
  public boolean getBooleanValue(Object object)
  {
    String value = (String) object;
    return value != null && TRUE.equals(value) ? true : false;
  }
  
  /**
   * Method to check if all mandatory columns are present
   * 
   * @param columnValues
   * @throws Exception
   */
  private void checkforMandatoryColumns(String... columnValues) throws Exception
  {
    for (String columnValue : columnValues) {
      if (DiValidationUtil.isBlank(columnValue)) {
        throw new Exception();
      }
    }
  }
  
  /**
   * Generate PXON for Context
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   */
  protected void generatePXONForContext(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    Map<String, Map<String, Object>> entityList = prepareAggregateMapfromSheet(workbook, CONTEXT_SHEET_NAME, fileName,
        executionStatusTable);
    // entities Map
    Map<String, String> entitiesToAddMap = new HashMap<>();
    entitiesToAddMap.put(ARTICLE_KEY, ARTICLE_INSTANCE);
    entitiesToAddMap.put(ASSET_KEY, ASSET_INSTANCE);
    entitiesToAddMap.put(MARKET_KEY, MARKET_INSTANCE);
    entitiesToAddMap.put(SUPPLIER_KEY, ASSET_INSTANCE);
    entitiesToAddMap.put(TEXT_ASSET_KEY, TEXT_ASSET_INSTANCE);
    entitiesToAddMap.put(VIRTUAL_CATALOG_KEY, VIRTUAL_CATALOG_INSTANCE);
    // type of Context Map
    Map<String, IContextDTO.ContextType> typeOfContextMap = new HashMap<>();
    typeOfContextMap.put(ATTRIBUTE_VARIANT_CONTEXT, ContextType.ATTRIBUTE_CONTEXT);
    typeOfContextMap.put(RELATIONSHIP_VARIANT_CONTEXT, ContextType.RELATIONSHIP_VARIANT);
    typeOfContextMap.put(PRODUCT_VARIANT_CONTEXT, ContextType.LINKED_VARIANT);
    
    if (!CollectionUtils.isEmpty(entityList)) {
      for (Map<String, Object> entity : entityList.values()) {
        try {
          String code = (String) entity.get(CODE_COLUMN_CONFIG);
          String label = (String) entity.get(LABEL_COLUMN_CONFIG);
          String type = (String) entity.get(TYPE_COLUMN);
          checkforMandatoryColumns(code, label, type);
          ConfigContextDTO contextDTO = new ConfigContextDTO();
          contextDTO.setLabel(label);
          if (typeOfContextMap.get(type) != null) {
            contextDTO.setContextDTO(code, typeOfContextMap.get(type));
          }
          contextDTO.setIsAutoCreate(false);
          contextDTO.setIsCurrentTime(getBooleanValue((String) entity.get(USE_CURRENT_TIME)));
          contextDTO.setIsDuplicateVariantAllowed(getBooleanValue((String) entity.get(ALLOW_DUPLICATES)));
          contextDTO.setIsTimeEnabled(getBooleanValue((String) entity.get(IS_TIME_ENABLED)));
          contextDTO.setIcon(entity.get(ICON_COLUMN) == null ? "" : (String) entity.get(ICON_COLUMN));
          
          List<String> entitiesToAdd = new ArrayList<>();
          List<String> entitiesFromSheet = getListFromString((String) entity.get(ENTITIES));
          for (String eachEntity : entitiesFromSheet) {
            if (entitiesToAddMap.get(eachEntity) != null) {
              entitiesToAdd.add((String) entitiesToAddMap.get(eachEntity));
            }
          }
          contextDTO.setEntities(entitiesToAdd);
          
          String enableTimeFrom = (String) entity.get(ENABLE_TIME_FROM);
          String enableTimeTo = (String) entity.get(ENABLE_TIME_TO);
          if (enableTimeFrom != null) {
            contextDTO.setDefaultStartTime(DiUtils.getLongValueOfDateString(enableTimeFrom));
          }
          if (enableTimeTo != null) {
            contextDTO.setDefaultEndTime(DiUtils.getLongValueOfDateString(enableTimeTo));
          }
          IJSONContent jsonContent = new JSONContent();
          if (entity.get(SELECTED_TAGS) != null) {
            if (entity.get(SELECTED_TAGS) instanceof String) {
              jsonContent.setStringArrayField((String) entity.get(SELECTED_TAGS),
                  getListFromString((String) entity.get(SELECTED_TAG_VALUES)));
            }
            else {
              // if not a string then fetch the list.
              List<String> tagsList = (List<String>) entity.get(SELECTED_TAGS);
              List<String> tagValuesList = (List<String>) entity.get(SELECTED_TAG_VALUES);
              for (int i = 0; i < tagsList.size(); i++) {
                jsonContent.setStringArrayField(tagsList.get(i), getListFromString((String) tagValuesList.get(i)));
              }
            }
          }
          
          contextDTO.setTagCodes(jsonContent);
          entities.add(contextDTO.toPXON());
        }
        catch (Exception e) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
              new String[] { (String) entity.get(CODE_COLUMN_CONFIG) });
        }
      }
    }
  }
  
  /**
   * Generate PXON for Task
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   */
  protected void generatePXONForTasks(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    List<Map<String, Object>> entityList = DiFileUtils.prepareMapfromSheet(workbook, TASK_SHEET_NAME, fileName, executionStatusTable, null);
    if (!CollectionUtils.isEmpty(entityList)) {
      for (Map<String, Object> entity : entityList) {
        try {
          String code = (String) entity.get(CODE_COLUMN_CONFIG);
          String type = (String) entity.get(TYPE_COLUMN);
          checkforMandatoryColumns(code, type);
          
          IConfigTaskDTO configTaskDTO = new ConfigTaskDTO();
          configTaskDTO.setTaskDTO(code, IConfigMap.getTaskType(type.toLowerCase()));
          configTaskDTO.setColor(entity.get(COLOR_COLUMN) == null ? "" : (String) entity.get(COLOR_COLUMN));
          configTaskDTO.setIcon(entity.get(ICON_KEY) == null ? "" : (String) entity.get(ICON_KEY));
          configTaskDTO.setLabel((String) entity.get(LABEL_COLUMN_CONFIG));
          configTaskDTO.setPriorityTag((String) entity.get(PRIORITY_TAG_COLUMN));
          configTaskDTO.setStatusTag((String) entity.get(STATUS_TAG_COLUMN));
          entities.add(configTaskDTO.toPXON());
        }
        catch (Exception e) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
              new String[] { (String) entity.get(CODE_COLUMN_CONFIG) });
        }
      }
    }
  }
  
  /**
   * Generate PXON for User
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   */
  protected void generatePXONForUsers(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    
    List<Map<String, Object>> entityList = DiFileUtils.prepareMapfromSheet(workbook, USER_SHEET_NAME, fileName, executionStatusTable, null);
    if (!CollectionUtils.isEmpty(entityList)) {
      for (Map<String, Object> entity : entityList) {
        try {
          String code = (String) entity.get(CODE);
          String userName = (String) entity.get(USERNAME);
          String firstName = (String) entity.get(FIRSTNAME);
          String lastName = (String) entity.get(LASTNAME);
          checkforMandatoryColumns(code, userName, firstName, lastName);
          
          IConfigUserDTO configUserDTO = new ConfigUserDTO();
          configUserDTO.setIsBackgroundUser(false);
          configUserDTO.setCode(code);
          configUserDTO.setUserName(userName);
          configUserDTO.setPassword((String) entity.get(PASSWORD));
          configUserDTO.setFirstName(firstName);
          configUserDTO.setLastName(lastName);
          configUserDTO.setGender((String) entity.get(GENDER));
          String email = (String) entity.get(EMAIL);
          configUserDTO.setPreferredDataLanguage((String)entity.get(DATA_LANGUAGE));
          configUserDTO.setPreferredUILanguage((String)entity.get(UI_LANGUAGE));
          
          if (!DiValidationUtil.emailValidator(email)) {
            executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN034, new String[] { email, code });
            continue;
          }
          configUserDTO.setEmail(email);
          String contact = (String) entity.get(CONTACT);
          if (!DiValidationUtil.contactNumberValidator(contact)) {
            executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN034, new String[] { contact, code });
            continue;
          }
          configUserDTO.setContact(contact);
          configUserDTO.setIcon((String) entity.get(ICON));
          entities.add(configUserDTO.toPXON());
        }
        catch (Exception e) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
              new String[] { (String) entity.get(CODE) });
        }
      }
    }
    
  }
  
  /**
   * This method prepares map from excel sheet
   * 
   * @param workbook
   * @param sheetName
   * @param fileName
   * @param executionStatusTable
   * @return
   */
  private Map<String, Map<String, Object>> prepareAggregateMapfromSheet(XSSFWorkbook workbook, String sheetName, String fileName,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    Map<String, Map<String, Object>> configEntityMap = new HashMap<>();
    try {
      XSSFSheet sheet = DiFileUtils.getSheet(workbook, sheetName);
      if (sheet == null) {
        throw new Exception();
      }
      List<String> headersToRead = DiFileUtils.getheaders(sheet, HEADER_ROW_NUMBER);
      Integer emptyRowNumber = 0;
      Integer lastRowNumber = sheet.getLastRowNum();
      lastRowNumber++;
      for (int rowCount = DATA_ROW_NUMBER - 1; rowCount < lastRowNumber; rowCount++) {
        Row itemRow = sheet.getRow(rowCount);
        if (itemRow != null && emptyRowNumber < 6) {
          emptyRowNumber = 0;
          Map<String, Object> entityMap = DiFileUtils.prepareMapfromRow(itemRow, headersToRead);
          switch (sheetName) {
            case RELATIONSHIP_SHEET_NAME_FOR_CONFIG:
              aggregateRelationshipMap(configEntityMap, entityMap);
              break;
            case DATA_RULES_SHEET_NAME:
              prepareAggregateDataRuleMap(configEntityMap, entityMap);
              break;
            case CONTEXT_SHEET_NAME:
              prepareAggregatedMapforContext(configEntityMap, entityMap, SELECTED_TAGS, SELECTED_TAG_VALUES);
              break;
            case PROPERTY_COLLECTION_SHEET_NAME:
              prepareAggregatedMapforPC(configEntityMap, entityMap, ENTITY_CODE_COLUMN, ENTITY_TYPE_COLUMN, INDEX_COLUMN);
              break;
            case GOLDEN_RECORD_RULES_SHEET_NAME:
              prepareAggregatedMapforGoldenRule(configEntityMap, entityMap);
              break;
            default:
              break;
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
      executionStatusTable.addError(new ObjectCode[] {}, MessageCode.GEN007, new String[] { sheetName, fileName });
    }
    return configEntityMap;
  }
  
 

  /**
   * This method prepares map of Partner DTO from excel sheet
   * 
   * @param workbook
   * @param sheetName
   * @param fileName
   * @param executionStatusTable
   * @return
   */
  private Map<String, ConfigOrganizationDTO> preparePartnerDTOMapfromExcelSheet(XSSFWorkbook workbook, String sheetName, String fileName,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    Map<String, ConfigOrganizationDTO> configDTOMap = new HashMap<>();
    try {
      XSSFSheet sheet = DiFileUtils.getSheet(workbook, sheetName);
      if (sheet == null) {
        throw new Exception();
      }
      List<String> headersToRead = DiFileUtils.getheaders(sheet, HEADER_ROW_NUMBER);
      Integer emptyRowNumber = 0;
      Integer lastRowNumber = sheet.getLastRowNum();
      lastRowNumber++;
      for (int rowCount = DATA_ROW_NUMBER - 1; rowCount < lastRowNumber; rowCount++) {
        Row itemRow = sheet.getRow(rowCount);
        if (itemRow != null && emptyRowNumber < 6) {
          emptyRowNumber = 0;
          createOrUpdatePartnerDTO(configDTOMap, DiFileUtils.prepareMapfromRow(itemRow, headersToRead), executionStatusTable);
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
      executionStatusTable.addError(new ObjectCode[] {}, MessageCode.GEN007, new String[] { sheetName, fileName });
    }
    return configDTOMap;
  }
  
  /**
   * Method prepares map of ConfigTranslationDTO by processing excel sheet
   * 
   * @param workbook
   * @param fileName
   * @param executionStatusTable
   * @return
   */
  private Map<String, ConfigTranslationDTO> prepareTranslationDTOMap(XSSFWorkbook workbook, String fileName,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    Map<String, ConfigTranslationDTO> configDTOMap = new HashMap<>();
    for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
      String sheetName = workbook.getSheetName(sheetIndex);
      try {
        XSSFSheet sheet = DiFileUtils.getSheet(workbook, sheetName);
        if (sheet == null) {
          throw new Exception();
        }
        List<String> headersToRead = DiFileUtils.getheaders(sheet, HEADER_ROW_NUMBER);
        Integer emptyRowNumber = 0;
        Integer lastRowNumber = sheet.getLastRowNum();
        lastRowNumber++;
        for (int rowCount = DATA_ROW_NUMBER - 1; rowCount < lastRowNumber; rowCount++) {
          Row itemRow = sheet.getRow(rowCount);
          if (itemRow != null && emptyRowNumber < 6) {
            emptyRowNumber = 0;
            createOrUpdateTranslationDTO(configDTOMap, DiFileUtils.prepareMapfromRow(itemRow, headersToRead), sheetName, executionStatusTable);
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
        executionStatusTable.addError(new ObjectCode[] {}, MessageCode.GEN007, new String[] { sheetName, fileName });
      }
    }
    
    return configDTOMap;
  }
  
  /**
   * Prepare DTO for organization by reading excel sheet
   * 
   * @param configDTOMap
   * @param entityMap
   * @param executionStatusTable
   */
  private void createOrUpdatePartnerDTO(Map<String, ConfigOrganizationDTO> configDTOMap, Map<String, Object> entityMap,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    if (!CollectionUtils.isEmpty(entityMap)) {
      ConfigOrganizationDTO organizationDTO = null;
      String entityCode = (String) entityMap.get(CODE_COLUMN_CONFIG);
      try {
        // Create organization
        if (!configDTOMap.containsKey(entityCode)) {
          String label = (String) entityMap.get(LABEL_COLUMN_CONFIG);
          checkforMandatoryColumns(entityCode, label);
          organizationDTO = new ConfigOrganizationDTO();
          organizationDTO.setCode(entityCode);
          organizationDTO.setLabel(label);
          organizationDTO.setType((String) entityMap.get(TYPE_COLUMN));
          organizationDTO.setIcon((String) entityMap.get(ICON_COLUMN));
          organizationDTO.setPhysicalCatalogs(getListFromString((String) entityMap.get(PHYSICAL_CATALOG)));
          organizationDTO.setPortals(getListFromString((String) entityMap.get(PORTALS)));
          organizationDTO.setTaxonomyIds(getListFromString((String) entityMap.get(TAXONOMY_CODE)));
          organizationDTO.setKlassIds(getListFromString((String) entityMap.get(TARGET_CLASS_CODE)));
          organizationDTO.setSystems(getListFromString((String) entityMap.get(SYSTEM_CODE)));
          organizationDTO.setEndpointIds(getListFromString((String) entityMap.get(ENDPOINT_CODE)));
        }
        else {
          organizationDTO = configDTOMap.get(entityCode);
        }
        
        // Add role to organization
        ConfigRoleDTO roleDTO = new ConfigRoleDTO();
        String roleCode = (String) entityMap.get(ROLE_CODE);
        String roleLabel = (String) entityMap.get(ROLE_LABEL);
        checkforMandatoryColumns(roleCode, roleLabel);
        roleDTO.setCode(roleCode);
        roleDTO.setLabel(roleLabel);
        roleDTO.setType(Role.class.getName());
        roleDTO.setRoleType((String) entityMap.get(ROLE_TYPE));
        roleDTO.setPhysicalCatalogs(getListFromString((String) entityMap.get(ROLE_PHYSICAL_CATALOG)));
        roleDTO.setPortals(getListFromString((String) entityMap.get(ROLE_PORTALS)));
        roleDTO.setTargetTaxonomies(getListFromString((String) entityMap.get(ROLE_TAXONOMY_CODE)));
        roleDTO.setTargetKlasses(getListFromString((String) entityMap.get(ROLE_TARGET_CLASS_CODE)));
        roleDTO.setUsers(getListFromString((String) entityMap.get(ROLE_USERS)));
        roleDTO.setIsDashboardEnable(getBooleanValue(entityMap.get(ROLE_ENABLED_DASHBOARD)));
        roleDTO.setIsReadOnly(getBooleanValue(entityMap.get(ROLE_READONLY_USER)));
        roleDTO.setKpis(getListFromString((String) entityMap.get(ROLE_KPI)));
        roleDTO.setEntities(getListFromString((String) entityMap.get(ROLE_ENTITIES)));
        roleDTO.setSystems(getListFromString((String) entityMap.get(ROLE_SYSTEM_CODE)));
        roleDTO.setEndpoints(getListFromString((String) entityMap.get(ROLE_ENDPOINT_CODE)));
        roleDTO.setIsStandard(false);
        roleDTO.setIsBackgroundRole(false);
        organizationDTO.getRoles().add(roleDTO);
        
        configDTOMap.put(entityCode, organizationDTO);
      }
      catch (Exception e) {
        executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
            new String[] { (String) entityMap.get(CODE_COLUMN_CONFIG) });
      }
    }
  }
  
  /**
   * Prepare Translation DTO by reading excel sheet
   * 
   * @param configDTOMap
   * @param entityMap
   * @param sheetName
   * @param executionStatusTable
   */
  private void createOrUpdateTranslationDTO(Map<String, ConfigTranslationDTO> configDTOMap, Map<String, Object> entityMap, String sheetName,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    if (!CollectionUtils.isEmpty(entityMap)) {
      ConfigTranslationDTO configTranslationDTO = null;
      String entityCode = (String) entityMap.get(ENTITY_CODE_COLUMN);
      try {
        // Create translation
        if (!configDTOMap.containsKey(entityCode)) {
          String type = (String) entityMap.get(ENTITY_TYPE_COLUMN);
          checkforMandatoryColumns(type, entityCode);
          configTranslationDTO = new ConfigTranslationDTO();
          configTranslationDTO.setType(EntityType.valueOf(type));
          configTranslationDTO.setCode(entityCode);
          IJSONContent languageCodeJSONContent = new JSONContent();
          languageCodeJSONContent.setField(sheetName, prepareTranslation(entityMap));
          configTranslationDTO.setTranslations(languageCodeJSONContent);
        }
        else {
          //Update translation
          configTranslationDTO = configDTOMap.get(entityCode);
          IJSONContent translations = configTranslationDTO.getTranslations();
          translations.setField(sheetName, prepareTranslation(entityMap));
        }
        configDTOMap.put(entityCode, configTranslationDTO);
      }
      catch (Exception e) {
        executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
            new String[] { entityCode });
      }
    }
    
  }

  /**
   * Prepares translation related info
   * 
   * @param entityMap
   * @return
   */
  private IJSONContent prepareTranslation(Map<String, Object> entityMap)
  {
    IJSONContent translations = new JSONContent();
    translations.setField(LABEL, (String) entityMap.get(LABEL_COLUMN_CONFIG));
    translations.setField(DESCRIPTION_VAL, (String) entityMap.get(DESCRIPTION_COLUMN_CONFIG));
    translations.setField(PLACEHOLDER_VAL, (String) entityMap.get(PLACEHOLDER_COLUMN_CONFIG));
    translations.setField(TOOLTIP_VAL, (String) entityMap.get(TOOLTIP_COLUMN_CONFIG));
    translations.setField(SIDE1_LABEL_VAL, (String) entityMap.get(SIDE1_LABEL));
    translations.setField(SIDE2_LABEL_VAL, (String) entityMap.get(SIDE2_LABEL));
    return translations;
  }
  
  /**
   * This method make list of data transfer properties and add in map based on relationship code.
   * 
   * @param relationshipMap
   * @param headersToRead
   * @param itemRow
   */
  private void aggregateRelationshipMap(Map<String, Map<String, Object>> relationshipMap, Map<String, Object> entityMap)
  {
    String relationshipCode = (String) entityMap.get(CODE);
    if (relationshipCode != null && !relationshipMap.containsKey(relationshipCode)) {
      if (entityMap.get(S1_PROPERTY_CODE) != null && entityMap.get(S1_PROPERTY_TYPE) != null
          && entityMap.get(S1_COUPLING_TYPE) != null) {
        List<IJSONContent> couplingPropertiesList = new ArrayList<>();
        IJSONContent dataTransferProperty = new JSONContent();
        dataTransferProperty.setField(ID_PROPERTY, (String) entityMap.remove(S1_PROPERTY_CODE));
        dataTransferProperty.setField(TYPE_PROPERTY, (String) entityMap.remove(S1_PROPERTY_TYPE));
        dataTransferProperty.setField(COUPLING_TYPE,
            DiTransformationUtils.convertCouplingType((String) entityMap.remove(S1_COUPLING_TYPE)));
        couplingPropertiesList.add(dataTransferProperty);
        entityMap.put(SIDE1_COUPLING_PROPERTIES, couplingPropertiesList);
      }
      if (entityMap.get(S2_PROPERTY_CODE) != null && entityMap.get(S2_PROPERTY_TYPE) != null
          && entityMap.get(S2_COUPLING_TYPE) != null) {
        List<IJSONContent> couplingPropertiesList = new ArrayList<>();
        IJSONContent dataTransferProperty = new JSONContent();
        dataTransferProperty.setField(ID_PROPERTY, (String) entityMap.remove(S2_PROPERTY_CODE));
        dataTransferProperty.setField(TYPE_PROPERTY, (String) entityMap.remove(S2_PROPERTY_TYPE));
        dataTransferProperty.setField(COUPLING_TYPE,
            DiTransformationUtils.convertCouplingType((String) entityMap.remove(S2_COUPLING_TYPE)));
        couplingPropertiesList.add(dataTransferProperty);
        entityMap.put(SIDE2_COUPLING_PROPERTIES, couplingPropertiesList);
      }
      relationshipMap.put(relationshipCode, entityMap);
    }
    else if (relationshipCode != null && relationshipMap.containsKey(relationshipCode)) {
      Map<String, Object> relMap = relationshipMap.get(relationshipCode);
      List<IJSONContent> s1CoupingProperties = (List<IJSONContent>) relMap.get(SIDE1_COUPLING_PROPERTIES);
      List<IJSONContent> s2CoupingProperties = (List<IJSONContent>) relMap.get(SIDE2_COUPLING_PROPERTIES);
      if (entityMap.get(S1_PROPERTY_CODE) != null && entityMap.get(S1_PROPERTY_TYPE) != null
          && entityMap.get(S1_COUPLING_TYPE) != null) {
        IJSONContent dataTransferProperty = new JSONContent();
        dataTransferProperty.setField(ID_PROPERTY, (String) entityMap.remove(S1_PROPERTY_CODE));
        dataTransferProperty.setField(TYPE_PROPERTY, (String) entityMap.remove(S1_PROPERTY_TYPE));
        dataTransferProperty.setField(COUPLING_TYPE,
            DiTransformationUtils.convertCouplingType((String) entityMap.remove(S1_COUPLING_TYPE)));
        s1CoupingProperties.add(dataTransferProperty);
      }
      if (entityMap.get(S2_PROPERTY_CODE) != null && entityMap.get(S2_PROPERTY_TYPE) != null
          && entityMap.get(S2_COUPLING_TYPE) != null) {
        IJSONContent dataTransferProperty = new JSONContent();
        dataTransferProperty.setField(ID_PROPERTY, (String) entityMap.remove(S2_PROPERTY_CODE));
        dataTransferProperty.setField(TYPE_PROPERTY, (String) entityMap.remove(S2_PROPERTY_TYPE));
        dataTransferProperty.setField(COUPLING_TYPE,
            DiTransformationUtils.convertCouplingType((String) entityMap.remove(S2_COUPLING_TYPE)));
        s2CoupingProperties.add(dataTransferProperty);
      }
    }
  }
  
  /**
   * Generate PXON for DataRules
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   */
  protected void generatePXONForDataRules(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    Map<String, Map<String, Object>> entityList = prepareAggregateMapfromSheet(workbook, DATA_RULES_SHEET_NAME, fileName,
        executionStatusTable);
    
    if (!CollectionUtils.isEmpty(entityList)) {
      for (Map<String, Object> entity : entityList.values()) {
        try {
          String code = (String) entity.get(CODE_COLUMN_CONFIG);
          String label = (String) entity.get(LABEL_COLUMN_CONFIG);
          String type = (String) entity.get(TYPE_COLUMN);
          checkforMandatoryColumns(code, label, type);
          
          ConfigDataRuleDTO dataRuleDTO = new ConfigDataRuleDTO();
          dataRuleDTO.setCode(code);
          dataRuleDTO.setLabel(label);
          dataRuleDTO.setType(type);
          dataRuleDTO.setIsLanguageDependent(getBooleanValue((String) entity.get(LANGUAGE_DEPENDENT_COLUMN)));
          dataRuleDTO.setIsStandard(false);
          
          if (dataRuleDTO.getIsLanguageDependent() == true) {
            dataRuleDTO.setLanguages(getListFromString((String) entity.get(LANGUAGES)));
          }
            dataRuleDTO.setPhysicalCatalogIds(getListFromString((String) entity.get(PHYSICALCATALOGS)));
            dataRuleDTO.setOrganizations(getListFromString((String) entity.get(PARTNERS)));
          
          switch (type) {
            case TYPE_CLASSIFICATION:
              dataRuleDTO.setTypes(getListFromString((String) entity.get(CLASSIFICATION_CAUSE_KLASSES)));
              dataRuleDTO.setTaxonomyCodes(getListFromString((String) entity.get(CLASSIFICATION_CAUSE_TAXONOMIES)));
              if (entity.get(CLASSIFICATION_EFFECT_KLASSES) != null) {
                dataRuleDTO.getNormalizations()
                    .add(setNormalizationObject(KLASS_TYPE, getListFromString((String) entity.get(CLASSIFICATION_EFFECT_KLASSES))));
              }
              if (entity.get(CLASSIFICATION_EFFECT_TAXONOMIES) != null) {
                dataRuleDTO.getNormalizations()
                    .add(setNormalizationObject(TAXONOMY, getListFromString((String) entity.get(CLASSIFICATION_EFFECT_TAXONOMIES))));
              }
              break;
            case TYPE_SAN:
              if (entity.get(CAUSE_ENTITY_CODE) != null) {
                dataRuleDTO = addAttributesAndTagsToCause(dataRuleDTO, entity);
              }
              dataRuleDTO.setTypes(getListFromString((String) entity.get(CAUSE_KLASSES)));
              dataRuleDTO.setTaxonomyCodes(getListFromString((String) entity.get(CAUSE_TAXONOMIES)));
              if (entity.get(EFFECT_ENTITY_CODE) != null) {
                dataRuleDTO = addAttributesAndTagsToSANNormalization(dataRuleDTO, entity);
              }
              if (entity.get(SAN_EFFECT_KLASSES) != null) {
                dataRuleDTO.getNormalizations()
                    .add(setNormalizationObject(KLASS_TYPE, getListFromString((String) entity.get(SAN_EFFECT_KLASSES))));
              }
              if (entity.get(SAN_EFFECT_TAXONOMIES) != null) {
                dataRuleDTO.getNormalizations()
                    .add(setNormalizationObject(TAXONOMY, getListFromString((String) entity.get(SAN_EFFECT_TAXONOMIES))));
              }
              break;
            case TYPE_VIO:
              if (entity.get(CAUSE_ENTITY_CODE) != null) {
                dataRuleDTO = addAttributesAndTagsToCause(dataRuleDTO, entity);
              }
              dataRuleDTO.setTypes(getListFromString((String) entity.get(CAUSE_KLASSES)));
              dataRuleDTO.setTaxonomyCodes(getListFromString((String) entity.get(CAUSE_TAXONOMIES)));
              if (entity.get(EFFECT_ENTITY_CODE) != null) {
                dataRuleDTO = prepareJSONElementsForRuleViolation(dataRuleDTO, entity);
              }
              break;
          }
          entities.add(dataRuleDTO.toPXON());
        }
        catch (Exception e) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
              new String[] { (String) entity.get(CODE_COLUMN_CONFIG) });
        }
      }
    }
  }
  
  /**
   * Set tag values in tag object of Data Rule
   * 
   * @param entityCode
   * @param entityValue
   * @param tagType
   * @return
   */
  JSONContent setTagValuesForTag(String entityCode, String entityValue, String tagType)
  {
    JSONContent setTagValueObj = new JSONContent();
    if (tagType.equals(TAG_TYPE_BOOL)) {
      setTagValueObj.setField(ID, entityCode + VALUE);
      Integer value = entityValue.equals(ONE) ? 100 : 0;
      setTagValueObj.setField(ConfigTag.to.toString(), value);
      setTagValueObj.setField(ConfigTag.from.toString(), value);
    }
    else {
      setTagValueObj.setField(ID, entityValue);
      setTagValueObj.setField(ConfigTag.to.toString(), 100);
      setTagValueObj.setField(ConfigTag.from.toString(), 100);
    }
    return setTagValueObj;
  }
  
  /**
   * method to add normalizations of attributes and tags to SAN
   * 
   * @param dataRuleDTO
   * @param entityCodes
   * @param entityTypes
   * @param entitySubType
   * @param transformationType
   * @param entityValues
   * @param entitySpclValues
   * @return
   * @throws CSFormatException
   */
  private ConfigDataRuleDTO addAttributesAndTagsToSANNormalization(ConfigDataRuleDTO dataRuleDTO, Map<String, Object> entity)
      throws CSFormatException
  {
    List<String> entityCodes = (List<String>) entity.get(EFFECT_ENTITY_CODE);
    List<String> entityTypes = (List<String>) entity.get(EFFECT_ENTITY_TYPE);
    List<String> entitySubType = (List<String>) entity.get(EFFECT_ENTITY_SUBTYPE);
    List<String> transformationType = (List<String>) entity.get(SAN_EFFECT_TRANSFORMATION_TYPE);
    List<String> entityValues = (List<String>) entity.get(SAN_EFFECT_ENTITY_VALUE);
    List<String> entitySpclValues = (List<String>) entity.get(SAN_EFFECT_ENTITY_SPECIAL_VALUE);
    for (int index = 0; index < entityCodes.size(); index++) {
      
      String entityType = entityTypes.get(index);
      String transformTypeOfEntity = transformationType.get(index);
      String entityValue = entityValues.get(index);
      String entityCode = entityCodes.get(index);
      String subTypeEntity = entitySubType.get(index);
      IConfigNormalizationDTO normalization = new ConfigNormalizationDTO();
      
      normalization.setEntityId(entityCode);
      normalization.setType(entityType);
      normalization.setTransformationType(transformTypeOfEntity);
      
      if (entityType.equals(ENTITY_TAG)) {
        normalization.setBaseType(CommonConstants.NORMALIZATION_BASE_TYPE);
        if (entityValues.get(index) != null) {
          JSONContent setTagValueObj = setTagValuesForTag(entityCode, entityValue, subTypeEntity);
          normalization.setTagValues(Arrays.asList(setTagValueObj));
        }
      }
      else if (entityType.equals(ENTITY_ATTRIBUTE)) {
        
        switch (transformTypeOfEntity) {
          case TRANSFORMATION_TYPE_REPLACE:
            normalization.setBaseType(CommonConstants.FIND_REPLACE_NORMALIZATION_BASE_TYPE);
            List<String> inputs = (List<String>) getListFromString(entityValue);
            normalization.setFindText(inputs.get(0));
            normalization.setReplaceText(inputs.get(1));
            break;
          case TRANSFORMATION_TYPE_LOWERCASE:
          case TRANSFORMATION_TYPE_UPPERCASE:
          case TRANSFORMATION_TYPE_PROPERCASE:
          case TRANSFORMATION_TYPE_TRIM:
            normalization.setBaseType(CommonConstants.NORMALIZATION_BASE_TYPE);
            break;
          case TRANSFORMATION_TYPE_ATTRIBUTEVALUE:
            normalization.setBaseType(CommonConstants.ATTRIBUTE_VALUE_NORMALIZATION_BASE_TYPE);
            normalization.setValueAttributeId(entityValue);
            break;
          case TRANSFORMATION_TYPE_SUBSTRING:
            normalization.setBaseType(CommonConstants.SUB_STRING_NORMALIZATION_BASE_TYPE);
            List<String> indices = (List<String>) getListFromString(entityValue);
            normalization.setStartIndex(Integer.valueOf(indices.get(0)));
            normalization.setEndIndex(Integer.valueOf(indices.get(1)));
            break;
          case TRANSFORMATION_TYPE_VALUE:
            normalization.setBaseType(CommonConstants.NORMALIZATION_BASE_TYPE);
            if (subTypeEntity.equals(TRANSFORMATION_TYPE_VALUE_HTML)) {
              normalization.setValueAsHTML(
                  TRANSFORMATION_TYPE_VALUE_HTML_PARAGRAPH_START + entityValue + TRANSFORMATION_TYPE_VALUE_HTML_PARAGRAPH_END);
            }
            if (subTypeEntity.equals(TRANSFORMATION_TYPE_VALUE_DATE)) {
              normalization.setValues(Arrays.asList(Long.toString(DiUtils.getLongValueOfDateString(entityValue))));
            }
            else {
              normalization.setValues(Arrays.asList(entityValue));
            }
            break;
          case TRANSFORMATION_TYPE_CONCAT:
            normalization.setBaseType(CommonConstants.CONCATENATED_NORMALIZATION_BASE_TYPE);
            List<String> concatValues = (List<String>) getListFromString(entityValue);
            List<String> concatSpecificType = getListFromString(entitySpclValues.get(index));
            List<IJSONContent> listOfConcatobjs = new ArrayList<>();
            for (int i = 0; i < concatSpecificType.size(); i++) {
              JSONContent setConcatObj = new JSONContent();
              setConcatObj.setField(VERSION_ID, 1);
              setConcatObj.setField(ENTITY_TYPE, concatSpecificType.get(i));
              // concat entitySpecialValue can be html or attribute
              if (concatSpecificType.get(i).equals(ENTITY_SPECIAL_VALUE_HTML)) {
                setConcatObj.setField(VALUE, concatValues.get(i));
              }
              else {
                setConcatObj.setField(ATTRIBUTE_ID, concatValues.get(i));
              }
              setConcatObj.setField(ORDER, i);
              listOfConcatobjs.add(setConcatObj);
            }
            normalization.setAttributeConcatenatedList(listOfConcatobjs);
            break;
        }
      }
      dataRuleDTO.getNormalizations().addAll(Arrays.asList(normalization));
    }
    return dataRuleDTO;
  }
  
  /**
   * Add elements to effect side of Violation Rule
   * 
   * @param dataRuleDTO
   * @param effectEntityCodes
   * @param effectEntityTypes
   * @param vioEffectColors
   * @param vioEffectDescription
   * @return
   * @throws RDBMSException
   */
  private ConfigDataRuleDTO prepareJSONElementsForRuleViolation(ConfigDataRuleDTO dataRuleDTO, Map<String, Object> entity)
      throws RDBMSException
  {
    List<String> effectEntityCodes = (List<String>) entity.get(EFFECT_ENTITY_CODE);
    List<String> effectEntityTypes = (List<String>) entity.get(EFFECT_ENTITY_TYPE);
    List<String> vioEffectColors = (List<String>) entity.get(VIOLATION_EFFECT_COLOR);
    List<String> vioEffectDescription = (List<String>) entity.get(VIOLATION_EFFECT_DESCRIPTION);
    
    List<IJSONContent> jsonElements = new ArrayList<>();
    for (int index = 0; index < effectEntityCodes.size(); index++) {
      IJSONContent element = new JSONContent();
      element.setField(ENTITY_ID, effectEntityCodes.get(index));
      element.setField(ENTITY_TYPE, effectEntityTypes.get(index));
      element.setField(VIO_COLOR, vioEffectColors.get(index));
      element.setField(VIO_DESCRIPTION, vioEffectDescription.get(index));
      element.setField(VERSION_ID, 1);
      jsonElements.add(element);
    }
    dataRuleDTO.setRuleVioloation(jsonElements);
    return dataRuleDTO;
  }
  
  /**
   * @param type
   * @param entityList
   * @return
   * @throws CSFormatException
   */
  private IConfigNormalizationDTO setNormalizationObject(String type, List<String> entityList) throws CSFormatException
  {
    IConfigNormalizationDTO normalizationObj = new ConfigNormalizationDTO();
    normalizationObj.setType(type);
    normalizationObj.setBaseType(CommonConstants.NORMALIZATION_BASE_TYPE);
    normalizationObj.setValues(entityList);
    normalizationObj.setTransformationType("");
    return normalizationObj;
  }
  
  /**
   * add Attributes And Tags to Cause side of SAN and Violation Data rule
   * 
   * @param dataRuleDTO
   * @param entityCodes
   * @param entityTypes
   * @param entitySubType
   * @param compareType
   * @param entityValues
   * @param entityValueTypes
   * @param from
   * @param to
   * @param comparetoCurrentDate
   * @return
   */
  private ConfigDataRuleDTO addAttributesAndTagsToCause(ConfigDataRuleDTO dataRuleDTO, Map<String, Object> entity)
  {
    List<String> entityCodes = (List<String>) entity.get(CAUSE_ENTITY_CODE);
    List<String> entityTypes = (List<String>) entity.get(CAUSE_ENTITY_TYPE);
    List<String> entitySubType = (List<String>) entity.get(CAUSE_ENTITY_SUBTYPE);
    List<String> compareType = (List<String>) entity.get(CAUSE_COMPARE_TYPE);
    List<String> entityValues = (List<String>) entity.get(CAUSE_ENTITY_VALUE);
    List<String> entityValueTypes = (List<String>) entity.get(CAUSE_ENTITY_VALUE_TYPE);
    List<String> from = (List<String>) entity.get(CAUSE_FROM);
    List<String> to = (List<String>) entity.get(CAUSE_TO);
    for (int index = 0; index < entityCodes.size(); index++) {
      
      List<String> compareTypesList = getListFromString(compareType.get(index));
      List<String> entityValueTypesList = getListFromString(entityValueTypes.get(index));
      List<String> entityValuesList = getListFromString(entityValues.get(index));
      List<String> fromList = getListFromString(from.get(index));
      List<String> toList = getListFromString(to.get(index));
      
      if (entityTypes.get(index).equals(ENTITY_ATTRIBUTE)) {
        IConfigDataRuleIntermediateEntitysDTO attributeDTO = new ConfigDataRuleIntermediateEntitysDTO();
        attributeDTO.setEntityId(entityCodes.get(index));
        
        for (int counter = 0; counter < compareTypesList.size(); counter++) {
          IConfigRuleEntityDTO attributeRuleDTO = new ConfigRuleEntityDTO();
          attributeRuleDTO.setType(compareTypesList.get(counter));
          switch (compareTypesList.get(counter)) {            
            case COMPARE_TYPE_CONTAINS:
            case COMPARE_TYPE_START:
            case COMPARE_TYPE_END:
            case COMPARE_TYPE_LT:
            case COMPARE_TYPE_GT:
            case COMPARE_TYPE_EQUAL:
              if (entityValueTypesList.get(counter).equals(ENTITY_ATTRIBUTE)) {
                attributeRuleDTO.setAttributeLinkId(entityValuesList.get(counter));
              }
              else {
                if (entityValuesList.isEmpty()) {
                  entityValuesList.add("");
                }
                attributeRuleDTO.setValues(Arrays.asList(entityValuesList.get(counter)));
              }
              break;
            
            case COMPARE_TYPE_EXACT:
              if (entityValueTypesList.get(counter).equals(ENTITY_ATTRIBUTE)) {
                attributeRuleDTO.setAttributeLinkId(entityValuesList.get(counter));
              }
              else {  
                if (entitySubType.get(index).equals(TRANSFORMATION_TYPE_VALUE_DATE)) {
                  attributeRuleDTO.setValues(Arrays.asList(Long.toString(DiUtils.getLongValueOfDateString(entityValuesList.get(counter)))));
                }
                else {
                  attributeRuleDTO.setValues(Arrays.asList(entityValuesList.get(counter)));
                }
              }
              break;
            case COMPARE_TYPE_NOT_EMPTY:
            case COMPARE_TYPE_EMPTY:
              break;
            case COMPARE_TYPE_RANGE:
              attributeRuleDTO.setFrom(fromList.get(counter));
              attributeRuleDTO.setTo(toList.get(counter));
              break;
            case COMPARE_TYPE_REGEX:
              attributeRuleDTO.setValues(Arrays.asList(entityValuesList.get(counter)));
              break;
            case COMPARE_TYPE_IS_DUPLICATE:
              String[] klasses = entityValuesList.get(counter).split("\\|", -1);
              attributeRuleDTO.setKlassLinkIds(Arrays.asList(klasses));
              break;
            case COMPARE_TYPE_DATE_LT:
            case COMPARE_TYPE_DATE_GT:
              if (entityValueTypesList.get(counter).equals(ENTITY_ATTRIBUTE)) {
                attributeRuleDTO.setAttributeLinkId(entityValuesList.get(counter));
              }
              else {
                if ((entitySubType.get(index).compareToIgnoreCase(DATE_ATTRIBUTE_TYPE))==0) {
                  if (entityValuesList.get(counter).isEmpty()) {
                    attributeRuleDTO.setShouldCompareWithSystemDate(true);
                  }
                  else {
                    attributeRuleDTO.setValues(Arrays.asList(Long.toString(
                        DiUtils.getLongValueOfDateString(entityValuesList.get(counter)))));
                    attributeRuleDTO.setShouldCompareWithSystemDate(false);
                  }
                }
                else {
                  // type is number, price, measurement 
                  if (entityValuesList.isEmpty()) {
                    entityValuesList.add("");
                  }
                  attributeRuleDTO.setValues(Arrays.asList(entityValuesList.get(counter)));
                }
              }
              break;
            case COMPARE_TYPE_DATE_RANGE:
              attributeRuleDTO.setFrom(Long.toString(DiUtils.getLongValueOfDateString(fromList.get(counter))));
              attributeRuleDTO.setTo(Long.toString(DiUtils.getLongValueOfDateString(toList.get(counter))));
              break;
          }
          
          attributeDTO.getRules().add(attributeRuleDTO);
          
        }
        dataRuleDTO.getAttributes().addAll(Arrays.asList(attributeDTO));
        
      }
      else if (entityTypes.get(index).equals(ENTITY_TAG)) {
        IConfigDataRuleTagsDTO tagDTO = new ConfigDataRuleTagsDTO();
        tagDTO.setEntityId(entityCodes.get(index));
        for (int counter = 0; counter < compareTypesList.size(); counter++) {
          IConfigDataRuleTagRuleDTO tagRuleDTO = new ConfigDataRuleTagRuleDTO();
          tagRuleDTO.setType(compareTypesList.get(counter));
          if (!entityValuesList.get(counter).isEmpty()) {
            JSONContent setTagValueObj = setTagValuesForTag(entityCodes.get(index), entityValuesList.get(counter), entitySubType.get(index));
            tagRuleDTO.setTagValues(Arrays.asList(setTagValueObj));
          }
          else {
            tagRuleDTO.setTagValues(new ArrayList<>());
          }
          tagDTO.getRules().add(tagRuleDTO);
        }
        dataRuleDTO.getTags().add(tagDTO);
      }
    }
    return dataRuleDTO;
  }
  
  /**
   * This method prepares map from Data Rules sheet
   * 
   * 
   * @param dataRulesMap
   * @param headersToRead
   * @param itemRow
   */
  private void prepareAggregateDataRuleMap(Map<String, Map<String, Object>> dataRulesMap, Map<String, Object> entityMap)
  {
   if (!CollectionUtils.isEmpty(entityMap)) {
      String entityCode = (String) entityMap.get(CODE_COLUMN_CONFIG);
      List<String> aggregateEntityCodes = new ArrayList<>();
      List<String> aggregateEntityType = new ArrayList<>();
      List<String> entitySubType = new ArrayList<>();
      List<String> compareType = new ArrayList<>();
      List<String> entityValueType = new ArrayList<>();
      List<String> entityValues = new ArrayList<>();
      List<String> from = new ArrayList<>();
      List<String> to = new ArrayList<>();
      List<String> compareWithCurrentDate = new ArrayList<>();
      // effect for violation and SAN
      List<String> effectEntityCodes = new ArrayList<>();
      List<String> effectEntityTypes = new ArrayList<>();
      List<String> violationEffectColors = new ArrayList<>();
      List<String> violationEffectDescription = new ArrayList<>();
      List<String> effectEntitySubType = new ArrayList<>();
      List<String> effectTransformationType = new ArrayList<>();
      List<String> effectEntityValue = new ArrayList<>();
      List<String> effectEntitySpclValue = new ArrayList<>();
      
      if (entityCode != null) {
        if (!dataRulesMap.containsKey(entityCode)) {
          if (entityMap.get(CAUSE_ENTITY_CODE) != null) {
            aggregateEntityCodes.add((String) entityMap.get(CAUSE_ENTITY_CODE));
            aggregateEntityType.add((String) entityMap.get(CAUSE_ENTITY_TYPE));
            entitySubType.add((String) entityMap.get(CAUSE_ENTITY_SUBTYPE));
            compareType.add((String) entityMap.get(CAUSE_COMPARE_TYPE));
            entityValueType.add((String) entityMap.get(CAUSE_ENTITY_VALUE_TYPE));
            entityValues.add((String) entityMap.get(CAUSE_ENTITY_VALUE));
            from.add((String) entityMap.get(CAUSE_FROM));
            to.add((String) entityMap.get(CAUSE_TO));
            compareWithCurrentDate.add((String) entityMap.get(CAUSE_COMPARE_WITH_CURRENT_DATE));
            
            entityMap.put(CAUSE_ENTITY_CODE, aggregateEntityCodes);
            entityMap.put(CAUSE_ENTITY_TYPE, aggregateEntityType);
            entityMap.put(CAUSE_ENTITY_SUBTYPE, entitySubType);
            entityMap.put(CAUSE_COMPARE_TYPE, compareType);
            entityMap.put(CAUSE_ENTITY_VALUE_TYPE, entityValueType);
            entityMap.put(CAUSE_ENTITY_VALUE, entityValues);
            entityMap.put(CAUSE_FROM, from);
            entityMap.put(CAUSE_TO, to);
            entityMap.put(CAUSE_COMPARE_WITH_CURRENT_DATE, compareWithCurrentDate);
          }
          if (entityMap.get(EFFECT_ENTITY_CODE) != null) {
            effectEntityCodes.add((String) entityMap.get(EFFECT_ENTITY_CODE));
            effectEntityTypes.add((String) entityMap.get(EFFECT_ENTITY_TYPE));
            
            entityMap.put(EFFECT_ENTITY_CODE, effectEntityCodes);
            entityMap.put(EFFECT_ENTITY_TYPE, effectEntityTypes);
            if (entityMap.get(TYPE_COLUMN) != null) {
              if (entityMap.get(TYPE_COLUMN).equals(TYPE_VIO)) {
                violationEffectColors.add(entityMap.get(VIOLATION_EFFECT_COLOR) == null ? VIOLATION_EFFECT_DEFAULT_COLOR
                    : (String) entityMap.get(VIOLATION_EFFECT_COLOR));
                violationEffectDescription.add(entityMap.get(VIOLATION_EFFECT_DESCRIPTION) == null ? VIOLATION_EFFECT_DEFAULT_DESCRIPTION
                    : (String) entityMap.get(VIOLATION_EFFECT_DESCRIPTION));
                
                entityMap.put(VIOLATION_EFFECT_COLOR, violationEffectColors);
                entityMap.put(VIOLATION_EFFECT_DESCRIPTION, violationEffectDescription);
              }
              else if (entityMap.get(TYPE_COLUMN).equals(TYPE_SAN)) {
                effectEntitySubType.add((String) entityMap.get(EFFECT_ENTITY_SUBTYPE));
                effectTransformationType.add((String) entityMap.get(SAN_EFFECT_TRANSFORMATION_TYPE));
                effectEntityValue.add((String) entityMap.get(SAN_EFFECT_ENTITY_VALUE));
                effectEntitySpclValue.add((String) entityMap.get(SAN_EFFECT_ENTITY_SPECIAL_VALUE));
                
                entityMap.put(EFFECT_ENTITY_SUBTYPE, effectEntitySubType);
                entityMap.put(SAN_EFFECT_TRANSFORMATION_TYPE, effectTransformationType);
                entityMap.put(SAN_EFFECT_ENTITY_VALUE, effectEntityValue);
                entityMap.put(SAN_EFFECT_ENTITY_SPECIAL_VALUE, effectEntitySpclValue);
                
              }
            }
          }
          dataRulesMap.put(entityCode, entityMap);
        }
        else {
          Map<String, Object> requestedMap = dataRulesMap.get(entityCode);
          if (entityMap.get(CAUSE_ENTITY_CODE) != null) {
            aggregateEntityCodes.addAll((List<String>) requestedMap.get(CAUSE_ENTITY_CODE));
            aggregateEntityType.addAll((List<String>) requestedMap.get(CAUSE_ENTITY_TYPE));
            entitySubType.addAll((List<String>) requestedMap.get(CAUSE_ENTITY_SUBTYPE));
            compareType.addAll((List<String>) requestedMap.get(CAUSE_COMPARE_TYPE));
            entityValueType.addAll((List<String>) requestedMap.get(CAUSE_ENTITY_VALUE_TYPE));
            entityValues.addAll((List<String>) requestedMap.get(CAUSE_ENTITY_VALUE));
            from.addAll((List<String>) requestedMap.get(CAUSE_FROM));
            to.addAll((List<String>) requestedMap.get(CAUSE_TO));
            compareWithCurrentDate.addAll((List<String>) requestedMap.get(CAUSE_COMPARE_WITH_CURRENT_DATE));
            
            aggregateEntityCodes.add((String) entityMap.get(CAUSE_ENTITY_CODE));
            aggregateEntityType.add((String) entityMap.get(CAUSE_ENTITY_TYPE));
            entitySubType.add((String) entityMap.get(CAUSE_ENTITY_SUBTYPE));
            compareType.add((String) entityMap.get(CAUSE_COMPARE_TYPE));
            entityValueType.add((String) entityMap.get(CAUSE_ENTITY_VALUE_TYPE));
            entityValues.add((String) entityMap.get(CAUSE_ENTITY_VALUE));
            from.add((String) entityMap.get(CAUSE_FROM));
            to.add((String) entityMap.get(CAUSE_TO));
            compareWithCurrentDate.add((String) entityMap.get(CAUSE_COMPARE_WITH_CURRENT_DATE));
            
            requestedMap.put(CAUSE_ENTITY_CODE, aggregateEntityCodes);
            requestedMap.put(CAUSE_ENTITY_TYPE, aggregateEntityType);
            requestedMap.put(CAUSE_ENTITY_SUBTYPE, entitySubType);
            requestedMap.put(CAUSE_COMPARE_TYPE, compareType);
            requestedMap.put(CAUSE_ENTITY_VALUE_TYPE, entityValueType);
            requestedMap.put(CAUSE_ENTITY_VALUE, entityValues);
            requestedMap.put(CAUSE_FROM, from);
            requestedMap.put(CAUSE_TO, to);
            requestedMap.put(CAUSE_COMPARE_WITH_CURRENT_DATE, compareWithCurrentDate);
            
          }
          if (entityMap.get(EFFECT_ENTITY_CODE) != null) {
            effectEntityCodes.addAll((List<String>) requestedMap.get(EFFECT_ENTITY_CODE));
            effectEntityTypes.addAll((List<String>) requestedMap.get(EFFECT_ENTITY_TYPE));
            
            effectEntityCodes.add((String) entityMap.get(EFFECT_ENTITY_CODE));
            effectEntityTypes.add((String) entityMap.get(EFFECT_ENTITY_TYPE));
            
            requestedMap.put(EFFECT_ENTITY_CODE, effectEntityCodes);
            requestedMap.put(EFFECT_ENTITY_TYPE, effectEntityTypes);
            
            if (requestedMap.get(TYPE_COLUMN).equals(TYPE_VIO)) {
              violationEffectColors.addAll((List<String>) requestedMap.get(VIOLATION_EFFECT_COLOR));
              violationEffectDescription.addAll((List<String>) requestedMap.get(VIOLATION_EFFECT_DESCRIPTION));
              
              violationEffectColors.add(entityMap.get(VIOLATION_EFFECT_COLOR) == null ? VIOLATION_EFFECT_DEFAULT_COLOR
                  : (String) entityMap.get(VIOLATION_EFFECT_COLOR));
              violationEffectDescription.add(entityMap.get(VIOLATION_EFFECT_DESCRIPTION) == null ? VIOLATION_EFFECT_DEFAULT_DESCRIPTION
                  : (String) entityMap.get(VIOLATION_EFFECT_DESCRIPTION));
              
              requestedMap.put(VIOLATION_EFFECT_COLOR, violationEffectColors);
              requestedMap.put(VIOLATION_EFFECT_DESCRIPTION, violationEffectDescription);
              
            }
            else if (requestedMap.get(TYPE_COLUMN).equals(TYPE_SAN)) {
              effectEntitySubType.addAll((List<String>) requestedMap.get(EFFECT_ENTITY_SUBTYPE));
              effectTransformationType.addAll((List<String>) requestedMap.get(SAN_EFFECT_TRANSFORMATION_TYPE));
              effectEntityValue.addAll((List<String>) requestedMap.get(SAN_EFFECT_ENTITY_VALUE));
              effectEntitySpclValue.addAll((List<String>) requestedMap.get(SAN_EFFECT_ENTITY_SPECIAL_VALUE));
              
              effectEntitySubType.add((String) entityMap.get(EFFECT_ENTITY_SUBTYPE));
              effectTransformationType.add((String) entityMap.get(SAN_EFFECT_TRANSFORMATION_TYPE));
              effectEntityValue.add((String) entityMap.get(SAN_EFFECT_ENTITY_VALUE));
              effectEntitySpclValue.add((String) entityMap.get(SAN_EFFECT_ENTITY_SPECIAL_VALUE));
              
              requestedMap.put(EFFECT_ENTITY_SUBTYPE, effectEntitySubType);
              requestedMap.put(SAN_EFFECT_TRANSFORMATION_TYPE, effectTransformationType);
              requestedMap.put(SAN_EFFECT_ENTITY_VALUE, effectEntityValue);
              requestedMap.put(SAN_EFFECT_ENTITY_SPECIAL_VALUE, effectEntitySpclValue);
            }
          }
          dataRulesMap.put(entityCode, requestedMap);
        }
      }
    }
  }
  
  /**
   * Prepares Map for context and PC entities
   * 
   * 
   * @param map
   * @param headersToRead
   * @param itemRow
   * @param entityCodeColumn
   * @param entityRelatedInfoColumn
   */
  protected void prepareAggregatedMapforContext(Map<String, Map<String, Object>> map, Map<String, Object> entityMap,
      String entityCodeColumn, String entityRelatedInfoColumn)
  {
    if (!CollectionUtils.isEmpty(entityMap)) {
      String entityCode = (String) entityMap.get(CODE_COLUMN_CONFIG);
      if (entityCode != null) {
        if (!map.containsKey(entityCode)) {
          map.put(entityCode, entityMap);
        }
        else {
          List<String> aggregateEntityCodes = new ArrayList<>();
          List<String> aggregateRequiredInfo = new ArrayList<>();
          
          Map<String, Object> requestedMap = map.get(entityCode);
          if (requestedMap.get(entityCodeColumn) != null) {
            if (requestedMap.get(entityCodeColumn) instanceof String) {
              aggregateEntityCodes.add((String) requestedMap.get(entityCodeColumn));
              aggregateRequiredInfo.add((String) requestedMap.get(entityRelatedInfoColumn));
            }
            else {
              aggregateEntityCodes.addAll((List<String>) requestedMap.get(entityCodeColumn));
              aggregateRequiredInfo.addAll((List<String>) requestedMap.get(entityRelatedInfoColumn));
            }
            aggregateEntityCodes.add((String) entityMap.get(entityCodeColumn));
            aggregateRequiredInfo.add((String) entityMap.get(entityRelatedInfoColumn));
            requestedMap.put(entityCodeColumn, aggregateEntityCodes);
            requestedMap.put(entityRelatedInfoColumn, aggregateRequiredInfo);
            map.put(entityCode, requestedMap);
          }
        }
      }
    }
  }
  
  /**
   * generate PXON for Golden Record Rules
   * 
   * @param workbook
   * @param fileName
   * @param entities
   * @param executionStatusTable
   */
  protected void generatePXONForGoldenRecordRules(XSSFWorkbook workbook, String fileName, List<String> entities,
      IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable)
  {
    Map<String, Map<String, Object>> entityList = prepareAggregateMapfromSheet(workbook, GOLDEN_RECORD_RULES_SHEET_NAME, fileName,
        executionStatusTable);
    
    if (!CollectionUtils.isEmpty(entityList)) {
      for (Map<String, Object> entity : entityList.values()) {
        try {
          String code = (String) entity.get(CODE_COLUMN_CONFIG);
          String label = (String) entity.get(LABEL_COLUMN_CONFIG);
          String klass = (String) entity.get(NATURE_TYPE);
          checkforMandatoryColumns(code, label, klass);
          
          IConfigGoldenRecordRuleDTO goldenRecordDTO = new ConfigGoldenRecordRuleDTO();
          goldenRecordDTO.setCode(code);
          goldenRecordDTO.setLabel(label);
          List<String> klassIds = new ArrayList<String>();
          klassIds.add(klass);
          if (entity.get(MATCH_NON_NATURE_CLASSES) != null) {
            klassIds.addAll(getListFromString((String) entity.get(MATCH_NON_NATURE_CLASSES)));
          }
          goldenRecordDTO.setKlassIds(klassIds);
          goldenRecordDTO.setOrganizations(getListFromString((String) entity.get(PARTNERS)));
          goldenRecordDTO.setPhysicalCatalogIds(getListFromString((String) entity.get(PHYSICALCATALOGS)));
          goldenRecordDTO.setAttributes(getListFromString((String) entity.get(MATCH_ATTRIBUTES)));
          goldenRecordDTO.setTags(getListFromString((String) entity.get(MATCH_TAGS)));
          goldenRecordDTO.setTaxonomyIds(getListFromString((String) entity.get(MATCH_TAXONOMIES)));
          goldenRecordDTO.setIsAutoCreate(getBooleanValue((String) entity.get(AUTOCREATE)));
          
          IConfigMergeEffectDTO configMergeEffectDTO = new ConfigMergeEffectDTO();
          if (entity.get(MERGE_ATTRIBUTES) != null) {
            addAttributesToConfigMergeEffectDTO(configMergeEffectDTO, entity);
          }
          if (entity.get(MERGE_TAGS) != null) {
            addTagsToConfigMergeEffectDTO(configMergeEffectDTO, entity);
          }
          if (entity.get(MERGE_RELATIONSHIPS) != null) {
            addRelationshipsToConfigMergeEffectDTO(configMergeEffectDTO, entity);
          }
          if (entity.get(MERGE_NATURE_RELATIONSHIPS) != null) {
            addNatureRelationshipsToConfigMergeEffectDTO(configMergeEffectDTO, entity);
          }
          goldenRecordDTO.setMergeEffect(configMergeEffectDTO);
          entities.add(goldenRecordDTO.toPXON());
        }
        catch (Exception e) {
          executionStatusTable.addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN014,
              new String[] { (String) entity.get(CODE_COLUMN_CONFIG) });
        }
      }
    }
  }
  
  /**
   * set attribute details to Merge section of Golden Record Rule
   * 
   * @param configMergeEffectDTO
   * @param entity
   * @throws CSFormatException
   */
  private void addAttributesToConfigMergeEffectDTO(IConfigMergeEffectDTO configMergeEffectDTO, Map<String, Object> entity)
      throws CSFormatException
  {
    List<IConfigMergeEffectTypeDTO> attributeList = configMergeEffectDTO.getAttributes();
    List<String> attributes = (List<String>) entity.get(MERGE_ATTRIBUTES);
    List<String> attributeType = (List<String>) entity.get(MERGE_ATTRIBUTES_LATEST);
    List<String> attributeSuppliers = (List<String>) entity.get(MERGE_ATTRIBUTES_SUPPLIERS);
    for (int index = 0; index < attributes.size(); index++) {
      attributeList.add(setConfigMergeEffectDTO(attributeType.get(index), CommonConstants.ATTRIBUTES, attributes.get(index),
          getListFromString(attributeSuppliers.get(index))));
    }
  }
  
  /**
   * set tag details to Merge section of Golden Record Rule
   * 
   * @param configMergeEffectDTO
   * @param entity
   * @throws CSFormatException
   */
  private void addTagsToConfigMergeEffectDTO(IConfigMergeEffectDTO configMergeEffectDTO, Map<String, Object> entity)
      throws CSFormatException
  {
    List<IConfigMergeEffectTypeDTO> tagList = configMergeEffectDTO.getTags();
    List<String> tags = (List<String>) entity.get(MERGE_TAGS);
    List<String> tagType = (List<String>) entity.get(MERGE_TAGS_LATEST);
    List<String> tagSuppliers = (List<String>) entity.get(MERGE_TAGS_SUPPLIERS);
    for (int index = 0; index < tags.size(); index++) {
      tagList.add(
          setConfigMergeEffectDTO(tagType.get(index), CommonConstants.TAGS, tags.get(index), getListFromString(tagSuppliers.get(index))));
    }
  }
  
  /**
   * set relationship details to Merge section of Golden Record Rule
   * 
   * @param configMergeEffectDTO
   * @param entity
   * @throws CSFormatException
   */
  private void addRelationshipsToConfigMergeEffectDTO(IConfigMergeEffectDTO configMergeEffectDTO, Map<String, Object> entity)
      throws CSFormatException
  {
    List<IConfigMergeEffectTypeDTO> relationshipList = configMergeEffectDTO.getRelationships();
    List<String> relationships = (List<String>) entity.get(MERGE_RELATIONSHIPS);
    List<String> relationshipSuppliers = (List<String>) entity.get(MERGE_RELATIONSHIPS_SUPPLIERS);
    for (int index = 0; index < relationships.size(); index++) {
      relationshipList.add(setConfigMergeEffectDTO(FALSE, CommonConstants.RELATIONSHIPS, relationships.get(index),
          getListFromString(relationshipSuppliers.get(index))));
    }
  }
  
  /**
   * set nature relationship details to Merge section of Golden Record Rule
   * 
   * @param configMergeEffectDTO
   * @param entity
   * @throws CSFormatException
   */
  private void addNatureRelationshipsToConfigMergeEffectDTO(IConfigMergeEffectDTO configMergeEffectDTO, Map<String, Object> entity)
      throws CSFormatException
  {
    List<IConfigMergeEffectTypeDTO> natureRelationshipList = configMergeEffectDTO.getNatureRelationships();
    List<String> relationships = (List<String>) entity.get(MERGE_NATURE_RELATIONSHIPS);
    List<String> relationshipSuppliers = (List<String>) entity.get(MERGE_NATURE_RELATIONSHIPS_SUPPLIERS);
    for (int index = 0; index < relationships.size(); index++) {
      natureRelationshipList.add(setConfigMergeEffectDTO(FALSE, CommonConstants.NATURE_RELATIONSHIPS, relationships.get(index),
          getListFromString(relationshipSuppliers.get(index))));
    }
  }
  
  /**
   * set DTO object of Merge section of Golden Record Rule
   * 
   * @param type
   * @param entityType
   * @param entityID
   * @param supplierIDs
   * @return
   * @throws CSFormatException
   */
  private IConfigMergeEffectTypeDTO setConfigMergeEffectDTO(String type, String entityType, String entityID, List<String> supplierIDs)
      throws CSFormatException
  {
    IConfigMergeEffectTypeDTO entityMergeDTO = new ConfigMergeEffectTypeDTO();
    entityMergeDTO.setType(type.equals(ONE) ? LATEST_PRIORITY : CommonConstants.SUPPLIER_PRIORITY);
    entityMergeDTO.setEntityType(entityType);
    entityMergeDTO.setEntityId(entityID);
    entityMergeDTO.setSupplierIds(supplierIDs);
    return entityMergeDTO;
  }
  
  /**
   * Prepare map for Golden Record
   * 
   * 
   * @param goldenRecordMap
   * @param headersToRead
   * @param itemRow
   */
  private void prepareAggregatedMapforGoldenRule(Map<String, Map<String, Object>> goldenRecordMap, Map<String, Object> entityMap)
  {
    if (!CollectionUtils.isEmpty(entityMap)) {
      String entityCode = (String) entityMap.get(CODE_COLUMN_CONFIG);
      if (entityCode != null) {
        if (!goldenRecordMap.containsKey(entityCode)) {
          if (entityMap.get(MERGE_ATTRIBUTES) != null) {
            entityMap.put(MERGE_ATTRIBUTES, new ArrayList<String>(Arrays.asList((String) entityMap.get(MERGE_ATTRIBUTES))));
            entityMap.put(MERGE_ATTRIBUTES_LATEST, new ArrayList<String>(Arrays.asList((String) entityMap.get(MERGE_ATTRIBUTES_LATEST))));
            entityMap.put(MERGE_ATTRIBUTES_SUPPLIERS,
                new ArrayList<String>(Arrays.asList((String) entityMap.get(MERGE_ATTRIBUTES_SUPPLIERS))));
          }
          if (entityMap.get(MERGE_TAGS) != null) {
            entityMap.put(MERGE_TAGS, new ArrayList<String>(Arrays.asList((String) entityMap.get(MERGE_TAGS))));
            entityMap.put(MERGE_TAGS_LATEST, new ArrayList<String>(Arrays.asList((String) entityMap.get(MERGE_TAGS_LATEST))));
            entityMap.put(MERGE_TAGS_SUPPLIERS, new ArrayList<String>(Arrays.asList((String) entityMap.get(MERGE_TAGS_SUPPLIERS))));
          }
          if (entityMap.get(MERGE_RELATIONSHIPS) != null) {
            entityMap.put(MERGE_RELATIONSHIPS, new ArrayList<String>(Arrays.asList((String) entityMap.get(MERGE_RELATIONSHIPS))));
            entityMap.put(MERGE_RELATIONSHIPS_SUPPLIERS,
                new ArrayList<String>(Arrays.asList((String) entityMap.get(MERGE_RELATIONSHIPS_SUPPLIERS))));
          }
          if (entityMap.get(MERGE_NATURE_RELATIONSHIPS) != null) {
            entityMap.put(MERGE_NATURE_RELATIONSHIPS,
                new ArrayList<String>(Arrays.asList((String) entityMap.get(MERGE_NATURE_RELATIONSHIPS))));
            entityMap.put(MERGE_NATURE_RELATIONSHIPS_SUPPLIERS,
                new ArrayList<String>(Arrays.asList((String) entityMap.get(MERGE_NATURE_RELATIONSHIPS_SUPPLIERS))));
          }
          goldenRecordMap.put(entityCode, entityMap);
        }
        else {
          Map<String, Object> requestedMap = goldenRecordMap.get(entityCode);
          if (entityMap.get(MERGE_ATTRIBUTES) != null) {
            ((List<String>) requestedMap.get(MERGE_ATTRIBUTES)).add((String) entityMap.get(MERGE_ATTRIBUTES));
            ((List<String>) requestedMap.get(MERGE_ATTRIBUTES_LATEST)).add((String) entityMap.get(MERGE_ATTRIBUTES_LATEST));
            ((List<String>) requestedMap.get(MERGE_ATTRIBUTES_SUPPLIERS)).add((String) entityMap.get(MERGE_ATTRIBUTES_SUPPLIERS));
            
            requestedMap.put(MERGE_ATTRIBUTES, requestedMap.get(MERGE_ATTRIBUTES));
            requestedMap.put(MERGE_ATTRIBUTES_LATEST, requestedMap.get(MERGE_ATTRIBUTES_LATEST));
            requestedMap.put(MERGE_ATTRIBUTES_SUPPLIERS, requestedMap.get(MERGE_ATTRIBUTES_SUPPLIERS));
          }
          if (entityMap.get(MERGE_TAGS) != null) {
            ((List<String>) requestedMap.get(MERGE_TAGS)).add((String) entityMap.get(MERGE_TAGS));
            ((List<String>) requestedMap.get(MERGE_TAGS_LATEST)).add((String) entityMap.get(MERGE_TAGS_LATEST));
            ((List<String>) requestedMap.get(MERGE_TAGS_SUPPLIERS)).add((String) entityMap.get(MERGE_TAGS_SUPPLIERS));
            
            requestedMap.put(MERGE_TAGS, requestedMap.get(MERGE_TAGS));
            requestedMap.put(MERGE_TAGS_LATEST, requestedMap.get(MERGE_TAGS_LATEST));
            requestedMap.put(MERGE_TAGS_SUPPLIERS, requestedMap.get(MERGE_TAGS_SUPPLIERS));
          }
          if (entityMap.get(MERGE_RELATIONSHIPS) != null) {
            ((List<String>) requestedMap.get(MERGE_RELATIONSHIPS)).add((String) entityMap.get(MERGE_RELATIONSHIPS));
            ((List<String>) requestedMap.get(MERGE_RELATIONSHIPS_SUPPLIERS)).add((String) entityMap.get(MERGE_RELATIONSHIPS_SUPPLIERS));
            
            requestedMap.put(MERGE_RELATIONSHIPS, requestedMap.get(MERGE_RELATIONSHIPS));
            requestedMap.put(MERGE_RELATIONSHIPS_SUPPLIERS, requestedMap.get(MERGE_RELATIONSHIPS_SUPPLIERS));
          }
          if (entityMap.get(MERGE_NATURE_RELATIONSHIPS) != null) {
            ((List<String>) requestedMap.get(MERGE_NATURE_RELATIONSHIPS)).add((String) entityMap.get(MERGE_NATURE_RELATIONSHIPS));
            ((List<String>) requestedMap.get(MERGE_NATURE_RELATIONSHIPS_SUPPLIERS))
                .add((String) entityMap.get(MERGE_NATURE_RELATIONSHIPS_SUPPLIERS));
            
            requestedMap.put(MERGE_NATURE_RELATIONSHIPS, requestedMap.get(MERGE_NATURE_RELATIONSHIPS));
            requestedMap.put(MERGE_NATURE_RELATIONSHIPS_SUPPLIERS, requestedMap.get(MERGE_NATURE_RELATIONSHIPS_SUPPLIERS));
          }
          goldenRecordMap.put(entityCode, requestedMap);
        }
      }
    }
  }
  private void prepareAggregatedMapforPC(Map<String, Map<String, Object>> map, Map<String, Object> entityMap,
      String entityCodeColumn, String entityRelatedInfoColumn, String indexColumn )
  {
    if (!CollectionUtils.isEmpty(entityMap)) {
      String entityCode = (String) entityMap.get(CODE_COLUMN_CONFIG);
      if (entityCode != null) {
        if (!map.containsKey(entityCode)) {
          map.put(entityCode, entityMap);
        }
        else {
          List<String> aggregateEntityCodes = new ArrayList<>();
          List<String> aggregateRequiredInfo = new ArrayList<>();
          List<Integer> indexColumnList = new ArrayList<>();
          
          Map<String, Object> requestedMap = map.get(entityCode);
          if (requestedMap.get(entityCodeColumn) != null) {
            if (requestedMap.get(entityCodeColumn) instanceof String) {
              aggregateEntityCodes.add((String) requestedMap.get(entityCodeColumn));
              aggregateRequiredInfo.add((String) requestedMap.get(entityRelatedInfoColumn));
              indexColumnList.add(Integer.valueOf((String) requestedMap.get(indexColumn)));
            }
            else {
              aggregateEntityCodes.addAll((List<String>) requestedMap.get(entityCodeColumn));
              aggregateRequiredInfo.addAll((List<String>) requestedMap.get(entityRelatedInfoColumn));
              indexColumnList.addAll((List<Integer>) requestedMap.get(indexColumn));
            }
            aggregateEntityCodes.add((String) entityMap.get(entityCodeColumn));
            aggregateRequiredInfo.add((String) entityMap.get(entityRelatedInfoColumn));
            indexColumnList.add(Integer.valueOf( (String) entityMap.get(indexColumn)));
            requestedMap.put(entityCodeColumn, aggregateEntityCodes);
            requestedMap.put(entityRelatedInfoColumn, aggregateRequiredInfo);
            requestedMap.put(indexColumn, indexColumnList);
            map.put(entityCode, requestedMap);
          }
        }
      }
    }
    
  }
  /**
   * Implementation of Formula for Concatenated Attribute Type
   *
   * @param entity
   * @return
   */
  private List<IJSONContent> setFormulaForConcatenated(Map<String, Object> entity)
  {
    List<IJSONContent> concatenatedList = new ArrayList<IJSONContent>();
    List<String> typeOfPropertyConcatenated = getListFromString((String) entity.get(TYPE_OF_PROPERTY_CONCATENATED));
    List<String> entityCodeOfPropertyConcatenated = getListFromString((String) entity.get(ATTRIBUTE_CODE_OF_PROPERTY_CONCATENATED));
    List<String> valueOfPropertyConcatenated = getListFromString((String) entity.get(VALUE_OF_PROPERTY_CONCATENATED));
    for (int index = 0; index < typeOfPropertyConcatenated.size(); index++) {
      String setType = typeOfPropertyConcatenated.get(index);
      String entityCode = entityCodeOfPropertyConcatenated.isEmpty() ? EMPTY_STRING : entityCodeOfPropertyConcatenated.get(index);
      IJSONContent element = new JSONContent();
      element.setField(ConfigTag.type.toString(), setType);
      element.setField(ConfigTag.order.toString(), index);
      if (setType.equals(CommonConstants.ATTRIBUTE)) {
        element.setField(ConfigTag.attributeId.toString(), entityCode);
      }
      else if (setType.equals(CommonConstants.TAG)) {
        element.setField(ConfigTag.tagId.toString(), entityCode);
      }
      else {
        String valueAsHTML = valueOfPropertyConcatenated.get(index);
        element.setField(ConfigTag.value.toString(), valueAsHTML);
        if (!valueAsHTML.isEmpty()) {
          valueAsHTML = valueAsHTML.replaceAll(" ", TRANSFORMATION_TYPE_VALUE_HTML_NON_BREAKING_SPACE);
          element.setField(ConfigTag.valueAsHtml.toString(),
              TRANSFORMATION_TYPE_VALUE_HTML_PARAGRAPH_START + valueAsHTML + TRANSFORMATION_TYPE_VALUE_HTML_PARAGRAPH_END);
        }
      }
      concatenatedList.add(element);
    }
    return concatenatedList;
  }

  /**
   * Implementation of Formula for Calculated Attribute Type
   *
   * @param entity
   * @return
   */
  private List<IJSONContent> setFormulaForCalculated(Map<String, Object> entity)
  {
    List<IJSONContent> calculatedList = new ArrayList<IJSONContent>();
    List<String> typeOfPropertyCalculated = getListFromString((String) entity.get(TYPE_OF_PROPERTY_CALCULATED));
    List<String> entityCodeOfPropertyCalculated = getListFromString((String) entity.get(ATTRIBUTE_CODE_OF_PROPERTY_CALCULATED));
    List<String> valueOfPropertyCalculated = getListFromString((String) entity.get(VALUE_OF_PROPERTY_CALCULATED));
    for (int index = 0; index < typeOfPropertyCalculated.size(); index++) {
      String typeKey = typeOfPropertyCalculated.get(index).toUpperCase();
      IJSONContent element = new JSONContent();
      element.setField(ConfigTag.type.toString(), typeKey);
      element.setField(ConfigTag.order.toString(), index);

      if (typeKey.equalsIgnoreCase(CommonConstants.ATTRIBUTE)) {
        element.setField(ConfigTag.attributeId.toString(), entityCodeOfPropertyCalculated.get(index));
      }
      else if (typeKey.equalsIgnoreCase(CommonConstants.VALUE_PROPERTY)) {
        element.setField(ConfigTag.value.toString(), valueOfPropertyCalculated.get(index));
      }
      else {
        // any of the operators
        element.setField(ConfigTag.operator.toString(), typeKey);
      }
      calculatedList.add(element);
    }
    return calculatedList;
  }

  /**
   * For each role create copy of given DTO and set roleId in it.
   * 
   * @param mainPermissionDTO
   * @param roleIds
   * @param entities
   * @throws CSFormatException
   */
  private void addRoleIds(IConfigPermissionDTO mainPermissionDTO, List<?> roleIds, List<String> entities) throws CSFormatException
  {
    for (Object roleId : roleIds) {
      mainPermissionDTO.setRoleId((String)roleId);
      entities.add(mainPermissionDTO.toPXON());
    }
  }
  
  /**
   * @param entityList
   * @return
   */
  private Map<String, List<Map<String, Object>>> prepareKlassCodeVsPermissionEntityMap(List<Map<String, Object>> entityList)
  {
    Map<String, List<Map<String, Object>>> klassCodeVsPermissions = new HashMap<>();
    for (Map<String, Object> entity : entityList) {
      try {
        String klassCode = (String) entity.get(KLASS_CODE);
        if (!klassCodeVsPermissions.containsKey(klassCode)) {
          List<Map<String, Object>> permissions = new ArrayList<>();
          permissions.add(entity);
          klassCodeVsPermissions.put(klassCode, permissions);
          
        }
        else {
          klassCodeVsPermissions.get(klassCode).add(entity);
        }
      }
      catch (Exception e) {
        // need to log
      }
    }
    
    return klassCodeVsPermissions;
  }
}