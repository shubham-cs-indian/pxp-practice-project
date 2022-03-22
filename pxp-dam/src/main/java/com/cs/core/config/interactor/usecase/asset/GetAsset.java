package com.cs.core.config.interactor.usecase.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.asset.IGetAssetService;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAsset extends AbstractGetConfigInteractor<IIdParameterModel, IAssetModel> implements IGetAsset {
  
  @Autowired
  IGetAssetService getAssetService;
  
  @Override
  public IAssetModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getAssetService.execute(idModel);
  }
}
