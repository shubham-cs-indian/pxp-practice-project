package com.cs.core.base.interactor.model;

import java.util.List;

public class ModuleEntitiesWithUserIdModel implements IModuleEntitiesWithUserIdModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          userId;
  protected String          klassId;
  protected String          clickedTaxonomyId;
  protected List<String>    moduleEntities;
  
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
  public String getUserId()
  {
    return userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  @Override
  public String getKlassId()
  {
    return klassId;
  }

  @Override
  public void setKlassId(String klassId)
  {
    this.klassId = klassId;
  }

  @Override
  public List<String> getAllowedEntities()
  {
    return moduleEntities;
  }

  @Override
  public void setAllowedEntities(List<String> moduleEntities)
  {
    this.moduleEntities = moduleEntities;
  }

  @Override
  public String getClickedTaxonomyId()
  {
    return clickedTaxonomyId;
  }

  @Override
  public void setClickedTaxonomyId(String clickedTaxonomyId)
  {
    this.clickedTaxonomyId = clickedTaxonomyId;
  }
  
}
