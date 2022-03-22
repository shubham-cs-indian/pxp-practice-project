package com.cs.core.config.businessapi.tag;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.asset.IGetAssetExtensionsModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetAllAssetExtensionsService extends IGetConfigService<IIdsListParameterModel, IGetAssetExtensionsModel>{
  
}
