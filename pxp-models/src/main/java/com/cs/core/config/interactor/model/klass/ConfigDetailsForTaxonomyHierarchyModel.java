package com.cs.core.config.interactor.model.klass;

import java.util.Set;

public class ConfigDetailsForTaxonomyHierarchyModel
    implements IConfigDetailsForTaxonomyHierarchyModel {
  
  private static final long serialVersionUID = 1L;
  protected Set<String>     klassIdsHavingRP;
  protected Set<String>     entities;
  protected Set<String>     taxonomyIdsHavingRP;
  
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
  public Set<String> getEntities()
  {
    return entities;
  }
  
  @Override
  public void setEntities(Set<String> entities)
  {
    this.entities = entities;
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
