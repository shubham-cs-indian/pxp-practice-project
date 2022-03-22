package com.cs.core.config.interactor.usecase.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.asset.IGetAssetParentIdService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAssetParentId extends AbstractGetConfigInteractor<IIdParameterModel, IIdParameterModel>
    implements IGetAssetParentId {
  
  @Autowired
  protected IGetAssetParentIdService getAssetParentIdService;
  
  @Override
  protected IIdParameterModel executeInternal(IIdParameterModel model) throws Exception
  {
    return getAssetParentIdService.execute(model);
  }
}
