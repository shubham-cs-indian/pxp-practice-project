package com.cs.core.config.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.strategy.usecase.asset.IGetAssetParentIdStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAssetParentIdService extends AbstractGetConfigService<IIdParameterModel, IIdParameterModel>
    implements IGetAssetParentIdService {
  
  @Autowired
  protected IGetAssetParentIdStrategy getAssetParentIdStrategy;
  
  @Override
  protected IIdParameterModel executeInternal(IIdParameterModel model) throws Exception
  {
    return getAssetParentIdStrategy.execute(model);
  }
}
