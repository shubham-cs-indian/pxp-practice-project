package com.cs.core.runtime.interactor.usecase.taskinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.taskinstance.IGetTaskCountByTypeService;

@Service
public class GetTaskCountByType extends AbstractRuntimeInteractor<IIdParameterModel, IIdParameterModel> implements IGetTaskCountByType {
  
  @Autowired
  protected IGetTaskCountByTypeService getTaskCountByTypeService;
  
  @Override
  public IIdParameterModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getTaskCountByTypeService.execute(dataModel);
  }
}
