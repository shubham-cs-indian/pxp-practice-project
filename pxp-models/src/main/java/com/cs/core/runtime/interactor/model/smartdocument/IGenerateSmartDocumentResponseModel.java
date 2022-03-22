package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;

public interface IGenerateSmartDocumentResponseModel extends IModel {
  
  public IGenerateSmartDocumentSuccessModel getSuccess();
  
  public void setSuccess(IGenerateSmartDocumentSuccessModel success);
  
  public IExceptionModel getFailure();
  
  public void setFailure(IExceptionModel failure);
}
