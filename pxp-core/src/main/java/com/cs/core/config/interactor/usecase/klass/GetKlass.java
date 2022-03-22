package com.cs.core.config.interactor.usecase.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassModel;
import com.cs.core.config.klass.IGetKlassService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetKlass extends AbstractGetConfigInteractor<IIdParameterModel, IGetKlassModel>
    implements IGetKlass {
  
  @Autowired
  IGetKlassService      getKlassService;
  
  @Override
  public IGetKlassModel executeInternal(IIdParameterModel klassModel) throws Exception
  {
    return getKlassService.execute(klassModel);
  }
}
