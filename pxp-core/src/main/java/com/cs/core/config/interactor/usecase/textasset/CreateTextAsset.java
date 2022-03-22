package com.cs.core.config.interactor.usecase.textasset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.config.textasset.ICreateTextAssetService;


@Service
public class CreateTextAsset extends AbstractCreateConfigInteractor<ITextAssetModel, IGetKlassEntityWithoutKPModel> implements ICreateTextAsset {
  
  @Autowired
  protected ICreateTextAssetService createTextAssetService;

  @Override
  public IGetKlassEntityWithoutKPModel executeInternal(ITextAssetModel klassModel)
      throws Exception
  {
    return createTextAssetService.execute(klassModel);
  }
}
