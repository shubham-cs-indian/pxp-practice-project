package com.cs.core.runtime.interacter.model.references;

import com.cs.core.runtime.interactor.model.references.IKlassInstanceReferenceInstanceModel;

import java.util.ArrayList;
import java.util.List;

public class KlassInstanceReferenceInstanceModel implements IKlassInstanceReferenceInstanceModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected List<String>    elementIds;
  protected Long            totalCount;
  protected String          sideId;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public List<String> getElementIds()
  {
    if (elementIds == null) {
      elementIds = new ArrayList<>();
    }
    return elementIds;
  }
  
  @Override
  public void setElementIds(List<String> elementIds)
  {
    this.elementIds = elementIds;
  }
  
  @Override
  public Long getTotalCount()
  {
    return totalCount;
  }
  
  @Override
  public void setTotalCount(Long totalCount)
  {
    this.totalCount = totalCount;
  }
  
  @Override
  public String getSideId()
  {
    return sideId;
  }
  
  @Override
  public void setSideId(String sideId)
  {
    this.sideId = sideId;
  }
}
