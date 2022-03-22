package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.offboarding.IKlassRelationshipSidesInfoModel;

import java.util.Map;

public interface IRelationshipsInfoModel extends IModel {
  
  public static final String RELATIONSHIP_INFO = "relationshipInfo";
  
  public Map<String, IKlassRelationshipSidesInfoModel> getRelationshipInfo();
  
  public void setRelationshipInfo(Map<String, IKlassRelationshipSidesInfoModel> relationshipInfo);
}
