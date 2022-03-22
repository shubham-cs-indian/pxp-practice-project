package com.cs.core.config.interactor.usecase.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.target.IGetTargetWithoutKPService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetTargetWithoutKP
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetKlassEntityWithoutKPModel>
    implements IGetTargetWithoutKP {
  
  @Autowired
  protected IGetTargetWithoutKPService getTargetWithoutKPService;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getTargetWithoutKPService.execute(idModel);
  }
}
