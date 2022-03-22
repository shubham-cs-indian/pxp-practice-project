package com.cs.core.config.interactor.model.permission;

public class PermissionModel implements IPermissionModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Boolean         canRead          = false;
  protected Boolean         canEdit          = false;
  protected Boolean         canDelete        = false;
  protected Boolean         canCreate        = false;
  protected Boolean         canDownload      = false;
  
  @Override
  public Boolean getCanRead()
  {
    return canRead;
  }
  
  @Override
  public void setCanRead(Boolean canRead)
  {
    this.canRead = canRead;
  }
  
  @Override
  public Boolean getCanEdit()
  {
    return canEdit;
  }
  
  @Override
  public void setCanEdit(Boolean canEdit)
  {
    this.canEdit = canEdit;
  }
  
  @Override
  public Boolean getCanDelete()
  {
    return canDelete;
  }
  
  @Override
  public void setCanDelete(Boolean canDelete)
  {
    this.canDelete = canDelete;
  }
  
  @Override
  public Boolean getCanCreate()
  {
    return canCreate;
  }
  
  @Override
  public void setCanCreate(Boolean canCreate)
  {
    this.canCreate = canCreate;
  }
  
  @Override
  public Boolean getCanDownload()
  {
    return canDownload;
  }
  
  @Override
  public void setCanDownload(Boolean canDownload)
  {
    this.canDownload = canDownload;
  }
}
