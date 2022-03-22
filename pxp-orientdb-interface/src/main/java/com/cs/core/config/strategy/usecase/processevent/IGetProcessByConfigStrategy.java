package com.cs.core.config.strategy.usecase.processevent;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.di.workflow.trigger.IWorkflowParameterModel;
import com.cs.di.workflow.trigger.IWorkflowTriggerModel;

public interface IGetProcessByConfigStrategy
    extends IConfigStrategy<IWorkflowTriggerModel, IListModel<IWorkflowParameterModel>> {
  
}
