package com.cs.core.runtime.interactor.model.datarule;

import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesStrategyModel;

import java.util.List;
import java.util.Set;

public class GetTargetKlassesStrategyModel implements IGetTargetKlassesStrategyModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    allowedTypes;
  protected String          klassType;
  protected Set<String>     entities;
  protected Set<String>     taxonomyIdsHavingRP;
  
  @Override
  public String getKlassType()
  {
    return klassType;
  }
  
  @Override
  public void setKlassType(String klassType)
  {
    this.klassType = klassType;
  }
  
  @Override
  public List<String> getAllowedTypes()
  {
    return allowedTypes;
  }
  
  @Override
  public void setAllowedTypes(List<String> allowedTypes)
  {
    this.allowedTypes = allowedTypes;
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
