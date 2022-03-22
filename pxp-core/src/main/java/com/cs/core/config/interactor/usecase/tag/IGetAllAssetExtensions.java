package com.cs.core.config.interactor.usecase.tag;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.asset.IGetAssetExtensionsModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetAllAssetExtensions extends ICreateConfigInteractor<IIdsListParameterModel, IGetAssetExtensionsModel>{
  
}
