package com.cs.core.runtime.interactor.model.collections;

import java.util.ArrayList;
import java.util.List;

public class GetStaticCollectionByIdsRequestModel implements IGetStaticCollectionByIdsRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          parentId;
  protected List<String>    childrenIds      = new ArrayList<>();
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
  @Override
  public List<String> getChildrenIds()
  {
    return childrenIds;
  }
  
  @Override
  public void setChildrenIds(List<String> childrenIds)
  {
    this.childrenIds = childrenIds;
  }
}
