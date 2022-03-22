package com.cs.di.config.interactor.mappings;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.mapping.IGetAllMappingsResponseModel;

public interface IGetAllMappings
    extends IGetConfigInteractor<IConfigGetAllRequestModel, IGetAllMappingsResponseModel> {
  
}
