package com.cs.core.runtime.interactor.model.configuration;

public interface IGetOffboardingEndpointsByUserRequestModel extends IModel {
  
  public static String ID                  = "id";
  public static String PHYSICAL_CATALOG_ID = "physicalCatalogId";
  public static String FROM                = "from";
  public static String SIZE                = "size";
  public static String SEARCH_TEXT         = "searchText";
  public static String SEARCH_COLUMN       = "searchColumn";
  public static String SORT_BY             = "sortBy";
  public static String SORT_ORDER          = "sortOrder";
  
  public String getId();
  
  public void setId(String id);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
  
  public Integer getSize();
  
  public void setSize(Integer size);
  
  public String getSearchText();
  
  public void setSearchText(String searchText);
  
  public String getSearchColumn();
  
  public void setSearchColumn(String searchColumn);
  
  public String getSortBy();
  
  public void setSortBy(String sortBy);
  
  public String getSortOrder();
  
  public void setSortOrder(String sortOrder);
}
