package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.HashSet;
import java.util.Set;

public class ConfigDetailsForMCalKlassTaxonomyTreeResponseModel extends ConfigDetailsKlassTaxonomyTreeResponseModel 
      implements IConfigDetailsForMCalKlassTaxonomyTreeResponseModel {
  
  private static final long serialVersionUID = 1L;
  protected Set<String>     eventIdsHavingRP;
  
  @Override
  public Set<String> getEventIdsHavingRP()
  {
    if (eventIdsHavingRP == null) {
      eventIdsHavingRP = new HashSet<>();
    }
    return eventIdsHavingRP;
  }
  
  @Override
  public void setEventIdsHavingRP(Set<String> eventIdsHavingRP)
  {
    this.eventIdsHavingRP = eventIdsHavingRP;
  }
}
