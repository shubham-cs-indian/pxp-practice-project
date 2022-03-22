package com.cs.core.runtime.interactor.model.filter;

public class FilterValueMatchModel extends AbstractFilterValueModel
    implements IFilterValueMatchModel {
  
  protected String value;
  
  @Override
  public String getValue()
  {
    return this.value;
  }
  
  @Override
  public void setValue(String value)
  {
    this.value = value;
  }
}
