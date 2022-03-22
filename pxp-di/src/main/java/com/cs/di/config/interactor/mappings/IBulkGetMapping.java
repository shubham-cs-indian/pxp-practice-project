package com.cs.di.config.interactor.mappings;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IBulkGetMapping
    extends IGetConfigInteractor<IIdParameterModel, IListModel<IMappingModel>> {
  
}
