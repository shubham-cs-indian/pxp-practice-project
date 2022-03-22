package com.cs.core.runtime.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TemplatePermissionForGetVariantInstancesModel extends BaseKlassTemplatePermissionModel
    implements ITemplatePermissionForGetVariantInstancesModel {
  
  private static final long serialVersionUID        = 1L;
  protected List<String>    contextKlassIdsHavingRP = new ArrayList<>();
  protected Set<String>     entitiesHavingRP        = new HashSet<>();
  
  @Override
  public List<String> getContextKlassIdsHavingRP()
  {
    return contextKlassIdsHavingRP;
  }
  
  @Override
  public void setContextKlassIdsHavingRP(List<String> contextKlassIdsHavingRP)
  {
    this.contextKlassIdsHavingRP = contextKlassIdsHavingRP;
  }
  
  @Override
  public Set<String> getEntitiesHavingRP()
  {
    return entitiesHavingRP;
  }
  
  @Override
  public void setEntitiesHavingRP(Set<String> entitiesHavingRP)
  {
    this.entitiesHavingRP = entitiesHavingRP;
  }
}
