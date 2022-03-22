package com.cs.core.runtime.interactor.usecase.workflow.processinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.process.IBGPProcessModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Component
public class GetBGPProcessInstance extends AbstractRuntimeInteractor<IIdParameterModel, IBGPProcessModel> implements IGetBGPProcessInstance {
  
  @Autowired
  protected IGetBGPProcessInstanceService getBGPProcessInstanceService;
  
  @Override
  protected IBGPProcessModel executeInternal(IIdParameterModel model) throws Exception
  {
    return getBGPProcessInstanceService.execute(model);
  }
}
