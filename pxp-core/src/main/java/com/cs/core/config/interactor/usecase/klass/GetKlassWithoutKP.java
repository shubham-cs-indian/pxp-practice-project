package com.cs.core.config.interactor.usecase.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.klass.IGetKlassWithoutKPService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetKlassWithoutKP
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetKlassEntityWithoutKPModel>
    implements IGetKlassWithoutKP {
  
  @Autowired
  protected IGetKlassWithoutKPService getKlassWithoutKPService;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeInternal(IIdParameterModel klassModel) throws Exception
  {
    return getKlassWithoutKPService.execute(klassModel);
  }
}
