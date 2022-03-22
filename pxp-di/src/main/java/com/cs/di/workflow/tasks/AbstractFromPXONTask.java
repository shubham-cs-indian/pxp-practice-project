package com.cs.di.workflow.tasks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleAttributeMappingModel;
import com.cs.core.config.interactor.model.mapping.IOutBoundMappingModel;
import com.cs.core.config.interactor.model.mapping.OutBoundMappingModel;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.di.config.interactor.mappings.IGetOutBoundMappingForExport;
import com.cs.di.config.interactor.model.configdetails.GetConfigDataForTransformRequestModel;
import com.cs.di.config.interactor.model.configdetails.IGetConfigDataForTransformResponseModel;
import com.cs.di.config.strategy.usecase.configdetails.IGetConfigDataForTransformStrategy;
import com.cs.di.runtime.utils.DiTransformationUtils;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;

/**
 * 
 * @author mayuri.wankhade 
 * This Class contain all common method used for PXON to
 * JSON/Excel Transformation
 *
 */
public abstract class AbstractFromPXONTask extends AbstractTransformationTask {
	
  public static final String             PXON                 = "PXON";
  public static final String             FAILED_FILES         = "FAILED_FILES";
  public static final String             JSON                 = "JSON";
  public static final String             EXCEL                = "EXCEL";
  public static final String             ASSETS               = "assets";
  public static final String             ARTICLE              = "content";
  public static final String             EMBEDDED_VARIANTS    = "embeddedVariants";
  public static final String             RELATIONSHIPS        = "relationships";
  public static final String             NATURE_RELATIONSHIPS = "natureRelationships";
  public static final String             TRANSFORMED_DATA     = "TRANSFORMED_DATA"; // map the output of the component
  public static final String             PXON_FILE_PATH       = "PXON_FILE_PATH";// Absolute file path to Export as JSON/EXCEL
  public static final String             OUTBOUND_MAPPING_ID  = "OUTBOUND_MAPPING_ID";
  
  public static final List<WorkflowType> WORKFLOW_TYPES       = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  public static final List<EventType>    EVENT_TYPES          = Arrays.asList(EventType.INTEGRATION);

  // Internal input parameters
  public static final String             OUTBOUND_MAPPING         = "OUTBOUND_MAPPING";
  public static final String             KLASS_ID_MAPPING_MAP     = "KLASS_ID_MAPPING_MAP";
  public static final String             TAXONOMY_ID_MAPPING_MAP  = "TAXONOMY_ID_MAPPING_MAP";
  public static final String             MAPPED_ATTRIBUTE_IDS_SET = "MAPPED_ATTRIBUTE_IDS_SET";
  public static final String             MAPPED_TAG_IDS_SET       = "MAPPED_TAG_IDS_SET";
  public static final String             RELATIONSHIP_ID_MAPPING_MAP = "RELATIONSHIP_ID_MAPPING_MAP";
  
  @Autowired
  IGetConfigDataForTransformStrategy     getConfigDataForTransformStrategy;
  
  @Autowired
  IGetOutBoundMappingForExport           getOutBoundMappingForExport;
 
  public abstract void generateFromPXON(WorkflowTaskModel model);

	@Override
	public List<WorkflowType> getWorkflowTypes() {
		return WORKFLOW_TYPES;
	}

	@Override
	public List<EventType> getEventTypes() {
		return EVENT_TYPES;
	}

	@Override
	public TaskType getTaskType() {
		return TaskType.SERVICE_TASK;
	}
  
  @Override
  public void transform(WorkflowTaskModel model) throws Exception
  {
    initializeMapping(model);     
    generateFromPXON(model);
  }
  
	/**
	 * Method to get Config Details for given Param
	 * @return 
	 * @throws Exception
	 */
  protected IGetConfigDataForTransformResponseModel getConfigDetails() throws Exception
  { 
    return getConfigDataForTransformStrategy.execute(new GetConfigDataForTransformRequestModel());
  }
    
  /**
   * @param attributesMap
   * @param tagsMap
   * @param attributeVariantsList
   * @param baseEntityDTO
   * @param configDetails
   * @param transformedExcelMap
   * @param workflowTaskModel
   */
  @SuppressWarnings("unchecked")
  public void convertPropertiesFromPXON(Map<String, Object> attributesMap,
      Map<String, Object> tagsMap, List<Map<String, Object>> attributeVariantsList,
      IBaseEntityDTO baseEntityDTO, IGetConfigDataForTransformResponseModel configDetails,
      Map<String, Object> transformedExcelMap, WorkflowTaskModel workflowTaskModel)
  {
    Set<String> mappedAttributeIdsSet = (Set<String>) workflowTaskModel.getInputParameters()
        .get(MAPPED_ATTRIBUTE_IDS_SET);
    Set<String> mappedTagIdsSet = (Set<String>) workflowTaskModel.getInputParameters()
        .get(MAPPED_TAG_IDS_SET);
    Map<String, IConfigRuleAttributeMappingModel> relationshipIdMappingMap = (Map<String, IConfigRuleAttributeMappingModel>) workflowTaskModel
        .getInputParameters()
        .get(RELATIONSHIP_ID_MAPPING_MAP);
    IOutBoundMappingModel outboundMapping = (IOutBoundMappingModel) workflowTaskModel
        .getInputParameters()
        .get(OUTBOUND_MAPPING);
    Boolean isAllProperyCollectionSelected = outboundMapping.getIsAllPropertyCollectionSelected();
    Set<IPropertyRecordDTO> propertyRecords = baseEntityDTO.getPropertyRecords();
    for (IPropertyRecordDTO propertyRecord : propertyRecords) {
      String propertyType = propertyRecord.getProperty().getPropertyType().name();
      if (propertyRecord instanceof IValueRecordDTO) {
        IValueRecordDTO valueRecoredDTO = ((IValueRecordDTO) propertyRecord);
        IContextualDataDTO contextualObject = valueRecoredDTO.getContextualObject();
        if (contextualObject != null && contextualObject.getContextCode() != null
            && !contextualObject.getContextCode().isBlank()) {
          // Preparing attribute variant to export
          transformAttributeVariantFromPXON(propertyRecord, valueRecoredDTO, baseEntityDTO,
              contextualObject, attributeVariantsList, configDetails, transformedExcelMap);
        }
        else {
          // Preparing attribute to export
          if (isAllProperyCollectionSelected || (mappedAttributeIdsSet.contains(propertyRecord.getProperty().getCode()))) {
            DiTransformationUtils.prepareAttributeToExport(attributesMap, propertyRecord, valueRecoredDTO);
          }
        }
      }
      else if (propertyRecord instanceof ITagsRecordDTO) {
        // Preparing tag to export
        if (isAllProperyCollectionSelected || mappedTagIdsSet.contains(propertyRecord.getProperty().getCode())) {
          DiTransformationUtils.prepareTagToExport(tagsMap, (ITagsRecordDTO) propertyRecord, propertyType);
        }
      }
      else if (propertyRecord instanceof IRelationsSetDTO) {
        // Preparing relationship to export
        if (outboundMapping.getIsAllRelationshipsSelected() || relationshipIdMappingMap.containsKey(propertyRecord.getProperty().getCode()))
          transformRelationshipFromPXON((IRelationsSetDTO) propertyRecord, baseEntityDTO.getBaseEntityID(), transformedExcelMap, configDetails);
      }
    }
  }
  
  abstract void transformAttributeVariantFromPXON(IPropertyRecordDTO propertyRecord,
      IValueRecordDTO valueRecoredDTO, IBaseEntityDTO baseEntityDTO, IContextualDataDTO contextDTO,
      List<Map<String, Object>> attributeVariantsList,
      IGetConfigDataForTransformResponseModel configDetails,
      Map<String, Object> transformedExcelMap);
  
  abstract void transformRelationshipFromPXON(IRelationsSetDTO relationshipDTO, String side1Id,
      Map<String, Object> transformedExcelMap,
      IGetConfigDataForTransformResponseModel configDetails);
  
  private void initializeMapping(WorkflowTaskModel model) throws Exception
  {
    String outboundMappingId = (String) model.getWorkflowModel().getWorkflowParameterMap().get(IEndpointModel.MAPPINGS);
    IOutBoundMappingModel outboundMapping;
    if (outboundMappingId == null) {
      outboundMapping = new OutBoundMappingModel();
    }
    else {
      IOutBoundMappingModel requestModel = new OutBoundMappingModel();
      requestModel.setId(outboundMappingId);
      outboundMapping = getOutBoundMappingForExport.execute(requestModel);
    }
    
    model.getInputParameters()
    .put(OUTBOUND_MAPPING, outboundMapping);
    
    Map<String, IConfigRuleAttributeMappingModel> klassIdMappingMap = outboundMapping
        .getClassMappings()
        .stream()
        .filter(x -> outboundMapping.getIsAllClassesSelected() || !x.getIsIgnored())
        .map(tagValueMapping -> tagValueMapping)
        .collect(Collectors.toMap(x -> x.getMappedElementId(), x -> x, (a, b) -> {
          return b;
        }));
    model.getInputParameters()
        .put(KLASS_ID_MAPPING_MAP, klassIdMappingMap);
    
    Map<String, IConfigRuleAttributeMappingModel> taxonomyIdMappingMap = outboundMapping
        .getTaxonomyMappings()
        .stream()
        .filter(x -> outboundMapping.getIsAllTaxonomiesSelected() || !x.getIsIgnored())
        .map(tagValueMapping -> tagValueMapping)
        .collect(Collectors.toMap(x -> x.getMappedElementId(), x -> x, (a, b) -> {
          return b;
        }));
    model.getInputParameters()
        .put(TAXONOMY_ID_MAPPING_MAP, taxonomyIdMappingMap);
    
    Map<String, IConfigRuleAttributeMappingModel> relationshipIdMappingMap = outboundMapping
        .getRelationshipMappings()
        .stream()
        .filter(x -> outboundMapping.getIsAllRelationshipsSelected() || !x.getIsIgnored())
        .map(tagValueMapping -> tagValueMapping)
        .collect(Collectors.toMap(x -> x.getMappedElementId(), x -> x, (a, b) -> {
          return b;
        }));
    model.getInputParameters()
        .put(RELATIONSHIP_ID_MAPPING_MAP, relationshipIdMappingMap);
    
    Set<String> mappedAttributeIdsSet = outboundMapping.getAttributeMappings()
        .stream()
        .filter(x -> outboundMapping.getIsAllPropertyCollectionSelected() || !x.getIsIgnored())
        .map(x -> x.getMappedElementId())
        .collect(Collectors.toSet());
    model.getInputParameters()
    .put(MAPPED_ATTRIBUTE_IDS_SET, mappedAttributeIdsSet);
    
    Set<String> mappedTagIdsSet = outboundMapping.getTagMappings()
        .stream()
        .filter(x -> outboundMapping.getIsAllPropertyCollectionSelected() || !x.getIsIgnored())
        .map(x -> x.getMappedElementId())
        .collect(Collectors.toSet());
    model.getInputParameters()
    .put(MAPPED_TAG_IDS_SET, mappedTagIdsSet);
  }
  
}
