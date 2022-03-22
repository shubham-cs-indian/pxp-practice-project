package com.cs.core.config.interactor.usecase.textasset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.textasset.IGetTextAssetWithoutKPService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetTextAssetWithoutKP
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetKlassEntityWithoutKPModel>
    implements IGetTextAssetWithoutKP {
  
  @Autowired
  protected IGetTextAssetWithoutKPService getTextAssetWithoutKPService;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getTextAssetWithoutKPService.execute(idModel);
  }
}
