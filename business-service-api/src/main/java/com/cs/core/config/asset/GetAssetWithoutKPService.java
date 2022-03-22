package com.cs.core.config.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.strategy.usecase.asset.IGetAssetWithoutKPStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAssetWithoutKPService
    extends AbstractGetConfigService<IIdParameterModel, IGetKlassEntityWithoutKPModel>
    implements IGetAssetWithoutKPService {
  
  @Autowired
  protected IGetAssetWithoutKPStrategy getAssetWithoutKPStrategy;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getAssetWithoutKPStrategy.execute(idModel);
  }
}
