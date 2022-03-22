package com.cs.core.runtime.interactor.entity.eventinstance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IEventInstanceLocation extends IEntity {
  
  public void setCountry(String country);
  
  public String getCountry();
  
  public void setState(String state);
  
  public String getState();
  
  public void setCity(String city);
  
  public String getCity();
  
  public void setStreet(String street);
  
  public String getStreet();
  
  public void setPincode(String pincode);
  
  public String getPincode();
  
  public void setMapCoordinates(IMapCoordinates coordinates);
  
  public IMapCoordinates getMapCoordinates();
}
