package com.cs.core.config.interactor.usecase.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetMasterKlassModel;
import com.cs.core.config.klass.IGetMasterKlassService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetMasterKlass
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetMasterKlassModel>
    implements IGetMasterKlass {
  
  @Autowired
  IGetMasterKlassService                    getMasterKlassService;
  
 
  
  @Override
  public IGetMasterKlassModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getMasterKlassService.execute(idModel);
  }
  
}
