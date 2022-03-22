package com.cs.core.config.interactor.usecase.propertycollection;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.propertycollection.IGetPropertyCollectionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetPropertyCollection extends IGetConfigInteractor<IIdParameterModel, IGetPropertyCollectionModel> {
  
}
