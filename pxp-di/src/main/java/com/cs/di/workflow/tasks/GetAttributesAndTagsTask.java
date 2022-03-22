package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.runtime.entity.dao.IEntityViolationDAO;
import com.cs.di.runtime.utils.DiTransformationUtils;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;
/**
 * The GetAttributesAndTagsTask provides the functionality to export Attribute and Tags based on selection .
 * @author priyaranjan.kumar
 *
 */
@Component("getAttributesAndTagsTask")
public class GetAttributesAndTagsTask extends AbstractTask {

  public static final String             KLASS_INSTANCE_ID            = "KLASS_INSTANCE_ID";
  public static final String             ATTRIBUTES_VALUES_TO_FETCH   = "ATTRIBUTES_VALUES_TO_FETCH";
  public static final String             TAGS_VALUES_TO_FETCH         = "TAGS_VALUES_TO_FETCH";
  public static final String             METADATA_VALUES_TO_FETCH    = "METADATA_VALUES_TO_FETCH";
  
  public static final String             ATTRIBUTES_AND_TAGS          = "ATTRIBUTES_AND_TAGS";
  public static final String             ENTITY_TYPE                  = "ENTITY_TYPE";
  
  public static final List<String>       INPUT_LIST                   = Arrays.asList(KLASS_INSTANCE_ID, ATTRIBUTES_VALUES_TO_FETCH , TAGS_VALUES_TO_FETCH, METADATA_VALUES_TO_FETCH );
  public static final List<String>       OUTPUT_LIST                  = Arrays.asList(ATTRIBUTES_AND_TAGS, EXECUTION_STATUS);
  public static final List<WorkflowType> WORKFLOW_TYPES               = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  public static final List<EventType>    EVENT_TYPES                  = Arrays.asList(EventType.BUSINESS_PROCESS);
  
  public static final String             IS_MANDATORY_VIOLATED        = "isMandatoryViolated";
  public static final String             IS_SHOULD_VIOLATED           = "isShouldViolated";
  public static final String             TYPES                        = "types";
  public static final String             TAXONOMY_IDS                 = "taxonomyIds";
  public static final int                SHOULD_VIOLATION_CODE        = 1;
  public static final int                MANDATORY_VIOLATION_CODE     = 0;
  
  
  @SuppressWarnings("unchecked")
  @Override
  public void executeTask(WorkflowTaskModel workflowTaskModel)
  {
   
    List<String> tagsValuesToFetch  = (List<String>) DiValidationUtil.validateAndGetRequiredCollectionAllowEmpty(workflowTaskModel, TAGS_VALUES_TO_FETCH);
    List<String> attributesValuesToFetch = (List<String>) DiValidationUtil.validateAndGetRequiredCollectionAllowEmpty(workflowTaskModel, ATTRIBUTES_VALUES_TO_FETCH);
    List<String> metaDataValuesToFetch = (List<String>) DiValidationUtil.validateAndGetRequiredCollectionAllowEmpty(workflowTaskModel, METADATA_VALUES_TO_FETCH);
    String klassInstanceId = DiValidationUtil.validateAndGetRequiredString(workflowTaskModel, KLASS_INSTANCE_ID);
      if ((tagsValuesToFetch == null || tagsValuesToFetch.isEmpty())
        && (attributesValuesToFetch == null || attributesValuesToFetch.isEmpty())) {
      return;
    } 
      ILocaleCatalogDAO localeCatalogDAO = DiUtils.createLocaleCatalogDAO(
          (IUserSessionDTO) workflowTaskModel.getWorkflowModel().getUserSessionDto(),
          (ITransactionData) workflowTaskModel.getWorkflowModel().getTransactionData());
      try {
        IBaseEntityDTO baseEntityDTO = localeCatalogDAO.getEntityByIID(Long.parseLong(klassInstanceId));
        Collection<IPropertyDTO> allEntityProperties = localeCatalogDAO.getAllEntityProperties(baseEntityDTO.getBaseEntityIID());
        IBaseEntityDAO baseEntityDAO = localeCatalogDAO.openBaseEntity(baseEntityDTO);
        IBaseEntityDTO baseEntityWithProperties = baseEntityDAO.loadPropertyRecords( allEntityProperties.toArray(new IPropertyDTO[allEntityProperties.size()]));
        Map<String, Object> responseMap = new HashMap<>();
        exportSelectedProperties(baseEntityWithProperties, attributesValuesToFetch, tagsValuesToFetch, responseMap);
        exportSelectedMetaDataList(baseEntityWithProperties, metaDataValuesToFetch, responseMap);
        Set<String> taxonomyIds = new HashSet<>();
        Set<String> types = new HashSet<>();       
        types.add(baseEntityWithProperties.getNatureClassifier().getClassifierCode());
        DiTransformationUtils.getSecondaryKlassesAndTaxonomies(types, taxonomyIds, baseEntityDTO, workflowTaskModel);
        IEntityViolationDAO propertyViolationDAO = localeCatalogDAO.openEntityViolationDAO();
        responseMap.put(IS_MANDATORY_VIOLATED, propertyViolationDAO.checkViolation(baseEntityWithProperties.getBaseEntityIID(),MANDATORY_VIOLATION_CODE));
        responseMap.put(IS_SHOULD_VIOLATED, propertyViolationDAO.checkViolation(baseEntityWithProperties.getBaseEntityIID(), SHOULD_VIOLATION_CODE));
        responseMap.put(TYPES, types);
        responseMap.put(TAXONOMY_IDS, taxonomyIds);
        workflowTaskModel.getOutputParameters().put(ATTRIBUTES_AND_TAGS, responseMap);
        
      }
      catch (RDBMSException | CSFormatException exception) {
        RDBMSLogger.instance().exception(exception);
      }
     
  }
    
  /**
   * Responsible to prepare the data to export metaData of instance.
   * @param baseEntityWithProperties
   * @param metaDataValuesToFetch
   * @param responseMap
   */
  private void exportSelectedMetaDataList(IBaseEntityDTO baseEntityWithProperties, List<String> metaDataValuesToFetch, Map<String, Object> responseMap)
  {
   for(String metaDatField : metaDataValuesToFetch) 
   {
      switch (metaDatField) 
      {
        case CommonConstants.SOURCE_ORGANIZATION:
          responseMap.put(CommonConstants.SOURCE_ORGANIZATION, baseEntityWithProperties.getSourceOrganizationCode());
          break;
          
        default:
          break;
      }
   }
  }

  /**
   * Responsible to prepare the data to export attribute and tags.
   * @param baseEntityDTO
   * @param attributesValuesToFetch
   * @param tagsValuesToFetch
   * @param responseMap
   */
  private void exportSelectedProperties(IBaseEntityDTO baseEntityDTO, List<String> attributesValuesToFetch, List<String> tagsValuesToFetch,
      Map<String, Object> responseMap)
  {
    
    Set<IPropertyRecordDTO> propertyRecords = baseEntityDTO.getPropertyRecords();
    for (IPropertyRecordDTO propertyRecord : propertyRecords) {
      String propertyType = propertyRecord.getProperty().getPropertyType().name();
      if (propertyRecord instanceof IValueRecordDTO) {
        IValueRecordDTO valueRecoredDTO = ((IValueRecordDTO) propertyRecord);
        IContextualDataDTO contextualObject = valueRecoredDTO.getContextualObject();
        if (contextualObject != null && contextualObject.getContextCode() != null
            && !contextualObject.getContextCode().isBlank()) {
          continue;
        }
        else {
          // Preparing attribute to export
          if (attributesValuesToFetch.contains(propertyRecord.getProperty().getCode())) {
            DiTransformationUtils.prepareAttributeToExport(responseMap, propertyRecord, valueRecoredDTO);
          }
        }
      }
      else if (propertyRecord instanceof ITagsRecordDTO) {
        // Preparing tag to export
        if (tagsValuesToFetch.contains(propertyRecord.getProperty().getCode())) {
          DiTransformationUtils.prepareTagToExport(responseMap, (ITagsRecordDTO) propertyRecord, propertyType);
        }
      }
      
    }
    
  }
  
  /**
   * Validate the valid input for the task
   * 
   */
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> returnList = new ArrayList<String>();
    String klassInstanceId = (String) inputFields.get(KLASS_INSTANCE_ID);
    if (DiValidationUtil.isBlank(klassInstanceId)) {
      returnList.add(KLASS_INSTANCE_ID);
    }
    return returnList;
  }

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
 
}
