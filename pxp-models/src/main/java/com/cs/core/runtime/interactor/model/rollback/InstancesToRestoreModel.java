package com.cs.core.runtime.interactor.model.rollback;

import com.cs.core.runtime.interactor.model.versionrollback.IInstancesToRestoreModel;

import java.util.ArrayList;
import java.util.List;

public class InstancesToRestoreModel implements IInstancesToRestoreModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          baseType;
  protected List<String>    klassIds         = new ArrayList<>();
  protected List<String>    taxonomyIds      = new ArrayList<>();
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
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
