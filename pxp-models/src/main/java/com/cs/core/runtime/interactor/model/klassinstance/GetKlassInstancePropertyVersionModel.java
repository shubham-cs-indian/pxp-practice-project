package com.cs.core.runtime.interactor.model.klassinstance;

public class GetKlassInstancePropertyVersionModel implements IGetKlassInstancePropertyVersionModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          propertyId;
  
  protected Long            versionId;
  
  @Override
  public String getPropertyId()
  {
    return propertyId;
  }
  
  @Override
  public void setPropertyId(String propertyId)
  {
    this.propertyId = propertyId;
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
}
