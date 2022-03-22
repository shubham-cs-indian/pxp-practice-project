package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IDeleteKlassInstanceModel extends IModel {
  
  public static final String BASE_TYPE = "baseType";
  public static final String IDS       = "ids";
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public List<String> getIds();
  
  public void setIds(List<String> ids);
}
