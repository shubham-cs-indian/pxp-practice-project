package com.cs.core.runtime.interactor.model.filter;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IPropertyInstanceFilterModel<T extends IFilterValueModel> extends IModel {
  
  public static final String ID                     = "id";
  public static final String TYPE                   = "type";
  public static final String MANDATORY              = "mandatory";
  public static final String SHOULD                 = "should";
  public static final String ADVANCED_SEARCH_FILTER = "advancedSearchFilter";
  
  public String getId();
  
  public void setId(String id);
  
  public String getType();
  
  public void setType(String type);
  
  public List<T> getMandatory();
  
  public void setMandatory(List<T> mandatory);
  
  public List<T> getShould();
  
  public void setShould(List<T> should);
  
  public boolean getAdvancedSearchFilter();
  
  public void setAdvancedSearchFilter(boolean advancedSearchFilter);
}
