package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.ArrayList;
import java.util.List;

public class KlassInstancesForComparisonStrategyModel extends KlassInstancesListForComparisonModel
    implements IKlassInstancesForComparisonStrategyModel {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    types            = new ArrayList<>();
  protected List<String>    taxonomyIds      = new ArrayList<>();
  
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
}
