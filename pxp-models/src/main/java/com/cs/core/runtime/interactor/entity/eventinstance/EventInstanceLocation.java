package com.cs.core.runtime.interactor.entity.eventinstance;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class EventInstanceLocation implements IEventInstanceLocation {
  
  protected String          country;
  protected String          state;
  protected String          city;
  protected String          street;
  protected String          pincode;
  protected IMapCoordinates mapCoordinates;
  protected String          id;
  protected Long            versionId;
  protected Long            versionTimestamp;
  protected String          lastModifiedBy;
  
  @Override
  public String getCountry()
  {
    return country;
  }
  
  @Override
  public void setCountry(String country)
  {
    this.country = country;
  }
  
  @Override
  public String getState()
  {
    return state;
  }
  
  @Override
  public void setState(String state)
  {
    this.state = state;
  }
  
  @Override
  public String getCity()
  {
    return city;
  }
  
  @Override
  public void setCity(String city)
  {
    this.city = city;
  }
  
  @Override
  public String getStreet()
  {
    return street;
  }
  
  @Override
  public void setStreet(String street)
  {
    this.street = street;
  }
  
  @Override
  public String getPincode()
  {
    return pincode;
  }
  
  @Override
  public void setPincode(String pincode)
  {
    this.pincode = pincode;
  }
  
  @Override
  public IMapCoordinates getMapCoordinates()
  {
    return mapCoordinates;
  }
  
  @JsonDeserialize(as = MapCoordinates.class)
  @Override
  public void setMapCoordinates(IMapCoordinates mapCoordinates)
  {
    this.mapCoordinates = mapCoordinates;
  }
  
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
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
}
