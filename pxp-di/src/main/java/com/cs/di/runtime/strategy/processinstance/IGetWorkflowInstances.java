package com.cs.di.runtime.strategy.processinstance;

import com.cs.core.config.interactor.model.articleimportcomponent.IProcessInstanceModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;
import com.cs.di.runtime.model.processinstance.IGetProcessInstanceModel;

public interface IGetWorkflowInstances extends
IRuntimeStrategy<IGetProcessInstanceModel, IListModel<IProcessInstanceModel>> {

}