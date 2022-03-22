package com.cs.core.runtime.interactor.model.configuration;

import java.util.List;

public interface IIdsListParameterModel extends IAdditionalPropertiesModel {
  
  public static String IDS = "ids";
  
  public List<String> getIds();
  
  public void setIds(List<String> ids);
}
