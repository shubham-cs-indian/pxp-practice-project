package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Set;

public interface IConfigDetailsForGetVariantLinkedInstancesQuicklistModel extends IModel {
  
  public static final String ALLOWED_ENTITIES       = "allowedEntities";
  public static final String ENTITIES               = "entities";
  public static final String KLASS_IDS_HAVING_RP    = "klassIdsHavingRP";
  public static final String TAXONOMY_IDS_HAVING_RP = "taxonomyIdsHavingRP";
  
  public List<String> getEntities();
  
  public void setEntities(List<String> entities);
  
  public Set<String> getAllowedEntities();
  
  public void setAllowedEntities(Set<String> allowedEntities);
  
  public Set<String> getKlassIdsHavingRP();
  
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingReadPermission);
  
  public Set<String> getTaxonomyIdsHavingRP();
  
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);
}
