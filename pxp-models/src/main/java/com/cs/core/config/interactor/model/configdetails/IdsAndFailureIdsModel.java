package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

public class IdsAndFailureIdsModel implements IIdsAndFailureIdsModel {
  
  protected List<String> ids;
  protected List<String> failureIds;
  
  @Override
  public List<String> getIds()
  {
    return ids;
  }
  
  @Override
  public void setIds(List<String> list)
  {
    this.ids = list;
  }
  
  @Override
  public List<String> getFailureIds()
  {
    return failureIds;
  }
  
  @Override
  public void setFailureIds(List<String> list)
  {
    this.failureIds = list;
  }
}
