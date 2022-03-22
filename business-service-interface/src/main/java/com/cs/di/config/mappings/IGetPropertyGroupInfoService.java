package com.cs.di.config.mappings;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.mapping.IGetPropertyGroupInfoRequestModel;
import com.cs.core.config.interactor.model.mapping.IGetPropertyGroupInfoResponseModel;

public interface IGetPropertyGroupInfoService extends
    IGetConfigService<IGetPropertyGroupInfoRequestModel, IGetPropertyGroupInfoResponseModel> {
  
}
