package com.cs.di.config.interactor.modeler;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.di.config.model.modeler.IGetTaskMetadataResponseModel;

public interface IGetTaskMetadata extends IGetConfigInteractor<IIdParameterModel, IGetTaskMetadataResponseModel> {
  
}
