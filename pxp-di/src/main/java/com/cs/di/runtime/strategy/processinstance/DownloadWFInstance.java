package com.cs.di.runtime.strategy.processinstance;

import com.cs.core.config.interactor.model.articleimportcomponent.IResponseModelForProcessInstance;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.workflow.get.IDownloadWFInstanceService;
import com.cs.di.runtime.model.processinstance.IGetProcessInstanceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  Get the workflow instances with their respective tasks on the basis of filter criteria.
 * Filter parameters : Workflow definition id, User Id, Start time, End time and message type.
 * 
 * @author mangesh.metkari
 *
 */
@Service
public class DownloadWFInstance extends AbstractRuntimeInteractor<IGetProcessInstanceModel, IResponseModelForProcessInstance>
    implements IDownloadWFInstance {
  
  @Autowired
  protected IDownloadWFInstanceService downloadWFInstanceService;

  @Override
  public IResponseModelForProcessInstance executeInternal(IGetProcessInstanceModel model)
      throws Exception
  {
    return downloadWFInstanceService.execute(model);
  }

}
