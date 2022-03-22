package com.cs.core.config.interactor.model.historyversions;

import java.util.List;

public class KlassesAndTaxonomiesForVersionsInfoRequestModel
    implements IKlassesAndTaxonomiesForVersionsInfoRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    klassIds;
  protected List<String>    taxonomyIds;
  
  @Override
  public List<String> getKlassIds()
  {
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
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
    this.taxonomyIds = taxonomyIds;
  }
}
