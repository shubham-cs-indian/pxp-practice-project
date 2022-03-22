package com.cs.core.config.interactor.model.klass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstanceCountRequestModel implements IInstanceCountRequestModel {
  
  private static final long           serialVersionUID = 1L;
  protected Map<String, List<String>> typeIdVsChildKlassIds;
  protected String                    klassType;
  protected Map<String, List<String>> taxonomyIdVsChildTaxonomyIds;
  
  @Override
  public Map<String, List<String>> getTypeIdVsChildKlassIds()
  {
    if (typeIdVsChildKlassIds == null) {
      typeIdVsChildKlassIds = new HashMap<>();
    }
    return typeIdVsChildKlassIds;
  }
  
  @Override
  public void setTypeIdVsChildKlassIds(Map<String, List<String>> typeIdVsChildKlassIds)
  {
    this.typeIdVsChildKlassIds = typeIdVsChildKlassIds;
  }
  
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
  public Map<String, List<String>> getTaxonomyIdVsChildTaxonomyIds()
  {
    if (taxonomyIdVsChildTaxonomyIds == null) {
      taxonomyIdVsChildTaxonomyIds = new HashMap<>();
    }
    return taxonomyIdVsChildTaxonomyIds;
  }
  
  @Override
  public void setTaxonomyIdVsChildTaxonomyIds(
      Map<String, List<String>> taxonomyIdVsChildTaxonomyIds)
  {
    this.taxonomyIdVsChildTaxonomyIds = taxonomyIdVsChildTaxonomyIds;
  }
}
