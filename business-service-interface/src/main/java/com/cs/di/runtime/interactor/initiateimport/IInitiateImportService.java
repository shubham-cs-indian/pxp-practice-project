package com.cs.di.runtime.interactor.initiateimport;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.di.runtime.model.initiateimport.IImportDataModel;

public interface IInitiateImportService extends ICreateConfigService<IImportDataModel, IVoidModel> {
  
  public static final String PRODUCT            = "product";
  
}
