package com.cs.core.runtime.interactor.model.instancetree;

import java.util.ArrayList;
import java.util.List;

public class SpecialUsecaseFiltersModel implements ISpecialUsecaseFiltersModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          type;
  protected List<String>    appliedValues;
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setAppliedValues(List<String> appliedValues)
  {
    this.appliedValues = appliedValues;
  }
  
  @Override
  public List<String> getAppliedValues()
  {
    if (appliedValues == null) {
      appliedValues = new ArrayList<>();
    }
    return appliedValues;
  }
}