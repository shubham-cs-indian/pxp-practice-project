package com.cs.core.config.interactor.model.klass;

public interface IValueIdAttributeModel extends IValueIdModel {
  
  public static final String VALUE = "value";
  
  public String getValue();
  
  public void setValue(String value);
}
