package com.cs.core.runtime.interactor.model.typeswitch;

import com.cs.core.runtime.interactor.model.instance.SaveInstanceStrategyListModel;

public class TypeSwitchSaveInstanceStrategyListModel extends SaveInstanceStrategyListModel
    implements ITypeSwitchSaveInstanceStrategyListModel {
  
  private static final long serialVersionUID = 1L;
  protected Integer         addedKlassCount;
  protected Integer         addedTaxonomyCount;
  protected Integer         removedKlassCount;
  protected Integer         removedTaxonomyCount;
  
  public Integer getAddedKlassCount()
  {
    return addedKlassCount;
  }
  
  public void setAddedKlassCount(Integer addedKlassCount)
  {
    this.addedKlassCount = addedKlassCount;
  }
  
  public Integer getAddedTaxonomyCount()
  {
    return addedTaxonomyCount;
  }
  
  public void setAddedTaxonomyCount(Integer addedTaxonomyCount)
  {
    this.addedTaxonomyCount = addedTaxonomyCount;
  }
  
  public Integer getRemovedKlassCount()
  {
    return removedKlassCount;
  }
  
  public void setRemovedKlassCount(Integer removedKlassCount)
  {
    this.removedKlassCount = removedKlassCount;
  }
  
  public Integer getRemovedTaxonomyCount()
  {
    return removedTaxonomyCount;
  }
  
  public void setRemovedTaxonomyCount(Integer removedTaxonomyCount)
  {
    this.removedTaxonomyCount = removedTaxonomyCount;
  }
}
