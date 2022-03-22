package com.cs.dam.config.interactor.model.downloadtracker;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetLabelsByIdsRequestModel extends IModel {
  
  public static final String IDS         = "ids";
  public static final String ENTITY_TYPE = "entityType";
  public static final String SEARCH_TEXT = "searchText";
  public static final String FROM        = "from";
  public static final String SIZE        = "size";
  public static final String SORT_ORDER  = "sortOrder";
  
  public List<String> getIds();
  public void setIds(List<String> ids);

  public String getEntityType();
  public void setEntityType(String entityType);

  public String getSearchText();
  public void setSearchText(String searchText);
  
  public Long getFrom();
  public void setFrom(Long from);

  public Long getSize();
  public void setSize(Long size);

  public String getSortOrder();
  public void setSortOrder(String sortOrder);
}
