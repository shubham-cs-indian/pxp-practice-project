package com.cs.core.config.klass;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.klass.IGetKlassesByBaseTypeModel;

public interface IGetKlassesListByBaseTypeService
    extends IGetConfigService<IGetKlassesByBaseTypeModel, IGetConfigDataEntityResponseModel> {
  
}
