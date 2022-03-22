package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.List;

public class TypesListInfoWithScrollIdModel extends TypesListModel
    implements ITypesListInfoWithScrollIdModel {
  
  private static final long serialVersionUID = 1L;
  private String            scrollId;
  private List<String>      physicalCatalogList;
  
  @Override
  public String getScrollId()
  {
    return scrollId;
  }
  
  @Override
  public void setScrollId(String scrollId)
  {
    this.scrollId = scrollId;
  }
  
  @Override
  public List<String> getPhysicalCatalogList()
  {
    
    if (physicalCatalogList == null) {
      physicalCatalogList = new ArrayList<>();
    }
    return physicalCatalogList;
  }
  
  @Override
  public void setPhysicalCatalogList(List<String> physicalCatalogList)
  {
    
    this.physicalCatalogList = physicalCatalogList;
  }
}
