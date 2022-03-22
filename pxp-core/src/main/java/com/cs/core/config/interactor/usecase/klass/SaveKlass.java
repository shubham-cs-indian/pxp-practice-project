package com.cs.core.config.interactor.usecase.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.klass.ISaveKlassService;

@Service
public class SaveKlass extends AbstractSaveConfigInteractor<IKlassSaveModel, IGetKlassEntityWithoutKPModel>
    implements ISaveKlass {
  
  @Autowired
  protected ISaveKlassService                     saveKlassService;
  
  public IGetKlassEntityWithoutKPModel executeInternal(IKlassSaveModel klassModel) throws Exception
  {
    return saveKlassService.execute(klassModel);
  }
  
}
