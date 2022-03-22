package com.cs.di.workflow.trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.ProcessEngineException;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.processevent.IGetProcessByConfigStrategy;
import com.cs.core.config.strategy.usecase.task.IGetProcessDefinitionByProcessDefinitionIdStrategy;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.exception.camunda.WorkflowEngineException;
import com.cs.core.runtime.interactor.exception.process.WorkflowNotFoundException;
import com.cs.core.runtime.interactor.model.camunda.IGetCamundaProcessDefinitionResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.di.workflow.WorkflowService;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel;
import com.cs.workflow.camunda.IBPMNEngineService;

public abstract class WorkflowTriggerService<T extends IWorkflowTriggerModel> extends WorkflowService implements IWorkflowTriggerService<T> {

    @Autowired
    protected IBPMNEngineService bpmnService;

    @Autowired
    protected ISessionContext context;

    @Autowired
    protected TransactionThreadData transactionThread;

    @Autowired
    IGetProcessByConfigStrategy getProcessByConfigStrategy;

    @Autowired
    IGetProcessDefinitionByProcessDefinitionIdStrategy getProcessDefinitionByProcessDefinitionIdStrategy;

    @Override
    public void triggerQualifyingWorkflows(T workflowTriggerModel) throws Exception {
        triggerQualifyingWorkflows(workflowTriggerModel, null);
    }
    
    @Override
    public void triggerQualifyingWorkflows(T workflowTriggerModel, List<String> executedProcessIds) throws Exception {
      if(executedProcessIds == null) {
           executedProcessIds =  new ArrayList<String>();
      }
        fillTransactionData(workflowTriggerModel);
        IListModel<IWorkflowParameterModel> qualifyingWorkflows = getQualifyingWorkflows(workflowTriggerModel);
        Map<String, Object> workflowParameters = prepareWorkflowParameters(workflowTriggerModel);
        executeQualifyingWorkflows(qualifyingWorkflows, workflowParameters, executedProcessIds);
    }

   
    
    protected void fillTransactionData(IWorkflowTriggerModel workflowTriggerModel) {
        ITransactionData transactionData = transactionThread.getTransactionData();
        workflowTriggerModel.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
        workflowTriggerModel.setOrganizationId(transactionData.getOrganizationId());
        if(workflowTriggerModel.getEndpointId() == null || workflowTriggerModel.getEndpointId().isEmpty()) {
          workflowTriggerModel.setEndpointId(transactionData.getEndpointId());
        }
    }

    private Map<String, Object> prepareWorkflowParameters(T workflowTriggerModel) {
    Map<String, Object> workflowParameters = new HashMap<>();
    if (workflowTriggerModel instanceof IBusinessProcessTriggerModel
        && (((IBusinessProcessTriggerModel) workflowTriggerModel).getTransactionData() != null
            && ((IBusinessProcessTriggerModel) workflowTriggerModel).getUserSession() != null)) {
      workflowParameters.put(CommonConstants.TRANSACTION_DATA, ((IBusinessProcessTriggerModel) workflowTriggerModel).getTransactionData());
      workflowParameters.put(ISessionContext.USER_SESSION_DTO, ((IBusinessProcessTriggerModel) workflowTriggerModel).getUserSession());
    }
    else {
      workflowParameters.put(CommonConstants.TRANSACTION_DATA, transactionThread.getTransactionData());
      workflowParameters.put(ISessionContext.USER_SESSION_DTO, context.getUserSessionDTO());
    }
        addExecutionParameters(workflowTriggerModel, workflowParameters);
        return workflowParameters;
    }

    private List<String> executeQualifyingWorkflows(IListModel<IWorkflowParameterModel> workflowsToExecute, Map<String, Object> workflowParameters, List<String> executedProcessIds)
        throws WorkflowEngineException
{
    try {
        if (workflowsToExecute.getList().isEmpty()) {
            throw new WorkflowNotFoundException("Workflow not found for specified trigger criteria");
        }
        for (IWorkflowParameterModel processMap : workflowsToExecute.getList()) {
          String processDefId =  (String) processMap.getWorkflowParameterMap().get(IProcessEvent.PROCESS_DEFINITION_ID);
          if(!executedProcessIds.contains(processDefId)) {
              workflowParameters.put(IWorkflowParameterModel.WORKFLOW_PARAMETER_MAP, processMap.getWorkflowParameterMap());
              bpmnService.startProcessInstanceById(processDefId,
                      workflowParameters);
              executedProcessIds.add(processDefId);
          }
        }
    }
    catch (WorkflowNotFoundException e) {
        RDBMSLogger.instance().debug(e.getMessage());
    }
    catch (ProcessEngineException e) {
        throw new WorkflowEngineException(e);
    }
    return executedProcessIds;
}

    protected IListModel<IWorkflowParameterModel> getProcessIds(IWorkflowTriggerModel filterModel) throws Exception {
        return getProcessByConfigStrategy.execute(filterModel);
    }

    protected IGetCamundaProcessDefinitionResponseModel getProcessDefinitions(List<String> workflowIds) throws Exception {
        IIdsListParameterModel model = new IdsListParameterModel();
        model.setIds(workflowIds);
        return getProcessDefinitionByProcessDefinitionIdStrategy.execute(model);
    }

    protected abstract IListModel<IWorkflowParameterModel> getQualifyingWorkflows(T workflowTriggerModel) throws Exception;
    protected abstract void addExecutionParameters(T workflowTriggerModel, Map<String, Object> workflowParameters);
}