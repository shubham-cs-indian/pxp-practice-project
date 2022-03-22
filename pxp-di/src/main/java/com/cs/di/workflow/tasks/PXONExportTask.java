package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.cs.config.idto.IConfigTranslationDTO.EntityType;
import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.dataintegration.dto.PXONExportPlanDTO;
import com.cs.core.dataintegration.dto.PXONExportScopeDTO;
import com.cs.core.dataintegration.idto.IPXONExportScopeDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IRootConfigDTO;
import com.cs.core.rdbms.config.idto.IRootConfigDTO.ItemType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.ICSEElement.ExportFormat;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.ConfigNodeType;
import com.cs.di.workflow.constants.ExportSubType;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.workflow.base.TaskType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component("PXONExportTask")
@SuppressWarnings("unchecked")
public class PXONExportTask extends AbstractBGPTask {
  
  protected static final String    BASE_ENTITY_IIDS          = "baseEntityIIds";
  protected static final String    PXON_FILE_PATH            = "PXON_FILE_PATH";
  protected static final String    CONFIG_PROPERTY_TYPE      = "CONFIG_PROPERTY_TYPE";
  protected static final String    CONFIG_ENTITY_CODES       = "CONFIG_ENTITY_CODES";
  protected static final String    EXPORT_TYPE               = "EXPORT_TYPE";
  protected static final String    PRODUCT                   = "product";
  protected static final String    ENTITY                    = "entity";
  private static final String      TRANSLATION               = "TRANSLATION";
  protected static final String    EXPORT_SUB_TYPE           = "exportSubType";
  protected static final String    SELECTED_TAXONOMY_IDS     = "selectedTaxonomyIds";
  protected static final String    SELECTED_TYPES            = "selectedTypes";
  protected static final String    SEARCH_CRITERIA           = "SEARCH_CRITERIA";
  protected static final String    LANGUAGE_CODE             = "LANGUAGE_CODE";
  protected static final String    SELECTED_BASE_TYPES       = "selectedBaseTypes";
  protected static final String    SUCCESS_IIDS              = "SUCCESS_IIDS";
  protected static final String    FAILED_IIDS               = "FAILED_IIDS";
  protected static final String    COLLECTION_ID             = "collectionId";
  protected static final String    BOOKMARK_ID               = "bookmarkId";
  protected static final String    PERMISSION                = "PERMISSION";
  protected static final String    MANUAL_SEARCH_CRITERIA    = "MANUAL_SEARCH_CRITERIA";
  
  
  public static final List<String> INPUT_LIST            = Arrays.asList(MANUAL_SEARCH_CRITERIA, PRIORITY, EXPORT_TYPE, "BASE_ENTITY_IIDS", SEARCH_CRITERIA, LANGUAGE_CODE, CONFIG_PROPERTY_TYPE, CONFIG_ENTITY_CODES);
  public static final List<String> OUTPUT_LIST           = Arrays.asList(JOB_ID, PXON_FILE_PATH, EXECUTION_STATUS, SUCCESS_IIDS, FAILED_IIDS);

	@Override
	public void executeTask(WorkflowTaskModel model) {
		IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = model.getExecutionStatusTable();
		try {
			model.getInputParameters().put(SERVICE, "PXON_EXPORT");
			String prior = model.getInputParameters().get(PRIORITY).toString().toUpperCase();
      model.getInputParameters().put(PRIORITY, BGPPriority.valueOf(prior));
			long jobIID = executeBGPService(model);
			model.getOutputParameters().put(PXON_FILE_PATH,
					getBGPSharedFolderpath() + "export#" + jobIID + ".pxon");
		} catch (CSFormatException | CSInitializationException | RDBMSException | JsonProcessingException e) {
			RDBMSLogger.instance().exception(e);
			executionStatusTable.addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN500,
					new String[] {});
		}
	}

  @Override
  protected String prepareInputDTO(WorkflowTaskModel model)
      throws CSFormatException, RDBMSException, JsonProcessingException
  {
    IPXONExportScopeDTO exportScopeDTO = new PXONExportScopeDTO();
    String exportType = DiValidationUtil.validateAndGetRequiredString(model, EXPORT_TYPE);
    if (exportType.equals(PRODUCT)) {
      prepareRuntimeExportScopeDTO(model, exportScopeDTO);
    }
    else {
      prepareConfigExportScopeDTO(model, exportScopeDTO);
    }
    
    return new PXONExportPlanDTO(exportScopeDTO).toJSON();
  }

  /** 
   * Crate exportScopeDTO for runtime products.
   * 
   * @param model
   * @param exportScopeDTO
   * @throws JsonProcessingException 
   * @throws JsonMappingException 
   */
  protected void prepareRuntimeExportScopeDTO(WorkflowTaskModel model, IPXONExportScopeDTO exportScopeDTO) throws JsonProcessingException
  {
    List<String> baseEntityIIDs = new ArrayList<>();
    List<String> selectedTaxonomyIds = new ArrayList<>();
    List<String> selectedTypes = new ArrayList<>();
    List<String> selectedBaseTypes = new ArrayList<>();
    ITransactionData transactionData = model.getWorkflowModel().getTransactionData();
    Boolean isManualSearchCriteria = DiValidationUtil.validateAndGetRequiredBoolean(model, MANUAL_SEARCH_CRITERIA);
    String criteria = DiValidationUtil.validateAndGetRequiredString(model, SEARCH_CRITERIA);
    Map<String, Object> searchCriteriaMap = new ObjectMapper().readValue(criteria, HashMap.class);
    String exportSubTypeVal = (String) searchCriteriaMap.get(EXPORT_SUB_TYPE);
    ExportSubType exportSubType = Enum.valueOf(ExportSubType.class, exportSubTypeVal);
    String exportDataLanguage = DiValidationUtil.validateAndGetOptionalString(model, LANGUAGE_CODE);
    String collectionId = (String) searchCriteriaMap.get(COLLECTION_ID);
    String bookmarkId = (String) searchCriteriaMap.get(BOOKMARK_ID);
    if (exportDataLanguage == null) {
      exportDataLanguage = transactionData.getDataLanguage();
    }
    exportScopeDTO.setLocaleCatalog(exportDataLanguage, transactionData.getPhysicalCatalogId(), transactionData.getOrganizationId(),
        Arrays.asList(exportDataLanguage));
    exportScopeDTO.setCSEFormat(ExportFormat.SYSTEM, "!$");
    exportScopeDTO.includeEmbeddedEntities(true);
    exportScopeDTO.includeAllEntities(exportSubType == ExportSubType.EXPORT_ALL ? true : false);
    
    
    if (exportSubType == ExportSubType.EXPORT_SELECTED) {
      Object param;
      if(isManualSearchCriteria)
        param = model.getInputParameters().get("BASE_ENTITY_IIDS");
      else
        param = searchCriteriaMap.get(BASE_ENTITY_IIDS);
        
      if (param instanceof Collection<?>) {
        baseEntityIIDs = (List<String>) param;
      }
      else if (param instanceof String) {
        try {
          baseEntityIIDs = new ObjectMapper().readValue(param.toString(), List.class);
        }
        catch (JsonProcessingException e) {
          model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005,
              new String[] { BASE_ENTITY_IIDS });
        }
      }
      exportScopeDTO.setBaseEntityIIDs(baseEntityIIDs.stream().map(Long::valueOf).collect(Collectors.toList()));
    }
    else if (exportSubType == ExportSubType.TAXONOMY_BASED_EXPORT) {
      List<String> taxonomyIds = (List<String>) searchCriteriaMap.get(SELECTED_TAXONOMY_IDS);
      if (taxonomyIds != null) {
        selectedTaxonomyIds.addAll(taxonomyIds);
      }
      List<String> selectedTypeIds = (List<String>) searchCriteriaMap.get(SELECTED_TYPES);
      if (selectedTypeIds != null) {
        selectedTypes.addAll(selectedTypeIds);
      }
    }

    String partnerAuthorizationId = (String) model.getWorkflowModel().getWorkflowParameterMap().get(IEndpointModel.AUTHORIZATION_MAPPING);
    exportScopeDTO.setPartnerAuthorizationId(partnerAuthorizationId);
    selectedBaseTypes.addAll((List<String>) searchCriteriaMap.get(SELECTED_BASE_TYPES));
    Map<String, Object> searchCriteria = new HashMap<>();
    searchCriteria.put(BASE_ENTITY_IIDS, baseEntityIIDs);
    searchCriteria.put(EXPORT_SUB_TYPE, exportSubTypeVal);
    searchCriteria.put(SELECTED_BASE_TYPES, selectedBaseTypes);
    searchCriteria.put(SELECTED_TAXONOMY_IDS, selectedTaxonomyIds);
    searchCriteria.put(SELECTED_TYPES, selectedTypes);
    searchCriteria.put(COLLECTION_ID, collectionId);
    searchCriteria.put(BOOKMARK_ID, bookmarkId);
    exportScopeDTO.setSearchCriteria(searchCriteria);
  }


  /**
   * Create exportScopeDTO for config entities.
   * 
   * @param model
   * @param exportScopeDTO
   */
  protected void prepareConfigExportScopeDTO(WorkflowTaskModel model, IPXONExportScopeDTO exportScopeDTO)
  {
    String configType = DiValidationUtil.validateAndGetRequiredString(model, CONFIG_PROPERTY_TYPE);
    String configNode = null;
    if (configType.startsWith(TRANSLATION)) {
      String[] types = configType.split("_", 2);
      configNode = types[0];
      String entityType = null;
      if (CommonConstants.PROCESS.equals(types[1])) {
        entityType = CommonConstants.PROCESS_EVENT;
      }
      else {
        entityType = types[1];
      }
      exportScopeDTO.setEnitytType(EntityType.valueOfEntityType(entityType));
    }
    else {
      configNode = configType;
    }
    
    Object param = model.getInputParameters().get(CONFIG_ENTITY_CODES);
    List<String> configEntityCodes = null;
    if(param instanceof Collection<?>) {
      configEntityCodes = (List<String>) DiValidationUtil.validateAndGetOptionalCollection(model, CONFIG_ENTITY_CODES);
    }else if(param instanceof String) {
      String codes = param.toString();
      try {
        configEntityCodes = new ObjectMapper().readValue(codes, List.class);
      }
      catch (JsonProcessingException e) {
        model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005,
            new String[] { CONFIG_ENTITY_CODES });
      }
    }
    ConfigNodeType configNodeType = Enum.valueOf(ConfigNodeType.class, configNode.toUpperCase());
    String dataLanguage = model.getWorkflowModel().getTransactionData().getDataLanguage();
    exportScopeDTO.setLocaleCatalog(dataLanguage, "", "", Arrays.asList(dataLanguage));
    exportScopeDTO.setCSEFormat(ExportFormat.SYSTEM, "!$");
    exportScopeDTO.includeAllEntities(false);
    exportScopeDTO.includeEmbeddedEntities(false);
    if (!CollectionUtils.isEmpty(configEntityCodes)) {
      Map<IRootConfigDTO.ItemType, Collection<String>> configItemMap = new HashMap<>();
      configItemMap.put(getItemTypeByconfigType(configNodeType), configEntityCodes);
      exportScopeDTO.setConfigItemByCodes(configItemMap);
    }
    else {
      prepareAndSetItemTypes(configNodeType, exportScopeDTO);
    }
  }

  /**
   * Prepare property item type in case of export all.
   * 
   * @param configType
   * @param exportScopeDTO
   */
  protected void prepareAndSetItemTypes(ConfigNodeType configType, IPXONExportScopeDTO exportScopeDTO)
  {
    switch (configType) {
      case ATTRIBUTE:
        exportScopeDTO.setPropertyItemTypes(Arrays.asList(IPropertyDTO.PropertyType.AUTO, IPropertyDTO.PropertyType.CALCULATED,
            IPropertyDTO.PropertyType.CONCATENATED, IPropertyDTO.PropertyType.DATE, IPropertyDTO.PropertyType.MEASUREMENT,
            IPropertyDTO.PropertyType.HTML, IPropertyDTO.PropertyType.NUMBER, IPropertyDTO.PropertyType.PRICE,
            IPropertyDTO.PropertyType.TEXT, IPropertyDTO.PropertyType.ASSET_ATTRIBUTE));
        break;
      case TAG:
        exportScopeDTO.setPropertyItemTypes(Arrays.asList(IPropertyDTO.PropertyType.TAG, IPropertyDTO.PropertyType.BOOLEAN));
        break;
      case RELATIONSHIP:
        exportScopeDTO.setPropertyItemTypes(Arrays.asList(IPropertyDTO.PropertyType.RELATIONSHIP, IPropertyDTO.PropertyType.NATURE_RELATIONSHIP));
        break;
      case PROPERTY_COLLECTION:
        exportScopeDTO.setConfigItemTypes(Arrays.asList(ItemType.PROPERTY_COLLECTION));
        break;
      case ARTICLE:
        exportScopeDTO.setConfigItemTypes(Arrays.asList(ItemType.CLASSIFIER));
        exportScopeDTO.setEnitytType(EntityType.ARTICLE);
        break;
      case ASSET:
        exportScopeDTO.setConfigItemTypes(Arrays.asList(ItemType.CLASSIFIER));
        exportScopeDTO.setEnitytType(EntityType.ASSET);
      break;
      case SUPPLIER:
        exportScopeDTO.setConfigItemTypes(Arrays.asList(ItemType.CLASSIFIER));
        exportScopeDTO.setEnitytType(EntityType.SUPPLIER);
      break;
      case TEXTASSET:
        exportScopeDTO.setConfigItemTypes(Arrays.asList(ItemType.CLASSIFIER));
        exportScopeDTO.setEnitytType(EntityType.TEXT_ASSET);
      break;
      // TODO: PXPFDEV-21451 : Deprecate - Taxonomy Hierarchies
      /*case VIRTUALCATALOG:
        exportScopeDTO.setConfigItemTypes(Arrays.asList(ItemType.CLASSIFIER));
        exportScopeDTO.setEnitytType(EntityType.VIRTUAL_CATALOG);
      break;*/
   /*  PXPFDEV-21215 : Deprecate - Taxonomy Hierarchies
    * case HIERARCHY:
        exportScopeDTO.setConfigItemTypes(Arrays.asList(ItemType.HIERARCHY_TAXONOMY));
        break;
        */
      case MASTERTAXONOMY:
        exportScopeDTO.setConfigItemTypes(Arrays.asList(ItemType.MASTER_TAXONOMY));
        break;
      case TASK:
        exportScopeDTO.setConfigItemTypes(Arrays.asList(ItemType.TASK));
        break;
      case USER:
        exportScopeDTO.setConfigItemTypes(Arrays.asList(ItemType.USER));
        break;
      case TRANSLATION:
        exportScopeDTO.setConfigItemTypes(Arrays.asList(ItemType.TRANSLATION));
        break;
      case CONTEXT:
        exportScopeDTO.setConfigItemTypes(Arrays.asList(ItemType.CONTEXT));
        break;
      case RULE:
        exportScopeDTO.setConfigItemTypes(Arrays.asList(ItemType.RULE));
        break;
      case GOLDENRECORDRULE:
        exportScopeDTO.setConfigItemTypes(Arrays.asList(ItemType.GOLDEN_RECORD_RULE));
        break;
      case PARTNER:
        exportScopeDTO.setConfigItemTypes(Arrays.asList(ItemType.ORGANIZATION));
        break;
      case TAB:
        exportScopeDTO.setConfigItemTypes(Arrays.asList(ItemType.TAB));
        break;
      case LANGUAGE:
        exportScopeDTO.setConfigItemTypes(Arrays.asList(ItemType.LANGUAGE));
        break;
      case PERMISSION:
        exportScopeDTO.setConfigItemTypes(Arrays.asList(ItemType.PERMISSION));
        break;
      default:
        break;
    }
  }
  
  /**
   * Return item type by node type.
   * 
   * @param configNodeType
   * @return
   */
  protected ItemType getItemTypeByconfigType(ConfigNodeType configNodeType)
  {
    switch (configNodeType) {
      case ATTRIBUTE:
      case TAG:
      case RELATIONSHIP:
      case REFERENCE:
        return IRootConfigDTO.ItemType.PROPERTY;
      case TASK:
        return IRootConfigDTO.ItemType.TASK;
      case USER:
        return IRootConfigDTO.ItemType.USER;
      case TRANSLATION:
        return IRootConfigDTO.ItemType.TRANSLATION;
      case CONTEXT:
        return IRootConfigDTO.ItemType.CONTEXT;
      case RULE:
        return IRootConfigDTO.ItemType.RULE;
      case GOLDENRECORDRULE:
        return IRootConfigDTO.ItemType.GOLDEN_RECORD_RULE;
      case PARTNER:
        return IRootConfigDTO.ItemType.ORGANIZATION;
      case TAB:
        return IRootConfigDTO.ItemType.TAB;
      case PERMISSION:
        return IRootConfigDTO.ItemType.PERMISSION;
      default:
        return null;
    }
  }
  
  /**
	 * Get shared folder path
	 * 
	 * @return
	 */
	private static String getBGPSharedFolderpath() {
		try {
			return CSProperties.instance().getString("bgp.PXON_EXPORT.filePath");
		} catch (CSInitializationException e) {
			RDBMSLogger.instance().exception(e);
		}
		return null;
	}

	@Override
	public List<String> getInputList() {
		return INPUT_LIST;
	}

	@Override
	public List<String> getOutputList() {
		return OUTPUT_LIST;
	}

	@Override
	public TaskType getTaskType() {
		return TaskType.BGP_TASK;
	}

	/**
	 * Validate input parameters
	 * 
	 * @param inputFields
	 */	
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> returnList = new ArrayList<>();
    String exportType = (String) inputFields.get(EXPORT_TYPE);
    if (exportType == null) {
      returnList.add(EXPORT_TYPE);
    }
    else if (exportType.equals(PRODUCT)) {
      String inputSearchCriteria = (String) inputFields.get(SEARCH_CRITERIA);
      if (inputSearchCriteria == null) {
        returnList.add(SEARCH_CRITERIA);
      }
      else if (!isRuntimeValue(inputSearchCriteria)) {
        try {
          new ObjectMapper().readValue(inputSearchCriteria, HashMap.class);
        }
        catch (JsonProcessingException e) {
          returnList.add(SEARCH_CRITERIA);
        }
      }
    }
    else if (exportType.equals(ENTITY)) {
      String propertyType = (String) inputFields.get(CONFIG_PROPERTY_TYPE);
      if (DiValidationUtil.isBlank(propertyType)) {
        returnList.add(CONFIG_PROPERTY_TYPE);
      }
      /* Validation required for CONFIG_ENTITY_CODES when CONFIG_PROPERTY_TYPE
       is PERMISSION */
      if (propertyType.equals(PERMISSION)) {
        String entityCodes = (String) inputFields.get(CONFIG_ENTITY_CODES);
        List<String> configEntityCodes = new ArrayList<String>();
        try {
          configEntityCodes = ObjectMapperUtil.readValue(entityCodes, List.class);
        }
        catch (Exception e) {
          returnList.add(CONFIG_ENTITY_CODES);
        }
        if (configEntityCodes.isEmpty() || configEntityCodes.size() > 1) {
          returnList.add(CONFIG_ENTITY_CODES);
        }
      }
    }
    // Note: Validation not required for :
    // CONFIG_CODES, JOB_ID, EXECUTION_STATUS as they are optional output
    // parameters
    return returnList;
  }
}
