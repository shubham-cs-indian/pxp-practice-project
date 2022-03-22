package com.cs.core.runtime.interactor.model.filter;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
    property = "baseType", visible = true)
public interface IFilterValueModel extends IModel {
  
  public static final String ID                     = "id";
  public static final String COMPARISON_OPERATOR    = "comparisonOperator";
  public static final String BASETYPE               = "baseType";
  public static final String TYPE                   = "type";
  public static final String ADVANCED_SEARCH_FILTER = "advancedSearchFilter";
  
  public String getId();
  
  public void setId(String id);
  
  public String getComparisonOperator();
  
  public void setComparisonOperator(String comparisonOperator);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public String getType();
  
  public void setType(String type);
  
  public boolean getAdvancedSearchFilter();
  
  public void setAdvancedSearchFilter(boolean advancedSearchFilter);
}
