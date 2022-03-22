package com.cs.core.runtime.interactor.model.component.klassinstance;

import java.util.Date;

public class PropertyCollectionsWithTimestampDiModel extends PropertyCollectionsDiModel
    implements IPropertyCollectionsWithTimestampDiModel {
  
  private static final long serialVersionUID = 1L;
  
  private Date              timestamp;
  
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
}
