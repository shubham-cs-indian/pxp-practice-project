package com.cs.core.config.interactor.model.datarule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetAllowedMasterTagsForHierarchyTaxonomyRequestModel extends IModel {
  
  public static final String SEARCH_TEXT = "searchText";
  public static final String SORT_ORDER  = "sortOrder";
  public static final String FROM        = "from";
  public static final String SIZE        = "size";
  public static final String ID          = "id";
  public static final String SORT_BY     = "sortBy";
  
  String getSearchColumn();
  
  void setSearchColumn(String searchColumn);
  
  String getSortOrder();
  
  void setSortOrder(String sortOrder);
  
  String getSortBy();
  
  void setSortBy(String sortBy);
  
  String getSearchText();
  
  void setSearchText(String searchText);
  
  String getId();
  
  void setId(String id);
  
  Long getFrom();
  
  void setFrom(Long from);
  
  Long getSize();
  
  void setSize(Long size);
}
