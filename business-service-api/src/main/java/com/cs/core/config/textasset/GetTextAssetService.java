package com.cs.core.config.textasset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.config.strategy.usecase.textasset.IGetTextAssetStrategy;
import com.cs.core.config.textasset.IGetTextAssetService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetTextAssetService extends AbstractGetConfigService<IIdParameterModel, ITextAssetModel>
    implements IGetTextAssetService {
  
  @Autowired
  IGetTextAssetStrategy getTextAssetStrategy;
  
  @Override
  public ITextAssetModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getTextAssetStrategy.execute(idModel);
  }
}
