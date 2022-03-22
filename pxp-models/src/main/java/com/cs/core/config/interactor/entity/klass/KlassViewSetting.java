package com.cs.core.config.interactor.entity.klass;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class KlassViewSetting implements IKlassViewSetting {
  
  private static final long                serialVersionUID = 1L;
  
  protected String                         id;
  
  protected Map<String, IKlassRoleSetting> roles            = new HashMap<String, IKlassRoleSetting>();
  
  protected Long                           versionId;
  
  protected Long                           versionTimestamp;
  
  protected String                         lastModifiedBy;
  protected String                         code;
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
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
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public Map<String, IKlassRoleSetting> getRoles()
  {
    return this.roles;
  }
  
  @JsonDeserialize(contentAs = KlassRoleSetting.class)
  @Override
  public void setRoles(Map<String, IKlassRoleSetting> roles)
  {
    this.roles = roles;
  }
}
