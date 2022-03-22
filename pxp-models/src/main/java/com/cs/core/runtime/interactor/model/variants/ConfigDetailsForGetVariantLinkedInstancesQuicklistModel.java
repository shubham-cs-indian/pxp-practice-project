package com.cs.core.runtime.interactor.model.variants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigDetailsForGetVariantLinkedInstancesQuicklistModel
    implements IConfigDetailsForGetVariantLinkedInstancesQuicklistModel {
  
  private static final long serialVersionUID    = 1L;
  
  protected List<String>    entities            = new ArrayList<>();
  protected Set<String>     allowedEntities     = new HashSet<String>();
  protected Set<String>     klassIdsHavingRP    = new HashSet<String>();
  protected Set<String>     taxonomyIdsHavingRP = new HashSet<>();
  
  @Override
  public List<String> getEntities()
  {
    return entities;
  }
  
  @Override
  public void setEntities(List<String> entities)
  {
    this.entities = entities;
  }
  
  @Override
  public Set<String> getAllowedEntities()
  {
    return allowedEntities;
  }
  
  @Override
  public void setAllowedEntities(Set<String> allowedEntities)
  {
    this.allowedEntities = allowedEntities;
  }
  
  @Override
  public Set<String> getKlassIdsHavingRP()
  {
    return klassIdsHavingRP;
  }
  
  @Override
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingRP)
  {
    this.klassIdsHavingRP = klassIdsHavingRP;
  }
  
  @Override
  public Set<String> getTaxonomyIdsHavingRP()
  {
    return taxonomyIdsHavingRP;
  }
  
  @Override
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP)
  {
    this.taxonomyIdsHavingRP = taxonomyIdsHavingRP;
  }
}
