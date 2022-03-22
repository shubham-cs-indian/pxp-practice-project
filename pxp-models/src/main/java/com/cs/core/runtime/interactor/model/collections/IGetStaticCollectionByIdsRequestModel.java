package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetStaticCollectionByIdsRequestModel extends IModel {
  
  public static final String CHILDREN_IDS = "childrenIds";
  public static final String PARENT_ID    = "parentId";
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public List<String> getChildrenIds();
  
  public void setChildrenIds(List<String> childrenIds);
}
