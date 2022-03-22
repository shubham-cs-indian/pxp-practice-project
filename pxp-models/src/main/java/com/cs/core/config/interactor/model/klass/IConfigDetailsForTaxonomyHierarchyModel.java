package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Set;

public interface IConfigDetailsForTaxonomyHierarchyModel extends IModel {
  
  public static final String ENTITIES               = "entities";
  public static final String KLASS_IDS_HAVING_RP    = "klassIdsHavingRP";
  public static final String TAXONOMY_IDS_HAVING_RP = "taxonomyIdsHavingRP";
  
  public Set<String> getKlassIdsHavingRP();
  
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingRP);
  
  public Set<String> getEntities();
  
  public void setEntities(Set<String> entities);
  
  public Set<String> getTaxonomyIdsHavingRP();
  
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);
}
