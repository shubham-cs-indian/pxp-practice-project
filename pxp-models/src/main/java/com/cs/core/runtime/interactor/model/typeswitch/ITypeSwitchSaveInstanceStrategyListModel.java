package com.cs.core.runtime.interactor.model.typeswitch;

import com.cs.core.runtime.interactor.model.instance.ISaveInstanceStrategyListModel;

public interface ITypeSwitchSaveInstanceStrategyListModel extends ISaveInstanceStrategyListModel {
  
  public static final String ADDED_KLASS_COUNT      = "addedKlassCount";
  public static final String ADDED_TAXONOMY_COUNT   = "addedTaxonomyCount";
  public static final String REMOVED_KLASS_COUNT    = "removedKlassCount";
  public static final String REMOVED_TAXONOMY_COUNT = "removedTaxonomyCount";
  
  public Integer getAddedKlassCount();
  
  public void setAddedKlassCount(Integer addedKlassCount);
  
  public Integer getAddedTaxonomyCount();
  
  public void setAddedTaxonomyCount(Integer addedTaxonomyCount);
  
  public Integer getRemovedKlassCount();
  
  public void setRemovedKlassCount(Integer removedKlassCount);
  
  public Integer getRemovedTaxonomyCount();
  
  public void setRemovedTaxonomyCount(Integer removedTaxonomyCount);
}
