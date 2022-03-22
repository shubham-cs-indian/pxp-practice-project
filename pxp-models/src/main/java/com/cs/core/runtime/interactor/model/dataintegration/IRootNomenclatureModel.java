package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IRootNomenclatureModel extends IModel {
  
  public static final String ID   = "id";
  public static final String NAME = "name";
  public static final String PATH = "path";
  
  public String getId();
  
  public void setId(String id);
  
  public String getName();
  
  public void setName(String name);
  
  public List<INomenclatureElementModel> getPath();
  
  public void setPath(List<INomenclatureElementModel> path);
}
