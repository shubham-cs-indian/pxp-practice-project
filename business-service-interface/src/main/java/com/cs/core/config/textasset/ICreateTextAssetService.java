package com.cs.core.config.textasset;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;

public interface ICreateTextAssetService
    extends ICreateConfigService<ITextAssetModel, IGetKlassEntityWithoutKPModel> {
  
}
