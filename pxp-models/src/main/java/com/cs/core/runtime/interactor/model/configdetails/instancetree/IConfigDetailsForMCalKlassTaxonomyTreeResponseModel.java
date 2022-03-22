package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.Set;

public interface IConfigDetailsForMCalKlassTaxonomyTreeResponseModel extends IConfigDetailsKlassTaxonomyTreeResponseModel {
  
  public static final String EVENT_IDS_HAVING_RP    = "eventIdsHavingRP";
  
  public Set<String> getEventIdsHavingRP();
  public void setEventIdsHavingRP(Set<String> eventIdsHavingRP);
  
}
