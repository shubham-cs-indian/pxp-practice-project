package com.cs.core.runtime.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.List;

public class GetConfigDetailsForSaveRelationshipInstancesRequestModel
    implements IGetConfigDetailsForSaveRelationshipInstancesRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    klassIds;
  protected List<String>    taxonomyIds;
  protected List<String>    relationshipIds;
  protected List<String>    natureRelationshipIds;
  
  @Override
  public List<String> getRelationshipIds()
  {
    if (relationshipIds == null) {
      relationshipIds = new ArrayList<>();
    }
    return relationshipIds;
  }
  
  @Override
  public void setRelationshipIds(List<String> relationshipIds)
  {
    this.relationshipIds = relationshipIds;
  }
  
  @Override
  public List<String> getNatureRelationshipIds()
  {
    if (natureRelationshipIds == null) {
      natureRelationshipIds = new ArrayList<>();
    }
    return natureRelationshipIds;
  }
  
  @Override
  public void setNatureRelationshipIds(List<String> natureRelationshipIds)
  {
    this.natureRelationshipIds = natureRelationshipIds;
  }
  
  @Override
  public List<String> getKlassIds()
  {
    if (klassIds == null) {
      klassIds = new ArrayList<>();
    }
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
    if (taxonomyIds == null) {
      taxonomyIds = new ArrayList<>();
    }
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
}
