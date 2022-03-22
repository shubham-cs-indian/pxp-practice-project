package com.cs.di.config.mappings;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IBulkGetMappingService
    extends IGetConfigService<IIdParameterModel, IListModel<IMappingModel>> {
  
}
