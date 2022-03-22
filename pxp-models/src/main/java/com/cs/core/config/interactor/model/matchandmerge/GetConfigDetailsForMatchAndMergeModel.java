package com.cs.core.config.interactor.model.matchandmerge;

import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForCustomTabModel;

import java.util.ArrayList;
import java.util.List;

public class GetConfigDetailsForMatchAndMergeModel extends GetConfigDetailsForCustomTabModel
    implements IGetConfigDetailsForMatchAndMergeModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    targetTypes;
  protected List<String>    matchRelationshipIds;
  
  @Override
  public List<String> getTargetTypes()
  {
    if (targetTypes == null) {
      targetTypes = new ArrayList<>();
    }
    return targetTypes;
  }
  
  @Override
  public void setTargetTypes(List<String> targetTypes)
  {
    this.targetTypes = targetTypes;
  }
  
  @Override
  public List<String> getMatchRelationshipIds()
  {
    if (matchRelationshipIds == null) {
      matchRelationshipIds = new ArrayList<>();
    }
    return matchRelationshipIds;
  }
  
  @Override
  public void setMatchRelationshipIds(List<String> matchRelationshipIds)
  {
    this.matchRelationshipIds = matchRelationshipIds;
  }
}
