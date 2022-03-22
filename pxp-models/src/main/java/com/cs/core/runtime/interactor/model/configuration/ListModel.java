package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Collection;

public class ListModel<T> extends AbstractAdditionalPropertiesModel implements IListModel<T> {
  
  private static final long         serialVersionUID = 1L;
  
  protected Collection<? extends T> list;
  
  public ListModel()
  {
    // TODO Auto-generated constructor stub
  }
  
  public ListModel(Collection<? extends T> list)
  {
    this.list = list;
  }
  
  @JsonValue
  @Override
  public Collection<? extends T> getList()
  {
    return this.list;
  }
  
  @Override
  public void setList(Collection<? extends T> list)
  {
    this.list = list;
  }
}
