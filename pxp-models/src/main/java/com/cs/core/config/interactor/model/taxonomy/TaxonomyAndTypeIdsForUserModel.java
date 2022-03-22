package com.cs.core.config.interactor.model.taxonomy;

import java.util.List;

public class TaxonomyAndTypeIdsForUserModel implements ITaxonomyAndTypeIdsForUserModel {
  
  private static final long serialVersionUID = 1L;
  private List<String>      taxonomyIds;
  private List<String>      types;
  
  @Override
  public List<String> getTaxonomyIds()
  {
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public List<String> getTypes()
  {
    return types;
  }
  
  @Override
  public void setTypes(List<String> types)
  {
    this.types = types;
  }
}
