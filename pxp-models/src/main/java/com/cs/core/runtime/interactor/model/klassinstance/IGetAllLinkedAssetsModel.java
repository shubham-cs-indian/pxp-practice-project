package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetAllLinkedAssetsModel extends IModel {
  
  public static final String ID         = "id";
  public static final String CONFIG_IDS = "configIds";
  
  public String getId();
  
  public void setId(String id);
  
  public List<String> getConfigIds();
  
  public void setConfigIds(List<String> configIds);
}
