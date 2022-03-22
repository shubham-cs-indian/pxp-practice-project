package com.cs.core.config.interactor.model.physicalcatalog;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetPhysicalCatalogIdsModel extends IModel {
  
  public static final String AVAILABLE_PHYSICAL_CATALOG_IDS = "availablePhysicalCatalogIds";
  
  public List<String> getAvailablePhysicalCatalogIds();
  
  public void setAvailablePhysicalCatalogIds(List<String> allAvailablePhysicalCatalogIds);
}
