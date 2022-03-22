package com.cs.di.config.strategy.usecase.processevent;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.processevent.IGetProcessByConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.di.workflow.trigger.IWorkflowParameterModel;
import com.cs.di.workflow.trigger.IWorkflowTriggerModel;
import com.cs.di.workflow.trigger.WorkflowParameterModel;
import com.fasterxml.jackson.core.type.TypeReference;

@Component("getProcessByConfigStrategy")
public class GetProcessByConfigStrategy extends OrientDBBaseStrategy
    implements IGetProcessByConfigStrategy {
  
  @Override
  public IListModel<IWorkflowParameterModel> execute(IWorkflowTriggerModel model) throws Exception
  {
    return execute(GET_PROCESS_BY_CONFIG, model,  new TypeReference<ListModel<WorkflowParameterModel>>()
    {
      
    });
  }

}
