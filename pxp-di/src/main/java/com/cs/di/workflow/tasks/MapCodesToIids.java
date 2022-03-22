package com.cs.di.workflow.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author mangesh.metkari
 *
 */

@Component("mapCodesToIIDs")
public class MapCodesToIids extends AbstractTask {
   
  public static final String               BASE_ENTITY_IDS  = "BASE_ENTITY_IDS";
  
  public static final String               BASE_ENTITY_IIDS = "BASE_ENTITY_IIDS";
  
  public static final List<String>         INPUT_LIST       = Arrays.asList(BASE_ENTITY_IDS);
  
  public static final List<String>         OUTPUT_LIST      = Arrays.asList(BASE_ENTITY_IIDS, EXECUTION_STATUS);
  
  public static final List<WorkflowType>   WORKFLOW_TYPES   = Arrays.asList(WorkflowType.STANDARD_WORKFLOW);
  
  public static final List<EventType>      EVENT_TYPES      = Arrays.asList(EventType.BUSINESS_PROCESS,
      EventType.INTEGRATION);
  
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
  public List<String> validate(Map<String, Object> inputFields)
  {
    List<String> returnList = new ArrayList<String>();
    if (DiValidationUtil.isBlank((String) inputFields.get(BASE_ENTITY_IDS))) {
      returnList.add(BASE_ENTITY_IDS);
    }
    return returnList;
 
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void executeTask(WorkflowTaskModel model)
  {
    List<String> baseEntityIids = new ArrayList<String>();
    Object param = model.getInputParameters().get(BASE_ENTITY_IDS);
    List<String> entityCodes = null;
    if (param instanceof Collection<?>) {
      entityCodes = (List<String>) DiValidationUtil.validateAndGetOptionalCollection(model, BASE_ENTITY_IDS);
    }
    else if (param instanceof String) {
      String codes = param.toString();
      try {
        entityCodes = new ObjectMapper().readValue(codes, List.class);
      }
      catch (JsonProcessingException e) {
        model.getExecutionStatusTable()
            .addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005, new String[] { BASE_ENTITY_IDS });
      }
    }
    
    ITransactionData transactionData = model.getWorkflowModel().getTransactionData();
    String endpointId = transactionData.getEndpointId();
    
   for (String entityCode : entityCodes) {
      ILocaleCatalogDAO catalogDAO;
      try {
        
        ILocaleCatalogDTO localeCatalogDTO = RDBMSUtils.newUserSessionDAO().newLocaleCatalogDTO(transactionData.getDataLanguage(),
            transactionData.getPhysicalCatalogId(), transactionData.getOrganizationId());
        catalogDAO = RDBMSUtils.newUserSessionDAO().openLocaleCatalog( model.getWorkflowModel().getUserSessionDto(), localeCatalogDTO);
        Long baseEntityIid = catalogDAO.getEntityIIDByID(entityCode, endpointId);
        baseEntityIids.add(baseEntityIid.toString());
      }
      catch (Exception e) {
        e.printStackTrace();
        
        model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.ENTITY_ID }, MessageCode.GEN005,
            new String[] { (String) entityCode });
      }
   }
   model.getOutputParameters().put(BASE_ENTITY_IIDS, baseEntityIids);
  }
}

