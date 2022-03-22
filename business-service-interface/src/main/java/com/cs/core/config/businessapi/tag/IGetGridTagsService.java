package com.cs.core.config.businessapi.tag;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.tag.IGetTagGridResponseModel;

public interface IGetGridTagsService extends IGetConfigService<IConfigGetAllRequestModel, IGetTagGridResponseModel> {
  
}
