
package com.cs.di.runtime.strategy.processinstance;

import com.cs.core.config.interactor.model.articleimportcomponent.IProcessInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.workflow.get.IGetTaskForWFInstancesService;
import com.cs.di.runtime.model.processinstance.IGetProcessInstanceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Get the workflow instance on the basis of filter parameter.
 * Filtet paramter : Workflow defination id, User Id, Start time, End time.
 * 
 * @author subham S
 *
 */
@Service
public class GetTaskForWFInstances extends AbstractRuntimeInteractor<IGetProcessInstanceModel, IProcessInstanceModel>
    implements IGetTaskForWFInstances {
  
  @Autowired
  protected IGetTaskForWFInstancesService getTaskForWFInstancesService;

  @Override
  public IProcessInstanceModel executeInternal(IGetProcessInstanceModel model) throws Exception
  {
    return getTaskForWFInstancesService.execute(model);
  }
}