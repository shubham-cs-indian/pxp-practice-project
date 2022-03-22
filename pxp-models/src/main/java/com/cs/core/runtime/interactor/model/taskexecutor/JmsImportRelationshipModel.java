package com.cs.core.runtime.interactor.model.taskexecutor;

import com.cs.core.runtime.interactor.model.offboarding.IKlassRelationshipSidesInfoModel;

import java.util.Map;

public class JmsImportRelationshipModel extends JmsImportTaskModel
    implements IJmsImportRelationshipModel {
  
  private static final long                               serialVersionUID = 1L;
  protected Map<String, IKlassRelationshipSidesInfoModel> relationshipIdToRelationshipInfoMap;
  protected Map<String, Object>                           sourceInfoMap;
  
  @Override
  public Map<String, IKlassRelationshipSidesInfoModel> getRelationshipIdToRelationshipInfoMap()
  {
    return relationshipIdToRelationshipInfoMap;
  }
  
  @Override
  public void setRelationshipIdToRelationshipInfoMap(
      Map<String, IKlassRelationshipSidesInfoModel> relationshipIdToRelationshipInfoMap)
  {
    this.relationshipIdToRelationshipInfoMap = relationshipIdToRelationshipInfoMap;
  }
  
  @Override
  public Map<String, Object> getSourceInfoMap()
  {
    return sourceInfoMap;
  }
  
  @Override
  public void setSourceInfoMap(Map<String, Object> sourceInfoMap)
  {
    this.sourceInfoMap = sourceInfoMap;
  }
}
