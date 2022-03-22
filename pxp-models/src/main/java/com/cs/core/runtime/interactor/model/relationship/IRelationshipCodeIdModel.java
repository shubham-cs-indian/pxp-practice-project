package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IRelationshipCodeIdModel extends IModel {
  
  public static final String CODE_ID_MAP = "codeIdMap";
  
  Map<String, Object> getCodeIdMap();
  
  void setCodeIdMap(Map<String, Object> codeIdMap);
}
