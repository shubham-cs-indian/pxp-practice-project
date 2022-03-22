   package com.cs.di.workflow.tasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.cs.config.dto.ConfigAttributeDTO;
import com.cs.config.dto.ConfigClassifierDTO;
import com.cs.config.dto.ConfigContextDTO;
import com.cs.config.dto.ConfigDataRuleDTO;
import com.cs.config.dto.ConfigGoldenRecordRuleDTO;
import com.cs.config.dto.ConfigLanguageDTO;
import com.cs.config.dto.ConfigOrganizationDTO;
import com.cs.config.dto.ConfigPermissionDTO;
import com.cs.config.dto.ConfigPropertyCollectionDTO;
import com.cs.config.dto.ConfigRelationshipDTO;
import com.cs.config.dto.ConfigTabDTO;
import com.cs.config.dto.ConfigTagDTO;
import com.cs.config.dto.ConfigTaskDTO;
import com.cs.config.dto.ConfigTranslationDTO;
import com.cs.config.dto.ConfigUserDTO;
import com.cs.config.idto.IConfigDataRuleIntermediateEntitysDTO;
import com.cs.config.idto.IConfigDataRuleTagRuleDTO;
import com.cs.config.idto.IConfigDataRuleTagsDTO;
import com.cs.config.idto.IConfigGlobalPermissionDTO;
import com.cs.config.idto.IConfigHeaderPermisionDTO;
import com.cs.config.idto.IConfigLanguageDTO;
import com.cs.config.idto.IConfigMergeEffectTypeDTO;
import com.cs.config.idto.IConfigNormalizationDTO;
import com.cs.config.idto.IConfigPropertyPermissionDTO;
import com.cs.config.idto.IConfigRelationshipPermissionDTO;
import com.cs.config.idto.IConfigRoleDTO;
import com.cs.config.idto.IConfigRuleEntityDTO;
import com.cs.config.idto.IConfigSectionElementDTO;
import com.cs.config.idto.IConfigSideRelationshipDTO;
import com.cs.config.idto.IConfigTabDTO;
import com.cs.config.idto.IConfigTagValueDTO;
import com.cs.config.standard.IConfigClass;
import com.cs.config.standard.IStandardConfig.TagType;
import com.cs.constants.CommonConstants;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.rdbms.config.dto.RootConfigDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.klassinstance.IWriteInstancesToXLSXFileModel;
import com.cs.core.runtime.interactor.model.klassinstanceexport.WriteInstancesToXLSXFileModel;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.definition.ICSEObject;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.di.runtime.utils.DiFileUtils;
import com.cs.di.runtime.utils.DiTransformationUtils;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.DiConstants;
import com.cs.di.workflow.constants.ExportTypeEnum;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;

/**
 * Conversion from PXON to Excel for Config data Outbound/Export Transformation
 * 
 * @author Mayuri.Wankhade
 *
 */
@Component("configPXONToExcelTask")
@SuppressWarnings("unchecked")
public class ConfigPXONToExcelTask extends AbstractConfigFromPXONTask {
  
  private String                         SEPARATOR          = ";";
  public static final List<WorkflowType> WORKFLOW_TYPES     = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  public static final List<EventType>    EVENT_TYPES        = Arrays.asList(EventType.INTEGRATION);
  public static final List<String>       INPUT_LIST         = Arrays.asList(PXON_FILE_PATH);
  public static final List<String>       OUTPUT_LIST        = Arrays.asList(TRANSFORMED_DATA, EXECUTION_STATUS, FAILED_FILES,EXPORTED_TYPE);
  protected int                            propCollectinCount = 1;
  protected int                            variantCount       = 1;
  protected int                            relationshipCount  = 1;
  protected String                         parentCode         = "-1";
  private String                           VALUE                = "VALUE";
  @Override
  public List<String> getInputList() {
    return INPUT_LIST;
  }

  @Override
  public List<String> getOutputList() {
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
    
    String path = (String) DiValidationUtil.validateAndGetRequiredFilePath(workflowTaskModel, PXON_FILE_PATH);
    List<String> failedfiles = new ArrayList<>();
    int count =0;
    String specificationType = null;
    ICSEElement.CSEObjectType cseObjectType = null;
    ICSEObject cseObject;
    try {
      if (workflowTaskModel.getExecutionStatusTable().isErrorOccurred()) {
        return;
      }
      @SuppressWarnings("resource")
      PXONFileParser pxonFileParser = new PXONFileParser(path);
      PXONFileParser.PXONBlock blockInfo = null;
      if (!workflowTaskModel.getExecutionStatusTable().isErrorOccurred()) {
      while ((blockInfo = pxonFileParser.getNextBlock()) != null) {   
          cseObject = parseCSEElement(blockInfo.getData());
          specificationType = cseObject.getSpecification(ICSEElement.Keyword.$type);
          cseObjectType = cseObject.getObjectType();   
         switch (cseObjectType) {
           
            case Classifier:
              ConfigClassifierDTO configClassDTO = new ConfigClassifierDTO();
              configClassDTO.fromPXON(blockInfo.getData());              
              
              switch (IClassifierDTO.ClassifierType.valueOf(specificationType)) {                
                case CLASS:
                  if (IConfigClass.ClassifierClass.PROJECT_KLASS_TYPE.name().equals(configClassDTO.getSubType())) {
                    generateConfigArticleExcel(transformedExcelMap, configClassDTO, workflowTaskModel);
                  }
                  else if (IConfigClass.ClassifierClass.ASSET_KLASS_TYPE.name().equals(configClassDTO.getSubType())) {
                    generateConfigAssetExcel(transformedExcelMap, configClassDTO, workflowTaskModel);
                  }
                  else if (IConfigClass.ClassifierClass.TEXT_ASSET_KLASS_TYPE.name().equals(configClassDTO.getSubType())) {
                    generateConfigTextAssetExcel(transformedExcelMap, configClassDTO, workflowTaskModel);
                  }
                  else if (IConfigClass.ClassifierClass.SUPPLIER_KLASS_TYPE.name().equals(configClassDTO.getSubType())) {
                    generateConfigSupplierExcel(transformedExcelMap, configClassDTO, workflowTaskModel);
                  }
                  else if (IConfigClass.ClassifierClass.VIRTUAL_CATALOG_KLASS_TYPE.name().equals(configClassDTO.getSubType())) {
                    generateConfigVirtualCatlogExcel(transformedExcelMap, configClassDTO, workflowTaskModel);
                  }
                  break;      
                 //TODO: PXPFDEV-21215: Deprecate - Taxonomy Hierarchies  
                /*case HIERARCHY:
                  generateConfigHierarchyTaxonomyExcel(transformedExcelMap, configClassDTO, workflowTaskModel);
                  break;*/                
           
                case TAXONOMY:
                  generateConfigMasterTaxonomyExcel(transformedExcelMap, configClassDTO, workflowTaskModel);
                  break;
                default:
                  break;
              }
              break;
            
            case Property:
              PropertyType propertyType = IPropertyDTO.PropertyType.valueOf(specificationType);
              IPropertyDTO.SuperType propertySuperType = propertyType.getSuperType();
              switch(propertySuperType) {
                case ATTRIBUTE:
                  //Attribute
                  ConfigAttributeDTO attributeDTO = new ConfigAttributeDTO();
                  attributeDTO.fromPXON(blockInfo.getData());
                  generateConfigAttributeExcel(transformedExcelMap, attributeDTO, workflowTaskModel);
                  break;
                case RELATION_SIDE:
               /*   if (propertyType.ordinal() == IPropertyDTO.PropertyType.REFERENCE.ordinal()) {
                    ConfigReferenceDTO configReferenceDTO = new ConfigReferenceDTO();
                    configReferenceDTO.fromPXON(blockInfo.getData());
                    generateConfigReferenceExcel(transformedExcelMap, configReferenceDTO, workflowTaskModel);
                  }
                  else {*/
                    // Relationship
                    ConfigRelationshipDTO configRelationshipDTO = new ConfigRelationshipDTO();
                    configRelationshipDTO.fromPXON(blockInfo.getData());
                    generateConfigRelationshipExcel(transformedExcelMap, configRelationshipDTO, workflowTaskModel);
                 // }
                  break;
                case TAGS:
                  ConfigTagDTO tagDTO = new ConfigTagDTO();
                  tagDTO.fromPXON(blockInfo.getData());
                  generateConfigTagExcel(transformedExcelMap, tagDTO, workflowTaskModel);
                  break;
                default:
                  break;                
              }
              break;              
            
            case PropertyCollection:
              ConfigPropertyCollectionDTO configPropertyCollDTO = new ConfigPropertyCollectionDTO();
              configPropertyCollDTO.fromPXON(blockInfo.getData());
              generateConfigPropertyCollectionExcel(transformedExcelMap, configPropertyCollDTO, workflowTaskModel);
              break;
              
            case Context:
              ConfigContextDTO configContextDTO = new ConfigContextDTO(); 
              configContextDTO.fromPXON(blockInfo.getData());
              generateConfigContextExcel(transformedExcelMap, configContextDTO, workflowTaskModel);
              break;
            
            case User:
              ConfigUserDTO configUserDTO = new ConfigUserDTO();
              configUserDTO.fromPXON(blockInfo.getData());
              generateConfigUserExcel(transformedExcelMap, configUserDTO, workflowTaskModel);
              break;
              
            case Task:
              ConfigTaskDTO configTaskDTO = new ConfigTaskDTO();
              configTaskDTO.fromPXON(blockInfo.getData());
              generateConfigTaskExcel(transformedExcelMap, configTaskDTO, workflowTaskModel);
              break;

            case Golden_Rule:
              ConfigGoldenRecordRuleDTO configGoldenRecordRuleDTO = new ConfigGoldenRecordRuleDTO();
              configGoldenRecordRuleDTO.fromPXON(blockInfo.getData());
              generateConfigGoldenRecordRuleExcel(transformedExcelMap, configGoldenRecordRuleDTO, workflowTaskModel);
              break;
              
            case Rule:
              ConfigDataRuleDTO dataRuleDTO = new ConfigDataRuleDTO();
              dataRuleDTO.fromPXON(blockInfo.getData());
              generateConfigDataRuleExcel(transformedExcelMap, dataRuleDTO, workflowTaskModel);
              break;
              
            case Organization:
              ConfigOrganizationDTO configOrganizationDTO = new ConfigOrganizationDTO();
              configOrganizationDTO.fromPXON(blockInfo.getData());
              generateConfigOrganizationExcel(transformedExcelMap, configOrganizationDTO, workflowTaskModel);
              break;
              
            case Tab:
              IConfigTabDTO tabDTO = new ConfigTabDTO();
              tabDTO.fromPXON(blockInfo.getData());
              generateConfigTabExcel(transformedExcelMap, tabDTO, workflowTaskModel);
              break;

            case Translation:
              ConfigTranslationDTO configTranslationDTO = new ConfigTranslationDTO();
              configTranslationDTO.fromPXON(blockInfo.getData());
              generateConfigTranslationExcel(transformedExcelMap, configTranslationDTO, workflowTaskModel);
              break;
              
            case LanguageConf:
              IConfigLanguageDTO configLanguageDTO = new ConfigLanguageDTO();
              configLanguageDTO.fromPXON(blockInfo.getData());
              generateConfigLanguageExcel(transformedExcelMap, configLanguageDTO, workflowTaskModel);
              break;
              
            case Permission:
              ConfigPermissionDTO  configPermissionDTO= new ConfigPermissionDTO();
              configPermissionDTO.fromPXON(blockInfo.getData());
              generateConfigPermissionExcel(transformedExcelMap, configPermissionDTO, workflowTaskModel);
              break;
              
            default:
               throw new CSInitializationException("Config Type "+cseObjectType.toString());
              
          }
        }
      }
      workflowTaskModel.getOutputParameters().put(FAILED_FILES, failedfiles);
      writeIntoExcel(workflowTaskModel, exportedExcelFilePath, transformedExcelMap);
      Path fileLocation = Paths.get(exportedExcelFilePath);
      workflowTaskModel.getOutputParameters().put(TRANSFORMED_DATA, Base64.getEncoder().encodeToString(Files.readAllBytes(fileLocation)));
      workflowTaskModel.getOutputParameters().put(EXPORTED_TYPE,ExportTypeEnum.ENTITY.getExportType());
      DiFileUtils.deleteDirectory(Paths.get(processingFolder), workflowTaskModel.getExecutionStatusTable());
    }
    catch (CSInitializationException e) {
      workflowTaskModel.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN038,
          new String[] { e.getMessage() });
    }
    catch (IllegalArgumentException e) {
      workflowTaskModel.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN037,
          new String[] { e.getMessage() });
    }
    catch (IOException e) {
      workflowTaskModel.getExecutionStatusTable().addError(MessageCode.GEN028);
    }
    catch (Exception e) {
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
      failedfiles.add(path);
      workflowTaskModel.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN009,
          new String[] { path });
    }
  }
  
  /**
   * 
   * @param transformedExcelMap
   * @param configPermissionDTO
   * @param workflowTaskModel
   */
  private void generateConfigPermissionExcel(Map<String, Object> transformedExcelMap,
      ConfigPermissionDTO configPermissionDTO, WorkflowTaskModel workflowTaskModel)
  {
    generateMainPermissionSheet(transformedExcelMap,  configPermissionDTO
        .getGlobalPermission(), configPermissionDTO.getPermissionType());
    
    generateHeaderPermissionSheet(transformedExcelMap, configPermissionDTO.getHeaderPermission());
    
    generatePropertyPermissionSheet(transformedExcelMap, configPermissionDTO
        .getPropertyPermission());
    
    generateRelationshipPermissionSheet(transformedExcelMap, configPermissionDTO
        .getRelationshipPermission());
  
  }

  /**
   * 
   * @param transformedExcelMap
   * @param propertyPermissionDTOs
   */
  private void generatePropertyPermissionSheet(Map<String, Object> transformedExcelMap,
      List<IConfigPropertyPermissionDTO> propertyPermissionDTOs)
  {
    if(transformedExcelMap.get(PROPERTY_PERMISSIONS_SHEET_NAME) == null) {
      preparePropertyPermissionSheet(transformedExcelMap);
    }
    
    Map<String, Object> propertyPermissionSheet = (Map<String, Object>) transformedExcelMap
        .get(PROPERTY_PERMISSIONS_SHEET_NAME);
    List<Map<String, String>> entityPropertyPermission = (List<Map<String, String>>) propertyPermissionSheet.get(DATA);
    
    for(IConfigPropertyPermissionDTO  propertyPermissionDTO: propertyPermissionDTOs) {
      
      Map<String, String> transformedPropertyPermissionMap = new LinkedHashMap<>();
      
      transformedPropertyPermissionMap.put(KLASS_CODE, propertyPermissionDTO.getEntityId());
      transformedPropertyPermissionMap.put(ATTRIBUTE_TAG_CODE, propertyPermissionDTO.getPropertyId());
      transformedPropertyPermissionMap.put(VISIBLE, propertyPermissionDTO.getIsVisible() ? "1" : "0");
      transformedPropertyPermissionMap.put(CAN_EDIT, propertyPermissionDTO.getCanEdit() ? "1" : "0");
      transformedPropertyPermissionMap.put(PROPERTY_TYPE, propertyPermissionDTO.getPropertyType());
      
      entityPropertyPermission.add(transformedPropertyPermissionMap);
      
    }
  }

  /**
   * 
   * @param transformedExcelMap
   * @param configRelationshipPermissionDTOs
   */
  private void generateRelationshipPermissionSheet(Map<String, Object> transformedExcelMap,
      List<IConfigRelationshipPermissionDTO> configRelationshipPermissionDTOs)
  {
    if(transformedExcelMap.get(RELATIONSHIP_PERMISSIONS_SHEET_NAME) == null) {
      prepareRelationShipPermissionSheet(transformedExcelMap);
    }
    
    Map<String, Object> relationshipPermissionSheet = (Map<String, Object>) transformedExcelMap
        .get(RELATIONSHIP_PERMISSIONS_SHEET_NAME);
    List<Map<String, String>> entityRelationshipPermission = (List<Map<String, String>>) relationshipPermissionSheet.get(DATA);
    
    for(IConfigRelationshipPermissionDTO  configRelationshipPermissionDTO: configRelationshipPermissionDTOs) {
      
      Map<String, String> transformedRelationshipPermissionMap = new LinkedHashMap<>();
      
      transformedRelationshipPermissionMap.put(KLASS_CODE, configRelationshipPermissionDTO.getEntityId());
      transformedRelationshipPermissionMap.put(PERMISSION_RELATIONSHIPS_COLOUMN, configRelationshipPermissionDTO.getRelationshipId());
      transformedRelationshipPermissionMap.put(VISIBLE, configRelationshipPermissionDTO.getIsVisible() ? "1" : "0");
      transformedRelationshipPermissionMap.put(CAN_ADD, configRelationshipPermissionDTO.getCanAdd() ? "1" : "0");
      transformedRelationshipPermissionMap.put(CAN_REMOVE, configRelationshipPermissionDTO.getCanDelete() ? "1" : "0");
      
      entityRelationshipPermission.add(transformedRelationshipPermissionMap);
      
    }
  }
 
/**
 * 
 * @param transformedExcelMap
 * @param configHeaderPermisionDTO
 */
  private void generateHeaderPermissionSheet(Map<String, Object> transformedExcelMap,
       IConfigHeaderPermisionDTO configHeaderPermisionDTO)
  {
    
    if (configHeaderPermisionDTO.getEntityId() == null || configHeaderPermisionDTO.getEntityId()
        .isEmpty()) {
      return;
    }
    if(transformedExcelMap.get(GENERALI_NFORMATION_PERMISSIONS_SHEET_NAME) == null) {
      prepareGeneralInfoPermissionSheet(transformedExcelMap);
    }
    
    Map<String, Object> generalInfoPermissionSheet = (Map<String, Object>) transformedExcelMap.get(GENERALI_NFORMATION_PERMISSIONS_SHEET_NAME);
    List<Map<String, String>> entityListGeneralInfoPermission = (List<Map<String, String>>) generalInfoPermissionSheet.get(DATA);
    
    Map<String, String> imageGeneralInfoMap = new LinkedHashMap<>();
    imageGeneralInfoMap.put(KLASS_CODE, configHeaderPermisionDTO.getEntityId());
    imageGeneralInfoMap.put(GENERAL_INFO_LABEL, GENERAL_INFO_IMAGE);
    imageGeneralInfoMap.put(VISIBLE, configHeaderPermisionDTO.getViewIcon() ? "1" : "0");
    imageGeneralInfoMap.put(CAN_EDIT, configHeaderPermisionDTO.getCanChangeIcon() ? "1" : "0");
    entityListGeneralInfoPermission.add(imageGeneralInfoMap);
   
    Map<String, String> nameGeneralInfoMap = new LinkedHashMap<>();
    nameGeneralInfoMap.put(KLASS_CODE, configHeaderPermisionDTO.getEntityId());
    nameGeneralInfoMap.put(GENERAL_INFO_LABEL, GENERAL_INFO_NAME);
    nameGeneralInfoMap.put(CAN_EDIT, configHeaderPermisionDTO.getCanEditName()? "1" : "0");
    nameGeneralInfoMap.put(VISIBLE, configHeaderPermisionDTO.getViewName()? "1" : "0");
    entityListGeneralInfoPermission.add(nameGeneralInfoMap);
    
    Map<String, String> natureTypeGeneralInfoMap = new LinkedHashMap<>();
    natureTypeGeneralInfoMap.put(KLASS_CODE, configHeaderPermisionDTO.getEntityId());
    natureTypeGeneralInfoMap.put(GENERAL_INFO_LABEL,GENERAL_INFO_NATURE_TYPE);
    natureTypeGeneralInfoMap.put(VISIBLE, configHeaderPermisionDTO.getViewPrimaryType()? "1" : "0");
        entityListGeneralInfoPermission.add(natureTypeGeneralInfoMap);
    
    Map<String, String> classesGeneralInfoMap = new LinkedHashMap<>();
    classesGeneralInfoMap.put(KLASS_CODE, configHeaderPermisionDTO.getEntityId());
    classesGeneralInfoMap.put(GENERAL_INFO_LABEL, GENERAL_INFO_ADDITIONAL_CLASSES);
    classesGeneralInfoMap.put(VISIBLE, configHeaderPermisionDTO.getViewAdditionalClasses()? "1" : "0");
    classesGeneralInfoMap.put(CAN_ADD, configHeaderPermisionDTO.getCanAddClasses()? "1" : "0");
    classesGeneralInfoMap.put(CAN_REMOVE, configHeaderPermisionDTO.getCanDeleteClasses()? "1" : "0");
    entityListGeneralInfoPermission.add(classesGeneralInfoMap);
    
    Map<String, String> taxonomiesGeneralInfoMap = new LinkedHashMap<>();
    taxonomiesGeneralInfoMap.put(KLASS_CODE, configHeaderPermisionDTO.getEntityId());
    taxonomiesGeneralInfoMap.put(GENERAL_INFO_LABEL,GENERAL_INFO_TAXONOMIES);
    taxonomiesGeneralInfoMap.put(VISIBLE, configHeaderPermisionDTO.getViewTaxonomies()? "1" : "0");
    taxonomiesGeneralInfoMap.put(CAN_ADD, configHeaderPermisionDTO.getCanAddTaxonomy()? "1" : "0");
    taxonomiesGeneralInfoMap.put(CAN_REMOVE, configHeaderPermisionDTO.getCanDeleteTaxonomy()? "1" : "0");
    entityListGeneralInfoPermission.add(taxonomiesGeneralInfoMap);
    
    Map<String, String> statusTagGeneralInfoMap = new LinkedHashMap<>();
    statusTagGeneralInfoMap.put(KLASS_CODE, configHeaderPermisionDTO.getEntityId());
    statusTagGeneralInfoMap.put(GENERAL_INFO_LABEL, GENERAL_INFO_LIFECYCLE_STATUS_TAGS);
    statusTagGeneralInfoMap.put(VISIBLE, configHeaderPermisionDTO.getViewStatusTags()? "1" : "0");
    statusTagGeneralInfoMap.put(CAN_EDIT, configHeaderPermisionDTO.getCanEditStatusTag()? "1" : "0");
    entityListGeneralInfoPermission.add(statusTagGeneralInfoMap);
    
    Map<String, String> lastCreeateAndModifiedGeneralInfoMap = new LinkedHashMap<>();
    lastCreeateAndModifiedGeneralInfoMap.put(KLASS_CODE, configHeaderPermisionDTO.getEntityId());
    lastCreeateAndModifiedGeneralInfoMap.put(GENERAL_INFO_LABEL, GENERAL_INFO_CREATED_AND_LAST_MODIFIED_INFO);
    lastCreeateAndModifiedGeneralInfoMap.put(VISIBLE, configHeaderPermisionDTO.getViewCreatedOn()
        & configHeaderPermisionDTO.getViewLastModifiedBy() ? "1" : "0");
    entityListGeneralInfoPermission.add(lastCreeateAndModifiedGeneralInfoMap);
  }

  /**
   * 
   * @param transformedExcelMap
   * @param configGlobalPermissionDTO
   * @param permissionType
   */
  private void generateMainPermissionSheet(Map<String, Object> transformedExcelMap,
      IConfigGlobalPermissionDTO configGlobalPermissionDTO, String permissionType)
  {
    if (transformedExcelMap.get(MAIN_PERMISSIONS_SHEET_NAME) == null) {
      prepareMainPermissionSheet(transformedExcelMap);
    }
    Map<String, String> transformedMainPermissionMap = new LinkedHashMap<>();
    Map<String, Object> mainPermissionSheet = (Map<String, Object>) transformedExcelMap
        .get(MAIN_PERMISSIONS_SHEET_NAME);
    List<Map<String, String>> entityListMainPermission = (List<Map<String, String>>) mainPermissionSheet
        .get(DATA);
    transformedMainPermissionMap.put(PERMISSION_TYPE, permissionType);
    transformedMainPermissionMap.put(KLASS_CODE, configGlobalPermissionDTO.getEntityId());
    
    transformedMainPermissionMap.put(PERMISSION_CREATE, configGlobalPermissionDTO.getCanCreate() ? "1" : "0");
    transformedMainPermissionMap.put(PERMISSION_DELETE, configGlobalPermissionDTO.getCanDelete() ? "1" : "0");
    
    if (permissionType.equals(ASSET_KEY)) {
      transformedMainPermissionMap.put(PERMISSION_DOWNLOAD, configGlobalPermissionDTO.getCanDownload() ? "1" : "0");
    }
    
    if (permissionType.equals(TASK_KEY)) {
      transformedMainPermissionMap.put(PERMISSION_READ, configGlobalPermissionDTO.getCanRead() ? "1" : "0");
      transformedMainPermissionMap.put(PERMISSION_UPDATE, configGlobalPermissionDTO.getCanEdit() ? "1" : "0");
    }
    
    entityListMainPermission.add(transformedMainPermissionMap);
  }

  /**
   * generate Config Tab Excel
   * @param transformedExcelMap
   * @param tabDTO
   * @param workflowTaskModel
   */
  private void generateConfigTabExcel(Map<String, Object> transformedExcelMap, IConfigTabDTO tabDTO,
      WorkflowTaskModel workflowTaskModel)
  {
    if (transformedExcelMap.get(TAB_SHEET_NAME) == null) {
      prepareTabSheet(transformedExcelMap);
    }
    
    Map<String, Object> tabSheet = (Map<String, Object>) transformedExcelMap.get(TAB_SHEET_NAME);
    List<Map<String, String>> tabList = (List<Map<String, String>>) tabSheet.get(DATA);
    Map<String, String> transformedTabMap = new LinkedHashMap<>();
    transformedTabMap.put(CODE_COLUMN_CONFIG, tabDTO.getCode());
    transformedTabMap.put(LABEL_COLUMN_CONFIG, tabDTO.getLabel());
    transformedTabMap.put(ICON_COLUMN, tabDTO.getIcon());
    transformedTabMap.put(SEQUENCE, String.valueOf(tabDTO.getSequence()));
    transformedTabMap.put(PROPERTY_COLLECTION_SHEET_NAME, String.join(SEPARATOR, tabDTO.getPropertySequenceList()));
    tabList.add(transformedTabMap);
  }
    
  /**
   * prepare Tab Sheet
   * 
   * @param transformedExcelMap
   */
  private void prepareTabSheet(Map<String, Object> transformedExcelMap)
  {
    LinkedHashSet<String> tabHeaders = new LinkedHashSet<>();
    tabHeaders.add(CODE_COLUMN_CONFIG);
    tabHeaders.add(LABEL_COLUMN_CONFIG);
    tabHeaders.add(ICON_COLUMN);
    tabHeaders.add(SEQUENCE);
    tabHeaders.add(PROPERTY_COLLECTION_SHEET_NAME);
    
    transformedExcelMap.put(TAB_SHEET_NAME, new HashMap<>());
    Map<String, Object> tabMap = (Map<String, Object>) transformedExcelMap.get(TAB_SHEET_NAME);
    tabMap.put(HEADERS, tabHeaders);
    tabMap.put(DATA, new ArrayList<>());
    
  }

  /**
   * generate excel for Data Rules
   * @param transformedExcelMap
   * @param dataRuleDTO
   * @param workflowTaskModel
   * @throws CSFormatException 
   */
  private void generateConfigDataRuleExcel(Map<String, Object> transformedExcelMap,
      ConfigDataRuleDTO dataRuleDTO, WorkflowTaskModel workflowTaskModel) throws CSFormatException
  {
    if (transformedExcelMap.get(DATA_RULES_SHEET_NAME) == null) {
      prepareRuleSheet(transformedExcelMap);
    }
    
    Map<String, Object> ruleSheet = (Map<String, Object>) transformedExcelMap.get(DATA_RULES_SHEET_NAME);
    List<Map<String, String>> ruleList = (List<Map<String, String>>) ruleSheet.get(DATA);
    Map<String, String> transformedRuleMap = new LinkedHashMap<>();
    ruleList.add(transformedRuleMap);
    transformedRuleMap.put(CODE_COLUMN_CONFIG, dataRuleDTO.getCode());
    transformedRuleMap.put(LABEL_COLUMN_CONFIG, dataRuleDTO.getLabel());
    transformedRuleMap.put(TYPE_COLUMN, dataRuleDTO.getType());
    transformedRuleMap.put(LANGUAGE_DEPENDENT_COLUMN, dataRuleDTO.getIsLanguageDependent() ? "1" : "0");
    transformedRuleMap.put(LANGUAGES, String.join(SEPARATOR, dataRuleDTO.getLanguages()));
    transformedRuleMap.put(PARTNERS, String.join(SEPARATOR, dataRuleDTO.getOrganizations()));
    transformedRuleMap.put(PHYSICALCATALOGS, String.join(SEPARATOR, dataRuleDTO.getPhysicalCatalogIds()));
    transformedRuleMap.put(IS_STANDARD, dataRuleDTO.getIsStandard() ? "1" : "0");
    List<IConfigNormalizationDTO> iConfigNormalizationDTOList = dataRuleDTO.getNormalizations();
    List<IConfigNormalizationDTO> iConfigNormalizationDTOUpdatedList = new ArrayList<IConfigNormalizationDTO>();
  
    switch (dataRuleDTO.getType()) {
      
      case TYPE_CLASSIFICATION:
        transformedRuleMap.put(CLASSIFICATION_CAUSE_KLASSES, String.join(SEPARATOR, dataRuleDTO.getTypes()));
        transformedRuleMap.put(CLASSIFICATION_CAUSE_TAXONOMIES, String.join(SEPARATOR, dataRuleDTO.getTaxonomies()));
       
        for (IConfigNormalizationDTO iConfigNormalizationDTO : iConfigNormalizationDTOList) {
          if (iConfigNormalizationDTO.getType().equals(KLASS_TYPE)) {
            transformedRuleMap.put(CLASSIFICATION_EFFECT_KLASSES, String.join(SEPARATOR, iConfigNormalizationDTO.getValues()));
          }
          else if (iConfigNormalizationDTO.getType().equals(TAXONOMY)) {
            transformedRuleMap.put(CLASSIFICATION_EFFECT_TAXONOMIES, String.join(SEPARATOR, iConfigNormalizationDTO.getValues()));
          }
        }
        break;
      case TYPE_SAN:
        transformedRuleMap.put(CAUSE_KLASSES, String.join(SEPARATOR, dataRuleDTO.getTypes()));
        transformedRuleMap.put(CAUSE_TAXONOMIES, String.join(SEPARATOR, dataRuleDTO.getTaxonomies()));
        
        iConfigNormalizationDTOUpdatedList.addAll(iConfigNormalizationDTOList);
        for (IConfigNormalizationDTO iConfigNormalizationDTO : iConfigNormalizationDTOUpdatedList) {
          if (iConfigNormalizationDTO.getType().equals(KLASS_TYPE)) {
            transformedRuleMap.put(SAN_EFFECT_KLASSES, String.join(SEPARATOR, iConfigNormalizationDTO.getValues()));
          }
          else if (iConfigNormalizationDTO.getType().equals(TAXONOMY)) {
            transformedRuleMap.put(SAN_EFFECT_TAXONOMIES, String.join(SEPARATOR, iConfigNormalizationDTO.getValues()));
          } 
          if(iConfigNormalizationDTO.getType().equals(KLASS_TYPE)||iConfigNormalizationDTO.getType().equals(TAXONOMY)) {
            iConfigNormalizationDTOList.remove(iConfigNormalizationDTO);
          }  
        }
        fillEffectAndCauseForDataRules(dataRuleDTO, ruleList, transformedRuleMap);
        break;
      case TYPE_VIO:
        transformedRuleMap.put(CAUSE_KLASSES, String.join(SEPARATOR, dataRuleDTO.getTypes()));
        transformedRuleMap.put(CAUSE_TAXONOMIES, String.join(SEPARATOR, dataRuleDTO.getTaxonomies()));
        fillEffectAndCauseForDataRules(dataRuleDTO, ruleList, transformedRuleMap);
        break;
      default:
        break;
    }
  }

  private void fillEffectAndCauseForDataRules(ConfigDataRuleDTO dataRuleDTO,
      List<Map<String, String>> ruleList, Map<String, String> transformedRuleMap)
  {
    //SAN and VIO Cause side
    Iterator<IConfigDataRuleIntermediateEntitysDTO> attributesIterator = dataRuleDTO.getAttributes().iterator();
    Iterator<IConfigDataRuleTagsDTO> tagsIterator = dataRuleDTO.getTags().iterator();
    //VIO Effect Side
    Iterator<IJSONContent> ViolationsIterator = dataRuleDTO.getRuleViolations().iterator();
    //SAN Effect Side
     Iterator<IConfigNormalizationDTO> normalizationsIterator = dataRuleDTO.getNormalizations().iterator();
    if(!attributesIterator.hasNext() && !tagsIterator.hasNext() && !normalizationsIterator.hasNext() && !ViolationsIterator.hasNext()) {
      ruleList.add(transformedRuleMap);
    }
    Boolean isFirst = true;
    Map<String,String> causeEffectTransformedRuleMap;
    
    while (attributesIterator.hasNext() || tagsIterator.hasNext()
        || normalizationsIterator.hasNext() || ViolationsIterator.hasNext()) {
      
      if (attributesIterator.hasNext() && isFirst) {
        checkAndFillCauseForAttributes(transformedRuleMap, attributesIterator);
        checkAndFillSANEffect(transformedRuleMap, normalizationsIterator);
        checkAndFillRuleViolations(transformedRuleMap, ViolationsIterator);
        isFirst = false;
      }
      else if (tagsIterator.hasNext() && isFirst) {
        checkAndFillCauseForTags(transformedRuleMap, tagsIterator);
        checkAndFillSANEffect(transformedRuleMap, normalizationsIterator);
        checkAndFillRuleViolations(transformedRuleMap, ViolationsIterator);
        isFirst = false;
      }
      else if (normalizationsIterator.hasNext() && isFirst) {
        checkAndFillSANEffect(transformedRuleMap, normalizationsIterator);
        isFirst = false;
      }
      else if (ViolationsIterator.hasNext() && isFirst) {
        checkAndFillRuleViolations(transformedRuleMap, ViolationsIterator);
        isFirst = false;
      }
      else if (attributesIterator.hasNext()) {
        causeEffectTransformedRuleMap = new HashMap<>();
        causeEffectTransformedRuleMap.put(CODE_COLUMN_CONFIG, dataRuleDTO.getCode());
        checkAndFillCauseForAttributes(causeEffectTransformedRuleMap, attributesIterator);
        if (normalizationsIterator.hasNext() || ViolationsIterator.hasNext()) {
          checkAndFillSANEffect(causeEffectTransformedRuleMap, normalizationsIterator);
          checkAndFillRuleViolations(causeEffectTransformedRuleMap, ViolationsIterator);
        }
        ruleList.add(causeEffectTransformedRuleMap);
      }
      else if (tagsIterator.hasNext()) 
      {
        causeEffectTransformedRuleMap = new HashMap<>();
        causeEffectTransformedRuleMap.put(CODE_COLUMN_CONFIG, dataRuleDTO.getCode());
        checkAndFillCauseForTags(causeEffectTransformedRuleMap, tagsIterator);
        if (normalizationsIterator.hasNext() || ViolationsIterator.hasNext()) {
          checkAndFillSANEffect(causeEffectTransformedRuleMap, normalizationsIterator);
          checkAndFillRuleViolations(causeEffectTransformedRuleMap, ViolationsIterator);
        }
        ruleList.add(causeEffectTransformedRuleMap);
      }
      else if (normalizationsIterator.hasNext()) {
        causeEffectTransformedRuleMap = new HashMap<>();
        causeEffectTransformedRuleMap.put(CODE_COLUMN_CONFIG, dataRuleDTO.getCode());
        checkAndFillSANEffect(causeEffectTransformedRuleMap, normalizationsIterator);
        if (ViolationsIterator.hasNext()) {
          checkAndFillRuleViolations(causeEffectTransformedRuleMap, ViolationsIterator);
        }
        ruleList.add(causeEffectTransformedRuleMap);
      }
      else if (ViolationsIterator.hasNext()) {
        causeEffectTransformedRuleMap = new HashMap<>();
        causeEffectTransformedRuleMap.put(CODE_COLUMN_CONFIG, dataRuleDTO.getCode());
        checkAndFillRuleViolations(transformedRuleMap, ViolationsIterator);
        if (normalizationsIterator.hasNext()) {
          checkAndFillSANEffect(causeEffectTransformedRuleMap, normalizationsIterator);
        }
        ruleList.add(causeEffectTransformedRuleMap);
      }
    }
  }
  
  
  /**
   * check And Fill RuleViolations
   * 
   * @param transformedRuleMap
   * @param ruleViolations
   */
  private void checkAndFillRuleViolations(Map<String, String> transformedRuleMap, Iterator<IJSONContent> ruleViolations)
  {
    JSONObject ruleViolation;
    if (ruleViolations.hasNext()) {
      ruleViolation = (JSONObject) ruleViolations.next();
      fillRuleViolation(transformedRuleMap, ruleViolation);
    }
  }
  
  /**
   * Fill Rule Violations in excel
   * @param transformedRuleMap
   * @param ruleViolation
   */
  private void fillRuleViolation(Map<String, String> transformedRuleMap, JSONObject ruleViolation)
  {
    // ruleViolation.getInitField(VERSION_ID, "");
    transformedRuleMap.put(EFFECT_ENTITY_CODE, (String) ruleViolation.get(ENTITY_ID));
    String entityType = (String) ruleViolation.get(ENTITY_TYPE);
    transformedRuleMap.put(EFFECT_ENTITY_TYPE, entityType);
    String entitySubType = transformedRuleMap.get(EFFECT_ENTITY_SUBTYPE);
    if (entitySubType == null) {
      transformedRuleMap.put(EFFECT_ENTITY_SUBTYPE,
          entityType.equals("attribute") ? IConfigClass.PropertyClass.valueOfClassPath((String) ruleViolation.get("entityType")).name()
              : (String) ruleViolation.get("entityAttributeType"));
    }
    transformedRuleMap.put(VIOLATION_EFFECT_COLOR, (String) ruleViolation.get(VIO_COLOR));
    transformedRuleMap.put(VIOLATION_EFFECT_DESCRIPTION, (String) ruleViolation.get(VIO_DESCRIPTION)); 
  }
  
  /**
   * check And Fill SAN Effect
   * @param transformedRuleMap
   * @param normIterator
   */
  private void checkAndFillSANEffect(Map<String, String> transformedRuleMap,
      Iterator<IConfigNormalizationDTO> normIterator)
  {
    IConfigNormalizationDTO configNormalizationDTO;
    if (normIterator.hasNext()) {
      configNormalizationDTO = normIterator.next();
      fillSANEffect(transformedRuleMap, configNormalizationDTO);
    }  
  }

  /**
   * fill Cause For Attribute
   * @param transformedRuleMap
   * @param iConfigDataRuleIntermediateEntitysDTO
   */
  private void fillCauseForAttribute(Map<String, String> transformedRuleMap,
      IConfigDataRuleIntermediateEntitysDTO iConfigDataRuleIntermediateEntitysDTO)
  {
    transformedRuleMap.put(CAUSE_ENTITY_CODE, iConfigDataRuleIntermediateEntitysDTO.getEntityId());
    transformedRuleMap.put(CAUSE_ENTITY_TYPE, ENTITY_ATTRIBUTE);
    String entitySubType = iConfigDataRuleIntermediateEntitysDTO.getEntityAttributeType();
    transformedRuleMap.put(CAUSE_ENTITY_SUBTYPE, entitySubType);
    
    List<String> compareTypesList = new ArrayList<String>();
    List<String> entityValueTypesList = new ArrayList<String>();
    List<String> entityValuesList = new ArrayList<String>();
    String from = "";
    String to = "";
    Boolean setShouldCompareWithSystemDate = false;
    
    for (IConfigRuleEntityDTO attributeRuleDTO : iConfigDataRuleIntermediateEntitysDTO.getRules()) {
      compareTypesList.add(attributeRuleDTO.getType());
      switch (attributeRuleDTO.getType()) {
        case COMPARE_TYPE_CONTAINS:
        case COMPARE_TYPE_START:
        case COMPARE_TYPE_END:
        case COMPARE_TYPE_LT:
        case COMPARE_TYPE_GT:
        case COMPARE_TYPE_EQUAL:
          if (attributeRuleDTO.getAttributeLinkId() != null
              && !attributeRuleDTO.getAttributeLinkId().isEmpty()) {
            entityValuesList.add(attributeRuleDTO.getAttributeLinkId());
            entityValueTypesList.add(ENTITY_ATTRIBUTE);
          }
          else {
            if (!attributeRuleDTO.getValues().isEmpty()) {
              entityValuesList.addAll(attributeRuleDTO.getValues());
            }
            else {
              entityValuesList.add("");
            }
            entityValueTypesList.add(VALUE);
          }
          break;
        case COMPARE_TYPE_EXACT:
          if (attributeRuleDTO.getAttributeLinkId() != null && !attributeRuleDTO.getAttributeLinkId().isEmpty()) {
            entityValuesList.add(attributeRuleDTO.getAttributeLinkId());
            entityValueTypesList.add(ENTITY_ATTRIBUTE);
          }
          else {
            //TODO TRANSFORMATION_TYPE_VALUE_DATE condition
            entityValuesList.addAll(attributeRuleDTO.getValues());
            entityValueTypesList.add(VALUE);
          }
          break;
        case COMPARE_TYPE_NOT_EMPTY:
        case COMPARE_TYPE_EMPTY:
          entityValuesList.add("");
          entityValueTypesList.add(VALUE);
          break;
        case COMPARE_TYPE_RANGE:
         from = attributeRuleDTO.getFrom();
         to= attributeRuleDTO.getTo();
         
          entityValuesList.add("");
          entityValueTypesList.add(VALUE);
          
          
          break;
        case COMPARE_TYPE_REGEX:
          entityValuesList.addAll(attributeRuleDTO.getValues());
          entityValueTypesList.add(VALUE);
          break;
        case COMPARE_TYPE_IS_DUPLICATE:
          attributeRuleDTO.getValues();
          entityValuesList.add(String.join("|", attributeRuleDTO.getKlassLinkIds()));
          break;
        case COMPARE_TYPE_DATE_LT:
        case COMPARE_TYPE_DATE_GT:
          
          if (attributeRuleDTO.getAttributeLinkId() != null
              && !attributeRuleDTO.getAttributeLinkId()
                  .isEmpty()) {
            entityValuesList.add(attributeRuleDTO.getAttributeLinkId());
            entityValueTypesList.add(ENTITY_ATTRIBUTE);
          }
          else {
            if (entitySubType.compareToIgnoreCase(DATE_ATTRIBUTE_TYPE)==0) {
              if (attributeRuleDTO.getAttributeLinkId()
                  .isEmpty()
                  && attributeRuleDTO.getValues()
                      .isEmpty()) {
                setShouldCompareWithSystemDate = true;
                entityValuesList.add("");
                entityValueTypesList.add(VALUE);
              }
              else {
                setShouldCompareWithSystemDate = false;
                long dateValue = Long.parseLong(attributeRuleDTO.getValues()
                    .get(0));
                entityValuesList.add(DiTransformationUtils.getTimeStampForFormat(
                    dateValue != 0 ? dateValue : null, DiConstants.DATE_FORMAT));
                entityValueTypesList.add(VALUE);
              }
            }
            else {
              if (!attributeRuleDTO.getValues()
                  .isEmpty()) {
                entityValuesList.addAll(attributeRuleDTO.getValues());
              }
              else {
                entityValuesList.add("");
              }
              entityValueTypesList.add(VALUE);
            }
          }
          break;
        case COMPARE_TYPE_DATE_RANGE:
          long fromValueDate = Long.parseLong(attributeRuleDTO.getFrom());
          long toValueDate = Long.parseLong(attributeRuleDTO.getTo());
           from =  DiTransformationUtils.getTimeStampForFormat(
               fromValueDate != 0 ? fromValueDate : null, DiConstants.DATE_FORMAT);
         
           to =  DiTransformationUtils.getTimeStampForFormat(
               toValueDate != 0 ? toValueDate : null, DiConstants.DATE_FORMAT);
          entityValuesList.add("");
          entityValueTypesList.add(VALUE);
          break;
      }
    }
    transformedRuleMap.put(CAUSE_COMPARE_TYPE, String.join(SEPARATOR, compareTypesList));
    transformedRuleMap.put(CAUSE_ENTITY_VALUE_TYPE, String.join(SEPARATOR, entityValueTypesList));
    transformedRuleMap.put(CAUSE_ENTITY_VALUE, String.join(SEPARATOR, entityValuesList));
    transformedRuleMap.put(CAUSE_FROM, from);
    transformedRuleMap.put(CAUSE_TO, to);
    transformedRuleMap.put(CAUSE_COMPARE_WITH_CURRENT_DATE, setShouldCompareWithSystemDate ? "1" : "0"); 
  }

  /**
   * check And Fill Cause For Attributes
   * @param transformedRuleMap
   * @param attributesIterator
   */
  private void checkAndFillCauseForAttributes(Map<String, String> transformedRuleMap,
      Iterator<IConfigDataRuleIntermediateEntitysDTO> attributesIterator)
  {
    IConfigDataRuleIntermediateEntitysDTO intermediateEntitysDTO;
    if (attributesIterator.hasNext()) {
      intermediateEntitysDTO = attributesIterator.next();
      fillCauseForAttribute(transformedRuleMap, intermediateEntitysDTO);
    }
  }
 
  /**
   * check And Fill Cause For Tags
   * @param transformedRuleMap
   * @param tagsIterator
   */
  private void checkAndFillCauseForTags(Map<String, String> transformedRuleMap,
      Iterator<IConfigDataRuleTagsDTO> tagsIterator)
  {
    IConfigDataRuleTagsDTO configDataRuleTagsDTO;
    if (tagsIterator.hasNext()) {
      configDataRuleTagsDTO = tagsIterator.next();
      fillCauseForTags(transformedRuleMap, configDataRuleTagsDTO);
    }
  }

  /**
   * fill Cause For Tags
   * @param transformedRuleMap
   * @param configDataRuleTagsDTO
   */
  private void fillCauseForTags(Map<String, String> transformedRuleMap,
      IConfigDataRuleTagsDTO configDataRuleTagsDTO)
  {
    transformedRuleMap.put(CAUSE_ENTITY_CODE, configDataRuleTagsDTO.getEntityId());
    transformedRuleMap.put(CAUSE_ENTITY_TYPE, ENTITY_TAG);
    transformedRuleMap.put(CAUSE_ENTITY_SUBTYPE, configDataRuleTagsDTO.getEntityAttributeType());
    
    List<String> compareTypesList = new ArrayList<String>();
    List<String> entityValueTypesList = new ArrayList<String>();
    List<String> entityValuesList = new ArrayList<String>();
    
    for (IConfigDataRuleTagRuleDTO iConfigDataRuleTagRuleDTO : configDataRuleTagsDTO.getRules()) {
      entityValueTypesList.add(ENTITY_TAG);
      compareTypesList.add(iConfigDataRuleTagRuleDTO.getType());
      List<IJSONContent> tagValues = iConfigDataRuleTagRuleDTO.getTagValues();
      
      if (tagValues.isEmpty()) {
        entityValuesList.add("");
      }
      else {
        tagValues.size();
        for (int i = 0; i < tagValues.size(); i++) {
          JSONObject tagValue = (JSONObject) tagValues.get(i);
          entityValuesList.add((String) tagValue.get(ID_COLUMN));
        }
      }
    }
    transformedRuleMap.put(CAUSE_COMPARE_TYPE, String.join(SEPARATOR, compareTypesList));
    transformedRuleMap.put(CAUSE_ENTITY_VALUE_TYPE, String.join(SEPARATOR, entityValueTypesList));
    transformedRuleMap.put(CAUSE_ENTITY_VALUE, String.join(SEPARATOR, entityValuesList));
  }
  
  private void prepareRuleSheet(Map<String, Object> ruleExcelMap)
  {
    LinkedHashSet<String> ruleHeader = new LinkedHashSet<>();
    ruleHeader.add(CODE_COLUMN_CONFIG);
    ruleHeader.add(LABEL_COLUMN_CONFIG);
    ruleHeader.add(TYPE_COLUMN);
    ruleHeader.add(LANGUAGE_DEPENDENT_COLUMN);
    ruleHeader.add(LANGUAGES);
    ruleHeader.add(PARTNERS);
    ruleHeader.add(PHYSICALCATALOGS);
    ruleHeader.add(CAUSE_ENTITY_CODE);
    ruleHeader.add(CAUSE_ENTITY_TYPE);
    ruleHeader.add(CAUSE_ENTITY_SUBTYPE);
    ruleHeader.add(CAUSE_COMPARE_TYPE);
    ruleHeader.add(CAUSE_ENTITY_VALUE_TYPE);
    ruleHeader.add(CAUSE_ENTITY_VALUE);
    ruleHeader.add(CAUSE_FROM);
    ruleHeader.add(CAUSE_TO);
    ruleHeader.add(CAUSE_COMPARE_WITH_CURRENT_DATE);
    ruleHeader.add(CAUSE_KLASSES);
    ruleHeader.add(CAUSE_TAXONOMIES);
    ruleHeader.add(EFFECT_ENTITY_CODE);
    ruleHeader.add(EFFECT_ENTITY_TYPE);
    ruleHeader.add(EFFECT_ENTITY_SUBTYPE);
    ruleHeader.add(SAN_EFFECT_TRANSFORMATION_TYPE);
    ruleHeader.add(SAN_EFFECT_ENTITY_VALUE);
    ruleHeader.add(SAN_EFFECT_ENTITY_SPECIAL_VALUE);
    ruleHeader.add(SAN_EFFECT_FROM);
    ruleHeader.add(SAN_EFFECT_TO);
    ruleHeader.add(SAN_EFFECT_KLASSES);
    ruleHeader.add(SAN_EFFECT_TAXONOMIES);
    ruleHeader.add(VIOLATION_EFFECT_COLOR);
    ruleHeader.add(VIOLATION_EFFECT_DESCRIPTION);
    ruleHeader.add(CLASSIFICATION_CAUSE_KLASSES);
    ruleHeader.add(CLASSIFICATION_CAUSE_TAXONOMIES);
    ruleHeader.add(CLASSIFICATION_EFFECT_KLASSES);
    ruleHeader.add(CLASSIFICATION_EFFECT_TAXONOMIES);
    
    ruleExcelMap.put(DATA_RULES_SHEET_NAME, new HashMap<>());
    Map<String, Object> excelMap = (Map<String, Object>) ruleExcelMap.get(DATA_RULES_SHEET_NAME);// RULE
    excelMap.put(HEADERS, ruleHeader);
    excelMap.put(DATA, new ArrayList<>());
  }

  /**
   *  /**
   * Prepare Map for Task Excel Sheet
   * 
   * @param transformedExcelMap
   * @param configTaskDTO
   * @param workflowTaskModel
   */
  private void generateConfigTaskExcel(Map<String, Object> transformedExcelMap,
      ConfigTaskDTO configTaskDTO, WorkflowTaskModel workflowTaskModel)
  {
    if (transformedExcelMap.get(TASK_SHEET_NAME) == null) {
      prepareTaskSheet(transformedExcelMap);
    }
    
    Map<String, Object> taskSheet = (Map<String, Object>) transformedExcelMap.get(TASK_SHEET_NAME);
    List<Map<String, String>> taskList = (List<Map<String, String>>) taskSheet.get(DATA);
    Map<String, String> transformedTaskMap = new LinkedHashMap<>();
    transformedTaskMap.put(CODE_COLUMN_CONFIG, configTaskDTO.getCode());
    transformedTaskMap.put(LABEL_COLUMN_CONFIG, configTaskDTO.getLabel());
    transformedTaskMap.put(TYPE_COLUMN, configTaskDTO.getType());
    transformedTaskMap.put(ICON_COLUMN, configTaskDTO.getIcon());
    transformedTaskMap.put(COLOR_COLUMN, configTaskDTO.getColor());
    transformedTaskMap.put(PRIORITY_TAG_COLUMN, configTaskDTO.getPriorityTag());
    transformedTaskMap.put(STATUS_TAG_COLUMN, configTaskDTO.getStatusTag());
    taskList.add(transformedTaskMap);
    
  }

  private void prepareTaskSheet(Map<String, Object> taskExcelMap)
  {
    LinkedHashSet<String> taskHeader = new LinkedHashSet<>();
    taskHeader.add(CODE_COLUMN_CONFIG);
    taskHeader.add(LABEL_COLUMN_CONFIG);
    taskHeader.add(TYPE_COLUMN);
    taskHeader.add(ICON_COLUMN);
    taskHeader.add(COLOR_COLUMN);
    taskHeader.add(PRIORITY_TAG_COLUMN);
    taskHeader.add(STATUS_TAG_COLUMN);
    taskExcelMap.put(TASK_SHEET_NAME, new HashMap<>());
    Map<String, Object> excelMap = (Map<String, Object>) taskExcelMap.get(TASK_SHEET_NAME);//ATTRIBUTE Sheet
    excelMap.put(HEADERS, taskHeader);
    excelMap.put(DATA, new ArrayList<>());
  }

  /**
   * Prepare Map for Tag Excel Sheet
   * 
   * @param transformedExcelMap
   * @param configTagDTO
   * @param workflowTaskModel
   */
  private void generateConfigTagExcel(Map<String, Object> transformedExcelMap,
      ConfigTagDTO configTagDTO, WorkflowTaskModel workflowTaskModel)
  {
    if (transformedExcelMap.get(TAG_SHEET_NAME) == null) {
      prepareTagSheet(transformedExcelMap);
    }
    
    Map<String, Object> tagSheet = (Map<String, Object>) transformedExcelMap.get(TAG_SHEET_NAME);
    List<Map<String, String>> tagList = (List<Map<String, String>>) tagSheet.get(DATA);
    Map<String, String> transformedTagMap = new LinkedHashMap<>();
    transformTag(transformedTagMap, configTagDTO);
    List<IConfigTagValueDTO> childTags = configTagDTO.getChildren();
    if(childTags!=null && !childTags.isEmpty()) {
     if (!transformedTagMap.get(TYPE).equals(TagType.BooleanTagCode)) {
       List<Map<String, String>> transformedChlidTags = new LinkedList<Map<String, String>>();
       preparechildTag(childTags, transformedChlidTags, configTagDTO.getCode());
       tagList.addAll(transformedChlidTags);
     }
      transformedTagMap.put(PARENT_CODE, "-1");
    }
    tagList.add(transformedTagMap);
  }

  private void transformTag(Map<String, String> transformedTagMap, ConfigTagDTO configTagDTO)
  {
    // configTagDTO
    transformedTagMap.put(CODE, configTagDTO.getCode());
    transformedTagMap.put(NAME, configTagDTO.getLabel());
    transformedTagMap.put(PARENT_CODE, configTagDTO.getCode());
    transformedTagMap.put(TYPE, configTagDTO.getTagType());
    transformedTagMap.put(DESCRIPTION, configTagDTO.getDescription());
    transformedTagMap.put(COLOR, configTagDTO.getColor());
    transformedTagMap.put(LINKED_MASTER_TAG_CODE, (String) configTagDTO.getLinkedMasterTag());
    transformedTagMap.put(MULTISELECT, configTagDTO.isMultiSelect() ? "1" : "0");
    transformedTagMap.put(TOOLTIP, configTagDTO.getToolTip());
    transformedTagMap.put(FILTERABLE, configTagDTO.isFilterable() ? "1" : "0");
    transformedTagMap.put(AVAILABILITY, String.join(SEPARATOR, configTagDTO.getAvailability()));
    transformedTagMap.put(GRID_EDITABLE, configTagDTO.isGridEditable() ? "1" : "0");
    transformedTagMap.put(READ_ONLY_COLUMN, configTagDTO.isDisabled() ? "1" : "0");
    transformedTagMap.put(ICON, configTagDTO.getIcon());
    transformedTagMap.put(REVISIONABLE, configTagDTO.isVersionable() ? "1" : "0");
    transformedTagMap.put(STANDARD, configTagDTO.isStandard() ? "1" : "0");
  }

  private void preparechildTag(List<IConfigTagValueDTO> childTags,
      List<Map<String, String>> transformedTag, String parentCode)
  {
    for (IConfigTagValueDTO iConfigTagValueDTO : childTags) {
      Map<String, String> transformedTagMap = new HashMap<String, String>();
      transformedTagMap.put(PARENT_CODE, parentCode);
      transformedTagMap.put(COLOR, iConfigTagValueDTO.getColor());
      transformedTagMap.put(NAME, iConfigTagValueDTO.getLabel());
      transformedTagMap.put(LINKED_MASTER_TAG_CODE, iConfigTagValueDTO.getLinkedMasterTag());
      transformedTagMap.put(CODE, iConfigTagValueDTO.getCode());
      transformedTagMap.put(ICON, iConfigTagValueDTO.getIcon());
      transformedTag.add(transformedTagMap);
    }
  }

  /**
   * Transform config USER to excel.
   * 
   * @param transformedExcelMap
   * @param configUserDTO
   * @param workflowTaskModel
   */
  private void generateConfigUserExcel(Map<String, Object> transformedExcelMap, ConfigUserDTO configUserDTO,
      WorkflowTaskModel workflowTaskModel)
  {
    if(transformedExcelMap.get(USER_SHEET_NAME) == null) {
      prepareUserSheet(transformedExcelMap);
    }
    
    Map<String, Object> userSheet = (Map<String, Object>) transformedExcelMap.get(USER_SHEET_NAME);
    List<Map<String, String>> userList = (List<Map<String, String>>) userSheet.get(DATA);
    
    Map<String, String> transformedContextMap = new LinkedHashMap<>();
    transformedContextMap.put(CODE, configUserDTO.getCode());
    transformedContextMap.put(USERNAME, configUserDTO.getUserName());
    transformedContextMap.put(PASSWORD, configUserDTO.getPassword());
    transformedContextMap.put(FIRSTNAME, configUserDTO.getFirstName());
    transformedContextMap.put(LASTNAME, configUserDTO.getLastName());
    transformedContextMap.put(GENDER, configUserDTO.getGender());
    transformedContextMap.put(EMAIL, configUserDTO.getEmail());
    transformedContextMap.put(CONTACT, configUserDTO.getContact());
    transformedContextMap.put(ICON, configUserDTO.getIcon());
    transformedContextMap.put(EMAIL_LOG_FILE, configUserDTO.getIsEmailLog() ? "1" : "0");
    transformedContextMap.put(UI_LANGUAGE, configUserDTO.getPreferredUILanguage());
    transformedContextMap.put(DATA_LANGUAGE, configUserDTO.getPreferredDataLanguage());
    
    userList.add(transformedContextMap);
    
  }

  /**
   * Transform config context to excel.
   * 
   * @param transformedExcelMap
   * @param configContextDTO
   * @param workflowTaskModel
   */
  private void generateConfigContextExcel(Map<String, Object> transformedExcelMap, ConfigContextDTO configContextDTO,
      WorkflowTaskModel workflowTaskModel)
  {

    if(transformedExcelMap.get(CONTEXT_SHEET_NAME) == null) {
      prepareContextSheet(transformedExcelMap);
    }
    
    Map<String, Object> contextSheet = (Map<String, Object>) transformedExcelMap.get(CONTEXT_SHEET_NAME);
    List<Map<String, String>> contextList = (List<Map<String, String>>) contextSheet.get(DATA);
    
    // entities Map
    Map<String, String> entitiesToAddMap = new HashMap<>();
    // type of Context Map
    Map<IContextDTO.ContextType, String> typeOfContextMap = new HashMap<>();
    prepareEntitiesAndTypeMapForContext(entitiesToAddMap, typeOfContextMap);

    Map<String, String> transformedContextMap = new LinkedHashMap<>();
    transformedContextMap.put(CODE_COLUMN_CONFIG, configContextDTO.getContextDTO().getCode());
    transformedContextMap.put(LABEL_COLUMN_CONFIG, configContextDTO.getLabel());
    transformedContextMap.put(TYPE_COLUMN, typeOfContextMap.getOrDefault(configContextDTO.getContextDTO().getContextType(),
        configContextDTO.getContextDTO().getContextType().name()));
    transformedContextMap.put(ICON_COLUMN, configContextDTO.getIcon());
    transformedContextMap.put(IS_TIME_ENABLED, configContextDTO.isTimeEnabled() ? "1" : "0");
    transformedContextMap.put(ENABLE_TIME_FROM,  DiTransformationUtils
        .getTimeStampForFormat(configContextDTO.getDefaultStartTime() != 0 ? configContextDTO.getDefaultStartTime() : null, DiConstants.DATE_FORMAT));
    transformedContextMap.put(ENABLE_TIME_TO, DiTransformationUtils
        .getTimeStampForFormat(configContextDTO.getDefaultEndTime() != 0 ? configContextDTO.getDefaultEndTime() : null, DiConstants.DATE_FORMAT));
    transformedContextMap.put(USE_CURRENT_TIME, configContextDTO.isCurrentTime() ? "1" : "0");
    transformedContextMap.put(ALLOW_DUPLICATES, configContextDTO.isDuplicateVariantAllowed() ? "1" : "0");
    List<String> entities = new ArrayList<>();
    for (String entity : configContextDTO.getEntities()) {
      entities.add(entitiesToAddMap.getOrDefault(entity, entity));
    }
    transformedContextMap.put(ENTITIES, String.join(";", entities));
    JSONObject tagCodes = (JSONObject) configContextDTO.getTagCodes().toJSONObject();
    if (tagCodes.keySet().isEmpty()) {
      contextList.add(transformedContextMap);
    }
    Boolean isFirstTag = true;
    for (Object key : tagCodes.keySet()) {
      if (isFirstTag) {
        isFirstTag = false;
        transformedContextMap.put(SELECTED_TAGS, (String)key);
        transformedContextMap.put(SELECTED_TAG_VALUES, String.join(";" ,(List<String> )tagCodes.get(key)));
        contextList.add(transformedContextMap);
      }
      else {
        Map<String, String> transformedTagsMap = new LinkedHashMap<>();
        transformedTagsMap.put(CODE_COLUMN_CONFIG, configContextDTO.getContextDTO().getCode());
        transformedTagsMap.put(SELECTED_TAGS, (String) key);
        transformedTagsMap.put(SELECTED_TAG_VALUES, String.join(";", (List<String>) tagCodes.get(key)));
        contextList.add(transformedTagsMap);
      }
    }
  }

  /**
   * Prepare Map for Attribute Excel Sheet
   * 
   * @param transformedExcelMap
   * @param configAttributeDTO
   * @param workflowTaskModel
   */
  public void generateConfigAttributeExcel(Map<String, Object> transformedExcelMap,
      ConfigAttributeDTO configAttributeDTO, WorkflowTaskModel workflowTaskModel)
  {
    if (transformedExcelMap.get(ATTRIBUTE_SHEET_NAME) == null) {
      prepareAttributeSheet(transformedExcelMap);
    }
    
    Map<String, Object> attributeSheet = (Map<String, Object>) transformedExcelMap.get(ATTRIBUTE_SHEET_NAME);
    List<Map<String, String>> attributeList = (List<Map<String, String>>) attributeSheet.get(DATA);
    Map<String, String> transformedAttributeMap = new LinkedHashMap<>();
    transformAttribute(transformedAttributeMap, configAttributeDTO);
    attributeList.add(transformedAttributeMap);
    
  }
 
  /**
   * Create Excel from Map
   * @param workflowTaskModel
   * @param exportedExcelFilePath
   * @param transformedExcelMap
   */
  private void writeIntoExcel(WorkflowTaskModel workflowTaskModel, String exportedExcelFilePath, Map<String, Object> transformedExcelMap)
  {
    for (Map.Entry<String, Object> entity : transformedExcelMap.entrySet()) {
      Map<String, Object> entityValue = (Map<String, Object>) entity.getValue();
      LinkedHashSet<String> headerSet = (LinkedHashSet<String>) entityValue.get(HEADERS);
      String[] headerToWrite = headerSet.toArray(new String[headerSet.size()]);
      List<String[]> dataToWrite = new ArrayList<>();
      try {
        dataToWrite = prepareInstancesDataToWrite(headerSet, (List<Map<String, String>>) entityValue.get(DATA));
        if (!dataToWrite.isEmpty()) {
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
   * Prepare Map for Article Excel Sheet
   * 
   * @param transformedExcelMap
   * @param configClassDTO
   * @param workflowTaskModel
   */
  private void generateConfigArticleExcel(Map<String, Object> transformedExcelMap, ConfigClassifierDTO configClassDTO,
      WorkflowTaskModel workflowTaskModel)
  {
    if(transformedExcelMap.get(KLASS_SHEET_NAME) == null) {
      prepareArticleKlassSheet(transformedExcelMap);
    }

    Map<String, Object> transformedKlassMap = new LinkedHashMap<>();
    generateConfigKlassExcel(transformedExcelMap, configClassDTO, transformedKlassMap);
    
    prepareBooleanValues(configClassDTO, transformedKlassMap);
  }
  
  /**
   * Prepare Map for Asset Excel Sheet
   * 
   * @param transformedExcelMap
   * @param configClassDTO
   * @param workflowTaskModel
   */
  private void generateConfigAssetExcel(Map<String, Object> transformedExcelMap, ConfigClassifierDTO configClassDTO,
      WorkflowTaskModel workflowTaskModel)
  {
    if(transformedExcelMap.get(KLASS_SHEET_NAME) == null) {
      prepareAssetKlassSheet(transformedExcelMap);
    }

    Map<String, Object> transformedKlassMap = new LinkedHashMap<>();
    generateConfigKlassExcel(transformedExcelMap, configClassDTO, transformedKlassMap);
    
    prepareBooleanValues(configClassDTO, transformedKlassMap);
  }
  
  /**
   * Prepare Map for TextAsset Excel Sheet
   * 
   * @param transformedExcelMap
   * @param configClassDTO
   * @param workflowTaskModel
   */
  private void generateConfigTextAssetExcel(Map<String, Object> transformedExcelMap, ConfigClassifierDTO configClassDTO,
      WorkflowTaskModel workflowTaskModel)
  {
    if (transformedExcelMap.get(KLASS_SHEET_NAME) == null) {
      prepareTextAssetKlassSheet(transformedExcelMap);
    }
    Map<String, Object> transformedKlassMap = new LinkedHashMap<>();
    generateConfigKlassExcel(transformedExcelMap, configClassDTO, transformedKlassMap);
    prepareBooleanValues(configClassDTO, transformedKlassMap);
  }
  /**
   * Prepare Map for Supplier Excel Sheet
   * 
   * @param transformedExcelMap
   * @param configClassDTO
   * @param workflowTaskModel
   */
  private void generateConfigSupplierExcel(Map<String, Object> transformedExcelMap, ConfigClassifierDTO configClassDTO,
      WorkflowTaskModel workflowTaskModel)
  {
    if (transformedExcelMap.get(KLASS_SHEET_NAME) == null) {
      prepareSupplierKlassSheet(transformedExcelMap);
    }
    
    Map<String, Object> transformedKlassMap = new LinkedHashMap<>();
    generateConfigKlassExcel(transformedExcelMap, configClassDTO, transformedKlassMap);
  }
  
  /**
   * Prepare Map for VirtualCatlog Excel Sheet
   * 
   * @param transformedExcelMap
   * @param configClassDTO
   * @param workflowTaskModel
   */
  private void generateConfigVirtualCatlogExcel(Map<String, Object> transformedExcelMap, ConfigClassifierDTO configClassDTO,
      WorkflowTaskModel workflowTaskModel)
  {
    if (transformedExcelMap.get(KLASS_SHEET_NAME) == null) {
      prepareVirtualCatlogKlassSheet(transformedExcelMap);
    }
    
    Map<String, Object> transformedKlassMap = new LinkedHashMap<>();
    generateConfigKlassExcel(transformedExcelMap, configClassDTO, transformedKlassMap);

  }
  
  /**
   * Prepare map for flat level field of klass and preapre the map of property collection, 
   * embedded variants and relationship which linked to the respective class.
   * 
   * @param transformedExcelMap
   * @param configClassDTO
   * @param transformedKlassMap
   */
  private void generateConfigKlassExcel(Map<String, Object> transformedExcelMap, ConfigClassifierDTO configClassDTO,
      Map<String, Object> transformedKlassMap)
  {
    Map<String, Object> klassSheet = (Map<String, Object>)transformedExcelMap.get(KLASS_SHEET_NAME);
    List<Map<String, Object>> entityList = (List<Map<String, Object>>)klassSheet.get(DATA);
    
    transformCommonFieldOfKlassEntity(transformedKlassMap, configClassDTO);
    
    if(configClassDTO.getSections() != null && !configClassDTO.getSections().isEmpty()) {
      String pcSheetName = PC_SHEET_PREFIX + propCollectinCount++;
      transformedKlassMap.put(SECTION_SHEET_NAME, pcSheetName);

      if (transformedExcelMap.get(pcSheetName) == null) {
        prepareKlassPropertyCollectionSheet(transformedExcelMap, pcSheetName);
      }
      
      Map<String, Object> propMap = (Map<String, Object>) transformedExcelMap.get(pcSheetName);
      List<Map<String, Object>> propCollList = (List<Map<String, Object>>) propMap.get(DATA);
      prepareKlassifierPropertyCollections(configClassDTO, propCollList);
    }
    
    if(configClassDTO.getEmbeddedClasses() != null && !configClassDTO.getEmbeddedClasses().isEmpty()) {
      String variantSheetName = VARIANTS_SHEET_PREFIX+ variantCount++;
      transformedKlassMap.put(VARIANT_SHEET_NAME_FOR_KLASS, variantSheetName);
      
      if (transformedExcelMap.get(variantSheetName) == null) {
        prepareVarientSheet(transformedExcelMap, variantSheetName);
      }
      Map<String, Object> varianSheet = (Map<String, Object>) transformedExcelMap.get(variantSheetName);
      List<Map<String, String>> variantsList = (List<Map<String, String>>) varianSheet.get(DATA);
      
      prepareKlassifierVariants(configClassDTO, variantsList);
    }
    if(configClassDTO.getRelationships() != null && !configClassDTO.getRelationships().isEmpty()) {
      String relationshipSheetName = RELATIONSHIPS_SHEET_PREFIX+ relationshipCount++;
      transformedKlassMap.put(RELATIONSHIP_SHEET_NAME_FOR_KLASS, relationshipSheetName);
      if (transformedExcelMap.get(relationshipSheetName) == null) {
      prepareKlassRelationshipSheet(transformedExcelMap, relationshipSheetName);
      }
      Map<String, Object> relationshipSheet = (Map<String, Object>) transformedExcelMap.get(relationshipSheetName);
      List<Map<String, String>> relationshipList = (List<Map<String, String>>) relationshipSheet.get(DATA);
      prepareKlassifirerRelationhips(configClassDTO, relationshipList);
      }    
    entityList.add(transformedKlassMap);
  }
  
  /**
   * Get coupling info
   * 
   * @param side1Couplings
   * @return the map of coupling information.
   */
  private Map<String, List<String>> getCouplingInfo(Collection<IJSONContent> side1Couplings)
  {
    Iterator<IJSONContent> iterator = side1Couplings.iterator();
    Map<String, List<String>> couplingInfoMap = new HashMap<>();
    List<String> attributes = new ArrayList<>();
    List<String> tags = new ArrayList<>();
    List<String> attributesCoupling = new ArrayList<>();
    List<String> tagsCoupling = new ArrayList<>();
    while(iterator.hasNext()) {
      JSONObject coupling = (JSONObject) iterator.next();
      if(coupling.get(CommonConstants.TYPE).equals(CommonConstants.ATTRIBUTE)) {
        attributes.add((String) coupling.get(CommonConstants.ID_PROPERTY));
        attributesCoupling.add((String) coupling.get(COUPLING_TYPE));
      }else if(coupling.get(CommonConstants.TYPE).equals(CommonConstants.TAG)) {
        tags.add((String) coupling.get(CommonConstants.ID_PROPERTY));
        tagsCoupling.add((String) coupling.get(COUPLING_TYPE));
      }
    }
    
    couplingInfoMap.put(ATTRIBUTES, attributes);
    couplingInfoMap.put(TAGS, tags);
    couplingInfoMap.put(ATTRIBUTES_COUPLING, attributesCoupling);
    couplingInfoMap.put(TAGS_COUPLING, tagsCoupling);
    
    return couplingInfoMap;
  } 
  
  /**
   * Prepare Map for Property Collection Excel Sheet
   * 
   * @param transformedExcelMap
   * @param configPropertyCollDTO
   * @param workflowTaskModel
   * @throws CSFormatException 
   */
  private void generateConfigPropertyCollectionExcel(Map<String, Object> transformedExcelMap, ConfigPropertyCollectionDTO configPropertyCollDTO,
      WorkflowTaskModel workflowTaskModel) throws CSFormatException
  {
    if(transformedExcelMap.get(PROPERTY_COLLECTION_SHEET_NAME) == null) {
      preparePropertyCollectionSheet(transformedExcelMap);
    }

    Map<String, String> transformedPropertyCollMap = new LinkedHashMap<>();
    Map<String, Object> propertyCollSheet = (Map<String, Object>)transformedExcelMap.get(PROPERTY_COLLECTION_SHEET_NAME);
    List<Map<String, String>> entityList = (List<Map<String, String>>)propertyCollSheet.get(DATA);
    
    transformedPropertyCollMap.put(CODE_COLUMN_CONFIG, configPropertyCollDTO.getCode());
    transformedPropertyCollMap.put(LABEL_COLUMN_CONFIG, configPropertyCollDTO.getLabel());
    transformedPropertyCollMap.put(TAB_COLUMN, configPropertyCollDTO.getTab());
    transformedPropertyCollMap.put(ICON_COLUMN, configPropertyCollDTO.getIcon());
    transformedPropertyCollMap.put(IS_FOR_XRAY_COLUMN, configPropertyCollDTO.isForXRay() ? "1":"0");
    transformedPropertyCollMap.put(IS_DEFAULT_FOR_XRAY_COLUMN, configPropertyCollDTO.isDefaultForXRay() ? "1":"0");
    transformedPropertyCollMap.put(IS_STANDARD_COLUMN, configPropertyCollDTO.isStandard() ? "1":"0");
    Collection<JSONObject> elements = configPropertyCollDTO.getElements();
    if (elements.isEmpty()) {
      entityList.add(transformedPropertyCollMap);
    }
    Boolean isFirstElement = true;
    for (JSONObject element : elements) {
      if (isFirstElement) {
        transformedPropertyCollMap.put(ENTITY_CODE_COLUMN, (String) element.getOrDefault(CODE_PROPERTY, new String()));
        transformedPropertyCollMap.put(ENTITY_TYPE_COLUMN, (String) element.getOrDefault(TYPE_PROPERTY, new String()));
        transformedPropertyCollMap.put(INDEX_COLUMN, String.valueOf(element.getOrDefault(INDEX_PROPERTY, new String())));
        entityList.add(transformedPropertyCollMap);
        isFirstElement = false;
      }
      else {
        Map<String, String> transformedPropertyCollectionMapForEntity = new LinkedHashMap<>();
        transformedPropertyCollectionMapForEntity.put(CODE_COLUMN_CONFIG, configPropertyCollDTO.getCode());
        transformedPropertyCollectionMapForEntity.put(ENTITY_CODE_COLUMN, (String) element.getOrDefault(CODE_PROPERTY, new String()));
        transformedPropertyCollectionMapForEntity.put(ENTITY_TYPE_COLUMN, (String) element.getOrDefault(TYPE_PROPERTY, new String()));
        transformedPropertyCollectionMapForEntity.put(INDEX_COLUMN, String.valueOf(element.getOrDefault(INDEX_PROPERTY, new String())));
        entityList.add(transformedPropertyCollectionMapForEntity);
      }
    }
  }
  
  /**
   * Prepare Map for Relationship Excel Sheet
   * 
   * @param transformedExcelMap
   * @param configRelationshipDTO
   * @param workflowTaskModel
   * @throws CSFormatException 
   */
  private void generateConfigRelationshipExcel(Map<String, Object> transformedExcelMap, ConfigRelationshipDTO configRelationshipDTO,
      WorkflowTaskModel workflowTaskModel) throws CSFormatException
  {
    if(transformedExcelMap.get(RELATIONSHIP_SHEET_NAME) == null) {
      prepareRelationshipSheet(transformedExcelMap);
    }

    Map<String, String> transformedRelationshipMap = new LinkedHashMap<>();
    Map<String, Object> relationshipSheet = (Map<String, Object>) transformedExcelMap.get(RELATIONSHIP_SHEET_NAME);
    List<Map<String, String>> entityList = (List<Map<String, String>>) relationshipSheet.get(DATA);
    
    transformedRelationshipMap.put(CODE, configRelationshipDTO.getCode());
    transformedRelationshipMap.put(LABEL_COLUMN, configRelationshipDTO.getLabel());
    transformedRelationshipMap.put(ICON, configRelationshipDTO.getIcon());
    transformedRelationshipMap.put(TAB_CODE_COLUMN, configRelationshipDTO.getTab());
    transformedRelationshipMap.put(ALLOW_AFTERSAVE_EVENT, configRelationshipDTO.isEnableAfterSave() ? "1":"0");
    transformedRelationshipMap.put(IS_LITE, configRelationshipDTO.isLite() ? "1":"0");
    IConfigSideRelationshipDTO side1Info = configRelationshipDTO.getSide1();
    transformedRelationshipMap.put(S1_CLASS_CODE, side1Info.getClassCode());
    transformedRelationshipMap.put(S1_LABEL, side1Info.getLabel());
    transformedRelationshipMap.put(S1_CARDINALITY, side1Info.getCardinality());
    transformedRelationshipMap.put(S1_EDITABLE, side1Info.isVisible() ? "1":"0");
    transformedRelationshipMap.put(S1_CONTEXT_CODE, side1Info.getContextCode());
    
    IConfigSideRelationshipDTO side2Info = configRelationshipDTO.getSide2();
    transformedRelationshipMap.put(S2_CLASS_CODE, side2Info.getClassCode());
    transformedRelationshipMap.put(S2_LABEL, side2Info.getLabel());
    transformedRelationshipMap.put(S2_CARDINALITY, side2Info.getCardinality());
    transformedRelationshipMap.put(S2_EDITABLE, side2Info.isVisible() ? "1":"0");
    transformedRelationshipMap.put(S2_CONTEXT_CODE, side2Info.getContextCode());
    
    Iterator<Object> side1CouplingIterator = ((JSONArray) side1Info.getCouplings()).iterator();
    Iterator<Object> side2CouplingIterator = ((JSONArray) side2Info.getCouplings()).iterator();
    if (!side1CouplingIterator.hasNext() && !side2CouplingIterator.hasNext()) {
      entityList.add(transformedRelationshipMap);
    }
    Boolean isFirstCoupling = true;
    JSONObject side1Coupling;
    JSONObject side2Coupling;
    Map<String,String> transformedCouplingMap;
    while (side1CouplingIterator.hasNext() && side2CouplingIterator.hasNext()) {
      side1Coupling = (JSONObject) side1CouplingIterator.next();
      side2Coupling = (JSONObject) side2CouplingIterator.next();
      if (isFirstCoupling) {
        fillSide1Coupling(transformedRelationshipMap, side1Coupling);
        fillSide2Coupling(transformedRelationshipMap, side2Coupling);
        entityList.add(transformedRelationshipMap);
        isFirstCoupling = false;
      }
      else {
        transformedCouplingMap = new HashMap<>();
        transformedCouplingMap.put(CODE_COLUMN_CONFIG, configRelationshipDTO.getCode());
        fillSide1Coupling(transformedCouplingMap, side1Coupling);
        fillSide2Coupling(transformedCouplingMap, side2Coupling);
        entityList.add(transformedCouplingMap);
      }
    }
    while(side1CouplingIterator.hasNext()){
      side1Coupling = (JSONObject) side1CouplingIterator.next();
      if (isFirstCoupling) {
        fillSide1Coupling(transformedRelationshipMap, side1Coupling);
        entityList.add(transformedRelationshipMap);
      }
      else {
        transformedCouplingMap = new HashMap<>();
        transformedCouplingMap.put(CODE_COLUMN_CONFIG, configRelationshipDTO.getCode());
        fillSide1Coupling(transformedCouplingMap, side1Coupling);
        entityList.add(transformedCouplingMap);
      }
    }

   while(side2CouplingIterator.hasNext()){
     side2Coupling = (JSONObject) side2CouplingIterator.next();
     if (isFirstCoupling) {
       fillSide2Coupling(transformedRelationshipMap, side2Coupling);
       entityList.add(transformedRelationshipMap);
     }
     else {
       transformedCouplingMap = new HashMap<>();
       transformedCouplingMap.put(CODE_COLUMN_CONFIG, configRelationshipDTO.getCode());
       fillSide2Coupling(transformedCouplingMap, side2Coupling);
       entityList.add(transformedCouplingMap);
    
     }
   }
  }
  
  /* Prepare Map for Property Collection Excel Sheet
   * 
   * @param transformedExcelMap
   * @param configPropertyCollDTO
   * @param workflowTaskModel
   * @throws CSFormatException 
   */
  private void generateConfigMasterTaxonomyExcel(Map<String, Object> transformedExcelMap, ConfigClassifierDTO configClassifierDTO,
      WorkflowTaskModel workflowTaskModel)
  {
    if (transformedExcelMap.get(MASTER_TAXONOMY_SHEET_NAME) == null) {
      prepareMasterTaxonomySheet(transformedExcelMap);
    }    
    Map<String, Object> transformedTxnomyMap = new LinkedHashMap<>();
    Map<String, Object> masterTaxonomySheet = (Map<String, Object>) transformedExcelMap.get(MASTER_TAXONOMY_SHEET_NAME);
    List<Map<String, Object>> entityList = (List<Map<String, Object>>) masterTaxonomySheet.get(DATA);
    transformTaxonomy(transformedTxnomyMap, configClassifierDTO, MASTER_TAXONOMY_SHEET_NAME);
    if (configClassifierDTO.getSections() != null && !configClassifierDTO.getSections().isEmpty()) {
      String pcSheetName = PC_SHEET_PREFIX + propCollectinCount++;
      transformedTxnomyMap.put(PROPERTY_COLLECTION_COLUMN, pcSheetName);
      if (transformedExcelMap.get(pcSheetName) == null) {
        prepareKlassPropertyCollectionSheet(transformedExcelMap, pcSheetName);
      }
      
      Map<String, Object> propMap = (Map<String, Object>) transformedExcelMap.get(pcSheetName);
      List<Map<String, Object>> propCollList = (List<Map<String, Object>>) propMap.get(DATA);
      prepareKlassifierPropertyCollections(configClassifierDTO, propCollList);
    }
    if(configClassifierDTO.getEmbeddedClasses() != null && !configClassifierDTO.getEmbeddedClasses().isEmpty()) {
      String variantSheetName = VARIANTS_SHEET_PREFIX+ variantCount++;
      transformedTxnomyMap.put(EMBEDDED_CLASS_COLUMN, variantSheetName);

      if (transformedExcelMap.get(variantSheetName) == null) {
        prepareVarientSheet(transformedExcelMap, variantSheetName);
      }
      Map<String, Object> varianSheet = (Map<String, Object>) transformedExcelMap.get(variantSheetName);
      List<Map<String, String>> variantsList = (List<Map<String, String>>) varianSheet.get(DATA);
      prepareKlassifierVariants(configClassifierDTO, variantsList);
    }
    entityList.add(transformedTxnomyMap);
  }
  
  /**
   * Prepare Map for Property Collection Excel Sheet
   * 
   * @param transformedExcelMap
   * @param configPropertyCollDTO
   * @param workflowTaskModel
   * @throws CSFormatException 
   */
  private void generateConfigHierarchyTaxonomyExcel(Map<String, Object> transformedExcelMap, ConfigClassifierDTO configClassifierDTO,
      WorkflowTaskModel workflowTaskModel)
  {
    if (transformedExcelMap.get(HIERARCHY_SHEET_NAME) == null) {
      prepareTaxonomyHierarchySheet(transformedExcelMap);
    }
    Map<String, Object> transformedTxnomyMap = new LinkedHashMap<>();
    Map<String, Object> propertyCollSheet = (Map<String, Object>) transformedExcelMap.get(HIERARCHY_SHEET_NAME);
    List<Map<String, Object>> entityList = (List<Map<String, Object>>) propertyCollSheet.get(DATA);
    transformTaxonomy(transformedTxnomyMap, configClassifierDTO, null);
    entityList.add(transformedTxnomyMap);
  }
  
  /**
   * Prepare Map for Golden Record Rule Excel Sheet
   * 
   * @param transformedExcelMap
   * @param configGoldenRecordRuleDTO
   * @param workflowTaskModel
   * @throws CSFormatException 
   */
  private void generateConfigGoldenRecordRuleExcel(Map<String, Object> transformedExcelMap, ConfigGoldenRecordRuleDTO configGoldenRecordRuleDTO,
      WorkflowTaskModel workflowTaskModel) throws CSFormatException
  {
    if(transformedExcelMap.get(GOLDEN_RECORD_RULES_SHEET_NAME) == null) {
      prepareGoldenRecordRuleSheet(transformedExcelMap);
    }

    Map<String, String> transformedGoldenRuleMap = new LinkedHashMap<>();
    Map<String, Object> goldenRuleSheet = (Map<String, Object>) transformedExcelMap.get(GOLDEN_RECORD_RULES_SHEET_NAME);
    List<Map<String, String>> entityList = (List<Map<String, String>>) goldenRuleSheet.get(DATA);
    List<String> klassIds = configGoldenRecordRuleDTO.getKlassIds();
    transformedGoldenRuleMap.put(CODE_COLUMN_CONFIG, configGoldenRecordRuleDTO.getCode());
    transformedGoldenRuleMap.put(LABEL_COLUMN_CONFIG, configGoldenRecordRuleDTO.getLabel());
    transformedGoldenRuleMap.put(NATURE_TYPE, klassIds.get(0)); 
    transformedGoldenRuleMap.put(PARTNERS, String.join(";", configGoldenRecordRuleDTO.getOrganizations()));
    transformedGoldenRuleMap.put(PHYSICALCATALOGS, String.join(";", configGoldenRecordRuleDTO.getPhysicalCatalogIds()));
    transformedGoldenRuleMap.put(MATCH_ATTRIBUTES, String.join(";", configGoldenRecordRuleDTO.getAttributes()));
    transformedGoldenRuleMap.put(MATCH_TAGS, String.join(";", configGoldenRecordRuleDTO.getTags()));
    klassIds.remove(0);
    transformedGoldenRuleMap.put(MATCH_NON_NATURE_CLASSES, String.join(";", klassIds));
    transformedGoldenRuleMap.put(MATCH_TAXONOMIES, String.join(";", configGoldenRecordRuleDTO.getTaxonomyIds()));
    transformedGoldenRuleMap.put(AUTOCREATE, configGoldenRecordRuleDTO.getIsAutoCreate() ? "1" : "0");
    
    Iterator<IConfigMergeEffectTypeDTO> attributesIterator = configGoldenRecordRuleDTO.getMergeEffect().getAttributes().iterator();
    Iterator<IConfigMergeEffectTypeDTO> tagsIterator = configGoldenRecordRuleDTO.getMergeEffect().getTags().iterator();
    Iterator<IConfigMergeEffectTypeDTO> relationshipsIterator = configGoldenRecordRuleDTO.getMergeEffect().getRelationships().iterator();
    Iterator<IConfigMergeEffectTypeDTO> natureRelationshipsIterator = configGoldenRecordRuleDTO.getMergeEffect().getNatureRelationships().iterator();
    if (!attributesIterator.hasNext() && !tagsIterator.hasNext() && !relationshipsIterator.hasNext() && !natureRelationshipsIterator.hasNext()) {
      entityList.add(transformedGoldenRuleMap);
    }
    Boolean isFirst = true;
    Map<String,String> transformedMergeEffectMap;
    
    while (attributesIterator.hasNext() || tagsIterator.hasNext() || relationshipsIterator.hasNext()
        || natureRelationshipsIterator.hasNext()) {
      if (isFirst) {
        checkAndFillMergeEffect(transformedGoldenRuleMap, attributesIterator);
        checkAndFillMergeEffect(transformedGoldenRuleMap, tagsIterator);
        checkAndFillMergeEffect(transformedGoldenRuleMap, relationshipsIterator);
        checkAndFillMergeEffect(transformedGoldenRuleMap, natureRelationshipsIterator);
        entityList.add(transformedGoldenRuleMap);
        isFirst = false;
      }
      else {
        transformedMergeEffectMap = new HashMap<>();
        transformedMergeEffectMap.put(CODE_COLUMN_CONFIG, configGoldenRecordRuleDTO.getCode());
        checkAndFillMergeEffect(transformedMergeEffectMap, attributesIterator);
        checkAndFillMergeEffect(transformedMergeEffectMap, tagsIterator);
        checkAndFillMergeEffect(transformedMergeEffectMap, relationshipsIterator);
        checkAndFillMergeEffect(transformedMergeEffectMap, natureRelationshipsIterator);
        entityList.add(transformedMergeEffectMap);
      }
    }
  }

  private void checkAndFillMergeEffect(Map<String, String> transformedGoldenRuleMap, Iterator<IConfigMergeEffectTypeDTO> entityIterator)
  {
    IConfigMergeEffectTypeDTO mergeEffectDTO;
    if (entityIterator.hasNext()) {
      mergeEffectDTO = entityIterator.next();
      fillMergeEffectColumns(transformedGoldenRuleMap, mergeEffectDTO);
    }
  }
  
  private void fillMergeEffectColumns(Map<String, String> transformedMergeEffectMap, IConfigMergeEffectTypeDTO mergeEffectDTO)
  {
    switch (mergeEffectDTO.getEntityType()) {
      case CommonConstants.ATTRIBUTES:
        transformedMergeEffectMap.put(MERGE_ATTRIBUTES, mergeEffectDTO.getEntityId());
        transformedMergeEffectMap.put(MERGE_ATTRIBUTES_LATEST, checkMergeEffectType(mergeEffectDTO.getType()));
        transformedMergeEffectMap.put(MERGE_ATTRIBUTES_SUPPLIERS, String.join(";", mergeEffectDTO.getSupplierIds()));
        break;
      case CommonConstants.TAGS:
        transformedMergeEffectMap.put(MERGE_TAGS, mergeEffectDTO.getEntityId());
        transformedMergeEffectMap.put(MERGE_TAGS_LATEST, checkMergeEffectType(mergeEffectDTO.getType()));
        transformedMergeEffectMap.put(MERGE_TAGS_SUPPLIERS, String.join(";", mergeEffectDTO.getSupplierIds()));
        break;
      case CommonConstants.RELATIONSHIPS:
        transformedMergeEffectMap.put(MERGE_RELATIONSHIPS, mergeEffectDTO.getEntityId());
        transformedMergeEffectMap.put(MERGE_RELATIONSHIPS_SUPPLIERS, String.join(";", mergeEffectDTO.getSupplierIds()));
        break;
      case CommonConstants.NATURE_RELATIONSHIPS:
        transformedMergeEffectMap.put(MERGE_NATURE_RELATIONSHIPS, mergeEffectDTO.getEntityId());
        transformedMergeEffectMap.put(MERGE_NATURE_RELATIONSHIPS_SUPPLIERS, String.join(";", mergeEffectDTO.getSupplierIds()));
        break;
    }
  }
  
  private String checkMergeEffectType (String mergeEffect) {
    if (mergeEffect.equals(LATEST_PRIORITY)) {
      return "1";
    }
    return "0";
  }
  
  /**
   * Prepare Map for Organization Excel Sheet
   * 
   * @param transformedExcelMap
   * @param configOrganizationDTO
   * @param workflowTaskModel
   * @throws CSFormatException 
   */
  private void generateConfigOrganizationExcel(Map<String, Object> transformedExcelMap, ConfigOrganizationDTO configOrganizationDTO,
      WorkflowTaskModel workflowTaskModel) throws CSFormatException
  {
    if(transformedExcelMap.get(PARTNER_SHEET_NAME) == null) {
      prepareOrganizationSheet(transformedExcelMap);
    }

    Map<String, String> transformedOrganizationMap = new LinkedHashMap<>();
    Map<String, String> transformedRoleMap = new LinkedHashMap<>();
    Map<String, Object> organizationSheet = (Map<String, Object>) transformedExcelMap.get(PARTNER_SHEET_NAME);
    List<Map<String, String>> entityList = (List<Map<String, String>>) organizationSheet.get(DATA);
    transformedOrganizationMap.put(CODE_COLUMN_CONFIG, configOrganizationDTO.getCode());
    transformedOrganizationMap.put(LABEL_COLUMN_CONFIG, configOrganizationDTO.getLabel());
    transformedOrganizationMap.put(TYPE_COLUMN, configOrganizationDTO.getType()); 
    transformedOrganizationMap.put(ICON_COLUMN, configOrganizationDTO.getIcon());
    transformedOrganizationMap.put(PHYSICAL_CATALOG, String.join(";", configOrganizationDTO.getPhysicalCatalogs()));
    transformedOrganizationMap.put(PORTALS, String.join(";", configOrganizationDTO.getPortals()));
    transformedOrganizationMap.put(TAXONOMY_CODE, String.join(";", configOrganizationDTO.getTaxonomyIds()));
    transformedOrganizationMap.put(TARGET_CLASS_CODE, String.join(";", configOrganizationDTO.getKlassIds()));
    transformedOrganizationMap.put(SYSTEM_CODE, String.join(";", configOrganizationDTO.getSystems()));
    transformedOrganizationMap.put(ENDPOINT_CODE, String.join(";", configOrganizationDTO.getEndpointIds()));
    
    List<IConfigRoleDTO> roles = configOrganizationDTO.getRoles();
    
    if (roles.isEmpty()) {
      entityList.add(transformedOrganizationMap);
    }
    Boolean isFirst = true;
    for (IConfigRoleDTO roleDTO : roles) {
      if (isFirst) {
        fillRolesForOrganization(transformedOrganizationMap, roleDTO);
        entityList.add(transformedOrganizationMap);
        isFirst = false;
      }
      else {
        transformedRoleMap = new LinkedHashMap<>();
        transformedRoleMap.put(CODE_COLUMN_CONFIG, configOrganizationDTO.getCode());
        fillRolesForOrganization(transformedRoleMap, roleDTO);
        entityList.add(transformedRoleMap);
      }
    }
    
  }

  private void fillRolesForOrganization(Map<String, String> transformedOrganizationMap, IConfigRoleDTO roleDTO)
  {
    transformedOrganizationMap.put(ROLE_CODE, roleDTO.getCode());
    transformedOrganizationMap.put(ROLE_LABEL, roleDTO.getLabel());
    transformedOrganizationMap.put(ROLE_TYPE, roleDTO.getRoleType());
    transformedOrganizationMap.put(ROLE_PHYSICAL_CATALOG, String.join(";", roleDTO.getPhysicalCatalogs()));
    transformedOrganizationMap.put(ROLE_PORTALS, String.join(";", roleDTO.getPortals()));
    transformedOrganizationMap.put(ROLE_TAXONOMY_CODE, String.join(";", roleDTO.getTargetTaxonomies()));
    transformedOrganizationMap.put(ROLE_TARGET_CLASS_CODE, String.join(";", roleDTO.getTargetKlasses()));
    transformedOrganizationMap.put(ROLE_USERS, String.join(";", roleDTO.getUsers()));
    transformedOrganizationMap.put(ROLE_ENABLED_DASHBOARD, roleDTO.isDashboardEnable() ? "1" : "0");
    transformedOrganizationMap.put(ROLE_KPI, String.join(";", roleDTO.getKpis()));
    transformedOrganizationMap.put(ROLE_ENTITIES, String.join(";", roleDTO.getEntities()));
    transformedOrganizationMap.put(ROLE_SYSTEM_CODE, String.join(";", roleDTO.getSystems()));
    transformedOrganizationMap.put(ROLE_ENDPOINT_CODE, String.join(";", roleDTO.getEndpoints()));
    transformedOrganizationMap.put(ROLE_READONLY_USER,roleDTO.isReadOnly() ? "1" : "0");
  }
  
  /**
   * Prepare Map for Translations Excel Sheet
   * 
   * @param transformedExcelMap
   * @param configTranslationDTO
   * @param workflowTaskModel
   * @throws CSFormatException 
   */
  private void generateConfigTranslationExcel(Map<String, Object> transformedExcelMap, ConfigTranslationDTO configTranslationDTO,
      WorkflowTaskModel workflowTaskModel) throws CSFormatException
  {
    Map<String, Object> languageObject = (Map<String, Object>) configTranslationDTO.getTranslations().toJSONObject();
    Set<String> languageKeys = new HashSet<>(languageObject.keySet());
    
    if (languageKeys.remove(CommonConstants.LANGUAGE_CODE_FOR_DEFAULT_LABEL_PROPERTY)) {
      preapreTransformedExcelMap(transformedExcelMap, configTranslationDTO, languageObject,
          CommonConstants.LANGUAGE_CODE_FOR_DEFAULT_LABEL_PROPERTY);
    }
    
    for (String sheetName : languageKeys) {
      preapreTransformedExcelMap(transformedExcelMap, configTranslationDTO, languageObject, sheetName);
    }
  }

  /**
   * Prepare the excel sheet map from translations DTOs
   * 
   * @param transformedExcelMap
   * @param configTranslationDTO
   * @param languageObject
   * @param sheetName
   */
  private void preapreTransformedExcelMap(Map<String, Object> transformedExcelMap, ConfigTranslationDTO configTranslationDTO,
      Map<String, Object> languageObject, String sheetName)
  {
    if (transformedExcelMap.get(sheetName) == null) {
      prepareTranslationSheet(transformedExcelMap, sheetName);
    }
    Map<String, String> transformedTranslationMap = new LinkedHashMap<>();
    Map<String, Object> languageSheet = (Map<String, Object>) transformedExcelMap.get(sheetName);
    List<Map<String, String>> entityList = (List<Map<String, String>>) languageSheet.get(DATA);
    transformedTranslationMap.put(ENTITY_TYPE_COLUMN, configTranslationDTO.getEntityType().name());
    transformedTranslationMap.put(ENTITY_CODE_COLUMN, configTranslationDTO.getCode());
    fillTranslationDetails(transformedTranslationMap, (Map<String, String>) languageObject.get(sheetName));
    entityList.add(transformedTranslationMap);
  }

  /**
   * @param transformedExcelMap
   * @param configLanguageDTO
   * @param workflowTaskModel
   */
  private void generateConfigLanguageExcel(Map<String, Object> transformedExcelMap,
      IConfigLanguageDTO configLanguageDTO, WorkflowTaskModel workflowTaskModel)
  {
    if(transformedExcelMap.get(LANGUAGE_SHEET_NAME) == null) {
      prepareLanguageSheet(transformedExcelMap);
    }
    
    Map<String, Object> languageSheet = (Map<String, Object>) transformedExcelMap.get(LANGUAGE_SHEET_NAME);
    List<Map<String, String>> languageList = (List<Map<String, String>>) languageSheet.get(DATA);
    
    Map<String, String> transformedLanguageMap = new LinkedHashMap<>();
    transformedLanguageMap.put(CODE, ((RootConfigDTO) configLanguageDTO).getCode());
    transformedLanguageMap.put(LABEL_COLUMN, configLanguageDTO.getLabel());
    transformedLanguageMap.put(PARENTCODE, configLanguageDTO.getParentCode());
    transformedLanguageMap.put(ABBREVIATION, configLanguageDTO.getAbbreviation());
    transformedLanguageMap.put(LOCALE_ID, configLanguageDTO.getLocaleId());
    transformedLanguageMap.put(NUMBER_FORMAT, configLanguageDTO.getNumberFormat());
    transformedLanguageMap.put(DATE_FORMAT, configLanguageDTO.getDateFormat());
    transformedLanguageMap.put(ICON, configLanguageDTO.getIcon());
    transformedLanguageMap.put(IS_DATA_LANGUAGE, configLanguageDTO.isDataLanguage() ? "1" : "0");
    transformedLanguageMap.put(IS_UI_LANGUAGE, configLanguageDTO.isUserInterfaceLanguage() ? "1" : "0");
    transformedLanguageMap.put(IS_DEFULT_LANGUAGE, configLanguageDTO.isDefaultLanguage() ? "1" : "0");
    
    languageList.add(transformedLanguageMap);
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
 * fill SAN Effect
 * @param transformedRuleMap
 * @param normalization
 */
private void fillSANEffect(Map<String, String> transformedRuleMap,
    IConfigNormalizationDTO normalization)
{
  List<String> entityValues = new ArrayList<String>();
  List<String> transformationType = new ArrayList<String>();
  List<String> entitySpclValues = new ArrayList<String>();
  
  if (normalization.getType()
      .equalsIgnoreCase(SuperType.TAGS.toString())) {
    transformedRuleMap.put(EFFECT_ENTITY_CODE, normalization.getEntityId());
    transformedRuleMap.put(EFFECT_ENTITY_TYPE, normalization.getType());
    transformedRuleMap.put(EFFECT_ENTITY_SUBTYPE, normalization.getEntityAttributeType());
    normalization.setBaseType(CommonConstants.NORMALIZATION_BASE_TYPE);
    transformationType.add(normalization.getTransformationType());
    List<IJSONContent> tagValues = normalization.getTagValues();
    for (int i = 0; i < tagValues.size(); i++) {
      JSONObject setConcatObj = (JSONObject) tagValues.get(i);
      entityValues.add((String) setConcatObj.get(ID_COLUMN));
    }
    
    // entityValues.add(normalization.getT);
  }
  else if (normalization.getType()
      .equalsIgnoreCase(SuperType.ATTRIBUTE.toString())) {
    transformedRuleMap.put(EFFECT_ENTITY_CODE, normalization.getEntityId());
    transformedRuleMap.put(EFFECT_ENTITY_TYPE, normalization.getType());
    transformedRuleMap.put(EFFECT_ENTITY_SUBTYPE, normalization.getEntityAttributeType());
    transformationType.add(normalization.getTransformationType());
    switch (normalization.getTransformationType()) {
      case TRANSFORMATION_TYPE_REPLACE:
        List<String> inputs = new ArrayList<String>();
        inputs.add(normalization.getFindText());
        inputs.add(normalization.getReplaceText());
        entitySpclValues.add(normalization.getType());
        entityValues.addAll(inputs);
        
        break;
      case TRANSFORMATION_TYPE_LOWERCASE:
      case TRANSFORMATION_TYPE_UPPERCASE:
      case TRANSFORMATION_TYPE_PROPERCASE:
      case TRANSFORMATION_TYPE_TRIM:
        normalization.setBaseType(CommonConstants.NORMALIZATION_BASE_TYPE);
        break;
      case TRANSFORMATION_TYPE_ATTRIBUTEVALUE:
        normalization.getBaseType();
        entityValues.add(normalization.getValueAttributeId());
        break;
      case TRANSFORMATION_TYPE_SUBSTRING:
        List<String> indices = new ArrayList<String>();
        indices.add(String.valueOf(normalization.getStartIndex()));
        indices.add(String.valueOf(normalization.getEndIndex()));
        entityValues.addAll(indices);
        break;
      case TRANSFORMATION_TYPE_VALUE:
        // TODO condition are based on subtype and subtype is missing in pxon
        entityValues.addAll(normalization.getValues());
        // }
        
        normalization.getBaseType();
        break;
      case TRANSFORMATION_TYPE_CONCAT:
        List<String> concatValues = new ArrayList<String>();
        List<String> concatSpecificType = new ArrayList<String>();
        
        List<IJSONContent> listOfConcatobjs = normalization.getAttributeConcatenatedList();
        
        for (int i = 0; i < listOfConcatobjs.size(); i++) {
          JSONObject setConcatObj = (JSONObject) listOfConcatobjs.get(i);
          concatSpecificType.add((String) setConcatObj.get(ENTITY_TYPE));
          
          // concat entitySpecialValue can be html or attribute
          if (setConcatObj.get(ENTITY_TYPE)
              .equals(ENTITY_SPECIAL_VALUE_HTML)) {
            concatValues.add((String) setConcatObj.get(VALUE));
          }
          else {
            concatValues.add((String) setConcatObj.get(ATTRIBUTE_ID));
          }
        }
        entityValues.addAll(concatValues);
        entitySpclValues.addAll(concatSpecificType);
        normalization.setAttributeConcatenatedList(listOfConcatobjs);
        normalization.getBaseType();
        break;
    }
  }
  transformedRuleMap.put(SAN_EFFECT_ENTITY_VALUE, String.join(SEPARATOR, entityValues));
  transformedRuleMap.put(SAN_EFFECT_TRANSFORMATION_TYPE,
      String.join(SEPARATOR, transformationType));
  transformedRuleMap.put(SAN_EFFECT_ENTITY_SPECIAL_VALUE, String.join(SEPARATOR, entitySpclValues));
}


protected void fillLevelInfo(Map<String, Object> transformedToxnomyMap,
    ConfigClassifierDTO configClassifierDTO)
{
  transformedToxnomyMap.put(LEVEL_CODES_COLUMN, getSepratedValues(configClassifierDTO.getLevelCodes()));
  transformedToxnomyMap.put(LEVEL_LABEL_COLUMN, getSepratedValues(configClassifierDTO.getLevelLables()));
  transformedToxnomyMap.put(IS_NEWLY_CREATED_LEVEL_COLUMN, String.join(SEPARATOR,
      configClassifierDTO.getIsNewlyCreated().stream().map(mapper -> mapper == true ? TRUE : FALSE).collect(Collectors.toList())));
}

protected String getSepratedValues(Collection<String> values)
{
  return String.join(SEPARATOR, values);
}

protected Object getDefaultValue(IConfigSectionElementDTO obj)
{
  return obj.getDefaultValue();
}

}