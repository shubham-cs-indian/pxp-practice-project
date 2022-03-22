package com.cs.core.config.klass;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

public interface IGetKlassesListByNatureTypeService
    extends IGetConfigService<IConfigGetAllRequestModel, IGetConfigDataEntityResponseModel> {
  
}
