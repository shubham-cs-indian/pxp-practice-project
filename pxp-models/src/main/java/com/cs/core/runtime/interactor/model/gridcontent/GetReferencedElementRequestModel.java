package com.cs.core.runtime.interactor.model.gridcontent;

import java.util.ArrayList;
import java.util.List;

public class GetReferencedElementRequestModel implements IGetReferencedElementRequestModel {
  
  private static final long serialVersionUID = 1L;
  public List<String>       taxonomyIds;
  protected List<String>    klassIds;
  
  @Override
  public List<String> getKlassIds()
  {
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    if (klassIds == null) {
      klassIds = new ArrayList<>();
    }
    this.klassIds = klassIds;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    if (taxonomyIds == null) {
      taxonomyIds = new ArrayList<>();
    }
    this.taxonomyIds = taxonomyIds;
  }
}
