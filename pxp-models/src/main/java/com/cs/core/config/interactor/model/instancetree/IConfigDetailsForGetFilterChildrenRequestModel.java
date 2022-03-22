package com.cs.core.config.interactor.model.instancetree;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IConfigDetailsForGetFilterChildrenRequestModel extends IModel {
  
  public static final String USER_ID          = "userId";
  public static final String ALLOWED_ENTITIES = "allowedEntities";
  public static final String ID               = "id";
  public static final String FILTER_TYPE      = "filterType";
  public static final String KPI_ID           = "kpiId";
  public static final String SEARCH_TEXT      = "searchText";
  
  public String getUserId();
  public void setUserId(String userId);
  
  public List<String> getAllowedEntities();
  public void setAllowedEntities(List<String> allowedEntities);
  
  public String getId();
  public void setId(String id);
  
  public String getFilterType();
  public void setFilterType(String filterType);
  
  public String getKpiId();
  public void setKpiId(String kpiId);
  
  public String getSearchText();
  public void setSearchText(String searchText);
}
