package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hamcrest.core.IsInstanceOf;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.cs.config.dto.ConfigAttributeDTO;
import com.cs.config.dto.ConfigClassifierDTO;
import com.cs.config.dto.ConfigContextDTO;
import com.cs.config.dto.ConfigLanguageDTO;
import com.cs.config.dto.ConfigOrganizationDTO;
import com.cs.config.dto.ConfigPropertyCollectionDTO;
import com.cs.config.dto.ConfigRelationshipDTO;
import com.cs.config.dto.ConfigTabDTO;
import com.cs.config.dto.ConfigTagDTO;
import com.cs.config.dto.ConfigTranslationDTO;
import com.cs.config.idto.IConfigLanguageDTO;
import com.cs.config.idto.IConfigOrganizationDTO;
import com.cs.config.idto.IConfigRoleDTO;
import com.cs.config.dto.ConfigDataRuleDTO;
import com.cs.config.dto.ConfigGoldenRecordRuleDTO;
import com.cs.config.dto.ConfigTaskDTO;
import com.cs.config.idto.IConfigContextDTO;
import com.cs.config.idto.IConfigDataRuleIntermediateEntitysDTO;
import com.cs.config.idto.IConfigDataRuleTagRuleDTO;
import com.cs.config.idto.IConfigDataRuleTagsDTO;
import com.cs.config.idto.IConfigMergeEffectTypeDTO;
import com.cs.config.idto.IConfigNormalizationDTO;
import com.cs.config.idto.IConfigRuleEntityDTO;
import com.cs.config.idto.IConfigSectionElementDTO;
import com.cs.config.idto.IConfigSideRelationshipDTO;
import com.cs.config.idto.IConfigTabDTO;
import com.cs.config.idto.IConfigTagValueDTO;
import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.standard.IConfigClass;
import com.cs.config.standard.IStandardConfig.TagType;
import com.cs.constants.CommonConstants;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.rdbms.config.dto.RootConfigDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.icsexpress.definition.ICSEObject;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.di.runtime.utils.DiTransformationUtils;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.CalculatedAttributeTypes;
import com.cs.di.workflow.constants.DiConstants;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

/**
 * Conversion from PXON to JSON for Config data Outbound/Export Transformation
 * 
 * @author annu.singh
 *
 */
@Component("configPXONToJSONTask")
@SuppressWarnings("unchecked")
public class ConfigPXONToJSONTask extends AbstractConfigFromPXONTask {
  
  private String                         SEPARATOR             = ",";
  private String                         CHILDTAG              = "Child_Tag";
  public static final List<EventType>    EVENT_TYPES           = Arrays
      .asList(EventType.BUSINESS_PROCESS);
  public static final List<String>       INPUT_LIST            = Arrays.asList(PXON_FILE_PATH);
  public static final List<String>       OUTPUT_LIST           = Arrays.asList(TRANSFORMED_DATA,
      EXECUTION_STATUS, FAILED_FILES);
  public static final List<WorkflowType> WORKFLOW_TYPES        = Arrays
      .asList(WorkflowType.STANDARD_WORKFLOW);
  public static final String             SECTION_PROPERTIES    = "Section_Properties";
  public static final String             VARIANTS              = "Variants";
  public static final String             RELATIONSHIPS         = "Relationships";
  public static final String             TAGS                  = "tags";
  private String                         VALUE              = "VALUE";
  
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
   * Prepare Map from PXON files.
   * 
   * @param workflowTaskModel
   */
  @Override
  public void generateFromPXON(WorkflowTaskModel workflowTaskModel)
  {
    Map<String, Object> transformedJSONMap = new LinkedHashMap<>();
    fillKeysWithEmptyValues(transformedJSONMap);
    String path = (String) DiValidationUtil.validateAndGetRequiredFilePath(workflowTaskModel, PXON_FILE_PATH);
    List<String> failedfiles = new ArrayList<>();
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
                   generateConfigKlassJson(transformedJSONMap, configClassDTO);
                  break;
         /*      TODO: PXPFDEV-21215: Need to remove code  
                 case HIERARCHY:
                  generateConfigHierarchyTaxonomyJson(transformedJSONMap, configClassDTO);
                  break;
          */      
                case TAXONOMY:
                  generateConfigMasterTaxonomyJson(transformedJSONMap, configClassDTO);
                  break;

                default:
                  break;
              }
              break;
            case Property:
              PropertyType propertyType = IPropertyDTO.PropertyType.valueOf(specificationType);
              IPropertyDTO.SuperType propertySuperType = propertyType.getSuperType();
              switch (propertySuperType) {
                case ATTRIBUTE:
                  // Attribute
                  ConfigAttributeDTO attributeDTO = new ConfigAttributeDTO();
                  attributeDTO.fromPXON(blockInfo.getData());
                  generateConfigAttributeJSON(transformedJSONMap, attributeDTO, workflowTaskModel);
                  break;
                case TAGS:
                  // Tag
                  ConfigTagDTO tagDTO = new ConfigTagDTO();
                  tagDTO.fromPXON(blockInfo.getData());
                  generateConfigTagJSON(transformedJSONMap, tagDTO, workflowTaskModel);
                  break;
                case RELATION_SIDE:
                  // Relationship
                  ConfigRelationshipDTO configRelationshipDTO = new ConfigRelationshipDTO();
                  configRelationshipDTO.fromPXON(blockInfo.getData());
                  generateConfigRelationshipJSON(transformedJSONMap, configRelationshipDTO, workflowTaskModel);
                default:
                  break;
              }
              break;
              
            case PropertyCollection:
              ConfigPropertyCollectionDTO configPropertyCollDTO = new ConfigPropertyCollectionDTO();
              configPropertyCollDTO.fromPXON(blockInfo.getData());
              generateConfigPropertyCollectionJSON(transformedJSONMap, configPropertyCollDTO, workflowTaskModel);
              break;
              
            case Tab:
              //Tab 
              IConfigTabDTO tabDTO = new ConfigTabDTO();
              tabDTO.fromPXON(blockInfo.getData());
              generateConfigTabJSON(transformedJSONMap, tabDTO, workflowTaskModel);
              break;

            case Translation:
              ConfigTranslationDTO translationDTO = new ConfigTranslationDTO();
              translationDTO.fromPXON(blockInfo.getData());
              generateConfigTranslationJSON(transformedJSONMap, translationDTO);
              break;
            
            case LanguageConf:
              IConfigLanguageDTO languageDTO = new ConfigLanguageDTO();
              languageDTO.fromPXON(blockInfo.getData());
              generateConfigLanguageJSON(transformedJSONMap, languageDTO);
              break;
            
            case Organization:
              IConfigOrganizationDTO organizationDTO = new ConfigOrganizationDTO();
              organizationDTO.fromPXON(blockInfo.getData());
              generateConfigOrganizationJSON(transformedJSONMap, organizationDTO);
              break;

            case Task:
              ConfigTaskDTO configTaskDTO = new ConfigTaskDTO();
              configTaskDTO.fromPXON(blockInfo.getData());
              generateConfigTaskJSON(transformedJSONMap,configTaskDTO,workflowTaskModel);
              break;
              
            case Golden_Rule:
              ConfigGoldenRecordRuleDTO configGoldenRecordRuleDTO = new ConfigGoldenRecordRuleDTO();
              configGoldenRecordRuleDTO.fromPXON(blockInfo.getData());
              generateConfigGoldenRecordRuleJSON(transformedJSONMap, configGoldenRecordRuleDTO, workflowTaskModel);
              break;
              
            case Rule:
              ConfigDataRuleDTO dataRuleDTO = new ConfigDataRuleDTO();
              dataRuleDTO.fromPXON(blockInfo.getData());
              generateConfigDataRuleJSON(transformedJSONMap, dataRuleDTO, workflowTaskModel);
              break;
              
            case Context:
              IConfigContextDTO contextDTO = new ConfigContextDTO();
              contextDTO.fromPXON(blockInfo.getData());
              generateConfigContextJSON(transformedJSONMap, contextDTO);
              break;

            default:
              throw new CSInitializationException("Config Type " + cseObjectType.toString());
            
          }
        }
      }
      workflowTaskModel.getOutputParameters().put(FAILED_FILES, failedfiles);
      // Remove empty config entities keys.
      removeEmptyKeys(transformedJSONMap);
      Gson gson = new Gson();
      String json = gson.toJson(transformedJSONMap);
      workflowTaskModel.getOutputParameters().put(TRANSFORMED_DATA, json);
    }
    catch (CSInitializationException csInitializationException) {
      workflowTaskModel.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN038,
          new String[] { csInitializationException.getMessage() });
    }
    catch (IllegalArgumentException illegalArgumentExceptione) {
      workflowTaskModel.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN037,
          new String[] { illegalArgumentExceptione.getMessage() });
    }
    catch (Exception exception) {
      RDBMSLogger.instance().exception(exception); 
      failedfiles.add(path);
      workflowTaskModel.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.MESSAGE_TYPE }, MessageCode.GEN009, new String[] { path });
    }
    
  }

  /**
   * @param transformedJSONMap
   */
  private void removeEmptyKeys(Map<String, Object> transformedJSONMap)
  {
    Iterator<Entry<String, Object>> iterator = transformedJSONMap.entrySet().iterator();
    while (iterator.hasNext()) {
      Entry<String, Object> next = iterator.next();
      Object transformationValue = next.getValue();
      if (transformationValue instanceof Map) {
        Map<?, ?> map = (Map<?, ?>) transformationValue;
        if (map.isEmpty()) {
          iterator.remove();
        }
      }
      else if (transformationValue instanceof List) {
        List<?> temp = (ArrayList<?>) transformationValue;
        if (temp.isEmpty()) {
          iterator.remove();
        }
      }
    }
  }

  /**
   * 
   * @param transformedJSONMap
   */
    private void fillKeysWithEmptyValues(Map<String, Object> transformedJSONMap)
  {
    transformedJSONMap.put(KLASS_SHEET_NAME, new ArrayList<>());
    transformedJSONMap.put(HIERARCHY_SHEET_NAME, new ArrayList<>());
    transformedJSONMap.put(MASTER_TAXONOMY_SHEET_NAME, new ArrayList());
    transformedJSONMap.put(ITransformationTask.TASK_COLUMN, new ArrayList());
    transformedJSONMap.put(ITransformationTask.DATA_RULES_SHEET_NAME, new ArrayList());
    transformedJSONMap.put(ITransformationTask.GOLDEN_RECORD_RULES_SHEET_NAME, new ArrayList());
    transformedJSONMap.put(ITransformationTask.ATTRIBUTE_SHEET_NAME, new ArrayList<>());
    transformedJSONMap.put(ITransformationTask.TAG_SHEET_NAME, new ArrayList<>());
    transformedJSONMap.put(ITransformationTask.PROPERTY_COLLECTION_SHEET_NAME, new ArrayList<>());
  }
  
  /**
   * generate excel for Data Rules
   * @param transformedExcelMap
   * @param dataRuleDTO
   * @param workflowTaskModel
   * @throws CSFormatException 
   * @throws JsonProcessingException 
   */
  private void generateConfigDataRuleJSON(Map<String, Object> transformedJSONMap,
      ConfigDataRuleDTO dataRuleDTO, WorkflowTaskModel workflowTaskModel) throws CSFormatException, JsonProcessingException
  {
    if (transformedJSONMap.get(ITransformationTask.DATA_RULES_SHEET_NAME) == null) {
      transformedJSONMap.put(ITransformationTask.DATA_RULES_SHEET_NAME, new ArrayList<>());
    }
    List<Map<String, Object>> ruleList = (List<Map<String, Object>>) transformedJSONMap.get(DATA_RULES_SHEET_NAME);
    Map<String, Object> transformedRuleMap = new LinkedHashMap<>();
    transformedRuleMap.put(CODE_COLUMN_CONFIG, dataRuleDTO.getCode());
    transformedRuleMap.put(LABEL_COLUMN_CONFIG, dataRuleDTO.getLabel());
    transformedRuleMap.put(TYPE_COLUMN, dataRuleDTO.getType());
    transformedRuleMap.put(LANGUAGE_DEPENDENT_COLUMN, dataRuleDTO.getIsLanguageDependent() ? "1" : "0");
    transformedRuleMap.put(LANGUAGES,getSepratedValues(dataRuleDTO.getLanguages()));
    transformedRuleMap.put(PARTNERS,getSepratedValues(dataRuleDTO.getOrganizations()));
    transformedRuleMap.put(PHYSICALCATALOGS,getSepratedValues( dataRuleDTO.getPhysicalCatalogIds()));
    transformedRuleMap.put(IS_STANDARD, dataRuleDTO.getIsStandard() ? "1" : "0");
    List<IConfigNormalizationDTO> iConfigNormalizationDTOList = dataRuleDTO.getNormalizations();
    List<Object> normalisationdata = new ArrayList<Object>();
    switch (dataRuleDTO.getType()) {     
      case TYPE_CLASSIFICATION:
        transformedRuleMap.put(CLASSIFICATION_CAUSE_KLASSES,getSepratedValues(dataRuleDTO.getTypes()));
        transformedRuleMap.put(CLASSIFICATION_CAUSE_TAXONOMIES,getSepratedValues( dataRuleDTO.getTaxonomies()));
        transformedRuleMap.put(ITransformationTask.ATTRIBUTE_SHEET_NAME, new ArrayList<Object>());
        transformedRuleMap.put(ITransformationTask.TAG_SHEET_NAME, new ArrayList<Object>());
        transformedRuleMap.put(RULE_VIOLATION, new ArrayList<Object>());
        for (IConfigNormalizationDTO iConfigNormalizationDTO : iConfigNormalizationDTOList) {
          if (iConfigNormalizationDTO.getType().equals(KLASS_TYPE)) {
            prepareNormalisationForClassification(normalisationdata, iConfigNormalizationDTO);
          }
          else if (iConfigNormalizationDTO.getType().equals(TAXONOMY)) {
            prepareNormalisationForClassification(normalisationdata, iConfigNormalizationDTO);
          }
          transformedRuleMap.put(NORMALISATION, normalisationdata);
        }
        break;
      case TYPE_SAN:
        transformedRuleMap.put(CAUSE_KLASSES, getSepratedValues(dataRuleDTO.getTypes()));
        transformedRuleMap.put(CAUSE_TAXONOMIES,getSepratedValues(dataRuleDTO.getTaxonomies()));    
        fillEffectAndCauseForDataRules(dataRuleDTO,transformedRuleMap);
        break;
      case TYPE_VIO:
        transformedRuleMap.put(CAUSE_KLASSES,getSepratedValues(dataRuleDTO.getTypes()));
        transformedRuleMap.put(CAUSE_TAXONOMIES,getSepratedValues(dataRuleDTO.getTaxonomies() ));
        fillEffectAndCauseForDataRules(dataRuleDTO, transformedRuleMap);
        break;
      default:
        break;

    }

    ruleList.add(transformedRuleMap);
  }

  private void prepareNormalisationForClassification(List<Object> normalisationdata, IConfigNormalizationDTO iConfigNormalizationDTO)
  {
    Map<String,Object> dataTransferProperty = new HashMap<>();
    dataTransferProperty.put(ITransformationTask.CALCULATED_ATTRIBUTE_UNIT_AS_HTML,iConfigNormalizationDTO.getCalculatedAttributeUnitAsHTML());
    dataTransferProperty.put(ITransformationTask.ATTRIBUTE_OPERATOT_LIST,iConfigNormalizationDTO.getAttributeOperatorList());
    dataTransferProperty.put(ITransformationTask.CODE_COLUMN_CONFIG,iConfigNormalizationDTO.getCode());
    dataTransferProperty.put(ITransformationTask.VALUE_AS_HTML,iConfigNormalizationDTO.getValueAsHTML());
    dataTransferProperty.put(ITransformationTask.VALUES,iConfigNormalizationDTO.getValues());
    dataTransferProperty.put(ITransformationTask.CALUCLUATED_ATTRIBUTE_UNIT,iConfigNormalizationDTO.getCalculatedAttributeUnit());
    dataTransferProperty.put(ITransformationTask.TRANSFORMATION_TYPE,iConfigNormalizationDTO.getTransformationType());
    dataTransferProperty.put(ITransformationTask.TAG_VALUES,iConfigNormalizationDTO.getTagValues());
    dataTransferProperty.put(ITransformationTask.TYPE,iConfigNormalizationDTO.getType());
    dataTransferProperty.put(ITransformationTask.BASE_TYPE,iConfigNormalizationDTO.getBaseType());
    normalisationdata.add(dataTransferProperty);
  }
              
  private void fillEffectAndCauseForDataRules(ConfigDataRuleDTO dataRuleDTO,
     Map<String, Object> transformedRuleMap)
  {
    //SAN and VIO Cause side
    Iterator<IConfigDataRuleIntermediateEntitysDTO> attributesIterator = dataRuleDTO.getAttributes().iterator();
    Iterator<IConfigDataRuleTagsDTO> tagsIterator = dataRuleDTO.getTags().iterator();
    //VIO Effect Side
    Iterator<IJSONContent> ViolationsIterator = dataRuleDTO.getRuleViolations().iterator();
    //SAN Effect Side
     Iterator<IConfigNormalizationDTO> normalizationsIterator = dataRuleDTO.getNormalizations().iterator();
    Boolean isFirst = true;
    List<Object> sanEffect = new ArrayList<Object>();
    List<Object> attributeCause = new ArrayList<Object>();
    List<Object> ruleCause = new ArrayList<Object>();
    List<Object> tagCause = new ArrayList<Object>();
    while (attributesIterator.hasNext() || tagsIterator.hasNext()
        || normalizationsIterator.hasNext() || ViolationsIterator.hasNext()) {
      
      if (attributesIterator.hasNext() && isFirst) {
        checkAndFillCauseForAttributes( attributesIterator,attributeCause);
        checkAndFillSANEffect( normalizationsIterator,sanEffect);
        checkAndFillRuleViolations( ViolationsIterator,ruleCause);
        isFirst = false;
      }
      else if (tagsIterator.hasNext() && isFirst) {
        checkAndFillCauseForTags(tagsIterator,tagCause);
        checkAndFillSANEffect(normalizationsIterator,sanEffect);
        checkAndFillRuleViolations( ViolationsIterator,ruleCause);
        isFirst = false;
      }
      else if (normalizationsIterator.hasNext() && isFirst) {
        checkAndFillSANEffect(normalizationsIterator,sanEffect);
        isFirst = false;
      }
      else if (ViolationsIterator.hasNext() && isFirst) {
        checkAndFillRuleViolations(ViolationsIterator,ruleCause);
        isFirst = false;
      }
      else if (attributesIterator.hasNext()) {
        checkAndFillCauseForAttributes( attributesIterator,attributeCause);
        if (normalizationsIterator.hasNext() || ViolationsIterator.hasNext()) {
          checkAndFillSANEffect(normalizationsIterator,sanEffect);
          checkAndFillRuleViolations(ViolationsIterator,ruleCause);
        }
      }
      else if (tagsIterator.hasNext()) 
      {
        checkAndFillCauseForTags(tagsIterator, tagCause);
        if (normalizationsIterator.hasNext() || ViolationsIterator.hasNext()) {
          checkAndFillSANEffect( normalizationsIterator,sanEffect);
          checkAndFillRuleViolations( ViolationsIterator,ruleCause);
        }
      }
      else if (normalizationsIterator.hasNext()) {
        checkAndFillSANEffect(normalizationsIterator,sanEffect);
        if (ViolationsIterator.hasNext()) {
          checkAndFillRuleViolations(ViolationsIterator,ruleCause);
        }
      }
      else if (ViolationsIterator.hasNext()) {
        checkAndFillRuleViolations( ViolationsIterator,ruleCause);
        if (normalizationsIterator.hasNext()) {
          checkAndFillSANEffect( normalizationsIterator,sanEffect);
        }
      }

    }
    transformedRuleMap.put(ITransformationTask.ATTRIBUTE_SHEET_NAME,attributeCause );
    transformedRuleMap.put(ITransformationTask.TAG_SHEET_NAME, tagCause);
    transformedRuleMap.put(NORMALISATION, sanEffect);
    transformedRuleMap.put(RULE_VIOLATION,ruleCause);
  }
  
  
  /**
   * check And Fill RuleViolations
   * 
   * @param transformedRuleMap
   * @param ruleViolations
   */
  private void checkAndFillRuleViolations( Iterator<IJSONContent> ruleViolations,List<Object> ruleCause)
  {
    JSONObject ruleViolation;
    if (ruleViolations.hasNext()) {
      ruleViolation = (JSONObject) ruleViolations.next();
      fillRuleViolation(ruleCause, ruleViolation);
    }
  }
  
  /**
   * Fill Rule Violations in excel
   * @param transformedRuleMap
   * @param ruleViolation
   */
  private void fillRuleViolation(List<Object> rules, JSONObject ruleViolation)
  {
    Map<String,Object> rulesViolation = new HashMap<String,Object>();
    // ruleViolation.getInitField(VERSION_ID, "");
    rulesViolation.put(EFFECT_ENTITY_CODE, (String) ruleViolation.get(ENTITY_ID));
    rulesViolation.put(EFFECT_ENTITY_TYPE, (String) ruleViolation.get(ENTITY_TYPE));
    rulesViolation.put(VIOLATION_EFFECT_COLOR, (String) ruleViolation.get(VIO_COLOR));
    rulesViolation.put(VIOLATION_EFFECT_DESCRIPTION, (String) ruleViolation.get(VIO_DESCRIPTION)); 
    if(ruleViolation.get(ENTITY_TYPE).equals(TAGS)) {
      rulesViolation.put(ENTITY_ATTRIBUTE_TYPE, ruleViolation.get(ENTITY_ATTRIBUTE_TYPE));
    }
    rules.add(rulesViolation);
  }
  
  /**
   * check And Fill SAN Effect
   * @param transformedRuleMap
   * @param normIterator
   */
  private void checkAndFillSANEffect( Iterator<IConfigNormalizationDTO> normIterator,List<Object> sanEffect)
  {
    IConfigNormalizationDTO configNormalizationDTO;
    if (normIterator.hasNext()) {
      configNormalizationDTO = normIterator.next();
      fillSANEffect( configNormalizationDTO,sanEffect);
    }  
  }
  
  /**
   * fill SAN Effect
   * @param transformedRuleMap
   * @param normalization
   */
  private void fillSANEffect( IConfigNormalizationDTO normalization,List<Object> sanEffect)
  {
    List<Object> entityValues = new ArrayList<Object>();
    List<String> transformationType = new ArrayList<String>();
    List<String> entitySpclValues = new ArrayList<String>();
    Map<String,Object> attributEeffect = new HashMap<>();
    Map<String,Object> tagvaluemap = new HashMap<>();
    if (normalization.getType()
        .equals(ENTITY_TAG)) {
      attributEeffect.put(EFFECT_ENTITY_CODE, normalization.getEntityId());
      attributEeffect.put(EFFECT_ENTITY_TYPE, normalization.getType());
      attributEeffect.put(EFFECT_ENTITY_SUBTYPE, normalization.getEntityAttributeType());
      attributEeffect.put(VALUES,normalization.getValues());
      attributEeffect.put(VALUE_AS_HTML,normalization.getValueAsHTML());
      attributEeffect.put(BASE_TYPE, CommonConstants.NORMALIZATION_BASE_TYPE);
      attributEeffect.put(TRANSFORMATION_TYPE,normalization.getTransformationType());
      attributEeffect.put(CALCULATED_ATTRIBUTE_UNIT_AS_HTML,normalization.getCalculatedAttributeUnitAsHTML());
      transformationType.add(normalization.getTransformationType());
      List<IJSONContent> tagValues = normalization.getTagValues();
      for (int i = 0; i < tagValues.size(); i++) {
        JSONObject setConcatObj = (JSONObject) tagValues.get(i);
        tagvaluemap.put(ID_COLUMN_CONFIG, setConcatObj.get(ID_COLUMN));
        tagvaluemap.put(From, setConcatObj.get("from"));
        tagvaluemap.put(To, setConcatObj.get("to"));
        tagvaluemap.put(INNER_TAG_ID, setConcatObj.get("innerTagId"));
        entityValues.add(tagvaluemap);
      }
      attributEeffect.put(TAG_VALUES, entityValues);
     //  entityValues.add(normalization.getT);
    }
    else if(normalization.getType().equals(ENTITY_TYPE)) {
      prepareClassAndTaxonomyEffect(normalization, attributEeffect);
      
    }
    else if(normalization.getType().equals(TAXONOMY)) {
      prepareClassAndTaxonomyEffect(normalization, attributEeffect);
    }
    else if (normalization.getType()
        .equals(ENTITY_ATTRIBUTE)) {
      attributEeffect.put(EFFECT_ENTITY_CODE, normalization.getEntityId());
      attributEeffect.put(EFFECT_ENTITY_TYPE, normalization.getType());
      attributEeffect.put(EFFECT_ENTITY_SUBTYPE, normalization.getEntityAttributeType());
      transformationType.add(normalization.getTransformationType());
      switch (normalization.getTransformationType()) {
        case TRANSFORMATION_TYPE_REPLACE:
          attributEeffect.put(FIND_TEXT, normalization.getFindText());
          attributEeffect.put(REPLACE_TEXT, normalization.getReplaceText());
         // normalization.setBaseType(CommonConstants.FIND_REPLACE_NORMALIZATION_BASE_TYPE);
          attributEeffect.put(BASE_TYPE, CommonConstants.FIND_REPLACE_NORMALIZATION_BASE_TYPE);
          break;
        case TRANSFORMATION_TYPE_LOWERCASE:
        case TRANSFORMATION_TYPE_UPPERCASE:
        case TRANSFORMATION_TYPE_PROPERCASE:
        case TRANSFORMATION_TYPE_TRIM:
          //normalization.setBaseType(CommonConstants.NORMALIZATION_BASE_TYPE);
          attributEeffect.put(BASE_TYPE,CommonConstants.NORMALIZATION_BASE_TYPE);
          break;
        case TRANSFORMATION_TYPE_ATTRIBUTEVALUE:
          normalization.getBaseType();
          attributEeffect.put(BASE_TYPE,CommonConstants.ATTRIBUTE_VALUE_NORMALIZATION_BASE_TYPE);
          entityValues.add(normalization.getValueAttributeId());
          attributEeffect.put(VALUE_ATTRIBUTE_ID, entityValues);
          break;
        case TRANSFORMATION_TYPE_SUBSTRING:
          String start = String.valueOf(normalization.getStartIndex());
          String end = String.valueOf(normalization.getEndIndex());
          attributEeffect.put(START_INDEX, start);
          attributEeffect.put(END_INDEX, end);
          break;
        case TRANSFORMATION_TYPE_VALUE:
          // TODO condition are based on subtype and subtype is missing in pxon
          entityValues.addAll(normalization.getValues());
          attributEeffect.put(VALUES, entityValues);
          // }
          
          //normalization.setBaseType(CommonConstants.ATTRIBUTE_VALUE_NORMALIZATION_BASE_TYPE);
          attributEeffect.put(BASE_TYPE,CommonConstants.ATTRIBUTE_VALUE_NORMALIZATION_BASE_TYPE);
          break;
        case TRANSFORMATION_TYPE_CONCAT:
          List<String> concatValues = new ArrayList<String>();
          List<String> concatSpecificType = new ArrayList<String>();
          
          List<IJSONContent> listOfConcatobjs = normalization.getAttributeConcatenatedList();
          
          for (int i = 0; i < listOfConcatobjs.size(); i++) {
            JSONObject setConcatObj = (JSONObject) listOfConcatobjs.get(i);
            concatValues.add((String) setConcatObj.get(ENTITY_TYPE));
            
            // concat entitySpecialValue can be html or attribute
            if (setConcatObj.get(ENTITY_TYPE)
                .equals(ENTITY_SPECIAL_VALUE_HTML)) {
              concatValues.add((String) setConcatObj.get(VALUE));
              concatValues.add((String)setConcatObj.get(VALUE_AS_HTML));
            }
            else {
              concatValues.add((String) setConcatObj.get(ATTRIBUTE_ID));
            }
          }
         entityValues.addAll(concatValues);
         attributEeffect.put(ATTRIBUTE_CONCATENATED_LIST, entityValues);
          //normalization.setAttributeConcatenatedList(listOfConcatobjs);        
          //normalization.setBaseType(CommonConstants.CONCATENATED_NORMALIZATION_BASE_TYPE);
          attributEeffect.put(BASE_TYPE,CommonConstants.CONCATENATED_NORMALIZATION_BASE_TYPE);
          break;
        default:
          break;
    }

      attributEeffect.put(SAN_EFFECT_ENTITY_VALUE, getSepratedValuesForTag(entityValues));
      attributEeffect.put(SAN_EFFECT_TRANSFORMATION_TYPE,getSepratedValues(transformationType));
      attributEeffect.put(SAN_EFFECT_ENTITY_SPECIAL_VALUE,getSepratedValues( entitySpclValues));
    }  
    sanEffect.add(attributEeffect);
   }

   private void prepareClassAndTaxonomyEffect(IConfigNormalizationDTO normalization, Map<String, Object> attributEeffect)
   {
     attributEeffect.put(VALUES, normalization.getValues());
     attributEeffect.put(CALCULATED_ATTRIBUTE_UNIT_AS_HTML, normalization.getCalculatedAttributeUnitAsHTML());
     attributEeffect.put(VALUE_AS_HTML, normalization.getValueAsHTML());
     attributEeffect.put(TAG_VALUES, normalization.getTagValues());
     attributEeffect.put(BASE_TYPE, CommonConstants.NORMALIZATION_BASE_TYPE);
     attributEeffect.put(TRANSFORMATION_TYPE, normalization.getTransformationType());
   }


  /**
   * fill Cause For Attribute
   * @param transformedRuleMap
   * @param iConfigDataRuleIntermediateEntitysDTO
   */
  private void fillCauseForAttribute(IConfigDataRuleIntermediateEntitysDTO iConfigDataRuleIntermediateEntitysDTO,List<Object> attributeCause)
  {
    Map<String,Object> attributeCauseForRule = new HashMap<>();
    attributeCauseForRule.put(CAUSE_ENTITY_CODE, iConfigDataRuleIntermediateEntitysDTO.getEntityId());
    attributeCauseForRule.put(CAUSE_ENTITY_TYPE, ENTITY_ATTRIBUTE);
    attributeCauseForRule.put(CAUSE_ENTITY_SUBTYPE, iConfigDataRuleIntermediateEntitysDTO.getEntityAttributeType());
    
    List<String> compareTypesList = new ArrayList<String>();
    List<Object> rulesList = new ArrayList<Object>();
    String from = "";
    String to = "";
    Boolean setShouldCompareWithSystemDate = false;

    for (IConfigRuleEntityDTO attributeRuleDTO : iConfigDataRuleIntermediateEntitysDTO.getRules()) {
      Map<String,Object> attributeRuleProperty = new HashMap<>();
      compareTypesList.add(attributeRuleDTO.getType());
      switch (attributeRuleDTO.getType()) {
        case COMPARE_TYPE_CONTAINS:
        case COMPARE_TYPE_START:
        case COMPARE_TYPE_END:
        case COMPARE_TYPE_LT:
        case COMPARE_TYPE_GT:
        case COMPARE_TYPE_EQUAL:
        case COMPARE_TYPE_EXACT:
        case COMPARE_TYPE_NOT_EMPTY:
        case COMPARE_TYPE_EMPTY:
        case COMPARE_TYPE_RANGE:
        case COMPARE_TYPE_REGEX:
        case COMPARE_TYPE_IS_DUPLICATE:
          attributeRuleProperty.put(RULE_LIST_LINKID,attributeRuleDTO.getRuleListLinkId());
          attributeRuleProperty.put(VALUES,attributeRuleDTO.getValues());
          attributeRuleProperty.put(KLASS_LINK_IDS ,attributeRuleDTO.getKlassLinkIds());
          attributeRuleProperty.put(ATTRIBUTE_LINK_IDS,attributeRuleDTO.getAttributeLinkId());
          attributeRuleProperty.put(ENTITYTYPE, attributeRuleDTO.getEntityType());
          attributeRuleProperty.put(CODE_COLUMN_CONFIG, attributeRuleDTO.getCode());
          attributeRuleProperty.put(FROM, attributeRuleDTO.getFrom());
          attributeRuleProperty.put("To", attributeRuleDTO.getTo());
          attributeRuleProperty.put(COMPARE_WITH_SYS_DATE,attributeRuleDTO.getEntityType());
          attributeRuleProperty.put(TYPE_COLUMN, attributeRuleDTO.getType());      
          break;
        case COMPARE_TYPE_DATE_LT:
        case COMPARE_TYPE_DATE_GT:   
          setShouldCompareWithSystemDate = false;
          long dateValue = Long.parseLong(attributeRuleDTO.getValues().get(0));
          attributeRuleProperty.put(RULE_LIST_LINKID, attributeRuleDTO.getRuleListLinkId());
          attributeRuleProperty.put(VALUES,dateValue);
          attributeRuleProperty.put(KLASS_LINK_IDS , attributeRuleDTO.getKlassLinkIds());
          attributeRuleProperty.put(ATTRIBUTE_LINK_IDS, attributeRuleDTO.getAttributeLinkId());
          attributeRuleProperty.put(ENTITYTYPE, attributeRuleDTO.getEntityType());
          attributeRuleProperty.put(CODE_COLUMN_CONFIG, attributeRuleDTO.getCode());
          attributeRuleProperty.put(From, attributeRuleDTO.getFrom());
          attributeRuleProperty.put(To, attributeRuleDTO.getTo());
          attributeRuleProperty.put(COMPARE_WITH_SYS_DATE, attributeRuleDTO.getEntityType());
          attributeRuleProperty.put(TYPE_COLUMN, attributeRuleDTO.getType());
          break;
        case COMPARE_TYPE_DATE_RANGE:
          long fromValueDate = Long.parseLong(attributeRuleDTO.getFrom());
          long toValueDate = Long.parseLong(attributeRuleDTO.getTo());
           from =  DiTransformationUtils.getTimeStampForFormat(
               fromValueDate != 0 ? fromValueDate : null, DiConstants.DATE_FORMAT);        
           to =  DiTransformationUtils.getTimeStampForFormat(
               toValueDate != 0 ? toValueDate : null, DiConstants.DATE_FORMAT);
           attributeRuleProperty.put(RULE_LIST_LINKID, attributeRuleDTO.getRuleListLinkId());
           attributeRuleProperty.put(VALUES,attributeRuleDTO.getValues());
           attributeRuleProperty.put(KLASS_LINK_IDS, attributeRuleDTO.getKlassLinkIds());
           attributeRuleProperty.put(ATTRIBUTE_LINK_IDS, attributeRuleDTO.getAttributeLinkId());
           attributeRuleProperty.put(ENTITYTYPE, attributeRuleDTO.getEntityType());
           attributeRuleProperty.put(CODE_COLUMN_CONFIG, attributeRuleDTO.getCode());
           attributeRuleProperty.put(From, from);
           attributeRuleProperty.put(To, to);
           attributeRuleProperty.put(COMPARE_WITH_SYS_DATE, attributeRuleDTO.getEntityType());
           attributeRuleProperty.put(TYPE_COLUMN, attributeRuleDTO.getType());
          break;
      }
      rulesList.add(attributeRuleProperty);
    }
    attributeCauseForRule.put(RULES, rulesList);
    attributeCause.add(attributeCauseForRule);

  }

  /**
   * check And Fill Cause For Attributes
   * @param transformedRuleMap
   * @param attributesIterator
   */
  private void checkAndFillCauseForAttributes( Iterator<IConfigDataRuleIntermediateEntitysDTO> attributesIterator,List<Object> attributeCause)
  {
    IConfigDataRuleIntermediateEntitysDTO intermediateEntitysDTO;
    if (attributesIterator.hasNext()) {
      intermediateEntitysDTO = attributesIterator.next();
      fillCauseForAttribute(intermediateEntitysDTO,attributeCause);
    }
  }
 
  /**
   * check And Fill Cause For Tags
   * @param transformedRuleMap
   * @param tagsIterator
   */
  private void checkAndFillCauseForTags(Iterator<IConfigDataRuleTagsDTO> tagsIterator,List<Object> tagCause1 )
  {
    IConfigDataRuleTagsDTO configDataRuleTagsDTO;
    if (tagsIterator.hasNext()) {
      configDataRuleTagsDTO = tagsIterator.next();
      fillCauseForTags( configDataRuleTagsDTO,tagCause1);
    }
  }

  /**
   * fill Cause For Tags
   * @param transformedRuleMap
   * @param configDataRuleTagsDTO
   */
  private void fillCauseForTags( IConfigDataRuleTagsDTO configDataRuleTagsDTO,List<Object> tagCause1)
  {
    Map<String,Object> tagProperty = new HashMap<>();
    tagProperty.put(CAUSE_ENTITY_CODE, configDataRuleTagsDTO.getEntityId());
    tagProperty.put(CAUSE_ENTITY_TYPE, ENTITY_TAG);
    tagProperty.put(CAUSE_ENTITY_SUBTYPE, configDataRuleTagsDTO.getEntityAttributeType());
    
    List<Object> compareTypesList = new ArrayList<Object>();//coupling type
    List<Object> entityValuesList = new ArrayList<Object>();
    Map<String,Object> couplingtype = new HashMap<>();
    for (IConfigDataRuleTagRuleDTO iConfigDataRuleTagRuleDTO : configDataRuleTagsDTO.getRules()) {
      List<IJSONContent> tagValues = iConfigDataRuleTagRuleDTO.getTagValues();
      Map<String,Object> tagValueMap = new HashMap<String,Object>();
        for (int i = 0; i < tagValues.size(); i++) {
         JSONObject tagValue = (JSONObject) tagValues.get(i);
         tagValueMap.put(From, "100");
         tagValueMap.put(To,"100");
         tagValueMap.put(ID_PROPERTY,(String) tagValue.get(ID_COLUMN));
         compareTypesList.add(tagValueMap);
        }  
        couplingtype.put(TYPE_COLUMN,iConfigDataRuleTagRuleDTO.getType());  
        couplingtype.put(TAG_VALUES,compareTypesList);
        entityValuesList.add(couplingtype);
    }
    tagProperty.put(COUPLINGS, getSepratedValuesForTag( entityValuesList));
    tagCause1.add(tagProperty);
  }
  /**
   * Prepare Map for Attribute
   * 
   * @param transformedJSONMap
   * @param configAttributeDTO
   * @param workflowTaskModel
   */
  public void generateConfigAttributeJSON(Map<String, Object> transformedJSONMap, ConfigAttributeDTO configAttributeDTO,
      WorkflowTaskModel workflowTaskModel)
  {
    if (transformedJSONMap.get(ITransformationTask.ATTRIBUTE_SHEET_NAME) == null) {
      transformedJSONMap.put(ITransformationTask.ATTRIBUTE_SHEET_NAME, new ArrayList<>());
    }
    Map<String, Object> transformedAttributeMap = new LinkedHashMap<>();
    List<Map<String, Object>> attributeList = (List<Map<String, Object>>) transformedJSONMap.get(ITransformationTask.ATTRIBUTE_SHEET_NAME);
    transformAttributeForJSON(transformedAttributeMap, configAttributeDTO);
    attributeList.add(transformedAttributeMap);
    
  }
  
  protected void transformAttributeForJSON(Map<String, Object> transformedAttributeMap, ConfigAttributeDTO configAttributeDTO)
  {
    transformedAttributeMap.put(ALLOWEDSTYLES, getSepratedValues(configAttributeDTO.getAllowedStyles()));
    transformedAttributeMap.put(AVAILABILITY_FOR_PROPERTY, getSepratedValues(configAttributeDTO.getAvailability()));
    transformedAttributeMap.put(CODE_COLUMN_CONFIG, configAttributeDTO.getCode());
    // boolean
    transformedAttributeMap.put(TRANSLATABLE_FOR_PROPERTY, configAttributeDTO.isTranslatable() ? "1" : "0");
    transformedAttributeMap.put(DISABLED_FOR_PROPERTY, configAttributeDTO.isDisabled() ? "1" : "0");
    transformedAttributeMap.put(FILTERABLE_FOR_PROPERTY, configAttributeDTO.isFilterable() ? "1" : "0");
    transformedAttributeMap.put(GRID_EDITABLE_FOR_PROPERTY, configAttributeDTO.isGridEditable() ? "1" : "0");
    transformedAttributeMap.put(MANDATORY_FOR_PROPERTY, configAttributeDTO.isMandatory() ? "1" : "0");
    transformedAttributeMap.put(SEARCHABLE_FOR_PROPERTY, configAttributeDTO.isSearchable() ? "1" : "0");
    transformedAttributeMap.put(SORTABLE_FOR_PROPERTY, configAttributeDTO.isSortable() ? "1" : "0");
    transformedAttributeMap.put(STANDARD_FOR_PROPERTY, configAttributeDTO.isStandard() ? "1" : "0");
    transformedAttributeMap.put(REVISIONABLE_FOR_PROPERTY, configAttributeDTO.isVersionable() ? "1" : "0");
    transformedAttributeMap.put(HIDE_SEPARATOR_FOR_PROPERTY, configAttributeDTO.hideSeparator() ? "1" : "0");
    transformedAttributeMap.put(READ_ONLY, configAttributeDTO.isDisabled() ? "1" : "0");
    transformedAttributeMap.put(ICON_COLUMN, configAttributeDTO.getIcon());// PXPFDEV-16987
    transformedAttributeMap.put(DEFAULT_UNIT_FOR_PROPERTY, configAttributeDTO.getDefaultUnit());
    transformedAttributeMap.put(DEFAULT_VALUE_FOR_PROPERTY, configAttributeDTO.getDefaultValue());
    transformedAttributeMap.put(DEFAULT_VALUE_AS_HTML_FOR_PROPERTY, configAttributeDTO.getDefaultValueAsHTML());
    transformedAttributeMap.put(DESCRIPTION_FOR_PROPERTY, configAttributeDTO.getDescription());
    // transformedAttributeMap.put(FORMULA, configAttributeDTO.getFormula());
    transformedAttributeMap.put(NAME_FOR_PROPERTY, configAttributeDTO.getLabel());
    transformedAttributeMap.put(NAME_FOR_PROPERTY, configAttributeDTO.getPlaceHolder());
    transformedAttributeMap.put(PRECISION_FOR_PROPERTY, String.valueOf(configAttributeDTO.getPrecision()));
    transformedAttributeMap.put(TOOLTIP_FOR_PROPERTY, configAttributeDTO.getToolTip());
    transformedAttributeMap.put(DEFAULT_UNIT_FOR_PROPERTY, configAttributeDTO.getDefaultUnit());
    String type = configAttributeDTO.getPropertyDTO().getPropertyType().name();
    transformedAttributeMap.put(TYPE_COLUMN, type);
    transformedAttributeMap.put(SUBTYPE, configAttributeDTO.getString(ConfigTag.subType));
    if (PropertyType.DATE.name().equalsIgnoreCase(type) && configAttributeDTO.getDefaultValue() != null
        && !configAttributeDTO.getDefaultValue().isEmpty()) {
      long longAttributeValue = Long.parseLong(configAttributeDTO.getDefaultValue());
      transformedAttributeMap.put(DEFAULT_VALUE_FOR_PROPERTY,
          DiTransformationUtils.getTimeStampForFormat(longAttributeValue != 0 ? longAttributeValue : null, DiConstants.DATE_FORMAT));
    }
    else {
      transformedAttributeMap.put(DEFAULT_VALUE_FOR_PROPERTY, configAttributeDTO.getDefaultValue());
    }
    if (PropertyType.CONCATENATED.name().equalsIgnoreCase(type)) {
      transformedAttributeMap.put(SHOW_CODE_TAG, configAttributeDTO.isCodeVisible() ? "1" : "0");
      if (!configAttributeDTO.getAttributeConcatenatedList().isEmpty()) {
        formulaForConcatenatedAttributeType(transformedAttributeMap, configAttributeDTO);
      }
    }
    if (PropertyType.CALCULATED.name().equalsIgnoreCase(type)) {
      if (configAttributeDTO.getCalculatedAttributeType() != null) {
        transformedAttributeMap.put(CALCULATED_ATTRIBUTE_TYPE_FOR_PROPERTY,
            CalculatedAttributeTypes
                .valueOf(IConfigClass.PropertyClass.valueOfClassPath(configAttributeDTO.getCalculatedAttributeType().toString()).name())
                .toString());
      }
      if (configAttributeDTO.getCalculatedAttributeUnit() != null) {
        transformedAttributeMap.put(CALCULATED_ATTRIBUTE_UNIT_FOR_PROPERTY, configAttributeDTO.getCalculatedAttributeUnit().toString());
      }
      if (!configAttributeDTO.getAttributeOperatorList().isEmpty()) {
        formulaForCalculatedAttributeType(transformedAttributeMap, configAttributeDTO);
      }
    }
  }
  
  /**
   * Implementation of formula for Attribute Type-Concatenated
   * 
   * @param transformedAttributeMap
   * @param configAttributeDTO
   */
  private void formulaForConcatenatedAttributeType(Map<String, Object> transformedAttributeMap, ConfigAttributeDTO configAttributeDTO)
  {
    List<String> typesOfPropertyConcat = new ArrayList<String>();
    List<String> entityCodesOfPropertyConcat = new ArrayList<String>();
    List<String> valuesOfPropertyConcat = new ArrayList<String>();
    List<IJSONContent> attributeConcatenatedList = configAttributeDTO.getAttributeConcatenatedList();
    for (int i = 0; i < attributeConcatenatedList.size(); i++) {
      JSONObject jsonElement = (JSONObject) attributeConcatenatedList.get(i);
      typesOfPropertyConcat.add((String) jsonElement.get(CommonConstants.TYPE));
      if (jsonElement.containsKey(ATTRIBUTE_ID)) {
        entityCodesOfPropertyConcat.add((String) jsonElement.get(ATTRIBUTE_ID));
      }
      else if (jsonElement.containsKey(TAG_ID)) {
        entityCodesOfPropertyConcat.add((String) jsonElement.get(TAG_ID));
      }
      else {
        entityCodesOfPropertyConcat.add(EMPTY_STRING);
      }
      if (jsonElement.containsKey(CommonConstants.VALUE_PROPERTY)) {
        valuesOfPropertyConcat.add((String) jsonElement.get(CommonConstants.VALUE_PROPERTY));
      }
      else {
        valuesOfPropertyConcat.add(EMPTY_STRING);
      }
    }
    transformedAttributeMap.put(TYPE_OF_PROPERTYCONCATENATED, getSepratedValues(typesOfPropertyConcat));
    transformedAttributeMap.put(ATTRIBUTE_CODE_OF_PROPERTYCONCATENATED, getSepratedValues(entityCodesOfPropertyConcat));
    transformedAttributeMap.put(VALUE_OF_PROPERTYCONCATENATED, getSepratedValues(valuesOfPropertyConcat));
  }
  
  /**
   * Implementation of formula for Attribute Type-Calculated
   * 
   * @param transformedAttributeMap
   * @param configAttributeDTO
   */
  private void formulaForCalculatedAttributeType(Map<String, Object> transformedAttributeMap, ConfigAttributeDTO configAttributeDTO)
  {
    List<IJSONContent> attributeCalculatedList = configAttributeDTO.getAttributeOperatorList();
    List<String> typesOfPropertyCalculated = new ArrayList<String>();
    List<String> entityCodesOfPropertyCalculated = new ArrayList<String>();
    List<String> valuesOfPropertyCalculated = new ArrayList<String>();
    
    for (int i = 0; i < attributeCalculatedList.size(); i++) {
      JSONObject jsonElement = (JSONObject) attributeCalculatedList.get(i);
      String typeKey = (String) jsonElement.get(CommonConstants.TYPE);
      typesOfPropertyCalculated.add(typeKey);
      if (typeKey.equals(ATTRIBUTE)) {
        entityCodesOfPropertyCalculated.add((String) jsonElement.get(ATTRIBUTE_ID));
        valuesOfPropertyCalculated.add(EMPTY_STRING);
      }
      else if (typeKey.equals(VALUE)) {
        valuesOfPropertyCalculated.add((String) jsonElement.get(CommonConstants.VALUE_PROPERTY));
        entityCodesOfPropertyCalculated.add(EMPTY_STRING);
      }
      else {
        // any of the operators
        entityCodesOfPropertyCalculated.add(EMPTY_STRING);
        valuesOfPropertyCalculated.add(EMPTY_STRING);
      }
    }
    transformedAttributeMap.put(TYPE_OF_PROPERTYCALCULATED, getSepratedValues(typesOfPropertyCalculated));
    transformedAttributeMap.put(ATTRIBUTE_CODE_OF_PROPERTYCALCULATED, getSepratedValues(entityCodesOfPropertyCalculated));
    transformedAttributeMap.put(VALUE_OF_PROPERTYCALCULATED, getSepratedValues(valuesOfPropertyCalculated));
  }
  
  /**
   * Prepare Map for Property Collection
   * 
   * @param transformedJSONMap
   * @param configPropertyCollectionDTO
   * @param workflowTaskModel
   */
  public void generateConfigPropertyCollectionJSON(Map<String, Object> transformedJSONMap,
      ConfigPropertyCollectionDTO configPropertyCollDTO, WorkflowTaskModel workflowTaskModel)
  {
    if (transformedJSONMap.get(ITransformationTask.PROPERTY_COLLECTION_SHEET_NAME) == null) {
      transformedJSONMap.put(ITransformationTask.PROPERTY_COLLECTION_SHEET_NAME, new ArrayList<>());
    }
    Map<String, Object> transformedPropertyCollection = new LinkedHashMap<>();
    List<Map<String, Object>> propertyCollectionList = (List<Map<String, Object>>) transformedJSONMap
        .get(ITransformationTask.PROPERTY_COLLECTION_SHEET_NAME);
    transformedPropertyCollection.put(CODE_COLUMN_CONFIG, configPropertyCollDTO.getCode());
    transformedPropertyCollection.put(LABEL_COLUMN_CONFIG, configPropertyCollDTO.getLabel());
    transformedPropertyCollection.put(TAB_COLUMN, configPropertyCollDTO.getTab());
    transformedPropertyCollection.put(ICON_COLUMN, configPropertyCollDTO.getIcon());
    transformedPropertyCollection.put(IS_FOR_XRAYCOLUMN, configPropertyCollDTO.isForXRay() ? "1" : "0");
    transformedPropertyCollection.put(IS_DEFAULT_FOR_XRAY_COLUMN, configPropertyCollDTO.isDefaultForXRay() ? "1" : "0");
    transformedPropertyCollection.put(IS_STANDARD_COLUMN, configPropertyCollDTO.isStandard() ? "1" : "0");
    Collection<JSONObject> elements = configPropertyCollDTO.getElements();
    List<Object> elementofpropertycollection = new ArrayList<Object>();
    for (JSONObject element : elements) {
      Map<Object, Object> transformedPropertyCollectionMapForEntity = new LinkedHashMap<>();
      transformedPropertyCollectionMapForEntity.put(ENTITY_CODE_COLUMN, element.get(CODE_PROPERTY));
      transformedPropertyCollectionMapForEntity.put(ENTITYTYPE, element.get(INDEX_PROPERTY));
      transformedPropertyCollectionMapForEntity.put(INDEX_COLUMN, element.get(ID_PROPERTY));
      transformedPropertyCollectionMapForEntity.put(TYPE_COLUMN, element.get(TYPE_PROPERTY));
      elementofpropertycollection.add(transformedPropertyCollectionMapForEntity);
    }
    transformedPropertyCollection.put(ELEMENTS, elementofpropertycollection);
    propertyCollectionList.add(transformedPropertyCollection);
    
  }
  /**
   * Prepare Map for Tag
   * 
   * @param transformedExcelMap
   * @param configTagDTO
   * @param workflowTaskModel
   */
  private void generateConfigTagJSON(Map<String, Object> transformedJSONMap, ConfigTagDTO configTagDTO, WorkflowTaskModel workflowTaskModel)
  {
    if (transformedJSONMap.get(TAG_SHEET_NAME) == null) {
      transformedJSONMap.put(TAG_SHEET_NAME, new ArrayList<>());
    }
    Map<String, Object> transformedTagMap = new LinkedHashMap<>();
    List<Map<String, Object>> tagList = (List<Map<String, Object>>) transformedJSONMap.get(TAG_SHEET_NAME);
    transformTag(transformedTagMap, configTagDTO);
    List<IConfigTagValueDTO> childTags = configTagDTO.getChildren();
    if (childTags != null && !childTags.isEmpty()) {
      if (!transformedTagMap.get(TYPE_COLUMN).equals(TagType.BooleanTagCode)) {
        if (transformedTagMap.get("") == null) {
          transformedTagMap.put(CHILDTAG, new ArrayList<>());
        }
        List<Map<String, Object>> transformedChlidList = (List<Map<String, Object>>) transformedTagMap.get(CHILDTAG);
        preparechildTag(childTags, transformedChlidList, configTagDTO.getCode());
        transformedTagMap.put(CHILDTAG, transformedChlidList);
      }
      transformedTagMap.put(PARENT_CODE_COLUMN, "-1");
    }
    tagList.add(transformedTagMap);
  }
  
  /**
   * Prepare Map for GoldenRule
   * 
   * @param transformedJSONMap
   * @param configAttributeDTO
   * @param workflowTaskModel
   */
  public void generateConfigGoldenRecordRuleJSON(Map<String, Object> transformedJSONMap, ConfigGoldenRecordRuleDTO configGoldenRecordRuleDTO,
      WorkflowTaskModel workflowTaskModel)
  {
    if (transformedJSONMap.get(ITransformationTask.GOLDEN_RECORD_RULES_SHEET_NAME) == null) {
      transformedJSONMap.put(ITransformationTask.GOLDEN_RECORD_RULES_SHEET_NAME, new ArrayList<>());
    }
    Map<String, String> transformedGoldenRecordRule = new LinkedHashMap<>();
    List<Map<String, Object>> goldenRecordRule = (List<Map<String, Object>>) transformedJSONMap.get(ITransformationTask.GOLDEN_RECORD_RULES_SHEET_NAME);
    Map<String, Object> transformedGoldenRuleMap = new LinkedHashMap<>();
    List<String> klassIds = configGoldenRecordRuleDTO.getKlassIds();
    transformedGoldenRuleMap.put(CODE_COLUMN_CONFIG, configGoldenRecordRuleDTO.getCode());
    transformedGoldenRuleMap.put(LABEL_COLUMN_CONFIG, configGoldenRecordRuleDTO.getLabel());
    transformedGoldenRuleMap.put(NATURE_TYPE, klassIds.get(0)); 
    transformedGoldenRuleMap.put(PARTNERS, getSepratedValues( configGoldenRecordRuleDTO.getOrganizations()));
    transformedGoldenRuleMap.put(PHYSICALCATALOGS, getSepratedValues(configGoldenRecordRuleDTO.getPhysicalCatalogIds()));
    transformedGoldenRuleMap.put(MATCH_ATTRIBUTES, getSepratedValues(configGoldenRecordRuleDTO.getAttributes()));
    transformedGoldenRuleMap.put(MATCH_TAGS, getSepratedValues(configGoldenRecordRuleDTO.getTags()));
     klassIds.remove(0);
    transformedGoldenRuleMap.put(MATCH_NON_NATURE_CLASSES,getSepratedValues(configGoldenRecordRuleDTO.getKlassIds()));
    transformedGoldenRuleMap.put(MATCH_TAXONOMIES,getSepratedValues(configGoldenRecordRuleDTO.getTaxonomyIds()));
    transformedGoldenRuleMap.put(AUTOCREATE, configGoldenRecordRuleDTO.getIsAutoCreate() ? "1" : "0");
    Iterator<IConfigMergeEffectTypeDTO> attributesIterator = configGoldenRecordRuleDTO.getMergeEffect().getAttributes().iterator();
    Iterator<IConfigMergeEffectTypeDTO> tagsIterator = configGoldenRecordRuleDTO.getMergeEffect().getTags().iterator();
    Iterator<IConfigMergeEffectTypeDTO> relationshipsIterator = configGoldenRecordRuleDTO.getMergeEffect().getRelationships().iterator();
    Iterator<IConfigMergeEffectTypeDTO> natureRelationshipsIterator = configGoldenRecordRuleDTO.getMergeEffect().getNatureRelationships().iterator();
    if (!attributesIterator.hasNext() && !tagsIterator.hasNext() && !relationshipsIterator.hasNext() && !natureRelationshipsIterator.hasNext()) {
      goldenRecordRule.add(transformedGoldenRuleMap);
    }    
    Map<String,Object> mergeeffect = new HashMap<>();
    List<Object> attributeList = new ArrayList<Object>();
    List<Object> tagList = new ArrayList<Object>();
    List<Object> relationshipList = new ArrayList<Object>();
    List<Object> nonnatureRelationshipList = new ArrayList<Object>();
    while (attributesIterator.hasNext() ) {
      checkAndFillMergeEffect( attributesIterator, attributeList);  
    }
    mergeeffect.put(ITransformationTask.ATTRIBUTE_SHEET_NAME, attributeList);
     while(tagsIterator.hasNext()) {
      checkAndFillMergeEffect(tagsIterator,tagList);   
     }
     mergeeffect.put(ITransformationTask.TAG_SHEET_NAME, tagList);
    while (relationshipsIterator.hasNext() ) {
      checkAndFillMergeEffect(relationshipsIterator,relationshipList);  
    }
    mergeeffect.put(ITransformationTask.RELATIONSHIP_SHEET_NAME_FOR_CONFIG, relationshipList);
    while (natureRelationshipsIterator.hasNext() ) {
      checkAndFillMergeEffect( natureRelationshipsIterator,nonnatureRelationshipList);  
    }
    mergeeffect.put(NON_NATURERELATIONSHIP, nonnatureRelationshipList);
     transformedGoldenRuleMap.put(MERGE_EFFECTS, mergeeffect);
     goldenRecordRule.add(transformedGoldenRuleMap);
  }
  
  private void checkAndFillMergeEffect(Iterator<IConfigMergeEffectTypeDTO> entityIterator, List<Object> listofEntity )
  {
    IConfigMergeEffectTypeDTO mergeEffectDTO;
    Map<String,Object> transformedMergeEffectMap    =  new HashMap<>();
    if (entityIterator.hasNext()) {
      mergeEffectDTO = entityIterator.next();
      fillMergeEffectColumns(transformedMergeEffectMap, mergeEffectDTO);
      listofEntity.add(transformedMergeEffectMap);
    }
  }
  
    
    private void fillMergeEffectColumns(Map<String, Object> transformedGoldenRuleMap, IConfigMergeEffectTypeDTO mergeEffectDTO)
    {
      switch (mergeEffectDTO.getEntityType()) {
        case CommonConstants.ATTRIBUTES:
          transformedGoldenRuleMap.put(MERGE_ATTRIBUTES, mergeEffectDTO.getEntityId());
          transformedGoldenRuleMap.put(ITransformationTask.TYPE_COLUMN, mergeEffectDTO.getType());
          transformedGoldenRuleMap.put(MERGE_ATTRIBUTES_SUPPLIERS, getSepratedValues( mergeEffectDTO.getSupplierIds()));
          transformedGoldenRuleMap.put(MERGE_ATTRIBUTE_ENTITY_TYPE, mergeEffectDTO.getEntityType());
          break;
        case CommonConstants.TAGS:
          transformedGoldenRuleMap.put(MERGE_TAGS, mergeEffectDTO.getEntityId());
          transformedGoldenRuleMap.put(MERGE_TAGS_LATEST, mergeEffectDTO.getType());
          transformedGoldenRuleMap.put(MERGE_TAGS_SUPPLIERS, getSepratedValues(mergeEffectDTO.getSupplierIds()));
          transformedGoldenRuleMap.put(MERGE_TAG_ENTITY_TYPE,mergeEffectDTO.getEntityType());
          break;
        case CommonConstants.RELATIONSHIPS:
          transformedGoldenRuleMap.put(MERGE_RELATIONSHIPS, mergeEffectDTO.getEntityId());
          transformedGoldenRuleMap.put(ITransformationTask.TYPE_COLUMN, mergeEffectDTO.getType());
          transformedGoldenRuleMap.put(MERGE_RELATIONSHIP_ENTITY_TYPE, mergeEffectDTO.getEntityType());
          transformedGoldenRuleMap.put(MERGE_RELATIONSHIPS_SUPPLIERS, getSepratedValues(mergeEffectDTO.getSupplierIds()));
          break;
        case CommonConstants.NATURE_RELATIONSHIPS:
          transformedGoldenRuleMap.put(MERGE_NATURE_RELATIONSHIPS, mergeEffectDTO.getEntityId());
          transformedGoldenRuleMap.put(MERGE_NATURE_RELATIONSHIPS_SUPPLIERS,getSepratedValues( mergeEffectDTO.getSupplierIds()));
          transformedGoldenRuleMap.put(MERGE_NON_NATURERELATIONSHIP_ENTITY_TYPE, mergeEffectDTO.getEntityType());
          transformedGoldenRuleMap.put(ITransformationTask.TYPE_COLUMN, mergeEffectDTO.getType());
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
   * Transform config Tag for JSON.
   * 
   * @param transformedTagMap
   * @param configTagDTO
   * 
   */
  private void transformTag(Map<String, Object> transformedTagMap, ConfigTagDTO configTagDTO)
  {
    // configTagDTO
    transformedTagMap.put(CODE_COLUMN_CONFIG, configTagDTO.getCode());
    transformedTagMap.put(NAME_FOR_PROPERTY, configTagDTO.getLabel());
    transformedTagMap.put(PARENT_CODE_COLUMN, configTagDTO.getCode());
    transformedTagMap.put(TYPE_COLUMN, configTagDTO.getTagType());
    transformedTagMap.put(DESCRIPTION_FOR_PROPERTY, configTagDTO.getDescription());
    transformedTagMap.put(COLOR_COLUMN, configTagDTO.getColor());
    transformedTagMap.put(LINKED_MASTER_TAG_CODE_FOR_TAG, (String) configTagDTO.getLinkedMasterTag());
    // transformedTagMap.put(DEFAULT_VALUE, configTagDTO.getDefaultValue());
    transformedTagMap.put(MULTISELECT_COLUMN, configTagDTO.isMultiSelect() ? "1" : "0");
    transformedTagMap.put(TOOLTIP_FOR_PROPERTY, configTagDTO.getToolTip());
    transformedTagMap.put(FILTERABLE_FOR_PROPERTY, configTagDTO.isFilterable() ? "1" : "0");
    transformedTagMap.put(AVAILABILITY_FOR_PROPERTY, String.join(SEPARATOR, configTagDTO.getAvailability()));
    transformedTagMap.put(GRID_EDITABLE_FOR_PROPERTY, configTagDTO.isGridEditable() ? "1" : "0");
    transformedTagMap.put(READ_ONLY_FOR_PROPERTY, configTagDTO.isDisabled() ? "1" : "0");
    transformedTagMap.put(ICON_COLUMN, configTagDTO.getIcon());
    transformedTagMap.put(REVISIONABLE_FOR_PROPERTY, configTagDTO.isVersionable() ? "1" : "0");
    transformedTagMap.put(STANDARD_FOR_PROPERTY, configTagDTO.isStandard() ? "1" : "0");
  }
  
  /**
   * Prepare child Tag for JSON.
   * 
   * @param transformedTagList
   * @param configTagDTO
   * @param parentCode
   * 
   */  
  private void preparechildTag(List<IConfigTagValueDTO> childTags, List<Map<String, Object>> transformedTag, String parentCode)
  {
    
    for (IConfigTagValueDTO iConfigTagValueDTO : childTags) {
      Map<String, Object> transformedTagMap = new HashMap<String, Object>();
      transformedTagMap.put(PARENT_CODE_COLUMN, parentCode);
      transformedTagMap.put(COLOR_COLUMN, iConfigTagValueDTO.getColor());
      transformedTagMap.put(NAME_FOR_PROPERTY, iConfigTagValueDTO.getLabel());
      transformedTagMap.put(LINKED_MASTER_TAG_CODE_FOR_TAG, iConfigTagValueDTO.getLinkedMasterTag());
      transformedTagMap.put(CODE_COLUMN_CONFIG, iConfigTagValueDTO.getCode());
      transformedTagMap.put(ICON_COLUMN, iConfigTagValueDTO.getIcon());
      transformedTag.add(transformedTagMap);
    }
    
  }
  
  /**
   * Transform config Relationship for JSON.
   * @param transformedJSONMap
   * @param configRelationshipDTO
   * @param workflowTaskModel
   */
  private void generateConfigRelationshipJSON(Map<String, Object> transformedJSONMap, ConfigRelationshipDTO configRelationshipDTO, WorkflowTaskModel workflowTaskModel)
  {

    if( transformedJSONMap.get(RELATIONSHIP_SHEET_NAME_FOR_CONFIG) == null) {
    transformedJSONMap.put(RELATIONSHIP_SHEET_NAME_FOR_CONFIG, new ArrayList<>());
    }
    List<Map<String, String>> relationshipsList = (List<Map<String, String>>) transformedJSONMap.get(RELATIONSHIP_SHEET_NAME_FOR_CONFIG);

    Map<String, String> transformedRelationshipMap = new LinkedHashMap<>();
    
    transformedRelationshipMap.put(CODE_KEY, configRelationshipDTO.getCode());
    transformedRelationshipMap.put(LABEL_KEY, configRelationshipDTO.getLabel());
    transformedRelationshipMap.put(ICON_KEY, configRelationshipDTO.getIcon());
    transformedRelationshipMap.put(TAB_CODE_KEY, configRelationshipDTO.getTab());
    transformedRelationshipMap.put(ALLOW_AFTERSAVE_EVENT_KEY, configRelationshipDTO.isEnableAfterSave() ? "1":"0");
    transformedRelationshipMap.put(IS_LITE_KEY, configRelationshipDTO.isLite() ? "1":"0");
    IConfigSideRelationshipDTO side1Info = configRelationshipDTO.getSide1();
    transformedRelationshipMap.put(S1_CLASS_CODE_KEY, side1Info.getClassCode());
    transformedRelationshipMap.put(S1_LABEL_KEY, side1Info.getLabel());
    transformedRelationshipMap.put(S1_CARDINALITY_KEY, side1Info.getCardinality());
    transformedRelationshipMap.put(S1_EDITABLE_KEY, side1Info.isVisible() ? "1":"0");
    transformedRelationshipMap.put(S1_CONTEXT_CODE_KEY, side1Info.getContextCode());
    
    IConfigSideRelationshipDTO side2Info = configRelationshipDTO.getSide2();
    transformedRelationshipMap.put(S2_CLASS_CODE_KEY, side2Info.getClassCode());
    transformedRelationshipMap.put(S2_LABEL_KEY, side2Info.getLabel());
    transformedRelationshipMap.put(S2_CARDINALITY_KEY, side2Info.getCardinality());
    transformedRelationshipMap.put(S2_EDITABLE_KEY, side2Info.isVisible() ? "1":"0");
    transformedRelationshipMap.put(S2_CONTEXT_CODE_KEY, side2Info.getContextCode());
    
    Iterator<Object> side1CouplingIterator = ((JSONArray) side1Info.getCouplings()).iterator();
    Iterator<Object> side2CouplingIterator = ((JSONArray) side2Info.getCouplings()).iterator();
    if (!side1CouplingIterator.hasNext() && !side2CouplingIterator.hasNext()) {
      relationshipsList.add(transformedRelationshipMap);
    }
    Boolean isFirstCoupling = true;
    JSONObject side1Coupling;
    JSONObject side2Coupling;
    Map<String,String> transformedCouplingMap;
    while (side1CouplingIterator.hasNext() && side2CouplingIterator.hasNext()) {
      side1Coupling = (JSONObject) side1CouplingIterator.next();
      side2Coupling = (JSONObject) side2CouplingIterator.next();
      if (isFirstCoupling) {
        fillSide1CouplingForJSON(transformedRelationshipMap, side1Coupling);
        fillSide2CouplingForJSON(transformedRelationshipMap, side2Coupling);
        relationshipsList.add(transformedRelationshipMap);
        isFirstCoupling = false;
      }
      else {
        transformedCouplingMap = new HashMap<>();
        transformedCouplingMap.put(CODE_COLUMN_CONFIG, configRelationshipDTO.getCode());
        fillSide1CouplingForJSON(transformedCouplingMap, side1Coupling);
        fillSide2CouplingForJSON(transformedCouplingMap, side2Coupling);
        relationshipsList.add(transformedCouplingMap);
      }
    }
    while(side1CouplingIterator.hasNext()){
      side1Coupling = (JSONObject) side1CouplingIterator.next();
      if (isFirstCoupling) {
        fillSide1CouplingForJSON(transformedRelationshipMap, side1Coupling);
        relationshipsList.add(transformedRelationshipMap);
      }
      else {
        transformedCouplingMap = new HashMap<>();
        transformedCouplingMap.put(CODE_COLUMN_CONFIG, configRelationshipDTO.getCode());
        fillSide1CouplingForJSON(transformedCouplingMap, side1Coupling);
        relationshipsList.add(transformedCouplingMap);
      }
    }

   while(side2CouplingIterator.hasNext()){
     side2Coupling = (JSONObject) side2CouplingIterator.next();
     if (isFirstCoupling) {
       fillSide2CouplingForJSON(transformedRelationshipMap, side2Coupling);
       relationshipsList.add(transformedRelationshipMap);
     }
     else {
       transformedCouplingMap = new HashMap<>();
       transformedCouplingMap.put(CODE_COLUMN_CONFIG, configRelationshipDTO.getCode());
       fillSide2Coupling(transformedCouplingMap, side2Coupling);
       relationshipsList.add(transformedCouplingMap);
    
     }
   }
  }
  
  protected void fillSide2CouplingForJSON(Map<String, String> transformedRelationshipMap, JSONObject side2Coupling)
  {
    transformedRelationshipMap.put(S2_PROPERTY_TYPE_KEY, (String) side2Coupling.getOrDefault(TYPE_PROPERTY, new String()));
    transformedRelationshipMap.put(S2_PROPERTY_CODE_KEY, (String) side2Coupling.getOrDefault(ID_PROPERTY, new String()));
    transformedRelationshipMap.put(S2_COUPLING_TYPE_KEY, (String) side2Coupling.getOrDefault(COUPLING_TYPE, new String()));
  }

  protected void fillSide1CouplingForJSON(Map<String, String> transformedRelationshipMap, JSONObject side1Coupling)
  {
    transformedRelationshipMap.put(S1_PROPERTY_TYPE_KEY, (String) side1Coupling.getOrDefault(TYPE_PROPERTY, new String()));
    transformedRelationshipMap.put(S1_PROPERTY_CODE_KEY, (String) side1Coupling.getOrDefault(ID_PROPERTY, new String()));
    transformedRelationshipMap.put(S1_COUPLING_TYPE_KEY, (String) side1Coupling.getOrDefault(COUPLING_TYPE, new String()));
  }
  
/**
 * Transform config Tabs for JSON.
 * @param transformedJSONMap
 * @param tabDTO
 * @param workflowTaskModel
 */
  private void generateConfigTabJSON(Map<String, Object> transformedJSONMap, IConfigTabDTO tabDTO, WorkflowTaskModel workflowTaskModel)
  {
    if( transformedJSONMap.get(CSEObjectType.Tab.name()) == null) {
    transformedJSONMap.put(CSEObjectType.Tab.name(), new ArrayList<>());
    }
    List<Map<String, String>> tabList = (List<Map<String, String>>) transformedJSONMap.get(CSEObjectType.Tab.name());
    Map<String, String> transformedTabMap = new LinkedHashMap<>();
    transformedTabMap.put(CODE_COLUMN_CONFIG, tabDTO.getCode());
    transformedTabMap.put(LABEL_COLUMN_CONFIG, tabDTO.getLabel());
    transformedTabMap.put(ICON_COLUMN, tabDTO.getIcon());
    transformedTabMap.put(SEQUENCE, String.valueOf(tabDTO.getSequence()));
    transformedTabMap.put(PROPERTY_COLLECTION_SHEET_NAME, String.join(";", tabDTO.getPropertySequenceList()));
    tabList.add(transformedTabMap);
  }
  
  /**
   * 
   * @param transformedJsonMap
   * @param configClassDTO
   */
  private void generateConfigKlassJson(Map<String, Object> transformedJsonMap,
      ConfigClassifierDTO configClassDTO)
  {
    Map<String, Object> transformedKlassMap = new LinkedHashMap<>();
    generateConfigKlassJson(transformedJsonMap, configClassDTO, transformedKlassMap);
    prepareBooleanValues(configClassDTO, transformedKlassMap);
  }
  

  /**
   * Prepare map for flat level field of klass and preapre the map of property
   * collection, embedded variants and relationship which linked to the
   * respective class.
   * 
   * @param transformedJsonMap
   * @param configClassDTO
   * @param transformedKlassMap
   */
  private void generateConfigKlassJson(Map<String, Object> transformedJsonMap,
      ConfigClassifierDTO configClassDTO, Map<String, Object> transformedKlassMap)
  {
    List<Map<String, Object>> klassJsonList =  (List<Map<String, Object>>) transformedJsonMap.get(KLASS_SHEET_NAME);
    transformCommonFieldOfKlassEntity(transformedKlassMap, configClassDTO);
    
    if (configClassDTO.getSections() != null && !configClassDTO.getSections().isEmpty()) {
      List<Map<String, Object>> propCollList = new ArrayList<Map<String, Object>>();
      transformedKlassMap.put(SECTION_PROPERTIES, propCollList);

      prepareKlassifierPropertyCollections(configClassDTO, propCollList);
    }
    
    if (configClassDTO.getEmbeddedClasses() != null && !configClassDTO.getEmbeddedClasses().isEmpty()) {
      List<Map<String, String>> variantList = new ArrayList<Map<String, String>>();
      transformedKlassMap.put(VARIANTS, variantList);
      prepareKlassifierVariants(configClassDTO, variantList);
    }
    
    if (configClassDTO.getRelationships() != null && !configClassDTO.getRelationships().isEmpty()) {
      List<Map<String, String>> relationshipList = new ArrayList<Map<String, String>>();
      transformedKlassMap.put(RELATIONSHIPS, relationshipList);
      prepareKlassifirerRelationhips(configClassDTO, relationshipList);
    }
    
    klassJsonList.add(transformedKlassMap);
  }
 
  /**
   * Prepare Map for Property Collection Excel Sheet
   * 
   * @param transformedJsonMap
   * @param configPropertyCollDTO
   * @param workflowTaskModel
   * @throws CSFormatException
   */
  private void generateConfigHierarchyTaxonomyJson(Map<String, Object> transformedJsonMap,
      ConfigClassifierDTO configClassifierDTO)
  {
    Map<String, Object> transformedTxnomyMap = new LinkedHashMap<>();
    List<Map<String, Object>> taxonomies = (List<Map<String, Object>>) transformedJsonMap.get(HIERARCHY_SHEET_NAME);
    transformTaxonomy(transformedTxnomyMap, configClassifierDTO, null);
    taxonomies.add(transformedTxnomyMap);
  }
  
  /**
   *  /**
   * Prepare Map for Task 
   * 
   * @param transformedExcelMap
   * @param configTaskDTO
   * @param workflowTaskModel
   */
  private void generateConfigTaskJSON(Map<String, Object> transformedJSONMap,
      ConfigTaskDTO configTaskDTO, WorkflowTaskModel workflowTaskModel)
  {
    if (transformedJSONMap.get(ITransformationTask.TASK_COLUMN) == null) {
      transformedJSONMap.put(ITransformationTask.TASK_COLUMN, new ArrayList<>());
    }
    Map<String, String> transformedTaskMap = new LinkedHashMap<>();
    List<Map<String, String>> taskList = (List<Map<String, String>>) transformedJSONMap.get(ITransformationTask.TASK_COLUMN);
    transformedTaskMap.put(CODE_COLUMN_CONFIG, configTaskDTO.getCode());
    transformedTaskMap.put(LABEL_COLUMN_CONFIG, configTaskDTO.getLabel());
    transformedTaskMap.put(TYPE_COLUMN, configTaskDTO.getType());
    transformedTaskMap.put(ICON_COLUMN, configTaskDTO.getIcon());
    transformedTaskMap.put(COLOR_COLUMN, configTaskDTO.getColor());
    transformedTaskMap.put(PRIORITY_TAG_COLUMN, configTaskDTO.getPriorityTag());
    transformedTaskMap.put(STATUS_TAG_COLUMN, configTaskDTO.getStatusTag());
    taskList.add(transformedTaskMap);
    
  }

  /* Prepare Map for  MasterTaxonomy
   * 
   * @param transformedExcelMap
   * @param configPropertyCollDTO
   * @param workflowTaskModel
   * @throws CSFormatException 
   */
  private void generateConfigMasterTaxonomyJson(Map<String, Object> transformedJsonMap,
      ConfigClassifierDTO configClassifierDTO)
  {
    Map<String, Object> transformedToxnomyMap = new LinkedHashMap<>();
    List<Map<String, Object>> masterTaxonomyList = (List<Map<String, Object>>) transformedJsonMap.get(MASTER_TAXONOMY_SHEET_NAME);
    transformTaxonomy(transformedToxnomyMap, configClassifierDTO, MASTER_TAXONOMY_SHEET_NAME);
    if (configClassifierDTO.getSections() != null && !configClassifierDTO.getSections().isEmpty()) {
      List propCollList = new ArrayList<Map<String, String>>();
      transformedToxnomyMap.put(SECTION_PROPERTIES, propCollList);
      prepareKlassifierPropertyCollections(configClassifierDTO, propCollList);
    }
    if (configClassifierDTO.getEmbeddedClasses() != null && !configClassifierDTO.getEmbeddedClasses().isEmpty()) {
      List variantList = new ArrayList<Map<String, String>>();
      transformedToxnomyMap.put(VARIANTS, variantList);
      prepareKlassifierVariants(configClassifierDTO, variantList);
    }
    masterTaxonomyList.add(transformedToxnomyMap);
  }


protected void fillLevelInfo(Map<String, Object> transformedToxnomyMap,
    ConfigClassifierDTO configClassifierDTO)
{
  List<Map<String, String>> levelList = new ArrayList<Map<String,String>>();
  transformedToxnomyMap.put(LEVELS, levelList);
  
  for(int i =0; i< configClassifierDTO.getLevelCodes().size(); i++) {
    Map<String, String> levelMap = new HashMap<String, String>();
    levelMap.put(LEVEL_CODE, configClassifierDTO.getLevelCodes().get(i));
    levelMap.put(LEVEL_LABEL_COLUMN, configClassifierDTO.getLevelLables().get(i));
    levelMap.put(IS_NEWLY_CREATED_LEVEL_COLUMN, booleanToString(configClassifierDTO.getIsNewlyCreated().get(i)));
    
    levelList.add(levelMap);
  }
  
}
  /**
   * Transform config Translation to JSON
   * @param transformedJSONMap
   * @param translationDTO
   */
  private void generateConfigTranslationJSON(Map<String, Object> transformedJSONMap, ConfigTranslationDTO translationDTO)
 {
    if (transformedJSONMap.get(CSEObjectType.Translation.name()) == null) {
      transformedJSONMap.put(CSEObjectType.Translation.name(), new HashMap<String,Object>());
   }
    Map<String,Object> translationMap = (Map<String, Object>) transformedJSONMap.get(CSEObjectType.Translation.name());
    Map<String, Object> languageObject = (Map<String, Object>) translationDTO.getTranslations().toJSONObject();
    Set<String> languageKeys = languageObject.keySet();
    for (String languageKey : languageKeys) {
      if (translationMap.get(languageKey) == null) {
        translationMap.put(languageKey, new ArrayList<Map<String, String>>());
      }
      Map<String, String> transformedTranslationMap = new LinkedHashMap<String, String>();
      List<Map<String, String>> languageMapList = (List<Map<String, String>>) translationMap.get(languageKey);
      transformedTranslationMap.put(ENTITYTYPE, translationDTO.getEntityType().name());
      transformedTranslationMap.put(ENTITY_CODE, translationDTO.getCode());
      
      fillTranslationDetails(transformedTranslationMap, (Map<String, String>) languageObject.get(languageKey));
      languageMapList.add(transformedTranslationMap);
    }
    
  }


 /**
   * Transform Config Partners to JSON
   * 
   * @param transformedJSONMap
   * @param organizationDTO
  */
 private void generateConfigOrganizationJSON(Map<String, Object> transformedJSONMap, IConfigOrganizationDTO organizationDTO)
 {
   if (transformedJSONMap.get(CSEObjectType.Organization.name()) == null) {
     transformedJSONMap.put(CSEObjectType.Organization.name(), new ArrayList<>());
   }
   List<Map<String, Object>> orgList = (List<Map<String, Object>>) transformedJSONMap.get(CSEObjectType.Organization.name());
   Map<String, Object> transformedOrgMap = new HashMap<String, Object>();
   Map<String, Object> transformedRoleMap = new LinkedHashMap<>();
   
   transformedOrgMap.put(CODE_COLUMN_CONFIG, organizationDTO.getCode());
   transformedOrgMap.put(TYPE_COLUMN, organizationDTO.getType());
   transformedOrgMap.put(LABEL_COLUMN_CONFIG, organizationDTO.getLabel());
   transformedOrgMap.put(ICON_COLUMN, organizationDTO.getIcon());
   transformedOrgMap.put(PORTALS, getSepratedValues(organizationDTO.getPortals()));
   transformedOrgMap.put(TAXONOMY_CODE, getSepratedValues(organizationDTO.getTaxonomyIds()));
   transformedOrgMap.put(TARGET_CLASS_CODE, getSepratedValues(organizationDTO.getKlassIds()));
   transformedOrgMap.put(SYSTEM_CODE, getSepratedValues(organizationDTO.getSystems()));
   transformedOrgMap.put(ENDPOINT_CODE,getSepratedValues(organizationDTO.getEndpointIds()));
   
   List<IConfigRoleDTO> roles = organizationDTO.getRoles();
   List<Map<String, Object>> roleList = new ArrayList<Map<String, Object>>();
   
     for (IConfigRoleDTO roleDTO : roles) {
         fillRolesForOrganization(transformedRoleMap, roleDTO);
         roleList.add(transformedRoleMap);
         }
     transformedOrgMap.put(ROLES, roleList);
     orgList.add(transformedOrgMap);
  
   }


private void fillRolesForOrganization(Map<String, Object> transformedRoleMap, IConfigRoleDTO roleDTO)
{
  transformedRoleMap.put(ROLE_CODE, roleDTO.getCode());
  transformedRoleMap.put(ROLE_LABEL, roleDTO.getLabel());
  transformedRoleMap.put(ROLE_TYPE, roleDTO.getRoleType());
  transformedRoleMap.put(ROLE_PHYSICAL_CATALOG, getSepratedValues(roleDTO.getPhysicalCatalogs()));
  transformedRoleMap.put(ROLE_PORTALS, getSepratedValues(roleDTO.getPortals()));
  transformedRoleMap.put(ROLE_TAXONOMY_CODE, getSepratedValues(roleDTO.getTargetTaxonomies()));
  transformedRoleMap.put(ROLE_TARGET_CLASS_CODE, getSepratedValues(roleDTO.getTargetKlasses()));
  transformedRoleMap.put(ROLE_USERS, getSepratedValues(roleDTO.getUsers()));
  transformedRoleMap.put(ROLE_ENABLED_DASHBOARD, roleDTO.isDashboardEnable() ? "1" : "0");
  transformedRoleMap.put(ROLE_KPI,getSepratedValues( roleDTO.getKpis()));
  transformedRoleMap.put(ROLE_ENTITIES, getSepratedValues( roleDTO.getEntities()));
  transformedRoleMap.put(ROLE_SYSTEM_CODE, getSepratedValues( roleDTO.getSystems()));
  transformedRoleMap.put(ROLE_ENDPOINT_CODE, getSepratedValues(roleDTO.getEndpoints()));
  transformedRoleMap.put(ROLE_READONLY_USER,roleDTO.isReadOnly() ? "1" : "0");
  
}

/**
 * Transform config Language to JSON.
 *
 * @param transformedJSONMap
 * @param languageDTO
 */
private void generateConfigLanguageJSON(Map<String, Object> transformedJSONMap, IConfigLanguageDTO languageDTO)
{
  if (transformedJSONMap.get(CSEObjectType.LanguageConf.name()) == null) {
    transformedJSONMap.put(CSEObjectType.LanguageConf.name(), new ArrayList<>());
  }
  List<Map<String, String>> languageList = (List<Map<String, String>>) transformedJSONMap.get(CSEObjectType.LanguageConf.name());
  Map<String, String> transformedTabMap = new LinkedHashMap<>();
  
  transformedTabMap.put(CODE_COLUMN_CONFIG, ((RootConfigDTO) languageDTO).getCode());
  transformedTabMap.put(LABEL_COLUMN_CONFIG, languageDTO.getLabel());
  transformedTabMap.put(PARENT_CODE_COLUMN, languageDTO.getParentCode());
  transformedTabMap.put(ABBREVIATION_CONFIG, languageDTO.getAbbreviation());
  transformedTabMap.put(LOCALE_ID_CONFIG, languageDTO.getLocaleId());
  transformedTabMap.put(NUMBER_FORMAT_CONFIG, languageDTO.getNumberFormat());
  transformedTabMap.put(DATE_FORMAT_CONFIG, languageDTO.getDateFormat());
  transformedTabMap.put(ICON_COLUMN, languageDTO.getIcon());
  transformedTabMap.put(IS_DATA_LANGUAGE_CONFIG, languageDTO.isDataLanguage() ? TRUE : FALSE);
  transformedTabMap.put(IS_UI_LANGUAGE_CONFIG , languageDTO.isUserInterfaceLanguage() ? TRUE : FALSE);
  transformedTabMap.put(IS_DEFULT_LANGUAGE_CONFIG , languageDTO.isDefaultLanguage() ? TRUE : FALSE);
  languageList.add(transformedTabMap);
  
}

/**
 * Transform config Context to JSON
 * 
 * @param transformedJSONMap
 * @param configContextDTO
 */
private void generateConfigContextJSON(Map<String, Object> transformedJSONMap, IConfigContextDTO configContextDTO)
{
  if (transformedJSONMap.get(CSEObjectType.Context.name()) == null) {
    transformedJSONMap.put(CSEObjectType.Context.name(), new ArrayList<>());
  }
  // entities Map
  Map<String, String> entitiesToAddMap = new HashMap<String, String>();
  // type of Context Map
  Map<IContextDTO.ContextType, String> typeOfContextMap = new HashMap<>();
  
  prepareEntitiesAndTypeMapForContext(entitiesToAddMap, typeOfContextMap);
  
  List<Map<String, Object>> contextList = (List<Map<String, Object>>) transformedJSONMap.get(CSEObjectType.Context.name());
  Map<String, Object> transformedContextMap = new LinkedHashMap<>();
  transformedContextMap.put(CODE_COLUMN_CONFIG, configContextDTO.getContextDTO().getCode());
  transformedContextMap.put(LABEL_COLUMN_CONFIG, configContextDTO.getLabel());
  transformedContextMap.put(TYPE_COLUMN, typeOfContextMap.getOrDefault(configContextDTO.getContextDTO().getContextType(),
      configContextDTO.getContextDTO().getContextType().name()));
  transformedContextMap.put(ICON_COLUMN, configContextDTO.getIcon());
  transformedContextMap.put(IS_TIME_ENABLED, configContextDTO.isTimeEnabled() ? TRUE : FALSE);
  transformedContextMap.put(ENABLE_TIME_FROM, DiTransformationUtils.getTimeStampForFormat(
      configContextDTO.getDefaultStartTime() != 0 ? configContextDTO.getDefaultStartTime() : null, DiConstants.DATE_FORMAT));
  transformedContextMap.put(ENABLE_TIME_TO, DiTransformationUtils.getTimeStampForFormat(
      configContextDTO.getDefaultEndTime() != 0 ? configContextDTO.getDefaultEndTime() : null, DiConstants.DATE_FORMAT));
  transformedContextMap.put(USE_CURRENT_TIME, configContextDTO.isCurrentTime() ? TRUE : FALSE);
  transformedContextMap.put(ALLOW_DUPLICATES, configContextDTO.isDuplicateVariantAllowed() ? TRUE : FALSE);
  List<String> entities = new ArrayList<>();
  for (String entity : configContextDTO.getEntities()) {
    entities.add(entitiesToAddMap.getOrDefault(entity, entity));
  }
  transformedContextMap.put(ENTITIES, getSepratedValues(entities));
  
  JSONObject tagCodes = (JSONObject) configContextDTO.getTagCodes().toJSONObject();
  List<Map<String, Object>> tagList = new ArrayList<Map<String, Object>>();
  Map<String, Object> tagAndTagValuesMap = null;
  for (Object tagKey : tagCodes.keySet()) {
    tagAndTagValuesMap = new HashMap<String, Object>();
    tagAndTagValuesMap.put(SELECTED_TAG, (String) tagKey);
    tagAndTagValuesMap.put(SELECTED_TAG_VALUES, (List<String>) getSepratedValues((Collection<String>) tagCodes.get(tagKey)));
    tagList.add(tagAndTagValuesMap);
  }
  transformedContextMap.put(SELECTED_TAGS, tagList);
  contextList.add(transformedContextMap);
}

/**
 * 
 * @param bool
 * @return
 */
private String booleanToString(Boolean bool)
{
  return bool == true ? TRUE : FALSE;
}


protected Object getSepratedValues(Collection<String> collection)
{
  return  collection;
}
protected Object getSepratedValuesForTag(Collection<Object> collection)
{
  return  collection;
}

protected Object getDefaultValue(IConfigSectionElementDTO obj)
{
  if(ENTITY_TAG.equals(obj.getType()))
    return Arrays.asList(obj.getDefaultValue().split(SEPARATOR));
  return obj.getDefaultValue();
}
}