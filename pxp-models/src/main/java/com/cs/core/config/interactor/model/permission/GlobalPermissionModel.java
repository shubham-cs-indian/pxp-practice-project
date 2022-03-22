package com.cs.core.config.interactor.model.permission;

import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.model.globalpermissions.IGlobalPermissionModel;

public class GlobalPermissionModel implements IGlobalPermissionModel {
  
  private static final long serialVersionUID = 1L;
  
  IGlobalPermission         entity;
  
  public GlobalPermissionModel()
  {
    entity = new GlobalPermission();
  }
  
  public GlobalPermissionModel(IGlobalPermission globalPermission)
  {
    this.entity = globalPermission;
  }
  
  @Override
  public String getCode()
  {
    return entity.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    entity.setCode(code);
  }
  
  @Override
  public Boolean getCanRead()
  {
    return entity.getCanRead();
  }
  
  @Override
  public void setCanRead(Boolean canRead)
  {
    entity.setCanRead(canRead);
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public Boolean getCanEdit()
  {
    return entity.getCanEdit();
  }
  
  @Override
  public void setCanEdit(Boolean canEdit)
  {
    entity.setCanEdit(canEdit);
  }
  
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    entity.setVersionId(versionId);
  }
  
  @Override
  public Boolean getCanDelete()
  {
    return entity.getCanDelete();
  }
  
  @Override
  public void setCanDelete(Boolean canDelete)
  {
    entity.setCanDelete(canDelete);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public Boolean getCanCreate()
  {
    return entity.getCanCreate();
  }
  
  @Override
  public void setCanCreate(Boolean canCreate)
  {
    entity.setCanCreate(canCreate);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public String getType()
  {
    return entity.getType();
  }
  
  @Override
  public void setType(String type)
  {
    entity.setType(type);
  }
  
  @Override
  public String getEntityId()
  {
    return entity.getEntityId();
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    entity.setEntityId(entityId);
  }
  
  @Override
  public String getRoleId()
  {
    return entity.getRoleId();
  }
  
  @Override
  public void setRoleId(String roleId)
  {
    entity.setRoleId(roleId);
  }
  
  @Override
  public Boolean getCanDownload()
  {
    return entity.getCanDownload();
  }
  
  @Override
  public void setCanDownload(Boolean canDownload)
  {
    entity.setCanDownload(canDownload);
  }
}
