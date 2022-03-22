package com.cs.core.config.interactor.usecase.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.asset.IGetAssetWithoutKPService;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAssetWithoutKP
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetKlassEntityWithoutKPModel>
    implements IGetAssetWithoutKP {
  
  @Autowired
  protected IGetAssetWithoutKPService getAssetWithoutKPService;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getAssetWithoutKPService.execute(idModel);
  }
}
