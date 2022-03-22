package com.cs.core.config.interactor.model.physicalcatalog;

import java.util.ArrayList;
import java.util.List;

public class GetPhysicalCatalogIdsModel implements IGetPhysicalCatalogIdsModel {
  
  protected List<String> availablePhysicalCatalogIds;
  
  @Override
  public List<String> getAvailablePhysicalCatalogIds()
  {
    if (availablePhysicalCatalogIds == null) {
      availablePhysicalCatalogIds = new ArrayList<>();
    }
    return availablePhysicalCatalogIds;
  }
  
  @Override
  public void setAvailablePhysicalCatalogIds(List<String> availablePhysicalCatalogIds)
  {
    this.availablePhysicalCatalogIds = availablePhysicalCatalogIds;
  }
}
