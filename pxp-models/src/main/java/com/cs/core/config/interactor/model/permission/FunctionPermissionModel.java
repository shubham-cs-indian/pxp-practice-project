package com.cs.core.config.interactor.model.permission;

public class FunctionPermissionModel implements IFunctionPermissionModel {
  
  private static final long serialVersionUID = 1L;
  protected Boolean         canClone;
  protected Boolean         canGridEdit;
  protected Boolean         canBulkEdit;
  protected Boolean         canTransfer;
  protected Boolean         canExport;
  protected Boolean         canImport;
  protected Boolean         canShare;
  
  protected Boolean         canBulkEditProperties;
  protected Boolean         canBulkEditTaxonomies;
  protected Boolean         canBulkEditClasses;
  
  @Override
  public Boolean getCanClone()
  {
    return canClone;
  }
  
  @Override
  public void setCanClone(Boolean canClone)
  {
    this.canClone = canClone;
  }
  
  @Override
  public Boolean getCanGridEdit()
  {
    return canGridEdit;
  }
  
  @Override
  public void setCanGridEdit(Boolean canGridEdit)
  {
    this.canGridEdit = canGridEdit;
  }
  
  @Override
  public Boolean getCanBulkEdit()
  {
    return canBulkEdit;
  }
  
  @Override
  public void setCanBulkEdit(Boolean canBulkEdit)
  {
    this.canBulkEdit = canBulkEdit;
  }
  
  @Override
  public Boolean getCanTransfer()
  {
    return canTransfer;
  }
  
  @Override
  public void setCanTransfer(Boolean canTransfer)
  {
    this.canTransfer = canTransfer;
  }
  
  @Override
  public Boolean getCanExport()
  {
    return canExport;
  }
  
  @Override
  public void setCanExport(Boolean canExport)
  {
    this.canExport = canExport;
  }
  
  @Override
  public Boolean getCanShare()
  {
    return this.canShare;
  }
  
  @Override
  public void setCanShare(Boolean canShare)
  {
    this.canShare = canShare;
  }
  
  @Override
  public Boolean getCanImport()
  {
    return canImport;
  }
  
  @Override
  public void setCanImport(Boolean canImport)
  {
    this.canImport = canImport;
  }

  @Override
  public Boolean getCanBulkEditProperties()
  {
    return canBulkEditProperties;
  }

  @Override
  public void setCanBulkEditProperties(Boolean canBulkEditProperties)
  {
    this.canBulkEditProperties = canBulkEditProperties;
  }

  @Override
  public Boolean getCanBulkEditTaxonomies()
  {
    return canBulkEditTaxonomies;
  }

  @Override
  public void setCanBulkEditTaxonomies(Boolean canBulkEditTaxonomies)
  {
    this.canBulkEditTaxonomies = canBulkEditTaxonomies;
  }

  @Override
  public Boolean getCanBulkEditClasses()
  {
    return canBulkEditClasses;
  }

  @Override
  public void setCanBulkEditClasses(Boolean canBulkEditClasses)
  {
    this.canBulkEditClasses = canBulkEditClasses;
  }

  
}
