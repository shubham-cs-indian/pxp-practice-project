package com.cs.core.runtime.interactor.entity.datarule;

import java.util.List;

import com.cs.core.runtime.interactor.entity.configuration.base.IRuntimeEntity;

public interface ITaxonomyConflict extends IRuntimeEntity{
  
  String CONFLICTS                       = "conflicts";
  String IS_RESOLVED                     = "isResolved";
  String TAXONOMY_INHERITANCE_SETTING    = "taxonomyInheritanceSetting";
  public enum Setting{
    off, auto, manual
  }
  
  public List<ITaxonomyConflictingSource> getConflicts();
  public void setConflicts(List<ITaxonomyConflictingSource> taxonomyConflictingSource);
  
  public Boolean getIsResolved();
  public void setIsResolved(Boolean isResolved);
  
  public Setting getTaxonomyInheritanceSetting();
  public void setTaxonomyInheritanceSetting(Setting taxonomyInheritanceSetting);
}