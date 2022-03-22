package com.cs.core.config.interactor.usecase.textasset;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.config.strategy.usecase.textasset.IGetTextAssetStrategy;
import com.cs.core.config.textasset.IGetTextAssetService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetTextAsset extends AbstractGetConfigInteractor<IIdParameterModel, ITextAssetModel>
    implements IGetTextAsset {
  
  @Autowired
  IGetTextAssetService getTextAssetService;
  
  @Override
  public ITextAssetModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getTextAssetService.execute(idModel);
  }
}
