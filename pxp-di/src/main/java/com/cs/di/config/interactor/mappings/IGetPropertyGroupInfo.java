package com.cs.di.config.interactor.mappings;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.mapping.IGetPropertyGroupInfoRequestModel;
import com.cs.core.config.interactor.model.mapping.IGetPropertyGroupInfoResponseModel;

public interface IGetPropertyGroupInfo extends
    IGetConfigInteractor<IGetPropertyGroupInfoRequestModel, IGetPropertyGroupInfoResponseModel> {
  
}
