package com.cs.core.config.interactor.usecase.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.target.IGetTargetService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetTarget extends AbstractGetConfigInteractor<IIdParameterModel, ITargetModel>
    implements IGetTarget {
  
  @Autowired
  IGetTargetService getTargetService;
  
  @Override
  public ITargetModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getTargetService.execute(idModel);
  }
}
