package com.cs.core.config.interactor.model.matchandmerge;

import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;

import java.util.List;

public interface IGetConfigDetailsForMatchAndMergeModel extends IGetConfigDetailsForCustomTabModel {
  
  String TARGET_TYPES           = "targetTypes";
  String MATCH_RELATIONSHIP_IDS = "matchRelationshipIds";
  
  public List<String> getTargetTypes();
  
  public void setTargetTypes(List<String> targetTypes);
  
  public List<String> getMatchRelationshipIds();
  
  public void setMatchRelationshipIds(List<String> matchRelationshipIds);
}
