package com.cs.core.config.interactor.usecase.klass;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.klass.ICreateKlassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateKlass extends AbstractCreateConfigInteractor<IKlassModel, IGetKlassEntityWithoutKPModel>
    implements ICreateKlass {
  
  @Autowired
  protected ICreateKlassService createKlassService;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeInternal(IKlassModel klassModel) throws Exception
  {

    return createKlassService.execute(klassModel);
  }
}
