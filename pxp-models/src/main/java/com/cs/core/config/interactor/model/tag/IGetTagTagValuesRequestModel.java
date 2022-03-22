package com.cs.core.config.interactor.model.tag;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Collection;

public interface IGetTagTagValuesRequestModel<T> extends IModel {
  
  public static final String LIST = "list";
  
  public Collection<? extends T> getList();
  
  public void setList(Collection<? extends T> list);
}
