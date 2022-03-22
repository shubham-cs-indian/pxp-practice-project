package com.cs.core.config.interactor.usecase.iconlibrary;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.asset.IGetAllIconsRequestModel;
import com.cs.core.config.interactor.model.asset.IGetAllIconsResponseModel;

public interface IGetAllIcons extends IGetConfigInteractor<IGetAllIconsRequestModel, IGetAllIconsResponseModel> {
  
}
