package com.cs.core.config.interactor.model.permission;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IFunctionPermissionModel extends IModel {
  
  public static final String CAN_CLONE                = "canClone";
  public static final String CAN_GRID_EDIT            = "canGridEdit";
  public static final String CAN_BULK_EDIT            = "canBulkEdit";
  public static final String CAN_TRANSFER             = "canTransfer";
  public static final String CAN_EXPORT               = "canExport";
  public static final String CAN_SHARE                = "canShare";
  public static final String CAN_IMPORT               = "canImport";
  
  public static final String CAN_BULK_EDIT_PROPERTIES = "canBulkEditProperties";
  public static final String CAN_BULK_EDIT_TAXONOMIES = "canBulkEditTaxonomies";
  public static final String CAN_BULK_EDIT_CLASSES    = "canBulkEditClasses";
  
  public Boolean getCanClone();
  
  public void setCanClone(Boolean canClone);
  
  public Boolean getCanGridEdit();

  public void setCanGridEdit(Boolean canGridEdit);

  public Boolean getCanBulkEdit();

  public void setCanBulkEdit(Boolean canBulkEdit);

  public Boolean getCanTransfer();

  public void setCanTransfer(Boolean canTransfer);

  public Boolean getCanExport();

  public void setCanExport(Boolean canExport);

  public Boolean getCanShare();

  public void setCanShare(Boolean canShare);
  
  public Boolean getCanImport();
  
  public void setCanImport(Boolean canImport);
  
  public Boolean getCanBulkEditProperties();
  
  public void setCanBulkEditProperties(Boolean canBulkEditProperties);
  
  public Boolean getCanBulkEditTaxonomies();
  
  public void setCanBulkEditTaxonomies(Boolean canBulkEditTaxonomies);
  
  public Boolean getCanBulkEditClasses();
  
  public void setCanBulkEditClasses(Boolean canBulkEditClasses);
}
