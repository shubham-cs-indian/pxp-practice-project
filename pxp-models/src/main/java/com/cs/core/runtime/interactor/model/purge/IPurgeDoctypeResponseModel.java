package com.cs.core.runtime.interactor.model.purge;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IPurgeDoctypeResponseModel extends IModel {
  
  public static final String DOC_TYPE = "docType";
  
  public Map<String, IPurgeResponseModel> getDocType();
  
  public void setDocType(Map<String, IPurgeResponseModel> docType);
}
