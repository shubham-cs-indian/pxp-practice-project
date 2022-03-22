package com.cs.core.config.interactor.model.taxonomy;

import java.util.List;
import java.util.Map;

public class ParentChildTaxonomyIdModel implements IParentChildTaxonomyIdModel {
  
  private static final long           serialVersionUID = 1L;
  protected Map<String, List<String>> taxonomyMap;
  
  @Override
  public Map<String, List<String>> getTaxonomyMap()
  {
    return taxonomyMap;
  }
  
  @Override
  public void setTaxonomyMap(Map<String, List<String>> taxonomyMap)
  {
    this.taxonomyMap = taxonomyMap;
  }
}
