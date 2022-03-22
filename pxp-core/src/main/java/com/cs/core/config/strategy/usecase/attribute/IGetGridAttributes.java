package com.cs.core.config.strategy.usecase.attribute;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.attribute.IGetGridAttributesResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

public interface IGetGridAttributes
    extends IGetConfigInteractor<IConfigGetAllRequestModel, IGetGridAttributesResponseModel> {
  
}
