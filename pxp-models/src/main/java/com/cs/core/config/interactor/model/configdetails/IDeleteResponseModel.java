package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDeleteResponseModel extends IModel {
  
  public List<String> getIds();
  
  public void setIds(List<String> ids);
}
