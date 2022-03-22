package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IIdLabelCodeDownloadPermissionModel extends IIdLabelCodeModel {
  
  public static final String CAN_DOWNLOAD = "canDownload";
  
  public Boolean getCanDownload();
  
  public void setCanDownload(Boolean canDownload);
}
