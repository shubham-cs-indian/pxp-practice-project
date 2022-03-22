package com.cs.core.config.interactor.usecase.klass;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

public interface IGetKlassesListByNatureType
    extends IGetConfigInteractor<IConfigGetAllRequestModel, IGetConfigDataEntityResponseModel> {
  
}
