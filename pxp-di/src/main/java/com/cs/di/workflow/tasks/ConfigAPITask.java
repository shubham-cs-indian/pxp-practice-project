package com.cs.di.workflow.tasks;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.di.config.model.configapi.ConfigAPIRequestModel;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;
import com.cs.di.config.model.configapi.IConfigAPIResponseModel;
import com.cs.di.config.strategy.api.IConfigurationAPIStrategy;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.ConfigNodeType;
import com.cs.di.workflow.constants.EntityAction;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component("configAPITask")
public class ConfigAPITask extends AbstractTask {
  
  private static final String                    NODE           = "NODE";
  private static final String                    ACTION_CONFIG  = "ACTION_CONFIG";
  public static final String                     DATA_TO_IMPORT = "DATA_TO_IMPORT";
  public static final String                     CONFIG_DETAILS = "CONFIG_DETAILS";
  
  public static final List<String>               INPUT_LIST     = Arrays.asList(NODE, ACTION_CONFIG, DATA_TO_IMPORT);
  
  public static final List<String>               OUTPUT_LIST    = Arrays.asList(CONFIG_DETAILS, EXECUTION_STATUS);
  
  public static final List<WorkflowType>         WORKFLOW_TYPES = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  
  public static final List<EventType>            EVENT_TYPES    = Arrays.asList(EventType.BUSINESS_PROCESS, EventType.INTEGRATION);
  
  @Autowired
  private Map<String, IConfigurationAPIStrategy> configurationAPIMap;

  @Autowired
  private TransactionThreadData transactionThreadData;
  
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
  public void executeTask(WorkflowTaskModel model)
  {
    ConfigNodeType node = DiValidationUtil.validateAndGetRequiredEnum(model, NODE, ConfigNodeType.class);
    EntityAction action = DiValidationUtil.validateAndGetRequiredEnum(model, ACTION_CONFIG, EntityAction.class);
    String data = DiValidationUtil.validateAndGetRequiredString(model, DATA_TO_IMPORT);
    if (model.getExecutionStatusTable().isErrorOccurred()) {
      return;
    }
    transactionThreadData.setTransactionData((TransactionData) model.getWorkflowModel().getTransactionData());
    IConfigAPIResponseModel responseModel = executeAPI(new ConfigAPIRequestModel(node, action, data), model.getExecutionStatusTable());
    model.getOutputParameters().put(CONFIG_DETAILS, responseModel.getConfigDetails());
  }
  
  /**
   * Execute API strategy
   * 
   * @param requestModel
   * @param executionStatus
   * @return
   */
  private IConfigAPIResponseModel executeAPI(IConfigAPIRequestModel requestModel, IExecutionStatus executionStatus)
  {
    IConfigurationAPIStrategy configurationAPIStrategy = configurationAPIMap.get(getConfigurationAPIStrategy(requestModel.getNodeType()));
    try {
      if (configurationAPIStrategy != null) {
        return configurationAPIStrategy.execute(requestModel);
      }
      else {
        RDBMSLogger.instance().exception(new UnsupportedOperationException());
        executionStatus.addError(MessageCode.GEN039);
      }
    }
    catch (Exception e) {
      executionStatus.addError(MessageCode.GEN500);
    }
    return null;
  }
  
  /**
   * Get API for a given config node type
   * 
   * @param nodeType
   * @return
   */
  private String getConfigurationAPIStrategy(ConfigNodeType nodeType)
  {
    switch (nodeType) {
      case ATTRIBUTE:
        return "attributeAPIStrategy";
      case TAG:
        return "tagAPIStrategy";
      case HIERARCHY:
        return "hierarchyAPIStrategy";
      case PROPERTY_COLLECTION:
        return "propertyCollectionAPIStrategy";
      case TAB:
        return "tabAPIStrategy";
      case MASTERTAXONOMY:
        return "masterTaxonomyAPIStrategy";
      case PARTNER:
        return "partnerAPIStrategy";
      case RELATIONSHIP:
        return "relationshipAPIStrategy";
      case USER:
        return "usersAPIStrategy";
      case LANGUAGE:
        return "languageAPIStrategy";
      case CONTEXT:
        return "contextAPIStrategy";
      case TEXTASSET:
        return "textAssetAPIStrategy";  
      case TRANSLATION:
        return "translationAPIStrategy";
      case RULE:
        return "datarulesAPIStrategy";
       case ARTICLE:
         return "articleAPIStrategy";
       case ASSET:
         return "assetAPIStrategy";
       case SUPPLIER:
         return "supplierAPIStrategy";
      // TODO: PXPFDEV-21451: Deprecate Virtual Catalog  
      /*case VIRTUALCATALOG:
        return "virtualCatalogAPIStrategy";*/ 
      default:
        return null;
    }
    
  }
  
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> returnList = new ArrayList<>();
    String action = (String) inputFields.get(ACTION_CONFIG);
    if (DiValidationUtil.isBlank(action)) {
      returnList.add(action);
    }
    String node = (String) inputFields.get(NODE);
    if (DiValidationUtil.isBlank(node)) {
      returnList.add(node);
    }
    String data = (String) inputFields.get(DATA_TO_IMPORT);
    if (DiValidationUtil.isBlank(data)) {
      returnList.add(data);
    }
    return returnList;
  }
}
