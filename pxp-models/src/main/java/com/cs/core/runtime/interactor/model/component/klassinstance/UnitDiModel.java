package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;
import java.util.List;

public class UnitDiModel implements IUnitDiModel {
  
  private static final long      serialVersionUID = 1L;
  
  private Date                   timestamp;
  private List<IUnitCollections> unitCollections;
  
  @Override
  public Date getTimestamp()
  {
    return this.timestamp;
  }
  
  @Override
  public void setTimestamp(Date timestamp)
  {
    this.timestamp = timestamp;
  }
  
  public List<IUnitCollections> getUnitCollections()
  {
    return this.unitCollections;
  }
  
  @JsonDeserialize(contentAs = UnitCollections.class)
  public void setUnitCollections(List<IUnitCollections> unitInfo)
  {
    this.unitCollections = unitInfo;
  }
}
