package com.cs.core.runtime.interactor.model.taxonomy;

import java.util.List;

public class GetTaxonomyTreeLeafIdsStrategyModel implements IGetTaxonomyTreeLeafIdsStrategyModel {
  
  private static final long serialVersionUID = 1L;
  protected String          parentTaxonomyId;
  protected List<String>    leafIds;
  
  @Override
  public String getParentTaxonomyId()
  {
    return parentTaxonomyId;
  }
  
  @Override
  public void setParentTaxonomyId(String parentTaxonomyId)
  {
    this.parentTaxonomyId = parentTaxonomyId;
  }
  
  @Override
  public List<String> getLeafIds()
  {
    return leafIds;
  }
  
  @Override
  public void setLeafIds(List<String> leafIds)
  {
    this.leafIds = leafIds;
  }
}
