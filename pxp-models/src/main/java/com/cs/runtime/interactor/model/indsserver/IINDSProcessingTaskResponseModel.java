package com.cs.runtime.interactor.model.indsserver;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IINDSProcessingTaskResponseModel extends IModel {
  
  public final static String SCRIPT_RESPONSE_MODEL = "scriptResponseModel";
  
  public IScriptResponseModel getScriptResponseModel();
  public void setScriptResponseModel(IScriptResponseModel scriptResponseModel);
  
}