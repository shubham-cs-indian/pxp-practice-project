package com.cs.core.config.interactor.model.grid;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetKlassesListRequestModel extends IModel {
  
  public static final String FROM            = "from";
  public static final String SIZE            = "size";
  public static final String SORT_BY         = "sortBy";
  public static final String SEARCH_TEXT     = "searchText";
  public static final String SORT_ORDER      = "sortOrder";
  public static final String SEARCH_COLUMN   = "searchColumn";
  public static final String BASETYPE        = "baseType";
  public static final String TYPES_TO_FILTER = "typesToFilter";
  
  public Long getFrom();
  
  public void setFrom(Long from);
  
  public Long getSize();
  
  public void setSize(Long size);
  
  public String getSortBy();
  
  public void setSortBy(String sortBy);
  
  public String getSearchText();
  
  public void setSearchText(String searchText);
  
  public String getSortOrder();
  
  public void setSortOrder(String sortOrder);
  
  public String getSearchColumn();
  
  public void setSearchColumn(String searchColumn);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public Map<String, List<String>> getTypesToFilter();
  
  public void setTypesToFilter(Map<String, List<String>> typesToFilter);
}
