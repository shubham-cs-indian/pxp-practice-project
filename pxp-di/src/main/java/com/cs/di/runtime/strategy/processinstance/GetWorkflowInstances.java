package com.cs.di.runtime.strategy.processinstance;

import com.cs.core.config.interactor.model.articleimportcomponent.IProcessInstanceModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.di.runtime.model.processinstance.IGetProcessInstanceModel;
import com.cs.workflow.get.IGetWorkflowInstancesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Get the workflow instance on the basis of filter parameter.
 * Filtet paramter : Workflow defination id, User Id, Start time, End time.
 * 
 * @author sopan.talekar
 *
 */
@Component
public class GetWorkflowInstances
    extends AbstractRuntimeInteractor<IGetProcessInstanceModel, IListModel<IProcessInstanceModel>>
    implements IGetWorkflowInstances {

  @Autowired protected IGetWorkflowInstancesService getWorkflowInstancesStrategy;

  @Override protected IListModel<IProcessInstanceModel> executeInternal(IGetProcessInstanceModel model) throws Exception
  {
    return getWorkflowInstancesStrategy.execute(model);
  }
}
