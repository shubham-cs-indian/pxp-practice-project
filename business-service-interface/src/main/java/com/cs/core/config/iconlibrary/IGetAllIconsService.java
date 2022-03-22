package com.cs.core.config.iconlibrary;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.asset.IGetAllIconsRequestModel;
import com.cs.core.config.interactor.model.asset.IGetAllIconsResponseModel;

public interface IGetAllIconsService extends IGetConfigService<IGetAllIconsRequestModel, IGetAllIconsResponseModel> {
  
}
