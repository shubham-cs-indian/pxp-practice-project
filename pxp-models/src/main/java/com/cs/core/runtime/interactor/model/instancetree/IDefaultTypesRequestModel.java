package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDefaultTypesRequestModel extends IModel {
  
  public static final String FROM           = "from";
  public static final String SIZE           = "size";
  public static final String SORT_BY        = "sortBy";
  public static final String USER_ID        = "userId";
  public static final String KLASS_IDS      = "klassIds";
  public static final String ENTITY_TYPE    = "entityType";
  public static final String MODULE_ID      = "moduleId";
  public static final String SEARCH_TEXT    = "searchText";
  public static final String SORT_ORDER     = "sortOrder";
  public static final String SEARCH_COLUMN  = "searchColumn";
  public static final String SELECTED_TYPES = "selectedTypes";
  
  public Long getFrom();
  public void setFrom(Long from);
  
  public Long getSize();
  public void setSize(Long size);
  
  public String getSortBy();
  public void setSortBy(String sortBy);
  
  public String getUserId();
  public void setUserId(String userId);
  
  public String getEntityType();
  public void setEntityType(String entityType);
  
  public List<String> getKlassIds();
  public void setKlassIds(List<String> klassIds);
  
  public String getSearchText();
  public void setSearchText(String searchText);
  
  public String getSortOrder();
  public void setSortOrder(String sortOrder);
  
  public String getSearchColumn();
  public void setSearchColumn(String searchColumn);
  
  public String getModuleId();
  public void setModuleId(String moduleId);
  
  public List<String> getSelectedTypes();
  public void setSelectedTypes(List<String> selectedTypes);
}
