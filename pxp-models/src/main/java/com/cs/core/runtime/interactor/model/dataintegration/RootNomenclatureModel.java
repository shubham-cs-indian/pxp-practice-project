package com.cs.core.runtime.interactor.model.dataintegration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class RootNomenclatureModel implements IRootNomenclatureModel {
  
  private static final long              serialVersionUID = 1L;
  
  public String                          id, name;
  public List<INomenclatureElementModel> path;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getName()
  {
    return name;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  @Override
  public List<INomenclatureElementModel> getPath()
  {
    return path;
  }
  
  @JsonDeserialize(contentAs = NomenclatureElementModel.class)
  @Override
  public void setPath(List<INomenclatureElementModel> path)
  {
    this.path = path;
  }
}
