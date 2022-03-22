package com.cs.core.config.interactor.usecase.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.asset.ICreateAssetService;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;

@Service
public class CreateAsset extends AbstractCreateConfigInteractor<IAssetModel, IGetKlassEntityWithoutKPModel>
    implements ICreateAsset {
  
  @Autowired
  protected ICreateAssetService createAssetService;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeInternal(IAssetModel model) throws Exception
  {
    return createAssetService.execute(model);
  }
}
