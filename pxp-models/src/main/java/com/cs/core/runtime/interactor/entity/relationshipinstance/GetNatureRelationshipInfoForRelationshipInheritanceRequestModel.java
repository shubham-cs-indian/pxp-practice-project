package com.cs.core.runtime.interactor.entity.relationshipinstance;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.model.klassinstance.TypesTaxonomiesModel;

public class GetNatureRelationshipInfoForRelationshipInheritanceRequestModel extends TypesTaxonomiesModel
    implements IGetNatureRelationshipInfoForRelationshipInheritanceRequestModel {
  
  public GetNatureRelationshipInfoForRelationshipInheritanceRequestModel()
  {
    // TODO Auto-generated constructor stub
  }
  
  public GetNatureRelationshipInfoForRelationshipInheritanceRequestModel(List<String> relationshipIds, List<String> types,
      List<String> taxonomyIds, List<String> referenceIds)
  {
    this.relationshipIds = relationshipIds;
    this.taxonomyIds = taxonomyIds;
    this.types = types;
    this.referenceIds = referenceIds;
  }
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    relationshipIds;
  protected String          sideId;
  protected List<String>    referenceIds;
  
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
  public String getSideId()
  {
    return sideId;
  }
  
  @Override
  public void setSideId(String sideId)
  {
    this.sideId = sideId;
  }
  
}
