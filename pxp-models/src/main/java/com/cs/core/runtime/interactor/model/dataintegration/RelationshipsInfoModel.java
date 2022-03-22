package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.offboarding.IKlassRelationshipSidesInfoModel;
import com.cs.core.runtime.interactor.model.offboarding.KlassRelationshipSidesInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class RelationshipsInfoModel implements IRelationshipsInfoModel {
  
  private static final long                               serialVersionUID = 1L;
  
  protected Map<String, IKlassRelationshipSidesInfoModel> relationshipInfo = new HashMap<>();
  
  public Map<String, IKlassRelationshipSidesInfoModel> getRelationshipInfo()
  {
    return relationshipInfo;
  }
  
  @JsonDeserialize(contentAs = KlassRelationshipSidesInfoModel.class)
  public void setRelationshipInfo(Map<String, IKlassRelationshipSidesInfoModel> relationshipInfo)
  {
    this.relationshipInfo = relationshipInfo;
  }
}
