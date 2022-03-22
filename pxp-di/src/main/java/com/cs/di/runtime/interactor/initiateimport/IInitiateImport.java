package com.cs.di.runtime.interactor.initiateimport;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.di.runtime.model.initiateimport.IImportDataModel;

public interface IInitiateImport extends ICreateConfigInteractor<IImportDataModel, IVoidModel> {
  
  public static final String PRODUCT            = "product";
  
}
