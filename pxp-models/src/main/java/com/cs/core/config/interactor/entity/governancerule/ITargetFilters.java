package com.cs.core.config.interactor.entity.governancerule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.List;

public interface ITargetFilters extends IEntity {
  
  public static final String KLASS_IDS    = "klassIds";
  public static final String TAXONOMY_IDS = "taxonomyIds";
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
}
