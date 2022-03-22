package com.cs.core.runtime.interactor.model.typeswitch;

import java.util.ArrayList;
import java.util.List;

public class GetAllowedTypesForModulesModel implements IGetAllowedTypesForModulesModel {
  
  protected String       id;
  protected List<String> standardKlassIds;
  
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
  public List<String> getStandardKlassIds()
  {
    if (standardKlassIds == null) {
      standardKlassIds = new ArrayList<>();
    }
    return standardKlassIds;
  }
  
  @Override
  public void setStandardKlassIds(List<String> standardKlassIds)
  {
    this.standardKlassIds = standardKlassIds;
  }
}
