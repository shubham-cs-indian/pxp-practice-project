package com.cs.core.config.interactor.model.configdetails;

import java.util.Collection;

// TODO :: shud be in base pkg..

import com.cs.core.runtime.interactor.model.configuration.IAdditionalPropertiesModel;

public interface IListModel<T> extends IAdditionalPropertiesModel {
  
  public static final String LIST = "list";
  
  public Collection<? extends T> getList();
  
  public void setList(Collection<? extends T> list);
}
