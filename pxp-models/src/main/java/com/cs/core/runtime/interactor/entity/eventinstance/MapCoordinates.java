package com.cs.core.runtime.interactor.entity.eventinstance;

public class MapCoordinates implements IMapCoordinates {
  
  protected Double latitude;
  protected Double longitude;
  protected String id;
  protected Long   versionId;
  protected Long   versionTimestamp;
  protected String lastModifiedBy;
  
  @Override
  public void setLatitude(Double latitude)
  {
    this.latitude = latitude;
  }
  
  @Override
  public Double getLatitude()
  {
    return latitude;
  }
  
  @Override
  public void setLongitude(Double longitude)
  {
    this.longitude = longitude;
  }
  
  @Override
  public Double getLongitude()
  {
    return longitude;
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
