package com.cs.core.config.interactor.model.marketingbundle;

import java.util.List;

public class KlassesAndTaxonomiesModel implements IKlassesAndTaxonomiesModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    klassIds;
  protected List<String>    taxonomyIds;
  protected String          userId;
  
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
  
  @Override
  public String getUserId()
  {
    return userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
}
