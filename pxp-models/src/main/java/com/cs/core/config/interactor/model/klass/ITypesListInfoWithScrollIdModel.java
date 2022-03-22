package com.cs.core.config.interactor.model.klass;

import java.util.List;

public interface ITypesListInfoWithScrollIdModel extends ITypesListModel {
  
  public static final String SCROLL_ID             = "scrollId";
  public static final String PHYSICAL_CATALOG_LIST = "physicalCatalogList";
  
  public String getScrollId();
  
  public void setScrollId(String scrollId);
  
  public List<String> getPhysicalCatalogList();
  
  public void setPhysicalCatalogList(List<String> physicalCatalogList);
}
