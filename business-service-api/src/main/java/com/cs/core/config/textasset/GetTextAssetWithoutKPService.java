package com.cs.core.config.textasset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.strategy.usecase.textasset.IGetTextAssetWithoutKPStrategy;
import com.cs.core.config.textasset.IGetTextAssetWithoutKPService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetTextAssetWithoutKPService
    extends AbstractGetConfigService<IIdParameterModel, IGetKlassEntityWithoutKPModel>
    implements IGetTextAssetWithoutKPService {
  
  @Autowired
  protected IGetTextAssetWithoutKPStrategy getTextAssetWithoutKPStrategy;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getTextAssetWithoutKPStrategy.execute(idModel);
  }
}
