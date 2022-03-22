package com.cs.core.runtime.interactor.model.dashboard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cs.core.runtime.interactor.model.searchable.InstanceSearchModel;

public class DashboardInformationRequestModel extends InstanceSearchModel
    implements IDashboardInformationRequestModel {
  
  private static final long serialVersionUID    = 1L;
  protected String          currentUserId;
  protected List<String>    moduleEntities;
  protected Set<String>     taxonomyIdsHavingRP = new HashSet<>();
  protected Set<String>     klassIdsHavingRP    = new HashSet<>();
  protected List<String>    majorTaxonomyIds;
  
  @Override
  public String getCurrentUserId()
  {
    return currentUserId;
  }
  
  @Override
  public void setCurrentUserId(String currentUserId)
  {
    this.currentUserId = currentUserId;
  }
  
  @Override
  public List<String> getModuleEntities()
  {
    return moduleEntities;
  }
  
  @Override
  public void setModuleEntities(List<String> moduleEntities)
  {
    this.moduleEntities = moduleEntities;
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
  
  @Override
  public List<String> getMajorTaxonomyIds() {
    if(majorTaxonomyIds == null) {
       majorTaxonomyIds = new ArrayList<String>();
    }
    return majorTaxonomyIds;
  }

  @Override
  public void setMajorTaxonomyIds(List<String> majorTaxonomyIds) {
    this.majorTaxonomyIds = majorTaxonomyIds;
  }
}
