package com.cs.core.config.businessapi.attribute;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.attribute.IGetGridAttributesResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

public interface IGetGridAttributesService extends IGetConfigService<IConfigGetAllRequestModel, IGetGridAttributesResponseModel> {
  
}
