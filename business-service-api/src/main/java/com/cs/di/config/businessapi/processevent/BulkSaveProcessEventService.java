package com.cs.di.config.businessapi.processevent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.processevent.IBulkSaveProcessEventResponseModel;
import com.cs.core.config.interactor.model.processevent.IProcessEventModel;
import com.cs.core.config.interactor.model.processevent.IValidationInformationModel;
import com.cs.core.config.interactor.model.processevent.ValidationInformationModel;
import com.cs.core.config.strategy.usecase.task.IGetProcessDefinitionByIdStrategy;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.camunda.IGetCamundaProcessDefinitionResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.dataintegration.IJMSConfigModel;
import com.cs.core.runtime.interactor.model.dataintegration.JMSConfigModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.strategy.cammunda.broadcast.IBulkUploadProcessToServerStrategy;
import com.cs.di.config.interactor.model.initializeworflowevent.BulkProcessEventSaveResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IBulkProcessEventSaveResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetAllProcessEventsResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;
import com.cs.di.config.strategy.usecase.processevent.IBulkSaveProcessEventStrategy;
import com.cs.di.config.strategy.usecase.processevent.IGetAllProcessEventsStrategy;
import com.cs.di.config.strategy.usecase.processevent.IGetProcessEventStrategy;
import com.cs.di.jms.IJMSMessageListenerService;
import com.cs.workflow.base.IAbstractExecutionReader;
import com.cs.workflow.base.IAbstractTask;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.TasksFactory;
import com.cs.workflow.base.WorkflowConstants;
import com.cs.workflow.base.WorkflowType;

@Service
@SuppressWarnings("unchecked")
public class BulkSaveProcessEventService
    extends AbstractSaveConfigService<IListModel<ISaveProcessEventModel>, IBulkProcessEventSaveResponseModel>
    implements IBulkSaveProcessEventService {

  @Autowired
  protected IBulkSaveProcessEventStrategy      bulkSaveProcessEventStrategy;

  @Autowired
  protected IBulkUploadProcessToServerStrategy bulkUploadProcessesToServerStrategy;

  @Autowired
  protected IGetProcessDefinitionByIdStrategy  getProcessDefinitionByIdStrategy;

  @Autowired
  protected IAbstractExecutionReader           camundaTask;

  @Autowired
  protected TasksFactory                       tasksFactory;

  @Autowired
  protected IGetProcessEventStrategy           getProcessEventStrategy;

  @Autowired
  protected IGetAllProcessEventsStrategy       getAllProcessEventsStrategy;

   @Autowired
  protected IJMSMessageListenerService         jmsMessageListenerService;
  
  @Override
  public IBulkProcessEventSaveResponseModel executeInternal(IListModel<ISaveProcessEventModel> saveProcessesModel) throws Exception
  {
    IListModel<ISaveProcessEventModel> updatedSaveProcessModel = new ListModel<ISaveProcessEventModel>();
    IExceptionModel failure = new ExceptionModel();
    updateIsExecutable(saveProcessesModel, updatedSaveProcessModel, failure);
    IBulkProcessEventSaveResponseModel bulkSaveProcessResponseModel = new BulkProcessEventSaveResponseModel();
    Map<String, IProcessEventModel> oldProcessEvents = getOldProcessEvents(updatedSaveProcessModel);
    if (!updatedSaveProcessModel.getList().isEmpty()) {
      bulkSaveProcessResponseModel = bulkSaveProcessEventStrategy.execute(updatedSaveProcessModel);
    }
    bulkSaveProcessResponseModel.getSuccess().getProcessEventsList().forEach(process -> {
      if (WorkflowType.JMS_WORKFLOW.name().equals(process.getWorkflowType())) {
        IProcessEventModel oldProcessEvent = oldProcessEvents.get(process.getId());
        IJMSConfigModel oldJmsConfig = getJmsConfigModel(oldProcessEvent);
        IJMSConfigModel newJMSConfig = getJmsConfigModel(process);
            jmsMessageListenerService.replaceJMSListener(oldJmsConfig, oldProcessEvent.getIsExecutable(),
            newJMSConfig, process.getIsExecutable(), process.getId(), process.getProcessDefinitionId());
           }
    });
    bulkSaveProcessResponseModel.setFailure(failure);
    return bulkSaveProcessResponseModel;
  }

  private IJMSConfigModel getJmsConfigModel(IProcessEventModel processEventModel)
  {
    IJMSConfigModel jmsConfigModel = new JMSConfigModel();
    if(WorkflowType.JMS_WORKFLOW.name().equals(processEventModel.getWorkflowType())) {
      jmsConfigModel.setIp(processEventModel.getIp());
      jmsConfigModel.setPort(processEventModel.getPort());
      jmsConfigModel.setQueueName(processEventModel.getQueue());
    }
    return jmsConfigModel;
  }
  private Map<String, IProcessEventModel> getOldProcessEvents(IListModel<ISaveProcessEventModel> updatedSaveProcessModel)
      throws Exception
  {
    List<String> ids = updatedSaveProcessModel.getList().stream()
        .map(ISaveProcessEventModel::getId).collect(Collectors.toList());
    IConfigGetAllRequestModel model = new ConfigGetAllRequestModel();
    model.setIds(ids);
    IGetAllProcessEventsResponseModel oldProcessEvents = getAllProcessEventsStrategy.execute(model);
    Map<String, IProcessEventModel> oldProcessEventsMap = new HashMap<String, IProcessEventModel>();
    oldProcessEvents.getList().forEach(process ->{
      oldProcessEventsMap.put(process.getId(), process);
    });
    return oldProcessEventsMap;
  }

  /**
   * @param saveProcessesModel
   * @param updatedSaveProcessModel
   * @param failure
   * @throws Exception
   */
  private void updateIsExecutable(IListModel<ISaveProcessEventModel> saveProcessesModel,
      IListModel<ISaveProcessEventModel> updatedSaveProcessModel, IExceptionModel failure) throws Exception
  {
    HashMap<String, ISaveProcessEventModel> processIdVsProcessEventMap = new HashMap<>();
    List<ISaveProcessEventModel> updatedSaveProcessModelList = new ArrayList<>();
    for (ISaveProcessEventModel saveProcessEventModel : saveProcessesModel.getList()) {
      if (saveProcessEventModel.getIsXMLModified()) {
        processIdVsProcessEventMap.put(saveProcessEventModel.getId(), saveProcessEventModel);
      }
      else {
        updatedSaveProcessModelList.add(saveProcessEventModel);
      }
    }
    if (!processIdVsProcessEventMap.isEmpty()) {
      IIdsListParameterModel idsList = new IdsListParameterModel();
      idsList.setIds(new ArrayList<String>(processIdVsProcessEventMap.keySet()));
      IGetCamundaProcessDefinitionResponseModel responseModel = getProcessDefinitionByIdStrategy.execute(idsList);
      IListModel<ISaveProcessEventModel> modifedSaveProcessesModel = new ListModel<ISaveProcessEventModel>();
      List<ISaveProcessEventModel> bulkUploadProcessDefinitionList = new ArrayList<>();
      for (Map.Entry<String, Object> processEntry : responseModel.getProcessDefinition().entrySet()) {
        ISaveProcessEventModel saveProcessEventModel = processIdVsProcessEventMap.get(processEntry.getKey());
        Map<String, String> idToProcessDefinitionMap = (Map<String, String>) processEntry.getValue();
        saveProcessEventModel.setProcessDefinitionId(idToProcessDefinitionMap.get(IProcessEvent.PROCESS_DEFINITION_ID));
        saveProcessEventModel.setProcessDefinition(idToProcessDefinitionMap.get(IProcessEvent.PROCESS_DEFINITION));
        setValidationInfo(saveProcessEventModel);
        bulkUploadProcessDefinitionList.add(saveProcessEventModel);
      }
      modifedSaveProcessesModel.setList(bulkUploadProcessDefinitionList);
      IBulkSaveProcessEventResponseModel uploadBulkResponse = bulkUploadProcessesToServerStrategy.execute(modifedSaveProcessesModel);
      if (uploadBulkResponse.getSuccess() != null) {
        for (IProcessEventModel uploadProcessDefinitionModel : uploadBulkResponse.getSuccess().getProcessEventsList()) {
          updatedSaveProcessModelList.add((ISaveProcessEventModel) uploadProcessDefinitionModel);
        }
      }
      if (uploadBulkResponse.getFailure() != null) {
        for (String id : uploadBulkResponse.getFailure().keySet()) {
          processIdVsProcessEventMap.remove(id);
          ExceptionUtil.addFailureDetailsToFailureObject(failure, uploadBulkResponse.getFailure().get(id), id, null);
        }
      }
    }
    updatedSaveProcessModel.setList(updatedSaveProcessModelList);
  }

  private void setValidationInfo(ISaveProcessEventModel saveProcessEventModel)
  {

    if (saveProcessEventModel.getIsExecutable()) {
      try {
        saveProcessEventModel.setValidationInfo(getValidationInfo(saveProcessEventModel));
      }
      catch (Exception e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
    }
  }

  IValidationInformationModel getValidationInfo(ISaveProcessEventModel model) throws Exception
  {
    Map<String, Map<String, Object>> taskVariablesMap = camundaTask.readTaskVariablesMap(model.getProcessDefinition(),
        model.getLabel());
    Map<String, List<String>> invalidInputList = new HashMap<>();
    IValidationInformationModel validationInformationModel = new ValidationInformationModel(true, invalidInputList);
    if (taskVariablesMap.isEmpty()) {
      validationInformationModel.setIsWorkflowValid(false);
      return validationInformationModel;
    }
    for (Map.Entry<String, Map<String, Object>> taskVariablesEntry : taskVariablesMap.entrySet()) {
      String uiTaskId = taskVariablesEntry.getKey();
      Map<String, Object> taskVariables = taskVariablesEntry.getValue();
      IAbstractTask task = tasksFactory.getTask((String) taskVariables.get(WorkflowConstants.TASK_ID));
      if (task == null || task.getTaskType() == TaskType.BGP_TASK) {
        continue;
      }
      List<String> invalidInputs = task.validate(taskVariables);
      if (invalidInputs != null && !invalidInputs.isEmpty()) {
        validationInformationModel.setIsWorkflowValid(false);
        invalidInputList.put(uiTaskId, invalidInputs);
      }
    }
    validationInformationModel.setValidationDetails(invalidInputList);
    return validationInformationModel;
  }

}