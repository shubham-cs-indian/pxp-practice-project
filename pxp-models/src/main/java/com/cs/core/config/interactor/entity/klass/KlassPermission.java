package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.role.IRolePermission;
import com.cs.core.config.interactor.entity.role.RolePermission;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class KlassPermission implements IKlassPermission {
  
  private static final long              serialVersionUID = 1L;
  
  protected String                       id;
  
  protected Map<String, IRolePermission> rolePermission   = new HashMap<>();
  
  protected Long                         versionId;
  
  protected Long                         versionTimestamp;
  
  protected String                       lastModifiedBy;
  
  protected String                       code;
  
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
  public Map<String, IRolePermission> getRolePermission()
  {
    return rolePermission;
  }
  
  @JsonDeserialize(contentAs = RolePermission.class)
  @Override
  public void setRolePermission(Map<String, IRolePermission> rolePermission)
  {
    this.rolePermission = rolePermission;
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
