package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISpecialUsecaseFiltersModel extends IModel {
  
  public static final String ID             = "id";
  public static final String TYPE           = "type";
  public static final String APPLIED_VALUES = "appliedValues";
  
  public void setId(String id);
  public String getId();
  
  public void setType(String type);
  public String getType();
  
  public void setAppliedValues(List<String> appliedValues);
  public List<String> getAppliedValues();
  
}
