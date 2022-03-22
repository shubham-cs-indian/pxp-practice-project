package com.cs.workflow.get;

import com.cs.core.config.interactor.model.articleimportcomponent.IProcessInstanceModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;
import com.cs.di.runtime.model.processinstance.IGetProcessInstanceModel;

public interface IGetWorkflowInstancesService extends
IRuntimeStrategy<IGetProcessInstanceModel, IListModel<IProcessInstanceModel>> {

}