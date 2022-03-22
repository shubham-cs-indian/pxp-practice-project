package com.cs.core.config.interactor.model.klass;

public class ValueIdAttributeModel extends ValueIdModel implements IValueIdAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          Value;
  
  @Override
  public String getValue()
  {
    return Value;
  }
  
  @Override
  public void setValue(String value)
  {
    Value = value;
  }
}
