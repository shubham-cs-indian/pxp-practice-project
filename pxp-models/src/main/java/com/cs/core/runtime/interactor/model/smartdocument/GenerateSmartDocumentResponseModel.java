package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GenerateSmartDocumentResponseModel implements IGenerateSmartDocumentResponseModel {
  
  private static final long                    serialVersionUID = 1L;
  protected IGenerateSmartDocumentSuccessModel success;
  protected IExceptionModel                    failure;
  
  @Override
  public IExceptionModel getFailure()
  {
    return failure;
  }
  
  @Override
  public void setFailure(IExceptionModel failure)
  {
    this.failure = failure;
  }
  
  @Override
  public IGenerateSmartDocumentSuccessModel getSuccess()
  {
    return success;
  }
  
  @Override
  @JsonDeserialize(as = GenerateSmartDocumentSuccessModel.class)
  public void setSuccess(IGenerateSmartDocumentSuccessModel success)
  {
    this.success = success;
  }
}
