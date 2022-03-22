package com.cs.core.runtime.interactor.model.configuration;

import java.util.ArrayList;
import java.util.List;

public class MigrationResponseModel implements IMigrationResponseModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    ids;
  protected List<String>    technicalVariantKlassIds;
  
  @Override
  public List<String> getIds()
  {
    if (ids == null) {
      ids = new ArrayList<String>();
    }
    return ids;
  }
  
  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
  
  @Override
  public List<String> getTechnicalVariantKlassIds()
  {
    return technicalVariantKlassIds;
  }
  
  @Override
  public void setTechnicalVariantKlassIds(List<String> technicalVariantKlassIds)
  {
    this.technicalVariantKlassIds = technicalVariantKlassIds;
  }
}
