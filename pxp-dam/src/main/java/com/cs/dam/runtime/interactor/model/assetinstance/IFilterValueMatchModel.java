package com.cs.dam.runtime.interactor.model.assetinstance;

public interface IFilterValueMatchModel extends IFilterValueModel {
  
  public static final String VALUE = "value";
  
  public String getValue();
  
  public void setValue(String value);
}
