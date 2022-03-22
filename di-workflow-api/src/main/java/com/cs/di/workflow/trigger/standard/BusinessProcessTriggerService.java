package com.cs.di.workflow.trigger.standard;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.di.workflow.trigger.IWorkflowParameterModel;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel.Usecase;
import com.cs.workflow.base.EventType;

/**
 * It creates workflow configuration model to support model
 * for workflow triggering criteria
 * and will trigger the workflow
 */
@Service
public class BusinessProcessTriggerService extends WorkflowEventTriggerService<IBusinessProcessTriggerModel> implements IBusinessProcessTriggerService {

    @Override
    protected void addExecutionParameters(IBusinessProcessTriggerModel workflowEventModel, Map<String, Object> workflowParameters) {
        workflowParameters.put("klassInstanceId", workflowEventModel.getKlassInstanceId());
        workflowParameters.put("baseType", workflowEventModel.getBaseType());
        //workflowParameters.put("klassInstance", classInstance);
    }

    @Override
    public IListModel<IWorkflowParameterModel> getQualifyingWorkflows(IBusinessProcessTriggerModel workflowEventModel) throws Exception {
        workflowEventModel.setEventType(EventType.BUSINESS_PROCESS.name());
        if (workflowEventModel.getUsecase() == null) {
          workflowEventModel.setUsecase(Usecase.OTHERS);
        }
        return getProcessIds(workflowEventModel);
    }
}
