package com.cs.core.config.interactor.usecase.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.target.ISaveTargetService;

@Service
public class SaveTarget extends AbstractSaveConfigInteractor<IKlassSaveModel, IGetKlassEntityWithoutKPModel>
    implements ISaveTarget {
  
  @Autowired
  ISaveTargetService saveTargetService;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeInternal(IKlassSaveModel klassModel) throws Exception
  {
    return saveTargetService.execute(klassModel);
  }
  
}
