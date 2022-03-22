package com.cs.core.runtime.interactor.model.targetinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Set;

public interface IGetTargetKlassesStrategyModel extends IModel {
  
  public static final String ALLOWED_TYPES          = "allowedTypes";
  public static final String KLASS_TYPE             = "klassType";
  public static final String ENTITIES               = "entities";
  public static final String TAXONOMY_IDS_HAVING_RP = "taxonomyIdsHavingRP";
  
  public List<String> getAllowedTypes();
  
  public void setAllowedTypes(List<String> allowedTypes);
  
  public String getKlassType();
  
  public void setKlassType(String klassType);
  
  public Set<String> getEntities();
  
  public void setEntities(Set<String> entities);
  
  public Set<String> getTaxonomyIdsHavingRP();
  
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);
}
