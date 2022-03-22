package com.cs.core.runtime.interactor.entity.eventinstance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IMapCoordinates extends IEntity {
  
  public void setLatitude(Double latitude);
  
  public Double getLatitude();
  
  public void setLongitude(Double longitude);
  
  public Double getLongitude();
}
