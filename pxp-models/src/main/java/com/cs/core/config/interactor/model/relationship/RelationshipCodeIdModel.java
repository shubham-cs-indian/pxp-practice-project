package com.cs.core.config.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.relationship.IRelationshipCodeIdModel;

import java.util.Map;

public class RelationshipCodeIdModel implements IRelationshipCodeIdModel {
  
  private static final long     serialVersionUID = 1L;
  protected Map<String, Object> CodeIdMap;
  
  @Override
  public Map<String, Object> getCodeIdMap()
  {
    return CodeIdMap;
  }
  
  @Override
  public void setCodeIdMap(Map<String, Object> codeIdMap)
  {
    CodeIdMap = codeIdMap;
  }
}
