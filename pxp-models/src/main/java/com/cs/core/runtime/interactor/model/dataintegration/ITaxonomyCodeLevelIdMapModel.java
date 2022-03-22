package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface ITaxonomyCodeLevelIdMapModel extends IModel {
  
  public static final String TAXONOMY_CODE_LEVEL_ID_MAP = "taxonomyCodeLevelIdMap";
  
  public Map<String, String> getTaxonomyCodeLevelIdMap();
  
  public void setTaxonomyCodeLevelIdMap(Map<String, String> taxonomyCodeLevelIdMap);
}
