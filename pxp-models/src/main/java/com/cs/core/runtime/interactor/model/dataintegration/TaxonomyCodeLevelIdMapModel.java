package com.cs.core.runtime.interactor.model.dataintegration;

import java.util.HashMap;
import java.util.Map;

public class TaxonomyCodeLevelIdMapModel implements ITaxonomyCodeLevelIdMapModel {
  
  private static final long     serialVersionUID       = 1L;
  
  protected Map<String, String> taxonomyCodeLevelIdMap = new HashMap<>();
  
  @Override
  public Map<String, String> getTaxonomyCodeLevelIdMap()
  {
    return taxonomyCodeLevelIdMap;
  }
  
  @Override
  public void setTaxonomyCodeLevelIdMap(Map<String, String> taxonomyCodeLevelIdMap)
  {
    this.taxonomyCodeLevelIdMap = taxonomyCodeLevelIdMap;
  }
}
