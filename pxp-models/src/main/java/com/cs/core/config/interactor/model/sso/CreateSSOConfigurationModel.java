package com.cs.core.config.interactor.model.sso;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CreateSSOConfigurationModel implements ICreateSSOConfigurationModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          domain;
  protected String          idp;
  protected String          type;
  protected String          orgnizationId;
  protected String          code;
  protected String          id;
    
  public String getDomain()
  {
    return domain;
  }
  
  public void setDomain(String domain)
  {
    this.domain = domain;
  }
  
  @Override
  public String getIdp() {
    return idp;
  }

  @Override
  public void setIdp(String ssoSetting) {
    this.idp = ssoSetting;
  }
  
  @Override
  public String getOrganizationId()
  {
    return orgnizationId;
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    this.orgnizationId = organizationId;
  }
  
  @Override
  public String getType()
  {
    return type;
  }

  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  public String getCode()
  {
    return code;
  }
  
  public void setCode(String code)
  {
    this.code = code;
  }
  
  public String getId()
  {
    return id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  @JsonIgnore
  public IEntity getEntity()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
