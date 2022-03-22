package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.klassinstance.ITypesTaxonomiesModel;
import java.util.List;

public interface IGetNatureRelationshipInfoForRelationshipInheritanceRequestModel
    extends ITypesTaxonomiesModel {
  
  String RELATIONSHIP_IDS = "relationshipIds";
  String SIDE_ID          = "sideId";
  
  public List<String> getRelationshipIds();
  
  public void setRelationshipIds(List<String> relationshipIds);
  
  public String getSideId();
  
  public void setSideId(String sideId);
}
