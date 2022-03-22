package com.cs.core.config.interactor.model.tag;

import org.apache.poi.ss.formula.functions.T;

import java.util.Collection;

public class GetTagTagValuesRequestModel implements IGetTagTagValuesRequestModel {
  
  private static final long         serialVersionUID = 1L;
  protected Collection<? extends T> list;
  
  @Override
  public Collection getList()
  {
    return list;
  }
  
  @Override
  public void setList(Collection list)
  {
    this.list = list;
  }
}
