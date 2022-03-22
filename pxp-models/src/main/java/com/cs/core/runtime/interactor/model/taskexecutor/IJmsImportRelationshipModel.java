package com.cs.core.runtime.interactor.model.taskexecutor;

import com.cs.core.runtime.interactor.model.offboarding.IKlassRelationshipSidesInfoModel;

import java.util.Map;

public interface IJmsImportRelationshipModel extends IJmsImportTaskModel {
  
  public static final String RELATIONSHIPID_TO_RELATIONSHIP_INFO_MAP = "relationshipIdToRelationshipInfoMap";
  public static final String SOURCE_INFO_MAP                         = "sourceInfoMap";
  
  public Map<String, IKlassRelationshipSidesInfoModel> getRelationshipIdToRelationshipInfoMap();
  
  public void setRelationshipIdToRelationshipInfoMap(
      Map<String, IKlassRelationshipSidesInfoModel> relationshipIdToRelationshipInfoMap);
  
  public Map<String, Object> getSourceInfoMap();
  
  public void setSourceInfoMap(Map<String, Object> sourceInfoMap);
}
