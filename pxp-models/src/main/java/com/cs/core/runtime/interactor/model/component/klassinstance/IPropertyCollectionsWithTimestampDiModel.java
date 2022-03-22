package com.cs.core.runtime.interactor.model.component.klassinstance;

import java.util.Date;

public interface IPropertyCollectionsWithTimestampDiModel extends IPropertyCollectionsDiModel {
  
  public static final String TIMESTAMP = "timestamp";
  
  public Date getTimestamp();
  
  public void setTimestamp(Date timestamp);
}
