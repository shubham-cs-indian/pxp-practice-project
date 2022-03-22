package com.cs.di.runtime.interactor.initiateexport;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.di.runtime.model.initiateexport.IExportDataModel;

public interface IInitiateExportService  extends IGetConfigService<IExportDataModel, IModel>{
  
}
