package com.cs.core.config.interactor.model.sso;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;

public class GetGridSSOConfigurationRequestModel extends ConfigGetAllRequestModel
    implements IGetGridSSOConfigurationRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          organizationId;
  
  @Override
  public String getOrganizationId()
  {
    return organizationId;
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    this.organizationId = organizationId;
  }
  
  @Override
  public String getCode()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setCode(String code)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public IEntity getEntity()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public String getId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setId(String id)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
