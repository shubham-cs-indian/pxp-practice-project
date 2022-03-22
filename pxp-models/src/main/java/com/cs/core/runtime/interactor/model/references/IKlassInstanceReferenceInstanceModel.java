package com.cs.core.runtime.interactor.model.references;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IKlassInstanceReferenceInstanceModel extends IModel {
  
  public static final String ID          = "id";
  public static final String ELEMENT_IDS = "elementIds";
  public static final String TOTAL_COUNT = "totalCount";
  public static final String SIDE_ID     = "sideId";
  
  public String getId();
  
  public void setId(String id);
  
  public String getSideId();
  
  public void setSideId(String sideId);
  
  public List<String> getElementIds();
  
  public void setElementIds(List<String> elementIds);
  
  public Long getTotalCount();
  
  public void setTotalCount(Long totalCount);
}
