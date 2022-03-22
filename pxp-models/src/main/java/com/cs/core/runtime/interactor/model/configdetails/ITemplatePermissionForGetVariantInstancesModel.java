package com.cs.core.runtime.interactor.model.configdetails;

import java.util.List;
import java.util.Set;

public interface ITemplatePermissionForGetVariantInstancesModel
    extends IBaseKlassTemplatePermissionModel {
  
  public static final String CONTEXT_KLASSIDS_HAVING_RP = "contextKlassIdsHavingRP";
  public static final String ENTITIES_HAVING_RP         = "entitiesHavingRP";
  
  public List<String> getContextKlassIdsHavingRP();
  
  public void setContextKlassIdsHavingRP(List<String> contextKlassIdsHavingRP);
  
  public Set<String> getEntitiesHavingRP();
  
  public void setEntitiesHavingRP(Set<String> entitiesHavingReadPermission);
}
