package com.cs.di.runtime.interactor.initiateexport;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.di.runtime.model.initiateexport.IExportDataModel;

public interface IInitiateExport  extends IGetConfigInteractor<IExportDataModel, IModel>{
  
}
