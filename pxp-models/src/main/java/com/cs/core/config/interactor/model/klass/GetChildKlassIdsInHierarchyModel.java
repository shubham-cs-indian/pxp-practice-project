package com.cs.core.config.interactor.model.klass;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetChildKlassIdsInHierarchyModel implements IGetChildKlassIdsInHierarchyModel {
  
  private static final long           serialVersionUID      = 1L;
  protected Map<String, List<Long>> typeIdVsChildKlassIds = new HashMap<>();
  protected Set<String>     klassTypes    = new HashSet<>();
  
  @Override
  public Map<String, List<Long>> getTypeIdVsChildKlassIds()
  {
    if (typeIdVsChildKlassIds == null) {
      typeIdVsChildKlassIds = new HashMap<>();
    }
    return typeIdVsChildKlassIds;
  }
  
  @Override
  public void setTypeIdVsChildKlassIds(Map<String, List<Long>> typeIdVsChildKlassIds)
  {
    this.typeIdVsChildKlassIds = typeIdVsChildKlassIds;
  }

  @Override
  public Set<String> getKlassTypes()
  {
    return klassTypes;
  }

  @Override
  public void setKlassTypes(Set<String> klassTypes)
  {
    this.klassTypes = klassTypes;
  }
  
 
}
